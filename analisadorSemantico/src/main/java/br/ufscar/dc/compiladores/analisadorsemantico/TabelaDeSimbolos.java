package br.ufscar.dc.compiladores.analisadorSemantico;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    public enum TiposGramatica {
        LITERAL,
        INTEIRO,
        REAL,
        LOGICO,
        INVALIDO
    }
    
    class EntradaTabelaDeSimbolos {
        String nome;
        TiposGramatica tipo;

        private EntradaTabelaDeSimbolos(String nome, TiposGramatica tipo) {
            this.nome = nome;
            this.tipo = tipo;
        }
    }
    
    private final Map<String, EntradaTabelaDeSimbolos> tabela;
    
    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }
    
    public void adicionar(String nome, TiposGramatica tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }
    
    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }
    
    public TiposGramatica verificar(String nome) {
        return tabela.get(nome).tipo;
    }
}
