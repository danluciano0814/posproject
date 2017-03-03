package com.droidcoder.gdgcorp.posproject.navactivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.droidcoder.gdgcorp.posproject.Adapter.CategorySummaryAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.ProductRecyclerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SubCategoryAdapter;
import com.droidcoder.gdgcorp.posproject.BaseCompatActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Discount;
import com.droidcoder.gdgcorp.posproject.fragments.CategorySummaryFragment;
import com.droidcoder.gdgcorp.posproject.fragments.DiscountFormFragment;
import com.droidcoder.gdgcorp.posproject.fragments.DiscountSummaryFragment;
import com.droidcoder.gdgcorp.posproject.fragments.ProductFormFragment;
import com.droidcoder.gdgcorp.posproject.fragments.ProductSummaryFragment;
import com.droidcoder.gdgcorp.posproject.fragments.SubCategorySummaryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 1/18/2017.
 */

public class InventoryActivity extends BaseCompatActivity implements ProductFormFragment.OnTransactionFinish,
        SubCategoryAdapter.OnSubCategoryRemove, ProductRecyclerAdapter.OnUpdateSubCategory, DiscountFormFragment.OnTransactionFinish{

    @BindView(R.id.main_frame) FrameLayout mainFrame;
    @BindView(R.id.ln_product) LinearLayout lnProduct;
    @BindView(R.id.ln_category) LinearLayout lnCategory;
    @BindView(R.id.ln_sub_category) LinearLayout lnSubCategory;
    @BindView(R.id.ln_discount) LinearLayout lnDiscount;
    @BindView(R.id.toolbar)Toolbar toolbar;

    Fragment productFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);

        toolbar.setTitle("Inventory");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        productFragment = new ProductSummaryFragment();
        getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), productFragment, "productSummary").commit();
        lnProduct.setBackground(getResources().getDrawable(R.drawable.line_below));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectLeftNavigation(View v){

        switch(v.getId()){
            case R.id.ln_product:
                productFragment = new ProductSummaryFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), productFragment, "productSummary").commit();

                lnProduct.setBackground(getResources().getDrawable(R.drawable.line_below));
                lnCategory.setBackground(null);
                lnSubCategory.setBackground(null);
                lnDiscount.setBackground(null);
                break;

            case R.id.ln_category:
                productFragment = new CategorySummaryFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), productFragment, "categorySummary").commit();

                lnProduct.setBackground(null);
                lnCategory.setBackground(getResources().getDrawable(R.drawable.line_below));
                lnSubCategory.setBackground(null);
                lnDiscount.setBackground(null);
                break;

            case R.id.ln_sub_category:
                productFragment = new SubCategorySummaryFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), productFragment, "subCategorySummary").commit();

                lnProduct.setBackground(null);
                lnCategory.setBackground(null);
                lnSubCategory.setBackground(getResources().getDrawable(R.drawable.line_below));
                lnDiscount.setBackground(null);
                break;

            case R.id.ln_discount:
                productFragment = new DiscountSummaryFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), productFragment, "discountSummary").commit();

                lnProduct.setBackground(null);
                lnCategory.setBackground(null);
                lnSubCategory.setBackground(null);
                lnDiscount.setBackground(getResources().getDrawable(R.drawable.line_below));
                break;

        }
    }

    @Override
    public void refresh() {
        if(productFragment != null && productFragment instanceof ProductSummaryFragment){
            ((ProductSummaryFragment)productFragment).refreshList();
        }

        if(productFragment != null && productFragment instanceof DiscountSummaryFragment){
            ((DiscountSummaryFragment)productFragment).refreshList();
        }
    }

    public void refreshCategory() {
        if(productFragment != null && productFragment instanceof CategorySummaryFragment ){
            ((CategorySummaryFragment)productFragment).refreshList();
        }
    }

    @Override
    public void onRefreshSubCategory() {
        if(productFragment != null && productFragment instanceof CategorySummaryFragment ){
            ((CategorySummaryFragment)productFragment).refreshList();
        }

        if(productFragment != null && productFragment instanceof SubCategorySummaryFragment){
            ((SubCategorySummaryFragment)productFragment).populateListView();
        }
    }

    @Override
    public void onUpdateFinish(boolean isAdd) {
        if(productFragment != null && productFragment instanceof SubCategorySummaryFragment ){
            ((SubCategorySummaryFragment)productFragment).refreshForm(isAdd);
        }
    }
}
