package org.example.thread;

import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public class LambdaDemo01 {
    public static void main(String[] args) {
        //lambda只关注参数和动作，动作就是做了啥指方法体
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("线程启动");
//            }
//        });
//        new Thread(()->{
//            System.out.println("新线程执行！");
//        }).start();

//        calculateNum(new IntBinaryOperator() {
//            @Override
//            public int applyAsInt(int left, int right) {
//                return left+right;
//            }
//        });
//
//        calculateNum((int left, int right)-> {
//                return left+right;
//            });
        printNUm((value) -> {
            return value%2==0;
        });

        Integer result = typeConver((String str)->{
            return Integer.valueOf(str);
        });
        System.out.println(result);
        //泛型返回值
        String result1 = typeConver((String s)->{
            return s+"lambda";
        });
        System.out.println(result1);

        foreachArr((int value)->{
            System.out.println(value);
        });
        foreachArr(System.out::println);
    }

    public static int calculateNum(IntBinaryOperator operator){
        int a = 10;
        int b = 20;
        return operator.applyAsInt(a,b);
    }

    public static void printNUm(IntPredicate predicate){
        int[] arr = {1,2,3,4,5,6,7,8,9,0};
        for (int i:arr) {
            if (predicate.test(i)){
                System.out.println(i);
            }
        }
    }

    public static <R> R typeConver(Function<String,R>function){
        String str = "123";
        R result = function.apply(str);
        return result;
    }

    public static void foreachArr(IntConsumer consumer){
        int[] arr = {1,2,3,4,5,6,7,8};
        for (int i:arr) {
            consumer.accept(i);
        }
    }
}
