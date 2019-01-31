package com.example.fatih.eksiyeniduzen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Fatih on 26.02.2018.
 */

public class MesajGonder extends AsyncTask<Void,Void,Integer> {

    String cookie;
    String verify;
    String kime;
    String mesajicerik;
    String mesajAtanNick;

    boolean birebirmesajlasma;

    WeakReference<Context> contextWeakReference;
    WeakReference<AlertDialog> alertDialogWeakReference;
    WeakReference<ProgressBar> progressBarWeakReference;
    BadiEngelFragHaberlesme badiEngelFragHaberlesme;

    public MesajGonder(String cookie, String verify, String kime, String mesajicerik,String mesajAtanNick,boolean birebirmesajlasma,Context context,AlertDialog alertDialog,ProgressBar progressBar) {
        this.cookie = cookie;
        this.verify = verify;
        this.kime = kime;
        this.mesajAtanNick=mesajAtanNick;
        this.mesajicerik = mesajicerik;
        this.birebirmesajlasma=birebirmesajlasma;
        contextWeakReference=new WeakReference<>(context);
        alertDialogWeakReference=new WeakReference<>(alertDialog);
        progressBarWeakReference=new WeakReference<>(progressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressBarWeakReference.get()!=null)
            progressBarWeakReference.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        Connection.Response document=null;
        int status=0;

        try {
            Log.d("kime",verify);



            document= Jsoup.connect("https://eksisozluk.com/mesaj/sendajax")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0")
                    .cookie("Cookie",cookie)
                    .header("X-Requested-With","XMLHttpRequest")
                    .data("__RequestVerificationToken",verify)
                    .data("Message",mesajicerik)
                    .data("To",kime)
                    .timeout(10*1000)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .execute();

            //EntryleriAyikla ayikla=new EntryleriAyikla();

            if(document!=null)
            {
                status=document.statusCode();
                Log.d("mesatgonde",document.body());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


        return status;
    }



    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);

        if(progressBarWeakReference.get()!=null)
        {
            progressBarWeakReference.get().setVisibility(View.GONE);
        }

        if(contextWeakReference.get()!=null)
        {



            if(s==500)
            {
                Toast.makeText(contextWeakReference.get(),"bu yazara mesaj gönderemezsin",Toast.LENGTH_SHORT).show();
            }
            else if(s==200)
            {
                if(alertDialogWeakReference.get()!=null)
                    alertDialogWeakReference.get().dismiss();

                if(birebirmesajlasma)
                {
                    badiEngelFragHaberlesme= (BadiEngelFragHaberlesme) contextWeakReference.get();

                    if(contextWeakReference.get() instanceof FavlayanlarActivity)
                        Log.d("MesajGonderFavlayanAct","FAvlayanlar activity contextiymiş");

                    StringBuilder stringBuilder=new StringBuilder();
                    StringBuilder mesajtarih=new StringBuilder();

                    stringBuilder.append(mesajAtanNick).append(" -> ").append(kime).append(": ").append(mesajicerik);



                    Calendar calander = Calendar.getInstance();
                    String gun;

                    if(calander.get(Calendar.DAY_OF_MONTH)<=9)
                        gun="0"+calander.get(Calendar.DAY_OF_MONTH);
                    else
                        gun= String.valueOf(calander.get(Calendar.DAY_OF_MONTH));

                    mesajtarih.append(gun).append(".").append(calander.get(Calendar.MONTH) + 1).append(".").append(calander.get(Calendar.YEAR))
                            .append(" ").append(calander.get(Calendar.HOUR_OF_DAY)).append(":").append(calander.get(Calendar.MINUTE));


                    BadiEngelProvider provider=new BadiEngelProvider(new SpannableString(stringBuilder.toString()),mesajtarih.toString(),false,7);
                    badiEngelFragHaberlesme.gonderilenMesajiRecycleEkle(provider);
                }
                else
                {
                    if(contextWeakReference.get() instanceof EntryActivity)
                        Log.d("MesajGonderEntryAct","entry activity contextiymiş");

                }

                Toast.makeText(contextWeakReference.get(),"mesaj gönderildi",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(contextWeakReference.get(),"hata",Toast.LENGTH_SHORT).show();
        }
    }
}
