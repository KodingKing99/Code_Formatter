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
    public Boolean isCompound() {
        return false;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        builder.append(prefix);
        builder.append(id).append("(");
        for(Expression expr : this.exprs){
            expr.toCminus(builder, prefix);
            builder.append(", ");
        }
        if(this.exprs.size() > 0){
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append(")");
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        this.toCminus(builder, prefix);
        return builder.toString();
    }
}
