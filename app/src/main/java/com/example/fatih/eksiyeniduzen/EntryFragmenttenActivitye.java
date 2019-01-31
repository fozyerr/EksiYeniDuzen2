package com.example.fatih.eksiyeniduzen;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Fatih on 01.01.2018.
 */

public interface EntryFragmenttenActivitye {

    void sayfayayonlendir(int yonlendirileceksayfa);

    void viewpagerToplamKacSayfa(int sayfasayisi);

    void sectionAdaptorUrlKur(String sectionurl);

    void seritlayoutGorunurlugu(int toplamsayfa);

    void basligiKur(String baslikString);

    void basliktiklamaBundleGonder(Bundle bundle);

    void takipedildimi(boolean takipediliyomu);

    void arsivbasliktiklamadinleyici();

    void arsiventryleriKur(ArrayList<EntryProvider> entryProviderArrayList);

    void arsivicikurulum();

}
