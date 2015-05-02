import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ReadFIFAFile {
	
	private String path;
	private String[] club = new String[0];
	private double[] points = new double[0];


	
	public ReadFIFAFile(String file_path){
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
	
	public void getValue() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = readLines();
		club = new String[numberOfLines];
		points = new double[numberOfLines];
		
		int i;
		for(i = 0; i < numberOfLines; i++){
			StringTokenizer st = new StringTokenizer(textReader.readLine(), ":;;;;;;;;;;;"); 
			while(st.hasMoreTokens()) { 
				club[i] = st.nextToken();
				int pointSum = 0;
				for(int j=1;j<=11;j++){
					pointSum += Integer.parseInt(st.nextToken());
				}
				points[i] = pointSum/11.0;
			}
		}
		textReader.close();
	}
	
	public double[] getScore() throws IOException {
		getValue();
		return points;
	} 

}
