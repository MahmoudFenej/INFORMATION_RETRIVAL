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
		
		List<Stream<String>> originalFiles = builder.buildOriginalFiles(stopWords);
		
		int i = 1;
		for (Stream<String> stream : originalFiles) {
			
			String st = stream.map(e -> new String(e)).findAny().get();
		
			String filterOriginalFiles = builder.filterOriginalFiles(stopWords, st);
			
			builder.buildStpFiles(filterOriginalFiles, i);
			
			i++;
		}

	}
}
