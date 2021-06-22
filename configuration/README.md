---
sidebarDepth: 3
---

# 通用配置

这里是框加载运行的一些基本配置

## include-rule

类型: `List<RobinRuleEnum>`

默认: `[]`

::: tip
为了方便和自定义的策略区分先后, 如果配置的策略为"策略1, 策略2, 策略3", 则1/2/3的权重分别为"0/100/200", 权重越低则策略的优先级越高

用户自定义的策略可以依照次规律进行配置权重

:::

> 应用校验的规则, 会依照顺序进行检测, 默认不启用任何规则

## resource

> 全局资源拦截器

### mode

类型: `kim.nzxy.robin.enums.RobinModeEnum`

默认: `DEFAULT`

> Robin 资源配置, 优先级高于`resource.include-patterns`和`resource.exclude-patterns`, 共三种: 
>
> ```
> GLOBAL		全部资源应用 Robin 检测
> DISABLED	全部资源禁止 Robin 检测
> DEFAULT		使用其它方式配置, 即resource.include-patterns和resource.exclude-patterns
> ```

### include-patterns

类型: `List<String>`

默认值: `["/**"]`

> 默认拦截全部资源, 一般需要用户自己配置

### exclude-patterns

类型: `List<String>`

默认值: `[]`

> 取消拦截的资源, 优先级高于`resource.include-patterns`, 一般用于验证码等不需要反爬的接口, 你也可以使用通过重写`kim.nzxy.robin.filter.RobinInterceptor`接口来动态拦截

## black-whiteList

> 黑白名单配置, 由于一些检测机制需要公用黑白名单, 所以次框架黑白名单是公用的, 即IP相关的检测会公用 IP 相关的黑白名单
>
> 共有属性: 
>
> #### whitelist
>
> 类型: `List<String>`
>
> 默认: `[]`
>
> > 白名单
>
> #### blacklist
>
> 类型: `List<String>`
>
> 默认: `[]`
>
> > 黑名单, 优先级高于白名单

### ip

> IP相关黑边名单, 属性见共有属性

## 各策略相关配置

### IP 地址黑名单

> 对应规则: BLACKLIST_IP_ADDRESS
>
> 见`black-whiteList.ip.blacklist`
>
>  优先级应高于每个 IP 相关策略, 即第一个配置次策略

### ip-frequent-access

> IP 访问频率限制
>
> 对应规则: FREQUENT_IP_ACCESS
>
> 在 [duration] 时间内, 如果同一 IP 访问次数超过 [frequency] 将会在 [unlock] 时间内拒绝该 IP 访问

#### duration

类型: `Duration`

默认: `1m`, 即1分钟

> 存续时间, 受控期

#### frequency

类型: `int`

默认: `10`

> 可访问次数

#### unlock

类型: `Duration`

默认: `1h`, 即1小时

> 锁定时长

### continuous-visit

> IP 持续访问
>
> 对应规则: CONTINUOUS_VISIT
>
> 如果同一 IP 在连续 [times] 个 [duration] 时间窗口访问, 将会在 [unlock] 时间内拒绝该 IP 访问
>
> 示例: 
>
> 在所有配置为默认值情况下, 如果一个程序在以下区间内皆有访问, 则会被封禁 1 小时: 
>
> ```
> 同一天的: 
> 08:01
> 08:02
> 08:03
> 08:04
> 08:05
> 08:06
> 08:07
> 08:08
> 08:09
> 08:10
> ```

#### duration

类型: `Duration`

默认: `1m`, 即1分钟

> 时间窗口

#### times

类型: `int`

默认: `100`

> 持续访问次数

#### unlock

类型: `Duration`

默认: `1h`, 即1小时

> 锁定时长

