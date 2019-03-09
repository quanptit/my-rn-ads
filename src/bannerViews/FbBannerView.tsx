import React, {Component, ReactPropTypes} from 'react'
import {requireNativeComponent, Text, StyleSheet, ViewStyle, StyleProp} from 'react-native'

let iface: any = {
    name: 'FbBannerView',
    propTypes: {
        typeAds: String
    },
};
let RNFbBannerView: any = requireNativeComponent('FbBannerView');


interface Props {
    typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "BANNER_HEIGHT_90"
    onAdFailedToLoad?: (errorCode: number) => void
    style?: StyleProp<ViewStyle>
}

export class FbBannerView extends Component<Props> {
    static defaultProps = {
        typeAds: "BANNER_50"
    };

    render() {
        return (
            <RNFbBannerView style={[this._getStyle(), this.props.style]}
                            typeAds={this.props.typeAds}
                            onAdFailedToLoad={this.props.onAdFailedToLoad}
            />
        )
    }

    shouldComponentUpdate(nextProps, nextState) {
        return false
    }

    private _getStyle(): any {
        if (this.props.typeAds === "RECTANGLE_HEIGHT_250") {
            return styles.RECTANGLE_HEIGHT_250;
        }
        if (this.props.typeAds === "BANNER_HEIGHT_90") {
            return styles.BANNER_HEIGHT_90;
        }
        return styles.BANNER_50;
    }
}
const styles = StyleSheet.create({
    RECTANGLE_HEIGHT_250: {height: 250, width: 320},
    BANNER_50: {height: 50, alignSelf: "stretch"},
    BANNER_HEIGHT_90: {height: 90, alignSelf: "stretch"}
});
