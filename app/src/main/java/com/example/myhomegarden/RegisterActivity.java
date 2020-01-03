package com.example.myhomegarden;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivbtMainPhoto, ivbtn_cancle, ivbtn_save, gohome, backBtn;
    EditText edtPlantType, edtPlantName, edtPlantDate,edtHeight;

    final String TAG = getClass().getSimpleName();
    private int id_View;
    static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath;


    View firstCalView;
    String firstdate;

    DBhelper helper;
    String db_photoPath, db_plantName, db_PlantmeetDate, db_plantType;

    Spinner spW, spFt, spRp;
    String spwaterItem[] = { "물을 언제 줄까요?","하루에 한번", "2일에 한번", "3일에 한번", "일주일에 한번", "10일에 한번", "이주에 한번", "한달에 한번"};
    String spFirtileItem[] = {"영양을 언제 줄까요?", "1개월에 한번", "2개월에 한번", "3개월에 한번", "5개월에 한번", "일년에 한번"};
    String spReppotItem[] = {"분갈이를 언제 할까요?", "3개월에 한번", "6개월에 한번", "1년에 한번", "2년에 한번"};
    String spw_item, spft_item, sprp_item;
    int plantTall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ivbtMainPhoto = (ImageView) findViewById(R.id.ivbtMainPhoto);
        ivbtn_save = (ImageView) findViewById(R.id.ivbtn_save);
        edtPlantType = (EditText) findViewById(R.id.edtPlantType);
        edtPlantName = (EditText) findViewById(R.id.edtPlantName);
        edtPlantDate = (EditText) findViewById(R.id.edtPlantDate);
        edtHeight=(EditText)findViewById(R.id.edtHeight);
        gohome=(ImageView)findViewById(R.id.gohome);
        backBtn=(ImageView)findViewById(R.id.backBtn);


        spW = (Spinner) findViewById(R.id.spW);
        spFt = (Spinner) findViewById(R.id.spFt);
        spRp = (Spinner) findViewById(R.id.spRp);

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gohomeIntent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(gohomeIntent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });


        ivbtMainPhoto.setOnClickListener(RegisterActivity.this);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spwaterItem);

        spW.setAdapter(adapter);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spFirtileItem);

        spFt.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spReppotItem);

        spRp.setAdapter(adapter2);

        helper = new DBhelper(RegisterActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.
                    PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


        edtPlantDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder firstCaldlg = new AlertDialog.Builder(RegisterActivity.this);
                firstCalView = (View) view.inflate(RegisterActivity.this, R.layout.firstmeetdialog, null);

                firstCaldlg.setView(firstCalView);


                DatePicker firstCalDpicker = (DatePicker) firstCalView.findViewById(R.id.firstCalDpicker);
                Calendar cal = Calendar.getInstance();
                final int cYear = cal.get(Calendar.YEAR);
                final int cMonth = cal.get(Calendar.MONTH);
                final int cDay = cal.get(Calendar.DAY_OF_MONTH);
                firstdate = cYear + "년" + (cMonth + 1) + "월" + cDay + "일";


                firstCalDpicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        firstdate = year + "년" + (month + 1) + "월" + day + "일";
                        //문자와 결합하기때문에 string상자에 담을수 있음.


                    }
                });


                firstCaldlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edtPlantDate.setText(firstdate);
                        /*                        btn_cal_view.setImageResource(R.drawable.search_icon_modify);*/
                    }
                });
                firstCaldlg.show();
                firstCaldlg.setCancelable(false);

            }
        });



        spW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spw_item=spwaterItem[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
               spFt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       spft_item=spFirtileItem[i];
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> adapterView) {

                   }
               });
     spRp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             sprp_item=spReppotItem[i];
         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

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


        ivbtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_PlantmeetDate = firstdate;
                db_plantName = edtPlantName.getText().toString();
                db_plantType = edtPlantType.getText().toString();
                db_photoPath = mCurrentPhotoPath;
                plantTall=Integer.parseInt(edtHeight.getText().toString());

                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

                sqLiteDatabase.execSQL("INSERT INTO plantinfoTBL VALUES('" + db_plantName + "','" + db_plantType + "','" + db_PlantmeetDate + "','" + db_photoPath + "','"+plantTall+"','"+spw_item+"','"+spft_item+"','"+sprp_item+"');"); //두번째 값은 정수형이라서 홑 따옴표를 안씀.
                sqLiteDatabase.close();
                Toast.makeText(getApplicationContext(), "식물등록을 성공했습니다!", Toast.LENGTH_LONG).show();
                Intent toMainSavedintent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(toMainSavedintent);

                /*    Toast.makeText(getApplicationContext(),"입력되지 않은 정보가 있습니다.",Toast.LENGTH_SHORT).show();*/


            }
        });


    }

    @Override
    public void onClick(View view) {
        id_View = view.getId();

        if (view.getId() == R.id.ivbtMainPhoto) {
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

            AlertDialog.Builder dlg = new AlertDialog.Builder(RegisterActivity.this);
            dlg.setTitle("식물 프로필 사진");
            dlg.setPositiveButton("사진촬영", cameraListener);
            dlg.setNegativeButton("취소", cancelListener);
            dlg.show();


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
        mCurrentPhotoPath = image.getAbsolutePath();


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
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    //권한요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                case REQUEST_TAKE_PHOTO: {


                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));

                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
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

                            ivbtMainPhoto.setImageBitmap(rotatedBitmap);
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
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);


    }
/*
    private void setPic() {
        // Get the dimensions of the View
        int targetW = ivbtMainPhoto.getWidth();
        int targetH = ivbtMainPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ivbtMainPhoto.setImageBitmap(bitmap);
    }*/
}