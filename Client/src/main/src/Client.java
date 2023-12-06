package main.src;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements Runnable {
    // 서버 포트 지정
    private static final int PORT = 8000;
    static Socket csocket;
    static BufferedReader in;
    static PrintWriter out;
    private static AtomicBoolean ID_reg_Flag = new AtomicBoolean(true);

    public void reg_stop() {
        ID_reg_Flag.set(false);
    }

    @Override
    public void run() {
        String msg;
        while (true) {
            try {
                msg = in.readLine();
            } catch (IOException e) {
                break;
            }
            if (msg == null) {
                System.out.println("서버 연결이 종료되었습니다.");
                break;
            } else if (msg.equals("Success:Reg_ID")) {
                reg_stop();
                System.out.println("ID 등록이 성공했습니다");
            } else {
                System.out.println("서버로부터 온 메시지: " + msg);
            }
        }
    }

    private static void showUsage() {
        System.out.println("USAGE: \"BR:message\"if you want to send your message to whole other clients");
        System.out.println("USAGE: \"TO:peerID:message\"if you want to send your message to the specific client");
        System.out.println("USAGE: \"Quit:\"if you want to stop");
    }

    public static void main(String[] args) throws Exception {
        Client t = new Client();
        final Scanner sc = new Scanner(System.in);

        // 소켓 생성
        csocket = new Socket("localhost", PORT);
        // 읽기 스트림
        in = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
        // 쓰기 스트림
        out = new PrintWriter(csocket.getOutputStream());

        Thread rt = new Thread(t);
        rt.start();

        String msg;
        String myID;

        do {
            System.out.println("Enter:");
            myID = sc.nextLine();
//            msg = "ID:" + myID + "\n" + "body " + myID;
            msg = "POST /user\t" +
                    // body
                    "username:testUsername," +
                    "password:testPassword," +
                    "nickname:testNickname";
            System.out.println(msg);
            out.println(msg);
            out.flush();
            System.out.println("전송");
            Thread.sleep(1000);
                /* 컴퓨터 성능에 따라 서버로부터 답이 늦게 오면 키보드 입력문으로 바로
                   실행이 넘어갈 수 있어서 서버로부터 답을 기다리기 위해 잠시 기다린 후
                   조건문을 검사합니다.
                 */
        } while (ID_reg_Flag.get());

        showUsage();

        while (true) {
            msg = sc.nextLine();
            String[] tokens = msg.split(":");
            String code = tokens[0];
            if (code.equalsIgnoreCase("Quit")) {
                msg = "QUIT:" + myID + ":";
                out.println(msg);
                out.flush();
                break;
            } else if ("TO".equals(code)) {
                String toID = tokens[1];
                String message = tokens[2];
                msg = "TO:" + myID + ":" + toID + ":" + message;
                out.println(msg);
                out.flush();
            } else if ("BR".equals(code)) {
                String message = tokens[1];
                msg = "BR:" + myID + ":" + message;
                out.println(msg);
                out.flush();
            } else {  //정해진 사용법이 아닌 것으로 입력시
                showUsage();
            }
        }
        out.close();
        csocket.close();
    }
}