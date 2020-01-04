package com.my.rn.ads;

import com.facebook.react.bridge.Promise;

import javax.annotation.Nullable;

public class PromiseSaveObj {
    private Promise promise;

    public PromiseSaveObj(Promise promise) {
        this.promise = promise;
    }

    /**
     * Successfully resolve the Promise.
     */
    public void resolve(@Nullable Object value) {
        try {
            if (promise != null) {
                promise.resolve(value);
                promise = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Report an error which wasn't caused by an exception.
     */
    public  void reject(String code, String message) {
        try {
            if (promise != null) {
                promise.reject(code, message);
                promise = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
