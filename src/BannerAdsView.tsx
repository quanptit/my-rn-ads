import React, {Component} from 'react'
import {StyleProp, ViewStyle, Text, View} from 'react-native'
import {FbBannerView} from "./bannerViews/FbBannerView";
import {AdmobBannerView} from "./bannerViews/AdmobBannerView";
import {AdxBannerView} from "./bannerViews/AdxBannerView";
import {isEqual} from "lodash"
import {RNAdsUtils} from "./RNAdsUtils";
import {MOPUBBannerView} from "./bannerViews/MOPUBBannerView";
import {RowOfflineAds} from "./RowOfflineAds";
import {OfflineAdsSetting} from "./OfflineAdsSetting";

interface Props {
    typeAds?: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER"
    isNoRefresh?: boolean
    onAdFailedToLoad?: (errorCode: number) => void
    style?: StyleProp<ViewStyle>
}

export class BannerAdsView extends Component<Props, { typeShow: string, offlineAds?: any }> {
    static defaultProps = {
        typeAds: "SMART_BANNER"
    };
    private noFail: number;

    constructor(props) {
        super(props);
        this.state = {typeShow: "NONE"};
        this.noFail = 0;
    }

    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(this.state, nextState);
    }

    async componentDidMount() {
        await this.updateTypeShow();
    }

    async onAdFailedToLoad() {
        this.noFail = this.noFail + 1;
        await this.updateTypeShow(true);
    }

    private async getNewTypeShow(): Promise<string> {
        if (this.noFail > 4) return null;

        let typeShow = await RNAdsUtils.getTypeShowBanner(this.props.typeAds, this.noFail);
        if (typeShow == null) return typeShow;
        if (typeShow === this.state.typeShow) {
            this.noFail = this.noFail + 1;
            return await this.getNewTypeShow();
        }
        return typeShow;
    }

    private async updateTypeShow(isFromFailCallback: boolean = false) {
        let typeShow = await this.getNewTypeShow();
        let offlineAds = (typeShow == null) ? await OfflineAdsSetting.getPreferAds(true) : null;
        if (isFromFailCallback && typeShow == null && offlineAds == null && this.props.onAdFailedToLoad) {
            this.props.onAdFailedToLoad(0);
            return;
        }
        this.setState({typeShow: typeShow, offlineAds: offlineAds});
    }

    render() {
        switch (this.state.typeShow) {
            case "MOPUB":
                return <MOPUBBannerView style={this.props.style} typeAds={this.props.typeAds}
                                        onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
            case "FB":
                return this._renderFbBanner();
            case "ADMOB":
                return <AdmobBannerView style={this.props.style} typeAds={this.props.typeAds} isNoRefresh={this.props.isNoRefresh}
                                        onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
            case "ADX":
                return <AdxBannerView style={this.props.style} typeAds={this.props.typeAds}
                                      onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>;
            case "NONE":
                return <View style={{height: this.props.typeAds === "RECTANGLE_HEIGHT_250" ? 250 : 50}}/>;
            default:
                if (this.state.offlineAds == null)
                    return <View style={{height: this.props.typeAds === "RECTANGLE_HEIGHT_250" ? 250 : 50}}/>;
                return <RowOfflineAds style={{height: this.props.typeAds === "RECTANGLE_HEIGHT_250" ? 250 : undefined}}
                                      myAdsObj={this.state.offlineAds}/>;
        }
    }

    private _renderFbBanner() {
        let fbTypeAds = this.props.typeAds;
        if (fbTypeAds === "SMART_BANNER")
            fbTypeAds = "BANNER_50";
        return <FbBannerView style={this.props.style} typeAds={fbTypeAds}
                             onAdFailedToLoad={this.onAdFailedToLoad.bind(this)}/>
    }
}
