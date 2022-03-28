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
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append("if(");
        this.simpleExpression.toCminus(builder, "");
        builder.append(")\n");
        this.statements.get(0).toCminus(builder, prefix);
        if(statements.size() == 2){
            builder.append("else\n");
            this.statements.get(1).toCminus(builder, prefix);
        }
    }
}
