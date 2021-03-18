package br.ufscar.dc.compiladores.analisadorSemantico;

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
            GramaticaLexer lexer = new GramaticaLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            GramaticaParser parser = new GramaticaParser(tokens);
            GramaticaParser.ProgramaContext arvore = parser.programa();
            GramaticaSemantico as = new GramaticaSemantico();
            as.visitPrograma(arvore);
            AnalisadorSemanticoUtils.errosSemanticos.forEach((s) -> pw.println(s));
            pw.println("Fim da compilacao");
        }
    }

}
