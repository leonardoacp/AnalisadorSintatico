package lexer;
import java.util.Collections;


public class Parser {

    private final LexemaMain lexer;
    private Token token;

    public Parser(LexemaMain lexer) {
        this.lexer = lexer;
        token = lexer.proxToken(); // Leitura inicial obrigatoria do primeiro simbolo
        System.out.println("[DEBUG]" + token.toString());
    }

    // Fecha os arquivos de entrada e de tokens
    public void fechaArquivos() {

        lexer.fechaArquivo();
    }

    public void erroSintatico(String mensagem) {

        System.out.println("[Erro Sintatico] na linha " + token.getLinha() + " e coluna " + token.getColuna());
        System.out.println(mensagem + "\n");
    }
    
    public void advance() {
    	token = lexer.proxToken();
    	System.out.println("[DEBUG]" + token.toString());
    }
    
	// verifica token esperado t
   public boolean eat(Tag t) {
		if(token.getClasse() == t) {
			advance();
			return true;
		} 
		else {
			return false;
		}
   }
    
   public void Program(){

        if(token.getClasse() == Tag.KW_PROGRAM) { // espera public	
			eat(Tag.KW_PROGRAM);
                        
                if (token.getClasse() == Tag.ID){      
                        eat(Tag.ID);
                        Body();
       
                }else{
                     erroSintatico("Esperado \" id \", encontrado " + token.getLexema());
		     System.exit(1);
                } 
        
        }else{
           erroSintatico("Esperado \"program\", encontrado " + token.getLexema());
	   System.exit(1);
        } 

   }
   
   
   public void Body(){
       
       Decl_list();
       
        if (token.getClasse() == Tag.SMB_OBC){
                 eat(Tag.SMB_OBC);  // {
                 Stmt_list();
       
                 if (token.getClasse() == Tag.SMB_CBC){
                        eat(Tag.SMB_CBC); 
                 
                 } else{
                        erroSintatico("Esperado \" } \", encontrado " + token.getLexema());
                        System.exit(1);
                 }

                 
        } else{
                erroSintatico("Esperado \" { \", encontrado " + token.getLexema());
                System.exit(1);
        } 

   }
   
   public void Decl_list(){
       
                   if (token.getClasse() == Tag.SMB_OBC){
                     return;
                   }
       
       
                    Decl();
       
                    if (token.getClasse() == Tag.SMB_SEM){
                            eat(Tag.SMB_SEM);  
                            Decl_list();
       
                    }else{
                         erroSintatico("Esperado \" ; \", encontrado " + token.getLexema());
                         System.exit(1);
                    } 

                        


   }
   
   public void Decl(){
       Type();
       Id_list();
   }
   
   
   
   public void Type(){
       if (token.getClasse() == Tag.KW_NUM){  // NUM
           eat(Tag.KW_NUM);
       
       } else if (token.getClasse() == Tag.KW_CHAR){  // CHAR
           eat(Tag.KW_CHAR);
       
       } else
       {
           erroSintatico("Esperado \"num , char\", encontrado " + token.getLexema());
	   System.exit(1);
       }

   }
       
   
   
   public void Id_list(){
        if (token.getClasse() == Tag.ID){
            eat(Tag.ID);
            Id_listA();
        
        }else{

           erroSintatico("Esperado \" id \", encontrado " + token.getLexema());
	   System.exit(1);
        }   
   }
   
   
   public void Id_listA(){
       if (token.getClasse() == Tag.SMB_COM){
           eat(Tag.SMB_COM);
           Id_list();       
       }
   }
   
   
   public void Stmt_list(){
       
       Stmt();
       
       if (token.getClasse() == Tag.SMB_SEM){
             eat(Tag.SMB_SEM);
             Stmt_list();
       
       }
       else{
           
          erroSintatico("Esperado \" ; \", encontrado " + token.getLexema());
	  System.exit(1);
       }
   }
   
   public void Stmt(){
       if (token.getClasse() == Tag.ID){
           Assign_stmt();
       
       } else if (token.getClasse() == Tag.KW_IF){
           If_stmt();
       
       }else if (token.getClasse() == Tag.KW_WHILE){
           While_stmt();
       
       }else if (token.getClasse() == Tag.KW_READ){
           Read_stmt();
       
       }else if (token.getClasse() == Tag.KW_WRITE){
           Write_stmt();
       
       }else{
           
         erroSintatico("Esperado \" id , if , while , read , write \", encontrado " + token.getLexema());
         System.exit(1);
       } 

       
   }
   
   public void Assign_stmt(){
        if (token.getClasse() == Tag.ID){ // id
              eat(Tag.ID);
       
                if (token.getClasse() == Tag.OP_ASS){ // =
                  eat(Tag.OP_ASS);
              
                  Simple_expr();
       
                }else{
                  erroSintatico("Esperado \" = \", encontrado " + token.getLexema());
                  System.exit(1);
                } 
                                        
        }else{
            
            erroSintatico("Esperado \" id \", encontrado " + token.getLexema());
            System.exit(1);
        } 

   }
   
   
   
   
   
   public void If_stmt(){
        if (token.getClasse() == Tag.KW_IF){ // if
                eat(Tag.KW_IF);
                
                if (token.getClasse() == Tag.SMB_OPA){ // abre parenteses
                    eat(Tag.SMB_OPA);           
                  
                    Condition();
           
                       if (token.getClasse() == Tag.SMB_CPA){ // fecha parenteses
                                eat(Tag.SMB_CPA);
                
  
                                if (token.getClasse() == Tag.SMB_OBC){ // abre chaves
                                        eat(Tag.SMB_OBC);
           
                                        Stmt_list();
           
                                            if (token.getClasse() == Tag.SMB_CBC){ // fecha chaves
                                                    eat(Tag.SMB_CBC);
                       
   
                                            If_stmtA();          
                                            
                                            }else{
                                                  erroSintatico("Esperado \" } \", encontrado " + token.getLexema());
                                                  System.exit(1);
                                            } 

                
                                    }  else{
                                    
                                        erroSintatico("Esperado \" { \", encontrado " + token.getLexema());
                                    	System.exit(1);
                                } 

                
                       } else {
                                  erroSintatico("Esperado \" ) \", encontrado " + token.getLexema());
                                  System.exit(1); 
                       }
     
                                
                } else{
                    
                                erroSintatico("Esperado \" ( \", encontrado " + token.getLexema());
                        	System.exit(1);   
                } 
                   
    
        }  else{
            
                       erroSintatico("Esperado \" if \", encontrado " + token.getLexema());
                    	System.exit(1);
        } 

        
   }
   
   public void If_stmtA(){
        if (token.getClasse() == Tag.KW_ELSE || token.getClasse() == Tag.SMB_SEM || token.getClasse() == Tag.SMB_CBC){
            eat(Tag.KW_ELSE);
            
            
             if (token.getClasse() == Tag.SMB_OBC){
                    eat(Tag.SMB_OBC);
                    
                    Stmt_list();
                
                    if (token.getClasse() == Tag.SMB_CBC){
                            eat(Tag.SMB_CBC);
                            
              
                     } else{
                            erroSintatico("Esperado \" } \", encontrado " + token.getLexema());
                            System.exit(1);
                    }    

                    
             } else{
                    erroSintatico("Esperado \" { \", encontrado " + token.getLexema());
                    System.exit(1);
             }     

        } else{
                   erroSintatico("Esperado \" else , ; , } \", encontrado " + token.getLexema());
                   System.exit(1);
        }     

                                
       
   }
   
   
   public void Condition(){
       Expression();
   }
   
   public void While_stmt(){
       Stmt_prefix();
       
        if (token.getClasse() == Tag.SMB_OBC){ // abre chaves
                eat(Tag.SMB_OBC);                     
                Stmt_list();
           
           
                if (token.getClasse() == Tag.SMB_CBC){ // fecha chaves
                     eat(Tag.SMB_CBC);
           
           
                }else{
                     erroSintatico("Esperado \" } \", encontrado " + token.getLexema());
                     System.exit(1);
                } 

                
        }else{
                 erroSintatico("Esperado \" { \", encontrado " + token.getLexema());
                 System.exit(1);
        } 

                        
   }
   
   
   public void Stmt_prefix(){
        if (token.getClasse() == Tag.KW_WHILE){ // while
           eat(Tag.KW_WHILE);
           
                if (token.getClasse() == Tag.SMB_OPA){ // (
                     eat(Tag.SMB_OPA);
                     Condition();
                      
                        if (token.getClasse() == Tag.SMB_CPA){ // )
                                eat(Tag.SMB_CPA);
                        
                        }else{
                            erroSintatico("Esperado \" ) \", encontrado " + token.getLexema());
                            System.exit(1);
                        } 

           
                }else{
                     erroSintatico("Esperado \" ( \", encontrado " + token.getLexema());
                     System.exit(1);
                } 

       
        }else{
                       erroSintatico("Esperado \" while \", encontrado " + token.getLexema());
                    	System.exit(1);
        } 

   }
   
   
   public void Read_stmt(){
       if (token.getClasse() == Tag.KW_READ){
           eat(Tag.KW_READ);
           
           if (token.getClasse() == Tag.ID){
                eat(Tag.ID);
           }else{
                   erroSintatico("Esperado \" id \", encontrado " + token.getLexema());
                   System.exit(1);
           } 

       }else{
                erroSintatico("Esperado \" read \", encontrado " + token.getLexema());
                System.exit(1);
       } 

   }
   
   public void Write_stmt(){
       if (token.getClasse() == Tag.KW_WRITE){
           eat(Tag.KW_WRITE);
           Writable();
       }else{
                    erroSintatico("Esperado \" write \", encontrado " + token.getLexema());
                    System.exit(1);
       } 

   }
   
   public void Writable(){
       if(token.getClasse() == Tag.ID || token.getClasse() == Tag.num_const || token.getClasse() == Tag.char_const || 
          token.getClasse() == Tag.SMB_OPA || token.getClasse() == Tag.KW_NOT || token.getClasse() == Tag.LITERAL){
          Simple_expr();
       
       }else{
              erroSintatico("Esperado \" id , num_const , char_const , ( , not , literal \", encontrado " + token.getLexema());
              System.exit(1);
       } 

           
   }
   
   public void Expression(){
       Simple_expr();
       ExpressionA();
   }
   
   public void ExpressionA(){
       if (token.getClasse() == Tag.OP_EQ || token.getClasse() == Tag.OP_GT || token.getClasse() == Tag.OP_GE || 
           token.getClasse() == Tag.OP_LT || token.getClasse() == Tag.OP_LE || token.getClasse() == Tag.OP_NE ||
           token.getClasse() == Tag.SMB_CPA){
           Relop();
           Simple_expr();
       
           
       }else{
       
          erroSintatico("Esperado \"== , > , >= , < , <= , != , )\", encontrado " + token.getLexema());
          System.exit(1);
       } 

   }
  
   
   public void Simple_expr(){
       Term();
       Simple_exprA();
   }
   
   
   public void Simple_exprA(){
        if ( /* token.getClasse() == Tag.SMB_SEM || */token.getClasse() == Tag.SMB_CBC ||/* token.getClasse() == Tag.SMB_CPA || */
            /*token.getClasse() == Tag.OP_EQ ||*/ token.getClasse() == Tag.OP_GT || token.getClasse() == Tag.OP_GE ||
            token.getClasse() == Tag.OP_LT || token.getClasse() == Tag.OP_LE || token.getClasse() == Tag.OP_NE ||
            token.getClasse() == Tag.OP_AD || token.getClasse() == Tag.OP_MIN || token.getClasse() == Tag.KW_OR){
            Addop();
            Term();
            Simple_exprA();
        
        }else{
        
             //   erroSintatico("Esperado \"+ , -  , == , > , >= , < , <= , != , or , ; , } , )\", encontrado " + token.getLexema());
              //  System.exit(1);
        } 

        
   }
   
   
   public void Term(){
        Factor_a();
        TermA();
   }
   
   
   public void TermA(){
        if (token.getClasse() == Tag.OP_MULT || token.getClasse() == Tag.OP_DIV || token.getClasse() == Tag.KW_AND || 
           /* token.getClasse() == Tag.SMB_SEM || */token.getClasse() == Tag.SMB_CBC ||/* token.getClasse() == Tag.SMB_CPA || */
            /*token.getClasse() == Tag.OP_EQ ||*/ token.getClasse() == Tag.OP_GT || token.getClasse() == Tag.OP_GE ||
            token.getClasse() == Tag.OP_LT || token.getClasse() == Tag.OP_LE || token.getClasse() == Tag.OP_NE ||
            token.getClasse() == Tag.OP_AD || token.getClasse() == Tag.OP_MIN || token.getClasse() == Tag.KW_OR){
            Mulop();
            Factor_a();
            TermA();
        
        }
  
        
        else {
               // erroSintatico("Esperado \"+ , - , * , / , == , > , >= , < , <= , != , or , ; , ) , }\", encontrado " + token.getLexema());
	//	System.exit(1);
        }
    }
   
   
      
    public void Factor_a(){
        if(token.getClasse() == Tag.ID || token.getClasse() == Tag.num_const || 
           token.getClasse() == Tag.char_const || token.getClasse() == Tag.SMB_OPA){
           Factor(); 
       
       
        }else if (token.getClasse() == Tag.KW_NOT){
           eat(Tag.KW_NOT);
           Factor();  
      
        } else{
                erroSintatico("Esperado \" id , num_const , char_const , ( , not \", encontrado " + token.getLexema());
                System.exit(1);
        } 

    }
    
   
   public void Factor(){
        if (token.getClasse() == Tag.ID){
             eat(Tag.ID);
       
        } else if (token.getClasse() == Tag.num_const || token.getClasse() == Tag.char_const){
             Constant();
       
        }else if (token.getClasse() == Tag.SMB_OPA){
                    eat(Tag.SMB_OPA);   
                    Expression();
                    
                    if (token.getClasse() == Tag.SMB_CPA){
                            eat(Tag.SMB_CPA);     
                    
                    }else{
                            erroSintatico("Esperado \" ) \", encontrado " + token.getLexema());
                            System.exit(1);
                    }

        }else{
                            erroSintatico("Esperado \" ( \", encontrado " + token.getLexema());
                                  System.exit(1);
        } 

        }
       
   
   
   public void Relop(){
       if (token.getClasse() == Tag.OP_EQ){  // ==
           eat(Tag.OP_EQ);
       
       } else if (token.getClasse() == Tag.OP_GT){  // >
           eat(Tag.OP_GT);
       
       }else if (token.getClasse() == Tag.OP_GE){  // >=
           eat(Tag.OP_GE);
       
       } else if (token.getClasse() == Tag.OP_LT){  // <
           eat(Tag.OP_LT);

       } else if (token.getClasse() == Tag.OP_LE){  // <=
           eat(Tag.OP_LE);
       
       }else if (token.getClasse() == Tag.OP_NE){  // !=
           eat(Tag.OP_NE);
       
       } else{
                erroSintatico("Esperado \"== , > , >= , < , <= , !=\", encontrado " + token.getLexema());
		System.exit(1);
       }

	}
       
       
   
   public void Addop(){
       if (token.getClasse() == Tag.OP_AD){  // +
           eat(Tag.OP_AD);
       
       } else if (token.getClasse() == Tag.OP_MIN){  // -
           eat(Tag.OP_MIN);
       
       }else if (token.getClasse() == Tag.KW_OR){  // or
           eat(Tag.KW_OR);
       
       } else{
                erroSintatico("Esperado \"+ , - , or\", encontrado " + token.getLexema());
		System.exit(1);
       }

	}
   
   
   public void Mulop(){
       if (token.getClasse() == Tag.OP_MULT){  // *
           eat(Tag.OP_MULT);
       
       } else if (token.getClasse() == Tag.OP_DIV){  // /
           eat(Tag.OP_DIV);
       
       }else if (token.getClasse() == Tag.KW_AND){  // and
           eat(Tag.KW_AND);
       
       } else{
           
                erroSintatico("Esperado \"* , / , and\", encontrado " + token.getLexema());
		System.exit(1);
       }

   }
       
   
   public void Constant(){
       if (token.getClasse() == Tag.num_const){  // num_const
           eat(Tag.num_const);
       
       } else if (token.getClasse() == Tag.char_const){  // char_const
           eat(Tag.char_const);
       
       } else{
               erroSintatico("Esperado \"* , / , and\", encontrado " + token.getLexema());
	       System.exit(1);
       }

	}
}