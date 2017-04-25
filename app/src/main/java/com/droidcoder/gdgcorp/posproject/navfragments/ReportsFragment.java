package com.droidcoder.gdgcorp.posproject.navfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.fragments.ProductSalesFragment;
import com.droidcoder.gdgcorp.posproject.fragments.ProductSummaryFragment;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/19/2017.
 */

public class ReportsFragment extends BaseFragment {

    @BindView(R.id.main_frame) FrameLayout mainFrame;
    @BindView(R.id.ln_product_sales) LinearLayout lnProduct;

    Fragment reportFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        reportFragment = new ProductSalesFragment();
        getChildFragmentManager().beginTransaction().replace(mainFrame.getId(), reportFragment, "productSales").commit();
        lnProduct.setBackground(getResources().getDrawable(R.drawable.line_below));

    }

    public void generateReport(Date startDate, Date endDate){

        if(reportFragment instanceof ProductSalesFragment){
            ((ProductSalesFragment)reportFragment).generateReport(startDate, endDate);
        }

    }

}
