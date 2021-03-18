// Utilitário para a verificação dos tipos das variáveis, assim como expressões, tanto lógica quanto aritmética.

package br.ufscar.dc.compiladores.analisadorSemantico;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;

public class AnalisadorSemanticoUtils {
    public static List<String> errosSemanticos = new ArrayList<>();
    
    // Armazena um erro semântico na lista quando detectado um (Armazenado o Token que originou o erro e uma mensagem referente ao erro)
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha;
        if (t != null){
            linha = t.getLine();                    // Obtém a linha do erro
        } else {
            linha = 0;
        }
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem)); // Armazenando a mensagem de erro (Com a posição referente ao erro)
    }
    
    // * * * * * * * * * * *
    // EXPRESSÃO ARITMÉTICA
    // * * * * * * * * * * *
    
    // Recebe uma expressão aritmética da parser e verifica o tipo resultante da expressão.
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.Expressao_aritmeticaContext ctx) {
        // Retorno da função
        TabelaDeSimbolos.TiposGramatica ret = null;
        // Varredura dos termos aritméticos para verificação de seus tipos.
        for (GramaticaParser.TermoContext termo : ctx.termo()) {
            TabelaDeSimbolos.TiposGramatica aux = verificarTipo(tabela, termo);
            // Caso em que há apenas um termo, que é o resultado da expressão.
            if (ret == null) {
                ret = aux;
            // Verificar os tipos de ambos termos da operação.
            } else if (ret != aux ) {
                // Caso o retorno aux seja 'null', significa que a variável não foi declarada.
                if (aux == null) {
                    adicionarErroSemantico(ctx.start, "identificador " + termo.getText() + " nao declarado");
                // REAL ( + | - ) INTEIRO
                } else if ((ret == TabelaDeSimbolos.TiposGramatica.REAL) && (aux == TabelaDeSimbolos.TiposGramatica.INTEIRO)) {
                    return TabelaDeSimbolos.TiposGramatica.REAL;
                // INTEIRO ( + | - ) REAL
                } else if ((ret == TabelaDeSimbolos.TiposGramatica.INTEIRO) && (aux == TabelaDeSimbolos.TiposGramatica.REAL)) {
                    return TabelaDeSimbolos.TiposGramatica.REAL;
                // Os dois inteiros ou reais
                } else if ((ret == aux) && ((aux == TabelaDeSimbolos.TiposGramatica.INTEIRO) || (aux == TabelaDeSimbolos.TiposGramatica.REAL))) {
                    return ret;
                }
                // Interrompe a execução, retornando inválido para tratar o erro em 'GramaticaSemantico.java'
                return TabelaDeSimbolos.TiposGramatica.INVALIDO;
            }
        }
        return ret;
    }

    // A lógica é a mesma que a etapa anterior, dessa vez examinando o 'termo'
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.TermoContext ctx) {
        TabelaDeSimbolos.TiposGramatica ret = null;

        // Examina seus fatores
        for (GramaticaParser.FatorContext fator : ctx.fator()) {
            TabelaDeSimbolos.TiposGramatica aux = verificarTipo(tabela, fator);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux ) {
                if (aux == null) {
                    adicionarErroSemantico(ctx.start, "identificador " + fator.getText() + " nao declarado");
                // REAL ( / | * ) INTEIRO
                } else if ((ret == TabelaDeSimbolos.TiposGramatica.REAL) && (aux == TabelaDeSimbolos.TiposGramatica.INTEIRO)) {
                    return TabelaDeSimbolos.TiposGramatica.REAL;
                // INTEIRO ( / | * ) REAL
                } else if ((ret == TabelaDeSimbolos.TiposGramatica.INTEIRO) && (aux == TabelaDeSimbolos.TiposGramatica.REAL)) {
                    return TabelaDeSimbolos.TiposGramatica.REAL;
                // Os dois inteiros ou reais
                } else if ((ret == aux) && ((aux == TabelaDeSimbolos.TiposGramatica.INTEIRO) || (aux == TabelaDeSimbolos.TiposGramatica.REAL))) {
                    return ret;
                }
                return TabelaDeSimbolos.TiposGramatica.INVALIDO;
            }
        }
        return ret;
    }

    // A lógica é a mesma que a etapa anterior, dessa vez examinando o 'fator'
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.FatorContext ctx) {
        
        TabelaDeSimbolos.TiposGramatica ret = null;

        // Examina suas parcelas
        for (GramaticaParser.ParcelaContext parcela : ctx.parcela()) {
            TabelaDeSimbolos.TiposGramatica aux = verificarTipo(tabela, parcela);
            if (ret == null) {
                ret = aux;
            } else if (ret != TabelaDeSimbolos.TiposGramatica.INTEIRO && aux != TabelaDeSimbolos.TiposGramatica.INTEIRO) {
                if (aux == null) {
                    adicionarErroSemantico(ctx.start, "identificador " + parcela.getText() + " nao declarado");
                }
                return TabelaDeSimbolos.TiposGramatica.INVALIDO;
            }
        }
        return ret;
    }
    
    // A lógica é a mesma que a etapa anterior, dessa vez examinando o 'parcela'
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.ParcelaContext ctx) {
        TabelaDeSimbolos.TiposGramatica ret = null;
        // Caso tenha uma parcela_unaria...
        if (ctx.parcela_unario() != null) {
            ret = verificarTipo(tabela, ctx.parcela_unario());
        // Ou então tenh uma parcela_nao_unaria...
        } else if (ctx.parcela_nao_unario() != null) {
            ret = verificarTipo(tabela, ctx.parcela_nao_unario());
        }
        return ret;
    }

    // Em caso da presença da 'parcela_unario'...
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.Parcela_unarioContext ctx) {
        TabelaDeSimbolos.TiposGramatica ret = null;
        
        // No caso do identificador ser -- '^'? identificador -- basta olhar na tabela seu tipo e retorná-lo.
        if (ctx.identificador() != null) {
            if (tabela.existe(ctx.identificador().getText())) {
                ret = tabela.verificar(ctx.identificador().getText());
            }
        }
        // Caso seja do tipo -- IDENT '(' expressao (',' expressao)* ')' -- verificar o tipo de IDENT e também expressões subsequentes usando a recursão
        else if (ctx.IDENT() != null) {
            // Verificar o tipo da variável e expressões
            String nomeVar = ctx.IDENT().getText();
            if (!tabela.existe(nomeVar)) {
                adicionarErroSemantico(ctx.IDENT().getSymbol(), "identificador " + nomeVar + " nao declarado");
                return TabelaDeSimbolos.TiposGramatica.INVALIDO;
            }
            // Examina as expressoes presentes na parcela_unaria...
            for (GramaticaParser.ExpressaoContext expressao : ctx.expressao()) {
                TabelaDeSimbolos.TiposGramatica aux = verificarTipo(tabela, expressao);
                if (ret == null) {
                    ret = aux;
                } else if (ret != aux && aux != TabelaDeSimbolos.TiposGramatica.INVALIDO) {
                    if (aux == null) {
                        adicionarErroSemantico(ctx.start, "??? - identificador " + expressao.getText() + " nao declarado");
                    }
                    return TabelaDeSimbolos.TiposGramatica.INVALIDO;
                }
            }
            if (ret != tabela.verificar(nomeVar))
                ret = TabelaDeSimbolos.TiposGramatica.INVALIDO;
        }
        // Caso seja constante, basta que retornemos seu tipo!
        else if (ctx.NUM_INT() != null) {
            ret = TabelaDeSimbolos.TiposGramatica.INTEIRO;
        }
        else if (ctx.NUM_REAL() != null) {
            ret = TabelaDeSimbolos.TiposGramatica.REAL;
        // Caso seja uma expressão entre parênteses -- '(' expressao ')' -- retornar o tipo dela, usando-se da recursão.
        } else {
            ret = verificarTipo(tabela, ctx.expr);
        }
        return ret;
    }
    
    // * * * * * * * * *
    // EXPRESSÃO LÓGICA
    // * * * * * * * * *
    
    // Recebe uma expressão lógica da parser e verifica o tipo resultante da expressão.
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.ExpressaoContext ctx) {
        // Retorno da função
        TabelaDeSimbolos.TiposGramatica ret = null;
        // Varredura dos termos lógicos para verificação de seus tipos.
        for (GramaticaParser.Termo_logicoContext termo_logico : ctx.termo_logico()) {
            TabelaDeSimbolos.TiposGramatica aux = verificarTipo(tabela, termo_logico);
            // Caso em que há apenas um termo, que é o resultado da expressão.
            if (ret == null) {
                ret = aux;
            // Caso em que o tipo do retorno referente ao termo não é igual ao tipo resultante até o momento! Reportar erro semântico.
            } else if (ret != aux && aux != TabelaDeSimbolos.TiposGramatica.INVALIDO) {
                if (aux == null) {
                    adicionarErroSemantico(ctx.start, "identificador " + termo_logico.getText() + " nao declarado");
                }
                return TabelaDeSimbolos.TiposGramatica.INVALIDO;
            }
        }
        return ret;
    }
    
    // A lógica é a mesma que a etapa anterior, dessa vez examinando o 'termo_logico'
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.Termo_logicoContext ctx) {
        TabelaDeSimbolos.TiposGramatica ret = null;

        // Examina seus fatores
        for (GramaticaParser.Fator_logicoContext fator_logico : ctx.fator_logico()) {
            TabelaDeSimbolos.TiposGramatica aux = verificarTipo(tabela, fator_logico);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TiposGramatica.INVALIDO) {
                if (aux == null) {
                    adicionarErroSemantico(ctx.start, "identificador " + fator_logico.getText() + " nao declarado");
                }
                return TabelaDeSimbolos.TiposGramatica.INVALIDO;
            }
        }
        return ret;
    }
    
    // A lógica é a mesma que a etapa anterior, dessa vez examinando o 'fator_logico', por consequência 'parcela_logica'.
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.Fator_logicoContext ctx) {
        if (ctx.parcela_logica().exp_relacional() != null) {
            // Ver o resultado da expressão relacional
            return verificarTipo(tabela, ctx.parcela_logica().exp_relacional());
        } else {
            return TabelaDeSimbolos.TiposGramatica.LOGICO;
        }
    }

    // Diante de duas expresões aritméticas, verificar o tipo dessas e retornar seus tipos caso iguais ou Invalido caso haja inconsistência.
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.Exp_relacionalContext ctx) {
        TabelaDeSimbolos.TiposGramatica ret1 = null, ret2 = null;
        ret1 = verificarTipo(tabela, ctx.expr_arit_1);
        if (ctx.expr_arit_2 != null) {
            ret2 = verificarTipo(tabela, ctx.expr_arit_2);
            if (ret1 == ret2)
                return TabelaDeSimbolos.TiposGramatica.LOGICO;
            else 
                return TabelaDeSimbolos.TiposGramatica.INVALIDO;

        }
        return ret1;
    }
    
    // Em caso da presença da 'parcela_nao_unario'...
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, GramaticaParser.Parcela_nao_unarioContext ctx) {
        // Caso seja uma cadeia, é literal
        if (ctx.CADEIA() != null) {
            return TabelaDeSimbolos.TiposGramatica.LITERAL;
        }
        // Caso contrário, será o valor do identificador (mas antes verificar sua existência).
        String nomeVar = ctx.identificador().getText();
        if (!tabela.existe(nomeVar)) {
            adicionarErroSemantico(null, "identificador " + nomeVar + " nao declarado");
            return TabelaDeSimbolos.TiposGramatica.INVALIDO;
        }
        return tabela.verificar(nomeVar);
    }
    
    public static TabelaDeSimbolos.TiposGramatica verificarTipo(TabelaDeSimbolos tabela, String nomeVar) {
        return tabela.verificar(nomeVar);
    }
}

