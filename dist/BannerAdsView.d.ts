import { Component } from 'react';
import { StyleProp, ViewStyle } from 'react-native';
interface Props {
    typeAds?: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER";
    isNoRefresh?: boolean;
    onAdFailedToLoad?: (errorCode: number) => void;
    style?: StyleProp<ViewStyle>;
}
export declare class BannerAdsView extends Component<Props, {
    noFail: number;
    typeShow: string;
    offlineAds?: any;
}> {
    static defaultProps: {
        typeAds: string;
    };
    constructor(props: any);
    shouldComponentUpdate(nextProps: any, nextState: any): boolean;
    componentDidMount(): Promise<void>;
    onAdFailedToLoad(): Promise<void>;
    private update;
    render(): JSX.Element;
    private _renderFbBanner;
}
export {};
