package com.droidcoder.gdgcorp.posproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.zxing.client.result.CalendarParsedResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/13/2017.
 */

public class GraphAveCustomerFragment extends BaseFragment {

    @BindView(R.id.lineChart)LineChart lineChart;
    @BindView(R.id.filterContainer)LinearLayout filterContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_average_customer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Fragment fragment = new DateFilterFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(filterContainer.getId(), fragment, "");
        ft.commit();

    }

    public void createLineChart(Date startDate, Date endDate){

        int dateDiff = 1;/*((int)((endDate.getTime()/(24*60*60*1000))
                -(int)(startDate.getTime()/(24*60*60*1000))));*/

        if(dateDiff < 0){
            dateDiff = 0;
        }

        ArrayList<Entry> entryList = new ArrayList<>();
        int counter1 = 0, counter2 = 0, counter3 = 0, counter4 = 0, counter5 = 0, counter6 = 0, counter7 = 0, counter8 = 0, counter9 = 0, counter10 = 0, counter11 = 0
                , counter12 = 0, counter13 = 0, counter14 = 0, counter15 = 0, counter16 = 0, counter17 = 0, counter18 = 0, counter19 = 0, counter20 = 0, counter21 = 0, counter22 = 0, counter23 = 0, counter24 = 0;

        List<OrderReceipt> orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                .where(OrderReceiptDao.Properties.Created.between(startDate, endDate)).list();

        XAxis xl = lineChart.getXAxis();
        xl.setGranularity(1f);
        xl.setLabelCount(24);
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value <= 11) {
                    return (int) value + "AM";
                }else if(value == 12){
                    return (int) value + "PM";
                }else if(value > 12 && value < 24){
                    return ((int)value - 12) + "PM";
                }else{
                    return ((int)value - 12) + "AM";
                }
            }

        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);


        for(OrderReceipt orderReceipt : orderReceiptList){

            Calendar cal = Calendar.getInstance();
            cal.setTime(orderReceipt.getCreated());

            switch (cal.get(Calendar.HOUR_OF_DAY)){

                case 1:
                    counter1++;
                    break;

                case 2:
                    counter2++;
                    break;

                case 3:
                    counter3++;
                    break;

                case 4:
                    counter4++;
                    break;

                case 5:
                    counter5++;
                    break;

                case 6:
                    counter6++;
                    break;

                case 7:
                    counter7++;
                    break;

                case 8:
                    counter8++;
                    break;

                case 9:
                    counter9++;
                    break;

                case 10:
                    counter10++;
                    break;

                case 11:
                    counter11++;
                    break;

                case 12:
                    counter12++;
                    break;

                case 13:
                    counter13++;
                    break;

                case 14:
                    counter14++;
                    break;

                case 15:
                    counter15++;
                    break;

                case 16:
                    counter16++;
                    break;

                case 17:
                    counter17++;
                    break;

                case 18:
                    counter18++;
                    break;

                case 19:
                    counter19++;
                    break;

                case 20:
                    counter20++;
                    break;

                case 21:
                    counter21++;
                    break;

                case 22:
                    counter22++;
                    break;

                case 23:
                    counter23++;
                    break;

                case 24:
                    counter24++;
                    break;

            }

        }

        entryList.add(new Entry(1f, counter1/dateDiff));
        entryList.add(new Entry(2f, counter2/dateDiff));
        entryList.add(new Entry(3f, counter3/dateDiff));
        entryList.add(new Entry(4f, counter4/dateDiff));
        entryList.add(new Entry(5f, counter5/dateDiff));
        entryList.add(new Entry(6f, counter6/dateDiff));
        entryList.add(new Entry(7f, counter7/dateDiff));
        entryList.add(new Entry(8f, counter8/dateDiff));
        entryList.add(new Entry(9f, counter9/dateDiff));
        entryList.add(new Entry(10f, counter10/dateDiff));
        entryList.add(new Entry(11f, counter11/dateDiff));
        entryList.add(new Entry(12f, counter12/dateDiff));
        entryList.add(new Entry(13f, counter13/dateDiff));
        entryList.add(new Entry(14f, counter14/dateDiff));
        entryList.add(new Entry(15f, counter15/dateDiff));
        entryList.add(new Entry(16f, counter16/dateDiff));
        entryList.add(new Entry(17f, counter17/dateDiff));
        entryList.add(new Entry(18f, counter18/dateDiff));
        entryList.add(new Entry(19f, counter19/dateDiff));
        entryList.add(new Entry(20f, counter20/dateDiff));
        entryList.add(new Entry(21f, counter21/dateDiff));
        entryList.add(new Entry(22f, counter22/dateDiff));
        entryList.add(new Entry(23f, counter23/dateDiff));
        entryList.add(new Entry(24f, counter24/dateDiff));

        LineDataSet dataSet = new LineDataSet(entryList, "Total Customer Transaction per Hour");

        LineData lineData = new LineData(dataSet);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setTextSize(17f);
        lineChart.getLegend().setTextColor(Color.parseColor("#1976D2"));
        lineChart.setData(lineData);
        lineChart.animateY(1200, Easing.EasingOption.EaseInOutQuad);
        lineChart.invalidate();

    }

}
