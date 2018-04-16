package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class SelectImgRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final static String TAG = "SelectImgRecyclerAdapter";

    public Context context;
    public List<MediaBean> list;
    int spanCount;
    int maxCount;
    boolean isShowDelete = false;
    boolean isOnlyShow = false;

    public SelectImgRecyclerAdapter(Context context, List<MediaBean> list, int spanCount, int maxCount, boolean isOnlyShow) {
        this.context = context;
        this.list = list;
        this.spanCount = spanCount;
        this.maxCount = maxCount;
        this.isOnlyShow = isOnlyShow;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int h = parent.getMeasuredWidth();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_select_img_item, null), h);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRecyclerViewItemClick(v, position);
                }
            }
        });
        /*viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isShowDelete = !isShowDelete;
                notifyDataSetChanged();
                return true;
            }
        });*/
        if (position == list.size()) {
            viewHolder.itemImg.setImageResource(R.drawable.btn_add_img_selector);
            viewHolder.deleteImg.setVisibility(View.GONE);
            return;
        }
        final MediaBean item = list.get(position);
        Glide.with(context).load(item.getOriginalPath()).into(viewHolder.itemImg);
        viewHolder.deleteImg.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
        viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size() < maxCount && !isOnlyShow ? list.size() + 1 : list.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, int position);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        ImageView deleteImg;

        public ViewHolder(View itemView, int h) {
            super(itemView);
            itemImg = (ImageView) itemView.findViewById(R.id.itemImg);
            deleteImg = (ImageView) itemView.findViewById(R.id.deleteImg);
            ViewGroup.LayoutParams layoutParams = itemImg.getLayoutParams();
            layoutParams.height = h / spanCount;
            layoutParams.width = h / spanCount;
            itemImg.setLayoutParams(layoutParams);
        }

    }
}
