for i, v in ipairs(KEYS) do
    redis.log(redis.LOG_WARNING, v, ARGV[1])
    redis.call('ZREMRANGEBYSCORE', v, 0, ARGV[1])
end
return true