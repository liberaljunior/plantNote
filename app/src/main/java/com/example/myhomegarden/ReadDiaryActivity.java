package com.example.myhomegarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadDiaryActivity extends AppCompatActivity {
    TextView tvReadDate,tvContext;
    ImageView ivDiaryDate,btnModifyDiary;


    String pathMainPt,pathDiarypt, dName,dDate,dDate2, dContext;
    ArrayList<String> ivPhotoPath=new ArrayList<String>();

    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_diary);

        tvReadDate=(TextView)findViewById(R.id.tvReadDate);
        tvContext=(TextView)findViewById(R.id.tvContext);
        ivDiaryDate=(ImageView)findViewById(R.id.ivDiaryDate);
        btnModifyDiary=(ImageView)findViewById(R.id.btnModifyDiary);


        tvContext.setMovementMethod(new ScrollingMovementMethod());

        Intent fromProfileIntent = getIntent();
        pathMainPt  =fromProfileIntent.getStringExtra("filepath03");
        pathDiarypt=fromProfileIntent.getStringExtra("filePath04");

        Uri uri3 = Uri.fromFile(new File(pathDiarypt));
        Bitmap readivbmp = BitmapFactory.decodeFile(uri3.getPath());
        readivbmp = Bitmap.createScaledBitmap(readivbmp, 300, 300, false);

        if (readivbmp != null) {
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(pathMainPt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(readivbmp, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(readivbmp, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(readivbmp, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = readivbmp;
            }
           ivDiaryDate .setImageBitmap(rotatedBitmap);

        }


        final DBhelper helper = new DBhelper(ReadDiaryActivity.this);
        final SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM plantinfoTBL WHERE plantivPath = '" + pathMainPt + "';", null);
        if (pathMainPt  != null) {
            cursor.moveToFirst();
            dName = cursor.getString(0);
            dDate= cursor.getString(2);




            cursor.close();


        }

        cursor = sqLiteDatabase.rawQuery("select * from diaryTBL WHERE diaryPhotoPath = '" + pathDiarypt + "';", null);

        if (pathDiarypt!=null) {
            cursor.moveToFirst();
            dDate2= cursor.getString(0);

            try {
                dContext = cursor.getString(1);
                tvContext.setText(dContext);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),"내용이 없습니다",Toast.LENGTH_SHORT).show();
            }
            tvReadDate.setText(dDate2);

        }


        cursor.close();
        sqLiteDatabase.close();



        btnModifyDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toModifyIntent = new Intent(ReadDiaryActivity.this, ModifyActivity.class);
                toModifyIntent.putExtra("filePath06" ,pathMainPt);
                toModifyIntent.putExtra("filePath05",pathDiarypt);
                startActivity(toModifyIntent);
            }
        });



    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
