import { Col, ComponentNoUpdate, Row, Touchable } from "my-rn-base-component";
import { isIOS } from "my-rn-base-utils";
import { CachedImage } from 'my-rn-cached-image';
import React from 'react';
import { Linking, StyleSheet, Text, View } from "react-native";
import { OfflineAdsSetting } from "./OfflineAdsSetting";
export class RowOfflineAds extends ComponentNoUpdate {
    async onListItemClick() {
        let myAdsObj = this.props.myAdsObj;
        let packageName = myAdsObj.package;
        OfflineAdsSetting.clickMyAds(packageName);
        if (isIOS()) {
            let schemes_url = packageName + "://";
            Linking.openURL(schemes_url).catch(err => {
                Linking.openURL("https://itunes.apple.com/app/id" + myAdsObj.ios_id + "#");
            });
        }
        else {
            if (await OfflineAdsSetting.ANDROID_isAppInstalled(packageName))
                OfflineAdsSetting.ANDROID_launchInstalled(packageName);
            else {
                OfflineAdsSetting.ANDROID_openAppFromMarket(packageName);
            }
        }
    }
    render() {
        let myAdsObj = this.props.myAdsObj;
        if (myAdsObj == null)
            return null;
        let styleImage = [styles.image];
        if (isIOS())
            styleImage.push({ borderRadius: 5 });
        let imgUrl = "https://adsservice.blob.core.windows.net/icons/" + myAdsObj.package + ".png";
        return (<Col style={[{ alignItems: "stretch", alignSelf: "stretch" }, this.props.style]}>
                <View style={styles.row}>
                    <CachedImage resizeMode="contain" source={{ uri: imgUrl }} style={styleImage}/>
                    <View style={styles.content}>
                        <Text numberOfLines={2} style={styles.title}>{myAdsObj.title}</Text>
                        <Row dial={5}>
                            <Text style={styles.des}>{myAdsObj.des == undefined ? "Good choice" : myAdsObj.des}</Text>
                        </Row>
                    </View>
                </View>
                <Touchable style={styles.btnInstall} onPress={this.onListItemClick.bind(this)}>
                    <Text style={styles.install}>{"INSTALL NOW"}</Text>
                </Touchable>
            </Col>);
    }
}
const styles = StyleSheet.create({
    row: {
        padding: 12, flexDirection: "row", alignItems: "stretch"
    },
    image: { width: 80, height: 60 },
    install: {
        color: "#ffffff", fontSize: 13, textAlign: "center",
        fontWeight: 'bold'
    },
    btnInstall: {
        borderRadius: 3, backgroundColor: "#42afe9", padding: 8, marginHorizontal: 8
    },
    content: { flex: 1, flexDirection: "column", marginLeft: 12 },
    title: { color: "#A05500", fontSize: 18, fontWeight: 'bold', marginBottom: 3, marginRight: 10 },
    des: { color: "black", fontSize: 16, marginRight: 6, flex: 1 },
});
