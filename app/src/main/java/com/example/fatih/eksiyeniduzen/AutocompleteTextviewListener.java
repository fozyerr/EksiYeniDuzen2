package com.example.fatih.eksiyeniduzen;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Fatih on 24.09.2017.
 */

 class AutocompleteTextviewListener implements TextWatcher, AdapterView.OnItemClickListener {

    private  AutoCompleteTextView autoCompleteTextView;
    private Context context;
    private String cookie;
    private  String nick;
    AutocompleteTextviewListener(AutoCompleteTextView autoCompleteTextView,Context context,String cookie,String nick)
    {
        this.autoCompleteTextView=autoCompleteTextView;
        this.context=context;
        this.cookie=cookie;
        this.nick=nick;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(!autoCompleteTextView.getText().toString().equals(""))
            new AramaIslemi(autoCompleteTextView.getText().toString(),autoCompleteTextView,context,1,"").execute();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (String.valueOf(parent.getItemAtPosition(position)).contains("@")) {

            String yazar= ((String) parent.getItemAtPosition(position)).replace("@","");

            Intent yazara=new Intent(context,YazarActivity.class);
            yazara.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            yazara.putExtra("yazarnick",yazar);
            yazara.putExtra("cookie",cookie);
            yazara.putExtra("nick",nick);
            Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.soldan_giris, R.anim.soldan_cikis).toBundle();
            context.startActivity(yazara,bundle);


        } else {
           String urlyap = "/?q=" + Uri.encode(String.valueOf(parent.getItemAtPosition(position)));

            Intent tekentry=new Intent(context,EntryActivity.class);
            tekentry.putExtra("link", urlyap);
            tekentry.putExtra("cookie",cookie);
            tekentry.putExtra("nick",nick);
            tekentry.putExtra("urlduzenle",true);
            tekentry.putExtra("mark"," ");
            tekentry.putExtra("gelenstring", String.valueOf(parent.getItemAtPosition(position)));
            tekentry.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(tekentry);

          //  new BasligaGit(urlyap).execute();


        }

    }





}
