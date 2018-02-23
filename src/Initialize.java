import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class Initialize {
	public static HashMap<String,String> h = new HashMap<>();

	public static void init() {
		// TODO Auto-generated method stub
		String []a = new String[2];
		
		File f = new File("OPtable.txt");
		try (BufferedReader reader = Files.newBufferedReader(f.toPath())) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	a=line.split(" ");
		    	h.put(a[0], a[1]);
		    }
		    
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
	}

}
