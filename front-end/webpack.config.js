// 内置插件
const path = require("path");// 安全拼接 baseURL 与 path 的工具
// 外部插件
const HtmlWebpackPlugin = require("html-webpack-plugin");
const {CleanWebpackPlugin} = require("clean-webpack-plugin");
const CompressionPlugin = require("compression-webpack-plugin");
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

module.exports = {
    // 指定下列依赖从远端获取，不打包进 .js 文件
    externals: {
        // "react": "React",
        // "react-dom": "ReactDOM"
        // "vditor": "Vditor"
    },
    // 添加需要解析的文件格式(import 时不需要再标注下列尾缀)
    resolve: {
        extensions: ['.ts', '.tsx', '.js', '.json']
    },
    mode: 'development', // development,production
    devtool: 'eval-source-map',
    //入口文件的路径(可配多个，此处只配置了 "index" 实体)
    entry: {
        login: "./src/tsx/login.tsx",
        a: "./src/tsx/a.tsx",
        b: "./src/tsx/b.tsx"
    },
    output: {
        path: path.join(__dirname, "/dist"),// distribution 的缩写
        filename: "./js/[name]-[chunkhash].js"
    },
    devServer: {
        // static: path.join(__dirname,"/src/html"),// html 页面不是动态生成，想指定特定目录作为 root 的话
        historyApiFallback: true,
        compress: true,
        open: true,
        port: 9000,
        // host: "192.168.18.12"
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
                options: {
                    presets: [
                        '@babel/preset-env',
                        '@babel/preset-react',
                        '@babel/preset-typescript'
                    ]
                }
            },
            {
                test: /\.css?$/,
                use: [
                    {loader: "style-loader"},
                    {loader: "css-loader"}
                ],
                // exclude: /node_modules/,
                // include: path.resolve(__dirname, "src")
            },
            {
                test: /\.less?$/,
                use: [
                    {loader: 'style-loader'},
                    {loader: 'css-loader'},
                    {loader: 'less-loader'}
                ],
                // exclude: /node_modules/,
                // include: path.resolve(__dirname, "src")
            },
            {
                test: /\.s[ac]ss$/i,
                use: [
                    // Creates `style` nodes from JS strings
                    {loader: "style-loader"},
                    // Translates CSS into CommonJS
                    {loader: "css-loader"},
                    // Compiles Sass to CSS
                    {loader: "sass-loader"},
                ],
            }
        ]
    },
    plugins: [
        new CleanWebpackPlugin(
            {
                cleanOnceBeforeBuildPatterns: [
                    path.join(__dirname, "./dist")
                ]
            }
        ),
        new HtmlWebpackPlugin({
            filename: "login.html",
            title: "登录页",
            favicon: "./public/image/favicon.ico",
            template: "./public/html/defaultTemplate.html",
            chunks: ["login", "commons"],
            inject: "body",
            minify: {
                removeComments: true,
                collapseWhitespace: true
            }
        }),
        new HtmlWebpackPlugin({
            filename: "a.html",
            title: "A 服务页面",
            favicon: "./public/image/favicon.ico",
            template: "./public/html/defaultTemplate.html",
            chunks: ["a", "commons"],
            inject: "body",
            minify: {
                removeComments: true,
                collapseWhitespace: true
            }
        }),
        new HtmlWebpackPlugin({
            filename: "b.html",
            title: "B 服务页面",
            favicon: "./public/image/favicon.ico",
            template: "./public/html/defaultTemplate.html",
            chunks: ["b", "commons"],
            inject: "body",
            minify: {
                removeComments: true,
                collapseWhitespace: true
            }
        }),
        new CompressionPlugin({
            algorithm: 'gzip', // 类型
            test: /\.(js|css)$/, // 匹配规则
            threshold: 10240, // 字节数 只处理比这个大的资源
            minRatio: 0.8 // 压缩率 只有比这个小的才会处理
        }),
        new BundleAnalyzerPlugin()
    ],
    optimization: {
        splitChunks: {
            // 打包公共依赖
            // chunks: "all",
            // name: "commons"
        }
    }
}