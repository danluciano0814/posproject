package com.droidcoder.gdgcorp.posproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/20/2017.
 */

public class ItemOptionFragment extends BaseFragment {

    @BindView(R.id.item1)LinearLayout item1;
    @BindView(R.id.item2)LinearLayout item2;
    @BindView(R.id.item3)LinearLayout item3;
    @BindView(R.id.item4)LinearLayout item4;
    @BindView(R.id.item5)LinearLayout item5;
    @BindView(R.id.item6)LinearLayout item6;
    @BindView(R.id.item7)LinearLayout item7;
    @BindView(R.id.item8)LinearLayout item8;

    @BindView(R.id.itemImage1)ImageView itemImage1;
    @BindView(R.id.itemImage2)ImageView itemImage2;
    @BindView(R.id.itemImage3)ImageView itemImage3;
    @BindView(R.id.itemImage4)ImageView itemImage4;
    @BindView(R.id.itemImage5)ImageView itemImage5;
    @BindView(R.id.itemImage6)ImageView itemImage6;
    @BindView(R.id.itemImage7)ImageView itemImage7;
    @BindView(R.id.itemImage8)ImageView itemImage8;

    @BindView(R.id.itemPrice1)TextView itemPrice1;
    @BindView(R.id.itemPrice2)TextView itemPrice2;
    @BindView(R.id.itemPrice3)TextView itemPrice3;
    @BindView(R.id.itemPrice4)TextView itemPrice4;
    @BindView(R.id.itemPrice5)TextView itemPrice5;
    @BindView(R.id.itemPrice6)TextView itemPrice6;
    @BindView(R.id.itemPrice7)TextView itemPrice7;
    @BindView(R.id.itemPrice8)TextView itemPrice8;

    @BindView(R.id.itemName1)TextView itemName1;
    @BindView(R.id.itemName2)TextView itemName2;
    @BindView(R.id.itemName3)TextView itemName3;
    @BindView(R.id.itemName4)TextView itemName4;
    @BindView(R.id.itemName5)TextView itemName5;
    @BindView(R.id.itemName6)TextView itemName6;
    @BindView(R.id.itemName7)TextView itemName7;
    @BindView(R.id.itemName8)TextView itemName8;

    String color = "#1976D2";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_option, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        if(!getArguments().isEmpty()){

            color = getArguments().getString("color");
            populateItemView(getArguments().getLong("id1", 0), item1, itemImage1, itemName1, itemPrice1);
            populateItemView(getArguments().getLong("id2", 0), item2, itemImage2, itemName2, itemPrice2);
            populateItemView(getArguments().getLong("id3", 0), item3, itemImage3, itemName3, itemPrice3);
            populateItemView(getArguments().getLong("id4", 0), item4, itemImage4, itemName4, itemPrice4);
            populateItemView(getArguments().getLong("id5", 0), item5, itemImage5, itemName5, itemPrice5);
            populateItemView(getArguments().getLong("id6", 0), item6, itemImage6, itemName6, itemPrice6);
            populateItemView(getArguments().getLong("id7", 0), item7, itemImage7, itemName7, itemPrice7);
            populateItemView(getArguments().getLong("id8", 0), item8, itemImage8, itemName8, itemPrice8);

        }
    }

    public void populateItemView(final Long id, View item, View itemImage, View itemName, View itemPrice){
        final Product product = DBHelper.getDaoSession().getProductDao().load(id);

        LinearLayout itemMainContainer = (LinearLayout)item;
        ImageView itemImageContainer = (ImageView)itemImage;
        TextView itemTxtName = (TextView)itemName;
        TextView itemTxtPrice = (TextView)itemPrice;

        if(id != 0){
            item.setBackgroundColor(Color.parseColor(color));
            itemTxtName.setText(product.getName());
            itemTxtPrice.setBackgroundColor(Color.parseColor(color));
            itemTxtPrice.setText(StringConverter.doubleFormatter(product.getSellPrice()));
            itemImageContainer.setImageBitmap(ImageConverter.bytesToBitmap(product.getImage()));
            itemMainContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SalesActivity)getActivity()).addOrderProduct(id);
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
