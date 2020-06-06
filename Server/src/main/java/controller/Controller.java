/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Interfaces.Actor;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BeginnerPlayer;
import model.Dice;
import model.Direction;
import model.Event;
import model.Gold;
import model.MasterPlayer;
import model.Monster;
import model.Player;
import model.Room;
import worker.PlayerProducer;
import worker.Consumer;
import worker.MonsterProducer;

/**
 *
 * @author Henrik
 */
public class Controller {

    private Actor player;
    private Monster monster;
    ServerSocket ss;
    Socket socket;
    Scanner scan = new Scanner(System.in);
    ArrayList<Room> rooms = new ArrayList<>();
    Dice dice;
    Gold gold;
    Random r = new Random();
    protected Room spawnPoint;
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    ArrayList<Actor> players = new ArrayList<Actor>();
    ArrayBlockingQueue<Event> events;
    PrintWriter[] printWrither;

    /**
     *
     * @param player
     * @param monster
     * @param events
     */
    public Controller() throws IOException {
        events = new ArrayBlockingQueue<Event>(10);
        this.ss = new ServerSocket(5555);
    }

    public void initGame() throws IOException {

        Monster monster1 = new Monster("Spider");
        Monster monster2 = new Monster("Troll");
        Monster monster3 = new Monster("Wizzard");
        Monster monster4 = new Monster("Org");

        monsters.add(monster1);
        monsters.add(monster2);
        monsters.add(monster3);
        monsters.add(monster4);

        System.out.println("Waiting");
        Room room1 = new Room("Entrance", "Entrance to the caves");
        Room room2 = new Room("Skeleton room", "You are in a room with a lot of old skeletons");
        Room room3 = new Room("Rat room", "You are in a room with a lot of rats");
        Room room4 = new Room("Spider room", "You are in a room with a lot of spiderwebs");
        Room room5 = new Room("Exit room", "You are in a room where you can see some daylight");
        Room room6 = new Room("Hurray you won", "People celebrate your return");

        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);
        rooms.add(room5);
        rooms.add(room6);

        room1.addRoomToExit(Direction.NORTH, room3);
        room1.addRoomToExit(Direction.EAST, room2);

        room2.addRoomToExit(Direction.NORTH, room4);
        room2.addRoomToExit(Direction.WEST, room1);

        room3.addRoomToExit(Direction.EAST, room4);
        room3.addRoomToExit(Direction.SOUTH, room1);

        room4.addRoomToExit(Direction.EAST, room5);
        room4.addRoomToExit(Direction.SOUTH, room2);
        room4.addRoomToExit(Direction.WEST, room3);

        room5.addRoomToExit(Direction.EAST, room6); //EXIT

        this.spawnPoint = room1;
    }

    public void runGame() throws IOException {
        System.out.println("Waiting");
        ArrayList<PlayerProducer> producers = new ArrayList<>();
        int counter = 0;
        int limit = 1;
        while (counter < limit) {
            counter++;
            Socket s = ss.accept();
            PlayerProducer producer = new PlayerProducer(s, this);
            producers.add(producer);
        }

        System.out.println("Socket from client");
        Consumer consumer = new Consumer(events);
        MonsterProducer monsterProducer = new MonsterProducer(this);

        for (PlayerProducer producer : producers) {
            producer.start();
        }

        monsterProducer.start();

    }

    public synchronized Actor registerPlayer(String name, PrintWriter printWriter) {
        Actor player = null;
        Actor basicplayer = new Player(name, printWriter);
        if (name.equals("kurt")) {
            player = new MasterPlayer(basicplayer);
        } else {
            player = new BeginnerPlayer(basicplayer);
        }
        System.out.println("You are " + player.getName());
        player.getWriter().println("Babtized " + player.getName());

        player.setRoom(this.spawnPoint);
        this.spawnPoint.addPlayer(player);
        players.add(player);
        return player;
    }

    public void processMessage(Actor player, String message) {
        String actionToken = "";
        String targetToken = "";
        System.out.println("Player ");
        player.getWriter().println("Hi you wrote " + message);
        String[] lineAr = message.split(" ");
        actionToken = lineAr[0];
        String[] targetTokenArray = Arrays.copyOfRange(lineAr, 1, lineAr.length);

        targetToken = String.join(" ", targetTokenArray);

        switch (actionToken) {
            case "MOVE":
                move(targetToken, player);
                return;
            case "FIGHT":
                fightMonster(targetToken, player);
                break;
            case "FIGHTPLAYER":
                fightPlayer(targetToken, player);
                break;
            case "EXIT":
                exitRoom(targetToken, player);
                break;
            case "NORTH":
                north(targetToken, player);
                break;
            case "EAST":
                east(targetToken, player);
                break;
            case "SOUTH":
                south(targetToken, player);
                break;
            case "WEST":
                west(targetToken, player);
                break;
            case "pick up gun":
                gun(targetToken, player);
                break;
            case "inventory":
                inventory(targetToken, player);
                break;
        }
    }

    public void move(String targetToken, Actor player) {
        player.getWriter().println("Hi move " + targetToken);
        synchronized (this) {
            Room room = player.getRoom();
            room.removePlayer(player);
            Room goToRoom = room.to(Direction.valueOf(targetToken));
            player.setRoom(goToRoom);
            goToRoom.addPlayer(player);
            player.getWriter().println("You are in a new room called" + goToRoom.getName());
            System.out.println(player.getName() + " You are in a new room called " + goToRoom.getName());
        }
    }

    public void fightMonster(String targetToken, Actor player) {
        gold = new Gold();
        dice = new Dice();
        Monster monster = monsters.get(r.nextInt(monsters.size()));
        player.getWriter().println("Hi into fight " + targetToken);
        if (player.playerFight() > monster.monsterFight(dice)) {
            System.out.println("You are fighting a " + monster.getMonsterName());
            System.out.println("Player wins");
            player.addGold(gold);
            System.out.println("You won pices of gold");
            System.out.println("Total amount of gold: " + player.getSizeOfGold());
        } else {
            System.out.println(monster.getMonsterName() + " wins, RIP");
        }
    }

    public synchronized void fightPlayer(String targetToken, Actor player) {
        gold = new Gold();
        dice = new Dice();
        int count = 0;
        Actor enemy = null;
        do {
            count++;
            enemy = player.getRoom().getRandomPlayer();
        } while (enemy.getName().equals(player.getName()) || count < 4);
        System.out.println(enemy.getName() + " Hi you are being attached " + enemy.getRoom().getName() + " " + targetToken);
        if (enemy == null) {
            System.out.println("No enemy");
            return;
        }
        try {
            System.out.println("Sleeps for 10 seconds. Enemy " + enemy.getRoom().getName());
            Thread.sleep(10000);
            System.out.println("Has slept for 10 sec.");
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        enemy.getWriter().println(enemy.getName() + " Hi you are being attached " + enemy.getRoom().getName() + " " + targetToken);
        System.out.println(enemy.getName() + " Hi you are being attached " + enemy.getRoom().getName() + " " + targetToken);
        System.out.println(player.getName() + " Hi you are being attacking in " + player.getRoom().getName() + " " + targetToken);
        if (enemy.playerFight() > player.playerFight()) {
            Event event = new Event("Monster wins");
            System.out.println("You are fighting a " + player.getName());
            System.out.println("Enemy wins" + enemy.getName());
            enemy.addGold(gold);
            System.out.println("You won gold");
            System.out.println("Total amount of gold: " + enemy.getSizeOfGold());
        } else {
            System.out.println(player.getName() + " wins, RIP");
            Event event = new Event("Player wins");
        }
    }

    public void exitRoom(String targetToken, Actor player) {
        player.getWriter().println("Hi exiting the room " + targetToken);
        System.out.println("You have exited a room");
    }

    public void north(String targetToken, Actor player) {
        player.getWriter().println("Hi you are going north" + targetToken);
        System.out.println("You have mooved north");
    }

    public void east(String targetToken, Actor player) {
        player.getWriter().println("Hi you are going east" + targetToken);
        System.out.println("You have mooved east");
    }

    public void south(String targetToken, Actor player) {
        player.getWriter().println("Hi you are going south" + targetToken);
        System.out.println("You have mooved south");
    }

    public void west(String targetToken, Actor player) {
        player.getWriter().println("Hi you are going west" + targetToken);
        System.out.println("You have mooved west");
    }

    public void gun(String targetToken, Actor player) {
        player.getWriter().println("Hi you pick up a gun");
        System.out.println("You have picked up a gun");
    }

    public void inventory(String targetToken, Actor player) {
        player.getWriter().println("You have following items in your inventory" + targetToken);
        System.out.println("You have following items");
    }

    public void fightMonster() {
        System.out.println("Fighting monster");

    }
}
