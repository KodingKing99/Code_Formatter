package submit.ast;

import java.util.List;

public class TermExpression implements Expression {
    private final Expression unaryExpression;
    private final BinaryOperator binaryOperation;
    public TermExpression(Expression unaryExpression){
        this.unaryExpression = unaryExpression;
        this.binaryOperation = null;
    }
    public TermExpression(BinaryOperator binaryOperation){
        this.unaryExpression = null;
        this.binaryOperation = binaryOperation;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(this.unaryExpression != null){
            this.unaryExpression.toCminus(builder, prefix);
        }
        else if(this.binaryOperation != null){
            this.binaryOperation.toCminus(builder, prefix);
        } 
    }
}
