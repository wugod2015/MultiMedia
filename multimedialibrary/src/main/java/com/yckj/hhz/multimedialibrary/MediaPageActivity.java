package com.yckj.hhz.multimedialibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPreviewFragment;

public class MediaPageActivity extends AppCompatActivity {

    MediaPreviewFragment mMediaPreviewFragment;
    ImageMultipleResultEvent resultEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_page);
    }
}
