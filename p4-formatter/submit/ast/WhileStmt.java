package submit.ast;

public class WhileStmt implements Statement{
    private final Expression simpleExpression;
    private final Statement statement;
    public WhileStmt(Expression simpleExpression, Statement statement){
        this.simpleExpression = simpleExpression;
        this.statement = statement;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix).append("while (");
        this.simpleExpression.toCminus(builder, prefix);
        builder.append(")\n");
        this.statement.toCminus(builder, prefix);

    }
}
