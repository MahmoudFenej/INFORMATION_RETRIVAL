package com.ir.project.builder;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface IRBuilder {

	public Set<String> buildStopWords();

	public Map<String,Stream<String>> buildOriginalFiles() throws Exception;

	public String filterOriginalFile(Set<String> stpSet, String st);

	public String buildStpFiles(String st, String fileName);
	
	public void createBooleanMatrix(String filtredtext, String fileName);
	
	public void createVectorMatrix(String filtredtext, String fileName);
	
	public Map<String, Double> createBooleanCosTable(String query,Map<String, String> booleanByWordMap);

	public Map<String, Double> createTfidfCosTable(String query,Map<String, String> tfidfByWordMap, Map<String, Integer> documentFreqMap);

}
