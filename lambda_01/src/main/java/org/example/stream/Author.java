package org.example.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode//用于后期的去重使用 重新equal方法，因为stream中的去重是依赖于equal的
public class Author {
    private Long id;
    private String name;
    private Integer age;
    private String intro;
    private List<Book> books;

}
