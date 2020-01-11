import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';
import { isEqual } from 'lodash';
let RNTapdaqBannerView = requireNativeComponent('TapdaqBannerView');
export class TapdaqBannerView extends Component {
    constructor(props) {
        super(props);
        let width, heigth;
        if (this.props.typeAds === "RECTANGLE_HEIGHT_250") {
            width = 320;
            heigth = 250;
        }
        else {
            width = 320;
            heigth = 50;
        }
        this.state = { width: width, height: heigth };
    }
    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }
    render() {
        return (<RNTapdaqBannerView style={[this.props.style, { width: this.state.width, height: this.state.height }]} typeAds={this.props.typeAds} onAdFailedToLoad={this.props.onAdFailedToLoad}/>);
    }
}
TapdaqBannerView.defaultProps = {
    typeAds: "RECTANGLE_HEIGHT_250"
};
