package com.example.fatih.eksiyeniduzen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fatih on 02.10.2017.
 */

public class FragmentLogin extends Fragment {

    boolean girisyapildimi;



    public static FragmentLogin newInstance() {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        // args.putInt("KEY", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        WebView webView=  rootView.findViewById(R.id.webView2);
        WebSettings settings=webView.getSettings();
        girisyapildimi=false;



        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        CookieManager.getInstance().removeAllCookie();


        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

           //     if (url.equals("https://eksisozluk.com/")) {

                    String gerekli = CookieManager.getInstance().getCookie(url);

                    String cookie="atama yapılmamış";
                String iq=gerekli;
                    String[] temp=gerekli.split(";");

                    for (String ar1 : temp ){
                        if(ar1.contains("a")){
                            String[] temp1=ar1.split("=");
                            for(String tekler:temp1)
                            {
                                if(tekler.trim().equals("a"))
                                {
                                    Log.d(tekler.trim(),temp1[1]);
                                    girisyapildimi=true;
                                    cookie = temp1[1];
                                    Log.d("giriş yaptı","giriş yapıldı yönlendirme lazım");
                                }
                            }


                        }
                    }

                if(girisyapildimi)
                {
                    new NickGetir(cookie,iq,getActivity()).execute();
                }

                  /*  if (gerekli.length() > 500) {


                      //  new NickGetir(cookie).execute();

                       // new Nickal(gerekli).execute();


                    } else {

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("cookie", "BOS");
                        editor.apply();
                    }*/




            //    }
            //    else {
                    Log.d("url", url);

                    view.loadUrl(url);

            //    }


                return true;
            }

            public void onPageFinished(WebView view, String url) {
                //     cookieler.put("cookies",CookieManager.getInstance().getCookie(url));
                //  Log.d("cookie", String.valueOf(CookieManager.getInstance().getCookie(url).length()));
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }


        });
        webView.loadUrl("https://eksisozluk.com/giris");




       /* TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }

   static class NickGetir extends AsyncTask<Void,Void,String>
    {
        ProgressDialog dialog;

        String cookie,iq;

        WeakReference<Context> contextWeakReference;

        NickGetir(String cookie,String iq,Context context)
        {
            this.cookie=cookie;
            this.iq=iq;

            contextWeakReference=new WeakReference<Context>(context);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(contextWeakReference.get());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Giriş yapılıyor ...");
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            Document document;
            String nick=null;
            try {
                document= Jsoup.connect("https://eksisozluk.com/")
                        .cookie("Cookie",iq)
                        .get();

                if(document!=null)
                {
                    nick=document.select("li.mobile-only > a").attr("title");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return nick;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            if(s!=null)
            {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("cookie", iq);
                editor.putString("iq",iq);
                editor.putString("nick",s);
                editor.apply();


                Toast.makeText(contextWeakReference.get(),"hoşgeldin "+s,Toast.LENGTH_SHORT).show();
                Intent git = new Intent(contextWeakReference.get(), MainActivity.class);


                contextWeakReference.get().startActivity(git);
            }


        }


    }
}
