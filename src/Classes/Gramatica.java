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

public class Gramatica {
    ArrayList<NonTerminal> NTs; 
    
    public Gramatica(ArrayList<String> gram){
        NTs = new ArrayList<NonTerminal>();
        CrearGramatica(gram);
    }
    
    public boolean isUpperCase(String letra){
        return ((int)letra.charAt(0) >= 65 && (int)letra.charAt(0) <=90 ) ? false : true;
    }
    
    public void CrearGramatica(ArrayList<String> gramatica){
        int pos = -1;
        
        //Añadir no terminales primero
        for (String gr : gramatica) {
            pos = -1;
            String[] part = gr.split("->");
            pos = NTs.indexOf(part[0]);
            if(pos != -1)
                NTs.add(new NonTerminal(part[0],false));
        }
        
        //Añadir lado derecho
        for (String gr : gramatica) {
            pos = 1;
            String[] part = gr.split("->")[1].split("\\|");
            for (String prod : part) {
                
            }
        }
    }
}
