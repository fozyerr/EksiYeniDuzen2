package com.example.fatih.eksiyeniduzen;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class AramaActivity extends AppCompatActivity implements FragmentAra.ActivityeBaglanti {

     SectionsPagerAdapter mSectionsPagerAdapter;


     ViewPager mViewPager;
    String cookie,kelime,nick,iq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        cookie=getIntent().getExtras().getString("cookie");
        kelime=getIntent().getExtras().getString("kelime");
        nick=getIntent().getExtras().getString("nick");
        iq=getIntent().getExtras().getString("iq");

        Log.d("YazarAct",nick);

        kelimeyegorekurulum();



    }

    private void kelimeyegorekurulum() {

        StringBuilder stringBuilder;



        if(kelime.equals(""))
        {
            mSectionsPagerAdapter.setSekmesayi(1);

            mSectionsPagerAdapter.notifyDataSetChanged();

        }
        else if(kelime.contains("/bas"))
        {
            stringBuilder=new StringBuilder();
            stringBuilder.append("https://eksisozluk.com")
                    .append(kelime);

           mSectionsPagerAdapter.setUrl(stringBuilder.toString());
            mSectionsPagerAdapter.setSekmesayi(2);
            mSectionsPagerAdapter.notifyDataSetChanged();

            mViewPager.setCurrentItem(1,true);

        }
        else
        {

            stringBuilder=new StringBuilder();

            stringBuilder.append("https://eksisozluk.com/basliklar/ara?SearchForm.Keywords=")
                    .append(kelime)
                    .append("&SearchForm.Author=")
                    .append("&SearchForm.When.From=")
                    .append("&SearchForm.When.To=")
                    .append("&SearchForm.NiceOnly=false")
                    .append("&SearchForm.SortOrder=Date");

            mSectionsPagerAdapter.setUrl(stringBuilder.toString());


            mSectionsPagerAdapter.setSekmesayi(2);
            mSectionsPagerAdapter.notifyDataSetChanged();

            mViewPager.setCurrentItem(1,true);
            Log.d("KelimeKurulumu","çalışma zamanı deneme");

          /*  BasliklarFragment basliklarFragment= (BasliklarFragment) mSectionsPagerAdapter.getItem(1);
            basliklarFragment.activitydenulasma(stringBuilder.toString());*/
           // Log.d("fragçekme",basliklarFragment.activitydenulasma("kuzuuu"));


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_arama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void veriGonder(String aramaURL) {


        String fragTag="android:switcher:" + R.id.container + ":" + 1;
        BasliklarFragment basliklarFragment= (BasliklarFragment) getSupportFragmentManager().findFragmentByTag(fragTag);

        basliklarFragment.activitydenulasma(aramaURL,true);

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        Log.d("OnAttachFragment","acaba bura ne zaman çalışıyör");

    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {


        private int sekmesayi;
        private String url;




        String getUrl() {
            return url;
        }

        void setUrl(String url) {
            this.url = url;
        }

        int getSekmesayi() {
            return sekmesayi;
        }

        void setSekmesayi(int sekmesayi) {
            this.sekmesayi = sekmesayi;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position)
            {

                case 0:
                    return new FragmentAra().newInstance(cookie,kelime,nick);

                case 1:
                    return new BasliklarFragment().newInstance(0,cookie,mSectionsPagerAdapter.getUrl(),nick);

                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            return getSekmesayi();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case 0:
                    return "Ara";

                default:
                    return "Sonuçlar";


            }
        }


    }
}
