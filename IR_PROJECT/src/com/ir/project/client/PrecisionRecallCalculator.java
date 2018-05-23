package com.ir.project.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.ir.project.builder.IRBuilderImpl;
import com.opencsv.CSVReader;

public class PrecisionRecallCalculator {
	public static void main(String[] args) {
		GenerateRecallPrecision("1");

	}

	public PrecisionRecallCalculator(String queryNumber) {
		GenerateRecallPrecision(queryNumber);
	}

	public static void GenerateRecallPrecision(String queryNumber) {
		Map<String, Set<String>> relevanceDocByQueryMap = getRelevanceDocByQueryMap();
		Map<String, Double> cosineReleventDocuments = getCosineReleventDocuments();
cosineReleventDocuments = cosineReleventDocuments .entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		long documentCount = cosineReleventDocuments.values().stream().filter(e -> e != 0d).count();
		Set<String> documentSet = relevanceDocByQueryMap.get(queryNumber);
		List<Double> precisionList = new ArrayList<>();
		List<Double> recallList = new ArrayList<>();
		double retreived = 0, relevantRetreived = 0, relevant = documentCount;
		for (String docName : cosineReleventDocuments.keySet()) {
		    if(docName.contains("doc")){
                     docName = docName.replace("doc","").replace(".TXT",""); 
                     }	
                   if (documentSet.contains(docName) && cosineReleventDocuments.get(docName) != 0d)
				relevantRetreived++;
			retreived++;
			double precisionResult = relevantRetreived / retreived;
			double recallResult = relevantRetreived / relevant;
			precisionList.add(precisionResult);
			recallList.add(recallResult);
		}
                toCsv("precision.csv,precisionList");
                toCsv("recall.csv,recallList");
		new LineChart(precisionList, "Precision");
		new LineChart(recallList, "Recall");
	}

	private static Map<String, Set<String>> getRelevanceDocByQueryMap() {
		Map<String, Set<String>> map = new HashMap<>();
		URL urlToFile = IRBuilderImpl.class.getResource("/relevance.txt");
		String path = urlToFile.getPath();
		if (path.contains(":"))
			path = path.replaceAll("/C:", "");
		try {
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String queryNumber = st.nextToken();
				String relDocument = st.nextToken();
				if (map.get(queryNumber) == null)
					map.put(queryNumber, new HashSet<>());

				map.get(queryNumber).add(relDocument);

			}
			fileReader.close();
			System.out.println(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	private static Map<String, Double> getCosineReleventDocuments() {
		String path = "/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/tfidfCosine.csv";
		List<String> wordsList = new ArrayList<>();
		Map<String, Double> map = new HashMap<>();
		CSVReader reader = null;
		try {
			File file = new File(path);
			reader = new CSVReader(new FileReader(file));
			String[] line;
			while ((line = reader.readNext()) != null) {
				if (line[0].contains("query")) {
					for (int i = 0; i < line.length; i++) {
						String word = line[i];
						if (!word.isEmpty())
							wordsList.add(word.split("/")[0]);
					}
					continue;
				}
				int wordValueCounter = 0;
				for (int i = 0; i < wordsList.size(); i++) {
					String df = line[wordValueCounter];
					map.put(wordsList.get(i), Double.valueOf(df));
					wordValueCounter++;
				}
				continue;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;

	}

private void toCsv(String csvName, List<Double> documentsByCos) {


		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(csvName));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		documentsByCos.forEach(e -> {
			e = e.toString().equalsIgnoreCase("Nan") ? 0d : e;
			sb.append(e);
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
