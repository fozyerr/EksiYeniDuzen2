package com.example.fatih.eksiyeniduzen;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Fatih on 19.09.2017.
 */

public class FragmentAra extends Fragment {

    Button ilktarih,ikincitarih,arabuton;
    AutoCompleteTextView yazar;
    EditText kelimeler;
    CheckBox guzeli;
    Spinner uclu;
    String tarih1,tarih2;

 //   FragmentAramaMain aramaMain;
    AramaActivity aramaMain;
    ActivityeBaglanti baglanti;


     /*
    LİNK BÖYLE
    https://eksisozluk.com/basliklar/ara?SearchForm.Keywords=
    olması
    &SearchForm.Author=
    &SearchForm.When.From=10/23/2015
    &SearchForm.When.To=01/15/2012
    &SearchForm.NiceOnly=false
    &SearchForm.SortOrder=Date&_=1507116064422
     */

    public static FragmentAra newInstance(String cookie,String kelime,String nick) {
        FragmentAra fragment = new FragmentAra();
        Bundle args = new Bundle();
        args.putString("cookie",cookie);
        args.putString("kelime",kelime);
        args.putString("nick",nick);
        // args.putInt("KEY", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ara, container, false);


        kelimeler=  rootView.findViewById(R.id.kelimeler);
        yazar= rootView.findViewById(R.id.yazar);
        ilktarih=  rootView.findViewById(R.id.ilktarih);
        ikincitarih= rootView.findViewById(R.id.ikincitarih);
        arabuton=  rootView.findViewById(R.id.araartik);
        guzeli=  rootView.findViewById(R.id.guzel);
        uclu= rootView.findViewById(R.id.secenek);

        tarih1="";
        tarih2="";

     // aramaMain = (FragmentAramaMain) getParentFragment();
        aramaMain= (AramaActivity) getActivity();

        String getKelime=getArguments().getString("kelime");

        assert getKelime != null;
        if(!getKelime.equals("") && !getKelime.contains("/bas"))
        kelimeler.setText(getArguments().getString("kelime"));

        ilktarihsecimi();
        ikincitarihsecimi();
        yazararama();
        aramabutonu();



       return rootView;
    }

    private void aramabutonu() {

        arabuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                StringBuilder  stringBuilder=new StringBuilder();

                String kelime=kelimeler.getText().toString();
                String yazaradi=yazar.getText().toString();
                boolean guzelinden= guzeli.isChecked();

                String siralama="";
                switch (uclu.getSelectedItemPosition())
                {
                    case 0:
                        siralama="Date";
                        break;
                    case 1:
                        siralama="Topic";
                        break;
                    case 2:
                        siralama="Count";
                        break;
                }


                stringBuilder.append("https://eksisozluk.com/basliklar/ara?SearchForm.Keywords=")
                        .append(kelime)
                        .append("&SearchForm.Author=").append(yazaradi)
                        .append("&SearchForm.When.From=").append(tarih1)
                        .append("&SearchForm.When.To=").append(tarih2)
                        .append("&SearchForm.NiceOnly=").append(String.valueOf(guzelinden))
                        .append("&SearchForm.SortOrder=").append(siralama);

                //Log.d("iç fragment",stringBuilder.toString());


                if(aramaMain.mSectionsPagerAdapter.getSekmesayi()!=2)
                {

                    //aramaMain.mSectionsPagerAdapter.setUrl(stringBuilder.toString());

                    aramaMain.mSectionsPagerAdapter.setSekmesayi(2);
                    aramaMain.mSectionsPagerAdapter.notifyDataSetChanged();


                    aramaMain.mViewPager.setCurrentItem(1,true);
                    baglanti.veriGonder(stringBuilder.toString());
                }
                else
                {
                   // aramaMain.mSectionsPagerAdapter.setSekmesayi(1);
                   // aramaMain.mSectionsPagerAdapter.notifyDataSetChanged();

                  //  aramaMain.mSectionsPagerAdapter.setUrl(stringBuilder.toString());

                   // aramaMain.mSectionsPagerAdapter.setSekmesayi(2);
                   // aramaMain.mSectionsPagerAdapter.notifyDataSetChanged();


                    aramaMain.mViewPager.setCurrentItem(1,true);
                    baglanti.veriGonder(stringBuilder.toString());
                }




            }
        });
    }

    private void yazararama() {

        yazar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!yazar.getText().toString().equals(""))
                    new YazarArama(yazar.getText().toString(),yazar,getActivity()).execute();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void ikincitarihsecimi() {
        ikincitarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub

                        //Log.d("2. tarih", dayOfMonth + "/" + monthOfYear+ "/"+year);

                        String tarihiki=(monthOfYear+1)+"/"+dayOfMonth+"/"+year;

                        ikincitarih.setText(tarihiki);
                        ikincitarih.setTextColor(ContextCompat.getColor(getActivity(),R.color.eksirenk));
                        tarih2=(monthOfYear+1)+"/"+dayOfMonth+"/"+year;

                    }
                },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                datePicker.show();


            }
        });
    }

    private void ilktarihsecimi() {
        ilktarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub

                        //Log.d("1. tarih", dayOfMonth + "/" + monthOfYear+ "/"+year);

                        String tarihbir=(monthOfYear+1)+"/"+dayOfMonth+"/"+year;

                        ilktarih.setText(tarihbir);
                        ilktarih.setTextColor(ContextCompat.getColor(getActivity(),R.color.eksirenk));
                        tarih1=(monthOfYear+1)+"/"+dayOfMonth+"/"+year;

                    }
                },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                datePicker.show();


            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baglanti= (ActivityeBaglanti) context;

    }

    interface ActivityeBaglanti {
        void veriGonder(String message);
    }


  static class YazarArama extends AsyncTask<Void,Void,String>
    {

        String query;

        WeakReference<AutoCompleteTextView> autoCompleteTextViewWeakReference;
        WeakReference<FragmentActivity> fragmentActivityWeakReference;

        YazarArama(String query,AutoCompleteTextView autoCompleteTextView,FragmentActivity fragmentActivity)
        {
            this.query=query;

            autoCompleteTextViewWeakReference=new WeakReference<AutoCompleteTextView>(autoCompleteTextView);
            fragmentActivityWeakReference=new WeakReference<FragmentActivity>(fragmentActivity);
        }

        @Override
        protected String doInBackground(Void... params) {

            String jsonveri="";

            try {
                Connection.Response response= Jsoup.connect("https://eksisozluk.com/autocomplete/nick?")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                        .header("X-Requested-With","XMLHttpRequest")
                        .data("term",query)
                        .header("Accept","application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Language","tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                        .method(Connection.Method.GET)
                        .ignoreContentType(true)
                        .execute();


                //Log.d("ress",response.body());

                jsonveri=response.body();



            } catch (IOException e) {
                e.printStackTrace();
            }




            return jsonveri;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            ArrayList<String> list=new ArrayList<>();
            ArrayAdapter<String> adapter;

            try {
                JSONArray object=new JSONArray(json);

                for (int i=0;i<object.length();i++)
                {
                    list.add(object.getString(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            adapter=new ArrayAdapter<>(fragmentActivityWeakReference.get(),R.layout.simpletext,list);

            autoCompleteTextViewWeakReference.get().setAdapter(adapter);

            adapter.notifyDataSetChanged();

        }
    }

}
