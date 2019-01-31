package com.example.fatih.eksiyeniduzen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 10.12.2016.
 */
 class SecenekAdapter extends ArrayAdapter<SecenekProvider> {

   private List<SecenekProvider> secenekProvider=new ArrayList<>();


   private Context context;

    @Override
    public void add(SecenekProvider object) {

        secenekProvider.add(object);

        super.add(object);

    }

    @Override
    public int getCount() {
        return secenekProvider.size();
    }

    @Override
    public SecenekProvider getItem(int position) {
        return secenekProvider.get(position);
    }


     SecenekAdapter(Context context, int resource) {
        super(context, resource);
        this.context=context;


    }



    void itemsil(int pos)
    {
        this.remove(this.getItem(pos));
        notifyDataSetChanged();
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

        SecenekViewHolder viewHolder;

        if(convertView==null)
        {
            viewHolder=new SecenekViewHolder();
            LayoutInflater inflator= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflator.inflate(R.layout.seceneklistyapisi,parent,false);
             viewHolder.resim=  convertView.findViewById(R.id.secenekresim);
            viewHolder.textView=  convertView.findViewById(R.id.secenektext);
            convertView.setTag(viewHolder);

        }
        else
        {
                viewHolder= (SecenekViewHolder) convertView.getTag();
        }



        SecenekProvider provider=getItem(position);

        if(provider!=null)
        {
            int id=provider.resimid;
            String text=provider.secenek;


            viewHolder.resim.setImageResource(id);
            viewHolder.textView.setText(text);
        }




     //   Animation animation = AnimationUtils.loadAnimation(context, R.anim.yenianim);
       // convertView.startAnimation(animation);


        return convertView;
    }

    private class SecenekViewHolder
    {
        ImageView resim;
        TextView textView;
    }






}
