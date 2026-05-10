package org.asura.modulith.structure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserDTO {
    private Long userId;
    private String userName;
}
