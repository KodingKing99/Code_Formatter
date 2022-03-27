package submit.ast;

import java.util.List;

public class UnaryExpression implements Expression{
    private final List<UnaryOperator> unaryOperators;
    private final Factor factor;
    public UnaryExpression(List<UnaryOperator> unaryOperators, Factor factor){
        this.unaryOperators = unaryOperators;
        this.factor = factor;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        for(UnaryOperator uOperator : this.unaryOperators){
            uOperator.toCminus(builder, "");
        }
        builder.append(" ");
        factor.toCminus(builder, "");
    }
}
