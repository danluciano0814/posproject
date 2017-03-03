package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.fragments.EditItemFragment;
import com.droidcoder.gdgcorp.posproject.fragments.PaymentFragment;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DanLuciano on 2/22/2017.
 */

public class OrderProductRecyclerAdapter extends RecyclerView.Adapter<OrderProductRecyclerAdapter.OrderVH>{

    LayoutInflater inflater;
    List<OrderProduct> orderProductList;
    Context context;
    FrameLayout rightFrame;
    EditItemFragment editItemFragment;

    public OrderProductRecyclerAdapter(Context context, FrameLayout rightFrame){

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.orderProductList = new ArrayList<>();
        this.rightFrame = rightFrame;
        editItemFragment = new EditItemFragment();

    }

    @Override
    public OrderProductRecyclerAdapter.OrderVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_order_product, parent, false);
        OrderProductRecyclerAdapter.OrderVH OrderVH = new OrderProductRecyclerAdapter.OrderVH(view);

        return OrderVH;
    }

    @Override
    public void onBindViewHolder(final OrderProductRecyclerAdapter.OrderVH holder, final int position) {

        //holder.txtId.setText(orderProductList.get(position).getId().toString());
        holder.productImage.setImageBitmap(ImageConverter.bytesToBitmap(orderProductList.get(position).getProductImage()));
        holder.txtOrderProductName.setText(orderProductList.get(position).getProductName());
        holder.txtOrderProductQuantity.setText(StringConverter.doubleFormatter(orderProductList.get(position).getProductQuantity()));
        holder.txtOrderProductDiscount.setText("-" + StringConverter.doubleFormatter(orderProductList.get(position).getDiscountTotal()));
        holder.txtOrderProductTotal.setText(StringConverter.doubleFormatter(orderProductList.get(position).getProductDeductedPrice()));
        holder.btnCancelOrderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editItemFragment != null && !editItemFragment.isVisible()){
                    int currentPos = position;
                    orderProductList.remove(position);
                    ((OnItemRemove)context).notifyOrderRemove(currentPos);
                }
            }
        });

        holder.btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editItemFragment != null && !editItemFragment.isVisible()){
                    double quantity = orderProductList.get(position).getProductQuantity();
                    quantity += 1;
                    orderProductList.get(position).setProductQuantity(quantity);
                    holder.txtOrderProductQuantity.setText(StringConverter.doubleFormatter(quantity));
                }
            }
        });

        holder.btnMinusQuantitiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editItemFragment != null && !editItemFragment.isVisible()){
                    double quantity = orderProductList.get(position).getProductQuantity();

                    if(!(quantity <= 1)){
                        quantity -= 1;
                    }
                    orderProductList.get(position).setProductQuantity(quantity);
                    holder.txtOrderProductQuantity.setText(StringConverter.doubleFormatter(quantity));
                }
            }
        });

        holder.orderProductItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((SalesActivity) context).getFragmentManager();
                editItemFragment = new EditItemFragment();
                FragmentTransaction transaction = ((SalesActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
                Bundle bundle = new Bundle();
                bundle.putInt("orderPosition", position);
                bundle.putByteArray("orderImage", orderProductList.get(position).getProductImage());
                bundle.putDouble("orderQuantity", orderProductList.get(position).getProductQuantity());
                bundle.putString("orderName", orderProductList.get(position).getProductName());
                bundle.putString("orderDescription", orderProductList.get(position).getProductRemarks());
                bundle.putBoolean("orderIsTaxExempt", orderProductList.get(position).getIsTaxExempt());
                bundle.putString("note", orderProductList.get(position).getNote());
                if(orderProductList.get(position).getDiscountId() > 0){
                    bundle.putLong("discountId", orderProductList.get(position).getDiscountId());
                }
                editItemFragment.setArguments(bundle);
                transaction.replace(rightFrame.getId(), editItemFragment, "editItemFragment").commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderProductList.size();
    }

    public void setOrderProductList(ArrayList<OrderProduct> orderProductList){
        this.orderProductList = orderProductList;
    }

    public void addItem(OrderProduct orderProduct){
        orderProductList.add(0, orderProduct);
    }

    public void editItem(int position, String quantity, String note, boolean isTaxExempt, long discountId) {
        orderProductList.get(position).setProductQuantity(Double.parseDouble(quantity));
        orderProductList.get(position).setNote(note);
        orderProductList.get(position).setIsTaxExempt(isTaxExempt);
        orderProductList.get(position).setDiscountId(discountId);
    }

    class OrderVH extends RecyclerView.ViewHolder {

        LinearLayout orderProductItem;
        TextView txtId;
        TextView txtOrderProductName;
        TextView txtOrderProductQuantity;
        TextView txtOrderProductTotal;
        TextView txtOrderProductDiscount;
        ImageView productImage;
        ImageView btnMinusQuantitiy;
        ImageView btnAddQuantity;
        ImageView btnCancelOrderProduct;

        public OrderVH(View itemView) {
            super(itemView);

            orderProductItem = (LinearLayout)itemView.findViewById(R.id.orderProductItem);
            txtId = (TextView) itemView.findViewById(R.id.txtId);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            txtOrderProductName = (TextView) itemView.findViewById(R.id.txtOrderProductName);
            txtOrderProductQuantity = (TextView) itemView.findViewById(R.id.txtOrderProductQuantity);
            txtOrderProductTotal = (TextView) itemView.findViewById(R.id.txtOrderProductTotal);
            txtOrderProductDiscount = (TextView) itemView.findViewById(R.id.txtOrderProductDiscount);
            btnMinusQuantitiy = (ImageView) itemView.findViewById(R.id.btnMinusQuantity);
            btnAddQuantity = (ImageView) itemView.findViewById(R.id.btnAddQuantity);
            btnCancelOrderProduct = (ImageView) itemView.findViewById(R.id.btnCancelOrderProduct);

        }


    }

    public interface OnItemRemove{
        void notifyOrderRemove(int position);
    }

}
