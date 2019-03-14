import React, {Component} from 'react'
import {requireNativeComponent, Text, View, StyleSheet, ViewProperties, StyleProp, ViewStyle} from 'react-native'
import {BannerAdsView} from "./BannerAdsView";
import {RNAdsUtils} from "./RNAdsUtils";
import {isEqual} from "lodash"
import {Col} from "my-rn-base-component";

let NativeAdsViewRef: any = requireNativeComponent('NativeAdsView');

interface Props {
    typeAds?: number
    allowBannerBackup?: boolean
    isAlwayPreferNative?: boolean
    delayTime?: boolean
    isScroll?: boolean
    style?: StyleProp<ViewStyle>
}

export class NativeAdsView extends Component<Props, { isLoading: boolean, needRender: boolean, height: number }> {
    public static TYPE_SUMMARY_FB = 0;
    public static TYPE_SUMMARY_SMALL_CUSTOM = 1;
    public static TYPE_SUMMARY_LARGE = 3;
    public static TYPE_DETAIL_FB = 10;
    public static TYPE_DETAIL_CUSTOM = 11;
    public static TYPE_DETAIL_VOCA = 12;

    private isCachedNativeAds: boolean;
    private isPreferShowBanner: boolean;
    static defaultProps = {
        typeAds: 3,
        allowBannerBackup: true
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
        if (this.props.isAlwayPreferNative)
            this.isPreferShowBanner = false;
        else
            this.isPreferShowBanner = await RNAdsUtils.isPreferShowBanner(this.props.typeAds);

        if (!this.isPreferShowBanner) {
            this.isCachedNativeAds = await RNAdsUtils.canShowNativeAds(this.props.typeAds);
            RNAdsUtils.cacheNativeAdsIfNeed(this.props.typeAds);
        }
        this.setState({isLoading: false});
    }


    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }

    render() {
        if (this.state.isLoading
            || (this.props.delayTime != undefined && !this.state.needRender))
            return this.renderEmptyView();
        if (this.isCachedNativeAds)
            return this._renderNativeView();

        if (this.props.allowBannerBackup === false)
            return null;

        if (this.state.height > 200)
            return this._renderRectBannerAds();

        return this._renderBanner50Ads();
    }

    //region banner backup =============
    private _renderBanner50Ads() {
        // if (this.state.bannerFail) return null;
        return (
            <View style={[styles.container, this.props.style]}>
                <BannerAdsView typeAds="BANNER_50"/>
            </View>
        );
    }

    private _renderRectBannerAds() {
        // if (this.state.bannerFail) return null;

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

    private _renderNativeView() {
        return (<NativeAdsViewRef
            style={[{height: this.state.height}, this.props.style]}
            typeAds={this.props.typeAds}
        />)
    }

    //region utils
    renderEmptyView() {
        return (
            <View style={[this.props.style, {height: this.state.height}]}/>
        )
    }

    //endregion
}

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
