/// <reference types="react" />
import { StyleProp, ViewStyle } from "react-native";
import { ComponentNoUpdate } from "my-rn-base-component";
interface Props {
    style?: StyleProp<ViewStyle>;
    myAdsObj: {
        des?: string;
        title?: string;
        ios_id?: string;
        package?: string;
    };
}
export declare class RowOfflineAds extends ComponentNoUpdate<Props, any> {
    onListItemClick(): Promise<void>;
    render(): JSX.Element;
}
export {};
