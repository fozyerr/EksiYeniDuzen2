package com.example.fatih.eksiyeniduzen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Fatih on 10.11.2016.
 */
 class LinkBaglama extends ClickableSpan {


    private String gelenstring;
    private String gonder;
    private Context context;
    private String cookie;
    private String nick;


     LinkBaglama(String gonder, String gelenString, Context context,String cookie,String nick) {
        this.gonder = gonder;
        this.gelenstring=gelenString;
        this.context=context;
         this.cookie=cookie;
         this.nick=nick;
    }

    @Override
    public void onClick(View widget) {

      if(gonder.contains("http"))
       {
           Log.d("if","if çalışti");

           if(gonder.contains("https://eksisozluk.com/"))
           {
               Toast.makeText(context,"Tıklanan url zaten ekşi sözlüğe yönlendiriyor",Toast.LENGTH_SHORT).show();
           }


           Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(gonder));
           i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(i);
       }
   /*   else if(gonder.contains("/entry/"))
      {
         Intent tekentry=new Intent(context,EntryActivity.class);

          Log.d("LinkBaglama",gonder);
          tekentry.putExtra("link",gonder);
          tekentry.putExtra("cookie",cookie);
          tekentry.putExtra("nick",nick);
          tekentry.putExtra("gelenstring",gelenstring);
          tekentry.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(tekentry);


      }*/

        else
       {


          // Log.d("ELSEIF",gonder);
           String url = "https://eksisozluk.com" + gonder;

           String ayir=Uri.decode(gonder);

           if(ayir.contains("ara?"))
           {
               Intent arama=new Intent(context,AramaActivity.class);
               arama.putExtra("kelime",ayir);
               arama.putExtra("cookie",cookie);
               arama.putExtra("nick",nick);
               context.startActivity(arama);
           }
           else
           {
               Log.d("linkbağlama",gonder);

               Intent tekentry=new Intent(context,EntryActivity.class);
               tekentry.putExtra("link",gonder);
               tekentry.putExtra("cookie",cookie);
               tekentry.putExtra("nick",nick);
               tekentry.putExtra("gelenstring",gelenstring);
               tekentry.putExtra("urlduzenle",true);
               tekentry.putExtra("mark"," ");
               tekentry.putExtra("arsiv",false);
               tekentry.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(tekentry);
           }





         //  new BasligaGit(url).execute();

         /*  tumu.putExtra("sayfasayi",sayi);
           tumu.putExtra("link", gonder);
           tumu.putExtra("baslik", baslikgonder);
           tumu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(tumu);*/
          // Toast.makeText(context,"link = https://eksisozluk.com"+gonder,Toast.LENGTH_SHORT).show();

       }



    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

     if(gonder.contains("http"))
       {

           TypedValue typedValue = new TypedValue();
           Resources.Theme theme = context.getTheme();
           theme.resolveAttribute(R.attr.httpText, typedValue, true);
           @ColorInt int color = typedValue.data;

        //   ds.setColor(Color.parseColor("#72ABBE"));
           ds.setColor(color);
       }

        else
     {

         TypedValue typedValue = new TypedValue();
         Resources.Theme theme = context.getTheme();
         theme.resolveAttribute(R.attr.spanText, typedValue, true);
         @ColorInt int color = typedValue.data;
     //    ds.setColor(Color.parseColor("#137CB4"));
         ds.setColor(color);
         ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
         ds.setUnderlineText(false);

     }


    }

   private class BasligaGit extends AsyncTask<Void,Void,Void> {



        //String baslikgonder;
      //  int sayi=1;
        String yenilink;
        String urrl;
        ProgressDialog alert;
        BasligaGit (String urrl)
        {

            this.urrl=urrl;


        }


        @Override
        protected Void doInBackground(Void... params) {



            try {
                Document doc = Jsoup.connect(urrl)
                        .followRedirects(true)
                        .get();



                Elements link =doc.select("h1 > a");
                yenilink=link.attr("href");




                String ayir=Uri.decode(gonder);

                Log.d("ayir",ayir);

                if(ayir.contains("@"))
                {
                    ayir=ayir.split("@")[1].trim();
                    yenilink=yenilink+"?a=search&author="+ayir;
                   // yenilink="/yaran-basliklar--109835?a=search&author=kingoflions";
                 //   Log.d("URET",);
                }
                else if(ayir.contains("ara?"))
                {

                    yenilink=ayir;
                }




            } catch (IOException e) {

                e.printStackTrace();




            }





            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            alert=new ProgressDialog(context);
            alert.setMessage("Biraz bekleticem");
            alert.setCancelable(false);
            alert.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            alert.show();



        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            alert.dismiss();
            Intent tum=new Intent(context,EntryActivity.class);
            tum.putExtra("cookie",cookie);
            tum.putExtra("nick",nick);
         //   tum.putExtra("cookie",getArguments().getString("cookie"));

            //    Log.d("sayi",String.valueOf(sayi));
            //     Log.d("sayi",yenilink);
            //   Log.d("sayi",baslikgonder);

            if(yenilink==null)
            {
                Log.d("HATA", "burası boş kanki");


                Log.d("baglama",gelenstring);

                if(gelenstring.contains("="))
                gelenstring=gelenstring.split("=")[1].replaceAll("\\+"," ");


                    Toast.makeText(context,"bura boş:  "+gelenstring, Toast.LENGTH_LONG).show();

            }
            else if(yenilink.contains("ara?"))
            {
                Intent arama=new Intent(context,AramaActivity.class);
                arama.putExtra("kelime",yenilink);
                arama.putExtra("cookie",cookie);
                arama.putExtra("nick",nick);
                context.startActivity(arama);
            }
            else {
                tum.putExtra("link", yenilink);
                tum.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(tum);
            }




        }


    }




/*    public class LinkDuzelt extends AsyncTask<Void,Void,Void>
    {
        Context context;
        String baslikgonder;
        int sayi=1;
        String yenilink;
        String urrl;
ProgressDialog alert;
        LinkDuzelt (Context context,String urrl)
        {

            this.urrl=urrl;
            this.context=context;

        }

        @Override
        protected Void doInBackground(Void... params) {




            try {
                Document doc = Jsoup.connect(urrl).get();

                Elements link =doc.select("h1 > a");
                yenilink="https://eksisozluk.com"+link.attr("href");
              //  Log.d("www",yenilink);
                baslikgonder=link.text();
              String sayfalar= doc.select("div.pager").attr("data-pagecount");

               if(!sayfalar.equals(""))
               {

                   sayi=Integer.parseInt(sayfalar);


               }

            } catch (IOException e) {

                e.printStackTrace();




            }

                return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            alert=new ProgressDialog(context);
            alert.setMessage("Biraz bekleticem");
            alert.setCancelable(false);
            alert.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            alert.show();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


alert.dismiss();
            Intent tum=new Intent(context,TumunuGoruntule.class);

        //    Log.d("sayi",String.valueOf(sayi));
       //     Log.d("sayi",yenilink);
         //   Log.d("sayi",baslikgonder);

            if(yenilink==null)
            {
                Log.d("HATA","burası boş kanki");
                Toast.makeText(context,"Burası boş zamki, Uygulama hata verdi ama sen görmüyon xd",Toast.LENGTH_SHORT).show();

            }
            else {
                tum.putExtra("sayfasayi", sayi);
                tum.putExtra("link", yenilink);
                tum.putExtra("baslik", baslikgonder);
                tum.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(tum);
            }


        }


    }*/



}
