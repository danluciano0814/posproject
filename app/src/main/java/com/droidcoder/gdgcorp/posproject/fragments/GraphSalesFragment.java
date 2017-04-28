package com.droidcoder.gdgcorp.posproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/7/2017.
 */

public class GraphSalesFragment extends BaseFragment {


    @BindView(R.id.barChartSales)BarChart barChartSales;
    @BindView(R.id.spinnerYear)Spinner spinnerYear;

    @BindView(R.id.txtCash)TextView txtCash;
    @BindView(R.id.txtCredit)TextView txtCredit;
    @BindView(R.id.txtPoints)TextView txtPoints;

    String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN"
            , "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", " ", " "};

    int year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_sales, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        year = Calendar.getInstance().get(Calendar.YEAR);

        populateSpinner(year);
        createBarChart(year);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(Integer.parseInt(parent.getItemAtPosition(position).toString().trim()) != year){
                    year = Integer.parseInt(parent.getItemAtPosition(position).toString().trim());
                    createBarChart(year);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void populateSpinner(int year) {
        ArrayList<String> yearList = new ArrayList<>();
        ArrayAdapter<String> yearAdapter = null;

        for(int i = year; i >= year - 5; i--){
            yearList.add("" + i);
        }

        yearAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, yearList);
        spinnerYear.setAdapter(yearAdapter);
    }

    private void createBarChart(int yearValue) {

        //data
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"

        int startYear = 0;
        int endYear = 13;

        Calendar calYear = Calendar.getInstance();
        calYear.set(Calendar.YEAR, yearValue);

        barChartSales.setDrawBarShadow(false);
        barChartSales.setDrawValueAboveBar(true);
        barChartSales.setMaxVisibleValueCount(50);
        barChartSales.setPinchZoom(false);
        barChartSales.setDrawGridBackground(false);

        XAxis xl = barChartSales.getXAxis();
        xl.setGranularity(1f);
        xl.setLabelCount(13);
        xl.setCenterAxisLabels(true);
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value < 0){
                    return "";
                }else{
                    return months[(int)value];
                }
            }

        });

        YAxis leftAxis = barChartSales.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true
        barChartSales.getAxisRight().setEnabled(false);


        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();

        List<OrderProduct> orderProductList = new ArrayList<>();

        //refresh dao first for updating orderProducts
        for(OrderReceipt orderRefresh : DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                    .where(OrderReceiptDao.Properties.Deleted.isNull())
                    .list()){
            orderRefresh.refresh();
        }

        //populate data here
        List<OrderReceipt> orderDetailsList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                .where(OrderReceiptDao.Properties.Deleted.isNull())
                .list();

        double cashSales = 0;
        double creditSales = 0;
        double pointSales = 0;
        double cashCost = 0;
        double creditCost = 0;
        double pointCost = 0;

        for(OrderReceipt orderReceipt : orderDetailsList){
            orderProductList.addAll(orderReceipt.getOrderProducts());

            Calendar cal = Calendar.getInstance();
            cal.setTime(orderReceipt.getCreated());

            if((orderReceipt.getPaymentType().equals(GlobalConstants.PAYMENT_TYPE_CASH) || orderReceipt.getPaymentType().equals(GlobalConstants.PAYMENT_TYPE_CREDIT))
                    && cal.get(Calendar.YEAR) == calYear.get(Calendar.YEAR)
                    && orderReceipt.getIsPaid()){
                cashSales += orderReceipt.getTotalDeductedPrice() + orderReceipt.getServiceChargeTotal();
                cashCost += orderReceipt.getTotalCostPrice();
            }else if(orderReceipt.getPaymentType().equals(GlobalConstants.PAYMENT_TYPE_CREDIT)
                    && cal.get(Calendar.YEAR) == calYear.get(Calendar.YEAR) && !orderReceipt.getIsPaid()){
                creditSales += orderReceipt.getTotalDeductedPrice() + orderReceipt.getServiceChargeTotal();
                creditCost += orderReceipt.getTotalCostPrice();
            }else if(orderReceipt.getPaymentType().equals(GlobalConstants.PAYMENT_TYPE_POINTS)
                    && cal.get(Calendar.YEAR) == calYear.get(Calendar.YEAR)){
                pointSales += orderReceipt.getTotalDeductedPrice() + orderReceipt.getServiceChargeTotal();
                pointCost += orderReceipt.getTotalCostPrice();
            }
        }

        txtCash.setText("Sales : " + StringConverter.doubleCommaFormatter(cashSales) + "\n" + "Profit : " + StringConverter.doubleCommaFormatter(cashSales - cashCost));
        txtCredit.setText("Sales : " + StringConverter.doubleCommaFormatter(creditSales) + "\n" + "Profit : " + StringConverter.doubleCommaFormatter(creditSales - creditCost));
        txtPoints.setText("Sales : " + StringConverter.doubleCommaFormatter(pointSales) + "\n" + "Profit : " + StringConverter.doubleCommaFormatter(pointSales - pointCost));

        for (int i = startYear; i < endYear; i++){

            float value1 = 0;
            float value2 = 0;

            for(OrderProduct orderProduct : orderProductList){

                Calendar cal = Calendar.getInstance();
                cal.setTime(orderProduct.getCreated());

                if(orderProduct.getCreated().getMonth() == i && cal.get(Calendar.YEAR) == calYear.get(Calendar.YEAR)){
                    value1 += orderProduct.getProductDeductedPrice() + (orderProduct.getProductDeductedPrice()/100 * orderProduct.getServiceCharge());
                    value2 += orderProduct.getProductCostPrice();
                }
            }

            yVals1.add(new BarEntry(i, value1));
            yVals2.add(new BarEntry(i, value1 - value2));

        }

        BarDataSet set1, set2;

        set1 = new BarDataSet(yVals1, "SALES");
        set1.setColor(Color.rgb(25,118,210));

        set2 = new BarDataSet(yVals2, "GROSS");
        set2.setColor(Color.rgb(76,175,80));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new LargeValueFormatter());
        
        barChartSales.setData(data);
        barChartSales.getLegend().setEnabled(false);
        barChartSales.getDescription().setEnabled(false);
        barChartSales.getBarData().setBarWidth(barWidth);
        barChartSales.getXAxis().setAxisMinValue(startYear);
        barChartSales.groupBars(0f, groupSpace, barSpace);
        barChartSales.invalidate();
        barChartSales.animateY(1200, Easing.EasingOption.EaseInOutQuad);

    }

}
