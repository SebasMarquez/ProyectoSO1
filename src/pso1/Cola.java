/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso1;

/**
 *
 * @author SebasBD
 */
public class Cola<T> {
    private Nodo<T> front;
    private Nodo<T> back;
    private int length; 
    
    public Cola(){
        this.front = null;
        this.back= null;
        this.length = 0;
    }
    
    public void encolar(T data) {
        Nodo<T> newNodo = new Nodo<>(data);
        if (isEmpty()) {
            front = newNodo;
            back = newNodo;
        } else {
            back.setSiguiente(newNodo);
            back = newNodo;
        }
        length++;
    }
    public T desencolar() {
        if (isEmpty()) {
            return null;
        }
        T data = front.getData();
        front = front.getSiguiente();
        if (front == null) {
            back = null;
        }
        length--;
        return data;
    }
    
    public void eliminar(String data) {
        Nodo<T> actual = front;
        Nodo<T> previo = null;
        
          
        while (actual != null) {
            if ((actual.getData().toString()).equals(data)) {
                System.out.println("AUMENTO DE PRIORIDAD PARA "+ data);
                if (previo == null) {
                    front = actual.getSiguiente();
                    if (front == null) {
                        back = null;
                    }
                } else {
                    previo.setSiguiente(actual.getSiguiente());
                    if (actual.getSiguiente() == null) {
                        back = previo;
                    }
                }
                length--;
                return; // Elemento encontrado y eliminado
            }

            previo = actual;
            actual = actual.getSiguiente();
        }
    }
    
    public void imprimirCola() {
        if (isEmpty()) {
            System.out.println("La cola está vacía.");
            return;
        }
        
        Nodo<T> actual = front;
        while (actual != null) {
            Proceso proceso = (Proceso) actual.getData(); // Hacemos un casting a Proceso
            System.out.print(proceso.getName() + " "); // Imprimimos el nombr
            //System.out.print(actual.getData() + " ");
            actual = actual.getSiguiente();
        }
        System.out.println();
    }

    public Proceso searchNotTaken() {
    Nodo<Proceso> acc = (Nodo<Proceso>) front; // Comienza desde el frente de la cola
    while (acc != null) {
        if (!acc.getData().isTaken()) {
            return acc.getData(); // Retorna el proceso no tomado
        }
        acc = acc.getSiguiente(); // Avanza al siguiente nodo
    }
    return null; // No se encontró ningún proceso no tomado
    }
    
    public void remove(Proceso proceso) {
        if (isEmpty()) {
            return; 
        }

        if (((Proceso)front.getData()).getProcess_id() == proceso.getProcess_id()) {
            front = front.getSiguiente(); // Mover el frente al siguiente nodo
            if (front == null) {
                back = null; // Si la cola quedó vacía
            }
            length--;
            return;
        }

        Nodo<Proceso> actual = (Nodo<Proceso>) front;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getData().getProcess_id() == proceso.getProcess_id()) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                if (actual.getSiguiente() == null) {
                    back = (Nodo<T>) actual; // Actualizar el final de la cola
                }
                length--;
                return;
            }
            actual = actual.getSiguiente();
        }
    }
    
    public boolean isEmpty(){
        return front == null;
    }

    /**
     * @return the frente
     */
    public Nodo<T> getFront() {
        return front;
    }

    /**
     * @param frente the frente to set
     */
    public void setFront(Nodo<T> front) {
        this.front= front;
    }

    /**
     * @return the trasera
     */
    public Nodo<T> getBack() {
        return back;
    }

    /**
     * @param trasera the trasera to set
     */
    public void setBack(Nodo<T> back) {
        this.back = back;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }
}

