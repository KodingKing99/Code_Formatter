package submit.ast;

public interface Constant extends Expression{
    @Override
    public abstract void toCminus(StringBuilder builder, String prefix);
}
