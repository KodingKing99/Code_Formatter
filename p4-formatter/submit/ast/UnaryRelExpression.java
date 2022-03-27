package submit.ast;

import java.util.List;

public class UnaryRelExpression implements Expression {
    private final Expression relExpression;
    private final List<String> bangs;
    public UnaryRelExpression(Expression relExpression, List<String> bangs){
        this.relExpression = relExpression;
        this.bangs = bangs;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        for(String bang : this.bangs){
            builder.append((bang));
        }
        this.relExpression.toCminus(builder, "");
    }
}
