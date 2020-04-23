package de.upb.swt.soot.test.core.printer;

import static org.junit.Assert.*;

import de.upb.swt.soot.core.Project;
import de.upb.swt.soot.core.frontend.OverridingClassSource;
import de.upb.swt.soot.core.frontend.OverridingMethodSource;
import de.upb.swt.soot.core.inputlocation.EagerInputLocation;
import de.upb.swt.soot.core.jimple.basic.StmtPositionInfo;
import de.upb.swt.soot.core.jimple.common.constant.IntConstant;
import de.upb.swt.soot.core.jimple.common.stmt.JNopStmt;
import de.upb.swt.soot.core.jimple.common.stmt.JReturnVoidStmt;
import de.upb.swt.soot.core.jimple.common.stmt.Stmt;
import de.upb.swt.soot.core.jimple.javabytecode.stmt.JSwitchStmt;
import de.upb.swt.soot.core.model.*;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.core.util.EscapedWriter;
import de.upb.swt.soot.core.util.Utils;
import de.upb.swt.soot.core.util.printer.Printer;
import de.upb.swt.soot.core.views.View;
import de.upb.swt.soot.java.core.JavaProject;
import de.upb.swt.soot.java.core.language.JavaLanguage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import org.junit.Test;

public class LegacyJimplePrinterTest {

  SootClass buildClass(List<Stmt> stmts) {

    Project project =
        JavaProject.builder(new JavaLanguage(8)).addClassPath(new EagerInputLocation()).build();
    View view = project.createOnDemandView();

    Body body = new Body(Collections.emptySet(), Collections.emptyList(), stmts, null);

    MethodSignature methodSignature =
        view.getIdentifierFactory()
            .getMethodSignature("main", "dummyMain", "void", Collections.emptyList());
    SootMethod dummyMainMethod =
        new SootMethod(
            new OverridingMethodSource(methodSignature, body),
            methodSignature,
            EnumSet.of(Modifier.PUBLIC, Modifier.STATIC),
            Collections.emptyList());

    return new SootClass(
        new OverridingClassSource(
            new EagerInputLocation(),
            null,
            view.getIdentifierFactory().getClassType("dummyMain"),
            null,
            Collections.emptySet(),
            null,
            Collections.emptySet(),
            Collections.singleton(dummyMainMethod),
            null,
            EnumSet.of(Modifier.PUBLIC)),
        SourceType.Application);
  }

  @Test
  public void printSwitchStmt() {

    StmtPositionInfo noPosInfo = StmtPositionInfo.createNoStmtPositionInfo();
    ArrayList<IntConstant> lookupValues = new ArrayList<>();
    lookupValues.add(IntConstant.getInstance(42));
    lookupValues.add(IntConstant.getInstance(33102));

    ArrayList<Stmt> targets = new ArrayList<>();
    final JReturnVoidStmt returnstmt = new JReturnVoidStmt(noPosInfo);
    final JNopStmt jNop = new JNopStmt(noPosInfo);
    targets.add(returnstmt);
    targets.add(jNop);

    Stmt tableSwitch = new JSwitchStmt(IntConstant.getInstance(42), 4, 5, targets, jNop, noPosInfo);

    SootClass tableClass = buildClass(Arrays.asList(tableSwitch, jNop, returnstmt));

    StringWriter sw = new StringWriter();
    new Printer(Printer.Option.LegacyMode)
        .printTo(tableClass, new PrintWriter(new EscapedWriter(sw)));

    assertEquals(
        Arrays.asList(
            "public static void main()",
            "tableswitch(42)",
            "case 4: goto label2",
            "case 5: goto label1",
            "default: goto label1",
            "label1:",
            "nop",
            "label2:",
            "return"),
        Utils.filterJimple(sw.toString()));

    Stmt lookupSwitch =
        new JSwitchStmt(IntConstant.getInstance(123), lookupValues, targets, jNop, noPosInfo);

    SootClass lookupClass = buildClass(Arrays.asList(lookupSwitch, jNop, returnstmt));

    StringWriter sw2 = new StringWriter();
    new Printer(Printer.Option.LegacyMode)
        .printTo(lookupClass, new PrintWriter(new EscapedWriter(sw2)));

    assertEquals(
        Arrays.asList(
            "public static void main()",
            "lookupswitch(123)",
            "case 42: goto label2",
            "case 33102: goto label1",
            "default: goto label1",
            "label1:",
            "nop",
            "label2:",
            "return"),
        Utils.filterJimple(sw2.toString()));
  }

  @Test(expected = RuntimeException.class)
  public void testValidOptions() {
    Printer p = new Printer(Printer.Option.UseImports, Printer.Option.LegacyMode);
    p.printTo(
        buildClass(
            Collections.singletonList(new JNopStmt(StmtPositionInfo.createNoStmtPositionInfo()))),
        new PrintWriter(new StringWriter()));
  }
}