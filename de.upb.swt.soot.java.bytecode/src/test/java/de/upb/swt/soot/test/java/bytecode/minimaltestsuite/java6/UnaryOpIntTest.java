package de.upb.swt.soot.test.java.bytecode.minimaltestsuite.java6;

import categories.Java8Test;
import de.upb.swt.soot.core.model.SootMethod;
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
public class UnaryOpIntTest extends MinimalBytecodeTestSuiteBase {

  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        "methodUnaryOpInt", getDeclaredClassSignature(), "void", Collections.emptyList());
  }

  @Test
  public void test() {
    SootMethod method = loadMethod(getMethodSignature());
    assertJimpleStmts(method, expectedBodyStmts());
    /**
     * TODO Do we need to check the type of variable as int?
     * assertTrue(getFields().stream().anyMatch(sootField -> {return
     * sootField.getType().equals("int");}));
     */
  }

  @Override
  public List<String> expectedBodyStmts() {
    return Stream.of(
            "l0 := @this: UnaryOpInt",
            "$stack3 = l0.<UnaryOpInt: int i>",
            "$stack2 = l0.<UnaryOpInt: int j>",
            "l1 = $stack3 + $stack2",
            "return")
        .collect(Collectors.toCollection(ArrayList::new));
  }
}