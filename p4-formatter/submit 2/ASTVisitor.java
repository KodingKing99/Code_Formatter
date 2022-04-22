package submit;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.CminusBaseVisitor;
import parser.CminusParser;
import parser.CminusParser.AndExpressionContext;
import parser.CminusParser.BreakStmtContext;
import parser.CminusParser.CallContext;
import parser.CminusParser.CompoundStmtContext;
import parser.CminusParser.ExpressionContext;
import parser.CminusParser.ExpressionStmtContext;
import parser.CminusParser.FactorContext;
import parser.CminusParser.FunDeclarationContext;
import parser.CminusParser.IfStmtContext;
import parser.CminusParser.ImmutableContext;
import parser.CminusParser.MulopContext;
import parser.CminusParser.MutableContext;
import parser.CminusParser.OrExpressionContext;
import parser.CminusParser.ParamContext;
import parser.CminusParser.RelExpressionContext;
import parser.CminusParser.RelopContext;
import parser.CminusParser.SimpleExpressionContext;
import parser.CminusParser.StatementContext;
import parser.CminusParser.SumExpressionContext;
import parser.CminusParser.SumopContext;
import parser.CminusParser.TermExpressionContext;
import parser.CminusParser.UnaryExpressionContext;
import parser.CminusParser.UnaryRelExpressionContext;
import parser.CminusParser.UnaryopContext;
import parser.CminusParser.VarDeclarationContext;
import parser.CminusParser.WhileStmtContext;
import submit.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ASTVisitor extends CminusBaseVisitor<Node> {
    private final Logger LOGGER;
    private SymbolTable symbolTable;

    public ASTVisitor(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }

    private VarType getVarType(CminusParser.TypeSpecifierContext ctx) {
        final String t = ctx.getText();
        return (t.equals("int")) ? VarType.INT : (t.equals("bool")) ? VarType.BOOL : VarType.CHAR;
    }

    private FunType getFunType(CminusParser.TypeSpecifierContext ctx) {
        final String t = ctx.getText();
        return (t.equals("void")) ? FunType.VOID
                : (t.equals("int")) ? FunType.INT : (t.equals("bool")) ? FunType.BOOL : FunType.CHAR;
    }

    private BinaryOperatorType getBiOpType(CminusParser.MulopContext ctx) {
        final String t = ctx.getText();
        return BinaryOperatorType.fromString(t);
    }

    private BinaryOperatorType getBiOpType(CminusParser.SumopContext ctx) {
        final String t = ctx.getText();
        return BinaryOperatorType.fromString(t);
    }

    private BinaryOperatorType getBiOpType(CminusParser.RelopContext ctx) {
        final String t = ctx.getText();
        return BinaryOperatorType.fromString(t);
    }

    private BinaryOperator computeBinaryOperator(List<BinaryOperatorType> types, List<Expression> exprs) {
        int index = types.size() - 1;
        BinaryOperator bo = new BinaryOperator(exprs.get(index), types.get(index), exprs.get(index + 1));
        index -= 1;
        while (index >= 0) {
            bo = new BinaryOperator(exprs.get(index), types.get(index), bo);
            index -= 1;
        }
        return bo;
    }

    @Override
    public Node visitProgram(CminusParser.ProgramContext ctx) {
        symbolTable = new SymbolTable();
        List<Declaration> decls = new ArrayList<>();
        for (CminusParser.DeclarationContext d : ctx.declaration()) {
            decls.add((Declaration) visitDeclaration(d));
        }
        return new Program(decls);
    }
    @Override
    public Node visitIfStmt(IfStmtContext ctx) {
        Expression expr = (Expression) visitSimpleExpression(ctx.simpleExpression());
        List<Statement> stmts = new ArrayList<>();
        for(StatementContext stmtCtx : ctx.statement()){
            stmts.add((Statement) visitStatement(stmtCtx));
        }
        return new IfStmt(expr, stmts);
    }
    @Override
    public Node visitVarDeclaration(CminusParser.VarDeclarationContext ctx) {
        for (CminusParser.VarDeclIdContext v : ctx.varDeclId()) {
            String id = v.ID().getText();
            LOGGER.fine("Var ID: " + id);
        }
        // return null;
        VarType type = getVarType(ctx.typeSpecifier());
        List<String> ids = new ArrayList<>();
        List<Integer> arraySizes = new ArrayList<>();
        for (CminusParser.VarDeclIdContext v : ctx.varDeclId()) {
            String id = v.ID().getText();
            ids.add(id);
            symbolTable.addSymbol(id, new SymbolInfo(id, type, false));
            if (v.NUMCONST() != null) {
                arraySizes.add(Integer.parseInt(v.NUMCONST().getText()));
            } else {
                arraySizes.add(-1);
            }
        }
        final boolean isStatic = false;
        return new VarDeclaration(type, ids, arraySizes, isStatic);
    }
    @Override
    public Node visitParam(ParamContext ctx) {
        VarType paramType = getVarType(ctx.typeSpecifier());
        String paramId = ctx.paramId().ID().getText();
        String paramText = ctx.paramId().getText();
        // LOGGER.fine("Param id is " + paramId);
        return new Parameter(paramType, paramId, paramText);
    }
    @Override
    public Node visitFunDeclaration(FunDeclarationContext ctx) {
        // TODO Auto-generated method stub
        String funText = ctx.getText();
        LOGGER.fine(funText);
        String funName = ctx.ID().getText();
        LOGGER.fine("Function ID: " + funName);
        FunType type;
        if(ctx.typeSpecifier() != null){
            type = getFunType(ctx.typeSpecifier());
        }
        else{
            type = FunType.VOID;
        }
        symbolTable.addSymbol(funName, new SymbolInfo(funName, type, true));
        symbolTable = symbolTable.createChild();
        LOGGER.fine("Type is: " + type.toString());
        List<Parameter> params = new ArrayList<>();
        for (CminusParser.ParamContext pCtx : ctx.param()) {
            Parameter param = (Parameter) visitParam(pCtx);
            params.add(param);
            symbolTable.addSymbol(param.getId(), new SymbolInfo(param.getId(), param.getType(), false));
        }
        // LOGGER.fine("Params are: ");
        // for (Parameter parameter : params) {
        //     LOGGER.fine(parameter.toString());
        // }
        Statement statement = (Statement) visitStatement(ctx.statement());
        symbolTable = symbolTable.getParent();
        return new FunDecleration(type, funName, params, statement, false);
    }

    @Override
    public Node visitStatement(StatementContext ctx) {
        if(ctx.expressionStmt() != null){
            return visitExpressionStmt(ctx.expressionStmt());
        }
        else if (ctx.ifStmt() != null){
            return visitIfStmt(ctx.ifStmt());
        }
        else if (ctx.compoundStmt() != null){
            return visitCompoundStmt(ctx.compoundStmt());
        }
        else if (ctx.whileStmt() != null){
            return visitWhileStmt(ctx.whileStmt());
        }
        else if (ctx.breakStmt() != null){
            return visitBreakStmt(ctx.breakStmt());
        }
        else return super.visitStatement(ctx);
    }



    @Override
    public Node visitCompoundStmt(CompoundStmtContext ctx) {
        // TODO Auto-generated method stub
        symbolTable = symbolTable.createChild();
        List<VarDeclaration> decls = new ArrayList<>();
        List<Statement> stmts = new ArrayList<>();
        for (VarDeclarationContext declCtx : ctx.varDeclaration()) {
            decls.add((VarDeclaration) visitVarDeclaration(declCtx));
        }
        for (StatementContext stmtCtx : ctx.statement()) {
            stmts.add((Statement) visitStatement(stmtCtx));
        }
        symbolTable = symbolTable.getParent();
        return new CompoundStatement(decls, stmts);
    }

    @Override
    public Node visitWhileStmt(WhileStmtContext ctx) {
        Expression simpleExpression = (Expression) visitSimpleExpression(ctx.simpleExpression());
        Statement statement = (Statement) visitStatement(ctx.statement());
        return new WhileStmt(simpleExpression, statement);
    }

    @Override
    public Node visitBreakStmt(BreakStmtContext ctx) {
        return new BreakStmt();
    }

    @Override
    public Node visitCall(CallContext ctx) {
        // LOGGER.fine("Call is: " + ctx.ID().getText());
        String id = ctx.ID().getText();
        if(symbolTable.find(id) == null){
            LOGGER.warning("Undefined symbol on line " + ctx.getStart().getLine() + ": " + id);
        }
        List<Expression> exprs = new ArrayList<>();
        for (ExpressionContext exprCtx : ctx.expression()) {
            exprs.add((Expression) visitExpression(exprCtx));
        }
        return new Call(id, exprs);
    }

    @Override
    public Node visitExpressionStmt(ExpressionStmtContext ctx) {
        if(ctx.expression() != null){
            return new ExpressionStmt((Expression) visitExpression(ctx.expression()));
        }
        else{
            return new ExpressionStmt();
        }
    }

    @Override
    public Node visitExpression(ExpressionContext ctx) {
        if(ctx.simpleExpression() != null){
            return visitSimpleExpression(ctx.simpleExpression());
        }
        else if(ctx.mutable() != null){
            Mutable mutable = (Mutable) visitMutable(ctx.mutable());
            String operator = ctx.children.get(1).getText();
            if(ctx.expression() != null){
                Expression expression = (Expression) visitExpression(ctx.expression());
                return new Assignment(mutable, operator, expression);
            }
            return new Assignment(mutable, operator);
        }
        else{
            return super.visitExpression(ctx);
        }
    }

    @Override
    public Node visitSimpleExpression(SimpleExpressionContext ctx) {
        return visitOrExpression(ctx.orExpression());
    }

    @Override
    public Node visitOrExpression(OrExpressionContext ctx) {
        List<BinaryOperatorType> binaryOperatorTypes = new ArrayList<>();
        List<Expression> andExpressions = new ArrayList<>();
        for (AndExpressionContext orExCtx : ctx.andExpression()) {
            andExpressions.add((Expression) visitAndExpression(orExCtx));
        }
        for(int i = 0; i < andExpressions.size() - 1; i++){
            binaryOperatorTypes.add(BinaryOperatorType.fromString("||"));
        }
        if (binaryOperatorTypes.size() > 0) {
            BinaryOperator bn = computeBinaryOperator(binaryOperatorTypes, andExpressions);
            return new RelExpression(bn);
        } else {
            return new RelExpression(andExpressions.get(0));
        }
    }

    @Override
    public Node visitAndExpression(AndExpressionContext ctx) {
        List<BinaryOperatorType> binaryOperatorTypes = new ArrayList<>();
        List<Expression> sumExpressions = new ArrayList<>();
        for (UnaryRelExpressionContext unRelCtx : ctx.unaryRelExpression()) {
            sumExpressions.add((Expression) visitUnaryRelExpression(unRelCtx));
        }
        for(int i = 0; i < sumExpressions.size() - 1; i++){
            binaryOperatorTypes.add(BinaryOperatorType.fromString("&&"));
        }
        if (binaryOperatorTypes.size() > 0) {
            BinaryOperator bn = computeBinaryOperator(binaryOperatorTypes, sumExpressions);
            // LOGGER.fine("In And expression. Binary op is: " + bn.toString());
            return new RelExpression(bn);
        } else {
            return new RelExpression(sumExpressions.get(0));
        }
    }

    @Override
    public Node visitUnaryRelExpression(UnaryRelExpressionContext ctx) {
        List<String> bangs = new ArrayList<>();
        for (TerminalNode term : ctx.BANG()) {
            bangs.add(term.getText());
        }
        RelExpression relExpression = (RelExpression) visitRelExpression(ctx.relExpression());
        return new UnaryRelExpression(relExpression, bangs);
    }

    @Override
    public Node visitRelExpression(RelExpressionContext ctx) {
        List<BinaryOperatorType> binaryOperatorTypes = new ArrayList<>();
        for (RelopContext mulopContext : ctx.relop()) {
            binaryOperatorTypes.add(getBiOpType(mulopContext));
        }
        List<Expression> sumExpressions = new ArrayList<>();
        for (SumExpressionContext sumContext : ctx.sumExpression()) {
            sumExpressions.add((Expression) visitSumExpression(sumContext));
        }
        if (binaryOperatorTypes.size() > 0) {
            BinaryOperator bn = computeBinaryOperator(binaryOperatorTypes, sumExpressions);
            // LOGGER.fine("In sum expression. Binary op is: " + bn.toString());
            return new RelExpression(bn);
        } else {
            return new RelExpression(sumExpressions.get(0));
        }
    }

    @Override
    public Node visitSumExpression(SumExpressionContext ctx) {
        List<BinaryOperatorType> binaryOperatorTypes = new ArrayList<>();
        for (SumopContext mulopContext : ctx.sumop()) {
            binaryOperatorTypes.add(getBiOpType(mulopContext));
        }
        List<Expression> termExpressions = new ArrayList<>();
        for (TermExpressionContext termContext : ctx.termExpression()) {
            termExpressions.add((Expression) visitTermExpression(termContext));
        }
        if (binaryOperatorTypes.size() > 0) {
            BinaryOperator bn = computeBinaryOperator(binaryOperatorTypes, termExpressions);
            // LOGGER.fine("In sum expression. Binary op is: " + bn.toString());
            return new SumExpression(bn);
        } else {
            return new SumExpression(termExpressions.get(0));
        }
    }

    @Override
    public Node visitTermExpression(TermExpressionContext ctx) {
        List<BinaryOperatorType> binaryOperatorTypes = new ArrayList<>();
        for (MulopContext mulopContext : ctx.mulop()) {
            binaryOperatorTypes.add(getBiOpType(mulopContext));
        }
        List<Expression> urnExpressions = new ArrayList<>();
        for (UnaryExpressionContext urnContext : ctx.unaryExpression()) {
            urnExpressions.add((Expression) visitUnaryExpression(urnContext));
        }
        if (binaryOperatorTypes.size() > 0) {
            BinaryOperator bn = computeBinaryOperator(binaryOperatorTypes, urnExpressions);
            // LOGGER.fine("In term expression. Binary op is: " + bn.toString());
            return new TermExpression(bn);
        } else {
            return new TermExpression(urnExpressions.get(0));
        }
    }
    @Override
    public Node visitUnaryop(UnaryopContext ctx) {
        String operator = ctx.getText();
        return new UnaryOperator(operator);
    }

    @Override
    public Node visitUnaryExpression(UnaryExpressionContext ctx) {
        List<UnaryOperator> unaryOperators = new ArrayList<>();
        for (UnaryopContext unOpCtx : ctx.unaryop()) {
            unaryOperators.add((UnaryOperator) visitUnaryop(unOpCtx));
        }
        Factor factor = (Factor) visitFactor(ctx.factor());
        return new UnaryExpression(unaryOperators, factor);
    }

    @Override
    public Node visitFactor(FactorContext ctx) {
        if (ctx.mutable() != null) {
            return new Factor((Mutable) visitMutable(ctx.mutable()));
        } else { // if (ctx.immutable() != null){
            return new Factor((Immutable) visitImmutable(ctx.immutable()));
        }
    }

    @Override
    public Node visitImmutable(ImmutableContext ctx) {
        final Immutable node;
        if (ctx.expression() != null) {
            node = new Immutable((Expression) visitExpression(ctx.expression()));
        } else if (ctx.call() != null) {
            node = new Immutable((Call) visitCall(ctx.call()));
        } else { // if (ctx.constant() != null) {
            node = new Immutable((Constant) visitConstant(ctx.constant()));
        }
        // LOGGER.fine("Immutable node: " + node.toString());
        return node;
    }

    @Override
    public Node visitMutable(MutableContext ctx) {
        String id = ctx.ID().getText();
        if(symbolTable.find(id) == null){
            LOGGER.warning("Undefined symbol on line " + ctx.getStart().getLine() + ": " + id);
        }
        if (ctx.expression() != null) {
            Node index = visitExpression(ctx.expression());
            return new Mutable(id, (Expression) index);
        } else {
            return new Mutable(id, null);
        }
    }
    @Override public Node visitReturnStmt(CminusParser.ReturnStmtContext ctx) {
    if (ctx.expression() != null) {
    return new Return((Expression) visitExpression(ctx.expression()));
    }
    return new Return(null);
    }

    @Override
    public Node visitConstant(CminusParser.ConstantContext ctx) {
        final Node node;
        if (ctx.NUMCONST() != null) {
            node = new NumConstant(Integer.parseInt(ctx.NUMCONST().getText()));
        } else if (ctx.CHARCONST() != null) {
            node = new CharConstant(ctx.CHARCONST().getText().charAt(0));
        } else if (ctx.STRINGCONST() != null) {
            node = new StringConstant(ctx.STRINGCONST().getText());
        } else {
            node = new BoolConstant(ctx.getText().equals("true"));
        }
        return node;
    }
}
