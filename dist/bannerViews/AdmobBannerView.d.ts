import { Component } from 'react';
import { ViewStyle, StyleProp } from 'react-native';
interface Props {
    typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER";
    isNoRefresh?: boolean;
    onAdFailedToLoad?: (errorCode: number) => void;
    style?: StyleProp<ViewStyle>;
}
export declare class AdmobBannerView extends Component<Props, {
    width: number;
    height: number;
}> {
    static defaultProps: {
        typeAds: string;
        isNoRefresh: boolean;
    };
    constructor(props: any);
    shouldComponentUpdate(nextProps: any, nextState: any): boolean;
    render(): JSX.Element;
}
export {};
