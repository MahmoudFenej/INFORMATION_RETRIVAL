package com.ir.project.builder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface IRBuilder {

	public Set<String> buildStopWords();

	public List<Stream<String>> buildOriginalFiles();

	public String filterOriginalFiles(Set<String> stpSet, String st);

	public void buildStpFiles(String st, int i);

}
