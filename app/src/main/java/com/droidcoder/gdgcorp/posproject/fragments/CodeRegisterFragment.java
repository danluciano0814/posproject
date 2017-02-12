package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.datasystem.CheckRegistration;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 1/12/2017.
 */

public class CodeRegisterFragment extends DialogFragment {

    @BindView(R.id.etCode) EditText etCode;
    @BindView(R.id.btnEnter) Button btnEnter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_register, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DBHelper.getDaoSession().getUserDao().loadAll().size()<=0){
                    User user = new User();
                    user.setPasswordCode("0000");
                    user.setFirstName("admin");
                    user.setLastName("admin");
                    DBHelper.getDaoSession().getUserDao().insert(user);
                    CheckRegistration.saveRegister("true", getActivity());
                }

                for(User user : DBHelper.getDaoSession().getUserDao().loadAll()){
                    if(user.getPasswordCode().equalsIgnoreCase(etCode.getText().toString().trim())){
                        CheckRegistration.saveRegister("true", getActivity());
                        ((OnRegistration)getActivity()).onRegistrationSuccess();
                        dismiss();
                    }else{
                        Toast.makeText(getActivity(), "Invalid Code", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public interface OnRegistration{
        void onRegistrationSuccess();
    }
}

