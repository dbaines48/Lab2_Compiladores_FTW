/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author Daniel
 */
public class Symbol {
    String name;
    boolean isterminal;
    
    public Symbol(String name, boolean terminal){
        this.name = name;
        this.isterminal = terminal;
    }
}
