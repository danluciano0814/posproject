package com.droidcoder.gdgcorp.posproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.droidcoder.gdgcorp.posproject.fragments.SalesSettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/28/2017.
 */

public class SettingsActivity extends BaseCompatActivity {

    @BindView(R.id.main_frame)FrameLayout mainFrame;
    @BindView(R.id.salesComputation)LinearLayout salesComputation;

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
                getSupportFragmentManager().beginTransaction().replace(mainFrame.getId(), settingsFragment, "saleSetting").commit();
                salesComputation.setBackground(getResources().getDrawable(R.drawable.line_below));
                //lnDiscount.setBackground(null);
                break;

        }
    }

}
