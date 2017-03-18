package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.Adapter.SpinnerAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Discount;
import com.droidcoder.gdgcorp.posproject.dataentity.DiscountDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/26/2017.
 */

public class EditItemFragment extends BaseFragment {

    @BindView(R.id.btnHide)Button btnHide;
    @BindView(R.id.btnDone)Button btnDone;
    @BindView(R.id.btnMinusQuantity)ImageView btnMinus;
    @BindView(R.id.btnAddQuantity)ImageView btnAdd;
    @BindView(R.id.productImage)ImageView productImage;
    @BindView(R.id.productName)TextView productName;
    @BindView(R.id.productDesc)TextView productDesc;
    @BindView(R.id.editQuantity)EditText editQuantity;
    @BindView(R.id.spnrDiscount)Spinner spnrDiscount;
    @BindView(R.id.cbxTaxExempt)CheckBox cbxTaxExempt;
    @BindView(R.id.editNote)EditText editNote;

    SpinnerAdapter spinnerAdapter;
    int orderPosition;
    long discountId;

    InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

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

        spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerItemList);
        spnrDiscount.setAdapter(spinnerAdapter);

        if(!getArguments().isEmpty()){

            orderPosition = getArguments().getInt("orderPosition");
            productName.setText(getArguments().getString("orderName"));
            productDesc.setText(getArguments().getString("orderDescription"));
            productImage.setImageBitmap(ImageConverter.bytesToBitmap(getArguments().getByteArray("orderImage")));
            cbxTaxExempt.setChecked(getArguments().getBoolean("orderIsTaxExempt"));
            editQuantity.setText(StringConverter.doubleFormatter(getArguments().getDouble("orderQuantity")));
            editNote.setText(getArguments().getString("note"));
            discountId = getArguments().getLong("discountId");

        }

        for(int x = 0;spinnerItemList.size() >= x; x++) {
            if(spinnerItemList.get(x).getId() == discountId) {
                spnrDiscount.setSelection(x);
                break;
            }
        }

        spnrDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discountId = Long.parseLong(((TextView)view.findViewById(R.id.txtId)).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editQuantity.getWindowToken(), 0);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                        .remove(EditItemFragment.this)
                        .commit();

                ((OnEditItem)getActivity()).updateChanges(orderPosition, editQuantity.getText().toString(), editNote.getText().toString(),
                        cbxTaxExempt.isChecked(), discountId);
            }
        });

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                        .remove(EditItemFragment.this)
                        .commit();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double quantity = Double.parseDouble(editQuantity.getText().toString());
                quantity += 1;
                editQuantity.setText(StringConverter.doubleFormatter(quantity));
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double quantity = Double.parseDouble(editQuantity.getText().toString());

                if(!(quantity <= 1)){
                    quantity -= 1;
                }
                editQuantity.setText(StringConverter.doubleFormatter(quantity));

            }
        });

    }

    public interface OnEditItem{
        void updateChanges(int position, String quantity, String note, boolean isTaxExempt, long discountId);
    }

}