import React, { Component } from 'react';
import { requireNativeComponent, StyleSheet } from 'react-native';
let iface = {
    name: 'FbBannerView',
    propTypes: {
        typeAds: String
    },
};
let RNFbBannerView = requireNativeComponent('FbBannerView');
export class FbBannerView extends Component {
    static defaultProps = {
        typeAds: "BANNER_50"
    };
    render() {
        return (<RNFbBannerView style={[this._getStyle(), this.props.style]} typeAds={this.props.typeAds} onAdFailedToLoad={this.props.onAdFailedToLoad}/>);
    }
    shouldComponentUpdate(nextProps, nextState) {
        return false;
    }
    _getStyle() {
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
    RECTANGLE_HEIGHT_250: { height: 250, width: 320 },
    BANNER_50: { height: 50, alignSelf: "stretch" },
    BANNER_HEIGHT_90: { height: 90, alignSelf: "stretch" }
});
