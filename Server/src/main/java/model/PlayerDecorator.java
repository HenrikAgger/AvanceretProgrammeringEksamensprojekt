/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Interfaces.Actor;
import java.io.PrintWriter;

/**
 *
 * @author Henrik
 */
public abstract class PlayerDecorator implements Actor {

    protected Actor player;

    public PlayerDecorator(Actor player) {
        this.player = player;
    }

    public int playerFight() {
        int retVal = this.player.playerFight();
        return retVal;
    }

    @Override
    public Room getRoom() {
        return this.player.getRoom();
    }

    @Override
    public void setRoom(Room room) {
        this.player.setRoom(room);
    }

    @Override
    public PrintWriter getWriter() {
        return this.player.getWriter();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }
}
