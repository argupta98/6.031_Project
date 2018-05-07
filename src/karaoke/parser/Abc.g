// Grammar for ABC music notation 


@skip whitespaceAndComment{
Composition ::= Header (Voice)+;
//Header grammar
Header ::= 'X:' TrackNumber '\nT:' Title ('\nC:' Composer|'\nM:' Meter|'\nL:' Length|'\nQ:' Tempo|'\nV:' VoiceName)* '\nK:' Key '\n';
TrackNumber ::= Number;
Composer ::= [\w]+;
Meter ::= Numerator '/' Denominator;
Length ::= Numerator '/' Denominator;
Tempo ::= Length '=' Number;
VoiceName ::= [\w]+;
Key ::= [A-G];
Title ::= [\w]+;
//Music grammar
Voice ::= ('V:' VoiceName '\n')? MusicLine ('\n')?('w:' Lyric)?;
MusicLine ::= (Measure)+;
///////////////Add repeat and other things around measure
Repeat ::= ('|:')? (Measure)+ (':|');
Measure ::= (Note | Chord | Tuple)+ ('|')?;
///////////////
Chord ::= '[' (note)+ ']';
Tuple ::= '('Number (note|chord)+;
Note ::= (Accidental)? Letter (OctaveUp|OctaveDown)* (Numerator)? (NoteDenominator)?;
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
Lyric ::= SyllableNote ([-| ] SyllableNote | '_')* '\n';
SyllableNote ::= syllable ([~] syllable)*;
Syllable ::= [\w*!?&^()$@'"]+;
Letter ::= [A-G]|[a-g];
Comment ::= '%'[^end]* end;
whitespace ::= [ \t\r]+;
whitespaceAndComment ::= (comment | whitespace)+;
Number ::= [1-9][0-9]*;
Numerator ::= [1-9][0-9]*;
Denominator ::= [1-9][0-9]*;
end ::= ([\r\n]|[\n]);