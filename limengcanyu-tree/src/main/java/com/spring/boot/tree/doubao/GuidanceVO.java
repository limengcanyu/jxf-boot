package com.spring.boot.tree.doubao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GuidanceVO {
    private Long id;
    private String identity;
    private String name;
    private String subType;
    private String parent;
    private String namePrefix;

    private List<GuidanceVO> childrenList = new ArrayList<>();

    public GuidanceVO() {
    }

    public GuidanceVO(String identity, String name, String namePrefix, String parent, String subType) {
        this.identity = identity;
        this.name = name;
        this.subType = subType;
        this.parent = parent;
        this.namePrefix = namePrefix;
    }

    public GuidanceVO(String identity, String name, String subType, String parent) {
        this.identity = identity;
        this.name = name;
        this.subType = subType;
        this.parent = parent;
        this.namePrefix = namePrefix;
    }

    public boolean isFolder() {
        return "folder".equals(subType);
    }

    public String getCombinedName() {
        return (namePrefix == null ? "" : namePrefix) + (name == null ? "" : name);
    }

    public void setChildrenList(List<GuidanceVO> childrenList) {
        this.childrenList = childrenList != null ? childrenList : new ArrayList<>();
    }

}
