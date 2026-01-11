package com.spring.boot.fastexcel.service;

import com.spring.boot.fastexcel.dto.UserExportDTO;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public List<UserExportDTO> queryUserByPage(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return new ArrayList<>();
    }

}

