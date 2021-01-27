package de.upb.swt.soot.callgraph.spark.pag;

import com.sun.javafx.geom.Edge;
import de.upb.swt.soot.callgraph.spark.pag.nodes.Node;
import de.upb.swt.soot.core.jimple.basic.Value;
import de.upb.swt.soot.core.jimple.common.expr.AbstractInvokeExpr;
import de.upb.swt.soot.core.jimple.common.expr.JStaticInvokeExpr;
import de.upb.swt.soot.core.jimple.common.expr.JVirtualInvokeExpr;
import de.upb.swt.soot.core.jimple.common.stmt.JAssignStmt;
import de.upb.swt.soot.core.jimple.common.stmt.Stmt;
import de.upb.swt.soot.core.jimple.javabytecode.stmt.JBreakpointStmt;
import de.upb.swt.soot.core.jimple.visitor.AbstractStmtVisitor;
import de.upb.swt.soot.core.model.Body;
import de.upb.swt.soot.core.model.SootMethod;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.core.types.ReferenceType;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import sun.jvm.hotspot.debugger.cdbg.RefType;
import sun.security.provider.certpath.Vertex;

import javax.annotation.Nonnull;

public class IntraproceduralPointerAssignmentGraph {

    private DefaultDirectedGraph<SparkVertex, SparkEdge> graph;

    private SootMethod method;

    public IntraproceduralPointerAssignmentGraph(SootMethod method){
        // TODO: need chaching?
        this.method = method;
    }

    public void build(){
        if(method.isConcrete()){
            Body body = method.getBody();
            for(Stmt stmt: body.getStmts()){
                processStmt(stmt);
            }
        } else {
            // TODO: build for native
        }
    }

    private void processStmt(Stmt stmt){
        if(!canProcess(stmt)){
            return;
        }
        stmt.accept(new AbstractStmtVisitor() {
            @Override
            final public void caseAssignStmt(JAssignStmt stmt){
                Value leftOp = stmt.getLeftOp();
                Value rightOp = stmt.getRightOp();
                if(!(leftOp.getType() instanceof ReferenceType)){
                    return;
                }
                if (!(rightOp.getType() instanceof ReferenceType))
                    throw new AssertionError("Type mismatch in assignment " + stmt + " in method "
                            + method.getSignature());
                //TODO: is MethodNodeFactory more suitable?
            }

            @Override
            public void caseBreakpointStmt(JBreakpointStmt stmt) {
                super.caseBreakpointStmt(stmt);
            }
        });
    }

    private boolean canProcess(Stmt stmt) {
        // TODO: types-for-invoke
        if(stmt.containsInvokeExpr()){
            AbstractInvokeExpr invokeExpr = stmt.getInvokeExpr();
            if(!isReflectionNewInstance(invokeExpr)){
                return false;
            } else if (!(invokeExpr instanceof JStaticInvokeExpr)){
                return false;
            }
        }
        return true;
    }

    public DefaultDirectedGraph<SparkVertex, SparkEdge> getGraph(){
        return graph;
    }

    /**
     * Checks whether the given invocation is for Class.newInstance()
     *
     * @param invokeExpr
     * @return
     */
    private boolean isReflectionNewInstance(AbstractInvokeExpr invokeExpr){
        // TODO: put this in a utility class?
        if(invokeExpr instanceof JVirtualInvokeExpr){
            JVirtualInvokeExpr virtualInvokeExpr = (JVirtualInvokeExpr) invokeExpr;
            if (virtualInvokeExpr.getBase().getType() instanceof RefType) {
                RefType rt = (RefType) virtualInvokeExpr.getBase().getType();
                if (rt.getClass().getName().equals("java.lang.Class")) {
                    MethodSignature signature = virtualInvokeExpr.getMethodSignature();
                    if (signature.getName().equals("newInstance") && signature.getParameterTypes().size() == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}