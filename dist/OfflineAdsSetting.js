import { FileUtils, isEmpty, isIOS, PreferenceUtils } from "my-rn-base-utils";
import RNFetchBlob from 'rn-fetch-blob';
import { NativeModules } from "react-native";
let androidID;
// Lấy ads trong file này. Nó thường được lưu khi tải left men. RNFetchBlob.fs.dirs.CacheDir + "/ads"
export class OfflineAdsSetting {
    /** Cần gọi hàm này trước. trong index*/
    static setAndroidID(id) {
        androidID = id;
    }
    /***
     * Thêm cái Prefer Ads vào danh sách
     */
    static async addMyAdsToList(listItem) {
        if (isEmpty(listItem))
            return false;
        let preferApp = await OfflineAdsSetting.getPreferAds(false);
        if (preferApp) {
            preferApp.isMyAds = true;
            listItem.splice(0, 0, preferApp);
            return true;
        }
        return false;
    }
    /**isOfflineAds để xác định ads này là show nếu user offline, chứ ko phải cái preffer ads*/
    static async getPreferAds(isOfflineAds) {
        let filePath = RNFetchBlob.fs.dirs.CacheDir + "/ads";
        if (await FileUtils.exists(filePath)) {
            let str = await FileUtils.readFile(filePath);
            let json = JSON.parse(str);
            if (json) {
                let preferApp = await OfflineAdsSetting._getPreferAdsFromList(json.preferApp, isOfflineAds);
                if (preferApp != undefined) {
                    return preferApp;
                }
                else {
                    if (isOfflineAds)
                        return OfflineAdsSetting._getPreferAdsFromList(json.tieng_anh, isOfflineAds);
                    return null; // chỉ lấy ở preferApp
                }
            }
        }
    }
    static async _getPreferAdsFromList(apps, isOfflineAds) {
        if (isEmpty(apps))
            return undefined;
        for (let i = 0; i < apps.length; i++) {
            let preferApp = apps[i];
            if (preferApp.package == undefined) {
                if (preferApp.url_schemes != undefined)
                    preferApp.package = preferApp.url_schemes;
                else if (preferApp.id != undefined)
                    preferApp.package = preferApp.id;
            }
            if (await OfflineAdsSetting.canShowMyAds(i, preferApp.package, isOfflineAds)) {
                await OfflineAdsSetting.showMyAds(preferApp.package);
                return preferApp;
            }
        }
    }
    static async showMyAds(packageName) {
        let noShow = await PreferenceUtils.getNumberSetting("SHOW_" + packageName);
        noShow++;
        await PreferenceUtils.saveSeting("SHOW_" + packageName, noShow);
    }
    static async clickMyAds(packageName) {
        await PreferenceUtils.saveBooleanSetting("CLICK_" + packageName, true);
    }
    static async canShowMyAds(index, packageName, isOfflineAds) {
        if (packageName === androidID)
            return false;
        if (!isIOS() && await OfflineAdsSetting.ANDROID_isAppInstalled(packageName))
            return false;
        if (await PreferenceUtils.getBooleanSetting("CLICK_" + packageName))
            return false;
        if (isOfflineAds)
            return true;
        let noShow = await PreferenceUtils.getNumberSetting("SHOW_" + packageName);
        if (index === 0 && noShow > 10)
            return false;
        if (index === 1 && noShow > 5)
            return false;
        if (index >= 2 && noShow > 2)
            return false;
        return true;
    }
    static ANDROID_isAppInstalled(packageName) {
        return new Promise(function (resolve, reject) {
            NativeModules.RNCommonUtilsAndroid.isAppInstalled(packageName, (value) => {
                resolve(value);
            });
        });
    }
    static ANDROID_openAppFromMarket(packageName) {
        NativeModules.RNCommonUtilsAndroid.openAppFromMarket(packageName);
    }
    static ANDROID_launchInstalled(packageName) {
        NativeModules.RNCommonUtilsAndroid.launchInstalled(packageName);
    }
}
