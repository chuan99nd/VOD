/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vod_ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;


/**
 *
 * @author Adminzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
 */
public class Population {
    ArrayList<Individual> pop = new ArrayList<>(GA.popSize);
    int n = GA.popSize;
    Random rd = new Random(GA.SEED);
    int genSize = GA.genSize;
    public void init(){
//    	Initiation init = new Initiation();
//    	Individual min = init.run(GA.SEED, GA.numForStat);
//    	pop.add(min);
        for (int i = 0; i< n; ++i){
            Individual temp = new Individual();
            temp.init(rd);
            temp.setFitness();
            pop.add(temp);
        }
        Collections.sort(pop);
        pop = new ArrayList<Individual>(pop.subList(0, n));
    }
    public void show() {
        Individual best = pop.get(0);
        byte[] temp = new byte[n];
        temp[0] = 1;
        for (int i = 0; i < GA.numberOfProgram ; ++i){
            Eval e = new Eval(best, i);
            e.run();
            String res = "Program " + (i + 1) + " :";
            Collections.sort(e.ARes);
            for (int v : e.ARes) {
            	temp[v] = 1; 
            	res = res + " " + v;
            }
            System.out.println(res);
        }
        System.out.println("Best : " + pop.get(0).getFitness());
    }
    public ArrayList<Individual>  crossover(Individual id1, Individual id2){
        Individual  child1 = new Individual();
        Individual child2 = new Individual();
        int p1 = rd.nextInt(genSize);
        int p2 = rd.nextInt(genSize - p1);
        for(int i = 1; i < genSize; ++i){
        	if (i < p1 || i >= p2) {
        		child1.gen[i] = id1.gen[i];
        		child2.gen[i] = id2.gen[i];
        	}
        	else {
        		child1.gen[i] = id2.gen[i];
        		child2.gen[i] = id1.gen[1];
        	}
        }
        child1.gen[0] = 1;
        child2.gen[0] = 1;
        child1.setFitness();
        child2.setFitness();
        ArrayList<Individual> off_spring = new ArrayList<Individual>();
        off_spring.add(child1);
        off_spring.add(child2);
  		return off_spring;
    }
    public ArrayList<Individual>  treeCrossover(Individual id1, Individual id2){
        Individual  child1 = new Individual();
        Individual child2 = new Individual();
        /////////////////////////////////////////
        Stack<Integer> stack = new Stack<Integer>();
        int kount = 0;
        boolean[] duyet = new boolean[GA.genSize];
        double upper = 0.5*GA.genSize;
        int r, temp;
        // Copy gen tu cha me xuong con
        for (int i =0; i <GA.genSize; ++i) {
        	child1.gen[i] = id1.gen[i];
        	child2.gen[i] = id2.gen[i];
        }
        while (kount < upper) {
        	r = rd.nextInt(GA.genSize);
        	if (duyet[r]) continue;
        	duyet[r] = true;
        	++ kount;
        	Stack<Integer> st = new Stack<Integer>();
        	st.add(r);
        	while (!st.empty()) {
        		temp = st.pop();
        		duyet[temp] = true;
        		++ kount;
        		child1.gen[temp] = id2.gen[temp];
        		child2.gen[temp] = id1.gen[temp];
        		for (int c: GA.child[temp]) {
        			st.push(c);
        		}
        	}
        }
        
        /////////////////////////////////////////
        
        child1.gen[0] = 1;
        child2.gen[0] = 1;
        child1.setFitness();
        child2.setFitness();
        ArrayList<Individual> off_spring = new ArrayList<Individual>();
        off_spring.add(child1);
        off_spring.add(child2);
  		return off_spring;
    }
    public Individual  mutation(Individual id) {
       // Dot bien 4 gen trong gen bo
       for (int i= 0; i< GA.mutGen; ++i) {
    	   int p = 0;
//    	   while (! GA.isSetServer[p]) {
//    		   p = rd.nextInt(GA.genSize);
//    	   }
    	   id.gen[p] =(byte) (1 - id.gen[p]);
       }
       id.setFitness();
       return id;
    }
    public void run(){       
        for (int generation = 0; generation < GA.converge; ++ generation){
        	int holdIndi = (int) Math.floor(n* GA.holdBack);
            ArrayList<Individual> temp = new ArrayList<>(pop.subList(0, holdIndi));
            int k = n *2;
            double r;
            while (temp.size() < k) {
            	int id1 = 0; 
            	int id2 = 0;
            	while(id1 == id2) {
            		id1 = rd.nextInt(n);
            		id2 = rd.nextInt(n);
            	}
            	r = rd.nextDouble();
            	if (r < GA.crossoverRate) {
            		Individual par1 = pop.get(id1);         
            		Individual par2 = pop.get(id2);
            		ArrayList<Individual> offspring;
        
        			offspring = crossover(par1, par2);

            		r = rd.nextDouble();
                	if (r < GA.mutationRate) {
                		mutation(offspring.get(0));
                		mutation(offspring.get(1));
                	}
                	if (generation%GA.updateStep == 0) {
                		offspring.get(0).hotSetFitness();
                		offspring.get(1).hotSetFitness();
                	}
                	temp.addAll(offspring);
            	}
            }
            Collections.sort(temp);
            pop = new ArrayList<Individual>(temp.subList(0, n));
            System.out.println(generation+ " "+  pop.get(0).getFitness());
        }
    }
}
