package com.jxyzh11.springbootdemo.config.redis;

import com.jxyzh11.springbootdemo.config.redis.entity.Page;
import com.jxyzh11.springbootdemo.config.redis.entity.RedisObject;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.Serializable;
import java.util.*;

/**
 * @author song.mao
 * @date 2018/03/08
 */
public interface RedisService {
    String NAME = "redisService";

    /**
     * 正则批量获取key
     *
     * @param pattern
     * @return
     */
    List<String> scan(String pattern, long count);

    /**
     * 将key中储存的数字加上指定的增量值。
     * 如果key不存在，那么key的值会先被初始化为0，然后再执行INCRBY命令。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     *
     * @param key   键
     * @param value 增量数值
     * @return 加上增量之后的key的值
     * @author sunny
     * @date 2018/6/8
     */
    Long incrBy(String key, final long value);

    /**
     * 根据key获取对象
     *
     * @param key 键
     * @param clz 反序列化对象的class对象
     * @param <T> 反序列化对象
     * @return key对应的对象
     */
    <T> T getObject(String key, Class<T> clz);

    /**
     * 根据key获取String
     *
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * 根据key获取对象，采用fastjson反序列化
     *
     * @param key 键
     * @param clz 反序列化对象的class对象
     * @param <T> 反序列化对象
     * @return 值
     */
    <T> T getObjectS2(final String key, final Class<T> clz);

    /**
     * 向redis中添加对象
     *
     * @param key   键
     * @param value 值
     */
    void setObject(String key, Serializable value);

    /**
     * 向redis中添加对象
     *
     * @param key           键
     * @param value         值
     * @param expireSeconds 过期时间
     */
    void setObject(String key, Serializable value, int expireSeconds);

    void batchSetObjectS2(final Map<String, ? extends Serializable> map, final int expireSeconds);

    /**
     * 向redis中添加对象
     *
     * @param key           键
     * @param value         值
     * @param expireSeconds 过期时间
     */
    void setObject(String key, String value, long expireSeconds);

    /**
     * 向redis中添加对象
     * 使用fastjson序列化
     *
     * @param key   键
     * @param value 值
     */
    void setObjectS2(String key, Object value);

    /**
     * 向redis中添加对象，使用fastjson序列化.
     * 将key的值设为value，当且仅当key不存在。
     * 若给定的key已经存在，则不做任何动作.
     *
     * @param key   键
     * @param value 值
     * @return 设置成功，返回true，反之false
     */
    Boolean setNX(String key, Object value);

    /**
     * 向redis中指定偏移量写入bit
     *
     * @param key    键
     * @param offset 偏移量
     * @return 原来的bit
     * @author sunny
     * @date 2019/5/29
     */
    Boolean setBit(String key, long offset);

    /**
     * 根据key和偏移量查询bit
     *
     * @param key    键
     * @param offset 偏移量
     * @author sunny
     * @date 2019/5/29
     */
    Boolean getBit(String key, long offset);

    Map<String, Boolean> multiGetBits(List<String> keys, long offset);

    /**
     * 删除指定key的对象
     *
     * @param key 键
     * @return 被删除key的数量
     */
    Long delObject(String key);

    /**
     * 删除指定key的对象
     *
     * @param keyList 键
     * @return 被删除key的数量
     */
    Map<String, Long> batchDelObjects(List<String> keyList);

    /**
     * 添加map对象
     *
     * @param key      键
     * @param data     值
     * @param liveTime 过期时间
     */
    void putObject(String key, Map<String, ?> data, long liveTime);

    /**
     * 获取redis分页数据
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @param <T>   反序列化对象
     * @return 指定范围集合
     */
    <T> List<T> getRangeData(String key, long start, long end);

    /**
     * 给对象设置过期时间
     *
     * @param key        键
     * @param expireTime 过期时间，单位秒数
     * @return true表示设置成功
     */
    boolean expire(final String key, final long expireTime);

    /**
     * 批量获取redis的数据
     *
     * @param keyList 键集合
     * @return key-value map
     */
    Map<String, Object> getBatchObjects(List<String> keyList);

    <T> Map<String, T> multiGetObjects(List<String> keyList, Class<T> clz);

    Map<String, String> multiGet(List<String> keyList);

    /**
     * 批量获取redis的数据
     *
     * @param keyList 键集合
     * @param clz     T的Class对象
     * @return key-value map
     */
    <T> Map<String, T> getBatchObjectsS2(List<String> keyList, Class<T> clz);

    /**
     * 添加元素到sorted set
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 添加成功
     */
    Boolean zAdd(final String key, final Object value, final double score);

    Long zBatchAdd(final String key, Map<Long, Double> batch);

    /**
     * 删除set 指定元素
     *
     * @param key    键
     * @param values 元素
     * @return 删除的个数
     * @author sunny
     * @date 2019/3/12
     */
    <T> Long zRem(final String key, final List<T> values);

    /**
     * 获取指定元素的分数值
     *
     * @param key   键
     * @param value 值
     * @return score值
     */
    Double zScore(final String key, final Object value);

    /**
     * 批量获取sorted set中的score值
     *
     * @param key       键
     * @param valueList value集合
     * @return 返回当前key的value-score map
     * @author sunny
     * @date 2018/5/25
     */
    Map<Long, Double> batchZScore(String key, List<Long> valueList);

    /**
     * 删除sorted set的key对应的value
     *
     * @param key   键
     * @param value 值
     * @return 成功移除的元素数量
     */
    Long zRem(final String key, final Object value);

    /**
     * 删除sorted set中score在min和max之间的value
     *
     * @param key 键
     * @param min 最小score
     * @param max 最大score
     * @return 被成功移除的元素数量
     */
    Long zRemRangeByScore(final String key, final double min, final double max);

    /**
     * 删除start和end之间的数据
     *
     * @param key   键
     * @param start 起始位置，从0开始
     * @param end   结束位置
     * @return 被移除的元素数量
     */
    Long zRemRangeByRank(final String key, final long start, final long end);

    /**
     * sorted set指定元素增加分数
     *
     * @param key       键
     * @param value     值
     * @param incrScore 每次增加的分数
     * @return 元素的新分数
     */
    Double zIncrby(final String key, final Object value, final double incrScore);

    /**
     * 按照score从大到小排序后，获取某一段索引区间的值
     * start,end=-1,取最小值，从-2起依次从小到大取值
     *
     * @param key   集合的key
     * @param start 查询起始位置(包含在内)
     * @param end   查询结束位置(包含在内)
     * @return 起始位置内的元素集合
     */
    List<RedisObject> zRevRangeWithScores(String key, long start, long end);

    List<Long> zRange(final String key, final long start, final long end);

    /**
     * 从小到大分页查询（含分数范围）
     *
     * @param key    the key
     * @param min    the min
     * @param max    the max
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    List<RedisObject> zRangeByScoreWithScores(String key, int min, int max, int offset, int limit);

    /**
     * 从大到小元素按照score排名，取当前value在排序中的索引值，首位索引为0
     *
     * @param key   集合键
     * @param value 集合中元素的值
     * @return 当前key-value值的排名位置
     */
    Long zRevRank(final String key, final Object value);

    /**
     * 计算集合中元素的数量
     *
     * @param key 集合的键
     * @return 元素数量
     */
    Long zCard(String key);

    /**
     * 批量计算集合中元素的数量
     *
     * @param keys 集合的键
     * @return 元素数量
     */
    Map<String, Long> batchZCard(List<String> keys);

    /**
     * 根据score从大到小排序，取start到end之间的值，包含首尾
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 结果集
     */
    List<RedisObject> zRevRange(String key, long start, long end);

    /**
     * 批量获取多个zset的某一区间values
     *
     * @param keys  键列表
     * @param start 起始位置
     * @param end   结束位置
     * @author sunny
     * @date 2019/3/12
     */
    Map<String, List<RedisObject>> batchZRevRange(List<String> keys, long start, long end);

    /**
     * redis开启事务
     * 需设置redisTemplate.setEnableTransactionSupport(true);
     * 事务开启后，提交前，中间只能写操作，查询操作报空指针
     * 配合spring的@Transactional有效
     */
    void transMulti();

    /**
     * redis事务提交
     */
    List<Object> transExec();

    /**
     * 向redis的set中添加多个元素
     *
     * @param key   键
     * @param value 值
     * @return 集合中的新元素的数量
     */
    Long sAdd(String key, List<String> value);

    /**
     * redis的set中添加元素
     *
     * @param key   键
     * @param value 值
     * @return 集合中的新元素的数量
     */
    Long sAdd(String key, String value);

    /**
     * 判断某个元素是否为集合的元素
     *
     * @param key   key
     * @param value 元素值
     * @return 判断结果
     */
    Boolean sIsMember(String key, String value);

    Set<String> sMembers(String key);

    /**
     * 获取集合的成员数
     *
     * @param key 键
     * @return 集合元素的数量
     */
    Long sCard(String key);

    /**
     * 删除set中的元素
     *
     * @param key    set的key
     * @param values 删除的值
     * @return 删除元素的个数
     * @author sunny
     * @date 2019/3/11
     */
    Long sRem(String key, List<String> values);

    Map<String, Long> multiSCard(List<String> keys);

    /**
     * 获取给定集合的并集
     *
     * @param key1 键
     * @param key2 键
     * @return 并集元素的列表
     */
    List<RedisObject> sUnion(String key1, String key2);

    /**
     * 向hash中设置一组field-value值
     *
     * @param key   键
     * @param field 域
     * @param value 值
     * @return 如果字段是哈希表中的一个新字段，并且值设置成功，返回true。
     * 如果字段已经存在且旧值已被新值覆盖，返回false。
     */
    Boolean hSet(String key, String field, String value);

    /**
     * 向hash表中设置多组field-value值
     *
     * @param key 键
     * @param map 字段-值map
     */
    void hMSet(String key, Map<String, String> map);

    /**
     * 获取hash表中的所有field字段
     *
     * @param key 键
     * @return 获取哈希表中所有域（field）列表
     */
    List<String> hKeys(String key);

    /**
     * 返回hash表里所有的field-value值
     *
     * @param key 键
     * @return 返回哈希表的字段及字段值
     */
    Map<String, String> hGetAll(String key);

    long hIncr(long key, long field);

    /**
     * 向hash表中的指定字段加上指定增量值
     * 增量为负数表示减法，hash表不存在会创建新表并执行hIncryBy指令
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为0
     * 对一个储存字符串值的字段执行HINCRBY命令报错。
     *
     * @param key       键
     * @param field     字段
     * @param increment 增量
     * @return 执行该命令后field的值
     */
    Long hIncryBy(String key, String field, long increment);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return key存在为true，反之为false
     */
    Boolean exists(String key);

    /**
     * 判断hash表中指定字段是否存在
     *
     * @param key   键
     * @param field 字段名
     * @return 含有给定字段为true，反之为false
     */
    Boolean hExists(String key, String field);

    /**
     * 根据keyList批量获取hash表结果
     *
     * @param keyList
     * @return 批量获取hash表数据
     */
    Map<String, Map<String, String>> getBatchHashObjects(List<String> keyList);

    /**
     * 判断redis的连接是否可用
     *
     * @return
     */
    String ping();

    /**
     * 监控redis，获取系统报告
     *
     * @return
     */
    Properties getServerInfo();

    Properties getServerInfo(String section);

    Page<RedisObject> zRangeWithScorePage(String key, int pageNo, int pageSize);

    /**
     * hmset and expire
     *
     * @param key
     * @param map
     * @param expireAt
     */
    void hMSet(String key, Map<String, String> map, Date expireAt);

    /**
     * hmset and expire
     *
     * @param key
     * @param map
     * @param expireSeconds
     */
    void hMSet(String key, Map<String, String> map, int expireSeconds);

    /**
     * @param keys
     * @param val
     * @param expireSeconds
     */
    void lpush(Set<String> keys, String val, int expireSeconds);

    /**
     * lrange
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<Serializable> lrange(String key, long start, long end);

    <T> T executeScript(RedisScript<T> script, List<String> keys, Object... args);
}
