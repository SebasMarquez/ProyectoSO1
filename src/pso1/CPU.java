/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso1;

import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pso1.PSO1.colaReady;
import static pso1.PSO1.colaBlock;
import static pso1.PSO1.colaFinish;
import static pso1.PSO1.mainPage;
import static pso1.PSO1.politicaActual;

/**
 *
 * @author SebasBD
 */
public class CPU extends Thread{
    private Proceso currentProcess;
    
    private boolean active;
    private int id;
    //private static int contadorId = 1;
    
    public CPU (int id){
        this.currentProcess = null;
        this.active = true;
        this.id = id;
    }

    /**
     * @return the currentProcess
     */
    public Proceso getCurrentProcess() {
        return currentProcess;
    }

    /**
     * @param currentProcess the currentProcess to set
     */
    public void setProcess(Proceso currentProcess) {
        if(active){
           this.currentProcess = currentProcess; 
           
           if (id == 1){
               PSO1.mainPage.cpuPane1.actualizarProceso();
               currentProcess.setCpuId(id);
           }else if(id == 2){
               PSO1.mainPage.cpuPane2.actualizarProceso();
               currentProcess.setCpuId(id);
           }else{
               PSO1.mainPage.cpuPane3.actualizarProceso();
               currentProcess.setCpuId(id);
           }
        } else {
            System.out.println("Error: No es posible asignar un proceso al CPU: " + id + "porque esta desactivado");
        } 
        
    }
    
    public void setFreeProcess(){
        if (id == 1){
               PSO1.mainPage.cpuPane1.freeCPU();
           }else if(id == 2){
               PSO1.mainPage.cpuPane2.freeCPU();
           }else{
               PSO1.mainPage.cpuPane3.freeCPU();
           }
        
        currentProcess = null;
    }
    
    @Override
    public void run(){
        int quantumTiempo = 5; //Rodaja de tiempo Round Robin
        while (active){
            try{
                PSO1.mutex.acquire();
                if(!colaReady.isEmpty()){
                    
                    currentProcess = colaReady.searchNotTaken();
                    if (currentProcess != null){
                        currentProcess.setTaken(true); 
                    }
                    
                    //System.out.println("CPU" + id + "Tomo el proceso: " + currentProcess.getName());
                } else {
                    //System.out.println("La cola se encuentra vacia. Waiting....");
                }
                PSO1.mutex.release();
                
                
                if(currentProcess != null){
                    
                    Proceso aux = currentProcess;
                    Proceso SO = new Proceso("SO", "CPU bound", 3, 0, 0);
                    SO.setProcess_id(0);
                    SO.setStatus("Running");
                    setProcess(SO);
                    
                    System.out.println("CPU " + id + " ejecutando el SO por 3 ciclos...");
                    
                    for(int i = 0; i < 3; i++){
                        int cycleDuration = PSO1.mainPage.getCycleDuration();
                        Thread.sleep(cycleDuration * 1000);
                        SO.setPc(SO.getPc() + 1);
                        SO.setMar(SO.getMar() + 1);
                        setProcess(getCurrentProcess());
                        
                    }
                    
                    setFreeProcess();
                    
                    aux.setStatus("Running");
                    colaReady.remove(aux);
                    aux.setTaken(false);
                    setProcess(aux);
                    
                    int cycleDuration = PSO1.mainPage.getCycleDuration();
                     
                     switch(PSO1.politicaActual){
                        case "Round Robin": 
                            
                            quantumTiempo = 5;
                            while(currentProcess.getInstructions() > 0){
                                 
                                Thread.sleep(cycleDuration * 1000);
                                 
                                currentProcess.setInstructions(currentProcess.getInstructions() - 1);
                                currentProcess.setExecuteInstructions(currentProcess.getExecuteInstructions() + 1);
                                currentProcess.setPc(currentProcess.getPc() + 1);
                                currentProcess.setMar(currentProcess.getMar() +1);
                                setProcess(getCurrentProcess());
                                
                                //I/O bound proceso
                                if (currentProcess.getType().equalsIgnoreCase("I/O bound")){
                                    currentProcess.setCyclesExecuteFromLastBlock(currentProcess.getCyclesExecuteFromLastBlock() +1);
               
                                    if(currentProcess.getCyclesExecuteFromLastBlock() > currentProcess.getInterrupTime()){
                                        currentProcess.setStatus("Blocked");
                                        //Aqui pensar logica para colocar SO en el CPU
                                        currentProcess.setCyclesExecuteFromLastBlock(0);
                                        PSO1.colaBlock.encolar(currentProcess);
                                        
                                        setFreeProcess();
                                        break;
                                    }
                                }
                                
                                quantumTiempo--;
                                // Si cantidadInstrucciones llega a 0 antes de consumir el quantum
                                if (currentProcess.getInstructions() <= 0) {
                                    PSO1.finishProcesses++;
                                    currentProcess.setStatus("Finished");
                                    PSO1.colaFinish.encolar(currentProcess);
                                
                                
                                    setFreeProcess();
                                    break;
                                }
                            
                                // Si se consume el quantum y quedan instrucciones
                                if (quantumTiempo <= 0) {
                                    currentProcess.setStatus("Ready");
                                    PSO1.colaBlock.encolar(currentProcess);
                                    setFreeProcess();
                                    break;
                                }       
                        }
                        break;
                        
                        case "SRT":
                            
                            while(currentProcess.getInstructions() > 0){
                                PSO1.mutex.acquire();
                                if(!colaReady.isEmpty() && currentProcess.getRemainingTime() > colaReady.getFront().getData().getRemainingTime()){
                                    currentProcess.setStatus("Ready");
                                    PSO1.colaReady.encolar(currentProcess);
                                    currentProcess = colaReady.desencolar();
                                    currentProcess.setStatus("Running");
                                }
                                PSO1.mutex.release();
                                
                                Thread.sleep(cycleDuration * 1000);
                                
                                currentProcess.setInstructions(currentProcess.getInstructions() - 1);
                                currentProcess.setExecuteInstructions(currentProcess.getExecuteInstructions() + 1);
                                currentProcess.setPc(currentProcess.getPc() + 1);
                                currentProcess.setMar(currentProcess.getMar() +1);
                                setProcess(this.getCurrentProcess());
                                
                                // Manejo de procesos I/O bound
                                if (currentProcess.getType().equalsIgnoreCase("I/O bound")){
                                    currentProcess.setCyclesExecuteFromLastBlock(currentProcess.getCyclesExecuteFromLastBlock() +1);
               
                                    if(currentProcess.getCyclesExecuteFromLastBlock() > currentProcess.getInterrupTime()){
                                        currentProcess.setStatus("Blocked");
                                        //Aqui pensar logica para colocar SO en el CPU
                                        currentProcess.setCyclesExecuteFromLastBlock(0);
                                        PSO1.colaBlock.encolar(currentProcess);
                                        
                                        setFreeProcess();
                                        break;
                                    }
                                }
                                if (currentProcess.getInstructions() <= 0) {
                                    PSO1.finishProcesses++;
                                    currentProcess.setStatus("Finished");
                                    PSO1.colaFinish.encolar(currentProcess);
                                
                               
                                    setFreeProcess();
                                    break;
                                }
                                
                            }
                            break;
                        case "FCFS": 
                            
                            while (currentProcess.getInstructions() > 0){
                                cycleDuration = PSO1.mainPage.getCycleDuration();
                                Thread.sleep(cycleDuration * 1000);
                                
                                currentProcess.setInstructions(currentProcess.getInstructions() - 1);
                                System.out.println("Instrucciones:" + currentProcess.getInstructions());
                                currentProcess.setExecuteInstructions(currentProcess.getExecuteInstructions() + 1);
                                currentProcess.setPc(currentProcess.getPc() + 1);
                                currentProcess.setMar(currentProcess.getMar() +1);
                                setProcess(this.getCurrentProcess());
                                
                                // Manejo de procesos I/O bound
                                if (currentProcess.getType().equalsIgnoreCase("I/O bound")){
                                    
                                    currentProcess.setCyclesExecuteFromLastBlock(currentProcess.getCyclesExecuteFromLastBlock() +1);
               
                                    if(currentProcess.getCyclesExecuteFromLastBlock() > currentProcess.getInterrupTime()){
                                        currentProcess.setStatus("Blocked");
                                        //Aqui pensar logica para colocar SO en el CPU
                                        currentProcess.setCyclesExecuteFromLastBlock(0);
                                        PSO1.colaBlock.encolar(currentProcess);
                                        
                                        setFreeProcess();
                                        break;
                                    }
                                }
                                
                                if (currentProcess.getInstructions() <= 0) {
                                    PSO1.finishProcesses++;
                                    currentProcess.setStatus("Finished");
                                    PSO1.colaFinish.encolar(currentProcess);
                                
                               
                                    setFreeProcess();
                                    break;
                                }
                            }
                            break;
                        case "SPN" :
                            
                            while (currentProcess.getInstructions() > 0){
                                cycleDuration = PSO1.mainPage.getCycleDuration();
                                Thread.sleep(cycleDuration * 1000);
                                
                                currentProcess.setInstructions(currentProcess.getInstructions() - 1);
                                currentProcess.setExecuteInstructions(currentProcess.getExecuteInstructions() + 1);
                                currentProcess.setPc(currentProcess.getPc() + 1);
                                currentProcess.setMar(currentProcess.getMar() +1);
                                setProcess(this.getCurrentProcess());
                                
                                // Manejo de procesos I/O bound
                                if (currentProcess.getType().equalsIgnoreCase("I/O bound")){
                                    currentProcess.setCyclesExecuteFromLastBlock(currentProcess.getCyclesExecuteFromLastBlock() +1);
               
                                    if(currentProcess.getCyclesExecuteFromLastBlock() > currentProcess.getInterrupTime()){
                                        currentProcess.setStatus("Blocked");
                                        //Aqui pensar logica para colocar SO en el CPU
                                        currentProcess.setCyclesExecuteFromLastBlock(0);
                                        PSO1.colaBlock.encolar(currentProcess);
                                        
                                        setFreeProcess();
                                        break;
                                    }
                                }
                                
                                if (currentProcess.getInstructions() <= 0) {
                                    PSO1.finishProcesses++;
                                    currentProcess.setStatus("Finished");
                                    PSO1.colaFinish.encolar(currentProcess);
                                
                              
                                    setFreeProcess();
                                    break;
                                }
                            }
                            break;
                        case "HRRN" :
                            
                            while (currentProcess.getInstructions() > 0){
                                cycleDuration = PSO1.mainPage.getCycleDuration();
                                Thread.sleep(cycleDuration * 1000);
                                
                                currentProcess.setInstructions(currentProcess.getInstructions() - 1);
                                currentProcess.setExecuteInstructions(currentProcess.getExecuteInstructions() + 1);
                                currentProcess.setPc(currentProcess.getPc() + 1);
                                currentProcess.setMar(currentProcess.getMar() +1);
                                setProcess(this.getCurrentProcess());
                                
                                // Manejo de procesos I/O bound
                                if (currentProcess.getType().equalsIgnoreCase("I/O bound")){
                                    currentProcess.setCyclesExecuteFromLastBlock(currentProcess.getCyclesExecuteFromLastBlock() +1);
               
                                    if(currentProcess.getCyclesExecuteFromLastBlock() > currentProcess.getInterrupTime()){
                                        currentProcess.setStatus("Blocked");
                                        //Aqui pensar logica para colocar SO en el CPU
                                        currentProcess.setCyclesExecuteFromLastBlock(0);
                                        PSO1.colaBlock.encolar(currentProcess);
                                        
                                        setFreeProcess();
                                        break;
                                    }
                                }
                                
                                if (currentProcess.getInstructions() <= 0) {
                                    PSO1.finishProcesses++;
                                    currentProcess.setStatus("Finished");
                                    PSO1.colaFinish.encolar(currentProcess);
                                
                           
                                    setFreeProcess();
                                    break;
                                }
                            }
                            break;
                    }
                }
            }catch (InterruptedException e){
                System.out.println("CPU " + id + " fue interrumpida.");
                Thread.currentThread().interrupt();
                
            }
        }
    
    }

    


    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the id
     */
    public int getsId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
