package com.yzh.utilts;

import java.util.Scanner;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 9:51
 */
public class EnvironmentSelectTool {
    public static String dev="";
    public static String devurl= "http://bt1.geosts.ac.cn/api/dae-dev/datastore";
    public static String produrl = "http://bt1.geosts.ac.cn/api/dae/datastore";
    public static String localHostUrl = "http://172.16.4.129:8085/datastore";

    public static String devUcUrl= "http://bt1.geosts.ac.cn/api/uc-dev";
    public static String prodUcUrl = "http://bt1.geosts.ac.cn/api/uc";
    public static String localHostUcUrl = "http://172.16.4.129:8085/btuc";


    public static String finalUrl = devurl;
    public static String finalUcUrl=devUcUrl;

    public static void selectEnv(){
        Scanner scanner = new Scanner(System.in);
        int select = scanner.nextInt();
            switch (select) {
                case 1:
                    finalUrl = devurl;
                    finalUcUrl = devUcUrl;
                    break;
                case 2:
                    finalUrl = produrl;
                    finalUcUrl = prodUcUrl;
                    break;
                case 3:
                    finalUrl = localHostUrl;
                    finalUcUrl = localHostUcUrl;
                    break;
                default:
                    break;
            }
    }

    public static void main(String[] args) {
        System.out.println(finalUrl);
        selectEnv();
        System.out.println(finalUrl);
    }
}
