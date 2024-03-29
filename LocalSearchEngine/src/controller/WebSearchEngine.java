package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import services.OtherFunctions;
import services.HTMLTextConverter;

public class WebSearchEngine {

	static ArrayList<String> key = new ArrayList<String>();
	static Hashtable<String, Integer> numbers = new Hashtable<String, Integer>();
	static Scanner sc = new Scanner(System.in);

	private static Map<File, Integer> searchWord(File filePath, String word,Map<File, Integer> occurrence) {
		int cnt=0, lastLoc = 0;

		String txt = HTMLTextConverter.fileCreator(filePath);

		for (int loc = 0; loc <= txt.length(); loc += lastLoc + word.length()) {
			lastLoc = search1(word, txt.substring(loc));
			if ((lastLoc + loc) < txt.length()) {
				cnt++;
			}
		}
		if(cnt!=0)	{
			occurrence.putIfAbsent(filePath, cnt);
		}
		return occurrence;
	}

	// Brute force method : Just matches and returns the offset.
	public static int search1(String pat, String txt) {
		int M = pat.length();
		int N = txt.length();

		for (int i = 0; i <= N - M; i++) {
			int j;
			for (j = 0; j < M; j++) {
				if (txt.charAt(i + j) != pat.charAt(j))
					break;
			}
			if (j == M)
				return i; // found at offset i.
		}
		return N; // not found.
	}

	/*using regex to find similar string to pattern */
	private static void alternativeWord(String p1) {
		try {

			// String to be scanned to find the pattern.
			String line = " ";
			String pattern3 = "[a-zA-Z0-9]+";


			// Create a Pattern object.
			Pattern r3 = Pattern.compile(pattern3);
			// Now create matcher object.
			Matcher m3 = r3.matcher(line);
			int _fileNumber=0;
			File _directory = new File(System.getProperty("user.dir")+"\\src\\ConvertedTextFiles\\");
			File[] _fileArray = _directory.listFiles();
			for(int i=0;i<100;i++)
			{
				findData(_fileArray[i],_fileNumber,m3,p1);
				_fileNumber++;
			}

			Set<?> keys = new HashSet<Object>();
			Integer value =1;
			Integer val = 0;

			System.out.print(">>Did you mean?\n>>");
			for(Map.Entry entry: numbers.entrySet()){
				if(val == entry.getValue()) {
					break;
				}
				else {
					if(value==entry.getValue()){
						System.out.print(entry.getKey()+"  ");
					}
				}
			}
		}
		catch(Exception e){
			System.out.println(">>Exception on alternativeWord: "+e);
		}
	}

	//finds strings with similar pattern and calls edit distance() on those strings
	public static void findData(File _sourceFile,int fileNumber,Matcher _m3,String p1) throws FileNotFoundException,ArrayIndexOutOfBoundsException
	{
		try
		{
			BufferedReader _rederObject = new BufferedReader(new FileReader(_sourceFile));
			String line = null;

			while ((line = _rederObject.readLine()) != null){
				_m3.reset(line);
				while (_m3.find()) {
					key.add(_m3.group());
				}
			}
			_rederObject.close();
			for(int p = 0; p<key.size(); p++){ 
				numbers.put(key.get(p), editDistance(p1.toLowerCase(),key.get(p).toLowerCase()));
			}
		}     
		catch(Exception e)
		{
			System.out.println(">>Exception on findData:"+e);
		}
	}

	//Uses Edit distance to compare nearest distance between keyword and similar patterns obtained from regex
	public static int editDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}

		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}

		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);

				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
		return dp[len1][len2];
	}

	private static void checkData(File[] fileNames,String pat) {
		int pages=0;

		Map<File,Integer> occurrence = new HashMap<File,Integer>();

		System.out.println(">>Searching in web...");
		for(File f: fileNames)
		{
			occurrence = searchWord(f, pat, occurrence);
			if(occurrence.containsKey(f)) 
				pages++;
		}
		if(pages==0) {
			System.out.println(">>Incorrect Input checking alternatives...");
			alternativeWord(pat);
			System.out.print("\n>>Enter proper String:\n>>");
			checkData(fileNames, sc.nextLine());
		}else {
			System.out.println(">>Found data in "+pages+" pages...");
		}
		//sortValue(occurrence,pages);
		printPages(occurrence);
	}

	private static void printPages(Map<File, Integer> occurence) {
		LinkedList<Entry<File, Integer>> occur = new LinkedList<Entry<File, Integer>>(occurence.entrySet());	
		occurence = OtherFunctions.sortCall(occur);
		System.out.println(">>...Page Ranking...");

		occurence.forEach((file, occ) -> System.out.println(">>"+file.getName().substring(0,file.getName().length()-4) + ".htm: " + occ));

	}

	public static void main(String[] args) {

		Scanner s = new Scanner (System.in);
		String projectDir = System.getProperty("user.dir");
		try {
			File dir = new File(projectDir+"\\src\\ConvertedTextFiles\\");
			if(dir.listFiles().length==0)
				HTMLTextConverter.convertHtmlToText(projectDir);
		
			File[] fileArray = dir.listFiles();
			System.out.print(">>Welcome to Tyrion Search\n>>Made by: Team Tyrion\n>>Team Members:"
					+ "\n\t Karanveer Singh"
					+ "\n\t Parminder Singh"
					+ "\n\t Sanyam Sareen"
					+ "\n\t Saharsh Bawankar");

			System.out.print("\n>>Enter your search:\n>>");

			checkData(fileArray,s.nextLine());
		}
		catch (Exception e) {
			System.out.println(">>Exception on maindata:"+e);
		}
		s.close();
	}
}