package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserDao;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.AsyncCheckEmail;
import com.droidcoder.gdgcorp.posproject.utils.ConnectionHelper;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 1/8/2017.
 */

public class InitialRegistrationFragment extends BaseFragment {

    @BindView(R.id.btnSend) Button btnSend;
    @BindView(R.id.btnEnterCode) Button btnEnterCode;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etFname) EditText etFname;
    @BindView(R.id.etLname) EditText etLname;

    private String passCode;
    private String email;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_initial_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveRegistration();

            }
        });

        btnEnterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = "";
                for(User user : DBHelper.getDaoSession().getUserDao().loadAll()){
                    res +=  "ID : " + user.getFirebaseId() + " / Code" + user.getPasswordCode() + "\n";
                }
                Toast.makeText(getActivity(), "" + res ,Toast.LENGTH_LONG).show();

                CodeRegisterFragment codeRegisterFragment = new CodeRegisterFragment();
                codeRegisterFragment.show(getActivity().getSupportFragmentManager(), "codeRegister");
            }
        });


        setUIProperties();
    }

    private void setUIProperties() {
        etEmail.setSelectAllOnFocus(true);
        etFname.setSelectAllOnFocus(true);
        etLname.setSelectAllOnFocus(true);
    }

    public void saveRegistration(){
        boolean isValid = true;
        passCode = getPasswordCode();

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher matcher = pattern.matcher(etEmail.getText().toString().trim());
        //Validators
        if(etEmail.getText().toString().trim().equalsIgnoreCase("")){
            etEmail.setError("Required");
            isValid = false;
        }
        if(etFname.getText().toString().trim().equalsIgnoreCase("")){
            etFname.setError("Required");
            isValid = false;
        }
        if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.FirstName.eq(etFname.getText().toString().trim().toUpperCase()))
                .list().size()>0){
            etFname.setError("Already used");
            isValid = false;
        }
        if(etLname.getText().toString().trim().equalsIgnoreCase("")){
            etLname.setError("Required");
            isValid = false;
        }
        if(!matcher.matches()){
            etEmail.setError("invalid email");
            isValid = false;
        }

        if(isValid) {

            if(ConnectionHelper.isNetworkAvailable(getActivity(), GlobalConstants.getConnectivityTypes())){

                User user = new User();
                user.setCreated(new Date());
                user.setEmail(etEmail.getText().toString().trim().toUpperCase());
                user.setFirstName(etFname.getText().toString().trim().toUpperCase());
                user.setLastName(etLname.getText().toString().trim().toUpperCase());
                user.setPasswordCode(passCode);

                email = etEmail.getText().toString().trim().toUpperCase();

                AsyncCheckEmail asyncCheckEmail = new AsyncCheckEmail(getActivity(), user);
                asyncCheckEmail.execute(etEmail.getText().toString().trim());

                btnSend.setEnabled(false);

            }else{
                Toast.makeText(getActivity(), "Device is not Connected to the internet", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void setEmailInvalid(boolean emailExist){

        if(emailExist){
            etEmail.setError("Already Used");
            btnSend.setEnabled(true);
        }

    }

    public void setBtnEnabled(){
        btnSend.setEnabled(true);
    }

    public String generatePassCode(){
        String result = "";

        int first = (int) (Math.random() * 10);
        int second = (int) (Math.random() * 10);
        int third = (int) (Math.random() * 10);
        int fourth = (int) (Math.random() * 10);

        result += (first+"") + (second+"") + (third+"") + (fourth+"");

        return result;
    }

    public String getPasswordCode(){

        String passCode;

        while (true) {

            passCode = generatePassCode();

            //checks if password code was used on offline database already
            if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                        .where(UserDao.Properties.PasswordCode.eq(passCode)).list().size() <= 0
                    ) {
                break;
            }

        }

        return passCode;
    }


}
