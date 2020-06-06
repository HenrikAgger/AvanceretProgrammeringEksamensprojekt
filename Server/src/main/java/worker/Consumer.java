/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worker;

import java.util.concurrent.ArrayBlockingQueue;
import model.Event;

/**
 *
 * @author Henrik
 */
public class Consumer extends Thread {

    private ArrayBlockingQueue<Event> queue;

    public Consumer(ArrayBlockingQueue<Event> queue) {
        this.queue = queue;
    }

    public Event readFromQueue() {
        Event event = null;
        try {
            event = queue.take();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    public void run() {
        while (true) {
            try {
                Event event = null;
                sleep(10000);
                event = readFromQueue();
                System.out.println(event + " print from consumer");
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
}
