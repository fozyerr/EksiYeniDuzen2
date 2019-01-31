package com.example.fatih.eksiyeniduzen;

/**
 * Created by Fatih on 29.12.2018.
 */

public interface BaslikFragHaberlesme {

    void sayfaDevamAyracEkle(int sayfa);

    void adaptorBosalt();

    void basliklariKur(BaslikProvider[] baslikProvider);

    void listviewBasaKaydir(int pagerPozisyon,boolean butondangelme);

    void bugunDevamEkle();

    void progressBarGosterVeyaGizle(boolean gosterilecekmi);

    void reloadGosterVeyaGizle(boolean reloadGoster);

}
