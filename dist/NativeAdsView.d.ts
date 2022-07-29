import { Component } from 'react';
import { StyleProp, ViewStyle } from 'react-native';
interface Props {
    typeAds?: number;
    allowBannerBackup?: boolean;
    skipCacheNative?: boolean;
    isAlwayPreferNative?: boolean;
    delayTime?: boolean;
    isScroll?: boolean;
    style?: StyleProp<ViewStyle>;
}
export declare class NativeAdsView extends Component<Props, {
    isLoading: boolean;
    needRender: boolean;
    height: number;
    showLoading?: boolean;
    isHasAds?: boolean;
}> {
    static TYPE_SUMMARY_FB: number;
    static TYPE_SUMMARY_SMALL_CUSTOM: number;
    static TYPE_SUMMARY_LARGE: number;
    static TYPE_DETAIL_FB: number;
    static TYPE_DETAIL_CUSTOM: number;
    static TYPE_DETAIL_VOCA: number;
    private isCachedNativeAds;
    private offlineAds?;
    private isPreferShowBanner;
    static defaultProps: {
        typeAds: number;
        allowBannerBackup: boolean;
        skipCacheNative: boolean;
    };
    constructor(props: any);
    componentDidMount(): Promise<void>;
    shouldComponentUpdate(nextProps: any, nextState: any): boolean;
    render(): any;
    renderLoadingView(): any;
    private _renderOfflineAds;
    private _onAdFailed;
    private _renderNativeView;
    private _renderBanner50Ads;
    private _renderRectBannerAds;
}
export {};
