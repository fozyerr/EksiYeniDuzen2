package com.example.fatih.eksiyeniduzen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 25.02.2018.
 */

public class FragmentMesajTablar extends Fragment implements BadiEngelFragHaberlesme {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    BadiEngelAdapter adapter;
    List<BadiEngelProvider> list;
    ProgressBar mesajspin;

    public static FragmentMesajTablar newInstance(String cookie,String nick,int position) {
        FragmentMesajTablar fragment = new FragmentMesajTablar();
        Bundle args = new Bundle();
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        args.putInt("pos",position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_badiengel, container, false);
        list=new ArrayList<>();
        recyclerView=  rootView.findViewById(R.id.recycleBadiengel);
        recyclerView.setHasFixedSize(true);
        manager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        // manager=new LinearLayoutManagerWithSmoothScroller(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter=new BadiEngelAdapter(list,getActivity(),getArguments().getString("cookie"),getArguments().getString("nick"));
        recyclerView.setAdapter(adapter);
        mesajspin=rootView.findViewById(R.id.fragspin);
        mesajspin.setVisibility(View.VISIBLE);

        /*
        Divider a renk verme kodu

          DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.divider));
         */

       // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        if(getArguments().getInt("pos")==0)
        {

            new Mesajlar(getArguments().getString("cookie"),false,FragmentMesajTablar.this,mesajspin).execute();
        }
        else
        {
            new Mesajlar(getArguments().getString("cookie"),true,FragmentMesajTablar.this,mesajspin).execute();
        }

        return rootView;
    }

    @Override
    public void listeyegerialmaislemi(String gerialmaurl, BadiEngelProvider silinenitem, int pos) {

    }

    @Override
    public void mesajlistkur(List<BadiEngelProvider> mesajlar) {

        list.addAll(mesajlar);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void requestverifykur(String requestverify) {

    }

    @Override
    public void gonderilenMesajiRecycleEkle(BadiEngelProvider gonderilenmesaj) {

    }

    @Override
    public void sorunsalKur(List<BadiEngelProvider> sorunsallar, boolean devam) {

    }

    private static class Mesajlar extends AsyncTask<Void,Void,List<BadiEngelProvider>>
    {


        String cookie;
        boolean arsivmi;

        WeakReference<BadiEngelFragHaberlesme> badiEngelFragHaberlesmeWeakReference;
        WeakReference<ProgressBar> progressBarWeakReference;


        Mesajlar(String cookie,boolean arsivmi,BadiEngelFragHaberlesme badiEngelFragHaberlesme,ProgressBar progressBar)
        {
            this.cookie=cookie;
            this.arsivmi=arsivmi;
            badiEngelFragHaberlesmeWeakReference=new WeakReference<>(badiEngelFragHaberlesme);
            progressBarWeakReference=new WeakReference<>(progressBar);

        }



        @Override
        protected List<BadiEngelProvider> doInBackground(Void... params) {

            List<BadiEngelProvider> badiEngelProvider=new ArrayList<>();
            Document document=null;

            String url;

            if(arsivmi)
            {
                url="https://eksisozluk.com/mesaj/arsiv";
            }
            else
                url="https://eksisozluk.com/mesaj";

            try {
                document = Jsoup.connect(url)
                        .cookie("Cookie", cookie)
                        .timeout(10*1000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document!=null)
            {
                Elements mesajlar=document.select("ul#threads > li > article");

                BadiEngelProvider pr;
                StringBuilder mesajatan;
                String mesajsayi;
                String mesajicerik;
                String mesajtarih;
                String silarsivleurl;
                String mesajurl;
                String requestverify="";
                String mesajAtilacakYazar="";

                if(document.select("form#message-send-form > input").first()!=null)
                requestverify=document.select("form#message-send-form > input").first().attr("value");

                for(Element mesaj:mesajlar)
                {

                    mesajsayi=mesaj.select("small").text();
                    mesaj.select("small").remove();
                    mesajatan=new StringBuilder();
                    mesajAtilacakYazar=mesaj.select("h2").text();
                    mesajatan.append(mesaj.select("h2").text()).append(" (").append(mesajsayi).append(")");
                    mesajicerik=mesaj.select("p").text();
                    mesajtarih=mesaj.select("time").text();
                    silarsivleurl=mesaj.select("input").attr("value");
                    mesajurl=mesaj.select("a").attr("href");



                    pr=new BadiEngelProvider(mesajatan.toString(),mesajicerik,mesajtarih,silarsivleurl,mesajurl,arsivmi,requestverify,mesajAtilacakYazar,5);

                    badiEngelProvider.add(pr);

                }

            }



            return badiEngelProvider;
        }

        @Override
        protected void onPostExecute(List<BadiEngelProvider> badiEngelProvider) {
            super.onPostExecute(badiEngelProvider);

            if(progressBarWeakReference.get()!=null)
                progressBarWeakReference.get().setVisibility(View.GONE);

            if(badiEngelProvider!=null)
            {
                //  List<BadiEngelProvider> liste=listWeakReference.get();

                if(badiEngelFragHaberlesmeWeakReference.get()!=null)
                    badiEngelFragHaberlesmeWeakReference.get().mesajlistkur(badiEngelProvider);

            }


        }
    }
}
