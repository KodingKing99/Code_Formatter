package submit.ast;

import java.util.ArrayList;
import java.util.List;

public class CompoundStatement implements Statement {

    private final List<VarDeclaration> decls;
    private final List<Statement> stmts;

    public CompoundStatement(List<VarDeclaration> decls, List<Statement> stmts) {
        this.decls = decls;
        this.stmts = stmts;
    }

    @Override
    public Boolean isCompound() {
        return true;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        String prefixCopy = prefix;
        builder.append(prefix);
        builder.append("{\n");
        prefix += "  ";
        for (VarDeclaration decl : this.decls) {
            decl.toCminus(builder, prefix);
        }
        for (Statement stmt : this.stmts) {
            stmt.toCminus(builder, prefix);
        }
        // remove the last two spaces 
        prefix = prefixCopy;
        builder.append(prefix).append("}\n");
    }
}
