package vod_ga;

import java.util.ArrayList;
import java.util.Random;

public class LocalSearch {
	public Individual init;
	public GA ga;
	public int[] tabuNum;
	public int tabuListSize = 30;
	public int tabuMax = 30;
	private ArrayList<Individual> tabuList = new ArrayList<>();
	public int maxIter = 200;	
	private int n=GA.numberOfNodes;
	
	public LocalSearch(Individual ind, GA ga) {
		this.init = ind;
		this.ga = ga;
	}
	private boolean checkSame(Individual ind1, Individual ind2) {
		for (int i=0; i<n; ++i) {
			if (ind1.gen[i]!=ind2.gen[i]) return false;
		}
		return true;
	}
	private boolean inTabuList(Individual candicate) {
		if (tabuList.size()==0) return false;
		for (Individual ind: tabuList) {
			if (checkSame(candicate, ind)) {
				return true;
			}
		}
		return false;
	}
	private ArrayList<Individual> getNeighbors(Individual ind){
		ArrayList<Individual> neighbors = new ArrayList<>();
		for (int i=1; i<n; ++i) {
			Individual neigh = ind.getCopy();
			neigh.gen[i] =(byte)(1 - ind.gen[i]);
			neigh.preMove = i;
			neigh.setFitness();
			neighbors.add(neigh);
		}
		return neighbors;
	}
	private void updateTSlist(Individual ind) {
		if (tabuList.size() < tabuListSize) tabuList.add(ind);
		for (Individual candicate: tabuList) {
			if (ind.getFitness() < candicate.getFitness()) {
				candicate = ind;
				break;
			}
		}
	}
	public Individual run() {
		Individual sBest = init;
		Individual bestCandidate = sBest;
		tabuNum = new int[n];
		for (int i =0; i<n;++i) {
			tabuNum[i] = -tabuMax;
		}
		tabuList.add(bestCandidate);
		int iter = 0;
		while (iter < maxIter) {
			ArrayList<Individual> sNeighbors = getNeighbors(bestCandidate);
			bestCandidate = sNeighbors.get(0);
			for (Individual ind: sNeighbors) {
				if ((ind.getFitness()<bestCandidate.getFitness()) &&  !inTabuList(ind)) {
					if(iter-tabuNum[ind.preMove]>=tabuMax) {
						bestCandidate = ind;
					}
				}
			}
			// update
			if (bestCandidate.getFitness() < sBest.getFitness()) {
				sBest = bestCandidate;
			}
			tabuNum[bestCandidate.preMove] = iter;
			updateTSlist(bestCandidate);
//			System.err.println("iter : " + iter + "  Fitness: "+ sBest.getFitness());
			++iter;
		}
		return sBest;
	}
	
	public static void main(String[] a) {
		GA ga = new GA();
		String filePath = "DataOut\\Type2_Large\\vod_900x500.txt";
        System.out.println(filePath);
        ga.filePath = filePath;
        ga.scanTest();
        ga.mod();
        Individual init = new Individual();
        init.init(new Random(0));
        System.out.println(init.getFitness());
        LocalSearch lc = new LocalSearch(init, ga);
        Individual res = lc.run();
        System.out.println(res.getFitness());
        res.show();
	}
}
