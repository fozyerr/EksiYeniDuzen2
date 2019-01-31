package com.example.fatih.eksiyeniduzen;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 31.10.2017.
 */

public class FragmentBadiEngelli extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,BadiEngelFragHaberlesme {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    BadiEngelAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    List<BadiEngelProvider> list;
    Paint p;
    ProgressBar progressBar;

    public static FragmentBadiEngelli newInstance(String cookie,String nick) {
        FragmentBadiEngelli fragment = new FragmentBadiEngelli();
        Bundle args = new Bundle();
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_badiengel, container, false);

        p=new Paint();
        list=new ArrayList<>();
        recyclerView=  rootView.findViewById(R.id.recycleBadiengel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        // manager=new LinearLayoutManagerWithSmoothScroller(getActivity());
        recyclerView.setLayoutManager(manager);
        MainActivity mainActivity= (MainActivity) getActivity();
        coordinatorLayout=  mainActivity.findViewById(R.id.koordinat);
        progressBar=rootView.findViewById(R.id.fragspin);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));


        adapter=new BadiEngelAdapter(list,getActivity(),getArguments().getString("cookie"),getArguments().getString("nick"));
        recyclerView.setAdapter(adapter);

       // yeniitemswipe();


   /*     ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,  ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //awesome code when user grabs recycler card to reorder

                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                //awesome code to run when user drops card and completes reorder
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                if(direction==ItemTouchHelper.RIGHT)
                {
                    Log.d("Sağa kaydı","sağaaaa");
                }
                 else if(direction==ItemTouchHelper.LEFT)
                {
                    Log.d("sola kaydı","SOLAAA");
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
*/



       ItemTouchHelper.SimpleCallback sagasolakaydir=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT,this);

        new ItemTouchHelper(sagasolakaydir).attachToRecyclerView(recyclerView);


        new Listeler(getArguments().getString("cookie"),list,adapter,progressBar).execute();

        return rootView;
    }

    private void yeniitemswipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
             //   int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){

                    Log.d("sola","sola kaydın");
                } else {

                    Log.d("sağa","sağa kaydın");
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    if(viewHolder instanceof BadiEngelAdapter.ViewHolder)
                    {  Drawable icon;
                        View itemView = viewHolder.itemView;
                        int height = itemView.getBottom() - itemView.getTop();
                        int width = itemView.getRight() - itemView.getLeft();
                        float iconH = getResources().getDisplayMetrics().density * 28;
                        float iconW = getResources().getDisplayMetrics().density * 28;

                        if(dX > 0){
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                            p.setColor(Color.parseColor("#FF9800"));
                            c.drawRect(background, p);

                            icon = ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_input_add);
                            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                            // you can also use icon size
                            // int iconWidth = icon.getIntrinsicWidth();
                            // int iconHeight = icon.getIntrinsicHeight();

                            float rate = Math.abs(dX) / width;

                            int iconLeft = (int) (itemView.getLeft() - iconW + width / 3 * rate);
                            int iconTop = (int) (itemView.getTop() + height /2 - iconH/2);
                            int iconRight = (int) (itemView.getLeft() + width / 3 * rate);
                            int iconBottom = (int) (itemView.getBottom() - height/2 + iconH/2);
                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                            icon.draw(c);

                        } else {
                          /*  p.setColor(Color.parseColor("#D32F2F"));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background,p);
                            icon = getBitmapFromVectorDrawable(getActivity(),R.drawable.arama);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                            c.drawBitmap(icon,null,icon_dest,p);*/
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            p.setColor(Color.parseColor("#E91E63"));
                            c.drawRect(background, p);

                            icon = ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_menu_delete);
                            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

                            float rate = Math.abs(dX) / width;

                            int iconLeft = (int) (itemView.getRight() - width / 3 * rate);
                            int iconTop = (int) (itemView.getTop() + height /2 - iconH/2);
                            int iconRight = (int) (itemView.getRight() + iconW - width / 3 * rate);
                            int iconBottom = (int) (itemView.getBottom() - height/2 + iconH/2);
                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                            icon.draw(c);

                        }

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

  /*  Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }*/






    @Override
    public void listeyegerialmaislemi(String gerialmaurl, BadiEngelProvider silinenitem, int pos) {
        new ListeyeGeriAl(gerialmaurl,silinenitem,pos,getArguments().getString("cookie"),adapter,getActivity()).execute();
    }

    @Override
    public void mesajlistkur(List<BadiEngelProvider> mesajlar) {

    }

    @Override
    public void requestverifykur(String requestverify) {

    }

    @Override
    public void gonderilenMesajiRecycleEkle(BadiEngelProvider gonderilenmesaj) {

    }

    @Override
    public void sorunsalKur(List<BadiEngelProvider> sorunsallar, boolean devam) {

    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof BadiEngelAdapter.ViewHolder)
        {
            Log.d("swipeKaydırma",String.valueOf(direction));

            if(direction==ItemTouchHelper.LEFT)
            {
                /*
                String cookie,BadiEngelFragHaberlesme badiEngelFragHaberlesme,
                      BadiEngelAdapter badiEngelAdapter,CoordinatorLayout coordinatorLayout,FragmentActivity fragmentActivity,List<BadiEngelProvider> liste
                 */

                new ListedenCikar(list.get(position).link,position,list.get(position).yazarnick,getArguments().getString("cookie"),FragmentBadiEngelli.this,
                        adapter,coordinatorLayout,getActivity(),list
                ).execute();

            }else if(direction==ItemTouchHelper.RIGHT)
            {
                Intent yazara=new Intent(getActivity(),YazarActivity.class);
                yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                yazara.putExtra("yazarnick", list.get(position).yazarnick);
                yazara.putExtra("yazarid",list.get(position).yazarid);
                yazara.putExtra("cookie",getArguments().getString("cookie"));
                yazara.putExtra("nick",getArguments().getString("nick"));
                // Bundle bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
                //startActivity(intent, bundle);
                startActivity(yazara);
                adapter.notifyDataSetChanged();


            }


        }


    }



    private static class Listeler extends AsyncTask<Void,Void,BadiEngelProvider[]>
    {


        String cookie;

     //   WeakReference< List<BadiEngelProvider>> listWeakReference;
        List<BadiEngelProvider> liste;
        WeakReference<BadiEngelAdapter> badiEngelAdapterWeakReference;
        WeakReference<ProgressBar> progressBarWeakReference;

        Listeler(String cookie,List<BadiEngelProvider> liste,BadiEngelAdapter badiEngelAdapter,ProgressBar progressBar)
        {
            this.cookie=cookie;
            this.liste=liste;

            //listWeakReference=new WeakReference<List<BadiEngelProvider>>(liste);
            badiEngelAdapterWeakReference=new WeakReference<>(badiEngelAdapter);
            progressBarWeakReference=new WeakReference<>(progressBar);

        }



        @Override
        protected BadiEngelProvider[] doInBackground(Void... params) {

            BadiEngelProvider[] badiEngelProvider=null;
            Document document=null;

            int bas1=1,bas2=1,bas3=1;

            try {
                document = Jsoup.connect("https://eksisozluk.com/takip-engellenmis")
                        .cookie("Cookie", cookie)
                        .timeout(10*1000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document!=null)
            {
                Elements h2ler=document.select("ul.relation-list > li > span > a");
                badiEngelProvider=new BadiEngelProvider[h2ler.size()+3];
                int i=0;
                for (Element h:h2ler)
                {
                    String a=h.attr("href");

                    if(a.contains("?r=b"))
                    {
                        if(bas1==1)
                        {
                           // list.add(new BadiEngelProvider("","Badiler","",0));
                            badiEngelProvider[i]=new BadiEngelProvider("","Badiler","",0);
                            bas1=2;
                            i++;
                        }
                       // list.add(new BadiEngelProvider(h.attr("data-userid"),h.attr("data-nick"),h.attr("href"),1));
                        badiEngelProvider[i]=new BadiEngelProvider(h.attr("data-userid"),h.attr("data-nick"),h.attr("href"),1);
                        //  list.add(new BaslikProvider());
                        //    Log.d("badi",h.attr("data-nick"));
                    }
                    else if(a.contains("?r=m")){

                        if(bas2==1)
                        {
                           // list.add(new BadiEngelProvider("","Engelliler","",0));

                            badiEngelProvider[i]=new BadiEngelProvider("","Engelliler","",0);
                            bas2=2;
                            i++;
                        }
                       // list.add(new BadiEngelProvider(h.attr("data-userid"),h.attr("data-nick"),h.attr("href"),1));
                        badiEngelProvider[i]=new BadiEngelProvider(h.attr("data-userid"),h.attr("data-nick"),h.attr("href"),1);


                        //  Log.d("engelli",h.attr("data-nick"));
                    }
                    else if(a.contains("?r=i"))
                    {

                        if(bas3==1)
                        {
                          //  list.add(new BadiEngelProvider("","Başlığı Engelliler","",0));
                            badiEngelProvider[i]=new BadiEngelProvider("","Başlığı Engelliler","",0);
                            bas3=2;
                            i++;
                        }
                       // list.add(new BadiEngelProvider(h.attr("data-userid"),h.attr("data-nick"),h.attr("href"),1));
                        badiEngelProvider[i]=new BadiEngelProvider(h.attr("data-userid"),h.attr("data-nick"),h.attr("href"),1);
                        //  Log.d("başık engelli",h.attr("data-nick"));
                    }
                i++;
                }
            }





            return badiEngelProvider;
        }

        @Override
        protected void onPostExecute(BadiEngelProvider[] badiEngelProvider) {
            super.onPostExecute(badiEngelProvider);

            if(progressBarWeakReference.get()!=null)
                progressBarWeakReference.get().setVisibility(View.GONE);

            if(badiEngelProvider!=null)
            {
              //  List<BadiEngelProvider> liste=listWeakReference.get();

                for(int i=0;i<badiEngelProvider.length;i++)
                {
                    if(badiEngelProvider[i]!=null)
                   liste.add(badiEngelProvider[i]);

                }
                badiEngelAdapterWeakReference.get().notifyDataSetChanged();
            }


        }
    }
    private static class ListedenCikar extends AsyncTask<Void,Void,Void>
    {



        Document document;
        String islemUrl;
        String ynick;
        int pos;

        String cookie;

        WeakReference<BadiEngelFragHaberlesme> badiEngelFragHaberlesmeWeakReference;
        WeakReference<BadiEngelAdapter> badiEngelAdapterWeakReference;
        WeakReference<List<BadiEngelProvider>> lisWeakReference;
        WeakReference<CoordinatorLayout> coordinatorLayoutWeakReference;
        WeakReference<FragmentActivity> fragmentActivityWeakReference;




        ListedenCikar(String islemUrl,int pos,String ynick,String cookie,BadiEngelFragHaberlesme badiEngelFragHaberlesme,
                      BadiEngelAdapter badiEngelAdapter,CoordinatorLayout coordinatorLayout,FragmentActivity fragmentActivity,List<BadiEngelProvider> liste)
        {

            this.islemUrl=islemUrl;
            this.pos=pos;
            this.ynick=ynick;

            this.cookie=cookie;

            lisWeakReference=new WeakReference<List<BadiEngelProvider>>(liste);
            badiEngelFragHaberlesmeWeakReference=new WeakReference<BadiEngelFragHaberlesme>(badiEngelFragHaberlesme);
            badiEngelAdapterWeakReference=new WeakReference<BadiEngelAdapter>(badiEngelAdapter);
            coordinatorLayoutWeakReference=new WeakReference<CoordinatorLayout>(coordinatorLayout);
            fragmentActivityWeakReference=new WeakReference<FragmentActivity>(fragmentActivity);

        }

        @Override
        protected Void doInBackground(Void... params) {

            String url="https://eksisozluk.com"+islemUrl;

            document=null;
            try {
                document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 ( compatible )")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .cookie("Cookie", cookie)
                        .header("Connection", "keep-alive")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .timeout(10*1000)
                        .ignoreContentType(true)
                        .post();


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(document!=null)
            {
                final BadiEngelProvider silinenitem=lisWeakReference.get().get(pos);
                badiEngelAdapterWeakReference.get().itemSil(pos);



                Snackbar snackbar = Snackbar
                        .make(coordinatorLayoutWeakReference.get(), "\""+ynick +"\""+ " bu listeden çıkarıldı", Snackbar.LENGTH_LONG);
                snackbar.setAction("Geri Al", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String gerialmaurl=islemUrl.replace("remove","add");
                     //   new ListeyeGeriAl(gerialmaurl,silinenitem,pos,cookie,adapter,getActivity()).execute();

                        badiEngelFragHaberlesmeWeakReference.get().listeyegerialmaislemi(gerialmaurl,silinenitem,pos);

                        // undo is selected, restore the deleted item
                        // adapter.restoreItem(deletedItem, deletedIndex);
                    }
                });
                snackbar.setActionTextColor(ContextCompat.getColor(fragmentActivityWeakReference.get(),R.color.eksirenk));
                snackbar.show();
            }
            else
                Toast.makeText(fragmentActivityWeakReference.get(),"bir şeyler yanlış gitti",Toast.LENGTH_SHORT).show();


            //Log.d("silinecek pozisyon",badiEngelProvider.yazarnick+" "+String.valueOf(pozisyon));
        }
    }
    private static class ListeyeGeriAl extends AsyncTask<Void,Void,Void>
    {
        private String gerialmaUrl;
        private Document document;
        private BadiEngelProvider geriAlinacakItem;
        private int itemPozisyonu;

        String cookie;

        WeakReference<BadiEngelAdapter> badiEngelAdapterWeakReference;
        WeakReference<Context> contextWeakReference;

        ListeyeGeriAl(String gerialmaUrl,BadiEngelProvider geriAlinacakItem,int itemPozisyonu,String cookie,BadiEngelAdapter badiEngelAdapter,Context context)
        {
            this.gerialmaUrl=gerialmaUrl;
            this.geriAlinacakItem=geriAlinacakItem;
            this.itemPozisyonu=itemPozisyonu;

            this.cookie=cookie;
            badiEngelAdapterWeakReference=new WeakReference<BadiEngelAdapter>(badiEngelAdapter);
            contextWeakReference=new WeakReference<Context>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {

            document=null;
            String url="https://eksisozluk.com"+gerialmaUrl;

            try {
                document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 ( compatible )")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .cookie("Cookie", cookie)
                        .header("Connection", "keep-alive")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .timeout(10*1000)
                        .ignoreContentType(true)
                        .post();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(document!=null)
            {
               badiEngelAdapterWeakReference.get().itemGeriGetir(geriAlinacakItem,itemPozisyonu);
                Log.d("geri alma",document.html());

            }
            else
                Toast.makeText(contextWeakReference.get(),"bir şeyler yanlış gitti",Toast.LENGTH_SHORT).show();

        }
    }

}
