package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.OnHoldRecyclerAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/10/2017.
 */

public class OnHoldReceiptFragment extends BaseFragment{

    @BindView(R.id.rvOnHold)RecyclerView rvOnHold;

    OnHoldRecyclerAdapter onHoldRecyclerAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onhold_receipt, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        List<OrderReceipt> orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder().
                where(OrderReceiptDao.Properties.OnHold.eq(true)).list();

        onHoldRecyclerAdapter = new OnHoldRecyclerAdapter(getActivity(), (ArrayList<OrderReceipt>) orderReceiptList);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvOnHold.setLayoutManager(staggeredGridLayoutManager);
        rvOnHold.setAdapter(onHoldRecyclerAdapter);

    }
}
