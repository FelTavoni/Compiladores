# Trabalho 3 (Parte 2) - Gerador de Código

O trabalho 3 (parte 2) da disciplina *Construção de Compiladores* consiste em implementar um gerador de código para a linguagem LA (Linguagem Algorítmica) desenvolvida pelo prof. Jander, no âmbito do DC/UFSCar. O gerador de código deve ler um programa-fonte, aplicar as análises léxica, sintática e semântica, e se tudo estiver nos conformes, gerar um código na linguagem C (programa-alvo), referente ao programa-fonte na linguagem LA.

O gerador de código é responsável por traduzir a linguagem de entrada (o programa-fonte) em uma linguagem alvo (programa-alvo), o que marca a etapa de *front-end* de um compilador.

A seguir, seguem-se as instruções de compilação e execução do gerador de código.

## Preparação do ambiente

O gerador de código foi construído em java, integrado com a ferramenta ANTLR, um reconhecedor de linguagens. Para utilizá-lo, é necessário que o usuário tenha instalado em seu ambiente de execução as seguintes ferramentas:

[MAVEN](https://maven.apache.org/) - *versão utilizada: 1.8.0_111*

[JAVA](https://www.java.com/pt-BR/) - *versão utilizada: 3.6.3*

*Vale lembrar que você deve adicionar estas ferramentas a variável $PATH!*

## Compilação do programa

Para compilar o programa, é necessário que você realize o download da pasta acima. Basta então adentrar o diretório e invocar o maven para realizar a construção do executável.

`mvn clean install`

Após a construção do programa, será gerado um executável, em que para executá-lo, basta executar o comando abaixo, no qual `<arquivo-de-entrada>` deve ser o arquivo-alvo a ser lido e `<arquivo-de-saída>` conterá a saída do programa.

`java -jar geradorCodigo-1.0-SNAPSHOT-jar-with-dependencies.jar <arquivo-de-entrada>.txt <arquivo-de-saída>.txt`

## Arquivo executável

Há também localizado no diretório `geradorCodigo\target` o executável do programa. Uma nova compilação irá sobreescreve-lô.

## Testes

No diretório principal, há uma pasta 'testes', caso queira testar o programa.

### Autor

Felipe Tavoni (758707) - Graduando na Universidade Federal de São Carlos.

