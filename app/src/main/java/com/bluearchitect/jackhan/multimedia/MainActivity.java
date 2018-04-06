package com.bluearchitect.jackhan.multimedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yckj.hhz.multimedialibrary.SelectImgGridView;
import com.yckj.hhz.multimedialibrary.SelectImgGridView1;
import com.yckj.hhz.multimedialibrary.SelectImgGridView2;

import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class MainActivity extends AppCompatActivity {

    SelectImgGridView1 selectImgGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*selectImgGridView = (SelectImgGridView1) findViewById(R.id.selectImgGridView);
        selectImgGridView.init();
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
