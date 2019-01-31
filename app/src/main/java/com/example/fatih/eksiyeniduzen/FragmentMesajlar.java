package com.example.fatih.eksiyeniduzen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 25.02.2018.
 */

public class FragmentMesajlar extends Fragment {

    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static FragmentMesajlar newInstance(String cookie,String nick) {
        FragmentMesajlar fragment = new FragmentMesajlar();
        Bundle args = new Bundle();
        args.putString("cookie",cookie);
        args.putString("nick",nick);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mesajlar, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),getArguments().getString("cookie"),getArguments().getString("nick"));
        mViewPager = rootView.findViewById(R.id.mesajtablar);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        String cookie;
        String nick;

        SectionsPagerAdapter(FragmentManager fm,String cookie,String nick) {
            super(fm);
            this.cookie=cookie;
            this.nick=nick;
        }

        @Override
        public Fragment getItem(int position) {

           return FragmentMesajTablar.newInstance(cookie,nick,position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                        return "gelen kutusu";
                default:
                        return "arşiv";
            }
        }
    }

}
