package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProductDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.List;

/**
 * Created by DanLuciano on 2/9/2017.
 */

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductVH>{

    LayoutInflater inflater;
    List<Product> productList;
    Context context;
    boolean isAdd;
    long subCategoryId;

    public ProductRecyclerAdapter(Context context, List<Product> productList, boolean isAdd, long subCategoryId){

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.productList = productList;
        this.isAdd = isAdd;
        this.subCategoryId = subCategoryId;
    }

    @Override
    public ProductVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_subcategory_products, parent, false);
        ProductVH productVH = new ProductVH(view);

        return productVH;
    }

    @Override
    public void onBindViewHolder(ProductVH holder, final int position) {

        holder.productId.setText(productList.get(position).getId().toString());
        holder.productName.setText(productList.get(position).getName().toString());
        if(isAdd){
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnRemove.setVisibility(View.GONE);

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SubCategoryProduct subCategoryProduct = new SubCategoryProduct();
                    subCategoryProduct.setSubCategoryId(subCategoryId);
                    subCategoryProduct.setProductId(Long.parseLong(productList.get(position).getId().toString()));
                    DBHelper.getDaoSession().getSubCategoryProductDao().insert(subCategoryProduct);

                    productList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, productList.size());
                    notifyDataSetChanged();
                    ((OnUpdateSubCategory)context).onUpdateFinish(true);
                }
            });
        }else{
            holder.btnAdd.setVisibility(View.GONE);
            holder.btnRemove.setVisibility(View.VISIBLE);

            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SubCategoryProduct subCategoryProduct = DBHelper.getDaoSession().getSubCategoryProductDao().queryBuilder()
                            .where(SubCategoryProductDao.Properties.ProductId.eq(Long.parseLong(productList.get(position).getId().toString())))
                            .where(SubCategoryProductDao.Properties.SubCategoryId.eq(subCategoryId)).list().get(0);

                    DBHelper.getDaoSession().getSubCategoryProductDao().delete(subCategoryProduct);

                    productList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, productList.size());
                    notifyDataSetChanged();
                    ((OnUpdateSubCategory)context).onUpdateFinish(false);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductVH extends RecyclerView.ViewHolder {

        TextView productId;
        TextView productName;
        ImageView btnRemove;
        ImageView btnAdd;

        public ProductVH(View itemView) {
            super(itemView);

            productId = (TextView) itemView.findViewById(R.id.txtProductId);
            productName = (TextView) itemView.findViewById(R.id.txtProductName);
            btnRemove = (ImageView) itemView.findViewById(R.id.btnRemove);
            btnAdd = (ImageView) itemView.findViewById(R.id.btnAdd);

        }


    }

    public interface OnUpdateSubCategory{
        void onUpdateFinish(boolean isAdd);
    }
}

