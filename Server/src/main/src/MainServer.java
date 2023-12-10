package main.src;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainServer implements Runnable {
    int port = 8000;
    ServerSocket server = null;
    Socket socket = null;
    public static HashMap<Long, PrintWriter> onlineUser;
    public MainServer() {}

    @Override
    public void run() {
        ServerThread serverThread;

        Thread thread;

        try {
            server = new ServerSocket(port); //소켓 생성부터 listen까지
            System.out.println("접속대기");//출력

            onlineUser = new HashMap<>(); // hashMap 객체 => String 랜덤 문자열 /

            while (true) {
                socket = server.accept(); // accept 메서드를 하게 되면 대기한다.
                System.out.println("Server accept");
                if (socket.isConnected()) {
                    serverThread = new ServerThread(socket);
                    thread = new Thread(serverThread);
                    thread.setDaemon(true); // 데몬스레드는 일반스레드의 보조역할을 하므로, 일반스레드가 종료되면 데몬스레드도 종료 됨.
                    // 다시 말해서 serverThread 는 main.src.MainServer 에 종속 됨.
                    thread.start(); //쓰레드 시작
                }
            }
            // server.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
