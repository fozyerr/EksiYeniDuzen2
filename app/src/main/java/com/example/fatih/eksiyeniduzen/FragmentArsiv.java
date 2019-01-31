package com.example.fatih.eksiyeniduzen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Fatih on 11.12.2017.
 */

public class FragmentArsiv extends Fragment{



    ListView listView;
    BaslikAdapter adapter;
    ProgressBar progressBar;
    ImageButton reload;
    VeriTabaniBaglanti vt;

    // FloatingActionButton aramaMain;

    SwipeRefreshLayout swipeRefreshLayout;


    public static FragmentArsiv newInstance(String cookie,String nick) {
        FragmentArsiv fragment = new FragmentArsiv();
        Bundle args = new Bundle();
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basliklar, container, false);

        progressBar=  rootView.findViewById(R.id.anasayfaspin);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout= rootView.findViewById(R.id.yenile);
        listView=  rootView.findViewById(R.id.listView);
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


       vt =new VeriTabaniBaglanti(getActivity());

        vt.okumakicinac();

        vt.hicentryvarmi();

        ArrayList<BaslikProvider> arsivBasliklar=vt.tumbasliklar();

        vt.veritabaniKapat();
        //Toast.makeText(getActivity(),"Toplam "+arsivBasliklar.size()+" başlık var",Toast.LENGTH_SHORT).show();

        for(BaslikProvider baslik:arsivBasliklar)
        {
            Log.d("baslik",baslik.baslik);
            adapter.add(baslik);
            adapter.notifyDataSetChanged();
        }





        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                final int checkedCount = listView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Seçilmiş Başlık");
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.itemsecmeislemi(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
               // mode.getMenuInflater().inflate(R.menu.coklu_secim, menu);
                getActivity().getMenuInflater().inflate(R.menu.coklu_secim,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class

                        new KayitSilme(vt,adapter,getActivity()).execute(adapter.secilenler());

                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                adapter.secilmeyisifirla();

            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               BaslikProvider provider= (BaslikProvider) parent.getItemAtPosition(position);

                Intent intent=new Intent(getActivity(),EntryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("link",provider.url);
                intent.putExtra("cookie",getArguments().getString("cookie"));
                intent.putExtra("nick",getArguments().getString("nick"));
                intent.putExtra("urlduzenle",false);
                intent.putExtra("mark"," ");
                intent.putExtra("gelenstring",provider.baslik);
                intent.putExtra("arsiv",true);
                getActivity().startActivity(intent);


            //   vt.okumakicinac();

             //   Log.d("sonuncu yazar",providers[sonuncueleman].yazar+"  "+providers[sonuncueleman].zaman+"  "+providers[sonuncueleman].favlar);

             /*  for(EntryProvider entryler:providers)
               {

                   Log.d("yazar-zaman",entryler.yazar+"  "+entryler.zaman+"  "+entryler.favlar);

               }*/


            //   vt.veritabaniKapat();



               // Toast.makeText(getActivity(),"kayıtlı entryleri görüntüleme çok yakında",Toast.LENGTH_SHORT).show();


            }
        });




        return rootView;
    }
    private static class KayitSilme extends AsyncTask<SparseBooleanArray,Void,SparseBooleanArray>
    {

        ProgressDialog dialog;

        WeakReference<VeriTabaniBaglanti> veriTabaniBaglantiWeakReference;
        WeakReference<BaslikAdapter> baslikAdapterWeakReference;
        WeakReference<Context> contextWeakReference;

        KayitSilme(VeriTabaniBaglanti veriTabaniBaglanti,BaslikAdapter baslikAdapter,Context context)
        {
            veriTabaniBaglantiWeakReference=new WeakReference<VeriTabaniBaglanti>(veriTabaniBaglanti);
            baslikAdapterWeakReference=new WeakReference<BaslikAdapter>(baslikAdapter);
            contextWeakReference=new WeakReference<Context>(context);

        }



        @Override
        protected SparseBooleanArray doInBackground(SparseBooleanArray... params) {

            VeriTabaniBaglanti vt=veriTabaniBaglantiWeakReference.get();


            SparseBooleanArray secilenler = params[0];

            vt.veritabaniAc();


            // Captures all selected ids with a loop
            for (int i = (secilenler.size() - 1); i >= 0; i--) {
                if (secilenler.valueAt(i)) {

                    BaslikProvider seciliitem=baslikAdapterWeakReference.get().getItem(secilenler.keyAt(i));

                    Log.d("silinen baslik",seciliitem.entrysayi+"  "+seciliitem.baslik);

                        vt.kayitSil(seciliitem.url);

                    // adapter.remove(seciliitem);

                }
            }




            return secilenler;
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
        protected void onPostExecute(SparseBooleanArray sparseBooleanArray) {
            super.onPostExecute(sparseBooleanArray);

            dialog.dismiss();

            if(veriTabaniBaglantiWeakReference.get()!=null)
            veriTabaniBaglantiWeakReference.get().veritabaniKapat();


            BaslikAdapter baslikAdapter=baslikAdapterWeakReference.get();

            if(baslikAdapter!=null)
            {

                for (int i = (sparseBooleanArray.size() - 1); i >= 0; i--) {
                    if (sparseBooleanArray.valueAt(i)) {

                        BaslikProvider seciliitem=baslikAdapter.getItem(sparseBooleanArray.keyAt(i));
                        baslikAdapter.remove(seciliitem);

                    }
                }
            }




        }
    }

}
