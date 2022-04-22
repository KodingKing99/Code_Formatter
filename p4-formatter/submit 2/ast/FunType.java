package submit.ast;

/**
 * @author Nick Sorenson
 * code Referenced from VarType
 */
public enum FunType {
    INT("int"), VOID("void"), CHAR("char"), BOOL("bool");
    private final String value;

    private FunType(String value){ this.value = value; }

    public static FunType fromString(String s){
        for(FunType ft : FunType.values()){
            if(ft.value.equals(s)){
                return ft;
            }
        }
        throw new RuntimeException("Illegal Type in FunType.fromString()");
    }
    @Override
    public String toString(){
        return value;
    }

}
