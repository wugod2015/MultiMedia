package com.yckj.hhz.multimedialibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.bumptech.glide.Glide;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;
import uk.co.senab.photoview.PhotoView;

public class ShowBigImgActivity extends AppCompatActivity {

    MediaBean mediaBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_img);
        mediaBean = getIntent().getParcelableExtra("MediaBean");
        int mPageColor = ThemeUtils.resolveColor(this, R.attr.gallery_page_bg, R.color.gallery_default_page_bg);

        PhotoView ivImage = (PhotoView) findViewById(R.id.iv_media_image);
        String path = null;
        if (mediaBean.getWidth() > 1200 || mediaBean.getHeight() > 1200) {
            path = mediaBean.getThumbnailBigPath();
        }
        if (TextUtils.isEmpty(path)) {
            path = mediaBean.getOriginalPath();
        }
        ivImage.setBackgroundColor(mPageColor);

        Glide.with(this).load(mediaBean.getOriginalPath()).into(ivImage);
        /*mConfiguration.getImageLoader().displayImage(mContext, path, ivImage,mDefaultImage, mConfiguration.getImageConfig(),
                false, mScreenWidth, mScreenHeight, mediaBean.getOrientation());*/
    }
}
