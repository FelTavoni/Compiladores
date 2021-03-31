# Trabalho 3 (Parte 1) - Analisador Semântico

O trabalho 3 (parte 1) da disciplina *Construção de Compiladores* consiste em implementar um analisador semântico para a linguagem LA (Linguagem Algorítmica) desenvolvida pelo prof. Jander, no âmbito do DC/UFSCar. O analisador semântico deve ler um programa-fonte e apontar onde existe erro semânticos, indicando a linha que causou a detecção do erro.

O analisador semântico é responsável por buscar determinar o significado das construções sintáticas identificadas pelo analisador sintático. Enquanto o analisador sintático cuida da **forma** em que as frases são construídas, o analisador semântico procura lhes atribuir um significado. Dessa forma, como o analisador sintático 'transforma' a linguagem de entrada (programa-fonte) em uma linguagem livre-de-contexto, cabe ao analisador semântico restaurá-la, verificando os significados ignorados na análise sintática.

A seguir, seguem-se as instruções de compilação e execução do analisador semântico.

## Preparação do ambiente

O analisador semântico foi construído em java, integrado com a ferramenta ANTLR, um reconhecedor de linguagens. Para utilizá-lo, é necessário que o usuário tenha instalado em seu ambiente de execução as seguintes ferramentas:

[MAVEN](https://maven.apache.org/) - *versão utilizada: 1.8.0_111*

[JAVA](https://www.java.com/pt-BR/) - *versão utilizada: 3.6.3*

*Vale lembrar que você deve adicionar estas ferramentas a variável $PATH!*

## Compilação do programa

Para compilar o programa, é necessário que você realize o download da pasta acima. Basta então adentrar o diretório e invocar o maven para realizar a construção do executável.

`mvn clean install`

Após a construção do programa, será gerado um executável, em que para executá-lo, basta executar o comando abaixo, no qual `<arquivo-de-entrada>` deve ser o arquivo-alvo a ser lido e `<arquivo-de-saída>` conterá a saída do programa.

`java -jar analisadorSemantico-1.0-SNAPSHOT-jar-with-dependencies.jar <arquivo-de-entrada>.txt <arquivo-de-saída>.txt`

## Arquivo executável

Há também localizado no diretório `analisadorSemantico\target` o executável do programa. Uma nova compilação irá sobreescreve-lô.

## Testes

No diretório principal, há uma pasta 'testes', caso queira testar o programa.

### Autor

Felipe Tavoni (758707) - Graduando na Universidade Federal de São Carlos.

