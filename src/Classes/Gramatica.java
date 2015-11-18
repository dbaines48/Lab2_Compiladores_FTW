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
    Terminal epsilon = new Terminal("€");
    Terminal peso = new Terminal("$");
    ArrayList<Boolean> PrimerOK = new ArrayList<Boolean>();
    ArrayList<Boolean> SgtOK = new ArrayList<Boolean>();

    public Gramatica(ArrayList<String> gram) {
        NTs = new ArrayList<NonTerminal>();
        CrearGramatica(gram);
        for (NonTerminal nt : NTs) {
            PrimerOK.add(false);
            SgtOK.add(false);
        }
        for (NonTerminal nt : NTs) {
           Primeros(nt);
        }
        showPrimeros(NTs);
        
        
        NTs.get(0).Siguiente.add(peso);
        for (NonTerminal nt : NTs) {
            //Sgt(nt);
            siguiente(nt);
        }        
        showSgts(NTs);
        System.out.println("");
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

    public void CrearGramatica(ArrayList<String> gramatica) {
        int pos = -1;
        //Añadir no terminales primero
        for (String gr : gramatica) {
            pos = -1;
            String[] part = gr.split("->");
            pos = NTs.indexOf(part[0]);
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
                findNT(gr.split("->")[0]).Producciones.add(produccion);
                for (int i = 0; i < prod.length(); i++) {
                    String cre = prod.substring(i, i + 1);
                    if (isUpperCase(cre)) { //Es un No Terminal
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
                    } else { //Es un terminal, o en su defecto una cadena de terminales
                        boolean entra = true;
                        int j = i + 1;
                        while (j < prod.length() && entra) {
                            if (!isUpperCase(prod.substring(j, j + 1))) {
                                cre += prod.substring(j, j + 1);
                                j++;
                            } else {
                                entra = false;
                            }
                        }
                        i = j - 1;
                        produccion.addSymbol(cre.compareTo("€") == 0 ? epsilon : new Terminal(cre));
                    }
                }
            }
        }
        //System.out.println("Dio esta monda!");
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
                } else {
                    Primeros((NonTerminal) s);
                    ini.Primero.addAll(((NonTerminal) s).Primero);
                    c++;
                    if (((NonTerminal) s).Primero.indexOf(epsilon) != -1 && c<ini.Producciones.get(i).Simbolos.size()) {
                        next = ini.Producciones.get(i).Simbolos.get(c);                        
                        if (next==epsilon) {
                            ini.Primero.remove(epsilon);
                            ini.Primero.add(((Terminal) next));                            
                        }else {
                            if (!next.isterminal) {
                                Primeros((NonTerminal) next);
                                for (Terminal t : ((NonTerminal) next).Primero) {
                                    if ((ini.Primero.indexOf(t))==-1) {
                                        ini.Primero.add(t);
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
        boolean sw=false;
        Symbol su,s;
        NonTerminal nt,act;
      
        for (Produccion p : ini.Producciones) {
            for (int i = p.Simbolos.size()-1; i >=0; i--) {
                s=p.Simbolos.get(i);
                if (!s.isterminal && s!=ini) {
                    if (llevaaE(p.Simbolos,(NonTerminal)s)) {
                        siguiente((NonTerminal)s);
                        for (Terminal t : ini.Siguiente) {
                            if (((NonTerminal)s).Siguiente.indexOf(t)==-1) {
                                ((NonTerminal)s).Siguiente.add(t);   
                            }
                        }
                    }//else{
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
                            sw=true;
                        }else{
                            for (Terminal t : ((NonTerminal)su).Primero) {
                                if (((NonTerminal)s).Siguiente.indexOf(t)==-1 && t!=epsilon) {
                                 ((NonTerminal)s).Siguiente.add(t);   
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
      //  showSgts(NTs);
    }
    
    public void Sgt(NonTerminal ini) {
        int c=0, f=0, index;
        Symbol su,s;
        NonTerminal nt,act;
        Terminal t;
        //if (!SgtOK.get(NTs.indexOf(ini))) {
            for (Produccion p : ini.Producciones) {
                //se recorre de atras a hacia adelante la cadena simbolos
                for (int i = p.Simbolos.size()-1; i >=0; i--) {
                    s=p.Simbolos.get(i);
                    // Si encontramos un terminal, añadir este a los siguientes de todos los NONTERMINALES a su izquierda
                    if (s.isterminal) {
                        for (int j = 0; j < i; j++) {
                            su=p.Simbolos.get(j);
                            if (!su.isterminal) {
                                ((NonTerminal)su).Siguiente.add((Terminal)s);
                            }
                        }
                    }else{
                    //si no es un terminal no que se encontró, añadir los primeros de este a los Siguiesntes de los NONTERminales a su izquierda
                        for (int j = 0; j < i; j++) {
                            //se buscan todos los NoTerminales (su) a la izquerda de S
                            su=p.Simbolos.get(j);
                            if (!su.isterminal) {
                                //A cada su, añadir los primeros de S a los siguientes de SU sin epsilon
                                for (int k=0; k< ((NonTerminal)s).Primero.size();k++) {
                                    t=((NonTerminal)s).Primero.get(k);
                                   if (t!=epsilon ) {
                                        ((NonTerminal)su).Siguiente.add((Terminal)t);
                                    }
                                }
                            }
                        }                        
                        if (llevaaE(p.Simbolos, (NonTerminal) s)) {
                          
                            for (int k=0; k< ((NonTerminal)ini).Siguiente.size();k++) {
                                t=((NonTerminal)ini).Siguiente.get(k);
                                if (t!=epsilon && t!=s ) {
                                    ((NonTerminal)s).Siguiente.add((Terminal)t);
                                }
                            }
                            
                        }
                    }
                    
                }
            }
          //  SgtOK.set(NTs.indexOf(ini),true);
        //}
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
}
