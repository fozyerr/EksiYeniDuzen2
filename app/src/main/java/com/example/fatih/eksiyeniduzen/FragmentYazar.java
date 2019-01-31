package com.example.fatih.eksiyeniduzen;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Fatih on 26.09.2017.
 */

public class FragmentYazar extends Fragment implements InterfaceArama {


    ListView listView;
    EntryAdapter adapter;

   // RelativeLayout progressBar;
   ConstraintLayout progressBar;


    boolean loading;

    int sayfano;

    String[] urller;

    //YazarActivity yazarActivity;

    public FragmentYazar() {

        urller=new String[]
                {
                    "https://eksisozluk.com/son-entryleri?nick=",
                        "https://eksisozluk.com/favori-entryleri?nick=",
                        "https://eksisozluk.com/en-cok-favorilenen-entryleri?nick=",
                        "https://eksisozluk.com/son-oylananlari?nick=",
                        "https://eksisozluk.com/bu-hafta-dikkat-cekenleri?nick=",
                        "https://eksisozluk.com/el-emegi-goz-nuru?nick=",
                        "https://eksisozluk.com/en-begenilenleri?nick=",
                        "https://eksisozluk.com/favori-yazarlari?nick=",
                        "https://eksisozluk.com/katkida-bulundugu-kanallar?nick=",
                        "https://eksisozluk.com/ukteleri?nick="

                };




    }


    public static FragmentYazar newInstance(int sectionNumber,String yazarnick,String cookie,String nick,String mesajverify) {
        FragmentYazar fragment = new FragmentYazar();
        Bundle args = new Bundle();
        args.putInt("KEY", sectionNumber);
        args.putString("yazar",yazarnick);
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        args.putString("verify",mesajverify);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_yazar_constraint, container, false);


        //Log.d("yazarfrag",getArguments().getString("cookie"));
        loading=false;
        sayfano=1;
        progressBar=  rootView.findViewById(R.id.yuklenmespin);
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

        StringBuilder builder=new StringBuilder();

        builder.append(urller[getArguments().getInt("KEY")-1]).append(getArguments().getString("yazar")).append("&p=").append(sayfano);

       // Log.d("profil",builder.toString());
        new YazarProfiliCek(builder.toString(),false,false,getArguments().getString("cookie"),getArguments().getString("nick"),adapter,
                progressBar,FragmentYazar.this,listView,getContext(),getArguments().getString("verify")).execute();

      // listviewscrolllistener();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EntryProvider pro= (EntryProvider) parent.getItemAtPosition(position);

                if(pro.tip==1)
                {

                    parent.getRootView().findViewById(R.id.devambuton).setVisibility(View.GONE);
                    parent.getRootView().findViewById(R.id.devamet).setVisibility(View.VISIBLE);

                    StringBuilder builder2=new StringBuilder();

                    sayfano++;
                    builder2.append(urller[getArguments().getInt("KEY")-1]).append(getArguments().getString("yazar")).append("&p=").append(sayfano);

                    //  adapter.yukleniyorekle();
                    loading=true;

                    /*
                    boolean loading,String cookie,String nick,EntryAdapter entryAdapter,
                        ConstraintLayout constraintLayout,FragmentYazar fragmentYazar,ListView listView,Context context
                     */

                    new YazarProfiliCek(builder2.toString(),true,true,getArguments().getString("cookie"),getArguments().getString("nick"),adapter,
                                                            progressBar,FragmentYazar.this,listView,getContext(),getArguments().getString("verify")
                            ).execute();

                }

            }
        });


        return rootView;
    }

    private void listviewscrolllistener() {

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = listView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        //  aramaMain.setVisibility(View.GONE);
                        return;
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem){
                    View v =  listView.getChildAt(totalItemCount-1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {

                        if(!loading)
                        {
                           // Log.d("FRAGMENTYAZAR","sonmu");
                          //  adapter.yukleniyorekle();
                            //loading=true;
                        }


                        return;
                    }
                }

                else
                {
                    // aramaMain.setVisibility(View.VISIBLE);
                }

            }



        });

    }

    @Override
    public void yazarprofiliVerifygonder(String mesajverify) {

    }

    @Override
    public void updateView(boolean video, String videoURL) {
        this.loading=false;
    }


    static class YazarProfiliCek extends AsyncTask<Void,Void,EntryProvider[]>
    {

        String url;
        boolean butondangelme;

        String cookie;
        String nick;
        String mesajverify;
        boolean loading;

        EntryAdapter entryAdapter;

        WeakReference<ConstraintLayout> constraintLayoutWeakReference;
        WeakReference<Context> contextWeakReference;
        WeakReference<FragmentYazar> fragmentYazarWeakReference;
        WeakReference<ListView> listViewWeakReference;


        YazarProfiliCek(String url,boolean butondangelme,boolean loading,String cookie,String nick,EntryAdapter entryAdapter,
                        ConstraintLayout constraintLayout,FragmentYazar fragmentYazar,ListView listView,Context context,String mesajverify
        )
        {
            this.url=url;
            this.butondangelme=butondangelme;
           // this.sayfano=sayfano;
            this.cookie=cookie;
            this.nick=nick;
            this.loading=loading;
            this.mesajverify=mesajverify;
            this.entryAdapter=entryAdapter;

            constraintLayoutWeakReference=new WeakReference<ConstraintLayout>(constraintLayout);
            fragmentYazarWeakReference=new WeakReference<FragmentYazar>(fragmentYazar);
            listViewWeakReference=new WeakReference<ListView>(listView);
            contextWeakReference=new WeakReference<Context>(context);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressBar.setVisibility(View.VISIBLE);

            if(!butondangelme)
            constraintLayoutWeakReference.get().setVisibility(View.VISIBLE);

         /*   if(!adapter.isEmpty())
            {
                adapter.remove(new EntryProvider(1));
                adapter.notifyDataSetChanged();
            }*/

        }

        @Override
        protected EntryProvider[] doInBackground(Void... params) {


            Document document;
            EntryProvider[] dizi=null;


            try {
                document= Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                        .header("Accept","*/*")
                        .cookie("Cookie",cookie)
                        .header("X-Requested-With","XMLHttpRequest")
                        .timeout(10*1000)
                        .get();



                //EntryleriAyikla ayikla=new EntryleriAyikla();

                dizi=EntryleriAyikla.entryler(document,contextWeakReference.get(),true,cookie,nick,mesajverify);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return dizi;
        }

        @Override
        protected void onPostExecute(EntryProvider[] entryProviders) {
            super.onPostExecute(entryProviders);


            if(loading && butondangelme)
            {
                InterfaceArama loadingkur=fragmentYazarWeakReference.get();

               // loading=false;
                entryAdapter.sonuncuyusil();
                loadingkur.updateView(false,"");


            }

            if(constraintLayoutWeakReference.get()!=null)
            constraintLayoutWeakReference.get().setVisibility(View.GONE);
            //progressBar.setVisibility(View.GONE);
          /*  if(entryProviders!=null)
            {
                for(int i=0;i<entryProviders.length;i++)
                {
                    adapter.add(entryProviders[i]);
                }
               // adapter.add(new EntryProvider(1));

                if(entryProviders.length>=10)
                adapter.yukleniyorekle();

                adapter.notifyDataSetChanged();

                if(adapter.getCount()<12)
                listView.setSelectionAfterHeaderView();

            }*/

            if(entryProviders!=null)
            {
                for(int i=0;i<entryProviders.length;i++)
                {
                    entryAdapter.add(entryProviders[i]);
                }
                // adapter.add(new EntryProvider(1));

                if(entryProviders.length>=10)
                    entryAdapter.yukleniyorekle();

                entryAdapter.notifyDataSetChanged();



                if(entryAdapter.getCount()<12)
                {
                    if(listViewWeakReference.get()!=null)
                    {
                        listViewWeakReference.get().setSelectionAfterHeaderView();
                    }
                }


            }

        }

    }

}
