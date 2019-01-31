package com.example.fatih.eksiyeniduzen;

/**
 * Created by Fatih on 28.09.2017.
 */

class YazarEkProvider {

    String soltext;
    String sagtext;
    String solurl;
    String sagurl;
    int tip;

    YazarEkProvider(String soltext,String sagtext,String solurl,String sagurl,int tip)
    {
        this.soltext=soltext;
        this.sagtext=sagtext;
        this.solurl=solurl;
        this.sagurl=sagurl;
        this.tip=tip;
    }

}
