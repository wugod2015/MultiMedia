package cn.finalteam.rxgalleryfinal.interactor.impl;

import android.content.Context;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.BucketBean;
import cn.finalteam.rxgalleryfinal.interactor.MediaBucketFactoryInteractor;
import cn.finalteam.rxgalleryfinal.utils.MediaUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/4 下午8:29
 */
public class MediaBucketFactoryInteractorImpl implements MediaBucketFactoryInteractor {

    private Context context;
    private boolean isImage;
    private OnGenerateBucketListener onGenerateBucketListener;

    public MediaBucketFactoryInteractorImpl(Context context, boolean isImage, OnGenerateBucketListener onGenerateBucketListener) {
        this.context = context;
        this.isImage = isImage;
        this.onGenerateBucketListener = onGenerateBucketListener;
    }

    @Override
    public void generateBuckets() {
        Observable.create((ObservableOnSubscribe<List<BucketBean>>) subscriber -> {
            List<BucketBean> bucketBeanList = null;
            if(isImage) {
                bucketBeanList = MediaUtils.getAllBucketByImage(context);
            } else {
                bucketBeanList = MediaUtils.getAllBucketByVideo(context);
            }
            subscriber.onNext(bucketBeanList);
            subscriber.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BucketBean>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGenerateBucketListener.onFinished(null);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BucketBean> bucketBeanList) {
                        onGenerateBucketListener.onFinished(bucketBeanList);
                    }
                });
    }
}
