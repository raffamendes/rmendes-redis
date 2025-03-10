package com.rmendes.sb_redis_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration(exclude = RedisReactiveAutoConfiguration.class)
public class SbRedisTestApplication implements CommandLineRunner {

	@Autowired
	@Qualifier("sourceRedisTemplate")
	private StringRedisTemplate sourceRedisTemplate;

	@Autowired
	@Qualifier("replicaRedisTemplate")
	private StringRedisTemplate replicaRedisTemplate;

//	public SbRedisTestApplication(@Qualifier("sourceRedisTemplate") StringRedisTemplate sourceRedisTemplate,
//			@Qualifier("replicaRedisTemplate") StringRedisTemplate replicaRedisTemplate) {
//		this.sourceRedisTemplate = sourceRedisTemplate;
//		this.replicaRedisTemplate = replicaRedisTemplate;
//	}

	public static void main(String[] args) {
		SpringApplication.run(SbRedisTestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 1; i <= 100; i++) {
			sourceRedisTemplate.opsForValue().set("key_" + i, String.valueOf(i));
		}
		System.out.println("Data inserted into source-db.");

		// Read and print data from replica-db in reverse order
		System.out.println("Reading data from replica-db in reverse order:");
		for (int i = 100; i >= 1; i--) {
			String value = replicaRedisTemplate.opsForValue().get("key_" + i);
			if (value != null) {
				System.out.println("key_" + i + ": " + value);
			} else {
				System.out.println("key_" + i + ": Not found");
			}
		}

	}

}
