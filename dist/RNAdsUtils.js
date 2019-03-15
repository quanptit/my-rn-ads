import { NativeModules, Platform } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { sendError } from "my-rn-base-utils";
import { DialogUtils } from "my-rn-base-component";
export class RNAdsUtils {
    static async initAds(settingAdsUrl) {
        try {
            return excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.initAds(settingAdsUrl);
            }, 5000);
        }
        catch (e) {
            sendError(e);
            return false;
        }
    }
    //region ========== Native ads ======
    /**Nếu setting cái quảng cáo banner ưu tiên hiển thị, thay vì cái native thì sẽ bỏ qua ko tải quảng cáo native*/
    static async loadNativeAdsWhenStartAppIfNeed() {
        if (await RNAdsUtils.isPreferShowBanner(5))
            return false;
        return RNAdsUtils.loadNativeAds();
    }
    static async loadNativeAds() {
        try {
            await excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.loadNativeAds();
            }, 3000);
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
        if (Platform.OS === "ios")
            return false;
        return NativeModules.RNAdsUtils.canShowNativeAds(typeAds);
    }
    static cacheNativeAdsIfNeed(typeAds) {
        if (Platform.OS === "ios")
            return;
        return NativeModules.RNAdsUtils.cacheNativeAdsIfNeed(typeAds);
    }
    //endregion
    //region full center & exit ads
    static canShowFullCenterAds() {
        return NativeModules.RNAdsUtils.canShowFullCenterAds();
    }
    static showFullCenterAds() {
        console.log("Call showFullCenterAds");
        if (Platform.OS === "ios")
            return NativeModules.RNCommonUtilsIOS.showFullCenterAds();
        else
            return NativeModules.RNAdsUtils.showFullCenterAds();
    }
    static async showFullCenterAdsAndBackPress(showRateDialogIfNoAds = null) {
        let canShowFullCenterAds;
        try {
            canShowFullCenterAds = await RNAdsUtils.canShowFullCenterAds();
            if (canShowFullCenterAds)
                await excuteFuncWithTimeOut(() => RNAdsUtils.showFullCenterAds(), 1000);
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
        if (Platform.OS === "ios")
            NativeModules.RNCommonUtilsIOS.loadRewardVideoAds();
        else
            NativeModules.RNAdsUtils.loadRewardVideoAds();
    }
    static showRewardVideoAds() {
        if (Platform.OS === "ios")
            NativeModules.RNCommonUtilsIOS.showRewardVideoAds();
        else
            NativeModules.RNAdsUtils.showRewardVideoAds();
    }
    static canShowRewardVideoAds() {
        if (Platform.OS === "ios")
            return NativeModules.RNCommonUtilsIOS.canShowRewardVideoAds();
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
