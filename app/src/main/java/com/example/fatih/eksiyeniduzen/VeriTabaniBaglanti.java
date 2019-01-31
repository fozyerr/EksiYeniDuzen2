package com.example.fatih.eksiyeniduzen;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.SpannableString;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 10.12.2017.
 */

public class VeriTabaniBaglanti extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    // Database adı
    private static final String DATABASE_NAME = "arsiv";

    // Başlık tablosu
    private static final String Tablo_Baslik = "basliklar";

    // Entry Tablosu
    private static final String Tablo_Entry="entryler";


    // İki tablodaki ortak alanlar
    private static final String KEY_ID = "id";
    private static final String baslikId="baslikid";

    // Başlık sütunları
    private static final String baslikAdi = "baslikad";
    private static final String baslikEntrySayi = "entrysayi";



    // Entry tablosu sütunları
    private static final  String Entry_KEY="keyid";
    private static final  String entrydokumani="doc";
   //private static final  String sayfanumarasi="sayfano";
    private static final  String entry ="entry";
    private static final  String yazar="yazar";
    private static final  String zaman="zaman";
    private static final  String favlar="favlar";
    private static final  String entryid="entryid";
    private static final  String yazarid="yazarid";
    private static final  String favmi="favmi";
    private static final  String yorumvarmi="yorumvarmi";
    private static final  String yorumsayisi="yorumsayisi";
    private static final  String basliktext="basliktext";


    SQLiteDatabase db;



    //Başlık tablosu oluşturma query
  private static final String baslikTabloOlustur = "CREATE TABLE " + Tablo_Baslik + "("
            + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + baslikAdi + " TEXT,"
            + baslikEntrySayi + " TEXT,"+baslikId+" TEXT" + ")";

   /* private static final String entryTabloOlusutur=  "CREATE TABLE " + Tablo_Entry + "("
            + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"+sayfanumarasi+" TEXT,"+ entrydokumani + " TEXT,"+baslikId+" TEXT" + ")";*/

    public static final String entryTabloOlusutur =
            "CREATE TABLE " + Tablo_Entry +
                    " (" + Entry_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    entry+" TEXT,"+
                    yazar+" TEXT,"+
                    zaman+" TEXT," +
                    favlar+" TEXT," +
                    favmi+" INTEGER," +
                    yazarid+" TEXT," +
                    entryid+" TEXT," +
                    baslikId+" TEXT," +
                    basliktext+" TEXT,"+
                    yorumvarmi+" INTEGER," +
                    yorumsayisi + " TEXT);";


    /*
    *
    *
    * SpannableString entry, String yazar, String zaman, String favlar, boolean favmi,
                   String yazarid, String entryid,boolean artimi,String baslikid,String ozelentrytext,
                   String ozelentryUrl,int tip,boolean yorumvarmi,String yorumsayisi)
    *
    *
    * */





    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

       db.enableWriteAheadLogging();

    }

    public VeriTabaniBaglanti(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


     //   db.enableWriteAheadLogging();

        db.execSQL(baslikTabloOlustur);
        db.execSQL(entryTabloOlusutur);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablo_Baslik);
        db.execSQL("DROP TABLE IF EXISTS " + Tablo_Entry);
        // Create tables again
        onCreate(db);

    }

    public void transactionBaslat()
    {
        db.beginTransactionNonExclusive();
      // db.beginTransaction();
    }
    public void transactionBitir()
    {
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void okumakicinac()
    {
        db=this.getReadableDatabase();
   //     db.enableWriteAheadLogging();
    }

    public void veritabaniAc()
    {
        db=this.getWritableDatabase();
    //    db.enableWriteAheadLogging();
    }
    public void veritabaniKapat()
    {
        db.close();
    }

    public void baslikekle(BaslikProvider baslik) {

        ContentValues values = new ContentValues();
        values.put(baslikAdi,baslik.baslik);
        values.put(baslikEntrySayi,baslik.entrysayi);
        values.put(baslikId,baslik.url);

        db.insert(Tablo_Baslik, null, values);

    }



    public void baslikguncelle(String guncellenecekbaslik,int kacentry) {

        String eskientrysayi=baslikgetir(guncellenecekbaslik);

       // Log.d("eski değer",eskientrysayi);

        int arttir= Integer.parseInt(eskientrysayi)+kacentry;

       // Log.d("yeni deger",String.valueOf(arttir));


        ContentValues values = new ContentValues();
        values.put(baslikEntrySayi,String.valueOf(arttir));


        db.update(Tablo_Baslik, values, baslikId + "= ?", new String[] {guncellenecekbaslik});

    }

    private String baslikgetir(String tekbaslik) {

        String selectQuery = "SELECT "+baslikEntrySayi +" FROM " + Tablo_Baslik+" WHERE "+baslikId+" = "+tekbaslik;


        Cursor cursor = db.rawQuery(selectQuery, null);

     //   Log.d("eski deger",cursor.getColumnName(0)+" "+cursor.getCount()+" "+cursor.getColumnIndex(baslikEntrySayi));

        cursor.moveToFirst();

        String eskientrysayisi=cursor.getString(0);
        cursor.close();

        return eskientrysayisi;

    }

    public void entryEkle(EntryProvider arsivEntry)
    {

        ContentValues values = new ContentValues();
        values.put(entry,arsivEntry.entry.toString());
        values.put(yazar,arsivEntry.yazar);
        values.put(zaman,arsivEntry.zaman);
        values.put(favlar,arsivEntry.favlar);

        if(arsivEntry.favmi)
        values.put(favmi,1);
        else
        values.put(favmi,0);

        values.put(yazarid,arsivEntry.yazarid);
        values.put(entryid,arsivEntry.entryid);
        values.put(baslikId,arsivEntry.baslikid);
        values.put(basliktext,arsivEntry.ozelentrytext);

        if(arsivEntry.yorumvarmi)
            values.put(yorumvarmi,1);
        else
        values.put(yorumvarmi,0);

        values.put(yorumsayisi,arsivEntry.yorumsayisi);

        db.insert(Tablo_Entry, null, values);
    }

    @SuppressLint("LogConditional")
    public void kayitSil(String silinecekbaslikID)
    {

        String entrysilmequey="DELETE FROM "+Tablo_Entry+" WHERE "+baslikId+" = "+silinecekbaslikID;
        String basliksilmequery="DELETE FROM "+Tablo_Baslik+" WHERE "+baslikId+" = "+silinecekbaslikID;


     int silinenetryler=db.delete(Tablo_Entry,baslikId+" = ?",new String[]{silinecekbaslikID});
      int silinenbaslik=  db.delete(Tablo_Baslik,baslikId+" = ?",new String[]{silinecekbaslikID});
       // db.execSQL(entrysilmequey);
     //   db.execSQL(basliksilmequery);

        Log.d("silinen entry",String.valueOf(silinenetryler));
        Log.d("silinen başlık",String.valueOf(silinenbaslik));


    }

    @SuppressLint("LogConditional")
    public void entrySil(String silinecekentryID)
    {

        String entrysilmequey="DELETE FROM "+Tablo_Entry+" WHERE "+entryid+" = "+silinecekentryID;



        int silinenetryler=db.delete(Tablo_Entry,entryid+" = ?",new String[]{silinecekentryID});


        Log.d("bir entry silindi", String.valueOf(silinenetryler));

    }



    public boolean veritabaniAcikMi()
    {

        if(db!=null)
            return true;
        else
            return false;

    }

    public ArrayList<EntryProvider> entrySayfagetir(String baslikidsi)
    {


        String selectQuery = "SELECT  * FROM " + Tablo_Entry +" "+"WHERE baslikid = "+baslikidsi  ;

        Cursor cursor =db.rawQuery(selectQuery,null);
             /*   db.query(Tablo_Entry, new String[] { sayfanumarasi, entrydokumani, baslikId }, sayfanumarasi + "=?",
                        new String[] { sayfanumara }, null, null, null, null);*/

        //Log.d("kaç tane sütun var", String.valueOf(cursor.getColumnCount()));


        ArrayList<EntryProvider> arsivEntry=new ArrayList<>();



        if (cursor.moveToFirst()) {
            do {
                String entrya=cursor.getString(1);
                String yazara=cursor.getString(2);
                String zamana=cursor.getString(3);
                String favlara=cursor.getString(4);
                boolean favmia;
                if(cursor.getInt(5)==1)
                    favmia=true;
                else
                    favmia=false;

                String yazarida=cursor.getString(6);
                String entryida=cursor.getString(7);
                String baslikida=cursor.getString(8);
                String basliktexta=cursor.getString(9);
                boolean yorumvarmia;
                if(cursor.getInt(10)==1)
                    yorumvarmia=true;
                else
                    yorumvarmia=false;

                String yorumsayisia=cursor.getString(11);




              EntryProvider gelenentry = new EntryProvider(new SpannableString(entrya),yazara,zamana,favlara,favmia,yazarida,entryida,false,
                      baslikida,basliktexta,"",0,yorumvarmia,yorumsayisia,"");
                arsivEntry.add(gelenentry);


            } while (cursor.moveToNext());
        }

        cursor.close();


        return arsivEntry;
    }

    public boolean baslikVarMi(String arananBaslik)
    {

        Log.d("gelen",arananBaslik);

        String aramaquery="SELECT "+baslikId+" FROM "+Tablo_Baslik+" WHERE "+baslikId+" = "+arananBaslik;

        Cursor cursor=db.rawQuery(aramaquery,null);


        if(cursor.getCount()==0)
        {
            cursor.close();
            return false;
        }
        else
        {
            cursor.close();
            return true;
        }

    }
    public boolean entryVarMi(String arananentryid)
    {

        // Sadece entryid ile de sorgu yapılabilir, bir ara performans kıyaslaması yap


        String aramaquery="SELECT "+baslikId+" FROM "+Tablo_Entry+" WHERE "+entryid+" = "+arananentryid;

        Cursor cursor=db.rawQuery(aramaquery,null);

        //Log.d("entry arama", String.valueOf(cursor.getCount()));

        if(cursor.getCount()==0)
        {
            cursor.close();
            return false;
        }
        else
        {
            cursor.close();
            return true;
        }

    }

    public void hicentryvarmi()
    {
        String selectQuery = "SELECT  * FROM " + Tablo_Entry;


        Cursor cursor = db.rawQuery(selectQuery, null);

     //   Log.d("curss", String.valueOf(cursor.getCount()));
        cursor.close();
    }


    public ArrayList<BaslikProvider> tumbasliklar() {
        ArrayList<BaslikProvider> basliklar = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Tablo_Baslik;


        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


             //   int kayitlientrysayisi=entrylerisay(cursor.getString(3));


                BaslikProvider baslikProvider = new BaslikProvider(cursor.getString(2),cursor.getString(1),cursor.getString(3),0,false);
                // Adding contact to list
                basliklar.add(baslikProvider);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list
        return basliklar;
    }

    public int entrylerisay(String entryleriSayilacakBaslik) {
        String selectQuery = "SELECT  * FROM " + Tablo_Entry +" WHERE baslikid = "+entryleriSayilacakBaslik  ;

        Cursor cursor =db.rawQuery(selectQuery,null);

        int sayi=cursor.getCount();

        cursor.close();

        return sayi;


    }


}
