// -*- coding: utf-8 -*-

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mandelbrot1 implements Runnable{
    final static Color noir =  new Color(0, 0, 0);
    final static Color blanc =  new Color(255, 255, 255);
    static int taille = 2000;
    static volatile Picture image = new Picture(taille, taille);   
    double xc;
    double yc; // Le point (xc,yc) est le centre de l'image
    int xoffset,yoffset;
    private volatile boolean libre = true;
    private volatile int line = 0;

    Mandelbrot1(double xc,double yc,int xoffset,int yoffset){
        this.xc=xc;
        this.yc=yc;
        this.xoffset=xoffset;
        this.yoffset=yoffset;
    }
    
    
    public static boolean mandelbrot(double a, double b, int max) {
        double x = 0;
	double y = 0;
	for (int t = 0; t < max; t++) {
            if (x*x + y*y > 4.0) return false;
            double nx = x*x - y*y + a;
	    double ny = 2*x*y + b;
	    x = nx;
	    y = ny;
        }
        return true;
    }
    
    @Override
    public void run(){  
		while(line!=taille)                             
			calculimg();			
    }
    
    public synchronized void calculimg(){
		while(!libre){
			try{wait();}
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		libre = false;
		double region = 2;
        int max = 500;                 
        for (int j = 0; j < taille; j++) {
                double a = xc - region/2 + region*line/taille;
                double b = yc - region/2 + region*j/taille;
		// Le pixel (i,j) correspond au point (a,b)
		if (mandelbrot(a, b, max))
		    image.set(line, j, noir);
		else
		    image.set(line, j, blanc); 
		// La fonction mandelbrot(a, b, max) determine si le point (a,b) est noir
        }       
        line++;
        libre=true;
        notifyAll();
	}

    public static void main(String[] args)  {
        double xc   = -.5 ;
        double yc   = 0 ; // Le point (xc,yc) est le centre de l'image
        double region = 2;
	// La région étudiée est un carré de côté égal à 2.
	// Elle s'étend du point (xc-1,yc-1) au point (xc+1,yc+1)
	// c'est-à-dire du point (-1.5,-1) en bas à gauche au
	// point (0.5,1) en haut à droite
        //int taille = 2000;   // nombre de pixels par ligne (et par colonne)
	// Il y a donc taille*taille pixels à déterminer
        //Picture image = new Picture(taille, taille);
        //int max = 500; 
	// C'est le nombre maximum d'itérations pour le calcul d'un pixel
	final long startTime = System.nanoTime();
	final long endTime;        
        Thread t1=new Thread(new Mandelbrot1(-.5,0,0,0));
        Thread t2=new Thread(new Mandelbrot1((-.5+1),0,taille/2,0));
        Thread t3=new Thread(new Mandelbrot1(-.5,1,0,taille/2));
        Thread t4=new Thread(new Mandelbrot1((-.5+1),1,taille/2,taille/2));
        t1.start();
        t2.start();
        t3.start();
        t4.start(); 
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Mandelbrot.class.getName()).log(Level.SEVERE, null, ex);
        }        
	endTime = System.nanoTime();
	final long duree = (endTime - startTime) / 1000000 ;
	System.out.println("Durée = " + (long) duree + " ms.");        
        image.show();
	}
}


/* Execution sur un MacBook pro dualcore
> javac Mandelbrot.java
> java Mandelbrot
Duree = 15703 ms.
*/
