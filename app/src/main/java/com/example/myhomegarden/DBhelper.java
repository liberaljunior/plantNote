package com.example.myhomegarden;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;


    public DBhelper(Context context) {
        super(context, "plantInfo", null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL("CREATE TABLE plantinfoTBL (plantName TEXT , plantType TEXT  , plantmeetDate TEXT ,  plantivPath TEXT, plantHeight INT, waterCicle TEXT, fertilizeCicle TEXT, reppottingCicle TEXT );");
        sqLiteDatabase.execSQL("CREATE TABLE diaryTBL (diaryDate TEXT , diaryContext TEXT , diaryCheckList int , diaryPhotoPath TEXT  , plantName02 TEXT , lastFertilize int, lastReppotting int, plantHeight int );");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVirsion) {
        if (newVirsion==DATABASE_VERSION){
            sqLiteDatabase.execSQL("drop table plantinfoTBL");
            sqLiteDatabase.execSQL("drop table diaryTBL");

            onCreate(sqLiteDatabase);
        } //db를 새로 만들어야 하는가?


    }
}