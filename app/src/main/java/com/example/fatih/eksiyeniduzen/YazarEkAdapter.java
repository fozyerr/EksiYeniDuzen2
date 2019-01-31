package com.example.fatih.eksiyeniduzen;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 28.09.2017.
 */

 class YazarEkAdapter extends ArrayAdapter<YazarEkProvider> {

  private List<YazarEkProvider> yazarEkProvider;
    Context context;
    String cookie;
    String nick;


     YazarEkAdapter(Context context, int resource,String cookie,String nick) {
         super(context, resource);

         yazarEkProvider=new ArrayList<>();
         this.context=context;
         this.cookie=cookie;
         this.nick=nick;
    }


    @Override
    public void add(YazarEkProvider object) {

        yazarEkProvider.add(object);

        super.add(object);
    }

    @Override
    public int getCount() {
        return yazarEkProvider.size();
    }

    @Override
    public YazarEkProvider getItem(int position) {
        return yazarEkProvider.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        YazarEkProvider provider=getItem(position);


        YazarekViewHolder viewHolder;


        if(convertView==null)
        {

            LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new YazarekViewHolder();
            convertView = inflator.inflate(R.layout.fragment_yazar_eksik_kisim, parent, false);

            viewHolder.badibuton=  convertView.findViewById(R.id.badibuton);
            viewHolder.soltaraf=  convertView.findViewById(R.id.soltaraf);
            viewHolder.sagtaraf=  convertView.findViewById(R.id.sagtaraf);

            convertView.setTag(viewHolder);

        }
        else
        {

            viewHolder= (YazarekViewHolder) convertView.getTag();

        }

      if(provider!=null)
      {
          if(provider.tip==0)
          {
              viewHolder.soltaraf.setText(provider.soltext);
              soltextclick(viewHolder.soltaraf,provider);
              sagbutonclcik(viewHolder.badibuton,provider);
          }
          else
          {

              viewHolder.soltaraf.setText(provider.soltext);
              viewHolder.sagtaraf.setText(provider.sagtext);
              viewHolder.badibuton.setVisibility(View.GONE);
              viewHolder.sagtaraf.setVisibility(View.VISIBLE);
              soltextclick(viewHolder.soltaraf,provider);


          }
      }




        return convertView;
    }

    private void sagbutonclcik(final ImageButton sagtaraf, final YazarEkProvider provider) {

        if(provider.sagtext.contains("takip"))
        {
            sagtaraf.setImageResource(R.drawable.badiekle);
        }
        else
            sagtaraf.setImageResource(R.drawable.badicikar);

        sagtaraf.setVisibility(View.VISIBLE);


        sagtaraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation dondur;

                if(provider.sagtext.contains("takip"))
                dondur= AnimationUtils.loadAnimation(context,R.anim.tamdondur);
                else
                dondur=AnimationUtils.loadAnimation(context,R.anim.tamgeridondur);

                sagtaraf.startAnimation(dondur);
                //Log.d("badibuton",provider.sagtext+"   "+ provider.sagurl);
                new SagButonBadiEkle(provider,cookie,context,YazarEkAdapter.this).execute();

            }
        });



    }


    private void soltextclick(TextView soltaraf, final YazarEkProvider provider) {


        soltaraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Log.d("favoriyazarlar",provider.soltext+"  "+provider.solurl);

                if(provider.tip==1)
                {
                    Log.d("kanallar",provider.sagtext);


                    Intent aramayagec=new Intent(context,AramaActivity.class);
                   // aramayagec.putExtra("fragment",R.id.ara);
                    aramayagec.putExtra("kelime",provider.solurl);
                    aramayagec.putExtra("cookie",cookie);
                    aramayagec.putExtra("nick",nick);
                   context.startActivity(aramayagec);


                }
                else
                {
                    Intent yazara=new Intent(context,YazarActivity.class);
                    yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    yazara.putExtra("yazarnick",provider.soltext);
                    yazara.putExtra("cookie",cookie);
                    yazara.putExtra("nick",nick);


                    Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                    //startActivity(intent, bundle);
                    context.startActivity(yazara,bundle);
                }


            }
        });

    }


    private static class SagButonBadiEkle extends AsyncTask<Void,Void,Document>
    {

         private ProgressDialog dialog;
         private YazarEkProvider yazarEkProvider;

         String cookie;

         WeakReference<Context> contextWeakReference;
         WeakReference<YazarEkAdapter> yazarEkAdapterWeakReference;


        SagButonBadiEkle(YazarEkProvider yazarEkProvider,String cookie,Context context,YazarEkAdapter yazarEkAdapter)
        {
            this.yazarEkProvider=yazarEkProvider;
            this.cookie=cookie;

            contextWeakReference=new WeakReference<Context>(context);
            yazarEkAdapterWeakReference=new WeakReference<YazarEkAdapter>(yazarEkAdapter);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(contextWeakReference.get());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if(yazarEkProvider.sagtext.contains("takip"))
            dialog.setMessage("badi ekleniyor ...");
            else
            dialog.setMessage("badilerden çıkarılıyor ...");
            dialog.show();
        }

        @Override
        protected Document doInBackground(Void... params) {
            Document document=null;

            try {
                String tamurl="https://eksisozluk.com"+yazarEkProvider.sagurl;

                Log.d("tamurl",tamurl);

                document  = Jsoup.connect(tamurl)
                        .userAgent("Mozilla/5.0 ( compatible )")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .cookie("Cookie",cookie)
                        .header("Connection", "keep-alive")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .ignoreContentType(true)
                        .post();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            if(dialog.isShowing())
                dialog.dismiss();

            if(document!=null)
            {
              if(yazarEkProvider.sagtext.contains("takip"))
              {

                  yazarEkProvider.sagurl=yazarEkProvider.sagurl.replace("Add","Remove");
                  yazarEkProvider.sagtext="takibi bırak";
                 yazarEkAdapterWeakReference.get().notifyDataSetChanged();

              }
              else
              {
                  yazarEkProvider.sagurl=yazarEkProvider.sagurl.replace("Remove","Add");
                  yazarEkProvider.sagtext="takip et";
                  yazarEkAdapterWeakReference.get().notifyDataSetChanged();
              }


            }
            else
                Toast.makeText(contextWeakReference.get(),"bir şeyler yanlış gitti",Toast.LENGTH_SHORT).show();

        }
    }

    private static class YazarekViewHolder
   {
       TextView soltaraf;
       TextView sagtaraf;
       ImageButton badibuton;
   }


}
