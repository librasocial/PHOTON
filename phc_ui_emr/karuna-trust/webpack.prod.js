const HtmlWebPackPlugin = require("html-webpack-plugin");
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");
const CopyPlugin = require("copy-webpack-plugin");
const deps = require("./package.json").dependencies;
const json = require("json-loader");
const Dotenv = require("dotenv-webpack");

module.exports = {
  mode: "production",
  output: {
    publicPath: "https://dev-phcdt.sampoornaswaraj.org/",
  },

  resolve: {
    extensions: [".tsx", ".ts", ".jsx", ".js", ".json"],
  },

  devServer: {
    port: 3005,
    historyApiFallback: true,
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
        },
      },
      {
        test: /\.json$/,
        loader: "json-loader",
      },
    ],
  },

  plugins: [
    new Dotenv(),
    new ModuleFederationPlugin({
      name: "karuna_trust",
      filename: "remoteEntry.js",
      remotes: {
        mainheader:
          "mainheader@https://dev-phcdt.sampoornaswaraj.org/healthui-header/remoteEntry.js",
        logincontainer:
          "logincontainer@https://dev-phcdt.sampoornaswaraj.org/healthui-login/remoteEntry.js",
        footer:
          "footer@https://dev-phcdt.sampoornaswaraj.org/healthui-footer/remoteEntry.js",
      },
      exposes: {},
      shared: {
        ...deps,
        react: {
          singleton: false,
          requiredVersion: deps.react,
        },
        "react-dom": {
          singleton: false,
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
