-- 返回值: boolean, 为true表示校验通过
local key = KEYS[1]
local value = ARGV[1]
local logEnabled = ARGV[5] == 'true'
if logEnabled then
    redis.log(redis.LOG_WARNING, 'sustain-visit.lua started', KEYS[1], ARGV[1], ARGV[2], ARGV[3], ARGV[4], ARGV[5], KEYS, ARGV)
end
-- 持续访问记录最大记录数量
local sustainVisitPrecision = tonumber(ARGV[4])
--持续访问记录步长
local sustainVisitStep = 1 / sustainVisitPrecision
local currentTimeFrame = tonumber(ARGV[2])

local latestVisit = tonumber(redis.call('ZSCORE', key, value))
-- 非连续访问
if not latestVisit or currentTimeFrame - latestVisit > 1 then
    redis.call('ZADD', key, currentTimeFrame + sustainVisitStep, value)
    if logEnabled then
        redis.log(redis.LOG_WARNING, 'latestVisit of metadata', key, ' is zero, set to', currentTimeFrame, sustainVisitStep)
    end
    return true
end
-- 不同窗口
if currentTimeFrame > latestVisit then
    latestVisit = latestVisit + 1 + sustainVisitStep

end
-- 最大可访问次数
local maxTimes = tonumber(ARGV[3])

local visitedTimes = latestVisit % 1 * sustainVisitPrecision
-- 达到访问限制
if visitedTimes > maxTimes then
    return false
end

redis.call("ZADD", key, latestVisit, value)
if logEnabled then
    redis.log(redis.LOG_WARNING, 'latestVisit of metadata', key, ' is update to, hasVisitedTimes', latestVisit)
end
return true
