package com.droidcoder.gdgcorp.posproject.navactivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.BaseCompatActivity;
import com.droidcoder.gdgcorp.posproject.R;

/**
 * Created by DanLuciano on 12/28/2016.
 */

public class CollectionsActivity extends BaseCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(this, getSupportActionBar().getTitle(), Toast.LENGTH_SHORT).show();
    }
}
