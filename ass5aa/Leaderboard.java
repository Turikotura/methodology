import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Leaderboard {
	
	private ArrayList<String> names;
	private ArrayList<Integer> scores;
	private BufferedReader br;
	private BufferedWriter bw;
	private File source;
	
	Leaderboard(String filename) throws Exception{
		File dir = new File("./");
		source = new File(dir, filename);
		boolean fileExists = source.exists();
		if(fileExists){
			br = new BufferedReader(new FileReader(source));
			System.out.println("EHELL");
			names = new ArrayList<String>();
			scores = new ArrayList<Integer>();
			int i = 0;
			String name="";
			while(i!=10 && (name=br.readLine()) != null){
				names.add(name);
				scores.add(Integer.parseInt(br.readLine()));
				i++;
			}
		}else{
			System.out.println("DOESNT EXIST");
			source.createNewFile();
			br = new BufferedReader(new FileReader(source));
			names = new ArrayList<String>();
			scores = new ArrayList<Integer>();
		}
	}
	
	public int addScore(String name, int score)throws Exception{
		int place = -1;
		boolean breakForLoop = false;
		for(int i = 0; i < scores.size(); i++){
			if(score > scores.get(i)){
				scores.add(i, score);
				names.add(i, name);
				place = i;
				breakForLoop = true;
				break;
			}
		}
		if(!breakForLoop){
			place = scores.size();
			scores.add(score);
			names.add(name);
		}
		if(scores.size()>10){
			place = -1;
			scores.remove(10);
			names.remove(10);
		}
		updateLeaderboard();
		return place;
	}

	private void updateLeaderboard()throws Exception {
		bw = new BufferedWriter(new FileWriter(source));
		for(int i = 0; i < scores.size(); i++){
			bw.write(names.get(i));
			bw.newLine();
			bw.write("" + scores.get(i));
			bw.newLine();
		}
		bw.close();
	}
}
