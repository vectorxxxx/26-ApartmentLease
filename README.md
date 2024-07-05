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

# 与网络时间保持同步
yum install chrony
# 开机自启
systemctl start chronyd
systemctl enable chronyd
# 查看时间源
chronyc sources

# 关闭防火墙
systemctl stop firewalld
# 禁止防火墙开机自启
systemctl disable firewalld

# 关闭SElinux
vi /etc/selinux/config
SELINUX=disabled
reboot
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

