package com.example.fatih.eksiyeniduzen;

import android.os.Bundle;

/**
 * Created by Fatih on 31.12.2017.
 */

public interface EntryFragmentHaberlesme {

    void stringgonder(String mesaj);

    void yonlendirmeYapildiMi(boolean yapildimikontrol);

    void asynctasktanfragmente(int yonlendirileceksayfa);

    void asynctasktanFragmenteToplamSayfaSayisi(int toplamsayfasayisi);

    void duzenlenmisUrlyiGonder(String duzenlenmisUrl);

    void updateView(boolean videoVarmi,String videoUrl);

    void seritlayoutGorunurlugu(int toplamsayfa);

    void basligiKur(String baslikString);

    void adaptoreEklemeIslemi(EntryProvider[] entryler,int diziboyutu);

    void progressAc();
    void progressKapa();

    void bundlegonder(Bundle bundle);

    void takipedildimi(boolean takipediliyomu);

}
