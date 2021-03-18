package br.ufscar.dc.compiladores.analisadorSemantico;

import br.ufscar.dc.compiladores.analisadorSemantico.TabelaDeSimbolos.TiposGramatica;
import org.antlr.v4.runtime.Token;
import java.util.ArrayList;

public class GramaticaSemantico extends GramaticaBaseVisitor<Void> {

    TabelaDeSimbolos tabela;

    @Override
    public Void visitPrograma(GramaticaParser.ProgramaContext ctx) {
        tabela = new TabelaDeSimbolos();
        return super.visitPrograma(ctx);
    }

    // **AJUSTAR O TOKEN**
    @Override
    public Void visitDeclaracao_local(GramaticaParser.Declaracao_localContext ctx) {
        ArrayList<String> nomesVar = new ArrayList<String>();
        String strTipoVar;
        TiposGramatica tipoVar = TiposGramatica.INVALIDO;
        Token tSymbol = ctx.start;
        
        // Caso 1 -- 'declare' variavel
        if (ctx.variavel() != null) {
            // Primeiro identificador "ident1" (obrigatório)
            nomesVar.add(ctx.variavel().ident1.getText());
            // Outros identificadores
            if (ctx.variavel().outrosIdent.size() > 0) {
                for (GramaticaParser.IdentificadorContext idents : ctx.variavel().outrosIdent) {
                    nomesVar.add(idents.getText());
                }
            }
            strTipoVar = ctx.variavel().tipo().getText();
        // Caso 2 -- 'constante' IDENT ':' tipo_basico '=' valor_constante
        } else if (ctx.tipo_basico() != null) {
            nomesVar.add(ctx.IDENT().getText());
            strTipoVar = ctx.tipo_basico().getText();
            tSymbol = ctx.IDENT().getSymbol();
        // Caso 3 -- 'tipo' IDENT ':' tipo
        } else {
            nomesVar.add(ctx.IDENT().getText());
            strTipoVar = ctx.tipo().getText();
            tSymbol = ctx.IDENT().getSymbol();
        }
        
        switch (strTipoVar) {
            case "literal":
            case "^literal":
                tipoVar = TiposGramatica.LITERAL;
                break;
            case "inteiro":
            case "^inteiro":
                tipoVar = TiposGramatica.INTEIRO;
                break;
            case "real":
            case "^real":
                tipoVar = TiposGramatica.REAL;
                break;
            case "logico":
            case "^logico":
                tipoVar = TiposGramatica.LOGICO;
                break;
            default:
                // Caso não esteja escrito corretamente
                AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "tipo " + strTipoVar + " nao declarado");
                break;
        }

        // Adiciona na tabela de símbolo aquelas que estão corretas.
        for (String nome : nomesVar) {
            if (tabela.existe(nome)) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "identificador " + nome + " ja declarado anteriormente");
            } else {
                tabela.adicionar(nome, tipoVar);
            }
        }

        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Void visitCmd_atribuicao(GramaticaParser.Cmd_atribuicaoContext ctx) {
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        Token tSymbol = ctx.start;
        String nomeVar = ctx.identificador().getText();

        System.out.println("Variável: " + AnalisadorSemanticoUtils.verificarTipo(tabela, nomeVar) + " -- Tipo da expresão: " + tipoExpressao); // Verificar o tipo do retorno

        if (tipoExpressao != TiposGramatica.INVALIDO) {
            if (!tabela.existe(nomeVar)) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "identificador " + nomeVar + " nao declarado");
            } else {
                TiposGramatica tipoVariavel = AnalisadorSemanticoUtils.verificarTipo(tabela, nomeVar);
                if (tipoVariavel != tipoExpressao) {
                    AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "atribuicao nao compativel para " + nomeVar);
                }
            }
        } else {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "atribuicao nao compativel para " + nomeVar);
        }
        return super.visitCmd_atribuicao(ctx);
    }

    @Override
    public Void visitCmd_leia(GramaticaParser.Cmd_leiaContext ctx) {
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

    // 'se' expressao 'entao' (cmd)* ('senao' (cmd)*)? 'fim_se'
    @Override
    public Void visitCmd_se(GramaticaParser.Cmd_seContext ctx) {
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        Token tSymbol = ctx.start; 

        if (tipoExpressao != TiposGramatica.INVALIDO) {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tSymbol, "erro na expressao do if!");
        }

        return super.visitCmd_se(ctx);
    }

    @Override
    public Void visitExpressao_aritmetica(GramaticaParser.Expressao_aritmeticaContext ctx) {
        AnalisadorSemanticoUtils.verificarTipo(tabela, ctx);
        return super.visitExpressao_aritmetica(ctx);
    }
}