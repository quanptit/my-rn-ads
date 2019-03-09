export declare class OfflineAdsSetting {
    /** Cần gọi hàm này trước. trong index*/
    static setAndroidID(id: string): void;
    /***
     * Thêm cái Prefer Ads vào danh sách
     */
    static addMyAdsToList(listItem: any[]): Promise<boolean>;
    /**isOfflineAds để xác định ads này là show nếu user offline, chứ ko phải cái preffer ads*/
    static getPreferAds(isOfflineAds: boolean): Promise<any>;
    private static _getPreferAdsFromList;
    static showMyAds(packageName: string): Promise<void>;
    static clickMyAds(packageName: string): Promise<void>;
    private static canShowMyAds;
    static ANDROID_isAppInstalled(packageName: string): Promise<boolean>;
    static ANDROID_openAppFromMarket(packageName: string): void;
    static ANDROID_launchInstalled(packageName: string): void;
}
