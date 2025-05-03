import '../../public/style/default.css'

import React from 'react';
import {createRoot} from 'react-dom/client';
import {Button, Checkbox, Form, Input, Layout, message} from "antd";
import {Content, Header} from "antd/es/layout/layout";
import {LoginService} from "./rest/ApiClient";
import {BaseUtil} from "./util/UtilContainer";

let backUrl = BaseUtil.getQueryParam("back_url")
if (backUrl == null) {
    message.error("回跳链接不可为空！")
}

let oldLoginInfo = localStorage.getItem("rememberMe");

showUI();

if (oldLoginInfo != null) {
    LoginService.validateAndAutoRefresh(oldLoginInfo,
        (data) => {
            message.info("已获取登陆用户信息，正在跳转，请稍后")

            // @ts-ignore
            let main: string | undefined = data!.main;
            // 后端自动刷新了登陆信息
            if (main) {
                // 刷新登陆信息
                localStorage.setItem("rememberMe", main);
                // 用已验证的登陆信息，直接跳转
                window.location.href = backUrl + "/sso-login?info=" + main;
            } else {
                // 后端未刷新且返回了成功，旧的登陆信息有效
                // 用已验证的登陆信息，直接跳转
                window.location.href = backUrl + "/sso-login?info=" + oldLoginInfo;
            }
        },
        (data) => {
            localStorage.removeItem("rememberMe");
            message.info("账号信息已失效，请重新登陆")
        });
}

function showUI() {
    const container: Element | null = document.getElementById('root');

    if (container != null) {
        const root = createRoot(container);

        root.render(
            <Layout>
                <Header>
                    <div style={{color: "white", lineHeight: "64px", fontSize: "32px"}}>登陆页</div>
                </Header>
                <Content style={{minHeight: window.innerHeight - 64}}>
                    <div style={{width: "600px", margin: "200px auto"}}>
                        <Form
                            name="basic"
                            labelCol={{span: 8}}
                            wrapperCol={{span: 16}}
                            initialValues={{remember: true}}
                            onFinish={(val) => {
                                LoginService.login(val.account, val.password,
                                    (data) => {
                                        localStorage.setItem("rememberMe", data!.main as string);
                                        console.log(data)
                                        window.location.href = backUrl + "/sso-login?info=" + localStorage.getItem("rememberMe");
                                    });
                            }}
                            onFinishFailed={() => {
                            }}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="账号"
                                name="account"
                                rules={[{required: true, message: '请输入账号!'}]}
                            >
                                <Input/>
                            </Form.Item>

                            <Form.Item
                                label="密码"
                                name="password"
                                rules={[{required: true, message: '请输入密码!'}]}
                            >
                                <Input.Password/>
                            </Form.Item>

                            <Form.Item name="remember" valuePropName="checked" wrapperCol={{offset: 8, span: 16}}>
                                <Checkbox>记住我</Checkbox>
                            </Form.Item>

                            <Form.Item wrapperCol={{offset: 8, span: 16}}>
                                <Button type="primary" htmlType="submit">
                                    登录
                                </Button>
                            </Form.Item>
                        </Form>
                    </div>
                </Content>
            </Layout>
        );
    }
}