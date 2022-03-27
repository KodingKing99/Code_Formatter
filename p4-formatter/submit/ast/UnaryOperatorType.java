package submit.ast;

public enum UnaryOperatorType {
    MINUS("-"), TIMES("*"), TURNARY("?");
    private final String value;

    private UnaryOperatorType(String value) {
      this.value = value;
    }
  
    public static UnaryOperatorType fromString(String s) {
      for (UnaryOperatorType at : UnaryOperatorType.values()) {
        if (at.value.equals(s)) {
          return at;
        }
      }
      throw new RuntimeException("Illegal string in OperatorType.fromString(): " + s);
    }
  
    @Override
    public String toString() {
      return value;
    }
}
