/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.PrintWriter;
import model.Gold;
import model.Room;

/**
 *
 * @author Henrik
 */
public interface Actor {
    public int playerFight();
    public Room getRoom();
    public void setRoom(Room room);
    public PrintWriter getWriter();
    public String getName();
    public Gold getGold();
    public void addGold(Gold gold);
    public int getSizeOfGold();
}
