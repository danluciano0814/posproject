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
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.MyValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/12/2017.
 */

public class GraphTransactionFragment extends BaseFragment {

    @BindView(R.id.pieChartTransaction)PieChart pieChartTransaction;
    @BindView(R.id.filterContainer)LinearLayout filterContainer;
    @BindView(R.id.txtCash)TextView txtCash;
    @BindView(R.id.txtCredit)TextView txtCredit;
    @BindView(R.id.txtPoints)TextView txtPoints;
    @BindView(R.id.txtVoided)TextView txtVoided;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_transaction, container, false);
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

    public void createPieChart(Date startDate, Date endDate){

        pieChartTransaction.setUsePercentValues(true);
        pieChartTransaction.animateY(1200, Easing.EasingOption.EaseInOutQuad);

        /*Legend l = pieChartTransaction.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(0f);*/

        pieChartTransaction.getLegend().setEnabled(false);
        pieChartTransaction.getDescription().setEnabled(false);

        pieChartTransaction.setEntryLabelColor(Color.BLACK);
        pieChartTransaction.setEntryLabelTextSize(12f);

        List<PieEntry> pieEntryList = new ArrayList<>();

        List<OrderReceipt> orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                .where(OrderReceiptDao.Properties.Created.between(startDate, endDate)).list();

        int cashCounter = 0;
        int creditCounter = 0;
        int pointsCounter = 0;
        int voidedCounter = 0;

        for(OrderReceipt orderReceipt : orderReceiptList){

            if(orderReceipt.getDeleted() != null){
                voidedCounter++;
            }else if(orderReceipt.getPaymentType().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CASH)
                    || orderReceipt.getOnHold()){
                cashCounter++;
            }else if(orderReceipt.getPaymentType().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CREDIT)){
                creditCounter++;
            }else if(orderReceipt.getPaymentType().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_POINTS)){
                creditCounter++;
            }

        }

        float cashPercent = cashCounter != 0 ? ((float)cashCounter/orderReceiptList.size()) * 100 : 0;
        float creditPercent = creditCounter != 0 ? ((float)creditCounter/orderReceiptList.size()) * 100 : 0;
        float pointsPercent = pointsCounter != 0 ? ((float)pointsCounter/orderReceiptList.size()) * 100 : 0;
        float voidedPercent = voidedCounter != 0 ? ((float)voidedCounter/orderReceiptList.size()) * 100 : 0;

        pieEntryList.add(new PieEntry(pointsPercent, "Points"));
        pieEntryList.add(new PieEntry(creditPercent, "Credit"));
        pieEntryList.add(new PieEntry(voidedPercent, "Voided"));
        pieEntryList.add(new PieEntry(cashPercent, "Cash"));

        PieDataSet dataSet = new PieDataSet(pieEntryList, "Transaction Types");
        dataSet.setValueFormatter(new MyValueFormatter());
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);

        pieChartTransaction.setData(pieData);
        pieChartTransaction.invalidate();

        txtCash.setText(cashCounter + " ");
        txtCredit.setText(creditCounter + " ");
        txtPoints.setText(pointsCounter + " ");
        txtVoided.setText(voidedCounter + " ");

    }

}
