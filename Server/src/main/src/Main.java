package main.src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        final Scanner sc = new Scanner(System.in);
        MainServer mainServer = new MainServer(); //main.src.MainServer 객체 생성

        Thread mt = new Thread(mainServer);
        mt.start();//쓰레드 시작
    }
}
