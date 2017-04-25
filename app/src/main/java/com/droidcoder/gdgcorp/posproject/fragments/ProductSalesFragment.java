package com.droidcoder.gdgcorp.posproject.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.EmployeeFilterAdapter;
import com.droidcoder.gdgcorp.posproject.BuildConfig;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserDao;
import com.droidcoder.gdgcorp.posproject.datasystem.CurrentUser;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by DanLuciano on 4/19/2017.
 */

public class ProductSalesFragment extends BaseFragment{

    @BindView(R.id.filterContainer)LinearLayout filterContainer;
    @BindView(R.id.lvEmployeeFilter)ListView lvEmployeeFilter;
    @BindView(R.id.cbxAll)CheckBox cbxAll;
    @BindView(R.id.txtSales)TextView txtSales;
    @BindView(R.id.txtProfit)TextView txtProfit;
    @BindView(R.id.txtDiscount)TextView txtDiscount;
    @BindView(R.id.txtSC)TextView txtSC;
    @BindView(R.id.btnExport)Button btnExport;

    //Total
    double totalSales = 0;
    double totalCostPrice = 0;
    double totalDiscount = 0;
    double totalSC = 0;

    private WritableCellFormat times;

    EmployeeFilterAdapter employeeFilterAdapter;

    ProgressFragment progressFragment;

    ArrayList<ArrayList<Object>> objectsList;
    ArrayList<Long> ids;
    String userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_sales, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        userName = CurrentUser.getUser().getFirstName() + " " + CurrentUser.getUser().getLastName();

        try {
            createCellStyle();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        List<User> userList = DBHelper.getDaoSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.Deleted.isNull()).list();
        employeeFilterAdapter = new EmployeeFilterAdapter(getActivity(), userList);
        lvEmployeeFilter.setAdapter(employeeFilterAdapter);

        Fragment fragment = new DateFilterFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(filterContainer.getId(), fragment, "");
        ft.commit();

        cbxAll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(cbxAll.isChecked()){
                        cbxAll.setChecked(false);
                        selectAll(true);
                    }else{
                        cbxAll.setChecked(true);
                        selectAll(false);
                    }
                }

                return true;
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStoragePermissionGranted()){

                    if(ids.size() > 0){
                        new AsyncWriteToExcel("CheapposProductSales", userName,  totalSales, totalCostPrice, totalDiscount, totalSC).execute(objectsList);
                        totalSales = 0;
                        totalCostPrice = 0;
                        totalDiscount = 0;
                        totalSC = 0;
                    }else{
                        Toast.makeText(getActivity(), "SALES REPORT IS EMPTY, PLEASE SELECT AT LEAST 1 EMPLOYEE", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    public void selectAll(boolean isSelectAll){
        List<User> userList = employeeFilterAdapter.getUserList();

        for(User user : userList){
            if(isSelectAll){
                user.setDeleted(new Date());
            }else{
                user.setDeleted(null);
            }
        }

        employeeFilterAdapter.setUserList(userList);
        employeeFilterAdapter.notifyDataSetChanged();
        lvEmployeeFilter.invalidate();
    }

    public void generateReport(Date startDate, Date endDate){

        ids = new ArrayList<>();

        for(User user : employeeFilterAdapter.getUserList()){
            if(user.getDeleted() == null){
                ids.add(user.getId());
            }
        }

        objectsList = fetchOrderProduct(ids, startDate, endDate);

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public ArrayList<ArrayList<Object>> fetchOrderProduct(ArrayList<Long> ids, Date from, Date to){

        List<OrderProduct> orderProductList = new ArrayList<>();
        ArrayList<ArrayList<Object>> objectsList = new ArrayList<ArrayList<Object>>();
        totalCostPrice = 0;
        totalSales = 0;
        totalSC = 0;

        if(ids.size() > 0){
            for(OrderReceipt orderDetails : DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                    .where(OrderReceiptDao.Properties.UserId.in(ids))
                    .where(OrderReceiptDao.Properties.Deleted.isNull())
                    .where(OrderReceiptDao.Properties.Created.between(from, to)).list()){

                orderProductList.addAll(orderDetails.getOrderProducts());
            }
        }else{
            Toast.makeText(getActivity(), "No employee selected", Toast.LENGTH_SHORT).show();
            txtSales.setText(StringConverter.doubleCommaFormatter(0));
            txtProfit.setText(StringConverter.doubleCommaFormatter(0));
            txtDiscount.setText(StringConverter.doubleCommaFormatter(0));
            txtSC.setText(StringConverter.doubleCommaFormatter(0));
        }

        ArrayList<Object> objects = new ArrayList<Object>();
        objects.add("Created");
        objects.add("Cashier");
        objects.add("Payment Type");
        objects.add("Product Name");
        objects.add("Order Quantity");
        objects.add("Total Cost Price");
        objects.add("Total Sell Price");
        objects.add("Total Paid Price");
        objects.add("Total Discount");
        objects.add("Total Service Charge");
        objectsList.add(objects);


        for(OrderProduct orderProduct : orderProductList){
            double sc = (orderProduct.getProductDeductedPrice() / 100) * orderProduct.getServiceCharge();

            totalCostPrice += orderProduct.getProductCostPrice();
            totalSales += orderProduct.getProductDeductedPrice() + sc;
            totalSC += sc;

            User user = DBHelper.getDaoSession().getUserDao().load(DBHelper.getDaoSession().getOrderReceiptDao().load(orderProduct.getOrderReceiptId()).getUserId());
            OrderReceipt orderReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(orderProduct.getOrderReceiptId());

            objects = new ArrayList<Object>();
            objects.add(orderProduct.getCreated());
            objects.add(user.getFirstName() + " " + user.getLastName());
            objects.add(orderReceipt.getPaymentType());
            objects.add(orderProduct.getProductName());
            objects.add(orderProduct.getProductQuantity());
            objects.add(orderProduct.getProductCostPrice());
            objects.add(orderProduct.getProductSellPrice());
            objects.add(orderProduct.getProductDeductedPrice());
            objects.add(orderProduct.getDiscountTotal());
            objects.add(sc);
            objectsList.add(objects);

            totalDiscount += orderProduct.getDiscountTotal();

            txtSales.setText(StringConverter.doubleCommaFormatter(totalSales));
            txtProfit.setText(StringConverter.doubleCommaFormatter(totalSales - totalCostPrice));
            txtDiscount.setText(StringConverter.doubleCommaFormatter(totalDiscount));
            txtSC.setText(StringConverter.doubleCommaFormatter(totalSC));
        }

        return objectsList;

    }

    public void addTitle(WritableSheet sheet, int startColumn, int endColumn, int row, String s)
            throws RowsExceededException, WriteException {

        WritableFont fontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
        WritableCellFormat Cellstatus = new WritableCellFormat(fontStatus);

        Cellstatus.setWrap(true);
        Cellstatus.setBackground(Colour.BLUE_GREY);
        Cellstatus.setAlignment(jxl.format.Alignment.CENTRE);
        Cellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Cellstatus.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.MEDIUM, Colour.WHITE);
        sheet.mergeCells(startColumn, row, endColumn, row + 1);
        Label label;
        label = new Label(startColumn, row, s, Cellstatus);
        sheet.addCell(label);

    }

    public void addDetails(WritableSheet sheet, int startColumn, int endColumn, int row, String s)
            throws RowsExceededException, WriteException {

        WritableFont fontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
        WritableCellFormat Cellstatus = new WritableCellFormat(fontStatus);

        Cellstatus.setWrap(true);
        Cellstatus.setBackground(Colour.GRAY_50);
        Cellstatus.setAlignment(jxl.format.Alignment.CENTRE);
        Cellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Cellstatus.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.MEDIUM, Colour.WHITE);
        sheet.mergeCells(startColumn, row, endColumn, row + 1);
        Label label;
        label = new Label(startColumn, row, s, Cellstatus);
        sheet.addCell(label);

    }

    public void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {

        WritableFont fontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
        WritableCellFormat Cellstatus = new WritableCellFormat(fontStatus);

        Cellstatus.setWrap(true);
        Cellstatus.setBackground(Colour.ORANGE);
        Cellstatus.setAlignment(jxl.format.Alignment.CENTRE);
        Cellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Cellstatus.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.MEDIUM, Colour.WHITE);

        Label label;
        label = new Label(column, row, s, Cellstatus);
        sheet.addCell(label);

    }

    public void addInteger(WritableSheet sheet, int column, int row,
                           Integer value) throws WriteException, RowsExceededException {
        jxl.write.Number number;
        number = new Number(column, row, value, times);
        sheet.addCell(number);
    }

    public void addDouble(WritableSheet sheet, int column, int row,
                          Double value) throws WriteException, RowsExceededException {
        jxl.write.Number number;
        number = new Number(column, row, value, times);
        sheet.addCell(number);
    }

    public void addLong(WritableSheet sheet, int column, int row, long value)
            throws WriteException, RowsExceededException{

        jxl.write.Number number;
        number = new Number(column, row, value, times);
        sheet.addCell(number);
    }

    public void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    public void addDate(WritableSheet sheet, int column, int row, Date date)
            throws WriteException, RowsExceededException{

        DateTime dateTime;
        dateTime = new DateTime(column, row, date);
        sheet.addCell(dateTime);
    }

    public void createCellStyle() throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setAutosize(true);
    }

    class AsyncWriteToExcel extends AsyncTask<ArrayList<ArrayList<Object>>, Integer, Boolean> {

        WorkbookSettings wbSettings;
        WritableWorkbook workbook = null;
        WritableSheet sheet;
        String reportName;
        int divisor;
        String generatedBy = "";

        double totalSales;
        double totalCostPrice;
        double totalGross;
        double totalExemptTax;
        double totalDiscount;
        double totalSC;

        ArrayList<ArrayList<Object>> objectsList;

        public AsyncWriteToExcel(String reportName, String generatedBy, double totalSales, double totalCostPrice, double totalDiscount, double totalSC){

            this.reportName = reportName;
            this.generatedBy = generatedBy;
            this.totalSales = totalSales;
            this.totalCostPrice = totalCostPrice;
            this.totalDiscount = totalDiscount;
            this.totalGross = (totalSales - totalCostPrice);
            this.totalSC = totalSC;

        }

        @Override
        protected void onPreExecute() {

            progressFragment = new ProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString("loadingMessage", "Generating Product Sales Excel...");
            progressFragment.setArguments(bundle);
            progressFragment.show(getChildFragmentManager(), "progress");

            File path = Environment.getExternalStoragePublicDirectory("Cheappos" + "/Reports");
            if(!path.exists()){
                path.mkdirs();
            }

            File file = new File(path.getAbsolutePath(), reportName + ".xls");
            if(file.exists()){
                file.delete();
            }
            wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
            } catch (IOException e) {
                e.printStackTrace();
            }
            workbook.createSheet("Report", 0);
            this.sheet = workbook.getSheet(0);

        }

        @Override
        protected Boolean doInBackground(ArrayList<ArrayList<Object>>... params)  {
            objectsList = params[0];
            int counter = 0;
            int rowAdjustment = 14;
            int rowCount = 6;
            boolean isSuccess = true;
            divisor = params[0].get(0).size();

            try {
                addTitle(sheet, 0, 4, 0, "GENERATED BY: " + generatedBy + " " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                addDetails(sheet, 0, 4, 2, "TOTAL SALES = " + new DecimalFormat("###,###,##0.00").format(totalSales));
                addDetails(sheet, 0, 4, 4, "TOTAL PROFIT = " + new DecimalFormat("###,###,##0.00").format(totalGross));
                addDetails(sheet, 0, 4, 6, "TOTAL DISCOUNT = " + new DecimalFormat("###,###,##0.00").format(totalDiscount));
                addDetails(sheet, 0, 4, 8, "TOTAL SERVICE CHARGE = " + new DecimalFormat("###,###,##0.00").format(totalSC));

                addTitle(sheet, 0, divisor - 1, 12, "Product Sales");

            } catch (WriteException e) {
                e.printStackTrace();
            }

            int x = 0;
            for(Object object: objectsList.get(0)){
                try {
                    addCaption(sheet, x, rowAdjustment, object.toString());
                    x++;
                } catch (WriteException e) {
                    e.printStackTrace();
                    isSuccess = false;
                }
                counter++;
                publishProgress(counter);
            }

            for(int r = 1; r < objectsList.size(); r++){
                rowCount++;
                for(int c = 0; c < objectsList.get(r).size(); c++){

                    if(objectsList.get(r).get(c)!= null && (objectsList.get(r).get(c) instanceof String) ) {
                        try {
                            addLabel(sheet, c, r + rowAdjustment, objectsList.get(r).get(c).toString());
                        } catch (WriteException e) {
                            isSuccess = false;
                            e.printStackTrace();
                        }

                    }else if(objectsList.get(r).get(c)!= null && (objectsList.get(r).get(c) instanceof Integer)){
                        try {
                            addInteger(sheet, c, r + rowAdjustment, (Integer) objectsList.get(r).get(c));
                        } catch (WriteException e) {
                            isSuccess = false;
                            e.printStackTrace();
                        }

                    }else if(objectsList.get(r).get(c)!= null && (objectsList.get(r).get(c) instanceof Double)){
                        try {
                            addDouble(sheet, c, r + rowAdjustment, (Double) objectsList.get(r).get(c));
                        } catch (WriteException e) {
                            isSuccess = false;
                            e.printStackTrace();
                        }

                    }else if(objectsList.get(r).get(c)!= null && (objectsList.get(r).get(c) instanceof Long)){
                        try {
                            addLong(sheet, c, r + rowAdjustment, (Long) objectsList.get(r).get(c));
                        } catch (WriteException e) {
                            isSuccess = false;
                            e.printStackTrace();
                        }

                    }else if(objectsList.get(r).get(c)!= null && (objectsList.get(r).get(c) instanceof Date) ) {
                        try {
                            addDate(sheet, c, r + rowAdjustment, (Date) objectsList.get(r).get(c));
                        } catch (WriteException e) {
                            isSuccess = false;
                            e.printStackTrace();
                        }
                    }
                    counter++;
                    publishProgress(counter);

                }
            }

            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean value) {

            String result = "Report was exported successfully";
            progressFragment.dismiss();

            try {
                workbook.write();


            } catch (IOException e) {
                e.printStackTrace();
                result = "Report exporting fails";
            } finally {
                try {
                    workbook.close();
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }

            if(value){
                File f = new File(Environment.getExternalStoragePublicDirectory("Cheappos/Reports/"),
                        reportName + ".xls");

                Uri path;

                if(BuildConfig.VERSION_CODE > 23){
                    path = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", f);
                }else{
                    path = Uri.fromFile(f);
                }

                Intent openExcel = new Intent(Intent.ACTION_VIEW);
                openExcel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                openExcel.setDataAndType(path, "application/vnd.ms-excel");
                try {
                    startActivity(openExcel);
                }
                catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
