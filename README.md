# WithMe3.0
## 现状
- 正在开发中，开发笔记可以在我的个人wiki中看到：  [WithMe 3.0 开发笔记](https://wiki.icedsoul.cn/?file=011-%E9%A1%B9%E7%9B%AE/001-WithMe/001-WithMe%E5%BC%80%E5%8F%91%E7%AC%94%E8%AE%B0)

- 目前基本单人聊天和群组聊天功能已经正常，后续开发计划正在思考中。

- 项目相关问题请在issue中提问，提issue时请详细描述场景、问题，给出必要的代码、控制台或者报错截图，感谢您的支持～

- 如果你对这个项目感兴趣，想要交流IM相关开发经验或者一起造轮子,欢迎加入QQ群 [730490237](https://jq.qq.com/?_wv=1027&k=5MscE6Q) 来一起讨论共同开发2333


## 运行说明
### 使用Docker-Compose运行
1. 安装 Maven, Docker 以及 Docker Compose
2. 代码clone或者下载到本地后请进入WithMe3.0/ 目录，master分支。
3. 执行 
    ```shell
    mvn clean package -DskipTests
    cd /deployment/docker-compose
    docker-compose build
    docker-compose up
    ```
4. 在浏览器中访问 http://localhost/ 即可访问项目主页面。

### 部署至Kubernetes集群
前提是有一个至少拥有两个节点且内存充足的Kubernetes集群，关于Kubernetes搭建教程请参考官方文档和博客。

1. 配置私有Docker镜像仓库或者注册DockerHub账号，打包并上传此项目镜像。
2. 修改 deployment/kubernetes/deployment中各服务镜像地址为自己的私有镜像仓库或者DockerHub仓库。
3. 配置 nfs文件共享服务器，并且在共享目录下（/nfs/share）新建如下文件夹
    - group-mysql/data
    - group-message-mysql/data
    - message-mysql/data
    - offline-message-mysql/data
    - user-mysql/data
    - user-relation-mysql/data
    - redis/data
    
    之后分别修改 /deployment/kubernetes/pv 下各文件夹 spec.nfs.path 为你的共享文件夹名称加上述名称（若为nfs-share则不必修改），同时修改server为你的nfs服务器IP地址。
    *请注意：确保集群每个Node都安装nfs*
 4. 在项目根目录依次执行以下命令：
 ```shell
 # 创建pv
 kubectl create -f deployment/kubernetes/pv
 
 # 创建pvc
 kubectl create -f deployment/kubernetes/pvc
 
 # 创建deployment
 kubectl create -f deployment/kubernetes/deployment
 
 # 创建service
 kubectl create -f deployment/kubernetes/service
 ```
    
5. 可在http://[cluster-master-ip]:30001 访问项目主页。

如果不进行前两步操作，则默认从我的DockerHub仓库中拉取镜像，本地修改无法生效。  

若集群已存在pv策略，可根据自己集群情况执行第三步的pv配置。  

Kubernetes部署初步试运行，后续会有更多相关文档和说明，开发笔记中也会更新踩坑记录。



## DockerCompose启动失败原因及解决方法
### 内存过小
因为项目使用了多个MySQL数据库，如果机器内存过小可能会启动失败。内存较小机器可切换至share-db分支，此分支所有数据库使用同一个MySQL数据库，较小内存机器也可以正常启动。

### 端口占用
虽然项目在Docker容器中启动，但为了方便和调试已将全部服务端口映射至主机，如果主机对应端口被占用也可能导致启动失败，请检查相关端口是否被占用：

| 服务名 | 主机映射端口 | 容器内端口 | 可否取消主机端口映射 |
| ---- | ---- | ---- | ----  |
| redis | 6379 | 6379 | 可以 |
| mysql | 3307 ～ 3312 | 3306 | 可以 |
| service | 21003 ~ 21008| 8081 | 可以 |
| websocket-service | 21002 | 8282 | 可以 |
| nginx | 80 | 8081 | 不可以 |

其中除nginx之外，其它端口映射都可以取消。  
需要保证上面 **主机映射端口** 这一列端口没有占用，如果占用可以去docker-compose.yml文件中取消或者修改端口映射，只要 **别改ui-service主机映射端口** 就好。

## 开发计划
### 业务
- ~~登录、注册、添加好友~~
- ~~双人聊天消息转发，存储双人聊天记录，查看双人聊天记录~~
- ~~创建群组~~
- ~~群组聊天消息转发，存储群组聊天记录，查看群组聊天记录~~
- ~~查看群组成员，邀请好友入群~~
- ~~存储离线聊天记录，登录时查询相关离线聊天消息~~
- 聊天页面可以点击查看更多，每次去查询下一页记录
- 用户可以修改个人信息，上传头像
- **解决Netty多实例在线成员信路由共享问题**
- 进行消息过滤，预防SQL注入和XSS攻击
- 增加统一鉴权机制，进行权限校验
- 可发送表情消息
- 可发送图片消息
- 解决用户在线状态错误Bug
- 优化前端请求后端次数，减少不必要的请求
- 重构前端
- 可发送语音消息(以下为待定计划)
- 实时语音聊天功能
- 实时视频聊天功能
- 修改或重写Android App

### 基础设施、中间件
- ~~使用Docker管理服务~~
- ~~使用Docker Compose进行服务编排~~
- 使用Kubernetes进行部署（包括自动扩容，负载均衡，服务注册，API网关等）
- 使用Jenkins实现继续集成
- 引入Zipkin/Pinpoint监控调用链
- 引入Istio监控管理服务调用

### 技术更新、优化
- 发送聊天消息时使用protobuf代替json
- 必要地方增加Redis缓存
- 内部服务调用增加RPC调用版本

## 项目文档
其它项目开发技术、开发文档见doc/ 目录，目前尚在施工中。