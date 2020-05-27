package eu.marcellofabbri.dailyroadmap;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class MyLineChartManager {
    private LineChart lineChart;
    private ArrayList<Entry> firstArrayOfEntries = new ArrayList<Entry>();
    private ArrayList<Entry> secondArrayOfEntries = new ArrayList<Entry>();

    public MyLineChartManager(LineChart injectedLineChart) {
        this.lineChart = injectedLineChart;
    }

    public ArrayList<Entry> getFirstArrayOfEntries() {
        return firstArrayOfEntries;
    }

    public ArrayList<Entry> getSecondArrayOfEntries() {
        return secondArrayOfEntries;
    }

    public void buildHourNotchesFirstHalf() {
        firstArrayOfEntries.add(new Entry(10000,420000));
        firstArrayOfEntries.add(new Entry(67920,420000));
        firstArrayOfEntries.add(new Entry(67920,420000));
        firstArrayOfEntries.add(new Entry(260000, 420000));
        firstArrayOfEntries.add(new Entry(299600, 389000));
        firstArrayOfEntries.add(new Entry(300000, 60000));
        firstArrayOfEntries.add(new Entry(300000,0));
        firstArrayOfEntries.add(new Entry(0, 0));
    }

    public void buildHourNotchesSecondHalf() {
        secondArrayOfEntries.add(new Entry(10000, 400000));
        secondArrayOfEntries.add(new Entry(10400, 60000));
        secondArrayOfEntries.add(new Entry(50000,0));
        secondArrayOfEntries.add(new Entry(230000, 0));
        secondArrayOfEntries.add(new Entry(230100, 60000));
        secondArrayOfEntries.add(new Entry(299999, 60000));
    }

    private void setLineDataSetStyle(LineDataSet lineDataSet) {
        lineDataSet.setLineWidth(30);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(7);
        lineDataSet.setCircleHoleColor(Color.BLUE);
    }

    private void setLineChartStyle(LineChart linechart) {
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setVisibleXRange(-30000, 310000);
    }

    public void createChart() {
        buildHourNotchesFirstHalf();
        buildHourNotchesSecondHalf();
        LineDataSet lineDataSetOne = new LineDataSet(firstArrayOfEntries, "Line Data Set One");
        LineDataSet lineDataSetTwo = new LineDataSet(secondArrayOfEntries, "Line Data Set Two");
        setLineDataSetStyle(lineDataSetOne);
        setLineDataSetStyle(lineDataSetTwo);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSetOne);
        dataSets.add(lineDataSetTwo);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        setLineChartStyle(lineChart);
        lineChart.invalidate();
    }

}
