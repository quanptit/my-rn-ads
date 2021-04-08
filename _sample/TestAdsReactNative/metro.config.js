/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */
const blacklist = require('metro-config/src/defaults/blacklist')

module.exports = {
  resolver: {
    blacklistRE: blacklist([
      new RegExp("(.*\\android\\.*|.*\\__fixtures__\\.*|node_modules[\\\\]\\.bin[\\\\].*|node_modules[\\\\]react[\\\\]dist[\\\\].*|website\\node_modules\\.*|heapCapture\\bundle\.js|.*\\__tests__\\.*)$"
      ),
    ]),
  },
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
};
