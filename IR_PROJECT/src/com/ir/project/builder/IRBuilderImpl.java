package com.ir.project.builder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IRBuilderImpl implements IRBuilder {

	private static IRFilterUtil filterUtil = new IRFilterUtil();

	public Set<String> buildStopWords() {
		URL urlToFile = IRBuilderImpl.class.getResource("/stopList.txt");
		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(urlToFile.getPath()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return lines.collect(Collectors.toSet());
	}

	public List<Stream<String>> buildOriginalFiles() {
		URL urlToFile = IRBuilderImpl.class.getResource("/ORIGIAL_DOCUMENT");
		List<Stream<String>> files = null;
		try {
			files = Files.list(Paths.get(urlToFile.getPath())).map(e -> {
				try {
					return Files.lines(e.toAbsolutePath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}

	public String filterOriginalFiles(Set<String> stpSet, String st) {
		String withoutStopWord = filterUtil.replaceStopWords(stpSet, st);
		String withoutPunctutaion = filterUtil.replacePunctuation(withoutStopWord);
		String withoutCommas = filterUtil.replaceCommas(withoutPunctutaion);
		String withoutDiez = filterUtil.replaceDiez(withoutCommas);
		return withoutDiez;
	}

	public void buildStpFiles(String st, int i) {
		String stpFileName = "DOC000";
		if (i == 10)
			stpFileName = "DOC00";
		File file = new File("/Users/mac/Desktop/IR/IR_PROJECT/src/STP_DOCUMENT/" + stpFileName + i + ".stp");
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
	}


}
