package br.ufscar.dc.compiladores.analisadorLexico;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {

    public static void main(String[] args) throws IOException {   
        // args[0] é o primeiro argumento da linha de comando.
        CharStream cs = CharStreams.fromFileName(args[0]);
        Gramatica lex = new Gramatica(cs);

        Token t = null;
        // Definição de uma string para obter o noke do token gerado (devido ao enum em 'Gramatica.tokens').
        String tokenName;
        // Abrindo o arquivo para escrita segundo o parâmetro passado.
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(args[1]));
        
        // Enquanto há letras/tokens a serem lidos (não atingiu o fim do arquivo), continuar lendo...
        while ( (t = lex.nextToken()).getType() != Token.EOF ) {
            // Obtém o nome do token lido para comparação e impressão no arquivo.
            tokenName = Gramatica.VOCABULARY.getSymbolicName(t.getType());
            // Em caso de erro, interrompe o programa nos casos abaixo (CADEIA_ERRADA, COMENTARIO_ERRADO ou ERRO(SÍMBOLO_ERRADO))
            if (tokenName.equals("ERRO")) {
                buffWrite.append("Linha " + t.getLine() + ": " + t.getText() +" - simbolo nao identificado" + "\n");
                break;
            }
            else if (tokenName.equals("CADEIA_ERRADA")) {
                buffWrite.append("Linha " + t.getLine() + ": cadeia literal nao fechada\n");
                break;
            }
            else if (tokenName.equals("COMENTARIO_ERRADO")) {
                buffWrite.append("Linha " + t.getLine() + ": comentario nao fechado\n");
                break;
            }
            // Caso a palavra lida seja algumas a seguir, o programa a imprime o token da seguinte forma (requisito do trabalho):
            // <'lido', 'lido'>
            else if ( tokenName.equals("PALAVRAS_CHAVE")  ||
                      tokenName.equals("REP_COND")        ||
                      tokenName.equals("TIPO_DECLA")      ||
                      tokenName.equals("OP_REL")          ||
                      tokenName.equals("OP_ARIT")         ||
                      tokenName.equals("OP_LOG")          ||
                      tokenName.equals("DELIM")           ||
                      tokenName.equals("ABRE")            ||
                      tokenName.equals("FECHA") ) {
                buffWrite.append("<'" + t.getText() + "','" + t.getText() + "'>\n");
            // Caso contrário, imprime o token da seguinte forma:
            // <'lido', tipo>
            } else {
                buffWrite.append("<'" + t.getText() + "'," + Gramatica.VOCABULARY.getDisplayName(t.getType()) + ">\n");
            }
        }
        
        buffWrite.close();
    }
}