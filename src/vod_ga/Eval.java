/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vod_ga;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Admin
 */
public class Eval {
    private Individual ind;
    private int program;
    private int n = GA.numberOfNodes;
    ArrayList<Integer> ARes = new ArrayList<Integer>();
    ArrayList<Integer> AStar[] = new ArrayList[n];
    ArrayList<Integer> BStar[] = new ArrayList[n];
    
    double value;
    double bonus = 0;
    double G[] = new double[n];
    double GN[] = new double[n];
    public Eval(Individual ind, int program) {
        this.ind = ind;
        this.program = program;
    }
    public void run(){
        AStar[0] = new ArrayList();
        BStar[0] = new ArrayList<Integer>();
        for (int i = n-1; i >0; --i){
            AStar[i] = new ArrayList();
            BStar[i] = new ArrayList<Integer>();
            double A, B, C;
            A = (ind.gen[i] == 1 ? GA.assignCost[program][i] : Double.POSITIVE_INFINITY) + G[i];
            B = (GA.request[program][i] ? Double.POSITIVE_INFINITY : 0) + GN[i];
            C = GA.bandwidthCost[program][i] + G[i];
            
            //A and B are both finite and infinit
            if (A <= B){
                for (int c : GA.child[i]){
//                    BStar[i].addAll(AStar[c]);
                }
                GN[i] = A;
            } else {
                GN[i] = B;
            }
            GN[GA.parrent[i]] += GN[i];
            if (GN[i] <= C){
                G[i] = GN[i];
            } else {
                G[i] = C;
            }
            G[GA.parrent[i]] += G[i];
            
//            System.out.println("Node "+ i+" : ");
//            AStar[i].show();
//            BStar[i].show();

        }
        this.value = G[0];
        // debug
//        String sv = "";
//        for (int i = 1; i < GA.numberOfNodes; ++i) sv=sv + this.ind.gen[i]+  "  ";
//        System.out.println(sv);
//        ARes.show();
//        String s = "";
//        for (int i : ARes) {
//        	s = s +i + " ";
//        }
//        System.out.println("program in node : " + s);
//        System.out.println("cost : "+ G[0]);

        
    }
    public void hotRun(){
        AStar[0] = new ArrayList();
        BStar[0] = new ArrayList<Integer>();
        for (int i = n-1; i >0; --i){
            AStar[i] = new ArrayList();
            BStar[i] = new ArrayList<Integer>();
            double A, B, C;
            A = (ind.gen[i] == 1 ? GA.assignCost[program][i] : Double.POSITIVE_INFINITY) + G[i];
            B = (GA.request[program][i] ? Double.POSITIVE_INFINITY : 0) + GN[i];
            C = GA.bandwidthCost[program][i] + G[i];
            
            //A and B are both finite and infinit
            if (A <= B){
                for (int c : GA.child[i]){
                    BStar[i].addAll(AStar[c]);
                }
                BStar[i].add(i);
                GN[i] = A;
            } else {
                for (int c : GA.child[i]){
                    BStar[i].addAll(BStar[c]);
                }
                GN[i] = B;
            }
            GN[GA.parrent[i]] += GN[i];
            if (GN[i] <= C){
                AStar[i] = BStar[i];
                G[i] = GN[i];
            } else {
                for (int c : GA.child[i]){
                    AStar[i].addAll(AStar[c]);
                }
                G[i] = C;
            }
            G[GA.parrent[i]] += G[i];
        }
        for (int i: GA.child[0]) {
        	ARes.addAll(AStar[i]);
        }
        this.value = G[0];
    }
}
