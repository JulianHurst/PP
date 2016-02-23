
import java.util.logging.Level;
import java.util.logging.Logger;

// -*- coding: utf-8 -*-

public class Diner {
    public static void main(String args[]) {
	int nbSauvages = 100, nbPortions = 5;
	try {
	    nbSauvages = Integer.parseInt(args[0]);
	    nbPortions = Integer.parseInt(args[1]); }
	catch(Exception e) {
	    System.err.println("Usage: java Diner <nb de sauvages> <taille de pot>");
	    System.exit(1);
	}
	System.out.println("Il y a " + nbSauvages + " sauvages.");
	System.out.println("Le pôt contient "+ nbPortions + " portions.");
	Pot pot = new Pot(nbPortions);
	new Cuisinier(pot).start();
	for (int i = 0; i < nbSauvages; i++) new Sauvage(pot).start();
    }
}  
class Sauvage extends Thread{
    public Pot pot;
    public Sauvage(Pot pot){ this.pot = pot; }
    public void run(){
	while(true){            
	    System.out.println(getName() + ": J'ai faim!");
	    System.out.println(getName() + ": Je vais me servir dans le pôt!");
	    pot.seServir();
	    System.out.println(getName()+": Je me suis servi et je vais manger! "+pot.quantite);
	}
    }
}	
class Cuisinier extends Thread {
    public Pot pot;
    public Cuisinier(Pot pot){ this.pot = pot; }
    public void run(){
	System.out.println("Cuisinier: Je suis endormi.");        
	while(true){
	    synchronized(pot){
                while(!pot.estVide()){
                    try {
                        pot.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cuisinier.class.getName()).log(Level.SEVERE, null, ex);
                    }                 
                }
                System.out.println("Cuisinier se réveille.");
                pot.remplir();
                System.out.println("Rempli");
                pot.notifyAll();
            }
	}
    }
}	
class Pot{
    int capacite;
    volatile int quantite;
    boolean louche;
    
    Pot(int capacite){
        this.capacite=quantite=capacite;    
        louche=false;
    }
    
    boolean estVide(){
        return quantite==0;        
    }
    
    void remplir(){
        quantite=capacite;
    }
    
    synchronized void toggleLouche(){        
        louche=!louche;
        if(louche)
            System.out.println("pris louche");
        else
            System.out.println("laisse louche");
    }
    
    
    
    synchronized void hasLouche(){
        while(louche){
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Pot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        toggleLouche();
    }
    
    synchronized void seServir(){            
        if(estVide())
            this.notifyAll();
        while(estVide()){
            System.out.println(Thread.currentThread().getName()+" dors");
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Sauvage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        hasLouche();
        quantite--; 
        toggleLouche();
        this.notifyAll();
    }
}