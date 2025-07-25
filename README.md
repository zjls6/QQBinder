# QQBinder

QQBinder 是一个用于 Minecraft 服务器的插件，它提供了 QQ 账号与游戏账号的绑定功能，以及每日签到系统。通过此插件，服务器管理员可以更好地管理玩家社区，玩家也可以享受到更多的社交功能。

## 功能特性

- QQ账号与游戏账号绑定
  - 基于令牌(Token)的安全绑定机制
  - 可配置的绑定请求过期时间
  - 双向验证（游戏内和QQ群内）
  - QQ等级限制功能
- 每日签到系统
  - 签到天数统计
  - 连续签到记录
- 解封系统
  - 每日解封次数限制
  - 解封记录追踪
- 数据持久化存储（YAML）
- 可配置的绑定成功命令执行

## 绑定系统说明

绑定流程：
1. 玩家在游戏内使用 `/qqbind bind <QQ号>` 或在QQ群内使用 `#bind <游戏ID>` 发起绑定请求
2. 系统生成唯一的绑定令牌(UUID Token)
3. 对方需要在配置的过期时间内（默认60秒）完成验证：
   - 如果在游戏内发起，需要在QQ群内验证
   - 如果在QQ群内发起，需要在游戏内使用 `/qqbind <token>` 验证
4. 验证成功后，系统会：
   - 执行配置文件中设置的绑定成功命令（如添加权限组）
   - 向玩家发送绑定成功通知

安全特性：
- 每个绑定请求都有唯一的令牌(UUID)，确保请求的唯一性和安全性
- 绑定请求会在指定时间后自动过期（可在配置文件中设置）
- 需要达到指定的QQ等级才能绑定（防止小号滥用）
- 支持解绑功能
- 双向验证机制确保账号所有权

技术实现：
- 使用 QQBindManager 管理绑定请求和状态
- 绑定请求通过 QQBindRequest 类进行封装，包含：
  - UUID token（唯一标识符）
  - QQ号码
  - 玩家UUID
  - 请求时间戳（用于检查过期）
- 使用 AmazingBot API 进行QQ绑定关系的存储
- 支持自定义绑定成功后的命令执行

## 签到系统说明

签到功能：
1. 玩家在QQ群内使用 `#qd` 命令进行每日签到
2. 系统会检查玩家是否已经绑定QQ账号
3. 系统会检查玩家今天是否已经签到
4. 签到成功后，系统会：
   - 记录签到时间
   - 增加总签到天数
   - 更新连续签到天数（如果昨天签到了，连续签到天数+1；否则重置为1）
   - 发送签到成功通知，包含签到天数和连续签到天数信息

## 解封系统说明

解封功能：
1. 玩家在QQ群内使用 `#unban` 命令申请自助解封
2. 系统会检查玩家是否已经绑定QQ账号
3. 系统会检查玩家是否被封禁
4. 系统会检查玩家是否符合解封条件：
   - 不是被管理员手动封禁的（只有被控制台封禁的才能自助解封）
   - 不是被永久封禁的
   - 今日解封次数未达到上限（通过配置文件中的 maxUnbanPerDay 设置）
5. 解封成功后，系统会：
   - 执行解封命令
   - 记录解封时间和当天解封次数
   - 发送解封成功通知，包含剩余解封次数信息

## 依赖要求

- LiteBans
- LuckPerms

## 安装说明

1. 下载最新版本的 QQBinder.jar
2. 将 QQBinder.jar 放入服务器的 plugins 文件夹
3. 重启服务器或使用 /reload 命令
4. 编辑生成的配置文件 (plugins/QQBinder/config.yml)
5. 再次重启服务器或使用 /qqbinder reload 命令

## 配置说明

配置文件位于 `plugins/QQBinder/config.yml`：

```yaml
# 启用插件功能的QQ群列表
enabledGroups:
  - 974519032
  - 833707295

# 绑定请求过期时间（秒）
expiresTime: 60

# 最低QQ等级要求
minLevel: 10

# 每天最大解封次数
maxUnbanPerDay: 2

# 绑定成功后执行的命令
# %player% 将被替换为玩家名称
bind-succeed-commands:
  - 'lp user %player% parent add qqbind'
```

## 命令说明

### 游戏内命令

- `/qqbind` - 查看绑定状态和插件信息
- `/qqbind <token>` - 确认绑定请求（使用QQ群内收到的token）
- `/qqbinder reload` - 重新加载插件配置（需要权限：`qqbinder.admin`）

### QQ群命令

- `#bind <游戏ID>` - 发起绑定请求，将QQ账号与游戏ID绑定
- `#qd` - 每日签到，累计签到天数并记录连续签到
- `#unban` - 自助解封（每日限制次数，仅适用于控制台封禁且非永久封禁的情况）

## 权限说明

- `qqbinder.bind` - 允许使用绑定命令
- `qqbinder.unbind` - 允许使用解绑命令
- `qqbinder.reload` - 允许重载配置文件
- `qqbinder.admin` - 管理员权限，包含所有权限

## 数据存储

插件使用 YAML 文件存储玩家数据，位于 `plugins/QQBinder/data.yml`

数据结构：
```yaml
# 玩家UUID作为键
069a79f4-44e9-4726-a5be-fca90e38aaf5:
  # QQ号
  qq: 123456789
  # 签到天数
  sign_in_days: 10
  # 连续签到天数
  keep_sign_in_days: 5
  # 最后签到时间（时间戳）
  last_sign_in_time: 1678234567890
  # 最后解封时间（时间戳）
  last_unban_time: 1678234567890
  # 今日解封次数
  unban_today: 1
```

存储的数据包括：
- 玩家绑定的QQ号
- 签到天数统计
- 连续签到天数
- 最后签到时间
- 最后解封时间
- 每日解封次数