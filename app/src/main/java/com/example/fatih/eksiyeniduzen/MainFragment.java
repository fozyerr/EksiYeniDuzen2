package com.example.fatih.eksiyeniduzen;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 19.09.2017.
 */

public class MainFragment extends Fragment implements MainFragHaberlesme {

    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    //MainActivity mainact;
  //  Spinner spinner;
    boolean acikmi;

  //  FloatingActionButton tarihsec,aramafab;
    //Animation acilis,kapanis;

    RelativeLayout rev;
    int toplamsayfa;
    ImageButton saga,sola,copbosalt;

    MainfragdanMainacte mainfragInterface;

    public MainFragment() {
    }




    public static MainFragment newInstance(String cookie,String nick,boolean copmu) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        args.putBoolean("cop",copmu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);


       if(!getArguments().getBoolean("cop"))
       {
          // mainact= (MainActivity) getActivity();
         //  tarihsec= mainact.findViewById(R.id.tarihsec);
          // aramafab= mainact.findViewById(R.id.fab);
           //  tarihsecmeislemi();

        //   acilis= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_open);
         //  kapanis=AnimationUtils.loadAnimation(getActivity(),R.anim.fab_kapat);

           Log.d("MainFragment",getArguments().getString("cookie")+"  "+getArguments().getString("nick"));




           List<String> liste=new ArrayList<>();

           liste.add("Bugün");
           liste.add("gündem");
           liste.add("tarihte bugün");

           if(!getArguments().getString("cookie").equals("BOS"))
           {
               liste.add("yazdıkları");
               liste.add("favladıkları");
               liste.add("son");
               liste.add("kenar");
               liste.add("çaylaklar");
           }

           liste.add("spor");
           liste.add("ilişkiler");
           liste.add("siyaset");
           liste.add("seyahat");
           liste.add("otomotiv");
           liste.add("tv");
           liste.add("anket");
           liste.add("bilim");
           liste.add("edebiyat");
           liste.add("eğitim");
           liste.add("ekonomi");
           liste.add("ekşi-sözlük");
           liste.add("haber");
           liste.add("havacılık");
           liste.add("magazin");
           liste.add("moda");
           liste.add("motosiklet");
           liste.add("müzik");
           liste.add("oyun");
           liste.add("programlama");
           liste.add("sağlık");
           liste.add("sanat");
           liste.add("sinema");
           liste.add("spoiler");
           liste.add("tarih");
           liste.add("teknoloji");
           liste.add("yeme-içme");
           liste.add("başıboşlar");
           liste.add("video");





           mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),getArguments().getString("cookie"),getArguments().getString("nick"));

           mSectionsPagerAdapter.setCop(1);
           mSectionsPagerAdapter.setKanalListesi(liste);


           mViewPager =  rootView.findViewById(R.id.container);
           mViewPager.setAdapter(mSectionsPagerAdapter);
          // spinner=  mainact.findViewById(R.id.baslikspinner);
          // mViewPager.setOffscreenPageLimit(4);

           ArrayAdapter<String> xmlarray;
           if (getArguments().getString("cookie").equals("BOS"))
           {
               xmlarray=new ArrayAdapter<>(getActivity(),R.layout.spinnertext, getResources().getStringArray(R.array.basliklar));
           }
           else
           {
               xmlarray=new ArrayAdapter<>(getActivity(),R.layout.spinnertext, getResources().getStringArray(R.array.basliklarLogin));
           }


           mainfragInterface.spinnerAdaptoru(xmlarray);
          // spinner.setAdapter(xmlarray);

           viewpagerListener();

       }
        else
       {
        /*  rev= (RelativeLayout) rootView.findViewById(R.id.reveal);

           rev.setVisibility(View.INVISIBLE);
           rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

               @Override
               public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                   v.removeOnLayoutChangeListener(this);

                   int cx = rev.getWidth() / 2;
                   int cy = rev.getHeight() / 2;

// get the final radius for the clipping circle
                   float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
                   Animator anim =       ViewAnimationUtils.createCircularReveal(rev, cx, cy, 0, finalRadius);

// make the view visible and start the animation
                   rev.setVisibility(View.VISIBLE);
                   anim.start();
               }
           });


           ÇALIŞIYOR SAĞA SOLA GİTME BUTONLARINI BU LAYOUTA EKLE


           */

     /*   Animation   slidedown = AnimationUtils.loadAnimation(getActivity(),
                   R.anim.slide_down);

       Animation   slideup = AnimationUtils.loadAnimation(getActivity(),
                   R.anim.slide_up);*/

           copbosalt=rootView.findViewById(R.id.copubosalt);
           saga=  rootView.findViewById(R.id.sag);
           sola= rootView.findViewById(R.id.sol);


           sagsolclick();
           rev=  rootView.findViewById(R.id.reveal);

         /*  rev.setVisibility(View.INVISIBLE);
           rev.startAnimation(slidedown);
           rev.setVisibility(View.VISIBLE);*/








           mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),getArguments().getString("cookie"),getArguments().getString("nick"));

           mSectionsPagerAdapter.setSayfasayisi(1);
           mSectionsPagerAdapter.setCop(2);
           mSectionsPagerAdapter.notifyDataSetChanged();

           mViewPager = rootView.findViewById(R.id.container);
           mViewPager.setAdapter(mSectionsPagerAdapter);
       }



       /* TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }



    private void sagsolclick() {

        saga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(toplamsayfa,true);
            }
        });
        sola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0,true);
            }
        });

        copbosalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                copbosaltmaislemi();

            }
        });


    }

    private void copbosaltmaislemi() {

        LayoutInflater inflator= LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams") final View view=inflator.inflate(R.layout.yamuk_koseli_dialog, null);


        final AlertDialog cikisdialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder
                //.setTitle("çıkış")
                .setView(view)
                // .setMessage("kapansın mı")
                .setCancelable(false);

        // final AlertDialog dialog=builder.create();
        cikisdialog=builder.create();

        Button evet=view.findViewById(R.id.evet);
        Button hayir=view.findViewById(R.id.hayir);
        TextView copbosaltext=view.findViewById(R.id.titletext);
        copbosaltext.setText(getResources().getString(R.string.copbosaltma));


        cikisdialog.show();

        cikisdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        evet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"emin misin birader uleeeee",Toast.LENGTH_SHORT).show();
                cikisdialog.dismiss();
            }
        });

        hayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cikisdialog.dismiss();
            }
        });
    }



   /* private void tarihsecmeislemi() {



        tarihsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("MainFragment","Diyalogla tarih seçilecek");
                Toast.makeText(getActivity(),"Tarih seçme diyaloğu ekle",Toast.LENGTH_SHORT).show();

            }
        });
    }*/

    private void viewpagerListener() {

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {


                    mainfragInterface.spinnerselection(position);
                    //spinner.setSelection(position,false);

                    if(position==2)
                    {

                       /* if(aramafab.getVisibility()==View.VISIBLE)
                        {
                            CoordinatorLayout.LayoutParams takvim= (CoordinatorLayout.LayoutParams) tarihsec.getLayoutParams();
                            takvim.setMargins(0,0,16,225);
                            tarihsec.setLayoutParams(takvim);
                            tarihsec.startAnimation(acilis);

                        }
                        else
                        {
                            tarihsec.startAnimation(acilis);
                        }*/
                      //  tarihsec.startAnimation(acilis);
                       // tarihsec.show();
                   //    tarihsec.setVisibility(View.VISIBLE);
                        mainfragInterface.gorunurTiklanabilir();
                        acikmi=true;
                      //  tarihsec.setClickable(true);
                    }
                    else
                    {
                        if(acikmi)
                        {



                            mainfragInterface.viewGone();
                           // tarihsec.startAnimation(kapanis);
                           // tarihsec.setVisibility(View.GONE);
                           // tarihsec.hide();
                            acikmi=false;
                           // tarihsec.setClickable(false);
                        }
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mainfragInterface.spinnerListener();

      /*  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mViewPager.setCurrentItem(position,false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


    }

    public void ViewPagerSayfaGecisActivityden(int position)
    {

        Log.d("MAİNFRAG","ULA ACTİVİTYDEN GELMİŞTURRR");
        mViewPager.setCurrentItem(position,false);
    }


    @Override
    public void sayfasayisikur(int sayfasayisi) {

        mSectionsPagerAdapter.setSayfasayisi(sayfasayisi);
        mSectionsPagerAdapter.notifyDataSetChanged();
        toplamsayfa=sayfasayisi;

    }

    @Override
    public void yonlendiricikur(boolean gozukecekmi) {
        if(gozukecekmi)
        {


         /*   if(rev.getVisibility()==View.GONE)
            {
                Animation   slidedown = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_up);

                rev.setVisibility(View.INVISIBLE);
                rev.startAnimation(slidedown);
                rev.setVisibility(View.VISIBLE);
            }*/
            rev.setVisibility(View.VISIBLE);


        }
        else
            rev.setVisibility(View.GONE);

    }


    public static class SectionsPagerAdapter extends FragmentPagerAdapter {


        private List<String> kanalListesi;
        private int cop;
        private int sayfasayisi;

        String cookie;
        String nick;




         SectionsPagerAdapter(FragmentManager fm,String cookie,String nick) {
            super(fm);
            this.cookie=cookie;
            this.nick=nick;
        }

        @Override
        public Fragment getItem(int position) {


            if(getCop()==1)
            {
                return BasliklarFragment.newInstance(position + 1,cookie,"",nick);
            }
            else
            {
                return FragmentCop.newInstance(position + 1,cookie,nick);
            }


        }

        @Override
        public int getCount() {

            if(getCop()==1)
            {
                return getKanalListesi().size();
            }else
            {
                return getSayfasayisi();
            }



        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                default:
                    if(getCop()==1)
                    {
                     return getKanalListesi().get(position);
                    }
                    else
                    {
                        return String.valueOf(position+1);
                    }


            }
        }


         void setKanalListesi(List<String> kanalListesi)
        {
            this.kanalListesi=kanalListesi;

        }
         List<String> getKanalListesi()
        {
            return this.kanalListesi;

        }

        public int getCop() {
            return cop;
        }

        public void setCop(int cop) {
            this.cop = cop;
        }
        public int getSayfasayisi() {
            return sayfasayisi;
        }

        public void setSayfasayisi(int sayfasayisi) {
            this.sayfasayisi = sayfasayisi;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("mainfrag","Mainfragment activitye bağlandı");
        mainfragInterface= (MainfragdanMainacte) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainfragInterface=null;
        Log.d("mainfrag","Mainfragment activityden koptu silinmiş olması lazım");
    }
}
