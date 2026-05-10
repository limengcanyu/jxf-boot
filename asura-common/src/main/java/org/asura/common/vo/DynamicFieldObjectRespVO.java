package org.asura.common.vo;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class DynamicFieldObjectRespVO {

    private String project;

    private String stage;

    private String identifier;

    private List<DynamicFieldObjectQuantityRespVO> biwSysVehicleBomQuantityList;

    public static List<String> getDynamicFieldList(List<DynamicFieldObjectRespVO> dynamicFieldObjectRespVOList) {
        if (!CollectionUtils.isEmpty(dynamicFieldObjectRespVOList)) {
            return dynamicFieldObjectRespVOList.stream()
                    .filter(respVO -> !CollectionUtils.isEmpty(respVO.getBiwSysVehicleBomQuantityList()))
                    .flatMap(respVO -> respVO.getBiwSysVehicleBomQuantityList().stream())
                    .map(DynamicFieldObjectQuantityRespVO::getBodyNumber)
                    .distinct().sorted().toList();
        }
        return new ArrayList<>();
    }

    public static List<List<String>> getHead(List<String> dynamicFieldList) {
        List<List<String>> head = new ArrayList<>();
        head.add(List.of("项目"));
        head.add(List.of("阶段"));
        head.add(List.of("标识"));
        head.addAll(Optional.ofNullable(dynamicFieldList).orElse(new ArrayList<>()).stream().map(Arrays::asList).toList());
        return head;
    }

    public static List<List<Object>> getData(List<String> dynamicFieldList, List<DynamicFieldObjectRespVO> dynamicFieldObjectRespVOList) {
        List<List<Object>> rowList = new ArrayList<>();

        for (DynamicFieldObjectRespVO dynamicFieldObjectRespVO : dynamicFieldObjectRespVOList) {
            Map<String, DynamicFieldObjectQuantityRespVO> dynamicRespVOMap = Optional.ofNullable(dynamicFieldObjectRespVO.getBiwSysVehicleBomQuantityList()).orElse(new ArrayList<>()).stream()
                    .collect(Collectors.toMap(
                            DynamicFieldObjectQuantityRespVO::getBodyNumber,
                            Function.identity(),
                            (existing, replacement) -> existing
                    ));

            List<Object> row = new ArrayList<>();
            row.add(dynamicFieldObjectRespVO.getProject());
            row.add(dynamicFieldObjectRespVO.getStage());
            row.add(dynamicFieldObjectRespVO.getIdentifier());

            for (String bodyInWhiteNumber : dynamicFieldList) {
                DynamicFieldObjectQuantityRespVO quantityRespVO = dynamicRespVOMap.get(bodyInWhiteNumber);
                if (quantityRespVO == null) {
                    row.add("");
                } else {
                    row.add(quantityRespVO.getQuantity() != null ? quantityRespVO.getQuantity() : "");
                }
            }

            rowList.add(row);
        }

        return rowList;
    }

    public static List<DynamicFieldObjectRespVO> getBiwSysVehicleBomRespVOList() {
        List<DynamicFieldObjectRespVO> biwSysVehicleBomPageReqVOList = new ArrayList<>();

        DynamicFieldObjectRespVO dynamicFieldObjectRespVO = new DynamicFieldObjectRespVO();
        dynamicFieldObjectRespVO.setProject("project001");
        dynamicFieldObjectRespVO.setStage("stage001");
        dynamicFieldObjectRespVO.setIdentifier("bli001");

        List<DynamicFieldObjectQuantityRespVO> dynamicFieldObjectQuantityRespVOList = new ArrayList<>();

        DynamicFieldObjectQuantityRespVO dynamicFieldObjectQuantityRespVO = new DynamicFieldObjectQuantityRespVO();
        dynamicFieldObjectQuantityRespVO.setBodyNumber("biw001");
        dynamicFieldObjectQuantityRespVO.setQuantity(1);
        dynamicFieldObjectQuantityRespVOList.add(dynamicFieldObjectQuantityRespVO);

        dynamicFieldObjectQuantityRespVO = new DynamicFieldObjectQuantityRespVO();
        dynamicFieldObjectQuantityRespVO.setBodyNumber("biw003");
        dynamicFieldObjectQuantityRespVO.setQuantity(3);
        dynamicFieldObjectQuantityRespVOList.add(dynamicFieldObjectQuantityRespVO);

        dynamicFieldObjectQuantityRespVO = new DynamicFieldObjectQuantityRespVO();
        dynamicFieldObjectQuantityRespVO.setBodyNumber("biw002");
        dynamicFieldObjectQuantityRespVO.setQuantity(2);
        dynamicFieldObjectQuantityRespVOList.add(dynamicFieldObjectQuantityRespVO);

        dynamicFieldObjectQuantityRespVO = new DynamicFieldObjectQuantityRespVO();
        dynamicFieldObjectQuantityRespVO.setBodyNumber("biw004");
        dynamicFieldObjectQuantityRespVO.setQuantity(4);
        dynamicFieldObjectQuantityRespVOList.add(dynamicFieldObjectQuantityRespVO);

        dynamicFieldObjectRespVO.setBiwSysVehicleBomQuantityList(dynamicFieldObjectQuantityRespVOList);
        biwSysVehicleBomPageReqVOList.add(dynamicFieldObjectRespVO);

        //====================================================================================
        dynamicFieldObjectRespVO = new DynamicFieldObjectRespVO();
        dynamicFieldObjectRespVO.setProject("project002");
        dynamicFieldObjectRespVO.setStage("stage002");
        dynamicFieldObjectRespVO.setIdentifier("bli002");

        dynamicFieldObjectQuantityRespVOList = new ArrayList<>();

        dynamicFieldObjectQuantityRespVO = new DynamicFieldObjectQuantityRespVO();
        dynamicFieldObjectQuantityRespVO.setBodyNumber("biw001");
        dynamicFieldObjectQuantityRespVO.setQuantity(5);
        dynamicFieldObjectQuantityRespVOList.add(dynamicFieldObjectQuantityRespVO);

        dynamicFieldObjectQuantityRespVO = new DynamicFieldObjectQuantityRespVO();
        dynamicFieldObjectQuantityRespVO.setBodyNumber("biw002");
        dynamicFieldObjectQuantityRespVO.setQuantity(6);
        dynamicFieldObjectQuantityRespVOList.add(dynamicFieldObjectQuantityRespVO);

        dynamicFieldObjectQuantityRespVO = new DynamicFieldObjectQuantityRespVO();
        dynamicFieldObjectQuantityRespVO.setBodyNumber("biw003");
        dynamicFieldObjectQuantityRespVO.setQuantity(7);
        dynamicFieldObjectQuantityRespVOList.add(dynamicFieldObjectQuantityRespVO);

//        biwSysVehicleBomQuantityRespVO = new BiwSysVehicleBomQuantityRespVO();
//        biwSysVehicleBomQuantityRespVO.setBodyInWhiteNumber("biw004");
//        biwSysVehicleBomQuantityRespVO.setQuantity(4);
//        biwSysVehicleBomQuantityRespVOList.add(biwSysVehicleBomQuantityRespVO);

        dynamicFieldObjectRespVO.setBiwSysVehicleBomQuantityList(dynamicFieldObjectQuantityRespVOList);
        biwSysVehicleBomPageReqVOList.add(dynamicFieldObjectRespVO);

        return biwSysVehicleBomPageReqVOList;
    }

}
