package submit.ast;

public class Assignment implements Expression{
    private Mutable mutable;
    private String operator;
    private Expression expression;

    public Assignment(Mutable mutable, String operator, Expression expression){
        this.mutable = mutable;
        this.operator = operator;
        this.expression = expression;
    }
    public Assignment(Mutable mutable, String operator){
        this.mutable = mutable;
        this.operator = operator;
        this.expression = null;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        this.mutable.toCminus(builder, "");
        builder.append(" ").append(this.operator);
        if(this.expression != null){
            builder.append(" ");
            this.expression.toCminus(builder, prefix);
        } 
        builder.append(";\n");
    }
}
