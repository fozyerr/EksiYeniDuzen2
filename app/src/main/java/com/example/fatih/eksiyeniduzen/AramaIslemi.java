package com.example.fatih.eksiyeniduzen;

import android.content.Context;

import android.os.AsyncTask;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Fatih on 24.09.2017.
 */

 class AramaIslemi extends AsyncTask<Void,Void,String> {

    private String encode;
    //private AutoCompleteTextView autoCompleteTextView;
   // private Context context;

    private int tip;
    private String baslikadi;

    WeakReference<AutoCompleteTextView> autoCompleteTextViewWeakReference;
    WeakReference<Context> contextWeakReference;


   AramaIslemi (String encode, AutoCompleteTextView autoCompleteTextView, Context context,int tip,String baslikadi)
    {
        this.encode=encode;
      //  this.autoCompleteTextView=autoCompleteTextView;
       // this.context=context;
        this.tip=tip;
        this.baslikadi=baslikadi;
        this.autoCompleteTextViewWeakReference=new WeakReference<>(autoCompleteTextView);
        this.contextWeakReference=new WeakReference<>(context);

    }


    @Override
    protected String doInBackground(Void... params) {

        String jsonveri=" ";
        try {
            Connection.Response response=null;

            if(tip==1)
            {
                 response= Jsoup.connect("https://eksisozluk.com/autocomplete/query?")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                        .header("X-Requested-With","XMLHttpRequest")
                        .data("q",encode)
                        .header("Accept","application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Language","tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .method(Connection.Method.GET)
                        .ignoreContentType(true)
                        .execute();

            }
            else
            {
                response= Jsoup.connect("https://eksisozluk.com/autocomplete/nick?")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                        .header("X-Requested-With","XMLHttpRequest")
                        .data("term",encode)
                        .data("title",baslikadi)
                        .header("Accept","application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Language","tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .method(Connection.Method.GET)
                        .ignoreContentType(true)
                        .execute();
            }



            jsonveri=response.body();

           // jsonveri=response.body();


        } catch (IOException e) {
            e.printStackTrace();
        }


        return jsonveri;
    }
    @Override
    protected void onPostExecute(String jsonveri) {
        super.onPostExecute(jsonveri);

        ArrayList<String> list=new ArrayList<>();
        ArrayAdapter<String> adapter;


        if(tip==1)
        {
            try {
                JSONObject object=new JSONObject(jsonveri);
                JSONArray baslikjson=object.getJSONArray("Titles");
                JSONArray yazarjson=object.getJSONArray("Nicks");

                for (int i=0;i<baslikjson.length();i++)
                {
                    list.add(baslikjson.get(i).toString());
                }
                for (int i=0;i<yazarjson.length();i++)
                {
                    list.add("@"+yazarjson.get(i).toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                JSONArray object=new JSONArray(jsonveri);
                StringBuilder builder;

                for (int i=0;i<object.length();i++)
                {
                    builder=new StringBuilder();
                    builder.append("@").append(object.getString(i));
                    list.add(builder.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        AutoCompleteTextView atview=autoCompleteTextViewWeakReference.get();
        Context ctx=contextWeakReference.get();


        adapter=new ArrayAdapter<>(ctx,R.layout.simpletext,list);



        atview.setAdapter(adapter);


        adapter.notifyDataSetChanged();

    }



}
