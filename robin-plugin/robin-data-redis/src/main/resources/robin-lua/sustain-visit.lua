-- 返回值: boolean, 为true表示校验通过
-- 主题
local topic = KEYS[1]
-- 元数据
local metadata = ARGV[1]
-- 当前时间窗口
local currentTimeFrame = tonumber(ARGV[2])
-- 持续访问记录最大记录数量
local sustainVisitPrecision = tonumber(ARGV[4])
local logEnabled = ARGV[5] == 'true'
--持续访问记录步长
local sustainVisitStep = 1 / sustainVisitPrecision
if logEnabled then
    redis.log(redis.LOG_WARNING, 'sustain-visit.lua started, args: ', KEYS[1], ARGV[1], ARGV[2], ARGV[3], ARGV[4], ARGV[5])
end

local latestVisit = tonumber(redis.call('ZSCORE', topic, metadata))
-- 非连续访问
if not latestVisit or currentTimeFrame - latestVisit > 1 then
    redis.call('ZADD', topic, currentTimeFrame + sustainVisitStep, metadata)
    if logEnabled then
        redis.log(redis.LOG_WARNING, 'topic: ', topic, 'metadata: ', metadata, ' is zero, set to', currentTimeFrame, sustainVisitStep)
    end
    return true
end
-- 不同窗口
if currentTimeFrame > latestVisit then
    latestVisit = latestVisit + 1 + sustainVisitStep
end
-- 最大可访问次数
local maxTimes = tonumber(ARGV[3])

local visitedTimes = math.floor(latestVisit % 1 * sustainVisitPrecision)
-- 达到访问限制
if visitedTimes > maxTimes then
    if logEnabled then
        redis.log(redis.LOG_WARNING, 'topic: ', topic, 'metadata: ', metadata, 'limited: visitedTimes', visitedTimes, 'maxTimes: ', maxTimes)
    end
    return false
end

redis.call("ZADD", topic, latestVisit, metadata)
if logEnabled then
    redis.log(redis.LOG_WARNING, 'latestVisit of metadata', topic, metadata, ' is update to, hasVisitedTimes', latestVisit)
end
return true
