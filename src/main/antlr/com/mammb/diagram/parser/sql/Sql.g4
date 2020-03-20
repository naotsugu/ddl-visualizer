grammar Sql;

tokens {
    DELIMITER
}

root
    : statements* EOF
    ;

statements
    : ';'* statement (';'+ statement)* ';'*
    ;

statement
    : createTable
    | createIndex
    | alterTable
    ;

createTable
    : CREATE TABLE (IF NOT EXISTS)? qualifiedTableName
        '(' columnDefinition (',' columnDefinition)*? (',' tableConstraint)* ')'
    ;

createIndex
    : CREATE UNIQUE? INDEX (IF NOT EXISTS)?
        (databaseName '.')? indexName ON tableName '(' indexedColumn (',' indexedColumn)* ')'
    ;

alterTable
    : ALTER TABLE qualifiedTableName alterSpecification
    ;

alterSpecification
    : ADD CONSTRAINT name FOREIGN KEY
        '(' columnName (',' columnName)* ')' referenceDefinition   #alterByAddForeignKey
    | ADD CONSTRAINT name PRIMARY KEY
        '(' indexedColumn (',' indexedColumn)* ')'              #alterByAddPrimaryKey
    | ADD CONSTRAINT name UNIQUE
        '(' indexedColumn (',' indexedColumn)* ')'              #alterByAddUniqueKey
    | ADD COLUMN? columnDefinition                              #alterByAddColumn
    | RENAME TO newTableName                                    #alterByRename
    ;

columnDefinition
    : columnName typeName? columnConstraint*
    ;

typeName
    : name+? (
        '(' signedNumber ')'
      | '(' signedNumber ',' signedNumber ')'
    )?
    ;

columnConstraint
    : (CONSTRAINT name)?
        (
            PRIMARY KEY (ASC | DESC)? AUTOINCREMENT?
          | NOT? NULL
          | UNIQUE
          | DEFAULT (signedNumber | literalValue)
          | referenceDefinition
        )
    ;

referenceDefinition
    : REFERENCES qualifiedTableName ('(' columnName ( ',' columnName )* ')')?
    ;

indexedColumn
    : columnName (ASC | DESC)?
    ;

tableConstraint
    : (CONSTRAINT name)?
        (
            PRIMARY KEY '(' indexedColumn (',' indexedColumn)* ')'
          | UNIQUE '(' indexedColumn (',' indexedColumn)* ')'
          | FOREIGN KEY '(' columnName (',' columnName)* ')' referenceDefinition
        )
    ;

qualifiedTableName
    : (databaseName '.')? tableName
    ;

signedNumber
    : ('+' | '-')? NUMERIC_LITERAL
    ;

literalValue
    : NUMERIC_LITERAL
    | STRING_LITERAL
    | NULL
    | CURRENT_TIME
    | CURRENT_DATE
    | CURRENT_TIMESTAMP
    ;

keyword
    : ADD
    | ALTER
    | ASC
    | AUTOINCREMENT
    | COLLATE
    | COLUMN
    | CONSTRAINT
    | CREATE
    | CURRENT_DATE
    | CURRENT_TIME
    | CURRENT_TIMESTAMP
    | DEFAULT
    | DESC
    | EXISTS
    | FOREIGN
    | IF
    | INDEX
    | INDEXED
    | KEY
    | NOT
    | NOTNULL
    | NULL
    | ON
    | PRIMARY
    | REFERENCES
    | RENAME
    | TABLE
    | TO
    | UNIQUE
    ;

name
    : anyName
    ;

databaseName
    : anyName
    ;

tableName
    : anyName
    ;

newTableName
    : anyName
    ;

columnName
    : anyName
    ;

indexName
    : anyName
    ;

anyName
    : IDENTIFIER
    | keyword
    | STRING_LITERAL
    | '(' anyName ')'
    ;

ADD : A D D;
ALTER : A L T E R;
ASC : A S C;
AUTOINCREMENT : A U T O I N C R E M E N T;
COLLATE : C O L L A T E;
COLUMN : C O L U M N;
CONSTRAINT : C O N S T R A I N T;
CREATE : C R E A T E;
CURRENT_DATE : C U R R E N T '_' D A T E;
CURRENT_TIME : C U R R E N T '_' T I M E;
CURRENT_TIMESTAMP : C U R R E N T '_' T I M E S T A M P;
DEFAULT : D E F A U L T;
DESC : D E S C;
EXISTS : E X I S T S;
FOREIGN : F O R E I G N;
IF : I F;
INDEX : I N D E X;
INDEXED : I N D E X E D;
KEY : K E Y;
NOT : N O T;
NOTNULL : N O T N U L L;
NULL : N U L L;
ON : O N;
PRIMARY : P R I M A R Y;
REFERENCES : R E F E R E N C E S;
RENAME : R E N A M E;
TABLE : T A B L E;
TO : T O;
UNIQUE : U N I Q U E;

IDENTIFIER
    : '"' (~'"' | '""')* '"'
    | '`' (~'`' | '``')* '`'
    | '[' ~']'* ']'
    | [a-zA-Z_] [a-zA-Z_0-9]*
    ;

NUMERIC_LITERAL
    : DIGIT+ ( '.' DIGIT* )? ( E [-+]? DIGIT+ )?
    | '.' DIGIT+ ( E [-+]? DIGIT+ )?
    ;

STRING_LITERAL
    : '\'' ( ~'\'' | '\'\'' )* '\''
    ;

SINGLE_LINE_COMMENT
    : '--' ~[\r\n]* -> channel(HIDDEN)
    ;

MULTILINE_COMMENT
    : '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN)
    ;

SPACES
    : [ \u000B\t\r\n] -> channel(HIDDEN)
    ;

UNRECOGNIZED
    : .
    ;

fragment DIGIT : [0-9];
fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];
