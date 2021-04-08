import React from 'react';
import { AppRegistry, LogBoxStatic, Text } from 'react-native';
import { LogBox } from 'react-native';
LogBox.ignoreAllLogs();
import { initPrototype } from './my-rn/base-utils/Prototype';
// YellowBox.ignoreWarnings([
//     'Non-serializable values were found in the navigation state',
// ]);
import { TestComponent } from './TestComponent';
initPrototype();

function MyAppLoading() {
    return <TestComponent />
}

AppRegistry.registerComponent("App", () => MyAppLoading);


