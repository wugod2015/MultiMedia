package com.bluearchitect.jackhan.multimedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yckj.hhz.multimedialibrary.SelectImgGridView;
import com.yckj.hhz.multimedialibrary.SelectImgRecyclerView;

import java.io.File;
import java.util.List;

import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class MainActivity extends AppCompatActivity {

    SelectImgGridView selectImgGridView;
    SelectImgRecyclerView selectImgRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectImgGridView = (SelectImgGridView) findViewById(R.id.selectImgGridView);
//        selectImgGridView.setMaxCount(12);
        selectImgRecyclerView = (SelectImgRecyclerView) findViewById(R.id.selectImgRecyclerView);
//        selectImgGridView.setMaxCount(12);
//        selectImgGridView.setIsCrop(true);
//        selectImgGridView.setSpanCount(4);
//        selectImgGridView.setSelectImgListener(new SelectImgGridView.SelectImgListener() {
//            @Override
//            public void onOneResult(ImageRadioResultEvent resultEvent) {
//
//            }
//
//            @Override
//            public void onMultipleResult(ImageMultipleResultEvent resultEvent) {
//
//            }
//        });
//        List<String> pathList = selectImgRecyclerView.getSelectedPathList();
//        List<File> fileList = selectImgRecyclerView.getSelectedFileList();
        /*selectImgGridView.init();
        selectImgGridView.getSelectedPathList();
        selectImgGridView.setSelectImgListener(new SelectImgGridView.SelectImgListener() {
            @Override
            public void onOneResult(ImageRadioResultEvent resultEvent) {

            }

            @Override
            public void onMultipleResult(ImageMultipleResultEvent resultEvent) {

            }
        });*/
    }
}
