package com.droidcoder.gdgcorp.posproject.navactivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.droidcoder.gdgcorp.posproject.BaseCompatActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.fragments.CustomerSettingFragment;
import com.droidcoder.gdgcorp.posproject.fragments.SalesSettingFragment;
import com.droidcoder.gdgcorp.posproject.fragments.StoreSettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/28/2017.
 */

public class SettingsActivity extends BaseCompatActivity {

    @BindView(R.id.main_frame)FrameLayout mainFrame;
    @BindView(R.id.salesComputation)LinearLayout salesComputation;
    @BindView(R.id.customerSettings)LinearLayout customerSettings;
    @BindView(R.id.storeSettings)LinearLayout storeSettings;

    Fragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        settingsFragment = new SalesSettingFragment();
        getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "productSummary").commit();
        salesComputation.setBackground(getResources().getDrawable(R.drawable.line_below));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectLeftNavigation(View v){

        switch(v.getId()){
            case R.id.salesComputation:
                settingsFragment = new SalesSettingFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "salesSetting").commit();
                salesComputation.setBackground(getResources().getDrawable(R.drawable.line_below));
                customerSettings.setBackground(null);
                storeSettings.setBackground(null);
                break;

            case R.id.customerSettings:
                settingsFragment = new CustomerSettingFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "customerSetting").commit();
                salesComputation.setBackground(null);
                customerSettings.setBackground(getResources().getDrawable(R.drawable.line_below));
                storeSettings.setBackground(null);
                break;

            case R.id.storeSettings:
                settingsFragment = new StoreSettingFragment();
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "storeSetting").commit();
                salesComputation.setBackground(null);
                customerSettings.setBackground(null);
                storeSettings.setBackground(getResources().getDrawable(R.drawable.line_below));
                break;
        }
    }

}
