package org.asura.easyexcel.vo;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ExcelIgnoreUnannotated
@Data
public class FixedFieldObjectRespVO {

    @ExcelProperty(value = "项目")
    private String project;

    @ExcelProperty(value = "阶段")
    private String stage;

    @ExcelProperty(value = "标识")
    private String identifier;

    // JSON字段
    private String changer;

    @ExcelProperty(value = "变更人")
    private String changerName;

    public String getChangerName() {
        if (JSONUtil.isTypeJSON(changer)) {
            JSONObject userObject = JSONUtil.parseObj(changer);
            return userObject.getOrDefault("userName", "").toString();
        }
        return changer;
    }

    public static List<FixedFieldObjectRespVO> getFixedFieldObjectRespVOList() {
        List<FixedFieldObjectRespVO> fixedFieldObjectRespVOList = new ArrayList<>();

        FixedFieldObjectRespVO fixedFieldObjectRespVO = new FixedFieldObjectRespVO();
        fixedFieldObjectRespVO.setProject("project001");
        fixedFieldObjectRespVO.setStage("stage001");
        fixedFieldObjectRespVO.setIdentifier("bli001");
        fixedFieldObjectRespVO.setChanger("{'userName': 'rock'}");
        fixedFieldObjectRespVOList.add(fixedFieldObjectRespVO);

        //====================================================================================
        fixedFieldObjectRespVO = new FixedFieldObjectRespVO();
        fixedFieldObjectRespVO.setProject("project002");
        fixedFieldObjectRespVO.setStage("stage002");
        fixedFieldObjectRespVO.setIdentifier("bli002");
        fixedFieldObjectRespVO.setChanger("{'userName': 'Jessica'}");
        fixedFieldObjectRespVOList.add(fixedFieldObjectRespVO);

        return fixedFieldObjectRespVOList;
    }

}
