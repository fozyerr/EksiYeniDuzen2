package com.example.fatih.eksiyeniduzen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Fatih on 07.12.2017.
 */

public class BroadcastYakalama extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,intent.getExtras().getString("mesaj"),Toast.LENGTH_SHORT).show();


    }
}
