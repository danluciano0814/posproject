package com.droidcoder.gdgcorp.posproject.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryDao;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/17/2017.
 */

public class MenuOptionFragment extends BaseFragment {

    @BindView(R.id.item1)LinearLayout item1;
    @BindView(R.id.item2)LinearLayout item2;
    @BindView(R.id.item3)LinearLayout item3;
    @BindView(R.id.item4)LinearLayout item4;

    @BindView(R.id.itemImage1)LinearLayout itemImage1;
    @BindView(R.id.itemImage2)LinearLayout itemImage2;
    @BindView(R.id.itemImage3)LinearLayout itemImage3;
    @BindView(R.id.itemImage4)LinearLayout itemImage4;

    @BindView(R.id.itemName1)TextView itemName1;
    @BindView(R.id.itemName2)TextView itemName2;
    @BindView(R.id.itemName3)TextView itemName3;
    @BindView(R.id.itemName4)TextView itemName4;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_option, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        if(!getArguments().isEmpty()){

            populateItemView(getArguments().getLong("id1", 0), item1, itemImage1, itemName1);
            populateItemView(getArguments().getLong("id2", 0), item2, itemImage2, itemName2);
            populateItemView(getArguments().getLong("id3", 0), item3, itemImage3, itemName3);
            populateItemView(getArguments().getLong("id4", 0), item4, itemImage4, itemName4);

        }
    }

    public void populateItemView(final Long id, View item, View itemImage, View itemName){
        final Category category = DBHelper.getDaoSession().getCategoryDao().load(id);

        LinearLayout itemMainContainer = (LinearLayout)item;
        LinearLayout itemImageContainer = (LinearLayout)itemImage;
        TextView itemTxtName = (TextView)itemName;

        if(id != 0){
            item.setBackgroundColor(Color.parseColor(category.getCategoryColor()));
            itemTxtName.setText(category.getName());
            itemMainContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SalesActivity)getActivity()).populateSubMenu(id, category.getCategoryColor());
                }
            });
        }else{
            itemMainContainer.setVisibility(View.INVISIBLE);
            itemImageContainer.setVisibility(View.INVISIBLE);
            itemTxtName.setVisibility(View.INVISIBLE);
            itemMainContainer.setEnabled(false);
        }

    }


}
