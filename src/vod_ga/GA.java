/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vod_ga;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  parameter of GA
 * @author Admin
 */
public class GA {
    public static String filePath = "DataOut\\Type1\\vod_100x10.txt";
    public static Scanner sc;
    public static int mutGen = 50;
    public static int updateStep = 20;
    public static double mutationRate = 0.2;
    public static double crossoverRate = 0.9;
    public static int popSize = 700;
    public static int SEED = 0;
    public static int genSize;
    public static int converge = 200;
    public static double holdBack = 0.3;
    
    public static byte split = 0; //this var decide the proportion of service
    public static double anpha = 0.5; //proportion of fitness between mom and dad
    
    public static LinkedList<Integer> child[];
    public static int[] parrent;
    public static double[][] assignCost;
    public static double[][] bandwidthCost;
    public static double[] setServerCost;
    public static boolean[] request[];
    public static double[] programSize;
    
    public static int numberOfNodes;
    public static int numberOfEdges;
    public static int numberOfProgram;
    
    // Node sure set server
//    public static boolean[] isSetServer;
//    public static ArrayList<Integer> server;
    public long runtime;
    public double fitness;
    
    // Stat for init
    public static int numForStat = 1000;
    public static double[] property0;
    public static double[] tTest0;
    public static double chooseProp = 0;
    /*
    nodes  = [0.1.......N]
    program = [0......p]
    link = [1.......N]
     */
    
    /**
     *  Test struct :
     *  n, p
     *  tree a -> b
     *  indx of node must increa int dfs
     *  assign cost [0...p-1][1...n]
     *  bandwidth cost [0...p-1][1....n]
     *  setserver cost [1....n]
     *  request[0...p-1][1...n] 0 - 1 value
     * 
     *  test OK!
     */
    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private void scan() {
        sc = new Scanner(System.in);
        int a, b;
        double c;
        // scan num of node, edges and program
        numberOfNodes = sc.nextInt();
        genSize = numberOfNodes;
        numberOfEdges = numberOfNodes - 1;
        numberOfProgram = sc.nextInt();

        //Init base
        parrent = new int[numberOfNodes];
        child = new LinkedList[numberOfNodes];
        for (int i = 0; i < numberOfNodes; ++i) {
            child[i] = new LinkedList<>();
        }

        //Scan tree
        for (int i = 0; i < numberOfEdges; ++i) {
            a = sc.nextInt();
            b = sc.nextInt();
            int min, max;
            min = Math.min(a, b);
            max = Math.max(a, b);

            child[min].add(max);
            parrent[max] = min;
        }

        //Init base
        assignCost = new double[numberOfProgram][numberOfNodes];
        bandwidthCost = new double[numberOfProgram][numberOfNodes];
        setServerCost = new double[numberOfNodes];
        request = new boolean[numberOfProgram][numberOfNodes];
        //Scan assign cost
        for (int i = 0; i < numberOfProgram; ++i) {
            //travse nodes 1 .... n
            for (int j = 1; j < numberOfNodes; ++j) {
                c = sc.nextDouble();
                assignCost[i][j] = c;
            }
        }

        // Scan Bandwidth
        for (int i = 0; i < numberOfProgram; ++i) {
            //travse nodes
            for (int j = 1; j < numberOfNodes; ++j) {
                c = sc.nextDouble();
                bandwidthCost[i][j] = c;
            }
        }

        //Scan setserver cost
        for (int i = 1; i < numberOfNodes; ++i) {
            c = sc.nextDouble();
            setServerCost[i] = c;
        }
//      System.err.println("succcccccccccccc!!!");
        //Scan request
        for (int i = 0; i < numberOfProgram; ++i) {
            //travse nodes 1 .... n
            for (int j = 1; j < numberOfNodes; ++j) {
                a = sc.nextInt();
                if (a == 1) {
                    request[i][j] = true;
                } else {
                    request[i][j] = false;
                }
            }
        }

        /*
        End of Scan
         */
    }
 
    
    private int time = 0;
    public static int mask[] ;
    public static int unmask[];
    public void scanTest(){
        try {
            sc = new Scanner(new File(filePath));
        } catch (FileNotFoundException ex) {
            System.err.print("File Not Found");
        }
        int a, b;
        double c;
        // scan num of node, edges and program
        String[] args = sc.nextLine().split(" ");
        numberOfNodes = Integer.valueOf(args[0]);
        genSize = numberOfNodes;
        numberOfEdges = numberOfNodes - 1;
        numberOfProgram = Integer.valueOf(args[1]);
        
        //Init base
        mask = new int[numberOfNodes];
        unmask = new int[numberOfNodes];
        parrent = new int[numberOfNodes];
        child = new LinkedList[numberOfNodes];
        for (int i = 0; i < numberOfNodes; ++i) {
            child[i] = new LinkedList<>();
        }
        assignCost = new double[numberOfProgram][numberOfNodes];
        bandwidthCost = new double[numberOfProgram][numberOfNodes];
        setServerCost = new double[numberOfNodes];
        request = new boolean[numberOfProgram][numberOfNodes];
        programSize = new double[numberOfProgram];
        //Scan tree and weight
        sc.nextLine();
        for (int i = 0; i < numberOfEdges; ++i) {
        	args = sc.nextLine().split(" ");
            a = Integer.valueOf(args[0]);
            b = Integer.valueOf(args[1]);
            c = Double.valueOf(args[2]);
            int min, max;
            min = Math.min(a, b);
            max = Math.max(a, b);

            child[min].add(max);
            parrent[max] = min;
            for (int j = 0; j < numberOfProgram; ++j){
                bandwidthCost[j][max] = c;
            }
        }

        //Mask the tree
        for (int i =0; i< numberOfNodes; ++i) unmask[mask[i]] = i;
         //Scan request
        sc.nextLine();
        while (true) {
        	args = sc.nextLine().split(" ");
        	if(!isNumeric(args[0])) {
//        		System.out.println(args[0]);
        		break;
        	}
//        	System.out.println(args[0]);
        	int n = Integer.valueOf(args[0]);
        	for (int i = 1; i < args.length; ++i) {
        		int p = Integer.valueOf(args[i]) - 1;
        		request[p][n] = true;
        	}
        }
        
        
        // Scan size of program 
//        sc.nextLine();
        while (true) {
        	args = sc.nextLine().split(" ");
        	if(!isNumeric(args[0])) {
//        		System.out.println(args[0]);
        		break;
        	}
//        	System.out.println(args[0]);
        	int n = Integer.valueOf(args[0]);
        	double s = Double.valueOf(args[1]);
        	programSize[n-1] = s;
        }
        
        //Scan setServerCost and assign program
        while (sc.hasNext()) {
        	args = sc.nextLine().split(" ");
        	int n = Integer.valueOf(args[0]);
        	double s = Double.valueOf(args[1]);
        	setServerCost[n] = s;
        	for (int i = 0; i < numberOfProgram; ++i) {
        		double temp = Double.valueOf(args[i+2]);
        		assignCost[i][n] = temp;
//        		System.out.println(temp);
        	}
        }
        /*
        End of Scan
         */
        
    }
    public void mod() {
        for (int i = 1; i < numberOfNodes; ++i) {
            for (int j = 0; j < numberOfProgram; ++j){
                bandwidthCost[j][i] = programSize[j]/bandwidthCost[j][i];
//                System.out.println("Node " + i + " pr " + j + " : " + bandwidthCost[j][i]);
            }
        }
        GA.setServerCost[0] = 0;
        
        // find node will sure set server
//        isSetServer = new boolean[numberOfNodes];
//        isSetServer[0] = true;
//        server = new ArrayList<Integer>();
//        for (int i = 1; i < numberOfNodes; ++i) {
//        	double serverSide = setServerCost[i];
//        	double nodeSide = 0;
//        	for (int j = 0; j < numberOfProgram; ++j) {
//        		serverSide += Math.min(assignCost[j][i], bandwidthCost[j][i]);
//        		nodeSide += bandwidthCost[j][i];
//        	}
//        	if (serverSide < nodeSide) isSetServer[i] =  true;
//        }
//        for (int i =0; i < numberOfNodes; ++i) {
//        	if (isSetServer[i]) server.add(i);
//        }
//        System.err.println("Size of sure server : " + server.size());
    }
    public void run(int seed){
    	long starttime = System.currentTimeMillis();
//		System.out.println("-------------------------------------------");
        /*
         * apply bandwidth cost
         */
    	GA.SEED = seed;
        Population pop = new Population();
        pop.init();
        pop.run();
        long finishTime = System.currentTimeMillis();
        LocalSearch lc = new LocalSearch(pop.pop.get(0), this);
        Individual ss = lc.run();
        System.err.println("Best: " + ss.getFitness());
        pop.pop.set(0, ss);
        runtime = finishTime - starttime;
        fitness = pop.pop.get(0).getFitness();
        System.out.print("Execution in : "+ (finishTime - starttime) + " miliseconds");
    }
    public static void main(String[] args) {    	
        GA ga = new GA();
        filePath = "DataOut\\Type2_Large\\vod_900x500.txt";
        System.out.println(filePath);
        ga.scanTest();
        ga.mod();
        ga.run(0);
    }
}
