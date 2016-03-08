// -*- coding: utf-8 -*-

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Barque{
    public enum Berge { NORD, SUD }
    final static int nbNainsAuNord = 6;
    final static int nbHobbitsAuNord = 0;
    final static int nbNainsAuSud = 4;
    final static int nbHobbitsAuSud = 0;
    volatile int nbPassagers = 0;
    Berge berge;
    boolean arret = true;
    public static void main(String[] args){
	Barque laBarque = new Barque();
	new Passeur(laBarque);
	for(int i=0; i<nbNainsAuNord; i++) new Nain(laBarque,Berge.NORD).setName("NN-"+i);
	for(int i=0; i<nbHobbitsAuNord; i++) new Hobbit(laBarque,Berge.NORD).setName("HN-"+i);
	for(int i=0; i<nbNainsAuSud; i++) new Nain(laBarque,Berge.SUD).setName("NS-"+i);
	for(int i=0; i<nbHobbitsAuSud; i++) new Hobbit(laBarque,Berge.SUD).setName("HS-"+i);
    }    

    /* Code associé au passeur */
    public synchronized void accosterLaBerge(Berge berge){
        this.berge=berge;
        arret=true;
    }
    public synchronized void charger(){  
        notifyAll(); 
        arret=false;
    }
    public synchronized void decharger(){        
        notifyAll();
        arret=false;
    }

    /* Code associé aux nains */
    public synchronized void embarquerUnNain(Berge origine){        
        while(!arret || nbPassagers>=4 || berge!=origine){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Barque.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        nbPassagers++;
        System.out.println(Thread.currentThread().getName()+" embarque : "+nbPassagers);
    }
    public synchronized void debarquerUnNain(Berge origine){
        if(arret && nbPassagers>0)
            nbPassagers--;
        System.out.println(Thread.currentThread().getName()+" débarque : "+nbPassagers);
        notifyAll();
    }
    /* Code associé aux hobbits */
    public synchronized void embarquerUnHobbit(Berge origine){}
    public synchronized void debarquerUnHobbit(Berge origine){}
}    


class Passeur extends Thread{
    public Barque laBarque;
    public Passeur(Barque b){
	this.laBarque = b;
	start();
    }
    public void run(){
	laBarque.accosterLaBerge(Barque.Berge.NORD);
	// Indique que la barque, initialement vide, est au NORD.
	System.out.println("PASSEUR> La barque est au NORD: montez!");
	while(true){
	    laBarque.charger();
	    System.out.println("PASSEUR> La barque est correctement chargée.");
	    System.out.println("PASSEUR> Je rame vers le SUD.");
	    try { sleep(1000); }
	    catch (InterruptedException e) {e.printStackTrace();}
	    laBarque.accosterLaBerge(Barque.Berge.SUD);
	    System.out.println("PASSEUR> La barque est au SUD: descendez!");
	    laBarque.decharger();
	    System.out.println("PASSEUR> La barque est vide: montez!");
	    laBarque.charger();
	    System.out.println("PASSEUR> La barque est correctement chargée.");
	    System.out.println("PASSEUR> Je rame vers le NORD.");
	    try { sleep(1000); }
	    catch (InterruptedException e) {e.printStackTrace();}
	    laBarque.accosterLaBerge(Barque.Berge.NORD);
	    System.out.println("PASSEUR> La barque est au NORD: descendez!");
	    laBarque.decharger();
	    System.out.println("PASSEUR> La barque est vide: montez!");
	}
    }
}



class Nain extends Thread{
    public Barque laBarque;
    public Barque.Berge origine;
    public Nain(Barque b, Barque.Berge l){
	this.laBarque = b;
	this.origine = l;
	start();
    }
    public void run(){
	Random alea = new Random();
	try {sleep(500+alea.nextInt(100)*50);}
	catch (InterruptedException e) {e.printStackTrace();}
	System.out.println(Thread.currentThread().getName()+
			   "> Je souhaite traverser.");
	laBarque.embarquerUnNain(origine);
	laBarque.debarquerUnNain(origine);
    }
}	

class Hobbit extends Thread{
    public Barque laBarque;
    public Barque.Berge origine;
    public Hobbit(Barque b, Barque.Berge l){
	this.laBarque = b;
	this.origine = l;
	start();
    }
    public void run(){
	Random alea = new Random();
	try {sleep(500+alea.nextInt(100)*50);}
	catch (InterruptedException e) {e.printStackTrace();}
	System.out.println(Thread.currentThread().getName()+
			   "> Je souhaite traverser.");
	laBarque.embarquerUnHobbit(origine);
	laBarque.debarquerUnHobbit(origine);
    }
}	

