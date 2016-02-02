
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author guest-hrs1jg
 */
public class MonVerrou extends Thread {
    volatile boolean verrou;    
    
    MonVerrou(){
        verrou=false;        
    }
    
    
    
    public synchronized void acquerir(){
        while(verrou)
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MonVerrou.class.getName()).log(Level.SEVERE, null, ex);
            }                
        verrou=true;    
        System.out.println(Thread.currentThread().getName()+" prends");
    }
    
    public synchronized void relacher(){
        verrou=false;
        notifyAll();
        System.out.println(Thread.currentThread().getName()+" relâche");
    }        
}
