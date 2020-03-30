package com.sevnis.reactive.parallelism.parallelism.repository;

import com.sevnis.reactive.parallelism.parallelism.repository.dto.FirstDto;
import com.sevnis.reactive.parallelism.parallelism.repository.dto.SecondDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
public class MonoRepository {

  public Mono<FirstDto> getFirstDto() {
    return Mono.fromCallable(() -> {
      for (int i = 0; i < 5; i++) {
        System.out.println("this is firstdDto");
        Thread.sleep(1000);
      }
      return FirstDto.builder().id("10").build();
    }).subscribeOn(Schedulers.parallel());
  }

  public Mono<SecondDto> getSecondDto() {
    return Mono.fromCallable(() -> {
      for (int i = 0; i < 5; i++) {
        System.out.println("this is secondDto");
        Thread.sleep(1000);
      }
      return SecondDto.builder().id("20").build();
    }).subscribeOn(Schedulers.parallel());
  }

  public Mono getErrorDto() {
    return Mono.error(() -> new RuntimeException("error"))
        .subscribeOn(Schedulers.parallel())
        .onErrorResume(e -> Mono.just("error"));
  }
}
