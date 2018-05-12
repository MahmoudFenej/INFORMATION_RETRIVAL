package com.ir.project.builder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class BooleanMatrix {
	
	private static BooleanMatrix instance;
	
	private List<List<Integer>>matrix;
	private List<String>documents;
	private LinkedHashSet<String>words;
	private Map<String, String> wordStateByDocument;
	
	public BooleanMatrix() {
		matrix=new ArrayList<List<Integer>>();
		documents=new ArrayList<String>();
		words=new LinkedHashSet<String>();
		wordStateByDocument = new HashMap<>();
	}
	
	public static BooleanMatrix getinstance() {
		if(instance!=null)
			return instance;
		instance= new BooleanMatrix();
		return instance;
	}
	
	public void addFile(String file ,String fileName) {
		if(file!=null && fileName!=null) {
			fileName = fileName.replace(".txt", "");
			List<Integer> docRow=new ArrayList<Integer>();
			documents.add(fileName);
			String[]splittedDocument=file.split(" ");
			List<String>splittedDocumentList=new ArrayList<String>(Arrays.asList(splittedDocument));
			int sizebefor=words.size();
			words.addAll(splittedDocumentList);
			words.remove("");
			addZeros(words.size()-sizebefor);
			for (String matrixWord : words) {
				if(matrixWord!=null) {
					if(splittedDocumentList.contains(matrixWord)) {
						docRow.add(1);
						wordStateByDocument.put(fileName+"_"+matrixWord, "1");
					}else {
						docRow.add(0);
						wordStateByDocument.put(fileName+"_"+matrixWord,"0");
					}
				}
			}
			
			matrix.add(docRow);
		}
	}
	
	private void addZeros(int newAdded) {
		for (List<Integer> row : matrix) {
			for (int i=0;i<newAdded;i++) {
				row.add(0);
			}

		}

	}

	public void toCsv() {
		BufferedWriter br=null;
		try {
			br = new BufferedWriter(new FileWriter("boolean.csv"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Document/word");
		sb.append(",");
		for (String element : words) {
			sb.append(element);
			sb.append(",");
		}
		sb.append("\n");
	
		for (int row =0;row<documents.size();row++) {
			for (int column =0;column<words.size();column++) {
				if(column==0) {
					sb.append(documents.get(row));
					sb.append(",");
				}
				sb.append(matrix.get(row).get(column));
				sb.append(",");
			}
			sb.append("\n");
		}


		try {
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public Map<String, String> getWordStateByDocument() {
		return wordStateByDocument;
	}
}
