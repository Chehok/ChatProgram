package main.src;

import main.config.CustomRequest;
import main.src.Domain.Chat.sendMessage;
import main.src.Domain.Room.CreateRoom;
import main.src.Domain.User.LoginDto;
import main.src.Domain.User.SignUpDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements Runnable {
    // 서버 포트 지정
    private static final int PORT = 8000;
    static Socket csocket;
    static BufferedReader in;
    static PrintWriter out;
    private static AtomicBoolean isLoggedIn = new AtomicBoolean(false);
    static final Scanner sc = new Scanner(System.in);

    public void setLoginStatus() {
        if (isLoggedIn.get()) isLoggedIn.set(false);
        else isLoggedIn.set(true);
    }

    static String func;
    static String myID;

    @Override
    public void run() {
        String msg;
        String header; // statusCode, isSuccess
        String body;

        while (true) {
            try {
                msg = in.readLine();
            } catch (IOException e) {
                break;
            }
            header = msg.split("\t")[0];
            body = msg.split("\t")[1];

            if (!isLoggedIn.get()) { // 비로그인
                if (func.equals("1")) {  // 회원가입
                    System.out.println(body.split(":")[1]);
                } else if (func.equals("2")) { // 로그인
                    if (header.split(" ")[1].equals("SUCCESS")) {
                        myID = body.split(",")[0].split(":")[1];
                        System.out.println("환영합니다! " + body.split(",")[1].split(":")[1] + " 님!\n");
                        setLoginStatus();
                    } else if (header.split(" ")[1].equals("FAIL")) {
                        System.out.println(body.split(":")[1]);
                    }
                } else if (func.equals("0")) { // 종료
                    break;
                }

            } else {
                if (func.equals("1")) { // 채팅방 목록 보기
                    for(String roomInfo: body.split("/")) {
                        System.out.println(roomInfo);
                    }
                } else if (func.equals("2")) { // 채팅방 생성
                    System.out.println(body.split(":")[1]);
                } else if (func.equals("3")) { // 채팅방 채팅 내역
                    for(String chatInfo: body.split("/")) {
                        System.out.println(chatInfo);
                    }
                } else if (func.equals("4")) { // 채팅 보내기
                    System.out.println(body);
                } else if (func.equals("0")) { // 로그아웃
                    System.out.println(body.split(":")[1]);
                    setLoginStatus();
                }
            }
        }

//        while (isLoggedIn.get()) {
//            try {
//                msg = in.readLine();
//                header = msg.split("\t")[0];
//                body = msg.split("\t")[1];
//            } catch (IOException e) {
//                break;
//            }
//            if (header.split(" ")[1].equals("SUCCESS")) {
////                if(body.equals(""))
//                System.out.println("서버로부터 온 메시지: " + body);
//            } else {
//                System.out.println("서버로부터 온 메시지: " + body);
//            }
//            if (msg == null) {
//                System.out.println("서버 연결이 종료되었습니다.");
//                break;
//            } else if (msg.split("\t")[0].split(" ")[1].equals("SUCCESS")) {
//                reg_stop();
//                System.out.println("ID 등록이 성공했습니다");
//            } else {
//                System.out.println("서버로부터 온 메시지: " + msg);
//            }
//        }
    }

    private static void showUsage() {
        System.out.println("기능을 선택 하십시오. \n" +
                "1 - 채팅방 목록 보기 \n" +
                "2 - 채팅방 생성 \n" +
                "3 - 채팅방 채팅 내역 \n" +
                "4 - 채팅 보내기 \n" +
                "0 - 로그아웃");
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Client t = new Client();
        try {
            // 소켓 생성
            csocket = new Socket("localhost", PORT);
            // 읽기 스트림
            in = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
            // 쓰기 스트림
            out = new PrintWriter(csocket.getOutputStream());
        } catch (ConnectException e) {
            System.out.println("서버가 열려있지 않습니다!");
            System.exit(0);
        } catch (UnknownHostException e) {
            System.out.println("서버 주소가 잘못됐습니다!");
            System.exit(0);
        }


        Thread rt = new Thread(t);
        rt.start();

        while (true) {
            String msg;
            CustomRequest customRequest;
            if (!isLoggedIn.get()) {    // 비로그인 상태
                String username;
                String password;
                String nickname;
                System.out.println("기능을 선택 하십시오.\n" +
                        "1 - 회원가입 \n" +
                        "2 - 로그인 \n" +
                        "0 - 종료");
                func = sc.nextLine();

                if (func.equals("1")) {
                    System.out.println("아이디를 입력하세요");
                    username = sc.nextLine();
                    System.out.println("비밀번호를 입력하세요");
                    password = sc.nextLine();
                    System.out.println("닉네임을 입력하세요");
                    nickname = sc.nextLine();
                    customRequest = new CustomRequest(
                            "POST", "/user",
                            new SignUpDto(username, password, nickname)
                    );
                } else if (func.equals("2")) {
                    System.out.println("아이디를 입력하세요");
                    username = sc.nextLine();
                    System.out.println("비밀번호를 입력하세요");
                    password = sc.nextLine();
                    customRequest = new CustomRequest(
                            "GET", "/user",
                            new LoginDto(username, password)
                    );
                } else if (func.equals("0")) {
                    System.out.println("종료합니다.");
                    out.close();
                    csocket.close();
                    break;
                } else {
                    System.out.println("잘못된 입력입니다.");
                    continue;
                }
            } else {                    // 로그인 상태
                showUsage();
                func = sc.nextLine();

                if (func.equals("1")) { // 채팅방 목록 보기 -> userId
                    customRequest = new CustomRequest("GET", "/room", "userId:" + myID);
                } else if (func.equals("2")) { // 채팅방 생성 -> roomName
                    System.out.println("생성할 채팅방 이름을 입력하세요");
                    String roomName = sc.nextLine();
                    customRequest = new CustomRequest(
                            "POST", "/room",
                            new CreateRoom(roomName, myID)
                    );
                } else if (func.equals("3")) { // 채팅방 채팅 내역 -> roomId
                    System.out.println("채팅방을 선택하세요. ex) 1");
                    String roomId = sc.nextLine();
                    customRequest = new CustomRequest(
                            "GET", "/chat",
                            "roomId:" + roomId
                    );
                } else if (func.equals("4")) { // 채팅 보내기 -> roomId, message, userId
                    System.out.println("채팅방을 선택하세요. ex) 1");
                    String roomId = sc.nextLine();
                    System.out.println("메시지를 입력하세요.");
                    String message = sc.nextLine();
                    customRequest = new CustomRequest(
                            "POST", "/chat",
                            new sendMessage(roomId, message, myID)
                    );
                } else if (func.equals("0")) { // 로그아웃
                    customRequest = new CustomRequest("PATCH", "/user", "userId:" + myID);
                } else {
                    System.out.println("잘못된 입력입니다.");
                    continue;
                }
            }
            msg = customRequest.getRequest();
            System.out.println(msg);
            out.println(msg);
            out.flush();
            System.out.println("전송");
            Thread.sleep(500);
            /* 컴퓨터 성능에 따라 서버로부터 답이 늦게 오면 키보드 입력문으로 바로
                실행이 넘어갈 수 있어서 서버로부터 답을 기다리기 위해 잠시 기다린 후
                조건문을 검사합니다.
            */
        }
//        showUsage();

//        while (ID_reg_Flag.get()) {
//            msg = sc.nextLine();
//            String[] tokens = msg.split(":");
//            String code = tokens[0];
//            if (code.equalsIgnoreCase("Quit")) {
//                msg = "QUIT:" + myID + ":";
//                out.println(msg);
//                out.flush();
//                break;
//            } else if ("TO".equals(code)) {
//                String toID = tokens[1];
//                String message = tokens[2];
//                msg = "TO:" + myID + ":" + toID + ":" + message;
//                out.println(msg);
//                out.flush();
//            } else if ("BR".equals(code)) {
//                String message = tokens[1];
//                msg = "BR:" + myID + ":" + message;
//                out.println(msg);
//                out.flush();
//            } else {  //정해진 사용법이 아닌 것으로 입력시
//                showUsage();
//            }
//        }
    }
}