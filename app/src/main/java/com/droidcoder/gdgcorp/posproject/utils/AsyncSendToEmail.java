package com.droidcoder.gdgcorp.posproject.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by DanLuciano on 1/11/2017.
 */

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

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("danluciano08@gmail.com", "eizenn1008gaviel");
            }
        });

        ((OnSendingEmail)context).showProgress("Sending Email");
    }

    @Override
    protected String doInBackground(String... params) {
        String messageContent = "Your new password code for CHEAPPOS is " + passwordCode + ". " +
                "Please do not reply, this is only a automated email";

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
            result = "Error sending email";
        } catch(Exception e) {
            e.printStackTrace();
            result = "Error sending email";
        }


        return result;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
        ((OnSendingEmail)context).hideProgress();
    }

    public interface OnSendingEmail{
        void showProgress(String message);
        void hideProgress();
    }
}
