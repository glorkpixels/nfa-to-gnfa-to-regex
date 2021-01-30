import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
	static List<String> list = new ArrayList<String>();
	static List<String> statelist = new ArrayList<String>();

	static List<String> transactionlist = new ArrayList<String>();

	static String txtname = "dfa1.txt";
	static String start;
	static String accept;
	static String alphabet;
	static int arraysize;
	static String[][] graph;
	static String[][] graphchanged;
	static int row = 1;
	static int work = 0;
	static String result;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.err.println("Either change the txt name at top or open scanner");
		Scanner sc = new Scanner(System.in);
		// String txtname = sc.next();
        
		read(txtname);
		transformdfatonfa();
		nfareducing();
		System.out.println(graph[0][1]+"e" + " is the regex");
	}

	public static void read(String txtname) {
		try {
			File file = new File(txtname); // creates a new file instance
			FileReader fr = new FileReader(file); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			StringBuffer sb = new StringBuffer(); // constructs a string buffer with no characters
			String line;

			while ((line = br.readLine()) != null)

			{
				list.add(line);
			}

			fr.close(); // closes the stream and release the resources

		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void transformdfatonfa() {
//		System.out.println(list);

		String[] startx = list.get(0).split("=");
		start = startx[1];

		String[] acceptx = list.get(1).split("=");
		accept = acceptx[1];

		String[] alphabetparse = list.get(2).split("=");
		alphabet = alphabetparse[1];

		String[] statespase = list.get(3).split("=");
		String statesnp = statespase[1];
		String[] states = statesnp.split(",");

		statelist.add("qs");
		for (int i = 0; i < states.length; i++) {
			statelist.add(states[i]);
		}
		statelist.add("qe");
		// System.out.println(statelist);
		arraysize = statelist.size();

		// transactionlist.add("qs,E=" + start + "");
		for (int i = 4; i < list.size(); i++) {
			String lol = list.get(i);
			transactionlist.add(lol);
		}
		// transactionlist.add(accept +",E=qe");
		// this function parses the txt probably wont work on many accept states.
	}

	public static void nfareducing() {
		
		
		// parsed list in other functiob is parsed to create table of states
//		System.out.println(transactionlist);
		graph = new String[arraysize][arraysize];
		graphchanged = new String[arraysize - 1][arraysize - 1];
		for (int i = 0; i < transactionlist.size(); i++) {
			String[] parsing = transactionlist.get(i).split(",");
//			System.out.println(transactionlist.get(i));
//			System.out.println(parsing[0].substring(1, 2));
			int c = Integer.valueOf(parsing[0].substring(1, 2));
			String[] lol = parsing[1].split("=");
			int g = Integer.valueOf(lol[1].substring(1, 2));

			if (lol != null) {
//				System.out.println(lol[0]);

				if (graph[c][g] != null) {
					graph[c][g] += lol[0];
				} else {
					graph[c][g] = lol[0];
				}
			}

		}
//		System.out.println(start);
		int x = Integer.valueOf(start.substring(1, 2));
		int y = Integer.valueOf(accept.substring(1, 2));
		graph[0][x] = "e";
		graph[arraysize - 1][y] = "e";
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[0].length; j++) {
				if (graph[i][j] != null) {

					System.out.print(graph[i][j] + " ");
				} else {
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
		System.out.println("-----------------------------");
		
		// states array created and sent to new to reg be a regex expression
		newtoreg();
	}
public static void newtoreg () {

	
	for (int i = 1; i < 2; i++) {
		for (int j = i; j < graph[0].length-1; j++) {
			
			String self = "";
			if (graph[i][i] != null ) {
				
				if (graph[i][i].length() >1) {
					self ="(" +graph[i][i]  + ")"+ "*";
				}
				else {
					self =graph[i][i]  + "*";
				}
				
			
			}
			String loop = "";
			if (graph[i][j] != null && graph[j][i] != null && i != j) {
				// there is a loop
				if (self.equals("")) {
					loop = graph[i][j] + self + graph[j][i];
				} else {
					loop = graph[i][j] + graph[j][i];
				}

			}
//			if (loop != "") {
//
//				System.out.println(loop + "of");
//			
//			}
//			
			
//			if (self != "") {
//
//				System.out.println(self + "xd");
//			
//			}
			
			String thereisone ="";
			
				if (graph[0][j] != null) {
//					System.out.println(graph[0][j]);
					thereisone = graph[0][j];
				}
			
				String goingright = "";
				
				
				if (graph[i][j + 1] != null) {
					goingright = graph[i][j + 1];
//					System.err.println(graph[i][j + 1]);
				}
//				System.out.println(goingright);
				
				String goingd = "";
				if (graph[i+1][j] != null) {
					goingd = graph[i+1][j];
//					graphchanged [i-1][j-1]= goingd;
//					System.err.println(graph[i+1][j]);
				}
//				System.out.println(goingd);
				
				if (work ==0) {
					graphchanged [0][j] = thereisone+self + goingright + loop ;
					
				}
				work++;
				for (int k = 2; k < graph.length; k++) {
					for (int k2 = 2; k2 < graph[0].length; k2++) {
						graphchanged[k-1][k2-1] = graph[k][k2];
					}
				}

		
		}

		graph = graphchanged;
		work=0;
		for (int x = 0; x < graphchanged.length; x++) {
			for (int j = 0; j < graphchanged[0].length; j++) {
				if (graph[x][j] != null) {

					System.out.print(graphchanged[x][j] + " ");
				} else {
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
		System.out.println("-----------------------------");
		
		arraysize--;
		graphchanged = new String[arraysize - 1][arraysize - 1];
		// if reduced to len 2 than we are done it means
		if (graph.length<3) {
			System.out.println("stop");
		}
		else {
			newtoreg();
		}
		

	}

	
	
}

}
