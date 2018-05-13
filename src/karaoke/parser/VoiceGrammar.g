abctune ::= abcheader abcbody;

abcbody ::= voice+;
@skip endofline{
voice ::= voicename musicline (lyrics)?;
}
voicename ::= "" | 'V:' anything endofline;
musicline ::= anything endofline?;
lyrics ::= 'w:' anything endofline?;
anything ::= [^\r\t\n%]+;

abcheader ::= fieldnumber comment* fieldtitle otherfields* fieldkey;

fieldnumber ::= "X:" anything endofline;
fieldtitle ::= "T:" anything endofline;
otherfields ::= fieldcomposer | fielddefaultlength | fieldmeter | fieldtempo | fieldvoice | comment;
fieldcomposer ::= "C:" anything endofline;
fielddefaultlength ::= "L:" anything endofline;
fieldmeter ::= "M:" anything endofline;
fieldtempo ::= "Q:" anything endofline;
fieldvoice ::= "V:" anything endofline;
fieldkey ::= "K:" anything endofline;


comment ::= spaceortab* "%" anything* newline;

endofline ::= comment | newline;

newline ::= "\n" | "\r" "\n"?;
spaceortab ::= " " | "\t";