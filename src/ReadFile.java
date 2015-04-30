import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ReadFile {
	
	private String path;
	private String[] club = new String[0];
	private String[] record = new String[0];
	private int[] pfor = new int[0];
	private int[] pagainst = new int[0];
	
	public ReadFile(String file_path){
		path = file_path;
	}
	
	int readLines() throws IOException {
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		String aLine;
		int numberOfLines = 0;
		while ((aLine = bf.readLine()) != null){
			numberOfLines++;
		}
		
		bf.close();
		return numberOfLines;
	}
	
	public void getStatistics() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = readLines();
		club = new String[numberOfLines];
		record = new String[numberOfLines];
		pfor = new int[numberOfLines];
		pagainst = new int[numberOfLines];
		
		int i;
		for(i = 0; i < numberOfLines; i++){
			StringTokenizer st = new StringTokenizer(textReader.readLine(), ":;;;"); 
			while(st.hasMoreTokens()) { 
				club[i] = st.nextToken();
				record[i] = st.nextToken();
				pfor[i] = Integer.parseInt(st.nextToken());
				pagainst[i] = Integer.parseInt(st.nextToken());
			}
		}
		textReader.close();
	}
	
	public String[] getClub() throws IOException {
		getStatistics();
		return club;
	}
	public String[] getRecord() throws IOException {
		getStatistics();
		return record;
	}
	public int[] getPointsFor() throws IOException {
		getStatistics();
		return pfor;
	}
	public int[] getPointsAgainst() throws IOException {
		getStatistics();
		return pagainst;
	}
	
}