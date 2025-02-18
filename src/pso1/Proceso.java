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
public class Proceso{
    private int process_id;
    private String name;
    private String type;
    private int pc = 0;
    private int mar = 0;
    private String status = "Ready";
    private int instructions;
    private int executeInstructions = 0; //SRT
    private int priority;
    private int cycle = 0;
    private Integer interrupTime;
    private Integer interrupDuration;
    private int cpuId;
    private int cicloEnqueCola = -1; //HRRN
    private static int contadorID = 1;
    private static boolean taken = false;
    private int cyclesExecuteFromLastBlock = 0;
    
    public Proceso (String name, String type, int instructions, Integer interrupTime, Integer interrupDuration){
        if(name == null || name.isEmpty() ){
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        if(instructions <= 0){
            throw new IllegalArgumentException("La cantidad de instrucciones debe ser mayor que 0.");
        }
        if (type == null || (!type.equals("CPU bound") && !type.equals("I/O bound"))) {
            throw new IllegalArgumentException("El tipo debe ser 'CPU bound' o 'I/O bound'.");
        }
        
        if (type.equals("I/O bound")) {
            if (interrupTime == null || interrupDuration == null) {
                throw new IllegalArgumentException("Ciclos para generar y satisfacer excepción son obligatorios para procesos I/O bound.");
            }
            this.interrupTime = interrupTime;
            this.interrupDuration = interrupDuration;
        }else{
            this.interrupTime = 0;
            this.interrupDuration = 0;
        }
        this.process_id = contadorID++;
        this.name = name;
        this.instructions = instructions;
        this.priority = priority;
        this.cpuId = 0;
        this.type = type;
        
    }
    
    // Calcula el tiempo restante para poilitica SRT
    public int getRemainingTime() {
    return instructions - getExecuteInstructions();
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

    /**
     * @return the cycle
     */
    public int getCycle() {
        return cycle;
    }

    /**
     * @param cycle the cycle to set
     */
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    /**
     * @return the interrupTime
     */
    public int getInterrupTime() {
        return interrupTime;
    }

    /**
     * @param interrupTime the interrupTime to set
     */
    public void setInterrupTime(int interrupTime) {
        this.interrupTime = interrupTime;
    }

    /**
     * @return the interrupDuration
     */
    public int getInterrupDuration() {
        return interrupDuration;
    }

    /**
     * @param interrupDuration the interrupDuration to set
     */
    public void setInterrupDuration(int interrupDuration) {
        this.interrupDuration = interrupDuration;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return the cpuId
     */
    public int getCpuId() {
        return cpuId;
    }

    /**
     * @param cpuId the cpuId to set
     */
    public void setCpuId(int cpuId) {
        this.cpuId = cpuId;
    }

    /**
     * @return the taken
     */
    public static boolean isTaken() {
        return taken;
    }

    /**
     * @param aTaken the taken to set
     */
    public static void setTaken(boolean aTaken) {
        taken = aTaken;
    }

    /**
     * @return the cyclesExecuteFromLastBlock
     */
    public int getCyclesExecuteFromLastBlock() {
        return cyclesExecuteFromLastBlock;
    }

    /**
     * @param cyclesExecuteFromLastBlock the cyclesExecuteFromLastBlock to set
     */
    public void setCyclesExecuteFromLastBlock(int cyclesExecuteFromLastBlock) {
        this.cyclesExecuteFromLastBlock = cyclesExecuteFromLastBlock;
    }

    /**
     * @return the executeInstructions
     */
    public int getExecuteInstructions() {
        return executeInstructions;
    }

    /**
     * @param executeInstructions the executeInstructions to set
     */
    public void setExecuteInstructions(int executeInstructions) {
        this.executeInstructions = executeInstructions;
    }

    /**
     * @return the cicloEnqueCola
     */
    public int getCicloEnqueCola() {
        return cicloEnqueCola;
    }

    /**
     * @param cicloEnqueCola the cicloEnqueCola to set
     */
    public void setCicloEnqueCola(int cicloEnqueCola) {
        this.cicloEnqueCola = cicloEnqueCola;
    }

    
    
    
}
