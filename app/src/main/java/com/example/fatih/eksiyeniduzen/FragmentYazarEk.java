package com.example.fatih.eksiyeniduzen;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Fatih on 28.09.2017.
 */

public class FragmentYazarEk extends Fragment {

    ListView listView;
    YazarEkAdapter adapter;

    RelativeLayout progressBar;
   // String cookie;

    public static FragmentYazarEk newInstance(int sectionNumber,String yazarnick,int tip,String cookie,String nick) {
        FragmentYazarEk fragment = new FragmentYazarEk();
        Bundle args = new Bundle();
        args.putInt("KEY", sectionNumber);
        args.putString("yazarnick",yazarnick);
        args.putInt("tip",tip);
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_yazar, container, false);

        progressBar=  rootView.findViewById(R.id.yuklenmespin);
        listView=rootView.findViewById(R.id.listView2);
        adapter=new YazarEkAdapter(getActivity(),R.layout.fragment_yazar_eksik_kisim,getArguments().getString("cookie"),getArguments().getString("nick"));
        listView.setAdapter(adapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        listView.setNestedScrollingEnabled(true);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });


        if(getArguments().getInt("tip")==0)
        {
            String url="https://eksisozluk.com/favori-yazarlari?nick="+getArguments().getString("yazarnick")+"&p=NaN";

            Log.d("favyazarlar çekiliyor",url);

            new YazarEkSayfalar(url,0,getArguments().getString("cookie"),progressBar,listView,adapter).execute();


        }
        else
        {
            String url="https://eksisozluk.com/katkida-bulundugu-kanallar?nick="+getArguments().getString("yazarnick")+"&p=NaN";

            new YazarEkSayfalar(url,1,getArguments().getString("cookie"),progressBar,listView,adapter).execute();
        }



        return rootView;
    }


   static class YazarEkSayfalar extends AsyncTask<Void,Void,YazarEkProvider[]>
    {

        String url;
        int tip;


        WeakReference<RelativeLayout> relativeLayoutWeakReference;
        WeakReference<ListView> listViewWeakReference;
        WeakReference<YazarEkAdapter> yazarEkAdapterWeakReference;

        String cookie;

        YazarEkSayfalar(String url,int tip,String cookie,RelativeLayout relativeLayout,ListView listView,YazarEkAdapter yazarEkAdapter)
        {
            this.url=url;
            this.tip=tip;
            this.cookie=cookie;

            relativeLayoutWeakReference=new WeakReference<RelativeLayout>(relativeLayout);
            listViewWeakReference=new WeakReference<ListView>(listView);
            yazarEkAdapterWeakReference=new WeakReference<YazarEkAdapter>(yazarEkAdapter);


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relativeLayoutWeakReference.get().setVisibility(View.VISIBLE);
        }

        @Override
        protected YazarEkProvider[] doInBackground(Void... params) {

            YazarEkProvider[] yazarEkProvider=null;
            Document document;

            try {
                document= Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                        .header("Accept","*/*")
                        .cookie("Cookie",cookie)
                        .header("X-Requested-With","XMLHttpRequest")
                        .timeout(10*1000)
                        .get();

                Elements satirlar=document.select("table.striped > tbody > tr ");

                yazarEkProvider=new YazarEkProvider[satirlar.size()];

                int i=0;

                for(Element sutunlar:satirlar)
                {

                    if(tip==0)
                    {
                        String soltext,solurl,sagtext,sagurl;

                        soltext=sutunlar.select("td").first().select("a").text();
                        solurl=sutunlar.select("td").first().select("a").attr("href");
                        sagtext=sutunlar.select("td").last().select("a").text();
                        if(!sagtext.contains("b"))
                        {
                            sagurl=sutunlar.select("td").last().select("a").attr("data-add-url");
                        }
                        else
                        {
                            sagurl=sutunlar.select("td").last().select("a").attr("data-remove-url");
                        }

                        yazarEkProvider[i]=new YazarEkProvider(soltext,sagtext,solurl,sagurl,0);

                        i++;
                    }
                    else
                    {
                        String soltext,solurl,sagtext;

                        soltext=sutunlar.select("td > a").text();
                        solurl=sutunlar.select("td > a").attr("href");
                        sagtext=sutunlar.select("td > span").attr("title");

                        yazarEkProvider[i]=new YazarEkProvider(soltext,sagtext,solurl,"",1);

                        i++;
                    }


                }




            } catch (IOException e) {
                e.printStackTrace();
            }


            return yazarEkProvider;
        }

        @Override
        protected void onPostExecute(YazarEkProvider[] yazarEkProviders) {
            super.onPostExecute(yazarEkProviders);
            relativeLayoutWeakReference.get().setVisibility(View.GONE);

            if(yazarEkProviders!=null)
            {
              /*  for(int i=0;i<yazarEkProviders.length;i++)
                {
                    yazarEkAdapterWeakReference.get().add(yazarEkProviders[i]);
                }*/

              for(YazarEkProvider yazarEkProvider:yazarEkProviders)
              {
                  yazarEkAdapterWeakReference.get().add(yazarEkProvider);
              }

                yazarEkAdapterWeakReference.get().notifyDataSetChanged();
                listViewWeakReference.get().setSelectionAfterHeaderView();

            }


        }
    }

}
