package submit.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Sorenson
 *         Code referenced from VarType and VarDecleration
 * @project CodeFromatter
 */
public class FunDecleration implements Declaration, Node {
    private final FunType type;
    private final String funName;
    private final List<Parameter> params;
    private final Boolean isStatic;
    private final Statement statement;

    public FunDecleration(FunType type, String funName, List<Parameter> params, Statement statement,
            Boolean isStatic) {
        this.type = type;
        this.funName = funName;
        this.params = params;
        this.isStatic = isStatic;
        this.statement = statement;
    }
    @Override
    public Boolean isCompound() {
        return false;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        builder.append(prefix);
        builder.append("\n");
        if (this.isStatic) {
            builder.append("static ");
        }
        builder.append(type).append(" ");
        builder.append(funName);
        builder.append("(");
        if (this.params.size() > 0) {
            for (Parameter param : this.params) {
                param.toCminus(builder, "");
                builder.append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append(")\n");
        this.statement.toCminus(builder, "");
        // builder.append("\n");
    }
}
