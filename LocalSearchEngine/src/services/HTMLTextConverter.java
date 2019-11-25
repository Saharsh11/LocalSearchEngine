package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;



public class HTMLTextConverter {

//This method converts HTML Files into text documents.
	public static void convertHtmlToText(String projectDir)
			throws IOException, FileNotFoundException, NullPointerException {
		System.out.println(">>Pre-processing HTML files...");
		org.jsoup.nodes.Document doc = null;
		BufferedWriter out = null;
		
		try {
			File dir = new File(projectDir+"\\src\\W3C Web Pages\\");
			File[] fileArray = dir.listFiles();
			for (File file : fileArray) {
				doc = Jsoup.parse(file, "UTF-8");
				String str = file.getName().substring(0, file.getName().lastIndexOf('.'));
				out = new BufferedWriter(
						new FileWriter(projectDir+"\\src\\ConvertedTextFiles\\"
								+ str  + ".txt"));
				out.write(fileCreator(file));
				out.close();
			}
		} catch (Exception e) {
			System.out.println(">>Exception on HTMLTextConverted.convertHtmlToText:"+e);
			// TODO: handle exception
		}
	}
	
	public static String fileCreator(File filePath) {
		String data="";
		try
		{
			BufferedReader Object = new BufferedReader(new FileReader(filePath));
			String line = null;

			while ((line = Object.readLine()) != null){
				data+=" "+line;
			}
			Object.close();

		}
		catch(Exception e)
		{
			System.out.println(">>Exception on HTMLTextConverted.fileConvertor:"+e);
		}
		
		return data;
	}
}