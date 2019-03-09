import {NativeModules, Platform} from 'react-native'
import {DialogExitAds} from "./DialogExitAds";
import {Actions} from 'react-native-router-flux'

export class RNAdsUtils {
    static async initAds(settingAdsUrl: string): Promise<boolean> {
        return excuteFuncWithTimeOut(() => {
            return NativeModules.RNAdsUtils.initAds(settingAdsUrl);
        }, 5000);
    }

    //region ========== Native ads ======
    static async loadNativeAdsWhenStartAppIfNeed(): Promise<boolean> {
        if (await RNAdsUtils.isPreferShowBanner(5)) return false;
        return RNAdsUtils.loadNativeAds();
    }

    static async loadNativeAds(): Promise<boolean> {
        try {
            await excuteFuncWithTimeOut(() => {
                return NativeModules.RNAdsUtils.loadNativeAds()
            }, 3000);
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
        if (Platform.OS === "ios") return false;
        return NativeModules.RNAdsUtils.canShowNativeAds(typeAds);
    }

    static cacheNativeAdsIfNeed(typeAds: number) {
        if (Platform.OS === "ios") return;
        return NativeModules.RNAdsUtils.cacheNativeAdsIfNeed(typeAds);
    }

    //endregion

    static showFullCenterAds(): Promise<boolean> {
        console.log("Call showFullCenterAds");
        if (Platform.OS === "ios")
            return NativeModules.RNCommonUtilsIOS.showFullCenterAds();
        else
            return NativeModules.RNAdsUtils.showFullCenterAds();
    }

    static async showFullCenterAdsAndBackPress() {
        await excuteFuncWithTimeOut(() => RNAdsUtils.showFullCenterAds(), 1000);
        Actions.pop();
    }

    /**Callback true, false chỉ ra có ads để show hay không*/
    static showExitAds() {
        console.log("Call showExitAds");
        DialogExitAds.showDialogExit();
    }

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

    static getTypeShowBanner(index: number): Promise<"FB" | "ADMOB" | "ADX" | "MOPUB"> {
        return NativeModules.RNAdsUtils.getTypeShowBanner(index);
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
