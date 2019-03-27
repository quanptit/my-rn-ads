import {NativeModules, Platform} from 'react-native'
import {Actions} from 'react-native-router-flux'
import {RNCommonUtils, sendError} from "my-rn-base-utils";
import {DialogUtils} from "my-rn-base-component";

export class RNAdsUtils {
    static async initAds(settingAdsUrl: string): Promise<boolean> {
        if (await RNCommonUtils.isVIPUser())
            return true;
        try {
            let result = excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.initAds(settingAdsUrl);
            }, 5000);
            return result;
        } catch (e) {
            sendError(e);
            return false;
        }
    }

    //region ========== Native ads ======
    /**Nếu setting cái quảng cáo banner ưu tiên hiển thị, thay vì cái native thì sẽ bỏ qua ko tải quảng cáo native*/
    static async loadNativeAdsWhenStartAppIfNeed(typeAds: number): Promise<boolean> {
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (await RNAdsUtils.isPreferShowBanner(typeAds)) return false;
        return RNAdsUtils.loadNativeAds(typeAds);
    }

    static async loadNativeAds(typeAds: number): Promise<boolean> {
        if (await RNCommonUtils.isVIPUser())
            return false;
        try {
            await excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.loadNativeAds(typeAds);
            }, 5000);
            console.log("loadNativeAds: Success");
            return true;
        } catch (e) {
            console.log("loadNativeAds: false");
            return false;
        }
    }

    static async isPreferShowBanner(typeAds: number): Promise<boolean> {
        if (Platform.OS === "ios") return true;
        return NativeModules.RNAdsUtils.isPreferShowBanner(typeAds);
    }

    static async canShowNativeAds(typeAds: number): Promise<boolean> {
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (Platform.OS === "ios") return false;
        return NativeModules.RNAdsUtils.canShowNativeAds(typeAds);
    }

    static async cacheNativeAdsIfNeed(typeAds: number) {
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (Platform.OS === "ios") return;
        return NativeModules.RNAdsUtils.cacheNativeAdsIfNeed(typeAds);
    }

    //endregion

    //region full center & exit ads
    static async canShowFullCenterAds(): Promise<boolean> {
        if (await RNCommonUtils.isVIPUser())
            return false;
        return NativeModules.RNAdsUtils.canShowFullCenterAds();
    }

    static async cacheAdsCenter() {
        if (await RNCommonUtils.isVIPUser())
            return;
        return NativeModules.RNAdsUtils.cacheAdsCenter();
    }

    static async showFullCenterAds(): Promise<boolean> {
        console.log("Call showFullCenterAds");
        if (await RNCommonUtils.isVIPUser())
            return false;
        if (Platform.OS === "ios")
            return NativeModules.RNCommonUtilsIOS.showFullCenterAds();
        else
            return NativeModules.RNAdsUtils.showFullCenterAds();
    }

    static async showFullCenterAdsAndBackPress(showRateDialogIfNoAds: {
        review_title: string, review_description: string,
        yes_sure: string, remind_me_late: string, androidID: string, iosId: string
    } = null) {
        let canShowFullCenterAds: boolean;
        try {
            canShowFullCenterAds = await RNAdsUtils.canShowFullCenterAds();
            if (canShowFullCenterAds)
                await excuteFuncWithTimeOut(() => RNAdsUtils.showFullCenterAds(), 1000);
            else
                setTimeout(RNAdsUtils.cacheAdsCenter, 1000);
        } catch (e) {sendError(e) }
        Actions.pop();
        if (showRateDialogIfNoAds != null) {
            // noinspection JSIgnoredPromiseFromCall
            DialogUtils.showRateDialogIfNeed(showRateDialogIfNoAds.review_title, showRateDialogIfNoAds.review_description,
                showRateDialogIfNoAds.yes_sure, showRateDialogIfNoAds.remind_me_late,
                showRateDialogIfNoAds.androidID, showRateDialogIfNoAds.iosId);
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

    static canShowRewardVideoAds(): Promise<boolean> {
        if (Platform.OS === "ios")
            return NativeModules.RNCommonUtilsIOS.canShowRewardVideoAds();
        return NativeModules.RNAdsUtils.canShowRewardVideoAds();
    }

    //endregion

    static getTypeShowBanner(typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER", index: number): Promise<"FB" | "ADMOB" | "ADX" | "MOPUB"> {
        return NativeModules.RNAdsUtils.getTypeShowBanner(typeAds, index);
    }
}

function excuteFuncWithTimeOut(func: () => Promise<any>, miliSeconds: number): Promise<any> {
    let didTimeOut = false;
    return new Promise(function (resolve, reject) {

        const timeout = setTimeout(function () {
            didTimeOut = true;
            reject(new Error('Request timed out'))
        }, miliSeconds);

        func().then((response) => {
            clearTimeout(timeout);
            if (!didTimeOut)
                resolve(response)
        }).catch((reason) => {
            clearTimeout(timeout);
            if (!didTimeOut)
                reject(reason)
        })
    })
}
