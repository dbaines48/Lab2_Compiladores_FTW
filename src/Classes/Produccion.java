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
public class Produccion {
    
    ArrayList<Symbol> Simbolos;
    
    public Produccion(){
        Simbolos = new ArrayList<>();
    }
    
    public void addSymbol(Symbol sym){
        Simbolos.add(sym);
    }
    
    public void CrearProduccion(String pr){
        
    }
}
