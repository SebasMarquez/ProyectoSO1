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

