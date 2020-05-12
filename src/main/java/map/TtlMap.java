package map;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class TtlMap<K, V> {
    private final int N; // count of bucket
    private final int ttl;
    private volatile int curBucket = -1;
    private volatile int prevBucket = -1;
    private final List<Map<K, V>> maps;
    private final ScheduledExecutorService cleanService;
    public TtlMap(int ttl) {
        this.N = 60 / ttl;
        this.ttl = ttl;
        this.maps = new ArrayList<>(this.N);
        for (int i = 0; i < N; i++) {
            maps.add(i, null);
        }
        cleanService = Executors.newSingleThreadScheduledExecutor();
        cleanService.scheduleAtFixedRate(
                () -> cleanOldData()
                , 0, ttl, TimeUnit.SECONDS);
    }

    public void put(K key, V value) {
        long seconds = System.currentTimeMillis()/1000;
        int bucket = (int) seconds % N;
        if (bucket != curBucket) {
            // move forward the curBucket
            curBucket = bucket;
            prevBucket = getPrev(curBucket);
        }
        if (maps.get(curBucket) == null) {
            maps.add(curBucket, new HashMap<>());
        }
        maps.get(curBucket).put(key, value);
    }

    public V get(K key) {
        if (curBucket == -1 || prevBucket == -1) return null;
        if (maps.get(curBucket) != null && maps.get(curBucket).containsKey(key)) {
            return maps.get(curBucket).get(key);
        }
        if (maps.get(prevBucket) != null && maps.get(prevBucket).containsKey(key)) {
            return maps.get(prevBucket).get(key);
        }
        return null;
    }

    private int getPrev(int i) {
        return i != 0? i - 1: N - 1;
    }

    private void cleanOldData() {
        for (int i = 0; i < N; i++) {
            // bypass the current and prev bucket
            if (i == curBucket || i == prevBucket) continue;
            this.maps.set(i, null);
        }
    }

    public void stop() {
        cleanService.shutdown();
        for (int i = 0; i < N; i++) {
            // help GC
            maps.set(i, null);
        }
    }

//    public static void main(String[] args) throws Exception {
//        TtlMap<String, String> map = new TtlMap<>(5);
//        map.put("key1", "value1");
//        map.put("key2", "value2");
//        System.out.println("0 seconds, get the key1 and key2");
//        System.out.println("key1 --> " + map.get("key1") + "\tkey2--> " + map.get("key2"));
//        Thread.sleep(5000);
//        System.out.println("5 seconds, update key2");
//        map.put("key2", "value2");
//        Thread.sleep(2000);
//        System.out.println("7 seconds, get the key1 and key2");
//        System.out.println("key1 --> " + map.get("key1") + "\tkey2--> " + map.get("key2"));
//        map.put("key3", "value3");
//        Thread.sleep(5000);
//        System.out.println("12 seconds");
//        System.out.println("key1 --> " + map.get("key1") + "\tkey2--> " + map.get("key2"));
//        map.stop();
//    }
public static void main(String[] args) {

    String str1 = "aaa";
    String str2 = "bbb";
    AtomicStampedReference<String> reference = new AtomicStampedReference<String>(str1,1);
    reference.compareAndSet(str1,str2,reference.getStamp(),reference.getStamp()+1);
    System.out.println("reference.getReference() = " + reference.getReference());

    boolean b = reference.attemptStamp(str2, reference.getStamp() + 1);
    System.out.println("b: "+b);
    System.out.println("reference.getStamp() = "+reference.getStamp());

    boolean c = reference.weakCompareAndSet(str2,"ccc",4, reference.getStamp()+1);
    System.out.println("reference.getReference() = "+reference.getReference());
    System.out.println("c = " + c);
}

/**
 *     输出:
 *             reference.getReference() = bbb
 *     b: true
 *             reference.getStamp() = 3
 *             reference.getReference() = bbb
 *             c = false
 *     c为什么输出false呢, 因为版本戳不一致啦
 */
}
