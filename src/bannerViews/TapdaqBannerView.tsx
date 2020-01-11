import React, {Component} from 'react'
import {requireNativeComponent, ViewStyle, StyleProp} from 'react-native'
import {isEqual} from 'lodash'

let RNTapdaqBannerView: any = requireNativeComponent('TapdaqBannerView');


interface Props {
    typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50"
    onAdFailedToLoad?: (errorCode: number) => void
    style?: StyleProp<ViewStyle>
}

export class TapdaqBannerView extends Component<Props, { width: number, height: number }> {
    static defaultProps = {
        typeAds: "RECTANGLE_HEIGHT_250"
    };

    constructor(props) {
        super(props);
        let width, heigth;
        if (this.props.typeAds === "RECTANGLE_HEIGHT_250") {
            width = 320;
            heigth = 250;
        } else {
            width = 320;
            heigth = 50;
        }
        this.state = {width: width, height: heigth};
    }

    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }

    render() {
        return (
            <RNTapdaqBannerView style={[this.props.style, {width: this.state.width, height: this.state.height}]}
                               typeAds={this.props.typeAds}
                               onAdFailedToLoad={this.props.onAdFailedToLoad}
            />
        )
    }
}
