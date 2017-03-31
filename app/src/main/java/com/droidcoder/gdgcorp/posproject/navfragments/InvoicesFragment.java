package com.droidcoder.gdgcorp.posproject.navfragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.InvoiceSummaryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.fragments.InvoiceFormFragment;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/22/2017.
 */

public class InvoicesFragment extends BaseFragment {

    SearchView searchView;
    @BindView(R.id.lv_invoice_summary)ListView lvInvoiceSummary;

    @BindView(R.id.linearStartDate)LinearLayout linearStartDate;
    @BindView(R.id.linearEndDate)LinearLayout linearEndDate;
    @BindView(R.id.txtStartDate)TextView txtStartDate;
    @BindView(R.id.txtEndDate)TextView txtEndDate;
    @BindView(R.id.spnrStatus)Spinner spnrStatus;

    InvoiceSummaryAdapter invoiceSummaryAdapter;
    List<OrderReceipt> orderReceiptList;

    SimpleDateFormat df;
    Date startDate = null;
    Date endDate = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoices_summary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        startDate = new Date();
        endDate = new Date();

        txtStartDate.setText(df.format(startDate));
        txtEndDate.setText(df.format(endDate));


        //populate spinner
        String[] statusList = {GlobalConstants.PAYMENT_TYPE_CASH, GlobalConstants.PAYMENT_TYPE_CREDIT, GlobalConstants.PAYMENT_TYPE_POINTS
        , "VOIDED"};
        ArrayAdapter<String> spnrAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, statusList);
        spnrStatus.setAdapter(spnrAdapter);

        try {
            invoiceFilter((String)spnrStatus.getSelectedItem(), df.format(startDate), df.format(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        spnrStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    invoiceFilter(((TextView)view).getText().toString(), txtStartDate.getText().toString(), txtEndDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvInvoiceSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InvoiceFormFragment invoiceFormFragment = new InvoiceFormFragment();
                Bundle bundle = new Bundle();
                bundle.putString("txtId", ((TextView)view.findViewById(R.id.txtId)).getText().toString());
                invoiceFormFragment.setArguments(bundle);
                invoiceFormFragment.show(getActivity().getSupportFragmentManager(), "invoiceForm");
            }
        });

        linearStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtStartDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                                try {
                                    invoiceFilter((String)spnrStatus.getSelectedItem(), txtStartDate.getText().toString(), txtEndDate.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        linearEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtEndDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                                try {
                                    invoiceFilter((String)spnrStatus.getSelectedItem(), txtStartDate.getText().toString(), txtEndDate.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.inventory_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Search Receipt");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchReceipt(query);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_search){
            Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchReceipt(String value){
        OrderReceipt orderReceipt = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                .where(OrderReceiptDao.Properties.ReceiptId.eq(value))
                .unique();
        if(orderReceipt!=null){
            InvoiceFormFragment invoiceFormFragment = new InvoiceFormFragment();
            Bundle bundle = new Bundle();
            bundle.putString("txtId", orderReceipt.getId() + "");
            invoiceFormFragment.setArguments(bundle);
            invoiceFormFragment.show(getActivity().getSupportFragmentManager(), "invoiceForm");
        }else{
            Toast.makeText(getActivity(), "Receipt ID does not exist", Toast.LENGTH_SHORT).show();
        }
    }

    public void invoiceFilter(String status, String start, String end) throws ParseException {

        Toast.makeText(getActivity(), "Status " + status, Toast.LENGTH_SHORT).show();
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(df.parse(start));
        calFrom.set(Calendar.HOUR, 0);
        calFrom.set(Calendar.MINUTE, 0);
        calFrom.set(Calendar.AM_PM,Calendar.AM);
        Date fromDate = df.parse(start);

        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(end));
        cal.set(Calendar.HOUR, 11);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 59000);
        cal.set(Calendar.AM_PM,Calendar.PM);
        Date toDate = cal.getTime();

        if(status.equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CASH)){

            orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                    .where(OrderReceiptDao.Properties.IsPaid.eq(true))
                    .where(OrderReceiptDao.Properties.PaymentType.eq(GlobalConstants.PAYMENT_TYPE_CASH))
                    .where(OrderReceiptDao.Properties.Deleted.isNull())
                    .where(OrderReceiptDao.Properties.Created.between(fromDate, toDate))
                    .orderDesc(OrderReceiptDao.Properties.Created).list();
            lvInvoiceSummary.setAdapter(new InvoiceSummaryAdapter(getActivity(), orderReceiptList));

        }else if(status.equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CREDIT)){

            orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                    .where(OrderReceiptDao.Properties.IsPaid.eq(false))
                    .where(OrderReceiptDao.Properties.PaymentType.eq(GlobalConstants.PAYMENT_TYPE_CREDIT))
                    .where(OrderReceiptDao.Properties.Deleted.isNull())
                    .where(OrderReceiptDao.Properties.Created.between(fromDate, toDate))
                    .orderDesc(OrderReceiptDao.Properties.Created).list();
            lvInvoiceSummary.setAdapter(new InvoiceSummaryAdapter(getActivity(), orderReceiptList));

        }else if(status.equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_POINTS)){

            orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                    .where(OrderReceiptDao.Properties.IsPaid.eq(true))
                    .where(OrderReceiptDao.Properties.PaymentType.eq(GlobalConstants.PAYMENT_TYPE_POINTS))
                    .where(OrderReceiptDao.Properties.Deleted.isNull())
                    .where(OrderReceiptDao.Properties.Created.between(fromDate, toDate))
                    .orderDesc(OrderReceiptDao.Properties.Created).list();
            lvInvoiceSummary.setAdapter(new InvoiceSummaryAdapter(getActivity(), orderReceiptList));

        }else{

            orderReceiptList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                    .where(OrderReceiptDao.Properties.Deleted.isNotNull())
                    .where(OrderReceiptDao.Properties.Created.between(fromDate, toDate))
                    .orderDesc(OrderReceiptDao.Properties.Created).list();
            lvInvoiceSummary.setAdapter(new InvoiceSummaryAdapter(getActivity(), orderReceiptList));

        }
    }

}
