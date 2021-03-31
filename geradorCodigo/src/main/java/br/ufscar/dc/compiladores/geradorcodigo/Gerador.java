package br.ufscar.dc.compiladores.geradorCodigo;

import br.ufscar.dc.compiladores.geradorCodigo.TabelaDeSimbolos.TiposGramatica;
import java.util.ArrayList;
import java.util.HashMap;

public class Gerador extends GramaticaBaseVisitor<Void> {

    StringBuilder saida;
    TabelaDeSimbolos tabela;
    HashMap<String, ArrayList<String>> registros = new HashMap<>();

    // O código a seguir será executado caso passe pela verificação semântica. Logo, não é necessário verificar detalhes, apenas traduzir
    //  o código em LA para C. Apenas detalhes de tipo de variáveis e registros são necessários manter, explicados adiante.

    public Gerador() {
        saida = new StringBuilder();
        this.tabela = new TabelaDeSimbolos();
    }

    // Método para obter o tipo da variável na linguagem C.
    public static String getTipoVariavelString(String tipo) {
        String tipoVar, strTipoVar;
        switch (tipo) {
            case "literal":
                return "char";
            case "^literal":
                return "char*";
            case "inteiro":
                return "int";
            case "^inteiro":
                return "int*";
            case "real":
                return "float";
            case "^real":
                return "float*";
            case "logico":
                return "boolean";
            case "^logico":
                return "boolean*";
            case "registro":
                return "struct";
            default:
                return null;
        }
    }

    // Método para obter o tipo da variável segundo 'TiposGramatica' dado o tipo em LA.
    public static TiposGramatica getTipoVariavelTiposGramatica(String tipo) {
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

    // A partir do tipo, obtém a string para o scanf daquele tipo em C.
    public static String getTipoScanf(TiposGramatica tipo) {
        switch (tipo) {
            case LITERAL:
                return "%s";
            case INTEIRO:
                return "%d";
            case REAL:
                return "%f";
            case LOGICO:
                return "%d";
            default:
                return null;
        }
    }

    /* Programa */

    // O 'visitPrograma' rege a geração do código, incluindo as bibliotecas, assim como as declaração e funções, além da função main.
    @Override
    public Void visitPrograma(GramaticaParser.ProgramaContext ctx) {
        saida.append("#include <stdio.h>\n");
        saida.append("#include <stdlib.h>\n");
        saida.append("#include <string.h>\n");
        saida.append("\n");
        ctx.declaracoes().decl_local_global().forEach(dec -> visitDecl_local_global(dec));
        saida.append("\n");
        saida.append("int main() {\n");
        ctx.corpo().declaracao_local().forEach(dec -> visitDeclaracao_local(dec));
        ctx.corpo().cmd().forEach(cmd -> visitCmd(cmd));
        saida.append("return 0;\n");
        saida.append("}\n");
        return null;
    }

    // O método a seguir é um 'switch', destinado a redirecionar os 'comandos' para serem tratados de determinada maneira.
    @Override 
    public Void visitCmd(GramaticaParser.CmdContext ctx) {
        if (ctx.cmd_atribuicao() != null) {
            return visitCmd_atribuicao(ctx.cmd_atribuicao());
        } else if (ctx.cmd_leia() != null) {
            return visitCmd_leia(ctx.cmd_leia());
        } else if (ctx.cmd_escreva() != null) {
            return visitCmd_escreva(ctx.cmd_escreva());
        } else if (ctx.cmd_se() != null) {
            return visitCmd_se(ctx.cmd_se());
        } else if (ctx.cmd_caso() != null) {
            return visitCmd_caso(ctx.cmd_caso());
        } else if (ctx.cmd_enquanto() != null) {
            return visitCmd_enquanto(ctx.cmd_enquanto());
        } else if (ctx.cmd_chamada() != null) {
            return visitCmd_chamada(ctx.cmd_chamada());
        } else if (ctx.cmd_para() != null) {
            return visitCmd_para(ctx.cmd_para());
        } else if (ctx.cmd_faca() != null) {
            return visitCmd_faca(ctx.cmd_faca());
        } else if (ctx.cmd_retorne() != null) {
            return visitCmd_retorne(ctx.cmd_retorne());
        }
        return null;
    }

    // O método 'visitDecl_local_global' redireciona a qual deve ser o tipo de declaração a se tratar, definido posteriormente.
    @Override 
    public Void visitDecl_local_global(GramaticaParser.Decl_local_globalContext ctx) {
        if (ctx.declaracao_global() != null) {
            visitDeclaracao_global(ctx.declaracao_global());
        } else {    // declaracao_local
            visitDeclaracao_local(ctx.declaracao_local());
        }
        return null;
    }

    // O método 'visitDeclaracao_global' serve para verificar se é um procedimento ou uma função. A descreve então no novo código, chamando a 
    //  recursão para tratar os comandos dentro do seu escopo.
    @Override
    public Void visitDeclaracao_global(GramaticaParser.Declaracao_globalContext ctx) {
        // Caso seja uma função...
        if (ctx.func != null) {
            saida.append(getTipoVariavelString(ctx.tipo_estendido().getText()) + " " + ctx.IDENT().getText() + "( ");
            visitParametros(ctx.parametros());
            saida.append(" ) {\n");
            ctx.declaracao_local().forEach(dec -> visitDeclaracao_local(dec));
            ctx.cmd().forEach(cmd -> visitCmd(cmd));
            saida.append("}\n\n");
        // Caso seja um procedimento...
        } else {
            saida.append("void " + ctx.IDENT().getText() + "( ");
            visitParametros(ctx.parametros());
            saida.append(" ) {\n");
            ctx.declaracao_local().forEach(dec -> visitDeclaracao_local(dec));
            ctx.cmd().forEach(cmd -> visitCmd(cmd));
            saida.append("}\n\n");
        }
        return null;
    }

    // o método 'visitDeclaracao_local' a seguir trata os diferentes tipos de declaração.
    @Override 
    public Void visitDeclaracao_local(GramaticaParser.Declaracao_localContext ctx) {
        // Caso seja do tipo - 'declare' variavel - então obtém o seu tipo, e a converte tanto para C quanto para TiposGramatica. Em seguida, verifica se é
        //  um registro ou não. Caso seja do tipo 'registro', o correspondente em C é a declaração de um struct. Adiciona-se também a tabela de símbolos
        //  todas as variáveis com ponto, a fim de obter o tipo de retorno quando necessário. Caso seja de um tipo definido anteriormente - em else if -
        //  então verificamos na hash o seu tipo para obtenção das variáveis correspondentes, e então a declaramos da mesma forma que anteriormente. Caso
        //  não seja nenhum dos dois, então a veriável é um tipo básico, sendo nessesário apenas traduzir para C. Vale lembrar que se a variável for um
        //  literal, adicionamos uma dimensão de 80 para que se torne uma string.
        if (ctx.variavel() != null) {
            String strTipoVar = getTipoVariavelString(ctx.variavel().tipo().getText());
            TiposGramatica tipoVar = getTipoVariavelTiposGramatica(ctx.variavel().tipo().getText());
            // Caso seja um struct, a formatação deverá ser diferente de tipos basicos. Adicionamos à tabela o registro e seu tipo, assim como suas extensões.
            if (ctx.variavel().tipo().registro() != null) {
                for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                    tabela.adicionar(ident.getText(), TiposGramatica.REGISTRO);
                    saida.append("struct " + " {\n");
                    for (GramaticaParser.VariavelContext vars : ctx.variavel().tipo().registro().variavel()) {
                        strTipoVar = getTipoVariavelString(vars.tipo().getText());
                        for (GramaticaParser.IdentificadorContext ident_reg : vars.identificador()) {
                            tabela.adicionar(ident.getText() + '.' + ident_reg.getText(), getTipoVariavelTiposGramatica(vars.tipo().getText()));
                            if (strTipoVar.equals("char")) {
                                saida.append("\t" + strTipoVar + " " + ident_reg.getText() + "[80];\n");
                            } else {
                                saida.append("\t" + strTipoVar + " " + ident_reg.getText() + ";\n");
                            }
                        }
                    }
                    saida.append("} " + ident.getText() + ";\n");
                }
            // Registro declarado anteriormente
            } else if (registros.containsKey(ctx.variavel().tipo().getText())) {
                ArrayList<String> variaveis_registro = registros.get(ctx.variavel().tipo().getText());
                for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                    saida.append(ctx.variavel().tipo().getText() + " " + ident.getText() + ";\n");
                    for (int i = 0; i < variaveis_registro.size(); i = i + 2) {
                        tabela.adicionar(ident.getText() + '.' + variaveis_registro.get(i), getTipoVariavelTiposGramatica(variaveis_registro.get(i+1)));
                    }
                } 
            // Tipo básico
            } else {
                saida.append(strTipoVar + " ");
                for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                    // Caso tenha dimensão, adicioná-la na tabela sem dimensão
                    if (ident.dimensao() != null) {
                        tabela.adicionar(ident.id.getText(), tipoVar);
                    }
                    // Caso seja char, adiciona uma dimensão para deixá-la padrão 'string'
                    if (strTipoVar.equals("char")) {
                        saida.append(ident.getText() + "[80]");
                    } else {
                        saida.append(ident.getText());
                    }
                    tabela.adicionar(ident.getText(), tipoVar);
                    saida.append(", ");
                }
                saida.delete(saida.length()-2, saida.length()); // Deleta a vírgula e espaço a mais...
                saida.append(";\n");
            }
        // Caso seja do tipo - 'constante' IDENT ':' tipo_basico '=' valor_constante - tem-se a declaração de um valor que se mantém constante.
        //  Logo, usamos o '#define' para declara-la em C.
        } else if (ctx.tipo_basico() != null) {
            saida.append("#define " + ctx.IDENT().getText() + " " + ctx.valor_constante().getText());
            tabela.adicionar(ctx.IDENT().getText(), getTipoVariavelTiposGramatica(ctx.tipo_basico().getText()));
        // Por fim, caso seja do tipo - 'tipo' IDENT ':' tipo - temos a definição de uma nova estrutura. Assim, declaramos em C um 'typedef struct',
        //  adicionando suas variáveis não na tabela, mas na hash 'registros', pois este é um modelo, não uma variável efetiva. 
        } else {
            ArrayList<String> variaveis_registro = new ArrayList<String>();
            saida.append("typedef struct {\n");
            for (GramaticaParser.VariavelContext vars : ctx.tipo().registro().variavel()) {
                String strTipoVar = vars.tipo().getText();
                for (GramaticaParser.IdentificadorContext ident_registro : vars.identificador()) {
                    variaveis_registro.add(ident_registro.getText());
                    variaveis_registro.add(vars.tipo().getText());
                    if (getTipoVariavelString(vars.tipo().getText()).equals("char")) {
                        saida.append("\t" + getTipoVariavelString(strTipoVar) + " " + ident_registro.getText() + "[80];\n");
                    } else {
                        saida.append("\t" + getTipoVariavelString(strTipoVar) + " " + ident_registro.getText() + ";\n");
                    }
                }
            }
            saida.append("} " + ctx.IDENT().getText() + ";\n");
            registros.put(ctx.IDENT().getText(), variaveis_registro);
        }

        return null;
    }

    // 'visitParametros' é responsável por tratar os parâmetros das funções. Assim, obtemos o tipo da variável, seguido de seu nome.
    //  Caso seja um char, então deve ser um ponteiro, pois é um literal de dimensão [80].
    @Override 
    public Void visitParametros(GramaticaParser.ParametrosContext ctx) {
        for (GramaticaParser.ParametroContext param : ctx.parametro()) {
            String strTipoVar = param.tipo_estendido().getText();
            for (GramaticaParser.IdentificadorContext ident : param.identificador()) {
                tabela.adicionar(ident.getText(), getTipoVariavelTiposGramatica(param.tipo_estendido().getText()));
                // Verificar se temos char, sendo necessário um ponteiro para a passagem de strings.
                if (getTipoVariavelString(strTipoVar).equals("char")) {
                    saida.append( getTipoVariavelString(strTipoVar) + "* " + ident.getText() + ",");
                } else {
                    saida.append( getTipoVariavelString(strTipoVar) + " " + ident.getText() + ",");
                }
            }
            saida.deleteCharAt(saida.length()-1);
        }
        return null;
    }

    // 'visitCmd_leia' obtém de início as strings referentes as leituras pela função 'getTipoScanf'. Ao juntá-los, obtém os identificadores,
    //  acompanhados do '&' para indicar endereço.
    @Override 
    public Void visitCmd_leia(GramaticaParser.Cmd_leiaContext ctx) {
        saida.append("scanf(\"");
        for (GramaticaParser.IdentificadorContext ident : ctx.identificador()) { 
            // Se estamos diante de um elemento com dimensão, removê-la para obter seu tipo na tabela.
            if (ident.dimensao() != null) {
                saida.append(getTipoScanf(tabela.verificar(ident.id.getText())) + " ");
            } else {
                saida.append(getTipoScanf(tabela.verificar(ident.getText())) + " ");
            }
        }
        // Retira eventual espaço.
        saida.deleteCharAt(saida.length()-1);
        saida.append("\"");
        for (GramaticaParser.IdentificadorContext ident : ctx.identificador()) { 
            saida.append(", &" + ident.getText());
        }
        saida.append(");\n");

        return null;
    }

    // 'visitCmd_escreva' obtém as expressoes do seu parâmetro, verificando seu tipo de leitura. Ao fim, obtém os identificadores.
    @Override 
    public Void visitCmd_escreva(GramaticaParser.Cmd_escrevaContext ctx) {
        saida.append("printf(\"");
        for (GramaticaParser.ExpressaoContext expressao : ctx.expressao()) {
            saida.append(getTipoScanf(AnalisadorSemanticoUtils.verificarTipo(tabela, expressao)));
        }
        saida.append("\",");
        for (GramaticaParser.ExpressaoContext expressao : ctx.expressao()) { 
            saida.append(expressao.getText() + ", ");
        }
        saida.delete(saida.length()-2, saida.length());
        saida.append(");\n");
        return null;
    }

    // O método 'visitCmd_atribuicao' apens converte o modelo apresentado na Gramática. Caso seja uma string que esteja sendo
    //  atribuida a um registro, deve-se então usar uma chamada 'strcpy'.
    @Override 
    public Void visitCmd_atribuicao(GramaticaParser.Cmd_atribuicaoContext ctx) {
        // Se estamos diante de um ponteiro...
        if (ctx.pointer != null) {
            saida.append("*");
        }
        // Se estamos diante de um registro...(Se a lista for maior que 1, temos o ponto, indicando struct)
        if (ctx.identificador().IDENT().size() > 1) {
            // Se for um char, devemos usar então strcpy.
            if (tabela.verificar(ctx.identificador().getText()) == TiposGramatica.LITERAL) {
                saida.append("strcpy(" + ctx.identificador().getText() + "," + ctx.expressao().getText() + ");\n");
            // Caso contrário, atribuição simples funciona.
            } else {
                saida.append(ctx.identificador().getText() + " = ");
                saida.append(ctx.expressao().getText() + ";\n");
            }
        } else {
            saida.append(ctx.identificador().getText() + " = ");
            saida.append(ctx.expressao().getText() + ";\n");
        }
        return null;
    }

    // O comando 'visitCmd_se' visita a expressão, com o intuito de ajustar diferenças de operadores entre as 2 linguagens. Usa-se da recursão
    //  para visitar os demais comandos.
    @Override 
    public Void visitCmd_se(GramaticaParser.Cmd_seContext ctx) {
        saida.append("if ( ");
        visitExpressao(ctx.expressao());
        saida.append(" ) {\n");
        int comandos = 0;
        for (GramaticaParser.CmdContext comando : ctx.cmd()) {
            if (comandos > 0) {
                saida.append("} else {\n");
            }
            visitCmd(comando);
            comandos++;
        }
        saida.append("}\n");
        return null;
    }

    // Comando 'caso' para 'switch'.
    @Override 
    public Void visitCmd_caso(GramaticaParser.Cmd_casoContext ctx) {
        // Iniciando declaração do 'switch'
        saida.append("switch ( " + ctx.expressao_aritmetica().getText() + ") { \n");
        // Segundo a Gramatica, para cada 'item_selecao' (cada caso), imprimir em cada intervalo, as constantes do caso. Finalizado essa impressão,
        //  então partir para a impressão dos comandos, seguido do 'break;'. A tabulação '\t' ajuda a manter um código gerado legível.
        for (GramaticaParser.Item_selecaoContext selecao : ctx.selecao().item_selecao()) {
            for (GramaticaParser.Numero_intervaloContext intervalo : selecao.constantes().numero_intervalo()) {
                if (intervalo.NUM_INT().size() > 1) {
                    for (int i = Integer.parseInt(intervalo.NUM_INT(0).getText()); i <= Integer.parseInt(intervalo.NUM_INT(1).getText()); i++ ) {
                        saida.append("\tcase " + i + ":\n");
                    }
                } else {
                    saida.append("\tcase " + intervalo.NUM_INT(0).getText() + ":\n");
                }
            }
            for (GramaticaParser.CmdContext cmd : selecao.cmd()) {
                saida.append("\t\t");
                visitCmd(cmd);
                saida.append("\t\tbreak;\n");
            }
        }
        // caso haja um 'senão', então temos um 'default'. (Verificação feita pela presença de comandos - cmd - na expressao principal do 'caso').
        if (ctx.cmd() != null) {
            saida.append("\tdefault:\n\t\t");
            ctx.cmd().forEach(cmd -> visitCmd(cmd));
            saida.append("\t\tbreak;\n");
        }
        // Fechando as chaves
        saida.append("};\n");
        return null;
    }

    // Simples conversão do comando enquanto para o 'while', definido na Gramática.
    @Override 
    public Void visitCmd_enquanto(GramaticaParser.Cmd_enquantoContext ctx) {
        saida.append("while ( ");
        visitExpressao(ctx.expressao());
        saida.append(" ) {\n");
        ctx.cmd().forEach(cmd -> visitCmd(cmd));
        saida.append("}\n");
        return null;
    }

    // Comando referente a chamada para, definida na Gramática, convertida para 'for'.
    @Override 
    public Void visitCmd_para(GramaticaParser.Cmd_paraContext ctx) {
        saida.append("for ( " + ctx.IDENT().getText() + " = " + ctx.expressao_aritmetica(0).getText() + "; " + ctx.IDENT().getText() + " <= " + 
            ctx.expressao_aritmetica(1).getText() + "; " + ctx.IDENT().getText() + "++ ) {\n");
        ctx.cmd().forEach(cmd -> visitCmd(cmd));
        saida.append("}\n");

        return null;
    }

    // Conversão do comando 'faca' ao 'do...while'.
    @Override 
    public Void visitCmd_faca(GramaticaParser.Cmd_facaContext ctx) {
        saida.append("do {\n");
        ctx.cmd().forEach(cmd -> visitCmd(cmd));
        saida.append("} while ( "); 
        visitExpressao(ctx.expressao());
        saida.append(" );\n");
        return null;
    }

    // Comando referente a chamadas de função ou procedimentos como comandos. A verificação semântica já foi feita, então apenas traduzir os parâmetros.
    @Override 
    public Void visitCmd_chamada(GramaticaParser.Cmd_chamadaContext ctx) {
        saida.append(ctx.IDENT().getText() + "(");
        for (GramaticaParser.ExpressaoContext exp : ctx.expressao()) {
            saida.append(exp.getText() + ", ");
            saida.delete(saida.length()-2, saida.length());
        }
        saida.append(");\n");
        return null;
    }

    // Conversão do comando 'retorne' ao return.
    @Override 
    public Void visitCmd_retorne(GramaticaParser.Cmd_retorneContext ctx) {
        saida.append("return ");
        visitExpressao(ctx.expressao());
        saida.append(";\n");
        return null;
    }

    /* ---- */
    /* Analisador Semantico Utils Override */
    /* ---- */

    // Os metódos a seguir visitam as expressões, ajustando elementos que se diferem na linguagem C.

    // Caso apresente 'ou', convertê-lo em '||'.
    @Override 
    public Void visitExpressao(GramaticaParser.ExpressaoContext ctx) {
        visitTermo_logico(ctx.termo_logico(0));
        for (int i = 0; i < ctx.op_logico_1().size(); i++) {
            saida.append(" || ");
            visitTermo_logico(ctx.termo_logico(i + 1));
        }
        return null;
    }

    // Caso apresente 'e', convertê-lo em '&&'.
    @Override 
    public Void visitTermo_logico(GramaticaParser.Termo_logicoContext ctx) {
        visitFator_logico(ctx.fator_logico(0));
        for (int i = 0; i < ctx.op_logico_2().size(); i++) {
            saida.append(" && ");
            visitFator_logico(ctx.fator_logico(i + 1));
        }
        return null;
    }

    // Caso apresente a negacao 'nao', convertê-la em '!'.
    @Override 
    public Void visitFator_logico(GramaticaParser.Fator_logicoContext ctx) {
        if (ctx.nao != null) {
            saida.append("!");
        }
        visitParcela_logica(ctx.parcela_logica());
        return null;
    }

    // Caso apresente os lógicos 'verdadeiro' ou 'falso', convertê-los respectivamente em 'true' ou 'false'.
    @Override 
    public Void visitParcela_logica(GramaticaParser.Parcela_logicaContext ctx) {
        if (ctx.verdadeiro != null) {
            saida.append("true");
        } else if (ctx.falso != null) {
            saida.append("false");
        }
        visitExp_relacional(ctx.exp_relacional());
        return null;
    }

    // Caso apresente os relacionais '=' ou '<>', convertê-los respectivamente em '==' ou '!='.
    @Override 
    public Void visitExp_relacional(GramaticaParser.Exp_relacionalContext ctx) {
        String aux;
        visitExpressao_aritmetica(ctx.expr_arit_1);
        if (ctx.op_relacional() != null) {
            if (ctx.op_relacional().getText().equals("=")) {
                System.out.println("Achei um op_relacional");
                aux = "==";
            } else if (ctx.op_relacional().getText().equals("<>")) {
                aux = "!=";
            } else {
                aux = ctx.op_relacional().getText();
            }
            saida.append(" " + aux + " ");
            visitExpressao_aritmetica(ctx.expr_arit_2);
        }
        return null;
    }

    // Caso a expressão aritmética esteja entre parênteses, destrinchá-la, para analisar com melhor precisão.
    @Override 
    public Void visitExpressao_aritmetica(GramaticaParser.Expressao_aritmeticaContext ctx) {
        if (ctx.termo(0).fator(0).parcela(0).parcela_unario().expr != null) {
            saida.append('(');
            visitExpressao(ctx.termo(0).fator(0).parcela(0).parcela_unario().expr);
            saida.append(')');
        } else {
            saida.append(ctx.getText());
        }
        return null;
    }
    
}