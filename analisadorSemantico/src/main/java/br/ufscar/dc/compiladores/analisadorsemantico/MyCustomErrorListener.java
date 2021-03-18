package br.ufscar.dc.compiladores.analisadorSemantico;

import java.io.PrintWriter;
import java.util.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class MyCustomErrorListener implements ANTLRErrorListener {
    PrintWriter pw;
    public MyCustomErrorListener(PrintWriter pw) {
        this.pw = pw;    
    }
    
    @Override
    public void	reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }
    
    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }

    @Override
    public void	syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        // Aqui vamos colocar o tratamento de erro customizado

        // Obtém o token do qual ocorreu a exceção.
        Token t = (Token) offendingSymbol;

        // Trata o erro referente ao EOF. Basicamente trata o '<EOF>' a 'EOF'.
        if (t.getText().equals("<EOF>")) {
            pw.println("Linha " + line + ": erro sintatico proximo a EOF");
            pw.println("Fim da compilacao");
        // Tratam os erros de qualquer outro token.
        } else {
            pw.println("Linha " + line + ": erro sintatico proximo a " + t.getText());
            pw.println("Fim da compilacao");
        } 
        // Envia o erro a main, interrompendo a execução.
        throw new RuntimeException();
    }
}
