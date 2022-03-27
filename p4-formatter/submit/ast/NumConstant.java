/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

/**
 *
 * @author edwajohn
 */
public class NumConstant implements Constant {

  private final int value;

  public NumConstant(int value) {
    this.value = value;
  }

  @Override
  public void toCminus(StringBuilder builder, final String prefix) {
    builder.append(Integer.toString(value));
  }

}
