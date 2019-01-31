package com.example.fatih.eksiyeniduzen;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;

/**
 * Created by Fatih on 17.09.2017.
 */

 class EntryProvider implements Parcelable {


     SpannableString entry;
     String yazar;
     String zaman;
     String favlar;
     String entryid;
     String yazarid;
     String baslikid;
     String zaman2;
     boolean favmi;
     boolean artimi,eksimi;
     String ozelentrytext;
     String ozelentryUrl;
     boolean yorumvarmi;
     String yorumsayisi;
     String mesajVerify;
     int tip;
     int sayfano;

     EntryProvider(SpannableString entry, String yazar, String zaman, String favlar, boolean favmi,
                   String yazarid, String entryid,boolean artimi,String baslikid,String ozelentrytext,
                   String ozelentryUrl,int tip,boolean yorumvarmi,String yorumsayisi,String mesajVerify) {
        super();
        this.entry = entry;
        this.yazar = yazar;
        this.zaman = zaman;
        this.yazarid = yazarid;
        this.favmi = favmi;
        this.favlar = favlar;
        this.entryid = entryid;
        this.artimi=artimi;
        this.eksimi=artimi;
        this.baslikid=baslikid;
        this.ozelentrytext=ozelentrytext;
        this.ozelentryUrl=ozelentryUrl;
        this.yorumvarmi=yorumvarmi;
        this.yorumsayisi=yorumsayisi;
        this.mesajVerify=mesajVerify;
        this.tip=tip;

    }
    EntryProvider(SpannableString entry, String zaman, String entryid, String ozelentrytext, String ozelentryUrl,String zaman2,int tip)
    {
        this.entry = entry;
        this.zaman = zaman;
        this.entryid = entryid;
        this.ozelentrytext=ozelentrytext;
        this.ozelentryUrl=ozelentryUrl;
        this.tip=tip;
        this.zaman2=zaman2;
    }
    EntryProvider(int sayfano,int tip)
    {
        this.sayfano=sayfano;
        this.tip=tip;
    }

    EntryProvider(int tip)
    {
        this.tip=tip;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        TextUtils.writeToParcel(this.entry, dest, flags);
        dest.writeString(this.yazar);
        dest.writeString(this.zaman);
        dest.writeString(this.favlar);
        dest.writeString(this.entryid);
        dest.writeString(this.yazarid);
        dest.writeString(this.baslikid);
        dest.writeString(this.zaman2);
        dest.writeByte(this.favmi ? (byte) 1 : (byte) 0);
        dest.writeByte(this.artimi ? (byte) 1 : (byte) 0);
        dest.writeByte(this.eksimi ? (byte) 1 : (byte) 0);
        dest.writeString(this.ozelentrytext);
        dest.writeString(this.ozelentryUrl);
        dest.writeByte(this.yorumvarmi ? (byte) 1 : (byte) 0);
        dest.writeString(this.yorumsayisi);
        dest.writeInt(this.tip);
        dest.writeInt(this.sayfano);
        dest.writeString(this.mesajVerify);
    }

    protected EntryProvider(Parcel in) {
        this.entry =  (SpannableString) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.yazar = in.readString();
        this.zaman = in.readString();
        this.favlar = in.readString();
        this.entryid = in.readString();
        this.yazarid = in.readString();
        this.baslikid = in.readString();
        this.zaman2 = in.readString();
        this.favmi = in.readByte() != 0;
        this.artimi = in.readByte() != 0;
        this.eksimi = in.readByte() != 0;
        this.ozelentrytext = in.readString();
        this.ozelentryUrl = in.readString();
        this.yorumvarmi = in.readByte() != 0;
        this.yorumsayisi = in.readString();
        this.tip = in.readInt();
        this.sayfano=in.readInt();
        this.mesajVerify=in.readString();
    }

    public static final Creator<EntryProvider> CREATOR = new Creator<EntryProvider>() {
        @Override
        public EntryProvider createFromParcel(Parcel source) {
            return new EntryProvider(source);
        }

        @Override
        public EntryProvider[] newArray(int size) {
            return new EntryProvider[size];
        }
    };
}
