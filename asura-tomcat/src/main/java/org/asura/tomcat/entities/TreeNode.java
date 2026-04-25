package org.asura.tomcat.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TreeNode {
    private String nodeId;
    private String parentId;
}
