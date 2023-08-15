for i, v in ipairs(KEYS) do
    redis.call('ZREMRANGEBYSCORE', v, 0, ARGV[i])
end
return true