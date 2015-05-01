import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LeagueTable {
	
	private int totalGames = 38;
	
	private String[] aryClub;
	private String[] aryRecord;
	private int[] gamesPlayed = new int[20];
	private int[] wins = new int[20];
	private int[] loses = new int[20];
	private int[] ties = new int[20];
	private int[] pointsFor = new int[20];
	private int[] pointsAgainst = new int[20];
	private int[] points = new int[20];
	private int[] fifaValue = new int[20];
	private ArrayList<String> club = new ArrayList<String>();
	private ArrayList<Integer> clubFrequency = new ArrayList<Integer>();
	private String path = "";
	
	public LeagueTable(String fileName) {
		path = fileName;
	}
	
	public void collectClubFrequency() throws IOException {
		String[] seasonFiles = {"LeagueTable_Mock.txt",
				"LeagueTable_2013-2014.txt",
				"LeagueTable_2012-2013.txt",
				"LeagueTable_2011-2012.txt",
				"LeagueTable_2010-2011.txt"};
		
		for(int i = 0; i < seasonFiles.length; i++){ //loop through seasons
			path = seasonFiles[i];
			sortTable();
			for(int j = 0; j < aryClub.length; j++){
				if(!club.contains(aryClub[j])){ //Not already in list
					club.add(aryClub[j]);
					clubFrequency.add(j);
				}else{
					int index = club.indexOf(aryClub[j]); //increment freq by 1
					int freq = clubFrequency.get(index);
					clubFrequency.set(index, freq + j);
				}
			}
		}
		for(int k = 0; k < aryClub.length; k++){
			System.out.println(club.get(k) + " " + clubFrequency.get(k));
		}
	}
	
	public void PrintRecord() throws IOException {
		URL url = getClass().getResource("LeagueTable_Mock.txt");
		URL fifaUrl = getClass().getResource("fifa14.txt");
		String file_name = url.getPath();
		String fifa_name = fifaUrl.getPath();
		ReadFile file = new ReadFile(file_name);
		ReadFIFAFile fifaFile = new ReadFIFAFile(fifa_name);
		aryClub = file.getClub();
		aryRecord = file.getRecord();
		pointsFor = file.getPointsFor();
		pointsAgainst = file.getPointsAgainst();
		fifaValue = fifaFile.getScore();
	}
	
	public void setTable() throws IOException {
		PrintRecord();
		for(int i = 0; i < aryRecord.length; i++){
			StringTokenizer st = new StringTokenizer(aryRecord[i], "--"); 
			while(st.hasMoreTokens()) { 
				wins[i] = Integer.parseInt(st.nextToken());
				ties[i] = Integer.parseInt(st.nextToken()); 
				loses[i] = Integer.parseInt(st.nextToken());
				points[i] = wins[i]*3 + ties[i];
				gamesPlayed[i] = wins[i] + ties[i] + loses[i];
			}
		}
	}
	
	public void sortTable() throws IOException {
		setTable();
		int counter = 0; //To stop Sort
		for(int j = 0; j < aryClub.length; j++){ //Worst case scenario
			counter = 0;
			for(int i = 0; i < aryClub.length; i++){
				if(i+1 < aryClub.length && (points[i+1] > points[i] || 
				((points[i+1] == points[i]) && (pointsFor[i+1] - pointsAgainst[i+1]) >  (pointsFor[i] - pointsAgainst[i])) || //Tiebreaker GD
				((pointsFor[i+1] - pointsAgainst[i+1]) == (pointsFor[i] - pointsAgainst[i]) && (pointsFor[i+1] > pointsFor[i])))){ //Tiebreaker GF
					String sTemp = aryClub[i];aryClub[i] = aryClub[i + 1];aryClub[i + 1] = sTemp;
					sTemp = aryRecord[i];aryRecord[i] = aryRecord[i + 1];aryRecord[i + 1] = sTemp;
				    int iTemp = gamesPlayed[i];gamesPlayed[i] = gamesPlayed[i+1];gamesPlayed[i+1] = iTemp;
				    iTemp = wins[i];wins[i] = wins[i+1];wins[i+1] = iTemp;
				    iTemp = ties[i];ties[i] = ties[i+1];ties[i+1] = iTemp;
				    iTemp = loses[i];loses[i] = loses[i+1];loses[i+1] = iTemp;
				    iTemp = pointsFor[i];pointsFor[i] = pointsFor[i+1];pointsFor[i+1] = iTemp;
				    iTemp = pointsAgainst[i];pointsAgainst[i] = pointsAgainst[i+1];pointsAgainst[i+1] = iTemp;
				    iTemp = points[i];points[i] = points[i+1];points[i+1] = iTemp;
				    counter++;
				}			
			}
			if(counter == 0){break;} //Done with sort
		}
	}
	
	public boolean isChampion(String clubName) throws IOException {
		sortTable();
		int clubNumber = -1;
		clubNumber = java.util.Arrays.asList(aryClub).indexOf(clubName);
		if(clubNumber == -1){
			System.out.println("You entered an invalid teamname");
			return false;
		}
		if(clubNumber==0){
			for(int i = 1; i < aryClub.length; i++){
				points[i] += (totalGames - gamesPlayed[i])*3;
				if(points[clubNumber] == points[i]){
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) <  (pointsFor[i] - pointsAgainst[i])) //Tiebreaker GD
						return false;
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) == (pointsFor[i] - pointsAgainst[i])){
						if(pointsFor[clubNumber] <  pointsFor[i]) //Tiebreaker GF
							return false;
						if(pointsFor[clubNumber] == pointsFor[i])
							return false; //Tied!
					}
				}
				if(points[clubNumber] < points[i]){
					return false; //not clinched
				}
			}
			return true;
		}
		return false; //Club isn't first seed
	}
	
	public boolean isInChampionsLeague(String clubName) throws IOException {
		sortTable();
		int clubNumber = -1;
		clubNumber = java.util.Arrays.asList(aryClub).indexOf(clubName);
		if(clubNumber == -1){
			System.out.println("You entered an invalid teamname");
			return false;
		}
		if(clubNumber>=0 && clubNumber<=3){
			for(int i = 4; i < aryClub.length; i++){
				points[i] += (totalGames - gamesPlayed[i])*3;
				if(points[clubNumber] == points[i]){
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) <  (pointsFor[i] - pointsAgainst[i])) //Tiebreaker GD
						return false;
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) == (pointsFor[i] - pointsAgainst[i])){
						if(pointsFor[clubNumber] <  pointsFor[i]) //Tiebreaker GF
							return false;
						if(pointsFor[clubNumber] == pointsFor[i])
							return false; //Tied!
					}
				}
				if(points[clubNumber] < points[i]){
					return false; //not clinched
				}
			}
			return true;
		}
		return false; //Club is not in top 4
	}
	
	public boolean isEliminatedFromChampionsLeague(String clubName) throws IOException {
		sortTable();
		int clubNumber = -1;
		clubNumber = java.util.Arrays.asList(aryClub).indexOf(clubName);
		if(clubNumber == -1){
			System.out.println("You entered an invalid teamname");
			return false;
		}
		if(!(clubNumber>=0 && clubNumber<=3)){
			points[clubNumber] += (totalGames - gamesPlayed[clubNumber])*3;
			for(int i = 0; i < 4; i++){ //Top 4
				if(points[clubNumber] == points[i]){
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) >  (pointsFor[i] - pointsAgainst[i])) //Tiebreaker GD
						return false;
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) == (pointsFor[i] - pointsAgainst[i])){
						if(pointsFor[clubNumber] >  pointsFor[i]) //Tiebreaker GF
							return false;
						if(pointsFor[clubNumber] == pointsFor[i])
							return false; //Tied!
					}
				}
				if(points[clubNumber] > points[i]){
					return false; //not eliminated
				}
			}
			return true;
		}
		return false; //In top 4
	}
	
	public boolean isEliminatedFromChampionship(String clubName) throws IOException {
		sortTable();
		int clubNumber = -1;
		clubNumber = java.util.Arrays.asList(aryClub).indexOf(clubName);
		if(clubNumber == -1){
			System.out.println("You entered an invalid teamname");
			return false;
		}
		if(!(clubNumber==0)){
			points[clubNumber] += (totalGames - gamesPlayed[clubNumber])*3;
			if(points[clubNumber] == points[0]){
				if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) >  (pointsFor[0] - pointsAgainst[0])) //Tiebreaker GD
					return false;
				if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) == (pointsFor[0] - pointsAgainst[0])){
					if(pointsFor[clubNumber] >  pointsFor[0]) //Tiebreaker GF
						return false;
					if(pointsFor[clubNumber] == pointsFor[0])
						return false; //Tied!
				}
			}
			if(points[clubNumber] > points[0]){
				return false; //not eliminated
			}
			return true;
		}
		return false; //Is 1 seed
	}
	
	public boolean isRelegated(String clubName) throws IOException {
		sortTable();
		int clubNumber = -1;
		clubNumber = java.util.Arrays.asList(aryClub).indexOf(clubName);
		if(clubNumber == -1){
			System.out.println("You entered an invalid teamname");
			return false;
		}
		if(clubNumber>=17 && clubNumber<=19){ //Last 3 clubs
			points[clubNumber] += (totalGames - gamesPlayed[clubNumber])*3;
			for(int i = 0; i < 17; i++){ //Not in bottom 3
				if(points[clubNumber] == points[i]){
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) >  (pointsFor[i] - pointsAgainst[i])) //Tiebreaker GD
						return false;
					if((pointsFor[clubNumber] - pointsAgainst[clubNumber]) == (pointsFor[i] - pointsAgainst[i])){
						if(pointsFor[clubNumber] >  pointsFor[i]) //Tiebreaker GF
							return false;
						if(pointsFor[clubNumber] == pointsFor[i])
							return false; //Tied!
					}
				}
				if(points[clubNumber] > points[i]){
					return false; //not relegated
				}
			}
			return true;
		}
		return false; //Not in bottom 3
	}
	
	public int totalPossibilitiesChampion(String clubName) throws IOException {
		sortTable();
		int clubNumber = -1;
		clubNumber = java.util.Arrays.asList(aryClub).indexOf(clubName);
		if(clubNumber == -1){
			System.out.println("You entered an invalid teamname");
			return 0;
		}
		if(isEliminatedFromChampionship(aryClub[clubNumber])){
			System.out.println("There are 0 scenarios because " + aryClub[clubNumber] + " cannot be the Champion.");
			return 0;
		}
		if(isChampion(aryClub[clubNumber])){
			System.out.println("There are 0 scenarios because " + aryClub[clubNumber] + "is the league Champion.");
			return 0;
		}
		
		int totalPossibilities = 0;
		ArrayList<Integer> weeksLeft = new ArrayList<Integer>(); 
		for(int i = 0; i < aryClub.length; i++){ //Gets weeks and teams
			if(!isEliminatedFromChampionship(aryClub[i])){ //Checks if team even affects the race for Champion
				if(totalGames-gamesPlayed[i] != 0){ //Checks if that team has any games left to play out
					weeksLeft.add(totalGames-gamesPlayed[i]);
				}
			}
		}
		int weeks = 0;
		Collections.sort(weeksLeft); // Sort the arraylist
		int maxWeek = weeksLeft.get(weeksLeft.size() - 1);
		
		for(int i = 0; i < maxWeek; i++){
			if(weeksLeft.size() == 0){break;}
			weeks = weeksLeft.get(0);
			//System.out.println("weeks: " + weeksLeft.get(0) + " teams: " + weeksLeft.size());
			totalPossibilities += Math.pow(((Math.pow(weeks,2) + 3*weeks + 2)/2),weeksLeft.size()); //((w^2 + 3w + 2)/2)^t
			for(int j = 0; j < weeksLeft.size(); j++){
				weeksLeft.set(j, weeksLeft.get(j)-1);
			}
			for(int j = 0; j < weeksLeft.size(); j++){
				if(weeksLeft.get(j) == 0){weeksLeft.remove(0);}
			}
		}
		System.out.println("There are " + totalPossibilities + " total possibilities for " + clubName + " to become the Champion.");
		return totalPossibilities;
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
	
	public void TableView() throws IOException {
		sortTable();
		System.out.println("\n  Club                 Record   GF  GA  GD  Pts");
		for(int i = 0; i < aryClub.length; i++){
			String playoffMarks = " ";
			String goalDif = Integer.toString(pointsFor[i] - pointsAgainst[i]);
			if((pointsFor[i] - pointsAgainst[i]) > 0){goalDif = "+"+goalDif;}
			if(pointsFor[i] == pointsAgainst[i]){goalDif = " "+goalDif;}
			if(isEliminatedFromChampionsLeague(aryClub[i])){playoffMarks="E";}
			if(isRelegated(aryClub[i])){playoffMarks="R";}
			if(isInChampionsLeague(aryClub[i])){playoffMarks="U";}
			if(isChampion(aryClub[i])){playoffMarks="C";}
			System.out.println(playoffMarks + " " + padRight(aryClub[i], 20) + " " + padRight(aryRecord[i], 9) +
					padRight(Integer.toString(pointsFor[i]), 3) + " " + padRight(Integer.toString(pointsAgainst[i]), 3) +
					padRight(goalDif, 4) + " " + points[i]);
		}
	}
	
	public static void main(String args[]) throws IOException {
		LeagueTable l = new LeagueTable("LeagueTable_Mock.txt");
		
		Scanner reader = new Scanner(System.in);
		System.out.println("Welcome to the Premier League Playoff Solver.");
		System.out.println("1 Show League Table");
		System.out.println("2 Show Projected League Table");
		System.out.println("3 Calculate scenerios: Champion");
		System.out.println("4 Calculate scenerios: Champions League");
		
		System.out.print  ("Enter Number: ");
		int counter = reader.nextInt();
		if(counter == 1){
			l.TableView();
		}
		else if(counter == 2){
			LeagueTable ls = new LeagueTable("LeagueTable_2010-2011.txt");
			ls.collectClubFrequency();
		}
		else if(counter == 3){
			System.out.print("Which team? : ");
			String club = reader.next();
			l.totalPossibilitiesChampion(club);
			//l.ChampionScenario();
		}
		else if(counter == 4){
			//l.ChampionsLeagueScenario();
		}
		else{
			System.out.println("You Entered an incorrect input!");
		}
	}
}