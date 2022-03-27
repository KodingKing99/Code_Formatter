package submit.ast;

public class Immutable implements Expression, Node{
    private final Expression expression;
    private final Call call;
    private final Constant constant;

    public Immutable(Call call){
        this.call = call;
        this.expression = null;
        this.constant = null;
    }
    public Immutable (Expression expression){
        this.expression = expression;
        this.call = null;
        this.constant = null;
    }
    public Immutable (Constant constant){
        this.expression = null;
        this.call = null;
        this.constant = constant;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        if(call != null){
            call.toCminus(builder, prefix);
        }
        else if(constant != null){
            constant.toCminus(builder, prefix);
        }
        else if(this.expression != null){
            builder.append("(");
            this.expression.toCminus(builder, "");
            builder.append(")");
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        this.toCminus(builder, prefix);
        return builder.toString();
    }
}
