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
import com.ir.project.builder.IRFilterUtil;

public class PrecisionRecallCalculator {
	public static void main(String[] args) {
		GenerateRecallPrecision("1");

	}
	public PrecisionRecallCalculator(String queryNumber) {
		GenerateRecallPrecision(queryNumber);
	}



	public static void GenerateRecallPrecision(String queryNumber) {
		Map<String, Set<String>> relevanceDocByQueryMap = getRelevanceDocByQueryMap();
		long documentCount = IRFilterUtil.documentCount();
		Set<String> documentSet = relevanceDocByQueryMap.get(queryNumber);
		List<Double> precisionList = new ArrayList<>();
		List<Double> recallList = new ArrayList<>();
		double retreived = 0, relevantRetreived = 0, relevant = documentSet.size();
		for (int docIndex = 1; docIndex <= documentCount; docIndex++) {
			if (documentSet.contains(String.valueOf(docIndex)))
				relevantRetreived++;
			retreived++;
			double precisionResult = relevantRetreived / retreived;
			double recallResult = relevantRetreived / relevant;
			precisionList.add(precisionResult);
			recallList.add(recallResult);
		}
		new LineChart(precisionList,"Precision");
		new LineChart(recallList,"Recall");
	}

	private static Map<String, Set<String>> getRelevanceDocByQueryMap() {
		Map<String, Set<String>> map = new HashMap<>();
		URL urlToFile = IRBuilderImpl.class.getResource("/MED.txt");
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

}
