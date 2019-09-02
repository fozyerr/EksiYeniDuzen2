package com.example.fatih.eksiyeniduzen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by Fatih on 16.09.2017.
 */

public class BasliklarFragment extends Fragment implements BaslikFragHaberlesme{


    ListView listView;
    BaslikAdapter adapter;
    ProgressBar progressBarLeakSebebiMi;
    ImageButton reload;

   // FloatingActionButton aramaMain;

    SwipeRefreshLayout swipeRefreshLayout;
    String aramaActivityURL;
    boolean activitydenmigelme;


    private final String tarihtebugunTemelUrl="https://eksisozluk.com/basliklar/tarihte-bugun?";

    boolean loading;

    private final String KEY = "section_number";

    String[] dizi;

    int sayfa;
    int yil;



    public BasliklarFragment()
    {

    }

    public static BasliklarFragment newInstance(int sectionNumber,String cookie,String aramaurl,String nick) {
        BasliklarFragment fragment = new BasliklarFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        args.putString("cookie",cookie);
        args.putString("aramaurl",aramaurl);
        args.putString("nick",nick);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basliklar, container, false);

     //   Log.d("bsgoncreate","fragment ne zaman oluştu");

        progressBarLeakSebebiMi= rootView.findViewById(R.id.anasayfaspin);

        activitydenmigelme=false;

        if(!getArguments().getString("cookie").equals("BOS"))
        {
            diziyitekrartanimla();
        }
        else
        {
            diziTanimla();
        }


        sayfa=1;
        loading=false;


        reload=  rootView.findViewById(R.id.reload);

        reloadfonk();


        swipeRefreshLayout= rootView.findViewById(R.id.yenile);
        listView= rootView.findViewById(R.id.listView);
        listView.setNestedScrollingEnabled(true);
        adapter=new BaslikAdapter(getActivity(),R.layout.baslikyapisi);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });


       listviewTiklamafonk();



        if(getArguments().getInt(KEY)==1)
        {
            new BugunGetir(String.valueOf(sayfa),getArguments().getString("cookie"),loading,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
        }
        else if(getArguments().getInt(KEY)==0)
        {
           // FragmentAramaMain aramaMain = (FragmentAramaMain) getParentFragment();

          //  Log.d("BaslikFragOncreateView","Eğer arama işlemleri null fırlatırsa düzeltme işlemi burada");

          AramaActivity aramaActivity= (AramaActivity) getActivity();


                if(aramaActivity.mSectionsPagerAdapter.getUrl()!=null)
            new BaslikGetir(aramaActivity.mSectionsPagerAdapter.getUrl(),false,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
            ,sayfa,swipeRefreshLayout,this ).execute();


        }
        else if(getArguments().getInt(KEY)==40)
        {

            new BaslikGetir(getArguments().getString("aramaurl"),false,getArguments().getString("cookie"),true,loading,getArguments().getInt(KEY)
                    ,sayfa,swipeRefreshLayout,this).execute();

        }
        else
        {
            //Log.d("BasliklarFragOncreate",dizi[getArguments().getInt(KEY)-2]);

            if(getArguments().getInt(KEY)==4)
            {
                FloatingActionButton actionButton= getActivity().findViewById(R.id.tarihsec);

                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numberPicker();
                    }
                });
            }


            StringBuilder yenisistemBaslikUrl=new StringBuilder();
            String birincilURL=dizi[getArguments().getInt(KEY)-2];

            if(getArguments().getInt(KEY)==4)
            {
                Log.d("pozisyon 4",birincilURL);
                yenisistemBaslikUrl.append(birincilURL).append("&p=").append(sayfa);
            }
            else
                yenisistemBaslikUrl.append(birincilURL).append("?p=").append(sayfa);


            Log.d("else",yenisistemBaslikUrl.toString());



           new BaslikGetir(yenisistemBaslikUrl.toString(),false,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
                   ,sayfa,swipeRefreshLayout,this).execute();
        }

        swipeRefreshfonk();

        return rootView;
    }


    private void reloadfonk()
    {

    reload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            reload.setVisibility(View.GONE);
            sayfa=1;
            if(getArguments().getInt(KEY)==1)
            {
                // adapter.bosalt();
                new BugunGetir("1",getArguments().getString("cookie"),loading,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
            }
            else if(getArguments().getInt(KEY)==0)
            {
                Log.d("onrefreshBaslik","Bura belki doldurulabilir");
            }
            else if(getArguments().getInt(KEY)==40)
            {
                // adapter.bosalt();
                new BaslikGetir(getArguments().getString("aramaurl"),false,getArguments().getString("cookie"),true,loading,getArguments().getInt(KEY)
                        ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
            }
            else
            {
                //  adapter.bosalt();

                if(getArguments().getInt(KEY)==4)
                {
                    Random ryil=new Random();
                    yil=ryil.nextInt(2017-1999)+1999;
                    dizi[2]=tarihtebugunTemelUrl+"year="+yil;

                    Log.d("hangi yil geldi", String.valueOf(yil));
                }

                new BaslikGetir(dizi[getArguments().getInt(KEY)-2],false,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
                        ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                loading=false;
            }


        }
    });

    }

    private void numberPicker() {

                LayoutInflater inflator = LayoutInflater.from(getActivity());
                @SuppressLint("InflateParams") View goster = inflator.inflate(R.layout.sayfasecme, null);
                final NumberPicker sayac =  goster.findViewById(R.id.sayisecici);
                sayac.setMaxValue(2019);
                sayac.setMinValue(1999);


//                Log.d("deneme entrysay",getSayfagecis());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Yıl")
                        .setView(goster)
                        .setPositiveButton("Getir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                              //  Log.d("Hangi yılmış", String.valueOf(sayac.getValue()));

                                sayfa=1;
                               // adapter.bosalt();
                                yil=sayac.getValue();
                                dizi[2]=tarihtebugunTemelUrl+"year="+yil;

                                new BaslikGetir(dizi[2],false,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
                                        ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                                loading=false;

                            }
                        });


                final AlertDialog alarm = builder.create();


                //   Button git= (Button) goster.findViewById(R.id.git);

                // alarm.setTitle("Sayfaya Git");


                alarm.setView(goster);

                Window window = alarm.getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                window.setAttributes(layoutParams);


                alarm.show();
                Button b = alarm.getButton(DialogInterface.BUTTON_POSITIVE);
                b.setTextColor(Color.parseColor("#84C44E"));


    }


    private void diziTanimla() {

        dizi=new String[]
                {
                        "https://eksisozluk.com/basliklar/gundem",
                        "https://eksisozluk.com/debe",
                        "",
                        "https://eksisozluk.com/basliklar/kanal/spor",
                        "https://eksisozluk.com/basliklar/kanal/ili%C5%9Fkiler",
                        "https://eksisozluk.com/basliklar/kanal/siyaset",
                        "https://eksisozluk.com/basliklar/kanal/seyahat",
                        "https://eksisozluk.com/basliklar/kanal/otomotiv",
                        "https://eksisozluk.com/basliklar/kanal/tv",
                        "https://eksisozluk.com/basliklar/kanal/anket",
                        "https://eksisozluk.com/basliklar/kanal/bilim",
                        "https://eksisozluk.com/basliklar/kanal/edebiyat",
                        "https://eksisozluk.com/basliklar/kanal/e%C4%9Fitim",
                        "https://eksisozluk.com/basliklar/kanal/ekonomi",
                        "https://eksisozluk.com/basliklar/kanal/ek%C5%9Fi-s%C3%B6zl%C3%BCk",
                        "https://eksisozluk.com/basliklar/kanal/haber",
                        "https://eksisozluk.com/basliklar/kanal/havac%C4%B1l%C4%B1k",
                        "https://eksisozluk.com/basliklar/kanal/magazin",
                        "https://eksisozluk.com/basliklar/kanal/moda",
                        "https://eksisozluk.com/basliklar/kanal/motosiklet",
                        "https://eksisozluk.com/basliklar/kanal/m%C3%BCzik",
                        "https://eksisozluk.com/basliklar/kanal/oyun",
                        "https://eksisozluk.com/basliklar/kanal/programlama",
                        "https://eksisozluk.com/basliklar/kanal/sa%C4%9Fl%C4%B1k",
                        "https://eksisozluk.com/basliklar/kanal/sanat",
                        "https://eksisozluk.com/basliklar/kanal/sinema",
                        "https://eksisozluk.com/basliklar/kanal/spoiler",
                        "https://eksisozluk.com/basliklar/kanal/tarih",
                        "https://eksisozluk.com/basliklar/kanal/teknoloji",
                        "https://eksisozluk.com/basliklar/kanal/yeme-i%C3%A7me",
                        "https://eksisozluk.com/basliklar/basiboslar",
                        "https://eksisozluk.com/basliklar/videolar"
                };

        Random ryil=new Random();
        yil=ryil.nextInt(2020-1999)+1999;
        dizi[2]=tarihtebugunTemelUrl+"year="+yil;

    }

    private void diziyitekrartanimla() {
        dizi=new String[]
                {
                        "https://eksisozluk.com/basliklar/gundem",
                        "https://eksisozluk.com/debe",
                        "",
                        "https://eksisozluk.com/basliklar/takipentry",
                        "https://eksisozluk.com/basliklar/takipfav",
                        "https://eksisozluk.com/basliklar/son",
                        "https://eksisozluk.com/basliklar/kenar",
                        "https://eksisozluk.com/basliklar/caylaklar",
                        "https://eksisozluk.com/basliklar/kanal/spor",
                        "https://eksisozluk.com/basliklar/kanal/ili%C5%9Fkiler",
                        "https://eksisozluk.com/basliklar/kanal/siyaset",
                        "https://eksisozluk.com/basliklar/kanal/seyahat",
                        "https://eksisozluk.com/basliklar/kanal/otomotiv",
                        "https://eksisozluk.com/basliklar/kanal/tv",
                        "https://eksisozluk.com/basliklar/kanal/anket",
                        "https://eksisozluk.com/basliklar/kanal/bilim",
                        "https://eksisozluk.com/basliklar/kanal/edebiyat",
                        "https://eksisozluk.com/basliklar/kanal/e%C4%9Fitim",
                        "https://eksisozluk.com/basliklar/kanal/ekonomi",
                        "https://eksisozluk.com/basliklar/kanal/ek%C5%9Fi-s%C3%B6zl%C3%BCk",
                        "https://eksisozluk.com/basliklar/kanal/haber",
                        "https://eksisozluk.com/basliklar/kanal/havac%C4%B1l%C4%B1k",
                        "https://eksisozluk.com/basliklar/kanal/magazin",
                        "https://eksisozluk.com/basliklar/kanal/moda",
                        "https://eksisozluk.com/basliklar/kanal/motosiklet",
                        "https://eksisozluk.com/basliklar/kanal/m%C3%BCzik",
                        "https://eksisozluk.com/basliklar/kanal/oyun",
                        "https://eksisozluk.com/basliklar/kanal/programlama",
                        "https://eksisozluk.com/basliklar/kanal/sa%C4%9Fl%C4%B1k",
                        "https://eksisozluk.com/basliklar/kanal/sanat",
                        "https://eksisozluk.com/basliklar/kanal/sinema",
                        "https://eksisozluk.com/basliklar/kanal/spoiler",
                        "https://eksisozluk.com/basliklar/kanal/tarih",
                        "https://eksisozluk.com/basliklar/kanal/teknoloji",
                        "https://eksisozluk.com/basliklar/kanal/yeme-i%C3%A7me",
                        "https://eksisozluk.com/basliklar/basiboslar",
                        "https://eksisozluk.com/basliklar/videolar"
                };


        if(getArguments().getInt(KEY)==4)
        {
            Random ryil=new Random();
            yil=ryil.nextInt(2020-1999)+1999;
            dizi[2]=tarihtebugunTemelUrl+"year="+yil;

            Log.d("hangi yil geldi", String.valueOf(yil));
        }


    }

    private void swipeRefreshfonk() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // adapter=new BaslikAdapter(getActivity(),R.layout.baslikyapisi);

                sayfa=1;
                if(getArguments().getInt(KEY)==1)
                {
                   // adapter.bosalt();
                    new BugunGetir("1",getArguments().getString("cookie"),loading,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                }
                else if(getArguments().getInt(KEY)==0)
                {
                    Log.d("onrefreshBaslik","Bura belki doldurulabilir");
                }
                else if(getArguments().getInt(KEY)==40)
                {
                   // adapter.bosalt();
                    new BaslikGetir(getArguments().getString("aramaurl"),false,getArguments().getString("cookie"),true,loading,getArguments().getInt(KEY)
                            ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                }
                else
                {
                  //  adapter.bosalt();

                    if(getArguments().getInt(KEY)==4)
                    {
                        Random ryil=new Random();
                        yil=ryil.nextInt(2020-1999)+1999;
                        dizi[2]=tarihtebugunTemelUrl+"year="+yil;

                        Log.d("hangi yil geldi", String.valueOf(yil));
                    }

                    new BaslikGetir(dizi[getArguments().getInt(KEY)-2],false,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
                            ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                    loading=false;
                }
            }
        });




    }


    // Başlık itemlerine tıklama tip 0 = entryactivityi açar
    //                           tip 1 = butona tıklayıp sonraki sayfayı ekleme
    private void listviewTiklamafonk()
    {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BaslikProvider provider= (BaslikProvider) parent.getItemAtPosition(position);


                if(provider.tip==0)
                {


                    String griurl;

                    if(provider.textigriyap)
                    {
                      griurl=provider.url.split("\\?a")[0].trim();
                    }
                    else
                    griurl=provider.url;



                    boolean urlduzenlenecekmi=griurl.contains("true#editbox");

                    Intent intent=new Intent(getActivity(),EntryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("link",griurl);
                    intent.putExtra("cookie",getArguments().getString("cookie"));
                    intent.putExtra("nick",getArguments().getString("nick"));
                    intent.putExtra("urlduzenle",urlduzenlenecekmi);
                    intent.putExtra("mark"," ");
                   // intent.putExtra("iq",getArguments().getString("iq"));
                    intent.putExtra("gelenstring",provider.baslik);
                    intent.putExtra("arsiv",false);



                    if(getArguments().getInt(KEY)==4)
                    {
                        intent.putExtra("yonlendirme",true);

                        Log.d("baslikfrag","niye true");
                    }
                    else
                        intent.putExtra("yonlendirme",false);




                   //Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                    startActivity(intent);
                }
                else if(provider.tip==1)
                {


                    parent.getRootView().findViewById(R.id.devambuton).setVisibility(View.GONE);
                    parent.getRootView().findViewById(R.id.devamet).setVisibility(View.VISIBLE);



                    if(getArguments().getInt(KEY)==0)
                    {


                        StringBuilder builder2=new StringBuilder();

                        sayfa++;

                       AramaActivity aramaActivity= (AramaActivity) getActivity();

                       if(!activitydenmigelme)
                         aramaActivityURL=aramaActivity.mSectionsPagerAdapter.getUrl();

                        if(!aramaActivityURL.contains("/kanal"))
                        builder2.append(aramaActivityURL).append("&p=").append(sayfa);
                        else
                        builder2.append(aramaActivityURL).append("?p=").append(sayfa);


                        loading=true;

                        Log.d("devamUrlsi",builder2.toString());

                        new BaslikGetir(builder2.toString(),true,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
                                ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();

                    }
                    else if(getArguments().getInt(KEY)==1)
                    {

                        sayfa++;

                        loading=true;
                        new BugunGetir(String.valueOf(sayfa),getArguments().getString("cookie"),loading,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                    }
                    else if(getArguments().getInt(KEY)==40)
                    {
                        StringBuilder olayurl=new StringBuilder();
                        sayfa++;

                        loading=true;

                        olayurl.append(getArguments().getString("aramaurl")).append("?p=").append(sayfa);

                        new BaslikGetir(olayurl.toString(),true,getArguments().getString("cookie"),true,loading,getArguments().getInt(KEY)
                                ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                    }
                    else
                    {
                        StringBuilder builder2=new StringBuilder();

                        sayfa++;

                        if(getArguments().getInt(KEY)==4)
                        {
                          //  dizi[1]=dizi[1]+"year="+yil;

                         //   Log.d("SayfaDevamdaYılAynıMı", String.valueOf(yil));
                            builder2.append(dizi[getArguments().getInt(KEY)-2]).append("&p=").append(sayfa);
                        }
                        else
                        builder2.append(dizi[getArguments().getInt(KEY)-2]).append("?p=").append(sayfa);

                       Log.d("neymiş bu url",builder2.toString());
                        loading=true;

                        new BaslikGetir(builder2.toString(),true,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
                                ,sayfa,swipeRefreshLayout,BasliklarFragment.this).execute();
                    }



                    // new YazarProfiliCek(builder2.toString()).execute();
                }

                // getActivity().startActivity(intent);

            }
        });


    }


    void activitydenulasma(String veri,boolean activityden)
    {
        Log.d("ActivitydenGelen",veri);

        activitydenmigelme=activityden;

        aramaActivityURL=veri;
        sayfa=1;
       // adapter.bosalt();

        new BaslikGetir(veri,false,getArguments().getString("cookie"),false,loading,getArguments().getInt(KEY)
                ,sayfa,swipeRefreshLayout,this).execute();
    }

    @Override
    public void sayfaDevamAyracEkle(int sayfa) {
        this.loading=false;
        adapter.sonuncuyusil();
        adapter.ayracekle(sayfa);
    }

    @Override
    public void adaptorBosalt() {
        adapter.bosalt();
    }

    @Override
    public void basliklariKur(BaslikProvider[] baslikProvider) {

        for (BaslikProvider basliklar : baslikProvider) {
            adapter.add(basliklar);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void listviewBasaKaydir(int pagerPozisyon, boolean butondangelme) {
        if(adapter.getCount()>=50)
            adapter.yukleniyorekle();

        //     listView.startAnimation(animation);

        adapter.notifyDataSetChanged();


        if(adapter.getCount()<52 ||pagerPozisyon==0 && !butondangelme)
            listView.setSelectionAfterHeaderView();
    }

    @Override
    public void bugunDevamEkle() {
        if(adapter.getCount()>=50)
            adapter.yukleniyorekle();

        adapter.notifyDataSetChanged();


        if(adapter.getCount()<52)
            listView.setSelectionAfterHeaderView();
    }

    @Override
    public void progressBarGosterVeyaGizle(boolean gosterilecekmi) {

       if (gosterilecekmi)
        progressBarLeakSebebiMi.setVisibility(View.VISIBLE);
       else
       {
           if(progressBarLeakSebebiMi!=null)
               progressBarLeakSebebiMi.setVisibility(View.GONE);
       }


    }

    @Override
    public void reloadGosterVeyaGizle(boolean reloadGoster) {
        if(reloadGoster)
            reload.setVisibility(View.VISIBLE);
        else
            reload.setVisibility(View.GONE);
    }


    static class BaslikGetir extends AsyncTask<Void,Void,BaslikProvider[]> {

        boolean butondangelme;
        String cookie;
        String kanalUrl;
        boolean olay;

        boolean loading;

        int pagerPozisyonu;
        int sayfa;

      //  WeakReference<ProgressBar> progressBarWeakReference;
        WeakReference<SwipeRefreshLayout> swipeRefreshLayoutWeakReference;
      //  WeakReference<ImageButton> reloadWeakRef;
        WeakReference<BasliklarFragment> basliklarFragmentWeakReference;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(!swipeRefreshLayoutWeakReference.get().isRefreshing()&&!loading)
                basliklarFragmentWeakReference.get().progressBarGosterVeyaGizle(true);
           // progressBarWeakReference.get().setVisibility(View.VISIBLE);
        }



        BaslikGetir(String kanalUrl, boolean butondangelme, String cookie, boolean olay, boolean loading,int pagerPozisyonu,int sayfa,
                   SwipeRefreshLayout swipeRefreshLayout,BasliklarFragment baslikFragHaberlesme) {
            this.kanalUrl = kanalUrl;
            this.butondangelme=butondangelme;
            this.cookie=cookie;
            this.olay=olay;
            this.loading=loading;
            this.pagerPozisyonu=pagerPozisyonu;
            this.sayfa=sayfa;

            //progressBarWeakReference=new WeakReference<ProgressBar>(progressBar);
            swipeRefreshLayoutWeakReference=new WeakReference<SwipeRefreshLayout>(swipeRefreshLayout);
            //reloadWeakRef=new WeakReference<ImageButton>(reload);
            basliklarFragmentWeakReference=new WeakReference<BasliklarFragment>(baslikFragHaberlesme);


        }


        @Override
        protected BaslikProvider[] doInBackground(Void... params) {

            Document document=null;

            BaslikProvider[] dizi;




            try {

                if(olay)
                {
                    document= Jsoup.connect(kanalUrl)
                            .cookie("Cookie",cookie)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                            .referrer("https://eksisozluk.com/")
                            .ignoreHttpErrors(true)
                            .timeout(10*1000)
                            .get();
                }
                else
                {

                   String aCookie=cookieyiayir(cookie);

                    Connection.Response response=Jsoup.connect(kanalUrl)
                            .header("Accept","*/*")
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                            .method(Connection.Method.GET)
                            .cookie("a",aCookie)
                            .header("X-Requested-With","XMLHttpRequest")
                            .timeout(10*1000)
                            .execute();


                    document=Jsoup.parse(response.body());

                }



            } catch (IOException e) {
                e.printStackTrace();
            }

           // BaslikGetirmeAsynctask baslikGetir=new BaslikGetirmeAsynctask();


            if(pagerPozisyonu!=40)
            dizi=BaslikGetirmeAsynctask.baslikProviders(document,true,false);
            else
                dizi=BaslikGetirmeAsynctask.baslikProviders(document,false,true);




            return dizi;
        }

        private String cookieyiayir(String gerekli) {
            String[] temp=gerekli.split(";");
            String donecek="BOS";

            for (String ar1 : temp ){
                if(ar1.contains("a")){
                    String[] temp1=ar1.split("=");
                    for(String tekler:temp1)
                    {
                        if(tekler.trim().equals("a"))
                        {
                         //   Log.d("başlıkfragasynctask",temp1[1]);
                           donecek=temp1[1];
                            //Log.d("giriş yaptı","giriş yapıldı yönlendirme lazım");
                        }
                    }
                }
            }
            return donecek;
        }

        @Override
        protected void onPostExecute(BaslikProvider[] baslikProviders) {
            super.onPostExecute(baslikProviders);


            BaslikFragHaberlesme baslikFragHaberlesme = basliklarFragmentWeakReference.get();

            if (basliklarFragmentWeakReference.get().isDetached()) {
                Log.d("asynctask", "detach olmuş başlıkfrag");
            } else
            {

            if (loading && butondangelme) {
                // loading=false;
                // adapter.sonuncuyusil();
                //  adapter.ayracekle(sayfa);

                baslikFragHaberlesme.sayfaDevamAyracEkle(sayfa);

            } else
                baslikFragHaberlesme.adaptorBosalt();

            //    adapter.bosalt();

            // if(progressBarWeakReference.get()!=null)
            // progressBarWeakReference.get().setVisibility(View.GONE);

            baslikFragHaberlesme.progressBarGosterVeyaGizle(false);


            if (baslikProviders != null) {
                // reloadWeakRef.get().setVisibility(View.GONE);
                baslikFragHaberlesme.reloadGosterVeyaGizle(false);

                if (swipeRefreshLayoutWeakReference.get().isRefreshing())
                    swipeRefreshLayoutWeakReference.get().setRefreshing(false);

                // Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.yenianim);

              /*  for (BaslikProvider baslikProvider : baslikProviders) {
                    adapter.add(baslikProvider);
                    adapter.notifyDataSetChanged();
                }*/
                baslikFragHaberlesme.basliklariKur(baslikProviders);

                baslikFragHaberlesme.listviewBasaKaydir(pagerPozisyonu, butondangelme);

           /*     if(adapter.getCount()>=50)
                adapter.yukleniyorekle();

           //     listView.startAnimation(animation);

                adapter.notifyDataSetChanged();


                if(adapter.getCount()<52 ||pagerPozisyonu==0 && !butondangelme)
                listView.setSelectionAfterHeaderView();*/


                if (baslikProviders.length == 0) {
                    // reloadWeakRef.get().setVisibility(View.VISIBLE);
                    baslikFragHaberlesme.reloadGosterVeyaGizle(true);
                }


            } else
                baslikFragHaberlesme.reloadGosterVeyaGizle(true);
            //  reloadWeakRef.get().setVisibility(View.VISIBLE);

        }

        }

    }

    static class BugunGetir extends AsyncTask<Void,Void,BaslikProvider[]>
    {

        String sayfano;
        String cookie;
        boolean loading;
        int sayfa;

        WeakReference<SwipeRefreshLayout> swipeRefreshLayoutWeakReference;
       // WeakReference<ProgressBar> progressBarWeakReference;
        WeakReference<BaslikFragHaberlesme> baslikFragHaberlesmeWeakReference;
      //  WeakReference<ImageButton> reloadWeakRef;

        BugunGetir(String sayfano,String cookie,boolean loading,int sayfa,SwipeRefreshLayout swipeRefreshLayout,BaslikFragHaberlesme baslikFragHaberlesme)
        {

            this.cookie=cookie;
            this.sayfano=sayfano;
            this.loading=loading;
            this.sayfa=sayfa;

            swipeRefreshLayoutWeakReference=new WeakReference<SwipeRefreshLayout>(swipeRefreshLayout);
          //  progressBarWeakReference=new WeakReference<ProgressBar>(progressBar);
            baslikFragHaberlesmeWeakReference=new WeakReference<BaslikFragHaberlesme>(baslikFragHaberlesme);
            //reloadWeakRef=new WeakReference<ImageButton>(reload);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(!swipeRefreshLayoutWeakReference.get().isRefreshing()&& !loading)
                baslikFragHaberlesmeWeakReference.get().progressBarGosterVeyaGizle(true);
          //  progressBarWeakReference.get().setVisibility(View.VISIBLE);
        }


        @Override
        protected BaslikProvider[] doInBackground(Void... params) {


            Document document=null;

            BaslikProvider[] dizi;

            try {

                String bcookie=cookieyiayir(cookie);

                Connection.Response response=Jsoup.connect("https://eksisozluk.com/basliklar/bugun/"+sayfano)
                        .header("Accept","*/*")
                        .method(Connection.Method.GET)
                        .timeout(7*1000)
                        .cookie("a",bcookie)
                        .header("X-Requested-With","XMLHttpRequest")
                        .execute();

                document=Jsoup.parse(response.body());

           if(document.select("div.pager").isEmpty())
           {
            Log.d("1","sayfa 1");
           }
                else

           {
               Log.d("sayfa  "+sayfano,document.select("div.pager").attr("data-currentpage")+"  "+document.select("div.pager").attr("data-pagecount"));
           }



            } catch (IOException e) {
                e.printStackTrace();
            }


//BaslikGetirmeAsynctask baslikGetirme=new BaslikGetirmeAsynctask();
            dizi=BaslikGetirmeAsynctask.baslikProviders(document,true,false);


            return dizi;
        }

        private String cookieyiayir(String gerekli) {
            String[] temp=gerekli.split(";");
            String donecek="BOS";

            for (String ar1 : temp ){
                if(ar1.contains("a")){
                    String[] temp1=ar1.split("=");
                    for(String tekler:temp1)
                    {
                        if(tekler.trim().equals("a"))
                        {
                            Log.d("başlıkfragasynctask",temp1[1]);
                            donecek=temp1[1];
                            //Log.d("giriş yaptı","giriş yapıldı yönlendirme lazım");
                        }
                    }
                }
            }
            return donecek;
        }


        @Override
        protected void onPostExecute(BaslikProvider[] baslikProviders) {
            super.onPostExecute(baslikProviders);

            BaslikFragHaberlesme baslikFragHaberlesme=baslikFragHaberlesmeWeakReference.get();

            if(baslikFragHaberlesme!=null) {


                if (loading) {
                    //loading=false;
                    // adapter.sonuncuyusil();
                    //adapter.ayracekle(sayfa);

                    baslikFragHaberlesme.sayfaDevamAyracEkle(sayfa);

                } else
                    baslikFragHaberlesme.adaptorBosalt();


                if (baslikProviders != null) {
                    //reloadWeakRef.get().setVisibility(View.GONE);
                    baslikFragHaberlesme.reloadGosterVeyaGizle(false);

                    if (swipeRefreshLayoutWeakReference.get().isRefreshing())
                        swipeRefreshLayoutWeakReference.get().setRefreshing(false);


              /*  for (BaslikProvider baslikProvider : baslikProviders) {
                    adapter.add(baslikProvider);
                }*/
                    baslikFragHaberlesme.basliklariKur(baslikProviders);


                    baslikFragHaberlesme.bugunDevamEkle();
             /*   if(adapter.getCount()>=50)
                    adapter.yukleniyorekle();

                adapter.notifyDataSetChanged();


                if(adapter.getCount()<52)
                listView.setSelectionAfterHeaderView();*/

                    baslikFragHaberlesme.progressBarGosterVeyaGizle(false);
                    // progressBarWeakReference.get().setVisibility(View.GONE);
                } else
                    baslikFragHaberlesme.reloadGosterVeyaGizle(true);
                //reloadWeakRef.get().setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("baslikfrag","başlıkfrag activitye bağlandı     ");

        if(context instanceof MainActivity)
            Log.d("contextinstance","mainactivity");

    //    Log.d("contextinstance",context.getClassLoader().toString());



    }

    @Override
    public void onDetach() {
        super.onDetach();
            progressBarLeakSebebiMi=null;
        Log.d("baslikfrag","başlıkfrag activityden koptu silinmiş olması lazım");
    }




    }
