import { NativeModules, Platform } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { RNCommonUtils, sendError } from "my-rn-base-utils";
import { DialogUtils } from "my-rn-base-component";
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
    static async isPreferShowBanner(typeAds) {
        if (Platform.OS === "ios")
            return true;
        return NativeModules.RNAdsUtils.isPreferShowBanner(typeAds);
    }
    static async canShowNativeAds(typeAds) {
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (Platform.OS === "ios")
            return false;
        return NativeModules.RNAdsUtils.canShowNativeAds(typeAds);
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
