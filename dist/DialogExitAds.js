import React from 'react';
import { Text, StyleSheet, BackHandler, Dimensions } from "react-native";
import { DialogUtils, BaseDialog, Button, ButtonModel, Col, Row } from "my-rn-base-component";
import { NativeAdsView } from "./NativeAdsView";
export class DialogExitAds extends BaseDialog {
    //region defaultProps and Variable
    //endregion
    renderContent() {
        return (<Col style={{ paddingVertical: 15 }}>
                <NativeAdsView typeAds={NativeAdsView.TYPE_DETAIL_VOCA} isAlwayPreferNative allowBannerBackup={false} style={{ marginBottom: 12, marginHorizontal: 3 }}/>
                <Text style={[{ fontSize: 18, color: "#333333" }, styles.margin_h]}>
                    {this.props.confirmDes != null ? this.props.confirmDes : "Are you sure want to quit?"}
                </Text>
                <Row style={[{ marginTop: 15 }, styles.margin_h]}>
                    <Button model={ButtonModel.light} title="OK" style={{ flex: 1, marginRight: 6 }} onPress={() => {
            this.dismiss();
            BackHandler.exitApp();
        }}/>
                    <Button model={ButtonModel.light} title="CANCEL" style={{ flex: 1, marginLeft: 6 }} onPress={() => this.dismiss()}/>
                </Row>
            </Col>);
    }
    getWidth() {
        let max = Dimensions.get("window").width * 0.9;
        if (max < 320)
            max = 320;
        return max > 400 ? 400 : max;
    }
    static showDialogExit() {
        DialogUtils.showDialog(<DialogExitAds />);
    }
}
const styles = StyleSheet.create({
    margin_h: { marginHorizontal: 12 }
});
