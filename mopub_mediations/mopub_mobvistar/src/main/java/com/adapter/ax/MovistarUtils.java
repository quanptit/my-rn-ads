package com.adapter.ax;

import com.mintegral.msdk.out.MIntegralSDKFactory;

public class MovistarUtils {
    public static void release() {
        MIntegralSDKFactory.getMIntegralSDK().release();
    }
}
