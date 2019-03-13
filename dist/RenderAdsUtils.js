import * as React from "react";
import { NativeAdsView } from "./NativeAdsView";
import { View } from "react-native";
import { BannerAdsView } from "./BannerAdsView";
export class RenderAdsUtils {
    //region Ads
    /**return row ads nếu như obj đó là ads obj. else return null
     * */
    static renderRowAds(adsObj, style) {
        if (adsObj.large != undefined)
            return RenderAdsUtils.renderAdsComponent(adsObj, style);
        return undefined;
    }
    static renderAdsComponent(adsObj, style) {
        return (<View style={style}>
                <NativeAdsView typeAds={adsObj.typeAds} allowBannerBackup={adsObj.allowBannerBackup}/>
            </View>);
    }
    static renderBannerAdsComponent(style) {
        return (<BannerAdsView style={style}/>);
    }
}
