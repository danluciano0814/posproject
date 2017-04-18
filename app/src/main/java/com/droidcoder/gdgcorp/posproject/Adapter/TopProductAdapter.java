package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.TopProduct;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.List;

/**
 * Created by DanLuciano on 4/17/2017.
 */

public class TopProductAdapter extends BaseAdapter {

    List<TopProduct> topProductList;
    Context context;

    public TopProductAdapter(Context context, List<TopProduct> topProductList){
        this.topProductList = topProductList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return topProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return topProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_top_product, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.txtProductName.setText(topProductList.get(position).getProductName());
        holder.txtRank.setText(topProductList.get(position).getRank() + "");
        holder.txtTotalSold.setText(StringConverter.doubleCommaFormatter(topProductList.get(position).getTotalSold()));
        holder.txtTotalSales.setText(StringConverter.doubleCommaFormatter(topProductList.get(position).getTotalSales()));

        return view;
    }

    class Holder{

        TextView txtRank;
        TextView txtProductName;
        TextView txtTotalSold;
        TextView txtTotalSales;


        public Holder(View view){
            txtRank = (TextView) view.findViewById(R.id.txtRank);
            txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            txtTotalSold = (TextView) view.findViewById(R.id.txtTotalSold);
            txtTotalSales = (TextView) view.findViewById(R.id.txtTotalSales);

        }
    }


}
