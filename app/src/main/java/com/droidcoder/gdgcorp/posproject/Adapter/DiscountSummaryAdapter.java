package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Discount;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 2/27/2017.
 */

public class DiscountSummaryAdapter extends BaseAdapter {

    List<Discount> discountList;
    Context context;

    public DiscountSummaryAdapter(Context context, List<Discount> discountList){
        this.discountList = discountList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return discountList.size();
    }

    @Override
    public Object getItem(int position) {
        return discountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final DiscountSummaryAdapter.Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_discount_summary, parent, false);
            holder = new DiscountSummaryAdapter.Holder(view);
            view.setTag(holder);
        }else{
            holder = (DiscountSummaryAdapter.Holder) view.getTag();
        }
        holder.discountId.setText(discountList.get(position).getId().toString());
        holder.discountName.setText(discountList.get(position).getName());
        holder.value.setText(StringConverter.doubleFormatter(discountList.get(position).getDiscountValue()));
        holder.cbxPercentage.setChecked(discountList.get(position).getIsPercentage());

        if(discountList.get(position).getDeleted() == null){
            holder.cbxActive.setChecked(true);
        }else{
            holder.cbxActive.setChecked(false);
        }

        holder.cbxActive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxActive.isChecked()){
                        holder.cbxActive.setChecked(false);
                        Discount discount = DBHelper.getDaoSession().getDiscountDao().load(discountList.get(position).getId());
                        discount.setDeleted(new Date());
                        DBHelper.getDaoSession().getDiscountDao().update(discount);

                    }else{
                        holder.cbxActive.setChecked(true);
                        Discount discount = DBHelper.getDaoSession().getDiscountDao().load(discountList.get(position).getId());
                        discount.setDeleted(null);
                        DBHelper.getDaoSession().getDiscountDao().update(discount);
                    }
                }

                return true;
            }
        });

        return view;
    }

    class Holder{

        TextView discountId;
        TextView discountName;
        TextView value;
        CheckBox cbxActive;
        CheckBox cbxPercentage;

        public Holder(View view){
            discountId = (TextView) view.findViewById(R.id.txtDiscountId);
            discountName = (TextView) view.findViewById(R.id.txtDiscountName);
            value = (TextView) view.findViewById(R.id.txtValue);
            cbxActive = (CheckBox) view.findViewById(R.id.cbxActive);
            cbxPercentage = (CheckBox) view.findViewById(R.id.cbxPercentage);

        }
    }

}