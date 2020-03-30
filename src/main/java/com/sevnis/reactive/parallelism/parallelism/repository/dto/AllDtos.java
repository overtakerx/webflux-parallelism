package com.sevnis.reactive.parallelism.parallelism.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllDtos {

  private FirstDto firstDto;
  private SecondDto secondDto;
}
