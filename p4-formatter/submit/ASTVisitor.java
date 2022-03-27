package submit;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.CminusBaseVisitor;
import parser.CminusParser;
import parser.CminusParser.AndExpressionContext;
import parser.CminusParser.CallContext;
import parser.CminusParser.CompoundStmtContext;
import parser.CminusParser.ExpressionContext;
import parser.CminusParser.ExpressionStmtContext;
import parser.CminusParser.FunDeclarationContext;
import parser.CminusParser.ImmutableContext;
import parser.CminusParser.MutableContext;
import parser.CminusParser.OrExpressionContext;
import parser.CminusParser.ParamContext;
import parser.CminusParser.RelExpressionContext;
import parser.CminusParser.SimpleExpressionContext;
import parser.CminusParser.StatementContext;
import parser.CminusParser.UnaryRelExpressionContext;
import parser.CminusParser.VarDeclarationContext;
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
    public Node visitFunDeclaration(FunDeclarationContext ctx) {
        // TODO Auto-generated method stub
        String funText = ctx.getText();
        LOGGER.fine(funText);
        String funId = ctx.ID().getText();
        LOGGER.fine("Function ID: " + funId);
        FunType type = getFunType(ctx.typeSpecifier());
        LOGGER.fine("Type is: " + type.toString());
        List<Parameter> params = new ArrayList<>();
        for (CminusParser.ParamContext p : ctx.param()) {
            VarType paramType = getVarType(p.typeSpecifier());
            String paramId = p.paramId().getText();
            params.add(new Parameter(paramType, paramId));
        }
        LOGGER.fine("Params are: ");
        for (Parameter parameter : params) {
            LOGGER.fine(parameter.toString());
        }
        visitCompoundStmt(ctx.statement().compoundStmt());

        return super.visitFunDeclaration(ctx);
    }

    @Override
    public Node visitStatement(StatementContext ctx) {
        // TODO Auto-generated method stub
        // String s = ctx.getText();
        // LOGGER.fine("In visit Statment, text is: " + s);
        // visitExpressionStmt(ctx.expressionStmt()); 
        return super.visitStatement(ctx);
    }

    @Override
    public Node visitParam(ParamContext ctx) {
        return super.visitParam(ctx);
    }

    @Override
    public Node visitCompoundStmt(CompoundStmtContext ctx) {
        // TODO Auto-generated method stub
        // List<VarDeclaration> decls = new ArrayList<>();
        // List<Statement> stmts = new ArrayList<>();
        List<Node> leafs = new ArrayList<>();
        for (VarDeclarationContext declCtx : ctx.varDeclaration()) {
            leafs.add(visitVarDeclaration(declCtx));
        }
        for (StatementContext stmtCtx : ctx.statement()) {
            visitStatement(stmtCtx);
        }
        return super.visitCompoundStmt(ctx);
        // return new CompoundStatement(leafs);
    }
    @Override
    public Node visitOrExpression(OrExpressionContext ctx) {
        // TODO Auto-generated method stub
        for(AndExpressionContext andCtx :  ctx.andExpression()){
            // visitAndExpression(andCtx);
            // andCtx.
        }
        return super.visitOrExpression(ctx);
    }
    @Override
    public Node visitCall(CallContext ctx) {
        // TODO Auto-generated method stub
        LOGGER.fine("Call is: " + ctx.ID().getText());
        String id = ctx.ID().getText();
        List<Expression> exprs = new ArrayList<>();
        for(ExpressionContext exprCtx : ctx.expression() ){
            exprs.add((Expression) visitExpression(exprCtx));
        }
        return new Call(id, exprs);
        // return super.visitCall(ctx);
    }
    @Override
    public Node visitExpressionStmt(ExpressionStmtContext ctx) {
        // TODO Auto-generated method stub
        return super.visitExpressionStmt(ctx);
    }
    @Override
    public Node visitExpression(ExpressionContext ctx) {
        // TODO Auto-generated method stub
        return super.visitExpression(ctx);
    }
    @Override
    public Node visitAndExpression(AndExpressionContext ctx) {
        // TODO Auto-generated method stub
        return super.visitAndExpression(ctx);
    }
    @Override
    public Node visitSimpleExpression(SimpleExpressionContext ctx) {
        // TODO Auto-generated method stub
        return super.visitSimpleExpression(ctx);
    }
    @Override
    public Node visitUnaryRelExpression(UnaryRelExpressionContext ctx) {
        // TODO Auto-generated method stub
        return super.visitUnaryRelExpression(ctx);
    }
    @Override
    public Node visitRelExpression(RelExpressionContext ctx) {
        // TODO Auto-generated method stub
        return super.visitRelExpression(ctx);
    }
    @Override
    public Node visitImmutable(ImmutableContext ctx) {
        // TODO Auto-generated method stub
        if(ctx.expression() != null){
            // return
        }
        else if(ctx.call() != null){
            LOGGER.fine("In visit Immutable");
            Node m = visitCall(ctx.call());
            LOGGER.fine(((Call) m).toString());
            return new Immutable((Call) visitCall(ctx.call()));
        }
        else if(ctx.constant() != null){
            return new Immutable((Constant) visitConstant(ctx.constant()));
        }
        return super.visitImmutable(ctx);
    }
    @Override
    public Node visitMutable(MutableContext ctx) {
        String id = ctx.ID().getText();
        if(ctx.expression() != null){
            Node index = visitExpression(ctx.expression());
            return new Mutable(id, (Expression) index);
        }
        else{
            return new Mutable(id, null);
        }
    }
    // @Override public Node visitReturnStmt(CminusParser.ReturnStmtContext ctx) {
    // if (ctx.expression() != null) {
    // return new Return((Expression) visitExpression(ctx.expression()));
    // }
    // return new Return(null);
    // }

    @Override public Node visitConstant(CminusParser.ConstantContext ctx) {
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
    
    // TODO implement whatever methods make sense
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitDeclaration(CminusParser.DeclarationContext ctx) {
    // return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitVarDeclId(CminusParser.VarDeclIdContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitFunDeclaration(CminusParser.FunDeclarationContext
    // ctx) { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitTypeSpecifier(CminusParser.TypeSpecifierContext ctx)
    // { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitParam(CminusParser.ParamContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitParamId(CminusParser.ParamIdContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitStatement(CminusParser.StatementContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitCompoundStmt(CminusParser.CompoundStmtContext ctx) {
    // return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitExpressionStmt(CminusParser.ExpressionStmtContext
    // ctx) { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitIfStmt(CminusParser.IfStmtContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitWhileStmt(CminusParser.WhileStmtContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitBreakStmt(CminusParser.BreakStmtContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitExpression(CminusParser.ExpressionContext ctx) {
    // return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitSimpleExpression(CminusParser.SimpleExpressionContext
    // ctx) { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitOrExpression(CminusParser.OrExpressionContext ctx) {
    // return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitAndExpression(CminusParser.AndExpressionContext ctx)
    // { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T
    // visitUnaryRelExpression(CminusParser.UnaryRelExpressionContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitRelExpression(CminusParser.RelExpressionContext ctx)
    // { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitRelop(CminusParser.RelopContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitSumExpression(CminusParser.SumExpressionContext ctx)
    // { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitSumop(CminusParser.SumopContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitTermExpression(CminusParser.TermExpressionContext
    // ctx) { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitMulop(CminusParser.MulopContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitUnaryExpression(CminusParser.UnaryExpressionContext
    // ctx) { return visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitUnaryop(CminusParser.UnaryopContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitFactor(CminusParser.FactorContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitMutable(CminusParser.MutableContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitImmutable(CminusParser.ImmutableContext ctx) { return
    // visitChildren(ctx); }
    // /**
    // * {@inheritDoc}
    // *
    // * <p>The default implementation returns the result of calling
    // * {@link #visitChildren} on {@code ctx}.</p>
    // */
    // @Override public T visitCall(CminusParser.CallContext ctx) { return
    // visitChildren(ctx); }

}
