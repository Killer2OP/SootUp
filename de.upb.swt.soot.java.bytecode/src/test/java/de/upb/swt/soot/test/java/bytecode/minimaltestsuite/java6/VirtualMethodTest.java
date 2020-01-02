package de.upb.swt.soot.test.java.bytecode.minimaltestsuite.java6;

import categories.Java8Test;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.test.java.bytecode.minimaltestsuite.MinimalBytecodeTestSuiteBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.experimental.categories.Category;

/** @author Kaustubh Kelkar */
@Category(Java8Test.class)
public class VirtualMethodTest extends MinimalBytecodeTestSuiteBase {

  // @Override
  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        "virtualMethodDemo", getDeclaredClassSignature(), "void", Collections.emptyList());
  }

  @Override
  public List<String> expectedBodyStmts() {
    return Stream.of(
            "l0 := @this: VirtualMethod",
            "$stack3 = new TempEmployee",
            "specialinvoke $stack3.<TempEmployee: void <init>(int,int)>(1500, 150)",
            "l1 = $stack3",
            "$stack4 = new RegEmployee",
            "specialinvoke $stack4.<RegEmployee: void <init>(int,int)>(1500, 500)",
            "l2 = $stack4",
            "$stack5 = <java.lang.System: java.io.PrintStream; out>",
            "$stack6 = virtualinvoke l1.<Employee: int getSalary()>()",
            "virtualinvoke $stack5.<java.io.PrintStream: void println(int)>($stack6)",
            "$stack7 = <java.lang.System: java.io.PrintStream; out>",
            "$stack8 = virtualinvoke l2.<Employee: int getSalary()>()",
            "virtualinvoke $stack7.<java.io.PrintStream: void println(int)>($stack8)",
            "return")
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
