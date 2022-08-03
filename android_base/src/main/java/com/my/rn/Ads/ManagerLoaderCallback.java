package com.my.rn.ads;

import androidx.annotation.Nullable;

import com.baseLibs.BaseApplication;

import java.util.LinkedList;
import java.util.Queue;

public class ManagerLoaderCallback {
    Queue<WrapLoadCallback> listCallback = new LinkedList<>();

    // đăng ký một callback, đảm bảo trong khoảng time truyền vào, nếu chưa phản hồi sẽ bị phản hồi fail
    public void registerCallback(IAdLoaderCallback loaderCallback, int timeMilisMakeFailLoad) {
        listCallback.add(new WrapLoadCallback(loaderCallback, timeMilisMakeFailLoad));
    }

    private @Nullable WrapLoadCallback getNextCallback() {
        WrapLoadCallback callback = listCallback.poll();
        if (callback == null) return null;
        if (callback.iAdLoaderCallback == null)
            return getNextCallback();
        return callback;
    }

    // Khi có loader event sẽ phải gọi cái này
    public void dispatchLoadedCallback() {
        WrapLoadCallback callaback = getNextCallback();
        if (callaback != null)
            callaback.dispatchLoadedCallback();
    }

    // Khi có load fail event sẽ phải gọi cái này
    public void dispatchLoadFailCallback() {
        WrapLoadCallback callaback = getNextCallback();
        if (callaback != null)
            callaback.dispatchLoadFailCallback();
    }

    static class WrapLoadCallback {
        IAdLoaderCallback iAdLoaderCallback;
        private Runnable runable;

        public WrapLoadCallback(IAdLoaderCallback iAdLoaderCallback, int timeMilisMakeFailLoad) {
            this.iAdLoaderCallback = iAdLoaderCallback;
            runable = new Runnable() {
                @Override public void run() {
                    dispatchLoadFailCallback();
                }
            };
            BaseApplication.getHandler().postDelayed(runable, timeMilisMakeFailLoad);
        }

        public void dispatchLoadedCallback() {
            if (iAdLoaderCallback != null) iAdLoaderCallback.onAdsLoaded();
            BaseApplication.getHandler().removeCallbacks(runable);
            runable = null;
        }

        public void dispatchLoadFailCallback() {
            if (iAdLoaderCallback != null) iAdLoaderCallback.onAdsFailedToLoad();
            BaseApplication.getHandler().removeCallbacks(runable);
            runable = null;
        }
    }
}
