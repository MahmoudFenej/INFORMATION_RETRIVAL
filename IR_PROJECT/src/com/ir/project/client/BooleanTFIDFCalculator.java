package com.ir.project.client;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.ir.project.builder.BooleanMatrix;
import com.ir.project.builder.IRBuilder;
import com.ir.project.builder.IRBuilderImpl;
import com.ir.project.builder.VectorMatrix;

public class BooleanTFIDFCalculator {

	public static void main(String[] args) throws Exception {
		generateBooleanTfidf();

	}

	public static void generateBooleanTfidf() throws Exception {
		IRBuilder builder = new IRBuilderImpl();

		Set<String> stopWords = builder.buildStopWords();

		Map<String, Stream<String>> originalFiles = builder.buildOriginalFiles();

		originalFiles.forEach((fileName, stream) -> {

			StringBuilder st = new StringBuilder();

			stream.forEach(e -> st.append(e));

			String filterOriginalFiles = builder.filterOriginalFile(stopWords, st.toString());

			String filtredtext = builder.buildStpFiles(filterOriginalFiles, fileName);

//			builder.createBooleanMatrix(filtredtext, fileName);
			builder.createVectorMatrix(filtredtext, fileName);

		});
//		BooleanMatrix.getinstance().toCsv();
		VectorMatrix.getinstance().toCsv();
//		VectorMatrix.getinstance().tfidfToCsv();

	}
}
