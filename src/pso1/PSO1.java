/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso1;

import Interfaz.MainPage;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SebasBD
 */
public class PSO1 {
    public static int cicloGlobal;
    public static Semaphore mutex = new Semaphore(1);
    public static Cola<Proceso> colaReady = new Cola<>();
    public static Cola<Proceso> colaBlock = new Cola<>();
    public static Cola<Proceso> colaFinish = new Cola<>();
    public static String politicaActual = "FCFS";
    public static int finishProcesses = 0;
    public static CPU cpu1 = new CPU(1);
    public static CPU cpu2 = new CPU(2);
    public static CPU cpu3 = new CPU(3);
    
    
    
    //para el hilo que maneja la cola de listos
    public static synchronized String getPoliticaActual() {
        return politicaActual;
    }
    
    //para setearla a la nueva politica que escoja el usuario
    public static synchronized void setPoliticaActual(String nuevaPolitica) {
        politicaActual = nuevaPolitica;
    }  
    public static MainPage mainPage = new MainPage();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        mainPage.setVisible(true);
        
        
         
        cpu1.start();
        cpu2.start();
        cpu3.start();
        ordenarColas();
        handlerColaBlock();
      
    }
    
    
    public static void ordenarColas(){
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100); 

                // Obtener la política actual desde MainClass
                    String politica = politicaActual;

                // Ordenar según la política
                    switch (politica) {
                        case "FCFS":
                        // No se necesita ordenar
                            break;
                        case "SRT":
                            ordenarSRT(colaReady);
                            break;
                        case "SPN":
                            ordenarSPN(colaReady);
                            break;
                        case "HRRN":
                            ordenarHRRN(colaReady, cicloGlobal);
                            break;
                        case "Round Robin":
                        // No se necesita ordenar
                            break;
                    }

               

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void handlerColaBlock(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int cycleDuration = mainPage.getCycleDuration();
                    // Procesar la cola de bloqueados
                    if (!colaBlock.isEmpty()) {
                        Proceso proceso = (Proceso) colaBlock.desencolar();
                        int durationInIO = proceso.getInterrupDuration();
                        while(proceso.getInterrupDuration() != 0){
                            proceso.setInterrupDuration(proceso.getInterrupDuration() - 1);
                            
                        }
                        proceso.setInterrupDuration(durationInIO);
                        proceso.setStatus("Ready");
                        colaReady.encolar(proceso);
                        
                    } else {
                        try {
                            Thread.sleep(cycleDuration * 1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PSO1.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }).start();
    }
    
    public static void iniciarRelojGlobal() {
        new Thread(() -> {
            cicloGlobal = 0; // Ciclo inicial
            while (true) {
                try {
                // Lee la duración actual del ciclo desde MainWindow
                    int cycleDuration = mainPage.getCycleDuration();
                   
                    Thread.sleep(cycleDuration * 1000); 
                    cicloGlobal++;
                    mainPage.actualizarCicloGlobal(cicloGlobal);
                    
                    // Pausa según la duración configurada

                // Incrementa el ciclo global y actualiza en la interfaz
                    
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    public static void ordenarSRT(Cola<Proceso> colaReady) {
        int size = colaReady.getLength();
        //array del tamano de la cola
        if (size <= 0) {
            return; // Salir de la función si no hay procesos
        }
        Proceso[] procesos = new Proceso[size];

        // Desencolar todos los procesos
        for (int i = 0; i < size; i++) {
            procesos[i] = colaReady.desencolar();
        }

        // Ordenar por menor tiempo restante
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (procesos[j].getRemainingTime() > procesos[j + 1].getRemainingTime()) {
                    Proceso temp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = temp;
                }
            }
        }

        // Reencolar procesos ordenados
        for (Proceso p : procesos) {
            colaReady.encolar(p);
        }
    }
    
    public static void ordenarSPN(Cola<Proceso> colaReady) {
    // Convertir la cola en un array para facilitar el ordenamiento
        int size = colaReady.getLength();
        if (size <= 0) {
            
            return; // Salir de la función si no hay procesos
        }
        Proceso[] procesos = new Proceso[size];
       
       
        for (int i = 0; i < size; i++) {
            procesos[i] = colaReady.desencolar();
        }

    // Ordenar por menor cantidad de instrucciones
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (procesos[j].getInstructions() > procesos[j + 1].getInstructions()) {
                // Intercambiar procesos
                    Proceso temp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = temp;
                }
            }
        }

    // Volver a encolar los procesos en la cola de listos ordenados
        for (Proceso p : procesos) {
            colaReady.encolar(p);
            
        }
        
    }
    
    public static void ordenarHRRN(Cola<Proceso> colaReady, int cicloGlobal) {
    // Convertir la cola en un array para facilitar el ordenamiento
        int size = colaReady.getLength();
        if (size <= 0) {
            return; // Salir de la función si no hay procesos
        }
        Proceso[] procesos = new Proceso[size];

        for (int i = 0; i < size; i++) {
            procesos[i] = colaReady.desencolar();
        }

    // Ordenar por mayor tasa de respuesta
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
            // Calcular tasa de respuesta dinámicamente para los procesos
                int tiempoEsperaJ = cicloGlobal - procesos[j].getCicloEnqueCola();
                double tasaRespuestaJ = (tiempoEsperaJ + procesos[j].getInstructions()) / (double) procesos[j].getInstructions();

                int tiempoEsperaJ1 = cicloGlobal - procesos[j + 1].getCicloEnqueCola();
                double tasaRespuestaJ1 = (tiempoEsperaJ1 + procesos[j + 1].getInstructions()) / (double) procesos[j + 1].getInstructions();

                if (tasaRespuestaJ < tasaRespuestaJ1) {
                // Intercambiar procesos
                    Proceso temp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = temp;
                }
            }
        }

    // Volver a encolar los procesos en la cola de listos ordenados
        for (Proceso p : procesos) {
            colaReady.encolar(p);
        }
    }

   
    
    }
