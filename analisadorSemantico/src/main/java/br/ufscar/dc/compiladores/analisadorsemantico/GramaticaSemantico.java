package br.ufscar.dc.compiladores.analisadorSemantico;

import br.ufscar.dc.compiladores.analisadorSemantico.TabelaDeSimbolos.TiposGramatica;
import br.ufscar.dc.compiladores.analisadorSemantico.Escopos;
import org.antlr.v4.runtime.Token;
import java.util.ArrayList;
import java.util.HashMap;

public class GramaticaSemantico extends GramaticaBaseVisitor<Void> {

    // Criando os escopos - o escopo já inicializado fará parte das variáveis globais
    Escopos escoposAninhados = new Escopos();
    // Hashmap para guardar registros
    HashMap<String, ArrayList<String>> registros = new HashMap<>();
    // Hashmap para guardar funcoes/procedimentos
    static HashMap<String, ArrayList<TiposGramatica>> funcoes_e_procedimentos = new HashMap<>();

    @Override
    public Void visitPrograma(GramaticaParser.ProgramaContext ctx) {
        // Novo escopo - a segunda lista - indica as variáveis que pertencem a função main.
        escoposAninhados.criarNovoEscopo();
        return super.visitPrograma(ctx);
    }

    public TiposGramatica getTipoVariavel(String tipo) {
        switch (tipo) {
            case "literal":
            case "^literal":
                return TiposGramatica.LITERAL;
            case "inteiro":
            case "^inteiro":
                return TiposGramatica.INTEIRO;
            case "real":
            case "^real":
                return TiposGramatica.REAL;
            case "logico":
            case "^logico":
                return TiposGramatica.LOGICO;
            case "registro":
                return TiposGramatica.REGISTRO;
            default:
                return TiposGramatica.INVALIDO;
        }
    }

    public void adicionaVariavelTabela(String nome, String strTipo, Token tokenNome, Token tokenTipo) {
        // Obtendo o escopo atual
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        System.out.println("STRING: " + strTipo);
        TiposGramatica tipo = getTipoVariavel(strTipo);
        System.out.println("TIPO: " + tipo);
        if (tipo == TiposGramatica.INVALIDO) {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tokenTipo, "tipo " + strTipo + " nao declarado");
        }
        if ( !tabela.existe(nome) ) {
            tabela.adicionar(nome, tipo);
            System.out.println("ADICIONADO VAR " + nome + " DE TIPO " + tipo);
        } else {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tokenNome, "identificador " + nome + " ja declarado anteriormente");
        }
    }

    @Override
    public Void visitDeclaracao_local(GramaticaParser.Declaracao_localContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();

        String strTipoVar = null;
        
        // Caso 1 -- 'declare' variavel
        if (ctx.variavel() != null) {
            // Caso o tipo seja 'registro', adicionamos na tabela de símbolos uma variável do tipo registro, e também as variáveis do registro concatenada por um ponto ao nome do registro!
            if (ctx.variavel().tipo().registro() != null) {
                for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                    adicionaVariavelTabela(ident.getText(), "registro", ident.getStart(), null);
                    for (GramaticaParser.VariavelContext vars : ctx.variavel().tipo().registro().variavel()) {
                        strTipoVar = vars.tipo().getText();
                        for (GramaticaParser.IdentificadorContext ident_reg : vars.identificador()) {
                            adicionaVariavelTabela(ident.getText() + '.' + ident_reg.getText(), strTipoVar, ident_reg.getStart(), vars.tipo().getStart());
                        }
                    }
                }
            // Caso contrário, obtemos o tipo da variável para verificação de integridade para então adicioná-las a Tabela. Tomando cuidado para não 're-adicioná-la'
            } else {
                strTipoVar = ctx.variavel().tipo().getText();
                if (registros.containsKey(strTipoVar)) {
                    ArrayList<String> variaveis_registro = registros.get(strTipoVar);
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        if (tabela.existe(ident.getText())) {
                            AnalisadorSemanticoUtils.adicionarErroSemantico(ident.getStart(), "identificador " + ident.getText() + " ja declarado anteriormente");
                        } else {
                            adicionaVariavelTabela(ident.getText(), "registro", ident.getStart(), ctx.variavel().tipo().getStart());
                            for (int i = 0; i < variaveis_registro.size(); i = i + 2) {
                                adicionaVariavelTabela(ident.getText() + '.' + variaveis_registro.get(i), variaveis_registro.get(i+1), ident.getStart(), ctx.variavel().tipo().getStart());
                            }
                        }
                    }
                } else {
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        adicionaVariavelTabela(ident.getText(), strTipoVar, ident.getStart(), ctx.variavel().tipo().getStart());
                    }
                }
            }

        // Caso 2 -- 'constante' IDENT ':' tipo_basico '=' valor_constante
        } else if (ctx.tipo_basico() != null) {
            strTipoVar = ctx.tipo_basico().getText();
            adicionaVariavelTabela(ctx.IDENT().getText(), strTipoVar, ctx.IDENT().getSymbol(), ctx.IDENT().getSymbol());

        // Caso 3 -- 'tipo' IDENT ':' tipo
        } else {
            if (ctx.tipo().registro() != null) {
                // Gravando na tabela de símbolos o registro
                adicionaVariavelTabela(ctx.IDENT().getText(), "registro", ctx.IDENT().getSymbol(), null);
                // Gravando numa hash global o nome do registro junto a seus atributos
                ArrayList<String> variaveis_registro = new ArrayList<String>();
                for (GramaticaParser.VariavelContext vars : ctx.tipo().registro().variavel()) {
                    strTipoVar = vars.tipo().getText();
                    for (GramaticaParser.IdentificadorContext ident_registro : vars.identificador()) {
                        variaveis_registro.add(ident_registro.getText());
                        variaveis_registro.add(vars.tipo().getText());
                    }
                }
                registros.put(ctx.IDENT().getText(), variaveis_registro);
            } else {
                strTipoVar = ctx.tipo().getText();
                adicionaVariavelTabela(ctx.IDENT().getText(), strTipoVar, ctx.IDENT().getSymbol(), ctx.tipo().getStart());
            }
        }

        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Void visitCmd_atribuicao(GramaticaParser.Cmd_atribuicaoContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        Token tSymbol = ctx.start;
        String nomeVar = ctx.identificador().getText();

        System.out.println("Atribuição de " + nomeVar + " -- Tipo da expresão: " + tipoExpressao); // Verificar o tipo do retorno

        if (tipoExpressao != TiposGramatica.INVALIDO) {
            if (!tabela.existe(nomeVar)) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "identificador " + nomeVar + " nao declarado");
            } else {
                TiposGramatica tipoVariavel = AnalisadorSemanticoUtils.verificarTipo(tabela, nomeVar);
                if (tipoVariavel != tipoExpressao) {
                    if ( !(tipoVariavel == TiposGramatica.REAL) || !(tipoExpressao == TiposGramatica.INTEIRO)) {
                        if (ctx.pointer != null) {
                            nomeVar = "^" + nomeVar;
                        }
                        AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "atribuicao nao compativel para " + nomeVar);
                    }
                }
            }
        } else {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "atribuicao nao compativel para " + nomeVar);
        }
        return super.visitCmd_atribuicao(ctx);
    }

    @Override
    public Void visitCmd_leia(GramaticaParser.Cmd_leiaContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        String nomeVar = ctx.ident1.getText();
        Token tSymbol = ctx.start;           // Token Symbol
        
        if (!tabela.existe(nomeVar)) {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "identificador " + nomeVar + " nao declarado");
        }
        // Outros identificadores
        if (ctx.outrosIdent.size() > 0) {
            for (GramaticaParser.IdentificadorContext idents : ctx.outrosIdent) {
                if (!tabela.existe(idents.getText())) {
                    AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "identificador " + idents.getText() + " nao declarado");
                }
            }
        }
        return super.visitCmd_leia(ctx);
    }

    @Override
    public Void visitCmd_escreva(GramaticaParser.Cmd_escrevaContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao;

        // Verificação de variáveis não declaradas feita em AnalisadorSemanticoUtils, ao analisar seus tipos. Se detectado 'null', não foi declarado.
        for (GramaticaParser.ExpressaoContext expressao : ctx.expressao()) {
            tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, expressao);
        }

        return super.visitCmd_escreva(ctx);
    }

    // 'se' expressao 'entao' (cmd)* ('senao' (cmd)*)? 'fim_se'
    @Override
    public Void visitCmd_se(GramaticaParser.Cmd_seContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        Token tSymbol = ctx.start; 

        System.out.println("Expressao if: " + ctx.expressao().getText() + " de tipo " + tipoExpressao);

        if (tipoExpressao != TiposGramatica.LOGICO) {
            System.out.println("Erro no comando if!");
        }

        return super.visitCmd_se(ctx);
    }

    @Override
    public Void visitCmd_enquanto(GramaticaParser.Cmd_enquantoContext ctx) { 
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        Token tSymbol = ctx.start; 

        System.out.println("Expressao while: " + ctx.expressao().getText() + " de tipo " + tipoExpressao);

        if (tipoExpressao != TiposGramatica.LOGICO) {
            System.out.println("Erro no comando while!");
        }

        return super.visitCmd_enquanto(ctx);
    }

    @Override 
    public Void visitDeclaracao_global(GramaticaParser.Declaracao_globalContext ctx) { 
        escoposAninhados.criarNovoEscopo();
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        ArrayList<TiposGramatica> variaveis_do_parametro = new ArrayList<TiposGramatica>();

        if (ctx.func != null) {
            System.out.println("É uma função!");
            for (GramaticaParser.ParametroContext parametro : ctx.parametros().parametro()) {
                for (GramaticaParser.IdentificadorContext ident : parametro.identificador()) {
                    adicionaVariavelTabela(ident.getText(), parametro.tipo_estendido().getText(), ident.getStart(), ident.getStart());
                    variaveis_do_parametro.add(getTipoVariavel(parametro.tipo_estendido().getText()));
                }
            }
            // Adicionando o tipo de retorno da função!
            variaveis_do_parametro.add(getTipoVariavel(ctx.tipo_estendido().getText()));
            funcoes_e_procedimentos.put(ctx.IDENT().getText(), variaveis_do_parametro);
        } else {
            System.out.println("É um procedimento!");
        }
        
        return super.visitDeclaracao_global(ctx);
    }
    
}