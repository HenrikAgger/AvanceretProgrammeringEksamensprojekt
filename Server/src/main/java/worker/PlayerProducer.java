/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worker;

import Interfaces.Actor;
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
public class PlayerProducer extends Thread {

    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader br;
    private Controller controller;
    private Actor player;

    public PlayerProducer(Socket socket, Controller controller) throws IOException {
        this.socket = socket;
        this.controller = controller;
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

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
            printWriter.println("Du har valgt at indsætte følgende: " + line);
        }
    }
}
