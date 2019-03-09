import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';
import { isEqual } from 'lodash';
let iface = {
    name: 'AdxBannerView',
    propTypes: {
        typeAds: String,
    },
};
let RNAdmobBannerView = requireNativeComponent('AdxBannerView');
export class AdxBannerView extends Component {
    constructor(props) {
        super(props);
        let width, heigth;
        switch (this.props.typeAds) {
            case "RECTANGLE_HEIGHT_250":
                width = 300;
                heigth = 250;
                break;
            default:
                width = 320;
                heigth = 50;
                break;
        }
        this.state = { width: width, height: heigth };
    }
    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }
    render() {
        console.log("render: ", this.state.width, this.state.height);
        return (<RNAdmobBannerView style={[this.props.style, { width: this.state.width, height: this.state.height }]} typeAds={this.props.typeAds} onSizeChange={(event) => {
            let { width, height } = event.nativeEvent;
            this.setState({ width: width, height: height });
        }} onAdFailedToLoad={this.props.onAdFailedToLoad}/>);
    }
}
AdxBannerView.defaultProps = {
    typeAds: "SMART_BANNER"
};
