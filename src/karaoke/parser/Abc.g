// Grammar for ABC music notation 


@skip whitespaceAndComment{
Composition ::= Header (Voice)+;
//Header grammar
Header ::= 'X:' TrackNumber '\nT:' Title ('\nC:' Composer|'\nM:' Meter|'\nL:' Length|'\nQ:' Tempo|'\nV:' VoiceName)* '\nK:' Key '\n';
TrackNumber ::= Number;
Composer ::= [\w]+;
Meter ::= Number '/' Number;
Length ::= Number '/' Number;
Tempo ::= Length '=' Number;
VoiceName ::= [\w]+;
Key ::= [A-G];
Title ::= [\w]+;
//Music grammar
Voice ::= ('V:' VoiceName '\n')? MusicLine '\n'('w:' Lyric)?;
MusicLine ::= (Measure)+;
///////////////Add repeat and other things around measure
Measure ::= (Note | Chord | Tuple)+ ('|')?;
///////////////
Chord ::= '[' (note)+ ']';
Tuple ::= '('Number (note)+;
Note ::= (Accidental)* Letter (OctaveUp|OctaveDown)* (NoteNumerator)? (NoteDenominator)?;
OctaveUp ::= '\'';
OctaveDown ::= ',';
NoteNumerator ::= Number;
NoteDenominator ::= '/' (Number)*;
Accidental ::= (Sharp|Flat);
Sharp ::= '^';
Flat ::= '_';
}
//Lyric grammar
Lyric ::= SyllableNote ([-| ] SyllableNote | '_')* '\n';
SyllableNote ::= syllable ([~] syllable)*;
Syllable ::= [\w*!?&^()$@'"]+;
Letter ::= [A-G]|[a-g]|'z';
Comment ::= '%'[^end]* end;
whitespace ::= [ \t\r]+;
whitespaceAndComment ::= (comment | whitespace)+;
Number ::= [1-9][0-9]*;
end ::= ([\r\n]|[\n]);