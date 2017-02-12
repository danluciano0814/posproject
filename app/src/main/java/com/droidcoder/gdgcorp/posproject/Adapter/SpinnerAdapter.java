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
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import java.util.List;

/**
 * Created by DanLuciano on 2/12/2017.
 */

public class SpinnerAdapter extends BaseAdapter {

    List<SpinnerItem> spinneItemList;
    Context context;

    public SpinnerAdapter(Context context, List<SpinnerItem> spinneItemList){
        this.spinneItemList = spinneItemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return spinneItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return spinneItemList.get(position);
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
            view = inflater.inflate(R.layout.row_spinner, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.id.setText(spinneItemList.get(position).getId() + "");
        holder.name.setText(spinneItemList.get(position).getName());

        return view;
    }

    class Holder{

        TextView id;
        TextView name;



        public Holder(View view){
            id = (TextView) view.findViewById(R.id.txtId);
            name = (TextView) view.findViewById(R.id.txtName);

        }
    }



}
