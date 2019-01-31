package com.example.fatih.eksiyeniduzen;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Scene;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FavlayanlarActivity extends AppCompatActivity implements BadiEngelFragHaberlesme {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;

    BadiEngelAdapter adapter;

    List<BadiEngelProvider> list;
    String cookie,nick;
    ProgressBar spin;
    String entryID;
    String requestverify;

    String mesajAtilacakYazar;

    int favlayanlarmi;
    ImageButton cevapgonder;
    String sorunsalURL;

    FloatingActionButton img;

    ConstraintLayout ustkisim;
    Spinner sorunsalspinner;

  //  String ek;
   // private BadiEngelProvider sorunsalNesne;
    //ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //girisAnim();
       // cikisAnim();
        setContentView(R.layout.activity_favlayanlar);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.black));




//        favbutonArcAnimasyon();


   //  img= (ImageView) findViewById(R.id.transit);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cookie=getIntent().getExtras().getString("cookie");
        nick=getIntent().getExtras().getString("nick");
        entryID=getIntent().getExtras().getString("entryid");
        mesajAtilacakYazar=getIntent().getExtras().getString("yazarnick");

        favlayanlarmi=getIntent().getExtras().getInt("favmi");
        Log.d("favactivity","oncreateeeeeeeeeeeee ne zaman çalıştı");

        list =new ArrayList<>();
        ustkisim=findViewById(R.id.ustkisim);
        spin=  findViewById(R.id.favspin);
        recyclerView=  findViewById(R.id.favlayanrecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        manager=new LinearLayoutManager(FavlayanlarActivity.this,LinearLayoutManager.VERTICAL,false);
        // manager=new LinearLayoutManagerWithSmoothScroller(getActivity());
        recyclerView.setLayoutManager(manager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter=new BadiEngelAdapter(list,FavlayanlarActivity.this,cookie,nick,entryID);
        recyclerView.setAdapter(adapter);

        if(favlayanlarmi==0)
        {
            recyclerView.addItemDecoration(new DividerItemDecoration(FavlayanlarActivity.this,DividerItemDecoration.VERTICAL));
            new Favlayan().execute();
        }
        else if(favlayanlarmi==1)
        {
            TextView baslik=  findViewById(R.id.basliktextviewi);
            baslik.setText(getResources().getString(R.string.yorumlar));
            new Yorumlar().execute();

        }
        else if(favlayanlarmi==2)
        {
            boolean arsivmi=getIntent().getExtras().getBoolean("arsivmi");
            requestverify=getIntent().getExtras().getString("verify");
            TextView baslik=  findViewById(R.id.basliktextviewi);
            final EditText cevapyaz=findViewById(R.id.cevap);
             cevapgonder=findViewById(R.id.mesajgonder);


            if(!arsivmi)
            {

                ustkisim.setVisibility(View.VISIBLE);
                cevapyaz.setVisibility(View.VISIBLE);
                cevapgonder.setVisibility(View.VISIBLE);
            }

            cevapgonder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  //  Toast.makeText(FavlayanlarActivity.this,,Toast.LENGTH_SHORT).show();
                //    Log.d("favactivity",requestverify+ " "+mesajAtilacakYazar+" "+cevapyaz.getText().toString());
                    if(!cevapyaz.getText().toString().isEmpty())
                    {
                        cevapgonder.setClickable(false);
                        new MesajGonder(cookie,requestverify,mesajAtilacakYazar,cevapyaz.getText().toString(),nick,true,FavlayanlarActivity.this,null,null).execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"bir şeyler yazmaya ne dersin",Toast.LENGTH_SHORT).show();
                    }
                }
            });

           // baslik.setText(getResources().getString(R.string.mesajlasma));
            baslik.setText(mesajAtilacakYazar);

            baslik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent yazara=new Intent(FavlayanlarActivity.this,YazarActivity.class);
                    yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // yazara.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    yazara.putExtra("yazarnick", mesajAtilacakYazar);
                    yazara.putExtra("yazarid","");
                    yazara.putExtra("cookie",cookie);
                    yazara.putExtra("nick",nick);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(FavlayanlarActivity.this, R.anim.soldan_giris,R.anim.soldan_cikis).toBundle();
                    // ctx.startActivity(yazara);
                    startActivity(yazara,bundle);

                }
            });
          /*  Calendar calander = Calendar.getInstance();
            StringBuilder mesajtarih=new StringBuilder();
            String gun;

            if(calander.get(Calendar.DAY_OF_MONTH)<=9)
                gun="0"+calander.get(Calendar.DAY_OF_MONTH);
            else
                gun= String.valueOf(calander.get(Calendar.DAY_OF_MONTH));

            mesajtarih.append(gun).append(".").append(calander.get(Calendar.MONTH) + 1).append(".").append(calander.get(Calendar.YEAR))
                    .append(" ").append(calander.get(Calendar.HOUR_OF_DAY)).append(":").append(calander.get(Calendar.MINUTE));

            Log.d("mesajgönder",mesajtarih.toString());*/
          //  Log.d("favact",nick);
                new MesajlasmaCek(entryID,cookie,nick,FavlayanlarActivity.this,FavlayanlarActivity.this,spin).execute();
        }

        else if(favlayanlarmi==3)
        {
            TextView baslik=  findViewById(R.id.basliktextviewi);
            baslik.setText(getResources().getString(R.string.sorunsallar));
            sorunsalURL=getIntent().getExtras().getString("sorunsalURL");
            sorunsalspinner=findViewById(R.id.sorunsalspinner);
            ustkisim.setVisibility(View.VISIBLE);
            sorunsalspinner.setVisibility(View.VISIBLE);
            //sorunsalNesne=null;

         //   recyclerViewScrollListener();

            sorunsalspinnerTiklamaIslemi();



            new Sorunsallar(sorunsalURL+"?a=nice",cookie,nick,FavlayanlarActivity.this,FavlayanlarActivity.this,spin,false,"?a=nice").execute();

        }
        else if(favlayanlarmi==4)
        {
            TextView baslik=  findViewById(R.id.basliktextviewi);
            baslik.setText(getResources().getString(R.string.yanitlar));

         //  sorunsalNesne=getIntent().getExtras().getParcelable("sorunsalNesne");

            sorunsalURL=getIntent().getExtras().getString("sorunsalURL");

            new SorunsalYanitlar(sorunsalURL,cookie,nick,this,this,spin,false).execute();

        }









    }

    private void sorunsalspinnerTiklamaIslemi() {
        sorunsalspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            boolean ilkacilis=true;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!ilkacilis)
                {
                    String yeniurl="";

                    adapter.bosalt();
                    spin.setVisibility(View.VISIBLE);


                    switch (i)
                    {

                        case 0:

                            yeniurl=sorunsalURL+"?a=nice";

                            new Sorunsallar(yeniurl,cookie,nick,FavlayanlarActivity.this,FavlayanlarActivity.this,spin,false,"?a=nice").execute();
                            break;

                        case 1:
                            yeniurl=sorunsalURL+"?a=new";


                            new Sorunsallar(yeniurl,cookie,nick,FavlayanlarActivity.this,FavlayanlarActivity.this,spin,false,"?a=new").execute();
                            break;

                        case 2:
                            yeniurl=sorunsalURL+"?a=count";


                            new Sorunsallar(yeniurl,cookie,nick,FavlayanlarActivity.this,FavlayanlarActivity.this,spin,false,"?a=count").execute();
                            break;
                    }


                }

                ilkacilis=false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void recyclerViewScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1))
                {
                    Log.d("son","sona ulaştı");
                }

            }
        });

    }

    private void favbutonArcAnimasyon() {
        img=findViewById(R.id.fab);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup transitionsContainer=findViewById(R.id.activity_favlayanlar);
                Transition transition=TransitionInflater.from(FavlayanlarActivity.this).inflateTransition(R.transition.explode);
                TransitionManager.beginDelayedTransition(transitionsContainer, transition);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img.getLayoutParams();
                //params.gravity = (Gravity.END | Gravity.BOTTOM);
                // params.setMargins(100,200,0,0);
                // (Gravity.BOTTOM | Gravity.RIGHT);
                //  params.alignWithParent=true;
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                img.setLayoutParams(params);

            }
        });
    }

    private void girisAnim() {

        Slide sli=new Slide();

        sli.setSlideEdge(Gravity.END);
        sli.setDuration(500);

        Transition transition= TransitionInflater.from(FavlayanlarActivity.this).inflateTransition(R.transition.explode);

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(transition);

    }

    private void cikisAnim() {


        Slide slid=new Slide();

        slid.setSlideEdge(Gravity.END);
        slid.setDuration(250);

        getWindow().setReturnTransition(slid);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                this.finish();
               // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
               overridePendingTransition(0, R.anim.sagdan_cikis);
                //overridePendingTransition(R.transition.explode,R.transition.explode);
                return true;

            case R.id.soru_sor:
                Toast.makeText(getApplicationContext(),"soru sorma",Toast.LENGTH_SHORT).show();

                return true;

            case R.id.yanit_yaz:
                Toast.makeText(getApplicationContext(),"yanit verme",Toast.LENGTH_SHORT).show();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         overridePendingTransition(0, R.anim.sagdan_cikis);
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

        list.add(gonderilenmesaj);
        adapter.notifyDataSetChanged();
        cevapgonder.setClickable(true);
    }

    @Override
    public void sorunsalKur(List<BadiEngelProvider> sorunsallar, boolean devam) {

       // if (sorunsalNesne!=null)
      //  list.add(sorunsalNesne);

        if(devam)
            adapter.sonuncuyusil();

        list.addAll(sorunsallar);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.d("favactivity","burası ne zaman çalıştı");

        if(favlayanlarmi==3)// sorunsal
        {

            getMenuInflater().inflate(R.menu.menu_favlayanlar,menu);

        }
        else if(favlayanlarmi==4) // sorunsal yanıtlar
        {
            getMenuInflater().inflate(R.menu.menu_sorunsal_yanit,menu);
        }


        return true;
    }


    private class Favlayan extends AsyncTask<Void,Void,BadiEngelProvider[]>
    {


        @Override
        protected BadiEngelProvider[] doInBackground(Void... params) {

            BadiEngelProvider[] favlayanlar=null;

            Document document=null;

            try {
                document= Jsoup.connect("https://eksisozluk.com/entry/favorileyenler?entryId="+entryID)
                        .header("X-Requested-With","XMLHttpRequest")
                        .timeout(10*1000)
                        .cookie("Cookie",cookie)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document!=null)
            {

                Elements yazarlar=document.select("ul > li > a");
                favlayanlar=new BadiEngelProvider[yazarlar.size()];

                int i=0;
                for(Element yazar:yazarlar)
                {

                    String yazarnicki=yazar.text();
                    String link=yazar.attr("href");

                    favlayanlar[i]=new BadiEngelProvider("",yazarnicki,link,2);
                    i++;
                }
                if(!document.select("li.separated").isEmpty())
                {
                    favlayanlar[yazarlar.size()-1].yazarnick= favlayanlar[yazarlar.size()-1].yazarnick.replace("...","+");


                    Log.d("bu neymiş ya",favlayanlar[yazarlar.size()-1].link);

                    favlayanlar[yazarlar.size()-1]=new BadiEngelProvider("", favlayanlar[yazarlar.size()-1].yazarnick, "f",3);
                }





            }


            return favlayanlar;
        }

        @Override
        protected void onPostExecute(BadiEngelProvider[] badiEngelProviders) {
            super.onPostExecute(badiEngelProviders);

            spin.setVisibility(View.GONE);
            if(badiEngelProviders!=null)
            {

                //  adapter.notifyItemInserted(i);
                list.addAll(Arrays.asList(badiEngelProviders));

              //  adapter.notifyItemRangeChanged(0,list.size());

              //  BadiEngelProvider yorumayrac=  new BadiEngelProvider("", "devam", "s",9);
                //list.add(yorumayrac);

                adapter.notifyDataSetChanged();

                   /*
                   for(int i=0;i<badiEngelProviders.length;i++)
                    {
                       list.add(badiEngelProviders[i]);
                      //  adapter.notifyItemInserted(i);
                    }

                    */

            }

        }
    }

    private class Yorumlar extends AsyncTask<Void,Void,BadiEngelProvider[]>
    {


        @Override
        protected BadiEngelProvider[] doInBackground(Void... params) {

            BadiEngelProvider[] yorumlar=null;

            Document document=null;

            try {
                document= Jsoup.connect("https://eksisozluk.com/yorum/liste/"+entryID)
                        .header("X-Requested-With","XMLHttpRequest")
                        .timeout(10*1000)
                        .cookie("Cookie",cookie)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document!=null)
            {

                Elements tumyorumlar=document.select("ul > li");
                yorumlar=new BadiEngelProvider[tumyorumlar.size()];

                int i=0;
                for(Element yorum:tumyorumlar)
                {

                    String entryhtml=yorum.select("div.comment-content").html();

                    SpannableString entry=yorumEntrySpanla(entryhtml);


                    String yazarnicki=yorum.attr("data-author");
                    String yazarid=yorum.attr("data-author-id");
                    String entryzaman=yorum.select("a.entry-date").text();
                    String yorumid=yorum.select("footer").attr("data-id");
                    String artioysayisi=yorum.attr("data-up-vote-count");
                    String eksioysayisi=yorum.attr("data-down-vote-count");

                    Log.d("artioy",artioysayisi);
                    Log.d("eksioy",eksioysayisi);

                    yorumlar[i]=new BadiEngelProvider(entry,yazarnicki,yazarid,entryzaman,yorumid,"y",artioysayisi,eksioysayisi,4,false,false);
                    i++;
                }



            }


            return yorumlar;
        }

        private SpannableString yorumEntrySpanla(String entryhtml) {

            SpannableString message;
            Spanned x= Html.fromHtml(entryhtml);
            message=new SpannableString(x.toString());
            Object[] spans=x.getSpans(0,x.length(), Object.class);
            for(Object span:spans)
            {
                int start=x.getSpanStart(span);
                int end=x.getSpanEnd(span);
                int flags=x.getSpanFlags(span);
                StringBuilder string=new StringBuilder();

                for(int qop=start;qop<end;qop++)
                {
                    string.append(message.charAt(qop));
                }



                //  Log.d("string",string);
                if(span instanceof URLSpan)
                {
                    URLSpan urlSpan= (URLSpan) span;


                    if (!urlSpan.getURL().startsWith("http")) {
                        //  span=new CallbackSpan(urlSpan.getURL());
                        // Log.d("EntryAyikla",urlSpan.getURL());
                        if(urlSpan.getURL().equals("BOS"))
                        {
                            span=new BackgroundColorSpan(Color.parseColor("#ffff9e"));
                        }else
                        {
                            span=new LinkBaglama(urlSpan.getURL(),string.toString(),FavlayanlarActivity.this,cookie,nick);
                        }
                        //   Log.d("span",((URLSpan) span).getURL());
                    }
                    else
                    {
                        span=new LinkBaglama(urlSpan.getURL()," ",FavlayanlarActivity.this,cookie,nick);
                    }

                }

                //if(!string.equals(arananKelime))
                message.setSpan(span, start, end, flags);
                //   else
                //    message.setSpan(new BackgroundColorSpan(Color.parseColor("#ffff9e")),start,end,flags);

            }

            return message;

        }

        @Override
        protected void onPostExecute(BadiEngelProvider[] badiEngelProviders) {
            super.onPostExecute(badiEngelProviders);

            spin.setVisibility(View.GONE);
            if(badiEngelProviders!=null)
            {

                //  adapter.notifyItemInserted(i);
                list.addAll(Arrays.asList(badiEngelProviders));


                /*
                for(int i=0;i<badiEngelProviders.length;i++)
                {
                    list.add(badiEngelProviders[i]);
                    //  adapter.notifyItemInserted(i);
                }
                 */

                if(list.size()==10)
                {
                    BadiEngelProvider yorumayrac=  new BadiEngelProvider("", "sonrakiler", "",3);
                    list.add(yorumayrac);
                }




                adapter.notifyDataSetChanged();

            }

        }
    }

    private static class MesajlasmaCek extends AsyncTask<Void,Void,List<BadiEngelProvider>>
    {

        String url,cookie,nick;
        WeakReference<BadiEngelFragHaberlesme> badiEngelFragHaberlesmeWeakReference;
        WeakReference<Context> contextWeakReference;
        WeakReference<ProgressBar> progressBarWeakReference;

        MesajlasmaCek(String url,String cookie,String nick,BadiEngelFragHaberlesme badiEngelFragHaberlesme,Context context,ProgressBar progressBar)
        {
            this.cookie=cookie;
            this.url=url;
            this.nick=nick;

            badiEngelFragHaberlesmeWeakReference=new WeakReference<>(badiEngelFragHaberlesme);
            contextWeakReference=new WeakReference<>(context);
            progressBarWeakReference=new WeakReference<>(progressBar);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<BadiEngelProvider> doInBackground(Void... voids) {

           List<BadiEngelProvider> mesajlar=new ArrayList<>();

            Document document=null;

            String tumurl="https://eksisozluk.com"+url;

            try {
                document= Jsoup.connect(tumurl)
                        .timeout(10*1000)
                        .cookie("Cookie",cookie)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document!=null)
            {
                BadiEngelProvider provider;
                Elements tummesajlar=document.select("div#message-thread > article");

                SpannableString mesajtext;
                String mesajzaman;
                boolean gelenmesajmi;
                int tip;


                for(Element mesaj:tummesajlar)
                {

                    mesajtext=stringtiSpanla(mesaj.select("p").html());
                    mesajzaman=mesaj.select("time").text();

                    if(mesaj.attr("class").equals("outgoing"))
                    {
                        gelenmesajmi=false;
                        tip=7;
                    }
                    else
                    {
                        gelenmesajmi=true;
                        tip=6;
                    }



                    provider=new BadiEngelProvider(mesajtext,mesajzaman,gelenmesajmi,tip);

                    mesajlar.add(provider);
                }

            }




            return mesajlar;
        }

        private SpannableString stringtiSpanla(String texts) {

            SpannableString message;

            Spanned x= Html.fromHtml(texts);
            message=new SpannableString(x.toString());

            //  Log.d("Mess",message.toString());
            Object[] spans=x.getSpans(0,x.length(), Object.class);

            for(Object span:spans)
            {
                int start=x.getSpanStart(span);
                int end=x.getSpanEnd(span);
                int flags=x.getSpanFlags(span);
                StringBuilder string=new StringBuilder();

                for(int qop=start;qop<end;qop++)
                {
                    string.append(message.charAt(qop));
                }

                //  Log.d("string",string);

                if(span instanceof URLSpan) {

                    URLSpan urlSpan = (URLSpan) span;
                    if (!urlSpan.getURL().startsWith("http")) {
                        //  span=new CallbackSpan(urlSpan.getURL());
                        // Log.d("span",((URLSpan) span).getURL());
                        //   Log.d("string",string);
                        span = new LinkBaglama(urlSpan.getURL(), string.toString(), contextWeakReference.get(),cookie,nick);
                        //   Log.d("span",((URLSpan) span).getURL());
                    } else {
                        span = new LinkBaglama(urlSpan.getURL(), " ", contextWeakReference.get(),cookie,nick);

                        // COOKİELERİ EKLE
                    }
                }


                message.setSpan(span, start, end, flags);
                // spannableString=message;
            }

            return message;
        }


        @Override
        protected void onPostExecute(List<BadiEngelProvider> list) {
            super.onPostExecute(list);

            progressBarWeakReference.get().setVisibility(View.GONE);

            if(badiEngelFragHaberlesmeWeakReference.get()!=null)
            {
                badiEngelFragHaberlesmeWeakReference.get().mesajlistkur(list);
            }

        }
    }

    public static class Sorunsallar extends AsyncTask<Void,Void,List<BadiEngelProvider>>
    {
        String url,cookie,nick;
        WeakReference<BadiEngelFragHaberlesme> badiEngelFragHaberlesmeWeakReference;
        WeakReference<Context> contextWeakReference;
        WeakReference<ProgressBar> progressBarWeakReference;


        String spinnerSecilen;
        boolean devam;

        boolean ayracEklenecekMi;


        Sorunsallar(String url,String cookie,String nick,BadiEngelFragHaberlesme badiEngelFragHaberlesme,Context context,ProgressBar progressBar,boolean devam,String spinnerSecilen)
        {
            this.cookie=cookie;
            this.url=url;
            this.nick=nick;
            this.devam=devam;
            this.spinnerSecilen=spinnerSecilen;

            badiEngelFragHaberlesmeWeakReference=new WeakReference<>(badiEngelFragHaberlesme);
            contextWeakReference=new WeakReference<>(context);
            progressBarWeakReference=new WeakReference<>(progressBar);
        }
        @Override
        protected List<BadiEngelProvider> doInBackground(Void... voids) {
            List<BadiEngelProvider> sorunsalList=new ArrayList<>();

            Document document=null;



            String tumurl="https://eksisozluk.com"+url;

            try {
                document= Jsoup.connect(tumurl)
                        .timeout(10*1000)
                        .cookie("Cookie",cookie)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document!=null)
            {
                BadiEngelProvider provider;
                Elements sorunsallar=document.select("ul#matter-index-item-list > li");

                String sorunsalBaslik;
                SpannableString sorunsalText;
                String sorunsalOySayisi;
                StringBuilder sorunsalYanit;
                String sorunsalYanitsayisi;

                String entryzaman;
                String yazarNick;
                String sorunsalURL;
                String yazarID;
                String sorunsalID;

                String temizurl=document.select("h1#title > a").attr("href");





                for(Element sorunsal:sorunsallar)
                {

                    sorunsalYanit=new StringBuilder();
                    sorunsalText=stringtiSpanla(sorunsal.select("div.content").html());
                    sorunsalBaslik=sorunsal.select("h2 > a > span").text();
                    sorunsalOySayisi=sorunsal.attr("data-rate");
                  //  sorunsalYanit=sorunsal.select("a.answer-link-info").text();
                    sorunsalYanitsayisi=sorunsal.attr("data-answer-count");
                    yazarNick=sorunsal.select("a.entry-author").text();
                    entryzaman=sorunsal.select("a.matter-date").text();
                    sorunsalURL=sorunsal.select("a.matter-index-item-title").attr("href");
                    sorunsalID=sorunsal.attr("data-matter-id");
                    yazarID=sorunsal.attr("data-author-id");




                    if(sorunsalYanitsayisi.equals("0"))
                        sorunsalYanit.append("yanıt yok");
                    else
                        sorunsalYanit.append(sorunsalYanitsayisi).append(" yanıt");






                    provider=new BadiEngelProvider(sorunsalText,sorunsalBaslik,sorunsalOySayisi,sorunsalYanit.toString(),
                            yazarNick,entryzaman,sorunsalURL,yazarID,sorunsalID,false,false,8);

                    sorunsalList.add(provider);
                }

                if(!document.select("div.pager").isEmpty())
                {

                   ayracEklenecekMi=document.select("div.pager").attr("data-currentpage")
                            .equals(document.select("div.pager").attr("data-pagecount"));

                    if(!ayracEklenecekMi)
                    {

                        StringBuilder devamurlOlustur=new StringBuilder();




                        devamurlOlustur.append(temizurl).append(spinnerSecilen);


                        BadiEngelProvider yorumayrac=  new BadiEngelProvider(devamurlOlustur.toString(), "devam", "s",spinnerSecilen,9);
                        sorunsalList.add(yorumayrac);
                    }





                }
                else
                    Log.d("tek","tek sayfa");





            }




            return sorunsalList;
        }

        private SpannableString stringtiSpanla(String texts) {

            SpannableString message;

            Spanned x= Html.fromHtml(texts);
            message=new SpannableString(x.toString());

            //  Log.d("Mess",message.toString());
            Object[] spans=x.getSpans(0,x.length(), Object.class);

            for(Object span:spans)
            {
                int start=x.getSpanStart(span);
                int end=x.getSpanEnd(span);
                int flags=x.getSpanFlags(span);
                StringBuilder string=new StringBuilder();

                for(int qop=start;qop<end;qop++)
                {
                    string.append(message.charAt(qop));
                }

                //  Log.d("string",string);

                if(span instanceof URLSpan) {

                    URLSpan urlSpan = (URLSpan) span;
                    if (!urlSpan.getURL().startsWith("http")) {
                        //  span=new CallbackSpan(urlSpan.getURL());
                        // Log.d("span",((URLSpan) span).getURL());
                        //   Log.d("string",string);
                        span = new LinkBaglama(urlSpan.getURL(), string.toString(), contextWeakReference.get(),cookie,nick);
                        //   Log.d("span",((URLSpan) span).getURL());
                    } else {
                        span = new LinkBaglama(urlSpan.getURL(), " ", contextWeakReference.get(),cookie,nick);

                        // COOKİELERİ EKLE
                    }
                }


                message.setSpan(span, start, end, flags);
                // spannableString=message;
            }

            return message;
        }

        @Override
        protected void onPostExecute(List<BadiEngelProvider> list) {
            super.onPostExecute(list);

            if(progressBarWeakReference.get()!=null)
            progressBarWeakReference.get().setVisibility(View.GONE);

            if(badiEngelFragHaberlesmeWeakReference.get()!=null)
            {
                badiEngelFragHaberlesmeWeakReference.get().sorunsalKur(list,devam);
            }
        }
    }

     public static class SorunsalYanitlar extends AsyncTask<Void,Void,List<BadiEngelProvider>>
    {

        String url,cookie,nick;
        WeakReference<BadiEngelFragHaberlesme> badiEngelFragHaberlesmeWeakReference;
        WeakReference<Context> contextWeakReference;
        WeakReference<ProgressBar> progressBarWeakReference;



        boolean devam;

        boolean ayracEklenecekMi;

        SorunsalYanitlar(String url,String cookie,String nick,BadiEngelFragHaberlesme badiEngelFragHaberlesme,Context context,ProgressBar progressBar,boolean devam)
        {
            this.cookie=cookie;
            this.url=url;
            this.nick=nick;
            this.devam=devam;

            badiEngelFragHaberlesmeWeakReference=new WeakReference<>(badiEngelFragHaberlesme);
            contextWeakReference=new WeakReference<>(context);
            progressBarWeakReference=new WeakReference<>(progressBar);
        }


        @Override
        protected List<BadiEngelProvider> doInBackground(Void... voids) {
            List<BadiEngelProvider> sorunsalList=new ArrayList<>();

            Document document=null;



            String tumurl="https://eksisozluk.com"+url;

            try {
                document= Jsoup.connect(tumurl)
                        .timeout(10*1000)
                        .cookie("Cookie",cookie)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document!=null)
            {
                BadiEngelProvider provider;
                Elements sorunsallar=document.select("ul#matter-answer-list > li");


                SpannableString yanitText;
                String artiOySayisi;
                String eksiOySayisi;
                String yanitID;

                String entryzaman;
                String yazarNick;
                String yazarID;


               // String temizurl=document.select("h1#title > a").attr("href");


                for(Element sorunsal:sorunsallar)
                {

                    yanitText=stringtiSpanla(sorunsal.select("div.content").html());
                    artiOySayisi=sorunsal.attr("data-upvote-count");
                    eksiOySayisi=sorunsal.attr("data-downvote-count");

                    // TODO - attr downvote sayısını yanlış veriyor sorunsal.select ile eksi oy sayısını al

                    yazarNick=sorunsal.attr("data-author");
                    yazarID=sorunsal.attr("data-author-id");
                    entryzaman=sorunsal.select("a.matter-date").text();
                    yanitID=sorunsal.attr("data-matter-answer-id");



                    provider=new BadiEngelProvider(yanitText,yazarNick,yazarID,entryzaman,yanitID,"t",artiOySayisi,eksiOySayisi,4,false,false);

                    //yorumlar[i]=new BadiEngelProvider(entry,yazarnicki,yazarid,entryzaman,yorumid,"y",artioysayisi,eksioysayisi,4,false,false);


                    sorunsalList.add(provider);
                }

                if(!document.select("div.pager").isEmpty())
                {
                    String temizurl=document.select("h1#title > a").attr("href");

                    ayracEklenecekMi=document.select("div.pager").attr("data-currentpage")
                            .equals(document.select("div.pager").attr("data-pagecount"));

                    if(!ayracEklenecekMi)
                    {
                        BadiEngelProvider yorumayrac=  new BadiEngelProvider(temizurl, "devam", "t","",9);
                        sorunsalList.add(yorumayrac);
                    }





                }
                else
                    Log.d("tek","tek sayfa");







            }




            return sorunsalList;
        }

        private SpannableString stringtiSpanla(String texts) {

            SpannableString message;

            Spanned x= Html.fromHtml(texts);
            message=new SpannableString(x.toString());

            //  Log.d("Mess",message.toString());
            Object[] spans=x.getSpans(0,x.length(), Object.class);

            for(Object span:spans)
            {
                int start=x.getSpanStart(span);
                int end=x.getSpanEnd(span);
                int flags=x.getSpanFlags(span);
                StringBuilder string=new StringBuilder();

                for(int qop=start;qop<end;qop++)
                {
                    string.append(message.charAt(qop));
                }

                //  Log.d("string",string);

                if(span instanceof URLSpan) {

                    URLSpan urlSpan = (URLSpan) span;
                    if (!urlSpan.getURL().startsWith("http")) {
                        //  span=new CallbackSpan(urlSpan.getURL());
                        // Log.d("span",((URLSpan) span).getURL());
                        //   Log.d("string",string);
                        span = new LinkBaglama(urlSpan.getURL(), string.toString(), contextWeakReference.get(),cookie,nick);
                        //   Log.d("span",((URLSpan) span).getURL());
                    } else {
                        span = new LinkBaglama(urlSpan.getURL(), " ", contextWeakReference.get(),cookie,nick);

                        // COOKİELERİ EKLE
                    }
                }


                message.setSpan(span, start, end, flags);
                // spannableString=message;
            }

            return message;
        }

        @Override
        protected void onPostExecute(List<BadiEngelProvider> list) {
            super.onPostExecute(list);

            if(progressBarWeakReference.get()!=null)
                progressBarWeakReference.get().setVisibility(View.GONE);

            if(badiEngelFragHaberlesmeWeakReference.get()!=null)
            {
                badiEngelFragHaberlesmeWeakReference.get().sorunsalKur(list,devam);
            }

        }
    }

}


