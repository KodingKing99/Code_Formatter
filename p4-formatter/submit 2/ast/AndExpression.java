package submit.ast;

public class AndExpression implements Expression{
    private final Expression unaryRelExpression;
    private final BinaryOperator binaryOperation;
    public AndExpression(Expression unaryRelExpression){
        this.unaryRelExpression = unaryRelExpression;
        this.binaryOperation = null;
    }
    public AndExpression(BinaryOperator binaryOperation){
        this.unaryRelExpression = null;
        this.binaryOperation = binaryOperation;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(this.unaryRelExpression != null){
            this.unaryRelExpression.toCminus(builder, prefix);
        }
        else if(this.binaryOperation != null){
            this.binaryOperation.toCminus(builder, prefix);
        } 
    }
}
