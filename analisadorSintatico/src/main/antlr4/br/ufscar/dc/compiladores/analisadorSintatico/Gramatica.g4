grammar Gramatica;

// =================================
// LEXER
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

// ===================
// PARSER
// ===================

programa:
            declaracoes 'algoritmo' corpo 'fim_algoritmo' EOF
        ;

declaracoes:
            (decl_local_global)*
        ;

decl_local_global:
            declaracao_local 
        |   declaracao_global
        ;

declaracao_local:
            'declare' variavel
        |   'constante' IDENT ':' tipo_basico '=' valor_constante
        |   'tipo' IDENT ':' tipo
        ;

variavel:
            identificador (',' identificador)* ':' tipo
        ;

identificador:
            IDENT ('.' IDENT)* dimensao
        ;

dimensao:
            ('[' expressao_aritmetica ']')*
        ;

tipo:
            registro
        |   tipo_estendido
        ;

tipo_basico:
            'literal'
        |   'inteiro'
        |   'real'
        |   'logico'
        ;

tipo_basico_ident:
            tipo_basico
        |   IDENT
        ;

tipo_estendido:
            '^'? tipo_basico_ident
        ;

valor_constante:
            CADEIA
        |   NUM_INT
        |   NUM_REAL
        |   'verdadeiro'
        |   'falso'
        ;

registro:
            'registro' (variavel)* 'fim_registro'
        ;

declaracao_global:
            'procedimento' IDENT '(' parametros? ')' (declaracao_local)* (cmd)* 'fim_procedimento'
        |   'funcao' IDENT '(' parametros? ')' ':' tipo_estendido (declaracao_local)* (cmd)* 'fim_funcao'
        ;

parametro:
            'var'? identificador (',' identificador)* ':' tipo_estendido
        ;

parametros:
            parametro (',' parametro)*
        ;

corpo:
            (declaracao_local)* (cmd)*
        ;

cmd:
            cmd_leia
        |   cmd_escreva
        |   cmd_se
        |   cmd_caso
        |   cmd_para
        |   cmd_enquanto    
        |   cmd_faca
        |   cmd_atribuicao
        |   cmd_chamada
        |   cmd_retorne
        ;

cmd_leia:
            'leia' '(' '^'? identificador (',' '^'? identificador)* ')'
        ;

cmd_escreva:
            'escreva' '(' expressao (',' expressao)* ')'
        ;

cmd_se:
            'se' expressao 'entao' (cmd)* ('senao' (cmd)*)? 'fim_se'
        ;

cmd_caso:
            'caso' expressao_aritmetica 'seja' selecao ('senao' (cmd)*)? 'fim_caso'
        ;

cmd_para:
            'para' IDENT '<-' expressao_aritmetica 'ate' expressao_aritmetica 'faca' (cmd)* 'fim_para'
        ;

cmd_enquanto:
            'enquanto' expressao 'faca' (cmd)* 'fim_enquanto'
        ;

cmd_faca:
            'faca' (cmd)* 'ate' expressao
        ;

cmd_atribuicao:
            '^'? identificador '<-' expressao
        ;

cmd_chamada:
            IDENT '(' expressao (',' expressao)* ')'
        ;

cmd_retorne:
            'retorne' expressao
        ;

selecao:
            (item_selecao)*
        ;

item_selecao:
            constantes ':' (cmd)*
        ;

constantes:
            numero_intervalo (',' numero_intervalo)*
        ;

numero_intervalo:
            (op_unario)? NUM_INT ('..' (op_unario)? NUM_INT)?
        ;

op_unario:
            '-'
        ;

expressao_aritmetica:
            termo (OP1 termo)*
        ;

termo:
            fator (OP2 fator)*
        ;

fator:
            parcela (OP3 parcela)*
        ;

OP1:
            '+' 
        |   '-'
        ;

OP2:
            '*'
        |   '/'
        ;

OP3:
            '%'
        ;

parcela:
            (op_unario)? parcela_unario
        |   parcela_nao_unario
        ;

parcela_unario:
            '^'? identificador
        |   IDENT '(' expressao (',' expressao)* ')'
        |   NUM_INT
        |   NUM_REAL
        |   '(' expressao ')'
        ;

parcela_nao_unario:
            '&' identificador
        |   CADEIA
        ;

exp_relacional:
            expressao_aritmetica (op_relacional expressao_aritmetica)?
        ;
    
op_relacional:
            '='
        |   '<>'
        |   '>='
        |   '<='
        |   '>'
        |   '<'
        ;

expressao:
            termo_logico (op_logico_1 termo_logico)*
        ;

termo_logico:
            fator_logico (op_logico_2 fator_logico)*
        ;

fator_logico:
            'nao'? parcela_logica
        ;

parcela_logica:
            ( 'verdadeiro' | 'falso' )
        |   exp_relacional
        ;

op_logico_1:
            'ou'
        ;

op_logico_2:
            'e'
        ;