/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
class NonTerminal extends Symbol{
 
    ArrayList<Terminal> Primero;
    ArrayList<Terminal> Siguiente;
    ArrayList<Produccion> Producciones;
    boolean ya=false;
    
    public NonTerminal(String name) {
        super(name, false);
        Primero = new ArrayList<Terminal>();
        Siguiente = new ArrayList<Terminal>();
        Producciones = new ArrayList<Produccion>();
    }
    
}
