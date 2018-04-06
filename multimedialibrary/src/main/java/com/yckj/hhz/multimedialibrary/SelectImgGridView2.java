package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
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
 * @author hhz
 * @time 2018/4/4 11:37
 * @description
 */

public class SelectImgGridView2 extends View {
    Context context;

    ArrayList<MediaBean> mediaBeans;
    int spanCount = 4;
    int maxCount = 1;
    boolean isMultiple = false;
    boolean isCrop = false;
    //int verticalSpacing = 2;
    //int horizontalSpacing = 2;
    int itemPadding = 1;

    private int mViewWidth; // 视图的宽度
    private int mViewHeight; // 视图的高度
    private int mItemSpace;// 单元格间距

    private int total_col; // 列
    private int total_row; // 行

    private float mDownX;
    private float mDownY;
    private int touchSlop; //

    public SelectImgGridView2(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SelectImgGridView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SelectImgGridView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /*public SelectImgGridView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

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

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawItems(canvas);
    }

    public void drawItems(Canvas canvas) {
        for (int i = 0; i < (mediaBeans.size() >= maxCount ? maxCount : mediaBeans.size() + 1); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.btn_add_img_normal);
            total_col = spanCount;
            int horizontalCount = (i + 1) % spanCount == 0 ? spanCount : (i + 1) % spanCount;
            int left = mItemSpace * horizontalCount;
            total_row = (i + 1) / spanCount + (horizontalCount == spanCount ? 0 : 1);
            int top = mItemSpace * total_row;
            Paint picturePaint = new Paint();

            Rect mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect mDestRect = new Rect(left - mItemSpace, top - mItemSpace, left, top);

            ImageView imag = new ImageView(context);
            imag.getDrawingCache();
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, picturePaint);
        }
    }

    public void onLoadItem(int position) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                    int col = (int) (mDownX / mItemSpace);
                    int row = (int) (mDownY / mItemSpace);
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
        if (col >= total_col || row >= total_row)
            return;
        int position = (row - 1) * spanCount + col;
        onGridViewItemClick(position);

        //update();

    }

    /**
     * 刷新界面
     */
    public void update() {
        //fillDate();
        requestLayout();
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

                            selectImgListener.onMultipleResult(resultEvent);
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
        if (position == mediaBeans.size()) {
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
