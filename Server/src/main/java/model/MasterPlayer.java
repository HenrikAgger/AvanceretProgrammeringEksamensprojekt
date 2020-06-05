/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Interfaces.Actor;

/**
 *
 * @author Henrik
 */
public class MasterPlayer extends PlayerDecorator {

    private String name;

    public MasterPlayer(Actor player) {
        super(player);
        this.name = "Master_" + player.getName();
    }

    @Override
    public int playerFight() {
        int retVal = 0;
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        retVal = d1.getValue() + d2.getValue();
        return retVal;
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
        return player.getSizeOfGold();
    }

    @Override
    public String getName() {
        return this.name;
    }

}
