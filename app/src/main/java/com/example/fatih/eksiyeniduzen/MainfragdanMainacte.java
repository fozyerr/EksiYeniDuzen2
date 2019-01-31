package com.example.fatih.eksiyeniduzen;

import android.widget.ArrayAdapter;

/**
 * Created by Fatih on 30.12.2018.
 */

public interface MainfragdanMainacte {

    void gorunurTiklanabilir();

    void viewGone();

    void spinnerselection(int pos);

    void spinnerAdaptoru(ArrayAdapter<String> xmlarray);

    void spinnerListener();

    void entryYazmaButonundanActivitye(int position);

    void kenaraAtildiMi(boolean kenaraAtildi);

}
