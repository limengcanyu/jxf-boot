package com.spring.boot.multi.datasource.transaction.controller;


import com.spring.boot.multi.datasource.transaction.service.IPlatformSalaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 平台薪资项定义信息表 前端控制器
 * </p>
 *
 * @author rock.jiang
 * @since 2020-09-02
 */
@RestController
@RequestMapping("/platform-salary-item")
public class PlatformSalaryItemController {

    @Autowired
    private IPlatformSalaryItemService platformSalaryItemService;

    /**
     * localhost:8080/platform-salary-item/addItem
     *
     * @return
     */
    @RequestMapping("/addItem")
    public String addItem() throws Exception {
        platformSalaryItemService.addItem();
        return "add item success";
    }

    /**
     * localhost:8080/platform-salary-item/updateItem
     *
     * @return
     */
    @RequestMapping("/updateItem")
    public String updateItem() throws Exception {
        platformSalaryItemService.updateItem();
        return "add item success";
    }
}
