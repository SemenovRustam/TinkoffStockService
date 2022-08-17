package com.semenov.tinkoff.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Stock  {
   private String ticker;
   private String figi;
   private String name;
   private String type;
   private Currency currency;
   private String source;
   private String isin;
   private BigDecimal price;
}
