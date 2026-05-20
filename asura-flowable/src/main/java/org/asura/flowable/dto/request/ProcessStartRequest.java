package org.asura.flowable.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ProcessStartRequest {

    private String processDefinitionKey;
    private String businessKey;
    private Map<String, Object> variables;
    private String initiator;

    public ProcessStartRequest() {
    }

}