package com.example.fatih.eksiyeniduzen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class EntryYazmaActivity extends AppCompatActivity implements MainfragdanMainacte {


    RecyclerView recyclerView;
   // EntryButonAdapter entryButonAdapter;
    RecyclerView.LayoutManager manager;
    EditText editText;

    boolean DuzeltmeMi;
    String duzenlemeentryid;

    String paketHazirtext;

    String paketEntry;
    String paketTitle;
    String paketBaslikid;
    String paketVerify;
    String COOKIE,NICK,iq;
    String kenarUrl;
  //  Animation slideup,slidedown;
    boolean kenar,kenardavarmi;

    boolean kenaraAtildi;

    int sorunsaldegeri; // Sorunsaldan numara yollamak gerekli





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_yazma);

/*
        String[] entryButonlar=new String[]{
                "(bkz: hede)",
                "hede",
                "*",
                "spoiler",
                "http"};*/

        List<BadiEngelProvider> butonTextler=new ArrayList<>();

      //  BadiEngelProvider badiEngelProvider=new BadiEngelProvider("(bkz: hede)","","",10);

        butonTextler.add(new BadiEngelProvider("(bkz: hede)","","",10));
        butonTextler.add(new BadiEngelProvider("hede","","",10));
        butonTextler.add(new BadiEngelProvider("*","","",10));
        butonTextler.add(new BadiEngelProvider("spoiler","","",10));
        butonTextler.add(new BadiEngelProvider("http","","",10));
        BadiEngelAdapter entryYazmaAdapter=new BadiEngelAdapter(butonTextler,this,this);


        TextView entryYazilanBaslik= findViewById(R.id.entrybaslik);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Yaz kızım");

        editText=  findViewById(R.id.girilecekentry);
        recyclerView=  findViewById(R.id.ilkrecycle);
        recyclerView.setHasFixedSize(true);
      //  entryButonAdapter=new EntryButonAdapter(entryButonlar);
        manager=new LinearLayoutManager(EntryYazmaActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(entryYazmaAdapter);
      //   slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

//       slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);


        Bundle paket=getIntent().getExtras();

        paketBaslikid=paket.getString("baslikid");
        paketTitle=paket.getString("title");
        paketVerify=paket.getString("verify");
        paketHazirtext=paket.getString("text");

        DuzeltmeMi=paket.getBoolean("edit");
        duzenlemeentryid=paket.getString("editid");
        kenardavarmi=paket.getBoolean("kenardaEntryVarMi");

        iq=paket.getString("cookie");
        NICK=paket.getString("nick");
      //  iq=paket.getString("iq");
        kenarUrl=paket.getString("temizurl");


        entryYazilanBaslik.setText(paketTitle);
        editText.setText(paketHazirtext);

        sorunsaldegeri=paket.getInt("sorunsal");

       // Log.d("sorunsal","sorunsal = "+sorunsaldegeri);


       /*editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {

               if(hasFocus)
               {
                recyclerView.startAnimation(slideup);
               }
               else
               {
                   recyclerView.startAnimation(slidedown);
               }


           }
       });*/

    /*    editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView.startAnimation(slideup);

            }
        });*/


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //String kenarUrl,String cookie,String paketTitle,String paketEntry,Context context

        switch (id) {

            case R.id.gonder:
                entryGonder();
                return true;

            case R.id.sakla:
                paketEntry=editText.getText().toString();
                new EntryKenar(1,kenarUrl,iq,paketTitle,paketEntry,EntryYazmaActivity.this).execute();
                return true;

            case R.id.temizle:

                if(kenardavarmi)
                {
                    paketEntry="";
                    new EntryKenar(0,kenarUrl,iq,paketTitle,paketEntry,EntryYazmaActivity.this).execute();

                }
                else
                    Toast.makeText(getApplicationContext(),"kenarda zaten bir şey yokmuş ki",Toast.LENGTH_SHORT).show();


                return true;

            case android.R.id.home:
                if(!DuzeltmeMi && !kenaraAtildi && !TextUtils.isEmpty(editText.getText().toString()))
                    yazilanEntryiKurtarmaDiyalog();
                else
                    this.finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void entrykenara() {

        if(kenar)
        {
            paketEntry=editText.getText().toString();

            if(!kenardavarmi)
            {
                new EntryKenar(1,kenarUrl,iq,paketTitle,paketEntry,EntryYazmaActivity.this).execute();
            }
            else
                new EntryKenar(0,kenarUrl,iq,paketTitle,paketEntry,EntryYazmaActivity.this).execute();



        }


    }

    private void entryGonder() {

        if(!editText.getText().toString().equals(""))
        {
            paketEntry=editText.getText().toString().replaceAll("\\n", "<br>");
            paketEntry=paketEntry.replaceAll("<br>","\n");

            //String cookie,String paketVerify,String paketTitle,String paketEntry,String paketBaslikid

            new EntryGonder(duzenlemeentryid,EntryYazmaActivity.this,DuzeltmeMi,iq,paketVerify,paketTitle,paketEntry,paketBaslikid).execute();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_entry_yazma, menu);
        return true;
    }

    @Override
    public void gorunurTiklanabilir() {

    }

    @Override
    public void viewGone() {
            this.finish();
    }

    @Override
    public void spinnerselection(int pos) {

    }

    @Override
    public void spinnerAdaptoru(ArrayAdapter<String> xmlarray) {

    }

    @Override
    public void spinnerListener() {

    }

    @Override
    public void entryYazmaButonundanActivitye(int position) {

        String str=editText.getText().toString();
        int selectionStart=editText.getSelectionStart();
        int selectionEnd=editText.getSelectionEnd();
        String selectedText = str.substring(selectionStart, selectionEnd);



        if(position==0)
        {

            if(!selectedText.isEmpty())
            {

                             /*   String selectionModifiedString=str.replace(selectedText,"(bkz: "+selectedText+")");
                                editText.setText(selectionModifiedString);
                                editText.setSelection(selectionModifiedString.length());*/



                int start = Math.max(editText.getSelectionStart(), 0);
                int end = Math.max(editText.getSelectionEnd(), 0);
                String secilentext = str.substring(start, end);
                String replacetext="(bkz: "+secilentext+")";

                editText.getText().replace(Math.min(start, end), Math.max(start, end),
                        replacetext, 0, replacetext.length());
                Log.d("sontext",editText.getText().toString());
                                /*
                                *
                                *
                                *  BU İŞLEMİ BÜTÜN BUTONLARA UYGULA
                                *
                                *
                                *
                                * */


            }
            else
            {
                editText.append("(bkz: )");
            }

        }
        else if(position==1)
        {

            if(!selectedText.isEmpty())
            {


                                /*String selectionModifiedString=str.replace(selectedText,"`"+selectedText+"`");
                                editText.setText(selectionModifiedString);
                                editText.setSelection(selectionModifiedString.length());*/

                int start = Math.max(editText.getSelectionStart(), 0);
                int end = Math.max(editText.getSelectionEnd(), 0);
                String secilentext = str.substring(start, end);
                String replacetext="`"+secilentext+"`";

                editText.getText().replace(Math.min(start, end), Math.max(start, end),
                        replacetext, 0, replacetext.length());
            }
            else
            {
                editText.append("` `");
            }

        }
        else if(position==2)
        {
            if(!selectedText.isEmpty())
            {


                              /*  String selectionModifiedString=str.replace(selectedText,"`:"+selectedText+"`");
                                editText.setText(selectionModifiedString);
                                editText.setSelection(selectionModifiedString.length());*/

                int start = Math.max(editText.getSelectionStart(), 0);
                int end = Math.max(editText.getSelectionEnd(), 0);
                String secilentext = str.substring(start, end);
                String replacetext="`:"+secilentext+"`";

                editText.getText().replace(Math.min(start, end), Math.max(start, end),
                        replacetext, 0, replacetext.length());
            }
            else
            {
                editText.append("`: `");

            }

        }
        else if(position==3)
        {
            if(!selectedText.isEmpty())
            {


                               /* String selectionModifiedString=str.replace(selectedText,"\n--- `spoiler` --- \n"+selectedText+"\n--- `spoiler` --- ");
                                editText.setText(selectionModifiedString);
                                editText.setSelection(selectionModifiedString.length());*/

                int start = Math.max(editText.getSelectionStart(), 0);
                int end = Math.max(editText.getSelectionEnd(), 0);
                String secilentext = str.substring(start, end);
                String replacetext="\n--- `spoiler` --- \n"+secilentext+"\n--- `spoiler` --- ";

                editText.getText().replace(Math.min(start, end), Math.max(start, end),
                        replacetext, 0, replacetext.length());
            }
            else
            {
                editText.append("\n--- `spoiler` --- \n --- `spoiler` --- \n");
            }
        }
        else if(position==4)
        {

            if(!selectedText.isEmpty())
            {


                              /*  String selectionModifiedString=str.replace(selectedText,"[http:// "+selectedText+"]");
                                editText.setText(selectionModifiedString);
                                editText.setSelection(selectionModifiedString.length());*/

                int start = Math.max(editText.getSelectionStart(), 0);
                int end = Math.max(editText.getSelectionEnd(), 0);
                String secilentext = str.substring(start, end);
                String replacetext="[http:// "+secilentext+"]";

                editText.getText().replace(Math.min(start, end), Math.max(start, end),
                        replacetext, 0, replacetext.length());
            }
            else
            {
                editText.append("[http:// ]");
            }


        }


    }


    @Override
    public void onBackPressed() {

        if(!DuzeltmeMi && !kenaraAtildi && !TextUtils.isEmpty(editText.getText().toString()))
            yazilanEntryiKurtarmaDiyalog();
        else
        super.onBackPressed();
    }

    private void yazilanEntryiKurtarmaDiyalog() {



        AlertDialog.Builder builder=new AlertDialog.Builder(EntryYazmaActivity.this);
        final AlertDialog kenarDiyalog=builder.setMessage(getResources().getString(R.string.entrykenara))
                .setPositiveButton(getResources().getString(R.string.dursun),null)
       .setNegativeButton(getResources().getString(R.string.durmasin), null)
                .create();

        kenarDiyalog.show();

        Button dursun=kenarDiyalog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button durmasin=kenarDiyalog.getButton(DialogInterface.BUTTON_NEGATIVE);

        dursun.setText(getResources().getString(R.string.dursun));
        durmasin.setText(getResources().getString(R.string.durmasin));
        dursun.setAllCaps(false);
        durmasin.setAllCaps(false);
        dursun.setTextColor(ContextCompat.getColor(this,R.color.eksirenk));
        durmasin.setTextColor(ContextCompat.getColor(this,R.color.acikgri));

        dursun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kenarDiyalog.dismiss();
                paketEntry=editText.getText().toString();
                new EntryKenar(1,kenarUrl,iq,paketTitle,paketEntry,EntryYazmaActivity.this).execute();

            }
        });

        durmasin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kenarDiyalog.dismiss();
                EntryYazmaActivity.this.finish();
            }
        });




    }

    @Override
    public void kenaraAtildiMi(boolean kenaraAtildi) {

        this.kenaraAtildi=kenaraAtildi;

    }


    private static class EntryGonder extends AsyncTask<Void, Void, Void> {


        String entryid;

        //Activity act;

        boolean DuzeltmeMi;
        String cookie;
        String paketVerify,paketTitle,paketEntry,paketBaslikid;

        WeakReference<MainfragdanMainacte> mainfragdanMainacteWeakReference;



        EntryGonder(String entryid,EntryYazmaActivity entryYazmaActivity,boolean DuzeltmeMi,String cookie,String paketVerify,String paketTitle,String paketEntry,String paketBaslikid)
        {
            this.entryid=entryid;
          //  this.act=act;
            this.cookie=cookie;
            this.DuzeltmeMi=DuzeltmeMi;
            this.paketVerify=paketVerify;
            this.paketEntry=paketEntry;
            this.paketBaslikid=paketBaslikid;
            this.paketTitle=paketTitle;

            mainfragdanMainacteWeakReference=new WeakReference<MainfragdanMainacte>(entryYazmaActivity);

        }

        @Override
        protected Void doInBackground(Void... params) {

            Document document=null;

            try {

                if(DuzeltmeMi)
                {
                    document= Jsoup.connect("https://eksisozluk.com/entry/duzelt/"+entryid)
                            .userAgent("Mozilla/5.0 ( compatible )")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .cookie("Cookie", cookie)
                            .timeout(10*1000)
                            .header("Connection", "keep-alive")
                            .data("__RequestVerificationToken", paketVerify)
                            .data("Title", paketTitle)
                            .data("Id", "#"+entryid)
                            .data("Content", paketEntry)
                            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .post();
                }
                else
                {


                    document= Jsoup.connect("https://eksisozluk.com/entry/ekle")
                            .userAgent("Mozilla/5.0 ( compatible )")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.5,en;q=0.3")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .cookie("Cookie", cookie)
                            .timeout(10*1000)
                            .header("Connection", "keep-alive")
                            .data("__RequestVerificationToken", paketVerify)
                            .data("Title", paketTitle)
                            .data("Id", paketBaslikid)
                            .data("Content", paketEntry)
                            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .post();

                }

                if(document!=null)
                {
                    Log.d("entryyazmacevap",document.html());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(mainfragdanMainacteWeakReference.get()!=null)
            mainfragdanMainacteWeakReference.get().viewGone();

           // activityWeakReference.get().finish();

           // this.act.finish();
        }
    }

    private static class EntryKenar extends AsyncTask<Void,Void,Connection.Response>
    {
        private int islem;

        String kenarUrl;
        String cookie;
        String paketTitle;
        String paketEntry;

        WeakReference<Context> contextWeakReference;


        EntryKenar(int islem,String kenarUrl,String cookie,String paketTitle,String paketEntry,Context context)
        {
            this.islem=islem;
            this.kenarUrl=kenarUrl;
            this.cookie=cookie;
            this.paketEntry=paketEntry;
            this.paketTitle=paketTitle;

            contextWeakReference=new WeakReference<Context>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Connection.Response doInBackground(Void... params) {

            Connection.Response document=null;
            String urlyap;

            if(islem==1)
            {
             urlyap="https://eksisozluk.com"+kenarUrl+"/savedraft";
            }
            else
            {
                urlyap="https://eksisozluk.com"+kenarUrl+"/deletedraft";
            }

            try {
                document = Jsoup.connect(urlyap)
                        .userAgent("Mozilla/5.0 ( compatible )")

                        .cookie("Cookie", cookie)
                        .header("X-Requested-With", "XMLHttpRequest")
                        .data("title",paketTitle)
                        .data("content",paketEntry)
                        .ignoreContentType(true)
                        .timeout(10*1000)
                        .method(Connection.Method.POST)
                        .execute();


               // Log.d("kenara",document.body());



            } catch (IOException e) {
                e.printStackTrace();
            }



            return document;
        }

        @Override
        protected void onPostExecute(Connection.Response document) {
            super.onPostExecute(document);
            if(document!=null)
            {
                try {
                    JSONObject jsoncevap=new JSONObject(document.body());
                    MainfragdanMainacte mainfragdanMainacte= (MainfragdanMainacte) contextWeakReference.get();
                    mainfragdanMainacte.kenaraAtildiMi(true);
                    if(islem==1)
                    {



                            Toast.makeText(contextWeakReference.get(),jsoncevap.getString("Message"),Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                            if(!jsoncevap.getString("Snapshot").isEmpty())
                                Toast.makeText(contextWeakReference.get(),"kenardan kaldırıldı",Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
