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
        try(PrintWriter pw = new PrintWriter(new File(args[1]))) {
            CharStream cs = CharStreams.fromFileName(args[0]);
            GramaticaLexer lex = new GramaticaLexer(cs);

            CommonTokenStream tokens = new CommonTokenStream(lex);
            GramaticaParser parser = new GramaticaParser(tokens);
            
            // Descomentar para depurar o Léxico
            Token t = null;
            while( (t = lex.nextToken()).getType() != Token.EOF) {
                System.out.println("<" + GramaticaLexer.VOCABULARY.getDisplayName(t.getType()) + "," + t.getText() + ">");
            }
        
            // Registrar o error lister personalizado aqui
            parser.removeErrorListeners();
            MyCustomErrorListener mcel = new MyCustomErrorListener(pw);
            parser.addErrorListener(mcel);
        
            parser.programa();
        }
    }
}
