import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ReadFIFAFile extends ReadFile{
	
	private String path;
	private String[] club = new String[0];
	private int[] points = new int[0];


	
	public ReadFIFAFile(String file_path){
		super(file_path);
	}
	
	public void getValue() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = readLines();
		club = new String[numberOfLines];
		points = new int[numberOfLines];
		
		int i;
		for(i = 0; i < numberOfLines; i++){
			StringTokenizer st = new StringTokenizer(textReader.readLine(), ":;;;"); 
			while(st.hasMoreTokens()) { 
				club[i] = st.nextToken();
				for(int j=1;j<=11;j++){
					points[i] += Integer.parseInt(st.nextToken());
				}
			}
		}
		textReader.close();
	}
	
	public int[] getScore() throws IOException {
		getValue();
		return points;
	} 

}
