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

    public ArrayList<NonTerminal> NTs;
    Terminal epsilon = new Terminal("&");
    Terminal peso = new Terminal("$");
    ArrayList<Boolean> PrimerOK = new ArrayList<Boolean>();
    ArrayList<Boolean> SgtOK = new ArrayList<Boolean>();
    public ArrayList<Terminal> terminals;
    public Produccion[][] tabla_M;

     public Gramatica(ArrayList<String> gram) {
        NTs = new ArrayList<NonTerminal>();
        terminals=getAllTerminals(gram);
        terminals.add(peso);
        CrearGramatica(gram);
        
        for (NonTerminal nt : NTs) {
            PrimerOK.add(false);
            SgtOK.add(false);
        }
        for (NonTerminal nt : NTs) {
           Primeros(nt);
        }
        showPrimeros(NTs);
        
        /* .---- Creando Siguientes*/
        NTs.get(0).Siguiente.add(peso);
        for (NonTerminal i : NTs) {
            for (NonTerminal nt : NTs) {
                //Sgt(nt);
                siguiente(nt);
            }     
        }        
        showSgts(NTs);
        System.out.println("");
         /* .---- End Sigiientes*/
        tabla_M = new Produccion[100][100];
        HacerTablaM();
        //showTablaM();
        System.out.println("ok");
    }
     
    void showTablaM(){
        System.out.print("\t");
        for (Terminal terminal : terminals) {
            System.out.print(terminal.name+"\t");
        }
        for (int i = 0; i < NTs.size(); i++) {
            System.out.print(NTs.get(i).name+"\t");
            for (int j = 0; j < terminals.size(); j++) {
                if(tabla_M[i][j] == null)
                    System.out.print(" \t");
                else
                    System.out.print(tabla_M[i][j].string+"\t");
            }
            System.out.println("");
        }
    }
     
    public void CrearGramatica(ArrayList<String> gramatica){
        int pos = -1;

//Añadir NT a nts
        for (String gr : gramatica) {
            pos = -1;
            String[] part = gr.split("->");
            for (NonTerminal nt : NTs) {
                if (nt.name.compareTo(part[0]) == 0) {
                    pos = NTs.indexOf(nt);
                    break;
                }
            }
            if (pos == -1) {
                NTs.add(new NonTerminal(part[0]));
            }
        }

//Añadir lado derecho
        for (String gr : gramatica) {
            pos = -1;
            String[] producciones = gr.split("->")[1].split("\\|");
            for (String prod : producciones) {
                Produccion produccion = new Produccion();
                produccion.string = gr.split("->")[0] + "->" + prod;
                findNT(gr.split("->")[0]).Producciones.add(produccion);
                for (int i = 0; i < prod.length(); i++) {
                    String cre = prod.substring(i, i + 1);
                    if (isUpperCase(cre)) { //Si es NoTerminal
                        boolean entra = true;
                        int j = i + 1;
                        while (j < prod.length() && entra) {
                            if (prod.substring(j, j + 1).compareTo("'") == 0) {
                                cre += "'";
                                j++;
                            } else {
                                entra = false;
                            }
                        }
                        i = j - 1;
                        produccion.addSymbol(findNT(cre));
                    } else { //Si es Terminal
                        produccion.addSymbol(cre.compareTo("&") == 0 ? epsilon : findTerminal(cre));
                    }
                }
            }
        }
    }

    public boolean isUpperCase(String letra) {
        return ((int) letra.charAt(0) >= 65 && (int) letra.charAt(0) <= 90) ? true : false;
    }

    public NonTerminal findNT(String name) {
        for (NonTerminal nt : NTs) {
            if (nt.name.compareTo(name) == 0) {
                return NTs.get(NTs.indexOf(nt));
            }
        }
        return null;
    }

    public Terminal findTerminal(String name){
        for (Terminal t : terminals) {
            if(t.name.compareTo(name)==0)
                return t;
        }
        return null;
    }
    
    public ArrayList<Terminal> getAllTerminals(ArrayList<String> lines){
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Terminal> terminals = new ArrayList<Terminal>();
        String[] part;
        for (String line : lines) {
            part = line.split("->");
            String[] div = part[1].split("\\|");
            for (String d : div) {
                result.add(part[0]+"->"+d);
            }
        }
        for (String res : result) {
            String p = res.split("->")[1];
            for (int i = 0; i < p.length(); i++) {
                String c = p.substring(i,i+1);
                if(!isUpperCase(c) && c.compareTo("&")!=0 && c.compareTo("'")!=0){
                    int pos = -1;
                    for (Terminal t : terminals) {
                        if(t.name.compareTo(c)==0){
                            pos = terminals.indexOf(t);
                            break;
                        }
                    }
                    if(pos==-1)
                        terminals.add(new Terminal(c));
                }
            }
        }
        return terminals;
    }
   
    public void Primeros(NonTerminal ini) {
        Symbol s, next;
        int c = 0;
        if (!PrimerOK.get(NTs.indexOf(ini))) {
            for (int i = 0; i < ini.Producciones.size(); i++) {
                s = ini.Producciones.get(i).Simbolos.get(0);
                c = 0;
                if (s.isterminal) {
                    if (ini.Primero.indexOf((Terminal) s)==-1) {
                        ini.Primero.add((Terminal) s);
                    }                     
                    if (ini.Producciones.get(i).primero.indexOf((Terminal) s)==-1) {
                        ini.Producciones.get(i).primero.add((Terminal) s);
                    }                      
                } else {
                    if(s != ini)
                        Primeros((NonTerminal) s);
                    ini.Primero.addAll(((NonTerminal) s).Primero);
                    ini.Producciones.get(i).primero.addAll(((NonTerminal) s).Primero);
                    c++;
                    if (((NonTerminal) s).Primero.indexOf(epsilon) != -1 && c<ini.Producciones.get(i).Simbolos.size()) {
                        next = ini.Producciones.get(i).Simbolos.get(c);  
                        if (next==epsilon) {
                            ini.Primero.remove(epsilon);
                            ini.Primero.add(((Terminal) next)); 
                            
                            ini.Producciones.get(i).primero.add((Terminal) next);
                            ini.Producciones.get(i).primero.add((Terminal) next);
                        }else {
                            if (!next.isterminal) {
                                Primeros((NonTerminal) next);
                                for (Terminal t : ((NonTerminal) next).Primero) {
                                    
                                    if ((ini.Primero.indexOf(t))==-1) {
                                        ini.Primero.add(t);
                                    }
                                    
                                    if (ini.Producciones.get(i).primero.indexOf((Terminal) t) == -1) {
                                        ini.Producciones.get(i).primero.add((Terminal) t);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            PrimerOK.set(NTs.indexOf(ini),true);
        }
    }
    
    public void siguiente(NonTerminal ini){
        int  j,size;
        boolean sw=false, ya=false;
        Symbol su,s;
        NonTerminal nt,act;
        Produccion p;
        
        for (int k = 0; k < ini.Producciones.size(); k++) {
            p=ini.Producciones.get(k);
            for (int i = p.Simbolos.size()-1; i >=0; i--) {
                s=p.Simbolos.get(i);
                if (!s.isterminal && s!=ini) {
                    
                    if (llevaaE(p.Simbolos,(NonTerminal)s)) {
                        for (Terminal t : ini.Siguiente) {                            
                            if (((NonTerminal)s).Siguiente.indexOf(t)==-1) {
                                ((NonTerminal)s).Siguiente.add(t);   
                            }
                            if (ini.Producciones.get(k).siguiente.indexOf((Terminal)t) == -1){                            
                                ini.Producciones.get(k).siguiente.add((Terminal)t);   
                            }
                        }
                    }
                    j=i+1;
                    sw=false;
                    size=p.Simbolos.size();
                    while (j<size && !sw) {
                        su=p.Simbolos.get(j);
                        j++;
                        if (su.isterminal) {
                            
                            if (((NonTerminal)s).Siguiente.indexOf((Terminal)su)==-1) {
                                ((NonTerminal)s).Siguiente.add((Terminal)su);
                            }
                            if (ini.Producciones.get(k).siguiente.indexOf((Terminal)su) == -1){                            
                                ini.Producciones.get(k).siguiente.add((Terminal)su);   
                            }
                            sw=true;
                            
                        }else{
                            for (Terminal t : ((NonTerminal)su).Primero) {
                                
                                if (((NonTerminal)s).Siguiente.indexOf(t)==-1 && t!=epsilon) {
                                 ((NonTerminal)s).Siguiente.add(t);   
                                }
                                if (ini.Producciones.get(k).siguiente.indexOf(t)==-1 && t!=epsilon) {
                                   // ini.Producciones.get(k).siguiente.add(t);   
                                }
                                
                            }
                            if ((((NonTerminal)su).Primero.indexOf(epsilon))==-1  && s!=epsilon) {
                                sw=true;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public boolean llevaaE(ArrayList<Symbol> prod, NonTerminal dude){
        int index=prod.indexOf(dude);
        Symbol s;
       //System.out.println(" indexOfDude: "+index);
        if (index==prod.size()-1) {
            return true;
        }
        for (int i = index+1; i < prod.size(); i++) {
            s=prod.get(i);
            if (s.isterminal) {
                return false;
            }else{
                if (((NonTerminal)s).Primero.indexOf(epsilon)==-1) {
                    return false;
                }
            }
        }
        
    return true;
   }
    
    public static void showPrimeros(ArrayList<NonTerminal> gram){
        System.out.println("........PRIMEROS........");
       
        for (NonTerminal nt : gram) {
            System.out.print(nt.name+" = {");
            for (Terminal t : nt.Primero) {
                System.out.print(t.name+",");
            }
            System.out.print("\b}\n");
        }
        System.out.println("................");
    }
   
    public static void showSgts(ArrayList<NonTerminal> gram){
        System.out.println("........SIGUIENTES........");
       
        for (NonTerminal nt : gram) {
            System.out.print(nt.name+" = {");
            for (Terminal t : nt.Siguiente) {
                System.out.print(t.name+",");
            }
            System.out.print("\b}\n");
        }
        System.out.println("................");
    }
    
    public void HacerTablaM(){
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                tabla_M[i][j] = null;
            }
        }
        for (NonTerminal nt : NTs) {
            for (Produccion pr : nt.Producciones) {
                if(pr.primero.indexOf(epsilon)== -1){ // no contiene a epsilon
                    for (Terminal ter : pr.primero) {
                        tabla_M[NTs.indexOf(nt)][terminals.indexOf(ter)] = pr;
                    }
                }else{ // contiene a epsilon
                    for (Terminal ter : nt.Siguiente) {
                        tabla_M[NTs.indexOf(nt)][terminals.indexOf(ter)] = pr;
                    }
                }
            }
        }
    }
}
