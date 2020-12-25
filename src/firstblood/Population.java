/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstblood;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Admin
 */
public class Population {
    ArrayList<Individual> pop = new ArrayList<>(GA.popSize);
    int n = GA.popSize;
    public void init(){
        for (int i = 0; i< n; ++i){
            Individual temp = new Individual();
            temp.init();
            temp.setFitnes();
            pop.add(temp);
        }
    }
    public void run(){
        
        for (int generation = 0; generation < GA.converge; ++ generation){
            ArrayList<Individual> temp = new ArrayList<>(pop);
            for (int i = 0; i < n; ++i){
                Individual kap = pop.get(GA.rd.nextInt(n));
                temp.add(pop.get(i).mate(kap));
            }
            Collections.sort(temp);
            pop = new ArrayList<> (temp.subList(0, n));
            System.out.println("Gen "+ generation+ " : "+pop.get(0).gen+ "  Fitness : "+ pop.get(0).fitness);
        }
        
    }
}
