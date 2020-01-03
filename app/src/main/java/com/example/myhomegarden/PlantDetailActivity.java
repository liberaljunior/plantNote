package com.example.myhomegarden;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlantDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_profile_name, tv_profile_date;
    String photoPath;
    String pName, pDate, pType, pWater, pFil, pRepot;
    ImageView goSearch, alertadd , homeBtn;


    public static Context mContext;
    FloatingActionButton fat_plus, fat_add_diary, fat_see_cal, fat_set_ring;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;


    GridView grid_profile;
    ArrayList<String> diaryPhotoPath = new ArrayList<String>();
    ArrayList<String> nameCunnected = new ArrayList<String>();


    String strDrPt, namename;

    Boolean alert = false;
    DiaryAdapter adapter;
    DBhelper helper;
    final static int REQUEST_CODE_SUCCESS = 2002;

    View careView;
    ArrayList<String> lastDate = new ArrayList<>();
    ArrayList<Integer> chA= new ArrayList<>();
    ArrayList<Integer> chB= new ArrayList<>();
    ArrayList<Integer> chC= new ArrayList<>();
    String a, b, c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        tv_profile_name = (TextView) findViewById(R.id.tv_profile_name);
        tv_profile_date = (TextView) findViewById(R.id.tv_profile_date);
        fat_plus = (FloatingActionButton) findViewById(R.id.fat_plus);
        fat_add_diary = (FloatingActionButton) findViewById(R.id.fat_add_diary);
        fat_see_cal = (FloatingActionButton) findViewById(R.id.fat_see_cal);
        fat_set_ring = (FloatingActionButton) findViewById(R.id.fat_set_ring);
        grid_profile = (GridView) findViewById(R.id.grid_profile);
        goSearch = (ImageView) findViewById(R.id.goSerch);
        alertadd = (ImageView) findViewById(R.id.alertadd);
        homeBtn=(ImageView)findViewById(R.id.homeBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gohomeIntent=new Intent(PlantDetailActivity.this,MainActivity.class);
                startActivity(gohomeIntent);
            }
        });

        mContext = getApplicationContext();

        Intent fromMainGetIntent = getIntent();
        photoPath = fromMainGetIntent.getStringExtra("filepath");
        namename = fromMainGetIntent.getStringExtra("plantname"); // 선택된 식물의 이름


        if (alert == false) {
            alertadd.setImageResource(R.drawable.ic_background);


        }


        helper = new DBhelper(PlantDetailActivity.this);
        final SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM plantinfoTBL WHERE plantivPath = '" + photoPath + "';", null);

        if (photoPath != null) {
            cursor.moveToFirst();
            pName = cursor.getString(0);
            pType = cursor.getString(1);
            pDate = cursor.getString(2);
            pWater = cursor.getString(5);
            pFil = cursor.getString(6);
            pRepot = cursor.getString(7);

            tv_profile_name.setText(pName);
            tv_profile_date.setText(pDate);


            cursor.close();


        }

        Cursor cursor1 = sqLiteDatabase.rawQuery("select * from diaryTBL where plantName02 = '" + pName + "';", null);
        if (pName != null) {
            while (cursor1.moveToNext()) {
                lastDate.add(cursor1.getString(0));
                chA.add(cursor1.getInt(2));
                diaryPhotoPath.add(cursor1.getString(3));
                nameCunnected.add(cursor1.getString(4));
                chB.add(cursor1.getInt(5));
                chC.add(cursor1.getInt(6));

                for (int z = 0; z < chA.size(); z++) {
                    if (chA.get(z) == 1) {
                        a = lastDate.get(z);

                    }

                }

                for (int zz=0; zz<chB.size(); zz++) {
                    if (chB.get(zz) == 1) {
                        b = lastDate.get(zz);
                    }
                }

                for (int zzz=0; zzz<chC.size(); zzz++) {
                    if (chC.get(zzz) == 1) {
                        c = lastDate.get(zzz);

                    }
                }
            }
        }



        sqLiteDatabase.close();
        cursor1.close();


        adapter = new DiaryAdapter(this);
        grid_profile.setAdapter(adapter);


        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);


        fat_plus.setOnClickListener(this);
        fat_add_diary.setOnClickListener(this);
        fat_see_cal.setOnClickListener(this);
        fat_set_ring.setOnClickListener(this);


        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(PlantDetailActivity.this);
                dlg.setTitle(pName + "의 케어 주기");
                dlg.setIcon(R.drawable.ic_leaf);


                careView = (View) view.inflate(PlantDetailActivity.this, R.layout.dialogue, null);
                dlg.setView(careView);

                TextView tv_dlg_water = (TextView) careView.findViewById(R.id.tv_dlg_water);
                TextView tv_dlg_lastW = (TextView) careView.findViewById(R.id.tv_dlg_lastW);
                TextView tv_dlg_firtilize = (TextView) careView.findViewById(R.id.tv_dlg_firtilize);
                TextView tv_dlg_last_fil = (TextView) careView.findViewById(R.id.tv_dlg_last_fil);
                TextView tv_dlg_reppot = (TextView) careView.findViewById(R.id.tv_dlg_reppot);
                TextView tv_dlg_last_rp = (TextView) careView.findViewById(R.id.tv_dlg_last_rp);

                tv_dlg_water.setText(pWater);
                tv_dlg_firtilize.setText(pFil);
                tv_dlg_reppot.setText(pRepot);

                if (a != null) {
                    tv_dlg_lastW.setText(a);
                }
                if (b != null) {
                    tv_dlg_last_fil.setText(b);
                }
                if (c != null) {
                    tv_dlg_last_rp.setText(c);
                }


                dlg.show();
            }
        });

        grid_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strDrPt = diaryPhotoPath.get(i);


                Intent toReadDiaryIntent = new Intent(PlantDetailActivity.this, ReadDiaryActivity.class);
                toReadDiaryIntent.putExtra("filepath03", photoPath);
                toReadDiaryIntent.putExtra("filePath04", strDrPt);
                startActivity(toReadDiaryIntent);


            }
        });


        grid_profile.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pathProfile = diaryPhotoPath.get(i);
                diaryPhotoPath.remove(i);
                adapter.notifyDataSetChanged();

                SQLiteDatabase sql = helper.getWritableDatabase();
                sql.execSQL("delete FROM diaryTBL where   diaryPhotoPath='" + pathProfile + "';");

                Toast.makeText(getApplicationContext(), "식물정보가 삭제되었습니다", Toast.LENGTH_LONG).show();


                sql.close();

                alert = false;


                return false;
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fat_plus:
                toggleFab();
                break;

            case R.id.fat_add_diary:
                toggleFab();
                Intent toWriteDiaryIntent = new Intent(PlantDetailActivity.this, WriteDiaryActivity.class);
                toWriteDiaryIntent.putExtra("filepath02", photoPath);
                toWriteDiaryIntent.putExtra("plantname02", pName);
                /*   startActivity(toWriteDiaryIntent);*/
                startActivityForResult(toWriteDiaryIntent, REQUEST_CODE_SUCCESS);


                break;

            case R.id.fat_see_cal:
                toggleFab();
                Intent toCalanderIntent = new Intent(PlantDetailActivity.this, CalCheckItemActivity.class);
                toCalanderIntent.putExtra("plantname02", pName);
                startActivity(toCalanderIntent);

                break;
            case R.id.fat_set_ring:
                toggleFab();
                Intent toAlarmIntent = new Intent(PlantDetailActivity.this, GrowthReportActivity.class);
                toAlarmIntent.putExtra("plantname02", pName);
                startActivity(toAlarmIntent);
                break;

        }
    }

    private void toggleFab() {

        if (isFabOpen) {
            fat_plus.setImageResource(R.drawable.ic_add_black_24dp);
            fat_add_diary.startAnimation(fab_close);
            fat_see_cal.startAnimation(fab_close);
            fat_set_ring.startAnimation(fab_close);
            fat_add_diary.setClickable(false);
            fat_see_cal.setClickable(false);
            fat_set_ring.setClickable(false);
            isFabOpen = false;

        } else {
            fat_plus.setImageResource(R.drawable.ic_clear_black_24dp);
            fat_add_diary.startAnimation(fab_open);
            fat_see_cal.startAnimation(fab_open);
            fat_set_ring.startAnimation(fab_open);
            fat_add_diary.setClickable(true);
            fat_see_cal.setClickable(true);
            fat_set_ring.setClickable(true);
            isFabOpen = true;

        }

    }


    public class DiaryAdapter extends BaseAdapter {
        Context ivcontext;


        public DiaryAdapter(Context ivcontext) {
            this.ivcontext = ivcontext;

        }

        @Override
        public int getCount() {
            return diaryPhotoPath.size();
        }

        @Override
        public Object getItem(int i) {
            return diaryPhotoPath.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView iv;


            if (view == null) {
                // if it's not recycled, initialize some attributes
                iv = new ImageView(ivcontext);
                iv.setLayoutParams(new GridView.LayoutParams(200, 200));
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setPadding(8, 8, 8, 8);
            } else {
                iv = (ImageView) view;
            }
            if (diaryPhotoPath != null) {

                String picturePath = diaryPhotoPath.get(i);
                android.util.Log.d("pathcheck2", picturePath);
                Uri uri2 = Uri.fromFile(new File(picturePath));
                Bitmap diarybmp = BitmapFactory.decodeFile(uri2.getPath());
                diarybmp = Bitmap.createScaledBitmap(diarybmp, 150, 170, false);

                if (diarybmp != null) {
                    ExifInterface ei = null;
                    try {
                        ei = new ExifInterface(picturePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(diarybmp, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(diarybmp, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(diarybmp, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = diarybmp;
                    }


                    if (pName.equals(nameCunnected.get(i))) {
                        iv.setImageBitmap(rotatedBitmap);
                    } else {

                    }
                    alert = true;
                    alertadd.setVisibility(View.INVISIBLE);


                }
            }

            return iv;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SUCCESS) {
            if (resultCode == RESULT_OK) {
                if (photoPath != null) {
                    photoPath = data.getStringExtra("path");
                    namename = data.getStringExtra("name");
                    helper = new DBhelper(PlantDetailActivity.this);

                    final SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
                    final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM plantinfoTBL WHERE plantivPath = '" + photoPath + "';", null);

                    if (photoPath != null) {
                        cursor.moveToFirst();
                        pName = cursor.getString(0);
                        pType = cursor.getString(1);
                        pDate = cursor.getString(2);


                        tv_profile_name.setText(pName);
                        tv_profile_date.setText(pDate);


                        cursor.close();


                    }
                    diaryPhotoPath.clear(); //★★★★★ 중복으로 리스트생성을 방지하기 위한 메소드!!!

                    Cursor cursor1 = sqLiteDatabase.rawQuery("select * from diaryTBL where plantName02 = '" + pName + "';", null);
                    if (pName != null) {
                        while (cursor1.moveToNext()) {
                            lastDate.add(cursor1.getString(0));
                            chA.add(cursor1.getInt(2));
                            diaryPhotoPath.add(cursor1.getString(3));
                            nameCunnected.add(cursor1.getString(4));
                            chB.add(cursor1.getInt(5));
                            chC.add(cursor1.getInt(6));

                            for (int z = 0; z < chA.size(); z++) {
                                if (chA.get(z) == 1) {
                                    a = lastDate.get(z);

                                }

                            }

                            for (int zz=0; zz<chB.size(); zz++) {
                                if (chB.get(zz) == 1) {
                                    b = lastDate.get(zz);
                                }
                            }

                            for (int zzz=0; zzz<chC.size(); zzz++) {
                                if (chC.get(zzz) == 1) {
                                    c = lastDate.get(zzz);

                                }
                            }
                        }
                    }
                    sqLiteDatabase.close();
                    cursor1.close();


                    adapter = new DiaryAdapter(this);
                    grid_profile.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                    fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
                    fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);


                    fat_plus.setOnClickListener(this);
                    fat_add_diary.setOnClickListener(this);
                    fat_see_cal.setOnClickListener(this);
                    fat_set_ring.setOnClickListener(this);

                    goSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(PlantDetailActivity.this);
                            dlg.setTitle(pName + "의 케어 주기");
                            dlg.setIcon(R.drawable.ic_leaf);

                            careView = (View) view.inflate(PlantDetailActivity.this, R.layout.dialogue, null);
                            dlg.setView(careView);

                            TextView tv_dlg_water = (TextView) careView.findViewById(R.id.tv_dlg_water);
                            TextView tv_dlg_lastW = (TextView) careView.findViewById(R.id.tv_dlg_lastW);
                            TextView tv_dlg_firtilize = (TextView) careView.findViewById(R.id.tv_dlg_firtilize);
                            TextView tv_dlg_last_fil = (TextView) careView.findViewById(R.id.tv_dlg_last_fil);
                            TextView tv_dlg_reppot = (TextView) careView.findViewById(R.id.tv_dlg_reppot);
                            TextView tv_dlg_last_rp = (TextView) careView.findViewById(R.id.tv_dlg_last_rp);

                            tv_dlg_water.setText(pWater);
                            tv_dlg_firtilize.setText(pFil);
                            tv_dlg_reppot.setText(pRepot);

                            if (a != null) {
                                tv_dlg_lastW.setText(a);
                            }
                            if (b != null) {
                                tv_dlg_last_fil.setText(b);
                            }
                            if (c != null) {
                                tv_dlg_last_rp.setText(c);
                            }

                            dlg.show();
                        }
                    });
                    grid_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            strDrPt = diaryPhotoPath.get(i);


                            Intent toReadDiaryIntent = new Intent(PlantDetailActivity.this, ReadDiaryActivity.class);
                            toReadDiaryIntent.putExtra("filepath03", photoPath);
                            toReadDiaryIntent.putExtra("filePath04", strDrPt);
                            startActivity(toReadDiaryIntent);


                        }
                    });


                    grid_profile.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String pathProfile = diaryPhotoPath.get(i);
                            diaryPhotoPath.remove(i);
                            adapter.notifyDataSetChanged();

                            SQLiteDatabase sql = helper.getWritableDatabase();
                            sql.execSQL("delete FROM diaryTBL where   diaryPhotoPath='" + pathProfile + "';");

                            Toast.makeText(getApplicationContext(), "식물정보가 삭제되었습니다", Toast.LENGTH_LONG).show();


                            sql.close();

                            alert = false;


                            return false;
                        }
                    });

                }
            }
        }

    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


}