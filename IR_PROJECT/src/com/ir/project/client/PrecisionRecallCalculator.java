package com.ir.project.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import com.ir.project.builder.IRBuilderImpl;

public class PrecisionRecallCalculator {

	public static void main(String[] args) {
		Map<String, Set<String>> relevanceDocByQueryMap = getRelevanceDocByQueryMap();
		long documentCount = documentCount();
		Set<String> documentSet = relevanceDocByQueryMap.get("1");
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

	private static long documentCount() {
		URL urlToFile = IRBuilderImpl.class.getResource("/ORIGIAL_DOCUMENT");
		String path = urlToFile.getPath();
		if (path.contains(":"))
			path = path.replaceAll("/C:", "");
		try (Stream<Path> files = Files.list(Paths.get(path))) {
			return files.count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
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

}
