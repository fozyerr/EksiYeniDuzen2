package com.example.fatih.eksiyeniduzen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EntryActivity extends AppCompatActivity implements InterfaceArama,EntryFragmenttenActivitye {


    public SectionsPagerAdapter mSectionsPagerAdapter;

    public ViewPager mViewPager;

    ImageButton sol,sag;

    Toolbar toolbar;

    ListView listView;

    RelativeLayout sayfaNumaraSec;
    String SayfaUrl;
    String Ek;
    boolean yonlendirme;
    int sayfaya;
    String COOKIE;
    String NICK;

    String gelenstring;
    String arananKelime;

    String entrysayi_baslik;

    SecenekAdapter secenekAdapter;

    FloatingActionButton videobutonu;
    Animation goster,gizle;
    boolean duzenlemnecekmi;
    boolean videovarmi;
    boolean arsiv;
    String videoURL;
    int ilkler;
    RelativeLayout seritlayout;

    ArrayList<EntryProvider> arsiventryleri;
    TextView maxtext;

    boolean takipediliyomu;

    boolean baskaUygulamadangelme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  setTheme(R.style.GeceTheme);

        setContentView(R.layout.activity_entry);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(null);

        seritlayout= findViewById(R.id.sayfaci);


        Intent globalapp=getIntent();

        if(Intent.ACTION_SEND.equals(globalapp.getAction()) && globalapp.getType()!=null)
        {
            if("text/plain".equals(globalapp.getType()))
            {
                Log.d("diğer uygulamadan gelen",globalapp.getStringExtra(Intent.EXTRA_TEXT));

                baskaUygulamadangelme=true;


            }

            Log.d("herzaman","giriliyomu");
        }
        else
            baskaUygulamadangelme=false;

        if(baskaUygulamadangelme)
        {

        }
        else
        {

            arsiv=getIntent().getExtras().getBoolean("arsiv");



            SayfaUrl=getIntent().getExtras().getString("link");
            maxtext= findViewById(R.id.maxtext);
            COOKIE=getIntent().getExtras().getString("cookie");
            NICK=getIntent().getExtras().getString("nick");
            if(arsiv)
            {


                new ArsivdenEntryGetir(EntryActivity.this,COOKIE,NICK).execute(SayfaUrl);
            }




            entrysayi_baslik=getIntent().getExtras().getString("entrysayi");

            // getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);




            duzenlemnecekmi =getIntent().getExtras().getBoolean("urlduzenle");
            statusbarRenkDegis();

            TextView basligikur=  findViewById(R.id.basliktextviewi);

            gelenstring=getIntent().getExtras().getString("gelenstring");
            arananKelime=getIntent().getExtras().getString("mark");

            Log.d("Entryactivity",gelenstring);

            if(gelenstring.equals("*"))
            {
                if(Uri.decode(SayfaUrl).contains("="))
                {
                    gelenstring= Uri.decode(SayfaUrl);
                    gelenstring=gelenstring.split("=")[1].replaceAll("\\+"," ");
                }
            }


            //  Log.d("STRING",gelenstring);
            basligikur.setText(gelenstring);

       /* if(duzenlemnecekmi)
            new UrlDuzenle(SayfaUrl,getIntent().getExtras().getString("gelenstring")).execute();
        else
        {*/
            if(!arsiv)
                entryActivityKurulumu();

            //   }


        }





    }



    private void entryActivityKurulumu() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sol=  findViewById(R.id.sol);
        sag= findViewById(R.id.sag);
        videobutonu=  findViewById(R.id.videobuton);
        sol.setVisibility(View.GONE);
        sayfaNumaraSec= findViewById(R.id.gosterge);

        goster= AnimationUtils.loadAnimation(EntryActivity.this,R.anim.fab_open);
        gizle=AnimationUtils.loadAnimation(EntryActivity.this,R.anim.fab_kapat);
        videovarmi=false;
        ilkler=0;

        sagsolButonlar();
        numberPicker();

        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSectionsPagerAdapter.setToplamsayfa(1);
        mSectionsPagerAdapter.notifyDataSetChanged();
        viewPagerListener();


        yonlendirme=getIntent().getExtras().getBoolean("yonlendirme");
        sayfaya=getIntent().getExtras().getInt("gidileceksayfa");

        //Log.d("yonlendirmevarmi", String.valueOf(yonlendirme));

        videobutonutiklama();


        //SayfaUrl="https://eksisozluk.com"+SayfaUrl;

        //SayfaUrl.contains("?a=popular")||SayfaUrl.contains("?a=dailynice")||SayfaUrl.contains("?a=nice") || SayfaUrl.contains("?day=") || SayfaUrl.contains("?a=search") || SayfaUrl.contains("?searchform")



        if(SayfaUrl.contains("?a=")|| SayfaUrl.contains("?day=") || SayfaUrl.contains("?searchform") || Uri.decode(SayfaUrl).contains("@") )
        {
            Ek="&p=";
        }
        else
        {
            Ek="?p=";
        }
    }

    private void arsivicinKurulum()
    {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sol=  findViewById(R.id.sol);
        sag= findViewById(R.id.sag);
        videobutonu= findViewById(R.id.videobuton);
        sol.setVisibility(View.GONE);
        sayfaNumaraSec=  findViewById(R.id.gosterge);
        sagsolButonlar();
        numberPicker();



        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSectionsPagerAdapter.setToplamsayfa(1);
        mSectionsPagerAdapter.notifyDataSetChanged();
        viewPagerListener();
    }

    private void statusbarRenkDegis() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.black));
    }

    private void videobutonutiklama() {

        videobutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(videoURL.contains("59saniye"))
                {
                    String uri="https:"+videoURL;
                    new VideoKaynak(uri,EntryActivity.this).execute();
                }
                else
                {
                    Log.d("url",videoURL);

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
                  //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }




             /*   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);*/

           /*     Dialog videoDialog=new Dialog(EntryActivity.this);

                String uri="https:"+videoURL;
                Uri uri1=Uri.parse("https://static.59saniye.com/videos/2014/07/17/20140717173301-5918_360.mp4");

                videoDialog.setContentView(R.layout.video_diyalog);
                videoDialog.setCancelable(true);

                 VideoView videoView= (VideoView) videoDialog.findViewById(R.id.video);
                MediaController mediaController=new MediaController(EntryActivity.this);
                videoView.setVideoURI(uri1);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);

                videoView.start();






                videoDialog.show();*/

            }
        });

    }

    private void numberPicker() {



        sayfaNumaraSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater inflator = LayoutInflater.from(EntryActivity.this);
                @SuppressLint("InflateParams") View goster = inflator.inflate(R.layout.sayfasecme, null);
                final NumberPicker sayac =  goster.findViewById(R.id.sayisecici);
                sayac.setMaxValue(mSectionsPagerAdapter.getToplamsayfa());
                sayac.setMinValue(1);


//                Log.d("deneme entrysay",getSayfagecis());

                AlertDialog.Builder builder = new AlertDialog.Builder(EntryActivity.this);
                builder.setTitle("Sayfaya Git")
                        .setView(goster)
                        .setPositiveButton("Git", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewPager.setCurrentItem(sayac.getValue() - 1);

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
                b.setTextColor(Color.parseColor("#33B4E4"));



            }
        });

    }

    private void sagsolButonlar() {


        sag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mSectionsPagerAdapter.getToplamsayfa()-1);
            }
        });


        sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(0);
            }
        });
    }

    private void viewPagerListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                TextView textView2 = findViewById(R.id.textt);
                textView2.setText(String.valueOf(position + 1));

              EntrylerFragment entrylerFragment= (EntrylerFragment) mSectionsPagerAdapter.findFragmentByPosition(position,R.id.container);

              if(position>=1 && mSectionsPagerAdapter.getYenilenmisURL()!=null)
              {
                  entrylerFragment.activitydenulasma(mSectionsPagerAdapter.getYenilenmisURL());

                  Log.d("FINDFRAG","BURASI KULLANILIYO MU");


              }


                if (position + 1 == 1) {
                    sol.setVisibility(View.GONE);
                    sag.setVisibility(View.VISIBLE);

                }
                else if (position + 1 == mSectionsPagerAdapter.getToplamsayfa()) {
                    sag.setVisibility(View.GONE);
                    sol.setVisibility(View.VISIBLE);


                }
                else {

                    sag.setVisibility(View.VISIBLE);
                    sol.setVisibility(View.VISIBLE);
                }


                if(position==0 && videovarmi)
                {
                 videobutonu.animate().setDuration(300).translationY(0);
                    videobutonu.setVisibility(View.VISIBLE);
                }
                else
                {
                   videobutonu.animate().translationY(videobutonu.getHeight()*2);
                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return true;
    }

 /*   @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.sagdan_giris, R.anim.sagdan_cikis);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.sagdan_cikis);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id)
        {

            case R.id.arama:

                findViewById(R.id.arama).setVisibility(View.GONE);

                final TextView textView=  toolbar.getRootView().findViewById(R.id.basliktextviewi);
                final RelativeLayout relativeLayout=  toolbar.getRootView().findViewById(R.id.ust);
                final AutoCompleteTextView editText=toolbar.getRootView().findViewById(R.id.aramayeri);
                ImageButton kapa= toolbar.getRootView().findViewById(R.id.kapa);
                textView.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);


                editText.requestFocus();
                InputMethodManager imgr = (InputMethodManager) EntryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

                AutocompleteTextviewListener autocompleteTextviewListener=new AutocompleteTextviewListener(editText,EntryActivity.this,COOKIE,NICK);

                editText.addTextChangedListener(autocompleteTextviewListener);
                editText.setOnItemClickListener(autocompleteTextviewListener);
                editTextButonaramasi(editText);

                kapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(editText.getText().toString().equals(""))
                        {
                            InputMethodManager imgr = (InputMethodManager) EntryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            relativeLayout.setVisibility(View.GONE);
                            findViewById(R.id.arama).setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            editText.setText("");
                        }



                    }
                });


                return true;

            case android.R.id.home:
                this.finish();
                overridePendingTransition(0, R.anim.sagdan_cikis);
                return true;
        }




        return super.onOptionsItemSelected(item);
    }

    private void editTextButonaramasi(final AutoCompleteTextView editText) {


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    String kelime=editText.getText().toString();

                    if(!kelime.equals(""))
                    {
                      /*  Fragment fragment ;


                        fragment=new FragmentAramaMain().newInstance(girisCOOKIE,kelime);

                        if (fragment != null) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
                        }*/
                        Intent aramayagec=new Intent(EntryActivity.this,AramaActivity.class);
                        // aramayagec.putExtra("fragment",R.id.ara);
                        aramayagec.putExtra("kelime",kelime);
                        aramayagec.putExtra("cookie",COOKIE);
                        aramayagec.putExtra("nick",NICK);
                        startActivity(aramayagec);

                    }

                    editText.dismissDropDown();

                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void yazarprofiliVerifygonder(String mesajverify) {

    }

    @Override
    public void updateView(boolean video, String videoURL) {


        videovarmi=video;
        this.videoURL=videoURL;

        if(video && ilkler==0)
        {
            videobutonu.animate().setDuration(300).translationY(0);
            videobutonu.setVisibility(View.VISIBLE);
        }


        ilkler=1;

    }

    @Override
    public void sayfayayonlendir(int yonlendirileceksayfa) {

        mViewPager.setCurrentItem(yonlendirileceksayfa,true);

    }

    @Override
    public void viewpagerToplamKacSayfa(int sayfasayisi) {

        mSectionsPagerAdapter.setToplamsayfa(sayfasayisi);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void sectionAdaptorUrlKur(String sectionurl) {
        mSectionsPagerAdapter.setYenilenmisURL(sectionurl);
    }

    @Override
    public void seritlayoutGorunurlugu(int toplamsayfa) {
        String text=" / "+toplamsayfa;
        maxtext.setText(text);

        if(toplamsayfa>1)
            seritlayout.setVisibility(View.VISIBLE);
        else
            seritlayout.setVisibility(View.GONE);
    }

    void baslikTiklamDinleyici(final Bundle basliktiklamaBundle)
    {
        TextView basliktextview =  findViewById(R.id.basliktextviewi);
        basliktextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //  SecenekAdapter adapt;

                final BottomSheetDialog dialog;



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
                final boolean sorunsalVarMi=basliktiklamaBundle.getBoolean("sorunsal");
                final String sorunsaltext=basliktiklamaBundle.getString("sorunsalText");

                LayoutInflater inflator = LayoutInflater.from(EntryActivity.this);
                @SuppressLint("InflateParams") View goster = inflator.inflate(R.layout.seceneklerlist, null);
                listView =  goster.findViewById(R.id.listView6);
                TextView texts =  goster.findViewById(R.id.baslik1);
                texts.setText(baslik);
                Animation animation=AnimationUtils.loadAnimation(EntryActivity.this,R.anim.yenianim);

                boolean kayityvarmi=servisikontrolet(baslikID);

                //   Animation animation = AnimationUtils.loadAnimation(entryActivity, R.anim.yenianim);


                if (!COOKIE.equals("BOS")) {

                    //listView = (ListView) goster.findViewById(R.id.listView6);
                    secenekAdapter= new SecenekAdapter(EntryActivity.this, R.layout.seceneklistyapisi);
                    // adapt = secenekAdapter;
                    int id[] = {R.drawable.paylas,
                            R.drawable.sukela,
                            R.drawable.yaz,
                            R.drawable.takipet,
                            R.drawable.sorunsal,
                            R.drawable.basliktaara,
                            R.drawable.tumunugoster,
                            R.drawable.kaydet
                    };

                    if (takipediliyomu) {
                        id[3] = R.drawable.takipetsecili;
                    } else {
                        id[3] = R.drawable.takipet;
                    }
                    if (kenardaentryVarMi)
                    {
                        id[2]=R.drawable.ic_sorunsal_entry_yaz_yeni;
                    }
                    else
                    {
                        id[2]=R.drawable.ic_sorunsal_entry_yaz_gri;
                    }

                    if(sorunsalVarMi)
                        id[4]=R.drawable.sorunsalvar;

                    StringBuilder kacentryvar=new StringBuilder();
                    kacentryvar.append("Tümünü Göster \t ( ").append(tumsayfasayikontrol).append(" )");
                    String[] dizi;




                    if(tumsayfasayikontrol!=0)
                        dizi=new String[]{"Paylaş","Şükela", "Yaz", "Takip",sorunsaltext, "Başlıkta ara", kacentryvar.toString(), "Kaydet"};
                    else
                        dizi=new String[]{"Paylaş","Şükela", "Yaz", "Takip",sorunsaltext, "Başlıkta ara", "Kaydet"};





                    secenekAdapter.add(new SecenekProvider(id[0], dizi[0]));
                    secenekAdapter.add(new SecenekProvider(id[1], dizi[1]));
                    secenekAdapter.add(new SecenekProvider(id[2], dizi[2]));

                    secenekAdapter.add(new SecenekProvider(id[3], dizi[3]));
                    if(!sorunsaltext.equals("yok"))
                    secenekAdapter.add(new SecenekProvider(id[4], dizi[4]));

                    secenekAdapter.add(new SecenekProvider(id[5],dizi[5]));
                    if(tumsayfasayikontrol!=0)
                    {
                        if(kayityvarmi)
                        {
                            id[7]=R.drawable.sil;
                            dizi[7]="Arşivden sil";
                        }
                        else
                        {
                            id[7]=R.drawable.kaydet;
                            dizi[7]="Kaydet";
                        }

                        secenekAdapter.add(new SecenekProvider(id[6], dizi[6]));
                        secenekAdapter.add(new SecenekProvider(id[7], dizi[7]));
                    }else
                    {
                        if(kayityvarmi)
                        {
                            id[7]=R.drawable.sil;
                            dizi[6]="Arşivden sil";
                        }
                        else
                        {
                            id[7]=R.drawable.kaydet;
                            dizi[6]="Kaydet";
                        }

                        secenekAdapter.add(new SecenekProvider(id[7], dizi[6]));
                    }





                    listView.setAnimation(animation);



                }

                else {

                    //  listView = (ListView) goster.findViewById(R.id.listView6);
                    secenekAdapter = new SecenekAdapter(EntryActivity.this, R.layout.seceneklistyapisi);
                    // adapt = secenekAdapter;
                    int id[] = {
                            R.drawable.paylas,
                            R.drawable.sukela,
                            R.drawable.sorunsal,
                            R.drawable.tumunugoster,
                            R.drawable.kaydet
                    };
                    StringBuilder kacentryvar=new StringBuilder();
                    kacentryvar.append("Tümünü Göster \t ( ").append(tumsayfasayikontrol).append(" )");
                    String[] dizi;

                    if(sorunsalVarMi)
                        id[2]=R.drawable.sorunsalvar;

                    if(tumsayfasayikontrol!=0)
                        dizi=new String[]{"Paylaş","Şükela",sorunsaltext, kacentryvar.toString(), "Kaydet"};
                    else
                        dizi=new String[]{"Paylaş","Şükela",sorunsaltext,"Kaydet"};


                    secenekAdapter.add(new SecenekProvider(id[0], dizi[0]));
                    secenekAdapter.add(new SecenekProvider(id[1], dizi[1]));
                    secenekAdapter.add(new SecenekProvider(id[2], dizi[2]));
                    if(tumsayfasayikontrol!=0)
                    {
                        if(kayityvarmi)
                        {
                            id[4]=R.drawable.sil;
                            dizi[4]="Arşivden sil";
                        }
                        else
                        {
                            id[4]=R.drawable.kaydet;
                            dizi[4]="Kaydet";
                        }


                        secenekAdapter.add(new SecenekProvider(id[3], dizi[3]));
                        secenekAdapter.add(new SecenekProvider(id[4], dizi[4]));
                    }else
                    {
                        if(kayityvarmi)
                        {
                            id[4]=R.drawable.sil;
                            dizi[3]="Arşivden sil";
                        }
                        else
                        {
                            id[4]=R.drawable.kaydet;
                            dizi[3]="Kaydet";
                        }
                        secenekAdapter.add(new SecenekProvider(id[4], dizi[3]));
                    }



                }

                listView.setAdapter(secenekAdapter);

                if(!COOKIE.equals("BOS"))
                    listView.startAnimation(animation);

                dialog = new BottomSheetDialog(EntryActivity.this, R.style.BottomSheetDialog);
                dialog.setContentView(goster);
                //    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
                dialog.show();



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        SecenekProvider provider= (SecenekProvider) parent.getItemAtPosition(position);

                        if(provider.secenek.contains("Tüm"))
                        {


                            int sayia=1+tumsayfasayikontrol/10;
                            Intent tekentry=new Intent(EntryActivity.this,EntryActivity.class);
                            tekentry.putExtra("link",urlyenile.split("\\?focusto")[0]);
                            tekentry.putExtra("yonlendirme",true);
                            tekentry.putExtra("gidileceksayfa",sayia);
                            tekentry.putExtra("cookie",COOKIE);
                            tekentry.putExtra("nick",NICK);
                            tekentry.putExtra("mark"," ");
                            tekentry.putExtra("gelenstring",baslik);
                            tekentry.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // Log.d("Tümünü Göster",urlyenile.split("\\?focusto")[0]);


                           startActivity(tekentry);
                            dialog.dismiss();
                        }
                        else if(provider.secenek.contains("Tak"))
                        {

                           new BaslikTakipArkaplan(baslikID, provider,COOKIE,takipediliyomu,secenekAdapter,EntryActivity.this,getApplicationContext()).execute();

                            dialog.dismiss();
                        }
                        else if(provider.secenek.contains("Baş"))
                        {
                            basliktaaradialog(kenarIıcnUrl,baslik);
                            dialog.dismiss();
                        }
                        else if(provider.secenek.contains("Yaz"))
                        {
                            Intent entry=new Intent(EntryActivity.this,EntryYazmaActivity.class);
                            entry.putExtra("title",baslik);
                            entry.putExtra("baslikid",baslikID);
                            entry.putExtra("verify",entryverify);
                            entry.putExtra("cookie",COOKIE);
                            entry.putExtra("nick",NICK);
                            entry.putExtra("text",kenardakiEntry);
                            entry.putExtra("editid","");
                            entry.putExtra("edit",false);
                            entry.putExtra("temizurl",kenarIıcnUrl);
                            entry.putExtra("kenardaEntryVarMi",kenardaentryVarMi);
                            startActivity(entry);

                            dialog.dismiss();
                        }
                        else if(provider.secenek.contains("Şük"))
                        {
                            sukelatiklama(kenarIıcnUrl,baslik);
                            dialog.dismiss();

                        }
                        else if(provider.secenek.contains("Pay"))
                        {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = "https://eksisozluk.com"+kenarIıcnUrl;
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Deneme Yaallaaa");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Başlık Paylaş"));

                            dialog.dismiss();
                        }
                        else if(provider.secenek.contains("Kay"))
                        {
                            String servisurl;
                            if(!urlDuzenle)
                            {
                                servisurl=SayfaUrl;

                            }
                            else
                            {
                                if(toplamSayfa==1)
                                {
                                    servisurl=SayfaUrl;
                                }else
                                {
                                    servisurl=temizurl;
                                }
                            }

                            Intent servis=new Intent(EntryActivity.this,ServisArsivleme.class);
                            servis.putExtra("url",servisurl);
                            servis.putExtra("cookie",COOKIE);
                            servis.putExtra("dongu",toplamSayfa);
                            servis.putExtra("ek",Ek);
                            servis.putExtra("baslik",baslik);
                            servis.putExtra("baslikid",baslikID);
                            servis.putExtra("nick",NICK);
                            startService(servis);
                            dialog.dismiss();
                        }
                        else if(provider.secenek.contains("sor"))
                        {
                            Log.d("sorunsal",provider.secenek);
                            Log.d("sorunsalurl",basliktiklamaBundle.getString("sorunsalURL"));

                           // if(!COOKIE.equals("BOS"))
                        //    {
                                Intent favlayanlar=new Intent(EntryActivity.this,FavlayanlarActivity.class);
                                favlayanlar.putExtra("cookie",COOKIE);
                                favlayanlar.putExtra("nick",NICK);
                                favlayanlar.putExtra("sorunsalURL",basliktiklamaBundle.getString("sorunsalURL"));
                                favlayanlar.putExtra("favmi",3);
                                //  Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                                Bundle bundle = ActivityOptions.makeCustomAnimation(EntryActivity.this, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                                startActivity(favlayanlar,bundle);
                         //   }
                         //   else
                             //   Toast.makeText(getApplicationContext(),"giriş yapmadan soru soramazsın",Toast.LENGTH_SHORT).show();


                            dialog.dismiss();

                        }

                        else if(provider.secenek.contains("Arş"))
                        {

                            new KayitSilme(EntryActivity.this).execute(baslikID);
                            dialog.dismiss();
                        }

                    }
                });



                // listviewlistener(adapt);
            }
        });

    }

    private void basliktaaradialog(final String kenarIıcnUrl,final String baslik) {

        LayoutInflater inflator=LayoutInflater.from(EntryActivity.this);
        final AlertDialog.Builder builder=new AlertDialog.Builder(EntryActivity.this);

        @SuppressLint("InflateParams") final View goster=inflator.inflate(R.layout.basliktaaramalayout, null);
        final AutoCompleteTextView editt= goster.findViewById(R.id.aramaedit);
        Button bugunbut=  goster.findViewById(R.id.bugunentryler);
        Button eksiseyler=  goster.findViewById(R.id.sukelaentry);
        Button linkbut=  goster.findViewById(R.id.linkentry);
        Button badiler= goster.findViewById(R.id.badientryler);
        Button benimkiler=  goster.findViewById(R.id.benimkiler);


        editt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!editt.getText().toString().equals(""))
                    new AramaIslemi(editt.getText().toString(),editt,EntryActivity.this,2,baslik).execute();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });







        builder.setTitle("Başlıkta Ara")
                .setView(goster)
                .setPositiveButton("Ara", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url;
                        if(!editt.getText().toString().equals(" ")&&!editt.getText().toString().equals(""))
                        {
                            if(editt.getText().toString().contains("@"))
                            {
                                url=kenarIıcnUrl+"?a=search&author="+editt.getText().toString().split("@")[1].trim();

                            }
                            else
                            {
                                url=kenarIıcnUrl+"?a=find&keywords="+editt.getText().toString();
                            }

                            Log.d("başlıkta ara",url);
                            Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                            gitt.putExtra("cookie",COOKIE);
                            gitt.putExtra("nick",NICK);
                            gitt.putExtra("yonlendirme",false);
                            gitt.putExtra("gidileceksayfa",0);
                            gitt.putExtra("gelenstring",baslik);
                            gitt.putExtra("mark",editt.getText().toString());
                            gitt.putExtra("link",url);
                            startActivity(gitt);



                        }
                        else
                            Toast.makeText(getApplicationContext(),"arama yapmak için bir şeyler yazmalısın",Toast.LENGTH_SHORT).show();


                    }
                });

        final AlertDialog alarm=builder.create();
        alarm.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        badiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=kenarIıcnUrl+"?a=buddy";

                Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                gitt.putExtra("cookie",COOKIE);
                gitt.putExtra("nick",NICK);
                gitt.putExtra("yonlendirme",false);
                gitt.putExtra("gidileceksayfa",0);
                gitt.putExtra("link",url);
                gitt.putExtra("mark"," ");
                gitt.putExtra("gelenstring",baslik);
                startActivity(gitt);
                alarm.dismiss();

            }
        });

        bugunbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // BUGÜNÜN TARHİNİ ALIP URL YE EKLE



                int gun= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int ay=Calendar.getInstance().get(Calendar.MONTH);
                int yil=Calendar.getInstance().get(Calendar.YEAR);

                StringBuilder tarihbuild=new StringBuilder();

                tarihbuild.append(yil).append("-").append(ay+1).append("-").append(gun);

                String url=kenarIıcnUrl+"?day="+tarihbuild.toString();

                Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                gitt.putExtra("cookie",COOKIE);
                gitt.putExtra("nick",NICK);
                gitt.putExtra("yonlendirme",false);
                gitt.putExtra("gidileceksayfa",0);
                gitt.putExtra("gelenstring",baslik);
                gitt.putExtra("mark"," ");
                gitt.putExtra("link",url);
                startActivity(gitt);
                alarm.dismiss();

            }
        });
        eksiseyler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=kenarIıcnUrl+"?a=eksiseyler";

                Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                gitt.putExtra("cookie",COOKIE);
                gitt.putExtra("nick",NICK);
                gitt.putExtra("yonlendirme",false);
                gitt.putExtra("gidileceksayfa",0);
                gitt.putExtra("gelenstring",baslik);
                gitt.putExtra("mark"," ");
                gitt.putExtra("link",url);
                startActivity(gitt);
                alarm.dismiss();


            }
        });
        linkbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=kenarIıcnUrl+"?a=find&keywords=http%3a%2f%2f";

                Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                gitt.putExtra("cookie",COOKIE);
                gitt.putExtra("nick",NICK);
                gitt.putExtra("yonlendirme",false);
                gitt.putExtra("gidileceksayfa",0);
                gitt.putExtra("gelenstring",baslik);
                gitt.putExtra("mark"," ");
                gitt.putExtra("link",url);
                startActivity(gitt);
                alarm.dismiss();


            }
        });

        benimkiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=kenarIıcnUrl+"?a=search&author="+ Uri.encode(NICK);

                Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                gitt.putExtra("cookie",COOKIE);
                gitt.putExtra("nick",NICK);
                gitt.putExtra("yonlendirme",false);
                gitt.putExtra("gidileceksayfa",0);
                gitt.putExtra("gelenstring",baslik);
                gitt.putExtra("mark"," ");
                gitt.putExtra("link",url);
                startActivity(gitt);
                alarm.dismiss();
            }
        });



        alarm.setView(goster);

        Window window=alarm.getWindow();
        assert window != null;
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.gravity= Gravity.CENTER;
        window.setAttributes(layoutParams);



        alarm.show();
        Button b=alarm.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setTextColor(Color.parseColor("#3553C3"));


    }

    private void sukelatiklama(final String temizurl,final String baslik) {

        List<String> secenekler = new ArrayList<>();
        secenekler.add("tümü");
        secenekler.add("bugün");
        //Create sequence of items
        final CharSequence[] Animals = secenekler.toArray(new String[secenekler.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EntryActivity.this);
        dialogBuilder.setTitle("Şükela");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //String selectedText = Animals[item].toString();  //Selected item in listview

                if (item == 0) {

                    String url=temizurl+"?a=nice";

                    Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                    gitt.putExtra("cookie",COOKIE);
                    gitt.putExtra("nick",NICK);
                    gitt.putExtra("yonlendirme",false);
                    gitt.putExtra("gidileceksayfa",0);
                    gitt.putExtra("link",url);
                    gitt.putExtra("mark"," ");
                    gitt.putExtra("gelenstring",baslik);
                    startActivity(gitt);
                    dialog.dismiss();
                }
                else
                {
                    String url=temizurl+"?a=dailynice";

                    Intent gitt=new Intent(EntryActivity.this,EntryActivity.class);
                    gitt.putExtra("cookie",COOKIE);
                    gitt.putExtra("nick",NICK);
                    gitt.putExtra("yonlendirme",false);
                    gitt.putExtra("gidileceksayfa",0);
                    gitt.putExtra("link",url);
                    gitt.putExtra("mark"," ");
                    gitt.putExtra("gelenstring",baslik);
                    startActivity(gitt);
                    dialog.dismiss();

                }

            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();

        //   alertDialogObject.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialogObject.getWindow().setWindowAnimations(R.style.DialogAnimation);
        //Show the dialog
        alertDialogObject.show();

    }

    private boolean servisikontrolet(String arananbaslik) {

         /*   ActivityManager manager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if("com.example.fatih.eksiyeniduzen.ServisArsivleme".equals(service.service.getClassName())) {
                    return true;
                }
            }
            VeriTabaniBaglanti vt=new VeriTabaniBaglanti(getActivity());

            vt.okumakicinac();

            ContentProvider contentProvider;
*/
        VeriTabaniBaglanti vt=new VeriTabaniBaglanti(EntryActivity.this);

        vt.okumakicinac();

        if(vt.baslikVarMi(arananbaslik))
        {
            vt.veritabaniKapat();
            return true;
        }
        else
        {
            vt.veritabaniKapat();
            return false;
        }



    }



    @Override
    public void basligiKur(String baslikString) {
        TextView basliktextview =  findViewById(R.id.basliktextviewi);
        basliktextview.setText(baslikString);
    }

    @Override
    public void basliktiklamaBundleGonder(Bundle bundle) {

        baslikTiklamDinleyici(bundle);

    }

    @Override
    public void takipedildimi(boolean takipediliyomu) {
        this.takipediliyomu=takipediliyomu;
    }

    @Override
    public void arsivbasliktiklamadinleyici() {
        TextView basliktextview =  findViewById(R.id.basliktextviewi);

        Log.d("arşivent",arsiventryleri.get(0).yazar);

        arsivbaslikTiklama(basliktextview,arsiventryleri.get(0).baslikid);
    }

    @Override
    public void arsiventryleriKur(ArrayList<EntryProvider> entryProviderArrayList) {

        this.arsiventryleri=entryProviderArrayList;
    }

    @Override
    public void arsivicikurulum() {
        arsivicinKurulum();
    }

    void arsivbaslikTiklama(TextView basliktext,final String baslikID)
    {
        basliktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //  SecenekAdapter adapt;

                final BottomSheetDialog dialog;



                LayoutInflater inflator = LayoutInflater.from(EntryActivity.this);
                @SuppressLint("InflateParams") View goster = inflator.inflate(R.layout.seceneklerlist, null);
                listView =  goster.findViewById(R.id.listView6);
                TextView texts =  goster.findViewById(R.id.baslik1);
                texts.setText(gelenstring);
                //   Animation animation = AnimationUtils.loadAnimation(entryActivity, R.anim.yenianim);


                //listView = (ListView) goster.findViewById(R.id.listView6);
                secenekAdapter= new SecenekAdapter(EntryActivity.this, R.layout.seceneklistyapisi);
                // adapt = secenekAdapter;
                int id[] = {R.drawable.paylas,
                        R.drawable.sil
                };


                String[] dizi=new String[]{"Paylaş", "Arşivden Sil"};





                secenekAdapter.add(new SecenekProvider(id[0], dizi[0]));
                secenekAdapter.add(new SecenekProvider(id[1], dizi[1]));




                listView.setAdapter(secenekAdapter);


                dialog = new BottomSheetDialog(EntryActivity.this, R.style.BottomSheetDialog);
                dialog.setContentView(goster);
                //    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
                dialog.show();



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        SecenekProvider provider= (SecenekProvider) parent.getItemAtPosition(position);


                        if(position==0)
                        {
                             /*   Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = "https://eksisozluk.com"+kenarIıcnUrl;
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Deneme Yaallaaa");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                              startActivity(Intent.createChooser(sharingIntent, "Başlık Paylaş"));*/

                            dialog.dismiss();
                        }
                        else if(position==1)
                        {

                            new KayitSilme(EntryActivity.this).execute(baslikID);
                            dialog.dismiss();
                        }

                    }
                });



                // listviewlistener(adapt);
            }
        });

    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        FragmentManager findFragmentByTag;

         SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.findFragmentByTag=fm;
        }


        private int toplamsayfa;

        public String getYenilenmisURL() {
            return yenilenmisURL;
        }

        public void setYenilenmisURL(String yenilenmisURL) {
            this.yenilenmisURL = yenilenmisURL;
        }

        private String yenilenmisURL;

        @Override
        public Fragment getItem(int position) {

            if(arsiv)
            {
                ArrayList<EntryProvider> entryProviderArrayList=new ArrayList<>();
                int ee=position+1;
                int baslangic=position*10;
                int bitis=(position+1)*10;

                if(bitis>arsiventryleri.size())
                    bitis=arsiventryleri.size();



                for(int i=baslangic;i<bitis;i++)
                {
                  entryProviderArrayList.add(arsiventryleri.get(i));
                }
;
                return EntrylerFragment.newInstance(position + 1,SayfaUrl,Ek,yonlendirme,sayfaya,COOKIE,NICK,duzenlemnecekmi,gelenstring,arananKelime,arsiv,entryProviderArrayList);
            }
            else
            {


                return EntrylerFragment.newInstance(position + 1,SayfaUrl,Ek,yonlendirme,sayfaya,COOKIE,NICK,duzenlemnecekmi,gelenstring,arananKelime,arsiv,null);
            }


        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return getToplamsayfa();
        }

         void setToplamsayfa(int toplamsayfa)
        {
            this.toplamsayfa=toplamsayfa;
        }

         int getToplamsayfa()
        {
            return this.toplamsayfa;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }


        public Fragment findFragmentByPosition(int position, int pagerId) {
            return findFragmentByTag.findFragmentByTag(
                    "android:switcher:" + pagerId + ":"
                            + getItemId(position));
        }

    }

    static class VideoKaynak extends AsyncTask<Void,Void,String>
    {
        String url;
        ProgressDialog alert;

        WeakReference<Context> contextWeakReference;


        VideoKaynak(String url,Context context)
        {
            this.url=url;
            contextWeakReference=new WeakReference<Context>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alert=new ProgressDialog(contextWeakReference.get());
            alert.setMessage("Video Yükleniyor...");
            alert.setCancelable(false);
            alert.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            alert.show();
        }

        @Override
        protected String doInBackground(Void... params) {


            Document document;
            String kaynak=null;

            try {
                document= Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                        .get();


                document.select("script").first().remove();
                Element divler=document.select("script").first();


                String urlcek=divler.html().split("\"src\", \"")[1].trim();
                urlcek=urlcek.split(".mp4")[0];



                kaynak="https:"+urlcek+".mp4";
                Log.d("kaynak",kaynak);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return kaynak;
        }


        @Override
        protected void onPostExecute(String kaynak) {
            super.onPostExecute(kaynak);

            alert.dismiss();

            if(kaynak!=null)
            {
                Intent play=new Intent(Intent.ACTION_VIEW);
                play.setDataAndType(Uri.parse(kaynak),"video/mp4");
                contextWeakReference.get().startActivity(play);
            }
            else
            {
                Toast.makeText(contextWeakReference.get(),"video yüklenemedi sanki",Toast.LENGTH_SHORT).show();
            }



          /*  Uri uri=Uri.parse(aVoid);
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                            MediaController mediaController=new MediaController();

                           mediaController.setAnchorView(videoView);
                            mediaController.setMediaPlayer(videoView);
                            videoView.setMediaController(mediaController);
                        }
                    });
                    alert.dismiss();
                    videoView.start();

                }
            });*/


          /*  surfaceView = (SurfaceView)findViewById(R.id.surface);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.setFixedSize(176, 144);
            mediaPlayer = new MediaPlayer();


            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDisplay(surfaceHolder);

            try {
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.start();*/

        }

    }

    static class ArsivdenEntryGetir extends AsyncTask<String,Void,ArrayList<EntryProvider>> {


       // EntryFragmenttenActivitye entryFragmenttenActivitye;
        WeakReference<Activity> activityWeakReference;
        String cookie;
        String nick;

        ArsivdenEntryGetir(EntryActivity activity,String cookie,String nick)
        {
           // this.entryFragmenttenActivitye=entryFragmenttenActivitye;
          //  contextWeakReference=new WeakReference<Context>(context);

            activityWeakReference=new WeakReference<Activity>(activity);

            this.cookie=cookie;
            this.nick=nick;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<EntryProvider> doInBackground(String... params) {

            String arsivbaslikId = params[0];

           VeriTabaniBaglanti veriTabaniBaglanti = new VeriTabaniBaglanti(activityWeakReference.get());
            veriTabaniBaglanti.okumakicinac();

            ArrayList<EntryProvider> entryProviderArrayList;


            //arsiventryleri = veriTabaniBaglanti.entrySayfagetir(arsivbaslikId);
            entryProviderArrayList=veriTabaniBaglanti.entrySayfagetir(arsivbaslikId);
            Log.d("başlıkid",arsivbaslikId);
          //  Log.d("arşiventry",entryProviderArrayList.get(0).yazar);

            for(EntryProvider p:entryProviderArrayList)
            {

                p.entry=entrylerispanla(p.entry);

            }


            veriTabaniBaglanti.veritabaniKapat();

            return entryProviderArrayList;
        }

        private SpannableString entrylerispanla(SpannableString spansiztext)
        {
            SpannableString message;
            Spanned x= Html.fromHtml(spansiztext.toString());
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
                            span=new LinkBaglama(urlSpan.getURL(),string.toString(),activityWeakReference.get(),cookie,nick);
                        }
                        //   Log.d("span",((URLSpan) span).getURL());
                    }
                    else
                    {
                        span=new LinkBaglama(urlSpan.getURL()," ",activityWeakReference.get(),cookie,nick);
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
        protected void onPostExecute(ArrayList<EntryProvider> entryler) {
            super.onPostExecute(entryler);

            EntryFragmenttenActivitye entryFragmenttenActivitye= (EntryFragmenttenActivitye) activityWeakReference.get();

            entryFragmenttenActivitye.arsiventryleriKur(entryler);

            int sayfasayisi=entryler.size();

            //entryActivityKurulumu();

            entryFragmenttenActivitye.arsivicikurulum();


            if((sayfasayisi%10)==0)
            {
                sayfasayisi=sayfasayisi/10;
            }
            else
            {
                sayfasayisi=(sayfasayisi/10)+1;
            }

            entryFragmenttenActivitye.viewpagerToplamKacSayfa(sayfasayisi);
            entryFragmenttenActivitye.seritlayoutGorunurlugu(sayfasayisi);

           // mSectionsPagerAdapter.setToplamsayfa(sayfasayisi);
         //   mSectionsPagerAdapter.notifyDataSetChanged();
        //    String text=" / "+sayfasayisi;
          //  maxtext.setText(text);

         //   TextView basliktextview =  findViewById(R.id.basliktextviewi);
        //   baslikTiklamDinleyici(basliktextview,arsiventryleri.get(0).baslikid);

            entryFragmenttenActivitye.arsivbasliktiklamadinleyici();

           /* if(sayfasayisi>1)
                seritlayout.setVisibility(View.VISIBLE);
            else
                seritlayout.setVisibility(View.GONE);*/


        }



    }




    private static class KayitSilme extends AsyncTask<String,Void,Void>
    {

        ProgressDialog dialog;
        WeakReference<Context> contextWeakReference;

        KayitSilme(Context context)
        {
            contextWeakReference=new WeakReference<Context>(context);
        }

        @Override
        protected Void doInBackground(String... params) {

            VeriTabaniBaglanti vt=new VeriTabaniBaglanti(contextWeakReference.get());

            vt.veritabaniAc();
            vt.kayitSil(params[0]);
            vt.veritabaniKapat();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(contextWeakReference.get());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Siliniyor ...");
            dialog.show();
        }


        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            dialog.dismiss();

        }
    }

    static class BaslikTakipArkaplan extends AsyncTask<Void,Void,Document> {

        String baslikid;
        String cookie;
        SecenekProvider provider;

        boolean takipediliyomu;
       SecenekAdapter secenekAdapter;

       EntryFragmenttenActivitye entryFragmenttenActivitye;
       WeakReference<Context> contextWeakReference;



        BaslikTakipArkaplan(String baslikid, SecenekProvider provider, String cookie,boolean takipediliyomu,
                            SecenekAdapter secenekAdapter,EntryFragmenttenActivitye entryFragmenttenActivitye,Context context) {
            this.baslikid = baslikid;
            this.provider = provider;
            this.cookie=cookie;
            this.takipediliyomu=takipediliyomu;

            this.secenekAdapter=secenekAdapter;
            this.entryFragmenttenActivitye=entryFragmenttenActivitye;
            contextWeakReference=new WeakReference<Context>(context);
        }

        @Override
        protected Document doInBackground(Void... params) {
            Document document = null;

            if (!takipediliyomu) {
                try {
                    document = Jsoup.connect("https://eksisozluk.com/baslik/takip-et/" + baslikid)
                            .userAgent("Mozilla/5.0 ( compatible )")
                            .header("Accept", "*/*")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                            .cookie("Cookie",cookie )
                            .header("Connection", "keep-alive")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .ignoreContentType(true)
                            .post();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    document = Jsoup.connect("https://eksisozluk.com/baslik/takip-etme/" + baslikid)
                            .userAgent("Mozilla/5.0 ( compatible )")
                            .header("Accept", "*/*")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                            .cookie("Cookie", cookie)
                            .header("Connection", "keep-alive")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .ignoreContentType(true)
                            .post();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            if(document!=null)
            {
                if(takipediliyomu)
                {

                   // takipediliyomu=false;
                    entryFragmenttenActivitye.takipedildimi(false);
                    provider.resimid=R.drawable.takipet;
                    secenekAdapter.notifyDataSetChanged();
                    Toast.makeText(contextWeakReference.get(),"takipten çıktın",Toast.LENGTH_SHORT).show();
                }
                else
                {
                   // takipediliyomu=true;
                    entryFragmenttenActivitye.takipedildimi(true);
                    provider.resimid=R.drawable.takipetsecili;
                    secenekAdapter.notifyDataSetChanged();
                    Toast.makeText(contextWeakReference.get(),"takip ettin",Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(contextWeakReference.get(),"hata oldu",Toast.LENGTH_SHORT).show();



        }
    }



}
