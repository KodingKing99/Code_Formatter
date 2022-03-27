package submit.ast;

import java.util.List;

public class TermExpression implements Expression {
    private final List<UnaryExpression> unaryExpressions;

    public TermExpression(List<UnaryExpression> unaryExpressions){
        this.unaryExpressions = unaryExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        
    }
}
