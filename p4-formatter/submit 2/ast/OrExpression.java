package submit.ast;

public class OrExpression implements Expression {
    private final Expression andExpression;
    private final BinaryOperator binaryOperation;
    public OrExpression(Expression andExpression){
        this.andExpression = andExpression;
        this.binaryOperation = null;
    }
    public OrExpression(BinaryOperator binaryOperation){
        this.andExpression = null;
        this.binaryOperation = binaryOperation;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(this.andExpression != null){
            this.andExpression.toCminus(builder, prefix);
        }
        else if(this.binaryOperation != null){
            this.binaryOperation.toCminus(builder, prefix);
        } 
    }
}
