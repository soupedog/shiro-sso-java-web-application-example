# shiro-sso-java-web-application-example

基于 Shiro 的 SSO(SingleSignOn) Java Web 应用示例

**Warning：**

此处未编写一个前端机制： `a.html`/ `b.html` 登陆后主动去刷新 `login.html` 中的缓存信息再调回业务页面，本项目主要关注后端，忽略了该逻辑。

出现未更新登陆信息缓存引发了异常可尝试访问 `http://localhost:8080/?reset=true` 手动清空 `login.html` 的登录缓存


**Tips:**

- `H2 数据库` 相关
    - `spring.datasource.username`、`spring.datasource.password` 中填写的啥，内嵌初始化的 `H2 数据库` 账号密码就是什么
    - [H2 数据库控制台传送门](http://localhost:8080/h2-console)
    - 控制台默认 url、账号、密码都不正确，记得从 `user-center-service` 的 `application.properties` 中复制粘贴
    - 如果 h2 控制台点击链接后提示： `…… or allow remote database creation (not recommended in secure environments)` ，大概率是存在权限问题，需要手动在 `C:\Users\Administrator` 下创建一个名为 `test.mv.db` 的文件(具体文件名取决于 `jdbc:h2:mem:test` 里填的啥)，内容为空也没问题
