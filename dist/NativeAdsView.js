import React, { Component } from 'react';
import { requireNativeComponent, Text, View, StyleSheet } from 'react-native';
import { BannerAdsView } from "./BannerAdsView";
import { RNAdsUtils } from "./RNAdsUtils";
import { isEqual } from "lodash";
import { Col } from "my-rn-base-component";
import { OfflineAdsSetting } from "./OfflineAdsSetting";
import { RowOfflineAds } from "./RowOfflineAds";
let NativeAdsViewRef = requireNativeComponent('NativeAdsView');
export class NativeAdsView extends Component {
    constructor(props) {
        super(props);
        this.state = { needRender: false, isLoading: true, height: getHeightAds(this.props.typeAds) };
    }
    async componentDidMount() {
        if (this.props.delayTime != undefined && !this.state.needRender) {
            setTimeout(() => {
                this.setState({ needRender: true });
            }, this.props.delayTime);
        }
        if (this.props.isAlwayPreferNative)
            this.isPreferShowBanner = false;
        else
            this.isPreferShowBanner = await RNAdsUtils.isPreferShowBanner(this.props.typeAds);
        if (!this.isPreferShowBanner) {
            if (!await RNAdsUtils.hasLoadNativeAds())
                this.setState({ showLoading: true });
            this.isCachedNativeAds = await RNAdsUtils.firstCacheAndCheckCanShowNativeAds(this.props.typeAds);
            if (!this.props.skipCacheNative)
                // noinspection JSIgnoredPromiseFromCall
                RNAdsUtils.cacheNativeAdsIfNeed(this.props.typeAds);
        }
        if (!this.isCachedNativeAds && this.props.allowBannerBackup === false) {
            this.offlineAds = await OfflineAdsSetting.getPreferAds(true);
        }
        this.setState({ isLoading: false, showLoading: false });
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
        if (this.props.allowBannerBackup === false) {
            if (this.offlineAds != null)
                return <RowOfflineAds style={{ height: this.state.height }} myAdsObj={this.offlineAds}/>;
            else
                return null;
        }
        if (this.state.height > 200)
            return this._renderRectBannerAds();
        return this._renderBanner50Ads();
    }
    //region banner backup =============
    _renderBanner50Ads() {
        return (<View style={[styles.container, this.props.style]}>
                <BannerAdsView typeAds="BANNER_50"/>
            </View>);
    }
    _renderRectBannerAds() {
        return (<Col stretch style={this.props.style}>
                <Text style={styles.tvSponsored}>Sponsored by: </Text>
                <View style={styles.containerBannerAds}>
                    <BannerAdsView typeAds="RECTANGLE_HEIGHT_250"/>
                </View>
            </Col>);
    }
    //endregion
    _renderNativeView() {
        return (<View style={this.props.style}>
                <NativeAdsViewRef style={{ height: this.state.height }} typeAds={this.props.typeAds}/>
            </View>);
    }
    renderEmptyView() {
        if (this.state.height > 200)
            return (<Col dial={5} style={[this.props.style, { height: this.state.height }]}>
                    {this.state.showLoading && <Text style={styles.tvSponsored}>Loading ... </Text>}
                </Col>);
        return (<View style={[this.props.style, { height: this.state.height }]}/>);
    }
}
NativeAdsView.TYPE_SUMMARY_FB = 0;
NativeAdsView.TYPE_SUMMARY_SMALL_CUSTOM = 1;
NativeAdsView.TYPE_SUMMARY_LARGE = 3;
NativeAdsView.TYPE_DETAIL_FB = 10;
NativeAdsView.TYPE_DETAIL_CUSTOM = 11;
NativeAdsView.TYPE_DETAIL_VOCA = 12;
NativeAdsView.defaultProps = {
    typeAds: 3,
    allowBannerBackup: true,
    skipCacheNative: false
};
//region styles
const styles = StyleSheet.create({
    container: { flexDirection: "column", justifyContent: 'flex-start', alignItems: 'center' },
    tvSponsored: { color: "#757575", fontSize: 13, marginTop: 6, alignSelf: "center" },
    containerBannerAds: { paddingVertical: 6, paddingHorizontal: 3, alignItems: "center" },
});
function getHeightAds(typeAds) {
    if (typeAds === 0 || typeAds === 1)
        return 100;
    if (typeAds === 3)
        return 250;
    return 300;
}
//endregion
