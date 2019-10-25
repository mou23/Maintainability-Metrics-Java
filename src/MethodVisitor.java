import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.Expression;
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
//    	System.out.println(name);
    	
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
			}
		});
        return false;
    }  
}
