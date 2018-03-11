package com.ir.project.client;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.ir.project.builder.IRBuilder;
import com.ir.project.builder.IRBuilderImpl;

public class ClientContext {

	public static void main(String[] args) {
		IRBuilder builder = new IRBuilderImpl();
		
		Set<String> stopWords = builder.buildStopWords();
		
		List<Stream<String>> originalFiles = builder.buildOriginalFiles();
		
		int i = 1;
		for (Stream<String> stream : originalFiles) {
			
			StringBuilder st = new StringBuilder();
			
			stream.forEach(e->st.append(e));
			
			String filterOriginalFiles = builder.filterOriginalFiles(stopWords, st.toString());
			
			builder.buildStpFiles(filterOriginalFiles, i);
			
			i++;
		}

	}
}
