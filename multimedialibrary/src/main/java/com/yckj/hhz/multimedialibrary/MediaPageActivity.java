package com.yckj.hhz.multimedialibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPreviewFragment;

public class MediaPageActivity extends AppCompatActivity {

    MediaPreviewFragment mMediaPreviewFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_page);
    }
}
