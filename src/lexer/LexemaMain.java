/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.io.*;



public class LexemaMain {
    
    private static final int END_OF_FILE = -1; // contante para fim do arquivo
    private static int lookahead = 0; // armazena o último caractere lido do arquivo	
    public static int n_line = 1; // contador de linhas
    public static int n_column = 1; // contador de colunas
    private RandomAccessFile instance_file; // referencia para o arquivo
    private static TS tabelaSimbolos; // tabela de simbolos
  
    
    public LexemaMain(String input_data) {
		
          tabelaSimbolos = new TS();
        // Abre instance_file de input_data
	try {
            instance_file = new RandomAccessFile(input_data, "r");
	}
	catch(IOException e) {
            System.out.println("Erro de abertura do arquivo " + input_data + "\n" + e);
            System.exit(1);
	}
	catch(Exception e) {
            System.out.println("Erro do programa ou falha da tabela de simbolos\n" + e);
            System.exit(2);
	}
    }
    
    // Fecha instance_file de input_data
    public void fechaArquivo() {

        try {
            instance_file.close();
        }
	catch (IOException errorFile) {
            System.out.println ("Erro ao fechar arquivo\n" + errorFile);
            System.exit(3);
	}
    }
    
    //Reporta erro para o usuário
    public void sinalizaErro(String mensagem) {
        System.out.println("[Erro Lexico]: " + mensagem + "\n");
    }
    
    //Volta uma posição do buffer de leitura
    public void retornaPonteiro(){
 
        try {
            // Não é necessário retornar o ponteiro em caso de Fim de Arquivo
            if (lookahead != END_OF_FILE) {
                instance_file.seek(instance_file.getFilePointer() - 1);
            }    
        }
        catch(IOException e) {
            System.out.println("Falha ao retornar a leitura\n" + e);
            System.exit(4);
        }
    }
    
    /* TODO:
    //[1]   Voce devera se preocupar quando incremetar as n_lines e n_columns,
    //      assim como quando decrementar ou reseta-las.
    //[2]   Toda vez que voce encontrar um lexema completo, voce deve retornar
    //      um objeto new Token(Tag, "lexema", n_line, n_column). Cuidado com as
    //      palavras reservadas que ja sao codastradas na TS. Essa consulta
    //      voce devera fazer somente quando encontrar um Identificador.
    //[3]   Se o caractere lido nao casar com nenhum caractere esperado,
    //      apresentar a mensagem de erro na n_line e n_column correspondente.
    
    */
    // Obtém próximo token
    
    public Token proxToken() {
       
        
	StringBuilder lexema = new StringBuilder();
	int estado = 1;
	char c;
		
	while(true) {
            c = '\u0000'; // null char
            
            // avanca caractere
            try {
                lookahead = instance_file.read(); // PEGANDO O PROXIMO CARACTERE NO ARQUIVO 
		if(lookahead != END_OF_FILE) {
                    c = (char) lookahead;
                }
            }
            catch(IOException e) {
                System.out.println("Erro na leitura do arquivo");
                System.exit(3);
            }
            
            // movimentacao do automato
            switch(estado) {
        
               case 1:
                   
                        if (lookahead == END_OF_FILE)
                            return new Token(Tag.EOF, "EOF", n_line, n_column);
                    
                        else if (c == ' ') {
                            n_column = n_column + 1;
                       
                        } else if (c == '\t') {
                            n_column = n_column + 3;
                       
                        } else if (c == '\n') {
                            n_column = 1;
                            n_line = n_line + 1;
                        
                        } else if (Character.isLetter(c)){
                            estado = 2;
                            n_column = n_column + 1;
                            lexema.append(c);
                        
                        } else if (Character.isDigit(c)) {
                            estado = 3;
                            n_column = n_column + 1;
                            lexema.append(c);
                        
                        } else  if (c == '+') {
                            estado = 4;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_AD, "+", n_line, n_column);
                       
                        } else if (c == '-') {
                            estado = 6;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_MIN, "-", n_line, n_column);
                       
                        } else if (c == '*') {
                            estado = 7;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_MULT, "*", n_line, n_column);
                        
                        } else if (c == '=') {
                            estado = 27;
                            n_column = n_column + 1;
                           // return new Token(Tag.OP_ASS, "=", n_line, n_column);
                            
                        } else if (c == '/') {
                            estado = 8;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_DIV, "/", n_line, n_column);
                       
                        } else if (c == '>') {
                            estado = 9;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_GT, ">", n_line, n_column);
                       
                        } else if (c == '<') {
                            estado = 10;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_LT, "<", n_line, n_column);
                      
                        } else if (c == ',') {
                            estado = 26;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_COM, ",", n_line, n_column);
                       
                        } else if (c == ';') {
                            estado = 25;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_SEM, ";", n_line, n_column);
                       
                        } else if (c == '(') {
                            estado = 23;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_OPA, "(", n_line, n_column);
                      
                        } else if (c == ')') {
                            estado = 24;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_CPA, ")", n_line, n_column);
                       
                        } else if (c == '{') {
                            estado = 20;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_OBC, "{", n_line, n_column);
                      
                        } else if (c == '}') {
                            estado = 22;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_CBC, "}", n_line, n_column);
                      
                        } else if (c == '!') {
                            estado = 18;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_EC, "!", n_line, n_column);

                        } else if (c == '"'){
                            estado = 35;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_AS, "\"", n_line, n_column);
                        
                        } else if (c == '\''){
                            estado = 37;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_APOS, "\'", n_line, n_column);
                        
                        } else if (c == '.'){
                            estado = 39;
                            n_column = n_column + 1;
                            return new Token(Tag.SMB_POI, ".", n_line, n_column);
                        
                        } else {
                            sinalizaErro("Caractere invalido " + c + " na linha " + n_line + " e coluna " + n_column);
                            estado = 1;
                    }
                   
                    break;
               
                case 7:
                        if (c == '*') {
                            estado = 15;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_EX, "**", n_line, n_column);
                      
                        } else if (c == '/') {
                            estado = 34;
                            //Comentário
                       
                        } else {
                            n_column = n_column - 1;
                            retornaPonteiro();
                            return new Token(Tag.OP_MULT, "*", n_line, n_column);
                        }        
		
                        
                case 27:
                        if (c == '=') {
                            estado = 28;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_EQ, "==", n_line, n_column);
                       
                        } else {
                            n_column = n_column - 1;
                            retornaPonteiro();
                            return new Token(Tag.OP_ASS, "=", n_line, n_column);
                        }        
                           
                
                        
                case 8:
                        if (c == '/') {
                            estado = 31;
                        } // Comentário '//' identificado
                        // linha e coluna não serão identificados 
                      
                        else if (c == '*') {
                            estado = 33;
                        } // Comentário /*
                     
                        else {
                            n_column = n_column + 1;
                            return new Token(Tag.OP_DIV, "/", n_line, n_column);        
                        }
                        
                        
                        
                case 37:
                    if (c == '\'') {
                        estado = 25;
                        n_column = n_column + 1;
                        return new Token(Tag.LITERAL, lexema.toString(), n_line, n_column);
                    }
                    else if (lookahead == END_OF_FILE) {
                        sinalizaErro("String deve ser fechada com \' antes do fim de arquivo");
			return null;
                    }
                    else { 
                        lexema.append(c);
                    }
                    break;
                
                
                    
                    
                case 9:
                        if (c == '=') {
                            estado = 11;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_GE, ">=", n_line, n_column);
                       
                        } else {
                            n_column = n_column - 1;
                            retornaPonteiro();
                            return new Token(Tag.OP_GT, ">", n_line, n_column);
                        }
                
                
                
                case 10:
                        if (c == '=') {
                            estado = 12;
                            n_column = n_column + 1;
                            return new Token(Tag.OP_LE, "<=", n_line, n_column);

                       
                        } else {
                            n_column = n_column - 1;
                            retornaPonteiro();
                            return new Token(Tag.OP_LE, "<", n_line, n_column);
                        }
                
                
                
                
                
                case 35:        
                    if (c == '"') {
                        estado = 25;
                        n_column = n_column + 1;
                        return new Token(Tag.LITERAL, lexema.toString(), n_line, n_column);
                    }
                    else if (lookahead == END_OF_FILE) {
                        sinalizaErro("String deve ser fechada com \" antes do fim de arquivo");
			return null;
                    }
                    else { // Se vier outro, permanece no estado 24
                        lexema.append(c);
                    }
                    break;        
                        
                        
                        
                        
                case 18:
                    if (c == '=') { // Estado 5
                        estado = 19;
                        n_column = n_column + 1;
			return new Token(Tag.OP_NE, "!=", n_line, n_column);
                    }
                    else {
                        n_column = n_column - 1;
                        retornaPonteiro();
                        sinalizaErro("Token incompleto para o caractere ! na linha " + n_line + " e coluna " + n_column);
			return null;
                    }
                
                
                
                case 2:
                    
                  if (Character.isLetterOrDigit(c) || c == '_') {
                        lexema.append(c);
                        n_column = n_column + 1;
			// Permanece no estado 14
                    }
                    else { 
                                                
                        n_column = n_column - 1;
			retornaPonteiro();  
                        Token token = tabelaSimbolos.retornaToken(lexema.toString());
                        
                        if (token == null) {
                            return new Token(Tag.ID, lexema.toString(), n_line, n_column);
                        }
                        return token;
                    }
                    break;
                               
                        
        
                   
                case 3:
                    if (Character.isDigit(c)) {
                        lexema.append(c);
                    }
                    else if (c == '.') {
                        lexema.append(c);
                        estado = 39;
                    }
                    else { // Estado 13
                        retornaPonteiro();						
			return new Token(Tag.num_const, lexema.toString(), n_line, n_column);
                    }
                
                break;
                
                case 39:
                    if (Character.isDigit(c)) {
                        lexema.append(c);
                        estado = 27;
                    }
                    else {
                        sinalizaErro("Padrao para double invalido na linha " + n_line + " coluna " + n_column);
			return null;
                    }
                    break;
                            
                            
                            
                case 14:
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        lexema.append(c);
			// Permanece no estado 14
                    }
                    else { // Estado 15
                        estado = 15;
			retornaPonteiro();  
                        Token token = tabelaSimbolos.retornaToken(lexema.toString());
                        
                        if (token == null) {
                            return new Token(Tag.ID, lexema.toString(), n_line, n_column);
                        }
                        return token;
                    }
                    break;
            }
        
        }
        
    }


        
    
    public static void main(String[] args) {
        
        /*
        LexemaMain lexer = new LexemaMain("teste.txt"); // parametro do Lexer: Um programa de acordo com a gramatica
        //Parser parser = new Parser(lexer);
        
	Token token;
        tabelaSimbolos = new TS();

	// Enquanto não houver erros ou não for fim de arquivo:
	do {
            token = lexer.proxToken();
           // parser.Program();
            System.out.println("OLHA");
            
            // Imprime token
	    if(token != null) {
                System.out.println("Token: " + token.toString() + "\t Linha: " + n_line + "\t Coluna: " + n_column);
                
                
                
            }
	     
	} while(token != null && token.getClasse() != Tag.EOF);
	lexer.fechaArquivo();
        
        //// Imprimir a tabela de simbolos
        System.out.println("");
        System.out.println("Tabela de simbolos:");
        System.out.println(tabelaSimbolos.toString());

*/
        
      LexemaMain lexer = new LexemaMain("teste.txt");
      tabelaSimbolos = new TS();
      Parser parser = new Parser(lexer);

      parser.Program();

      parser.fechaArquivos();
      
      //Imprimir a tabela de simbolos
        System.out.println("");
        System.out.println("Tabela de simbolos:");
        System.out.println(tabelaSimbolos.toString());
        

      System.out.println("Compilação de Programa Realizada!");
        
    }
    
}
