import { Component } from 'react';
import { ViewStyle, StyleProp } from 'react-native';
interface Props {
    typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "BANNER_HEIGHT_90";
    onAdFailedToLoad?: (errorCode: number) => void;
    style?: StyleProp<ViewStyle>;
}
export declare class FbBannerView extends Component<Props> {
    static defaultProps: {
        typeAds: string;
    };
    render(): any;
    shouldComponentUpdate(nextProps: any, nextState: any): boolean;
    private _getStyle;
}
export {};
