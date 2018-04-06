package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * @author hanhuizhong
 */
public class SelectImgGridView1 extends GridView implements SelectImgAdapter.OnGridViewItemClickListener {

    public SelectImgGridView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectImgGridView1(Context context) {
        this(context, null);
    }

    Context context;

    //GridView myGridView;
    SelectImgAdapter selectImgAdapter;
    ArrayList<MediaBean> mediaBeans;
    int spanCount = 4;
    int maxCount = 1;
    boolean isMultiple = false;
    boolean isCrop = false;

    public SelectImgGridView1(Context context, AttributeSet attrs, int defStyleAttr) {
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


    }

    public void init() {
        //LayoutInflater.from(context).inflate(R.layout.layout_select_img_gridview, this, true);
        //myGridView = (GridView) findViewById(R.id.myGridView);
        setNumColumns(spanCount);
        mediaBeans = new ArrayList<>();
        maxCount = maxCount == 0 ? spanCount * spanCount : maxCount;
        if (maxCount > 1) {
            isMultiple = true;
        }
        selectImgAdapter = new SelectImgAdapter(context, mediaBeans, spanCount, maxCount);
        setAdapter(selectImgAdapter);
        selectImgAdapter.setOnGridViewItemClickListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        int colums = getNumColumns();
        int height = b - t; // The height of the layout area
        int width = r - l; // The width of the layout area
        // Calculate the number  of  rows
        int rows = count % colums == 0 ? count / colums : count / colums + 1;
        if (count == 0) {
            return;
        }
        // Calculate every item  width
        int gridW = width / colums;
        // item height
        int gridH = gridW;
        // int gridH = (height - mVerticalSpacing * rows) / rows;
        int left = 0;
        int top = 0;

        for (int i = 0; i < rows; i++) {
            // Iterator the elements of each row
            for (int j = 0; j < colums; j++) {
                View child = getChildAt(i * colums + j);
                if (child == null) {
                    return;
                }
                left = j * gridW;

                /**
                 * If the current layout is not the same width and width
                 * measurements, the direct use of re-measuring the width of the
                 * current layout
                 */
                if (gridW != child.getMeasuredWidth()
                        || gridH != child.getMeasuredHeight()) {
                    child.measure(makeMeasureSpec(gridW, EXACTLY),
                            makeMeasureSpec(gridH, EXACTLY));
                }
                /**
                 * set current child view layout
                 */
                child.layout(left, top, left + gridW, top + gridH);
            }
            top += gridH; // next rows
        }

    }

    /* @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //遍历子View，测量每个View的大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthSize / spanCount, widthSize / spanCount);
        }
        int size = mediaBeans == null ? 0 : mediaBeans.size();
        int column = (size == maxCount) ? (maxCount / spanCount + maxCount % spanCount) : ((size + 1) / spanCount + (size + 1) % spanCount);
        setMeasuredDimension(widthSize, widthSize / spanCount * column);
    }

    RxGalleryFinal rxGalleryFinal;

    @Override
    public void onGridViewItemClick(View view, int position) {
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
                    mediaBeans.clear();
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
