package submit.ast;

public class BreakStmt implements Statement{
    public BreakStmt(){}

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        builder.append(prefix).append("break;\n");
    }
}
