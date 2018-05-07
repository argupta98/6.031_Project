// Grammar for ABC music notation 


@skip whitespaceAndComment{
Composition ::= Header (Voice)+;

//Header grammar
Header ::= 'X:' TrackNumber '\n' 'T:' Title '\n'('C:' Composer '\n'|'M:' Meter '\n'|'L:' Length '\n'|'Q:' Tempo '\n'|'V:' VoiceName '\n')* 'K:' Key '\n';
TrackNumber ::= Number;
Composer ::= [^\r\t\n%]+;
Meter ::= Numerator '/' Denominator;
Length ::= Numerator '/' Denominator;
Tempo ::= Length '=' Number;
VoiceName ::= [^\r\t\n%]+;
Key ::= [A-G#mb]+;
Title ::= [^\r\t\n%]+;

//Music grammar
Voice ::= ('V:' VoiceName '\n')? MusicLine ('\n')?('w:' Lyric)?;
MusicLine ::= (Measure | Repeat)+;
Repeat ::= ('|:')? (Measure)+ (':|');
Measure ::= ('|')? (Note | Chord | Tuple | Rest)+ ('[')?('|')?('|')?(']')?;
Chord ::= '[' (note)+ ']';
Tuple ::= '('Number (note|chord)+;
Note ::= (Accidental)? Letter (OctaveUp|OctaveDown)* (Numerator)? (NoteDenominator)?;
Rest ::= 'z' (Numerator)? (NoteDenominator)?;
OctaveUp ::= '\'';
OctaveDown ::= ',';
NoteDenominator ::= '/' (Denominator)?;
Accidental ::= (Sharp|Flat|DoubleSharp|DoubleFlat|Natural);
Sharp ::= '^';
Flat ::= '_';
DoubleSharp ::= '^^';
DoubleFlat ::= '__';
Natural ::= '=';
}

//Lyric grammar
Lyric ::= SyllableNote ([-| ]+ SyllableNote | '_')* ('\n')?;
SyllableNote ::= syllable ([~] syllable)*;
Syllable ::= [\w*!?&^()$@'".,]+;
Letter ::= [A-G]|[a-g];
Comment ::= '%'[^\n]* '\n';
whitespace ::= [ \t\r]+;
whitespaceAndComment ::= (comment | whitespace)+;
Number ::= [1-9][0-9]*;
Numerator ::= [1-9][0-9]*;
Denominator ::= [1-9][0-9]*;
