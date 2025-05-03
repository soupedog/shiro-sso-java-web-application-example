# shiro-sso-java-web-application-example

基于 Shiro 的 SSO(SingleSignOn) Java Web 应用示例。

试验请先启动三个服务：
- [a-service](a-service)
- [b-service](b-service)
- [user-center-service](user-center-service)


a、b 服务分别提供一个已登录用户才允许点击的按钮。

一次登录后即可在 a、b 系统中共享登录信息。

[A 系统前端页面](http://localhost:8081/)

[B 系统前端页面](http://localhost:8082/)

**Tips:**

- `H2 数据库` 相关
    - `spring.datasource.username`、`spring.datasource.password` 中填写的啥，内嵌初始化的 `H2 数据库` 账号密码就是什么
    - [H2 数据库控制台传送门](http://localhost:8080/h2-console)
    - 控制台默认 url、账号、密码都不正确，记得从 `user-center-service` 的 `application.properties` 中复制粘贴
    - 如果 h2 控制台点击链接后提示： `…… or allow remote database creation (not recommended in secure environments)` ，大概率是存在权限问题，需要手动在 `C:\Users\Administrator` 下创建一个名为 `test.mv.db` 的文件(具体文件名取决于 `jdbc:h2:mem:test` 里填的啥)，内容为空也没问题
