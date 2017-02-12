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
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 2/5/2017.
 */

public class CategorySummaryAdapter extends BaseAdapter {

    List<Category> categoryList;
    Context context;

    public CategorySummaryAdapter(Context context, List<Category> categoryList){
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
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
            view = inflater.inflate(R.layout.row_category_summary, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.categoryId.setText(categoryList.get(position).getId().toString());
        holder.categoryName.setText(categoryList.get(position).getName());
        holder.subCategoryCount.setText(categoryList.get(position).getSubCategories().size() + "");

        if(categoryList.get(position).getDeleted() == null){
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
                        Category category = DBHelper.getDaoSession().getCategoryDao().load(categoryList.get(position).getId());
                        category.setDeleted(new Date());
                        DBHelper.getDaoSession().getCategoryDao().update(category);

                    }else{
                        holder.cbxActive.setChecked(true);
                        Category category = DBHelper.getDaoSession().getCategoryDao().load(categoryList.get(position).getId());
                        category.setDeleted(null);
                        DBHelper.getDaoSession().getCategoryDao().update(category);
                    }
                }

                return true;
            }
        });

        return view;
    }

    class Holder{

        TextView categoryId;
        TextView categoryName;
        TextView subCategoryCount;
        CheckBox cbxActive;


        public Holder(View view){
            categoryId = (TextView) view.findViewById(R.id.txtCategoryId);
            categoryName = (TextView) view.findViewById(R.id.txtCategoryName);
            subCategoryCount = (TextView) view.findViewById(R.id.txtSubCount);
            cbxActive = (CheckBox) view.findViewById(R.id.cbxActive);

        }
    }

}
