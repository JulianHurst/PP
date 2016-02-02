// -*- coding: utf-8 -*-

import java.util.ArrayList;

public class SeptNains{
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
    private volatile boolean libre = true;
    private volatile ArrayList<String> al = new ArrayList<>();
              // Initialement, Blanche-Neige est libre.
    public synchronized void requerir(){
	System.out.println(Thread.currentThread().getName()
			   + " veut la ressource");
        al.add(Thread.currentThread().getName());        
    }

    public synchronized void acceder(){
	while( ! libre || !al.get(0).equals(Thread.currentThread().getName()))
	    // Le nain s'endort sur le moniteur Blanche-Neige.            
	    try { wait(); }
	    catch (InterruptedException e) {e.printStackTrace();}
	libre = false;
	System.out.println("\t" + Thread.currentThread().getName()
			   + " accède à la ressource.");        
    }

    public synchronized void relacher(){
	System.out.println("\t\t" + Thread.currentThread().getName()
			   + " relâche la ressource.");
	libre = true;
	notifyAll();
        al.remove(Thread.currentThread().getName());
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
