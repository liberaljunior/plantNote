package com.example.myhomegarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModifyActivity extends AppCompatActivity {
    TextView tv_profile_name04, tv_profile_date04;
    ImageView ivbtDiaryPhoto03, btn_modi_cal, modi_ivbtn_cancle, modi_ivbtn_save;
    EditText edtModiDate, edtModiCon;
    CheckBox check_water_modi;

    String pathpath, namepath;
    String namename, datedate, date, context, pt;
    int water;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        tv_profile_name04 = (TextView) findViewById(R.id.tv_profile_name04);
        tv_profile_date04 = (TextView) findViewById(R.id.tv_profile_date04);
        ivbtDiaryPhoto03 = (ImageView) findViewById(R.id.ivbtDiaryPhoto03);


        modi_ivbtn_save = (ImageView) findViewById(R.id.modi_ivbtn_save);
        edtModiDate = (EditText) findViewById(R.id.edtModiDate);
        edtModiCon = (EditText) findViewById(R.id.edtModiCon);
        check_water_modi = (CheckBox) findViewById(R.id.check_water_modi);

        Intent gIntent = getIntent();
        namepath = gIntent.getStringExtra("filePath06");
        pathpath = gIntent.getStringExtra("filePath05");


        DBhelper db = new DBhelper(ModifyActivity.this);
        SQLiteDatabase sq = db.getWritableDatabase();
         cursor = sq.rawQuery(" select * from diaryTBL where diaryPhotoPath = '" + pathpath + "';", null);
        if (pathpath != null) {
            cursor.moveToFirst();
            date = cursor.getString(0);
            context = cursor.getString(1);
            water = cursor.getInt(2);
            pt = cursor.getString(3);

            edtModiDate.setText(date);
            edtModiCon.setText(context);

            if (water == 1) {
                check_water_modi.setChecked(true);
            }
           //이미지 수정도 코딩해야해!!!
            cursor.close();

        }

        cursor = sq.rawQuery("SELECT * FROM plantinfoTBL WHERE plantivPath = '" + namepath + "';", null);
        if (namepath != null) {
            cursor.moveToFirst();
            namename = cursor.getString(0);
            datedate = cursor.getString(2);

            tv_profile_name04.setText(namename);
            tv_profile_date04.setText(datedate);


            cursor.close();


        }


        modi_ivbtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        cursor =sq.execSQL("update diaryTBL set diaryDate where '날짜'",null);*/
              /*  sqLiteDatabase.execSQL("CREATE TABLE diaryTBL (diaryDate TEXT , diaryContext TEXT , diaryCheckList TEXT, diaryPhotoPath TEXT);");*/
            }
        });
    }
}
