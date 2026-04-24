package com.spring.boot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/16 20:26
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    private String name;
    private Date date;
    private LocalDateTime localDateTime;
    private LocalDate localDate;
    private LocalTime localTime;
}
