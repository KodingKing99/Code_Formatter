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

        // System.out.println("In assignment to cminus. prefix length: " + prefix.length() + " . Mutable is " + this.mutable.toString());
        builder.append(prefix);
        this.mutable.toCminus(builder, prefix);
        
        if(this.expression != null){
            builder.append(" ").append(this.operator).append(" ");
            this.expression.toCminus(builder, "");
        } 
        else{
            builder.append(this.operator);
        }
        // builder.append("");
    }
}
