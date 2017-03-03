package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.fragments.MenuOptionFragment;

import java.util.List;

/**
 * Created by DanLuciano on 2/17/2017.
 */

public class MenuPagerAdapter extends FragmentPagerAdapter {

    Context context;
    MenuOptionFragment menuOptionFragment;
    List<Long> idList;
    int pageCount;


    public MenuPagerAdapter(FragmentManager fm, Context context, List<Long> idList) {
        super(fm);
        this.context = context;
        this.idList = idList;
        pageCount = idList.size()/4;
    }

    @Override
    public Fragment getItem(int position) {

        menuOptionFragment = new MenuOptionFragment();

        int idPosition = (position + 1) * 4;

        Bundle bundle = new Bundle();
        bundle.putLong("id1", idList.get(idPosition - 4));
        bundle.putLong("id2", idList.get(idPosition - 3));
        bundle.putLong("id3", idList.get(idPosition - 2));
        bundle.putLong("id4", idList.get(idPosition - 1));
        menuOptionFragment.setArguments(bundle);

        return menuOptionFragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

}
