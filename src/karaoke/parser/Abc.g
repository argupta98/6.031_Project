// Grammar for ABC music notation 
abc ::= 'not implemented yet';

Composition ::= Header "\n" (Voice "\n")+;
Voice ::= MusicLine "\n" (Lyric)?;
MusicLine ::= (Measure)+ (Comment)*;

Lyric ::= Syllable ([-|\s] Syllable)*;

Syllable ::=

Comment ::= '%'[\w]+;