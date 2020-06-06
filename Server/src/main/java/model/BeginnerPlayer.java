/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Interfaces.Actor;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author Henrik
 */
public class BeginnerPlayer extends PlayerDecorator {

    private String name;
    private Random rd;

    public BeginnerPlayer(Actor player) {
        super(player);
        this.name = "Beginner_" + player.getName();
        this.rd = new Random();
    }

    @Override
    public int playerFight() {
        int retVal = 0;
        retVal = rd.nextInt(3);
        return retVal;
    }

    @Override
    public Room getRoom() {
        return player.getRoom();
    }

    @Override
    public void setRoom(Room room) {
        player.setRoom(room);
    }

    @Override
    public PrintWriter getWriter() {
        return player.getWriter();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Gold getGold() {
        return this.player.getGold();
    }

    @Override
    public void addGold(Gold gold) {
        this.player.addGold(gold);
    }

    @Override
    public int getSizeOfGold() {
        return this.player.getSizeOfGold();
    }
}
