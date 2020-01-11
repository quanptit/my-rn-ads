import { Component } from 'react';
import { StyleProp, ViewStyle } from 'react-native';
interface Props {
    typeAds?: "RECTANGLE_HEIGHT_250" | "BANNER_50";
    isNoRefresh?: boolean;
    onAdFailedToLoad?: (errorCode: number) => void;
    style?: StyleProp<ViewStyle>;
}
export declare class BannerAdsView extends Component<Props, {
    typeShow: string;
    offlineAds?: any;
    isVip: boolean;
}> {
    static defaultProps: {
        typeAds: string;
    };
    private noFail;
    constructor(props: any);
    shouldComponentUpdate(nextProps: any, nextState: any): boolean;
    componentDidMount(): Promise<void>;
    onAdFailedToLoad(): Promise<void>;
    private getNewTypeShow;
    private updateTypeShow;
    render(): JSX.Element;
    private _renderFbBanner;
}
export {};
