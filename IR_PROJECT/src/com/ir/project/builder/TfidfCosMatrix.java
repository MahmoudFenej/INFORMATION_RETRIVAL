package com.ir.project.builder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class TfidfCosMatrix {

	private static TfidfCosMatrix instance;

	private Map<String, Double> documentsByCos;

	public TfidfCosMatrix() {
		documentsByCos = new TreeMap<>();
	}

	public static TfidfCosMatrix getinstance() {
		if (instance != null)
			return instance;
		instance = new TfidfCosMatrix();
		return instance;
	}

	public void addFile(double cos, String fileName) {
		documentsByCos.put(fileName, cos);
	}

	public void toCsv() {
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter("tfidfCosine.csv"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		for (String element : documentsByCos.keySet()) {
			sb.append(element + "/query");
			sb.append(",");
		}
		sb.append("\n");
		documentsByCos.forEach((key, value) -> {
			sb.append(value);
			sb.append(",");
		});
		sb.append("\n");

		try {
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
