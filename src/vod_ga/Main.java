package vod_ga;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.Random;

import javax.swing.JOptionPane;

public class Main {
	public static void getMax() {
		File file = new File("DataOut");
		for (File subtest: file.listFiles()) if (subtest.isDirectory()){
			String i = subtest.getName();
			PrintStream fo;
			File folder = new File("DataOut"+ "\\"+i);
			try {
				fo = new PrintStream("CanTren"+ "\\"+i +".txt");
				System.setOut(fo);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			File[] listOfFiles = folder.listFiles();
			for (File ff : listOfFiles) {
				System.err.println(ff.getName());
				String fileName = ff.getName();
//				Get Max
				GA.filePath = "DataOut\\"+i+"\\" + fileName;
				GA ga = new GA();
				ga.scanTest();
				ga.mod();
				Individual id = new Individual();
				id.init(new Random());
				id.gen[0] = 1;
				for (int it = 1; it < GA.genSize; ++it) {
					id.gen[it] = 0;
				}
				id.setFitness();
				System.out.println(fileName+ " : " + id.getFitness());
				System.err.println(fileName);
			}
		}
	}
	static void purgeDirectory(File dir) {
	    try {
			for (File file: dir.listFiles()) {
			    if (file.isDirectory())
			        purgeDirectory(file);
			    file.delete();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Delete old test Ok!");
		}
	}
	/**
	 * modify data to true dfs index
	 * @param testPath
	 */
	public static void modify(String testPath) {
		File file = new File(testPath);
		purgeDirectory(new File("DataOut"));		
		for (File subtest: file.listFiles()) if (subtest.isDirectory()){
			String i = subtest.getName();
			File folder = new File(testPath+ "\\"+i);
			File[] listOfFiles = folder.listFiles();
			for (File ff : listOfFiles) {
				System.err.println(ff.getName());
				String fileName = ff.getName();
				String fileOut = "DataOut\\"+ i+ "\\" + fileName;
				File f = new File("DataOut\\"+i);
				f.mkdirs();
				PrintStream fo;
				try {
					fo = new PrintStream(fileOut);
					System.setOut(fo);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
//				Modify data
				String src = testPath + "\\"+i+"\\" + fileName;
				String out = "DataOut\\"+i+"\\" + fileName;
				Modify md = new Modify(src, out, new GA());
				System.err.println(src);
			}
		}
		
	}
	public static void run(int startSeed, int endSeed) {
		File file = new File("DataOut");
		for (int it = 0; it < 30; ++it) {
			for (File subtest: file.listFiles()) {
				String i = subtest.getName();
				File folder = new File("DataOut\\"+i);
				File[] listOfFiles = folder.listFiles();
				for (File ff : listOfFiles) {
					String fileName = (ff.getName().replaceFirst("[.][^.]+$", ""));
					GA ga = new GA();
					GA.filePath = "DataOut\\"+i+"\\" + fileName + ".txt";
					ga.scanTest();
					ga.mod();
					File f = new File("Result\\"+i+"\\" + fileName);
					f.mkdirs();
					String fileOut = "Result\\"+ i+ "\\" + fileName + "\\" + fileName +"_seed_" + it+ ".txt";
					PrintStream fo;
					try {
						fo = new PrintStream(fileOut);
						System.setOut(fo);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Generations "+ fileName);
					//Run GA here
					ga.run(it);
					fileOut = "Result\\"+ i+ "\\" + fileName + "\\" + fileName +"_seed_" + it+ ".opt";
					try {
						fo = new PrintStream(fileOut);
						System.setOut(fo);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Filename: " + fileName);
					System.out.println("Seed: " + it);
					System.out.println("Fitness: "+ ga.fitness);
					LocalTime time = LocalTime.ofNanoOfDay(ga.runtime * 1000000);
					System.out.println("Time: " + time);
					System.err.println(i +  " : " +  fileName+ " Seed:" + it + " Fitness:" +ga.fitness + " Time:"+ time);
				}
			}
		}
		
	}
	public static void main(String[] args) {
		System.err.println(args[0]);
		System.err.print("GA with statistic init");
		String testDir = args[0];
		int startSeed = Integer.valueOf(args[1]);
		int endSeed = Integer.valueOf(args[2]);
//		
		Main.modify(testDir);
		Main.getMax();
		run(startSeed, endSeed);
		String st = "Vod OK";
		JOptionPane.showMessageDialog(null, st);
	}
}	
