package submit.ast;

/**
 * @author Nick Sorenson
 * @project Code Formatter
 */
public class Parameter implements Declaration {
    private final VarType type;
    private final String paramId;
    private final String paramText;
    public Parameter(VarType type, String paramId, String paramText){
        this.type = type;
        this.paramId = paramId;
        this.paramText = paramText;
    }
    public String getId(){
        return this.paramId;
    }
    public VarType getType(){
        return this.type;
    }
    @Override
    public Boolean isCompound() {
        return false;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append(type.toString()).append(" ");
        builder.append(paramText);
    }
    @Override
    public String toString(){
        return type.toString() + " " + paramText;
    }
}
