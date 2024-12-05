package com.lsadf.bdd.config;

import io.cucumber.java.Before;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BddStackCleaner {

  private final List<Stack<?>> stackList;

  public BddStackCleaner() {
    stackList = new ArrayList<>();
  }

  @Before
  public void clearStacks() {
    stackList.forEach(Stack::clear);
  }

  public void addStack(Stack<?> stack) {
    stackList.add(stack);
  }
}
