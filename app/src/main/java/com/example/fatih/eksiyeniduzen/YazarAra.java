package com.example.fatih.eksiyeniduzen;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Fatih on 27.02.2018.
 */

class YazarAra extends AsyncTask<Void,Void,String>
{

    String query;

    WeakReference<AutoCompleteTextView> autoCompleteTextViewWeakReference;
    WeakReference<Context> fragmentActivityWeakReference;

    YazarAra(String query,AutoCompleteTextView autoCompleteTextView,Context fragmentActivity)
    {
        this.query=query;

        autoCompleteTextViewWeakReference=new WeakReference<>(autoCompleteTextView);
        fragmentActivityWeakReference=new WeakReference<>(fragmentActivity);
    }

    @Override
    protected String doInBackground(Void... params) {

        String jsonveri="";

        try {
            Connection.Response response= Jsoup.connect("https://eksisozluk.com/autocomplete/nick?")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                    .header("X-Requested-With","XMLHttpRequest")
                    .data("term",query)
                    .header("Accept","application/json, text/javascript, */*; q=0.01")
                    .header("Accept-Language","tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute();


            //Log.d("ress",response.body());

            jsonveri=response.body();



        } catch (IOException e) {
            e.printStackTrace();
        }




        return jsonveri;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);

        ArrayList<String> list=new ArrayList<>();
        ArrayAdapter<String> adapter;

        try {
            JSONArray object=new JSONArray(json);

            for (int i=0;i<object.length();i++)
            {
                list.add(object.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter=new ArrayAdapter<>(fragmentActivityWeakReference.get(),R.layout.simpletext,list);

        autoCompleteTextViewWeakReference.get().setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }
}