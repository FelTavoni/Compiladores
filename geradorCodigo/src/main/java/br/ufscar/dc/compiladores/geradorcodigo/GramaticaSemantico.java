package br.ufscar.dc.compiladores.geradorCodigo;

import br.ufscar.dc.compiladores.geradorCodigo.TabelaDeSimbolos.TiposGramatica;
import br.ufscar.dc.compiladores.geradorCodigo.Escopos;
import org.antlr.v4.runtime.Token;
import java.util.ArrayList;
import java.util.HashMap;

public class GramaticaSemantico extends GramaticaBaseVisitor<Void> {

    // Criando os escopos - o escopo já inicializado fará parte das variáveis globais
    static Escopos escoposAninhados = new Escopos();
    // Hashmap para guardar protótipos de registros
    HashMap<String, ArrayList<String>> registros = new HashMap<>();
    // Hashmap para guardar protótipos de novos tipos
    HashMap<String, String> novoTipoUnario = new HashMap<>();
    // Hashmap para guardar funcoes/procedimentos
    static HashMap<String, ArrayList<TiposGramatica>> funcoes_e_procedimentos = new HashMap<>();

    @Override
    public Void visitCorpo(GramaticaParser.CorpoContext ctx) {
        // Criando o escopo da função principal
        escoposAninhados.criarNovoEscopo();
        for (GramaticaParser.CmdContext cmd : ctx.cmd())
        if (cmd.cmd_retorne() != null) {
            AnalisadorSemanticoUtils.adicionarErroSemantico(cmd.getStart(), "comando retorne nao permitido nesse escopo");
        }
        return super.visitCorpo(ctx);
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

    // Método que adiciona a variável passada por parâmetro a tabela. É capaz de lançar erros caso detecte alguma anomalia, tanto no tipo da
    //  variável, quanto no nome (se já declarada ou não). Caso detecte um erro, adiciona a variável mesmo assim, pois foi declarada de certa
    //  forma, apenas seu tipo foi escrito errado. A medida é util para futuras vereificações.
    public void adicionaVariavelTabela(String nome, String strTipo, Token tokenNome, Token tokenTipo) {
        // Obtendo o escopo atual.
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        // Obtendo o escopo global do programa (pegar o último elemento dado a lista ligada).
        TabelaDeSimbolos tabelaEscopoGlobal = escoposAninhados.percorrerEscoposAninhados().get(escoposAninhados.percorrerEscoposAninhados().size()-1);
        TiposGramatica tipo = getTipoVariavel(strTipo);
        if (tipo == TiposGramatica.INVALIDO) {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tokenTipo, "tipo " + strTipo + " nao declarado");
        }
        if ( !tabela.existe(nome) ) {
            // Verificando o escopo global!
            if (!tabelaEscopoGlobal.existe(nome)) {
                tabela.adicionar(nome, tipo);
            } else {
                AnalisadorSemanticoUtils.adicionarErroSemantico(tokenNome, "identificador " + nome + " ja declarado anteriormente");
            }
        } else {
            AnalisadorSemanticoUtils.adicionarErroSemantico(tokenNome, "identificador " + nome + " ja declarado anteriormente");
        }
    }

    // Método para verificar se a variável possui dimensão. Em caso afirmativo, exclui a parte de dimensão e a trata como uma variável comum. 
    //  Retorna o nome da variável recortada ou não.
    public String verificaDimensao(GramaticaParser.IdentificadorContext ident) {
        String nomeVar;
        if (ident.dimensao().expressao_aritmetica().size() > 0) {
            nomeVar = ident.id.getText();
            for (GramaticaParser.Expressao_aritmeticaContext expr : ident.dimensao().expressao_aritmetica()) {
                System.out.println(ident.getText() + " tem dimensao: " + ident.id.getText() + ", expr: " + expr.getText());
            }
        } else {
            nomeVar = ident.getText();
            System.out.println(ident.getText() + " não tem dimensao");
        }
        return nomeVar;
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
                String nome;
                if (registros.containsKey(strTipoVar)) {
                    ArrayList<String> variaveis_registro = registros.get(strTipoVar);
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        nome = verificaDimensao(ident);
                        if (tabela.existe(nome) || registros.containsKey(nome)) {
                            AnalisadorSemanticoUtils.adicionarErroSemantico(ident.getStart(), "identificador " + nome + " ja declarado anteriormente");
                        } else {
                            adicionaVariavelTabela(nome, "registro", ident.getStart(), ctx.variavel().tipo().getStart());
                            for (int i = 0; i < variaveis_registro.size(); i = i + 2) {
                                adicionaVariavelTabela(nome + '.' + variaveis_registro.get(i), variaveis_registro.get(i+1), ident.getStart(), ctx.variavel().tipo().getStart());
                            }
                        }
                    }
                // Caso o tipo dessa variável seja um novo tipo unário definido pelo usuário.
                } else if (novoTipoUnario.containsKey(strTipoVar)) {
                    strTipoVar = novoTipoUnario.get(strTipoVar);
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        adicionaVariavelTabela(ident.getText(), strTipoVar, ident.getStart(), ctx.variavel().tipo().getStart());
                    }
                // Se uma variável for padrão (nomes: tipo), apenas adicioná-las a tabela.
                } else {
                    for (GramaticaParser.IdentificadorContext ident : ctx.variavel().identificador()) {
                        String nomeVar = verificaDimensao(ident);
                        if (funcoes_e_procedimentos.containsKey(nomeVar)) {
                            AnalisadorSemanticoUtils.adicionarErroSemantico(ident.getStart(), "identificador " + nomeVar + " ja declarado anteriormente");
                        } else {
                            adicionaVariavelTabela(nomeVar, strTipoVar, ident.getStart(), ctx.variavel().tipo().getStart());
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
            // Caso esteja declarando um novo tipo 'registro', adicioná-la a estrutura Hash global de protótipos de tipos de registro - 'registros'.
            //  Não é necessário adicioná-los a tabela, pois é um novo tipo, a verificação de seu nome será feita ao declarar uma variável desse tipo.
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
            // Caso seja um outro tipo, não registro, adicioná-la aqui.
            } else {
                strTipoVar = ctx.tipo().getText();
                novoTipoUnario.put(ctx.IDENT().getText(), strTipoVar);
            }
        }

        return super.visitDeclaracao_local(ctx);
    }

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

    // O comando de atribuição deve ser dividido em 2 análises. A análise do tipo da variável a se atribuir um valor e o tipo resultante da expressão.
    //  Ambas devem possuir o mesmo tipo para que a atribuição ocorra (com excessão dos valores INTEIRO e REAL, definidos na documentação). Essa veri-
    //  ficação fica a cargo da chamada 'verificarTipo', enquanto a comparação entre esses é responsabilidade do método a seguir.
    @Override
    public Void visitCmd_atribuicao(GramaticaParser.Cmd_atribuicaoContext ctx) {
        // Obtenção do escopo atual, o tipo da expressão a atribuir e o nome da variável (removendo possíveis dimensões).
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        String varAtribuicao = verificaDimensao(ctx.identificador());

        // Caso o retorno não seja inválido, verificar o tipo da variável a receber o valor, assim como os tipos correspondentes.
        // Se a veriável existe, 
        if (tipoExpressao != TiposGramatica.INVALIDO) {
            // Verificação de existência da variável. Obtém-se então o tipo dessa variável e a compara com o retorno da expressão. Se divergentes, 
            //  é permitido apenas que estejam entre INTEIRO e REAL, caso contrário, lançar erro.
            if (!tabela.existe(varAtribuicao)) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.identificador().getStart(), "identificador " + ctx.identificador().getText() + " nao declarado");
            } else {
                TiposGramatica tipoVarAtribuicao = AnalisadorSemanticoUtils.verificarTipo(tabela, varAtribuicao);
                if (tipoVarAtribuicao != tipoExpressao) {
                    if ( !(((tipoVarAtribuicao == TiposGramatica.REAL) || (tipoVarAtribuicao == TiposGramatica.INTEIRO)) & ((tipoExpressao == TiposGramatica.INTEIRO) || (tipoExpressao == TiposGramatica.REAL))) ) {
                        // Se ponteiro, adicionar o indicador na mensagem do erro.
                        if (ctx.pointer != null) {
                            AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.identificador().getStart(), "atribuicao nao compativel para ^" + ctx.identificador().getText());
                        } else {
                            AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.identificador().getStart(), "atribuicao nao compativel para " + ctx.identificador().getText());
                        }
                    }
                }
            }
        // Retorno da expressão é inválido.
        } else {
            AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.identificador().getStart(), "atribuicao nao compativel para " + ctx.identificador().getText());
        }
        return super.visitCmd_atribuicao(ctx);
    }

    // O comando leia a seguir varre a lista dos identificadores no comando leia. A cada identificador, procura verificar a existência do elemento
    //  na tabela de símbolos. Caso não esteja disponível, a variável não foi declarada, sendo necessário lançar um erro.
    @Override
    public Void visitCmd_leia(GramaticaParser.Cmd_leiaContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao;
        String nomeVar;
        
        for (GramaticaParser.IdentificadorContext ident : ctx.identificador()) {
            nomeVar = verificaDimensao(ident);
            if (!tabela.existe(nomeVar)) {
                AnalisadorSemanticoUtils.adicionarErroSemantico(ident.getStart(), "identificador " + ident.getText() + " nao declarado");
            }
        }

        return super.visitCmd_leia(ctx);
    }

    // O comando escreva a seguir percorre a lista de expressões presentes no comando escreva. A cada expressão, invoca 'verificarTipo' para verificar
    //  o tipo de retorno das expressões. Erros como não declaração de variáveis ou incompatibilidade de parâmetros (em caso de funções e procedimentos)
    //  estão inclusos na implementação das chamadas 'verificarTipo' em 'AnalisadorSemanticoUtils'.
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

    // O comando 'se' a seguir apenas implementa a verificação da expressão do comando if. Outros comandos chamados serão tratados pelos outros métodos.
    //  Erros referentes a variáveis ou incompatibilidade do retorno das expressões (em caso de relacionais) serão tratados com a chamada 'verificarTipo'.
    @Override
    public Void visitCmd_se(GramaticaParser.Cmd_seContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());

        return super.visitCmd_se(ctx);
    }

    // Assim como no comando 'if', o método para tratar o comando da expressão do 'enquanto' deve apenas se preocupar com a expressão da condição. Logo, a 
    //  idéia da implementação é idêntica a implementada no comando 'if', com as alterações necessárias.
    @Override
    public Void visitCmd_enquanto(GramaticaParser.Cmd_enquantoContext ctx) { 
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao());

        return super.visitCmd_enquanto(ctx);
    }

    // Para o comando 'caso', verificar o tipo da expressao aritmetica da condição, e os comandos associados aos casos, por meio da recursão.
    @Override 
    public Void visitCmd_caso(GramaticaParser.Cmd_casoContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        TiposGramatica tipoExpressao = AnalisadorSemanticoUtils.verificarTipo(tabela, ctx.expressao_aritmetica());

        ctx.cmd().forEach(cmd -> visitCmd(cmd));

        return super.visitCmd_caso(ctx);
    }

    // Para o comando 'para', verificamos se o contador é uma variável declarada, assim como suas expressões aritméticas. 'verificarTipo' novamente se
    //  encarrega dessas verificações. Uma chamada recursiva ao comando é feita para verificá-los.
    @Override 
    public Void visitCmd_para(GramaticaParser.Cmd_paraContext ctx) {
        TabelaDeSimbolos tabela = escoposAninhados.obterEscopoAtual();
        if (!tabela.existe(ctx.IDENT().getText())) {
            AnalisadorSemanticoUtils.adicionarErroSemantico(ctx.IDENT().getSymbol(), "identificador " + ctx.IDENT().getText() + " nao declarado");
        }
        ctx.expressao_aritmetica().forEach(expr_arit -> AnalisadorSemanticoUtils.verificarTipo(tabela, expr_arit));
        ctx.cmd().forEach(cmd -> visitCmd(cmd));

        return super.visitCmd_para(ctx);
    }

    // Quando deparado com uma declaração global, há 2 possibilidades: função ou procedimento. A função seria responsável por retornar algo, enquanto o procedi-
    // mento não. Logo, em ambos temos a presença de parâmetros. Esses devem ser declarados em um novo escopo, assim como as próprias declarações presentes no 
    //  método. Para isso, utiliza-se uma estrutura de Lista (variaveis_do_parametro) que durante a leitura dos parâmetros, armazenará os tipos das variáveis de 
    //  forma sequencial nesta lista, enquanto os identificadores são inseridos na tabela do novo escopo. Finalizada essa leitura, essa Lista é adicionada a uma 
    //  estrutura Hash global, que terá como chave o nome da função. Assim, quando uma expressão em 'verificarTipo' se deparar com uma chamada de função, usará 
    //  essa lista para comparar os tipos correspondentes das variáveis.
    @Override 
    public Void visitDeclaracao_global(GramaticaParser.Declaracao_globalContext ctx) { 
        // Criação de um novo escopo, atualização da tabela com o novo escopo e declaração de uma lista para armazenar os tipos das variáveis do parâmetro.
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
            // Em caso de procedimento, adicionando as variáveis ao escopo atual.
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
                    AnalisadorSemanticoUtils.adicionarErroSemantico(comando.getStart(), "comando retorne nao permitido nesse escopo");
                }
            }
            funcoes_e_procedimentos.put(ctx.IDENT().getText(), variaveis_do_parametro);
        }
        
        return super.visitDeclaracao_global(ctx);
    }
    
}