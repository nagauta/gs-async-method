package com.example.asyncmethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
public class AppRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

	private final GitHubLookupService gitHubLookupService;

	@Autowired
	private  HeavyService heavyService;

	private final RestTemplate restTemplate;

	public AppRunner(GitHubLookupService gitHubLookupService, RestTemplateBuilder restTemplateBuilder) {

		this.gitHubLookupService = gitHubLookupService;
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public void run(String... args) throws Exception {
		this.heavyService.run();
	}

	@Async
	public CompletableFuture<HeavyDto> exec(){
		HeavyDto heavyDto = new HeavyDto();
		logger.info("getting cat for " + heavyDto.getUid().toString());
		String url = "https://cataas.com/cat/cute";
		try {
			byte[] icon = restTemplate.getForObject(url, byte[].class);
			heavyDto.setImgBase64(Base64.getEncoder().encodeToString(icon));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return CompletableFuture.completedFuture(heavyDto);
	}

}
