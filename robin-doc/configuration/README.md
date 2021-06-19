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



