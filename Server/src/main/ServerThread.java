package main;

import main.Controller.ChatController;
import main.Controller.DefaultController;
import main.Controller.RoomController;
import main.Controller.UserController;
import main.config.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerThread implements Runnable {
    Socket client;//Socket 클래스 타입의 변수 child 선언
    BufferedReader in; // BufferReader 클래스 타입의 변수 ois 선언
    PrintWriter out; // PrintWriter 클래스 타입의 변수 oos 선언
//    public HashMap<Long, PrintWriter> onlineUser; // 접속자 관리
    InetAddress ip; // InetAddress 클래스 타입의 변수 ip 선언
    DefaultController controller;
    ResponseBody responseBody;

    public ServerThread(Socket socket) throws IOException {
        client = socket;
//        onlineUser = main.MainServer.onlineUser;

        // 클라이언트 소켓의 input / output stream 과 ip를 등록 하고 있음.
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(client.getOutputStream(), false, StandardCharsets.UTF_8);
            ip = client.getInetAddress();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        String msg;
        String[] request;
        String code;
        String fromID = null;
        try {
            while ((msg = in.readLine()) != null) { // BufferedReader 에 값이 들어올 때 까지 대기함.
                request = msg.split("\t");
                /**
                 * request[0] (header) == POST /user Chat/1.0
                 * request[1] (body) == username:username1,password:password1,nickname:nickname1
                 */
                switch (request[0].split(" ")[1]) { // path
                    case "/user":
                        controller = UserController.getInstance();
                        break;
                    case "/room":
                        controller = RoomController.getInstance();
                        break;
                    case "/chat":
                        controller = ChatController.getInstance();
                        break;
                    default:
                        ; // 잘못된 코드
                }
                responseBody = controller.callMethod(request[0].split(" ")[0], request[2]);

//                final String[] tokens = msg.split("\n");
//                code = tokens[0];
//                fromID = tokens[1];

//                if ("QUIT".equals(code)) {
//                    System.out.println(fromID + "종료합니다.");
//                    synchronized (접속자) {
//                        try {
//                            접속자.remove(fromID);
//                        } catch (Exception ignored) {
//                        }
//                    }
//                    break;
//                } else if ("ID".equals(code)) { //ID 등록
//                    if (접속자.containsKey(fromID)) {
//                        out.println("FAIL:Reg_ID");
//                        out.flush();
//                    } else {
//                        synchronized (접속자) {
//                            접속자.put(fromID, out);
//                            out.println("Success:Reg_ID");
//                            out.flush();
//                        }
//                    }
//                } else if ("TO".equals(code)) {
//                    String toID = tokens[2];
//                    sendTo(toID, msg);
//                } else {
//                    broadcast(msg);
//                }
//            }
//            } catch(Exception e){
//                System.out.println(e.toString());
//            } finally{
//                try {
//                    in.close();
//                    out.close();
//                    client.close();
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//            }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
//    private void sendTo(String toID, String message) throws IOException {
//        PrintWriter out = onlineUser.get(toID);
//
//        if (out != null) {
//            out.println(message);
//            out.flush();
//        }
//    }
}