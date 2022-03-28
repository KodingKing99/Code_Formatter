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
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append("{\n");
        for (VarDeclaration decl : this.decls) {
            decl.toCminus(builder, "  ");
        }
        for (Statement stmt : this.stmts) {
            stmt.toCminus(builder, "  ");
        }
        builder.append("}\n");
    }
}
