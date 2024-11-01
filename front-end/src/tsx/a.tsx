import '../../public/style/default.css'

import React from 'react';
import {createRoot} from 'react-dom/client';
import {Content, Header} from "antd/es/layout/layout";
import {Button, Layout} from "antd";
import {AService} from "./rest/ApiClient";

const container: Element | null = document.getElementById('root');

if (container != null) {
    const root = createRoot(container);

    root.render(
        <Layout>
            <Header>
                <div style={{color: "white", lineHeight: "64px", fontSize: "32px"}}>A 服务页面</div>
            </Header>
            <Content style={{minHeight: window.innerHeight - 64}}>
                <div style={{width: "600px", margin: "200px auto"}}>
                    <Button type="primary" onClick={() => {
                        AService.clickButton();
                    }}>A 服务需登录身份按钮</Button>
                </div>
            </Content>
        </Layout>
    );
}