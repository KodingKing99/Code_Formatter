package submit.ast;

public class Call implements Statement{
    private final String id;

    public Call(String id){
        this.id = id;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        // TODO Auto-generated method stub
        builder.append(prefix);
        builder.append(id).append("(");
    }
    @Override
    public String toString() {
        return id;
    }
}
