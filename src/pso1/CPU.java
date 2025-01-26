/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso1;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SebasBD
 */
public class CPU extends Thread{
    private Proceso currentProcess;
    
    
     @Override
    
    public void run (){
        while(true){
            
            try {
                execute();
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void assingProcess(Proceso process){
        this.currentProcess = process;
        System.out.println("Proceso asignado: " + process.getName());
    }
    
    public void execute() {
        if (currentProcess != null) {
            currentProcess.run();
        } else {
            System.out.println("No hay proceso asignado.");
        }
    }
}
