package com.yzh.test;

/**
 * @author Yzh
 * @create 2020-12-09 11:21
 */

public class Test {
    @org.junit.Test
    public void Test1(){
        int i = 1;
        Long integer = new Long(5);
        Student student = new Student("小红");
        System.out.println("初始化："+"int"+i+","+"integer"+integer+"名字"+student.getName());
        TestDemo(i,integer,student);
        System.out.println("结束后："+"int"+i+","+"integer"+integer+"名字"+student.getName());
    }
    public void TestDemo(int i,Long integer,Student student){
        i=i+1;
        integer = integer+5;
        student.setName("小er");
        System.out.println("方法内："+"int"+i+","+"integer"+integer+"名字"+student.getName());
    }
    class Student {
        private String name;
        public Student (String name){
            this.name=name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
