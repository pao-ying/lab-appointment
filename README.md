# 教师实验室预约系统-后端
## 项目介绍
教师实验室预约系统是一款基于 springboot 的预约系统，该系统旨在协助教师基于课程要求预约符合条件的实验室。

本系统后端采用 restful 风格为前端系统提供一系列接口完成前后端交互，同时系统分为两种角色，管理员端和教师端。

### 管理员端
管理员端可以统一管理实验室和教师信息，并且对教师预约实验室的预约信息进行管理。
### 教师端
教师端可以管理个人信息、课程信息和预约信息，并对实验室进行预约。

## 技术栈
### 运行环境

| 环境     | 版本            |
| -------- | --------------- |
| 操作系统 | windows / linux |
| JDK      | 11.0            |
| mysql    | 8.0             |

### 依赖

- spring-boot
- spring-mvc
- mysql
- mybatis-plus
- spring-boot-validation 验证依赖
- spring-security-crypto 加密依赖
- spring-boot-aop AOP依赖
- caffeine 缓存依赖
- slf4j 日志框架

## 特色

- 使用 `Restful` 风格的接口，用一个 URI 表示一个资源，并通过 GET、POST、PUT、DELETE 操作资源。
- 前后端约定了创建 `ResultVO` 类用于封装数据，包含状态码，信息和数据，其中数据封装在 Map 中以键值对的形式返回。并且通过 `Jackson` 框架序列化所有属性。
- 由于 springboot 不会处理受检异常，所以显示捕获受检异常，转抛为非受检异常，并且自定义异常controller，创建方法捕获自定义异常，返回 JSON 数据。
- 使用 springboot 验证依赖检测前端请求参数，通过注解的方式进行简单的数值验证，如 `@NotNull`、`@Max/@Min`等，不符合的情况下抛出异常，并由自定义异常 controller 捕获对应的异常，返回 JSON 数据。
- 使用 spring 提供的安全框架，注入 `PasswordEncoder` 组件，对密码进行非对称加密入库，可以防止撞库或脱库时对用户密码的泄露。
- 使用 `token` 对身份进行验证，Restful 设计思想中，客户端不再保存用户状态，用户登录后直接将身份信息保存在 header 中，客户端和服务器的身份通过 `token` 中的信息来判别。同时 `token` 使用 Spring-Security 提供的非对称加密技术，可自定义密钥和盐值，通过 `@Value` 注解并结合 `SpEL`表达式，可以将`application.yml`配置中的信息注入。
- 使用三个 `Interceptor` 对用户身份进行拦截，首先通过 `LoginInterceptor` 的用户可以访问公共接口，通过 `adminInterceptor` 的用户可以访问管理员接口，通过 `teacherInterceptor` 的用户可以访问教师接口。拦截的方法就是通过判断用户 `header` 中的 `token` ，判断其中的身份，给予对应的权限。
- 使用 `Caffeine` 缓存技术对系统中的预约信息进行缓存，如对应教师的预约信息，对应实验室的预约信息。
- 使用 `Mybatis-Plus`持久层框架，可以通过实现 `BaseMapper` 接口，直接调用插入、查询等方法。
- 使用 MyBatis xml 的动态SQL查询实现数据按条件查询的功能。
- 在数据库设计阶段没有使用外键，而是使用的索引。级联操作在应用层用代码实现。

## 缺陷

- token 是自定义实现的，没有设置过期时间。可以使用缓存，如 redis，对 token 数据进行管理。
- 缓存技术使用的是 `Caffeine`，而不是 `Redis`，所以只适用于单机缓存解决方案，而对于集群服务不支持。
- 数据的查询与表与表之间的关联是在应用层对各个表进行查询实现的，实际上可以使用 `Join` 完成表的连接进行操作。

## 数据库依赖

### 教师表 teacher

| 字段名      | 类型     | 注释                       |
| ----------- | -------- | -------------------------- |
| id          | int      | 主键                       |
| name        | varchar  | 教师名称                   |
| profession  | varchar  | 教师职称教授、副教授、讲师 |
| user_id     | int      | 每个教师都的唯一ID         |
| password    | varchar  | 非对称加密的密码           |
| update_time | datetime | 教师信息更新的时间         |
| create_time | datetime | 教师创建的时间             |

### 实验室表 lab

| 字段名      | 类型     | 注释               |
| ----------- | -------- | ------------------ |
| id          | int      | 主键               |
| name        | varchar  | 实验室名称         |
| num         | int      | 实验室可容纳的人数 |
| des         | varchar  | 实验室的描述信息   |
| update_time | datetime | 实验室信息更新时间 |
| create_time | datetime | 实验室创建时间     |

### 课程表 course

| 字段名      | 类型     | 注释               |
| ----------- | -------- | ------------------ |
| id          | int      | 主键               |
| name        | varchar  | 课程名称           |
| time        | int      | 课程学时           |
| num         | int      | 课程可教授学生数量 |
| tid         | int      | 课程对应的教师     |
| update_time | datetime | 课程信息更新时间   |
| create_time | datetime | 课程创建时间       |

### 预约时间 course_time

| 字段名 | 类型 | 注释           |
| ------ | ---- | -------------- |
| id     | int  | 主键           |
| cid    | int  | 课程id         |
| lid    | int  | 实验室id       |
| day    | int  | 预约的星期几   |
| lesson | int  | 预约的第几节课 |

### 预约周次 course_week

| 字段名 | 类型 | 注释         |
| ------ | ---- | ------------ |
| id     | int  | 主键         |
| ctid   | int  | 预约时间主键 |
| week   | int  | 预约的周次   |

## 接口文档

本项目采用 `token` 来进行身份判定，所以除登录外的其他接口，请求时都需要在 `header` 中添加 `token` 字段验明身份。`token` 值可以在登录后，从响应 `header` 中获取。

### 教师端

#### 登录

- 接口地址：`/api/login`

- 请求方式：`POST`

- 请求参数：

  ```json
  {
      "userName": 20210001,
     	"password": "123456"
  }
  ```

- 返回数据：

  ```json
  {
      "code": 200,
      "message": "登录成功",
      "data": {
          "role": "teacher",
          "user": {
              "id": "1404352314713686018",
              "nickName": "t1",
              "userName": 20210001,
              "profession": "教授",
              "createTime": "2021-06-14T16:17:38",
              "updateTime": "2021-06-17T20:03:06"
          }
      }
  }
  ```

#### 添加课程

- 接口地址：`/api/teacher/:tid/course`

- 请求方式：`POST`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ```

  ```json
  {
      "name": "courseTest1", 
      "time": 20, 
      "number": 100
  }
  ```

- 返回参数

  ```json
  {
      "code": 200,
      "message": "添加课程成功",
      "data": {}
  }
  ```

#### 更新课程

- 接口地址：`/api/teacher/:tid/course/:cid`

- 请求方式：`PUT`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  cid: 1404643241558556673 // 课程主键
  ```

  以下信息均为【可选】

  ```json
  {
      "name": "courseTest2", 
      "time": 20, 
      "number": 100, 
  }
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "更新课程成功",
      "data": {}
  }
  ```

#### 删除课程

- 接口地址：`/api/teacher/:tid/course/:cid`

- 请求方式：`DELETE`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  cid: 1404643241558556673 // 课程主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "删除课程成功",
      "data": {}
  }
  ```

#### 获取所有课程

- 接口地址：`/api/teacher/:tid/course`

- 请求方式：`GET`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "获取所有课程成功",
  	"data": {
          "courseList": [
              {
                  "id": "1404352314713686018",
                  "name": "c1",
                  "time": 20,
                  "number": 120,
                  "createTime": "2021-06-14T20:58:53",
                  "updateTime": "2021-06-14T20:58:53"
              }
          ]
      }
  }
  ```

#### 获取指定课程

- 接口地址：`/api/teacher/:tid/course/:cid`

- 请求方式：`POST`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  cid: 1404643241558556673 // 课程主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "获取教师对应课程成功",
      "data": {
          "course": {
              "id": 1404423095191642113,
              "tid": 1404352314713686018,
              "name": "c1",
              "time": 20,
              "num": 120,
              "createTime": "2021-06-14T20:58:53",
              "updateTime": "2021-06-14T20:58:53"
          }
      }
  }
  ```

#### 修改密码

- 请求地址：`/api/teacher/:tid`

- 请求方式：`PUT`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ```

  ```json
  {
      "pwd": "123456"
  }
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "修改密码成功",
      "data": {}
  }
  ```

  

#### 实验室预约

- 请求地址：`/api/teacher/:tid/course/:cid/lab/:lid/appointment`

- 请求方式：`POST`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  cid: 1404643241558556673 // 课程主键
  lid: 1402679606984589313 // 实验室主键
  ```

  ```json
  {
      "day": 3, 
      "lesson": 5, 
      "weeks": [
          5,
          6
      ]
  }
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "预约成功",
      "data": {}
  }
  ```

#### 获取符合人数实验室

- 请求地址：`/api/teacher/:tid/course/:cid/appointment`

- 请求方式：`GET`

- 请求参数：

  ```json
  tid: 1404352314713686018 // 教师主键
  cid: 1404643241558556673 // 课程主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "获取符合要求实验室列表成功",
      "data": {
          "laboratoryList": [
              {
                  "id": 1402679606984589313,
                  "name": "#9011",
                  "number": 120,
                  "des": "该实验室编号为901，可容纳120名学生",
                  "createTime": "2021-06-10T01:30:53",
                  "updateTime": "2021-06-10T01:42:31"
              },
              {
                  "id": 1402897182062809090,
                  "name": "#902",
                  "number": 140,
                  "des": "该实验室编号为902，可容纳140名学生",
                  "createTime": "2021-06-10T15:55:27",
                  "updateTime": "2021-06-10T15:55:27"
              }
          ]
      }
  }
  ```

### 共用端

共用端主要是对预约的一些操作，但是对于某一个教师来说只能操作该教师自己的预约，而管理员可以操作所有教师的预约。若越权操作会返回 `403`。

#### 获取对应教师预约

- 请求地址：`/api/teacher/:tid/appointment`

- 请求方式：`GET`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "获得该教师的所有预约",
      "data": {
          "appointmentList": [
              {
                  "ctId": 1403771091146600450,
                  "courseName": "c2",
                  "labName": "#9011",
                  "lesson": 1,
                  "day": 1,
                  "weeks": [
                      4,
                      5,
                      6,
                      7,
                      8
                  ]
              }
          ]
      }
  }
  ```

#### 获得对应实验室的预约

- 请求地址：`/api/lab/:lid/appointment`

- 请求方式：`GET`

- 请求参数

  ```json
  lid: 1404352314713686018 // 实验室主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "获得该实验室的所有预约",
      "data": {
          "appointmentList": [
              {
                  "lesson": 2,
                  "day": 3,
                  "weeks": [
                      1,
                      2,
                      3,
                      4,
                      5
                  ]
              },
              {
                  "lesson": 1,
                  "day": 3,
                  "weeks": [
                      1,
                      2,
                      3,
                      4,
                      5
                  ]
              },
              {
                  "lesson": 2,
                  "day": 4,
                  "weeks": [
                      1,
                      2,
                      3,
                      4,
                      5
                  ]
              }
          ]
      }
  }
  ```

#### 删除预约

- 请求地址：`/api/teacher/:tid/appointment/:ctid`

- 请求方式：`DELETE`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ctid: 1404352314713686017 // 预约时间主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "删除该预约成功",
      "data": {}
  }
  ```

### 管理端

#### 添加实验室

- 请求地址：`/api/admin/lab`

- 请求方式：`POST`

- 请求参数

  ```json
  {
      "name": "#903", 
      "number": 160, 
      "des":  "该实验室的描述信息"
  }
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "添加实验室成功",
      "data": {}
  }
  ```

#### 删除实验室

- 请求地址：`/api/admin/lab/:lid`

- 请求方式：`DELETE`

- 请求参数

  ```json
  lid: 1404352314713686017 // 实验室主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "删除实验室成功",
      "data": {}
  }
  ```

#### 修改实验室

- 请求地址：`/api/admin/lab/:lid`

- 请求方式：`PUT`

- 请求参数

  ```json
  lid: 1404352314713686017 // 实验室主键
  ```

  以下信息均为【可选】

  ```json
  {
      "name": "#903", 
      "number": 160, 
      "des":  "该实验室的描述信息"
  }
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "修改实验室成功",
      "data": {}
  }
  ```

#### 添加教师

- 请求地址：`/api/admin/teacher`

- 请求方式：`POST`

- 请求参数

  ```json
  {
      "nickName": "teacher3",
      "major": "软件工程",
      "profession": "教授"
  }
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "添加教师成功",
      "data": {
          "teacher": {
              "id": 1404357138616164354,
              "nickName": "t3",
              "major": "软件工程",
              "pwd": "20210003",
              "userId": "20210003",
              "profession": "教授"
          }
      }
  }
  ```

#### 删除教师

- 请求地址：`/api/admin/teacher/:tid`

- 请求方式：`DELETE`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "删除教师成功",
      "data": {}
  }
  ```

#### 修改教师

- 请求地址：`/api/admin/teacher/:tid`

- 请求方式：`PUT`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ```

  以下信息均为【可选】

  ```json
  {
      "nickName": "teacher3",
      "major": "软件工程", 
      "profession": "教授"
  }
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "修改成功",
      "data": {}
  }
  ```

#### 重置教师密码

- 请求地址：`/api/admin/teacher/:tid/password`

- 请求方式：`PUT`

- 请求参数

  ```json
  tid: 1404352314713686018 // 教师主键
  ```

- 返回数据

  ```json
  {
      "code": 200,
      "message": "重置教师密码成功",
      "data": {}
  }
  ```

  



