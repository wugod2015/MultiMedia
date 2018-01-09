package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
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
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;

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
    ArrayList<MediaBean> mediaBeans;
    int spanCount = 4;
    int maxCount = 1;
    boolean isMultiple = false;
    boolean isCrop = false;

    public SelectImgGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SelectImgGridView, defStyleAttr, 0);

        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.SelectImgGridView_is_crop) {
                isCrop = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.SelectImgGridView_max_count) {
                maxCount = typedArray.getInteger(attr, 1);
            } else if (attr == R.styleable.SelectImgGridView_span_count) {
                spanCount = typedArray.getInteger(attr, 4);
            }

        }

        LayoutInflater.from(context).inflate(R.layout.layout_select_img_gridview, this, true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        mediaBeans = new ArrayList<>();
        maxCount = maxCount == 0 ? spanCount * spanCount : maxCount;
        if (maxCount > 1) {
            isMultiple = true;
        }
        selectImgAdapter = new SelectImgAdapter(context, mediaBeans, spanCount, maxCount);
        recyclerView.setAdapter(selectImgAdapter);
        selectImgAdapter.setOnRecyclerViewItemClickListener(this);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    RxGalleryFinal rxGalleryFinal;

    public RxGalleryFinal getRxGalleryFinal() {
        return rxGalleryFinal;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        if (rxGalleryFinal == null) {
            rxGalleryFinal = RxGalleryFinal
                    .with(context)
                    .image()
                    .radio()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .selected(mediaBeans);
            if (isCrop)
                rxGalleryFinal.crop();
        }
        if (isMultiple)
            rxGalleryFinal.maxSize(maxCount)
                    .multiple()
                    .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {

                        @Override
                        protected void onEvent(ImageMultipleResultEvent resultEvent) throws Exception {
                            mediaBeans.clear();
                            mediaBeans.addAll(resultEvent.getResult());
                            selectImgAdapter.notifyDataSetChanged();

                            selectImgListener.onMultipleResult(resultEvent);
                        }
                    });
        else
            rxGalleryFinal.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                @Override
                protected void onEvent(ImageRadioResultEvent resultEvent) throws Exception {
                    mediaBeans.add(resultEvent.getResult());
                    selectImgAdapter.notifyDataSetChanged();
                    if (selectImgListener != null) {
                        selectImgListener.onOneResult(resultEvent);
                    }
                }
            });
        if (position == mediaBeans.size()) {
            rxGalleryFinal.openGallery();
        } else {
            rxGalleryFinal.openGallery(position);
            /*Intent intent = new Intent(context, ShowBigImgActivity.class);
            intent.putExtra("MediaBean", mediaBeans.get(position));
            context.startActivity(intent);*/
            /*Intent intent = new Intent(context, MediaActivity.class);
            intent.putExtra("isShowSelected",true);
            Bundle bundle = new Bundle();
            bundle.putParcelable(MediaActivity.EXTRA_CONFIGURATION, rxGalleryFinal.configuration);
            intent.putExtras(bundle);
            intent.putExtra("PreviewPosition", position);
            context.startActivity(intent);*/
        }
    }

    public SelectImgListener getSelectImgListener() {
        return selectImgListener;
    }

    public void setSelectImgListener(SelectImgListener selectImgListener) {
        this.selectImgListener = selectImgListener;
    }

    SelectImgListener selectImgListener;

    public interface SelectImgListener {
        void onOneResult(ImageRadioResultEvent resultEvent);

        void onMultipleResult(ImageMultipleResultEvent resultEvent);
    }
}
