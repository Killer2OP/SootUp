package de.upb.swt.soot.test.java.bytecode.minimaltestsuite.java6;

import static org.junit.Assert.assertTrue;

import categories.Java8Test;
import de.upb.swt.soot.core.model.SootClass;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.test.java.bytecode.minimaltestsuite.MinimalBytecodeTestSuiteBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/** @author Kaustubh Kelkar */
@Category(Java8Test.class)
public class InterfaceImplClassTest extends MinimalBytecodeTestSuiteBase {
  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        "interfaceMethod", getDeclaredClassSignature(), "void", Collections.emptyList());
  }

  @Test
  @Override
  public void defaultTest() {
    super.defaultTest();
    SootClass clazz = loadClass(getDeclaredClassSignature());
    assertTrue(
        clazz.getInterfaces().stream()
            .anyMatch(
                javaClassType -> {
                  return javaClassType.getClassName().equalsIgnoreCase("InterfaceImpl");
                }));
  }

  @Override
  public List<String> expectedBodyStmts() {
    return Stream.of(
            "l0 := @this: InterfaceImplClass",
            "$stack1 = <java.lang.System: java.io.PrintStream; out>",
            "virtualinvoke $stack1.<java.io.PrintStream: void print(java.lang.String)>(\"Method from InterfaceImpl is implemented\")",
            "$stack2 = <java.lang.System: java.io.PrintStream; out>",
            "virtualinvoke $stack2.<java.io.PrintStream: void println(java.lang.String)>(\"Variable from InterfaceImpl is 10\")",
            "return")
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
