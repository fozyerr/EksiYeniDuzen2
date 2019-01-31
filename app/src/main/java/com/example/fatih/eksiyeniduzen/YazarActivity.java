package com.example.fatih.eksiyeniduzen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class YazarActivity extends AppCompatActivity implements InterfaceArama{


    String yazarnick;
    String yazarid;

    String toplamen;

    TextView istatistik,kunye,toplametr,yazarP,textView;
    Toolbar toolbar;

    FloatingActionButton[] butonlar;
    FloatingActionButton butonlarigoster,badiekle,engelle,baslikengelle,badicikar,engelkaldir,baslikengelkaldir;
    Animation acilis,kapanis,dondur,geridondur,acilis2,acilis3;

    MenuItem mesajgonder;

    boolean acikmi;

    String requestverify;

    String cookie,nick;
   // TextView text;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.sagdan_cikis);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yazar);

         toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toplamen="";

        acikmi=false;
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         textView=  findViewById(R.id.basliktextviewi);
        AppBarLayout appBarLayout= findViewById(R.id.appbar);

        istatistik=  findViewById(R.id.yazartanim);
        kunye=  findViewById(R.id.yazarkunye);
        toplametr=  findViewById(R.id.toplamentry);
        yazarP=findViewById(R.id.yazarparagraf);


        cookie=getIntent().getExtras().getString("cookie");
        nick=getIntent().getExtras().getString("nick");


        acilis= AnimationUtils.loadAnimation(YazarActivity.this,R.anim.fab_open);
        kapanis=AnimationUtils.loadAnimation(YazarActivity.this,R.anim.fab_kapat);
        dondur=AnimationUtils.loadAnimation(YazarActivity.this,R.anim.saat_dondur);
        geridondur=AnimationUtils.loadAnimation(YazarActivity.this,R.anim.geri_dondur);


        //text = new TextView(YazarActivity.this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.black));

       /* SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

//TODO eğer interface olmazsa burayı aç

        ViewPager mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);*/

        //collapsingToolbarLayout.setTitleEnabled(false);



        yazarnick=getIntent().getExtras().getString("yazarnick");
     //  collapsingToolbarLayout.setTitle(yazarnick);
       // collapsingToolbarLayout.setCollapsedTitleTextAppearance(0);
       // collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);



       textView.setText(yazarnick);

        new YazarBilgileri(yazarnick,YazarActivity.this,cookie,nick).execute();

        butonlarigoster= findViewById(R.id.yazarIslemleri);

        if(cookie.equals("BOS") || nick.equals(yazarnick))
            butonlarigoster.setVisibility(View.GONE);
        else
        floatingbutonlar();


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                //verticalOffset!=0
                if(Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange()==0)
                {
                    toplametr.setVisibility(View.VISIBLE);

                }
                else
                {
                    toplametr.setVisibility(View.GONE);
                }


            }
        });


    }

    private void floatingbutonlar() {


        baslikengelle= findViewById(R.id.baslikengelle);
        engelle=  findViewById(R.id.engelle);
        badiekle = findViewById(R.id.badiekle);
        baslikengelkaldir=  findViewById(R.id.baslikengelkaldir);
        engelkaldir= findViewById(R.id.engelkaldir);
        badicikar =  findViewById(R.id.badicikar);

        butonlar=new FloatingActionButton[6];
        butonlar[0]=badiekle;
        butonlar[1]=badicikar;
        butonlar[2]=engelle;
        butonlar[3]=engelkaldir;
        butonlar[4]=baslikengelle;
        butonlar[5]=baslikengelkaldir;


     /*   acilis.setDuration(200);
        acilis2.setDuration(350);
        acilis3.setDuration(500);*/
        butonlarigoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /* Intent broad=new Intent();
                broad.setAction("ilkbroadcast");
                broad.putExtra("mesaj","broadcast mesajı");
                broad.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(broad);*/


                if(acikmi)
                {
                    for(int i=0;i<butonlar.length;i++)
                    {
                        butonkapanacakmi(butonlar[i]);

                    }
                    butonlarigoster.startAnimation(geridondur);
                    acikmi=false;
                }
                else
                {

                    for(int i=0;i<butonlar.length;i++)
                    {
                        butongozukecekmi(butonlar[i]);
                    }
                    butonlarigoster.startAnimation(dondur);

                    acikmi=true;
                }



            }
        });


        fabButonlariTiklamalar();

    }

    private void butonlarigizlemefonksiyonu()
    {
        for(int i=0;i<butonlar.length;i++)
        {
            butonkapanacakmi(butonlar[i]);

        }
        butonlarigoster.startAnimation(geridondur);
        acikmi=false;
    }

    private void fabButonlariTiklamalar() {


        badiekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                butonlarigizlemefonksiyonu();
               new Butonislemleri(1,yazarid,cookie,YazarActivity.this).execute();

            }
        });
        badicikar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonlarigizlemefonksiyonu();

                new Butonislemleri(2,yazarid,cookie,YazarActivity.this).execute();
            }
        });
        engelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               butonlarigizlemefonksiyonu();
                new Butonislemleri(3,yazarid,cookie,YazarActivity.this).execute();
            }
        });
        engelkaldir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonlarigizlemefonksiyonu();
                new Butonislemleri(4,yazarid,cookie,YazarActivity.this).execute();
            }
        });
        baslikengelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonlarigizlemefonksiyonu();
                new Butonislemleri(5,yazarid,cookie,YazarActivity.this).execute();
            }
        });
        baslikengelkaldir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               butonlarigizlemefonksiyonu();
                new Butonislemleri(6,yazarid,cookie,YazarActivity.this).execute();
            }
        });

    }

    private void butongozukecekmi(FloatingActionButton floatingActionButton)
    {
        if(floatingActionButton.getVisibility()!=View.GONE)
        {
            floatingActionButton.startAnimation(acilis);
            floatingActionButton.setClickable(true);
        }
        else
            floatingActionButton.clearAnimation();

    }
    private void butonkapanacakmi(FloatingActionButton floatingActionButton)
    {

     //   Log.d("döngüye gelen", (String) floatingActionButton.getContentDescription());

        if(floatingActionButton.getVisibility()==View.INVISIBLE)
        {
           // Log.d("işlem yapılacak", (String) floatingActionButton.getContentDescription());

            floatingActionButton.startAnimation(kapanis);
            floatingActionButton.setClickable(false);
        }
        else
            floatingActionButton.clearAnimation();

    }

    private void mesajatmadialogolustur(final String yazaradi, final String mesajVerify) {


        LayoutInflater inflator= LayoutInflater.from(this);
        @SuppressLint("InflateParams") View view=inflator.inflate(R.layout.mesaj_yolla_dialog, null);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("gönder",null)
                .setNegativeButton("iptal",null);
        final AlertDialog dialog=builder.create();

        final EditText mesajicerik=view.findViewById(R.id.mesajicerik);
        AutoCompleteTextView mesajkime=view.findViewById(R.id.mesajatilanyazar);
        final ProgressBar progressBar=view.findViewById(R.id.progressBar);
        mesajkime.setText(yazaradi);
        mesajicerik.requestFocus();
        dialog.show();
        Button pozitif = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negatif=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        negatif.setAllCaps(false);
        pozitif.setAllCaps(false);
        pozitif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!mesajicerik.getText().toString().isEmpty())
                    new MesajGonder(cookie,mesajVerify,yazaradi,mesajicerik.getText().toString(),nick,false,YazarActivity.this,dialog,progressBar).execute();
                else
                    Toast.makeText(getApplicationContext(),"bir şeyler yazmaya ne dersin delikanlı",Toast.LENGTH_SHORT).show();

                //dialog.dismiss();

            }
        });
        negatif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_yazar, menu);

        mesajgonder=menu.findItem(R.id.mesajgonder);

        if(cookie.equals("BOS") || nick.equals(yazarnick))
            mesajgonder.setVisible(false);
        else
            mesajgonder.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       switch (id)
       {
           case android.R.id.home:
               this.finish();
               overridePendingTransition(0, R.anim.sagdan_cikis);
               return true;

           case R.id.mesajgonder:

               //mesajatmadialogolustur(yazarnick,requestverify);

               new MesajAtmaKontrolVeDiyalog(yazarnick,cookie,"",requestverify,YazarActivity.this,false,null).execute();

               return true;

           case R.id.arama:

               findViewById(R.id.arama).setVisibility(View.GONE);

               final RelativeLayout textView=  findViewById(R.id.yazartoolbar);
               final RelativeLayout relativeLayout= findViewById(R.id.ust);
               final AutoCompleteTextView editText=findViewById(R.id.aramayeri);
               ImageButton kapa= findViewById(R.id.kapa);
               textView.setVisibility(View.GONE);
               relativeLayout.setVisibility(View.VISIBLE);


               editText.requestFocus();
               InputMethodManager imgr = (InputMethodManager) YazarActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
               imgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

               AutocompleteTextviewListener autocompleteTextviewListener=new AutocompleteTextviewListener(editText,YazarActivity.this,cookie,nick);

               editText.addTextChangedListener(autocompleteTextviewListener);
               editText.setOnItemClickListener(autocompleteTextviewListener);
               editTextbuttonAramasi(editText);

               kapa.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {


                       if(editText.getText().toString().equals(""))
                       {
                           InputMethodManager imgr = (InputMethodManager) YazarActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

        }

        return super.onOptionsItemSelected(item);
    }

    private void editTextbuttonAramasi(final AutoCompleteTextView editText) {

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

                        Intent aramayagec=new Intent(YazarActivity.this,AramaActivity.class);
                        aramayagec.putExtra("kelime",kelime);
                        aramayagec.putExtra("cookie",cookie);
                        aramayagec.putExtra("nick",nick);
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

        this.requestverify=mesajverify;
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),mesajverify);


        ViewPager mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public void updateView(boolean video, String videoURL) {

        this.yazarid=videoURL;

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        String mesajverify;

         SectionsPagerAdapter(FragmentManager fm,String mesajverify) {
            super(fm);
             this.mesajverify=mesajverify;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 7:
                    return FragmentYazarEk.newInstance(position+1,yazarnick,0,cookie,nick);

                case 8:
                    return FragmentYazarEk.newInstance(position+1,yazarnick,1,cookie,nick);

                default:
                  return  FragmentYazar.newInstance(position + 1,yazarnick,cookie,nick,mesajverify);

            }


        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "entry'ler";
                case 1:
                    return "favoriler";
                case 2:
                    return "en çok favorilenenler";
                case 3:
                    return "son oylananları";
                case 4:
                    return "bu hafta dikkat çekenleri";
                case 5:
                    return "el emeği göz nuru";
                case 6:
                    return "en beğenilenleri";
                case 7:
                    return "favori yazarları";
                case 8:
                    return "katkıda bulunduğu kanallar";
                case 9:
                    return "ukteleri";
            }
            return null;
        }
    }


  static class YazarBilgileri extends AsyncTask<Void,Void,String>
    {

        String url,toplamtext;

        String yazarid;
        String entryUrl,entryBaslik,yazarUrl;
        Document document;
        String mesajverify;

        String[] diz=null;
        SpannableStringBuilder builder;
        SpannableStringBuilder paragraf;


        WeakReference<Activity> activityWeakReference;

       // EntryFragmentHaberlesme entryFragmentHaberlesme;


        String cookie,nick;


        YazarBilgileri(String url,Activity activity,String cookie,String nick)
        {
            this.activityWeakReference=new WeakReference<Activity>(activity);
            this.url=url;
            this.cookie=cookie;
            this.nick=nick;

        }

        @Override
        protected String doInBackground(Void... params) {

            document=null;

            StringBuilder stats=new StringBuilder();

            try {
                document= Jsoup.connect("https://eksisozluk.com/biri/"+url)
                        .cookie("Cookie",cookie)
                        .get();

                if (document!=null)
                {


                SpannableString message;
                builder=new SpannableStringBuilder();
                Elements badge=document.select("ul#user-badges > li");
                Elements stat=document.select("ul#user-entry-stats > li");

                yazarid=document.select("form#user-note-form > input").attr("value");
                toplamtext=document.select("ul#user-entry-stats > li#entry-count-total").text();


                entryUrl=document.select("h2 > a").attr("href");
                entryBaslik=document.select("h2 > a").text();
                yazarUrl=document.select("h1 > a").attr("href");

                if(!cookie.equals("BOS") && document.select("form#message-send-form > input").first()!=null)
                    mesajverify=document.select("form#message-send-form > input").first().attr("value");


                Elements butts =document.select("div.sub-title-menu > a.relation-link");
                diz=new String[butts.size()];
                int as=0;

                for(Element a:butts)
                {
                    diz[as]=a.text();
                    as++;
                }

                for(Element sta:stat)
                {
                    stats.append(sta.text()).append(" · ");
                }


               paragraf=new SpannableStringBuilder();



                SpannableString yazarParagrafi=stringtiSpanla(document.select("div.content > p").html());
                SpannableString devaminioku=new SpannableString(" devamını okuyayım...");//21

                Object span=new LinkBaglama(entryUrl, entryBaslik, activityWeakReference.get(),cookie,nick);
                devaminioku.setSpan(span,0,21,devaminioku.getSpanFlags(devaminioku));


                      if(yazarParagrafi.length()<=179)
                      {
                          paragraf.append(yazarParagrafi);
                                  //.append(devaminioku);
                      }
                        else
                      {
                          paragraf.append(yazarParagrafi.subSequence(0,179)).append(devaminioku);
                      }


                for(Element bad:badge)
                {

                   message=stringtiSpanla(bad.html());


                    builder.append(message).append("  ");
                }

                }





            } catch (IOException e) {
                e.printStackTrace();
            }


            return stats.toString();
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
                        span = new LinkBaglama(urlSpan.getURL(), string.toString(), activityWeakReference.get(),cookie,nick);
                        //   Log.d("span",((URLSpan) span).getURL());
                    } else {
                        span = new LinkBaglama(urlSpan.getURL(), " ", activityWeakReference.get(),cookie,nick);

                        // COOKİELERİ EKLE
                    }
                }


                message.setSpan(span, start, end, flags);
                // spannableString=message;
            }

            return message;
        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(document!=null)
            {
                Activity yazarAct=activityWeakReference.get();

                InterfaceArama yazaridkur= (InterfaceArama) yazarAct;
                yazaridkur.updateView(false,yazarid);
                yazaridkur.yazarprofiliVerifygonder(mesajverify);

             TextView istatistik= yazarAct.findViewById(R.id.yazartanim);
                TextView kunye= yazarAct. findViewById(R.id.yazarkunye);
                TextView toplametr=  yazarAct.findViewById(R.id.toplamentry);
                TextView  yazarP=yazarAct.findViewById(R.id.yazarparagraf);


                kunye.setText(builder, TextView.BufferType.SPANNABLE);

                //  kunye.setText(spannableString, TextView.BufferType.SPANNABLE);
                kunye.setLinksClickable(true);
                kunye.setMovementMethod(LinkMovementMethod.getInstance());

                // text.setText("toplam entry :  "+toplamtext);
                //text.setTextAppearance(R.style.CollapsedAppBar);

                yazarP.setText(paragraf, TextView.BufferType.SPANNABLE);
                yazarP.setLinksClickable(true);
                yazarP.setMovementMethod(LinkMovementMethod.getInstance());

                if(!cookie.equals("BOS"))
                gorulmeduzenle();


                butonTikalamIslemleri();



               String toplamentryler="toplam entry :  "+toplamtext;
                toplametr.setText(toplamentryler);
                istatistik.setText(aVoid);
            }
            else
                Toast.makeText(activityWeakReference.get(),"bir şeyler yanlış gitti",Toast.LENGTH_SHORT).show();




        }


        private void gorulmeduzenle() {

            if (diz != null && diz.length>0) {
                Activity yAct=activityWeakReference.get();

              FloatingActionButton  baslikengelle= yAct.findViewById(R.id.baslikengelle);
                FloatingActionButton    engelle=  yAct.findViewById(R.id.engelle);
                FloatingActionButton   badiekle =yAct. findViewById(R.id.badiekle);
                FloatingActionButton   baslikengelkaldir= yAct. findViewById(R.id.baslikengelkaldir);
                FloatingActionButton   engelkaldir= yAct.findViewById(R.id.engelkaldir);
                FloatingActionButton   badicikar =  yAct.findViewById(R.id.badicikar);


            switch (diz[0]) {
                case "takip et":
                    if (nick.equals(url)) {
                   /* badiekle.setVisibility(View.GONE);  engelle.setVisibility(View.GONE);   baslikengelle.setVisibility(View.GONE);
                    badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.GONE);      baslikengelkaldir.setVisibility(View.GONE);*/

                    } else {
                        badiekle.setVisibility(View.INVISIBLE);
                        engelle.setVisibility(View.INVISIBLE);
                        baslikengelle.setVisibility(View.INVISIBLE);
                        badicikar.setVisibility(View.GONE);
                        engelkaldir.setVisibility(View.GONE);
                        baslikengelkaldir.setVisibility(View.GONE);
                    }

                    break;
                case "engelle":
                    badiekle.setVisibility(View.GONE);
                    engelle.setVisibility(View.INVISIBLE);
                    baslikengelle.setVisibility(View.GONE);
                    badicikar.setVisibility(View.GONE);
                    engelkaldir.setVisibility(View.GONE);
                    baslikengelkaldir.setVisibility(View.INVISIBLE);

                    break;
                case "engellemeyi bırak":
                    if (diz[1].equals("başlıklarını engelle")) {
                        badiekle.setVisibility(View.GONE);
                        engelle.setVisibility(View.GONE);
                        baslikengelle.setVisibility(View.INVISIBLE);
                        badicikar.setVisibility(View.GONE);
                        engelkaldir.setVisibility(View.INVISIBLE);
                        baslikengelkaldir.setVisibility(View.GONE);

                    } else {
                        badiekle.setVisibility(View.GONE);
                        engelle.setVisibility(View.GONE);
                        baslikengelle.setVisibility(View.GONE);
                        badicikar.setVisibility(View.GONE);
                        engelkaldir.setVisibility(View.INVISIBLE);
                        baslikengelkaldir.setVisibility(View.INVISIBLE);

                    }


                    break;
                default:
                    badiekle.setVisibility(View.GONE);
                    engelle.setVisibility(View.GONE);
                    baslikengelle.setVisibility(View.GONE);
                    badicikar.setVisibility(View.INVISIBLE);
                    engelkaldir.setVisibility(View.GONE);
                    baslikengelkaldir.setVisibility(View.GONE);

                    break;
            }

        }

        }



        private void butonTikalamIslemleri() {
            TextView textView=activityWeakReference.get().findViewById(R.id.basliktextviewi);
            TextView  yazarP=activityWeakReference.get().findViewById(R.id.yazarparagraf);

            yazarP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tekentry=new Intent(activityWeakReference.get(),EntryActivity.class);

                    //Log.d("ELSEIF",gonder);
                    tekentry.putExtra("link",entryUrl);
                    tekentry.putExtra("cookie",cookie);
                    tekentry.putExtra("nick",nick);
                    tekentry.putExtra("gelenstring",entryBaslik);
                    tekentry.putExtra("mark"," ");
                    tekentry.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   activityWeakReference.get().startActivity(tekentry);


                   // Log.d(YazarActivity.this.toString(),entryUrl);

                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tekentry=new Intent(activityWeakReference.get(),EntryActivity.class);
                    tekentry.putExtra("link",yazarUrl);
                    tekentry.putExtra("cookie",cookie);
                    tekentry.putExtra("nick",nick);
                    tekentry.putExtra("gelenstring",url);
                    tekentry.putExtra("mark"," ");
                    tekentry.putExtra("urlduzenle",true);
                    tekentry.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityWeakReference.get().startActivity(tekentry);
                }
            });
        }

    }

 static class Butonislemleri extends AsyncTask<Void,Void,Document>
    {
        int islemnumara;
        String yazarid;
        String tamurl="";

        String cookie;
        WeakReference<YazarActivity> yazarActivityWeakReference;

        Butonislemleri(int islemnumara,String yazarid,String cookie,YazarActivity yazarActivity) {
            this.islemnumara = islemnumara;
            this.yazarid = yazarid;

            this.cookie=cookie;
            yazarActivityWeakReference=new WeakReference<YazarActivity>(yazarActivity);
        }


        @Override
        protected Document doInBackground(Void... params) {

            String eklemeurl="https://eksisozluk.com/userrelation/addrelation/";
            String cikarmaurl="https://eksisozluk.com/userrelation/removerelation/";
            String badil="?r=b";
            String engell="?r=m";
            String baslikl1="?r=i";
            Document doc=null;
            /*

            Bütün Button tanımlama işlemlerini Fragment classının içinde yap, atamaları oncreateViewde yap


             */

                /*

                       Jsoup post işlemi için kopyala yapıştır yap tek jsouptan işlemleri hallet


                */

            switch (islemnumara)
            {
                case 1:
                    tamurl=eklemeurl+yazarid+badil;
                    Log.d("yazar id +url=",tamurl);
                    break;
                case 2:
                    tamurl=cikarmaurl+yazarid+badil;
                    Log.d("yazar id +url=",tamurl);
                    break;
                case 3:
                    tamurl=eklemeurl+yazarid+engell;
                    Log.d("yazar id +url=",tamurl);
                    break;
                case 4:
                    tamurl=cikarmaurl+yazarid+engell;
                    Log.d("yazar id +url=",tamurl);
                    break;
                case 5:
                    tamurl=eklemeurl+yazarid+baslikl1;
                    Log.d("yazar id +url=",tamurl);
                    break;
                case 6:
                    tamurl=cikarmaurl+yazarid+baslikl1;
                    Log.d("yazar id +url=",tamurl);
                    break;
            }
            try {
              doc  = Jsoup.connect(tamurl)
                        .userAgent("Mozilla/5.0 ( compatible )")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .cookie("Cookie",cookie)
                        .header("Connection", "keep-alive")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .ignoreContentType(true)
                        .post();

            } catch (IOException e) {
                e.printStackTrace();
            }








            return doc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Document aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid!=null)
            {
                YazarActivity yazarActivity=yazarActivityWeakReference.get();

                 FloatingActionButton   baslikengelle= yazarActivity.findViewById(R.id.baslikengelle);
                FloatingActionButton  engelle=  yazarActivity.findViewById(R.id.engelle);
                FloatingActionButton  badiekle = yazarActivity.findViewById(R.id.badiekle);
                FloatingActionButton   baslikengelkaldir=  yazarActivity.findViewById(R.id.baslikengelkaldir);
                FloatingActionButton  engelkaldir= yazarActivity.findViewById(R.id.engelkaldir);
                FloatingActionButton  badicikar = yazarActivity. findViewById(R.id.badicikar);



                switch (islemnumara)
                {
                    case 1:
                        badiekle.setVisibility(View.GONE);  engelle.setVisibility(View.GONE);   baslikengelle.setVisibility(View.GONE);
                        badicikar.setVisibility(View.INVISIBLE);    engelkaldir.setVisibility(View.GONE);      baslikengelkaldir.setVisibility(View.GONE);
                        Toast.makeText(yazarActivity,"badilere eklendi",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        badiekle.setVisibility(View.INVISIBLE);  engelle.setVisibility(View.INVISIBLE);   baslikengelle.setVisibility(View.INVISIBLE);
                        badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.GONE);      baslikengelkaldir.setVisibility(View.GONE);
                          Toast.makeText(yazarActivity,"badilerden çıkarıldı",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:

                        if(baslikengelkaldir.getVisibility()==View.GONE)
                        {
                            badiekle.setVisibility(View.GONE);engelle.setVisibility(View.GONE);   baslikengelle.setVisibility(View.INVISIBLE);
                            badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.INVISIBLE);      baslikengelkaldir.setVisibility(View.GONE);

                        }
                        else
                        {
                            badiekle.setVisibility(View.GONE);engelle.setVisibility(View.GONE);   baslikengelle.setVisibility(View.GONE);
                            badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.INVISIBLE);      baslikengelkaldir.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(yazarActivity,"engellendi",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                       // butonlarigizlemefonksiyonu();
                        if(baslikengelkaldir.getVisibility() == View.GONE)
                        {
                            badiekle.setVisibility(View.INVISIBLE);engelle.setVisibility(View.INVISIBLE);   baslikengelle.setVisibility(View.INVISIBLE);
                            badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.GONE);      baslikengelkaldir.setVisibility(View.GONE);
                            //      Toast.makeText(context,"Engel kaldırıldı",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            badiekle.setVisibility(View.GONE);engelle.setVisibility(View.INVISIBLE);   baslikengelle.setVisibility(View.GONE);
                            badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.GONE);      baslikengelkaldir.setVisibility(View.INVISIBLE);
                            //  Toast.makeText(context,"Engel kaldırıldı",Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(yazarActivity,"engel kaldırıldı",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                     //   butonlarigizlemefonksiyonu();
                        badiekle.setVisibility(View.GONE);
                        badicikar.setVisibility(View.GONE);

                        if(engelle.getVisibility()==View.INVISIBLE)
                        {
                            engelle.setVisibility(View.INVISIBLE);   baslikengelle.setVisibility(View.GONE);
                            engelkaldir.setVisibility(View.GONE);      baslikengelkaldir.setVisibility(View.INVISIBLE);
                            //  Toast.makeText(context,"Başlıkları engellendi",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            engelle.setVisibility(View.GONE);   baslikengelle.setVisibility(View.GONE);
                            engelkaldir.setVisibility(View.INVISIBLE);      baslikengelkaldir.setVisibility(View.INVISIBLE);
                            //   Toast.makeText(context,"Başlıkları engellendi",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(yazarActivity,"başlıkları engellendi",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                       // butonlarigizlemefonksiyonu();
                        if(engelkaldir.getVisibility()==View.INVISIBLE)
                        {
                            badiekle.setVisibility(View.GONE);engelle.setVisibility(View.GONE);   baslikengelle.setVisibility(View.INVISIBLE);
                            badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.INVISIBLE);      baslikengelkaldir.setVisibility(View.GONE);
                            //   Toast.makeText(context,"Başlık engeli kaldırıldı",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            badiekle.setVisibility(View.INVISIBLE);engelle.setVisibility(View.INVISIBLE);   baslikengelle.setVisibility(View.INVISIBLE);
                            badicikar.setVisibility(View.GONE);    engelkaldir.setVisibility(View.GONE);      baslikengelkaldir.setVisibility(View.GONE);
                            //  Toast.makeText(context,"Başlık engeli kaldırıldı",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(yazarActivity,"başlık engeli kaldırıldı",Toast.LENGTH_SHORT).show();
                        break;
                }
            }



        }
    }

}
