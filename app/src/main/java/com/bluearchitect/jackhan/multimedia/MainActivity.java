package com.bluearchitect.jackhan.multimedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yckj.hhz.multimedialibrary.SelectImgGridView;

import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class MainActivity extends AppCompatActivity {

    SelectImgGridView selectImgGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectImgGridView = (SelectImgGridView) findViewById(R.id.selectImgGridView);
        selectImgGridView.getSelectedPathList();
        selectImgGridView.setSelectImgListener(new SelectImgGridView.SelectImgListener() {
            @Override
            public void onOneResult(ImageRadioResultEvent resultEvent) {

            }

            @Override
            public void onMultipleResult(ImageMultipleResultEvent resultEvent) {

            }
        });
    }
}
