-- KEYS: [1]channel_id, [2]user_id
-- ARGV: [1]dnum, [2]device_id, [3]time_diff, [4]client_timestamp, [5]user_id

local tvc = "tvc:" .. KEYS[2]
local ctv = "ctv:" .. KEYS[1]
if redis.call("EXISTS", tvc) == 1 then
local tvcvalue = redis.call("get", tvc)
if tvcvalue == KEYS[1] then
return "not change"
end
redis.call("HDEL", "ctv:" .. tvcvalue, KEYS[2])
redis.call("SET", tvc, KEYS[1])
redis.call("HSET", ctv, KEYS[2], ARGV[1])
return ctv
else
redis.call("SET", tvc, KEYS[1])
redis.call("HSET", ctv, KEYS[2], ARGV[1])
return ctv
end