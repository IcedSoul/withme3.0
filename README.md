# WithMe3.0
## 现状
- 正在开发中，开发笔记可以在我的个人wiki中看到：  [WithMe 3.0 开发笔记](https://wiki.icedsoul.cn/?file=011-%E9%A1%B9%E7%9B%AE/001-WithMe/001-WithMe%E5%BC%80%E5%8F%91%E7%AC%94%E8%AE%B0)

- 项目相关问题请在issue中提问，提issue时请详细描述场景、问题，给出必要的代码、控制台或者报错截图，感谢您的支持～

- 如果你对这个项目感兴趣，想要交流IM相关开发经验或者一起造轮子,欢迎加入QQ群 [730490237](https://jq.qq.com/?_wv=1027&k=5MscE6Q) 来一起讨论共同开发2333


## 运行说明
1. 安装 Maven, Docker 以及 Docker Compose
2. 代码clone或者下载到本地后请进入WithMe3.0/ 目录，master分支。
3. 执行 
    ```shell
    mvn clean package -DskipTests
    docker-compose build
    docker-compose up
    ```
4. 在浏览器中访问 http://localhost/ 即可访问项目主页面。
## 启动失败原因及解决方法
### 内存过小
因为项目使用了多个MySQL数据库，如果机器内存过小可能会启动失败。内存较小机器可切换至share-db分支，此分支所有数据库使用同一个MySQL数据库，较小内存机器也可以正常启动。

### 端口占用
虽然项目在Docker容器中启动，但为了方便和调试已将全部服务端口映射至主机，如果主机对应端口被占用也可能导致启动失败，请检查相关端口是否被占用：

| 服务名 | 主机映射端口 | 容器内端口 | 可否取消主机端口映射 |
| ---- | ---- | ---- | ----  |
| redis | 6379 | 6379 | 可以 |
| mysql | 3307 ～ 3312 | 3306 | 可以 |
| service | 21003 ~ 21008| 8081 | 可以 |
| websocket-service | 21002 | 8282 | 不可以 |
| nginx | 80 | 8081 | 不可以 |

其中除nginx和websocket-service这两个，其它端口映射都可以取消。  
其实websocket-service这个端口使用nginx代理后也可以不映射到主机的，不过目前我还没配，配过之后会更新这里的说明。
需要保证上面 **主机映射端口** 这一列端口没有占用，如果占用可以去docker-compose.yml文件中取消或者修改端口映射，只要 **别改最后两个** 就好。

## 项目文档
其它项目开发技术、开发文档见doc/ 目录，目前尚在施工中。