package com.example.fatih.eksiyeniduzen;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Fatih on 17.11.2017.
 */

public class FragmentCop extends Fragment implements MainFragHaberlesme {


    ListView coplist;
    EntryAdapter entryAdapter;
    RelativeLayout progressbar;
    MainFragment mainFragment;

  //  InterfaceArama childmi;

    MainFragHaberlesme fragHaberlesme;

    public static FragmentCop newInstance(int position,String cookie,String nick) {
        FragmentCop fragment = new FragmentCop();
        Bundle args = new Bundle();
        args.putInt("pos",position);
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        fragment.setArguments(args);
        return fragment;
    }





    public void parentFrag(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        fragHaberlesme= (MainFragHaberlesme) childFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entry, container, false);

        mainFragment= (MainFragment) getParentFragment();


        parentFrag(getParentFragment());


        coplist= rootView.findViewById(R.id.listView2);
        progressbar=  rootView.findViewById(R.id.yuklenmespin);

       // coplist.setPadding(0,0,0,40);
      //  coplist.setClipToPadding(false);



        entryAdapter=new EntryAdapter(getActivity(),R.layout.entrylayout,getArguments().getString("cookie"),getArguments().getString("nick"));
        coplist.setAdapter(entryAdapter);
        coplist.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        coplist.setNestedScrollingEnabled(true);


        /*
        String cookie,String nick,EntryAdapter entryAdapter,
                      Context context,RelativeLayout relativeLayout,ListView listView

         */

        new CopEntryGetir(getArguments().getInt("pos"),FragmentCop.this,getArguments().getString("cookie"),getArguments().getString("nick"),
                            entryAdapter,getContext(),progressbar,coplist)
                .execute();







        return rootView;
    }

    @Override
    public void sayfasayisikur(int sayfasayisi) {

        fragHaberlesme.sayfasayisikur(sayfasayisi);

    }

    @Override
    public void yonlendiricikur(boolean gozukecekmi) {

        fragHaberlesme.yonlendiricikur(gozukecekmi);

    }


    private static class CopEntryGetir extends AsyncTask<Void,Void,EntryProvider[]>
    {

        private int url;

        private int sayfasayisi;

        MainFragHaberlesme mainFragHaberlesme;

        String cookie;
        String nick;
        EntryAdapter entryAdapter;

        WeakReference<Context> contextWeakReference;
        WeakReference<RelativeLayout> relativeLayoutWeakReference;
        WeakReference<ListView> listViewWeakReference;

        CopEntryGetir(int url,MainFragHaberlesme mainFragHaberlesme,String cookie,String nick,EntryAdapter entryAdapter,
                      Context context,RelativeLayout relativeLayout,ListView listView)
        {
            this.url=url;
            this.mainFragHaberlesme=mainFragHaberlesme;
            this.cookie=cookie;
            this.nick=nick;
            this.entryAdapter=entryAdapter;

            contextWeakReference=new WeakReference<Context>(context);
            relativeLayoutWeakReference=new WeakReference<RelativeLayout>(relativeLayout);
            listViewWeakReference=new WeakReference<ListView>(listView);

        }

        @Override
        protected EntryProvider[] doInBackground(Void... params) {

            Document document;
            EntryProvider[] dizi=null;

            try {
                document= Jsoup.connect("https://eksisozluk.com/cop?p="+url)
                        .cookie("Cookie",cookie)
                        .timeout(10*1000)
                        .get();


                if(!document.select("div.pager").isEmpty())
                {
                    sayfasayisi=Integer.parseInt(document.select("div.pager").attr("data-pagecount"));
                }
                else
                {
                    sayfasayisi=1;
                }

             //   EntryleriAyikla ayikla=new EntryleriAyikla();

                dizi=EntryleriAyikla.copEntryler(document,contextWeakReference.get(),cookie,nick);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return dizi;
        }

        @Override
        protected void onPostExecute(EntryProvider[] entryProviders) {
            super.onPostExecute(entryProviders);

            relativeLayoutWeakReference.get().setVisibility(View.GONE);
            if(entryProviders!=null)
            {


              //  mainFragment.mSectionsPagerAdapter.setSayfasayisi(sayfasayisi);
               // mainFragment.mSectionsPagerAdapter.notifyDataSetChanged();

                mainFragHaberlesme.sayfasayisikur(sayfasayisi);


                for (EntryProvider entryProvider : entryProviders) {
                    entryAdapter.add(entryProvider);
                }
                entryAdapter.notifyDataSetChanged();
                listViewWeakReference.get().setSelectionAfterHeaderView();

                if(url==1)
                {
                    /*if(sayfasayisi==1)
                        childmi.updateView(false, String.valueOf(sayfasayisi));
                    else
                        childmi.updateView(true, String.valueOf(sayfasayisi));*/

                    if(sayfasayisi==1)
                       mainFragHaberlesme.yonlendiricikur(false);
                    else
                        mainFragHaberlesme.yonlendiricikur(true);

                }


            }





        }
    }


    }
