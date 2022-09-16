package org.example.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode//用于后期的去重使用
public class Book {

    private Long id;
    private String name;
    //分类
    private String category;
    private Integer score;
    //简介
    private String intro;
}
