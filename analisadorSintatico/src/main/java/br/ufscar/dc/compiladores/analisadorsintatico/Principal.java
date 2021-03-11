package br.ufscar.dc.compiladores.analisadorSintatico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import org.antlr.v4.runtime.ANTLRErrorListener; // cuidado para importar a versão 4
import org.antlr.v4.runtime.Token; // Vamos também precisar de Token

public class Principal {
    public static void main(String args[]) throws IOException {
        // Declarando o escritor do arquivo. Lançará erro caso haja falha na abertura
        try(PrintWriter pw = new PrintWriter(new File(args[1]))) {

            // Inicialização do CharStream, o Lexer e os Tokens
            CharStream cs = CharStreams.fromFileName(args[0]);
            GramaticaLexer lex = new GramaticaLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lex);

            // Inicialização do Parser
            GramaticaParser parser = new GramaticaParser(tokens);

            // Registrar o error lister personalizado aqui, e apagar o padrão do ANTLR
            parser.removeErrorListeners();
            MyCustomErrorListener mcel = new MyCustomErrorListener(pw);
            parser.addErrorListener(mcel);

            // Irá executar o lexer para localizar erros léxicos, como cadeias e comentários errados
            if (executaLexer(args[0], pw)) {
                try {
                    // Caso tudo certo com a execução do lexer, então executa o parser.
                    parser.programa();
                } catch (Exception e) {
                    // Caso o parser identifique alguma irregularidade, será tratada em 'MyCustomErrorListener.java', e o erro lançado será captado aqui.
                    System.out.println(""); // Mero print para não deixar vazio. Pode-se imprimir o erro no terminal se desejar.
                }
            }
        }
    }

    // Função destinada a execução do Lexer.
    // Basicamente o mesmo código utilizado no analisadorLexico postado anteriormente.
    // Recebe o arquivo de entrada passado como parâmetro, assim como o objeto que escreverá no arquivo de saída.
    public static boolean executaLexer(String arquivo, PrintWriter pw) throws IOException {

        // Redeclaração dos objetos para evitar conflitos com o do parser
        CharStream cs = CharStreams.fromFileName(arquivo);
        GramaticaLexer lex = new GramaticaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lex);

        Token t = null;
        // Definição de uma string para obter o noke do token gerado (devido ao enum em 'Gramatica.tokens').
        String tokenName;
        
        // Enquanto há letras/tokens a serem lidos (não atingiu o fim do arquivo), continuar lendo...
        while ( (t = lex.nextToken()).getType() != Token.EOF ) {
            // Obtém o nome do token lido para comparação e impressão no arquivo.
            tokenName = GramaticaLexer.VOCABULARY.getSymbolicName(t.getType());
            // Em caso de erro, indicará irregularidade, gravando essa no arquivo, retornando logo em seguida um valor 'false'.
            if (tokenName == null) {
                continue;
            }
            else if (tokenName.equals("ERRO")) {
                pw.println("Linha " + t.getLine() + ": " + t.getText() +" - simbolo nao identificado");
                pw.println("Fim da compilacao");
                return false;
            }
            else if (tokenName.equals("CADEIA_ERRADA")) {
                pw.println("Linha " + t.getLine() + ": cadeia literal nao fechada");
                pw.println("Fim da compilacao");
                return false;
            }
            else if (tokenName.equals("COMENTARIO_ERRADO")) {
                pw.println("Linha " + t.getLine() + ": comentario nao fechado");
                pw.println("Fim da compilacao");
                return false;
            }
        }
        return true;
    }
}
