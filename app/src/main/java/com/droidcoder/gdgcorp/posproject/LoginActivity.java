package com.droidcoder.gdgcorp.posproject;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserDao;
import com.droidcoder.gdgcorp.posproject.datasystem.CheckRegistration;
import com.droidcoder.gdgcorp.posproject.datasystem.CurrentUser;
import com.droidcoder.gdgcorp.posproject.fragments.CodeRegisterFragment;
import com.droidcoder.gdgcorp.posproject.fragments.InitialRegistrationFragment;
import com.droidcoder.gdgcorp.posproject.fragments.ProgressFragment;
import com.droidcoder.gdgcorp.posproject.utils.AsyncCheckEmail;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 12/30/2016.
 */

public class LoginActivity extends BaseCompatActivity implements AsyncCheckEmail.OnCheckingEmail, CodeRegisterFragment.OnRegistration {

    @BindView(R.id.txtPassword) TextView txtPassword;
    @BindView(R.id.fragmentContainer) LinearLayout fragmentContainer;

    String password = "";

    InitialRegistrationFragment initialRegistrationFragment;

    ProgressFragment progressFragment;
    Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        //if not initially register show fragment
        if(!CheckRegistration.isRegistered(this)){
            initialRegistrationFragment = new InitialRegistrationFragment();
            getSupportFragmentManager().beginTransaction().replace(fragmentContainer.getId(), initialRegistrationFragment).addToBackStack("initFrag").commit();
        }

        if(savedInstanceState!=null){
            setPasswordValue(savedInstanceState.getString("passCode"));
        }

    }

    @Override
    public void onBackPressed() {
        if(initialRegistrationFragment != null){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        super.onBackPressed();
    }

    public void setPasswordValue(String value){

        txtPassword.setText(txtPassword.getText().toString().trim() + value);
        password = txtPassword.getText().toString().trim();

    }

    public void recoverPassword(View v){

        CheckRegistration.saveRegister("", this);
        DBHelper.getDaoSession().getUserDao().deleteAll();
        DBHelper.getDaoSession().getFirebaseUserDao().deleteAll();
        DBHelper.getDaoSession().getProductDao().deleteAll();

        Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();

    }

    public void editTxtPassword(View v){

        switch (v.getId()){

            case R.id.btn0:
                setPasswordValue("0");
                break;

            case R.id.btn1:
                setPasswordValue("1");
                break;

            case R.id.btn2:
                setPasswordValue("2");
                break;

            case R.id.btn3:
                setPasswordValue("3");
                break;

            case R.id.btn4:
                setPasswordValue("4");
                break;

            case R.id.btn5:
                setPasswordValue("5");
                break;

            case R.id.btn6:
                setPasswordValue("6");
                break;

            case R.id.btn7:
                setPasswordValue("7");
                break;

            case R.id.btn8:
                setPasswordValue("8");
                break;

            case R.id.btn9:
                setPasswordValue("9");
                break;

            case R.id.btnClear:
                txtPassword.setText("");
                password = "";
                break;

            case R.id.btnLogin:

                if(CheckRegistration.isRegistered(this)){
                    if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                            .where(UserDao.Properties.PasswordCode.eq(password))
                            .where(UserDao.Properties.Deleted.isNull()).list().size() > 0){

                        CurrentUser.initUser(password);
                        startActivity(new Intent(this, NavigationActivity.class));

                    }else{
                        Toast.makeText(this, "Login pin is invalid", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Register the 4 digit code first", Toast.LENGTH_LONG).show();
                }
                break;

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("passCode", password);
    }

    @Override
    public void onFinish(boolean emailExist, String email, String passCode) {
        if(emailExist){
            if(initialRegistrationFragment != null){
                initialRegistrationFragment.setEmailInvalid(emailExist);
            }
        }else{
            new AsyncSendToEmail(this, passCode).execute(email);
        }
    }

    public class AsyncSendToEmail extends AsyncTask<String, Void, String> {

        Session session = null;
        Context context;
        String passwordCode;

        public AsyncSendToEmail(Context context, String passwordCode){
            this.context = context;
            this.passwordCode = passwordCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressFragment = new ProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString("loadingMessage", "Sending Email...");
            progressFragment.setArguments(bundle);
            progressFragment.show(LoginActivity.this.getSupportFragmentManager(), "progress");

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("danluciano08@gmail.com", "eizenn1008gaviel");
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            String messageContent = "Your new password code for CHEAPPOS is " + passwordCode + ". " +
                    "Please do not reply, this is only an automated email";

            String result = "Email sent successfully";
            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("danluciano08@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params[0]));
                message.setSubject("CHEAPPOS PASSWORD CODE");
                message.setContent(messageContent, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();

            } catch(Exception e) {
                e.printStackTrace();

            }

            return result;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Toast.makeText(context, res, Toast.LENGTH_LONG).show();
            progressFragment.dismiss();
            initialRegistrationFragment.setBtnEnabled();
        }

    }

    @Override
    public void onRegistrationSuccess() {
        if(initialRegistrationFragment!=null){
            getSupportFragmentManager().popBackStack("initFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }
}

