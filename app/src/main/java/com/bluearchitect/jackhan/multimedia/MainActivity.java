package com.bluearchitect.jackhan.multimedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yckj.hhz.multimedialibrary.SelectImgGridView;

public class MainActivity extends AppCompatActivity {

    SelectImgGridView selectImgGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectImgGridView = (SelectImgGridView) findViewById(R.id.selectImgGridView);
        selectImgGridView.setMaxCount(12);
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
