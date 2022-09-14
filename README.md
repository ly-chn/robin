## Robin 爬虫的绊脚石

Robin 是一个简易的 Java 反爬框架, 目标是填补 Java 没有一个好用的反爬库的空白, 为中小型公司的反爬工作的提供良好的解决方案.

## Links

- [Documentation](https://ly-chn.github.io/robin/)

todo:

- IP白名单
- 大请求校验（频繁请求长时间无响应的数据接口）
- springboot-starter迁移到robin-starter目录, 抽离redis-starter，缓存使用hashmap实现一个
- 同理samples也抽离出来
- 不建议通过副作用实现其他功能，如用户连续上线时间等
- 注解实现
