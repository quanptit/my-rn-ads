import React from 'react';
import {Text, StyleSheet, BackHandler, Dimensions} from "react-native";
import {DialogUtils, BaseDialog, Button, ButtonModel, Col, Row} from "my-rn-base-component";
import {NativeAdsView} from "./NativeAdsView";

interface Props {
    confirmDes?: string
}

export class DialogExitAds extends BaseDialog<Props, any> {
    //region defaultProps and Variable
    //endregion

    protected renderContent(): React.ReactChild {
        return (
            <Col style={{paddingVertical: 15}}>
                <NativeAdsView typeAds={NativeAdsView.TYPE_DETAIL_VOCA} isAlwayPreferNative style={{marginBottom: 12, marginHorizontal: 3}}/>
                <Text style={[{fontSize: 18, color: "#333333"}, styles.margin_h]}>
                    {this.props.confirmDes != null ? this.props.confirmDes : "Are you sure want to quit?"}
                </Text>
                <Row style={[{marginTop: 15}, styles.margin_h]}>
                    <Button model={ButtonModel.light} title="OK" style={{flex: 1, marginRight: 6}}
                            onPress={() => {
                                this.dismiss();
                                BackHandler.exitApp();
                            }}/>
                    <Button model={ButtonModel.light} title="CANCEL" style={{flex: 1, marginLeft: 6}} onPress={() => this.dismiss()}/>
                </Row>
            </Col>
        )
    }

    protected getWidth(): number | string {
        let max = Dimensions.get("window").width * 0.9;
        if (max < 320) max = 320;
        return max > 400 ? 400 : max;
    }

    public static showDialogExit() {
        DialogUtils.showDialog(<DialogExitAds/>);
    }
}

const styles = StyleSheet.create({
    margin_h: {marginHorizontal: 12}
});