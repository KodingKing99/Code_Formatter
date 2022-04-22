package submit.ast;

public class Factor implements Statement{
    private final Mutable mutable;
    private final Immutable immutable;

    public Factor(Mutable mutable) {
        this.mutable = mutable;
        this.immutable = null;
    }
    public Factor(Immutable immutable) {
        this.mutable = null;
        this.immutable = immutable;
    }
    @Override
    public Boolean isCompound() {
        return false;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(this.mutable != null){
            this.mutable.toCminus(builder, prefix);
        }
        else if(this.immutable != null){
            this.immutable.toCminus(builder, prefix);
        }
    }
}
