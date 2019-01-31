package com.example.fatih.eksiyeniduzen;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fatih on 18.09.2017.
 */

 class EntryleriAyikla {


    static EntryProvider[] entryler(Document document, Context context,boolean yazarmi,String cookie,String nick,String mesajverify)
    {
        EntryProvider[] entryProviders=null;

        if(document!=null)
        {

            Elements entries=document.select("ul#entry-item-list > li");
            String baslikid,basliktext="";
            String mesajVerify="";
            baslikid=document.select("h1").attr("data-id");

          //  Log.d("entryayiklama",baslikid);

            entryProviders=new EntryProvider[entries.size()];
            Elements h1ler=null;
            if(yazarmi){
               h1ler=document.select("h1#title");
            mesajVerify=mesajverify;
            }
            else
            {
                if(!cookie.equals("BOS"))
                {
                    if(!document.select("form#message-send-form > input").isEmpty())
                    mesajVerify=document.select("form#message-send-form > input").first().attr("value");
                }

                basliktext=document.select("h1").attr("data-title");

            }


         //   Log.d("basliktext",basliktext);
      //      Log.d("doc",document.select("h1").attr("data-title"));




            int e=0;

            for (Element tumentry : entries) {

                String texts;
                SpannableString message;
                String yazar;
                String zaman;
                String favlar;
                String entryid;
                String yazarid;
                String ozelentry="";
                String ozelUrl="";


                boolean favmi;
                boolean yorumvarmi;
                String yorumsayisi;
               String entryler=tumentry.select("div.content").html();



                if(entryler.contains("<mark>"))
                {
                   // texts=entryler.replaceAll("<mark>","<font color='#C11212'> <b>");
                    texts=entryler.replaceAll("<mark>","<a class=\"b\" href=\"BOS\">");

                  //  texts=texts.replaceAll("</mark>","</b> </font>");
                    texts=texts.replaceAll("</mark>","</a>");
                }
                else
                {
                    texts=entryler;
                }


             //   message=new SpannableString(highlightSearchKey(texts,"yunus"));




                Spanned x= Html.fromHtml(texts);
                message=new SpannableString(x.toString());
                Object[] spans=x.getSpans(0,x.length(), Object.class);
                for(Object span:spans)
                {
                    int start=x.getSpanStart(span);
                    int end=x.getSpanEnd(span);
                    int flags=x.getSpanFlags(span);
                    StringBuilder string=new StringBuilder();

                    for(int qop=start;qop<end;qop++)
                    {
                        string.append(message.charAt(qop));
                    }



                    //  Log.d("string",string);
                    if(span instanceof URLSpan)
                    {
                        URLSpan urlSpan= (URLSpan) span;


                        if (!urlSpan.getURL().startsWith("http")) {
                            //  span=new CallbackSpan(urlSpan.getURL());
                            // Log.d("EntryAyikla",urlSpan.getURL());
                            if(urlSpan.getURL().equals("BOS"))
                            {
                                span=new BackgroundColorSpan(Color.parseColor("#ffff9e"));
                            }else
                            {
                                span=new LinkBaglama(urlSpan.getURL(),string.toString(),context,cookie,nick);
                            }
                            //   Log.d("span",((URLSpan) span).getURL());
                        }
                        else
                        {
                            span=new LinkBaglama(urlSpan.getURL()," ",context,cookie,nick);
                        }

                    }

                    //if(!string.equals(arananKelime))
                        message.setSpan(span, start, end, flags);
                 //   else
                    //    message.setSpan(new BackgroundColorSpan(Color.parseColor("#ffff9e")),start,end,flags);

                }

                yazar=tumentry.attr("data-author");
                zaman=tumentry.select("a.entry-date").first().text();
                favlar=tumentry.attr("data-favorite-count");
                entryid=tumentry.attr("data-id");
                yazarid=tumentry.attr(" data-author-id");
                favmi = tumentry.attr("data-isfavorite").equals("true");
                yorumsayisi=tumentry.attr("data-comment-count");

                if(yorumsayisi.equals("0"))
                    yorumsayisi="";


                if(tumentry.attr("data-flags").contains("comment"))
                    yorumvarmi=true;
                else
                yorumvarmi=false;


                if(yazarmi)
                {
                    baslikid=h1ler.get(e).attr("data-id");
                    ozelentry=h1ler.get(e).attr("data-title");
                    ozelUrl=h1ler.get(e).select("a").attr("href");
                }
                else
                ozelentry=basliktext;



                entryProviders[e]=new EntryProvider(message,yazar,zaman,favlar,favmi,yazarid,entryid,false,baslikid,ozelentry,ozelUrl,0,yorumvarmi,yorumsayisi,mesajVerify);

                e++;

            }



        }
        return entryProviders;
    }
    static EntryProvider[] ArsivIcınAyikla(Document document, Context context,boolean yazarmi,String cookie,String nick)
    {
        EntryProvider[] entryProviders=null;

        if(document!=null)
        {

            Elements entries=document.select("ul#entry-item-list > li");
            String baslikid,basliktext="";
            baslikid=document.select("h1").attr("data-id");
            // Log.d("entryayiklama",baslikid);

            entryProviders=new EntryProvider[entries.size()];
            Elements h1ler=null;
            if(yazarmi){
                h1ler=document.select("h1#title");
            }
            else
                basliktext=document.select("h1").attr("data-title");




            int e=0;

            for (Element tumentry : entries) {

                String texts;
                SpannableString message;
                String yazar;
                String zaman;
                String favlar;
                String entryid;
                String yazarid;
                String ozelentry="";
                String ozelUrl="";

                boolean favmi;
                boolean yorumvarmi;
                String yorumsayisi;
                String entryler=tumentry.select("div.content").html();



                if(entryler.contains("<mark>"))
                {
                    // texts=entryler.replaceAll("<mark>","<font color='#C11212'> <b>");
                    texts=entryler.replaceAll("<mark>","<a class=\"b\" href=\"BOS\">");

                    //  texts=texts.replaceAll("</mark>","</b> </font>");
                    texts=texts.replaceAll("</mark>","</a>");
                }
                else
                {
                    texts=entryler;
                }


                //   message=new SpannableString(highlightSearchKey(texts,"yunus"));


                message=new SpannableString(texts);
                yazar=tumentry.attr("data-author");
                zaman=tumentry.select("a.entry-date").first().text();
                favlar=tumentry.attr("data-favorite-count");
                entryid=tumentry.attr("data-id");
                yazarid=tumentry.attr(" data-author-id");
                favmi = tumentry.attr("data-isfavorite").equals("true");
                yorumsayisi=tumentry.attr("data-comment-count");

                if(yorumsayisi.equals("0"))
                    yorumsayisi="";


                if(tumentry.attr("data-flags").contains("comment"))
                    yorumvarmi=true;
                else
                    yorumvarmi=false;


                if(yazarmi)
                {
                    baslikid=h1ler.get(e).attr("data-id");
                    ozelentry=h1ler.get(e).attr("data-title");
                    ozelUrl=h1ler.get(e).select("a").attr("href");
                }
                else
                    ozelentry=basliktext;



                entryProviders[e]=new EntryProvider(message,yazar,zaman,favlar,favmi,yazarid,entryid,false,baslikid,ozelentry,ozelUrl,0,yorumvarmi,yorumsayisi,"");

                e++;

            }



        }
        return entryProviders;
    }
    static  EntryProvider[] copEntryler(Document document, Context context,String cookie,String nick)
    {
        EntryProvider[] entryProviders=null;

        if(document!=null)
        {

            Elements entries=document.select("article");
            entryProviders=new EntryProvider[entries.size()];

            document.select("div.links").remove();
            int e=0;

            for (Element tumentry : entries) {

                String texts;
                SpannableString message;
                String zaman;
                String entryid;
                String ozelentry;
                String ozelUrl;
                String zaman2;




                 texts=tumentry.select("p.content").html();





                //   message=new SpannableString(highlightSearchKey(texts,"yunus"));




                Spanned x= Html.fromHtml(texts);
                message=new SpannableString(x.toString());
                Object[] spans=x.getSpans(0,x.length(), Object.class);
                for(Object span:spans)
                {
                    int start=x.getSpanStart(span);
                    int end=x.getSpanEnd(span);
                    int flags=x.getSpanFlags(span);
                    StringBuilder string=new StringBuilder();

                    for(int qop=start;qop<end;qop++)
                    {
                        string.append(message.charAt(qop));
                    }



                    //  Log.d("string",string);
                    if(span instanceof URLSpan)
                    {
                        URLSpan urlSpan= (URLSpan) span;


                        if (!urlSpan.getURL().startsWith("http")) {
                            //  span=new CallbackSpan(urlSpan.getURL());
                            // Log.d("EntryAyikla",urlSpan.getURL());
                            if(urlSpan.getURL().equals("BOS"))
                            {
                                span=new BackgroundColorSpan(Color.parseColor("#ffff9e"));
                            }else
                            {
                                span=new LinkBaglama(urlSpan.getURL(),string.toString(),context,cookie,nick);
                            }
                            //   Log.d("span",((URLSpan) span).getURL());
                        }
                        else
                        {
                            span=new LinkBaglama(urlSpan.getURL()," ",context,cookie,nick);
                        }

                    }

                    //if(!string.equals(arananKelime))
                    message.setSpan(span, start, end, flags);
                    //   else
                    //    message.setSpan(new BackgroundColorSpan(Color.parseColor("#ffff9e")),start,end,flags);

                }


                zaman2=tumentry.select("span.delete-info").text();
                zaman=tumentry.select("footer").text();
                ozelentry=tumentry.select("h2 > a").text();
                ozelUrl=tumentry.select("h2 > a").attr("href");
                entryid=ozelUrl.split("entry/")[1].trim();






                entryProviders[e]=new EntryProvider(message,zaman,entryid,ozelentry,ozelUrl,zaman2,2);

                e++;

            }



        }
        return entryProviders;
    }

    private Spannable highlightSearchKey(String title,String word) {
        Spannable  highlight;
        Pattern pattern;
        Matcher matcher;
        int        word_index;
        String     title_str;

        word_index = 1;
        title_str  = Html.fromHtml(title).toString();
        highlight  = (Spannable) Html.fromHtml(title);
        for (int index = 0; index < word_index; index++) {
            pattern = Pattern.compile("(?i)" + word);
            matcher = pattern.matcher(title_str);
            while (matcher.find()) {
                highlight.setSpan(
                        new BackgroundColorSpan(0x44444444), matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
        return highlight;
    }

}
