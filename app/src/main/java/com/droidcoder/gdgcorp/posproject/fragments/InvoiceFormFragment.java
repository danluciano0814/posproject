package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.datasystem.CurrentUser;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/23/2017.
 */

public class InvoiceFormFragment extends BaseDialogFragment {

    @BindView(R.id.txtCreated)TextView txtCreated;
    @BindView(R.id.txtUserName)TextView txtUserName;
    @BindView(R.id.txtCustomerName)TextView txtCustomerName;
    @BindView(R.id.txtPaidDate)TextView txtPaidDate;
    @BindView(R.id.txtRemarks)TextView txtRemarks;
    @BindView(R.id.txtVoidDate)TextView txtVoidDate;
    @BindView(R.id.txtVoidBy)TextView txtVoidBy;
    @BindView(R.id.txtReward)TextView txtReward;
    @BindView(R.id.txtDiscount)TextView txtDiscount;
    @BindView(R.id.txtTotal)TextView txtTotal;
    @BindView(R.id.txtCashTender)TextView txtCashTender;
    @BindView(R.id.txtOrderSummary)TextView txtOrderSummary;
    @BindView(R.id.txtReceiptId)TextView txtReceiptId;
    @BindView(R.id.txtId)TextView txtId;
    @BindView(R.id.imageVoided)ImageView imageVoided;

    @BindView(R.id.btnCancel)Button btnCancel;
    @BindView(R.id.btnVoid)Button btnVoid;
    @BindView(R.id.btnCash)Button btnCash;
    @BindView(R.id.btnCredit)Button btnCredit;
    @BindView(R.id.btnMail)Button btnMail;
    @BindView(R.id.btnPrint)Button btnPrint;

    SimpleDateFormat sdf;
    String spaceContent = "                                                                ";
    double reward = 0;
    Customer customer = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);
        sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        if(!getArguments().isEmpty()){

            OrderReceipt orderReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(Long.parseLong(getArguments().getString("txtId")));

            User user = DBHelper.getDaoSession().getUserDao().load(orderReceipt.getUserId());
            String userName = user.getFirstName() + " " + user.getLastName();

            String customerName = "";

            if(orderReceipt.getCustomerId() > 0){
                customer = DBHelper.getDaoSession().getCustomerDao().load(orderReceipt.getCustomerId());
                customerName = customer.getFirstName() + " " + customer.getLastName();

                reward = (((orderReceipt.getTotalDeductedPrice() + orderReceipt.getServiceChargeTotal()) / orderReceipt.getPurchaseSetting()) * orderReceipt.getRewardSetting());
            }

            txtCreated.setText(sdf.format(orderReceipt.getCreated()));
            txtUserName.setText(userName);
            txtCustomerName.setText(customerName);
            txtPaidDate.setText(orderReceipt.getPaidDate()!=null ? sdf.format(orderReceipt.getPaidDate()) : "");
            txtRemarks.setText(orderReceipt.getIsPaid() ? "Paid" : "Unpaid");
            txtVoidDate.setText(orderReceipt.getDeleted()!=null ? sdf.format(orderReceipt.getDeleted()) : "");
            txtVoidBy.setText(orderReceipt.getDeleted()!=null ? orderReceipt.getVoidBy() : "");
            txtReward.setText(StringConverter.doubleFormatter(reward));
            txtDiscount.setText(StringConverter.doubleFormatter(orderReceipt.getTotalDiscount() + orderReceipt.getTotalTaxExempt()));
            txtTotal.setText(StringConverter.doubleFormatter((orderReceipt.getTotalDeductedPrice() + orderReceipt.getServiceChargeTotal())));
            txtCashTender.setText(StringConverter.doubleFormatter(orderReceipt.getCashTender()));
            txtReceiptId.setText("RECEIPT # : " + orderReceipt.getReceiptId() + "");

            StringBuilder res = new StringBuilder("");

            String left = "  QTY          " + " PRICE " + "        " + "DISCOUNT";
            String right = "TOTAL";
            String spaceCont = spaceContent.substring(left.length(), spaceContent.length() - right.length());
            String con = left + spaceCont + right;
            res.append(con + "\n\n");

            for(OrderProduct orderProduct : orderReceipt.getOrderProducts()){
                String leftContent = "    " + orderProduct.getProductQuantity() + "  x     " + StringConverter.doubleFormatter(orderProduct.getProductSellPrice()) + "             -" + StringConverter.doubleFormatter(orderProduct.getDiscountTotal());
                String rightContent = StringConverter.doubleFormatter(orderProduct.getProductDeductedPrice());
                String space = spaceContent.substring(leftContent.length(), spaceContent.length() - rightContent.length());
                String content = leftContent + space + rightContent;

                res.append(orderProduct.getProductName() + "\n");
                res.append(content + "\n");

            }

            txtOrderSummary.setText(res);

            if(orderReceipt.getPaymentType().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CASH)){
                btnCash.setVisibility(View.GONE);
                btnCredit.setVisibility(View.GONE);
            }else if(orderReceipt.getPaymentType().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CREDIT)){
                btnCredit.setVisibility(View.GONE);
            }else if(orderReceipt.getPaymentType().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_POINTS)){
                btnCredit.setVisibility(View.GONE);
                btnCash.setVisibility(View.GONE);
            }

            if(orderReceipt.getDeleted()!= null){
                btnVoid.setVisibility(View.GONE);
                btnCash.setVisibility(View.GONE);
                btnCredit.setVisibility(View.GONE);
                imageVoided.setVisibility(View.VISIBLE);
            }

        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Warning");
                alertDialogBuilder
                        .setMessage("Continue voiding this receipt?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                OrderReceipt orderReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(Long.parseLong(getArguments().getString("txtId")));
                                orderReceipt.setDeleted(new Date());
                                orderReceipt.setVoidBy(CurrentUser.getUser().getFirstName() + " " + CurrentUser.getUser().getLastName());

                                if(orderReceipt.getCustomerId() > 0){
                                    if(customer.getPoints() > reward){
                                        customer.setPoints(customer.getPoints() - reward);
                                    }else{
                                        customer.setPoints(0);
                                    }
                                    DBHelper.getDaoSession().getCustomerDao().update(customer);
                                }

                                for(OrderProduct orderProduct : orderReceipt.getOrderProducts()){
                                    Product product = DBHelper.getDaoSession().getProductDao().load(orderProduct.getProductId());
                                    product.setStocks(product.getStocks() + orderProduct.getProductQuantity());
                                    DBHelper.getDaoSession().getProductDao().update(product);
                                }
                                DBHelper.getDaoSession().getOrderReceiptDao().update(orderReceipt);
                                Toast.makeText(getActivity(), "Receipt was voided successfully, Product stocks are returned", Toast.LENGTH_LONG).show();
                                dismiss();

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
        });

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Warning");
                alertDialogBuilder
                        .setMessage("This credit will be marked as paid, continue payment for this receipt?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                OrderReceipt orderReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(Long.parseLong(getArguments().getString("txtId")));
                                orderReceipt.setPaidDate(new Date());
                                orderReceipt.setIsPaid(true);

                                DBHelper.getDaoSession().getOrderReceiptDao().update(orderReceipt);
                                Toast.makeText(getActivity(), "Receipt was voided successfully, Product stocks are returned", Toast.LENGTH_LONG).show();
                                dismiss();

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
        });



    }

}
