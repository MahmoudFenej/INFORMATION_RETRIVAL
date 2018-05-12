package com.ir.project.builder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IRBuilderImpl implements IRBuilder {

	private static IRFilterUtil filterUtil = new IRFilterUtil();

	public Set<String> buildStopWords() {
		URL urlToFile = IRBuilderImpl.class.getResource("/stopList.txt");
		Stream<String> lines = null;
		try {
			String path = urlToFile.getPath();
			if(path.contains(":"))
				path = path.replaceAll("/C:", "");
			lines = Files.lines(Paths.get(path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return lines.collect(Collectors.toSet());
	}

	public Map<String, Stream<String>> buildOriginalFiles() throws Exception {
		Map<String, Stream<String>> map = new TreeMap<>();
		URL urlToFile = IRBuilderImpl.class.getResource("/ORIGIAL_DOCUMENT");
		String path = urlToFile.getPath();
		if(path.contains(":"))
			path = path.replaceAll("/C:", "");
		@SuppressWarnings("resource")
		Stream<Path> files = Files.list(Paths.get(path));
		files.forEach(e -> {
			try {
				Stream<String> lines = Files.lines(e.toAbsolutePath());
				map.put(e.getFileName().toString(), lines);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});
		return map;
	}

	public String filterOriginalFile(Set<String> stpSet, String st) {
		String withoutStopWord = filterUtil.replaceStopWords(stpSet, st);
		String withoutPunctutaion = filterUtil.replacePunctuation(withoutStopWord);
		String withoutCommas = filterUtil.replaceCommas(withoutPunctutaion);
		String withoutDiez = filterUtil.replaceDiez(withoutCommas);
		String withoutsemiColumn=filterUtil.replacesemiColumn(withoutDiez);
		String stemmredDocument=filterUtil.stemmring(withoutsemiColumn);
		return stemmredDocument;
	}

	public String buildStpFiles(String st, String fileName) {
		fileName = fileName.replace(".txt", "");
		File file = new File("/Users/mahmo/Desktop/IR_WORKSPACE/IR_PROJECT/src/STP_DOCUMENT/" + fileName + ".stp");
		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Files.write(Paths.get(file.getPath()), st.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();

		}
		return st;
	}

	@Override
	public void createBooleanMatrix(String filterredText, String fileName) {
		if(filterredText!=null) {
		BooleanMatrix matrix=BooleanMatrix.getinstance();
		matrix.addFile(filterredText,fileName);
		}
	}
	
	@Override
	public void createVectorMatrix(String filterredText, String fileName) {
		if(filterredText!=null) {
		VectorMatrix matrix=VectorMatrix.getinstance();
		matrix.addFile(filterredText,fileName);
		}
	}
	private Integer searchQueryValueInDocumentsMap(String word,String[] queryWrods) {
		for(String queryWord : queryWrods){
			if(word.equals(queryWord)){
				return 1;
			}
		}
		return 0;
	
	}
	@Override
	public Map<String, Double> createBooleanCosTable(String query,
			Map<String, String> wordStateByDocument) {
		Map<String, Double> documentsByCos = new HashMap<>();
		String[] queryWrods = query.split(" ");
		Set<String> doucumentSet = wordStateByDocument.keySet().stream().map(e -> e.split("_")[0])
				.collect(Collectors.toSet());
		Set<String> wordSet = wordStateByDocument.keySet().stream().map(e -> e.split("_")[1])
				.collect(Collectors.toSet());
		for (String fileName : doucumentSet) {
			int sigmaTiXTj = 0;
			int sigmaTi2 = 0;
			int sigmaTj2 = 0;
			for (String word : wordSet) {
				Integer wordByDocVal = wordStateByDocument.get(fileName+"_"+word) == null ? 0 : Integer.valueOf(wordStateByDocument.get(fileName+"_"+word));
				Integer queryByDocVal = searchQueryValueInDocumentsMap(word, queryWrods);
				sigmaTiXTj += wordByDocVal * queryByDocVal;
				sigmaTi2 += Math.pow(wordByDocVal, 2);
				sigmaTj2 += Math.pow(queryByDocVal, 2);

			}
			double sigmaTi2XSigmaTj2 = sigmaTi2 * sigmaTj2;
			double res = sigmaTiXTj/ Math.sqrt(sigmaTi2XSigmaTj2);

			BooleanCosMatrix matrix = BooleanCosMatrix.getinstance();
			matrix.addFile(res, fileName);
			documentsByCos.put(fileName, res);

		}
		return documentsByCos;
	}
	@Override
	public Map<String, Double> createTfidfCosTable(String query,
			Map<String, String> tfidfByDocumentMap, Map<String, Integer> documentFreqMap) {
		Map<String, Double> documentsByCos = new HashMap<>();

		String[] queryWrods = query.split(" ");
		Set<String> doucumentSet = tfidfByDocumentMap.keySet().stream().map(e -> e.split("_")[0])
				.collect(Collectors.toSet());
		int matrixSize = doucumentSet.size();
		Set<String> wordSet = tfidfByDocumentMap.keySet().stream().map(e -> e.split("_")[1])
				.collect(Collectors.toSet());
		doucumentSet = new TreeSet<>(doucumentSet);
		for (String fileName : doucumentSet) {
			double sigmaTiXTj = 0;
			double sigmaTi2 = 0;
			double sigmaTj2 = 0;
			for (String word : wordSet) {
				double wordByDocVal = tfidfByDocumentMap.get(fileName+"_"+word) == null ? 0 : Double.valueOf(tfidfByDocumentMap.get(fileName+"_"+word));
				double queryByDocVal = getQueryTfidf(word, queryWrods, matrixSize, documentFreqMap);
				sigmaTiXTj += wordByDocVal * queryByDocVal;
				sigmaTi2 += Math.pow(wordByDocVal, 2);
				sigmaTj2 += Math.pow(queryByDocVal, 2);
				
			}
			double sigmaTi2XSigmaTj2 = sigmaTi2 * sigmaTj2;
			double res = sigmaTiXTj/ Math.sqrt(sigmaTi2XSigmaTj2);
			
			TfidfCosMatrix matrix = TfidfCosMatrix.getinstance();
			matrix.addFile(res, fileName);
			documentsByCos.put(fileName, res);
			
		}
		
		return documentsByCos;
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
	
	

}
