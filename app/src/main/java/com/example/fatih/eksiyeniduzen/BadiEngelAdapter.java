package com.example.fatih.eksiyeniduzen;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatih.eksiyeniduzen.Interfaceler.HolderTiklamaIslemi;
import com.squareup.haha.perflib.Main;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fatih on 31.10.2017.
 */

 class BadiEngelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HolderTiklamaIslemi {


    private List<BadiEngelProvider> list;

    private final int VIEW_ITEM=1;
    private final int VIEW_AYRAC=0;
    private final int VIEW_FAVITEM=2;
    private final int VIEW_FAVAYRAC=3;
    private final int VIEW_YORUM=4;
    private final int VIEW_MESAJ=5;
    private final int VIEW_MESAJLASMA=6;
    private final int VIEW_MESAJLASMA_SAG=7;
    private final int VIEW_SORUNSAL=8;
    private final int VIEW_IKONDEVAM=9;
   // private final int VIEW_SORUNSALYANIT=10;
    private final int VIEW_ENTRYYAZMA=10;

    private String cookie;
    private String nick;
    private Context context;
    private String entryid;

    private int sayfanumarasi=2;

    private MainfragdanMainacte mainfragdanMainacte; // Entryyazma activitye pozisyon yollanaarak edittextin textini değiştirmek için

    BadiEngelAdapter (List<BadiEngelProvider> list,Context context,String cookie,String nick)
    {
        this.list=list;
        this.cookie=cookie;
        this.nick=nick;
        this.context=context;

    }
    BadiEngelAdapter(List<BadiEngelProvider> list,Context context,String cookie,String nick,String entryid)
    {
        this.list=list;
        this.cookie=cookie;
        this.nick=nick;
        this.context=context;
        this.entryid=entryid;

    }
    BadiEngelAdapter (List<BadiEngelProvider> list,Context context,MainfragdanMainacte mainfragdanMainacte) // EntryYazmaActivity için adaptör
    {
        this.list=list;
        this.context=context;
        this.mainfragdanMainacte=mainfragdanMainacte;
    }


    void bosalt()
    {
        list.clear();
        sayfanumarasi=2;
        this.notifyDataSetChanged();
    }

    void sonuncuyusil()
    {
        list.remove(list.size()-1);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==VIEW_ITEM)
        {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.badiengelyapi, parent, false);

            return new ViewHolder(v);

        }
        else if(viewType==VIEW_AYRAC)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.badiengel_ayrac, parent, false);



            return new AyracHolder(v);
        }
       else if(viewType==VIEW_FAVITEM)
        {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favlayanlar_yapi, parent, false);

            return new FavlayanView(v,this);

        }
        else if(viewType==VIEW_FAVAYRAC)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.baslik_sayfa_ayrac, parent, false);



            return new FavAyrac(v);
        }
        else if(viewType==VIEW_YORUM)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_yorum_layout, parent, false);



            return new YorumHolder(v);
        }
        else if(viewType==VIEW_MESAJ)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mesaj_yapisi, parent, false);

            return new MesajHolder(v);
        }
        else if(viewType==VIEW_MESAJLASMA)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mesajlasma_yapisi, parent, false);

            return new MesajlasmaHolder(v);
        }
        else if(viewType==VIEW_MESAJLASMA_SAG)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mesaj_yapisi_sol, parent, false);

            return new MesajlasmaHolder(v);
        }
        else if(viewType==VIEW_SORUNSAL)
        {


            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_yorum_layout, parent, false);

            return new YorumHolder(v);
        }
        else if(viewType==VIEW_IKONDEVAM)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ayrac_ikon, parent, false);

            return new DevamAyracHolder(v,this);
        }
        else if(viewType==VIEW_ENTRYYAZMA)
        {   View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entrybutonlari, parent, false);

            return new EntryYazmaHolder(v,this);
        }


        else
            return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BadiEngelProvider badiEngelProvider=list.get(position);


        if(holder.getItemViewType()==VIEW_ITEM)
        {

            ViewHolder viewHolder= (ViewHolder) holder;

            viewHolder.textView.setText(badiEngelProvider.yazarnick);

        }
        else if (holder.getItemViewType()==VIEW_AYRAC)
        {
                AyracHolder ayracHolder= (AyracHolder) holder;

                ayracHolder.ayractextView.setText(badiEngelProvider.yazarnick);
        }
        else if(holder.getItemViewType()==VIEW_FAVITEM)
        {
            FavlayanView favlayanView= (FavlayanView) holder;
          //  TypedValue outValue = new TypedValue();
          //  context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
              //  favlayanView.item.setBackgroundResource(outValue.resourceId);

            favlayanView.favyazar.setText(badiEngelProvider.yazarnick);


        }
        else if(holder.getItemViewType()==VIEW_FAVAYRAC)
        {
            FavAyrac favAyrac= (FavAyrac) holder;

            favAyrac.caylaksayisi.setText(badiEngelProvider.yazarnick);
            favAyrac.favspin.setVisibility(View.GONE);
            favAyrac.caylaksayisi.setVisibility(View.VISIBLE);

        }
        else if(holder.getItemViewType()==VIEW_YORUM)
        {
            YorumHolder yorumHolder= (YorumHolder) holder;

            yorumHolder.entry.setText(badiEngelProvider.entry, TextView.BufferType.SPANNABLE);
            yorumHolder.entry.setLinksClickable(true);
            yorumHolder.entry.setMovementMethod(LinkMovementMethod.getInstance());

            yorumHolder.yazar.setText(badiEngelProvider.yazarnick);
            yorumHolder.zaman.setText(badiEngelProvider.entryzaman);
            yorumHolder.artioysayisi.setText(badiEngelProvider.artioysayisi);
            yorumHolder.eksioysayisi.setText(badiEngelProvider.eksioysayisi);

            if(badiEngelProvider.artimi)
                yorumHolder.artioy.setImageResource(R.drawable.artioysecili);
            else
                yorumHolder.artioy.setImageResource(R.drawable.artioy);

            if(badiEngelProvider.eksimi)
                yorumHolder.eksioy.setImageResource(R.drawable.eksioysecili);
            else
                yorumHolder.eksioy.setImageResource(R.drawable.eksioy);
        }
        else if(holder.getItemViewType()==VIEW_MESAJ)
        {
            MesajHolder mesajHolder= (MesajHolder) holder;

            mesajHolder.mesajatan.setText(badiEngelProvider.yazarnick);
            mesajHolder.mesajicerik.setText(badiEngelProvider.yazarid);
            mesajHolder.mesajtarih.setText(badiEngelProvider.entryzaman);

            if(badiEngelProvider.artimi)
                mesajHolder.arsivle.setVisibility(View.GONE);
            else
                mesajHolder.arsivle.setVisibility(View.VISIBLE);
        }
        else if(holder.getItemViewType()==VIEW_MESAJLASMA)
        {

            MesajlasmaHolder mesajlasmaHolder= (MesajlasmaHolder) holder;

            mesajlasmaHolder.mesajicerik.setText(badiEngelProvider.entry, TextView.BufferType.SPANNABLE);
            mesajlasmaHolder.mesajicerik.setLinksClickable(true);
            mesajlasmaHolder.mesajicerik.setMovementMethod(LinkMovementMethod.getInstance());

            mesajlasmaHolder.mesajzaman.setText(badiEngelProvider.entryzaman);

         /*   if(badiEngelProvider.artimi)
            {
                mesajlasmaHolder.mesajicerik.setGravity(Gravity.START);
                mesajlasmaHolder.mesajzaman.setGravity(Gravity.START);
            }
            else
            {
                mesajlasmaHolder.mesajicerik.setGravity(Gravity.END);
                mesajlasmaHolder.mesajzaman.setGravity(Gravity.END);
                mesajlasmaHolder.mesajicerik.setTextColor(ContextCompat.getColor(context,R.color.eksirenk));
                mesajlasmaHolder.mesajicerik.setTypeface(Typeface.DEFAULT_BOLD);
            }*/


        }
        else if(holder.getItemViewType()==VIEW_MESAJLASMA_SAG)
        {
            MesajlasmaHolder mesajlasmaHolder= (MesajlasmaHolder) holder;

            mesajlasmaHolder.mesajicerik.setText(badiEngelProvider.entry, TextView.BufferType.SPANNABLE);
            mesajlasmaHolder.mesajicerik.setLinksClickable(true);
            mesajlasmaHolder.mesajicerik.setMovementMethod(LinkMovementMethod.getInstance());
            mesajlasmaHolder.mesajzaman.setText(badiEngelProvider.entryzaman);
        }
        else if(holder.getItemViewType()==VIEW_SORUNSAL)
        {
            YorumHolder yorumHolder= (YorumHolder) holder;



            if(TextUtils.isEmpty(badiEngelProvider.entry))
            {
                yorumHolder.entry.setVisibility(View.GONE);
            }
            else
            {
                yorumHolder.entry.setVisibility(View.VISIBLE);
            }

            if(badiEngelProvider.artimi)
                yorumHolder.artioy.setImageResource(R.drawable.artioysecili);
            else
                yorumHolder.artioy.setImageResource(R.drawable.artioy);

            if(badiEngelProvider.eksimi)
                yorumHolder.eksioy.setImageResource(R.drawable.eksioysecili);
            else
                yorumHolder.eksioy.setImageResource(R.drawable.eksioy);
/*
            if(!TextUtils.isEmpty(badiEngelProvider.yazarid))
                yorumHolder.sorunsalYanit.setVisibility(View.VISIBLE);
            else
                yorumHolder.sorunsalYanit.setText(context.getResources().getString(R.string.yanityok));*/

            Log.d("sorunsalyanit",position+"  " +badiEngelProvider.yazarid);
            yorumHolder.sorunsalBaslik.setVisibility(View.VISIBLE);
            yorumHolder.sorunsalYanit.setVisibility(View.VISIBLE);

            yorumHolder.entry.setText(badiEngelProvider.entry, TextView.BufferType.SPANNABLE);
            yorumHolder.entry.setLinksClickable(true);
            yorumHolder.entry.setMovementMethod(LinkMovementMethod.getInstance());


            yorumHolder.artioysayisi.setText(badiEngelProvider.artioysayisi);
            yorumHolder.yazar.setText(badiEngelProvider.yazarnick);
            yorumHolder.zaman.setText(badiEngelProvider.entryzaman);
            yorumHolder.sorunsalBaslik.setText(badiEngelProvider.mesajAtilacakYazar);
            yorumHolder.sorunsalYanit.setText(badiEngelProvider.eksioysayisi);
            yorumHolder.artioysayisi.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            yorumHolder.artioysayisi.setTextSize(15);
            yorumHolder.artioysayisi.setTextColor(ContextCompat.getColor(context,R.color.eksimavi));
        }
        else if(holder.getItemViewType()==VIEW_IKONDEVAM)
        {
            DevamAyracHolder devamAyracHolder= (DevamAyracHolder) holder;
            devamAyracHolder.devamet.setVisibility(View.VISIBLE);
            devamAyracHolder.devamprogress.setVisibility(View.GONE);
        }
        else if(holder.getItemViewType()==VIEW_ENTRYYAZMA)
        {
            EntryYazmaHolder entryYazmaHolder= (EntryYazmaHolder) holder;

            entryYazmaHolder.entryYazmaButon.setText(badiEngelProvider.yazarid);

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).tip)
        {
            case 0:
                return VIEW_AYRAC;
            case 1:
                return VIEW_ITEM;
            case 2:
                return VIEW_FAVITEM;
            case 3:
                return VIEW_FAVAYRAC;
            case 5:
                return VIEW_MESAJ;
            case 6:
                return VIEW_MESAJLASMA;
            case 7:
                return VIEW_MESAJLASMA_SAG;
            case 8:
                return VIEW_SORUNSAL;
            case 9:
                return VIEW_IKONDEVAM;
            case 10:
                return VIEW_ENTRYYAZMA;


            default:
                return VIEW_YORUM;


        }
    }

     void itemSil(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    void itemGeriGetir(BadiEngelProvider provider,int pozisyon)
    {
        list.add(pozisyon,provider);


        notifyItemInserted(pozisyon);

    }

    @Override
    public void sorunsalDevam(int pos) {

        if(list.get(pos).link.equals("t"))
        {
            String devamurlsi=list.get(pos).yazarid+"?p="+sayfanumarasi;

            Log.d("sorunsalYanıtDevam",devamurlsi);

            new FavlayanlarActivity.SorunsalYanitlar(devamurlsi,cookie,nick, (BadiEngelFragHaberlesme) context,context,null,true).execute();
        }
        else
        {
            String devamurlsi=list.get(pos).yazarid+"&p="+sayfanumarasi;
            String spinnersecilen=list.get(pos).entryzaman;

            Log.d("sorunsalNormalDevam",devamurlsi);

            new FavlayanlarActivity.Sorunsallar(devamurlsi,cookie,nick, (BadiEngelFragHaberlesme) context,context,null,true,spinnersecilen).execute();
        }


        sayfanumarasi++;

    }

    @Override
    public void favItemTiklama(int pos) {


        String ynick=list.get(pos).yazarnick.replace("@","");

        Intent yazara=new Intent(context,YazarActivity.class);
        yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        yazara.putExtra("yazarnick", ynick);
        yazara.putExtra("cookie",cookie);
        yazara.putExtra("nick",nick);
        Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
        //startActivity(intent, bundle);
        context.startActivity(yazara,bundle);


    }

    @Override
    public void entryYazmaButonIslemleri(int pos) {

        mainfragdanMainacte.entryYazmaButonundanActivitye(pos);

    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;

        RelativeLayout onplan,arkaplan,arka2;

         ViewHolder(View itemView) {
            super(itemView);

             textView=  itemView.findViewById(R.id.badiengelYazarNick);
             onplan=  itemView.findViewById(R.id.onplan);
             arkaplan=  itemView.findViewById(R.id.arkaplan);
             arka2= itemView.findViewById(R.id.arkaplan2);

        }
    }
    private class AyracHolder extends RecyclerView.ViewHolder
    {

        TextView ayractextView;

         AyracHolder(View itemView) {
            super(itemView);
            ayractextView=  itemView.findViewById(R.id.ayrac);
        }
    }
    static class FavlayanView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView favyazar;

        RelativeLayout item;
        HolderTiklamaIslemi holderTiklamaIslemi;

        FavlayanView(View itemView,HolderTiklamaIslemi holderTiklamaIslemi) {
            super(itemView);

            this.holderTiklamaIslemi=holderTiklamaIslemi;
            favyazar=  itemView.findViewById(R.id.favyazar);
            item=  itemView.findViewById(R.id.item);

            item.setOnClickListener(this);

           /* item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                //    Log.d("favlayanitem",list.get(getAdapterPosition()).yazarnick+"  "+list.get(getAdapterPosition()).link);

                    String ynick=list.get(getAdapterPosition()).yazarnick.replace("@","");

                    Intent yazara=new Intent(context,YazarActivity.class);
                    yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    yazara.putExtra("yazarnick", ynick);
                    yazara.putExtra("cookie",cookie);
                    yazara.putExtra("nick",nick);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                    //startActivity(intent, bundle);
                    context.startActivity(yazara,bundle);

                }
            });*/

        }

        @Override
        public void onClick(View view) {
            holderTiklamaIslemi.favItemTiklama(getAdapterPosition());
        }
    }

    private class FavAyrac extends RecyclerView.ViewHolder
    {

        TextView caylaksayisi;
        ConstraintLayout caylakayrac;
        ProgressBar favspin;

        FavAyrac(View itemView) {
            super(itemView);
            caylaksayisi=  itemView.findViewById(R.id.sayfanumarasi);
            caylakayrac= itemView.findViewById(R.id.caylakayrac);
            favspin=  itemView.findViewById(R.id.favspin);

            caylakayrac.setOnClickListener(new View.OnClickListener() {
                int yorumsayfanumarasi=2;

                @Override
                public void onClick(View v) {

                    caylaksayisi.setVisibility(View.GONE);
                    favspin.setVisibility(View.VISIBLE);

                    if(list.get(getAdapterPosition()).link.equals("f"))
                    new Favlayan(entryid,cookie,BadiEngelAdapter.this,list).execute();

                    else if(list.get(getAdapterPosition()).link.equals("s"))
                    {


                    String devamurlsi=list.get(getAdapterPosition()).yazarid+"&p="+sayfanumarasi;
                    String spinnersecilen=list.get(getAdapterPosition()).entryzaman;

                    Log.d("devamurl",devamurlsi);

                    new FavlayanlarActivity.Sorunsallar(devamurlsi,cookie,nick, (BadiEngelFragHaberlesme) context,context,null,true,spinnersecilen).execute();
                    sayfanumarasi++;

                    }
                    else
                    {
                        new YorumDevam(entryid,yorumsayfanumarasi,caylaksayisi,favspin,BadiEngelAdapter.this,context,list,cookie,nick).execute();
                        yorumsayfanumarasi++;

                    }


                }
            });

        }
    }
    private class YorumHolder extends RecyclerView.ViewHolder // Sorunsal ve yorum viewholderı
    {
        TextView entry;
        TextView yazar;
        TextView zaman;
        ImageButton artioy,eksioy;
        TextView artioysayisi,eksioysayisi;
        TextView sorunsalBaslik,sorunsalYanit;

        public YorumHolder(View itemView) {
            super(itemView);

            entry= itemView.findViewById(R.id.entry);
            yazar=  itemView.findViewById(R.id.yazaradi);
            zaman= itemView.findViewById(R.id.entryzaman);
            artioy=  itemView.findViewById(R.id.artioy);
            eksioy=  itemView.findViewById(R.id.eksioy);
            artioysayisi= itemView.findViewById(R.id.artioytext);
            eksioysayisi= itemView.findViewById(R.id.eksioytext);
            sorunsalBaslik=itemView.findViewById(R.id.sorunsalBaslik);
            sorunsalYanit=itemView.findViewById(R.id.sorunsalYanit);

            yazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent yazara=new Intent(context,YazarActivity.class);
                    yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // yazara.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    yazara.putExtra("yazarnick", list.get(getAdapterPosition()).yazarnick);
                 //   yazara.putExtra("yazarid",yazarid);
                    yazara.putExtra("cookie",cookie);
                    yazara.putExtra("nick",nick);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.soldan_giris,R.anim.soldan_cikis).toBundle();
                    // ctx.startActivity(yazara);
                    context.startActivity(yazara,bundle);
                }
            });

            sorunsalBaslik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.d("badiengeladaptYorumHold",list.get(getAdapterPosition()).yazarid+"    "+list.get(getAdapterPosition()).link);

                    Intent favlayanlar=new Intent(context,FavlayanlarActivity.class);
                    favlayanlar.putExtra("cookie",cookie);
                    favlayanlar.putExtra("nick",nick);
                    favlayanlar.putExtra("sorunsalURL",list.get(getAdapterPosition()).link);
                   // favlayanlar.putExtra("sorunsalNesne",list.get(getAdapterPosition()));
                    favlayanlar.putExtra("favmi",4);
                    //  Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                    Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                    context.startActivity(favlayanlar,bundle);


                }
            });

            artioy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context,"yorum id = "+list.get(getAdapterPosition()).yorumid+"  "+list.get(getAdapterPosition()).yazarid,Toast.LENGTH_SHORT).show();

                    String url;
                    boolean sorunOylamaMi;
                    boolean secenek=!list.get(getAdapterPosition()).artimi;

                    // secenek false ise oylama yapılacak, true ise oy geri alınacak

                    if (secenek)
                    {

                        if(list.get(getAdapterPosition()).link.equals("t"))
                        {
                            url="https://eksisozluk.com/matter/voteanswer";
                            sorunOylamaMi=false;
                        }
                        else if(list.get(getAdapterPosition()).link.equals("y"))
                        {
                            url="https://eksisozluk.com/yorum/vote";
                            sorunOylamaMi=false;
                        }
                        else
                        {
                            url="https://eksisozluk.com/matter/votematter";
                            sorunOylamaMi=true;
                        }


                     //   new YorumOylama(list.get(getAdapterPosition()), 1, false, context, BadiEngelAdapter.this,cookie,artioy,eksioy).execute();

                    }
                    else
                        {
                        if(list.get(getAdapterPosition()).link.equals("t"))
                        {
                            url="https://eksisozluk.com/matter/removeanswervote";
                            sorunOylamaMi=false;

                        }
                        else if(list.get(getAdapterPosition()).link.equals("y"))
                        {
                            url="https://eksisozluk.com/yorum/removevote";
                            sorunOylamaMi=false;
                        }
                        else
                        {
                                url="https://eksisozluk.com/matter/votematter";
                                sorunOylamaMi=true;
                        }
                     //   new YorumOylama(list.get(getAdapterPosition()), 1, true, context, BadiEngelAdapter.this,cookie,artioy,eksioy).execute();

                    }

                    Log.d("oylamaurl",url);

                    new YorumOylama(list.get(getAdapterPosition()), 1, secenek, context, BadiEngelAdapter.this,cookie,artioy,eksioy,url,sorunOylamaMi,getAdapterPosition()).execute();

                }
            });
            eksioy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context,"yorum id = "+list.get(getAdapterPosition()).yorumid+"  "+list.get(getAdapterPosition()).yazarid,Toast.LENGTH_SHORT).show();


                    String url;
                    boolean sorunOylamaMi;
                    boolean secenek=!list.get(getAdapterPosition()).eksimi;

                    if (secenek)
                    {

                        if(list.get(getAdapterPosition()).link.equals("t"))
                        {
                            url="https://eksisozluk.com/matter/voteanswer";
                            sorunOylamaMi=false;
                        }
                        else if(list.get(getAdapterPosition()).link.equals("y"))
                        {
                            url="https://eksisozluk.com/yorum/vote";
                            sorunOylamaMi=false;
                        }
                        else
                        {
                            url="https://eksisozluk.com/matter/votematter";
                            sorunOylamaMi=true;
                        }

                        //   new YorumOylama(list.get(getAdapterPosition()), 1, false, context, BadiEngelAdapter.this,cookie,artioy,eksioy).execute();

                    }
                    else
                    {
                        if(list.get(getAdapterPosition()).link.equals("t"))
                        {
                            url="https://eksisozluk.com/matter/removeanswervote";
                            sorunOylamaMi=false;

                        }
                        else if(list.get(getAdapterPosition()).link.equals("y"))
                        {
                            url="https://eksisozluk.com/yorum/removevote";
                            sorunOylamaMi=false;
                        }
                        else
                        {
                            url="https://eksisozluk.com/matter/votematter";
                            sorunOylamaMi=true;
                        }

                        //   new YorumOylama(list.get(getAdapterPosition()), 1, true, context, BadiEngelAdapter.this,cookie,artioy,eksioy).execute();

                    }

                    Log.d("oylamaurl",url);


                  /*  if (list.get(getAdapterPosition()).eksimi) {
                        new YorumOylama(list.get(getAdapterPosition()), -1, false, context, BadiEngelAdapter.this,cookie,artioy,eksioy).execute();

                    } else {
                        new YorumOylama(list.get(getAdapterPosition()), -1, true, context, BadiEngelAdapter.this,cookie,artioy,eksioy).execute();

                    }*/

                    new YorumOylama(list.get(getAdapterPosition()), -1, secenek, context, BadiEngelAdapter.this,cookie,artioy,eksioy,url,sorunOylamaMi,getAdapterPosition()).execute();

                }
            });


        }
    }
    private class MesajHolder extends RecyclerView.ViewHolder
    {

        TextView mesajatan;
        TextView mesajicerik;
        TextView mesajtarih;
        TextView mesajsil;
        TextView arsivle;
        ConstraintLayout mesajkalip;

        MesajHolder(View itemView) {
            super(itemView);
            mesajatan=  itemView.findViewById(R.id.mesajatan);
            mesajicerik=itemView.findViewById(R.id.mesajicerik);
            mesajtarih=itemView.findViewById(R.id.mesajtarih);
            mesajsil=itemView.findViewById(R.id.mesajsil);
            arsivle=itemView.findViewById(R.id.arsivle);
            mesajkalip=itemView.findViewById(R.id.mesajkalip);

            mesajkalip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent favlayanlar=new Intent(context,FavlayanlarActivity.class);
                    favlayanlar.putExtra("cookie",cookie);
                    favlayanlar.putExtra("nick",nick);
                    favlayanlar.putExtra("entryid",list.get(getAdapterPosition()).link);
                    Log.d("ne bu entryid",list.get(getAdapterPosition()).link);
                    favlayanlar.putExtra("favmi",2);
                    favlayanlar.putExtra("arsivmi",list.get(getAdapterPosition()).artimi);
                    favlayanlar.putExtra("verify",list.get(getAdapterPosition()).artioysayisi);
                    favlayanlar.putExtra("yazarnick",list.get(getAdapterPosition()).mesajAtilacakYazar);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                    context.startActivity(favlayanlar,bundle);


                }
            });

            mesajsil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("sil",list.get(getAdapterPosition()).yorumid);

                    if(list.get(getAdapterPosition()).yorumid.contains(","))
                    new MesajSil(getAdapterPosition(),list.get(getAdapterPosition()).yorumid,0,cookie,BadiEngelAdapter.this).execute();
                    else
                        new MesajSil(getAdapterPosition(),list.get(getAdapterPosition()).yorumid,2,cookie,BadiEngelAdapter.this).execute();

                }
            });

            arsivle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("arşivle",list.get(getAdapterPosition()).yorumid);
                    new MesajSil(getAdapterPosition(),list.get(getAdapterPosition()).yorumid,1,cookie,BadiEngelAdapter.this).execute();

                }
            });

        }
    }
    private class MesajlasmaHolder extends RecyclerView.ViewHolder
    {

        TextView mesajicerik;
        TextView mesajzaman;

        MesajlasmaHolder(View itemView) {
            super(itemView);
            mesajicerik=  itemView.findViewById(R.id.mesajtext);
            mesajzaman=itemView.findViewById(R.id.mesajzaman);
        }
    }
    static class DevamAyracHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView devamet;
        ProgressBar devamprogress;

        HolderTiklamaIslemi tiklamaIslemi;

        public DevamAyracHolder(View itemView,HolderTiklamaIslemi holderTiklamaIslemi) {
            super(itemView);
            devamet=itemView.findViewById(R.id.devamikon);
            devamprogress=itemView.findViewById(R.id.devam_progress);
            this.tiklamaIslemi=holderTiklamaIslemi;

            devamet.setOnClickListener(this);

         /*   devamet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String devamurlsi=list.get(getAdapterPosition()).yazarid+"&p="+sayfanumarasi;
                    String spinnersecilen=list.get(getAdapterPosition()).entryzaman;

                    Log.d("devamurl",devamurlsi);

                    new FavlayanlarActivity.Sorunsallar(devamurlsi,cookie,nick, (BadiEngelFragHaberlesme) context,context,null,true,spinnersecilen).execute();
                    sayfanumarasi++;

                }
            });*/

        }

        @Override
        public void onClick(View view) {

            tiklamaIslemi.sorunsalDevam(getAdapterPosition());
            devamprogress.setVisibility(View.VISIBLE);
            devamet.setVisibility(View.GONE);

        }
    }

    static class EntryYazmaHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        Button entryYazmaButon;
        HolderTiklamaIslemi holderTiklamaIslemi;

        public EntryYazmaHolder(View itemView,HolderTiklamaIslemi holderTiklamaIslemi) {
            super(itemView);

         this.holderTiklamaIslemi=holderTiklamaIslemi;
         entryYazmaButon=itemView.findViewById(R.id.reccybut);

         entryYazmaButon.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            holderTiklamaIslemi.entryYazmaButonIslemleri(getAdapterPosition());
        }
    }



    private static class Favlayan extends AsyncTask<Void,Void,BadiEngelProvider[]>
    {


        String faventryid;
        String cookie;

        WeakReference<BadiEngelAdapter> badiEngelAdapterWeakReference;
        List<BadiEngelProvider> listBadi;

        Favlayan(String faventryid,String cookie,BadiEngelAdapter badiEngelAdapter,List<BadiEngelProvider> listBadi)
        {
            this.faventryid=faventryid;
            this.cookie=cookie;
            this.listBadi=listBadi;

            badiEngelAdapterWeakReference=new WeakReference<BadiEngelAdapter>(badiEngelAdapter);

        }

        @Override
        protected BadiEngelProvider[] doInBackground(Void... params) {

            BadiEngelProvider[] favlayanlar=null;

            Document document=null;
            String curl="https://eksisozluk.com/entry/caylakfavorites";

            try {
                Connection.Response response= Jsoup.connect(curl)
                        .header("X-Requested-With","XMLHttpRequest")
                        .cookie("Cookie",cookie)
                        .data("entryid","#"+faventryid)
                        .method(Connection.Method.GET)
                        .execute();

                document=Jsoup.parse(response.body());

            } catch (IOException e) {
                e.printStackTrace();
            }


            if(document!=null)
            {

                Elements yazarlar=document.select("ul > li > a");
                favlayanlar=new BadiEngelProvider[yazarlar.size()];

                int i=0;
                for(Element yazar:yazarlar)
                {

                    String yazarnicki=yazar.text();
                    String link=yazar.attr("href");

                    favlayanlar[i]=new BadiEngelProvider("",yazarnicki,link,2);
                    i++;
                }



            }


            return favlayanlar;
        }

        @Override
        protected void onPostExecute(BadiEngelProvider[] badiEngelProviders) {
            super.onPostExecute(badiEngelProviders);

            //list.remove(list.size()-1);

            listBadi.remove(listBadi.size()-1);

            if(badiEngelProviders!=null)
            {

                Collections.addAll(listBadi, badiEngelProviders);
             badiEngelAdapterWeakReference.get().notifyDataSetChanged();

            }

        }

    }

    private static class YorumDevam extends AsyncTask<Void,Void,BadiEngelProvider[]>
    {

        String yorum_entryid;
        int sayfanumarasi;
      //  TextView caylaksayisi;
       // ProgressBar favspin;

        WeakReference<TextView> textViewWeakReference;
        WeakReference<ProgressBar> progressBarWeakReference;
        WeakReference<BadiEngelAdapter> badiEngelAdapterWeakReference;
        WeakReference<Context> contextWeakReference;
        List<BadiEngelProvider> listBadi;
        String cookie;
        String nick;


        YorumDevam(String yorum_entryid,int sayfanumarasi,TextView caylaksayisi,ProgressBar favspin,BadiEngelAdapter badiEngelAdapter,Context context,
                   List<BadiEngelProvider> listBadi,String cookie,String nick)
        {
            this.yorum_entryid=yorum_entryid;
            this.sayfanumarasi=sayfanumarasi;
            this.listBadi=listBadi;
            this.cookie=cookie;
            this.nick=nick;
          //  this.caylaksayisi=caylaksayisi;
          //  this.favspin=favspin;

            textViewWeakReference=new WeakReference<TextView>(caylaksayisi);
            progressBarWeakReference=new WeakReference<ProgressBar>(favspin);
            badiEngelAdapterWeakReference=new WeakReference<BadiEngelAdapter>(badiEngelAdapter);
            contextWeakReference=new WeakReference<Context>(context);
         //   badiEngelAdapterWeakReference=new WeakReference<BadiEngelAdapter>(badiEngelAdapter);

        }

        @Override
        protected BadiEngelProvider[] doInBackground(Void... params) {

            BadiEngelProvider[] yorumlar=null;

            Document document=null;
            String yorum_devam_url="https://eksisozluk.com/yorum/liste/"+yorum_entryid+"?p="+sayfanumarasi;

            try {
                Connection.Response response= Jsoup.connect(yorum_devam_url)
                        .header("X-Requested-With","XMLHttpRequest")
                        .cookie("Cookie",cookie)
                        .method(Connection.Method.GET)
                        .execute();

                document=Jsoup.parse(response.body());

            } catch (IOException e) {
                e.printStackTrace();
            }


            if(document!=null)
            {
                Elements tumyorumlar=document.select("ul > li");
                yorumlar=new BadiEngelProvider[tumyorumlar.size()];

                int i=0;
                for(Element yorum:tumyorumlar)
                {

                    String entryhtml=yorum.select("div.comment-content").html();

                    SpannableString entry=yorumEntrySpanla(entryhtml);


                    String yazarnicki=yorum.attr("data-author");
                    String yazarid=yorum.attr("data-author-id");
                    String entryzaman=yorum.select("a.entry-date").text();
                    String yorumid=yorum.select("footer").attr("data-id");
                    String artioysayisi=yorum.attr("data-up-vote-count");
                    String eksioysayisi=yorum.attr("data-down-vote-count");

                    yorumlar[i]=new BadiEngelProvider(entry,yazarnicki,yazarid,entryzaman,yorumid,"y",artioysayisi,eksioysayisi,4,false,false);
                    i++;
                }
            }


            return yorumlar;
        }
        private SpannableString yorumEntrySpanla(String entryhtml) {

            SpannableString message;
            Spanned x= Html.fromHtml(entryhtml);
            message=new SpannableString(x.toString());
            Object[] spans=x.getSpans(0,x.length(), Object.class);
            for(Object span:spans)
            {
                int start=x.getSpanStart(span);
                int end=x.getSpanEnd(span);
                int flags=x.getSpanFlags(span);
                StringBuilder string= new StringBuilder();

                for(int qop=start;qop<end;qop++)
                {
                    string.append(message.charAt(qop));
                }



                //  Log.d("string",string);
                if(span instanceof URLSpan)
                {
                    URLSpan urlSpan= (URLSpan) span;


                    if (!urlSpan.getURL().startsWith("http")) {
                        //  span=new CallbackSpan(urlSpan.getURL());
                        // Log.d("EntryAyikla",urlSpan.getURL());
                        if(urlSpan.getURL().equals("BOS"))
                        {
                            span=new BackgroundColorSpan(Color.parseColor("#ffff9e"));
                        }else
                        {
                            span=new LinkBaglama(urlSpan.getURL(),string.toString(),contextWeakReference.get(),cookie,nick);
                        }
                        //   Log.d("span",((URLSpan) span).getURL());
                    }
                    else
                    {
                        span=new LinkBaglama(urlSpan.getURL()," ",contextWeakReference.get(),cookie,nick);
                    }

                }

                //if(!string.equals(arananKelime))
                message.setSpan(span, start, end, flags);
                //   else
                //    message.setSpan(new BackgroundColorSpan(Color.parseColor("#ffff9e")),start,end,flags);

            }

            return message;

        }

        @Override
        protected void onPostExecute(BadiEngelProvider[] badiEngelProviders) {
            super.onPostExecute(badiEngelProviders);

            progressBarWeakReference.get().setVisibility(View.GONE);
            textViewWeakReference.get().setVisibility(View.VISIBLE);

           // favspin.setVisibility(View.GONE);
           // caylaksayisi.setVisibility(View.VISIBLE);

            listBadi.remove(listBadi.size()-1);

            //list.remove(list.size()-1);

            if(badiEngelProviders!=null)
            {

                Collections.addAll(listBadi, badiEngelProviders);

                if(badiEngelProviders.length==10)
                {
                    BadiEngelProvider yorumayrac=  new BadiEngelProvider("", "sonrakiler", "",3);
                    listBadi.add(yorumayrac);
                }
                badiEngelAdapterWeakReference.get().notifyDataSetChanged();


            }


        }

    }

    private static class YorumOylama extends AsyncTask<Void,Void,Document>
    {
        boolean secenek;

        int oy;
        //  SecenekAdapter adapter;
        //    ImageButton artioy,eksioy;
        BadiEngelProvider provider;
        ProgressDialog dialog;


        WeakReference<Context> contextWeakReference;
        WeakReference<BadiEngelAdapter> entryAdapterWeakReference;
        WeakReference<ImageButton> artioyWeak;
        WeakReference<ImageButton> eksioyWeak;
        boolean sorunsalOylamaMi;
        String cookie;
        String url;
        int pozisyon;

        YorumOylama(BadiEngelProvider provider, int oy,boolean secenek,Context context,BadiEngelAdapter badiEngelAdapter,
                    String cookie,ImageButton artioy,ImageButton eksioy,String url,boolean sorunsalOylamaMi,int pozisyon) {

            this.pozisyon=pozisyon;
            this.provider = provider;
            this.oy = oy;
            this.secenek=secenek;
            this.url=url;
            this.cookie=cookie;
            this.sorunsalOylamaMi=sorunsalOylamaMi;

            contextWeakReference=new WeakReference<Context>(context);
            entryAdapterWeakReference=new WeakReference<BadiEngelAdapter>(badiEngelAdapter);
            artioyWeak=new WeakReference<ImageButton>(artioy);
            eksioyWeak=new WeakReference<ImageButton>(eksioy);
            //   this.artioy=artioy;
            //  this.eksioy=eksioy;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(contextWeakReference.get());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Oylanıyor...");
            dialog.show();
        }

        // fav için seçenek true ise favla, false ise favlama
        // oylama için seçenek true ise vote, false ise removevote
        @Override
        protected Document doInBackground(Void... params) {
            Document document=null;


            try {
                document= Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 ( compatible )")

                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .cookie("Cookie", cookie)
                        .header("Connection", "keep-alive")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .data("id", provider.yorumid )
                        .data("rate", String.valueOf(oy))
                        .data("owner", provider.yazarid)
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .ignoreContentType(true)
                        .post();

                Log.d("idler",provider.yorumid+"    "+provider.yazarid);



            } catch (IOException e) {
                e.printStackTrace();
            }

            /*    if(secenek)
                {
                    try {
                        document= Jsoup.connect(url)
                                .userAgent("Mozilla/5.0 ( compatible )")

                                .header("Accept-Encoding", "gzip, deflate, br")
                                .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                                .cookie("Cookie", cookie)
                                .header("Connection", "keep-alive")
                                .header("X-Requested-With", "XMLHttpRequest")
                                .data("id", provider.yorumid )
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
                        document= Jsoup.connect(url)
                                .userAgent("Mozilla/5.0 ( compatible )")

                                .header("Accept-Encoding", "gzip, deflate, br")
                                .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                                .cookie("Cookie", cookie)
                                .header("Connection", "keep-alive")
                                .header("X-Requested-With", "XMLHttpRequest")
                                .data("id", provider.yorumid )
                                .data("rate", String.valueOf(oy))
                                .data("owner", provider.yazarid)
                                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .ignoreContentType(true)
                                .post();



                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }*/

            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            dialog.dismiss();

            if (document != null) {

                JSONObject jsonObject;
                String oys;

                oys=jsonVeriAyikla(document.body().html());


                if (secenek) { //oy verme başlangıç

                        if (oy == 1) { // artı oy verme


                            //   secenekProvider.resimid=R.drawable.artioysecili;
                            if(sorunsalOylamaMi)
                            {
                                provider.artioysayisi=String.valueOf(Integer.parseInt(provider.artioysayisi)+1);
                                //artioyWeak.get().setImageResource(R.drawable.artioysecili);
                                provider.artimi = true;
                                provider.eksimi = false;

                            }
                            else
                            {

                                provider.artioysayisi= String.valueOf(Integer.parseInt(provider.artioysayisi)+1);

                                if(provider.eksimi)
                                    provider.eksioysayisi= String.valueOf(Integer.parseInt(provider.eksioysayisi)-1);

                                artioyWeak.get().setImageResource(R.drawable.artioysecili);
                                provider.artimi = true;
                                provider.eksimi = false;
                            }

                        //    entryAdapterWeakReference.get().notifyDataSetChanged();
                        }
                        else // eksi oy verme
                            {

                            //   secenekProvider.resimid=R.drawable.eksioysecili;

                            if(sorunsalOylamaMi)
                            {
                                provider.artioysayisi= String.valueOf(Integer.parseInt(provider.artioysayisi)-1);

                              //  eksioyWeak.get().setImageResource(R.drawable.eksioysecili);
                                provider.eksimi = true;
                                provider.artimi = false;
                            }
                            else
                            {

                                provider.eksioysayisi= String.valueOf(Integer.parseInt(provider.eksioysayisi)+1);

                                if(provider.artimi)
                                    provider.artioysayisi= String.valueOf(Integer.parseInt(provider.artioysayisi)-1);

                                eksioyWeak.get().setImageResource(R.drawable.eksioysecili);
                                provider.eksimi = true;
                                provider.artimi = false;
                            }

                        //    entryAdapterWeakReference.get().notifyDataSetChanged();
                        }

                    } // oy verme bitişi

                    else // oy geri alma başlangıç
                        {
                        if (oy == 1) {
                            //  secenekProvider.resimid=R.drawable.artioy;
                            if(sorunsalOylamaMi)
                            {

                               // artioyWeak.get().setImageResource(R.drawable.artioy);
                                provider.artioysayisi= String.valueOf(Integer.parseInt(provider.artioysayisi)-1);
                                provider.artimi = false;
                                provider.eksimi = false;
                            }
                            else
                            {
                                artioyWeak.get().setImageResource(R.drawable.artioy);
                                provider.artioysayisi= String.valueOf(Integer.parseInt(provider.artioysayisi)-1);
                                provider.artimi = false;
                                provider.eksimi = false;

                            }


                          //  entryAdapterWeakReference.get().notifyDataSetChanged();
                        } else {

                            // secenekProvider.resimid=R.drawable.eksioy;

                            if(sorunsalOylamaMi)
                            {
                                provider.artioysayisi= String.valueOf(Integer.parseInt(provider.artioysayisi)+1);
                              //  eksioyWeak.get().setImageResource(R.drawable.eksioy);
                                provider.artimi = false;
                                provider.eksimi = false;
                            }
                            else
                            {
                                eksioyWeak.get().setImageResource(R.drawable.eksioy);
                                provider.eksioysayisi= String.valueOf(Integer.parseInt(provider.eksioysayisi)-1);


                                provider.artimi = false;
                                provider.eksimi = false;
                            }



                          //  entryAdapterWeakReference.get().notifyDataSetChanged();
                        }


                    }
                //entryAdapterWeakReference.get().notifyDataSetChanged();
                entryAdapterWeakReference.get().notifyItemChanged(pozisyon);
                Toast.makeText(contextWeakReference.get(),oys,Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(contextWeakReference.get(),"Hata",Toast.LENGTH_SHORT).show();
            //   Log.d("islem sonucu",document.html());


        }

        private String jsonVeriAyikla(String html) {

            String jsonmesaj="Hata";

            try {
                JSONObject jsonveri=new JSONObject(html);
                jsonmesaj=jsonveri.getString("Message");



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonmesaj;
        }
    }

    private static class MesajSil extends AsyncTask<Void,Void,Void>
    {

        int pos;
        String url;
        String cookie;
        int islem;
        BadiEngelAdapter adapter;

        MesajSil(int pos,String url,int islem,String cookie,BadiEngelAdapter adapter)
        {
            this.pos=pos;
            this.cookie=cookie;
            this.url=url;
            this.islem=islem;
            this.adapter=adapter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Connection.Response response=null;

            if(islem==0)
            {
                    url="https://eksisozluk.com/message/deletethread/"+url;
            }
            else if(islem==1)
            {
                    url="https://eksisozluk.com/message/archivethread/"+url;
            }
            else
            {
                url="https://eksisozluk.com/message/deletearchive/"+url;
            }

            try {
                response=Jsoup.connect(url)
                        .cookie("Cookie",cookie)
                        .header("X-Requested-With","XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .execute();


                //Log.d("badiengeladapter",response.body());
                //Log.d("badiengeladapter", String.valueOf(response.statusCode()));

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.itemSil(pos);

        }
    }








}
