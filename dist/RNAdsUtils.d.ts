export declare class RNAdsUtils {
    static initAds(settingAdsUrl: string): Promise<boolean>;
    static loadNativeAdsWhenStartAppIfNeed(): Promise<boolean>;
    static loadNativeAds(): Promise<boolean>;
    static isPreferShowBanner(typeAds: number): Promise<boolean>;
    static canShowNativeAds(typeAds: number): Promise<boolean>;
    static cacheNativeAdsIfNeed(typeAds: number): any;
    static showFullCenterAds(): Promise<boolean>;
    static showFullCenterAdsAndBackPress(): Promise<void>;
    /**Callback true, false chỉ ra có ads để show hay không*/
    static showExitAds(): void;
    static loadRewardVideoAds(): void;
    static showRewardVideoAds(): void;
    static canShowRewardVideoAds(): Promise<boolean>;
    static getTypeShowBanner(index: number): Promise<"FB" | "ADMOB" | "ADX" | "MOPUB">;
}
