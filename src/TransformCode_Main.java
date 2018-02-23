import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TransformCode_Main {

	public static void main(String[] args) throws Exception {
		boolean error = false;
		//THE ASSEMBLY PROGRAM IN THIS FILE
		File f = new File("aaa.txt");
		try (BufferedReader reader1 = Files.newBufferedReader(f.toPath())) {
			String line = null;
			while ((line = reader1.readLine()) != null) {
				try {
					if (FirstPass.firstPass(line)) {
						break;
					}
				} catch (Exception e) {
					System.out.println("First pass error please fix errors that are in intermediate file");
					error = true;
				}
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		if (!error) {
			File f2 = new File("IntermediateFile.txt");
			try (BufferedReader reader2 = Files.newBufferedReader(f2.toPath())) {
				String line = null;

				while ((line = reader2.readLine()) != null) {
					if (SecondPass.secondPass(line)) {
						break;
					}
				}

			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
			SecondPass.writeObjectCode();
			SecondPass.printObjectCode();
//			SecondPass.printListingTable();
		}

	}

}
