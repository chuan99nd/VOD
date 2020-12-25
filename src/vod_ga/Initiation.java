package vod_ga;

import java.util.ArrayList;
import java.util.Random;

public class Initiation {
	private double maxTValue = -10000;
	public Individual run(int seed, int statPopSize) {
		/*
		 * Get property of 0 exist in gene i by array property0
		 * Return best of individual in statistic analysist
		 */
		ArrayList<Individual> pop = new ArrayList<Individual>();
		Random rd = new Random(seed);
	    int genSize = GA.genSize;
		Individual minInd = new Individual();
		minInd.init(rd);
		minInd.setFitness();
		GA.property0 = new double[genSize];
		GA.tTest0 = new double[genSize];
        for (int i = 0; i< statPopSize; ++i){
            Individual temp = new Individual();
            temp.init(rd);
            temp.hotSetFitness();
            pop.add(temp);
            if (minInd.getFitness() > temp.getFitness())
            	minInd = temp;
	    }
        // Iter n gen end get property of 0 in each gen
        for (int gene = 1; gene < genSize; ++gene) {
        	ArrayList<Individual> A0 = new ArrayList<Individual>();
        	ArrayList<Individual> A1 = new ArrayList<Individual>();
        	double sum0 = 0, sum1 = 0;
        	for (Individual ind: pop) {
        		 if (ind.gen[gene] == 0) {
        			 A0.add(ind);
        			 sum0 += ind.getFitness();
        		 } else {
        			 A1.add(ind);
        			 sum1 += ind.getFitness();
        		 }
        	}
        	int n0 = A0.size();
        	int n1 = A1.size();
        	// Check special case
        	if (n0 == 0) {
        		GA.property0[gene] = 0; 
        		continue;
        	}
        	if (n1 == 0) {
        		GA.property0[gene] = 1;
        		continue;
        	}
        	double mean0 = sum0/n0, mean1 = sum1/n1;
        	double sumSquare0 = 0, sumSquare1 = 0;
        	for (Individual ind: A0) {
        		double temp = (ind.getFitness() - mean0);
//        		System.out.println("    Sample difference: "+ temp);
        		sumSquare0 += temp*temp;
        	}
        	for (Individual ind: A1) {
        		double temp = (ind.getFitness() - mean1);
        		sumSquare1 += temp*temp;
        	}
        	double sp = Math.sqrt((sumSquare0 + sumSquare1)/GA.numForStat);
        	GA.tTest0[gene] = (mean1 - mean0)/(sp * Math.sqrt(1.0/n0 + 1.0/n1));
        	maxTValue = Math.max(maxTValue, Math.abs(GA.tTest0[gene]));
//        	System.out.println("mean 0: " + mean0 + " mean 1: " + mean1 + " dev: " + Math.sqrt(sumSquare0/n0) + "  " + Math.sqrt(sumSquare1/n1));
//        	System.out.println("     So luong: " + n0 + "   " + n1);
//        	System.out.println("     T-test value: " + sp + "  " +  GA.tTest0[gene]);
        }
        double maxProp = 6;
        for (int gene = 1; gene < genSize; ++gene) {
        	if (GA.tTest0[gene] == 0) continue;
        	GA.property0[gene] = 0.5 + (Math.min(1, GA.tTest0[gene]/maxProp))*0.5;
//        	GA.property0[gene] = 0.5;
        }
		return minInd;
	}
	private double cumulativeGause(double x, double mean, double devitation){
		double gaussePoint = (x - mean)/devitation;
		double division = 1 + Math.exp(-0.07056*gaussePoint*gaussePoint*gaussePoint - 1.5976*gaussePoint);
		return 1/division;
	}
	public static void main(String a[]) {
		Initiation i = new Initiation();
		double aa = i.cumulativeGause(1.5, 0, 2);
		System.err.println(aa);
	}
}
