package com.example.fatih.eksiyeniduzen;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Fatih on 29.12.2018.
 */

public class LeakTespiti extends Application {

  /*  private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        LeakTespiti application = (LeakTespiti) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }*/
}
