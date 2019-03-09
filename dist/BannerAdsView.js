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
export class BannerAdsView extends Component {
    constructor(props) {
        super(props);
        this.state = { noFail: 0, typeShow: "NONE" };
    }
    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }
    async componentDidMount() {
        await this.update(this.state.noFail);
    }
    async onAdFailedToLoad() {
        let newNoFail = this.state.noFail + 1;
        await this.update(newNoFail);
    }
    async update(noFail) {
        let typeShow = await RNAdsUtils.getTypeShowBanner(noFail);
        if (typeShow === this.state.typeShow)
            typeShow = null;
        let offlineAds = typeShow == null ? await OfflineAdsSetting.getPreferAds(true) : null;
        if (typeShow == null && offlineAds == null && this.props.onAdFailedToLoad) {
            this.props.onAdFailedToLoad(0);
            return;
        }
        this.setState({ noFail: noFail, typeShow: typeShow, offlineAds: offlineAds });
    }
    render() {
        // if (true){
        //     return <AdxBannerView style={this.props.style} typeAds={this.props.typeAds}
        //                           onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
        // }
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
                let heihgt = this.props.typeAds === "RECTANGLE_HEIGHT_250" ? 250 : 50;
                return <View style={{ height: heihgt }}/>;
            default:
                return <RowOfflineAds myAdsObj={this.state.offlineAds}/>;
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
