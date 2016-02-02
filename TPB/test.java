/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author guest-hrs1jg
 */
public class test extends Thread {
    MonVerrou v=new MonVerrou();
    
    @Override
    public void run(){
        while(true){           
            v.acquerir();           
            v.relacher();          
        }
    }
    
    public static void main(String args[]){
        test T1=new test();
        test T2=new test();
        T1.start();
        T2.start();
    }
}
