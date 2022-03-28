/*
 * Code formatter project
 * CS 4481
 */
package submit;

import submit.ast.FunType;
import submit.ast.VarType;

/**
 *
 * @author edwajohn
 */
public class SymbolInfo {

  private final String id;
  // In the case of a function, type is the return type
  private final VarType type;
  private final boolean function;
  private final FunType funType;
  public SymbolInfo(String id, VarType type, boolean function) {
    this.id = id;
    this.type = type;
    this.function = function;
    this.funType = null;
  }
  public SymbolInfo(String id, FunType type, boolean function){
    this.id = id;
    this.funType = type;
    this.function = function;
    this.type = null;
  }

  @Override
  public String toString() {
    if(type != null){

      return "<" + id + ", " + type + '>';
    }
    return "<" + id + ", " + funType + ">";
  }

}
