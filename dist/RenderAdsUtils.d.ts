/// <reference types="react" />
import { AdsObj } from "my-rn-base-utils";
export declare class RenderAdsUtils {
    /**return row ads nếu như obj đó là ads obj. else return null
     * */
    static renderRowAds(adsObj: AdsObj, style?: any): JSX.Element;
    static renderAdsComponent(adsObj: AdsObj, style?: any): JSX.Element;
    static renderBannerAdsComponent(style?: any): JSX.Element;
}
