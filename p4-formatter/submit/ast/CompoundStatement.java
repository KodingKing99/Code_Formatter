package submit.ast;

import java.util.ArrayList;
import java.util.List;

public class CompoundStatement implements Statement{

    private final List<Node> leafs;
    // private final ArrayList<Statement> stmts;

    public CompoundStatement(List<Node> leafs){
        this.leafs = leafs;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        for(Node leaf : this.leafs){
            leaf.toCminus(builder, "");
        }
    }
}
