package cn.finalteam.rxgalleryfinal.rxbus;

import cn.finalteam.rxgalleryfinal.utils.Logger;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/22 下午2:40
 */
public abstract class RxBusSubscriber<T> implements Observer<T> {
    public Disposable disposable;
    @Override
    public void onSubscribe(Disposable d) {

        //RxBus.getDefault().add(d);
        disposable=d;
    }

    @Override
    public void onNext(T t) {
        try {
            onEvent(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onError(Throwable e) {
        Logger.e(e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onEvent(T t) throws Exception;
}