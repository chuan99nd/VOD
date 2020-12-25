package vod_ga;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.Scanner;


public class Modify {
	String filePath;
	String dest;
	FileWriter fw;
	BufferedWriter bw;
	GA  ga;
	LinkedList<Integer> adj[];
	
	boolean duyet[];
	double band[];
	int t1[], t2[];
	int time = 0;
	private void dfs(int u) {
		duyet[u] = true;
		ga.mask[u] = time;
		ga.unmask[ga.mask[u]] = u;
		time ++;
//		System.out.println(time);
		for (int v : adj[u]) if (!duyet[v]){
			duyet[u] = true;
			dfs(v);
			System.out.println(u+ " " + v);
		}
	}
	private void scan() {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
		}
		ga.scanTest();
		
		int n = Integer.valueOf(sc.nextLine().split(" ")[0]);
//		System.out.println(n);
		sc.nextLine();
		band = new double[n];
		duyet = new boolean[n];
		adj = new LinkedList[n];
		t1 = new int[n];
		t2 = new int[n];
		for (int i = 0; i <n; ++i) adj[i] = new LinkedList<Integer>();
		for (int i = 0; i < n-1;++i) {
			int a, b;
			double c;
			String s[] = sc.nextLine().split(" ");
			a = Integer.valueOf(s[0]);
			b = Integer.valueOf(s[1]);
			c = Double.valueOf(s[2]);
			
		
			band[i] = c;
			t1[i] = a;
			t2[i] = b;
			adj[a].add(b);
			adj[b].add(a);
		}
		dfs(0);
	}
	public void mod() {
		try {
			bw = new BufferedWriter(fw);
			bw.write(ga.numberOfNodes+ " "+ ga.numberOfProgram + "\n");
			bw.write("TREE NETWORK (nID nID Weight):\r\n" );
			for (int i = 0; i < ga.numberOfEdges; ++i) {
				bw.write(ga.mask[t1[i]] + " " + ga.mask[t2[i]] + " " + band[i] +" \n");
			}
			bw.write("PROGRAMS ASSIGNED TO NODES (nID pID1 pID2 ...):\r\n" );
			for (int i = 0; i < ga.numberOfNodes; ++i) {
				bw.write(ga.mask[i]+ " ");
				for (int j = 0; j < ga.numberOfProgram; ++j) {
					if (ga.request[j][i]) bw.write((j+1) + " ");
				}
				bw.write(" \n");
			}
			bw.write("SIZE OF PROGRAMS (pID SIZE):\r\n" );
			for (int i = 1; i <= ga.numberOfProgram; ++i) {
				bw.write(i + " " + ga.programSize[i-1]);
				bw.write(" \n");
			}
			bw.write("COST_INSTALL_SERVER_AND_PROGRAMS :\r\n");
			for (int i = 0; i < ga.numberOfNodes; ++ i) {
				bw.write(ga.mask[i] + " ");
				bw.write(ga.setServerCost[i] + " ");
				for (int j = 0; j < ga.numberOfProgram; ++j) bw.write(ga.assignCost[j][i] + " ");
				bw.write(" \n");
			}
			bw.close();
		} catch (IOException e) {
			System.out.println(e);
		}

	}
	public Modify(String filePath, String dest, GA ga) {
		this.filePath = filePath;
		this.ga = ga;
		try {
			fw = new FileWriter(new File(dest));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ga.filePath = this.filePath;
//		for (int i : adj[0])
//			System.out.println(i);
		scan();
		for (int i = 0; i < ga.numberOfNodes; ++i) {
			System.out.println(i + " : " + ga.mask[i]);
		}
		mod();
	}
	public static void main(String[] args) {
		String fileName = "vod_20x10.txt";
		String src = "NewData\\\\Type1\\";
		String dt = "ModData\\Type1\\";
		Modify md = new Modify(src + fileName, dt + fileName, new GA());
	}
}
