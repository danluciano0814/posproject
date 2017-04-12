package com.droidcoder.gdgcorp.posproject.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/12/2017.
 */

public class DateFilterFragment extends BaseFragment {

    @BindView(R.id.txtStartDate)TextView txtStartDate;
    @BindView(R.id.txtStartTime)TextView txtStartTime;
    @BindView(R.id.txtEndDate)TextView txtEndDate;
    @BindView(R.id.txtEndTime)TextView txtEndTime;
    @BindView(R.id.btnSearch)Button btnSearch;

    SimpleDateFormat sdf;
    SimpleDateFormat stf;

    Calendar startCal;
    Calendar endCal;

    MyTimePickerDialog mTimePicker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_filter, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initDate();

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtStartDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTimePicker = new MyTimePickerDialog(getActivity(), new MyTimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                        txtStartTime.setText(String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds));
                    }
                }, startCal.get(Calendar.HOUR_OF_DAY), startCal.get(Calendar.MINUTE), startCal.get(Calendar.SECOND), true);
                mTimePicker.show();

            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtEndDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTimePicker = new MyTimePickerDialog(getActivity(), new MyTimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                        txtEndTime.setText(String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds));
                    }
                }, endCal.get(Calendar.HOUR_OF_DAY), endCal.get(Calendar.MINUTE), endCal.get(Calendar.SECOND), true);
                mTimePicker.show();

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date startDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(txtStartDate.getText().toString().trim() + " " + txtStartTime.getText().toString().trim());
                    Date endDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(txtEndDate.getText().toString().trim() + " " + txtEndTime.getText().toString().trim());
                    ((SetOnDateFilter)getActivity()).onDateFilter(startDate, endDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error on using date filter", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void initDate(){

        sdf = new SimpleDateFormat("MM/dd/yyyy");
        stf = new SimpleDateFormat("HH:mm:yy");

        startCal = Calendar.getInstance();
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);

        endCal = Calendar.getInstance().getInstance();
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);

        txtStartDate.setText(sdf.format(startCal.getTime()));
        txtStartTime.setText(stf.format(startCal.getTime()));
        txtEndDate.setText(sdf.format(endCal.getTime()));
        txtEndTime.setText(stf.format(endCal.getTime()));

        ((SetOnDateFilter)getActivity()).onDateFilter(startCal.getTime(), endCal.getTime());

    }


    public interface SetOnDateFilter{

        void onDateFilter(Date startDate, Date endDate);

    }


}
