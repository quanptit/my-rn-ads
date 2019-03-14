Show các quảng cáo Admob, facebook, AppotaADX ...       
chung cho tất cả các loại quảng cáo
## Installation

##### Thêm Vào package.json
```
"my-rn-ads": "git+https://gitlab.com/react-native-my-libs/my-rn-ads.git",
```

Chạy 2 lệnh sau
```
npm install
react-native link my-rn-ads
```

Copy project AppShareLibs:  react-native-my-libs\Template\AppShareLibs vào thư mục android
```
>>> react-native-my-libs\Template\AppShareLibs
``` 

Trong file setting của android project thêm nội dung sau:
```
include ':BaseLibs'
project(':BaseLibs').projectDir = new File('S:/Codes/libs/ReactNativeLibs/BaseLibs')
include ':AppShareLibs'

include ':MopubFacebookAudienceNetworkMediation'
project(':MopubFacebookAudienceNetworkMediation').projectDir = new File('S:/Codes/react-native-my-libs/MopubFacebookAudienceNetworkMediation')
include ':my-rn-ads'
project(':my-rn-ads').projectDir = new File('S:/Codes/react-native-my-libs/my-rn-ads/android') // mopub

```

##### làm cho mọi Application đang sử dụng phải kế thừa ApplicationContainAds
##### Sửa đổi file KeysAds trong AppShareLibs cho chuẩn
##### Xóa mấy cái layout liên quan đến ads, splash_activity.xml, dialog_confirm_exit_ads.xml
- Xóa trong manifresh

### Cách sử dụng
Trước khi load app:
```
OfflineAdsSetting.setAndroidID(Keys.androidID);
// init cái mopub + sẽ update cái setting Ads từ server
await RNAdsUtils.initAds(Keys.getADS_SETTING_ORDER());
```

### VPS Ads File Setting    
định dạng file có dạng như sau:
```json
{
  "full_start": 2,
  "full_center": 0,
  "banner": 0,
  "large_native": 0,
  "small_native": 0,
  "key_start": {
    "ADX": "ca-app-pub-3940256099942544/1033173712",
    "FB": "",
    "ADMOB": ""
  }
}
```
	* 0:  Mopub => FB => Admob => ADX
	* 1:  FB => Mopub => Admob => ADX
	* 2:  Admob => ADX => FB => Mopub
	* 3: Mopub => ADX => FB => Admob

Native sẽ luôn là Mopub. Các setting large_native, small_native là để chỉ ra sử dụng native hay banner thay thế cho cái native đó.
- large_native = 0    ==>  hiển thị cái large là native
- large_native = 1    ==>  hiển thị cái large là RECT banner


key_start: Có thể có hoặc không. Setting giá trị key cho ads start 

### Cập nhật từ ứng dụng cũ
- tìm kiếm import RNAdsUtils ==> import RNAdsUtils from "my-rn-ads/RNAdsUtils"  
- import NativeAdsView => import NativeAdsView from "my-rn-ads/NativeAdsView"
import BannerAdsView => import BannerAdsView from "my-rn-ads/BannerAdsView"
- Sửa hàm renderAdsNative như sau: 
``` 
renderAdsComponent(isLarge: boolean, style) {
        return (
            <NativeAdsView typeAds={isLarge ? 12 : 1} style={style}/>
        );
}
```
