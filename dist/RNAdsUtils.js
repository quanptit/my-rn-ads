import { NativeModules, Platform } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { CommonUtils, DataTypeUtils, RNCommonUtils, sendError } from "my-rn-base-utils";
import { DialogUtils } from "my-rn-base-component";
let firstTimeCallShowCenter = true;
export class RNAdsUtils {
    static async initAds(settingAdsUrl) {
        if (await RNCommonUtils.isVIPUser())
            return true;
        try {
            let result = excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.initAds(settingAdsUrl);
            }, 5000);
            return result;
        }
        catch (e) {
            sendError(e);
            return false;
        }
    }
    //region start Ads =========
    static loadStartAds() {
        return NativeModules.RNAdsUtils.loadStartAds();
    }
    static showStartAdsIfCache() {
        return NativeModules.RNAdsUtils.showStartAdsIfCache();
    }
    static async showStartAds(maxTimeMilisecond, callbackOpenAds) {
        let currentTime = DataTypeUtils.getCurrentTimeMiliseconds();
        try {
            await CommonUtils.excuteFuncWithTimeOut(async () => {
                let isCache = await RNAdsUtils.loadStartAds();
                if (isCache) {
                    if (DataTypeUtils.getCurrentTimeMiliseconds() - currentTime > maxTimeMilisecond) {
                        console.log("TIME OUT FOR SHOW ASD");
                        return;
                    }
                    if (await RNAdsUtils.showStartAdsIfCache())
                        callbackOpenAds && callbackOpenAds();
                }
            }, maxTimeMilisecond);
        }
        catch (e) {
            console.log("TIME OUT FOR SHOW ASD, not cache image in start time");
            NativeModules.RNAdsUtils.destroyStartAdsIfNeed();
        }
    }
    //endregion
    //region ========== Native ads ======
    /**Nếu setting cái quảng cáo banner ưu tiên hiển thị, thay vì cái native thì sẽ bỏ qua ko tải quảng cáo native*/
    static async loadNativeAdsWhenStartAppIfNeed(typeAds) {
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (await RNAdsUtils.isPreferShowBanner(typeAds))
            return false;
        return RNAdsUtils.loadNativeAds(typeAds);
    }
    static async loadNativeAds(typeAds) {
        if (await RNCommonUtils.isVIPUser())
            return false;
        try {
            await excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.loadNativeAds(typeAds);
            }, 5000);
            console.log("loadNativeAds: Success");
            return true;
        }
        catch (e) {
            console.log("loadNativeAds: false");
            return false;
        }
    }
    static async firstCacheAndCheckCanShowNativeAds(typeAds) {
        try {
            return await excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.firstCacheAndCheckCanShowNativeAds(typeAds);
            }, 8000);
        }
        catch (e) {
            console.log("firstCacheAndCheckCanShowNativeAds: TIMEOUT");
            return false;
        }
    }
    // chỉ ra native ads đã được cố load, dù là fail hay success
    static async hasLoadNativeAds() {
        return NativeModules.RNAdsUtils.hasLoadNativeAds();
    }
    static async canShowNativeAds(typeAds) {
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (Platform.OS === "ios")
            return false;
        return NativeModules.RNAdsUtils.canShowNativeAds(typeAds);
    }
    static async isPreferShowBanner(typeAds) {
        if (Platform.OS === "ios")
            return true;
        return NativeModules.RNAdsUtils.isPreferShowBanner(typeAds);
    }
    static async cacheNativeAdsIfNeed(typeAds) {
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (Platform.OS === "ios")
            return;
        return NativeModules.RNAdsUtils.cacheNativeAdsIfNeed(typeAds);
    }
    //endregion
    //region full center & exit ads
    /**Hiển thị quảng cáo xong, mới mở screen mới*/
    static async showCenterAdsAndOpenScreen(screenName, screenProps, skipFirstTime) {
        let isShowAds = !firstTimeCallShowCenter || !skipFirstTime;
        firstTimeCallShowCenter = false;
        if (isShowAds) {
            let canShowFullCenterAds;
            try {
                canShowFullCenterAds = await RNAdsUtils.canShowFullCenterAds();
                if (canShowFullCenterAds)
                    await CommonUtils.excuteFuncWithTimeOut(() => RNAdsUtils.showFullCenterAds(), 1000);
                else
                    setTimeout(RNAdsUtils.cacheAdsCenter, 3000);
            }
            catch (e) {
                sendError(e);
            }
            if (canShowFullCenterAds) {
                await CommonUtils.waitAfterInteractions();
                await CommonUtils.wait(100);
            }
        }
        else {
            console.log("showCenterAdsAndOpenScreen Skip at first time call ");
            setTimeout(RNAdsUtils.cacheAdsCenter, 3000);
        }
        console.log("showCenterAdsAndOpenScreen openScreen: ", screenName);
        CommonUtils.openScreen(screenName, screenProps);
    }
    static async canShowFullCenterOnBackBtn() {
        return NativeModules.RNAdsUtils.canShowFullCenterOnBackBtn();
    }
    static async canShowFullCenterAds() {
        if (await RNCommonUtils.isVIPUser())
            return false;
        return NativeModules.RNAdsUtils.canShowFullCenterAds();
    }
    static async cacheAdsCenter() {
        if (await RNCommonUtils.isVIPUser())
            return;
        return NativeModules.RNAdsUtils.cacheAdsCenter();
    }
    static async showFullCenterAds() {
        console.log("Call showFullCenterAds");
        if (await RNCommonUtils.isVIPUser())
            return false;
        return NativeModules.RNAdsUtils.showFullCenterAds();
    }
    static async showFullCenterAdsAndBackPress(showRateDialogIfNoAds = null) {
        let canShowFullCenterAds;
        try {
            canShowFullCenterAds = await RNAdsUtils.canShowFullCenterAds();
            if (canShowFullCenterAds)
                await excuteFuncWithTimeOut(() => RNAdsUtils.showFullCenterAds(), 1000);
            else
                setTimeout(RNAdsUtils.cacheAdsCenter, 1000);
        }
        catch (e) {
            sendError(e);
        }
        Actions.pop();
        if (canShowFullCenterAds)
            return;
        if (showRateDialogIfNoAds != null) {
            // noinspection JSIgnoredPromiseFromCall
            DialogUtils.showRateDialogIfNeed(showRateDialogIfNoAds.review_title, showRateDialogIfNoAds.review_description, showRateDialogIfNoAds.yes_sure, showRateDialogIfNoAds.remind_me_late, showRateDialogIfNoAds.androidID, showRateDialogIfNoAds.iosId);
        }
    }
    //endregion
    // Reward Ads
    static loadRewardVideoAds() {
        NativeModules.RNAdsUtils.loadRewardVideoAds();
    }
    static showRewardVideoAds() {
        NativeModules.RNAdsUtils.showRewardVideoAds();
    }
    static canShowRewardVideoAds() {
        return NativeModules.RNAdsUtils.canShowRewardVideoAds();
    }
    //endregion
    static getTypeShowBanner(typeAds, index) {
        return NativeModules.RNAdsUtils.getTypeShowBanner(typeAds, index);
    }
}
function excuteFuncWithTimeOut(func, miliSeconds) {
    let didTimeOut = false;
    return new Promise(function (resolve, reject) {
        const timeout = setTimeout(function () {
            didTimeOut = true;
            reject(new Error('Request timed out'));
        }, miliSeconds);
        func().then((response) => {
            clearTimeout(timeout);
            if (!didTimeOut)
                resolve(response);
        }).catch((reason) => {
            clearTimeout(timeout);
            if (!didTimeOut)
                reject(reason);
        });
    });
}
