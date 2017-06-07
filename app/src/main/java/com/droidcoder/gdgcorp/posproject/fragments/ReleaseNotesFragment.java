package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 6/3/2017.
 */

public class ReleaseNotesFragment extends BaseDialogFragment {

    @BindView(R.id.btnCancel)Button btnCancel;
    @BindView(R.id.txtReleaseVersion)TextView txtVersion;
    @BindView(R.id.txtContent)TextView txtContent;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_release_notes, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if(!getArguments().isEmpty()){

            txtVersion.setText(getArguments().getString("releaseVersion", ""));
            txtContent.setText(getArguments().getString("content", ""));

        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                LFHelper.saveLocalData(getActivity(), GlobalConstants.RELEASE_VERSION_1_0_1, "shown");
            }
        });

    }
}
