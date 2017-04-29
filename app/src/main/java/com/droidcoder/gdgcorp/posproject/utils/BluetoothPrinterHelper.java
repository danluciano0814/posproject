package com.droidcoder.gdgcorp.posproject.utils;

import android.content.Context;

import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.printer.BluetoothService;
import com.droidcoder.gdgcorp.posproject.printer.PrinterCommand;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DanLuciano on 4/28/2017.
 */

public class BluetoothPrinterHelper {

    public static String spaceFormatter(String left, String spaces, String right){
        String result;
        String space = spaces.substring(left.length(), spaces.length() - right.length());
        result = left + space + right;
        return result;
    }

    public static void printReceipt(Context context, BluetoothService bluetoothService, boolean isBluetoothAvailable, OrderReceipt orderReceipt){

        int itemCounter = 0;
        //paper 42 char length.
        String spaceContent = "                                          ";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String date = sdf.format(cal.getTime());
        double totalAmount = 0;

        List<OrderProduct> orderProductList = orderReceipt.getOrderProducts();

        StringBuilder receiptHeader = new StringBuilder("");
        StringBuilder receiptBody = new StringBuilder("");
        StringBuilder receiptTotal = new StringBuilder("");
        StringBuilder receiptOtherTotal = new StringBuilder("");
        StringBuilder receiptFooter = new StringBuilder("");

        //Header
        if(!LFHelper.getLocalData(context, GlobalConstants.RECEIPT_SHOW_ADDRESS).equalsIgnoreCase("0")){

            if(!LFHelper.getLocalData(context, GlobalConstants.STORE_ADDRESS1_FILE).equalsIgnoreCase("0")){
                receiptHeader.append(LFHelper.getLocalData(context, GlobalConstants.STORE_ADDRESS1_FILE) + "\n");
            }

            if(!LFHelper.getLocalData(context, GlobalConstants.STORE_ADDRESS2_FILE).equalsIgnoreCase("0")){
                receiptHeader.append(LFHelper.getLocalData(context, GlobalConstants.STORE_ADDRESS2_FILE) + "\n");
            }

        }

        if(!LFHelper.getLocalData(context, GlobalConstants.RECEIPT_SHOW_EMAIL).equalsIgnoreCase("0")){

            if(!LFHelper.getLocalData(context, GlobalConstants.STORE_EMAIL_FILE).equalsIgnoreCase("0")){
                receiptHeader.append(LFHelper.getLocalData(context, GlobalConstants.STORE_EMAIL_FILE) + "\n");
            }

        }

        if(!LFHelper.getLocalData(context, GlobalConstants.RECEIPT_SHOW_MOBILE).equalsIgnoreCase("0")){

            if(!LFHelper.getLocalData(context, GlobalConstants.STORE_MOBILE_FILE).equalsIgnoreCase("0")){
                receiptHeader.append(LFHelper.getLocalData(context, GlobalConstants.STORE_MOBILE_FILE) + "\n");
            }

        }

        if(!LFHelper.getLocalData(context, GlobalConstants.RECEIPT_SHOW_LANDLINE).equalsIgnoreCase("0")){

            if(!LFHelper.getLocalData(context, GlobalConstants.STORE_LANDLINE_FILE).equalsIgnoreCase("0")){
                receiptHeader.append(LFHelper.getLocalData(context, GlobalConstants.STORE_LANDLINE_FILE) + "\n");
            }

        }

        receiptHeader.append("\n\n");

        receiptBody.append("RECEIPT #: " + orderReceipt.getReceiptId() + "\n");
        receiptBody.append("CASHIER  : " + DBHelper.getDaoSession().getUserDao().load(orderReceipt.getUserId()).getFirstName() + " " +  DBHelper.getDaoSession().getUserDao().load(orderReceipt.getUserId()).getLastName() + "\n");
        receiptBody.append("DATE     : " + date + "\n");
        receiptBody.append("NAME     : ________________________\n");
        receiptBody.append("ADDRESS  : ________________________\n");
        receiptBody.append("TIN      : ________________________\n");
        receiptBody.append("------------------------------------------\n");
        receiptBody.append("DESCRIPTION                          PRICE\n");
        receiptBody.append("------------------------------------------\n");
        for(OrderProduct orderProduct : orderProductList){
            String leftContent = "  " + orderProduct.getProductQuantity() + " x   " + new DecimalFormat("###,###,##0.00").format(orderProduct.getProductSellPrice() / orderProduct.getProductQuantity());
            String rightContent = new DecimalFormat("###,###,##0.00").format(orderProduct.getProductDeductedPrice());
            String space = spaceContent.substring(leftContent.length(), spaceContent.length() - rightContent.length());
            String content = leftContent + space + rightContent;

            receiptBody.append(orderProduct.getProductName() +"\n");
            receiptBody.append(content + "\n");
            itemCounter += orderProduct.getProductQuantity();
        }
        receiptBody.append("------------------------------------------\n");

        totalAmount = orderReceipt.getTotalDeductedPrice() + orderReceipt.getServiceChargeTotal();

        String amountTotal = "Amount Total:";
        String totalResult = spaceFormatter(amountTotal, spaceContent, new DecimalFormat("###,###,##0.00").format(totalAmount));
        receiptTotal.append(totalResult + "\n");

        String tenderedCash = "Cash Tender :";
        String tenderResult = spaceFormatter(tenderedCash, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getCashTender()));
        receiptTotal.append(tenderResult + "\n");

        String changeTotal = "Change      :";
        String changeResult = spaceFormatter(changeTotal, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getCashTender() - totalAmount));
        receiptTotal.append(changeResult + "\n");


        //vat computation
        String lblVatSales = "VAT Sales     :";
        String resultVatSales = spaceFormatter(lblVatSales, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getTotalVatSales()));
        receiptOtherTotal.append(resultVatSales + "\n");

        String lblVat = "VAT " + new DecimalFormat("###,###,##0.00").format(orderReceipt.getTaxValue()) +"%    :";
        String resultVat = spaceFormatter(lblVat, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getTotalVat()));
        receiptOtherTotal.append(resultVat + "\n");

        String lblNonVat = "Non VAT Sales :";
        String resultNonVat = spaceFormatter(lblNonVat, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getTotalNonVatSales()));
        receiptOtherTotal.append(resultNonVat + "\n");

        String lblVatExempt = "VAT Exempt    :";
        String resultVatExempt = spaceFormatter(lblVatExempt, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getTotalTaxExempt()));
        receiptOtherTotal.append(resultVatExempt + "\n");

        String lblDiscount = "Total Discount:";
        String resultDiscount = spaceFormatter(lblDiscount, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getTotalDiscount()));
        receiptOtherTotal.append(resultDiscount + "\n");

        String lblServiceCharge = "Service Charge:";
        String resultServiceCharge = spaceFormatter(lblServiceCharge, spaceContent, new DecimalFormat("###,###,##0.00").format(orderReceipt.getServiceChargeTotal()));
        receiptOtherTotal.append(resultServiceCharge + "\n");


        //footer
        receiptFooter.append("THIS RECEIPT IS POWERED BY:\n");
        receiptFooter.append("CHEAPPOS APP\n");
        receiptFooter.append("FOR MORE INFO VISIT APP ON GOOGLE PLAY\n");
        receiptFooter.append("https://googleplay.cheappos\n\n\n\n");

        if(isBluetoothAvailable){

            bluetoothService.write(PrinterCommand.POS_Set_PrtInit());
            bluetoothService.write(new byte[]{ 27, 69, 1});
            bluetoothService.write(new byte[]{ 27, 97, 49});
            bluetoothService.write(PrinterCommand.POS_Set_FontSize(0, 1));
            bluetoothService.write("\n\n".getBytes());

            if(!LFHelper.getLocalData(context, GlobalConstants.STORE_NAME_FILE).equalsIgnoreCase("0")){
                bluetoothService.write((LFHelper.getLocalData(context, GlobalConstants.STORE_NAME_FILE) + "\n").getBytes());
            }


            bluetoothService.write(PrinterCommand.POS_Set_PrtInit());
            bluetoothService.write(new byte[]{ 27, 77, 49});
            bluetoothService.write(new byte[]{ 27, 97, 49});
            bluetoothService.write(receiptHeader.toString().getBytes());

            bluetoothService.write(PrinterCommand.POS_Set_PrtInit());
            bluetoothService.write(new byte[]{ 27, 77, 49});
            bluetoothService.write(receiptBody.toString().getBytes());
            bluetoothService.write(new byte[]{ 27, 97, 49});
            bluetoothService.write((itemCounter + "   ITEM(s)\n\n").getBytes());

            if(orderReceipt.getDeleted()!=null){
                bluetoothService.write(PrinterCommand.POS_Set_PrtInit());
                bluetoothService.write(new byte[]{ 27, 69, 1});
                bluetoothService.write(new byte[]{ 27, 97, 49});
                bluetoothService.write(PrinterCommand.POS_Set_FontSize(1, 1));
                bluetoothService.write("VOIDED\n".getBytes());
                bluetoothService.write(PrinterCommand.POS_Set_PrtInit());
                bluetoothService.write(new byte[]{ 27, 97, 49});
                bluetoothService.write(new byte[]{ 27, 77, 49});
                bluetoothService.write(("VOIDED BY : " + orderReceipt.getVoidBy() + " \n").getBytes());
                bluetoothService.write(("VOID DATE : " + sdf.format(orderReceipt.getDeleted()) + " \n").getBytes());
            }

            bluetoothService.write(PrinterCommand.POS_Set_PrtInit());
            bluetoothService.write(new byte[]{ 27, 69, 1});
            bluetoothService.write(new byte[]{ 27, 77, 49});
            bluetoothService.write(new byte[]{ 27, 97, 49});
            bluetoothService.write(receiptTotal.toString().getBytes());

            bluetoothService.write(PrinterCommand.POS_Set_PrtInit());
            bluetoothService.write(new byte[]{ 27, 77, 49});
            bluetoothService.write(("------------------------------------------\n").getBytes());
            bluetoothService.write(receiptOtherTotal.toString().getBytes());

            bluetoothService.write(("------------------------------------------\n\n").getBytes());
            bluetoothService.write(new byte[]{ 27, 97, 49});
            bluetoothService.write(new byte[]{ 27, 77, 49});
            bluetoothService.write(receiptFooter.toString().getBytes());
            bluetoothService.write(PrinterCommand.POS_Set_PrtInit());

        }



    }

}
