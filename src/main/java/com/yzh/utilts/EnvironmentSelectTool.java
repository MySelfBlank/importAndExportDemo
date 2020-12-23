package com.yzh.utilts;

import java.util.Scanner;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 9:51
 */
public class EnvironmentSelectTool {
    public static String dev= "";

    public static void selectEnv(){
        Scanner scanner = new Scanner(System.in);
        int select = scanner.nextInt();
        switch (select){
            case 1:
                dev="";
                break;
            case 2:
                dev="-dev";
                break;
        }
    }
}
