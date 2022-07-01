import React, {Component} from 'react'
import {requireNativeComponent, Text, View, StyleSheet, StyleProp, ViewStyle} from 'react-native'
import {BannerAdsView} from "./BannerAdsView";
import {RNAdsUtils} from "./RNAdsUtils";
import {isEqual} from "lodash"
import {Col} from "my-rn-base-component";
import {OfflineAdsSetting} from "./OfflineAdsSetting";
import {RowOfflineAds} from "./RowOfflineAds";

let NativeAdsViewRef: any = requireNativeComponent('NativeAdsView');

interface Props {
    typeAds?: number
    allowBannerBackup?: boolean
    skipCacheNative?: boolean
    isAlwayPreferNative?: boolean
    delayTime?: boolean
    isScroll?: boolean
    style?: StyleProp<ViewStyle>
}

export class NativeAdsView extends Component<Props, { isLoading: boolean, needRender: boolean, height: number, showLoading?: boolean , isHasAds?: boolean }> {
    public static TYPE_SUMMARY_FB = 0;
    public static TYPE_SUMMARY_SMALL_CUSTOM = 1;
    public static TYPE_SUMMARY_LARGE = 3;
    public static TYPE_DETAIL_FB = 10;
    public static TYPE_DETAIL_CUSTOM = 11;
    public static TYPE_DETAIL_VOCA = 12;

    private isCachedNativeAds: boolean;
    private offlineAds?: any;
    private isPreferShowBanner: boolean;
    static defaultProps = {
        typeAds: 3,
        allowBannerBackup: false,
        skipCacheNative: false
    };

    constructor(props) {
        super(props);
        this.state = {needRender: false, isLoading: true, height: getHeightAds(this.props.typeAds)};
    }

    async componentDidMount() {
        if (this.props.delayTime != undefined && !this.state.needRender) {
            setTimeout(() => {
                this.setState({needRender: true})
            }, this.props.delayTime);
        }
    }


    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }

    render() {
        return <View style={this.props.style}>
            <View style={{ height: this.state.height }}>
                {this._renderNativeView()}
            </View>
            {this.renderLoadingView()}
            {this._renderOfflineAds()}
        </View>;
    }

    
    renderLoadingView() {
        if (this.state.isLoading)
            return (
                <Col dial={5} style={[{ height: this.state.height, position: "absolute", left: 0, top: 0, right: 0, backgroundColor: "white" }]}>
                    {<Text style={styles.tvSponsored}>Loading ... </Text>}
                </Col>
            );
        return null;
    }
    
    private _renderOfflineAds() {
        if (this.state.isLoading || this.offlineAds == null || this.state.isHasAds)
            return null;
        return (
            <Col stretch style={[{ height: this.state.height , position: "absolute", left: 0, top: 0, right: 0}]}>
                <Text style={styles.tvSponsored}>Sponsored by: </Text>
                <View style={styles.containerBannerAds}>
                    <RowOfflineAds myAdsObj={this.offlineAds} />
                </View>
            </Col>
        );
    }

    private async _onAdFailed(){
        console.log("NativeView onAdFailed Call");
        this.offlineAds = await OfflineAdsSetting.getPreferAds(true);
        this.setState({ isLoading: false, isHasAds: false });
    }

    private _renderNativeView() {
        if (!this.state.isLoading && !this.state.isHasAds)
            return null;

        return (
            <NativeAdsViewRef
                style={{ height: this.state.isLoading ? 1 : this.state.height }}
                onUnknownError={this._onAdFailed.bind(this)}
                onAdLoaded={() => {
                    console.log("NativeView onAdLoaded Call: ");
                    this.setState({ isLoading: false, isHasAds: true });
                }}
                // onAdLoaded={this._onAdFailed.bind(this)}
                onAdFailed={this._onAdFailed.bind(this)}
                typeAds={this.props.typeAds}
            />
        );
    }

    //region banner backup =============
    private _renderBanner50Ads() {
        return (
            <View style={[styles.container, this.props.style as any]}>
                <BannerAdsView typeAds="BANNER_50"/>
            </View>
        );
    }

    private _renderRectBannerAds() {
        return (
            <Col stretch style={this.props.style}>
                <Text style={styles.tvSponsored}>Sponsored by: </Text>
                <View style={styles.containerBannerAds}>
                    <BannerAdsView typeAds="RECTANGLE_HEIGHT_250"/>
                </View>
            </Col>
        );
    }

    //endregion
}

//region styles
const styles = StyleSheet.create({
    container: {flexDirection: "column", justifyContent: 'flex-start', alignItems: 'center'},
    tvSponsored: {color: "#757575", fontSize: 13, marginTop: 6, alignSelf: "center"},
    containerBannerAds: {paddingVertical: 6, paddingHorizontal: 3, alignItems: "center"},
});

function getHeightAds(typeAds: number) {
    if (typeAds === 0 || typeAds === 1)
        return 100;
    if (typeAds === 3)
        return 250;
    return 300;
}

//endregion
