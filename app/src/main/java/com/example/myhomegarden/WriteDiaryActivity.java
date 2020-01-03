package com.example.myhomegarden;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.applandeo.materialcalendarview.EventDay;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteDiaryActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_profile_name02, tv_profile_date02;
    ImageView ivbtDiaryPhoto, btn_diary_cal, diary_ivbtn_cancle, diary_ivbtn_save, gohome;
    EditText edtDiaryDate, edtContext, edtHeight;
    LinearLayout linear_checkbox;
    /*heckBox check_water, check_fertilizer, check_repotting;*/

    String photoPath, pName, pDate, namename, newheight;
    View diary_calander_view;
    String diaryDate;

    final String TAG = getClass().getSimpleName();
    private int cameraView;
    String diary_PhotoPath;
    static final int REQUEST_TAKE_PICTURE = 1;
    LinearLayout entireLayout;

    CheckBox check[] = new CheckBox[3];
    Integer checkID[] = {R.id.check_water, R.id.check_fertilizer, R.id.check_repotting};
    Boolean checking = false;
    static final int WATER = 0;
    static final int FIRTILIZE = 1;
    static final int REPOTTING = 2;
    int checked[]={0, 0, 0};



    String db_diary_Date, db_diary_context, db_diary_photoPath;
    int db_check;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        entireLayout=(LinearLayout)findViewById(R.id.entireLayout);
        tv_profile_name02 = (TextView) findViewById(R.id.tv_profile_name02);
        tv_profile_date02 = (TextView) findViewById(R.id.tv_profile_date02);
        ivbtDiaryPhoto = (ImageView) findViewById(R.id.ivbtDiaryPhoto);
        edtHeight=(EditText)findViewById(R.id.edtHeight);
        gohome=(ImageView)findViewById(R.id.gohome);


        diary_ivbtn_save = (ImageView) findViewById(R.id.diary_ivbtn_save);
        edtDiaryDate = (EditText) findViewById(R.id.edtDiaryDate);
        edtContext = (EditText) findViewById(R.id.edtContext);
        linear_checkbox = (LinearLayout) findViewById(R.id.linear_checkbox);
       /* check_water = (CheckBox) findViewById(R.id.check_water);*/
      /*  check_fertilizer = (CheckBox) findViewById(R.id.check_fertilizer);
        check_repotting = (CheckBox) findViewById(R.id.check_repotting);
*/

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gohomeIntent=new Intent(WriteDiaryActivity.this,MainActivity.class);
                startActivity(gohomeIntent);
            }
        });

        ivbtDiaryPhoto.setOnClickListener(WriteDiaryActivity.this);
        for (int i = 0; i < checkID.length; i++) {
            check[i] = (CheckBox) findViewById(checkID[i]);
        }

        Calendar cal = Calendar.getInstance();
        final int cYear = cal.get(Calendar.YEAR);
        final int cMonth = cal.get(Calendar.MONTH);
        final int cDay = cal.get(Calendar.DAY_OF_MONTH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.
                    PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


        Intent fromDetailIntent = getIntent();
        photoPath = fromDetailIntent.getStringExtra("filepath02");
        namename = fromDetailIntent.getStringExtra("plantname02");

        final DBhelper helper = new DBhelper(WriteDiaryActivity.this);
        final SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        final Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM plantinfoTBL WHERE plantivPath = '" + photoPath + "';", null);
        if (photoPath != null) {
            cursor.moveToFirst();
            pName = cursor.getString(0);
            pDate = cursor.getString(2);

            tv_profile_name02.setText(pName);
            tv_profile_date02.setText(pDate);


            cursor.close();
            sqLiteDatabase.close();

        }
        edtDiaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder diaryCaldlg = new AlertDialog.Builder(WriteDiaryActivity.this);
                diary_calander_view = (View) view.inflate(WriteDiaryActivity.this, R.layout.firstmeetdialog, null);
                diaryCaldlg.setView(diary_calander_view);


                DatePicker firstCalDpicker = (DatePicker) diary_calander_view.findViewById(R.id.firstCalDpicker);
                diaryDate = cYear + "/" + (cMonth + 1) + "/" + cDay;


                firstCalDpicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        diaryDate = year + "/" + (month + 1) + "/" + day;
                        //문자와 결합하기때문에 string상자에 담을수 있음.

                        calendar = Calendar.getInstance();
                        calendar.set(year, month, day);


                    }
                });

                diaryCaldlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edtDiaryDate.setText(diaryDate);


                   /*     Intent returnIntent = new Intent();
                        EventDay eventDay = new EventDay(calendar,R.drawable.sample_icon_2);
                        returnIntent.putExtra();*/
                    }
                });
                diaryCaldlg.show();
                diaryCaldlg.setCancelable(false);
            }
        });

        edtHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력되는 텍스트에 변화가 있을 때 호출된다.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력이 끝났을 때 호출된다.
                edtHeight.setBackgroundResource(R.drawable.ic_height_cm);

                if ((edtHeight.getText().toString()).equals("")) {
                    edtHeight.setBackgroundResource(0);
                }else {
                    edtHeight.setBackgroundResource(R.drawable.ic_height_cm);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력하기 전에 호출된다.

            }
        });


        for (int c=0; c<checkID.length; c++){
            final int index=c;
        check[index].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked[index]=1;
            }

        });


        }

/*

        check_water.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (check_water.isChecked() == true) {
                    db_check = 1;
                } else {
                    db_check = 2;
                }

            }
        });

*/


        diary_ivbtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_diary_Date = diaryDate;
                db_diary_context = edtContext.getText().toString();
                db_diary_photoPath = diary_PhotoPath;
                newheight=edtHeight.getText().toString();



                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                /*  sqLiteDatabase.execSQL("CREATE TABLE diaryTBL (diaryDate TEXT , diaryContext TEXT , diaryCheckList TEXT, diaryPhotoPath TEXT);");*/
                sqLiteDatabase.execSQL("INSERT INTO diaryTBL VALUES('" + db_diary_Date + "','" + db_diary_context + "','" +  checked[0] + "','" + db_diary_photoPath + "','" + namename + "','" +  checked[1] + "','" + checked[2] + "','" + newheight+ "');"); //두번째 값은 정수형이라서 홑 따옴표를 안씀.
                sqLiteDatabase.close();
                Toast.makeText(getApplicationContext(), "일기등록을 성공했습니다!", Toast.LENGTH_LONG).show();

                Intent intent=new Intent();
                intent.putExtra("path", photoPath);

                intent.putExtra("name",namename);
                setResult(RESULT_OK,intent);
                finish();




                /*  Toast.makeText(getApplicationContext(), "입력되지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show();*/

            }
        });


    }

    @Override
    public void onClick(View view) {
        view.getId();

        if (view.getId() == R.id.ivbtDiaryPhoto) {
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doTakePhotoAction();
                }
            };


            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };

            AlertDialog.Builder cameraDlg = new AlertDialog.Builder(WriteDiaryActivity.this);
            cameraDlg.setTitle("식물일지 사진 촬영하기");
            cameraDlg.setPositiveButton("사진촬영", cameraListener);
            cameraDlg.setNegativeButton("취소", cancelListener);
            cameraDlg.show();


        }


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        diary_PhotoPath = image.getAbsolutePath();


        return image;


    }

    public void doTakePhotoAction() { //카메라 촬영 후 이미지 가져오기

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            //카메라로 촬영한 이미지를 파일로 저장해주는 함수를 만들어줍니다.
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myhomegarden.fileprovider",
                        photoFile);


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }

    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE: {


                    if (resultCode == RESULT_OK) {
                        File file = new File(diary_PhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));

                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(diary_PhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

                            Bitmap rotatedBitmap = null;
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }

                            ivbtDiaryPhoto.setImageBitmap(rotatedBitmap);
                            galleryAddPic();


                        }
                    }
                    break;
                }


            }
        } catch (Exception error) {
            error.printStackTrace();
        }


    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(diary_PhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);


    }


}