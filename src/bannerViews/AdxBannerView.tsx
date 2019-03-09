import React, {Component, ReactPropTypes} from 'react'
import {requireNativeComponent, View, StyleSheet, ViewStyle, StyleProp} from 'react-native'
import {isEqual} from 'lodash'

let iface: any = {
    name: 'AdxBannerView',
    propTypes: {
        typeAds: String,
    },
};
let RNAdmobBannerView: any = requireNativeComponent('AdxBannerView');


interface Props {
    typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER"
    onAdFailedToLoad?: (errorCode: number) => void
    style?: StyleProp<ViewStyle>
}

export class AdxBannerView extends Component<Props, { width: number, height: number }> {
    static defaultProps = {
        typeAds: "SMART_BANNER"
    };

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
        this.state = {width: width, height: heigth};
    }

    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }

    render() {
        console.log("render: ", this.state.width, this.state.height);
        return (
            <RNAdmobBannerView style={[this.props.style, {width: this.state.width, height: this.state.height}]}
                               typeAds={this.props.typeAds}
                               onSizeChange={(event) => {
                                   let {width, height} = event.nativeEvent;
                                   this.setState({width: width, height: height});
                               }}
                               onAdFailedToLoad={this.props.onAdFailedToLoad}
            />
        )
    }
}
