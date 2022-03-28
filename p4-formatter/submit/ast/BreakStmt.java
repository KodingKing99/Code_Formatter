package submit.ast;

public class BreakStmt implements Statement{
    public BreakStmt(){}
    @Override
    public Boolean isCompound() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        builder.append(prefix).append("break;\n");
    }
}
