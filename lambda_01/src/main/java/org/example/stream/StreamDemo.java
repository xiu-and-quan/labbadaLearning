package org.example.stream;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

    public static void main(String[] args) {
        List<Author> authors = getAuthors();
        //根据集合的存储方式不同，将集合划分两大类：一类是单列集合List Set ，另一类是双列集合 Map
        //流必须有终结操作
        //对于单列集合直接.stream 比如List
        //对于数组 Arrays.stream(arr)，也可以使用Stream.of(arr) 本质一样的 
        // public static<T> Stream<T> of(T... values) {
        //        return Arrays.stream(values);
        //    }
        //对于map集合 双列集合需要转成单列集合后再用流

        //单列集合
//        authors.stream() //把集合转换成流
//                .distinct() //去重
//                .filter(author -> author.getAge()<18) //过滤器  留下年龄小于18的留在流中
//                //打印
//                .forEach(author -> System.out.println(author.getName()));
        //双列集合
        //mapStreamTest();

        //过滤器filter的使用
        authors.stream().filter(new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.getName().length()>1;
            }
        }).forEach(new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                System.out.println(author.getName());
            }
        });

        //中间件map 主要用于堆流中的元素进行计算和转换 重要
        mapTest();

        //去重
//        Author author3 = new Author(3L,"易",14,"是这个世界在限制他的思维",null);
//        Author author4 = new Author(3L,"易",14,"是这个世界在限制他的思维",null);
//        boolean res = author3.equals(author4);
//        System.out.println(res);

        //sort 排序  如果调用空参的sorted，需要将比较的类需要实现compareAble接口，实现比较方法
        authors.stream().
                distinct().
                sorted(new Comparator<Author>() {
            @Override
            public int compare(Author o1, Author o2) {
                return o2.getAge()-o1.getAge();
            }
        }).forEach(new Consumer<Author>() {
                    @Override
                    public void accept(Author author) {
                        System.out.println(author.getName()+"========"+author.getAge());
                    }
                });
        System.out.println("======================LIMIT======================");
        //limit
        //对流中的数据按照年龄降序，并且去重，打印其中年龄最大的两个作家
        authors.stream().
                distinct().
                sorted(new Comparator<Author>() {
            @Override
            public int compare(Author o1, Author o2) {
                return o2.getAge()-o1.getAge();
            }
        }).limit(2)
                .forEach(new Consumer<Author>() {
                    @Override
                    public void accept(Author author) {
                        System.out.println(author.getName()+"===="+author.getAge());
                    }
                });
        System.out.println("======================SKIP======================");
        //skip 跳过前n个打印
        //打印除了年龄最大的其他作家
        authors.stream()
                .distinct()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o2.getAge()-o1.getAge();
                    }
                })
                .skip(1)
                .forEach(new Consumer<Author>() {
                    @Override
                    public void accept(Author author) {
                        System.out.println(author.getName()+"===="+author.getAge());
                    }
                });
        System.out.println("======================flatMap======================");
        //flatMap map是可以将数据进行类型转换 map只能将一个对象转换成另一个对象作为流中的元素
        //flatMap 可以把一个对象转换成多个对象作为流中的元素
        //打印所有作家的书籍 每个作家里面有很多书籍
        //用map的问题
        System.out.println("======================Map的方法======================");
        authors.stream().map(author -> author.getBooks())
                .forEach(new Consumer<List<Book>>() {
                    @Override
                    public void accept(List<Book> books) {
                        //这里面是List
                        books.forEach(new Consumer<Book>() {
                            @Override
                            public void accept(Book book) {
                                System.out.println(book.getName());
                            }
                        });
                    }
                });
        System.out.println("======================flatMap的方法======================");
        authors.stream()
                .flatMap(author -> author.getBooks().stream())//flatMap 可以把一个对象转换成多个对象作为流中的元素
                .distinct()
                .forEach(book -> System.out.println(book.getName()));
        System.out.println("======================flatMap的方法-分类======================");
        //分类
        authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .flatMap(book -> Arrays.stream(book.getCategory().split(",")))//public String[] split() split方法是返回字符串数组的 然后把数组转成流 调用Arrays.stream()
                .distinct()
                .forEach(System.out::println);

        //终结操作
        //输出所有作家的名字
        System.out.println("======================foreach的方法-输出作家的名字======================");
        authors.stream()
                .distinct()
                .forEach(author -> System.out.println(author.getName()));
        System.out.println("======================foreach的方法map-输出作家的名字======================");
        authors.stream()
                .map(author -> author.getName())
                .distinct()
                .forEach(name->{
                    System.out.println(name);
                });
        System.out.println("======================count的方法-输出作家的名字======================");
        //count 获取当前流中的元素
        Long count = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .count();//count()有返回值 需要接收
        System.out.println(count);
        System.out.println("======================max&min的方法======================");
        //max&min求流中的最值
        //分别获取这些作家的所出书籍的最高分和最低分并打印。
        //最大值
        Optional<Integer> max = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .map(book -> book.getScore())
                .max((o1, o2) -> o1 - o2);
        System.out.println(max.get());
        System.out.println("======================collect的方法转成toList======================");
        //collect把流转成集合 重要
        //获取一个存放所有作者名字的List集合
        List<String> resList = authors.stream()
                .map(new Function<Author, String>() {
                    @Override
                    public String apply(Author author) {
                        return author.getName();
                    }
                })
                .collect(Collectors.toList());//用的工具类Collectors
        System.out.println(resList);
        System.out.println("======================collect的方法转成Set======================");
        //获取一个所有书名的Set集合。
        Set<Book> resSet = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .collect(Collectors.toSet());
        System.out.println(resSet);

        System.out.println("======================collect的方法转成Map集合======================");
        //获取一个Map集合，map的key为作者名，value为List<Book>
        //toMap需要两个参数 map分为键值对的
        authors.stream()
                .distinct()
                .collect(Collectors.toMap(new Function<Author, String>() {
                    @Override
                    public String apply(Author author) {
                        return author.getName();
                    }
                }, new Function<Author, List<Book>>() {
                    @Override
                    public List<Book> apply(Author author) {
                        return author.getBooks();
                    }
                }))
    }

    //中间件map 主要用于堆流中的元素进行计算和转换 重要
    public static void mapTest(){
        //打印所有作家的姓名
        List<Author> authors = getAuthors();
        //可以连续使用
        authors.stream().map(author -> author.getAge()).map(age->age+10).forEach(x -> System.out.println(x));
    }

    
    public static void mapStreamTest(){
        Map<String,Integer> map = new HashMap<>();
        map.put("xiu",18);
        map.put("quan",19);
        map.put("li",27);
        //Set是单列集合
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        entrySet.stream().filter(new Predicate<Map.Entry<String, Integer>>() {
            @Override
            public boolean test(Map.Entry<String, Integer> stringIntegerEntry) {
                return stringIntegerEntry.getValue()>18;
            }
        })
                .forEach(new Consumer<Map.Entry<String, Integer>>() {
                    @Override
                    public void accept(Map.Entry<String, Integer> stringIntegerEntry) {
                        System.out.println(stringIntegerEntry.getKey()+"======="+stringIntegerEntry.getValue());
                    }
                });

    }
    private static List<Author> getAuthors() {
        //数据初始化
        Author author = new Author(1L,"蒙多",33,"一个从菜刀中明悟哲理的祖安人",null);
        Author author2 = new Author(2L,"亚拉索",15,"狂风也追逐不上他的思考速度",null);
        Author author3 = new Author(3L,"易",14,"是这个世界在限制他的思维",null);
        Author author4 = new Author(3L,"易",14,"是这个世界在限制他的思维",null);

        //书籍列表
        List<Book> books1 = new ArrayList<>();
        List<Book> books2 = new ArrayList<>();
        List<Book> books3 = new ArrayList<>();

        books1.add(new Book(1L,"刀的两侧是光明与黑暗","哲学,爱情",88,"用一把刀划分了爱恨"));
        books1.add(new Book(2L,"一个人不能死在同一把刀下","个人成长,爱情",99,"讲述如何从失败中明悟真理"));

        books2.add(new Book(3L,"那风吹不到的地方","哲学",85,"带你用思维去领略世界的尽头"));
        books2.add(new Book(3L,"那风吹不到的地方","哲学",85,"带你用思维去领略世界的尽头"));
        books2.add(new Book(4L,"吹或不吹","爱情,个人传记",56,"一个哲学家的恋爱观注定很难把他所在的时代理解"));

        books3.add(new Book(5L,"你的剑就是我的剑","爱情",56,"无法想象一个武者能对他的伴侣这么的宽容"));
        books3.add(new Book(6L,"风与剑","个人传记",100,"两个哲学家灵魂和肉体的碰撞会激起怎么样的火花呢？"));
        books3.add(new Book(6L,"风与剑","个人传记",100,"两个哲学家灵魂和肉体的碰撞会激起怎么样的火花呢？"));

        author.setBooks(books1);
        author2.setBooks(books2);
        author3.setBooks(books3);
        author4.setBooks(books3);

        List<Author> authorList = new ArrayList<>(Arrays.asList(author,author2,author3,author4));
        return authorList;
    }
}
