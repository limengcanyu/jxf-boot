package com.spring.boot.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/01/03 15:59
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenEntity {
    private String tenantId;
    private String companyId;
    private String userId;
    private String loginUUID;
}
