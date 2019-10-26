import java.io.File;

public class MyMain {
	
	public static void main(String[] args) {
		File rootFolder = new File("dataset/jslack-master");
		File[] listOfFiles = rootFolder.listFiles();

		try {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					scanDirectory(listOfFiles[i]);
				}
			}
			System.out.println("\n");
			MetricsCalculator metricsCalculator = new MetricsCalculator();
			metricsCalculator.showResult();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}
	}

	private static void scanDirectory(File folder) throws Exception {
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".java")) {
				System.out.println("Processing " + listOfFiles[i].getAbsolutePath());
				MetricsCalculator metricsCalculator = new MetricsCalculator();
				metricsCalculator.calculateMetrics(listOfFiles[i]);
			}
			else if (listOfFiles[i].isDirectory()) {
				scanDirectory(new File(folder+"/"+listOfFiles[i].getName()));
			}
		}
	}
}
