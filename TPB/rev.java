// -*- coding: utf-8 -*-

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class rev{
    public static void main(String[] args){
	int nbNains = 7;
	
	BlancheNeige bn = new BlancheNeige();
	String nom [] = {"Simplet", "Dormeur",  "Atchoum", "Joyeux",
			 "Grincheux", "Prof", "Timide"};
	Nain nain [] = new Nain [nbNains];
	for(int i=0; i<nbNains; i++)
	    nain[i] = new Nain(nom[i], bn);
	for(int i=0; i<nbNains; i++)
	    nain[i].start();
    }
}    

class BlancheNeige{
    private volatile int libre = 0;
    private volatile boolean premierquit = false;
    private volatile boolean firstq = false;
    private volatile ArrayList<String> al = new ArrayList<>();
              // Initialement, Blanche-Neige est libre.
    public synchronized void requerir(){
	System.out.println(Thread.currentThread().getName()
			   + " veut la ressource");
        al.add(Thread.currentThread().getName());
        //System.out.println(al);
    }

    public synchronized void acceder(){
	while( libre==2 || (!al.get(0).equals(Thread.currentThread().getName()) && !al.get(1).equals(Thread.currentThread().getName())) || premierquit)
	    // Le nain s'endort sur le moniteur Blanche-Neige.            
	    try { wait(); }
	    catch (InterruptedException e) {e.printStackTrace();}
	libre++;
        if(libre==2)
            premierquit=true;
	System.out.println("\t" + Thread.currentThread().getName()
			   + " accède à la ressource.");
        //notifyAll();
    }

    public synchronized void relacher(){
        while(libre!=2 && !firstq){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(BlancheNeige.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //notifyAll();
	System.out.println("\t\t" + Thread.currentThread().getName()
			   + " relâche la ressource.");	
        firstq=!firstq;
        libre--;         
        if(libre==0)
            premierquit=false;        
        al.remove(Thread.currentThread().getName());
        notifyAll();
    }
}

class Nain extends Thread{
    public BlancheNeige bn;
    public Nain(String nom, BlancheNeige bn){
	this.setName(nom);
	this.bn = bn;
    }
    public void run(){
	while(true){
	    bn.requerir();
	    bn.acceder();
	    try {sleep(1000);}
	    catch (InterruptedException e) {e.printStackTrace();}
	    bn.relacher();
	}
    }	
}

/*
$ javac -encoding ISO-8859-1 SeptNains.java
$ java SeptNains 
Simplet veut la ressource
        Simplet accède à la ressource.
Atchoum veut la ressource
Timide veut la ressource
Joyeux veut la ressource
Grincheux veut la ressource
Dormeur veut la ressource
Prof veut la ressource
                Simplet relâche la ressource.
Simplet veut la ressource
        Simplet accède à la ressource.
                Simplet relâche la ressource.
Simplet veut la ressource
        Simplet accède à la ressource.
                Simplet relâche la ressource.
Simplet veut la ressource
        Simplet accède à la ressource.
                Simplet relâche la ressource.
Simplet veut la ressource
        Simplet accède à la ressource.
                Simplet relâche la ressource.
Simplet veut la ressource
        Simplet accède à la ressource.
*/
