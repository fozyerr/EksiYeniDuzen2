package com.example.fatih.eksiyeniduzen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Fatih on 08.11.2016.
 */
 class EntryAdapter extends ArrayAdapter<EntryProvider> {


    private Context ctx;

    private BottomSheetDialog dialog2;
    private ListView listView;

    String cookie;
    String nick;
    private  List<EntryProvider> entryProviders=new ArrayList<>();


     EntryAdapter(Context context, int resource,String cookie,String nick) {
        super(context, resource);
        ctx=context;
         this.cookie=cookie;
         this.nick=nick;

    }


    @Override
    public void add(EntryProvider object) {

        entryProviders.add(object);

        super.add(object);
    }

     void sonuncuyusil()
    {
        entryProviders.remove(entryProviders.size()-1);
        notifyDataSetChanged();
    }

    void yukleniyorekle()
    {

        entryProviders.add(new EntryProvider(1));
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return entryProviders.size();
    }

    @Override
    public EntryProvider getItem(int position) {
        return entryProviders.get(position);
    }


    private static class EntryViewHolder
    {
        TextView entry;
        TextView yazar;
        TextView zaman;
        TextView fav;
        ImageView favresim;
        ImageButton button;
    //    ImageButton artioy,eksioy;
        TextView ozelentry;
        TextView yorum;
    }

    private static class ProgressViewHolder
    {
        ProgressBar progressBar;
        ImageView imageView;
    }

    private static class CopEntryholder
    {
        TextView ozelentry;
        TextView duzelt;
        TextView canlandir;
        TextView sil;
        TextView copentry;
        TextView zaman;
        TextView zaman2;

    }
    private static class SayfaNumarasiHolder
    {
        TextView sayfanotext;
    }


    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {


        final EntryProvider provider=getItem(position);

        EntryViewHolder viewHolder=null;
        ProgressViewHolder progressViewHolder;
        CopEntryholder copEntryholder=null;
        SayfaNumarasiHolder sayfaNumarasiHolder=null;


        /*


        VIEWLERIN TANIMLANDIĞI AN, FINDVIEWBYID BURADA KULLANILMALI


         */
        assert provider != null;
        if (provider.tip==0) {

            if(convertView==null || !(convertView.getTag() instanceof  EntryViewHolder))
            {
                LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                viewHolder = new EntryViewHolder();
             //   convertView = inflator.inflate(R.layout.entrylayout, parent, false);
                convertView = inflator.inflate(R.layout.entrylayout_constraint, parent, false);

                viewHolder.entry = convertView.findViewById(R.id.entry);
                viewHolder.yazar = convertView.findViewById(R.id.yazaradi);
                viewHolder.zaman =  convertView.findViewById(R.id.entryzaman);
                viewHolder.fav = convertView.findViewById(R.id.favoritext);
                viewHolder.favresim =  convertView.findViewById(R.id.favresim);
                viewHolder.button =  convertView.findViewById(R.id.entrybuton);
                viewHolder.ozelentry =  convertView.findViewById(R.id.ozelentry);
                viewHolder.yorum=  convertView.findViewById(R.id.yorumtext);
                //   viewHolder.artioy= (ImageButton) convertView.findViewById(R.id.artioy);
                //   viewHolder.eksioy= (ImageButton) convertView.findViewById(R.id.eksioy);

                convertView.setTag(viewHolder);
              //  Log.d("ilk tanıtma","ilk kod feyşın");

            }
            else
            {
             //   Log.d("viewholhder","mekanik kod feyşın");
                viewHolder= (EntryViewHolder) convertView.getTag();
            }



        }
        else if(provider.tip==1)
        {

            if(convertView==null || !(convertView.getTag() instanceof  ProgressViewHolder))
            {
                LayoutInflater inflator= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                progressViewHolder=new ProgressViewHolder();
                convertView=inflator.inflate(R.layout.progressbar_sayfa_devam,parent,false);

                progressViewHolder.progressBar=  convertView.findViewById(R.id.devamet);
                progressViewHolder.imageView=  convertView.findViewById(R.id.devambuton);

                convertView.setTag(progressViewHolder);
               // Log.d("progressnull","progress kod feyşın");

            }
            else
            {
              //  Log.d("progressholder","progress viewholder kod feyşın");
                progressViewHolder= (ProgressViewHolder) convertView.getTag();
            }





        }
        else if(provider.tip==2)
        {

            if(convertView==null || !(convertView.getTag() instanceof  CopEntryholder))
            {
                LayoutInflater inflator= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                copEntryholder=new CopEntryholder();
                convertView=inflator.inflate(R.layout.entrycoplayout,parent,false);

                copEntryholder.copentry= convertView.findViewById(R.id.entry);
                copEntryholder.ozelentry= convertView.findViewById(R.id.ozelentry);
                copEntryholder.duzelt=  convertView.findViewById(R.id.duzelttext);
                copEntryholder.canlandir=convertView.findViewById(R.id.canlandirtext);
                copEntryholder.sil= convertView.findViewById(R.id.siltext);
                copEntryholder.zaman=  convertView.findViewById(R.id.entryzaman);
                copEntryholder.zaman2=  convertView.findViewById(R.id.silinmezamani);

                convertView.setTag(copEntryholder);
            }
            else
            {
                copEntryholder= (CopEntryholder) convertView.getTag();
            }

        }
        else if(provider.tip==3)
        {

            if(convertView==null || !(convertView.getTag() instanceof  SayfaNumarasiHolder))
            {
                LayoutInflater inflator= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                sayfaNumarasiHolder=new SayfaNumarasiHolder();
                convertView=inflator.inflate(R.layout.baslik_sayfa_ayrac,parent,false);

                sayfaNumarasiHolder.sayfanotext= convertView.findViewById(R.id.sayfanumarasi);

                convertView.setTag(sayfaNumarasiHolder);
            }
            else
            {
                sayfaNumarasiHolder= (SayfaNumarasiHolder) convertView.getTag();

            }

        }







        /*


        VIEWLERE ITEMLERIN ATANDIĞI YER


         */

        if(provider.tip==0)
        {


         /*   int uzunluktoplam=provider.entry.length()+provider.zaman.length()+provider.favlar.length()+provider.yazar.length()
                    + provider.ozelentrytext.length()+provider.yazarid.length()+provider.yorumsayisi.length();

            Log.d("pozisyon "+position,String.valueOf(uzunluktoplam));*/

            SpannableString ent = provider.entry;
            String date = provider.zaman;
            String favoriler = provider.favlar;
             boolean favkont = provider.favmi;

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (nick.equals(provider.yazar)) {
                        kendientrydialogu(getItem(position));
                    } else {
                        dialoggoster(getItem(position));
                    }


                }
            });

            if(!provider.ozelentryUrl.isEmpty())
            viewHolder.ozelentry.setText(provider.ozelentrytext);




            viewHolder.entry.setText(ent, TextView.BufferType.SPANNABLE);
            viewHolder.entry.setLinksClickable(true);
            //Linkify.addLinks(entry, Linkify.WEB_URLS);
            viewHolder.entry.setMovementMethod(LinkMovementMethod.getInstance());


            if(provider.yorumvarmi)
            {
                viewHolder.yorum.setText(provider.yorumsayisi);
                viewHolder.yorum.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.yorum.setVisibility(View.GONE);
            }



            if (favoriler.equals("0")) {
                viewHolder.fav.setText(" ");
                viewHolder.favresim.setVisibility(View.GONE);

            } else {
                viewHolder.fav.setText(favoriler);
                viewHolder.favresim.setVisibility(View.VISIBLE);
            }
            //  fav.setText(favoriler);



            if (favkont) {
                viewHolder.favresim.setImageResource(R.drawable.favli);
            } else {
                viewHolder.favresim.setImageResource(R.drawable.favv);
            }

          /*  if(provider.artimi)
            {
                viewHolder.artioy.setImageResource(R.drawable.artioysecili);
            }
            else
            {
                viewHolder.artioy.setImageResource(R.drawable.artioy);
            }

            if(provider.eksimi)
            {
                viewHolder.eksioy.setImageResource(R.drawable.eksioysecili);
            }
            else
            {
                viewHolder.eksioy.setImageResource(R.drawable.eksioy);
            }*/

            viewHolder.yazar.setText(provider.yazar);
            viewHolder.zaman.setText(date);

            if(!cookie.equals("BOS"))
            {
                favbutonunatikla(viewHolder.favresim,provider.entryid);
                favtextinetiklama(viewHolder.fav,provider.entryid);
            }


           // artioyeksioy(viewHolder.artioy,viewHolder.eksioy,provider);
            yorumclicklistener(viewHolder.yorum,provider.entryid, provider.yorumsayisi);
            ozelentryclick(viewHolder.ozelentry, provider.ozelentryUrl,provider.ozelentrytext);
            yazarclicklistener(viewHolder.yazar, provider.yazarid);
        }
        else if(provider.tip==1)
        {
          /*  ProgressViewHolder progressViewHolder= (ProgressViewHolder) convertView.getTag();

            progressViewHolder.imageView.setVisibility(View.GONE);
            progressViewHolder.progressBar.setVisibility(View.VISIBLE);

            Log.d("adaptör içi","adaptörrr");*/

            Log.d("ENTRYADAPTER","PROGRESS AMA GEREKLİ DEĞİL");

        }
        else if(provider.tip==2)
        {



            copEntryholder.ozelentry.setText(provider.ozelentrytext);
            copEntryholder.copentry.setText(provider.entry, TextView.BufferType.SPANNABLE);
            copEntryholder.copentry.setLinksClickable(true);
            //Linkify.addLinks(entry, Linkify.WEB_URLS);
            copEntryholder.copentry.setMovementMethod(LinkMovementMethod.getInstance());
            copEntryholder.zaman.setText(provider.zaman);
            copEntryholder.zaman2.setText(provider.zaman2);

            String duzelturl="https://eksisozluk.com/entry/duzelt/"+provider.entryid;
            final String canlandirurl="canlandir?id="+provider.entryid;
            final String silurl="sil?id="+provider.entryid;



            ozelentryclick(copEntryholder.ozelentry,provider.ozelentryUrl,provider.ozelentrytext);

            copEntryholder.canlandir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    entrysilmedialog(canlandirurl,true,"Entry canlandır");
                }
            });
            copEntryholder.duzelt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new DuzenlemeYonlendirme(provider.entryid, ctx, "",cookie).execute();

                }
            });
            copEntryholder.sil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  entrysilmedialog(silurl,true,"Entry sil");
                }
            });




         /*   Log.d("ozelurl",provider.ozelentryUrl);
            Log.d("duzlt",duzelturl);
            Log.d("canlan",canlandirurl);
            Log.d("sil",silurl);*/

        }
        else if(provider.tip==3)
        {
            String numara="Sayfa "+provider.sayfano;

            sayfaNumarasiHolder.sayfanotext.setText(numara);
        }


        return convertView;
    }

    private void yorumclicklistener(TextView yorum, final String entryid, final String yorumsayisi) {

        yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(ctx,entryid,Toast.LENGTH_SHORT).show();

                if(!yorumsayisi.equals(""))
                {
                    Intent favlayanlar=new Intent(ctx,FavlayanlarActivity.class);
                    favlayanlar.putExtra("cookie",cookie);
                    favlayanlar.putExtra("nick",nick);
                    favlayanlar.putExtra("entryid",entryid);
                    favlayanlar.putExtra("favmi",1);
                    //  Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                    Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                    ctx.startActivity(favlayanlar,bundle);
                }

            }
        });
    }


    private void favtextinetiklama(TextView fav,final String entryid) {

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent favlayanlar=new Intent(ctx,FavlayanlarActivity.class);
                favlayanlar.putExtra("cookie",cookie);
                favlayanlar.putExtra("nick",nick);
                favlayanlar.putExtra("entryid",entryid);
                favlayanlar.putExtra("favmi",0);
                //  Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
               ctx.startActivity(favlayanlar,bundle);
               // ctx.startActivity(favlayanlar,ActivityOptions.makeSceneTransitionAnimation((Activity) ctx).toBundle());



            }
        });

    }

    private void favbutonunatikla(ImageView favresim,final String entryid) {


        favresim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favlayanlar=new Intent(ctx,FavlayanlarActivity.class);
                favlayanlar.putExtra("cookie",cookie);
                favlayanlar.putExtra("nick",nick);
                favlayanlar.putExtra("entryid",entryid);
                favlayanlar.putExtra("favmi",0);
              //  Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                //startActivity(intent, bundle);
                ctx.startActivity(favlayanlar,bundle);
              //  ctx.startActivity(favlayanlar,ActivityOptions.makeSceneTransitionAnimation((Activity) ctx).toBundle());
               // ctx.startActivity(favlayanlar);
            }
        });



    }

    private void artioyeksioy(final ImageButton artioy, final ImageButton eksioy, final EntryProvider entryProvider) {

        artioy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entryProvider.artimi)
                {
                    new ArkaplanEntryIslem(entryProvider,1,false,null,ctx,EntryAdapter.this,cookie).execute();

                }
                else
                {
                    new ArkaplanEntryIslem(entryProvider,1,true,null,ctx,EntryAdapter.this,cookie).execute();

                }
            }
        });

        eksioy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(entryProvider.eksimi)
                {
                    new ArkaplanEntryIslem(entryProvider,-1,false,null,ctx,EntryAdapter.this,cookie).execute();

                }
                else
                {
                    new ArkaplanEntryIslem(entryProvider,-1,true,null,ctx,EntryAdapter.this,cookie).execute();
                }

            }
        });

    }

    private void ozelentryclick(TextView ozelentry, final String ozelentryUrl, final String ozeltext) {

        ozelentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent yazara=new Intent(ctx,EntryActivity.class);
                yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                yazara.putExtra("yonlendirme",false);
                yazara.putExtra("link",ozelentryUrl);
                yazara.putExtra("gelenstring",ozeltext);
                yazara.putExtra("cookie",cookie);
                yazara.putExtra("nick",nick);
                yazara.putExtra("mark"," ");
                yazara.putExtra("arsiv",false);
                Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                //startActivity(intent, bundle);
                ctx.startActivity(yazara,bundle);
            }
        });

    }

    private void kendientrydialogu(EntryProvider entryProvider) {
        //SecenekAdapter ad;

        LayoutInflater inflator= LayoutInflater.from(ctx);
        @SuppressLint("InflateParams") View view=inflator.inflate(R.layout.seceneklerlist, null);
        TextView texts= view.findViewById(R.id.baslik1);
        String textstringi="#"+entryProvider.entryid;
        texts.setText(textstringi);


        listView=  view.findViewById(R.id.listView6);
        SecenekAdapter secenekAdapter=new SecenekAdapter(ctx,R.layout.seceneklistyapisi);
        //ad=secenekAdapter;
        int id[]={R.drawable.sil,
                R.drawable.duzenle,
                R.drawable.paylas,
                R.drawable.kaydet
        };




        String[] dizi={"Sil","Düzenle","Paylaş","Kaydet"};
        secenekAdapter.add(new SecenekProvider(id[0],dizi[0]));
        secenekAdapter.add(new SecenekProvider(id[1],dizi[1]));
        secenekAdapter.add(new SecenekProvider(id[2],dizi[2]));
        secenekAdapter.add(new SecenekProvider(id[3],dizi[3]));
        listView.setAdapter(secenekAdapter);
        dialog2=new BottomSheetDialog(ctx,R.style.BottomSheetDialog);
        dialog2.setContentView(view);
        dialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog2.show();
        tiklamadinleyici(entryProvider);


    }

    private void yazarclicklistener(final TextView yazar, final String yazarid) {

        yazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent yazara=new Intent(ctx,YazarActivity.class);
                yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // yazara.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                yazara.putExtra("yazarnick", yazar.getText());
                yazara.putExtra("yazarid",yazarid);
                yazara.putExtra("cookie",cookie);
                yazara.putExtra("nick",nick);
                Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.soldan_giris,R.anim.soldan_cikis).toBundle();
             // ctx.startActivity(yazara);
                ctx.startActivity(yazara,bundle);

            }
        });

    }

    private void dialoggoster(final EntryProvider entryProvider)
    {
      //  SecenekAdapter ad;

        LayoutInflater inflator= LayoutInflater.from(ctx);
        @SuppressLint("InflateParams") View view=inflator.inflate(R.layout.seceneklerlist, null);
        TextView texts=  view.findViewById(R.id.baslik1);
        VeriTabaniBaglanti veriTabaniBaglanti=new VeriTabaniBaglanti(ctx);
        String textstringi="#"+entryProvider.entryid;
        texts.setText(textstringi);
        Animation animationEntry= AnimationUtils.loadAnimation(ctx,R.anim.yenianim);
        texts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent yazara=new Intent(ctx,EntryActivity.class);
                yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                yazara.putExtra("yonlendirme",true);
                yazara.putExtra("link","/entry/"+entryProvider.entryid);
                yazara.putExtra("cookie",cookie);
                yazara.putExtra("nick",nick);
                yazara.putExtra("gelenstring",entryProvider.entryid);
                yazara.putExtra("mark"," ");
                yazara.putExtra("arsiv",false);
                Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                //startActivity(intent, bundle);
                ctx.startActivity(yazara,bundle);

            }
        });



        boolean kayityapiliyormu=servisikontrolet(entryProvider.baslikid,entryProvider.entryid);







            if(!cookie.equals("BOS"))
            {
                listView=  view.findViewById(R.id.listView6);
                SecenekAdapter secenekAdapter=new SecenekAdapter(ctx,R.layout.seceneklistyapisi);
                //ad=secenekAdapter;
                int id[]={R.drawable.favv,
                        R.drawable.artioy,
                        R.drawable.eksioy,
                        R.drawable.mesajat,
                        R.drawable.paylas,
                        R.drawable.kaydet
                };

               if(entryProvider.artimi)
                {
                    id[1]=R.drawable.artioysecili;
                }
                else
                {
                    id[1]=R.drawable.artioy;
                }
                if (entryProvider.eksimi)
                {
                    id[2]=R.drawable.eksioysecili;
                }
                else
                {
                    id[2]=R.drawable.eksioy;
                }


                if(entryProvider.favmi)
                {
                    id[0]=R.drawable.favli;
                }
                else
                {
                    id[0]=R.drawable.favv;
                }



                String[] dizi={"Favori","Artı oy","Eksi oy","Mesaj yolla","Paylaş","Kaydet"};

                if(kayityapiliyormu)
                {
                id[5]=R.drawable.sil;
                    dizi[5]="Arşivden sil";
                }
                else
                {
                    id[5]=R.drawable.kaydet;
                    dizi[5]="Kaydet";
                }


                secenekAdapter.add(new SecenekProvider(id[0],dizi[0]));
                secenekAdapter.add(new SecenekProvider(id[1],dizi[1]));
                secenekAdapter.add(new SecenekProvider(id[2],dizi[2]));
                secenekAdapter.add(new SecenekProvider(id[3],dizi[3]));
                secenekAdapter.add(new SecenekProvider(id[4], dizi[4]));
                secenekAdapter.add(new SecenekProvider(id[5], dizi[5]));
                listView.setAnimation(animationEntry);
                listView.setAdapter(secenekAdapter);




            }
            else
            {
                listView=  view.findViewById(R.id.listView6);
                SecenekAdapter secenekAdapter=new SecenekAdapter(ctx,R.layout.seceneklistyapisi);
                //ad=secenekAdapter;
                int id[]={
                        R.drawable.artioy,
                        R.drawable.eksioy,
                        R.drawable.paylas,
                        R.drawable.kaydet
                };

               if(entryProvider.artimi)
                {
                    id[0]=R.drawable.artioysecili;
                }
                else
                {
                    id[0]=R.drawable.artioy;
                }
                if (entryProvider.eksimi)
                {
                    id[1]=R.drawable.eksioysecili;
                }
                else
                {
                    id[1]=R.drawable.eksioy;
                }

                String[] dizi={"Artı oy","Eksi oy","Paylaş","Kaydet"};

                if(kayityapiliyormu)
                {
                    id[3]=R.drawable.sil;
                    dizi[3]="Arşivden sil";
                }
                else
                {
                    id[3]=R.drawable.kaydet;
                    dizi[3]="Kaydet";
                }



                secenekAdapter.add(new SecenekProvider(id[0], dizi[0]));
                secenekAdapter.add(new SecenekProvider(id[1], dizi[1]));
                secenekAdapter.add(new SecenekProvider(id[2], dizi[2]));
                secenekAdapter.add(new SecenekProvider(id[3], dizi[3]));
                listView.setAdapter(secenekAdapter);


                // listviewtiklamasi(listView);

            }

        if(!cookie.equals("BOS"))
            listView.startAnimation(animationEntry);


        dialog2=new BottomSheetDialog(ctx,R.style.BottomSheetDialog);
        dialog2.setContentView(view);
        dialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog2.show();


tiklamadinleyici(entryProvider);

    }

    private boolean servisikontrolet(String aranacakbaslik,String aranacakentry) {


       /* ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.fatih.eksiyeniduzen.ServisArsivleme".equals(service.service.getClassName())) {
               return true;
            }
        }*/
        VeriTabaniBaglanti vt=new VeriTabaniBaglanti(ctx);

        vt.okumakicinac();

        if(vt.entryVarMi(aranacakentry))
        {
            vt.veritabaniKapat();
            return true;
        }
        else
        {
            vt.veritabaniKapat();
            return false;
        }



    }

    private void tiklamadinleyici(final EntryProvider entryProvider)
    {



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                SecenekProvider secenekProvider= (SecenekProvider) parent.getItemAtPosition(position);

                switch (secenekProvider.secenek) {
                    case "Favori":
                        if (!entryProvider.favmi) {

                            new ArkaplanEntryIslem(entryProvider, true,ctx,EntryAdapter.this,cookie).execute();


                            dialog2.dismiss();
                        } else {
                            new ArkaplanEntryIslem(entryProvider, false,ctx,EntryAdapter.this,cookie).execute();


                            dialog2.dismiss();
                        }


                        break;
                    case "Paylaş":
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "https://eksisozluk.com/entry/" + entryProvider.entryid;
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Deneme Yaallaaa");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        ctx.startActivity(Intent.createChooser(sharingIntent, "Entry Paylaş"));

                        break;
                    case "Artı oy":
                        if (entryProvider.artimi) {
                            new ArkaplanEntryIslem(entryProvider, 1, false, secenekProvider, ctx, EntryAdapter.this,cookie).execute();

                        } else {
                            new ArkaplanEntryIslem(entryProvider, 1, true, secenekProvider, ctx, EntryAdapter.this,cookie).execute();

                        }
                        dialog2.dismiss();

                        break;
                   case "Eksi oy":
                        if (entryProvider.eksimi) {
                            new ArkaplanEntryIslem(entryProvider, -1, false, secenekProvider, ctx, EntryAdapter.this,cookie).execute();

                        } else {
                            new ArkaplanEntryIslem(entryProvider, -1, true, secenekProvider, ctx,EntryAdapter.this,cookie).execute();
                        }
                        dialog2.dismiss();

                        break;

                    case "Mesaj yolla":

                        //mesajatmadialogolustur(entryProvider.yazar,entryProvider.entryid,entryProvider.mesajVerify);

                        StringBuilder entryidbuild=new StringBuilder();
                        entryidbuild.append("(#").append(entryProvider.entryid).append(")");
                        new MesajAtmaKontrolVeDiyalog(entryProvider.yazar,cookie,entryidbuild.toString(),entryProvider.mesajVerify,ctx,false,null).execute();

                        dialog2.dismiss();

                        break;

                    case "Düzenle":
                        new DuzenlemeYonlendirme(entryProvider.entryid, ctx, entryProvider.baslikid,cookie).execute();
                        dialog2.dismiss();
                        break;
                    case "Sil":

                        entrysilmedialog(entryProvider.entryid,false,"Entry sil");

                        dialog2.dismiss();
                       /* AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                        builder.setTitle("Entry sil")
                                .setMessage("Silinsin mi")
                                .setCancelable(false)
                                .setPositiveButton("evet", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        new EntrySil(entryProvider.entryid,false).execute();
                                        Log.d("cevap", "entry silindi");


                                    }
                                }).setNegativeButton("hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        Button c = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        c.setTextColor(Color.GRAY);
                        b.setTextColor(Color.GRAY);
                        b.setAllCaps(false);
                        c.setAllCaps(false);

                        dialog2.dismiss();*/

                        break;

                    case "Kaydet":

                      /*  Intent servis=new Intent(ctx,ServisArsivleme.class);
                        servis.putExtra("url","/entry/"+entryProvider.entryid);
                        servis.putExtra("cookie",cookie);
                        servis.putExtra("nick",nick);
                        servis.putExtra("dongu",1);
                        servis.putExtra("ek","?p=");
                        servis.putExtra("baslik","Entry indiriliyor");
                        servis.putExtra("baslikid",entryProvider.baslikid);
                        ctx.startService(servis);*/

                        new Kayitsilme(entryProvider,false,ctx,cookie,nick).execute();

                        dialog2.dismiss();

                        break;

                    case "Arşivden sil":


                        /*


                        Silme işlemlerini asynctask ile yapmayı dene


                         */
                        Log.d("Silme işlemi","Silme işlemlerini asynctask ile yapmayı dene");
                        new Kayitsilme(entryProvider,true,ctx,"","").execute();

                        dialog2.dismiss();
                        break;
                }





            }
        });
    }

    private void mesajatmadialogolustur(final String yazaradi, String entryid, final String mesajVerify) {

         StringBuilder entryidbuild=new StringBuilder();
         entryidbuild.append("(#").append(entryid).append(")");

        LayoutInflater inflator= LayoutInflater.from(ctx);
        @SuppressLint("InflateParams") final View view=inflator.inflate(R.layout.mesaj_yolla_dialog, null);



        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("gönder",null)
                .setNegativeButton("iptal",null);

        final AlertDialog dialog=builder.create();


        final EditText mesajicerik=view.findViewById(R.id.mesajicerik);
        final AutoCompleteTextView mesajkime=view.findViewById(R.id.mesajatilanyazar);
        final ProgressBar progressBar=view.findViewById(R.id.progressBar);
        mesajkime.setText(yazaradi);
        mesajicerik.setText(entryidbuild.toString());
      //  mesajicerik.requestFocus();

        yazaramaislemi(mesajkime);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
               // new MesajAtmaKontrolVeDiyalog(yazaradi,cookie,view).execute();
            }
        });

        dialog.show();
       // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Button pozitif = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negatif=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        negatif.setAllCaps(false);
        pozitif.setAllCaps(false);
        negatif.setVisibility(View.GONE);
        pozitif.setVisibility(View.GONE);
        pozitif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!mesajicerik.getText().toString().isEmpty())
                new MesajGonder(cookie,mesajVerify,mesajkime.getText().toString(),mesajicerik.getText().toString(),nick,false,ctx,dialog,progressBar).execute();
                else
                    Toast.makeText(ctx,"bir şeyler yazmaya ne dersin delikanlı",Toast.LENGTH_SHORT).show();

                //dialog.dismiss();

            }
        });
        negatif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });




    }

    private void yazaramaislemi(final AutoCompleteTextView mesajkime) {

        mesajkime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!mesajkime.getText().toString().isEmpty())
                    new YazarAra(mesajkime.getText().toString(),mesajkime,ctx).execute();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private static class Kayitsilme extends AsyncTask<Void,Void,Void>
    {


        boolean silmemi;
        EntryProvider ent;
        String cookie;
        String nick;

        WeakReference<Context> contextWeakReference;

        Kayitsilme(EntryProvider ent,boolean silmemi,Context context,String cookie,String nick)
        {
            this.cookie=cookie;
            this.ent=ent;
            this.silmemi=silmemi;
            this.nick=nick;
            contextWeakReference=new WeakReference<Context>(context);

        }

        @Override
        protected Void doInBackground(Void... params) {

            if(silmemi) {
               VeriTabaniBaglanti vtbaglanti = new VeriTabaniBaglanti(contextWeakReference.get());
                vtbaglanti.veritabaniAc();
                vtbaglanti.entrySil(ent.entryid);
                vtbaglanti.baslikguncelle(ent.baslikid, -1);

                if(vtbaglanti.entrylerisay(ent.baslikid)==0)
                {
                    Log.d("entryadapter","başlıkta entry kalmadı kayıt komple siliniyor");
                    vtbaglanti.kayitSil(ent.baslikid);
                }




                vtbaglanti.veritabaniKapat();

            }
            else
            {
                Document entrydoc=null;
                String entryurl="https://eksisozluk.com/entry/"+ent.entryid;

                try {
                             entrydoc=Jsoup.connect(entryurl)
                            .cookie("Cookie",cookie)
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(entrydoc!=null)
                {

                    EntryProvider[] entrykayit=EntryleriAyikla.ArsivIcınAyikla(entrydoc,contextWeakReference.get(),false,cookie,nick);

                    veritabaniEntryiEkle(entrykayit[0]);

                }





            }
            return null;
        }

        private void veritabaniEntryiEkle(EntryProvider entryProvider) {

            VeriTabaniBaglanti veriTabaniBaglanti=new VeriTabaniBaglanti(contextWeakReference.get());
            veriTabaniBaglanti.veritabaniAc();
            veriTabaniBaglanti.entryEkle(entryProvider);

            if(!veriTabaniBaglanti.baslikVarMi(ent.baslikid))
            {
                BaslikProvider baslikobj=new BaslikProvider("1",ent.ozelentrytext,ent.baslikid,0,false);

                veriTabaniBaglanti.baslikekle(baslikobj);
            }else
                veriTabaniBaglanti.baslikguncelle(ent.baslikid,1);


            veriTabaniBaglanti.veritabaniKapat();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    private void entrysilmedialog(final String entryid,final boolean cop,String islemstr)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setTitle(islemstr)
                .setMessage("son kararın mı")
                .setCancelable(false)
                .setPositiveButton("evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        new EntrySil(entryid,cop,ctx,cookie).execute();



                    }
                }).setNegativeButton("hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button c = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        c.setTextColor(Color.GRAY);
        b.setTextColor(Color.GRAY);
        b.setAllCaps(false);
        c.setAllCaps(false);

    }



   private static class ArkaplanEntryIslem extends AsyncTask<Void,Void,Document>
    {
        boolean secenek;

        int oy;
        String islem;
      //  SecenekAdapter adapter;
    //    ImageButton artioy,eksioy;
        SecenekProvider secenekProvider;
        EntryProvider provider;
        ProgressDialog dialog;


        WeakReference<Context> contextWeakReference;
        WeakReference<EntryAdapter> entryAdapterWeakReference;
        String cookie;

         ArkaplanEntryIslem(EntryProvider provider, int oy,boolean secenek,SecenekProvider secenekProvider,Context context,EntryAdapter entryAdapter,String cookie) {

             this.secenekProvider=secenekProvider;
             this.provider = provider;
             this.oy = oy;
             this.secenek=secenek;

             this.cookie=cookie;
             contextWeakReference=new WeakReference<Context>(context);
             entryAdapterWeakReference=new WeakReference<EntryAdapter>(entryAdapter);
          //   this.artioy=artioy;
           //  this.eksioy=eksioy;
             islem="oy";
        }

         ArkaplanEntryIslem( EntryProvider provider,boolean secenek,Context context,EntryAdapter entryAdapter,String cookie) {

            this.provider = provider;
            this.secenek=secenek;
            this.cookie=cookie;
             contextWeakReference=new WeakReference<Context>(context);
             entryAdapterWeakReference=new WeakReference<EntryAdapter>(entryAdapter);
            islem="fav";

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(contextWeakReference.get());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if(islem.equals("oy"))
            dialog.setMessage("Oylanıyor...");
            else
            dialog.setMessage("Favlanıyor...");

            dialog.show();
        }

        // fav için seçenek true ise favla, false ise favlama
                // oylama için seçenek true ise vote, false ise removevote
        @Override
        protected Document doInBackground(Void... params) {
            Document document=null;

           if(islem.equals("fav"))
           {
               if(secenek)
               {

                   try {
                       document = Jsoup.connect("https://eksisozluk.com/entry/favla")
                               .userAgent("Mozilla/5.0 ( compatible )")

                               .header("Accept","*/*")
                               .header("Accept-Encoding", "gzip, deflate, br")
                               .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                               .cookie("Cookie", cookie)
                               .header("Connection", "keep-alive")
                               .header("X-Requested-With", "XMLHttpRequest")
                               .data("entryId", provider.entryid)
                               .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                               .ignoreContentType(true)
                               .post();



                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }

               else
               {
                   try {
                       document = Jsoup.connect("https://eksisozluk.com/entry/favlama")
                               .userAgent("Mozilla/5.0 ( compatible )")

                               .header("Accept","*/*")
                               .header("Accept-Encoding", "gzip, deflate, br")
                               .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                               .cookie("Cookie", cookie)
                               .header("Connection", "keep-alive")
                               .header("X-Requested-With", "XMLHttpRequest")
                               .data("entryId", provider.entryid)
                               .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                               .ignoreContentType(true)
                               .post();



                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }

           }

            else
           {
               if(secenek)
               {
                   try {
                       document= Jsoup.connect("https://eksisozluk.com/entry/vote")
                               .userAgent("Mozilla/5.0 ( compatible )")

                               .header("Accept-Encoding", "gzip, deflate, br")
                               .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                               .cookie("Cookie", cookie)
                               .header("Connection", "keep-alive")
                               .header("X-Requested-With", "XMLHttpRequest")
                               .data("id", provider.entryid )
                               .data("rate", String.valueOf(oy))
                               .data("owner", provider.yazarid)
                               .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                               .ignoreContentType(true)
                               .post();



                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
               else
               {
                   try {
                       document= Jsoup.connect("https://eksisozluk.com/entry/removevote")
                               .userAgent("Mozilla/5.0 ( compatible )")

                               .header("Accept-Encoding", "gzip, deflate, br")
                               .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                               .cookie("Cookie", cookie)
                               .header("Connection", "keep-alive")
                               .header("X-Requested-With", "XMLHttpRequest")
                               .data("id", provider.entryid )
                               .data("rate", String.valueOf(oy))
                               .data("owner", provider.yazarid)
                               .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                               .ignoreContentType(true)
                               .post();



                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }



           }










            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            dialog.dismiss();

            if (document != null) {

            JSONObject jsonObject;
            String yenifav = "";
            String oys;
            if (islem.equals("fav")) {

                if (secenek) {
                    try {
                        jsonObject = new JSONObject(document.body().html());
                        yenifav = jsonObject.getString("Count");
                        Toast.makeText(contextWeakReference.get(),"favlandı",Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    provider.favmi = true;
                    provider.favlar = yenifav;
                    entryAdapterWeakReference.get().notifyDataSetChanged();

                } else {
                    try {
                        jsonObject = new JSONObject(document.body().html());
                        yenifav = jsonObject.getString("Count");
                        Toast.makeText(contextWeakReference.get(),"favlanmadı",Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    provider.favlar = yenifav;
                    provider.favmi = false;
                    entryAdapterWeakReference.get().notifyDataSetChanged();

                }
            } else {
                if (secenek) {
                    if (oy == 1) {
                        try {
                            jsonObject = new JSONObject(document.body().html());
                            oys = jsonObject.getString("Message");
                            Log.d("artı oy", oys);
                            Toast.makeText(contextWeakReference.get(),oys,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //   secenekProvider.resimid=R.drawable.artioysecili;
                        //  artioy.setImageResource(R.drawable.artioysecili);
                        provider.artimi = true;
                        provider.eksimi = false;
                        entryAdapterWeakReference.get().notifyDataSetChanged();
                    } else {
                        try {
                            jsonObject = new JSONObject(document.body().html());
                            oys = jsonObject.getString("Message");
                            Log.d("eksi oy", oys);
                            Toast.makeText(contextWeakReference.get(),oys,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //   secenekProvider.resimid=R.drawable.eksioysecili;
                        //  eksioy.setImageResource(R.drawable.eksioysecili);
                        provider.eksimi = true;
                        provider.artimi = false;
                        entryAdapterWeakReference.get().notifyDataSetChanged();
                    }

                } else {
                    if (oy == 1) {
                        try {
                            jsonObject = new JSONObject(document.body().html());
                            oys = jsonObject.getString("Message");
                            Log.d("artı oy geri alma", oys);
                            Toast.makeText(contextWeakReference.get(),oys,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //  secenekProvider.resimid=R.drawable.artioy;
                        //  artioy.setImageResource(R.drawable.artioy);
                        provider.artimi = false;
                        provider.eksimi = false;
                        entryAdapterWeakReference.get().notifyDataSetChanged();
                    } else {
                        try {
                            jsonObject = new JSONObject(document.body().html());
                            oys = jsonObject.getString("Message");
                           // Log.d("eksi oy geri alma", oys);
                            Toast.makeText(contextWeakReference.get(),oys,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // secenekProvider.resimid=R.drawable.eksioy;
                        //   eksioy.setImageResource(R.drawable.eksioy);
                        provider.artimi = false;
                        provider.eksimi = false;
                        entryAdapterWeakReference.get().notifyDataSetChanged();
                    }


                }

            }

        }
            else
                Toast.makeText(contextWeakReference.get(),"Hata",Toast.LENGTH_SHORT).show();
         //   Log.d("islem sonucu",document.html());


        }
    }

   private static class DuzenlemeYonlendirme extends AsyncTask<Void,Void,Document>
    {

        String entryid;
        String url;
        String baslikid;
        String cookie;

        WeakReference<Context> contextWeakReference;

       private ProgressDialog dialog;

        DuzenlemeYonlendirme(String entryid, Context context, String baslikid,String cookie)
        {
            this.entryid=entryid;
            contextWeakReference=new WeakReference<Context>(context);
            this.baslikid=baslikid;
            this.cookie=cookie;
            url="https://eksisozluk.com/entry/duzelt/"+entryid;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(contextWeakReference.get());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("entry getiriliyor...");
            dialog.show();
        }

        @Override
        protected Document doInBackground(Void... params) {
            Document document=null;

            try {
                document = Jsoup.connect(url)
                        .cookie("Cookie", cookie)
                        .userAgent("Mozilla/5.0 ( compatible )")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .timeout(10 * 1000)
                        .method(Connection.Method.GET)
                        .get();


                String title=document.select("h1 > a").text();
                String verify=document.select("form.editform > input").first().attr("value");
                String textt=document.select("textarea.edittextbox").text();

                Log.d("başlık",title);
                Log.d("verify",verify);
                Log.d("text",textt);

            } catch (IOException e) {
                e.printStackTrace();
            }



            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            dialog.dismiss();

            String title=document.select("h1 > a").text();
            String verify=document.select("form.editform > input").first().attr("value");
            String textt=document.select("textarea.edittextbox").text();



            Intent entry=new Intent(contextWeakReference.get(),EntryYazmaActivity.class);
            entry.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            entry.putExtra("title", title);
            entry.putExtra("baslikid",baslikid);
            entry.putExtra("verify",verify);
            entry.putExtra("editid",entryid);
            entry.putExtra("edit",true);
            entry.putExtra("text", textt);
            entry.putExtra("cookie",cookie);
            contextWeakReference.get().startActivity(entry);



        }
    }

   private static class EntrySil extends AsyncTask<Void,Void,Connection.Response>
    {
        String entryid;
        private ProgressDialog dialog;
        boolean cop;

        WeakReference<Context> contextWeakReference;
        String cookie;

        EntrySil(String entryid,boolean cop,Context context,String cookie)
        {
            this.entryid=entryid;
            this.cop=cop;
            this.cookie=cookie;

            contextWeakReference=new WeakReference<Context>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(contextWeakReference.get());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("siliniyor...");
            dialog.show();

        }

        @Override
        protected Connection.Response doInBackground(Void... params) {
            Connection.Response document=null;

            try {

                if(cop)
                {
                    document = Jsoup.connect("https://eksisozluk.com/cop/"+entryid)
                            .userAgent("Mozilla/5.0 ( compatible )")
                            .cookie("Cookie", cookie)
                            .header("X-Requested-With", "XMLHttpRequest")
                            .ignoreContentType(true)
                            .timeout(10*1000)
                            .method(Connection.Method.POST)
                            .execute();


                }
                else
                {
                    document = Jsoup.connect("https://eksisozluk.com/entry/sil")
                            .userAgent("Mozilla/5.0 ( compatible )")
                            .header("Accept","*/*")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                            .cookie("Cookie", cookie)
                            .header("Connection", "keep-alive")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .data("id", entryid)
                            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .ignoreContentType(true)
                            .timeout(10*1000)
                            .method(Connection.Method.POST)
                            .execute();



                }



            } catch (IOException e) {

                e.printStackTrace();
            }


            return document;
        }

        @Override
        protected void onPostExecute(Connection.Response aVoid) {
            super.onPostExecute(aVoid);

            dialog.dismiss();
            if(aVoid!=null)
            {



                Toast.makeText(contextWeakReference.get(),"işlem tamam", Toast.LENGTH_SHORT).show();
            }
            else
            {

                Toast.makeText(contextWeakReference.get(),"bir şeyler yanlış gitti", Toast.LENGTH_SHORT).show();
            }


        }
    }




}
