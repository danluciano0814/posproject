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
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProductDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 2/7/2017.
 */

public class SubCategorySummaryAdapter extends BaseAdapter {

    List<SubCategory> subCategoryList;
    Context context;

    public SubCategorySummaryAdapter(Context context, List<SubCategory> subCategoryList){
        this.subCategoryList = subCategoryList;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_subcategory_summary, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.subCategoryId.setText(subCategoryList.get(position).getId().toString());
        holder.subCategoryName.setText(subCategoryList.get(position).getName());
        holder.productCount.setText(DBHelper.getDaoSession().getSubCategoryProductDao().queryBuilder()
                .where(SubCategoryProductDao.Properties.SubCategoryId.eq(subCategoryList.get(position).getId())).list().size() + "");

        if(DBHelper.getDaoSession().getCategoryDao().load(subCategoryList.get(position).getCategoryId())!=null){
            holder.categoryName.setText(DBHelper.getDaoSession().getCategoryDao().load(subCategoryList.get(position).getCategoryId()).getName());
        }else{
            holder.categoryName.setText("-N/A-");
        }

        if(subCategoryList.get(position).getDeleted() == null){
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
                        SubCategory subCategory = DBHelper.getDaoSession().getSubCategoryDao().load(subCategoryList.get(position).getId());
                        subCategory.setDeleted(new Date());
                        DBHelper.getDaoSession().getSubCategoryDao().update(subCategory);

                    }else{
                        holder.cbxActive.setChecked(true);
                        SubCategory subCategory = DBHelper.getDaoSession().getSubCategoryDao().load(subCategoryList.get(position).getId());
                        subCategory.setDeleted(null);
                        DBHelper.getDaoSession().getSubCategoryDao().update(subCategory);
                    }
                }

                return true;
            }
        });

        return view;
    }

    class Holder{

        TextView subCategoryId;
        TextView subCategoryName;
        TextView categoryName;
        TextView productCount;
        CheckBox cbxActive;


        public Holder(View view){
            subCategoryId = (TextView) view.findViewById(R.id.txtSubCategoryId);
            subCategoryName = (TextView) view.findViewById(R.id.txtSubCategoryName);
            categoryName = (TextView) view.findViewById(R.id.txtCategoryName);
            productCount = (TextView) view.findViewById(R.id.txtProductCount);
            cbxActive = (CheckBox) view.findViewById(R.id.cbxActive);

        }
    }

}
