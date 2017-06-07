package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.droidcoder.gdgcorp.posproject.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 5/27/2017.
 */

public class TutorialFragment extends BaseDialogFragment {

    @BindView(R.id.btnPDF)Button btnPDF;
    @BindView(R.id.btnVideo)Button btnVideo;
    @BindView(R.id.btnCancel)Button btnCancel;

    private static final int  MEGABYTE = 1024 * 1024;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadTutorialFile();
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo("l1NPcLs3Uow");
            }
        });


    }

    public void downloadTutorialFile(){
        File folder = new File(Environment.getExternalStoragePublicDirectory("/Cheappos") + "/");
        InputStream file = null;

        try{
            file = getActivity().getApplicationContext().getAssets().open("tutorial.pdf");
        }catch (IOException ex){
            ex.printStackTrace();
        }

        folder.mkdir();

        File pdfFile = new File(folder, "tutorial.pdf");

        try{
            pdfFile.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pdfFile);

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = file.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(Environment.getExternalStoragePublicDirectory("/Cheappos"),
                "tutorial.pdf");

        Uri path;

        if(Build.VERSION.SDK_INT < 24){
            System.out.println("*** version below 24");
            path = Uri.fromFile(f);
        } else {
            System.out.println("*** version 24 and up");

            path = FileProvider.getUriForFile(getActivity(),
                    getActivity().getApplicationContext().getPackageName() + ".provider",
                    f);
        }

        System.out.println(path.getPath());

        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void watchYoutubeVideo(String id){

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

}
