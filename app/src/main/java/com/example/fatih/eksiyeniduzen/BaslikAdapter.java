package com.example.fatih.eksiyeniduzen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 16.09.2017.
 */
 class BaslikAdapter extends ArrayAdapter<BaslikProvider> {

   private List<BaslikProvider> chatlist=new ArrayList<>();

   private Context ctx;

    private SparseBooleanArray secilmisItemler;



     BaslikAdapter(Context context, int resource) {
        super(context, resource);
        ctx=context;
         secilmisItemler=new SparseBooleanArray();
    }


    @Override
    public void add(BaslikProvider object) {

        chatlist.add(object);

        super.add(object);
    }

    @Override
    public void remove(BaslikProvider object) {
        chatlist.remove(object);
        notifyDataSetChanged();

        super.remove(object);
    }


    @Override
    public int getCount() {
        return chatlist.size();
    }

    @Override
    public BaslikProvider getItem(int position) {
        return chatlist.get(position);
    }


    void bosalt()
    {
        chatlist.clear();
        notifyDataSetChanged();
    }

    void sonuncuyusil()
    {
        chatlist.remove(chatlist.size()-1);
        notifyDataSetChanged();
    }

    void yukleniyorekle()
    {

        chatlist.add(new BaslikProvider(1,0));
        notifyDataSetChanged();
    }
    void ayracekle(int numara)
    {
        chatlist.add(new BaslikProvider(2,numara));
        notifyDataSetChanged();
    }


    public void itemSec(int position, boolean deger) {
        if (deger)
            secilmisItemler.put(position, deger);
        else
            secilmisItemler.delete(position);

        notifyDataSetChanged();
    }

    public int secilenSayisi()
    {
        return secilmisItemler.size();
    }
    public SparseBooleanArray secilenler()
    {
        return secilmisItemler;
    }
    public void secilmeyisifirla()
    {
        secilmisItemler=new SparseBooleanArray();
        notifyDataSetChanged();

    }

    public void itemsecmeislemi(int position)
    {
        itemSec(position,!secilmisItemler.get(position));
    }



    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {


        BaslikProvider provider=getItem(position);


        BaslikViewHolder viewHolder=null;
        ProgressViewHolder progressViewHolder;
        SayfaNumarasi holder=null;

            assert provider != null;
            if(provider.tip==0)
            {

              if(convertView==null || !(convertView.getTag() instanceof  BaslikViewHolder))
              {
                  viewHolder=new BaslikViewHolder();
                  LayoutInflater inflator= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  convertView=inflator.inflate(R.layout.baslikyapisi_constraint,parent,false);

                  viewHolder.entrysayi=  convertView.findViewById(R.id.entrysayi);
                  viewHolder.basliktext= convertView.findViewById(R.id.basliktext);

                  convertView.setTag(viewHolder);
              }
              else
              {
                  viewHolder= (BaslikViewHolder) convertView.getTag();
              }
            }
            else if(provider.tip==1)
            {

                if(convertView==null || !(convertView.getTag() instanceof  ProgressViewHolder))
                {

                    LayoutInflater inflator= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    progressViewHolder=new ProgressViewHolder();
                    convertView=inflator.inflate(R.layout.progressbar_sayfa_devam,parent,false);

                    progressViewHolder.progressBar= convertView.findViewById(R.id.devamet);
                    progressViewHolder.imageView=  convertView.findViewById(R.id.devambuton);

                    convertView.setTag(progressViewHolder);
                }
                else
                {
                    progressViewHolder= (ProgressViewHolder) convertView.getTag();
                }


            }
            else
            {

                if(convertView==null || !(convertView.getTag() instanceof  SayfaNumarasi))
                {
                    LayoutInflater inflator= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    holder=new SayfaNumarasi();
                    convertView=inflator.inflate(R.layout.baslik_sayfa_ayrac,parent,false);

                    holder.sayfanotext= convertView.findViewById(R.id.sayfanumarasi);

                    convertView.setTag(holder);
                }
                else
                {
                    holder= (SayfaNumarasi) convertView.getTag();

                }



            }






            if(provider.tip==0)
            {


                String sayi=provider.entrysayi;
                String baslik=provider.baslik;
                //String link=provider.url;

                viewHolder.entrysayi.setText(sayi);
                viewHolder.basliktext.setText(baslik);




                if(provider.textigriyap)
                {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = ctx.getTheme();
                    theme.resolveAttribute(R.attr.olayText, typedValue, true);
                    @ColorInt int color = typedValue.data;
                    viewHolder.basliktext.setTextColor(color);
                }
                else
                {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = ctx.getTheme();
                    theme.resolveAttribute(R.attr.normalText, typedValue, true);
                    @ColorInt int color = typedValue.data;
                    viewHolder.basliktext.setTextColor(color);
                }



            }
             else if(provider.tip==2)
            {

                String numara="Sayfa "+provider.sayfano;

                holder.sayfanotext.setText(numara);
            }






        return convertView;
    }

    private class BaslikViewHolder
    {
        TextView entrysayi;
        TextView basliktext;

    }
    private static class ProgressViewHolder
    {
        ProgressBar progressBar;
        ImageView imageView;
    }
    private static class SayfaNumarasi
    {
        TextView sayfanotext;
    }
}
