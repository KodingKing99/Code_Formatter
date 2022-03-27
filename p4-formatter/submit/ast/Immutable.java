package submit.ast;

public class Immutable implements Node{
    private final Expression expression;
    private final Call call;

    public Immutable(Call call){
        this.call = call;
        this.expression = null;
    }
    public Immutable (Expression expression){
        this.expression = expression;
        this.call = null;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        if(call != null){
            call.toCminus(builder, prefix);
        }
    }
}
