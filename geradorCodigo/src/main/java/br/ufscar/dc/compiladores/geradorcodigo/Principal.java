package br.ufscar.dc.compiladores.geradorCodigo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import br.ufscar.dc.compiladores.geradorCodigo.GramaticaParser.ProgramaContext;

import org.antlr.v4.runtime.ANTLRErrorListener; // cuidado para importar a versão 4
import org.antlr.v4.runtime.Token; // Vamos também precisar de Token

public class Principal {
    public static void main(String args[]) throws IOException {
        CharStream cs = CharStreams.fromFileName(args[0]);
        GramaticaLexer lexer = new GramaticaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GramaticaParser parser = new GramaticaParser(tokens);
        GramaticaParser.ProgramaContext arvore = parser.programa();
        GramaticaSemantico as = new GramaticaSemantico();
        as.visitPrograma(arvore);
        AnalisadorSemanticoUtils.errosSemanticos.forEach((s) -> System.out.println(s));
        
        if(AnalisadorSemanticoUtils.errosSemanticos.isEmpty()) {
            Gerador agc = new Gerador();
            agc.visitPrograma(arvore);
            try(PrintWriter pw = new PrintWriter(args[1])) {
                pw.print(agc.saida.toString());
            }
        }
    }
}
