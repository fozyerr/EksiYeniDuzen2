package com.example.fatih.eksiyeniduzen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class ServisArsivleme extends Service {


    private String sayfaUrl,ek;
    private int dongu_sayisi;
    private String cookie;
    private String yazarnick;
    private String basliktext;
    private String baslikid;
    NotificationManager notificationManager;
 //   Notification notification;
    Notification.Builder builder;
    VeriTabaniBaglanti veriTabani;
    private static final String kod="com.example.fatih.eksiyeniduzen.ServisArsivleme.DUR";
    int artis;


   boolean indirmeyidurdur;

    public ServisArsivleme() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        indirmeyidurdur=false;
        baslikid="";
        basliktext=intent.getExtras().getString("baslik");
        veriTabani=new VeriTabaniBaglanti(this);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        //Intent bros=new Intent(this,Bro.class);

        Intent intent2=new Intent();
        intent2.setAction(kod);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);




        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);



        artis=0;



        //PendingIntent pendingIntent1=PendingIntent.getBroadcast(this,0,bros,0);


        Notification.Action action = new Notification.Action.Builder(R.drawable.kapat,"Kapat",pIntent).build();





        notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         builder =
                new Notification.Builder(this)
                        .setContentTitle(basliktext)
                        .setContentText("İndiriliyor")
                        .setSmallIcon(R.drawable.arsiv)
                        .addAction(action)
                        .setContentIntent(pendingIntent)
                        .setTicker("ticker texti");

        startForeground(101, builder.build());

        sayfaUrl=intent.getExtras().getString("url");
        dongu_sayisi=intent.getExtras().getInt("dongu");
        cookie=intent.getExtras().getString("cookie");
        ek=intent.getExtras().getString("ek");
        yazarnick=intent.getExtras().getString("nick");
        baslikid=intent.getExtras().getString("baslikid");

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(kod); //further more
        registerReceiver(receiver, filter2);


        /*

        String sayfaUrl;
        String ek;
        String cookie;
        String yazarnick;
        int artis;
         */
        new Arsivleme(notificationManager,builder,veriTabani,ServisArsivleme.this,basliktext,baslikid,dongu_sayisi,sayfaUrl,ek,cookie,yazarnick,artis).execute();




        return START_NOT_STICKY;
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(kod)){
                indirmeyidurdur = true;  // this is a class variable

            }

        }
    };


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private static class Arsivleme extends AsyncTask<Void,Integer,Void>
    {


        WeakReference<NotificationManager> notificationManagerWeakReference;
        WeakReference<Notification.Builder> builderWeakReference;
        WeakReference<VeriTabaniBaglanti> veriTabaniBaglantiWeakReference;
        WeakReference<ServisArsivleme> servisArsivlemeWeakReference;
        String basliktext;
        String baslikid;
        int dongu_sayisi;

        String sayfaUrl;
        String ek;
        String cookie;
        String yazarnick;
        int artis;

        Arsivleme(NotificationManager notificationManager,Notification.Builder builder,VeriTabaniBaglanti veriTabaniBaglanti,ServisArsivleme servisArsivleme,
                  String basliktext,String baslikid,int dongu_sayisi,String sayfaUrl,String ek,String cookie,String yazarnick,int artis)
        {
            notificationManagerWeakReference=new WeakReference<NotificationManager>(notificationManager);
            builderWeakReference=new WeakReference<Notification.Builder>(builder);
            veriTabaniBaglantiWeakReference=new WeakReference<VeriTabaniBaglanti>(veriTabaniBaglanti);
            servisArsivlemeWeakReference=new WeakReference<ServisArsivleme>(servisArsivleme);
            this.basliktext=basliktext;
            this.baslikid=baslikid;
            this.dongu_sayisi=dongu_sayisi;

            this.sayfaUrl=sayfaUrl;
            this.ek=ek;
            this.cookie=cookie;
            this.yazarnick=yazarnick;
            this.artis=artis;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

           // builder.setProgress(100,values[0],false);
          //  notificationManager.notify(101,builder.build());

            builderWeakReference.get().setProgress(100,values[0],false);
            notificationManagerWeakReference.get().notify(101, builderWeakReference.get().build());

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   builder.setProgress(100,0,false);
         //   notificationManager.notify(101,builder.build());
          //  veriTabani.veritabaniAc();

            builderWeakReference.get().setProgress(100,0,false);
            notificationManagerWeakReference.get().notify(101,builderWeakReference.get().build());
            veriTabaniBaglantiWeakReference.get().veritabaniAc();

        }

        @Override
        protected Void doInBackground(Void... params) {

            BaslikProvider baslik;
            ArsivEntry arsivEntry;
            StringBuilder url;
            int sayfano;
            int toplamentrysayisi=0;

            /*


            HTML STRINGINI BYTE ARRAYA ÇEVİRİP ÖYLE KAYIT TUTMAYI DENE,

            BYTE ARRAY SQLITE İÇİNDE BLOB TİPİNDE TUTULUYOR

            KAYITLI SİTELERDEN ÖRNEKLERE BAK

             */

            VeriTabaniBaglanti veriTabani=veriTabaniBaglantiWeakReference.get();
            ServisArsivleme servisArsivleme= servisArsivlemeWeakReference.get();

            if(!veriTabani.baslikVarMi(baslikid))
            {
                baslik=new BaslikProvider("0",basliktext,baslikid,0,false);

                veriTabani.baslikekle(baslik);
            }




            for(int i=1;i<=dongu_sayisi;i++)
            {

                if(servisArsivleme.indirmeyidurdur)
                {

                    veriTabani.veritabaniKapat();
                      //  ServisArsivleme.this.stopForeground(true);
                       // ServisArsivleme.this.stopSelf();
                    servisArsivleme.stopForeground(true);
                    servisArsivleme.stopSelf();
                        this.cancel(true);


                }


                if(isCancelled())
                {

                    Log.d("kesme","asynctaska kesme atıldı işlem duracak");
                    break;
                }
                else
                {
                    veriTabani.transactionBaslat();

                    //  notificationManager.notify(101,notification);


                    url=new StringBuilder();

                    if(i==1)
                    url.append("https://eksisozluk.com").append(sayfaUrl);
                    else
                        url.append("https://eksisozluk.com").append(sayfaUrl).append(ek).append(i);

                     Log.d("kaydedilen url",url.toString());

                    try {
                        Connection.Response response= Jsoup.connect(url.toString())
                                .cookie("Cookie",cookie)
                                .method(Connection.Method.GET)
                                .execute();

                        Document document=Jsoup.parse(response.body());
                        //  String entrysayi= String.valueOf(document.select("div.content").size());



                        String entrydokumaniazaltilmis=document.select("div#content").html();
                        basliktext=document.select("h1").attr("data-title");

                        toplamentrysayisi=toplamentrysayisi+document.select("div.content").size();

                        if(document.select("div.pager").isEmpty())
                        {
                            sayfano= 1;
                        }
                        else
                        {
                            sayfano= Integer.parseInt(document.select("div.pager").attr("data-currentpage"));
                        }

                       // EntryleriAyikla entryleriAyikla=new EntryleriAyikla();

                        EntryProvider[] entryProvider=EntryleriAyikla.ArsivIcınAyikla(document,servisArsivlemeWeakReference.get(),false,cookie,yazarnick);

                        for(EntryProvider p:entryProvider)
                        {
                            veriTabani.entryEkle(p);
                        }




                        artis=(100*i)/dongu_sayisi;
                        publishProgress(artis);


                        veriTabani.baslikguncelle(baslikid,document.select("div.content").size());

                        Log.d("servisiçi", String.valueOf(document.select("div.content").size()));

                        veriTabani.transactionBitir();




                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }


            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
           // veriTabani.veritabaniKapat();
            Log.d("durması lazım","durrrrrr orda durr");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           veriTabaniBaglantiWeakReference.get().veritabaniKapat();
            servisArsivlemeWeakReference.get().stopForeground(true);
            servisArsivlemeWeakReference.get().stopSelf();
        }
    }

}
