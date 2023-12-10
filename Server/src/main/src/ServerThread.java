package main.src;

import main.config.CustomException;
import main.src.Controller.ProxyController;
import main.src.DAO.DefaultDAO;

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
    public PrintWriter out; // PrintWriter 클래스 타입의 변수 oos 선언
    InetAddress ip; // InetAddress 클래스 타입의 변수 ip 선언
    ProxyController proxyController;

    public ServerThread(Socket socket) {
        client = socket;
        proxyController = new ProxyController();

        // 클라이언트 소켓의 input / output stream 과 ip를 등록 하고 있음.
        try {
            new DefaultDAO().init();
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8);
            ip = client.getInetAddress();
        } catch (IOException | CustomException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        String msg;
        String[] request;
        String response;
        try {
            while ((msg = in.readLine()) != null) { // BufferedReader 에 값이 들어올 때 까지 대기함.
                request = msg.split("\t");
                /**
                 * request[0] (header) == POST /user
                 * request[1] (body) == username:username1,password:password1,nickname:nickname1
                 */
                try {
                    response = proxyController.callService(request[0], request[1], out).getResponse();
                    out.println(response);
                } catch (Exception ignore) {

                }

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
}