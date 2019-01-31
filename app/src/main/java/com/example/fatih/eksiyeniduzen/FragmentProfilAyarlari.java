package com.example.fatih.eksiyeniduzen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Fatih on 21.01.2018.
 */

public class FragmentProfilAyarlari extends Fragment implements ProfilAyarlariInterface {


    CheckBox copsilCheck,eksiseylerCheck,mesajkapaCheck,badimesajCheck;
    SeekBar entrylimit,basliklimit;
     TextView entrygosterge,baslikgosterge;

    public static FragmentProfilAyarlari newInstance(String cookie,String nick) {
        FragmentProfilAyarlari fragment = new FragmentProfilAyarlari();
        Bundle args = new Bundle();
        // args.putInt("KEY", sectionNumber);
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profil_ayarlari, container, false);

        copsilCheck=rootView.findViewById(R.id.copsil);
        eksiseylerCheck=rootView.findViewById(R.id.eksiseyler);
        mesajkapaCheck=rootView.findViewById(R.id.mesajyok);
        badimesajCheck=rootView.findViewById(R.id.badimesaj);
        entrylimit=rootView.findViewById(R.id.sayfabasientry);
        entrygosterge=rootView.findViewById(R.id.entrygosterge);
        basliklimit=rootView.findViewById(R.id.basliklimit);
        baslikgosterge=rootView.findViewById(R.id.baslikgosterge);

        entrylimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(i==0)
                {
                    entrygosterge.setText(getResources().getString(R.string.on));
                }
                else
                {
                    entrygosterge.setText(String.valueOf(i*25));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        basliklimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                baslikgosterge.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new ProfilAyarlariCek(FragmentProfilAyarlari.this).execute(getArguments().getString("cookie"));



        return rootView;
    }


    @Override
    public void profilAyarlariGonder(boolean copsil, boolean eksiseyler, boolean mesajkapa, boolean badimesaj, int sayfabasientry, int sayfabasibaslik) {

        copsilCheck.setChecked(copsil);
        eksiseylerCheck.setChecked(eksiseyler);
        mesajkapaCheck.setChecked(mesajkapa);
        badimesajCheck.setChecked(badimesaj);

        if(sayfabasientry==10)
        {
            entrylimit.setProgress(0);
        }
        else
        {
            entrylimit.setProgress(sayfabasientry/25);
        }

        basliklimit.setProgress((sayfabasibaslik/25)-1);

        String entrytexti="sayfa başı entry "+sayfabasientry;
        String basliktexti="sayfa başı başlık "+sayfabasibaslik;

        entrygosterge.setText(entrytexti);
        baslikgosterge.setText(basliktexti);


    }

    static class ProfilAyarlariCek extends AsyncTask<String,Void,Bundle>
    {

        private ProfilAyarlariInterface profilAyarlariInterface;

        ProfilAyarlariCek(ProfilAyarlariInterface profilAyarlariInterface)
        {
            this.profilAyarlariInterface=profilAyarlariInterface;
        }
        private String cookieyiayir(String gerekli) {
            String[] temp=gerekli.split(";");
            String donecek="BOS";

            for (String ar1 : temp ){
                if(ar1.contains("a")){
                    String[] temp1=ar1.split("=");
                    for(String tekler:temp1)
                    {
                        if(tekler.trim().equals("a"))
                        {
                            //   Log.d("başlıkfragasynctask",temp1[1]);
                            donecek=temp1[1];
                            //Log.d("giriş yaptı","giriş yapıldı yönlendirme lazım");
                        }
                    }
                }
            }
            return donecek;
        }


        @Override
        protected Bundle doInBackground(String... strings) {

            Bundle bundle=null;

            Document document=null;



            try {


                String aCookie=cookieyiayir(strings[0]);

                document= Jsoup.connect("https://eksisozluk.com/ayarlar/tercihler")
                       // .header("X-Requested-With","XMLHttpRequest")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                        .timeout(10*1000)
                        .cookie("Cookie",strings[0])
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(document!=null)
            {

                bundle=new Bundle();

                boolean copsil,eksiseyler,mesajkapa,badimesaj;
                int entrysayi,basliksayi;

                Log.d("copsil", String.valueOf(document.select("input#SeylerDisallow").hasAttr("checked")));

                copsil=!document.select("input#SkipTrash").attr("checked").isEmpty();
                eksiseyler=!document.select("input#SeylerDisallow").attr("checked").isEmpty();
                mesajkapa=!document.select("input#MessagingDisabled").attr("checked").isEmpty();
                badimesaj=!document.select("input#OnlyBuddiesCanSendMessage").attr("checked").isEmpty();

               // Element entry=document.select("fieldset").last().select("div > div").first();
              //  Element baslik=document.select("fieldset").last().select("div > div").last();


                entrysayi= Integer.parseInt(document.select("input#TopicPageSize").attr("value"));
                basliksayi= Integer.parseInt(document.select("input#IndexPageSize").attr("value"));

              //  document.select("div.checkbox").first().remove();
                //document.select("div.checkbox").first().remove();
               // Elements inputlar=document.select("div.checkbox");

              /*  Log.d("inputlar", String.valueOf(inputlar.size()));

                for(Element input:inputlar)
                {
                    Log.d("input",input.html());
                }*/


                bundle.putBoolean("copsil",copsil);
                bundle.putBoolean("eksiseyler",eksiseyler);
                bundle.putBoolean("mesajkapa",mesajkapa);
                bundle.putBoolean("badimesaj",badimesaj);

                bundle.putInt("entry",entrysayi);
                bundle.putInt("baslik",basliksayi);

                Log.d("değerler",copsil+" "+eksiseyler+" "+mesajkapa+" "+badimesaj+" "+entrysayi+" "+basliksayi);

            }


            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            super.onPostExecute(bundle);

            if(bundle!=null)
            {

                profilAyarlariInterface.profilAyarlariGonder(bundle.getBoolean("copsil"),bundle.getBoolean("eksiseyler"),bundle.getBoolean("mesajkapa"),bundle.getBoolean("badimesaj"),
                        bundle.getInt("entry"),bundle.getInt("baslik"));
            }



        }
    }


}
