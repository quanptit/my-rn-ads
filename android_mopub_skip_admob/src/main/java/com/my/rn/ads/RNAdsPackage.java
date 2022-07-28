package com.my.rn.ads;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.my.rn.ads.fb.rn.FbBannerView;
import com.my.rn.ads.modules.*;
import com.my.rn.ads.mopub.rn.MOPUBBannerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RNAdsPackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext context) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new BaseRNAdsUtilsModule(context));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext context) {
        return Arrays.<ViewManager>asList(
                new NativeAdsView(),
                new FbBannerView(),
                new MOPUBBannerView(),
                new TapdaqBannerViewEmpty()
        );
    }

}
