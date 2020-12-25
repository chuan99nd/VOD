/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstblood;

import java.util.Random;

/**
 *
 * @author Admin
 */
public class Individual implements Comparable<Individual>{
    public static int n = GA.genSize;
    public static Random rd = GA.rd;
    public static String GENS = "qwertyuiopasdfghjklzxcvbnm, !@#$%^&*(";
    public String gen;
    public int fitness;
    public void init(){
        char[] temp = new char[n];
        for (int i = 0; i < n; ++i){
            int index = rd.nextInt(GENS.length());
            temp[i] = GENS.charAt(index);
        }
        gen = new String(temp);
        this.setFitnes();
    }
    public void setFitnes(){
        String s = GA.target;
        int res = 0;
        for (int i = 0; i < n; ++i){
            if (s.charAt(i) == gen.charAt(i)) ++res;
        }
        fitness = res;
    }
    public int getFitness(){
        return this.fitness;
    }
    public Individual mate(Individual ind){
        Individual child = new Individual();
        char[] temp = new char[n];
        for(int i = 0; i < n; ++i){
            double p = rd.nextDouble();
            if (p < 0.45){
                temp[i] = this.gen.charAt(i);
            } else if ( p < 0.9){
                temp[i] = ind.gen.charAt(i);
            } else {
                temp[i] = GENS.charAt(rd.nextInt(GENS.length()));
            }
        }
        child.gen = new String(temp);
        child.setFitnes();
        return child;
    }

    @Override
    public int compareTo(Individual o) {
        return o.getFitness() - this.getFitness();
    }
}
