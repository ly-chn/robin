-- 返回值: boolean, 为true表示校验通过, 成功获取令牌
-- 主题
local topic = KEYS[1]
-- 元数据
local metadata = ARGV[1]
-- 当前时间窗口
local currentTimeFrame = tonumber(ARGV[2])
-- 桶容量
local capacity = tonumber(ARGV[3])
-- 速率, 每次下发的数量
local tokenCount = tonumber(ARGV[4])
-- 精度
local bucketPrecision = tonumber(ARGV[5])
-- 最小时间窗口, 小于此窗口表示数据无效
local minTimeframe = currentTimeFrame - math.ceil(capacity / tokenCount)
local logEnabled = ARGV[6] == 'true'
if logEnabled then
    redis.log(redis.LOG_WARNING, 'sustain-visit.lua started, args: ', KEYS[1], ARGV[1], ARGV[2], ARGV[3], ARGV[4], ARGV[5])
end
local bucketInfo = tonumber(redis.call('ZSCORE', topic, metadata))
if not bucketInfo or bucketInfo <= minTimeframe then
    redis.call('ZADD', topic, currentTimeFrame + (tokenCount - 1) / bucketPrecision, metadata)
    if logEnabled then
        redis.log(redis.LOG_WARNING, 'topic: ', topic, 'metadata: ', metadata, 'rest token: ', tokenCount - 1)
    end
    return true
end
local restToken = math.min(capacity, math.ceil(currentTimeFrame - bucketInfo) * tokenCount + math.floor(bucketInfo % 1 * bucketPrecision)) - 1
if restToken < 0 then
    return false
end
redis.call('ZADD', topic, restToken, metadata)
return true