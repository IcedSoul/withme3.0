# WithMe3.0

## 运行说明
### 使用Docker-Compose运行
1. 安装 Maven, Docker 以及 Docker Compose
2. 代码clone或者下载到本地后请进入one-day-in-ali/ 目录，master分支。
3. 执行 
    ```shell
    mvn clean package -DskipTests
    cd /deployment/docker-compose
    docker-compose build
    docker-compose up
    ```
4. 在浏览器中访问 http://localhost/ 即可访问项目主页面。
