package submit.ast;

import java.util.ArrayList;

/**
 * @author Nick Sorenson
 * Code referenced from VarType and VarDecleration
 * @project CodeFromatter 
 */
public class FunDecleration implements Declaration, Node{
    private final FunType type;
    private final String funName;
    private final ArrayList<Parameter> params;
    private final Boolean isStatic;
    public FunDecleration(FunType type, String funName, ArrayList<Parameter> params, Boolean isStatic){
        this.type = type;
        this.funName = funName;
        this.params = params;
        this.isStatic = isStatic;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        builder.append(prefix);
        if(isStatic){
            builder.append("static ");
        }
        builder.append(type).append(" ");
        builder.append(funName).append(" ");
    }
}
