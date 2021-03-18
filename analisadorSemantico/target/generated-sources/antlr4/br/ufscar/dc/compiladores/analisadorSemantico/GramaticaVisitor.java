// Generated from br\u005Cufscar\dc\compiladores\analisadorSemantico\Gramatica.g4 by ANTLR 4.9.1
package br.ufscar.dc.compiladores.analisadorSemantico;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GramaticaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GramaticaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(GramaticaParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#declaracoes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracoes(GramaticaParser.DeclaracoesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#decl_local_global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_local_global(GramaticaParser.Decl_local_globalContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#declaracao_local}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_local(GramaticaParser.Declaracao_localContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#variavel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariavel(GramaticaParser.VariavelContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#identificador}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentificador(GramaticaParser.IdentificadorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#dimensao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensao(GramaticaParser.DimensaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(GramaticaParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#tipo_basico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_basico(GramaticaParser.Tipo_basicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#tipo_basico_ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_basico_ident(GramaticaParser.Tipo_basico_identContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#tipo_estendido}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_estendido(GramaticaParser.Tipo_estendidoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#valor_constante}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValor_constante(GramaticaParser.Valor_constanteContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#registro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegistro(GramaticaParser.RegistroContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#declaracao_global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_global(GramaticaParser.Declaracao_globalContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#parametro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametro(GramaticaParser.ParametroContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#parametros}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametros(GramaticaParser.ParametrosContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#corpo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCorpo(GramaticaParser.CorpoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd(GramaticaParser.CmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_leia}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_leia(GramaticaParser.Cmd_leiaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_escreva}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_escreva(GramaticaParser.Cmd_escrevaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_se}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_se(GramaticaParser.Cmd_seContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_caso}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_caso(GramaticaParser.Cmd_casoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_para}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_para(GramaticaParser.Cmd_paraContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_enquanto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_enquanto(GramaticaParser.Cmd_enquantoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_faca}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_faca(GramaticaParser.Cmd_facaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_atribuicao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_atribuicao(GramaticaParser.Cmd_atribuicaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_chamada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_chamada(GramaticaParser.Cmd_chamadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cmd_retorne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd_retorne(GramaticaParser.Cmd_retorneContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#selecao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelecao(GramaticaParser.SelecaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#item_selecao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItem_selecao(GramaticaParser.Item_selecaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#constantes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantes(GramaticaParser.ConstantesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#numero_intervalo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumero_intervalo(GramaticaParser.Numero_intervaloContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#op_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_unario(GramaticaParser.Op_unarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#expressao_aritmetica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressao_aritmetica(GramaticaParser.Expressao_aritmeticaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#termo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermo(GramaticaParser.TermoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#fator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFator(GramaticaParser.FatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#op1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp1(GramaticaParser.Op1Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#op2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp2(GramaticaParser.Op2Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#op3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp3(GramaticaParser.Op3Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#parcela}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela(GramaticaParser.ParcelaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#parcela_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_unario(GramaticaParser.Parcela_unarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_nao_unario(GramaticaParser.Parcela_nao_unarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#exp_relacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_relacional(GramaticaParser.Exp_relacionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#op_relacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_relacional(GramaticaParser.Op_relacionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#expressao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressao(GramaticaParser.ExpressaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#termo_logico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermo_logico(GramaticaParser.Termo_logicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#fator_logico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFator_logico(GramaticaParser.Fator_logicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#parcela_logica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_logica(GramaticaParser.Parcela_logicaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#op_logico_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_logico_1(GramaticaParser.Op_logico_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#op_logico_2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_logico_2(GramaticaParser.Op_logico_2Context ctx);
}