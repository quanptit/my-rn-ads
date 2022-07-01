import React, { Component, PureComponent, useEffect, useState } from 'react';
import { Platform, StyleProp, StyleSheet, Text, ToastAndroid, View, ViewStyle } from 'react-native';
import { ScrollView } from 'react-native-gesture-handler';
import { Button } from 'react-native-paper';
import { RenderAdsUtils } from './my-rn/ads/RenderAdsUtils';
import { RNAdsUtils } from './my-rn/ads/RNAdsUtils';
import { Col } from './my-rn/base-cpn/Col';
import { LoadAdsUtils } from './my-rn/my-ads/LoadAdsUtils';


interface Props {
    style?: StyleProp<ViewStyle>
}


export function TestComponent() {
    const [showNativeAdsState, setShowNativeAdsState] = useState(false);

    LoadAdsUtils.cacheJsonAdsIfNeed("http://learnlanguage.xyz/ads/android_japan_english");

    // useEffect(() => {
    // }, []);

    async function showFANFULL() {
        console.log('Show FAN FULL Pressed')
        if (await RNAdsUtils.canShowFullCenterAds())
            RNAdsUtils.showFullCenterAds()
        else {
            ToastAndroid.show("Caching Ads, Wait a few seconds", ToastAndroid.LONG);
            RNAdsUtils.cacheAdsCenter();
        }
    }
    console.log("TestComponent Render=================");
    return (
        <ScrollView style={{ flex: 1, backgroundColor: '#FBFBD0' }}>
            <Col>
                <Text>Setting trong KeyAds.java để xác định Full Ads sẽ show là gì (FAN, ADMOB, MOPUB only ...)</Text>
                <Button mode="contained" style={[styles.marginAll]} onPress={showFANFULL}>Show FULL</Button>

                <Text style={{ marginTop: 15 }}>Setting trong KeyAds.java để xác định native sẽ show bên dưới là gì (FAN, ADMOB, MOPUB only ...)</Text>
                <Button mode="contained" style={styles.marginAll} onPress={() => {
                    setShowNativeAdsState(true);
                }}>Show Native Ads</Button>

                {showNativeAdsState &&
                    <>
                        <Text>TYPE_NATIVE_SUMMARY_LARGE</Text>
                        {RenderAdsUtils.renderRowAds({ large: true, typeAds: 3 }, [styles.card, styles.marginAll])}
                        <Text>TYPE_NATIVE_DETAIL_VOCA</Text>
                        {RenderAdsUtils.renderRowAds({ large: true, typeAds: 12 }, [styles.card, styles.marginAll])}
                        <Text>TYPE_NATIVE_SUMMARY_SMALL_CUSTOM --- Chưa làm, mới chỉnh size lại của cái trên thành nhỏ. Cần làm lại cái này.</Text>
                        {RenderAdsUtils.renderRowAds({ large: false, typeAds: 1 }, [styles.card, styles.marginAll])}
                    </>
                }

            </Col>
        </ScrollView >
    );
}

const styles = StyleSheet.create({
    marginAll: { marginHorizontal: 8, marginVertical: 2 },
    card: {
        borderWidth: StyleSheet.hairlineWidth * 2,
        borderRadius: 3,
        borderColor: "rgba(255,255,255,0.12)",
        backgroundColor: 'white',
        padding: 6,
        marginHorizontal: 6, marginVertical: 3,
        ...Platform.select({
            ios: {
                shadowOpacity: 0.3,
                shadowRadius: 3,
                shadowOffset: {
                    height: 0,
                    width: 0
                }
            },
            android: {
                elevation: 1
            },
        })
    },
    marginCommon: { marginHorizontal: 6, marginVertical: 3 }
});


