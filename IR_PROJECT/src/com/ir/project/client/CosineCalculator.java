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
import com.ir.project.builder.IRFilterUtil;
import com.ir.project.builder.TfidfCosMatrix;
import com.opencsv.CSVReader;

public class CosineCalculator {
	private String query;

	public CosineCalculator(String query) {
		this.query = query;
	}

	public void generateCos() {
		IRBuilder builder = new IRBuilderImpl();
		Set<String> stopWords = builder.buildStopWords();

		String booleanCsvPath = "/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/boolean.csv";
		String tfidfCsvPath = "/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/tfidf.csv";
		String vectorCsvPath = "/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/Vector.csv";
		long documentCount = IRFilterUtil.documentCount();
		Map<String, Integer> documentFreqByWordMap = fillDocumentFreqByWordMap(vectorCsvPath);

		query = builder.filterOriginalFile(stopWords, query);

		Map<String, Double> booleanCosine = calulateCosine(booleanCsvPath, documentFreqByWordMap, documentCount);
		toCsv("booleanCosine.csv", booleanCosine);

		Map<String, Double> tfidfCosine = calulateCosine(tfidfCsvPath, documentFreqByWordMap, documentCount);
		toCsv("tfidfCosine.csv", tfidfCosine);

	}

	private double getQueryTfidf(String word, String[] queryWrods, int matrixSize,
			Map<String, Integer> documentFreqMap) {
		int tf = 0;
		for (String queryWord : queryWrods) {
			if (queryWord.equals(word)) {
				tf++;
			}
		}
		Integer dfByWord = documentFreqMap.get(word);
		Integer integer = dfByWord == null ? 0 : dfByWord;
		double sizee = matrixSize / Double.valueOf(integer);

		return tf * (Math.log10(sizee));

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

	private Map<String, Double> calulateCosine(String csvFile, Map<String, Integer> documentFreqMap,
			long documentCount) {
		List<String> wordSet = new ArrayList<>();
		TfidfCosMatrix matrix = TfidfCosMatrix.getinstance();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] line;
			while ((line = reader.readNext()) != null) {
				if (line[0].contains("Document/word")) {
					for (int i = 1; i < line.length; i++) {
						String word = line[i];
						if (!word.isEmpty())
							wordSet.add(word);
					}
					continue;
				}
				if (line[0].contains("DF"))
					continue;

				String fileName = line[0];
				String[] queryWrods = query.split(" ");
				int matrixSize = (int) documentCount;
				double sigmaTiXTj = 0;
				double sigmaTi2 = 0;
				double sigmaTj2 = 0;
				int i = 1;
				for (String word : wordSet) {
					double wordByDocVal = line[i] == null ? 0 : Double.valueOf(line[i]);
					double queryByDocVal = getQueryTfidf(word, queryWrods, matrixSize, documentFreqMap);
					sigmaTiXTj += wordByDocVal * queryByDocVal;
					sigmaTi2 += Math.pow(wordByDocVal, 2);
					sigmaTj2 += Math.pow(queryByDocVal, 2);
					i++;
				}
				double sigmaTi2XSigmaTj2 = sigmaTi2 * sigmaTj2;
				double res = sigmaTiXTj / Math.sqrt(sigmaTi2XSigmaTj2);

				matrix.addFile(res, fileName);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return matrix.getDocumentsByCos();
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

	public static void main(String[] args) {
		new CosineCalculator("the crystalline lens in vertebrates, including humans.").generateCos();
	}
}
