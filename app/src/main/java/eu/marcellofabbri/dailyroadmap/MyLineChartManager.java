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
        for (int i = 0; i < 240; i++) {
            int upperSideNotch = (299600 - 20000)/300;
            firstArrayOfEntries.add(new Entry(20000 + (upperSideNotch*i),420000, new NumeralId(i)));
        }

        firstArrayOfEntries.add(new Entry(285600, 380000));

        for (int i = 1; i < 6; i++) {
            int rightSideNotch = 375000/6;
            firstArrayOfEntries.add(new Entry(285601 + i,380000 - i*rightSideNotch));
        }
    }

    public void buildPointsFirstHalf() {
        int upperSideNotch = (299600 - 20000)/360;
        int verticalSideNotch = (420000 - 25000)/420;
        int verticalSideNotch2 = (420000 - 25000)/328;
        for (int i = 0; i < 1440; i++) {
            if (i < 300) {
                firstArrayOfEntries.add(new Entry(20000 + upperSideNotch*i, 420000, new NumeralId(i)));
            }
            if (i >= 300 && i < 360) {
                firstArrayOfEntries.add(new Entry(20000 + upperSideNotch*i, 420000 - verticalSideNotch*(i-300), new NumeralId(i)));
            }
            if (i >= 360 && i < 660) {
                firstArrayOfEntries.add(new Entry(20000 + upperSideNotch*360 + i-300, 435000 - verticalSideNotch2*(i-300), new NumeralId(i)));
            }
        }
    }

    public void buildPointsSecondHalf() {
        int upperSideNotch = (299600 - 20000)/360;
        int verticalSideNotch = (420000 - 25000)/350;
        int verticalSideNotch2 = (420000 - 25000)/280;
        int verticalSideNotch3 = (420000 - 25000)/390;
        for (int i = 0; i < 1400; i++) {
            if (i < 360) {
                secondArrayOfEntries.add(new Entry(20000 + i, 405000 - verticalSideNotch*i, new NumeralId(1399-i)));
            }
            if (i >= 360 && i < 420) {
                secondArrayOfEntries.add(new Entry(20300 + upperSideNotch*(i-360), 405000 - verticalSideNotch*360 - verticalSideNotch*(i-360), new NumeralId(1399-i)));
            }
            if (i >= 420 && i < 660) {
                secondArrayOfEntries.add(new Entry(20300 + upperSideNotch*60 + upperSideNotch*(i-420), 405000 - verticalSideNotch*420, new NumeralId(1399-i)));
            }
            if (i >= 660 && i < 720) {
                secondArrayOfEntries.add(new Entry(20300 + upperSideNotch*300 + (i-660), 405000 - verticalSideNotch*420 + verticalSideNotch*(i-660), new NumeralId(1399-i)));
            }
            if (i >= 720 && i < 780) {
                secondArrayOfEntries.add(new Entry(20300 + upperSideNotch*300 + 60 + upperSideNotch*(i-720), 405000 - verticalSideNotch*360, new NumeralId(1399-i)));
            }
        }
    }

    public void buildHourNotchesSecondHalf() {
        for (int i = 0; i < 6; i++) {
            int leftSideNotch = (405000)/6;
            secondArrayOfEntries.add(new Entry(24000 + i,405000 - i*leftSideNotch));
        }

        secondArrayOfEntries.add(new Entry(60000, 25000));

        for (int i = 1; i <= 3; i++) {
            int lowerSideNotch = (299600 - 20000)/5;
            secondArrayOfEntries.add(new Entry(64000 + (lowerSideNotch*i),25000));
        }

        secondArrayOfEntries.add(new Entry(232000, 67000));
        secondArrayOfEntries.add(new Entry(285607, 67000));
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
        lineChart.setVisibleXRange(-50000, 310000);
    }

    public void createChart() {
        buildPointsFirstHalf();
        buildPointsSecondHalf();
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

    class NumeralId {
        private int index;

        public NumeralId(int index) {
            this.index = index;
        }
    }

}
