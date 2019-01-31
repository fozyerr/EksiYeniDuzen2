package com.example.fatih.eksiyeniduzen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Fatih on 28.02.2018.
 */

public class MesajAtmaKontrolVeDiyalog extends AsyncTask<Void,Void,Void> {

    String yazarnick;
    String cookie;
    String entryid;
    String mesajVerify;

    boolean mesajatilabilirmi;
    StringBuilder ensontarih;
    AlertDialog dialog;

    WeakReference<Context> contextWeakReference;
    WeakReference<AlertDialog> alertDialogWeakReference;
    boolean tekrarkontrolmü;


    MesajAtmaKontrolVeDiyalog(String yazarnick,String cookie,String entryid,String mesajVerify,Context context,boolean tekrarkontrolmü,AlertDialog icdiyalog)
    {
        this.yazarnick=yazarnick;
        this.cookie=cookie;
        this.entryid=entryid;
        this.mesajVerify=mesajVerify;
        this.tekrarkontrolmü=tekrarkontrolmü;
        contextWeakReference=new WeakReference<>(context);
        alertDialogWeakReference=new WeakReference<>(icdiyalog);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(!tekrarkontrolmü)
        diyalogOlustur();

    }


    @Override
    protected Void doInBackground(Void... voids) {

        Document document=null;
        String url="https://eksisozluk.com/message/recipientinfo?recipient="+yazarnick;
        String json=null;

        try {
            document= Jsoup.connect(url)
                    .cookie("Cookie",cookie)
                    .header("X-Requested-With","XMLHttpRequest")
                    .timeout(10*1000)
                    .ignoreContentType(true)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(document!=null)
        {

            Log.d("json",yazarnick+"  "+document.body().html());
            try {
                JSONObject jsonObject=new JSONObject(document.body().html());
                mesajatilabilirmi=jsonObject.getBoolean("CanSend");
                ensontarih=new StringBuilder();
                ensontarih.append("en son ").append(jsonObject.getString("LatestEntryTurkishDateDiff")).append(" entry girmiş");




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        return null;
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);

        if(tekrarkontrolmü)
        {
            if(alertDialogWeakReference.get()!=null)
                dialog=alertDialogWeakReference.get();
        }


        if(dialog!=null)
        {

            ConstraintLayout icerik=dialog.findViewById(R.id.dialogicerik);
            icerik.setVisibility(View.VISIBLE);

            ProgressBar progressBar=dialog.findViewById(R.id.dialogspin);
            TextView yollanabilirmi=dialog.findViewById(R.id.mesajatilabilirmi);
            TextView ensonnezaman=dialog.findViewById(R.id.ensonnezaman);

            Button pozitif = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negatif=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);




            if(!mesajatilabilirmi)
            {
                Log.d("mesaj","buraya girildi mi");
                Log.d("eee",yollanabilirmi.getText().toString());
                Log.d("xd", String.valueOf(yollanabilirmi.getVisibility()));
                yollanabilirmi.setVisibility(View.VISIBLE);
                pozitif.setClickable(false);
                Log.d("xd", String.valueOf(yollanabilirmi.getVisibility()));
                negatif.setVisibility(View.VISIBLE);
            }
            else
            {
                pozitif.setClickable(true);
                yollanabilirmi.setVisibility(View.GONE);
                pozitif.setVisibility(View.VISIBLE);
                negatif.setVisibility(View.VISIBLE);
            }



            ensonnezaman.setText(ensontarih.toString());
            progressBar.setVisibility(View.GONE);


        }
        else
        {
            Toast.makeText(contextWeakReference.get(),"hata",Toast.LENGTH_SHORT).show();
        }



    }


    private void diyalogOlustur() {



        LayoutInflater inflator= LayoutInflater.from(contextWeakReference.get());
        @SuppressLint("InflateParams") final View view=inflator.inflate(R.layout.mesaj_yolla_dialog, null);



        AlertDialog.Builder builder = new AlertDialog.Builder(contextWeakReference.get());

        builder
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("gönder",null)
                .setNegativeButton("iptal",null);

       dialog=builder.create();


        final EditText mesajicerik=view.findViewById(R.id.mesajicerik);
        final AutoCompleteTextView mesajkime=view.findViewById(R.id.mesajatilanyazar);
        final ProgressBar progressBar=view.findViewById(R.id.progressBar);
        ProgressBar dialogspin=view.findViewById(R.id.dialogspin);
        dialogspin.setVisibility(View.VISIBLE);
        mesajkime.setText(yazarnick);
        mesajicerik.setText(entryid);
        mesajicerik.requestFocus();

        yazaramaislemi(mesajkime,dialog);
        dialog.show();
        // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Button pozitif = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negatif=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        negatif.setAllCaps(false);
        pozitif.setAllCaps(false);
        negatif.setVisibility(View.GONE);
        pozitif.setVisibility(View.GONE);
        pozitif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!mesajicerik.getText().toString().isEmpty())
                    new MesajGonder(cookie,mesajVerify,mesajkime.getText().toString(),mesajicerik.getText().toString(),yazarnick,false,contextWeakReference.get(),dialog,progressBar).execute();
                else
                    Toast.makeText(contextWeakReference.get(),"bir şeyler yazmaya ne dersin delikanlı",Toast.LENGTH_SHORT).show();

                //dialog.dismiss();

            }
        });
        negatif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
    private void yazaramaislemi(final AutoCompleteTextView mesajkime, final AlertDialog alertDialog) {

        mesajkime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!mesajkime.getText().toString().isEmpty())
                    new YazarAra(mesajkime.getText().toString(),mesajkime,contextWeakReference.get()).execute();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mesajkime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String tekrarnick=adapterView.getItemAtPosition(i).toString();

                new MesajAtmaKontrolVeDiyalog(tekrarnick,cookie,"",mesajVerify,contextWeakReference.get(),true,alertDialog).execute();


            }
        });

    }



}
