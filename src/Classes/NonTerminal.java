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
    boolean prod_E;
    
    public NonTerminal(String name, boolean terminal) {
        super(name, terminal);
        Primero = new ArrayList<Terminal>();
        Siguiente = new ArrayList<Terminal>();
        Producciones = new ArrayList<Produccion>();
        prod_E = false;
    }
    
}
