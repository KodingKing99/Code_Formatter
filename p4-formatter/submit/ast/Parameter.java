package submit.ast;

/**
 * @author Nick Sorenson
 * @project Code Formatter
 */
public class Parameter implements Declaration {
    private final VarType type;
    private final String paramId;
    public Parameter(VarType type, String paramId){
        this.type = type;
        this.paramId = paramId;
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
        builder.append(paramId);
    }
    @Override
    public String toString(){
        return type.toString() + " " + paramId;
    }
}
