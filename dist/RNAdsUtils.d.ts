export declare class RNAdsUtils {
    static initAds(settingAdsUrl: string): Promise<boolean>;
    static loadStartAds(): Promise<boolean>;
    static showStartAdsIfCache(): Promise<boolean>;
    static showStartAds(maxTimeMilisecond: number, callbackOpenAds: VoidFunction): Promise<void>;
    /**Nếu setting cái quảng cáo banner ưu tiên hiển thị, thay vì cái native thì sẽ bỏ qua ko tải quảng cáo native*/
    static loadNativeAdsWhenStartAppIfNeed(typeAds: number): Promise<boolean>;
    static loadNativeAds(typeAds: number): Promise<boolean>;
    static firstCacheAndCheckCanShowNativeAds(typeAds: number): Promise<boolean>;
    static hasLoadNativeAds(): Promise<boolean>;
    static canShowNativeAds(typeAds: number): Promise<boolean>;
    static isPreferShowBanner(typeAds: number): Promise<boolean>;
    static cacheNativeAdsIfNeed(typeAds: number): Promise<any>;
    /**Hiển thị quảng cáo xong, mới mở screen mới*/
    static showCenterAdsAndOpenScreen<P>(screenName: string, screenProps: P, skipFirstTime: boolean): Promise<void>;
    static canShowFullCenterOnBackBtn(): Promise<boolean>;
    static canShowFullCenterAds(): Promise<boolean>;
    static cacheAdsCenter(): Promise<any>;
    static showFullCenterAds(): Promise<boolean>;
    static showFullCenterAdsAndBackPress(showRateDialogIfNoAds?: {
        review_title: string;
        review_description: string;
        yes_sure: string;
        remind_me_late: string;
        androidID: string;
        iosId: string;
    }): Promise<void>;
    static loadRewardVideoAds(): void;
    static showRewardVideoAds(): void;
    static canShowRewardVideoAds(): Promise<boolean>;
    static getTypeShowBanner(typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER", index: number): Promise<"FB" | "ADMOB" | "ADX" | "MOPUB">;
}
