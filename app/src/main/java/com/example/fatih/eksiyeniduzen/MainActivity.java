package com.example.fatih.eksiyeniduzen;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.transition.Scene;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AppBarLayout.OnOffsetChangedListener,MainfragdanMainacte {



    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    Animation fabac,fabkapa;
    FloatingActionButton aramaMain,takvimfab;
    Spinner kanalSpinner;
    MainFragment mainFragRefi;

    AlertDialog cikisdialog;

    String girisCOOKIE,iq;
    String NICK;

    boolean anasayfadaMi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String deneyek=preferences.getString("cookie","bok");
        Log.d("ne geldi",deneyek);

       // setTheme(R.style.GeceTheme);
        setContentView(R.layout.activity_main);




        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        girisCOOKIE="BOS";
        NICK="BOS";


      //this.deleteDatabase("arsiv");

       // Log.d("data",this.getDatabasePath("arsiv").toString());


        appBarLayout= findViewById(R.id.appbar);
        aramaMain=  findViewById(R.id.fab);
        kanalSpinner=findViewById(R.id.baslikspinner);
        fabac= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabkapa=AnimationUtils.loadAnimation(this,R.anim.fab_kapat);
        takvimfab= findViewById(R.id.tarihsec);

     //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);




        drawerKur();

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.black));


        giriskontrolu();

        Bundle bund=getIntent().getExtras();

        if(bund!=null)
        {

            if(bund.getString("kelime")!=null)
            displaySelectedScreen(bund.getInt("fragment"));
            else
                displaySelectedScreen(R.id.basliklar);


           // Log.d("bund null değil",bund.getInt("fragment")+" "+bund.getString("kelime"));
        }
        else
        {
            displaySelectedScreen(R.id.basliklar);
        }






    //    appbarlistener();
        aramaMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aramabutonuislemleri();
            }
        });




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

      /*  outState.putString("cookie",girisCOOKIE);
        outState.putString("nick",NICK);*/

    }


    private void giriskontrolu() {


        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

       girisCOOKIE=preferences.getString("cookie","BOS");


        if(!girisCOOKIE.equals("BOS"))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.giris_menu);
            TextView kullanicinick=navigationView.getHeaderView(0).findViewById(R.id.kullanicinick);
            SwitchCompat temadegis=navigationView.getHeaderView(0).findViewById(R.id.temadegis);


            temadegis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b)
                    {
                        Log.d("b","b true");
                    }
                    else
                    {
                        Log.d("b","b false");
                    }

                }
            });

            NICK=preferences.getString("nick","BOS");
            iq=preferences.getString("iq","yok");
            kullanicinick.setText(NICK);
            kullanicinick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent yazara=new Intent(MainActivity.this,YazarActivity.class);
                    yazara.putExtra("yazarnick", NICK);
                    yazara.putExtra("cookie",girisCOOKIE);
                    yazara.putExtra("nick",NICK);
                    Bundle yazarbund = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                    startActivity(yazara,yazarbund);
                }
            });

        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.ana_menu);
        }


    }



    private void aramabutonuislemleri() {

        LayoutInflater layoutInflater=LayoutInflater.from(this);
        @SuppressLint("InflateParams") View aramadialog=layoutInflater.inflate(R.layout.autocomplete,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(aramadialog);
        }
        builder.setCancelable(true);

        final AlertDialog dialog= builder.create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().getAttributes().gravity= Gravity.TOP;


        RelativeLayout relat=  aramadialog.findViewById(R.id.ust);
        relat.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.yenitoolbar));
        relat.setVisibility(View.VISIBLE);
        final AutoCompleteTextView editText= aramadialog.findViewById(R.id.aramayeri);
        editText.setHintTextColor(ContextCompat.getColor(MainActivity.this,android.R.color.darker_gray));
        editText.setTextColor(ContextCompat.getColor(MainActivity.this,android.R.color.white));
        // final AutoCompleteTextView listView= (ListView) findViewById(R.id.oneriler);
        ImageButton temizleme=  aramadialog.findViewById(R.id.kapa);

        editText.setDropDownHorizontalOffset(-30);


        AutocompleteTextviewListener autocompleteTextviewListener=new AutocompleteTextviewListener(editText,this,girisCOOKIE,NICK);

        editText.addTextChangedListener(autocompleteTextviewListener);
        editText.setOnItemClickListener(autocompleteTextviewListener);

        temizleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty())
                {
                    InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    dialog.dismiss();

                }
                else
                {
                    editText.setText("");
                }
            }
        });

        //  editText.requestFocus();
       /* InputMethodManager imgr = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);*/

        //    edittextListenerKur(editText,temizleme,dialog);


        dialog.show();

    }

    private void appbarlistener() {



        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


               if(Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange()==0)
                {


                  /*  if(takvimfab.getVisibility()==View.VISIBLE)
                    {
                        Log.d("MainActivity","tarihte bugündeyiz arama butonu gelicek takvimi yukarı kaydır");

                    }*/

                   aramaMain.animate().setDuration(250).translationY(0);
                    aramaMain.setVisibility(View.VISIBLE);

                   // aramaMain.show();

                 //   aramaMain.startAnimation(fabac);


                }
                else
                {

                    Animation ann=AnimationUtils.loadAnimation(MainActivity.this,R.anim.floatinganim);
                  //  aramaMain.animate().setDuration(200).translationY(aramaMain.getHeight()*2);
                   // aramaMain.startAnimation(ann);

               // aramaMain.hide();


                   // Log.d("aramain", String.valueOf(aramaMain.getHeight()));
                    //aramaMain.setVisibility(View.INVISIBLE);
                 //  aramaMain.startAnimation(fabkapa);
                 //   aramaMain.setVisibility(View.INVISIBLE);
                }


            }
        });
    }

    private void drawerKur() {


        drawerLayout=  findViewById(R.id.drawer);

        drawerLayout.isDrawerOpen(GravityCompat.START);
        drawerLayout.closeDrawer(GravityCompat.START);

        toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.acik,R.string.kapali)
        {


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();

            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                syncState();


            }
        };
        drawerLayout.setDrawerListener(toggle);


        //    getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView =  findViewById(R.id.nav_view);
        //  navigationView.menu
        //navigationView.inflateMenu(R.menu.giris_menu);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.arama) {

            findViewById(R.id.arama).setVisibility(View.GONE);

            final Spinner spinner= toolbar.getRootView().findViewById(R.id.baslikspinner);
            final RelativeLayout relativeLayout= toolbar.getRootView().findViewById(R.id.ust);
           final AutoCompleteTextView editText= toolbar.getRootView().findViewById(R.id.aramayeri);
            ImageButton kapa=  toolbar.getRootView().findViewById(R.id.kapa);
            spinner.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

            editText.requestFocus();
            InputMethodManager imgr = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            AutocompleteTextviewListener autocompleteTextviewListener=new AutocompleteTextviewListener(editText,MainActivity.this,girisCOOKIE,NICK);

            editText.addTextChangedListener(autocompleteTextviewListener);
           editText.setOnItemClickListener(autocompleteTextviewListener);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                        String kelime=editText.getText().toString();

                        if(!kelime.equals(""))
                        {

                            Intent aramayagec=new Intent(MainActivity.this,AramaActivity.class);
                            // aramayagec.putExtra("fragment",R.id.ara);
                            aramayagec.putExtra("kelime",kelime);
                            aramayagec.putExtra("cookie",girisCOOKIE);
                            aramayagec.putExtra("iq",iq);
                            aramayagec.putExtra("nick",NICK);

                            Bundle bundle = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.yukari, R.anim.asagi).toBundle();

                            startActivity(aramayagec,bundle);

                        }

                        editText.dismissDropDown();

                        return true;
                    }

                    return false;
                }
            });


            kapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(editText.getText().toString().equals(""))
                    {
                        InputMethodManager imgr = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        relativeLayout.setVisibility(View.GONE);
                        findViewById(R.id.arama).setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);

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



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Bundle bundle=new Bundle();
        bundle.putString("cookie",girisCOOKIE);
        bundle.putString("nick",NICK);



        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.basliklar:

                anasayfadaMi=true;

                appBarLayout.addOnOffsetChangedListener(this);
                fragment =  MainFragment.newInstance(girisCOOKIE,NICK,false);
                mainFragRefi= (MainFragment) fragment;
             //   takvimfab.setVisibility(View.INVISIBLE);
                break;

            case R.id.ara:

                anasayfadaMi=false;

                Intent aramayagec=new Intent(MainActivity.this,AramaActivity.class);
                // aramayagec.putExtra("fragment",R.id.ara);
                aramayagec.putExtra("kelime","");
                aramayagec.putExtra("cookie",girisCOOKIE);
                aramayagec.putExtra("nick",NICK);
                Bundle activityAnimasyonu = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.yukari, R.anim.asagi).toBundle();
                startActivity(aramayagec,activityAnimasyonu);
              //  getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
             //   getSupportActionBar().setTitle("Şikayet Yaz");
                break;

            case R.id.ben:
                anasayfadaMi=false;

                Intent yazara=new Intent(MainActivity.this,YazarActivity.class);
               // yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                yazara.putExtra("yazarnick", NICK);
                yazara.putExtra("cookie",girisCOOKIE);
                yazara.putExtra("nick",NICK);
               Bundle yazarbund = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
              //  startActivity(yazara);
               // overridePendingTransition(0,0);
               startActivity(yazara,yazarbund);
                break;

            case R.id.olay:
                anasayfadaMi=false;

                appBarLayout.addOnOffsetChangedListener(this);
                fragment=new BasliklarFragment().newInstance(40,girisCOOKIE,"https://eksisozluk.com/basliklar/olay",NICK);
                takvimfab.setClickable(false);
              //  takvimfab.setVisibility(View.GONE);

                break;

            case R.id.giris:
                anasayfadaMi=false;

                appBarLayout.removeOnOffsetChangedListener(this);
                fragment=new FragmentLogin().newInstance();
                takvimfab.setClickable(false);
                //takvimfab.setVisibility(View.GONE);
                break;

            case R.id.badiengel:

                anasayfadaMi=false;

                appBarLayout.addOnOffsetChangedListener(this);
                fragment=new FragmentBadiEngelli().newInstance(girisCOOKIE,NICK);
                takvimfab.setClickable(false);
              //  takvimfab.setVisibility(View.GONE);
                break;

            case R.id.cop:
                anasayfadaMi=false;

                aramaMain.clearAnimation();
                aramaMain.setVisibility(View.INVISIBLE);
                appBarLayout.removeOnOffsetChangedListener(this);
                takvimfab.setClickable(false);
                fragment = new MainFragment().newInstance(girisCOOKIE,NICK,true);
                break;

            case R.id.arsiv:
                anasayfadaMi=false;

                fragment=new FragmentArsiv().newInstance(girisCOOKIE,NICK);
                takvimfab.setClickable(false);
                break;

            case R.id.ayar:
                anasayfadaMi=false;

                fragment=new FragmentProfilAyarlari().newInstance(girisCOOKIE,NICK);
                aramaMain.clearAnimation();
                aramaMain.setVisibility(View.INVISIBLE);
                appBarLayout.removeOnOffsetChangedListener(this);
                takvimfab.setClickable(false);
                break;

            case R.id.mesaj:
                anasayfadaMi=false;

                fragment=FragmentMesajlar.newInstance(girisCOOKIE,NICK);
                aramaMain.clearAnimation();
                aramaMain.setVisibility(View.INVISIBLE);
                appBarLayout.removeOnOffsetChangedListener(this);
                takvimfab.setClickable(false);
                break;


            case R.id.cik:
                new Cikis(girisCOOKIE,MainActivity.this).execute();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
        //    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        if(anasayfadaMi)
        {
            LayoutInflater inflator= LayoutInflater.from(MainActivity.this);
            @SuppressLint("InflateParams") final View view=inflator.inflate(R.layout.yamuk_koseli_dialog, null);


            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

            builder
                    //.setTitle("çıkış")
                    .setView(view)
                    // .setMessage("kapansın mı")
                    .setCancelable(false);

            // final AlertDialog dialog=builder.create();
            cikisdialog=builder.create();

            Button evet=view.findViewById(R.id.evet);
            Button hayir=view.findViewById(R.id.hayir);


            cikisdialog.show();

            cikisdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            evet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.this.finishAffinity();
                }
            });

            hayir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cikisdialog.dismiss();
                }
            });
        }
        else
            displaySelectedScreen(R.id.basliklar);



      /*  Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button c=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        c.setTextColor(Color.GRAY);
        b.setTextColor(Color.GRAY);
        b.setAllCaps(false);
        c.setAllCaps(false);*/

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


        if(Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange()==0)
        {
            aramaMain.animate().setDuration(250).translationY(0);
            aramaMain.setVisibility(View.VISIBLE);


        }
        else
        {

            //  aramaMain.animate().setDuration(200).translationY(aramaMain.getHeight()*2);


            aramaMain.animate().setDuration(200).translationY(aramaMain.getHeight()*2);

        }


    }

    @Override
    public void gorunurTiklanabilir() {
        takvimfab.setVisibility(View.VISIBLE);
        takvimfab.setClickable(true);
    }

    @Override
    public void viewGone() {
        takvimfab.setVisibility(View.GONE);
        takvimfab.setClickable(false);
    }

    @Override
    public void spinnerselection(int pos) {
        kanalSpinner.setSelection(pos,false);
    }

    @Override
    public void spinnerAdaptoru(ArrayAdapter<String> xmlarray) {
        kanalSpinner.setAdapter(xmlarray);
    }

    @Override
    public void spinnerListener() {
        kanalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean ilkacilis=true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!ilkacilis)
                mainFragRefi.ViewPagerSayfaGecisActivityden(position);

                ilkacilis=false;
             //   mViewPager.setCurrentItem(position,false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void entryYazmaButonundanActivitye(int position) {

    }

    @Override
    public void kenaraAtildiMi(boolean kenaraAtildi) {

    }


    private static class Cikis extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog alert;

        String cookiea;

        WeakReference<Context> contextWeakReference;

        Cikis(String cookiea,Context context)
        {
            this.cookiea=cookiea;

            contextWeakReference=new WeakReference<Context>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            alert=new ProgressDialog(contextWeakReference.get());
            alert.setMessage("gidiyorsun");
            alert.setCancelable(false);
            alert.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            alert.show();

        }

        @Override
        protected Void doInBackground(Void... params) {



          //  Log.d("eski cookie",cookiea);
            try {
                Connection.Response response=Jsoup.connect("https://eksisozluk.com/terk")
                        .cookie("Cookie",cookiea)
                        .method(Connection.Method.GET)
                        .execute();


             //   Log.d("yeni cookieler",response.cookies().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            alert.dismiss();


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("cookie", "BOS");
            editor.apply();
            Intent git = new Intent(contextWeakReference.get(), MainActivity.class);


            contextWeakReference.get().startActivity(git);

        }


    }




}
