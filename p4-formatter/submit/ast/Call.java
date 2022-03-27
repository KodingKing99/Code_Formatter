package submit.ast;

import java.util.List;

public class Call implements Statement{
    private final String id;
    private final List<Expression> exprs;
    public Call(String id, List<Expression> exprs){
        this.id = id;
        this.exprs = exprs;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        builder.append(prefix);
        builder.append(id).append("(");
        for(Expression expr : this.exprs){
            expr.toCminus(builder, "");
            builder.append(", ");
        }
        // builder.
        builder.delete(builder.length() - 2, builder.length());
        builder.append(");\n");
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        this.toCminus(builder, prefix);
        return builder.toString();
    }
}
