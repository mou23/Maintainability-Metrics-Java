import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jface.text.Document;

public class MetricsCalculator {
	static double totalLOC = 0;
	static double longFileLOC = 0;
	static double deepMethodLOC = 0;
	static double longMethodLOC = 0;
	static CompilationUnit compilationUnit;

	void calculateMetrics(File file) {
		fileLOCCalculator(file);
		methodLOCCalculator(file);
	}
	
	void showResult() {
		System.out.println("Too long files: "+ (longFileLOC*100/totalLOC) + "%");
		System.out.println("Too long methods: "+ (longMethodLOC*100/totalLOC)+ "%");
		System.out.println("Nesting depth: "+ (deepMethodLOC*100/totalLOC)+ "%");
	}
	
	void fileLOCCalculator(File file) {
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(file));
			int counter = 0;
			String line = "";
			while ((line = fileReader.readLine()) != null) {
				counter++;
			}
			totalLOC = totalLOC + counter;
			if(counter>750) {
				longFileLOC = longFileLOC + counter;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void methodLOCCalculator(File file) {
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		String fileContent = readFileToString(file.getAbsolutePath());
		Document document = new Document(fileContent);
		Map<String, String> options = JavaCore.getOptions();
		options.put("org.eclipse.jdt.core.compiler.source", "1.8");
		parser.setCompilerOptions(options);
		parser.setSource(document.get().toCharArray());
		parser.setStatementsRecovery(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		
		parser.setEnvironment(new String[0], new String[0] , null, true);
		parser.setUnitName("file.java");
		compilationUnit = (CompilationUnit) parser.createAST(null);
		compilationUnit.accept(new MethodVisitor());
	}
	
	public String readFileToString(String filePath) {
		StringBuilder fileData = new StringBuilder(100000);
		try{		
			BufferedReader reader = new BufferedReader(new FileReader(filePath));

			char[] buffer = new char[10];
			int numRead = 0;
			while ((numRead = reader.read(buffer)) != -1) {
				String readData = String.valueOf(buffer, 0, numRead);
				fileData.append(readData);
				buffer = new char[1024];
			}

			reader.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		return  fileData.toString();	
	}
}
