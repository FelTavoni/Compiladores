# Trabalho 2 - Analisador Sintático

O trabalho 2 da disciplina *Construção de Compiladores* consiste em implementar um analisador sintático para a linguagem LA (Linguagem Algorítmica) desenvolvida pelo prof. Jander, no âmbito do DC/UFSCar. O analisador sintático deve ler um programa-fonte e apontar onde existe erro sintático, indicando a linha e o lexema que causou a detecção do erro.

O analisador sintático deve, através dos tokens gerados pelo analisador léxico, reconhecer a estrutura das 'frases' por meio de sua disposição, verificando a estrutura gramatical seguindo uma determinada gramática formal.

A seguir, seguem-se as instruções de compilação e execução do analisador sintático.

## Preparação do ambiente

O analisador sintático foi construído em java, integrado com a ferramenta ANTLR, um reconhecedor de linguagens. Para utilizá-lo, é necessário que o usuário tenha instalado em seu ambiente de execução as seguintes ferramentas:

[MAVEN](https://maven.apache.org/) - *versão utilizada: 1.8.0_111*

[JAVA](https://www.java.com/pt-BR/) - *versão utilizada: 3.6.3*

*Vale lembrar que você deve adicionar estas ferramentas a variável $PATH!*

## Compilação do programa

Para compilar o programa, é necessário que você realize o download da pasta acima. Basta então adentrar o diretório e invocar o maven para realizar a construção do executável.

`mvn clean install`

Após a construção do programa, será gerado um executável, em que para executá-lo, basta executar o comando abaixo, no qual `<arquivo-de-entrada>` deve ser o arquivo-alvo a ser lido e `<arquivo-de-saída>` conterá a saída do programa.

`java -jar analisadorSintatico-1.0-SNAPSHOT-jar-with-dependencies.jar <arquivo-de-entrada>.txt <arquivo-de-saída>.txt`

## Arquivo executável

Há também localizado no diretório `analisadorSintatico\target` o executável do programa. Uma nova compilação irá sobreescreve-lô.

## Testes

No diretório principal, há uma pasta 'testes', caso queira testar o programa.

### Autor

Felipe Tavoni (758707) - Graduando na Universidade Federal de São Carlos.

