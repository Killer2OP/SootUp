package de.upb.swt.soot.test.java.bytecode.minimaltestsuite.java6;

import categories.Java8Test;
import de.upb.swt.soot.core.model.SootMethod;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.test.java.bytecode.minimaltestsuite.MinimalBytecodeTestSuiteBase;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/** @author Kaustubh Kelkar */
@Category(Java8Test.class)
public class DoWhileLoopTest extends MinimalBytecodeTestSuiteBase {
  @Override
  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        "doWhileLoop", getDeclaredClassSignature(), "void", Collections.emptyList());
  }

  /**
   *
   *
   * <pre>
   * public void doWhileLoop() {
   * int num = 10;
   * int i = 0;
   * do {
   * i++;
   * } while (num > i);
   *
   * </pre>
   */
  /**
   *
   *
   * <pre>
   * public void whileLoop(){
   * int num = 10;
   * int i = 0;
   * while(num>i){
   * num--;
   * }
   *
   * </pre>
   */
  @Override
  public List<String> expectedBodyStmts() {
    return Stream.of(
            "l0 := @this: DoWhileLoop",
            "l1 = 10",
            "l2 = 0",
            "label1:",
            "l2 = l2 + 1",
            "$stack4 = l1",
            "$stack3 = l2",
            "if $stack4 > $stack3 goto label1",
            "return")
        .collect(Collectors.toList());
  }

  @Test
  public void test() {
    SootMethod method = loadMethod(getMethodSignature());
    assertJimpleStmts(method, expectedBodyStmts());
  }
}
