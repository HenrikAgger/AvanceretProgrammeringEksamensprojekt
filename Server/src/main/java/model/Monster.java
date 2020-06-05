/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Henrik
 */
public class Monster {

    private int d;

    private String monsterName;

    public Monster(String monsterName) {
        this.monsterName = monsterName;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public int monsterFight(Dice dice) {
        d = dice.roll();
        return d;
    }

}
