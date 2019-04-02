import * as React from "react";
import {NativeAdsView} from "./NativeAdsView";
import {View} from "react-native";
import {BannerAdsView} from "./BannerAdsView";
import {AdsObj} from "my-rn-base-utils";

export class RenderAdsUtils {

    //region Ads
    /**return row ads nếu như obj đó là ads obj. else return null
     * */
    public static renderRowAds(adsObj: AdsObj, style?) {
        if (adsObj.large != undefined)
            return RenderAdsUtils.renderAdsComponent(adsObj, style);
        return undefined
    }

    public static renderAdsComponent(adsObj: AdsObj, style?) {
        return (
            <View style={style}>
                <NativeAdsView typeAds={adsObj.typeAds} allowBannerBackup={adsObj.allowBannerBackup}/>
            </View>
        )
    }

    public static renderBannerAdsComponent(style?) {
        return (
            <BannerAdsView style={style}/>
        )
    }

    //endregion
}
