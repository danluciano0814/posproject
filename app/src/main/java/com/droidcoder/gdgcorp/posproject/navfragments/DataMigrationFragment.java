package com.droidcoder.gdgcorp.posproject.navfragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.dataentity.CustomerDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Discount;
import com.droidcoder.gdgcorp.posproject.dataentity.DiscountDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProduct;
import com.droidcoder.gdgcorp.posproject.fragments.ProgressFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.app.Activity.RESULT_OK;

/**
 * Created by DanLuciano on 4/4/2017.
 */

public class DataMigrationFragment extends BaseFragment {

    @BindView(R.id.btnHelp)LinearLayout btnHelp;
    @BindView(R.id.btnImportProduct)LinearLayout btnImportProduct;
    @BindView(R.id.btnImportCustomer)LinearLayout btnImportCustomer;
    @BindView(R.id.btnImportDiscount)LinearLayout btnImportDiscount;
    @BindView(R.id.btnExportProduct)LinearLayout btnExportProduct;
    @BindView(R.id.btnExportDiscount)LinearLayout btnExportDiscount;
    @BindView(R.id.btnExportCustomer)LinearLayout btnExportCustomer;
    @BindView(R.id.txtLogs)TextView txtLogs;

    ProgressFragment progressFragment;

    private static final int OPEN_IMPORT_CODE = 40;

    private String importType = "";
    private String exportType = "";
    Bitmap icon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_migration, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.logo);

        btnImportCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStoragePermissionGranted()){
                    importType = "customer";
                    openFile(OPEN_IMPORT_CODE);
                }

            }
        });

        btnImportDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStoragePermissionGranted()){
                    importType = "discount";
                    openFile(OPEN_IMPORT_CODE);
                }

            }
        });

        btnImportProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStoragePermissionGranted()){
                    importType = "product";
                    openFile(OPEN_IMPORT_CODE);
                }

            }
        });

        btnExportDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStoragePermissionGranted()){
                    exportType = "discount";
                    confirmProcess(exportType);
                }

            }
        });

        btnExportCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStoragePermissionGranted()){
                    exportType = "customer";
                    confirmProcess(exportType);
                }

            }
        });

        btnExportProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStoragePermissionGranted()){
                    exportType = "product";
                    confirmProcess(exportType);
                }

            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder instruction = new StringBuilder();
                //PRODUCT
                instruction.append("Cheappos data migration is in the form of CSV, if you are new to this concept try to research online it was very easy and there's a lot of tutorial.\n");
                instruction.append("\n\nIMPORTING PRODUCT CSV\n\n");
                instruction.append("Format for product csv separated by comma is as follows fields with(*) is required.\n\n");
                instruction.append("PRODUCT NAME* , CATEGORY , SUB CATEGORY , CREATED DATE , DELETED DATE , DESCRIPTION , IMAGE BYTE , STOCKS* , COST PRICE* , SELL PRICE*\n\n");
                instruction.append("FIELDS INPUT VALUES\n");
                instruction.append("PRODUCT NAME = ( TEXT ) cannot be blank ex. Brewed.\n");
                instruction.append("CATEGORY = ( TEXT ) ex. Breakfast.\n");
                instruction.append("SUB CATEGORY = ( TEXT ) ex. Coffee.\n");
                instruction.append("CREATED DATE = ( DATE ) blank = date today, format is strictly mm/dd/yyyy  ex. 01/01/2017\n");
                instruction.append("DELETED DATE = ( DATE ) blank = null, format is strictly mm/dd/yyyy  ex. 12/31/2016\n");
                instruction.append("DESCRIPTION = ( TEXT ) ex. This is so hot.\n");
                instruction.append("IMAGE BYTE = ( TEXT BYTE ) this data will be generated once you export.\n");
                instruction.append("STOCKS = ( NUMBER ) ex 10.5.\n");
                instruction.append("COST PRICE = ( NUMBER ) ex 10.5.\n");
                instruction.append("SELL PRICE = ( NUMBER ) ex 10.5.\n");
                //DISCOUNT
                instruction.append("\n\nIMPORTING DISCOUNT CSV\n\n");
                instruction.append("Format for discount csv separated by comma is as follows fields with(*) is required.\n\n");
                instruction.append("DISCOUNT NAME* , DESCRIPTION , IS PERCENT* , VALUE*\n\n");
                instruction.append("FIELDS INPUT VALUES\n");
                instruction.append("DISCOUNT NAME = ( TEXT ) cannot be blank ex. Brewed.\n");
                instruction.append("DESCRIPTION = ( TEXT ) ex. Breakfast.\n");
                instruction.append("IS PERCENT = ( TRUE/FALSE ) ex. true.\n");
                instruction.append("VALUE = ( NUMBER ) ex 10.5.\n");
                //CUSTOMER
                instruction.append("\n\nIMPORTING CUSTOMER CSV\n\n");
                instruction.append("Format for customer csv separated by comma is as follows fields with(*) is required.\n\n");
                instruction.append("EMAIL* , F. NAME* , L. NAME* , CREATED DATE , DELETED DATE , POINTS , ADDRESS , CONTACTS , IMAGE BYTE , CUSTOMER ID*\n\n");
                instruction.append("FIELDS INPUT VALUES\n");
                instruction.append("EMAIL = ( TEXT ) must be valid email ex. me@gmail.com.\n");
                instruction.append("F. NAME = ( TEXT ) ex. Dan.\n");
                instruction.append("L. NAME = ( TEXT ) ex. L.\n");
                instruction.append("CREATED DATE = ( DATE ) blank = date today, format is strictly mm/dd/yyyy  ex. 01/01/2017\n");
                instruction.append("DELETED DATE = ( DATE ) blank = null, format is strictly mm/dd/yyyy  ex. 12/31/2016\n");
                instruction.append("POINTS = ( NUMBER ) ex. 200.5.\n");
                instruction.append("ADDRESS = ( TEXT ) ex myplace.\n");
                instruction.append("CONTACT = ( TEXT ).\n");
                instruction.append("IMAGE BYTE = ( TEXT BYTE ) this data will be generated once you export.\n");
                instruction.append("CUSTOMER ID = ( TEXT ) must be unique ex 124124523saasf.\n");

                txtLogs.setText(instruction.toString());
            }
        });

    }

    public void confirmProcess(final String exportType){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Warning");
        alertDialogBuilder
                .setMessage("Continue exporting " + exportType + ".csv file?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                        new AsyncValidateExport(exportType).execute();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void openFile(int requestCode){
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");

        if(Build.VERSION.SDK_INT>19){
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }else{
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        try{
            startActivityForResult(intent, requestCode);
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getActivity(), "No application found to open the document.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_IMPORT_CODE) {

            if(resultCode == RESULT_OK) {

                if (data != null) {

                    final Uri uri = data.getData();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Warning");
                    alertDialogBuilder
                            .setMessage("Continue importing " + importType +".csv file?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    importFile(uri, importType);
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            }

        }


    }

    public void importFile(Uri uri, String importType){

        List<String[]> list= new ArrayList<>();

        if(uri != null){
            String next[];

            try {
                CSVReader reader = new CSVReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(uri)));//Specify asset file name
                //in open();
                list = reader.readAll();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
            }
        }

        new AsyncValidateImport(list, importType).execute();

    }

    public String validateImportCustomer(List<String[]> list){

        int counter = 0;
        int successCounter = 0;
        StringBuilder logs = new StringBuilder();
        StringBuilder errorLogs = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher matcher;

        for(String[] values : list){

            counter++;
            boolean isValid = true;
            String lineError = "";
            matcher = pattern.matcher(values[0].trim());

            try{
                if(values.length < 10){
                    isValid = false;
                    errorLogs.append("Error! comma separated value is lower than 10, Make sure you are following the correct format. \nClick Import Format and Instructions button for more details");
                    break;
                }

                if(values[0].trim().equalsIgnoreCase("")){
                    isValid = false;
                    lineError += "Email required ";

                } else if(!values[0].trim().equalsIgnoreCase("")){
                    if(DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                            .where(CustomerDao.Properties.Email.eq(values[0].trim().toUpperCase()))
                            .count() > 0){

                        isValid = false;
                        lineError += "Email exist ";
                    }

                    if(!matcher.matches()){
                        isValid = false;
                        lineError += "Email invalid ";
                    }
                }

                if(values[1].trim().equalsIgnoreCase("")){
                    isValid = false;
                    lineError += "FirstName required ";
                }

                if(values[2].trim().equalsIgnoreCase("")){
                    isValid = false;
                    lineError += "LastName required ";
                }

                if(!values[4].trim().equalsIgnoreCase("")){
                    try {
                        sdf.parse(values[4]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        lineError += "Date Deleted must be in this format mm/dd/yy hh:mm:ss ";
                        isValid = false;
                    }
                }

                if(!values[5].trim().equalsIgnoreCase("")){
                    try{
                        Double.parseDouble(values[5]);
                    }catch(Exception e){
                        lineError += "Points must be a number";
                        isValid = false;
                    }
                }

                if(values[9].trim().equalsIgnoreCase("")){
                    isValid = false;
                    lineError += "Code required ";

                }else if(!values[9].trim().equalsIgnoreCase("")) {
                    if (DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                            .where(CustomerDao.Properties.Code.eq(values[9].trim()))
                            .count() > 0) {

                        isValid = false;
                        lineError += "Code exist ";
                    }
                }

            }catch(Exception e){
                isValid = false;
            }

            if(isValid){
                //errorLogs.append("Line " + counter + " was successfully inserted.\n");
                successCounter++;
                createCustomer(values);
            }else{
                errorLogs.append("Line " + counter + ": (" + lineError + "), failed to insert this line.\n");
                continue;
            }
        }

        logs.append("Finish inserting ( " + successCounter + "/" + list.size() +" ) Customer entry.\n\n" );
        logs.append(errorLogs.toString());
        return logs.toString();

    }

    public void createCustomer(String[] values){
        double points = 0;
        Date created = new Date();
        Date deleted = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


        if(!values[3].equalsIgnoreCase("")){
            try {
                created = sdf.parse(values[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(!values[4].equalsIgnoreCase("")){
            try {
                deleted = sdf.parse(values[4]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(!values[5].equalsIgnoreCase("")){
            points = Double.parseDouble(values[5]);
        }

        Customer customer = new Customer();
        customer.setCreated(created);
        customer.setDeleted(deleted);
        customer.setEmail(values[0].trim().toUpperCase());
        customer.setFirstName(values[1].trim().toUpperCase());
        customer.setLastName(values[2].trim().toUpperCase());
        customer.setPoints(points);
        customer.setAddress(values[6].trim().toUpperCase());
        customer.setContact(values[7]);
        customer.setImage(Base64.decode(values[8], Base64.DEFAULT));
        customer.setCode(values[9]);

        DBHelper.getDaoSession().getCustomerDao().insert(customer);
    }

    public String validateImportDiscount(List<String[]> list){
        int counter = 0;
        int successCounter = 0;
        StringBuilder logs = new StringBuilder();
        StringBuilder errorLogs = new StringBuilder();

        for(String[] values : list){

            counter++;
            boolean isValid = true;
            String lineError = "";

            try{
                if(values.length < 4){
                    isValid = false;
                    errorLogs.append("Error! comma separated value is lower than 4, Make sure you are following the correct format. \nClick Import Format and Instructions button for more details");
                    break;
                }

                if(values[0].trim().equalsIgnoreCase("")){
                    isValid = false;
                    lineError += "Discount Name empty ";
                }else{

                    if(DBHelper.getDaoSession().getDiscountDao().queryBuilder()
                            .where(DiscountDao.Properties.Name.eq(values[0].trim().toUpperCase()))
                            .count() > 0){

                        isValid = false;
                        lineError += "Discount Name exist ";
                    }

                }

                if(!values[2].trim().equalsIgnoreCase("true") && !values[2].trim().equalsIgnoreCase("false") && values[2].trim().equals("")){
                    isValid = false;
                    lineError += "IsPercentage invalid ";

                }

                if(values[2].equals("true")){
                    try{
                        if(Double.parseDouble(values[3]) <= 0 && Double.parseDouble(values[3]) > 100){
                            isValid = false;
                            lineError += "Value must be between 0.1 - 100";
                        }
                    }catch(Exception e){
                        isValid = false;
                        lineError += "Value invalid";
                    }
                }else{
                    try{
                        if(Double.parseDouble(values[3]) < 0){
                            isValid = false;
                            lineError += "Value must be greater than 0";
                        }
                    }catch(Exception e){
                        isValid = false;
                        lineError += "Value invalid";
                    }
                }



            }catch(Exception e){
                isValid = false;
            }

            if(isValid){
                //errorLogs.append("Line " + counter + " was successfully inserted.\n");
                successCounter++;
                createDiscount(values);
            }else{
                errorLogs.append("Line " + counter + ": (" + lineError + "), failed to insert this line.\n");
                continue;
            }
        }

        logs.append("Finish inserting ( " + successCounter + "/" + list.size() +" ) Discounts entry.\n\n" );
        logs.append(errorLogs.toString());
        return logs.toString();
    }

    public void createDiscount(String[] values){

        Discount discount = new Discount();
        discount.setCreated(new Date());
        discount.setDeleted(null);
        discount.setName(values[0].trim().toUpperCase());
        discount.setDescription(values[1].trim().toUpperCase());
        discount.setIsPercentage(Boolean.parseBoolean(values[2].trim()));
        discount.setDiscountValue(Double.parseDouble(values[3].trim()));

        DBHelper.getDaoSession().getDiscountDao().insert(discount);

    }

    public String validateImportProduct(List<String[]> list){

        int counter = 0;
        int successCounter = 0;
        StringBuilder logs = new StringBuilder();
        StringBuilder errorLogs = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        for(String[] values : list){

            counter++;
            boolean isValid = true;
            String lineError = "";

            try{
                if(values.length < 10){
                    isValid = false;
                    errorLogs.append("Error! comma separated value is lower than 10, Make sure you are following the correct format. \nClick Import Format and Instructions button for more details");
                    break;
                }

                if(values[0].trim().equalsIgnoreCase("")){
                    isValid = false;
                    lineError += "Product Name required ";

                } else if(!values[0].trim().equalsIgnoreCase("")){
                    if(DBHelper.getDaoSession().getProductDao().queryBuilder()
                            .where(ProductDao.Properties.Name.eq(values[0].trim().toUpperCase()))
                            .count() > 0){

                        isValid = false;
                        lineError += "Product Name exist ";
                    }

                }

                if(!values[4].trim().equalsIgnoreCase("")){
                    try {
                        sdf.parse(values[4]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        lineError += "Date Deleted must be in this format mm/dd/yy hh:mm:ss";
                        isValid = false;
                    }
                }

                try{
                    Double.parseDouble(values[7]);
                }catch(Exception e){
                    isValid = false;
                    lineError += "Stocks invalid";
                }

                try{
                    if(Double.parseDouble(values[8]) < 0){
                        isValid = false;
                        lineError += "Cost Price must be greater than 0";
                    }
                }catch(Exception e){
                    isValid = false;
                    lineError += "Cost Price invalid";
                }

                try{
                    if(Double.parseDouble(values[9]) < 0){
                        isValid = false;
                        lineError += "Sell Price must be greater than 0";
                    }
                }catch(Exception e){
                    isValid = false;
                    lineError += "Sell Price invalid";
                }



            }catch(Exception e){
                isValid = false;
            }

            if(isValid){
                //errorLogs.append("Line " + counter + " was successfully inserted.\n");
                successCounter++;
                createProduct(values);
            }else{
                errorLogs.append("Line " + counter + ": (" + lineError + "), failed to insert this line.\n");
                continue;
            }
        }

        logs.append("Finish inserting ( " + successCounter + "/" + list.size() +" ) Products entry.\n\n" );
        logs.append(errorLogs.toString());
        return logs.toString();

    }

    public void createProduct(String[] values) {

        long productId;
        long categoryId;
        long subCategoryId;
        Date created = new Date();
        Date deleted = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        double points = 0;

        //get category id
        if(values[1].trim().equalsIgnoreCase("")){
            categoryId = 0;
        }else{

            if(DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.eq(values[1].trim().toUpperCase()))
                    .unique() != null){

                categoryId = DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                        .where(CategoryDao.Properties.Name.eq(values[1].trim().toUpperCase()))
                        .unique().getId();

            }else{
                //create if it doesnt exist
                Category category = new Category();
                category.setCreated(new Date());
                category.setDeleted(null);
                category.setCategoryColor("#2196F3");
                category.setName(values[1].trim().toUpperCase());

                categoryId = DBHelper.getDaoSession().getCategoryDao().insert(category);
            }

        }

        if(values[2].trim().equalsIgnoreCase("")){
            subCategoryId = 0;
        }else{

            if(DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                    .where(SubCategoryDao.Properties.Name.eq(values[2].trim().toUpperCase()))
                    .unique() != null){

                subCategoryId = DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                        .where(SubCategoryDao.Properties.Name.eq(values[2].trim().toUpperCase()))
                        .unique().getId();

            }else{
                //create if it doesnt exist
                SubCategory subCategory = new SubCategory();
                subCategory.setCreated(new Date());
                subCategory.setDeleted(null);
                subCategory.setName(values[2].trim().toUpperCase());
                subCategory.setCategoryId(categoryId);

                subCategoryId = DBHelper.getDaoSession().getSubCategoryDao().insert(subCategory);
            }

        }

        if(!values[3].equalsIgnoreCase("")){
            try {
                created = sdf.parse(values[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(!values[4].equalsIgnoreCase("")){
            try {
                deleted = sdf.parse(values[4]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Product product = new Product();
        product.setCreated(created);
        product.setDeleted(deleted);
        product.setName(values[0].trim().toUpperCase());
        product.setDescription(values[0].trim().toUpperCase());
        product.setImage(Base64.decode(values[6], Base64.DEFAULT));
        product.setStocks(Double.parseDouble(values[7]));
        product.setCostPrice(Double.parseDouble(values[8]));
        product.setSellPrice(Double.parseDouble(values[9]));

        productId = DBHelper.getDaoSession().getProductDao().insert(product);

        if(productId > 0 && subCategoryId > 0){
            SubCategoryProduct subCategoryProduct = new SubCategoryProduct();
            subCategoryProduct.setProductId(productId);
            subCategoryProduct.setSubCategoryId(subCategoryId);
            DBHelper.getDaoSession().getSubCategoryProductDao().insert(subCategoryProduct);
        }
    }

    public String validateExportDiscount(){

        String logs = "";
        List<Discount> discountList = DBHelper.getDaoSession().getDiscountDao().loadAll();
        List<String[]> values = new ArrayList<>();

        if(discountList.size() > 0){

            for(Discount discount : discountList){

                String isPercent = discount.getIsPercentage() ? "true" : "false";
                String[] entries = {discount.getName(), discount.getDescription(), isPercent, discount.getDiscountValue() + ""};
                values.add(entries);
            }

            try {
                String filename = "Discount"+ new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv";
                File path = Environment.getExternalStoragePublicDirectory("/CheapposCSV" + "/DiscountCSV");
                if(!path.exists()){
                    path.mkdirs();
                }
                File file = new File(path.getAbsolutePath(), filename);
                if(file.exists()){
                    file.delete();
                }
                CSVWriter writer = null;
                writer = new CSVWriter(new FileWriter(file), ',');
                // feed in your array (or convert your data to an array)
                writer.writeAll(values);
                writer.close();

                logs = "Finish exporting " + discountList.size() + " Discount items." +
                        "\nThe csv file name is " + filename + "." +
                        "\nFile location is " + path.getPath();

            } catch (IOException e) {
                e.printStackTrace();

                logs = "An error has occur while exporting this file.";
            }

        }else{
            logs = "No Data to Export";
        }

        return logs;
    }

    public String validateExportCustomer(){

        String logs = "";
        List<Customer> customerList = DBHelper.getDaoSession().getCustomerDao().loadAll();
        List<String[]> values = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        if(customerList.size() > 0){

            for(Customer customer : customerList){

                String deleted = "";
                String image = Base64.encodeToString(customer.getImage(), Base64.DEFAULT);

                if(customer.getDeleted() != null){
                    deleted = sdf.format(customer.getDeleted());
                }

                String[] entries = {customer.getEmail(), customer.getFirstName(), customer.getLastName(),
                        sdf.format(customer.getCreated()), deleted, customer.getPoints() + "",
                        customer.getAddress(), customer.getContact(), image, customer.getCode()};

                values.add(entries);
            }

            try {
                String filename = "Customer"+ new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv";
                File path = Environment.getExternalStoragePublicDirectory("/CheapposCSV" + "/CustomerCSV");
                if(!path.exists()){
                    path.mkdirs();
                }
                File file = new File(path.getAbsolutePath(), filename);
                if(file.exists()){
                    file.delete();
                }
                CSVWriter writer = null;
                writer = new CSVWriter(new FileWriter(file), ',');
                // feed in your array (or convert your data to an array)
                writer.writeAll(values);
                writer.close();

                logs = "Finish exporting " + customerList.size() + " Customer items." +
                        "\nThe csv file name is " + filename + "." +
                        "\nFile location is " + path.getPath();

            } catch (IOException e) {
                e.printStackTrace();

                logs = "An error has occur while exporting this file.";
            }

        }else{
            logs = "No Data to Export";
        }

        return logs;
    }

    public String validateExportProduct(){

        String logs = "";
        List<Product> productList = DBHelper.getDaoSession().getProductDao().loadAll();
        List<String[]> values = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        if(productList.size() > 0){

            for(Product product : productList){

                String deleted = "";
                String image = Base64.encodeToString(product.getImage(), Base64.DEFAULT);

                if(product.getDeleted() != null){
                    deleted = sdf.format(product.getDeleted());
                }

                String[] entries = {product.getName(), "", "",
                        sdf.format(product.getCreated()), deleted, product.getDescription(),
                        image, product.getStocks()+"", product.getCostPrice()+"", product.getSellPrice()+""};

                values.add(entries);
            }

            try {
                String filename = "Product"+ new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv";
                File path = Environment.getExternalStoragePublicDirectory("/CheapposCSV" + "/ProductCSV");
                if(!path.exists()){
                    path.mkdirs();
                }
                File file = new File(path.getAbsolutePath(), filename);
                if(file.exists()){
                    file.delete();
                }
                CSVWriter writer = null;
                writer = new CSVWriter(new FileWriter(file), ',');
                // feed in your array (or convert your data to an array)
                writer.writeAll(values);
                writer.close();

                logs = "Finish exporting " + productList.size() + " Product items." +
                        "\nThe csv file name is " + filename + "." +
                        "\nFile location is " + path.getPath();

            } catch (IOException e) {
                e.printStackTrace();

                logs = "An error has occur while exporting this file.";
            }

        }else{
            logs = "No Data to Export";
        }

        return logs;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public class AsyncValidateImport extends AsyncTask<Void, Void, String> {

        String importType;
        List<String[]> list;

        public AsyncValidateImport(List<String[]> list, String importType){
            this.importType = importType;
            this.list = list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressFragment = new ProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString("loadingMessage", "Importing "+ importType +"...");
            progressFragment.setArguments(bundle);
            progressFragment.show(getActivity().getSupportFragmentManager(), "progress");

        }

        @Override
        protected String doInBackground(Void... params) {

            String result = "";

            if(importType.equals("customer")){
                result = validateImportCustomer(list);
            }else if(importType.equals("discount")){
                result = validateImportDiscount(list);
            }else if(importType.equals("product")){
                result = validateImportProduct(list);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            txtLogs.setText(result);
            progressFragment.dismiss();
        }

    }

    public class AsyncValidateExport extends AsyncTask<Void, Void, String> {

        String exportType;

        public AsyncValidateExport(String exportType){
            this.exportType = exportType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressFragment = new ProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString("loadingMessage", "Exporting "+ exportType +"...");
            progressFragment.setArguments(bundle);
            progressFragment.show(getActivity().getSupportFragmentManager(), "progress");

        }

        @Override
        protected String doInBackground(Void... params) {

            String result = "";

            if(exportType.equals("customer")){
                result = validateExportCustomer();
            }else if(exportType.equals("discount")){
                result = validateExportDiscount();
            }else if(exportType.equals("product")){
                result = validateExportProduct();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            txtLogs.setText(result);
            progressFragment.dismiss();
        }

    }


}
