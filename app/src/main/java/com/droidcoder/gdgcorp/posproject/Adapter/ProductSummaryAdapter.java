package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 1/20/2017.
 */

public class ProductSummaryAdapter extends BaseAdapter {

    List<Product> productList;
    Context context;

    public ProductSummaryAdapter(Context context, List<Product> productList){
        this.productList = productList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
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
            view = inflater.inflate(R.layout.row_product_summary, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.productId.setText(productList.get(position).getId().toString());
        holder.productName.setText(productList.get(position).getName());
        holder.productCostPrice.setText(StringConverter.doubleFormatter(productList.get(position).getCostPrice()));
        holder.productSellPrice.setText(StringConverter.doubleFormatter(productList.get(position).getSellPrice()));
        holder.productStocks.setText(StringConverter.doubleFormatter(productList.get(position).getStocks()));
        if(productList.get(position).getImage() != null ){
            holder.productImage.setImageBitmap(ImageConverter.bytesToBitmap(productList.get(position).getImage()));
        }else{
            holder.productImage.setImageResource(R.drawable.noimage);
        }

        if(productList.get(position).getDeleted() == null){
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
                        Product product = DBHelper.getDaoSession().getProductDao().load(productList.get(position).getId());
                        product.setDeleted(new Date());
                        DBHelper.getDaoSession().getProductDao().update(product);
                        //((InventoryActivity)context).refresh();
                    }else{
                        holder.cbxActive.setChecked(true);
                        Product product = DBHelper.getDaoSession().getProductDao().load(productList.get(position).getId());
                        product.setDeleted(null);
                        DBHelper.getDaoSession().getProductDao().update(product);
                        //((InventoryActivity)context).refresh();
                    }
                }

                return true;
            }
        });

        return view;
    }

    class Holder{

        TextView productId;
        TextView productName;
        TextView productCostPrice;
        TextView productSellPrice;
        TextView productStocks;
        ImageView productImage;
        CheckBox cbxActive;


        public Holder(View view){
            productId = (TextView) view.findViewById(R.id.txtProductId);
            productName = (TextView) view.findViewById(R.id.txtProductName);
            productCostPrice = (TextView) view.findViewById(R.id.txtCostPrice);
            productSellPrice = (TextView) view.findViewById(R.id.txtSellPrice);
            productStocks = (TextView) view.findViewById(R.id.txtStocks);
            productImage = (ImageView) view.findViewById(R.id.imageProduct);
            cbxActive = (CheckBox) view.findViewById(R.id.cbxActive);

        }
    }
}
