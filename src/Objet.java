
import static java.lang.System.exit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juju
 */
public class Objet implements Comparable<Objet>{
    private final int poids;
    private final int valeur;
    private float rapport;
    //boolean x;
    
    public Objet(int poids,int valeur){        
        this.poids=poids;
        this.valeur=valeur;
        try{
            rapport=(float)valeur/poids;
        }catch(ArithmeticException e){
            System.out.println("Erreur : division par zéro");
            exit(1);
        }
    }
    
    int getpoids(){
        return poids;
    }
    
    int getvaleur(){
        return valeur;
    }
    
    float getrapport(){
        return rapport;
    }

    @Override
    public int compareTo(Objet t) {
        float c= t.getrapport();
        float a=this.getrapport();  
        if(a>c)
            return -1;
        else if(a<c)
            return 1;
        else
            return 0;                         
    }    
}
