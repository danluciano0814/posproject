package com.droidcoder.gdgcorp.posproject.navactivities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.Adapter.ItemPagerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.MenuPagerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.OrderProductRecyclerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SubPagerAdapter;
import com.droidcoder.gdgcorp.posproject.BaseCompatActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProductDao;
import com.droidcoder.gdgcorp.posproject.fragments.EditItemFragment;
import com.droidcoder.gdgcorp.posproject.fragments.ItemOptionFragment;
import com.droidcoder.gdgcorp.posproject.fragments.PaymentFragment;
import com.droidcoder.gdgcorp.posproject.fragments.SubOptionFragment;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/14/2017.
 */

public class SalesActivity extends BaseCompatActivity implements OrderProductRecyclerAdapter.OnItemRemove, EditItemFragment.OnEditItem{

    @BindView(R.id.menuPager)ViewPager menuPager;
    @BindView(R.id.tabMenuDots)TabLayout tabMenuDots;
    @BindView(R.id.subPager)ViewPager subPager;
    @BindView(R.id.tabSubDots)TabLayout tabSubDots;
    @BindView(R.id.itemPager)ViewPager itemPager;
    @BindView(R.id.tabItemDots)TabLayout tabItemDots;

    @BindView(R.id.txtMenuIndicator)TextView txtMenuIndicator;
    @BindView(R.id.txtSubIndicator)TextView txtSubIndicator;

    @BindView(R.id.btnAddCustomer)Button btnAddCustomer;

    @BindView(R.id.rvOrder)RecyclerView rvOrder;

    @BindView(R.id.btnPayment)Button btnPayment;

    @BindView(R.id.rightFrame)FrameLayout rightFrame;

    MenuPagerAdapter menuPagerAdapter;
    SubPagerAdapter subPagerAdapter;
    ItemPagerAdapter itemPagerAdapter;

    OrderProductRecyclerAdapter orderProductRecyclerAdapter;
    LinearLayoutManager layoutManager;

    FragmentManager fm;
    PaymentFragment paymentFragment;

    boolean isPaymentVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales);
        ButterKnife.bind(this);

        txtMenuIndicator.setVisibility(View.INVISIBLE);
        txtSubIndicator.setVisibility(View.INVISIBLE);

        fm = getSupportFragmentManager();

        populateMenu();
        populateOrder();

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPaymentVisible){
                    paymentFragment = new PaymentFragment();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
                    transaction.replace(rightFrame.getId(), paymentFragment, "paymentFragment").commit();
                    isPaymentVisible = true;
                }else{
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
                    transaction.remove(paymentFragment).commit();
                    isPaymentVisible = false;
                }
            }
        });

    }

    public void addOrderProduct(long id){
        Product product = DBHelper.getDaoSession().getProductDao().load(id);
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductName(product.getName());
        orderProduct.setProductImage(product.getImage());
        orderProduct.setProductCostPrice(product.getCostPrice());
        orderProduct.setProductQuantity(1.00);
        orderProduct.setProductDeductedPrice(product.getSellPrice());
        orderProduct.setIsDiscountPercent(false);
        orderProduct.setDiscountId(0);
        orderProduct.setTaxValue(Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.TAX_FILE)));
        orderProduct.setDiscountTotal(0);
        orderProduct.setProductRemarks(product.getDescription());
        orderProduct.setNote("");
        orderProductRecyclerAdapter.addItem(orderProduct);
        orderProductRecyclerAdapter.notifyItemInserted(0);
        orderProductRecyclerAdapter.notifyItemRangeChanged(0, orderProductRecyclerAdapter.getItemCount());
        layoutManager.scrollToPosition(0);
        layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void updateChanges(int position, String quantity, String note, boolean isTaxExempt, long discountId) {
        orderProductRecyclerAdapter.editItem(position, quantity, note, isTaxExempt, discountId);
        orderProductRecyclerAdapter.notifyItemChanged(position);
    }

    @Override
    public void notifyOrderRemove(int position) {
        orderProductRecyclerAdapter.notifyItemRemoved(position);
        orderProductRecyclerAdapter.notifyItemRangeChanged(0, orderProductRecyclerAdapter.getItemCount());
    }

    public void populateOrder(){
        orderProductRecyclerAdapter = new OrderProductRecyclerAdapter(this, rightFrame);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(orderProductRecyclerAdapter);
    }

    public void populateMenu(){
        if(categoryIdList().size() != 0){
            tabMenuDots.setupWithViewPager(menuPager, true);
            menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), this, categoryIdList());
            menuPager.setAdapter(menuPagerAdapter);
        }
    }

    public void populateSubMenu(long id, String color){

        txtMenuIndicator.setVisibility(View.VISIBLE);
        txtSubIndicator.setVisibility(View.INVISIBLE);
        Category category = DBHelper.getDaoSession().getCategoryDao().load(id);
        txtMenuIndicator.setText(category.getName());
        //txtMenuIndicator.setBackgroundColor(Color.parseColor(color));
        txtMenuIndicator.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.DST_IN);

        List<Long> idList = subCategoryIdList(id);
        List<Fragment> fragmentList = new ArrayList<>();

        if(idList.size() != 0){

            int pageCount = idList.size() / 4;
            int idPosition;

            for(int x = 0; x < pageCount; x++){
                idPosition = (x + 1) * 4;

                SubOptionFragment subOptionFragment = new SubOptionFragment();;
                Bundle bundle = new Bundle();
                bundle.putString("color", color);
                bundle.putLong("id1", idList.get(idPosition - 4));
                bundle.putLong("id2", idList.get(idPosition - 3));
                bundle.putLong("id3", idList.get(idPosition - 2));
                bundle.putLong("id4", idList.get(idPosition - 1));
                subOptionFragment.setArguments(bundle);
                fragmentList.add(subOptionFragment);

            }

        }
        tabSubDots.setupWithViewPager(subPager, true);
        subPagerAdapter = new SubPagerAdapter(getSupportFragmentManager(), this, fragmentList);
        subPager.setAdapter(subPagerAdapter);

    }

    public void populateItemMenu(long id, String color){

        txtSubIndicator.setVisibility(View.VISIBLE);
        SubCategory subCategory = DBHelper.getDaoSession().getSubCategoryDao().load(id);
        txtSubIndicator.setText(subCategory.getName());
        //txtSubIndicator.setBackgroundColor(Color.parseColor(color));
        txtSubIndicator.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.DST_IN);

        List<Long> idList = productIdList(id);
        List<Fragment> fragmentList = new ArrayList<>();

        if(idList.size() != 0){

            int pageCount = idList.size() / 8;
            int idPosition;

            for(int x = 0; x < pageCount; x++){
                idPosition = (x + 1) * 8;

                ItemOptionFragment itemOptionFragment = new ItemOptionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("color", color);
                bundle.putLong("id1", idList.get(idPosition - 8));
                bundle.putLong("id2", idList.get(idPosition - 7));
                bundle.putLong("id3", idList.get(idPosition - 6));
                bundle.putLong("id4", idList.get(idPosition - 5));
                bundle.putLong("id5", idList.get(idPosition - 4));
                bundle.putLong("id6", idList.get(idPosition - 3));
                bundle.putLong("id7", idList.get(idPosition - 2));
                bundle.putLong("id8", idList.get(idPosition - 1));
                itemOptionFragment.setArguments(bundle);
                fragmentList.add(itemOptionFragment);

            }

        }
        tabItemDots.setupWithViewPager(itemPager, true);
        itemPagerAdapter = new ItemPagerAdapter(getSupportFragmentManager(), this, fragmentList);
        itemPager.setAdapter(itemPagerAdapter);

    }


    public List<Category> categoryList(){
        return DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Deleted.isNull())
                .orderAsc(CategoryDao.Properties.Name)
                .list();
    }

    public List<Long> categoryIdList(){
        List<Long> ids = new ArrayList<>();
        int idRemainder;

        for(Category category : categoryList()){
            ids.add(category.getId());
        }

        idRemainder = ids.size()%4;

        //add 0 if not divisible by 4
        if(idRemainder != 0 && (idRemainder%4)!=0){
            for(int x = idRemainder; x < 4; x++){
                ids.add(0l);
            }
        }

        return ids;
    }

    public List<SubCategory> subCategoryList(long id){
        return DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                .where(SubCategoryDao.Properties.CategoryId.eq(id))
                .where(SubCategoryDao.Properties.Deleted.isNull())
                .orderAsc(SubCategoryDao.Properties.Name)
                .list();
    }

    public List<Long> subCategoryIdList(long id){
        List<Long> ids = new ArrayList<>();
        int idRemainder;

        for(SubCategory subCategory : subCategoryList(id)){
            ids.add(subCategory.getId());
        }

        idRemainder = ids.size()%4;

        //add 0 if not divisible by 4
        if(idRemainder != 0 && (idRemainder%4)!=0){
            for(int x = idRemainder; x < 4; x++){
                ids.add(0l);
            }
        }

        return ids;
    }

    public List<Product> productList(long id){
        List<Product> productList;

        ArrayList<Long> ids = new ArrayList<>();

        List<SubCategoryProduct> subCategoryProductList = DBHelper.getDaoSession().getSubCategoryProductDao().queryBuilder()
                .where(SubCategoryProductDao.Properties.SubCategoryId.eq(id)).list();

        for(SubCategoryProduct subCategoryProduct : subCategoryProductList){
            ids.add(subCategoryProduct.getProductId());
        }

        productList = DBHelper.getDaoSession().getProductDao().queryBuilder()
                .where(ProductDao.Properties.Id.in(ids))
                .where(ProductDao.Properties.Deleted.isNull())
                .orderAsc(ProductDao.Properties.Name).list();

        return productList;
    }

    public List<Long> productIdList(long id){
        List<Long> ids = new ArrayList<>();
        int idRemainder;

        for(Product product : productList(id)){
            ids.add(product.getId());
        }

        idRemainder = ids.size()%8;

        //add 0 if not divisible by 8
        if(idRemainder != 0 && (idRemainder%8)!=0){
            for(int x = idRemainder; x < 8; x++){
                ids.add(0l);
            }
        }

        return ids;
    }

}
