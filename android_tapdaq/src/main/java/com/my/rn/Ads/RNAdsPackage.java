package com.my.rn.Ads;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.my.rn.Ads.modules.*;
import com.my.rn.tapdaq.MOPUBBannerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RNAdsPackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext context) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new RNAdsUtilsModule(context));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext context) {
        return Arrays.<ViewManager>asList(
                new NativeAdsView(),
                new FbBannerView(),
                new AdmobBannerView(),
                new AdxBannerView(),
                new MOPUBBannerView()
        );
    }

}
