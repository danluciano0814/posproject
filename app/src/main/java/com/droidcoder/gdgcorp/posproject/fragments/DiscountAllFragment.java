package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.Adapter.SpinnerAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Discount;
import com.droidcoder.gdgcorp.posproject.dataentity.DiscountDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/7/2017.
 */

public class DiscountAllFragment extends BaseDialogFragment {

    @BindView(R.id.btnOk)Button btnOk;
    @BindView(R.id.btnNo)Button btnNo;
    @BindView(R.id.spnrDiscount)Spinner spnrDiscount;

    SpinnerAdapter spinnerAdapter;
    long discountId = 0;
    String discountName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount_all, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        List<SpinnerItem> spinnerItemList = new ArrayList<>();
        SpinnerItem spinnerItem = new SpinnerItem();
        spinnerItem.setName("-N/A-");
        spinnerItem.setId(0);
        spinnerItemList.add(spinnerItem);

        for(Discount discount : DBHelper.getDaoSession().getDiscountDao().queryBuilder()
                .where(DiscountDao.Properties.Deleted.isNull())
                .orderAsc(DiscountDao.Properties.Name).list()){

            SpinnerItem item = new SpinnerItem();
            item.setId(discount.getId());
            String label = discount.getIsPercentage()? "%":"";
            item.setName(discount.getName() + " - less " + StringConverter.doubleFormatter(discount.getDiscountValue()) + label);
            spinnerItemList.add(item);
        }

        spnrDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discountId = Long.parseLong(((TextView)view.findViewById(R.id.txtId)).getText().toString());
                discountName = ((TextView)view.findViewById(R.id.txtName)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerItemList);
        spnrDiscount.setAdapter(spinnerAdapter);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SalesActivity)getActivity()).discountAll(discountId, discountName);
                dismiss();
            }
        });

    }

}