package com.example.fatih.eksiyeniduzen;

import java.util.List;

/**
 * Created by Fatih on 15.01.2018.
 */

public interface BadiEngelFragHaberlesme {

    void listeyegerialmaislemi(String gerialmaurl,BadiEngelProvider silinenitem,int pos);

    void mesajlistkur(List<BadiEngelProvider> mesajlar);

    void requestverifykur(String requestverify);

    void gonderilenMesajiRecycleEkle(BadiEngelProvider gonderilenmesaj);

    void sorunsalKur(List<BadiEngelProvider> sorunsallar,boolean devam);

}
