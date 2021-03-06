# Trabalho 1 - Analisador Léxico

O trabalho 1 da disciplina de *Construção de Compiladores* consiste em implementar um analisador léxico para a linguagem LA (Linguagem Algorítmica) desenvolvida pelo prof. Jander, no âmbito do DC/UFSCar. O analisador léxico deve ler um programa-fonte e produzir uma lista de tokens identificados.

O analisador léxico deve ler um fluxo de caracteres e os agrupar em sequências significativas, os chamados *lexemas*. Para cada lexema, é produzido um token <'valor-do-atributo','nome-token'>

A seguir, seguem-se as instruções de compilação e execução do analisador léxico

## Preparação do ambiente

O analisador léxico foi construído em java, integrado com a ferramenta ANTLR, um reconhecedor de linguagens. Para utilizá-lo, é necessário que o usuário tenha instalado em seu ambiente de execução as seguintes ferramentas:

[MAVEN](https://maven.apache.org/) - *versão utilizada: 1.8.0_111*

[JAVA](https://www.java.com/pt-BR/) - *versão utilizada: 3.6.3*

*Vale lembrar que você deve adicionar estas ferramentas a variável $PATH!*

## Compilação do programa

Para compilar o programa, é necessário que você realize o download da pasta acima. Basta então adentrar o diretório e invocar o maven para realizar a construção do executável.

`mvn install`

Após a construção do programa, será gerado um executável, em que para executá-lo, basta executar o comando abaixo, no qual `<arquivo-de-entrada>` deve ser o arquivo-alvo a ser lido e `<arquivo-de-saída>` conterá a saída do programa.

`java -jar analisadorLexico-1.0-SNAPSHOT-jar-with-dependencies.jar <arquivo-de-entrada<arquivo-de-saída>`

## Casos teste

Dentro do diretório, há também uma pasta que contém os arquivos testes.

## Arquivo executável

Há também localizado no diretório `T1-analisadorLexico\target` o executável do programa.

### Autores

Felipe Tavoni (758707) - Graduando na Universidade Federal de São Carlos.

