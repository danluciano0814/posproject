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
import android.widget.ListView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.Adapter.TopProductAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.TopProduct;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.MapUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/17/2017.
 */

public class GraphTopItems extends BaseFragment {

    @BindView(R.id.topItemChart)HorizontalBarChart topItemChart;
    @BindView(R.id.filterContainer)LinearLayout filterContainer;
    @BindView(R.id.lvTopItems)ListView lvTopItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_topitems, container, false);
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

    public void createTopItemChart(Date startDate, Date endDate){

        ArrayList<TopProduct> topProductList = new ArrayList<>();

        List<BarEntry> barEntryList = new ArrayList<BarEntry>();

        List<OrderProduct> orderProductList = DBHelper.getDaoSession().getOrderProductDao().queryBuilder()
                .where(OrderProductDao.Properties.Created.between(startDate, endDate)).list();

        Map<Long, Double> productMap = new HashMap<>();
        Map<Long, Double> productCount = new HashMap<>();

        YAxis leftAxis = topItemChart.getAxisRight();
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(1f);
        leftAxis.setCenterAxisLabels(true);
        leftAxis.setValueFormatter(new LargeValueFormatter());

        for(OrderProduct orderProduct : orderProductList){

            long key = orderProduct.getProductId();
            double totalDeductedPrice = orderProduct.getProductDeductedPrice();
            double totalQuantity = orderProduct.getProductQuantity();

            if(productMap.containsKey(key)){
                double total = productMap.get(key) + totalDeductedPrice;
                double totalQty = productCount.get(key) + totalQuantity;
                productMap.put(key, total);
                productCount.put(key, totalQty);
            }else{
                productMap.put(key, totalDeductedPrice);
                productCount.put(key, totalQuantity);
            }
        }

        String res = "";
        int counter = 1;
        for(Map.Entry<Long, Double> product : MapUtil.sortByValue(productCount).entrySet()){
            if(counter > 10){
                break;
            }
            //res += "ID : " + product.getKey() + " / VALUE : " + productMap.get(product.getKey()) + "\n";
            Product prod = DBHelper.getDaoSession().getProductDao().load(product.getKey());
            barEntryList.add(new BarEntry((float)counter, product.getValue().floatValue()));
            topProductList.add(new TopProduct(counter, prod.getName(), productCount.get(product.getKey()), productMap.get(product.getKey())));
            counter++;
        }
        //Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();

        BarDataSet barDataSet = new BarDataSet(barEntryList, "Top Items");
        barDataSet.setColor(Color.rgb(25,118,210));
        BarData barData = new BarData(barDataSet);
        topItemChart.getLegend().setEnabled(false);
        topItemChart.getDescription().setEnabled(false);
        topItemChart.setData(barData);
        topItemChart.animateY(1200, Easing.EasingOption.EaseInOutQuad);
        topItemChart.invalidate();

        TopProductAdapter topProductAdapter = new TopProductAdapter(getActivity(), topProductList);
        lvTopItems.setAdapter(topProductAdapter);
        lvTopItems.invalidate();

    }

}
