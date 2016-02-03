
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juju
 */
public class BranchandBound {
    private int n,W;
    List<Objet> obj = new ArrayList<>();
    boolean x[];
    boolean best[];
    int somw;
    float somv;
    int Vmax,Vtmp;

    public BranchandBound(int W){
        n=6;
        this.W=W;
        somw=W;
        somv=0;
        Vmax=Vtmp=0;
        x=new boolean[n];
        for(int i=0;i<n;i++)
            x[i]=true;
        obj.add(new Objet(4,8));
        obj.add(new Objet(6,6));
        obj.add(new Objet(8,4));               
        obj.add(new Objet(10,4));
        obj.add(new Objet(8,2));
        obj.add(new Objet(9,3));
    }

    public BranchandBound(String file){
        try {
            readfic(file);
            x=new boolean[n];
            best=new boolean[n];
            for(int i=0;i<n;i++)
                x[i]=false;
        } catch (IOException ex) {
            Logger.getLogger(BranchandBound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void readfic(String file) throws FileNotFoundException, IOException{
        obj.clear();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        W=Integer.parseInt(br.readLine());
        while ((line = br.readLine()) != null) {
            String p=line.split(" ")[0];
            String v=line.split(" ")[1];
            obj.add(new Objet(Integer.parseInt(p),Integer.parseInt(v)));
        }
        br.close();
        n=obj.size();
    }

    public float sol(){
        somw=W;
        float somva=0;
        //boolean frac=false;
        float fract;
        for(int i=0;i<obj.size() && somw!=0;i++){
            if(obj.get(i).getpoids()<=somw && x[i]){
                somw-=obj.get(i).getpoids();
                somva+=obj.get(i).getvaleur();
                //System.out.println("i: "+i+" w"+i+": "+obj.get(i).getpoids());
            }
            else{
                /*for(int j=1;j<obj.get(i).getpoids() && !frac;j++)
                    if((obj.get(i).getpoids()/j)<=somw && x[i]){
                        somw-=(obj.get(i).getpoids()/j);
                        somva+=obj.get(i).getvaleur()/j;
                        //System.out.println("i: "+i+" w"+i+": "+obj.get(i).getpoids()+" j: "+j+" w"+i+"/"+j+": "+(obj.get(i).getpoids()/j)+" frac: "+(float)1/j);
                        frac=true;
                    }*/
                if(x[i]){
                    fract=(float)obj.get(i).getpoids()/somw;
                    //System.out.println("fract : "+fract+" "+somw+" "+obj.get(i).getpoids());
                    //System.out.println("fract : "+fract);
                    somw-=(obj.get(i).getpoids()/fract);
                    somva+=((float)obj.get(i).getvaleur())/fract;
                }
            }
            //frac=false;
        }
        //System.out.println("Valeur totale : "+somva);

        return somva;
    }

    public void combrec(int nbfils){
        float somvtmp;        
        somvtmp=sol();
        if(somvtmp>somv){
            somv=somvtmp;
            System.arraycopy(x, 0, best, 0, n);
        }
        if(nbfils==0 || somw==0){            
            return;
        }
        for(int i=0;i<n;i++){
            if(!x[i]){
                x[i]=true;
                combrec(nbfils-1);
                x[i]=false;                
            }
        }
    }
    
    public void combrecbin(int i){        
        float somvtmp;        
        somvtmp=sol();
        if(somvtmp>somv){
            somv=somvtmp;
            System.arraycopy(x, 0, best, 0, n);
        }
        if(i==n || somw==0){            
            return;
        }        
        x[i]=true;
        System.out.println("obj "+i+" pris");        
        combrecbin(i+1);
        x[i]=false; 
        System.out.println("obj "+i+" retiré");        
        combrecbin(i+1);
    }
    
    /*public float combrecbinssarb(int i){        
        float somvtmp;
        float val=0;
        somvtmp=sol();
        if(somvtmp>val){
            val=somvtmp;
            //System.arraycopy(x, 0, best, 0, n);
        }
        if(i==n || somw==0){            
            return val;
        }                    
        x[i]=true;
        //System.out.println("obj "+i+" pris");        
        val=combrecbinssarb(i+1);
        x[i]=false; 
        //System.out.println("obj "+i+" retiré");        
        val=combrecbinssarb(i+1);
        return val;
    }*/

    public List<Objet> copy(List<Objet> x){
        List<Objet> tmp= new ArrayList<>();
        for (Objet x1 : x) {
            tmp.add(x1);
        }
        return tmp;
    }

    public void reinit(){
        for(int i=0;i<n;i++)
            x[i]=false;
    }

    public void print(){
        for(int i=0;i<n;i++){
            System.out.println("i : "+i+" "+obj.get(i).getpoids()+" "+obj.get(i).getvaleur());
        }

    }

    public void printx(boolean x[]){
        for(int i=0;i<n;i++)
            System.out.println(x[i]);
    }

    public static void main(String args[]){
        BranchandBound B=new BranchandBound(14);
        Collections.sort(B.obj);
        for (Objet obj1 : B.obj)
            System.out.println(obj1.getrapport());
        System.out.println();
        System.out.println("1. valeur trouvée : "+B.sol()+"\n");
        if(args.length==1){
            BranchandBound B2=new BranchandBound(args[0]);
            //B2.combrec(B2.obj.size());
            //Collections.sort(B2.obj);
            B2.combrecbin(0);
            System.out.println("meilleure valeur : "+B2.somv);
            B2.printx(B2.best);
        }
    }
}
