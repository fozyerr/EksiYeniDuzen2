package com.example.fatih.eksiyeniduzen;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;

/**
 * Created by Fatih on 20.12.2016.
 */
 class BadiEngelProvider implements Parcelable {
    String yazarid;
    String yazarnick;
    String link;
    int tip;

    String mesajAtilacakYazar;

    SpannableString entry;
    String entryzaman;
    String yorumid;
    String artioysayisi;
    String eksioysayisi;
    boolean artimi,eksimi;



     BadiEngelProvider(String yazarid, String yazarnick, String link, int tip)  //Badiengel yapısı
     {
        this.link = link;
        this.yazarid = yazarid;
        this.yazarnick=yazarnick;
        this.tip=tip;
     }
    BadiEngelProvider(String yazarid, String yazarnick, String link,String entryzaman, int tip)  //Sorunsal ayraç yapısı
    {
        this.link = link;
        this.yazarid = yazarid;
        this.yazarnick=yazarnick;
        this.entryzaman=entryzaman;
        this.tip=tip;
    }

    BadiEngelProvider(SpannableString entry,String yazarnick,String yazarid,String entryzaman,String yorumid,String link,String artioysayisi,String eksioysayisi,int tip,
                        boolean artimi,boolean eksimi) // Yorum yapısı
    {
        this.entry=entry;
        this.yazarnick=yazarnick;
        this.yazarid=yazarid;
        this.entryzaman=entryzaman;
        this.yorumid=yorumid;
        this.link=link;
        this.artioysayisi=artioysayisi;
        this.eksioysayisi=eksioysayisi;
        this.tip=tip;
        this.artimi=artimi;
        this.eksimi=eksimi;

    }

    BadiEngelProvider(String mesajatan,String mesajicerik,String mesajtarih,String silarsivleUrl,String mesajurl,boolean arsivmi,String requestverify,String mesajAtilacakYazar,int tip) // Mesaj yapısı
    {
        this.yazarnick=mesajatan;
        this.yazarid=mesajicerik;
        this.entryzaman=mesajtarih;
        this.yorumid=silarsivleUrl;
        this.link=mesajurl;
        this.artimi=arsivmi;
        this.artioysayisi=requestverify;
        this.mesajAtilacakYazar=mesajAtilacakYazar;
        this.tip=tip;

    }

    BadiEngelProvider(SpannableString mesaj,String mesajzaman,boolean artimi,int tip) // Mesajlasma yapısı
    {
        this.entry=mesaj;
        this.artimi=artimi;
        this.entryzaman=mesajzaman;
        this.tip=tip;

    }

    BadiEngelProvider(SpannableString sorunsalText,String sorunsalBaslik,String sorunsalOySayisi,String sorunsalYanit,String yazarnick,String entryzaman,String sorunsalURL,
                      String yazarID,String sorunsalID,boolean artimi,boolean eksimi,int tip) //Sorunsal Yapısı
    {
        this.entry=sorunsalText;
        this.yazarnick=yazarnick;
        this.entryzaman=entryzaman;
        this.link=sorunsalURL;
        this.yorumid=sorunsalID;
        this.mesajAtilacakYazar=sorunsalBaslik;
        this.yazarid=yazarID;
        this.artioysayisi=sorunsalOySayisi;
        this.eksioysayisi=sorunsalYanit;
        this.artimi=artimi;
        this.eksimi=eksimi;
        this.tip=tip;


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.yazarid);
        dest.writeString(this.yazarnick);
        dest.writeString(this.link);
        dest.writeInt(this.tip);
        dest.writeString(this.mesajAtilacakYazar);
        TextUtils.writeToParcel(this.entry, dest, flags);
        dest.writeString(this.entryzaman);
        dest.writeString(this.yorumid);
        dest.writeString(this.artioysayisi);
        dest.writeString(this.eksioysayisi);
        dest.writeByte(this.artimi ? (byte) 1 : (byte) 0);
        dest.writeByte(this.eksimi ? (byte) 1 : (byte) 0);
    }

    protected BadiEngelProvider(Parcel in) {
        this.yazarid = in.readString();
        this.yazarnick = in.readString();
        this.link = in.readString();
        this.tip = in.readInt();
        this.mesajAtilacakYazar = in.readString();
        this.entry =  (SpannableString) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);;
        this.entryzaman = in.readString();
        this.yorumid = in.readString();
        this.artioysayisi = in.readString();
        this.eksioysayisi = in.readString();
        this.artimi = in.readByte() != 0;
        this.eksimi = in.readByte() != 0;
    }

    public static final Parcelable.Creator<BadiEngelProvider> CREATOR = new Parcelable.Creator<BadiEngelProvider>() {
        @Override
        public BadiEngelProvider createFromParcel(Parcel source) {
            return new BadiEngelProvider(source);
        }

        @Override
        public BadiEngelProvider[] newArray(int size) {
            return new BadiEngelProvider[size];
        }
    };
}
