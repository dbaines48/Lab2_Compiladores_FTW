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
        return ((int)letra.charAt(0) >= 65 && (int)letra.charAt(0) <=90 ) ? true : false;
    }
    
    public NonTerminal findNT(String name){
        for (NonTerminal nt : NTs) {
            if(nt.name.compareTo(name)==0)
                return NTs.get(NTs.indexOf(nt));
        }
        return null;
    }
    
    public void CrearGramatica(ArrayList<String> gramatica){
        int pos = -1;
        
        //Añadir no terminales primero
        for (String gr : gramatica) {
            pos = -1;
            String[] part = gr.split("->");
            pos = NTs.indexOf(part[0]);
            if(pos == -1)
                NTs.add(new NonTerminal(part[0],false));
        }
        
        //Añadir lado derecho
        for (String gr : gramatica) {
            pos = -1;
            String[] producciones = gr.split("->")[1].split("\\|");
            for (String prod : producciones) {
                Produccion produccion = new Produccion();
                findNT(gr.split("->")[0]).Producciones.add(produccion);
                for (int i = 0; i < prod.length(); i++) {
                    String cre = prod.substring(i,i+1);
                    if(isUpperCase(cre)){ //Es un No Terminal
                        boolean entra = true;
                        int j=i+1;
                        while(j<prod.length() && entra){
                            if(prod.substring(j,j+1).compareTo("'")==0){
                                cre+="'";
                                j++;
                            }else
                                entra = false;
                        }
                        i=j-1;
                        produccion.addSymbol(findNT(cre));
                    }else{ //Es un terminal, o en su defecto una cadena de terminales
                        boolean entra = true;
                        int j=i+1;
                        while(j<prod.length() && entra){
                            if(!isUpperCase(prod.substring(j,j+1))){
                                cre+=prod.substring(j,j+1);
                                j++;
                            }else
                                entra = false;
                        }
                        i=j-1;
                        produccion.addSymbol(new Terminal(cre,true));
                    }
                }
            }
            
        }
        System.out.println("Dio esta monda!");
    }
}
