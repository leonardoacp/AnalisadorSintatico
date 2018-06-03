/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;






public enum Tag {
    
    // fim de arquivo
    EOF,  
         
    //Operadores
    OP_EC,  // !
    OP_EQ,  // ==
    OP_NE,   // !=
    OP_GT,   // >
    OP_LT,   // <
    OP_GE,  // >=
    OP_LE,   // <=
    OP_AD,   // +
    OP_MIN,  // -
    OP_MULT, // *
    OP_DIV,  // /
    OP_ASS,  // =
    OP_EX,   // **
    
    //Simbolos    
    SMB_OBC, // {
    SMB_CBC, // }
    SMB_OPA, // (
    SMB_CPA, //  )
    SMB_COM, // ,
    SMB_SEM, // ;
    SMB_AS,  // "
    SMB_APOS,
    SMB_POI,
        
    //identificador
    ID,
    
    //numeros
    INTEGER,
    DOUBLE,
    
    //strings
    LITERAL,
    
    // palavra reservada
    KW,
    KW_PROGRAM,
    KW_IF,
    KW_ELSE,
    KW_WHILE,
    KW_WRITE,
    KW_READ,
    KW_NUM,
    KW_CHAR,
    KW_NOT,
    KW_OR,
    KW_AND,
    
    // constantes
    num_const,
    char_const;
}
 