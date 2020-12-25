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
public class GA {
    public static double mutationRate = 0.1;
    public static double crossoverRate = 0.9;
    public static int popSize = 50;
    public static Random rd = new Random();
   
    public static String target = "this is my first bloos!!!";
    public static int genSize = target.length();
    public static int converge = 100;
}
