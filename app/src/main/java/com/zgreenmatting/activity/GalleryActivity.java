package com.zgreenmatting.activity;


import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zgreenmatting.BaseActivity;
import com.zgreenmatting.R;
import com.zgreenmatting.adapter.ImageAdapter;
import com.zgreenmatting.adapter.SpacesItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GalleryActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private ImageAdapter adapter;
    private List<String> imageList;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void preInitView() {
    }

    @Override
    protected void preInitData() {
        getImagesFromSD();
        LinearLayoutManager layoutManager = new LinearLayoutManager(GalleryActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.addItemDecoration(new SpacesItemDecoration(5));
        adapter = new ImageAdapter(this,imageList);
        recycler_view.setAdapter(adapter);
    }

    private List<String> getImagesFromSD() {
        imageList = new ArrayList<>();
        File f = Environment.getExternalStorageDirectory();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ZGreenMatting");
        } else {
            Toast.makeText(GalleryActivity.this, "sdcarderror", Toast.LENGTH_LONG).show();
            return imageList;
        }

        File[] files = f.listFiles();
        if (files == null || files.length == 0)
            return imageList;

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isImageFile(file.getPath()))
                imageList.add(file.getPath());
        }
        return imageList;
    }

    private boolean isImageFile(String fName) {
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return true;
        }
        return false;
    }
}
