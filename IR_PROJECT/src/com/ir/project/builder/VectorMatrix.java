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

public class VectorMatrix {
	
	private static VectorMatrix instance;
	
	private List<List<Integer>>matrix;
	private List<String>documents;
	private LinkedHashSet<String>words;
	private Map<String, String> tfidfOfwordByDocument;
	private Map<String, Integer> documentFreqByWord;
	
	public VectorMatrix() {
		matrix=new ArrayList<List<Integer>>();
		documents=new ArrayList<String>();
		words=new LinkedHashSet<String>();
		tfidfOfwordByDocument = new HashMap<>();
		documentFreqByWord = new HashMap<>();
	}
	
	public static VectorMatrix getinstance() {
		if(instance!=null)
			return instance;
		instance= new VectorMatrix();
		return instance;
	}
	
	public void addFile(String file ,String fileName) {
		if(file!=null && fileName!=null) {
			fileName = fileName.replace(".txt", "");
			List<Integer> docRow=new ArrayList<Integer>();
			documents.add(fileName);
			String[]splittedDocument=file.split(" ");
			List<String>splittedDocumentList=new ArrayList<String>(Arrays.asList(splittedDocument));
			Map<String,Integer>wordCount=new HashMap<String,Integer>();
			for (String word : splittedDocumentList) {
				int count = wordCount.containsKey(word) ? wordCount.get(word) : 0;
				wordCount.put(word, count + 1);
			}
			
			int sizebefor=words.size();
			words.addAll(splittedDocumentList);
			words.remove("");
			addZeros(words.size()-sizebefor);
			for (String matrixworlds : words) {
				if(matrixworlds!=null) {
					if(wordCount.containsKey(matrixworlds)) {
							docRow.add(wordCount.get(matrixworlds));
					}else {
						docRow.add(0);
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
	
	private Integer getDfOfAWord(String word) {
		if(word==null || word.isEmpty() || !words.contains(word)) {
			return null;
		}
		Integer indexOfWord=indexOfWord(word);
		int count=0;
		for (List<Integer> row : matrix) {
			if(row.get(indexOfWord)>0)
				count++;
		}
		return count;
		
	} 
	
	private Integer indexOfWord(String word) {
		Integer index =0;
		for (String string : words) {
			if(string.equals(word))
				return index;
			index++;
		}
		return null;
	}

	public void toCsv() {
		BufferedWriter br=null;
		try {
			br = new BufferedWriter(new FileWriter("Vector.csv"));
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
	
	/*	for (int row =0;row<documents.size();row++) {
			for (int column =0;column<words.size();column++) {
				if(column==0) {
					sb.append(documents.get(row));
					sb.append(",");
				}
				sb.append(matrix.get(row).get(column));
				sb.append(",");
			}
			sb.append("\n");
		}*/
		sb.append("DF");
		sb.append(",");
		for (String element : words) {
			Integer dfOfAWord = getDfOfAWord(element);
			sb.append(dfOfAWord);
			documentFreqByWord.put(element, dfOfAWord);
			sb.append(",");
		}
		sb.append("\n");

		try {
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void tfidfToCsv() {
		BufferedWriter br=null;
		try {
			br = new BufferedWriter(new FileWriter("tfidf.csv"));
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
			int column =0;
			for (String word : words) {
				if(column==0) {
					sb.append(documents.get(row));
					sb.append(",");
				}
				double tfidf = tfidf(word,matrix.get(row).get(column));
				sb.append( tfidf);
				tfidfOfwordByDocument.put(documents.get(row)+"_"+word, String.valueOf(tfidf));
				sb.append(",");
				column++;
			}
			sb.append("\n");
		}
		sb.append("DF");
		sb.append(",");
		for (String element : words) {
			sb.append(getDfOfAWord(element));
			sb.append(",");
		}
		sb.append("\n");

		try {
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public double tfidf(String word,int tf) {
		double sizee=matrix.size() /Double.valueOf(getDfOfAWord(word)) ;
		return tf*(Math.log10(sizee));
	}
	public Map<String, String> getTfidfOfwordByDocument() {
		return tfidfOfwordByDocument;
	}
	public Map<String, Integer> getDocumentFreqByWord() {
		return documentFreqByWord;
	}
	public int getMatrixSize(){
		return matrix.size();
	}
	
	
}
