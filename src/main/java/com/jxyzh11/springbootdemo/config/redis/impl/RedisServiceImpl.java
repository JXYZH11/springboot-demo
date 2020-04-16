package com.jxyzh11.springbootdemo.config.redis.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jxyzh11.springbootdemo.common.utils.DataUtil;
import com.jxyzh11.springbootdemo.config.redis.RedisService;
import com.jxyzh11.springbootdemo.config.redis.entity.Page;
import com.jxyzh11.springbootdemo.config.redis.entity.RedisObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author sunny
 * @date
 */
@Component(value = RedisService.NAME)
public class RedisServiceImpl implements RedisService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    class ValueScoreTuple implements RedisZSetCommands.Tuple {
        private byte[] value;
        private Double score;

        ValueScoreTuple(byte[] value, Double score) {
            this.value = value;
            this.score = score;
        }

        @Override
        public byte[] getValue() {
            return value;
        }

        @Override
        public Double getScore() {
            return score;
        }

        @Override
        public int compareTo(Double o) {
            return score.compareTo(o);
        }
    }

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;
    private RedisSerializer defaultSerializer = new JdkSerializationRedisSerializer();

    /*
    @PostConstruct
    public void init() {
        redisTemplate.setEnableTransactionSupport(true);
    }
    */

    @Override
    public List<String> scan(String pattern, long count) {
        List<String> binaryKeys = new ArrayList<>();
        if (pattern.contains(":*") || pattern.contains("_*")) {
            this.redisTemplate.execute((RedisConnection connection) -> {
                Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(pattern).count(count).build());
                while (cursor.hasNext()) {
                    binaryKeys.add(new String(cursor.next()));
                }
                return binaryKeys;
            });
        } else {
            binaryKeys.add(pattern);
        }
        return binaryKeys;
    }

    @Override
    public Long incrBy(String key, final long value) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        return redisTemplate.execute((RedisConnection connection) -> connection.incrBy(keyBytes, value));
    }

    @Override
    public <T> T getObject(final String key, final Class<T> clz) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return redisTemplate.execute((RedisConnection connection) -> {
            byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
            if (connection.exists(keyBytes)) {
                byte[] valueBytes = connection.get(keyBytes);
                Object object = unserialize(valueBytes);
                if (clz.isAssignableFrom(object.getClass())) {
                    return (T) object;
                }
            }
            return null;
        });
    }

    @Override
    public String getString(final String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] keyBytes = serializer.serialize(key);

            byte[] valueBytes = connection.get(keyBytes);

            if (valueBytes != null) {
                return serializer.deserialize(valueBytes);
            }
            return null;
        });
    }


    @Override
    public <T> T getObjectS2(final String key, final Class<T> clz) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        final RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        final byte[] keyBytes = serializer.serialize(key);
        return redisTemplate.execute((RedisConnection connection) -> {
            if (connection.exists(keyBytes)) {
                byte[] valueBytes = connection.get(keyBytes);
                return JSON.parseObject(valueBytes, clz);
            }
            return null;
        });
    }

    @Override
    public void setObject(final String key, final Serializable value) {
        redisTemplate.execute((RedisConnection connection) -> {
            byte[] valueBytes;
            if (value instanceof String) {
                valueBytes = redisTemplate.getStringSerializer().serialize((String) value);
            } else {
                valueBytes = serialize(value);
            }
            connection.set(redisTemplate.getStringSerializer().serialize(key), valueBytes);
            return null;
        });
    }

    @Override
    public void batchSetObjectS2(final Map<String, ? extends Serializable> map, final int expireSeconds) {
        redisTemplate.executePipelined((RedisConnection connection) -> {
            for (Map.Entry<String, ? extends Serializable> entry : map.entrySet()) {
                final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(entry.getKey());
                final byte[] valueBytes = JSON.toJSONBytes(entry.getValue());
                connection.setEx(keyBytes, expireSeconds, valueBytes);
            }
            return null;
        });
    }

    @Override
    public void setObject(String key, Serializable value, final int expireSeconds) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final byte[] valueBytes = serialize(value);
        redisTemplate.execute((RedisConnection connection) -> {
            connection.setEx(keyBytes, expireSeconds, valueBytes);
            return null;
        });
    }

    @Override
    public void setObject(String key, String value, final long expireSeconds) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        final byte[] keyBytes = serializer.serialize(key);
        final byte[] valueBytes = serializer.serialize(value);
        redisTemplate.execute((RedisConnection connection) -> {
            connection.setEx(keyBytes, expireSeconds, valueBytes);
            return null;
        });
    }

    @Override
    public void setObjectS2(String key, Object value) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        final byte[] keyBytes = serializer.serialize(key);
        final byte[] valueBytes = JSON.toJSONBytes(value);
        redisTemplate.execute((RedisConnection connection) -> {
            connection.set(keyBytes, valueBytes);
            return null;
        });
    }

    @Override
    public Boolean setNX(String key, Object value) {
        final byte[] keyBytes = JSON.toJSONBytes(key);
        final byte[] valueBytes = JSON.toJSONBytes(value);
        return redisTemplate.execute((RedisConnection connection) -> connection.setNX(keyBytes, valueBytes));
    }

    @Override
    public Boolean setBit(String key, long offset) {
        byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        return redisTemplate.execute((RedisConnection connection) -> connection.setBit(keyBytes, offset, true));
    }

    @Override
    public Map<String, Boolean> multiGetBits(List<String> keys, long offset) {
        if (CollectionUtils.isEmpty(keys)) {
            return new HashMap<>();
        }
        final RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        List<Object> pipelined = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (String key : keys) {
                connection.getBit(redisSerializer.serialize(key), offset);
            }
            return null;
        });
        Map<String, Boolean> result = new HashMap<>(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            result.put(keys.get(i), (Boolean) pipelined.get(i));
        }
        return result;
    }

    @Override
    public Boolean getBit(String key, long offset) {
        byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        return redisTemplate.execute((RedisConnection connection) -> connection.getBit(keyBytes, offset));
    }

    @Override
    public Long delObject(final String key) {
        return redisTemplate.execute((RedisConnection connection) -> connection.del(key.getBytes()));
    }

    public Map<String, Long> batchDelObjects(final List<String> keyList) {
        Map<String, Long> result = Maps.newHashMap();
        if (CollectionUtils.isEmpty(keyList)) {
            return result;
        }
        final RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        List<Object> delResult = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (String key : keyList) {
                connection.del(redisSerializer.serialize(key));
            }
            return null;
        });
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            Long value = (Long) delResult.get(i);
            result.put(key, value);
        }
        return result;
    }

    @Override
    public void putObject(String key, Map<String, ?> data, long liveTime) {
        redisTemplate.opsForHash().putAll(key, data);
        if (liveTime > 0) {
            redisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public <T> List<T> getRangeData(final String key, final long start, final long end) {
        List<byte[]> redisList;
        List<T> result = Lists.newArrayList();
        redisList = redisTemplate.execute((RedisConnection connection) -> {
            byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
            return connection.lRange(keyBytes, start, end);
        });
        if (null != redisList) {
            for (byte[] bs : redisList) {
                T t = (T) unserialize(bs);
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public boolean expire(final String key, final long expireTime) {
        return redisTemplate.execute(
                (RedisConnection connection) -> connection.expire(redisTemplate.getStringSerializer().serialize(key), expireTime));
    }

    @Override
    public Map<String, Object> getBatchObjects(final List<String> keyList) {
        Map<String, Object> result = Maps.newHashMap();
        if (CollectionUtils.isEmpty(keyList)) {
            return result;
        }
        List<Object> valueList = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (int i = 0; i < keyList.size(); i++) {
                connection.get(redisTemplate.getStringSerializer().serialize(keyList.get(i)));
            }
            return null;
        }, defaultSerializer);

        for (int i = 0; i < keyList.size(); i++) {
            result.put(keyList.get(i), valueList.get(i));
        }
        return result;
    }

    public <T> Map<String, T> multiGetObjects(List<String> keyList, Class<T> clz) {
        if (CollectionUtils.isEmpty(keyList)) {
            return new HashMap<>();
        }
        final byte[][] keyBytes = new byte[keyList.size()][];
        int index = 0;
        for (String key : keyList) {
            keyBytes[index++] = redisTemplate.getStringSerializer().serialize(key);
        }
        List<byte[]> valueBytes = redisTemplate.execute((RedisConnection connection) -> connection.mGet(keyBytes));
        Map<String, T> result = new HashMap<>(keyList.size());
        Serializer<T> serializer = new Serializer<>(clz);
        for (int i = 0; i < valueBytes.size(); i++) {
            byte[] value = valueBytes.get(i);
            if (value != null) {
                result.put(keyList.get(i), serializer.deserialize(value));
            }
        }
        return result;
    }

    @Override
    public Map<String, String> multiGet(final List<String> keyList) {
        if (CollectionUtils.isEmpty(keyList)) {
            return new HashMap<>();
        }
        List<Object> values = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (String key : keyList) {
                connection.get(redisTemplate.getStringSerializer().serialize(key));
            }
            return null;
        }, redisTemplate.getStringSerializer());
        Map<String, String> result = new HashMap<>(keyList.size());
        for (int i = 0; i < keyList.size(); i++) {
            result.put(keyList.get(i), (String) values.get(i));
        }
        return result;
    }

    @Override
    public <T> Map<String, T> getBatchObjectsS2(final List<String> keyList, Class<T> clz) {
        Map<String, T> result = Maps.newHashMap();
        if (CollectionUtils.isEmpty(keyList)) {
            return result;
        }
        List<Object> valueList = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (int i = 0; i < keyList.size(); i++) {
                connection.get(redisTemplate.getStringSerializer().serialize(keyList.get(i)));
            }
            return null;
        }, new Serializer<>(clz));

        for (int i = 0; i < keyList.size(); i++) {
            result.put(keyList.get(i), (T) valueList.get(i));
        }
        return result;
    }

    @Override
    public Long zRem(final String key, final Object value) {
        return redisTemplate.execute(
                (RedisConnection connection) -> connection.zRem(redisTemplate.getStringSerializer().serialize(key), serialize(value)));
    }

    @Override
    public <T> Long zRem(final String key, final List<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        byte[][] valueBytes = values.stream()
                .map(this::serialize)
                .toArray(byte[][]::new);
        return redisTemplate.execute((RedisConnection connection) ->
                connection.zRem(redisTemplate.getStringSerializer().serialize(key), valueBytes));
    }

    @Override
    public Boolean zAdd(final String key, final Object value, final double score) {
        return redisTemplate.execute(
                (RedisConnection connection) -> connection.zAdd(redisTemplate.getStringSerializer().serialize(key), score, serialize(value)));
    }

    @Override
    public Long zBatchAdd(final String key, final Map<Long, Double> batch) {
        return redisTemplate.execute((RedisConnection connection) -> {
            Set<RedisZSetCommands.Tuple> sets = Sets.newHashSet();
            for (Map.Entry<Long, Double> entry : batch.entrySet()) {
                sets.add(new ValueScoreTuple(
                        serialize(entry.getKey()),
                        entry.getValue()
                ));
            }
            return connection.zAdd(redisTemplate.getStringSerializer().serialize(key), sets);
        });
    }

    @Override
    public Double zScore(final String key, final Object value) {
        return redisTemplate.execute(
                (RedisConnection connection) -> connection.zScore(redisTemplate.getStringSerializer().serialize(key), serialize(value)));
    }

    @Override
    public Map<Long, Double> batchZScore(final String key, final List<Long> valueList) {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(valueList)) {
            return new HashMap<>();
        }
        final RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        List<Object> scoreList = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (Long value : valueList) {
                connection.zScore(serializer.serialize(key), serialize(value));
            }
            return null;
        });
        Map<Long, Double> result = new HashMap<>(valueList.size());
        for (int i = 0; i < valueList.size(); i++) {
            Object score = scoreList.get(i);
            if (!(score instanceof Double)) {
                continue;
            }
            result.put(valueList.get(i), (Double) score);
        }
        return result;
    }


    @Override
    public Double zIncrby(final String key, final Object value, final double incrScore) {

        return redisTemplate.execute(
                (RedisConnection connection) -> connection.zIncrBy(redisTemplate.getStringSerializer().serialize(key), incrScore, serialize(value))
        );
    }

    @Override
    public List<RedisObject> zRevRangeWithScores(final String key, final long start, final long end) {
        return redisTemplate.execute((RedisConnection connection) -> {
            Set<RedisZSetCommands.Tuple> tupleSet = connection.zRevRangeWithScores(redisTemplate.getStringSerializer().serialize(key), start, end);
            List<RedisObject> keyValues = new ArrayList<>(tupleSet.size());
            for (RedisZSetCommands.Tuple tuple : tupleSet) {
                keyValues.add(new RedisObject(unserialize(tuple.getValue()), tuple.getScore()));
            }
            return keyValues;
        });
    }

    @Override
    public List<Long> zRange(final String key, final long start, final long end) {
        return redisTemplate.execute((RedisConnection connection) -> {
                    Set<byte[]> bytes = connection.zRange(redisTemplate.getStringSerializer().serialize(key), start, end);
                    return bytes.stream()
                            .filter(Objects::nonNull)
                            .map(data -> (Long) unserialize(data))
                            .collect(Collectors.toList());
                }

        );
    }

    @Override
    public Long zRemRangeByRank(final String key, final long start, final long end) {
        return redisTemplate.execute(
                (RedisConnection connection) -> connection.zRemRange(redisTemplate.getStringSerializer().serialize(key), start, end));
    }

    @Override
    public Long zRemRangeByScore(final String key, final double min, final double max) {
        return redisTemplate.execute(
                (RedisConnection connection) -> connection.zRemRangeByScore(redisTemplate.getStringSerializer().serialize(key), min, max));
    }

    @Override
    public List<RedisObject> zRangeByScoreWithScores(final String key, final int min, final int max, final int offset, final int limit) {
        return redisTemplate.execute((RedisConnection connection) -> {
            List<RedisObject> keyValues = Lists.newArrayList();
            Set<RedisZSetCommands.Tuple> tupleSet = connection.zRangeByScoreWithScores(redisTemplate.getStringSerializer().serialize(key), max, min, offset, limit);
            for (RedisZSetCommands.Tuple tuple : tupleSet) {
                keyValues.add(new RedisObject(unserialize(tuple.getValue()), tuple.getScore()));
            }
            return keyValues;
        });

    }

    @Override
    public Long zRevRank(final String key, final Object value) {
        return redisTemplate.execute(
                (RedisConnection connection) -> connection.zRevRank(redisTemplate.getStringSerializer().serialize(key), serialize(value)));
    }

    @Override
    public Long zCard(final String key) {
        return redisTemplate.execute((RedisConnection connection) -> connection.zCard(redisTemplate.getStringSerializer().serialize(key)));
    }

    @Override
    public Map<String, Long> batchZCard(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return new HashMap<>();
        }
        List<Object> pipeResult = redisTemplate.executePipelined((RedisConnection connection) -> {
            keys.forEach(key -> connection.zCard(redisTemplate.getStringSerializer().serialize(key)));
            return null;
        });
        Map<String, Long> result = new HashMap<>(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            result.put(keys.get(i), (Long) pipeResult.get(i));
        }
        return result;
    }

    @Override
    public List<RedisObject> zRevRange(final String key, final long start, final long end) {
        return redisTemplate.execute((RedisConnection connection) -> {
            Set<byte[]> resultSet = connection.zRevRange(redisTemplate.getStringSerializer().serialize(key), start, end);
            if (CollectionUtils.isEmpty(resultSet)) {
                return new ArrayList<>();
            }
            List<RedisObject> keyValues = new ArrayList<>(resultSet.size());
            for (byte[] result : resultSet) {
                keyValues.add(new RedisObject(unserialize(result), null));
            }
            return keyValues;
        });
    }

    @Override
    public Map<String, List<RedisObject>> batchZRevRange(List<String> keys, long start, long end) {
        if (CollectionUtils.isEmpty(keys)) {
            return new HashMap<>();
        }
        List<Object> pipeList = redisTemplate.executePipelined((RedisConnection connection) -> {
            keys.forEach(key -> connection.zRevRange(redisTemplate.getStringSerializer().serialize(key), start, end));
            return null;
        });
//        return pipeList.stream()
//                .collect(Collectors.toMap(result -> keys.get(pipeList.indexOf(result)),
//                        result -> {
//                            Set<byte[]> resultSet = (Set<byte[]>) result;
//                            if (CollectionUtils.isEmpty(resultSet)) {
//                                return new ArrayList<>();
//                            }
//                            return resultSet.stream()
//                                    .map(data -> new RedisObject(unserialize(data), null))
//                                    .collect(Collectors.toList());
//                        }
//                ));
        Map<String, List<RedisObject>> resultMap = new HashMap<>();
        for (int i = 0; i < pipeList.size(); i++) {
            Set<String> resultSet = (Set<String>) pipeList.get(i);
            List<RedisObject> keyValues = new ArrayList<>(resultSet.size());
            for (String result : resultSet) {
                keyValues.add(new RedisObject(unserialize(result.getBytes()), null));
            }
            resultMap.put(keys.get(i), keyValues);
        }
        return resultMap;
    }


    @Override
    public void transMulti() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.multi();
            return null;
        });
    }

    @Override
    public List<Object> transExec() {
        return redisTemplate.execute(RedisConnection::exec);
    }

    @Override
    public Long sAdd(final String key, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0L;
        }
        byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        byte[][] valueBytes = values.stream()
                .map(value -> redisTemplate.getStringSerializer().serialize(value))
                .toArray(byte[][]::new);
        return redisTemplate.execute((RedisConnection connection) -> connection.sAdd(keyBytes, valueBytes));
    }

    @Override
    public Long sAdd(final String key, final String value) {
        return sAdd(key, Lists.newArrayList(value));
    }

    @Override
    public Boolean sIsMember(String key, String value) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final byte[] valueBytes = redisTemplate.getStringSerializer().serialize(value);
        return redisTemplate.execute((RedisConnection connection) -> connection.sIsMember(keyBytes, valueBytes));
    }

    @Override
    public Set<String> sMembers(String key) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        Set<byte[]> byteRet = redisTemplate.execute((RedisConnection connection) -> connection.sMembers(keyBytes));
        Set<String> result = Sets.newHashSet();
        for (byte[] bytes : byteRet) {
            result.add(redisTemplate.getStringSerializer().deserialize(bytes));
        }
        return result;
    }

    @Override
    public Long sCard(String key) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        return redisTemplate.execute((RedisConnection connection) -> connection.sCard(keyBytes));
    }

    @Override
    public Long sRem(String key, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0L;
        }
        byte[][] valueBytes = values.stream()
                .map(value -> redisTemplate.getStringSerializer().serialize(value))
                .toArray(byte[][]::new);
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        return redisTemplate.execute((RedisConnection connection) -> connection.sRem(keyBytes, valueBytes));
    }

    @Override
    public Map<String, Long> multiSCard(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return new HashMap<>();
        }
        List<Object> pipeResult = redisTemplate.executePipelined((RedisConnection connection) -> {
            keys.forEach(key -> connection.sCard(redisTemplate.getStringSerializer().serialize(key)));
            return null;
        });
        Map<String, Long> result = new HashMap<>(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            result.put(keys.get(i), (Long) pipeResult.get(i));
        }
        return result;
    }

    @Override
    public List<RedisObject> sUnion(String key1, String key2) {
        final byte[][] keys = new byte[][]{
                redisTemplate.getStringSerializer().serialize(key1),
                redisTemplate.getStringSerializer().serialize(key2)
        };
        return redisTemplate.execute((RedisConnection connection) -> {
            Set<byte[]> resultSet = connection.sUnion(keys);
            if (CollectionUtils.isEmpty(resultSet)) {
                return null;
            }
            List<RedisObject> keyValues = Lists.newArrayList();
            for (byte[] result : resultSet) {
                String ret = redisTemplate.getStringSerializer().deserialize(result);
                keyValues.add(new RedisObject(ret, null));
            }
            return keyValues;
        });
    }

    @Override
    public Boolean hSet(String key, String field, String value) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final byte[] fieldBytes = redisTemplate.getStringSerializer().serialize(field);
        final byte[] valueBytes = redisTemplate.getStringSerializer().serialize(value);
        return redisTemplate.execute((RedisConnection connection) -> connection.hSet(keyBytes, fieldBytes, valueBytes));
    }

    @Override
    public void hMSet(String key, Map<String, String> map) {
        if (MapUtils.isEmpty(map)) {
            return;
        }
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final Map<byte[], byte[]> byteMap = Maps.newHashMap();
        for (String field : map.keySet()) {
            String value = map.get(field);
            byteMap.put(redisTemplate.getStringSerializer().serialize(field), redisTemplate.getStringSerializer().serialize(value));
        }
        redisTemplate.execute((RedisConnection connection) -> {
            connection.hMSet(keyBytes, byteMap);
            return null;
        });
    }

    @Override
    public List<String> hKeys(String key) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        Set<byte[]> fieldSet = redisTemplate.execute((RedisConnection connection) -> connection.hKeys(keyBytes));
        if (CollectionUtils.isEmpty(fieldSet)) {
            return null;
        }
        List<String> keyList = Lists.newArrayList();
        for (byte[] res : fieldSet) {
            keyList.add(redisTemplate.getStringSerializer().deserialize(res));
        }
        return keyList;
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        Map<byte[], byte[]> fieldValues = redisTemplate.execute((RedisConnection connection) -> connection.hGetAll(keyBytes));
        if (MapUtils.isEmpty(fieldValues)) {
            return null;
        }
        Map<String, String> retMap = Maps.newHashMap();
        for (Map.Entry<byte[], byte[]> entry : fieldValues.entrySet()) {
            retMap.put(redisTemplate.getStringSerializer().deserialize(entry.getKey()), redisTemplate.getStringSerializer().deserialize(entry.getValue()));
        }
        return retMap;
    }

    @Override
    public Long hIncryBy(String key, String field, final long increment) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final byte[] fieldBytes = redisTemplate.getStringSerializer().serialize(field);
        return redisTemplate.execute((RedisConnection connection) -> connection.hIncrBy(keyBytes, fieldBytes, increment));
    }

    @Override
    public long hIncr(long key, long field) {
        byte[] keyBytes = DataUtil.getLong(key);
        byte[] fieldBytes = DataUtil.getLong(field);
        return redisTemplate.execute((RedisConnection connection) -> connection.hIncrBy(keyBytes, fieldBytes, 1));
    }

    @Override
    public Boolean exists(String key) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        return redisTemplate.execute((RedisConnection connection) -> connection.exists(keyBytes));
    }

    @Override
    public Boolean hExists(String key, String field) {
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final byte[] fieldBytes = redisTemplate.getStringSerializer().serialize(field);
        return redisTemplate.execute((RedisConnection connection) -> connection.hExists(keyBytes, fieldBytes));
    }

    @Override
    public Map<String, Map<String, String>> getBatchHashObjects(final List<String> keyList) {
        Map<String, Map<String, String>> retMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(keyList)) {
            return retMap;
        }
        List<Object> fieldValues = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                connection.hGetAll(redisTemplate.getStringSerializer().serialize(key));
            }
            return null;
        });
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            Object obj = fieldValues.get(i);
            if (!(obj instanceof Map)) {
                retMap.put(key, null);
                continue;
            }
            Map<String, String> byteMap = (Map<String, String>) obj;
            retMap.put(key, byteMap);
        }
        return retMap;
    }

    @Override
    public String ping() {
        return redisTemplate.execute(RedisConnection::ping);
    }

    @Override
    public Properties getServerInfo() {
        return redisTemplate.execute((RedisConnection connection) -> connection.info());
    }

    @Override
    public Properties getServerInfo(final String section) {
        return redisTemplate.execute((RedisConnection connection) -> connection.info(section));
    }

    @Override
    public void hMSet(String key, Map<String, String> map, final Date expireAt) {
        if (expireAt == null) {
            hMSet(key, map);
            return;
        }
        if (MapUtils.isEmpty(map)) {
            return;
        }
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final Map<byte[], byte[]> byteMap = Maps.newHashMap();
        for (String field : map.keySet()) {
            String value = map.get(field);
            byteMap.put(redisTemplate.getStringSerializer().serialize(field), redisTemplate.getStringSerializer().serialize(value));
        }

        final long expireMillis = expireAt.getTime();
        redisTemplate.execute((RedisConnection connection) -> {
            connection.hMSet(keyBytes, byteMap);
            connection.expireAt(keyBytes, expireMillis);
            return null;
        });
    }

    @Override
    public void hMSet(String key, Map<String, String> map, final int expireSeconds) {
        if (expireSeconds <= 0) {
            hMSet(key, map);
            return;
        }
        if (MapUtils.isEmpty(map)) {
            return;
        }
        final byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
        final Map<byte[], byte[]> byteMap = Maps.newHashMap();
        for (String field : map.keySet()) {
            String value = map.get(field);
            byteMap.put(redisTemplate.getStringSerializer().serialize(field), redisTemplate.getStringSerializer().serialize(value));
        }

        redisTemplate.execute((RedisConnection connection) -> {
            connection.hMSet(keyBytes, byteMap);
            connection.expire(keyBytes, expireSeconds);
            return null;
        });
    }

    @Override
    public void lpush(Set<String> keys, String val, final int expireSeconds) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        final byte[] valueByte = redisTemplate.getStringSerializer().serialize(val);
        final List<byte[]> keyBytes = new ArrayList<>(keys.size());
        for (String k : keys) {
            keyBytes.add(redisTemplate.getStringSerializer().serialize(k));
        }
        redisTemplate.executePipelined((RedisConnection connection) -> {
            for (byte[] keyByte : keyBytes) {
                connection.lPush(keyByte, valueByte);
                connection.expire(keyByte, expireSeconds);
            }
            return null;
        });
    }

    @Override
    public List<Serializable> lrange(String key, final long start, final long end) {
        final byte[] ketByte = redisTemplate.getStringSerializer().serialize(key);
        List<byte[]> cache = redisTemplate.execute((RedisConnection connection) -> connection.lRange(ketByte, start, end));
        if (!CollectionUtils.isEmpty(cache)) {
            List<Serializable> ret = new ArrayList<>(cache.size());
            for (byte[] b : cache) {
                ret.add(redisTemplate.getStringSerializer().deserialize(b));
            }
            return ret;
        }
        return null;
    }

    @Override
    public <T> T executeScript(RedisScript<T> script, List<String> keys, Object... args) {
        return redisTemplate.execute(script, keys, args);
    }

    /**
     * 将对象序列化成byte数组
     *
     * @param object 序列化对象
     * @return 字节数组
     */
    private byte[] serialize(Object object) {
        ObjectOutputStream objectOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            logger.error("serialize fail,Object:{}", JSON.toJSONString(object), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * byte数组反序列化成对象
     *
     * @param bytes 字节数组
     * @return 反序列化对象
     */
    private Object unserialize(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
            return ois.readObject();
        } catch (Exception e) {
            logger.error("unserialize fail,bytes[]:{}", JSON.toJSONString(bytes), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 自定义序列化类，用于pipeline
     *
     * @author sunny
     * @date 2018/5/31
     */
    private class Serializer<T> implements RedisSerializer<T> {
        private Class<T> clz;

        Serializer(Class<T> clz) {
            this.clz = clz;
        }

        @Override
        public byte[] serialize(T t) throws SerializationException {
            return JSON.toJSONBytes(t);
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            return JSON.parseObject(bytes, clz);
        }
    }

    @Override
    public Page<RedisObject> zRangeWithScorePage(final String key, final int pageNo, final int pageSize) {
        if (pageSize <= 0) {
            throw new RuntimeException("pageSize不能小于等于0");
        }
        List<Object> list = redisTemplate.executePipelined((RedisConnection connection) -> {
            connection.zRangeWithScores(redisTemplate.getStringSerializer().serialize(key), (pageNo - 1) * pageSize, pageNo * pageSize - 1);
            connection.zCard(redisTemplate.getStringSerializer().serialize(key));
            return null;
        }, defaultSerializer);

        Page<RedisObject> ret = new Page<>(pageNo, pageSize);
        Set<DefaultTypedTuple> pageSet = (Set<DefaultTypedTuple>) list.get(0);
        List<RedisObject> pageList = new ArrayList<>();
        for (DefaultTypedTuple tuple : pageSet) {
            pageList.add(new RedisObject((tuple.getValue()), tuple.getScore()));
        }
        ret.setTotalCount(((Long) list.get(1)).intValue());
        ret.setTotalPage(ret.getTotalCount() == 0 ? 0 : (ret.getTotalCount() - 1) / pageSize + 1);
        ret.setResult(pageList);
        return ret;
    }
}
