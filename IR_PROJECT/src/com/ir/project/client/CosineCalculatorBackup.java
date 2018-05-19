package com.ir.project.client;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.ir.project.builder.IRBuilder;
import com.ir.project.builder.IRBuilderImpl;
import com.opencsv.CSVReader;

public class CosineCalculatorBackup {
	private String query;

	public CosineCalculatorBackup(String query) {
		this.query = query;
	}

	public void generateCos() {
		IRBuilder builder = new IRBuilderImpl();
		Set<String> stopWords = builder.buildStopWords();
		// String query = "using carpets in houses";

		String booleanCsvPath = "/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/boolean.csv";
		String tfidfCsvPath = "/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/tfidf.csv";
		String vectorCsvPath = "/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/Vector.csv";
		Map<String, String> booleanByWordMap = fillWordByDocumentWeightMap(booleanCsvPath);
		Map<String, String> tfidfByWordMap = fillWordByDocumentWeightMap(tfidfCsvPath);
		Map<String, Integer> documentFreqByWordMap = fillDocumentFreqByWordMap(vectorCsvPath);

		query = builder.filterOriginalFile(stopWords, query);

		Map<String, Double> booleanCosTable = builder.createBooleanCosTable(query, booleanByWordMap);
		toCsv("BooleanCosine.csv", booleanCosTable);

		Map<String, Double> tfidfCosTable = builder.createTfidfCosTable(query, tfidfByWordMap, documentFreqByWordMap);
		toCsv("tfidfCosine.csv", tfidfCosTable);
	}

	private Map<String, Integer> fillDocumentFreqByWordMap(String csvFile) {

		List<String> wordsList = new ArrayList<>();
		Map<String, Integer> map = new TreeMap<>();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] line;
			while ((line = reader.readNext()) != null) {
				if (line[0].contains("Document/word")) {
					for (int i = 1; i < line.length; i++) {
						String word = line[i];
						if (!word.isEmpty())
							wordsList.add(word);
					}
					continue;
				}
				if (line[0].contains("DF")) {
					int wordValueCounter = 1;
					for (int i = 0; i < wordsList.size(); i++) {
						String df = line[wordValueCounter];
						map.put(wordsList.get(i), Integer.valueOf(df));
						wordValueCounter++;
					}
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;

	}

	private Map<String, String> fillWordByDocumentWeightMap(String csvFile) {
		List<String> wordsList = new ArrayList<>();
		Map<String, String> map = new TreeMap<>();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] line;
			while ((line = reader.readNext()) != null) {
				if (line[0].contains("Document/word")) {
					for (int i = 1; i < line.length; i++) {
						String word = line[i];
						if (!word.isEmpty())
							wordsList.add(word);
					}
					continue;
				}
				if (line[0].contains("DF"))
					continue;

				int wordValueCounter = 1;
				for (int i = 0; i < wordsList.size(); i++) {
					map.put(line[0] + "_" + wordsList.get(i), line[wordValueCounter]);
					wordValueCounter++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	private void toCsv(String csvName, Map<String, Double> documentsByCos) {
		documentsByCos = documentsByCos.entrySet().stream() 
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed()) 
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(csvName));
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
			value = value.toString().equalsIgnoreCase("Nan") ? 0d : value;
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
