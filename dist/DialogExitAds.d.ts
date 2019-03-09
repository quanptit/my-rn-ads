import React from 'react';
import { BaseDialog } from "my-rn-base-component";
interface Props {
    confirmDes?: string;
}
export declare class DialogExitAds extends BaseDialog<Props, any> {
    protected renderContent(): React.ReactChild;
    protected getWidth(): number | string;
    static showDialogExit(): void;
}
export {};
