package com.example.fatih.eksiyeniduzen;

import java.io.Serializable;

/**
 * Created by Fatih on 03.11.2016.
 */
 class BaslikProvider implements Serializable {

     String entrysayi;
     String baslik;
     String url;
     int tip;
     int sayfano;
     boolean textigriyap;


     BaslikProvider(String entrysayi, String baslik, String url,int tip,boolean textigriyap) {
        super();
        this.entrysayi = entrysayi;
        this.baslik = baslik;
        this.url=url;
        this.tip=tip;
        this.textigriyap=textigriyap;

    }
    BaslikProvider(int tip,int sayfano)
    {
        this.tip=tip;
        this.sayfano=sayfano;
    }


}
