package org.asura.tomcat.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Result {
    private String code;
    private String message;
    private Object data;
}
