package com.rmendes.sb_redis_test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class SbRedisTestApplicationTests {

	@Autowired
    @Qualifier("sourceRedisTemplate")
    private StringRedisTemplate sourceRedisTemplate;

    @Autowired
    @Qualifier("replicaRedisTemplate")
    private StringRedisTemplate replicaRedisTemplate;

    @Test
    public void testDataInsertionAndReplication() {
        // Clear any existing data
        sourceRedisTemplate.keys("key_*").forEach(sourceRedisTemplate::delete);
        replicaRedisTemplate.keys("key_*").forEach(replicaRedisTemplate::delete);

        // Insert data into source-db
        for (int i = 1; i <= 10; i++) { // Reduced to 10 for faster testing
            sourceRedisTemplate.opsForValue().set("key_" + i, String.valueOf(i));
        }

        // Verify data in replica-db
        for (int i = 1; i <= 10; i++) {
            String expectedValue = String.valueOf(i);
            String actualValue = replicaRedisTemplate.opsForValue().get("key_" + i);
            assertEquals(expectedValue, actualValue, "Value mismatch for key_ " + i);
        }
    }

    @Test
    public void testReverseOrderRead() {
        // Clear any existing data
        sourceRedisTemplate.keys("key_*").forEach(sourceRedisTemplate::delete);
        replicaRedisTemplate.keys("key_*").forEach(replicaRedisTemplate::delete);

        // Insert data into source-db
        for (int i = 1; i <= 5; i++) {
            sourceRedisTemplate.opsForValue().set("key_" + i, String.valueOf(i));
        }

        // Verify reverse order read from replica-db
        for (int i = 5; i >= 1; i--) {
            String expectedValue = String.valueOf(i);
            String actualValue = replicaRedisTemplate.opsForValue().get("key_" + i);
            assertEquals(expectedValue, actualValue, "Reverse order value mismatch for key_" + i);
        }
    }

    @Test
    public void testKeyNotFound() {
        // Clear any existing data
        sourceRedisTemplate.keys("key_*").forEach(sourceRedisTemplate::delete);
        replicaRedisTemplate.keys("key_*").forEach(replicaRedisTemplate::delete);
        //insert one value.
        sourceRedisTemplate.opsForValue().set("key_1", "1");
        //check if key_2 is null.
        assertNull(replicaRedisTemplate.opsForValue().get("key_2"));
    }

}
