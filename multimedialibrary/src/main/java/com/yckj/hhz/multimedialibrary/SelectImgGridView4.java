package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

/**
 * @author hhz
 * @time 2018/4/4 11:37
 * @description
 */

public class SelectImgGridView4 extends ViewGroup {
    Context context;

    ArrayList<MediaBean> mediaBeans;
    int spanCount = 4;
    int maxCount = 1;
    boolean isMultiple = false;
    boolean isCrop = false;
    int itemPadding = 2;

    private int mViewWidth; // 视图的宽度
    private int mViewHeight; // 视图的高度
    private int mItemSpace;// 单元格间距

    private int total_col; // 列
    private int total_row; // 行

    private float mDownX;
    private float mDownY;
    private int touchSlop; //

    public SelectImgGridView4(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SelectImgGridView4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SelectImgGridView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
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

        mediaBeans = new ArrayList<>();
        maxCount = maxCount == 0 ? spanCount * spanCount : maxCount;
        if (maxCount > 1) {
            isMultiple = true;
        }
        //typedArray.recycle();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        //requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < (mediaBeans.size() >= maxCount ? maxCount : mediaBeans.size() + 1); i++) {
            total_col = spanCount;
            int horizontalCount = (i + 1) % spanCount == 0 ? spanCount : (i + 1) % spanCount;
            int right = mItemSpace * horizontalCount;
            total_row = (i + 1) / spanCount + (horizontalCount == spanCount ? 0 : 1);
            int bottom = mItemSpace * total_row;

            //FrameLayout frameLayout=new FrameLayout(context);
            //View view= LayoutInflater.from(context).inflate(R.layout.layout_select_img_item, this,false);
            //frameLayout.addView(view,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//            LayoutParams param = new MarginLayoutParams(imageInfoList.get(i).width, imageInfoList.get(i).height);
//            addView(imageView, param);
            addView(imageView);
            imageView.layout(right - mItemSpace, bottom - mItemSpace, right, bottom);
            if (i < mediaBeans.size()) {
                Glide.with(context).load(mediaBeans.get(i).getOriginalPath()).into(imageView);
            } else {
                //imageView.setImageResource(R.drawable.btn_add_img_selector);
                Glide.with(context).load(R.drawable.btn_add_img_normal).into(imageView);
            }
            final int finalI = i;
            View child = getChildAt(i);/*
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    onGridViewItemClick(finalI);
                }
            });*/
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int count = mediaBeans.size() == maxCount ? mediaBeans.size() : (mediaBeans.size() + 1);
        int verticalCount = count / spanCount + (count % spanCount > 0 ? 1 : 0);
        mItemSpace = widthSize / spanCount;
        int heightSize = mItemSpace * verticalCount;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mItemSpace + 1);
                    int row = (int) (mDownY / mItemSpace + 1);
                    measureClickItem(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 计算点击的item
     *
     * @param col
     * @param row
     */
    private void measureClickItem(int col, int row) {
        if (col > total_col || row > total_row)
            return;
        int position = (row - 1) * spanCount + col - 1;
        onGridViewItemClick(position);
        update();
    }

    /**
     * 刷新界面
     */
    public void update() {
        //fillDate();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mItemSpace = mViewWidth / spanCount;
    }

    RxGalleryFinal rxGalleryFinal;

    public void onGridViewItemClick(int position) {
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
                            //selectImgAdapter.notifyDataSetChanged();
                            update();

                            if (selectImgListener != null) {
                                selectImgListener.onMultipleResult(resultEvent);
                            }
                        }
                    });
        else
            rxGalleryFinal.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                @Override
                protected void onEvent(ImageRadioResultEvent resultEvent) throws Exception {
                    mediaBeans.clear();
                    mediaBeans.add(resultEvent.getResult());
                    //selectImgAdapter.notifyDataSetChanged();
                    update();
                    if (selectImgListener != null) {
                        selectImgListener.onOneResult(resultEvent);
                    }
                }
            });
        if (position >= mediaBeans.size()) {
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

    public SelectImgGridView.SelectImgListener getSelectImgListener() {
        return selectImgListener;
    }

    /**
     * @param selectImgListener
     */
    public void setSelectImgListener(SelectImgGridView.SelectImgListener selectImgListener) {
        this.selectImgListener = selectImgListener;
    }

    SelectImgGridView.SelectImgListener selectImgListener;

    public interface SelectImgListener {
        void onOneResult(ImageRadioResultEvent resultEvent);

        void onMultipleResult(ImageMultipleResultEvent resultEvent);
    }
}
