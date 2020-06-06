/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worker;

import controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import model.Event;
import model.Player;

/**
 *
 * @author Henrik
 */
public class Producer extends Thread {

    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader br;
    private Controller controller;
    private Player player;

    public Producer(Socket socket, Controller controller) throws IOException {
        this.socket = socket;
        this.controller = controller;
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

//    public void Write(Event event) {
//        try {
//            queue.add(event);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void run() {
        while (true) {
            String line = "";
            printWriter.println("What is your name");
            try {

                line = br.readLine();
                player = controller.registerPlayer(line, printWriter);
                Thread.currentThread().setName(line);
                while ((line = br.readLine()) != null) {
                    controller.processMessage(player, line);
                    
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
            Event event = new Event(line);
//            Write(event);
            printWriter.println("Du har valgt at indsætte følgende: " + line);
        }
    }

}
