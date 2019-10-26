import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MethodVisitor extends ASTVisitor {
	static boolean depthFound; 
	
	
    public boolean visit(MethodDeclaration methodDeclaration) {
    	depthFound = false;
    	SimpleName name = methodDeclaration.getName();
    	Block block = methodDeclaration.getBody();
    	if(block==null) {
    		return false;
    	}
    	
//    	System.out.println(name + " "+splitCamelCaseString(name.toString()));
    	
    	int counter = MetricsCalculator.compilationUnit.getLineNumber(block.getStartPosition()+block.getLength())-MetricsCalculator.compilationUnit.getLineNumber(name.getStartPosition())+1;
    	if(counter>75) {
    		MetricsCalculator.longMethodLOC = MetricsCalculator.longMethodLOC + counter;
		}
//        System.out.println(counter);
//        System.out.println("\n\n");
        
        methodDeclaration.accept(new ASTVisitor() {
        	
			@Override
			public void preVisit(ASTNode child) {
				if(child instanceof Statement) {
//					System.out.println("STMT " +child);
					int depth = 0;
					ASTNode currentNode = child.getParent();
					while(currentNode!= null && currentNode.getNodeType()!=ASTNode.METHOD_DECLARATION) {
						if(currentNode instanceof Statement && currentNode.getNodeType()!=ASTNode.BLOCK) {
//							System.out.println(currentNode);
							depth++;
						}
						
						currentNode = currentNode.getParent();
					}
//					System.out.println("depth : "+depth);
					if(depth>=5 && depthFound==false) {
			    		MetricsCalculator.deepMethodLOC = MetricsCalculator.deepMethodLOC + counter;
			    		depthFound = true;
					}
				}
				
				if(child instanceof Javadoc) {
//					System.out.println(name.toString() + " "+ child.toString());
//					System.out.println(splitCamelCaseString(name.toString()));
//					System.out.println(parseComment(child.toString()));
					double c_coeff = commentCoherence(name.toString(), child.toString());
					if(c_coeff==0 || c_coeff >0.5) {
						MetricsCalculator.bad_c_coeff++;
					}
					MetricsCalculator.total_c_coeff++;
				}
			}
		});
        return false;
    }  
    
    public static int lavenstein(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
    
//    private static ArrayList<String> parseMethodName(String meth){
//    	ArrayList<String> ans= new ArrayList<String>();
//    	String cur = "";
//    	for(int i=0;i<meth.length();i++){
//    		if(Character.isUpperCase(meth.charAt(i))){
//    			ans.add(cur.toLowerCase());
//    			cur = ""+meth.charAt(i);
//    		}
//    		else{
//    			cur=cur+meth.charAt(i);
//    		}
//    	}
//    	ans.add(cur.toLowerCase());
//    	System.out.println(ans);
//    	ans.removeIf(s-> s.length()==0);
//    	System.out.println(ans);
//    	return ans;
//    }
    
    private static ArrayList<String> parseComment(String comment){
    	ArrayList<String> ans = new ArrayList<String>();
    	String[] splitStr = comment.trim().split("\\s+");
        for(String text:splitStr) {
        	boolean atleastOneAlpha = text.matches(".*[a-zA-Z]+.*");
        	if(atleastOneAlpha)
        		ans.add(text);
        }
//        System.out.println(ans);
    	return ans;
    }
    
    private static double commentCoherence(String meth,String cmnt){
    	ArrayList<String> cmntWords = parseComment(cmnt);
        ArrayList<String> match = new ArrayList<String>();
        for(String s:splitCamelCaseString(meth)){
        	for(String w:cmntWords){
        		if(lavenstein(s,w)<2){
        			match.add(s);
        		}
        	}
        }
//        System.out.println(match);
        return match.size()*1.0/cmntWords.size();
    }
    
//    public static void main(String [] args) {
//        String [] data = { "kitten", "sitting", "saturday", "sunday", "rosettacode", "raisethysword" };
//        String meth="removeAllMarkar";
//        String cmnt = "removes , all defined markers";
//        double a= commentCoherene(meth,cmnt);
//        System.out.println(a);
//    }
//    
    public static ArrayList<String> splitCamelCaseString(String s){
    	ArrayList<String> words = new ArrayList<String>();	
        for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
        	words.add(w);
        }    
        
        return words;
    }
}










