/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import model.Dice;
import model.Direction;
import model.Event;
import model.Gold;
import model.Monster;
import model.Player;
import model.Room;
import worker.Producer;
import worker.Consumer;
import worker.MonsterProducer;

/**
 *
 * @author Henrik
 */
public class Controller {

    private Player player;
    private Monster monster;
    ArrayBlockingQueue<Event> events;
    ServerSocket ss;
    Socket socket;
    Scanner scan = new Scanner(System.in);
    ArrayList<Room> rooms = new ArrayList<>();
    Dice dice;
    Gold gold;
    Random r = new Random();
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    ArrayList<Player> players = new ArrayList<Player>();

    /**
     *
     * @param player
     * @param monster
     * @param events
     */
    public Controller(Player player, Monster monster, ArrayBlockingQueue<Event> events) throws IOException {
        this.player = player;
        this.monster = monster;
        this.events = events;
        this.ss = new ServerSocket(5555);
    }

    public Controller(ArrayBlockingQueue<Event> events, Monster monster) throws IOException {
        this.events = events;
        this.monster = new Monster("Troll");
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
        Room room1 = new Room("Entrence", "Entrence to the caves");
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
    }

    public void runGame() throws IOException {
        // Now initializing clienthandler_producer
        System.out.println("Waiting");
        Socket s = ss.accept();
        Producer producer = new Producer(s, this);
        int choice = 0;
        boolean notempty = true;

        Room room1 = new Room(" Entrence", "Entrence to the caves");
        Room room2 = new Room(" Skeleton room", "You are in a room with a lot of old skeletons");
        Room room3 = new Room(" Rat room", "You are in a room with a lot of rats");
        Room room4 = new Room(" Spider room", "You are in a room with a lot of spiderwebs");
        Room room5 = new Room(" Exit room", "You are in a room where you can see some daylight");
        Room room6 = new Room(" Hurray you won", "People celebrate your return");

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

        System.out.println("Socket from client");
        Consumer consumer = new Consumer(events);
        MonsterProducer monsterProducer = new MonsterProducer(this);

        consumer.start();
        producer.start();
        monsterProducer.start();

//        while (notempty) {
//            System.out.println("1 Fight monster");
//            System.out.println("2 Quit game");
//            int input = scan.nextInt();
//            consumer.readFromQueue();
//            switch (input) {
//                case 1:
//                    fightMonster();
//                    break;
//                case 2:
//                    notempty = false;
//                    break;
//            }
//        }
    }

    public synchronized Player registerPlayer(String name, PrintWriter printWriter) {
        Player player = new Player(name, printWriter);
        players.add(player);
        player.setRoom(rooms.get(0));
        return player;
    }

    public void processMessage(Player player, String message) {
        String actionToken = "";
        String targetToken = "";
        System.out.println("Player ");
        player.getWriter().println("Hi you wrote " + message);
        String[] lineAr = message.split(" ");
        actionToken = lineAr[0];
        String[] targetTokenArray = Arrays.copyOfRange(lineAr, 1, lineAr.length);

        targetToken = String.join(" ", targetTokenArray);
        // process input fx "fight monster" 
        // process input fx "leave room1"
        // process input fx "leave NORTH"
        // process input fx "pick up gun"
        // process input fx "inventory"
        // process input fx "fight monster"

        // process dvs tokenize string
        switch (actionToken) {
            case "MOVE":
                move(targetToken, player);
                return;
            case "FIGHT":
                // creates message for fighting a monster
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

    public void move(String targetToken, Player player) {
        player.getWriter().println("Hi move " + targetToken);
        // flyt spiller til det rum der ligger i direction NORTH
        // Hentes fra players nuværende rooms hashtable
        Room room = player.getRoom();
        Room goToRoom = room.to(Direction.valueOf(targetToken));
        player.setRoom(goToRoom);
        player.getWriter().println("You are in a new room called" + goToRoom.getName());

    }

    public void fightMonster(String targetToken, Player player) {
        gold = new Gold();
        dice = new Dice();
        Monster monster = monsters.get(r.nextInt(monsters.size()));
        player.getWriter().println("Hi into fight " + targetToken);
        if (player.playerFight(dice) > monster.monsterFight(dice)) {
            System.out.println("You are fighting a " + monster.getMonsterName());
            System.out.println("Player wins");
            System.out.println("You won " + player.playerGold(gold) + " pices of gold");
            System.out.println("Total amount of gold: " + player.playerGoldTotal(gold));
        } else {
            System.out.println(monster.getMonsterName() + " wins, RIP");

        }

        // TODO: Monsteret har en terning player har 2
        // Monster kaster terning spiller kaster 2 terninger
        // Den med højeste værdi vinder
        // At vinde over monster giver 1 til 10 point
        // Den første som når exit får 15 point. (MANGLER)
        // Implementer 1 terning monster : 2 terninger spiller
    }

    public void fightPlayer(String targetToken, Player player) {
        gold = new Gold();
        dice = new Dice();
        Player enemy = player.getRoom().getRandomPlayer();
        
        if (enemy==null){
            System.out.println("No enemy");
            return;
        } 
        enemy.getWriter().println("Hi you are being attached " + targetToken);
        if (enemy.playerFight(dice) > player.playerFight(dice)) {
            System.out.println("You are fighting a " + player.getName());
            System.out.println("Enemy wins");
            System.out.println("You won " + enemy.playerGold(gold) + " pices of gold");
            System.out.println("Total amount of gold: " + enemy.playerGoldTotal(gold));
        } else {
            System.out.println(player.getName() + " wins, RIP");

        }
    }

    public void exitRoom(String targetToken, Player player) {
        player.getWriter().println("Hi exiting the room " + targetToken);
        System.out.println("You have exited a room");
    }

    public void north(String targetToken, Player player) {
        player.getWriter().println("Hi you are going north" + targetToken);
        System.out.println("You have mooved north");
    }

    public void east(String targetToken, Player player) {
        player.getWriter().println("Hi you are going east" + targetToken);
        System.out.println("You have mooved east");
    }

    public void south(String targetToken, Player player) {
        player.getWriter().println("Hi you are going south" + targetToken);
        System.out.println("You have mooved south");
    }

    public void west(String targetToken, Player player) {
        player.getWriter().println("Hi you are going west" + targetToken);
        System.out.println("You have mooved west");
    }

    public void gun(String targetToken, Player player) {
        player.getWriter().println("Hi you pick up a gun");
        System.out.println("You have picked up a gun");
    }

    public void inventory(String targetToken, Player player) {
        player.getWriter().println("You have following items in your inventory" + targetToken);
        System.out.println("You have following items");
    }

    public void fightMonster() {
        System.out.println("Fighting monster");

    }

}
