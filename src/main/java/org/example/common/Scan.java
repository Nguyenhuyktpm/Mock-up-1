package org.example.common;

import java.util.Scanner;

public class Scan {
    public int scanInt(){

        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
    public String scanString(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
