package com.droidcoder.gdgcorp.posproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.UserRole;
import com.droidcoder.gdgcorp.posproject.datasystem.CurrentUser;
import com.droidcoder.gdgcorp.posproject.fragments.DateFilterFragment;
import com.droidcoder.gdgcorp.posproject.fragments.EmployeeFormFragment;
import com.droidcoder.gdgcorp.posproject.fragments.GraphAveCustomerFragment;
import com.droidcoder.gdgcorp.posproject.fragments.GraphSalesFragment;
import com.droidcoder.gdgcorp.posproject.fragments.GraphTopItems;
import com.droidcoder.gdgcorp.posproject.fragments.GraphTransactionFragment;
import com.droidcoder.gdgcorp.posproject.fragments.ProgressFragment;
import com.droidcoder.gdgcorp.posproject.fragments.RoleSummaryFragment;
import com.droidcoder.gdgcorp.posproject.fragments.UserFormFragment;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navactivities.SettingsActivity;
import com.droidcoder.gdgcorp.posproject.navfragments.CustomerFragment;
import com.droidcoder.gdgcorp.posproject.navfragments.EmployeesFragment;
import com.droidcoder.gdgcorp.posproject.navfragments.InvoicesFragment;
import com.droidcoder.gdgcorp.posproject.navfragments.MissingPageFragment;
import com.droidcoder.gdgcorp.posproject.navfragments.ReportsFragment;
import com.droidcoder.gdgcorp.posproject.utils.AsyncCheckEmail;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import java.util.Date;
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

public class NavigationActivity extends BaseCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncCheckEmail.OnCheckingEmail, DateFilterFragment.SetOnDateFilter {

    //butterknife sample init
    @BindView(R.id.content_main) FrameLayout content_main;
    @BindView(R.id.main_frame) FrameLayout mainFrame;
    @BindView(R.id.yearlySales)LinearLayout yearlySales;
    @BindView(R.id.topItems)LinearLayout topItems;
    @BindView(R.id.transType)LinearLayout transType;
    @BindView(R.id.averageCustomer)LinearLayout averageCustomer;

    //user
    NavigationView navigationView;

    LinearLayout userMain;
    ImageView imageUser;
    TextView userRole;
    TextView userEmail;

    FragmentManager fm;
    ProgressFragment progressFragment;
    Fragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        fm = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        userMain = (LinearLayout) header.findViewById(R.id.userMainLayout);
        userEmail = (TextView) header.findViewById(R.id.txtUserEmail);
        userRole = (TextView) header.findViewById(R.id.txtUserRole);
        imageUser = (ImageView) header.findViewById(R.id.imageUser);
        

        //populate user
        imageUser.setImageBitmap(ImageConverter.bytesToBitmap(CurrentUser.getUser().getImage()));
        if(CurrentUser.getUser().getUserRoleId() > 0){
            UserRole role = DBHelper.getDaoSession().getUserRoleDao().load(CurrentUser.getUser().getUserRoleId());
            userRole.setText(role.getRoleName());
            navigationView.getMenu().findItem(R.id.nav_sales).setVisible(role.getAllowSales());
            navigationView.getMenu().findItem(R.id.nav_invoice).setVisible(role.getAllowInvoice());
            navigationView.getMenu().findItem(R.id.nav_reports).setVisible(role.getAllowReport());
            navigationView.getMenu().findItem(R.id.nav_inventory).setVisible(role.getAllowInventory());
            navigationView.getMenu().findItem(R.id.nav_customer).setVisible(role.getAllowInventory());
            navigationView.getMenu().findItem(R.id.nav_employee).setVisible(role.getAllowEmployee());
            navigationView.getMenu().findItem(R.id.nav_dataMigration).setVisible(role.getAllowData());
            navigationView.getMenu().findItem(R.id.nav_settings).setVisible(role.getAllowSetting());

        }else{
            userRole.setText("ADMIN");
        }
        userEmail.setText(CurrentUser.getUser().getEmail());
        userMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();

                UserFormFragment userFormFragment = new UserFormFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("userId", CurrentUser.getUser().getId());
                userFormFragment.setArguments(bundle);
                ft.replace(content_main.getId(), userFormFragment, "userForm");
                ft.commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        settingsFragment = new GraphSalesFragment();
        getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "salesSetting").commit();
        yearlySales.setBackground(getResources().getDrawable(R.drawable.line_below));

    }

    public void selectLeftNavigation(View v){

        switch(v.getId()){
            case R.id.yearlySales:
                settingsFragment = new GraphSalesFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "graphSales").commit();
                yearlySales.setBackground(getResources().getDrawable(R.drawable.line_below));
                transType.setBackground(null);
                averageCustomer.setBackground(null);
                topItems.setBackground(null);
                break;

            case R.id.transType:
                settingsFragment = new GraphTransactionFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "graphTransaction").commit();
                yearlySales.setBackground(null);
                transType.setBackground(getResources().getDrawable(R.drawable.line_below));
                averageCustomer.setBackground(null);
                topItems.setBackground(null);
                break;

            case R.id.averageCustomer:
                settingsFragment = new GraphAveCustomerFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "graphAveCustomer").commit();
                yearlySales.setBackground(null);
                transType.setBackground(null);
                averageCustomer.setBackground(getResources().getDrawable(R.drawable.line_below));
                topItems.setBackground(null);

                break;

            case R.id.topItems:
                settingsFragment = new GraphTopItems();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "graphTransaction").commit();
                yearlySales.setBackground(null);
                topItems.setBackground(getResources().getDrawable(R.drawable.line_below));
                averageCustomer.setBackground(null);
                transType.setBackground(null);
                break;

        }
    }

    public void refreshDashBoard(){
        settingsFragment = new GraphSalesFragment();
        getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "graphSales").commit();
        yearlySales.setBackground(getResources().getDrawable(R.drawable.line_below));
        transType.setBackground(null);
        averageCustomer.setBackground(null);
        topItems.setBackground(null);
    }

    @Override
    public void onDateFilter(Date startDate, Date endDate) {

        if(settingsFragment != null && settingsFragment instanceof GraphTransactionFragment){

            ((GraphTransactionFragment)settingsFragment).createPieChart(startDate, endDate);

        }else if(settingsFragment != null && settingsFragment instanceof GraphAveCustomerFragment){

            ((GraphAveCustomerFragment)settingsFragment).createLineChart(startDate, endDate);

        }else if(settingsFragment != null && settingsFragment instanceof GraphTopItems){

            ((GraphTopItems)settingsFragment).createTopItemChart(startDate, endDate);

        }

        if(fm.findFragmentByTag("ReportsFragment") != null && fm.findFragmentByTag("ReportsFragment").isVisible()){

            ((ReportsFragment)fm.findFragmentByTag("ReportsFragment")).generateReport(startDate, endDate);

        }

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Warning");
            alertDialogBuilder
                    .setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            NavigationActivity.super.onBackPressed();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);

        if(CurrentUser.getUser().getUserRoleId() > 0) {
            UserRole role = DBHelper.getDaoSession().getUserRoleDao().load(CurrentUser.getUser().getUserRoleId());
            menu.findItem(R.id.nav_settings).setVisible(role.getAllowSetting());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if(id == R.id.nav_logout){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Warning");
            alertDialogBuilder
                    .setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            NavigationActivity.super.onBackPressed();
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        String name = item.getTitleCondensed().toString().trim();
        if (name.equals("")) {
            name = item.getTitle().toString().trim();
        }
        name = name.replaceAll(" ", "");

        /*
        If item is checkable, the content is a fragment and will be displayed in the main content layout.
        Otherwise, the content is an intent or activity that can be launched separately.
        */
        if (item.isCheckable()) {
            try {
                Class<?> cls = Class.forName(getPackageName().concat(".navfragments.").concat(name.concat("Fragment")));
                Fragment fragment = (Fragment) cls.getConstructor().newInstance();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(content_main.getId(), fragment, name.concat("Fragment"));
                Toast.makeText(this, "" + name.concat("Fragment"), Toast.LENGTH_SHORT).show();
                ft.commit();
            } catch (ClassNotFoundException ex) {
                Fragment fragment = new MissingPageFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(content_main.getId(), fragment, name.concat("Fragment"));
                ft.commit();
            } catch (NoSuchMethodException ex) {
                logE("No such method. " + ex.getMessage());
            } catch (Exception ex) {
                logE(ex.getMessage());
            }
        } else {
            try {
                Class<?> cls = Class.forName(getPackageName().concat(".navactivities.").concat(name.concat("Activity")));
                Intent intent = new Intent(this, cls);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                logE(ex.getMessage());
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void refreshList(){

        if(fm.findFragmentByTag("CustomerFragment") != null && fm.findFragmentByTag("CustomerFragment").isVisible()){
            ((CustomerFragment)fm.findFragmentByTag("CustomerFragment")).refreshList();
        }

        if(fm.findFragmentByTag("EmployeesFragment") != null && fm.findFragmentByTag("EmployeesFragment").isVisible()){
            ((EmployeesFragment)fm.findFragmentByTag("EmployeesFragment")).refreshEmployeeList();
        }

        if(fm.findFragmentByTag("roleSummary") != null && fm.findFragmentByTag("roleSummary").isVisible()){
            ((RoleSummaryFragment)fm.findFragmentByTag("roleSummary")).refreshList();
        }

        if(fm.findFragmentByTag("InvoicesFragment") != null && fm.findFragmentByTag("InvoicesFragment").isVisible()){
            ((InvoicesFragment)fm.findFragmentByTag("InvoicesFragment")).refreshInvoice();
        }

    }

    public void showRoleMaintenance(){

        FragmentTransaction ft = fm.beginTransaction();

        RoleSummaryFragment roleSummaryFragment = new RoleSummaryFragment();
        ft.replace(content_main.getId(), roleSummaryFragment, "roleSummary");
        ft.commit();
    }

    public void showEmployeeMaintenance(){

        FragmentTransaction ft = fm.beginTransaction();

        EmployeesFragment employeesFragment = new EmployeesFragment();
        ft.replace(content_main.getId(), employeesFragment, "EmployeesFragment");
        ft.commit();
    }

    @Override
    public void onFinish(boolean emailExist, String email, String passCode) {
        if(emailExist){
            if(fm.findFragmentByTag("employeeForm")!=null && ((EmployeeFormFragment)fm.findFragmentByTag("employeeForm")).isVisible()){
                ((EmployeeFormFragment)fm.findFragmentByTag("employeeForm")).setEmailInvalid(emailExist);
            }
        }else{
            new AsyncSendToEmail(this, passCode).execute(email);
            ((EmployeeFormFragment)fm.findFragmentByTag("employeeForm")).setEmailInvalid(emailExist);

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
            progressFragment.show(getSupportFragmentManager(), "progress");

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
        }

    }

}
