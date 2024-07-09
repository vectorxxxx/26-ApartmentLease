## 1、环境搭建

### 1.1、安装 linux

```bash
# 初始化一个centos7系统
vagrant init centos7 https://mirrors.ustc.edu.cn/centos-cloud/centos/7/vagrant/x86_64/images/CentOS-7.box

# 启动虚拟机
vagrant up

# 连接虚拟机
vagrant ssh

# 使用 root 账号登录
su root
vagrant

# 允许账号密码登录
vi /etc/ssh/sshd_config
# PasswordAuthentication yes
# PermitRootLogin no
service sshd restart

# 同步时间
# 查看当前系统的本地时间
date
# 查看硬件时钟（RTC），即BIOS中的实时时钟
hwclock
# 查看系统时间、硬件时钟设置，以及时区等信息
timedatectl
# 设置时区为 Asia/Shanghai
timedatectl set-timezone Asia/Shanghai
# 使用本地时间来存储硬件时钟的值，而不是UTC时间
timedatectl set-local-rtc 1
# 安装和配置 ntpdate 服务
yum install -y ntpdate
systemctl enable ntpdate
systemctl is-enabled ntpdate
systemctl status ntpdate
systemctl start ntpdate
# 手动同步时间
ntpdate pool.ntp.org
# 自动同步时间
crontab -e
# 每10分钟同步一次
*/10 * * * *  /usr/sbin/ntpdate -u pool.ntp.org >/dev/null 2>&1
# 重启服务
service crond restart
```

### 1.2、安装 MySQL

```bash
# 下载yum库
wget https://dev.mysql.com/get/mysql80-community-release-el7-9.noarch.rpm

# 安装yum库
rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023
# i-install
# v-verbose
# h-hash marks
rpm -ivh mysql80-community-release-el7-9.noarch.rpm

# 配置国内镜像
vi /etc/yum.repos.d/mysql-community.repo
[mysql80-community]
name=MySQL 8.0 Community Server
baseurl=https://mirrors.tuna.tsinghua.edu.cn/mysql/yum/mysql-8.0-community-el7-$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql-2023
       file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql

# 安装MySQL
yum install -y mysql-community-server
# 启动MySQL
systemctl start mysqld
systemctl status mysqld
systemctl enable mysqld

# 查看root用户初始密码
cat /var/log/mysqld.log | grep password
# 使用初始密码登录
mysql -uroot -p':JayQs0snJhR'
# 修改root用户密码
ALTER USER 'root'@'localhost' IDENTIFIED BY 'VectorX.123';
# 授予root用户远程登录权限
CREATE USER 'root'@'%' IDENTIFIED BY 'VectorX.123';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

### 1.3、安装 Redis

```bash
# 下载yum库
wget http://rpms.famillecollet.com/enterprise/remi-release-7.rpm
# 安装yum库
yum install epel-release -y
rpm -ivh remi-release-7.rpm

# 安装Redis
# --enablerepo选项的作用为启用一个仓库
yum --enablerepo=remi -y install redis

# 配置Redis允许远程访问
vi /etc/redis/redis.conf
#监听所有网络接口，默认只监听localhost
bind 0.0.0.0
#关闭保护模式，默认开启。开始保护模式后，远程访问必须进行认证后才能访问。
protected-mode no

# 启动Redis
systemctl start redis
systemctl status redis
systemctl enable redis
```

### 1.4、安装 MinIO

```bash
# 下载yum库
wget https://dl.min.io/server/minio/release/linux-amd64/archive/minio-20230809233022.0.0.x86_64.rpm
# 安装yum库
rpm -ivh minio-20230809233022.0.0.x86_64.rpm

# 编写MinIO服务配置文件
vi /etc/systemd/system/minio.service
```

`minio.service`

```bash
[Unit]
Description=MinIO
Documentation=https://min.io/docs/minio/linux/index.html
Wants=network-online.target
After=network-online.target
AssertFileIsExecutable=/usr/local/bin/minio

[Service]
WorkingDirectory=/usr/local
ProtectProc=invisible
EnvironmentFile=-/etc/default/minio
ExecStartPre=/bin/bash -c "if [ -z \"${MINIO_VOLUMES}\" ]; then echo \"Variable MINIO_VOLUMES not set in /etc/default/minio\"; exit 1; fi"
ExecStart=/usr/local/bin/minio server $MINIO_OPTS $MINIO_VOLUMES
Restart=always
LimitNOFILE=65536
TasksMax=infinity
TimeoutStopSec=infinity
SendSIGKILL=no

[Install]
WantedBy=multi-user.target
```

编写`EnvironmentFile`文件

```bash
mkdir /data
vi /etc/default/minio
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin
MINIO_VOLUMES=/data
MINIO_OPTS="--console-address :9001"

# 启动MinIO
systemctl start minio
systemctl status minio
systemctl enable minio
```

访问MinIO管理页面：[http://192.168.56.26:9001](http://192.168.56.26:9001)

- 账号：minioadmin
- 密码：minioadmin

MinIO服务器默认监听9000端口，用于提供HTTP访问。通过该端口，可以使用标准的HTTP工具（如浏览器、curl）访问MinIO服务器，并执行各种操作，例如上传、下载和管理存储桶等。

而9001端口是MinIO服务器的默认管理端口，用于提供MinIO的Web管理界面。通过该端口，可以访问MinIO的管理控制台，并进行存储桶的创建、权限管理、监控等操作。


### 1.5、安装 Nginx

server02 服务器

```bash
vi /etc/yum.repos.d/nginx.repo
[nginx-stable]
name=nginx stable repo
baseurl=http://nginx.org/packages/centos/$releasever/$basearch/
gpgcheck=1
enabled=1
gpgkey=https://nginx.org/keys/nginx_signing.key
module_hotfixes=true

[nginx-mainline]
name=nginx mainline repo
baseurl=http://nginx.org/packages/mainline/centos/$releasever/$basearch/
gpgcheck=1
enabled=0
gpgkey=https://nginx.org/keys/nginx_signing.key
module_hotfixes=true

# 在线安装Nginx
yum -y install nginx
systemctl start nginx
systemctl status nginx
systemctl enable nginx
systemctl is-enabled nginx
```
静态资源服务器案例

```bash
# 解压静态资源
yum install -y unzip
unzip hello-nginx.zip -d /usr/share/nginx/html

# 配置nginx
vi /etc/nginx/conf.d/hello-nginx.conf
server {
    listen       8080;
    server_name  192.168.56.226;

    location /hello-nginx {
        root   /usr/share/nginx/html;
        index  index.html;
    }
}

# 重新加载nginx配置
systemctl reload nginx

# 访问地址
http://192.168.56.226:8080/hello-nginx
```

访问地址: [http://192.168.56.226:8080/hello-nginx](http://192.168.56.226:8080/hello-nginx)


反向代理案例

```bash
vi /etc/nginx/conf.d/hello-proxy.conf
server {
    listen       9090;
    server_name  192.168.56.226;

    location / {
        proxy_pass http://www.atguigu.com;
    }
}

# 重新加载nginx配置
systemctl reload nginx
```

访问地址: [http://192.168.56.226:9090](http://192.168.56.226:9090)


### 1.6、安装 JDK

server01 服务器

```bash
yum install -y wget
wget https://download.oracle.com/java/17/archive/jdk-17.0.8_linux-x64_bin.tar.gz
tar -zxvf jdk-17.0.8_linux-x64_bin.tar.gz -C /opt

# 验证
/opt/jdk-17.0.8/bin/java -version
```

### 1.7、集成 Systemd

server02 服务器

使用Systemd管理后端服务进程，方便项目的启停

```bash
# 移动端集成Systemd
vi /etc/systemd/system/lease-app.service
[Unit]
Description=lease-app
After=syslog.target

[Service]
User=root
ExecStart=/opt/jdk-17.0.8/bin/java -jar /opt/lease/web-app-1.0-SNAPSHOT.jar 1>/opt/lease/

2>&1
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target

# 后台管理系统集成Systemd
vi /etc/systemd/system/lease-admin.service
[Unit]
Description=lease-admin
After=syslog.target

[Service]
User=root
ExecStart=/opt/jdk-17.0.8/bin/java -jar /opt/lease/web-admin-1.0-SNAPSHOT.jar 1>/opt/lease/admin.log 2>&1
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target

# 启动两个后端项目
systemctl start lease-app
systemctl enable lease-app
systemctl start lease-admin
systemctl enable lease-admin
```

### 1.8、部署前端项目

server02 服务器

#### 1.8.1、移动端

```bash
yum install -y vim
vim /etc/nginx/conf.d/app.conf
server {
    listen       80;
    server_name  lease.atguigu.com;
    
    # 静态资源
    location / {
        root   /usr/share/nginx/html/app;
        index  index.html;
    }
    
    # 接口地址
    location /app {
        proxy_pass http://192.168.56.126:8081;
    }
}

# 重新加载nginx配置
systemctl reload nginx
```

访问地址：[http://lease.atguigu.com](http://lease.atguigu.com)

#### 1.8.2、后台管理系统

```bash
vim /etc/nginx/conf.d/admin.conf
server {
    listen       80;
    server_name  admin.lease.atguigu.com;
    
    location / {
        root   /usr/share/nginx/html/admin;
        index  index.html;
    }
    location /admin {
        proxy_pass http://192.168.56.126:8080;
    }
}

# 重新加载nginx配置
systemctl reload nginx
```

访问地址：[http://admin.lease.atguigu.com](http://admin.lease.atguigu.com)



## 2、知识点汇总

- feat: 公寓信息管理

  1、统一异常处理：@ControllerAdvice、@ExceptionHandler
  2、配置文件类：@ConfigurationProperties、@EnableConfigurationProperties
  3、MP自动填充：MetaObjectHandler#strictInsertFill
  4、构造器：@Builder、.builder().build()
  5、Knife4j(OpenApi3): @Tag、@Operation、@Schema
  6、LambdaUpdateWrapper#set

- feat: 租赁管理

  1、日期格式与时区：@JsonFormat、date-format、time-zone
  2、定时任务：@EnableScheduling、@Scheduled

- feat: 用户管理

  1、@TableField#select=false

- feat: 系统管理

  1、加密：DigestUtils.md5Hex（commons-codec）
  2、MP更新策略：updateStrategy#IGNORED、NOT_NULL、NOT_EMPTY、DEFAULT、NEVER

- feat: 登录管理

  1、验证码：SpecCaptcha(easy-captcha)
  2、spring-boot-starter-data-redis已经完成了StringRedisTemplate的自动配置（RedisAutoConfiguration），直接注入使用
  3、MP手写SQL时，需要带上is_deleted逻辑删除字段
  4、链式调用：@Builder
  5、线程局部变量：ThreadLocal配合HandlerInterceptor进行全局统一拦截处理
  6、多绘制流程图，理清功能逻辑，辅助思考过程，固化思考结果

- build: 移动端后端开发-项目初始化配置

  1、Knife4j: OpenAPI、GroupedOpenApi
  2、@ConditionalOnProperty：配置项存在时，才会加载，避免报错
  3、default-flat-param-object：json做打平处理，方便使用Knife4j调试

- feat: 移动端后端开发-登录管理、找房

  1、@ConfigurationProperties：配置文件
  2、@EnableConfigurationProperties：开启配置文件
  3、@ConditionalOnProperty：属性条件
  4、阿里云发送短信请求：SendSmsRequest、client.sendSms

- feat: 移动端后端开发-个人中心

  1、异步功能：@EnableAsync、@Async

- build: 项目部署

解决编译运行报错问题

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <!-- 解决报错问题： -source 8 中不支持 文本块 -->
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
    </plugins>
    <!--编译打包过虑配置-->
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>**/*</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
    </resources>
</build>
```

解决打包问题

```xml
<!-- Spring Boot Maven插件，用于打包可执行的JAR文件 -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <!-- 解决打包不可单独运行的问题 -->
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

