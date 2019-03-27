import React, { Component } from 'react';
import { View } from 'react-native';
import { FbBannerView } from "./bannerViews/FbBannerView";
import { AdmobBannerView } from "./bannerViews/AdmobBannerView";
import { AdxBannerView } from "./bannerViews/AdxBannerView";
import { isEqual } from "lodash";
import { RNAdsUtils } from "./RNAdsUtils";
import { MOPUBBannerView } from "./bannerViews/MOPUBBannerView";
import { RowOfflineAds } from "./RowOfflineAds";
import { OfflineAdsSetting } from "./OfflineAdsSetting";
import { RNCommonUtils } from "my-rn-base-utils";
export class BannerAdsView extends Component {
    constructor(props) {
        super(props);
        this.state = { typeShow: "NONE", isVip: false };
        this.noFail = 0;
    }
    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }
    async componentDidMount() {
        let isVip = await RNCommonUtils.isVIPUser();
        if (isVip) {
            this.setState({ isVip: true });
            return;
        }
        await this.updateTypeShow();
    }
    async onAdFailedToLoad() {
        this.noFail = this.noFail + 1;
        await this.updateTypeShow(true);
    }
    async getNewTypeShow() {
        if (this.noFail > 4)
            return null;
        let typeShow = await RNAdsUtils.getTypeShowBanner(this.props.typeAds, this.noFail);
        if (typeShow == null)
            return typeShow;
        if (typeShow === this.state.typeShow) {
            this.noFail = this.noFail + 1;
            return await this.getNewTypeShow();
        }
        return typeShow;
    }
    async updateTypeShow(isFromFailCallback = false) {
        let typeShow = await this.getNewTypeShow();
        let offlineAds = (typeShow == null) ? await OfflineAdsSetting.getPreferAds(true) : null;
        if (isFromFailCallback && typeShow == null && offlineAds == null && this.props.onAdFailedToLoad) {
            this.props.onAdFailedToLoad(0);
            return;
        }
        this.setState({ typeShow: typeShow, offlineAds: offlineAds });
    }
    render() {
        if (this.state.isVip)
            return null;
        switch (this.state.typeShow) {
            case "MOPUB":
                return <MOPUBBannerView style={this.props.style} typeAds={this.props.typeAds} onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
            case "FB":
                return this._renderFbBanner();
            case "ADMOB":
                return <AdmobBannerView style={this.props.style} typeAds={this.props.typeAds} isNoRefresh={this.props.isNoRefresh} onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
            case "ADX":
                return <AdxBannerView style={this.props.style} typeAds={this.props.typeAds} onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
            case "NONE":
                return <View style={{ height: this.props.typeAds === "RECTANGLE_HEIGHT_250" ? 250 : 50 }}/>;
            default:
                if (this.state.offlineAds == null)
                    return <View style={{ height: this.props.typeAds === "RECTANGLE_HEIGHT_250" ? 250 : 50 }}/>;
                return <RowOfflineAds style={{ height: this.props.typeAds === "RECTANGLE_HEIGHT_250" ? 250 : undefined }} myAdsObj={this.state.offlineAds}/>;
        }
    }
    _renderFbBanner() {
        let fbTypeAds = this.props.typeAds;
        if (fbTypeAds === "SMART_BANNER")
            fbTypeAds = "BANNER_50";
        return <FbBannerView style={this.props.style} typeAds={fbTypeAds} onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
    }
}
BannerAdsView.defaultProps = {
    typeAds: "SMART_BANNER"
};
