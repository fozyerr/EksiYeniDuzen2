package com.example.fatih.eksiyeniduzen;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServisArsiv extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
   // private static final String ACTION_FOO = "com.example.fatih.eksiyeniduzen.action.FOO";
  //  private static final String ACTION_BAZ = "com.example.fatih.eksiyeniduzen.action.BAZ";

    // TODO: Rename parameters
    private String sayfaUrl,ek;
    private int dongu_sayisi;
    private String cookie;

    public ServisArsiv() {
        super("ServisArsiv");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification notification =
                    new Notification.Builder(this)
                            .setContentTitle("Arşivleme işlemi")
                            .setContentText("İndiriliyor")
                            .setSmallIcon(R.drawable.arsiv)
                            .setContentIntent(pendingIntent)
                            .setTicker("ticker texti")
                            .build();

            startForeground(101, notification);

            sayfaUrl=intent.getExtras().getString("url");
            dongu_sayisi=intent.getExtras().getInt("dongu");
            cookie=intent.getExtras().getString("cookie");
            ek=intent.getExtras().getString("ek");

            StringBuilder url;
            for(int i=1;i<=dongu_sayisi;i++)
            {
              url=new StringBuilder();

                url.append("https://eksisozluk.com").append(sayfaUrl).append(ek).append(i);

                dokumanindir(url.toString());
            }

            stopForeground(true);
            stopSelf();

        }
    }

    private void dokumanindir(String url){

        try {
            Connection.Response response= Jsoup.connect(url)
                    .cookie("Cookie",cookie)
                    .method(Connection.Method.GET)
                    .execute();

            Document document=Jsoup.parse(response.body());

          //  Log.d("res",document.select("div.pager").attr("data-currentpage")+document.select("h1").attr("data-title"));


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
