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
    ArrayList<Boolean> PrimerOK = new ArrayList<Boolean>();

    public Gramatica(ArrayList<String> gram) {
        NTs = new ArrayList<NonTerminal>();
        CrearGramatica(gram);
        for (NonTerminal nt : NTs) {
            PrimerOK.add(false);
        }
        for (NonTerminal nt : NTs) {
            Primeros(nt);
        }
        showPrimeros(NTs);
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
                        System.out.println("next class: " + next + "  name: "+ next.name);
                        System.out.println("epsilon class: " + epsilon.getClass());
                        System.out.println(" equals ? " + next.getClass().equals(epsilon.getClass()));
                        
                        if (next==epsilon) {
                            ini.Primero.remove(epsilon);
                            ini.Primero.add(((Terminal) next));                            
                        }
                        /*if (ini.Primero.indexOf(epsilon)==-1) {
                            ini.Primero.add(((Terminal) next));
                        }*/
                        else {
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
            PrimerOK.set(NTs.indexOf(ini),true);
        }
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
}
