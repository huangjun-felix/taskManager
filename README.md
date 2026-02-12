# 任务管理系统
## 启动方式
### 环境要求
- jdk 17
- Maven 3.9+
- Mysql 8.0+
- redis 7.2+

### 启动步骤
1. **导入项目**
    - 将项目用idea打开
    - 确保pom.xml文件能够被正常解析
   
2. **配置数据库**
    - 创建数据库：taskmanage
    - 执行sql语句
    ```sql
    create database taskmanager;
    
    use taskmanager;
    
    CREATE TABLE if not exists t_user (
    id varchar(100)  PRIMARY KEY COMMENT '主键',
    username VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名（唯一）',
    password VARCHAR(255) NOT NULL COMMENT '密码（明文即可，不强制加密）',
    create_time DATETIME not null  COMMENT '创建时间'
    );
    
    CREATE TABLE if not exists t_task (
    id varchar(100)  PRIMARY KEY COMMENT '主键',
    user_id varchar(100) NOT NULL COMMENT '所属用户',
    title VARCHAR(255) NOT NULL COMMENT '任务标题',
    description TEXT COMMENT '任务描述',
    status int DEFAULT 0 COMMENT '任务状态（0-待办，1-完成）',
    create_time DATETIME not null COMMENT '创建时间',
    update_time DATETIME not null COMMENT '更新时间'
    );
    ```
3. **配置redis**
    -启动redis服务
    - 修改yaml配置文件访问redis服务
4. **启动应用**
   - 运行主类：com.huangjun.taskmanager.TaskManagerApplication

# 接口说明

## 用户注册接口
### 接口路径：http://localhost:8080/api/user/registry

**接口参数示例**：```json
         {
	        "username": "zhangsan",
	        "password": "123456"
         }   
        ```
**返回结果**：```json
{
    "code": 200,
    "msg": "success",
    "data": null
}
        ```

## 用户登录接口
### 接口路径：http://localhost:8080/api/user/login
**接口参数示例**：```json
         {
	        "username": "zhangsan",
	        "password": "123456"
         }   
        ```
**返回结果**：```json
{
    "code": 200,
    "msg": "success",
    "data": "b0190391-00ee-4cdd-a3d8-c1cfc3be7145"
}
        ```
**登录接口返回的data数据是token数据也是用户的id**

## 任务新增接口
### 接口路径：http://localhost:8080/api/task/insert
**请求头**
authorization:token
**接口参数示例**：```json
{
  "title": "完成项目需求分析",
  "description": "对新功能模块进行详细的需求调研与文档编写。",
  "status": 1
}
        ```
**返回结果**：```json
{
    "code": 200,
    "msg": "success",
    "data": "3275c666-8267-4de0-bca2-e70bfb5e0733"
}
        ```
**接口返回的data数据是新增任务的id**

## 任务修改接口
### 接口路径：http://localhost:8080/api/task/update
**请求头**
authorization:token
**接口参数示例**：```json
{
	"id": "3275c666-8267-4de0-bca2-e70bfb5e0733",
  "title": "完成项目",
  "description": "对新功能模块进行详细的需求调研与文档编写。",
  "status": 1
}
        ```
**返回结果**：```json
{
    "code": 200,
    "msg": "success",
    "data": null
}
        ```

## 任务删除接口
### 接口路径：http://localhost:8080/api/task/delete?id={？}
**请求头**
authorization:token
**接口参数示例**：id=任务id
**返回结果**：```json
{
    "code": 200,
    "msg": "success",
    "data": null
}
        ```

## 任务查找接口
### 接口路径：http://localhost:8080/api/task/queryById?id={？}
**请求头**
authorization:token
**接口参数示例**：id=任务id
**返回结果**：```json
{
    "code": 200,
    "msg": "success",
    "data": {
        "id": "9e9acc58-8abd-433b-a581-29b471b9ba66",
        "userId": "b0190391-00ee-4cdd-a3d8-c1cfc3be7145",
        "title": "完成项目",
        "description": "对新功能模块进行详细的需求调研与文档编写。",
        "status": 1,
        "createTime": "2026-02-12T11:21:56.9614943",
        "updateTime": "2026-02-12T11:21:56.9614943"
    }
}
        ```

## 根据用户查找接口
### 接口路径：http://localhost:8080/api/task/findByUserId?userId={？}
**请求头**
authorization:token
**接口参数示例**：userId = 查找的用户id
**返回结果**：```json
{
    "code": 200,
    "msg": "success",
    "data": [
        {
            "id": "9e9acc58-8abd-433b-a581-29b471b9ba66",
            "userId": "b0190391-00ee-4cdd-a3d8-c1cfc3be7145",
            "title": "完成项目",
            "description": "对新功能模块进行详细的需求调研与文档编写。",
            "status": 1,
            "createTime": "2026-02-12T11:21:56.9614943",
            "updateTime": "2026-02-12T11:21:56.9614943"
        },
        {
            "id": "4511b641-b428-4091-a0bf-3ee28f060924",
            "userId": "b0190391-00ee-4cdd-a3d8-c1cfc3be7145",
            "title": "完成项目需求分析",
            "description": "对新功能模块进行详细的需求调研与文档编写。",
            "status": 1,
            "createTime": "2026-02-12T11:19:36.5859371",
            "updateTime": "2026-02-12T11:19:36.5859371"
        }
    ]
}
        ```
# redis使用说明
## 1. 基础配置要求
   Redis 服务已启动（默认端口 6379）
   Spring Boot 已正确配置 RedisTemplate


## 2. String 类型操作

### 存储（带过期时间）
```RedisUtils.set("user:1001", user, 30000L);``` // 30秒过期（毫秒）
### 存储（永不过期）
```RedisUtils.set("config:app", config);```
### 获取字符串值
```String json = RedisUtils.get("user:1001");```
### 获取并反序列化为对象
```User user = RedisUtils.get("user:1001", User.class);```
### 删除 key
```RedisUtils.delete("user:1001");```
### 单独设置过期时间
```RedisUtils.expire("user:1001", 60000L); // 60秒```

## 3. Hash 类型操作

### 存储单个 field（无过期）
```RedisUtils.setHash("TASK:1001", "task_001", task);```
### 存储单个 field（带过期）
```RedisUtils.setHash("TASK:1001", "task_001", task, 300000L); // 5分钟```
### 获取指定 field 的值（反序列化）
```Task task = RedisUtils.getFieldHashValue("TASK:1001", "task_001", Task.class);```
### 获取所有 field 的值（返回 List）
```List<Task> tasks = RedisUtils.getHashAllValue("TASK:1001", Task.class);```
### 删除指定 field
```RedisUtils.deleteHashField("TASK:1001", "task_001");```

## 4. 分布式锁（基于 Redis + Lua）

### 尝试加锁
```
boolean locked = RedisUtils.tryLock("lock:create_order", 10000L); // 10秒自动释放
if (locked) {
try {
// 执行业务逻辑
} finally {
RedisUtils.unlock("lock:create_order");
}
}
```
#### 锁值为 UUID，确保线程安全
#### 自动续期由业务控制（建议配合 expireTime）
### 释放锁
```RedisUtils.unlock("lock:create_order");```

## 5. 自增 ID 生成

```long taskId = RedisUtils.nextId("task");```
