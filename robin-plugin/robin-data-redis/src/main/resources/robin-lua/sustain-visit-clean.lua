for i, v in ipairs(KEYS) do
    redis.call('ZREMRANGEBYSCORE', v, ARGV[i], '+inf')
end
return true