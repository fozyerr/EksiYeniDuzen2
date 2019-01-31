package com.example.fatih.eksiyeniduzen;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by Fatih on 16.09.2017.
 */

 class BaslikGetirmeAsynctask  {


  static  BaslikProvider[] baslikProviders(Document document,boolean bugun,boolean olay)
    {
        BaslikProvider[] dizi=null;

        if(document!=null)
        {


            if(!bugun)
            document.select("ul.topic-list").first().remove();

            Elements basliklar=document.select("ul.topic-list > li > a");

            dizi=new BaslikProvider[basliklar.size()];

            int diziboyutu=0;

            String entrysayi,basliktexti,basliklinki;
            boolean griyap=false;

            StringBuilder stringBuilder;

            for(Element baslik:basliklar)
            {
                stringBuilder=new StringBuilder();

                if(olay)
               griyap=baslik.parent().attr("class").isEmpty();

                if(baslik.select("small").isEmpty())
                {
                    if( baslik.select("div.detail").isEmpty())
                    entrysayi="1";
                    else
                        entrysayi= baslik.select("div.detail").text();
                }
                else
                entrysayi=baslik.select("small").text();

                if(entrysayi.length()>5)
                    stringBuilder.append(entrysayi.substring(0,5)).append("...");
                else
                stringBuilder.append(entrysayi);


                baslik.select("div.detail").remove();
                baslik.select("small").remove();
                basliktexti=baslik.text();
                basliklinki=baslik.attr("href");

                dizi[diziboyutu]=new BaslikProvider(stringBuilder.toString(),basliktexti,basliklinki,0,griyap);
                diziboyutu++;
            }


        }

        return dizi;
    }

}
