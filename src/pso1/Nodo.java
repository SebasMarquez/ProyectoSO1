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
public class Nodo<T> {
    private T data;
    private Nodo<T> siguiente;
    private String nombre;
    
    public Nodo(T data){
        this.data = data;
        this.siguiente = null;
    }
    
    public T getData(){
        return data;
    }
    
    public Nodo<T> getSiguiente(){
        return siguiente;
    }
    
    public void setSiguiente(Nodo<T> siguiente){
        this.siguiente = siguiente;
    }
}
