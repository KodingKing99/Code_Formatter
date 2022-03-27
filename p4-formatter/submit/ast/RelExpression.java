package submit.ast;

public class RelExpression  implements Expression{
    private final Expression sumExpression;
    private final BinaryOperator binaryOperation;
    public RelExpression(Expression sumExpression){
        this.sumExpression = sumExpression;
        this.binaryOperation = null;
    }
    public RelExpression(BinaryOperator binaryOperation){
        this.sumExpression = null;
        this.binaryOperation = binaryOperation;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(this.sumExpression != null){
            this.sumExpression.toCminus(builder, prefix);
        }
        else if(this.binaryOperation != null){
            this.binaryOperation.toCminus(builder, prefix);
        } 
    }
}