package submit.ast;

public class ExpressionStmt implements Statement{
    private final Expression expr;
    public ExpressionStmt(Expression expr){
        this.expr = expr;
    }
    public ExpressionStmt(){
        this.expr = null;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        if(this.expr != null){
            this.expr.toCminus(builder, prefix);
        }
    }
}
