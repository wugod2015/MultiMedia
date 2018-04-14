package com.yckj.hhz.multimedialibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class SelectImgAdapter extends BaseAdapter {
    final static String TAG = "SelectImgAdapter";

    public Context context;
    public List<MediaBean> list;
    int spanCount;
    int maxCount;
    boolean isShowDelete = false;

    public SelectImgAdapter(Context context, List<MediaBean> list, int spanCount, int maxCount) {
        this.context = context;
        this.list = list;
        this.spanCount = spanCount;
        this.maxCount = maxCount;
    }

    @Override
    public int getCount() {
        return list.size() < maxCount ? list.size() + 1 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position < list.size() ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_select_img_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onGridViewItemClick(v, position);
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

            //Glide.with(context).load(R.drawable.btn_add_img_selector).into(viewHolder.itemImg);
            viewHolder.itemImg.setImageResource(R.drawable.btn_add_img_selector);
            viewHolder.deleteImg.setVisibility(View.GONE);
        } else {
            final MediaBean item = list.get(position);
            Glide.with(context).load(item.getOriginalPath()).error(R.drawable.pic_no_default).into(viewHolder.itemImg);
            viewHolder.deleteImg.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
            viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    public interface OnGridViewItemClickListener {
        void onGridViewItemClick(View view, int position);
    }

    private OnGridViewItemClickListener mOnItemClickListener = null;

    public void setOnGridViewItemClickListener(OnGridViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class ViewHolder {
        ImageView itemImg;
        ImageView deleteImg;
        View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            itemImg = (ImageView) itemView.findViewById(R.id.itemImg);
            deleteImg = (ImageView) itemView.findViewById(R.id.deleteImg);
        }

    }
}
