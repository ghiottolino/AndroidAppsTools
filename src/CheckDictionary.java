import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale.Category;

public class CheckDictionary {

	public static void main(String[] args) throws Exception {

		String dictToCheckPath = "dicts/main/basic_dictionary.txt";
		List<GermanWord> dictToCheck = loadDictionary(dictToCheckPath);

		
		String comparisonDict2Path = "dicts/check/dict2-chemnitz.txt";
		List<GermanWord> comparisonDic2 = loadDictionary2(comparisonDict2Path);
		
		String comparisonDictPath = "dicts/check/dict1.txt";
		List<GermanWord> comparisonDic1 = loadDictionary1(comparisonDictPath);


		String comparisonDict3Path = "dicts/check/dict3.txt";
		List<GermanWord> comparisonDic3 = loadDictionary3(comparisonDict3Path);

		List<GermanWord> comparisonDic = new ArrayList<>();
		
		comparisonDic.addAll(comparisonDic2);
		comparisonDic.addAll(comparisonDic1);
		comparisonDic.addAll(comparisonDic3);

		compareDictionaries(dictToCheck, comparisonDic);
		
		
	}

	private static void compareDictionaries(List<GermanWord> dictToCheck,
			List<GermanWord> comparisonDic) {

		System.out.println("Dictionary size " + dictToCheck.size());
		System.out.println("Control Dictionary size " + comparisonDic.size());

		int n = 0;
		
		Map<String, String> dictToCheckGenderMap = getGenderMap(dictToCheck);
		Map<String, String> comparisoDictGenderMap = getGenderMap(comparisonDic);
		

		for (GermanWord word : dictToCheck) {

			if (!comparisonDic.contains(word)) {
				n++;
//				System.out.println("Control dictionary does not contain "
//						+ word.word + "(" + word.gender + ")");

			}
			
			else {
				
				//word is found compare genders
				String checkGender = dictToCheckGenderMap.get(word.word);
				String controlGender = comparisoDictGenderMap.get(word.word);
				
				if (checkGender!=null && controlGender!=null && checkGender.equals(controlGender)){
					if (word.category!=null) {
						System.out.println(word.translation+"|"+word.gender+" "+word.word+"|"+word.category);

					}
					else {
						System.out.println(word.translation+"|"+word.gender+" "+word.word);

					}
				}
				else {
					n++;
//					System.out.println("Control dictionary contains a differed gender ("+comparisoDictGenderMap.get(word.word)+")"+" for the "
//							+ word.word + "(" + word.gender + ")");
				}
				
				
				
			}

		}

		System.out.println("Mismatches: " + n);

	}

	private static Map<String, String> getGenderMap(List<GermanWord> dict) {
		Map<String, String> genderMap =  new HashMap<String, String>();
		
		for (GermanWord word : dict) {
			if (genderMap.get(word.word)==null) {
				genderMap.put(word.word, word.gender);
			}
			else {
				String storedArticle = genderMap.get(word.word);
				if (!storedArticle.equals(word.gender)) {
					System.out.println("Mismatch: the word " + word.word+" has been found with genders "+storedArticle+" and "+word.gender);
					genderMap.put(word.word, "invalid");
				}
			}
		}
		
		return genderMap;
	}

	private static List<GermanWord> loadDictionary(String fileName)
			throws Exception {

		InputStream in = CheckDictionary.class.getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		List<GermanWord> germanWords = new ArrayList<>();
		while ((strLine = br.readLine()) != null) {

			String[] columns = strLine.split("\\|");
			String en = columns[0].trim();
			String de = columns[1].trim();
			
			String category=null;
			if (columns.length==3) {
				category = columns[2].trim();
			}

			// replace apostrophs with apostrophs + spaces
			de = de.replace('\'', ' ');
			String[] split;
			split = de.split(" ", 2);

			if (split.length == 2) {
				String word = split[1];
				String article = split[0];

				GermanWord germanWord = new GermanWord();
				germanWord.gender = article;
				germanWord.word = word;
				germanWord.translation=en;
				germanWord.category=category;
				germanWords.add(germanWord);
			}

		}

		return germanWords;
	}

	private static List<GermanWord> loadDictionary1(String fileName)
			throws Exception {

		InputStream in = CheckDictionary.class.getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		List<GermanWord> germanWords = new ArrayList<>();
		while ((strLine = br.readLine()) != null) {

			// replace apostrophs with apostrophs + spaces
			String de = strLine.replace('\'', ' ');
			String[] split;
			split = de.split(" ", 2);

			if (split.length == 2) {
				String word = split[1];
				String article = split[0];

				GermanWord germanWord = new GermanWord();
				germanWord.gender = article;
				germanWord.word = word;
				germanWords.add(germanWord);
			}

		}

		return germanWords;
	}

	private static List<GermanWord> loadDictionary2(String fileName)
			throws Exception {

		InputStream in = CheckDictionary.class.getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		List<GermanWord> germanWords = new ArrayList<>();
		while ((strLine = br.readLine()) != null) {

			if (strLine != null) {
				strLine = strLine;

				String[] wordsInAline = strLine.split(";");

				for (String entry : wordsInAline) {

					if ((entry.contains("{m}")) || (entry.contains("{n}"))
							|| (entry.contains("{f}"))) {
						String[] split;
						split = entry.split("\\{", 2);
						if (split.length == 2) {
							String word = split[0].trim();

							String article = split[1];

							String[] split2;
							split2 = article.split("\\}");

							article = split2[0].trim();
							if (article.endsWith("m"))
								article = "der";
							else if (article.endsWith("n"))
								article = "das";
							else if (article.endsWith("f"))
								article = "die";

							GermanWord germanWord = new GermanWord();
							germanWord.gender = article;
							germanWord.word = word;
							germanWords.add(germanWord);
						}

					}

				}
			}

		}

		return germanWords;
	}

	private static List<GermanWord> loadDictionary3(String fileName)
			throws Exception {

		InputStream in = CheckDictionary.class.getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		List<GermanWord> germanWords = new ArrayList<>();
		while ((strLine = br.readLine()) != null) {

			if (strLine != null) {
				strLine = strLine;

				if ((strLine.startsWith("der")) || (strLine.startsWith("die"))
						|| (strLine.startsWith("das"))) {
					String[] split;
					split = strLine.split(" ", 2);
					if (split.length == 2) {

						String article = split[0];
						String word = split[1];

						String[] split2;
						split2 = word.split("\\(");

						word = split2[0].trim();

						GermanWord germanWord = new GermanWord();
						germanWord.gender = article;
						germanWord.word = word;
						germanWords.add(germanWord);
					}

				}

			}

		}

		return germanWords;
	}

}
