import React from 'react';
import { StyleSheet, View, Text, Linking } from "react-native";
import { CachedImage } from 'react-native-cached-image';
import { Col, ComponentNoUpdate, Row, Touchable } from "my-rn-base-component";
import { OfflineAdsSetting } from "./OfflineAdsSetting";
import { isIOS } from "my-rn-base-utils";
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
        let imgUrl = "http://learnlanguage.xyz/icon/" + myAdsObj.package + ".png";
        return (<Col style={{ alignItems: "stretch", alignSelf: "stretch" }}>
                <View style={styles.row}>
                    <CachedImage resizeMode="contain" source={{ uri: imgUrl }} style={styleImage}/>
                    <View style={styles.content}>
                        <Text numberOfLines={2} style={styles.title}>{myAdsObj.title}</Text>
                        <Row dial={5}>
                            <Text style={styles.des}>{myAdsObj.des == undefined ? "Good choice" : myAdsObj.des}</Text>
                            <Touchable onPress={this.onListItemClick.bind(this)}>
                                <Text style={styles.install}>{"INSTALL NOW"}</Text>
                            </Touchable>
                        </Row>
                    </View>
                </View>
            </Col>);
    }
}
const styles = StyleSheet.create({
    row: {
        padding: 12, flexDirection: "row", alignItems: "stretch"
    },
    image: { width: 80, height: 60 },
    install: {
        fontSize: 13, borderRadius: 5, borderColor: "#42afe9", color: "#42afe9", borderWidth: 0.8,
        paddingHorizontal: 6, paddingVertical: 3
    },
    content: { flex: 1, flexDirection: "column", marginLeft: 12 },
    title: { color: "#A05500", fontSize: 18, fontWeight: 'bold', marginBottom: 3, marginRight: 10 },
    des: { color: "black", fontSize: 16, marginRight: 6, flex: 1 },
});
