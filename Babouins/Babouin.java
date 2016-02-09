
import java.util.logging.Level;
import java.util.logging.Logger;

// -*- coding: utf-8 -*-

enum Cote { EST, OUEST }                   // Le canyon possède un côté EST et un côté OUEST

class Babouin extends Thread{
  private static int numeroSuivant = 0;    // Compteur partagé par tous les babouins
  private int numero;                      // Numéro du babouin
  private Corde corde;                     // Corde utilisée par le babouin
  private Cote origine;                    // Côté du canyon où apparaît le babouin: EST ou OUEST
  Babouin(Corde corde, Cote origine){      // Constructeur de la classe Babouin
    this.corde = corde;                    // Chaque babouin peut utiliser la corde
    this.origine = origine;                // Chaque babouin apparaît d'un côté précis du canyon
    numero = ++numeroSuivant;              // Chaque babouin possède un numéro distinct
  }
  public void run(){
    System.out.println("Le babouin " + numero + " arrive sur le côté " + origine + " du canyon.");
    corde.saisir(origine);                 // Pour traverser, le babouin saisit la corde
    System.out.println("Le babouin " + numero +
                        " commence à traverser sur la corde en partant de l'" + origine + ".");
    try { sleep(5000); } catch(InterruptedException e){} // La traversée ne dure que 5 secondes
    System.out.println("Le babouin " + numero + " a terminé sa traversée.");
    corde.lacher(origine);                 // Arrivé de l'autre côté, le babouin lâche la corde
    System.out.println("Le babouin " + numero + " a lâché la corde et s'en va.");
  }
  public static void main(String[] args){ 
    Corde corde = new Corde();    // La corde relie les deux côtés du canyon
    for (int i = 1; i < 20; i++){
      try { Thread.sleep(500); } catch(InterruptedException e){}		    
      if (Math.random() >= 0.5){
        new Babouin(corde, Cote.EST).start();    // Création d'un babouin à l'est du canyon
      } else {
        new Babouin(corde, Cote.OUEST).start();  // Création d'un babouin à l'ouest du canyon
      }
    } // Une vingtaine de babouins sont répartis sur les deux côtés du canyon
  }
}

class Corde {
    volatile int libre;
    volatile Cote direction;
    volatile int attenteE;
    volatile int attenteO;
    
    Corde(){
        libre=0;
        direction=Cote.OUEST;
        attenteE=attenteO=0;
    }
    
    public synchronized void saisir(Cote origine){
        while(libre==5 || origine!=direction || attenteE>0){
            try {
                if(origine!=direction && libre>0){
                    if(origine==Cote.EST)
                        attenteE++;
                    else
                        attenteO++;
                }                    
                wait();
                //System.out.println(libre+" "+direction+" "+attente);
            } catch (InterruptedException ex) {
                Logger.getLogger(Corde.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        direction=origine;
        libre++;                
    }

    public synchronized void lacher(Cote origine){
        libre--;
        if(libre==0){
            if(direction==Cote.EST)                        
                direction=Cote.OUEST;
            else
                direction=Cote.EST;
            attente--;
        }
        System.out.println(direction+" "+attente);
        notifyAll();
    }
}
