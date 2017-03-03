package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.droidcoder.gdgcorp.posproject.Adapter.DiscountSummaryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Discount;
import com.droidcoder.gdgcorp.posproject.dataentity.DiscountDao;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/26/2017.
 */

public class DiscountSummaryFragment extends BaseFragment {

    @BindView(R.id.btnCreate)Button btnCreate;
    @BindView(R.id.lv_discount_summary)ListView lvDiscountSummary;

    DiscountFormFragment discountFormFragment;
    DiscountSummaryAdapter discountSummaryAdapter;
    List<Discount> discountList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount_summary, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        discountList = DBHelper.getDaoSession().getDiscountDao().queryBuilder()
                .orderAsc(DiscountDao.Properties.Name).list();

        discountSummaryAdapter = new DiscountSummaryAdapter(getActivity(), discountList);
        lvDiscountSummary.setAdapter(discountSummaryAdapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountFormFragment = new DiscountFormFragment();
                Bundle bundle = new Bundle();
                discountFormFragment.setArguments(bundle);
                discountFormFragment.show(getActivity().getSupportFragmentManager(), "discount_form");
            }
        });

        lvDiscountSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                discountFormFragment = new DiscountFormFragment();
                Bundle bundle = new Bundle();
                bundle.putString("discountId",((TextView)view.findViewById(R.id.txtDiscountId)).getText().toString());
                discountFormFragment.setArguments(bundle);
                discountFormFragment.show(getActivity().getSupportFragmentManager(), "discount_form");
            }
        });

    }

    public void refreshList(){
        discountList = DBHelper.getDaoSession().getDiscountDao().queryBuilder()
                .orderAsc(DiscountDao.Properties.Name).list();
        lvDiscountSummary.setAdapter(new DiscountSummaryAdapter(getActivity(),discountList));
    }

}
