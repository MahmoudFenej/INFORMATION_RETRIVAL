package com.ir.project.builder;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IRFilterUtil {
	public String replaceStopWords(Set<String> stpSet, String allText) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String st : allText.split(" ")) {
			if (!stpSet.contains(st.toLowerCase()))
				stringBuilder.append(st + " ");
		}
		return stringBuilder.toString();
	}

	public String replaceDiez(String withoutCommas) {
		Pattern pt = Pattern.compile("\\s#");
		Matcher match = pt.matcher(withoutCommas);
		while (match.find()) {
			String s = match.group();
			withoutCommas = withoutCommas.replaceAll("\\" + s, "");
		}
		return withoutCommas;
	}

	public String replaceCommas(String withoutStopWord) {
		Pattern pt = Pattern.compile("\\,");
		Matcher match = pt.matcher(withoutStopWord);
		while (match.find()) {
			String s = match.group();
			withoutStopWord = withoutStopWord.replaceAll("\\" + s, "");
		}
		return withoutStopWord;
	}

	public String replacePunctuation(String withoutStopWord) {
		Pattern pt = Pattern.compile("\\s*\\p{Punct}+\\s*$");
		Matcher match = pt.matcher(withoutStopWord);
		while (match.find()) {
			String s = match.group();
			withoutStopWord = withoutStopWord.replaceAll("\\" + s, "");
		}
		return withoutStopWord;
	}
}
