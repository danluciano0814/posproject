package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.navactivities.InventoryActivity;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.List;

/**
 * Created by DanLuciano on 2/6/2017.
 */

public class SubCategoryAdapter extends BaseAdapter {

    List<SubCategory> subCategoryList;
    Context context;
    long categoryId;

    public SubCategoryAdapter(Context context, List<SubCategory> subCategoryList, long categoryId){
        this.subCategoryList = subCategoryList;
        this.context = context;
        this.categoryId = categoryId;
    }

    @Override
    public int getCount() {
        return subCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return subCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View view = convertView;
        final Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_category_subcategories, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.subCategoryId.setText(subCategoryList.get(position).getId().toString());
        holder.subCategoryName.setText(subCategoryList.get(position).getName());

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubCategory subCategory = DBHelper.getDaoSession().getSubCategoryDao().load(subCategoryList.get(position).getId());
                subCategory.setCategoryId(0);
                DBHelper.getDaoSession().getSubCategoryDao().update(subCategory);
                DBHelper.getDaoSession().getCategoryDao().load(categoryId).resetSubCategories();
                ((InventoryActivity)context).refreshCategory();
                ((OnSubCategoryRemove)context).onRefreshSubCategory();

            }
        });
        return view;
    }

    class Holder{

        TextView subCategoryId;
        TextView subCategoryName;
        ImageView btnRemove;

        public Holder(View view){
            subCategoryId = (TextView) view.findViewById(R.id.txtSubCategoryId);
            subCategoryName = (TextView) view.findViewById(R.id.txtSubCategoryName);
            btnRemove = (ImageView) view.findViewById(R.id.btnRemove);
        }
    }

    public interface OnSubCategoryRemove{
        void onRefreshSubCategory();
    }

}
