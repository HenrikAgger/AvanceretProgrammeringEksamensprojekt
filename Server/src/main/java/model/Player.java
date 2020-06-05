/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Interfaces.Actor;
import java.io.PrintWriter;
import java.util.Stack;

/**
 *
 * @author Henrik
 */
public class Player implements Actor {

    private int d1;
    private int d2;
    private Stack<Gold> golds;
    private String name;
    private PrintWriter out;
    private Room room;

    public Player(String name) {
        this.name = name;
        this.golds = new Stack<>();
    }

    public Player(String name, PrintWriter out) {
        this.name = name;
        this.out = out;
        this.golds = new Stack<>();
    }

    public void setWriter(PrintWriter out) {
        if (this.out != null) {
            throw new RuntimeException("Writer already set");
        }
        if (out == null) {
            throw new IllegalArgumentException("Writer must be not-null");
        }
        this.out = out;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public PrintWriter getWriter() {
        return this.out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrintWriter getOut() {
        return out;
    }

    public int playerFight(Dice dice) {
        d1 = dice.roll();
        d2 = dice.roll();
        return d1 + d2;
    }

    public void addGold(Gold gold) {
        golds.push(gold);
    }

    public Gold getGold() {
        Gold retValGold = null;
        retValGold = golds.pop();
        return retValGold;
    }

    public int getSizeOfGold() {
        int retVal = 0;
        if (golds.size() < 1) {
            return retVal;
        } else {
            return golds.size();
        }
    }

    @Override
    public int playerFight() {
        int retVal = 0;
        return retVal;
    }
}
