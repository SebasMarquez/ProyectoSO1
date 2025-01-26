/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SebasBD
 */
public class Proceso extends Thread{
    private int process_id;
    private String name;
    private Boolean is_io_bound;
    private int pc = 0;
    private int mar = 0;
    private String status = "Ready";
    private int instructions;
    
    public Proceso (int process_id, String name, Boolean is_io_bound, int instructions){
        this.process_id = process_id;
        this.name = name;
        this.is_io_bound = is_io_bound;
        this.instructions = instructions;
    }
    
     @Override
    
    public void run (){
        while(true){
            
            try {
                executeInstruction();
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        
    public void executeInstruction(){
        while(pc < instructions) {
            // Simular la ejecución de una instrucción
            System.out.println("Ejecutando instrucción " + pc + " del proceso " + name);
            mar = pc; // Actualizar el MAR con la dirección de la instrucción actual
            pc++; // Incrementar el PC para la siguiente instrucción
        }
            status = "Completed"; // Cambiar el estado si se completan todas las instrucciones
            System.out.println("Proceso " + name + " completado.");
    }
    
    

    /**
     * @return the process_id
     */
    public int getProcess_id() {
        return process_id;
    }

    /**
     * @param process_id the process_id to set
     */
    public void setProcess_id(int process_id) {
        this.process_id = process_id;
    }

    /**
     * @return the is_io_bound
     */
    public Boolean getIs_io_bound() {
        return is_io_bound;
    }

    /**
     * @param is_io_bound the is_io_bound to set
     */
    public void setIs_io_bound(Boolean is_io_bound) {
        this.is_io_bound = is_io_bound;
    }

    /**
     * @return the pc
     */
    public int getPc() {
        return pc;
    }

    /**
     * @param pc the pc to set
     */
    public void setPc(int pc) {
        this.pc = pc;
    }

    /**
     * @return the mar
     */
    public int getMar() {
        return mar;
    }

    /**
     * @param mar the mar to set
     */
    public void setMar(int mar) {
        this.mar = mar;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the instructions
     */
    public int getInstructions() {
        return instructions;
    }

    /**
     * @param instructions the instructions to set
     */
    public void setInstructions(int instructions) {
        this.instructions = instructions;
    }

    
    
    
}
