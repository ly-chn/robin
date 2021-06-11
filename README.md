# Robin

[toc]



> 此项目意为知更鸟, 跟罗宾木的关系

## 支持的所有规则: 

- [x] `FREQUENT_IP_ACCESS ` 同一 ip 访问频率控制(后续添加访问频率规律规则)
- [x] `BLACKLIST_IP_ADDRESS ` ip黑名单
- [ ] `QPS` QPS限制
- [ ] ~~`ua`~~ ~~User-Agent验证, 没啥用, 放弃~~
- [ ] `URI-Limit`指定接口限制访问次数, 暂时没想到场景~
- [ ] `CONTINUOUS_VISIT` 持续访问限制(防止随机间隔爬虫进行访问)
- [ ] ...其它需要前端配合的

## todo:

- [ ] 单测(核心包需要依赖defaultCacheManager)
- [ ] 配置中的时间单位, 默认是毫秒, 还没找到一种合理的方式去解决, 目前有两种想到的方案, 自己读取/写个类拷贝过去, 都很不好
- [ ] 日志如何配置?不能像mybatis中支持那么多吧~
