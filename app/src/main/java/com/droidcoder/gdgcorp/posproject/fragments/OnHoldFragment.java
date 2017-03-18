package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/7/2017.
 */

public class OnHoldFragment extends BaseDialogFragment {

    @BindView(R.id.btnOk)Button btnOk;
    @BindView(R.id.btnNo)Button btnNo;
    @BindView(R.id.editIdentification)EditText editId;
    long receiptId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_hold, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        if(!getArguments().getString("receiptId").equalsIgnoreCase("")){
            receiptId = Long.parseLong(getArguments().getString("receiptId"));
            if(receiptId>0){
                String uniqueId = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                        .where(OrderReceiptDao.Properties.ReceiptId.eq(receiptId))
                        .where(OrderReceiptDao.Properties.OnHold.eq(true)).list().get(0).getReceiptIdentification();

                editId.setText(uniqueId);
            }
        }

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editId.getText().toString().trim().toUpperCase();
                boolean isValid = true;

                OrderReceipt orderReceipt1 = null;
                OrderReceipt orderReceipt2 = null;

                if(DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                        .where(OrderReceiptDao.Properties.ReceiptIdentification.eq(input))
                        .where(OrderReceiptDao.Properties.OnHold.eq(true)).list().size() > 0){

                    orderReceipt1 = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                            .where(OrderReceiptDao.Properties.ReceiptIdentification.eq(input))
                            .where(OrderReceiptDao.Properties.OnHold.eq(true)).list().get(0);

                }

                if(receiptId > 0){
                    orderReceipt2 = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                            .where(OrderReceiptDao.Properties.ReceiptId.eq(receiptId))
                            .where(OrderReceiptDao.Properties.OnHold.eq(true)).list().get(0);
                }

                if(input.equalsIgnoreCase("")){
                    editId.setError("This field is required.");
                    isValid = false;
                }

                if(orderReceipt2 != null){

                    if(orderReceipt1.getId() != orderReceipt2.getId()){
                        editId.setError("Identification already used");
                        isValid = false;
                    }

                }else{

                    if(DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                            .where(OrderReceiptDao.Properties.ReceiptIdentification.eq(input))
                            .where(OrderReceiptDao.Properties.OnHold.eq(true)).list().size() > 0){

                        editId.setError("Identification already used");
                        isValid = false;
                    }

                }

                if(isValid){
                    ((SalesActivity)getActivity()).saveToOnHold(input);
                    dismiss();
                }

            }
        });

    }

}
