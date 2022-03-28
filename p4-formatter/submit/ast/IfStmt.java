package submit.ast;

import java.util.List;

public class IfStmt implements Statement{
    private Expression simpleExpression;
    private List<Statement> statements;

    public IfStmt(Expression simpleExpression, List<Statement> statements){
        this.simpleExpression = simpleExpression;
        this.statements = statements;
    }
    @Override
    public Boolean isCompound() {
        return false;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append("if (");
        this.simpleExpression.toCminus(builder, "");
        builder.append(")\n");
        String prefixCopy = prefix;
        if(!this.statements.get(0).isCompound()){
            prefix += " ";
        }
        this.statements.get(0).toCminus(builder, prefix);
        prefix = prefixCopy;
        if(statements.size() == 2){
            builder.append(prefix).append("else\n");
            if(!this.statements.get(1).isCompound()){
                prefix += " ";
            }
            this.statements.get(1).toCminus(builder, prefix);
            prefix = prefixCopy;
        }
    }
}
