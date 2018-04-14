package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.content.res.TypedArray;
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

public class SelectImgRecyclerView extends LinearLayout implements RecyclerView.RecyclerListener, SelectImgRecyclerAdapter.OnRecyclerViewItemClickListener {
    public SelectImgRecyclerView(Context context) {
        this(context, null);
    }

    public SelectImgRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    Context context;

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    SelectImgRecyclerAdapter selectImgRecyclerAdapter;
    ArrayList<MediaBean> mediaBeans;
    int spanCount = 4;
    int maxCount = 1;
    boolean isCrop = false;

    public SelectImgRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SelectImgRecyclerView, defStyleAttr, 0);

        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.SelectImgRecyclerView_is_crop) {
                isCrop = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.SelectImgRecyclerView_max_count) {
                maxCount = typedArray.getInteger(attr, 1);
            } else if (attr == R.styleable.SelectImgRecyclerView_span_count) {
                spanCount = typedArray.getInteger(attr, 4);
            }

        }

        LayoutInflater.from(context).inflate(R.layout.layout_select_img_recyclerview, this, true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        mediaBeans = new ArrayList<>();
        maxCount = maxCount == 0 ? spanCount * spanCount : maxCount;

        selectImgRecyclerAdapter = new SelectImgRecyclerAdapter(context, mediaBeans, spanCount, maxCount);
        recyclerView.setAdapter(selectImgRecyclerAdapter);
        selectImgRecyclerAdapter.setOnRecyclerViewItemClickListener(this);
        setSelectImgView();
    }

    public void setSelectImgView() {
        selectImgRecyclerAdapter = new SelectImgRecyclerAdapter(context, mediaBeans, spanCount, maxCount);
        recyclerView.setAdapter(selectImgRecyclerAdapter);
        selectImgRecyclerAdapter.setOnRecyclerViewItemClickListener(this);
    }

    /**
     * 最大选择数量
     *
     * @param maxCount
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        setSelectImgView();
    }

    /**
     * 每行列数
     *
     * @param spanCount
     */
    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        gridLayoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        maxCount = maxCount == 0 ? spanCount * spanCount : maxCount;

        setSelectImgView();
    }

    /**
     * 是否剪裁图片
     *
     * @param isCrop
     */
    public void setIsCrop(boolean isCrop) {
        this.isCrop = isCrop;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    RxGalleryFinal rxGalleryFinal;

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        if (rxGalleryFinal == null) {
            rxGalleryFinal = RxGalleryFinal
                    .with(context)
                    .image()
                    .radio()
                    .imageLoader(ImageLoaderType.GLIDE);
            if (isCrop)
                rxGalleryFinal.crop();
        }
        if (maxCount > 1) {
            rxGalleryFinal.selected(mediaBeans);
            rxGalleryFinal.maxSize(maxCount)
                    .multiple()
                    .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {

                        @Override
                        protected void onEvent(ImageMultipleResultEvent resultEvent) throws Exception {
                            mediaBeans.clear();
                            mediaBeans.addAll(resultEvent.getResult());
                            selectImgRecyclerAdapter.notifyDataSetChanged();

                            selectImgListener.onMultipleResult(resultEvent);
                        }
                    });
        } else {
            rxGalleryFinal.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                @Override
                protected void onEvent(ImageRadioResultEvent resultEvent) throws Exception {
                    mediaBeans.clear();
                    mediaBeans.add(resultEvent.getResult());
                    selectImgRecyclerAdapter.notifyDataSetChanged();
                    if (selectImgListener != null) {
                        selectImgListener.onOneResult(resultEvent);
                    }
                }
            });
        }
        if (position == mediaBeans.size() || (position == 0 && maxCount == 1)) {
            rxGalleryFinal.openGallery();
        } else {
            rxGalleryFinal.openGallery(position);
            /*Intent intent = new Intent(context, ShowBigImgActivity.class);
            intent.putExtra("MediaBean", mediaBeans.get(position));
            context.startActivity(intent);*/
        }
    }

    /**
     * 获取选择的图片地址
     *
     * @return
     */
    public List<String> getSelectedPathList() {
        List<String> pathList = new ArrayList<>();
        for (MediaBean item : mediaBeans) {
            pathList.add(item.getOriginalPath());
        }
        return pathList;
    }

    public List<MediaBean> getSelectedMediaBeanList() {
        return mediaBeans;
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
