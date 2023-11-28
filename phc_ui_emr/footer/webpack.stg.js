const HtmlWebPackPlugin = require("html-webpack-plugin");
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");
const CopyPlugin = require("copy-webpack-plugin");
const deps = require("./package.json").dependencies;
module.exports = {
  output: {
    publicPath: "https://dev-phcdt.sampoornaswaraj.org/healthui-footer/",
  },

  resolve: {
    extensions: [".tsx", ".ts", ".jsx", ".js", ".json"],
  },

  module: {
    rules: [
      {
        test: /\.m?js/,
        type: "javascript/auto",
        resolve: {
          fullySpecified: false,
        },
      },
      {
        test: /\.(css|s[ac]ss)$/i,
        use: ["style-loader", "css-loader", "postcss-loader"],
      },
      {
        test: /\.(ts|tsx|js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            presets: [
              "@babel/preset-env" /* to transfer any advansed ES to ES5 */,
              "@babel/preset-react",
            ], // to compile react to ES5
          },
        },
      },
      {
        test: /\.(jpg|png)$/,
        use: {
          loader: "url-loader",
        },
      },
      {
        test: /\.json$/,
        loader: "json-loader",
      },
    ],
  },

  plugins: [
    new ModuleFederationPlugin({
      name: "footer",
      filename: "remoteEntry.js",
      remotes: {},
      exposes: {
        "./Footer": "./src/Footer.js",
      },
      shared: {
        ...deps,
        react: {
          singleton: true,
          requiredVersion: deps.react,
        },
        "react-dom": {
          singleton: true,
          requiredVersion: deps["react-dom"],
        },
      },
    }),
    new HtmlWebPackPlugin({
      template: "./src/index.html",
    }),
    new CopyPlugin({
      patterns: [{ from: "public/img", to: "img" }],
      options: {
        concurrency: 100,
      },
    }),
  ],
};
