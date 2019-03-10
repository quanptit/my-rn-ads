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

//include ':my-rn-ads'
//project(':my-rn-ads').projectDir = new File('S:/Codes/react-native-my-libs/my-rn-ads/android')

```

##### làm cho mọi Application đang sử dụng phải kế thừa ApplicationContainAds
##### Sửa đổi file KeysAds trong AppShareLibs cho chuẩn
##### Xóa mấy cái layout liên quan đến ads, splash_activity.xml, dialog_confirm_exit_ads.xml
- Xóa trong manifresh

### Cách sử dụng
Trước khi load app:
```
await RNAdsUtils.initAds(PathUtils.getROOT() + Keys.ADS_SETTING);
await RNAdsUtils.loadNativeAdsWhenStartAppIfNeed();
```
 

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
