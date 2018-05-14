// Grammar for ABC music notation 


@skip whitespaceAndComment{
Composition ::= Header (Voice)+;

//Header grammar
Header ::= 'X:' TrackNumber '\n' 'T:' Title '\n'('C:' Composer '\n'|'M:' Meter '\n'|'L:' Length '\n'|'Q:' Tempo '\n'|'V:' VoiceName '\n')* 'K:' Key '\n';
TrackNumber ::= Number;
Composer ::= [^\r\t\n%]+;
Meter ::= Numerator '/' Denominator | 'C';
Length ::= Numerator '/' Denominator;
Tempo ::= Length '=' Number;
VoiceName ::= [^\r\t\n%]+;
Key ::= Letter (FlatKey|SharpKey)? Minor?;
Title ::= [^\r\t\n%]+;
FlatKey ::= '#';
SharpKey ::= 'b';
Minor ::= 'm';

//Music grammar
Voice ::= ('V:' VoiceName '\n')? MusicLine ('\n')?('w:' Lyric)?;
MusicLine ::= (Repeat | EndSection |Measure)+;
Repeat ::= ('|:' | '[|' | '||' | '|]')? (Measure)+ ('|')?(':|' | FirstEnding SecondEnding) ;
FirstEnding ::= '[' Number (Measure)+ ':|';
SecondEnding ::= '[' Number MusicLine;
EndSection ::= measure ('[|' | '|]' | '||' | '|');
Measure ::= ('|')? (Note | Chord | Tuple | Rest)+ ;
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
//Needs to cover special case: --
Lyric ::= SyllableNote ((Hyphens|NewMeasure|Star|Spaces)* SyllableNote |(Hyphens|NewMeasure|Star|Spaces)* Hold)* ('\n' | '\r\n')?;
Hyphens ::= (Space)? (Hyphen)+;
Star ::= '*';
Hyphen ::= '-';
Spaces ::= [ ]+;
Space ::= [ ];
Hold ::= '_';
NewMeasure ::= '|';
SyllableNote ::= Syllable ((Tilda|backslashhyphen)+ Syllable)*;
Tilda ::= '~';
backslashhyphen ::= "\\""-";

Syllable ::= [a-zA-Z0-9\!.\(\)\'\?\,]+;

//Misc
Letter ::= [A-G]|[a-g];

@skip whitespace{
    Comment ::= '%'[^\n]* '\n';
}
whitespace ::= [ \t\r]+;
whitespaceAndComment ::= (comment | whitespace)+;
Number ::= [0-9]+;
Numerator ::= [1-9][0-9]*;
Denominator ::= [1-9][0-9]*;
