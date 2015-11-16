/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

/**
 *
 * @author faanbece
 */
public class Refinador {
    
    
    public static ArrayList<String> noRecursive(ArrayList<String> grama){
        String gem, nogem, singem,prod, alfa, beta;        
        ArrayList<String> gram=(ArrayList<String>) grama.clone();
        String[] subnogem;
        for (int i = 0; i < gram.size(); i++) {
            prod=gram.get(i);
            gem=prod.split("->")[0];
            nogem=prod.split("->")[1]; 
            if (nogem.indexOf(gem)==0 && (nogem.indexOf(gem+"'") != nogem.indexOf(gem) )) {
                subnogem=nogem.split("\\|");
                alfa="";
                beta="";
                for (String p : subnogem) {
                    if (p.indexOf(gem)==0 && (p.indexOf(gem+"'") != p.indexOf(gem))) { 
                        alfa+=p.replaceFirst(gem, "").replace(gem,gem+"'")+"|";                        
                    }else{
                        if (p.compareTo("€")!=0) {
                            beta+=p+gem+"'|";
                        }else{
                            beta+=gem+"'|";
                        }
                    }
                }
                if (subnogem.length==1) {
                    beta=gem+"'|";
                }
                gram.remove(prod);                
                gram.add(i,   gem+"->"+beta.substring(0,beta.length()-1));               
                gram.add(i+1, gem+"'"+"->"+alfa.substring(0,alfa.length()-1)+gem+"'|"+"€");
            }
        }
        return gram;
    }
    public static ArrayList<String> factorize(ArrayList<String> grama){
        String gem, nogem, eval,compare, beta, alfa, gamma, element, exp="'";        
        ArrayList<String> gram=(ArrayList<String>) grama.clone();
        String[] prod;
        for (int h = 0; h < gram.size(); h++) {
            gem=gram.get(h).split("->")[0];
            nogem=gram.get(h).split("->")[1]; 
            prod=nogem.split("\\|");
            ArrayList<String> production=vectToArray(prod);
            Stack<String> factor= new Stack<String>();
            Stack<Integer> numFactor= new Stack<Integer>();
            eval="";
            compare=""; 
            gamma="";
            beta="";
            int count=0, index=0;
            production=sortByLength(production);
            //showArray(production, false);
            production=reverseArray(production);
            //showArray(production, false);
            for (int k = 0; k < production.size(); k++) {            
                compare=production.get(k);
                for (int i = 0; i < compare.length(); i++) {
                    eval=compare.substring(0,i+1);
                    count=0;
                    element="";
                    for (int j = 0; j < production.size(); j++) {
                        element=production.get(j);
                        if (element.indexOf(eval)==0) { 
                            count++;   
                        }
                    }
                    if (count>1) {
                        factor.push(eval);
                        numFactor.push(count);
                    }
                }
                if (!factor.isEmpty()) {
                    System.out.println("Factor : "+factor.peek() );
                    String f=factor.peek();
                    index=gram.indexOf(gem+"->"+nogem);
                    gram.remove(gem+"->"+nogem);
                    beta="";
                    gamma="";
                    element="";
                    for (int i=0; i<production.size(); i++) {
                        element= production.get(i);
                        if (element.indexOf(f)==0) { 
                            alfa=element.replaceFirst(f, "");
                            if (alfa.length()>0) {
                                i--;
                                production.remove(element);
                                beta+=alfa+"|";
                            }else{
                                beta+="€ "; 
                            }
                        }else{
                            gamma+="|"+element;
                        }
                    }
                    gram.add(index,gem+"->"+f+gem+exp+gamma);
                    gram.add(index+1,gem+exp+"->"+beta.substring(0,beta.length()-1));
                    gem=gram.get(h).split("->")[0];
                    nogem=gram.get(h).split("->")[1]; 
                    factor.clear();
                    numFactor.clear();
                    exp+="'";
                    h--;
                    k=production.size(); 
                   // showArray(gram, false);                    
                }else{
                    exp="'";
                }
            }     
        }
        return gram;
    } 
    public static ArrayList<String> sortByLength(ArrayList<String> ToSort){
         ArrayList<String> toSort=(ArrayList<String>) ToSort.clone();
         
         class StringLengthListSort implements Comparator<String>{
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
         }
        StringLengthListSort ss = new StringLengthListSort();
        toSort.sort(ss);
        return  toSort;
    }   
    public static ArrayList<String> vectToArray(String[] vect){
        ArrayList<String> array= new ArrayList<>();
        array.addAll(Arrays.asList(vect));
        return array;
    }    
    public static ArrayList<String> reverseArray(ArrayList<String> toReverse){
        ArrayList<String> reverse=new ArrayList<String>();
        int size=toReverse.size();
        for (int i = size-1; i >=0 ; i--) {
            reverse.add(toReverse.get(i));
        }
        return reverse;
    }    
    public static String reverse(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);
        for (i = (len - 1); i >= 0; i--){
            dest.append(source.charAt(i));
        }
        return dest.toString();
    }
    public static void showArray(ArrayList<String> array, boolean oneLine){
        System.out.println("................");
        if (oneLine) {
             System.out.println("{");
            for (String e : array) {
                System.out.print(e+",");
            }
            System.out.print("\b}\n");
        }else{
            for (String e : array) {
                System.out.println(e);
            }
        }
        System.out.println("................");
    }
}
