package br.ufscar.dc.compiladores.analisadorSemantico;

import br.ufscar.dc.compiladores.analisadorSemantico.TabelaDeSimbolos.TiposGramatica;
import br.ufscar.dc.compiladores.analisadorSemantico.Escopos;
import org.antlr.v4.runtime.Token;
import java.util.ArrayList;
import java.util.HashMap;

public class GramaticaSemantico extends GramaticaBaseVisitor<Void> {

    // Criando os escopos - o escopo já inicializado fará parte das variáveis globais
    Escopos escoposAninhados = new Escopos();
    // Hashmap para guardar protótipos de registros
    HashMap<String, ArrayList<String>> registros = new HashMap<>();
    // Hashmap para guardar protótipos de novos tipos
    HashMap<String, String> novoTipoUnario = new HashMap<>();
    // Hashmap para guardar funcoes/procedimentos
    static HashMap<String, ArrayList<TiposGramatica>> funcoes_e_procedimentos = new HashMap<>();

    @Override
    public Void visitPrograma(GramaticaParser.ProgramaContext ctx) {
        // Novo escopo, indica as variáveis que pertencem a função main.
        escoposAninhados.criarNovoEscopo();
        for (GramaticaParser.CmdContext comando : ctx.corpo().cmd()) {
            if (comando.cmd_retorne() != null) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(comando.cmd_retorne().getStart(), "comando retorne nao permitido nesse escopo");
            }
        }
        
        return super.visitPrograma(ctx);
    }

    // Simples método que obtém o TipoGramatica a partir de uma string.
    public TiposGramatica getTipoVariavel(String tipo) {
        if (registros.containsKey(tipo)) {
            return TiposGramatica.REGISTRO;
        }

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

    // Método que adiciona a variável passada por parámetro a tabela. É capaz de lançar erros caso detecte alguma anomalia, tanto no tipo da
    //  variável, quanto no nome (se já declarada ou não). Caso o tipo seja um problema, adiona-o mesmo assim, pois foi declarada de certa
    //  forma, apenas seu tipo foi escrito errado.
    public void adicionaVariavelTabela(String nome, String strTipo, Token tokenNome, Token tokenTipo) {
        // Obtendo o escopo atual
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipo = getTipoVariavel(strTipo);
        if (tipo == TiposGramatica.INVALIDO) {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tokenTipo, "tipo " + strTipo + " nao declarado");
        }
        if ( !tabela.existe(nome) ) {
            tabela.adicionar(nome, tipo);
            System.out.println("NOVA VARIÁVEL " + nome + " DE TIPO " + tipo);
        } else {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tokenNome, "identificador " + nome + " ja declarado anteriormente");
        }
    }

    // Método que trata os diferentes tipos de declaração.
    @Override
    public Void visitDeclaracao_local(GramaticaParser.Declaracao_localContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();

        String strTipoVar = null;
        
        // Primeiro caso: 'declare' variavel
        if (ctx.variavel() != null) {
            // Caso o tipo seja 'registro', adicionamos na tabela de símbolos uma variável do tipo registro, indicando que há um registro declarado. A seguir, 
            //  adicionamos também as variáveis do registro concatenada por um ponto ao nome do registro, para compará-las quando necessário.
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
                // Caso elá seja um tipo de registro já declarado anteriormente, obtemos por meio de uma estrutura Hash 'registros', uma Lista das variáveis que 
                //  pertecem à aquele registro. Em seguida, o mesmo processo de adição que anteriormente.
                if (registros.containsKey(strTipoVar)) {
                    ArrayList<String> variaveis_registro = registros.get(strTipoVar);
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        if (tabela.existe(ident.getText()) || registros.containsKey(ident.getText())) {
                            AnalisadorSemanticoUtils.adicionarErroSemantico(ident.getStart(), "identificador " + ident.getText() + " ja declarado anteriormente");
                        } else {
                            adicionaVariavelTabela(ident.getText(), "registro", ident.getStart(), ctx.variavel().tipo().getStart());
                            for (int i = 0; i < variaveis_registro.size(); i = i + 2) {
                                adicionaVariavelTabela(ident.getText() + '.' + variaveis_registro.get(i), variaveis_registro.get(i+1), ident.getStart(), ctx.variavel().tipo().getStart());
                            }
                        }
                    }
                // Caso o tipo dessa variável seja um novo tipo unário definido pelo usuário.
                } else if (novoTipoUnario.containsKey(strTipoVar)) {
                    strTipoVar = novoTipoUnario.get(strTipoVar);
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        adicionaVariavelTabela(ident.getText(), strTipoVar, ident.getStart(), ctx.variavel().tipo().getStart());
                    }
                // Se uma variável padrão (nomes: tipo), apenas adicioná-las a tabela.
                } else {
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        if (funcoes_e_procedimentos.containsKey(ident.getText())) {
                            AnalisadorSemanticoUtils.adicionarErroSemantico(ident.getStart(), "identificador " + ident.getText() + " ja declarado anteriormente");
                        } else {
                            adicionaVariavelTabela(ident.getText(), strTipoVar, ident.getStart(), ctx.variavel().tipo().getStart());
                        }
                    }
                }
            }

        // Segundo caso: 'constante' IDENT ':' tipo_basico '=' valor_constante
        } else if (ctx.tipo_basico() != null) {
            strTipoVar = ctx.tipo_basico().getText();
            adicionaVariavelTabela(ctx.IDENT().getText(), strTipoVar, ctx.IDENT().getSymbol(), ctx.IDENT().getSymbol());

        // Terceiro caso: 'tipo' IDENT ':' tipo
        } else {
            // Caso esteja declarando um novo tipo 'registro', adicioná-la a estrutura Hash global de protótipos de tipos de registro - 'registros.
            //  Não é necessário adicioná-los a tabela, pois é um novo tipo, a verificação de seu nome será feita ao declarar uma variável.
            if (ctx.tipo().registro() != null) {
                ArrayList<String> variaveis_registro = new ArrayList<String>();
                for (GramaticaParser.VariavelContext vars : ctx.tipo().registro().variavel()) {
                    strTipoVar = vars.tipo().getText();
                    for (GramaticaParser.IdentificadorContext ident_registro : vars.identificador()) {
                        variaveis_registro.add(ident_registro.getText());
                        variaveis_registro.add(vars.tipo().getText());
                    }
                }
                registros.put(ctx.IDENT().getText(), variaveis_registro);
            // Caso seja um outro tipo, adicioná-la aqui.
            } else {
                strTipoVar = ctx.tipo().getText();
                novoTipoUnario.put(ctx.IDENT().getText(), strTipoVar);
            }
        }

        return super.visitDeclaracao_local(ctx);
    }

    // O comando de atribuição deve ser dividido em 2 análises. A análise do tipo da variável a se atribuir um valor e o tipo resultante da expressão.
    //  Ambas devem possuir o mesmo tipo para que a atribuição ocorra (com excessão dos valores INTEIRO e REAL, definidos na documentação). Essa veri-
    //  ficação fica a cargo da chamada 'verificarTipo', enquanto a comparação entre esses é responsábilidade do método a seguir.
    @Override
    public Void visitCmd_atribuicao(GramaticaParser.Cmd_atribuicaoContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        String varAtribuicao = ctx.identificador().getText();

        System.out.println(varAtribuicao + "(Tipo: " + AnalisadorSemanticoUtils.verificarTipo(tabela, varAtribuicao) + ") <- " + tipoExpressao);

        // Retorno da expressão não é inválido!
        if (tipoExpressao != TiposGramatica.INVALIDO) {
            // Variável de atribuição existe?
            if (!tabela.existe(varAtribuicao)) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.identificador().getStart(), "identificador " + varAtribuicao + " nao declarado");
            // Caso exista, obter o tipo dessa variável e compará-la com o retorno da expressão. Se divergentes, é permitido apenas que estejam en-
            //  tre INTEIRO e REAL, caso contrário, lançar erro.
            } else {
                TiposGramatica tipoVarAtribuicao = AnalisadorSemanticoUtils.verificarTipo(tabela, varAtribuicao);
                if (tipoVarAtribuicao != tipoExpressao) {
                    if ( !(((tipoVarAtribuicao == TiposGramatica.REAL) || (tipoVarAtribuicao == TiposGramatica.INTEIRO)) & ((tipoExpressao == TiposGramatica.INTEIRO) || (tipoExpressao == TiposGramatica.REAL))) ) {
                        // Se ponteiro, adicionar o indicador na mensagem do erro.
                        if (ctx.pointer != null) {
                            varAtribuicao = "^" + varAtribuicao;
                        }
                        AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.identificador().getStart(), "atribuicao nao compativel para " + varAtribuicao);
                    }
                }
            }
        // Retorno da expressão é inválido.
        } else {
            AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.identificador().getStart(), "atribuicao nao compativel para " + varAtribuicao);
        }
        return super.visitCmd_atribuicao(ctx);
    }

    // O comando leia a seguir varre a lista dos identificadores no comando leia. A cada identificador, procura verificar a existência do elemento
    //  na tabela de símbolos. Caso não esteja disponível, a variável não foi declarada, sendo necessário reportar o erro.
    @Override
    public Void visitCmd_leia(GramaticaParser.Cmd_leiaContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao;
        
        for (GramaticaParser.IdentificadorContext ident : ctx.identificador()) {
            System.out.println("OLHA SO: " + ident.getText());
            if (!tabela.existe(ident.getText())) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(ident.getStart(), "identificador " + ident.getText() + " nao declarado");
            }
        }

        return super.visitCmd_leia(ctx);
    }

    // O comando escreva a seguir percorre a lista de expressões presentes no comando escreva. A cada expressão, invoca 'verificarTipo' para verificar
    //  o tipo de retorno das expressões. Erros como não declaração de variáveis ou incompatibilidade de parâmetros (em caso de funções e procedimentos)
    //  estão inclusos na implementação das chamadas 'verificarTipo'.
    @Override
    public Void visitCmd_escreva(GramaticaParser.Cmd_escrevaContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao;

        for (GramaticaParser.ExpressaoContext expressao : ctx.expressao()) {
            System.out.println("OLHA SO: " + expressao.getText());
            tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, expressao);
        }

        return super.visitCmd_escreva(ctx);
    }

    // O comando 'se' a seguir apenas implementa a verificação da expressão 'master' do comando if. Outros comandos chamados serão tratados pelos outros métodos.
    //  Erros referentes a variáveis ou incompatibilidade do retorno das expressões (em caso de relacionais) serão tratados com a chamada 'verificarTipo'.
    @Override
    public Void visitCmd_se(GramaticaParser.Cmd_seContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());

        return super.visitCmd_se(ctx);
    }

    // Assim como no comando 'if', o método para tratar o comando 'enquanto' deve apenas se preocupar com a expressão da condição. Logo, a idéia da implementação
    //  é idêntica a implementada no comando 'if', com as alterações necessárias.
    @Override
    public Void visitCmd_enquanto(GramaticaParser.Cmd_enquantoContext ctx) { 
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());

        return super.visitCmd_enquanto(ctx);
    }

    // Quando deparado com uma declaração global, há 2 possibilidades: função ou procedimento. A função seria responsável por retornar algo, enquanto o procedi-
    // mento não. Logo, em ambos temos a presença de parâmetros. Esses devem ser declarados em um novo escopo, assim como as próprias declarações presentes no 
    //  método. Para isso, utiliza-se uma estrutura de Lista (variaveis_do_parametro) que durante a leitura dos parâmetros, armazenará os tipos das variáveis de 
    //  forma sequencial nesta lista, enquanto os identificadores são inseridos na tabela do novo escopo. Finalizada essa leitura, essa Lista é adicionada a uma 
    //  estrutura Hash global, que terá como chave o nome da função. Assim, quando uma expressão em 'verificarTipo' se deparar com uma chamada de função, usará 
    //  essa lista para comparar os tipos correspondentes das variáveis.
    @Override 
    public Void visitDeclaracao_global(GramaticaParser.Declaracao_globalContext ctx) { 
        escoposAninhados.criarNovoEscopo();
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        ArrayList<TiposGramatica> variaveis_do_parametro = new ArrayList<TiposGramatica>();

        if (ctx.func != null) {
            // Na função, adicionando as variáveis ao escopo atual.
            for (GramaticaParser.ParametroContext parametro : ctx.parametros().parametro()) {
                // É um tipo básico!
                if (parametro.tipo_estendido().tipo_basico_ident().tipo_basico() != null) {
                    for (GramaticaParser.IdentificadorContext ident : parametro.identificador()) {
                        adicionaVariavelTabela(ident.getText(), parametro.tipo_estendido().getText(), ident.getStart(), parametro.tipo_estendido().getStart());
                        variaveis_do_parametro.add(getTipoVariavel(parametro.tipo_estendido().getText()));
                    }
                // É um registro! Verificar existência e então obter a lista das váriaveis do tipo registro declarado. Após essa etapa, adicionar aos tipos do
                //  parâmetro da função o tipo registro. Logo após, adicionar a tabela de símbolos do novo escopo as variáveis da estrutura registro.
                } else {
                    if (registros.containsKey(parametro.tipo_estendido().tipo_basico_ident().IDENT().getText())) {
                        ArrayList<String> variaveis_registro = registros.get(parametro.tipo_estendido().tipo_basico_ident().IDENT().getText());
                        for (GramaticaParser.IdentificadorContext ident : parametro.identificador()) {
                            variaveis_do_parametro.add(getTipoVariavel(parametro.tipo_estendido().tipo_basico_ident().IDENT().getText()));
                            for (int i = 0; i < variaveis_registro.size(); i = i + 2) {
                                adicionaVariavelTabela(ident.getText() + '.' + variaveis_registro.get(i), variaveis_registro.get(i+1), ident.getStart(), ident.getStart());
                            }
                        }
                    } else {
                        AnalisadorSemanticoUtils.adicionarErroSemantico(parametro.getStart(), "tipo não declarado");
                    }
                }
            }
            // Adicionando o tipo de retorno da função!
            variaveis_do_parametro.add(getTipoVariavel(ctx.tipo_estendido().getText()));
            funcoes_e_procedimentos.put(ctx.IDENT().getText(), variaveis_do_parametro);

        } else {
            // No procedimento, adicionando as variáveis ao escopo atual.
            for (GramaticaParser.ParametroContext parametro : ctx.parametros().parametro()) {
                // É um tipo básico!
                if (parametro.tipo_estendido().tipo_basico_ident().tipo_basico() != null) {
                    for (GramaticaParser.IdentificadorContext ident : parametro.identificador()) {
                        adicionaVariavelTabela(ident.getText(), parametro.tipo_estendido().getText(), ident.getStart(), parametro.tipo_estendido().getStart());
                        variaveis_do_parametro.add(getTipoVariavel(parametro.tipo_estendido().getText()));
                    }
                // É um registro! Verificar existência e então obter a lista das váriaveis do tipo registro declarado. Após essa etapa, adicionar aos tipos do
                //  parâmetro da função o tipo registro. Logo após, adicionar a tabela de símbolos do novo escopo as variáveis da estrutura registro.
                } else {
                    if (registros.containsKey(parametro.tipo_estendido().tipo_basico_ident().IDENT().getText())) {
                        ArrayList<String> variaveis_registro = registros.get(parametro.tipo_estendido().tipo_basico_ident().IDENT().getText());
                        for (GramaticaParser.IdentificadorContext ident : parametro.identificador()) {
                            variaveis_do_parametro.add(getTipoVariavel(parametro.tipo_estendido().tipo_basico_ident().IDENT().getText()));
                            for (int i = 0; i < variaveis_registro.size(); i = i + 2) {
                                adicionaVariavelTabela(ident.getText() + '.' + variaveis_registro.get(i), variaveis_registro.get(i+1), ident.getStart(), ident.getStart());
                            }
                        }
                    } else {
                        AnalisadorSemanticoUtils.adicionarErroSemantico(parametro.getStart(), "tipo não declarado");
                    }
                }
            }
            for (GramaticaParser.CmdContext comando : ctx.cmd()) {
                if (comando.cmd_retorne() != null) {
                    System.out.println("COMANDO RETORNE NÃO PERMITIDO");
                }
            }
            funcoes_e_procedimentos.put(ctx.IDENT().getText(), variaveis_do_parametro);
        }
        
        return super.visitDeclaracao_global(ctx);
    }
    
}