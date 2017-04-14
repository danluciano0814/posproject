package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
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

        createLineChart(new Date(), new Date());

    }

    public void createLineChart(Date startDate, Date endDate){

        List<Entry> entryList = new ArrayList<>();

        List<OrderReceipt> orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                .where(OrderReceiptDao.Properties.Created.between(startDate, endDate)).list();

        entryList.add(new Entry(4f, 0f));
        entryList.add(new Entry(8f, 1f));
        entryList.add(new Entry(6f, 2f));
        entryList.add(new Entry(2f, 3f));
        entryList.add(new Entry(18f, 4f));
        entryList.add(new Entry(9f, 5f));


        LineDataSet dataSet = new LineDataSet(entryList, "labels");

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

    }

}
