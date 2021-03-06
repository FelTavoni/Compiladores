lexer grammar Gramatica;

// =================================
// Regras do lexema
// =================================

// Palavras reservadas
PALAVRAS_CHAVE 
	:   'algoritmo' | 'declare' | 'leia' | 'escreva' | 'fim_algoritmo' | '..' | '^' | '&' | '.' | 'tipo' | 'procedimento' | 'fim_procedimento' | 'var' | 'funcao' | 'retorne' | 'fim_funcao' | 'constante' | 'verdadeiro' | 'falso' | '<-'
	; 

// Estrutura de repetições e condicionais 
REP_COND
        : 'se' | 'entao' | 'senao' | 'fim_se' | 'caso' | 'seja' | 'fim_caso' | 'para' | 'ate' | 'faca' | 'fim_para' | 'enquanto' | 'fim_enquanto'
        ;

// Tipo das variáveis a serem declaradas
TIPO_DECLA
        :   'literal' | 'inteiro' | 'real' | 'logico' | 'registro' | 'fim_registro'
        ;

// Operadores lógicos
OP_LOG
        :   'e' | 'ou' | 'nao'
        ;

// Representação de números inteiros
NUM_INT
        :   ('0'..'9')+
	;

// Representação de números reais
NUM_REAL	
        :   ('0'..'9')+ ('.' ('0'..'9')+)?
	;

// Identificadores/variáveis
IDENT 
        :   ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
	;

// Cadeia de caracteres errada (não fechada)
CADEIA_ERRADA
        :   '"' (~('\n' | '\r' | '"'))* ('\n' | '\r')
	;
// Cadeia de caracteres correta
CADEIA
        :   '"' (~('\n' | '\r' | '"'))* '"'
	;

// Comentário errado (não fechado)
COMENTARIO_ERRADO
        :   '{' (~('\n'|'\r'|'}'))* ('\n'|'\r')
        ;

// Comentário correto
COMENTARIO
        :   '{' (~('\n'|'\r'|'}'))* '}' {skip();}
        ;

// Whitespace - espaço em branco
WS
        :   ( ' ' | '\t' | '\r' | '\n' ) {skip();}
        ;

// Operadores relacionais
OP_REL
        :   '>' | '>=' | '<' | '<=' | '<>' | '='
	;

// Operadores aritméticos
OP_ARIT	
        :   '+' | '-' | '*' | '/' | '%'
	;

// Delimitadores
DELIM	
        :   ':' | ','
	;

ABRE
        :   '(' | '['
	;

FECHA
        :   ')' | ']'
	;

ERRO
        :   .
        ;