import React, {Component} from 'react'
import {requireNativeComponent, ViewStyle, StyleProp} from 'react-native'
import {isEqual} from 'lodash'

let RNMOPUBBannerView: any = requireNativeComponent('MOPUBBannerView');


interface Props {
    typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER"
    onAdFailedToLoad?: (errorCode: number) => void
    style?: StyleProp<ViewStyle>
}

export class MOPUBBannerView extends Component<Props, { width: number, height: number }> {
    static defaultProps = {
        typeAds: "BANNER_50"
    };

    constructor(props) {
        super(props);
        let width, heigth;
        switch (this.props.typeAds) {
            case "RECTANGLE_HEIGHT_250":
                width = 320;
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
        return (
            <RNMOPUBBannerView style={[this.props.style, {width: this.state.width, height: this.state.height}]}
                               typeAds={this.props.typeAds}
                               onSizeChange={(event) => {
                                   let {width, height} = event.nativeEvent;
                                   if (width === this.state.width && height === this.state.height)
                                       this.forceUpdate();
                                   else
                                       this.setState({width: width, height: height});
                               }}
                               onAdFailedToLoad={this.props.onAdFailedToLoad}
            />
        )
    }
}
