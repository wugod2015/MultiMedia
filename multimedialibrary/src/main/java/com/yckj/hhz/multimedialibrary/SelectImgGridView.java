package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class SelectImgGridView extends LinearLayout implements RecyclerView.RecyclerListener, SelectImgAdapter.OnRecyclerViewItemClickListener {
    public SelectImgGridView(Context context) {
        this(context, null);
    }

    public SelectImgGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    Context context;

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    SelectImgAdapter selectImgAdapter;
    List<MediaBean> mediaBeans;
    int spanCount = 4;
    int maxCount;
    boolean isMultiple = true;
    boolean isCrop = true;

    public SelectImgGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_select_img_gridview, this, true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        mediaBeans = new ArrayList<>();
        maxCount = maxCount == 0 ? spanCount * spanCount : maxCount;
        selectImgAdapter = new SelectImgAdapter(context, mediaBeans, spanCount, maxCount);
        recyclerView.setAdapter(selectImgAdapter);
        selectImgAdapter.setOnRecyclerViewItemClickListener(this);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        if (position == mediaBeans.size()) {

            RxGalleryFinal rxGalleryFinal = RxGalleryFinal
                    .with(context)
                    .image()
                    .radio()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .selected(mediaBeans);
            if (isCrop)
                rxGalleryFinal.crop();
            if (isMultiple)
                rxGalleryFinal.maxSize(maxCount)
                        .multiple()
                        .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {

                            @Override
                            protected void onEvent(ImageMultipleResultEvent resultEvent) throws Exception {
                                mediaBeans.clear();
                                mediaBeans.addAll(resultEvent.getResult());
                                selectImgAdapter.notifyDataSetChanged();

                            }
                        });
            else
                rxGalleryFinal.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent resultEvent) throws Exception {
                        mediaBeans.add(resultEvent.getResult());
                        selectImgAdapter.notifyDataSetChanged();
                    }
                });
            rxGalleryFinal.openGallery();
        } else {

        }
    }
}
