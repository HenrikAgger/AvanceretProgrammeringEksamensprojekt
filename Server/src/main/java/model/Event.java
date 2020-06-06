/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.PrintWriter;

/**
 *
 * @author Henrik
 */
public class Event {
    
    protected PrintWriter[] writers;
    protected String output;

    public Event(PrintWriter[] w, String s) {
        this.writers = w;
        this.output = s;
    }

    public Event(String s) {
        this.output = s;
    }

    public String toString() {
        return this.output;
    }

    public PrintWriter[] getWriters() {
        return this.writers;
    }
}
