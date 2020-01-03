package com.example.myhomegarden;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GrowthReportActivity extends AppCompatActivity {
    TextView titlename;

    private LineChart lineChart;


    ArrayList<String> dbName = new ArrayList<>();
    String plantName;
    int firstHeight;
    ArrayList<Integer> heights = new ArrayList<>();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growrh_report);
        lineChart = (LineChart) findViewById(R.id.chart);
        titlename = (TextView) findViewById(R.id.titlename);

        Intent fromPlantDetailIntent = getIntent();
        plantName = fromPlantDetailIntent.getStringExtra("plantname02");
        titlename.setText(plantName);

        final DBhelper helper = new DBhelper(GrowthReportActivity.this);
        final SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from plantinfoTBL WHERE plantName = '" + plantName + "';", null);
        //Cursor cursor = sqLiteDatabase.rawQuery("select * from diaryTBL", null);

        while (cursor.moveToNext()) {

            firstHeight = cursor.getInt(4);
        }
        cursor.close();

        cursor = sqLiteDatabase.rawQuery("select * from diaryTBL WHERE plantName02 = '" + plantName + "';", null);

        while (cursor.moveToNext()) {

            heights.add(cursor.getInt(7));
        }


        sqLiteDatabase.close();

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, firstHeight));
        try {
            if (heights != null) {
                int x = 2;
                for (int i = 0; i < heights.size(); i++) {
                    int y = heights.get(i);

                    if (y != 0) {
                        entries.add(new Entry(x, y));
                        x++;
                    }


                }

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "새롭게 등록된 식물의 키 정보가 없어요", Toast.LENGTH_SHORT);
        }


        LineDataSet lineDataSet = new LineDataSet(entries, "cm");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFC44D"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFC44D"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(5, 20, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2500, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();


    }
}