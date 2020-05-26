package com.sevnis.reactive.parallelism.parallelism.repository;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sevnis.reactive.parallelism.parallelism.repository.dto.AllDtos;
import com.sevnis.reactive.parallelism.parallelism.repository.dto.FirstDto;
import com.sevnis.reactive.parallelism.parallelism.repository.dto.SecondDto;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

@SpringBootTest
@ActiveProfiles("test")
public class ParallelRepositoryTest {

  @Autowired
  private MonoRepository monoRepository;

  @Test
  public void test() {
    Mono<FirstDto> firstDto = monoRepository.getFirstDto();
    Mono<SecondDto> secondDto = monoRepository.getSecondDto();
    Mono error = monoRepository.getErrorDto();

    AllDtos allDtos = Mono.zipDelayError(Arrays.asList(firstDto, secondDto, (Mono<SecondDto>) error),
        this::combinator).block();

    assertAll("AllDtos",
        () -> assertThat(allDtos, notNullValue()),
        () -> assertThat(allDtos.getFirstDto(), notNullValue()),
        () -> assertThat(allDtos.getFirstDto().getId(), is("10")),
        () -> assertThat(allDtos.getSecondDto(), notNullValue()),
        () -> assertThat(allDtos.getSecondDto().getId(), is("20"))
    );
  }

  private AllDtos combinator(Object[] dtos) {

    AllDtos allDtos = new AllDtos();
    for (Object dto : dtos) {
      if (dto instanceof String) {
        // ignore, this is the error or something else
      }

      if (dto instanceof FirstDto) {
        allDtos.setFirstDto((FirstDto) dto);
      }

      if (dto instanceof SecondDto) {
        allDtos.setSecondDto((SecondDto) dto);
      }
    }

    return allDtos;
  }
}
