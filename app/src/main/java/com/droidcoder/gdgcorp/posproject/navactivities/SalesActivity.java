package com.droidcoder.gdgcorp.posproject.navactivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.Adapter.ItemPagerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.MenuPagerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.OrderProductRecyclerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SpinnerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SubPagerAdapter;
import com.droidcoder.gdgcorp.posproject.BaseCompatActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.barcode.BarcodeCaptureActivity;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.dataentity.CustomerDao;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProductDao;
import com.droidcoder.gdgcorp.posproject.datasystem.CurrentUser;
import com.droidcoder.gdgcorp.posproject.fragments.CancelAllFragment;
import com.droidcoder.gdgcorp.posproject.fragments.CustomerFormFragment;
import com.droidcoder.gdgcorp.posproject.fragments.DiscountAllFragment;
import com.droidcoder.gdgcorp.posproject.fragments.EditItemFragment;
import com.droidcoder.gdgcorp.posproject.fragments.ItemOptionFragment;
import com.droidcoder.gdgcorp.posproject.fragments.OnHoldFragment;
import com.droidcoder.gdgcorp.posproject.fragments.PaymentFragment;
import com.droidcoder.gdgcorp.posproject.fragments.PaymentSuccessFragment;
import com.droidcoder.gdgcorp.posproject.fragments.RetrieveFragment;
import com.droidcoder.gdgcorp.posproject.fragments.SubOptionFragment;
import com.droidcoder.gdgcorp.posproject.fragments.TaxAllFragment;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/**
 * Created by DanLuciano on 2/14/2017.
 */

public class SalesActivity extends BaseCompatActivity implements OrderProductRecyclerAdapter.OnSummaryEdit, EditItemFragment.OnEditItem{

    @BindView(R.id.menuPager)ViewPager menuPager;
    @BindView(R.id.tabMenuDots)TabLayout tabMenuDots;
    @BindView(R.id.subPager)ViewPager subPager;
    @BindView(R.id.tabSubDots)TabLayout tabSubDots;
    @BindView(R.id.itemPager)ViewPager itemPager;
    @BindView(R.id.tabItemDots)TabLayout tabItemDots;

    @BindView(R.id.btnCancelAll)Button btnCancelAll;
    @BindView(R.id.btnHold)Button btnHold;
    @BindView(R.id.btnRetrieve)Button btnRetrieve;
    @BindView(R.id.btnDiscountAll)Button btnDiscountAll;
    @BindView(R.id.btnTaxExemptAll)Button btnTaxExemptAll;
    @BindView(R.id.btnPayment)Button btnPayment;
    @BindView(R.id.btnReturn)Button btnReturn;

    @BindView(R.id.txtVatSales)TextView txtTotalVatSales;
    @BindView(R.id.txtNonVatSales)TextView txtTotalNonVatSales;
    @BindView(R.id.txtVatExempt)TextView txtTotalVatExempt;
    @BindView(R.id.txtDiscount)TextView txtTotalDiscount;
    @BindView(R.id.txtVat)TextView txtTotalVat;
    @BindView(R.id.txtTotal)TextView txtTotal;

    @BindView(R.id.txtReceiptId)TextView txtReceiptId;

    @BindView(R.id.txtMenuIndicator)TextView txtMenuIndicator;
    @BindView(R.id.txtSubIndicator)TextView txtSubIndicator;

    @BindView(R.id.btnAddCustomer)Button btnAddCustomer;
    @BindView(R.id.btnScanCustomer)Button btnScanCustomer;
    @BindView(R.id.btnRemoveCustomer)Button btnRemoveCustomer;
    @BindView(R.id.txtCustomerName)TextView txtCustomerName;

    @BindView(R.id.rvOrder)RecyclerView rvOrder;
    @BindView(R.id.search)AutoCompleteTextView search;

    @BindView(R.id.rightFrame)FrameLayout rightFrame;

    MenuPagerAdapter menuPagerAdapter;
    SubPagerAdapter subPagerAdapter;
    ItemPagerAdapter itemPagerAdapter;

    OrderProductRecyclerAdapter orderProductRecyclerAdapter;
    LinearLayoutManager layoutManager;

    FragmentManager fm;
    PaymentFragment paymentFragment = null;

    OrderReceipt orderReceipt = null;
    Customer customer = null;

    RetrieveFragment retrieveFragment = null;

    public static final int BARCODE_READER_REQUEST_CODE = 143;

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

        orderReceipt = new OrderReceipt();

        List<String> spinnerList = new ArrayList<>();

        for(Product product : DBHelper.getDaoSession().getProductDao().queryBuilder()
                .where(ProductDao.Properties.Deleted.isNull())
                .orderAsc(ProductDao.Properties.Name).list()){

            spinnerList.add(product.getName());

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerList);
        search.setThreshold(1);
        search.setAdapter(arrayAdapter);

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

                String itemName = ((TextView)view).getText().toString().toUpperCase();

                long productId = DBHelper.getDaoSession().getProductDao().queryBuilder()
                        .where(ProductDao.Properties.Name.eq(itemName))
                        .where(ProductDao.Properties.Deleted.isNull())
                        .list().get(0).getId();

                addOrderProduct(productId);
                search.setText("");
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderProductRecyclerAdapter.getOrderProductList().size() > 0 ){

                    Toast.makeText(SalesActivity.this, "Unable to return, order is not empty", Toast.LENGTH_LONG).show();

                }else if(!txtReceiptId.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(SalesActivity.this, "Unable to return, receipt id is not empty, cancel or hold the current transaction first", Toast.LENGTH_LONG).show();

                }else{
                    SalesActivity.super.onBackPressed();
                }
            }
        });

        btnCancelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((fm.findFragmentByTag("editItemFragment") != null && (fm.findFragmentByTag("editItemFragment").isVisible()))){

                    Toast.makeText(SalesActivity.this, "Cannot process delete while edit form is not hidden", Toast.LENGTH_SHORT).show();

                }else if(fm.findFragmentByTag("paymentFragment") != null && fm.findFragmentByTag("paymentFragment").isVisible()){

                    Toast.makeText(SalesActivity.this, "Cannot process delete while payment form is not hidden", Toast.LENGTH_SHORT).show();

                }else{
                    if(orderProductRecyclerAdapter.getItemCount() > 0 && txtReceiptId.getText().toString().equalsIgnoreCase("")){
                        CancelAllFragment cancelAllFragment = new CancelAllFragment();
                        Bundle bundle = new Bundle();
                        bundle.putLong("id", 0);
                        cancelAllFragment.setArguments(bundle);
                        cancelAllFragment.show(getSupportFragmentManager(), "cancelAllFragment");
                    }else if(!txtReceiptId.getText().toString().equalsIgnoreCase("")){
                        CancelAllFragment cancelAllFragment = new CancelAllFragment();
                        Bundle bundle = new Bundle();
                        bundle.putLong("id", orderReceipt.getId());
                        cancelAllFragment.setArguments(bundle);
                        cancelAllFragment.show(getSupportFragmentManager(), "cancelAllFragment");
                    }else{
                        Toast.makeText(SalesActivity.this, "No transaction to process", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((fm.findFragmentByTag("editItemFragment") != null && (fm.findFragmentByTag("editItemFragment").isVisible()))){
                    Toast.makeText(SalesActivity.this, "Cannot process to on hold while edit form is not hidden", Toast.LENGTH_SHORT).show();

                }else if(fm.findFragmentByTag("paymentFragment") != null && fm.findFragmentByTag("paymentFragment").isVisible()){

                    Toast.makeText(SalesActivity.this, "Cannot process to on hold while payment form is not hidden", Toast.LENGTH_SHORT).show();

                }else{
                    if(orderProductRecyclerAdapter.getItemCount() > 0){
                        boolean isValid = true;
                        String result = checkProductAvailability(orderProductRecyclerAdapter.getOrderProductList());

                        if(!result.equalsIgnoreCase("")){
                            Toast.makeText(SalesActivity.this, result, Toast.LENGTH_LONG).show();
                            isValid = false;
                        }

                        if(isValid){
                            OnHoldFragment onHoldFragment = new OnHoldFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("receiptId", txtReceiptId.getText().toString());
                            onHoldFragment.setArguments(bundle);
                            onHoldFragment.show(getSupportFragmentManager(), "onHold");
                        }

                    }else{
                        Toast.makeText(SalesActivity.this, "No transaction to process", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderProductRecyclerAdapter.getOrderProductList().size() > 0 ){

                    Toast.makeText(SalesActivity.this, "Unable to retrieve, order is not empty", Toast.LENGTH_LONG).show();

                }else if(!txtReceiptId.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(SalesActivity.this, "Unable to retrieve, receipt id is not empty, cancel or hold the current transaction first", Toast.LENGTH_LONG).show();

                }else{
                    retrieveFragment = new RetrieveFragment();
                    retrieveFragment.show(getSupportFragmentManager(), "retrieveFragment");
                }

            }
        });

        btnDiscountAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((fm.findFragmentByTag("editItemFragment") != null && (fm.findFragmentByTag("editItemFragment").isVisible()))){

                    Toast.makeText(SalesActivity.this, "Cannot process discount while edit form is not hidden", Toast.LENGTH_SHORT).show();

                }else if(fm.findFragmentByTag("paymentFragment") != null && fm.findFragmentByTag("paymentFragment").isVisible()){

                    Toast.makeText(SalesActivity.this, "Cannot process discount while payment form is not hidden", Toast.LENGTH_SHORT).show();

                }else{
                    if(orderProductRecyclerAdapter.getItemCount() > 0){
                        DiscountAllFragment discountAllFragment = new DiscountAllFragment();
                        discountAllFragment.show(getSupportFragmentManager(), "discountAll");
                    }else{
                        Toast.makeText(SalesActivity.this, "No transaction to process", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        btnTaxExemptAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((fm.findFragmentByTag("editItemFragment") != null && (fm.findFragmentByTag("editItemFragment").isVisible()))){

                    Toast.makeText(SalesActivity.this, "Cannot process tax exemption while edit form is not hidden", Toast.LENGTH_SHORT).show();

                }else if(fm.findFragmentByTag("paymentFragment") != null && fm.findFragmentByTag("paymentFragment").isVisible()){

                    Toast.makeText(SalesActivity.this, "Cannot tax exemption delete while payment form is not hidden", Toast.LENGTH_SHORT).show();

                } else{
                    if(orderProductRecyclerAdapter.getItemCount() > 0){
                        TaxAllFragment taxAllFragment = new TaxAllFragment();
                        taxAllFragment.show(getSupportFragmentManager(), "taxAllFragment");
                    }else{
                        Toast.makeText(SalesActivity.this, "No transaction to process", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;
                String result = checkProductAvailability(orderProductRecyclerAdapter.getOrderProductList());

                if(orderProductRecyclerAdapter.getOrderProductList().size() > 0){

                    if(!result.equalsIgnoreCase("")){
                        Toast.makeText(SalesActivity.this, result, Toast.LENGTH_LONG).show();
                        isValid = false;
                    }

                    if(isValid){
                        if(paymentFragment == null || !paymentFragment.isVisible()){
                            paymentFragment = new PaymentFragment();
                            FragmentTransaction transaction = fm.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putDouble("total", orderReceipt.getTotalDeductedPrice());
                            bundle.putDouble("serviceCharge", orderReceipt.getServiceChargeValue());
                            paymentFragment.setArguments(bundle);
                            transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
                            transaction.replace(rightFrame.getId(), paymentFragment, "paymentFragment").commit();
                        }
                    }

                }else{
                    Toast.makeText(SalesActivity.this, "No transaction to process", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerFormFragment customerFormFragment = new CustomerFormFragment();
                Bundle bundle = new Bundle();
                customerFormFragment.setArguments(bundle);
                customerFormFragment.show(fm, "customerForm");
            }
        });

        btnScanCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

        btnRemoveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer = null;
                txtCustomerName.setText("Walk-in Customer");
                btnScanCustomer.setVisibility(View.VISIBLE);
                btnRemoveCustomer.setVisibility(View.GONE);
            }
        });

    }

    public void retrieveFromOnhold(long id){

        OrderReceipt retrievedReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(id);

        orderReceipt = retrievedReceipt;
        orderReceipt.setId(retrievedReceipt.getId());
        if(orderReceipt.getCustomerId() > 0){
            customer = DBHelper.getDaoSession().getCustomerDao().load(orderReceipt.getCustomerId());
            txtCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
            btnScanCustomer.setVisibility(View.GONE);
            btnRemoveCustomer.setVisibility(View.VISIBLE);
        }else{
            customer = null;
            txtCustomerName.setText("Walk-in Customer");
            btnScanCustomer.setVisibility(View.VISIBLE);
            btnRemoveCustomer.setVisibility(View.GONE);
        }

        txtReceiptId.setText(retrievedReceipt.getReceiptId() + "");

        ArrayList<OrderProduct> orderProductList = (ArrayList<OrderProduct>)retrievedReceipt.getOrderProducts();

        orderProductRecyclerAdapter.setOrderProductList(orderProductList);
        orderProductRecyclerAdapter.notifyDataSetChanged();
        rvOrder.invalidate();

        updateComputation(orderProductRecyclerAdapter.getOrderProductList());

        if(retrieveFragment != null && retrieveFragment.isVisible()) {
            retrieveFragment.dismiss();
        }

    }

    public void cancelAll(long id){
        orderProductRecyclerAdapter.setOrderProductList(new ArrayList<OrderProduct>());
        orderProductRecyclerAdapter.notifyDataSetChanged();
        rvOrder.invalidate();
        updateComputation(orderProductRecyclerAdapter.getOrderProductList());

        if(id > 0){
            DBHelper.getDaoSession().getOrderReceiptDao().deleteByKey(id);
            DBHelper.getDaoSession().getOrderProductDao()
                    .deleteInTx(DBHelper.getDaoSession().getOrderProductDao().queryBuilder().where(OrderProductDao.Properties.OrderReceiptId.eq(id)).list());
        }

        txtReceiptId.setText("");
    }

    public void saveToOnHold(String receiptIdentification){

        boolean isValid = true;
        boolean isSave = txtReceiptId.getText().toString().equalsIgnoreCase("");

        if(isValid){

            if(isSave){

                OrderReceipt onHoldReceipt = new OrderReceipt();
                onHoldReceipt.setCreated(new Date());
                onHoldReceipt.setDeleted(null);
                onHoldReceipt.setOnHold(true);
                onHoldReceipt.setIsPaid(false);
                onHoldReceipt.setPaymentType("");
                onHoldReceipt.setReceiptIdentification(receiptIdentification);
                onHoldReceipt.setVoidBy("");
                onHoldReceipt.setPaidDate(null);
                onHoldReceipt.setUserId(CurrentUser.getUser().getId());
                if(customer != null){
                    onHoldReceipt.setCustomerId(customer.getId());
                }else{
                    onHoldReceipt.setCustomerId(0);
                }

                onHoldReceipt.setTotalDeductedPrice(orderReceipt.getTotalDeductedPrice());
                onHoldReceipt.setTotalCostPrice(orderReceipt.getTotalCostPrice());
                onHoldReceipt.setTotalDiscount(orderReceipt.getTotalDiscount());
                onHoldReceipt.setTotalNonVatSales(orderReceipt.getTotalNonVatSales());
                onHoldReceipt.setTotalVatSales(orderReceipt.getTotalVatSales());
                onHoldReceipt.setTotalVat(orderReceipt.getTotalVat());
                onHoldReceipt.setTotalTaxExempt(orderReceipt.getTotalTaxExempt());
                onHoldReceipt.setTaxValue(orderReceipt.getTaxValue());
                onHoldReceipt.setServiceChargeValue(orderReceipt.getServiceChargeValue());

                long id = DBHelper.getDaoSession().getOrderReceiptDao().insert(onHoldReceipt);
                saveOrderToReceipt(id, orderProductRecyclerAdapter.getOrderProductList(), true, false);

                OrderReceipt newReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(id);
                Calendar calendar = Calendar.getInstance();
                String receiptId = "";
                receiptId += (calendar.get(Calendar.MONTH) + 1) + "" + calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.YEAR) + newReceipt.getId();
                newReceipt.setReceiptId(Long.parseLong(receiptId));

                DBHelper.getDaoSession().getOrderReceiptDao().update(newReceipt);

            }else{

                OrderReceipt onHoldReceipt = orderReceipt;
                onHoldReceipt.setDeleted(null);
                onHoldReceipt.setOnHold(true);
                onHoldReceipt.setIsPaid(false);
                onHoldReceipt.setPaymentType("");
                onHoldReceipt.setReceiptIdentification(receiptIdentification);
                onHoldReceipt.setVoidBy("");
                onHoldReceipt.setUserId(CurrentUser.getUser().getId());
                if(customer != null){
                    onHoldReceipt.setCustomerId(customer.getId());
                }else{
                    onHoldReceipt.setCustomerId(0);
                }

                onHoldReceipt.setTotalDeductedPrice(orderReceipt.getTotalDeductedPrice());
                onHoldReceipt.setTotalCostPrice(orderReceipt.getTotalCostPrice());
                onHoldReceipt.setTotalDiscount(orderReceipt.getTotalDiscount());
                onHoldReceipt.setTotalNonVatSales(orderReceipt.getTotalNonVatSales());
                onHoldReceipt.setTotalVatSales(orderReceipt.getTotalVatSales());
                onHoldReceipt.setTotalVat(orderReceipt.getTotalVat());
                onHoldReceipt.setTotalTaxExempt(orderReceipt.getTotalTaxExempt());
                onHoldReceipt.setTaxValue(orderReceipt.getTaxValue());
                onHoldReceipt.setServiceChargeValue(orderReceipt.getServiceChargeValue());

                DBHelper.getDaoSession().getOrderReceiptDao().update(onHoldReceipt);
                saveOrderToReceipt(orderReceipt.getId(), orderProductRecyclerAdapter.getOrderProductList(), false, false);

            }

            txtReceiptId.setText("");
            txtCustomerName.setText("Walk-in Customer");
            btnScanCustomer.setVisibility(View.VISIBLE);
            btnRemoveCustomer.setVisibility(View.GONE);

        }

    }

    public void saveToSales(String paymentType, double tender, double scTotal){

        boolean isValid = true;
        boolean isSave = txtReceiptId.getText().toString().equalsIgnoreCase("");

        if(isValid) {


            PaymentSuccessFragment paymentSuccessFragment = new PaymentSuccessFragment();
            Bundle bundle = new Bundle();
            bundle.putString("total", StringConverter.doubleFormatter(orderReceipt.getTotalDeductedPrice() + scTotal));
            bundle.putString("change", StringConverter.doubleFormatter(tender - (orderReceipt.getTotalDeductedPrice() + scTotal)));
            if(customer != null){
                bundle.putString("email", customer.getEmail());
            }else{
                bundle.putString("email", "");
            }
            paymentSuccessFragment.setArguments(bundle);
            paymentSuccessFragment.show(fm, "paymentSuccess");

            if (isSave) {

                OrderReceipt onHoldReceipt = new OrderReceipt();
                onHoldReceipt.setCreated(new Date());
                onHoldReceipt.setDeleted(null);
                onHoldReceipt.setOnHold(false);
                if(paymentType.equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CREDIT)){
                    onHoldReceipt.setIsPaid(false);
                }else{
                    onHoldReceipt.setIsPaid(true);
                    onHoldReceipt.setPaidDate(new Date());
                }
                onHoldReceipt.setPaymentType(paymentType);
                onHoldReceipt.setReceiptIdentification("");
                onHoldReceipt.setVoidBy("");
                onHoldReceipt.setUserId(CurrentUser.getUser().getId());
                if(customer != null){
                    onHoldReceipt.setCustomerId(customer.getId());

                    //customer points computation if payment is cash and customer feature is enabled
                    if(!paymentType.equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_POINTS)
                            && !LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_FEATURE).equals("0")){

                        double purchaseSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE));
                        double rewardSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE_POINTS));
                        onHoldReceipt.setRewardSetting(rewardSetting);
                        onHoldReceipt.setPurchaseSetting(purchaseSetting);

                        double totalPurchase = orderReceipt.getTotalDeductedPrice() + scTotal;
                        double points = 0;

                        points = ((totalPurchase / purchaseSetting) * rewardSetting);
                        Toast.makeText(this, "Points accumulate " + points, Toast.LENGTH_SHORT).show();

                        Customer customerRewarded = DBHelper.getDaoSession().getCustomerDao().load(customer.getId());
                        customerRewarded.setPoints(customerRewarded.getPoints() + points);
                        DBHelper.getDaoSession().getCustomerDao().update(customerRewarded);

                    }else{

                        double purchaseSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE));
                        double rewardSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE_POINTS));
                        onHoldReceipt.setRewardSetting(rewardSetting);
                        onHoldReceipt.setPurchaseSetting(purchaseSetting);

                        double totalPurchase = orderReceipt.getTotalDeductedPrice() + scTotal;
                        double points = 0;

                        points = ((totalPurchase / purchaseSetting) * rewardSetting);
                        Toast.makeText(this, "Points accumulate " + points, Toast.LENGTH_SHORT).show();

                        Customer customerClaim = DBHelper.getDaoSession().getCustomerDao().load(customer.getId());
                        customerClaim.setPoints(customerClaim.getPoints() - totalPurchase);
                        DBHelper.getDaoSession().getCustomerDao().update(customerClaim);

                    }
                }else{
                    onHoldReceipt.setCustomerId(0);
                }

                onHoldReceipt.setCashTender(tender);
                onHoldReceipt.setServiceChargeTotal(scTotal);
                onHoldReceipt.setTotalDeductedPrice(orderReceipt.getTotalDeductedPrice());
                onHoldReceipt.setTotalCostPrice(orderReceipt.getTotalCostPrice());
                onHoldReceipt.setTotalDiscount(orderReceipt.getTotalDiscount());
                onHoldReceipt.setTotalNonVatSales(orderReceipt.getTotalNonVatSales());
                onHoldReceipt.setTotalVatSales(orderReceipt.getTotalVatSales());
                onHoldReceipt.setTotalVat(orderReceipt.getTotalVat());
                onHoldReceipt.setTotalTaxExempt(orderReceipt.getTotalTaxExempt());
                onHoldReceipt.setTaxValue(orderReceipt.getTaxValue());
                onHoldReceipt.setServiceChargeValue(orderReceipt.getServiceChargeValue());

                long id = DBHelper.getDaoSession().getOrderReceiptDao().insert(onHoldReceipt);
                saveOrderToReceipt(id, orderProductRecyclerAdapter.getOrderProductList(), true, true);

                OrderReceipt newReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(id);
                Calendar calendar = Calendar.getInstance();
                String receiptId = "";
                receiptId += (calendar.get(Calendar.MONTH) + 1) + "" + calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.YEAR) + newReceipt.getId();
                newReceipt.setReceiptId(Long.parseLong(receiptId));

                DBHelper.getDaoSession().getOrderReceiptDao().update(newReceipt);

            } else {

                OrderReceipt onHoldReceipt = orderReceipt;
                onHoldReceipt.setDeleted(null);
                onHoldReceipt.setOnHold(false);
                if(paymentType.equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CREDIT)){
                    onHoldReceipt.setIsPaid(false);
                }else{
                    onHoldReceipt.setIsPaid(true);
                    onHoldReceipt.setPaidDate(new Date());
                }
                onHoldReceipt.setPaymentType(paymentType);
                onHoldReceipt.setVoidBy("");
                onHoldReceipt.setUserId(CurrentUser.getUser().getId());
                if(customer != null){
                    onHoldReceipt.setCustomerId(customer.getId());

                    //customer points computation if payment is cash and customer fature is enabled
                    if(!paymentType.equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_POINTS)
                            && !LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_FEATURE).equals("0")){

                        double purchaseSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE));
                        double rewardSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE_POINTS));
                        onHoldReceipt.setRewardSetting(rewardSetting);
                        onHoldReceipt.setPurchaseSetting(purchaseSetting);

                        double totalPurchase = orderReceipt.getTotalDeductedPrice() + scTotal;
                        double points = 0;

                        points = ((totalPurchase / purchaseSetting) * rewardSetting);
                        Toast.makeText(this, "Points accumulate " + points, Toast.LENGTH_SHORT).show();

                        Customer customerRewarded = DBHelper.getDaoSession().getCustomerDao().load(customer.getId());
                        customerRewarded.setPoints(customerRewarded.getPoints() + points);
                        DBHelper.getDaoSession().getCustomerDao().update(customerRewarded);

                    }else{

                        double purchaseSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE));
                        double rewardSetting = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.CUSTOMER_PURCHASE_POINTS));
                        onHoldReceipt.setRewardSetting(rewardSetting);
                        onHoldReceipt.setPurchaseSetting(purchaseSetting);

                        double totalPurchase = orderReceipt.getTotalDeductedPrice() + scTotal;
                        double points = 0;

                        points = ((totalPurchase / purchaseSetting) * rewardSetting);
                        Toast.makeText(this, "Points accumulate " + points, Toast.LENGTH_SHORT).show();

                        Customer customerClaim = DBHelper.getDaoSession().getCustomerDao().load(customer.getId());
                        customerClaim.setPoints(customerClaim.getPoints() - totalPurchase);
                        DBHelper.getDaoSession().getCustomerDao().update(customerClaim);

                    }
                }else{
                    onHoldReceipt.setCustomerId(0);
                }

                onHoldReceipt.setCashTender(tender);
                onHoldReceipt.setServiceChargeTotal(scTotal);
                onHoldReceipt.setTotalDeductedPrice(orderReceipt.getTotalDeductedPrice());
                onHoldReceipt.setTotalCostPrice(orderReceipt.getTotalCostPrice());
                onHoldReceipt.setTotalDiscount(orderReceipt.getTotalDiscount());
                onHoldReceipt.setTotalNonVatSales(orderReceipt.getTotalNonVatSales());
                onHoldReceipt.setTotalVatSales(orderReceipt.getTotalVatSales());
                onHoldReceipt.setTotalVat(orderReceipt.getTotalVat());
                onHoldReceipt.setTotalTaxExempt(orderReceipt.getTotalTaxExempt());
                onHoldReceipt.setTaxValue(orderReceipt.getTaxValue());
                onHoldReceipt.setServiceChargeValue(orderReceipt.getServiceChargeValue());

                DBHelper.getDaoSession().getOrderReceiptDao().update(onHoldReceipt);
                saveOrderToReceipt(orderReceipt.getId(), orderProductRecyclerAdapter.getOrderProductList(), false, true);

            }

            txtReceiptId.setText("");
            txtCustomerName.setText("Walk-in Customer");
            btnScanCustomer.setVisibility(View.VISIBLE);
            btnRemoveCustomer.setVisibility(View.GONE);

        }
    }

    public void saveOrderToReceipt(long receiptId, ArrayList<OrderProduct> orderProductList, boolean isSave, boolean removeFromProduct){

        for(int x = 0; x < orderProductList.size(); x++){

            orderProductList.get(x).setOrderReceiptId(receiptId);

            Product product = DBHelper.getDaoSession().getProductDao().load(orderProductList.get(x).getProductId());
            product.setStocks(product.getStocks() - orderProductList.get(x).getProductQuantity());

            if(removeFromProduct){
                DBHelper.getDaoSession().getProductDao().update(product);
            }
        }

        if(isSave){
            DBHelper.getDaoSession().getOrderProductDao().insertInTx(orderProductList);
        }else{
            DBHelper.getDaoSession().getOrderProductDao().insertOrReplaceInTx(orderProductList);
        }



        orderProductRecyclerAdapter.setOrderProductList(new ArrayList<OrderProduct>());
        orderProductRecyclerAdapter.notifyDataSetChanged();
        rvOrder.invalidate();
        updateComputation(orderProductRecyclerAdapter.getOrderProductList());

    }

    public String checkProductAvailability(ArrayList<OrderProduct> orderProductList){
        String result = "";

        Map<Long, Double> productMap = new HashMap<>();

        for(OrderProduct orderProduct : orderProductList){

            long key = orderProduct.getProductId();
            double quantity = orderProduct.getProductQuantity();

            if(productMap.containsKey(key)){
                double total = productMap.get(key) + quantity;
                productMap.put(key, total);
            }else{
                productMap.put(key, quantity);
            }
        }

        for(Map.Entry<Long, Double> product: productMap.entrySet()){

            Product prod = DBHelper.getDaoSession().getProductDao().load(product.getKey());

            if(product.getValue() > prod.getStocks()){
                result += "Process failed. (" + prod.getName() + ") stocks is short by (" + StringConverter.doubleFormatter(product.getValue() - prod.getStocks()) + "). \n";
            }

        }

        return result;
    }

    public void taxExemptAll(boolean taxExempt){

        ArrayList<OrderProduct> orderProductList = orderProductRecyclerAdapter.getOrderProductList();

        for(OrderProduct orderProduct : orderProductList){
            orderProduct.setIsTaxExempt(taxExempt);
        }

        orderProductRecyclerAdapter.setOrderProductList(orderProductList);
        orderProductRecyclerAdapter.notifyDataSetChanged();
        rvOrder.invalidate();

        for(int x = 0; x < orderProductRecyclerAdapter.getItemCount(); x++){
            orderProductRecyclerAdapter.calculatePerOrder(x);
            orderProductRecyclerAdapter.notifyItemChanged(x);
        }

        updateComputation(orderProductRecyclerAdapter.getOrderProductList());

        if(taxExempt){
            Toast.makeText(this, "Tax exempt has been applied to all items", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Tax exempt has been removed to all items", Toast.LENGTH_LONG).show();
        }
    }

    public void discountAll(long discountId, String name){
        ArrayList<OrderProduct> orderProductList = orderProductRecyclerAdapter.getOrderProductList();

        for(OrderProduct orderProduct : orderProductList){
            orderProduct.setDiscountId(discountId);
        }

        orderProductRecyclerAdapter.setOrderProductList(orderProductList);
        orderProductRecyclerAdapter.notifyDataSetChanged();
        rvOrder.invalidate();

        for(int x = 0; x < orderProductRecyclerAdapter.getItemCount(); x++){
            orderProductRecyclerAdapter.calculatePerOrder(x);
            orderProductRecyclerAdapter.notifyItemChanged(x);
        }

        updateComputation(orderProductRecyclerAdapter.getOrderProductList());

        if(discountId == 0){
            Toast.makeText(this, "Discount has been removed to all items", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, name + " discount has been applied to all items", Toast.LENGTH_LONG).show();
        }
    }

    public void addOrderProduct(long id){
        double serviceCharge = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.SC_FILE));

        Product product = DBHelper.getDaoSession().getProductDao().load(id);
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(product.getId());
        orderProduct.setProductName(product.getName());
        orderProduct.setProductImage(product.getImage());
        orderProduct.setProductQuantity(1.00);
        orderProduct.setProductCostPrice(product.getCostPrice());
        orderProduct.setProductSellPrice(product.getSellPrice());
        orderProduct.setProductDeductedPrice(product.getSellPrice());
        orderProduct.setTaxValue(Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.TAX_FILE)));
        orderProduct.setServiceCharge(serviceCharge);
        orderProduct.setDiscountId(0);
        orderProduct.setIsDiscountPercent(false);
        orderProduct.setDiscountValue(0);
        orderProduct.setDiscountTotal(0);
        orderProduct.setProductRemarks(product.getDescription());
        orderProduct.setNote("");
        orderProductRecyclerAdapter.addItem(orderProduct);
        orderProductRecyclerAdapter.notifyItemInserted(0);
        orderProductRecyclerAdapter.notifyItemRangeChanged(0, orderProductRecyclerAdapter.getItemCount());
        layoutManager.scrollToPosition(0);
        layoutManager.findFirstCompletelyVisibleItemPosition();

        updateComputation(orderProductRecyclerAdapter.getOrderProductList());
    }

    @Override
    public void updateChanges(int position, String quantity, String note, boolean isTaxExempt, long discountId) {
        orderProductRecyclerAdapter.editItem(position, quantity, note, isTaxExempt, discountId);
        orderProductRecyclerAdapter.notifyItemChanged(position);
        updateComputation(orderProductRecyclerAdapter.getOrderProductList());
    }

    @Override
    public void notifyOrderRemove(int position) {
        orderProductRecyclerAdapter.notifyItemRemoved(position);
        orderProductRecyclerAdapter.notifyItemRangeChanged(0, orderProductRecyclerAdapter.getItemCount());
        updateComputation(orderProductRecyclerAdapter.getOrderProductList());
    }

    @Override
    public void notifyOrderEdit(int position) {
        orderProductRecyclerAdapter.notifyItemChanged(position);
        updateComputation(orderProductRecyclerAdapter.getOrderProductList());
    }

    public void updateComputation(ArrayList<OrderProduct> orderProductList){

        double totalVatSales = 0;
        double totalNonVatSales = 0;
        double totalVatExempt = 0;
        double totalDiscount = 0;
        double totalVat = 0;


        double totalCostPrice = 0;
        double totalSellPrice = 0;
        double total = 0;

        double tax = Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.TAX_FILE));

        for(OrderProduct orderProduct : orderProductList){

            double sellPrice = orderProduct.getProductSellPrice() * orderProduct.getProductQuantity();

            if(orderProduct.getIsTaxExempt()){

                double vat = ((sellPrice / 100) * tax);
                totalVatExempt += vat;
                sellPrice = (sellPrice - vat);//remove the tax first

                if(orderProduct.getDiscountId() > 0) {

                    if (orderProduct.getIsDiscountPercent()) {

                        totalDiscount += ((sellPrice / 100) * orderProduct.getDiscountValue());
                        sellPrice = (sellPrice - ((sellPrice / 100) * orderProduct.getDiscountValue()));

                    } else {

                        totalDiscount += (orderProduct.getDiscountValue() * orderProduct.getProductQuantity());
                        sellPrice = (sellPrice - (orderProduct.getDiscountValue() * orderProduct.getProductQuantity()));

                        if(sellPrice <= 0){
                            sellPrice = 0;
                        }

                    }

                }

                if((sellPrice) > 0){
                    totalNonVatSales += (sellPrice);
                }

            }else{

                totalVat += ((sellPrice / 100) * tax);
                double vat = ((sellPrice / 100) * tax);

                if(orderProduct.getDiscountId() > 0) {

                    if (orderProduct.getIsDiscountPercent()) {

                        totalDiscount += ((sellPrice / 100) * orderProduct.getDiscountValue());
                        sellPrice = (sellPrice - ((sellPrice / 100) * orderProduct.getDiscountValue()));

                    } else {

                        totalDiscount += (orderProduct.getDiscountValue() * orderProduct.getProductQuantity());
                        sellPrice = (sellPrice - (orderProduct.getDiscountValue() * orderProduct.getProductQuantity()));

                        if(sellPrice <= 0){
                            sellPrice = 0;
                        }

                    }

                }

                totalVatSales += (sellPrice - vat);

            }

            total += sellPrice;
            totalCostPrice += orderProduct.getProductCostPrice();
            totalSellPrice += orderProduct.getProductSellPrice();

        }

        txtTotalVatSales.setText(StringConverter.doubleFormatter(totalVatSales));
        txtTotalNonVatSales.setText(StringConverter.doubleFormatter(totalNonVatSales));
        txtTotalVatExempt.setText(StringConverter.doubleFormatter(totalVatExempt));
        txtTotalDiscount.setText(StringConverter.doubleFormatter(totalDiscount));
        txtTotalVat.setText(StringConverter.doubleFormatter(totalVat));
        txtTotal.setText(StringConverter.doubleFormatter(total));

        orderReceipt.setTotalDeductedPrice(total);
        orderReceipt.setTotalCostPrice(totalCostPrice);
        orderReceipt.setTotalDiscount(totalDiscount);
        orderReceipt.setTotalNonVatSales(totalNonVatSales);
        orderReceipt.setTotalVatSales(totalVatSales);
        orderReceipt.setTotalVat(totalVat);
        orderReceipt.setTotalTaxExempt(totalVatExempt);
        orderReceipt.setTaxValue(tax);
        orderReceipt.setServiceChargeValue(Double.parseDouble(LFHelper.getLocalData(this, GlobalConstants.SC_FILE)));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;

                    customer = DBHelper.getDaoSession().getCustomerDao().queryBuilder().where(CustomerDao.Properties.Code.eq(barcode.displayValue)).unique();

                    if(customer != null){
                        txtCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
                        btnScanCustomer.setVisibility(View.GONE);
                        btnRemoveCustomer.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this, "invalid customer code", Toast.LENGTH_LONG).show();

                    }

                } else Toast.makeText(this, "" + R.string.no_barcode_captured, Toast.LENGTH_SHORT).show();
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    public Customer getCustomer(){
        return customer;
    }

    @Override
    public void onBackPressed() {
        if(orderProductRecyclerAdapter.getOrderProductList().size() > 0 ){

            Toast.makeText(SalesActivity.this, "Unable to return, order is not empty", Toast.LENGTH_LONG).show();

        }else if(!txtReceiptId.getText().toString().equalsIgnoreCase("")){

            Toast.makeText(SalesActivity.this, "Unable to return, receipt id is not empty, cancel or hold the current transaction first", Toast.LENGTH_LONG).show();

        }else{
            super.onBackPressed();
        }

    }
}
