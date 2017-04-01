package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserDao;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.ConnectionHelper;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
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
 * Created by DanLuciano on 4/1/2017.
 * todo
 * 1. update firebase
 */

public class UserFormFragment extends BaseFragment{

    @BindView(R.id.editRemarks)EditText editRemarks;
    @BindView(R.id.editEmail)EditText editEmail;
    @BindView(R.id.editFirstName)EditText editFirstName;
    @BindView(R.id.editLastName)EditText editLastName;
    @BindView(R.id.editRoleName)EditText editRoleName;
    @BindView(R.id.editCode)EditText editCode;
    @BindView(R.id.btnOpen)Button btnOpen;
    @BindView(R.id.btnDelete)Button btnDelete;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.btnGenerateCode)Button btnGenerateCode;
    @BindView(R.id.imgUser)ImageView imageUser;
    @BindView(R.id.txtEmailMessage)TextView txtEmailMessage;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    boolean isCodeChange = false;
    String passwordCode;
    private long userId;

    ProgressFragment progressFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        imageUser.setImageResource(R.drawable.noimage);


        if(!getArguments().isEmpty()){
            userId = getArguments().getLong("userId");
            User user = DBHelper.getDaoSession().getUserDao().load(getArguments().getLong("userId"));
            editEmail.setText(user.getEmail());
            editFirstName.setText(user.getFirstName());
            editLastName.setText(user.getLastName());
            editRemarks.setText(user.getRemarks());
            editCode.setText(user.getPasswordCode());
            if(user.getUserRoleId() > 0){
                editRoleName.setText(DBHelper.getDaoSession().getUserRoleDao().load(user.getUserRoleId()).getRoleName());
            }else{
                editRoleName.setText("ADMIN");
            }

            imageUser.setImageBitmap(ImageConverter.bytesToBitmap(user.getImage()));
        }

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUser.setImageResource(R.drawable.noimage);
            }
        });

        btnGenerateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordCode = getPasswordCode();
                editCode.setText(passwordCode);
                isCodeChange = true;
                txtEmailMessage.setVisibility(View.VISIBLE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;

                if(editFirstName.getText().toString().trim().equalsIgnoreCase("")){
                    editFirstName.setError("First name is required");
                    isValid = false;
                }

                if(editLastName.getText().toString().trim().equalsIgnoreCase("")){
                    editLastName.setError("Last name is required");
                    isValid = false;
                }

                if(editEmail.getText().toString().trim().equalsIgnoreCase("")){
                    editEmail.setError("Email is required");
                    isValid = false;
                }

                User user = DBHelper.getDaoSession().getUserDao().load(userId);
                User user2 = null;

                if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                        .where(UserDao.Properties.FirstName.eq(editFirstName.getText().toString().trim().toUpperCase()))
                        .count() != 0){
                    user2 = DBHelper.getDaoSession().getUserDao().queryBuilder()
                            .where(UserDao.Properties.FirstName.eq(editFirstName.getText().toString().trim().toUpperCase())).list().get(0);
                }

                //check if name exist and Id is not the same

                if(user2 != null){
                    if(user.getId() != user2.getId()){
                        editFirstName.setError("First name already exist in database");
                        isValid = false;
                    }
                }


                if(isValid){

                    if(isCodeChange){

                        if(ConnectionHelper.isNetworkAvailable(getActivity(), GlobalConstants.getConnectivityTypes())){

                            bitmap = ((BitmapDrawable) imageUser.getDrawable()).getBitmap();
                            User userEdit = DBHelper.getDaoSession().getUserDao().load(userId);
                            userEdit.setFirstName(editFirstName.getText().toString().trim().toUpperCase());
                            userEdit.setLastName(editLastName.getText().toString().trim().toUpperCase());
                            userEdit.setRemarks(editRemarks.getText().toString().trim().toUpperCase());
                            userEdit.setPasswordCode(passwordCode);
                            userEdit.setImage(ImageConverter.bitmapToBytes(bitmap));

                            btnSave.setEnabled(false);
                            DBHelper.getDaoSession().getUserDao().update(userEdit);

                            //update fire base
                            FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                            DatabaseReference ref = fdb.getReference();

                            //create id then use this to save on offline database and fire base
                            DatabaseReference userCode = ref.child("users").child(userEdit.getFirebaseId()).child("passwordCode");
                            DatabaseReference userFname = ref.child("users").child(userEdit.getFirebaseId()).child("firstName");
                            DatabaseReference userLname = ref.child("users").child(userEdit.getFirebaseId()).child("lastName");
                            userCode.setValue(passwordCode);
                            userFname.setValue(editFirstName.getText().toString().trim().toUpperCase());
                            userLname.setValue(editLastName.getText().toString().trim().toUpperCase());

                            new AsyncSendToEmail(getActivity(), passwordCode).execute(user.getEmail());

                            Toast.makeText(getActivity(), "User account has been updated", Toast.LENGTH_LONG).show();
                            isCodeChange = false;
                            txtEmailMessage.setVisibility(View.INVISIBLE);

                        }else{
                            Toast.makeText(getActivity(), "Device is not Connected to the internet", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        //for editing

                        bitmap = ((BitmapDrawable) imageUser.getDrawable()).getBitmap();
                        User userEdit = DBHelper.getDaoSession().getUserDao().load(userId);
                        userEdit.setFirstName(editFirstName.getText().toString().trim().toUpperCase());
                        userEdit.setLastName(editLastName.getText().toString().trim().toUpperCase());
                        userEdit.setRemarks(editRemarks.getText().toString().trim().toUpperCase());
                        userEdit.setImage(ImageConverter.bitmapToBytes(bitmap));

                        DBHelper.getDaoSession().getUserDao().update(userEdit);

                        Toast.makeText(getActivity(), "User account has been updated", Toast.LENGTH_LONG).show();
                        isCodeChange = false;
                        txtEmailMessage.setVisibility(View.INVISIBLE);

                    }

                }

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                //Toast toast = Toast.makeText(getActivity(),"Exception", Toast.LENGTH_LONG);
                //toast.show();
                e.printStackTrace();
            }
        }
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

    public String generatePassCode(){
        String result = "";

        int first = (int) (Math.random() * 10);
        int second = (int) (Math.random() * 10);
        int third = (int) (Math.random() * 10);
        int fourth = (int) (Math.random() * 10);

        result += (first+"") + (second+"") + (third+"") + (fourth+"");

        return result;
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
            progressFragment.show(getActivity().getSupportFragmentManager(), "progress");

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(GlobalConstants.EMAIL_SENDER, GlobalConstants.EMAIL_PASSWORD);
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
                message.setFrom(new InternetAddress(GlobalConstants.EMAIL_SENDER));
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
            btnSave.setEnabled(true);
        }

    }
}
