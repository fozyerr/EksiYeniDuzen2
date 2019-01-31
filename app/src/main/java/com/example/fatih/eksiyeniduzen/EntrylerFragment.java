package com.example.fatih.eksiyeniduzen;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Fatih on 17.09.2017.
 */

public class EntrylerFragment extends Fragment implements EntryFragmentHaberlesme {

    private static final String ARG_SECTION_NUMBER = "section_number";

    EntryActivity entryActivity;
    ListView listView;
   // TextView textView,basliktextview;
            //,ontextview;
    boolean test,yapildimi;
    SecenekAdapter secenekAdapter;
   // RelativeLayout seritlayout;
  //RelativeLayout progressBar;
    ConstraintLayout progressBar;
    EntryAdapter adapter;
    int SAYFAYA;
  //  boolean takipediliyomu;

    ArrayList<EntryProvider> arsivListesi;
    InterfaceArama denemeinter;
    EntryFragmenttenActivitye entryFragmenttenActivitye;

    boolean enaltaindimi;
    int sayfanumarasi;

    String sectionUrlsi="BOŞ";
  //static String sectionUrlsiYerine="BOŞ";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        denemeinter= (InterfaceArama) context;
        entryFragmenttenActivitye= (EntryFragmenttenActivitye) context;
    }

    FloatingActionButton videobutonu;

    Animation goster,gizle;

    public EntrylerFragment() {

        yapildimi=false;

    }

    public static EntrylerFragment newInstance(int sectionNumber,String url,String sonrakisayfaeki,boolean yonlendirme,
                                               int sayfayagit,String cookie,String nick,boolean urlduzenle,String gelenstring,
                                               String mark,boolean arsiv,ArrayList<EntryProvider> entryProviderArrayList) {
        EntrylerFragment fragment = new EntrylerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("URL",url);
        args.putString("EK",sonrakisayfaeki);
        args.putBoolean("yonlendirme",yonlendirme);
        args.putInt("sayfaya",sayfayagit);
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        args.putBoolean("urlduzenle",urlduzenle);
        args.putString("gelenstring",gelenstring);
        args.putString("mark",mark);
        args.putBoolean("arsiv",arsiv);
        args.putParcelableArrayList("arsiventryler",entryProviderArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entry_constraint, container, false);


        entryActivity= (EntryActivity) getActivity();
        //textView=  entryActivity.findViewById(R.id.maxtext);
      // entryActivity.findViewById(R.id.actspin).setVisibility(View.GONE);
        //ontextview= (TextView) entryActivity.findViewById(R.id.textt);
       //basliktextview= entryActivity.findViewById(R.id.basliktextviewi);
      // seritlayout= entryActivity.findViewById(R.id.sayfaci);

        progressBar= rootView.findViewById(R.id.yuklenmespin);
        videobutonu= getActivity().findViewById(R.id.videobuton);

        enaltaindimi=false;
        sayfanumarasi=getArguments().getInt(ARG_SECTION_NUMBER)+1;

        goster= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_open);
        gizle=AnimationUtils.loadAnimation(getActivity(),R.anim.fab_kapat);



        listView= rootView.findViewById(R.id.listView2);
        adapter=new EntryAdapter(getActivity(),R.layout.entrylayout,getArguments().getString("cookie"),getArguments().getString("nick"));
        listView.setAdapter(adapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
        listView.setNestedScrollingEnabled(true);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });




        if(getArguments().getBoolean("arsiv"))
        {


            progressBar.setVisibility(View.GONE);
            ArsivdenEntryCekmeIslemeleri();

        }
        else
        {

            NormalEntrySayfasiIslemleri();

        }


        return rootView;
    }

    private void ArsivdenEntryCekmeIslemeleri() {



      /*  int pos=getArguments().getInt(ARG_SECTION_NUMBER);


        int baslangic=(pos-1)*10;
        int bitis=pos*10;

        if(bitis>entryActivity.arsiventryleri.size())
            bitis=entryActivity.arsiventryleri.size();*/

      ArrayList<EntryProvider> entryler=getArguments().getParcelableArrayList("arsiventryler");
        int bitis=entryler.size();
        for(int i=0;i<bitis;i++)
        {
            adapter.add(entryler.get(i));
        }
        adapter.notifyDataSetChanged();
        listView.setSelectionAfterHeaderView();


    }

    private void NormalEntrySayfasiIslemleri() {



        StringBuilder urlbuild=new StringBuilder();


        String cookie=getArguments().getString("cookie");

        String gelenurl=getArguments().getString("URL");


        gelenurl=gelenurl.split("\\?focusto")[0];


      // listviewscrolllistener();

        if(!getArguments().getBoolean("urlduzenle"))
            urlbuild.append(gelenurl).append(getArguments().getString("EK")).append(getArguments().getInt(ARG_SECTION_NUMBER));
        else
        {

           // Log.d("GETVIEW ÇALIŞTI",sectionUrlsi);
           urlbuild.append(entryActivity.mSectionsPagerAdapter.getYenilenmisURL()).append(getArguments().getString("EK")).append(getArguments().getInt(ARG_SECTION_NUMBER));

           // Log.d("else içi",sectionUrlsiYerine);

          //  urlbuild.append(sectionUrlsiYerine).append(getArguments().getString("EK")).append(getArguments().getInt(ARG_SECTION_NUMBER));
        }




        test = getArguments().getInt(ARG_SECTION_NUMBER) == 1 && getArguments().getBoolean("yonlendirme") && !yapildimi;

        SAYFAYA=getArguments().getInt("sayfaya");





        // Log.d("entryfrag",getArguments().getString("URL"));

        if(getArguments().getInt(ARG_SECTION_NUMBER)!=1)
        {
            new EntryGetir(urlbuild.toString(),test,cookie,EntrylerFragment.this,entryActivity,getArguments().getBoolean("urlduzenle"),
                    getArguments().getString("URL"),getArguments().getInt(ARG_SECTION_NUMBER),SAYFAYA,getArguments().getString("nick")
            ).execute();
        }
        else
        {
            new EntryGetir(getArguments().getString("URL"),test,cookie,EntrylerFragment.this,entryActivity,getArguments().getBoolean("urlduzenle"),
                    getArguments().getString("URL"),getArguments().getInt(ARG_SECTION_NUMBER),SAYFAYA,getArguments().getString("nick")
            ).execute();
        }
    }

    private void listviewscrolllistener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if (i == 0) {
                    // check if we reached the top or bottom of the list
                    View v = listView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                      //  Log.d("if","burası if");
                        return;
                    }
                } else if (i2 - i1 == i){
                    View v =  listView.getChildAt(i2-1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {

                        if(!enaltaindimi && sayfanumarasi<=entryActivity.mSectionsPagerAdapter.getToplamsayfa())
                        {
                            String gelenurl=getArguments().getString("URL");


                            gelenurl=gelenurl.split("\\?focusto")[0];
                            enaltaindimi=true;
                            StringBuilder urlbuild=new StringBuilder();
                           // sayfanumarasi++;
                            if(!getArguments().getBoolean("urlduzenle"))
                                urlbuild.append(gelenurl).append(getArguments().getString("EK")).append(sayfanumarasi);
                            else
                            {

                                // Log.d("GETVIEW ÇALIŞTI",sectionUrlsi);
                                urlbuild.append(entryActivity.mSectionsPagerAdapter.getYenilenmisURL()).append(getArguments().getString("EK")).append(sayfanumarasi);

                                // Log.d("else içi",sectionUrlsiYerine);

                                //  urlbuild.append(sectionUrlsiYerine).append(getArguments().getString("EK")).append(getArguments().getInt(ARG_SECTION_NUMBER));
                            }
                            new EntryGetir(urlbuild.toString(),test,getArguments().getString("cookie"),EntrylerFragment.this,entryActivity,getArguments().getBoolean("urlduzenle"),
                                    getArguments().getString("URL"),getArguments().getInt(ARG_SECTION_NUMBER),SAYFAYA,getArguments().getString("nick")
                            ).execute();

                            sayfanumarasi++;
                        }
                        else
                            Log.d("durdur","durdur artık muhittin abi");

                        return;
                    }
                }

                else
                {
                 //   Log.d("else","burası else");;
                }

            }
        });
    }

    private void yüklemeyap() {

        Log.d("yükleme yapildi","tekrar false");
        enaltaindimi=false;

    }

    @Override
    public void stringgonder(String mesaj) {
        Log.d("entryfrag",mesaj);
    }

    @Override
    public void yonlendirmeYapildiMi(boolean yapildimikontrol) {



        yapildimi=yapildimikontrol;

     //   Log.d("interface YAPILDI","interface e girildi "+ yapildimi);

    }

    @Override
    public void asynctasktanfragmente(int yonlendirileceksayfa) {



        entryFragmenttenActivitye.sayfayayonlendir(yonlendirileceksayfa);
    }

    @Override
    public void asynctasktanFragmenteToplamSayfaSayisi(int toplamsayfasayisi) {

        entryFragmenttenActivitye.viewpagerToplamKacSayfa(toplamsayfasayisi);
    }

    @Override
    public void duzenlenmisUrlyiGonder(String duzenlenmisUrl) {

        //sectionUrlsiYerine=duzenlenmisUrl;

        entryFragmenttenActivitye.sectionAdaptorUrlKur(duzenlenmisUrl);

     //   Log.d("interface fonk",sectionUrlsiYerine);

    }

    @Override
    public void updateView(boolean videovarmi, String videoUrl) {

        if(videovarmi)
        {
            //videobutonu.setVisibility(View.VISIBLE);
            denemeinter.updateView(true,videoUrl);

        }
        else
            denemeinter.updateView(false,"");

    }

    @Override
    public void seritlayoutGorunurlugu(int toplamsayfa) {

        entryFragmenttenActivitye.seritlayoutGorunurlugu(toplamsayfa);

      /*  String text=" / "+toplamsayfa;
        textView.setText(text);

        if(toplamsayfa>1)
            seritlayout.setVisibility(View.VISIBLE);
        else
            seritlayout.setVisibility(View.GONE);*/


    }

    @Override
    public void basligiKur(String baslikString) {

        entryFragmenttenActivitye.basligiKur(baslikString);
     //   basliktextview.setText(baslikString);
    }

    @Override
    public void adaptoreEklemeIslemi(EntryProvider[] entryler, int diziboyutu) {

        if(enaltaindimi)
        {
            adapter.add(new EntryProvider(sayfanumarasi-1,3));
        }



        for(int i=0;i<diziboyutu;i++)
        {
            adapter.add(entryler[i]);
        }
        adapter.notifyDataSetChanged();

        if(!enaltaindimi)
        listView.setSelectionAfterHeaderView();

        enaltaindimi=false;

    }

    @Override
    public void progressAc() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void progressKapa() {

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void bundlegonder(Bundle bundle) {

        entryFragmenttenActivitye.basliktiklamaBundleGonder(bundle);

    }

    @Override
    public void takipedildimi(boolean takipediliyomu) {

        entryFragmenttenActivitye.takipedildimi(takipediliyomu);
    }

    public void activitydenulasma(String acurl)
    {
        this.sectionUrlsi=acurl;

        Log.d("ENTRYRAF","BURAYI HİÇ KULLANDIM MI");
    }


   static class EntryGetir extends AsyncTask<Void,Void,EntryProvider[]>
    {

        String sayfaurl,baslik,yeniurl,temizurl;
        int toplamSayfa,tumuneyonlendirme;
        boolean gidileceksayfa;
        String cookie,baslikID;
        boolean kenardaentryVarMi;
        String kenardakiEntry,entryverify;
        String kenarIıcnUrl;

        boolean takipediliyomu;
        boolean urlDuzenle;


        String nick;
        boolean videoVarMi;
        String videoURL;
        String getURL;
        int argSectionNumber;
        int Sayfaya;

        String sorunsalURL;
        String sorunsalText;
        boolean sorunsal;



        EntryFragmentHaberlesme entryFragmentHaberlesme;
        WeakReference<EntryActivity> entryActivityWeakReference;




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressBar.setVisibility(View.VISIBLE);

        //    EntryActivity ent= entryActivityWeakReference.get();
        //   RelativeLayout relativeLayout=ent.findViewById(R.id.yuklenmespin);
       //  relativeLayout.setVisibility(View.VISIBLE);

            entryFragmentHaberlesme.progressAc();
        }

        EntryGetir(String sayfaurl, boolean gidileceksayfa,String cookie,EntryFragmentHaberlesme entryFragmentHaberlesme,EntryActivity activityRef,
                   boolean urlDuzenle,String getURL,int argSectionNumber,int Sayfaya,String nick)
        {

            this.sayfaurl=sayfaurl;
            this.gidileceksayfa=gidileceksayfa;
            this.cookie=cookie;
            this.entryFragmentHaberlesme=entryFragmentHaberlesme;
            this.urlDuzenle=urlDuzenle;
            this.nick=nick;
            this.argSectionNumber=argSectionNumber;
            this.Sayfaya=Sayfaya;
            this.getURL=getURL;
            entryActivityWeakReference=new WeakReference<EntryActivity>(activityRef);

        }

        @SuppressLint("LogConditional")
        @Override
        protected EntryProvider[] doInBackground(Void... params) {

            Document document;
            EntryProvider[] dizi=null;

           try {
               document= Jsoup.connect("https://eksisozluk.com"+sayfaurl)
                        .cookie("Cookie",cookie)
                        .timeout(15*1000)
                        .get();

              Log.d("EntrylerFragment","https://eksisozluk.com"+sayfaurl);


              // Log.d("cookie",cookie);

               baslik=document.select("h1").attr("data-title");
               temizurl=document.select("h1 > a").attr("href");
               kenarIıcnUrl=temizurl;


               takipediliyomu = document.select("a#track-topic-link").attr("data-tracked").equals("1");
               baslikID=document.select("h1").attr("data-id");
               videoVarMi=!document.select("div#video").isEmpty();
               kenardaentryVarMi=!document.select("textarea#editbox").text().isEmpty();



               if(!cookie.equals("BOS"))
               {
                   if(!document.select("form.editform > input").isEmpty())
                   entryverify=document.select("form.editform > input").first().attr("value");

               }

               sorunsal=document.select("div.sub-title-menu > a").text().contains("lar");
               sorunsalText=document.select("div.sub-title-menu > a").text();
               sorunsalURL=document.select("div.sub-title-menu > a").attr("href");

               if(TextUtils.isEmpty(document.select("div.sub-title-menu > a").text()))
                   sorunsalText="yok";

               Log.d("sorunsaltext",sorunsalText+"   "+sorunsal);

               Log.d("sorunsaldeneme",document.select("div.sub-title-menu > a").attr("href"));


               if(kenardaentryVarMi)
                   kenardakiEntry=document.select("textarea#editbox").text();
               else
               kenardakiEntry="";


               if(videoVarMi)
               {
                   if(document.select("div#video > iframe").isEmpty())
                       videoURL=document.select("div#video > a").attr("data-videouri");
                   else
                       videoURL=document.select("div#video > iframe").attr("src");


               }


               if(!document.select("a.showall.more-data").isEmpty())
               {
                   tumuneyonlendirme= Integer.parseInt(document.select("a.showall.more-data").text().split("entry")[0].trim());
                   yeniurl=document.select("a.showall.more-data").attr("href");
               }
               else
               tumuneyonlendirme=0;

               if(!document.select("div.pager").isEmpty())
               {
                   toplamSayfa=Integer.parseInt(document.select("div.pager").attr("data-pagecount"));
               }
               else
               {
                    toplamSayfa=1;
               }

             //  EntryleriAyikla ayikla=new EntryleriAyikla();

               dizi=EntryleriAyikla.entryler(document,entryActivityWeakReference.get(),false,cookie,nick,"");

            } catch (IOException e) {
                e.printStackTrace();
            }


            return dizi;
        }


        @Override
        protected void onPostExecute(EntryProvider[] entryProviders) {
            super.onPostExecute(entryProviders);



            //progressBar.setVisibility(View.GONE);
            entryFragmentHaberlesme.progressKapa();

            EntryActivity entryActivityRef=entryActivityWeakReference.get();


            if(entryProviders!=null)
            {

                if(argSectionNumber==1 && urlDuzenle )
                {
                    String yazaraozelmi=Uri.decode(getURL);

                    if(yazaraozelmi.contains("@"))
                    {
                        yazaraozelmi=yazaraozelmi.split("@")[1].trim();
                        temizurl=temizurl+"?a=search&author="+yazaraozelmi;
                    }


                  //  entryActivityRef.mSectionsPagerAdapter.setYenilenmisURL(temizurl);
                    entryFragmentHaberlesme.duzenlenmisUrlyiGonder(temizurl);
                    /*

                    Interface ile url düzenlenmesi gereken başlıklarda düzenlenmiş url activitynin adaptörüne kuruluyor, daha sonra adaptörden çekiliyor

                     */

                  // entryActivity.mSectionsPagerAdapter.setYenilenmisURL(temizurl);
                   // entryFragmentHaberlesme.duzenlenmisUrlyiGonder(temizurl);


                  //  entryFragmentHaberlesme.stringgonder("section adaptörün urlsini gönderiyorum xD  "+temizurl);

                }

                entryFragmentHaberlesme.asynctasktanFragmenteToplamSayfaSayisi(toplamSayfa);

                /*

                Interface ile toplamsayfa sayısı kuruluyor

                 */

          //  entryActivity.mSectionsPagerAdapter.setToplamsayfa(toplamSayfa);
          //  entryActivity.mSectionsPagerAdapter.notifyDataSetChanged();



            if (gidileceksayfa)
            {



              //  entryActivity.mViewPager.setCurrentItem(SAYFAYA-1,true);

              //  entryFragmenttenActivitye.sayfayayonlendir(SAYFAYA-1);


                /*

                Interface ile sayfa yönlendirmesi yapılıyor

                 */


                entryFragmentHaberlesme.asynctasktanfragmente(Sayfaya-1);




                //yapildimi=true;


                /*

                INTERFACE İLE YÖNLENDİRİLME YAPILDIĞINI TASDİK EDİYOR

                EĞER SIKINTI ÇIKARSA
                yapildimi=true;
                kodunu kullan


                 */

                entryFragmentHaberlesme.yonlendirmeYapildiMi(true);
            }





            if(argSectionNumber==1)
            {
                /*

                   final boolean kenardaentryVarMi=basliktiklamaBundle.getBoolean("kenarvarmi");
                final String  entryverify=basliktiklamaBundle.getString("verify");
                final String   kenardakiEntry=basliktiklamaBundle.getString("kenarentry");
                final String   kenarIıcnUrl=basliktiklamaBundle.getString("kenarurl");
                final boolean urlDuzenle=basliktiklamaBundle.getBoolean("urlduzenle");
                final String baslik=basliktiklamaBundle.getString("baslik");
                final String baslikID=basliktiklamaBundle.getString("baslikid");
                final String urlyenile=basliktiklamaBundle.getString("urlyenile");
                final int tumsayfasayikontrol=basliktiklamaBundle.getInt("tumsayfasayikontrol");
                final int toplamSayfa=basliktiklamaBundle.getInt("toplamsayfa");
                final String temizurl=basliktiklamaBundle.getString("temizurl");


                 */

                Bundle basliktiklamaBundle=new Bundle();
                basliktiklamaBundle.putBoolean("kenarvarmi",kenardaentryVarMi);
                basliktiklamaBundle.putString("verify",entryverify);
                basliktiklamaBundle.putString("kenarentry",kenardakiEntry);
                basliktiklamaBundle.putString("kenarurl",kenarIıcnUrl);
                basliktiklamaBundle.putBoolean("urlduzenle",urlDuzenle);
                basliktiklamaBundle.putString("baslik",baslik);
                basliktiklamaBundle.putString("baslikid",baslikID);
                basliktiklamaBundle.putString("urlyenile",yeniurl);
                basliktiklamaBundle.putInt("tumsayfasayikontrol",tumuneyonlendirme);
                basliktiklamaBundle.putInt("toplamsayfa",toplamSayfa);
                basliktiklamaBundle.putString("temizurl",temizurl);
                basliktiklamaBundle.putBoolean("sorunsal",sorunsal);
                basliktiklamaBundle.putString("sorunsalText",sorunsalText);
                basliktiklamaBundle.putString("sorunsalURL",sorunsalURL);


                entryFragmentHaberlesme.takipedildimi(takipediliyomu);
                entryFragmentHaberlesme.bundlegonder(basliktiklamaBundle);

               // entryFragmentHaberlesme.bundlegonder(basliktiklamaBundle);

              //  baslikTiklamDinleyici(basliktextview,tumuneyonlendirme,yeniurl,baslikID);
                    tumuneyonlendirme=1+tumuneyonlendirme/10;



                    entryFragmentHaberlesme.updateView(videoVarMi,videoURL);
                    /*

                    interface ile önce fragmente video bilgisi gönderiliyor, oradan da activity e interface ile bilgiler gönderiliyor

                     */



               /* if(videoVarMi)
                {
                    //videobutonu.setVisibility(View.VISIBLE);
                    denemeinter.updateView(true,videoURL);

                }
                else
                    denemeinter.updateView(false,"");*/


               // Log.d("GİDİLECEK SAYFA", String.valueOf(tumuneyonlendirme));
            }


             /*   for(int i=0;i<entryProviders.length;i++)
                {
                    adapter.add(entryProviders[i]);
                }
                adapter.notifyDataSetChanged();
                listView.setSelectionAfterHeaderView();

                */



            //basliktextview.setText(baslik);

                entryFragmentHaberlesme.adaptoreEklemeIslemi(entryProviders,entryProviders.length);
                entryFragmentHaberlesme.basligiKur(baslik);
                entryFragmentHaberlesme.seritlayoutGorunurlugu(toplamSayfa);

            /*


            Interface ile

             listview entryler ekleniyor

             şerit layoutun görünürlüğünü düzenliyor

             başlık stringi tam olarak kuruluyor


             */


         /*   String text=" / "+toplamSayfa;
            textView.setText(text);

            if(toplamSayfa>1)
            seritlayout.setVisibility(View.VISIBLE);
            else
                seritlayout.setVisibility(View.GONE);*/


            }
            else
            {
                if(entryActivityRef!=null)
                Toast.makeText(entryActivityRef,"bura boş",Toast.LENGTH_SHORT).show();
            }

        }







    }






}

