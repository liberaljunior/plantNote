package com.example.myhomegarden;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalCheckItemActivity extends AppCompatActivity{
    String onegetDate;
    int y, m, d;

    ArrayList<String> calData = new ArrayList<String>();
    ArrayList<Integer> calcheckItem = new ArrayList<Integer>();

    String plantName;
    ArrayList<String> dbName = new ArrayList<>();

    TextView titlename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_check_item);
        titlename=(TextView)findViewById(R.id.titlename);

        Intent fromPlantDetailIntent = getIntent();
        plantName = fromPlantDetailIntent.getStringExtra("plantname02");

        titlename.setText(plantName);


        final DBhelper helper = new DBhelper(CalCheckItemActivity.this);
        final SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from diaryTBL", null);


        while (cursor.moveToNext()) {
            calData.add(cursor.getString(0));
            calcheckItem.add(cursor.getInt(2));
            dbName.add(cursor.getString(4));

        }
        cursor.close();
        sqLiteDatabase.close();
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        List<EventDay> events = new ArrayList<>();



        for (int a = 0; a < calData.size(); a++) {
            try {
                if ((dbName.get(a)).equals(plantName)) {
                    if (calcheckItem.get(a) == 1) {
                        onegetDate = calData.get(a);
                        String getDate[] = onegetDate.split("/");
                        y = Integer.parseInt(getDate[0]);
                        m = Integer.parseInt(getDate[1]);
                        d = Integer.parseInt(getDate[2]);
                        android.util.Log.d("checkcheck", y + "/" + m + "/" + d);

                        Calendar calendar = Calendar.getInstance();


                        calendar.set(y, m - 1, d);
                        events.add(new EventDay(calendar, R.drawable.ic_water));
                        calendarView.setEvents(events);
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장된 데이터가 없습니다", Toast.LENGTH_SHORT).show();
            }

        }

        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -2);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 2);

        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);


    }


}