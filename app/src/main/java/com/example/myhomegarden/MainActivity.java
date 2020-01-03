package com.example.myhomegarden;

import android.app.SearchManager;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView btnMainAdd, goSearch;
    GridView gridMainList;
    EditText edtSearch;

    ArrayList<String> MainPhotoPath = new ArrayList<String>();
    ArrayList<String> nameCunnected = new ArrayList<String>();
    Cursor cursor;

    String namename, pType;
    ImageView noplant;

    Boolean main = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMainAdd = (ImageView) findViewById(R.id.btnMainAdd);
        gridMainList = (GridView) findViewById(R.id.gridMainList);
        noplant = (ImageView) findViewById(R.id.noplant);
        goSearch = (ImageView) findViewById(R.id.goSearch);
        edtSearch = (EditText) findViewById(R.id.edtSearch);


        if (main == false) {
            noplant.setImageResource(R.drawable.ic_background);



        }

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력되는 텍스트에 변화가 있을 때 호출된다.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력이 끝났을 때 호출된다.
                goSearch.setImageResource(R.drawable.ic_tointernet02);

                if ((edtSearch.getText().toString()).equals("")) {
                    goSearch.setImageResource(0);
                }else {
                    goSearch.setImageResource(R.drawable.ic_tointernet02);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력하기 전에 호출된다.

            }
        });


        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pType = edtSearch.getText().toString();
                /* Uri uri = Uri.parse("http://www.google.com");*/
                Intent toSerchInfoIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                toSerchInfoIntent.putExtra(SearchManager.QUERY, pType);
                startActivity(toSerchInfoIntent);
            }
        });


        btnMainAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegisterIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(toRegisterIntent);


            }
        });

        final DBhelper helper = new DBhelper(MainActivity.this);
        final SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("select* from plantinfoTBL", null);
        while (cursor.moveToNext()) {
            nameCunnected.add(cursor.getString(0));
            MainPhotoPath.add(cursor.getString(3));
        }
        cursor.close();


        final MyAdapter myadapter = new MyAdapter(this);
        gridMainList.setAdapter(myadapter);


        gridMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    namename = nameCunnected.get(i);
                    android.util.Log.d("pathcheck", namename);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String path = MainPhotoPath.get(i);
                android.util.Log.i("filePath", path);
                Intent toPlantProfile = new Intent(MainActivity.this, PlantDetailActivity.class);
                toPlantProfile.putExtra("filepath", path);
                toPlantProfile.putExtra("plantname", namename);
                startActivity(toPlantProfile);


            }
        });


        gridMainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String path = MainPhotoPath.get(i);
                MainPhotoPath.remove(i);

                myadapter.notifyDataSetChanged();

                SQLiteDatabase sql = helper.getWritableDatabase();
                sql.execSQL("delete FROM plantinfoTBL where  plantivPath='" + path + "';");

                Toast.makeText(getApplicationContext(), "식물정보가 삭제되었습니다", Toast.LENGTH_LONG).show();

                main = false;
                if (gridMainList == null) {
                    noplant.setVisibility(View.VISIBLE);
                }


                sql.close();


                return false;
            }
        });


    }


    public class MyAdapter extends BaseAdapter {
        Context context;



        public MyAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return MainPhotoPath.size();
        }

        @Override
        public Object getItem(int i) {
            return MainPhotoPath.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView;


            if (view == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(420, 500));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 10, 8, 10);
            } else {
                imageView = (ImageView) view;
            }

            if (MainPhotoPath != null) {

                String photoPath = MainPhotoPath.get(i);
                Uri uri = Uri.fromFile(new File(photoPath));
                android.util.Log.d("pathcheck2", photoPath);
                Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());
                bmp = Bitmap.createScaledBitmap(bmp, 300, 380, false);


                if (bmp != null) {
                    ExifInterface ei = null;
                    try {
                        ei = new ExifInterface(photoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bmp, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bmp, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bmp, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bmp;
                    }
                    imageView.setImageBitmap(rotatedBitmap);
                    noplant.setVisibility(View.INVISIBLE);

                }
            }

            return imageView;


        }
    }

    //이미지 저장경로를 DB로 받아서 그리드뷰의 이미지뷰에 셋팅시키는 작업
    // 기존에는 일반배열에 넣어서 담아진 경로를 다이렉트로 받아왔지만 동적배열의 경로를 어떻게 담아옴?


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}



