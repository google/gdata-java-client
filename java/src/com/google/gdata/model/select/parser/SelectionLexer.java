/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// $ANTLR 3.1.1 java/com/google/gdata/model/select/parser/Selection.g 

package com.google.gdata.model.select.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SelectionLexer extends Lexer {
    public static final int SIGN=8;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int LETTER=11;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int NUMBER=6;
    public static final int WHITESPACE=7;
    public static final int EOF=-1;
    public static final int NONLETTER=12;
    public static final int T__19=19;
    public static final int NCNAME=4;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int EXP=10;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int STRINGLITERAL=5;
    public static final int DIGIT=9;

    @Override
    /** Fails at the first error and throws {@link InternalParseException}. */
    public void reportError(RecognitionException e) {
      throw new InternalParseException(getErrorMessage(e, getTokenNames()), e);
    }


    // delegates
    // delegators

    public SelectionLexer() {;} 
    public SelectionLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SelectionLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "java/com/google/gdata/model/select/parser/Selection.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:14:7: ( ':' )
            // java/com/google/gdata/model/select/parser/Selection.g:14:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:15:7: ( '=' )
            // java/com/google/gdata/model/select/parser/Selection.g:15:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:16:7: ( ',' )
            // java/com/google/gdata/model/select/parser/Selection.g:16:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:17:7: ( '[' )
            // java/com/google/gdata/model/select/parser/Selection.g:17:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:18:7: ( ']' )
            // java/com/google/gdata/model/select/parser/Selection.g:18:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:19:7: ( '(' )
            // java/com/google/gdata/model/select/parser/Selection.g:19:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:20:7: ( ')' )
            // java/com/google/gdata/model/select/parser/Selection.g:20:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:21:7: ( '!=' )
            // java/com/google/gdata/model/select/parser/Selection.g:21:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:22:7: ( '>' )
            // java/com/google/gdata/model/select/parser/Selection.g:22:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:23:7: ( '>=' )
            // java/com/google/gdata/model/select/parser/Selection.g:23:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:24:7: ( '<' )
            // java/com/google/gdata/model/select/parser/Selection.g:24:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:25:7: ( '<=' )
            // java/com/google/gdata/model/select/parser/Selection.g:25:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:26:7: ( '/' )
            // java/com/google/gdata/model/select/parser/Selection.g:26:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:27:7: ( '@' )
            // java/com/google/gdata/model/select/parser/Selection.g:27:9: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:28:7: ( '*' )
            // java/com/google/gdata/model/select/parser/Selection.g:28:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:306:3: ( ( ' ' | '\\t' )+ )
            // java/com/google/gdata/model/select/parser/Selection.g:306:5: ( ' ' | '\\t' )+
            {
            // java/com/google/gdata/model/select/parser/Selection.g:306:5: ( ' ' | '\\t' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


                _channel = HIDDEN;
              

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    // $ANTLR start "STRINGLITERAL"
    public final void mSTRINGLITERAL() throws RecognitionException {
        try {
            int _type = STRINGLITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:312:3: ( ( '\\'' ( '\\'\\'' | ~ ( '\\'' ) )* '\\'' ) | ( '\"' ( '\"\"' | ~ ( '\"' ) )* '\"' ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\'') ) {
                alt4=1;
            }
            else if ( (LA4_0=='\"') ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:312:5: ( '\\'' ( '\\'\\'' | ~ ( '\\'' ) )* '\\'' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:312:5: ( '\\'' ( '\\'\\'' | ~ ( '\\'' ) )* '\\'' )
                    // java/com/google/gdata/model/select/parser/Selection.g:312:6: '\\'' ( '\\'\\'' | ~ ( '\\'' ) )* '\\''
                    {
                    match('\''); 
                    // java/com/google/gdata/model/select/parser/Selection.g:312:11: ( '\\'\\'' | ~ ( '\\'' ) )*
                    loop2:
                    do {
                        int alt2=3;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0=='\'') ) {
                            int LA2_1 = input.LA(2);

                            if ( (LA2_1=='\'') ) {
                                alt2=1;
                            }


                        }
                        else if ( ((LA2_0>='\u0000' && LA2_0<='&')||(LA2_0>='(' && LA2_0<='\uFFFF')) ) {
                            alt2=2;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // java/com/google/gdata/model/select/parser/Selection.g:312:12: '\\'\\''
                    	    {
                    	    match("\'\'"); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // java/com/google/gdata/model/select/parser/Selection.g:312:21: ~ ( '\\'' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);

                    match('\''); 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:313:5: ( '\"' ( '\"\"' | ~ ( '\"' ) )* '\"' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:313:5: ( '\"' ( '\"\"' | ~ ( '\"' ) )* '\"' )
                    // java/com/google/gdata/model/select/parser/Selection.g:313:6: '\"' ( '\"\"' | ~ ( '\"' ) )* '\"'
                    {
                    match('\"'); 
                    // java/com/google/gdata/model/select/parser/Selection.g:313:10: ( '\"\"' | ~ ( '\"' ) )*
                    loop3:
                    do {
                        int alt3=3;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0=='\"') ) {
                            int LA3_1 = input.LA(2);

                            if ( (LA3_1=='\"') ) {
                                alt3=1;
                            }


                        }
                        else if ( ((LA3_0>='\u0000' && LA3_0<='!')||(LA3_0>='#' && LA3_0<='\uFFFF')) ) {
                            alt3=2;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // java/com/google/gdata/model/select/parser/Selection.g:313:11: '\"\"'
                    	    {
                    	    match("\"\""); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // java/com/google/gdata/model/select/parser/Selection.g:313:18: ~ ( '\"' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);

                    match('\"'); 

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRINGLITERAL"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:317:3: ( SIGN ( ( ( DIGIT )+ ( '.' ( DIGIT )* )? ( EXP )? ) | ( '.' ( DIGIT )+ ( EXP )? ) ) | ( '-' 'INF' ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='-') ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1=='I') ) {
                    alt12=2;
                }
                else if ( (LA12_1=='.'||(LA12_1>='0' && LA12_1<='9')) ) {
                    alt12=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA12_0=='+'||LA12_0=='.'||(LA12_0>='0' && LA12_0<='9')) ) {
                alt12=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:317:5: SIGN ( ( ( DIGIT )+ ( '.' ( DIGIT )* )? ( EXP )? ) | ( '.' ( DIGIT )+ ( EXP )? ) )
                    {
                    mSIGN(); 
                    // java/com/google/gdata/model/select/parser/Selection.g:317:10: ( ( ( DIGIT )+ ( '.' ( DIGIT )* )? ( EXP )? ) | ( '.' ( DIGIT )+ ( EXP )? ) )
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                        alt11=1;
                    }
                    else if ( (LA11_0=='.') ) {
                        alt11=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 11, 0, input);

                        throw nvae;
                    }
                    switch (alt11) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:317:11: ( ( DIGIT )+ ( '.' ( DIGIT )* )? ( EXP )? )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:317:11: ( ( DIGIT )+ ( '.' ( DIGIT )* )? ( EXP )? )
                            // java/com/google/gdata/model/select/parser/Selection.g:317:12: ( DIGIT )+ ( '.' ( DIGIT )* )? ( EXP )?
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:317:12: ( DIGIT )+
                            int cnt5=0;
                            loop5:
                            do {
                                int alt5=2;
                                int LA5_0 = input.LA(1);

                                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                                    alt5=1;
                                }


                                switch (alt5) {
                            	case 1 :
                            	    // java/com/google/gdata/model/select/parser/Selection.g:317:12: DIGIT
                            	    {
                            	    mDIGIT(); 

                            	    }
                            	    break;

                            	default :
                            	    if ( cnt5 >= 1 ) break loop5;
                                        EarlyExitException eee =
                                            new EarlyExitException(5, input);
                                        throw eee;
                                }
                                cnt5++;
                            } while (true);

                            // java/com/google/gdata/model/select/parser/Selection.g:317:19: ( '.' ( DIGIT )* )?
                            int alt7=2;
                            int LA7_0 = input.LA(1);

                            if ( (LA7_0=='.') ) {
                                alt7=1;
                            }
                            switch (alt7) {
                                case 1 :
                                    // java/com/google/gdata/model/select/parser/Selection.g:317:20: '.' ( DIGIT )*
                                    {
                                    match('.'); 
                                    // java/com/google/gdata/model/select/parser/Selection.g:317:24: ( DIGIT )*
                                    loop6:
                                    do {
                                        int alt6=2;
                                        int LA6_0 = input.LA(1);

                                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                                            alt6=1;
                                        }


                                        switch (alt6) {
                                    	case 1 :
                                    	    // java/com/google/gdata/model/select/parser/Selection.g:317:24: DIGIT
                                    	    {
                                    	    mDIGIT(); 

                                    	    }
                                    	    break;

                                    	default :
                                    	    break loop6;
                                        }
                                    } while (true);


                                    }
                                    break;

                            }

                            // java/com/google/gdata/model/select/parser/Selection.g:317:33: ( EXP )?
                            int alt8=2;
                            int LA8_0 = input.LA(1);

                            if ( (LA8_0=='E'||LA8_0=='e') ) {
                                alt8=1;
                            }
                            switch (alt8) {
                                case 1 :
                                    // java/com/google/gdata/model/select/parser/Selection.g:317:33: EXP
                                    {
                                    mEXP(); 

                                    }
                                    break;

                            }


                            }


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:317:41: ( '.' ( DIGIT )+ ( EXP )? )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:317:41: ( '.' ( DIGIT )+ ( EXP )? )
                            // java/com/google/gdata/model/select/parser/Selection.g:317:42: '.' ( DIGIT )+ ( EXP )?
                            {
                            match('.'); 
                            // java/com/google/gdata/model/select/parser/Selection.g:317:46: ( DIGIT )+
                            int cnt9=0;
                            loop9:
                            do {
                                int alt9=2;
                                int LA9_0 = input.LA(1);

                                if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                                    alt9=1;
                                }


                                switch (alt9) {
                            	case 1 :
                            	    // java/com/google/gdata/model/select/parser/Selection.g:317:46: DIGIT
                            	    {
                            	    mDIGIT(); 

                            	    }
                            	    break;

                            	default :
                            	    if ( cnt9 >= 1 ) break loop9;
                                        EarlyExitException eee =
                                            new EarlyExitException(9, input);
                                        throw eee;
                                }
                                cnt9++;
                            } while (true);

                            // java/com/google/gdata/model/select/parser/Selection.g:317:53: ( EXP )?
                            int alt10=2;
                            int LA10_0 = input.LA(1);

                            if ( (LA10_0=='E'||LA10_0=='e') ) {
                                alt10=1;
                            }
                            switch (alt10) {
                                case 1 :
                                    // java/com/google/gdata/model/select/parser/Selection.g:317:53: EXP
                                    {
                                    mEXP(); 

                                    }
                                    break;

                            }


                            }


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:318:5: ( '-' 'INF' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:318:5: ( '-' 'INF' )
                    // java/com/google/gdata/model/select/parser/Selection.g:318:6: '-' 'INF'
                    {
                    match('-'); 
                    match("INF"); 


                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "EXP"
    public final void mEXP() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:322:3: ( ( 'e' | 'E' ) SIGN ( DIGIT )+ )
            // java/com/google/gdata/model/select/parser/Selection.g:322:5: ( 'e' | 'E' ) SIGN ( DIGIT )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            mSIGN(); 
            // java/com/google/gdata/model/select/parser/Selection.g:322:20: ( DIGIT )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>='0' && LA13_0<='9')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:322:20: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "EXP"

    // $ANTLR start "SIGN"
    public final void mSIGN() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:326:3: ( ( '+' | '-' )? )
            // java/com/google/gdata/model/select/parser/Selection.g:326:5: ( '+' | '-' )?
            {
            // java/com/google/gdata/model/select/parser/Selection.g:326:5: ( '+' | '-' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0=='+'||LA14_0=='-') ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "SIGN"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:330:3: ( ( '0' .. '9' ) )
            // java/com/google/gdata/model/select/parser/Selection.g:330:5: ( '0' .. '9' )
            {
            // java/com/google/gdata/model/select/parser/Selection.g:330:5: ( '0' .. '9' )
            // java/com/google/gdata/model/select/parser/Selection.g:330:6: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "NCNAME"
    public final void mNCNAME() throws RecognitionException {
        try {
            int _type = NCNAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // java/com/google/gdata/model/select/parser/Selection.g:337:3: ( ( LETTER | '_' ) ( LETTER | NONLETTER | '.' | '-' | '_' )* )
            // java/com/google/gdata/model/select/parser/Selection.g:337:5: ( LETTER | '_' ) ( LETTER | NONLETTER | '.' | '-' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u0131')||(input.LA(1)>='\u0134' && input.LA(1)<='\u013E')||(input.LA(1)>='\u0141' && input.LA(1)<='\u0148')||(input.LA(1)>='\u014A' && input.LA(1)<='\u017E')||(input.LA(1)>='\u0180' && input.LA(1)<='\u01C3')||(input.LA(1)>='\u01CD' && input.LA(1)<='\u01F0')||(input.LA(1)>='\u01F4' && input.LA(1)<='\u01F5')||(input.LA(1)>='\u01FA' && input.LA(1)<='\u0217')||(input.LA(1)>='\u0250' && input.LA(1)<='\u02A8')||(input.LA(1)>='\u02BB' && input.LA(1)<='\u02C1')||input.LA(1)=='\u0386'||(input.LA(1)>='\u0388' && input.LA(1)<='\u038A')||input.LA(1)=='\u038C'||(input.LA(1)>='\u038E' && input.LA(1)<='\u03A1')||(input.LA(1)>='\u03A3' && input.LA(1)<='\u03CE')||(input.LA(1)>='\u03D0' && input.LA(1)<='\u03D6')||input.LA(1)=='\u03DA'||input.LA(1)=='\u03DC'||input.LA(1)=='\u03DE'||input.LA(1)=='\u03E0'||(input.LA(1)>='\u03E2' && input.LA(1)<='\u03F3')||(input.LA(1)>='\u0401' && input.LA(1)<='\u040C')||(input.LA(1)>='\u040E' && input.LA(1)<='\u044F')||(input.LA(1)>='\u0451' && input.LA(1)<='\u045C')||(input.LA(1)>='\u045E' && input.LA(1)<='\u0481')||(input.LA(1)>='\u0490' && input.LA(1)<='\u04C4')||(input.LA(1)>='\u04C7' && input.LA(1)<='\u04C8')||(input.LA(1)>='\u04CB' && input.LA(1)<='\u04CC')||(input.LA(1)>='\u04D0' && input.LA(1)<='\u04EB')||(input.LA(1)>='\u04EE' && input.LA(1)<='\u04F5')||(input.LA(1)>='\u04F8' && input.LA(1)<='\u04F9')||(input.LA(1)>='\u0531' && input.LA(1)<='\u0556')||input.LA(1)=='\u0559'||(input.LA(1)>='\u0561' && input.LA(1)<='\u0586')||(input.LA(1)>='\u05D0' && input.LA(1)<='\u05EA')||(input.LA(1)>='\u05F0' && input.LA(1)<='\u05F2')||(input.LA(1)>='\u0621' && input.LA(1)<='\u063A')||(input.LA(1)>='\u0641' && input.LA(1)<='\u064A')||(input.LA(1)>='\u0671' && input.LA(1)<='\u06B7')||(input.LA(1)>='\u06BA' && input.LA(1)<='\u06BE')||(input.LA(1)>='\u06C0' && input.LA(1)<='\u06CE')||(input.LA(1)>='\u06D0' && input.LA(1)<='\u06D3')||input.LA(1)=='\u06D5'||(input.LA(1)>='\u06E5' && input.LA(1)<='\u06E6')||(input.LA(1)>='\u0905' && input.LA(1)<='\u0939')||input.LA(1)=='\u093D'||(input.LA(1)>='\u0958' && input.LA(1)<='\u0961')||(input.LA(1)>='\u0985' && input.LA(1)<='\u098C')||(input.LA(1)>='\u098F' && input.LA(1)<='\u0990')||(input.LA(1)>='\u0993' && input.LA(1)<='\u09A8')||(input.LA(1)>='\u09AA' && input.LA(1)<='\u09B0')||input.LA(1)=='\u09B2'||(input.LA(1)>='\u09B6' && input.LA(1)<='\u09B9')||(input.LA(1)>='\u09DC' && input.LA(1)<='\u09DD')||(input.LA(1)>='\u09DF' && input.LA(1)<='\u09E1')||(input.LA(1)>='\u09F0' && input.LA(1)<='\u09F1')||(input.LA(1)>='\u0A05' && input.LA(1)<='\u0A0A')||(input.LA(1)>='\u0A0F' && input.LA(1)<='\u0A10')||(input.LA(1)>='\u0A13' && input.LA(1)<='\u0A28')||(input.LA(1)>='\u0A2A' && input.LA(1)<='\u0A30')||(input.LA(1)>='\u0A32' && input.LA(1)<='\u0A33')||(input.LA(1)>='\u0A35' && input.LA(1)<='\u0A36')||(input.LA(1)>='\u0A38' && input.LA(1)<='\u0A39')||(input.LA(1)>='\u0A59' && input.LA(1)<='\u0A5C')||input.LA(1)=='\u0A5E'||(input.LA(1)>='\u0A72' && input.LA(1)<='\u0A74')||(input.LA(1)>='\u0A85' && input.LA(1)<='\u0A8B')||input.LA(1)=='\u0A8D'||(input.LA(1)>='\u0A8F' && input.LA(1)<='\u0A91')||(input.LA(1)>='\u0A93' && input.LA(1)<='\u0AA8')||(input.LA(1)>='\u0AAA' && input.LA(1)<='\u0AB0')||(input.LA(1)>='\u0AB2' && input.LA(1)<='\u0AB3')||(input.LA(1)>='\u0AB5' && input.LA(1)<='\u0AB9')||input.LA(1)=='\u0ABD'||input.LA(1)=='\u0AE0'||(input.LA(1)>='\u0B05' && input.LA(1)<='\u0B0C')||(input.LA(1)>='\u0B0F' && input.LA(1)<='\u0B10')||(input.LA(1)>='\u0B13' && input.LA(1)<='\u0B28')||(input.LA(1)>='\u0B2A' && input.LA(1)<='\u0B30')||(input.LA(1)>='\u0B32' && input.LA(1)<='\u0B33')||(input.LA(1)>='\u0B36' && input.LA(1)<='\u0B39')||input.LA(1)=='\u0B3D'||(input.LA(1)>='\u0B5C' && input.LA(1)<='\u0B5D')||(input.LA(1)>='\u0B5F' && input.LA(1)<='\u0B61')||(input.LA(1)>='\u0B85' && input.LA(1)<='\u0B8A')||(input.LA(1)>='\u0B8E' && input.LA(1)<='\u0B90')||(input.LA(1)>='\u0B92' && input.LA(1)<='\u0B95')||(input.LA(1)>='\u0B99' && input.LA(1)<='\u0B9A')||input.LA(1)=='\u0B9C'||(input.LA(1)>='\u0B9E' && input.LA(1)<='\u0B9F')||(input.LA(1)>='\u0BA3' && input.LA(1)<='\u0BA4')||(input.LA(1)>='\u0BA8' && input.LA(1)<='\u0BAA')||(input.LA(1)>='\u0BAE' && input.LA(1)<='\u0BB5')||(input.LA(1)>='\u0BB7' && input.LA(1)<='\u0BB9')||(input.LA(1)>='\u0C05' && input.LA(1)<='\u0C0C')||(input.LA(1)>='\u0C0E' && input.LA(1)<='\u0C10')||(input.LA(1)>='\u0C12' && input.LA(1)<='\u0C28')||(input.LA(1)>='\u0C2A' && input.LA(1)<='\u0C33')||(input.LA(1)>='\u0C35' && input.LA(1)<='\u0C39')||(input.LA(1)>='\u0C60' && input.LA(1)<='\u0C61')||(input.LA(1)>='\u0C85' && input.LA(1)<='\u0C8C')||(input.LA(1)>='\u0C8E' && input.LA(1)<='\u0C90')||(input.LA(1)>='\u0C92' && input.LA(1)<='\u0CA8')||(input.LA(1)>='\u0CAA' && input.LA(1)<='\u0CB3')||(input.LA(1)>='\u0CB5' && input.LA(1)<='\u0CB9')||input.LA(1)=='\u0CDE'||(input.LA(1)>='\u0CE0' && input.LA(1)<='\u0CE1')||(input.LA(1)>='\u0D05' && input.LA(1)<='\u0D0C')||(input.LA(1)>='\u0D0E' && input.LA(1)<='\u0D10')||(input.LA(1)>='\u0D12' && input.LA(1)<='\u0D28')||(input.LA(1)>='\u0D2A' && input.LA(1)<='\u0D39')||(input.LA(1)>='\u0D60' && input.LA(1)<='\u0D61')||(input.LA(1)>='\u0E01' && input.LA(1)<='\u0E2E')||input.LA(1)=='\u0E30'||(input.LA(1)>='\u0E32' && input.LA(1)<='\u0E33')||(input.LA(1)>='\u0E40' && input.LA(1)<='\u0E45')||(input.LA(1)>='\u0E81' && input.LA(1)<='\u0E82')||input.LA(1)=='\u0E84'||(input.LA(1)>='\u0E87' && input.LA(1)<='\u0E88')||input.LA(1)=='\u0E8A'||input.LA(1)=='\u0E8D'||(input.LA(1)>='\u0E94' && input.LA(1)<='\u0E97')||(input.LA(1)>='\u0E99' && input.LA(1)<='\u0E9F')||(input.LA(1)>='\u0EA1' && input.LA(1)<='\u0EA3')||input.LA(1)=='\u0EA5'||input.LA(1)=='\u0EA7'||(input.LA(1)>='\u0EAA' && input.LA(1)<='\u0EAB')||(input.LA(1)>='\u0EAD' && input.LA(1)<='\u0EAE')||input.LA(1)=='\u0EB0'||(input.LA(1)>='\u0EB2' && input.LA(1)<='\u0EB3')||input.LA(1)=='\u0EBD'||(input.LA(1)>='\u0EC0' && input.LA(1)<='\u0EC4')||(input.LA(1)>='\u0F40' && input.LA(1)<='\u0F47')||(input.LA(1)>='\u0F49' && input.LA(1)<='\u0F69')||(input.LA(1)>='\u10A0' && input.LA(1)<='\u10C5')||(input.LA(1)>='\u10D0' && input.LA(1)<='\u10F6')||input.LA(1)=='\u1100'||(input.LA(1)>='\u1102' && input.LA(1)<='\u1103')||(input.LA(1)>='\u1105' && input.LA(1)<='\u1107')||input.LA(1)=='\u1109'||(input.LA(1)>='\u110B' && input.LA(1)<='\u110C')||(input.LA(1)>='\u110E' && input.LA(1)<='\u1112')||input.LA(1)=='\u113C'||input.LA(1)=='\u113E'||input.LA(1)=='\u1140'||input.LA(1)=='\u114C'||input.LA(1)=='\u114E'||input.LA(1)=='\u1150'||(input.LA(1)>='\u1154' && input.LA(1)<='\u1155')||input.LA(1)=='\u1159'||(input.LA(1)>='\u115F' && input.LA(1)<='\u1161')||input.LA(1)=='\u1163'||input.LA(1)=='\u1165'||input.LA(1)=='\u1167'||input.LA(1)=='\u1169'||(input.LA(1)>='\u116D' && input.LA(1)<='\u116E')||(input.LA(1)>='\u1172' && input.LA(1)<='\u1173')||input.LA(1)=='\u1175'||input.LA(1)=='\u119E'||input.LA(1)=='\u11A8'||input.LA(1)=='\u11AB'||(input.LA(1)>='\u11AE' && input.LA(1)<='\u11AF')||(input.LA(1)>='\u11B7' && input.LA(1)<='\u11B8')||input.LA(1)=='\u11BA'||(input.LA(1)>='\u11BC' && input.LA(1)<='\u11C2')||input.LA(1)=='\u11EB'||input.LA(1)=='\u11F0'||input.LA(1)=='\u11F9'||(input.LA(1)>='\u1E00' && input.LA(1)<='\u1E9B')||(input.LA(1)>='\u1EA0' && input.LA(1)<='\u1EF9')||(input.LA(1)>='\u1F00' && input.LA(1)<='\u1F15')||(input.LA(1)>='\u1F18' && input.LA(1)<='\u1F1D')||(input.LA(1)>='\u1F20' && input.LA(1)<='\u1F45')||(input.LA(1)>='\u1F48' && input.LA(1)<='\u1F4D')||(input.LA(1)>='\u1F50' && input.LA(1)<='\u1F57')||input.LA(1)=='\u1F59'||input.LA(1)=='\u1F5B'||input.LA(1)=='\u1F5D'||(input.LA(1)>='\u1F5F' && input.LA(1)<='\u1F7D')||(input.LA(1)>='\u1F80' && input.LA(1)<='\u1FB4')||(input.LA(1)>='\u1FB6' && input.LA(1)<='\u1FBC')||input.LA(1)=='\u1FBE'||(input.LA(1)>='\u1FC2' && input.LA(1)<='\u1FC4')||(input.LA(1)>='\u1FC6' && input.LA(1)<='\u1FCC')||(input.LA(1)>='\u1FD0' && input.LA(1)<='\u1FD3')||(input.LA(1)>='\u1FD6' && input.LA(1)<='\u1FDB')||(input.LA(1)>='\u1FE0' && input.LA(1)<='\u1FEC')||(input.LA(1)>='\u1FF2' && input.LA(1)<='\u1FF4')||(input.LA(1)>='\u1FF6' && input.LA(1)<='\u1FFC')||input.LA(1)=='\u2126'||(input.LA(1)>='\u212A' && input.LA(1)<='\u212B')||input.LA(1)=='\u212E'||(input.LA(1)>='\u2180' && input.LA(1)<='\u2182')||input.LA(1)=='\u3007'||(input.LA(1)>='\u3021' && input.LA(1)<='\u3029')||(input.LA(1)>='\u3041' && input.LA(1)<='\u3094')||(input.LA(1)>='\u30A1' && input.LA(1)<='\u30FA')||(input.LA(1)>='\u3105' && input.LA(1)<='\u312C')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FA5')||(input.LA(1)>='\uAC00' && input.LA(1)<='\uD7A3') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // java/com/google/gdata/model/select/parser/Selection.g:337:20: ( LETTER | NONLETTER | '.' | '-' | '_' )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>='-' && LA15_0<='.')||(LA15_0>='0' && LA15_0<='9')||(LA15_0>='A' && LA15_0<='Z')||LA15_0=='_'||(LA15_0>='a' && LA15_0<='z')||LA15_0=='\u00B7'||(LA15_0>='\u00C0' && LA15_0<='\u00D6')||(LA15_0>='\u00D8' && LA15_0<='\u00F6')||(LA15_0>='\u00F8' && LA15_0<='\u0131')||(LA15_0>='\u0134' && LA15_0<='\u013E')||(LA15_0>='\u0141' && LA15_0<='\u0148')||(LA15_0>='\u014A' && LA15_0<='\u017E')||(LA15_0>='\u0180' && LA15_0<='\u01C3')||(LA15_0>='\u01CD' && LA15_0<='\u01F0')||(LA15_0>='\u01F4' && LA15_0<='\u01F5')||(LA15_0>='\u01FA' && LA15_0<='\u0217')||(LA15_0>='\u0250' && LA15_0<='\u02A8')||(LA15_0>='\u02BB' && LA15_0<='\u02C1')||(LA15_0>='\u02D0' && LA15_0<='\u02D1')||(LA15_0>='\u0300' && LA15_0<='\u0345')||(LA15_0>='\u0360' && LA15_0<='\u0361')||(LA15_0>='\u0386' && LA15_0<='\u038A')||LA15_0=='\u038C'||(LA15_0>='\u038E' && LA15_0<='\u03A1')||(LA15_0>='\u03A3' && LA15_0<='\u03CE')||(LA15_0>='\u03D0' && LA15_0<='\u03D6')||LA15_0=='\u03DA'||LA15_0=='\u03DC'||LA15_0=='\u03DE'||LA15_0=='\u03E0'||(LA15_0>='\u03E2' && LA15_0<='\u03F3')||(LA15_0>='\u0401' && LA15_0<='\u040C')||(LA15_0>='\u040E' && LA15_0<='\u044F')||(LA15_0>='\u0451' && LA15_0<='\u045C')||(LA15_0>='\u045E' && LA15_0<='\u0481')||(LA15_0>='\u0483' && LA15_0<='\u0486')||(LA15_0>='\u0490' && LA15_0<='\u04C4')||(LA15_0>='\u04C7' && LA15_0<='\u04C8')||(LA15_0>='\u04CB' && LA15_0<='\u04CC')||(LA15_0>='\u04D0' && LA15_0<='\u04EB')||(LA15_0>='\u04EE' && LA15_0<='\u04F5')||(LA15_0>='\u04F8' && LA15_0<='\u04F9')||(LA15_0>='\u0531' && LA15_0<='\u0556')||LA15_0=='\u0559'||(LA15_0>='\u0561' && LA15_0<='\u0586')||(LA15_0>='\u0591' && LA15_0<='\u05A1')||(LA15_0>='\u05A3' && LA15_0<='\u05B9')||(LA15_0>='\u05BB' && LA15_0<='\u05BD')||LA15_0=='\u05BF'||(LA15_0>='\u05C1' && LA15_0<='\u05C2')||LA15_0=='\u05C4'||(LA15_0>='\u05D0' && LA15_0<='\u05EA')||(LA15_0>='\u05F0' && LA15_0<='\u05F2')||(LA15_0>='\u0621' && LA15_0<='\u063A')||(LA15_0>='\u0640' && LA15_0<='\u0652')||(LA15_0>='\u0660' && LA15_0<='\u0669')||(LA15_0>='\u0670' && LA15_0<='\u06B7')||(LA15_0>='\u06BA' && LA15_0<='\u06BE')||(LA15_0>='\u06C0' && LA15_0<='\u06CE')||(LA15_0>='\u06D0' && LA15_0<='\u06D3')||(LA15_0>='\u06D5' && LA15_0<='\u06E8')||(LA15_0>='\u06EA' && LA15_0<='\u06ED')||(LA15_0>='\u06F0' && LA15_0<='\u06F9')||(LA15_0>='\u0901' && LA15_0<='\u0903')||(LA15_0>='\u0905' && LA15_0<='\u0939')||(LA15_0>='\u093C' && LA15_0<='\u094D')||(LA15_0>='\u0951' && LA15_0<='\u0954')||(LA15_0>='\u0958' && LA15_0<='\u0963')||(LA15_0>='\u0966' && LA15_0<='\u096F')||(LA15_0>='\u0981' && LA15_0<='\u0983')||(LA15_0>='\u0985' && LA15_0<='\u098C')||(LA15_0>='\u098F' && LA15_0<='\u0990')||(LA15_0>='\u0993' && LA15_0<='\u09A8')||(LA15_0>='\u09AA' && LA15_0<='\u09B0')||LA15_0=='\u09B2'||(LA15_0>='\u09B6' && LA15_0<='\u09B9')||LA15_0=='\u09BC'||(LA15_0>='\u09BE' && LA15_0<='\u09C4')||(LA15_0>='\u09C7' && LA15_0<='\u09C8')||(LA15_0>='\u09CB' && LA15_0<='\u09CD')||LA15_0=='\u09D7'||(LA15_0>='\u09DC' && LA15_0<='\u09DD')||(LA15_0>='\u09DF' && LA15_0<='\u09E3')||(LA15_0>='\u09E6' && LA15_0<='\u09F1')||LA15_0=='\u0A02'||(LA15_0>='\u0A05' && LA15_0<='\u0A0A')||(LA15_0>='\u0A0F' && LA15_0<='\u0A10')||(LA15_0>='\u0A13' && LA15_0<='\u0A28')||(LA15_0>='\u0A2A' && LA15_0<='\u0A30')||(LA15_0>='\u0A32' && LA15_0<='\u0A33')||(LA15_0>='\u0A35' && LA15_0<='\u0A36')||(LA15_0>='\u0A38' && LA15_0<='\u0A39')||LA15_0=='\u0A3C'||(LA15_0>='\u0A3E' && LA15_0<='\u0A42')||(LA15_0>='\u0A47' && LA15_0<='\u0A48')||(LA15_0>='\u0A4B' && LA15_0<='\u0A4D')||(LA15_0>='\u0A59' && LA15_0<='\u0A5C')||LA15_0=='\u0A5E'||(LA15_0>='\u0A66' && LA15_0<='\u0A74')||(LA15_0>='\u0A81' && LA15_0<='\u0A83')||(LA15_0>='\u0A85' && LA15_0<='\u0A8B')||LA15_0=='\u0A8D'||(LA15_0>='\u0A8F' && LA15_0<='\u0A91')||(LA15_0>='\u0A93' && LA15_0<='\u0AA8')||(LA15_0>='\u0AAA' && LA15_0<='\u0AB0')||(LA15_0>='\u0AB2' && LA15_0<='\u0AB3')||(LA15_0>='\u0AB5' && LA15_0<='\u0AB9')||(LA15_0>='\u0ABC' && LA15_0<='\u0AC5')||(LA15_0>='\u0AC7' && LA15_0<='\u0AC9')||(LA15_0>='\u0ACB' && LA15_0<='\u0ACD')||LA15_0=='\u0AE0'||(LA15_0>='\u0AE6' && LA15_0<='\u0AEF')||(LA15_0>='\u0B01' && LA15_0<='\u0B03')||(LA15_0>='\u0B05' && LA15_0<='\u0B0C')||(LA15_0>='\u0B0F' && LA15_0<='\u0B10')||(LA15_0>='\u0B13' && LA15_0<='\u0B28')||(LA15_0>='\u0B2A' && LA15_0<='\u0B30')||(LA15_0>='\u0B32' && LA15_0<='\u0B33')||(LA15_0>='\u0B36' && LA15_0<='\u0B39')||(LA15_0>='\u0B3C' && LA15_0<='\u0B43')||(LA15_0>='\u0B47' && LA15_0<='\u0B48')||(LA15_0>='\u0B4B' && LA15_0<='\u0B4D')||(LA15_0>='\u0B56' && LA15_0<='\u0B57')||(LA15_0>='\u0B5C' && LA15_0<='\u0B5D')||(LA15_0>='\u0B5F' && LA15_0<='\u0B61')||(LA15_0>='\u0B66' && LA15_0<='\u0B6F')||(LA15_0>='\u0B82' && LA15_0<='\u0B83')||(LA15_0>='\u0B85' && LA15_0<='\u0B8A')||(LA15_0>='\u0B8E' && LA15_0<='\u0B90')||(LA15_0>='\u0B92' && LA15_0<='\u0B95')||(LA15_0>='\u0B99' && LA15_0<='\u0B9A')||LA15_0=='\u0B9C'||(LA15_0>='\u0B9E' && LA15_0<='\u0B9F')||(LA15_0>='\u0BA3' && LA15_0<='\u0BA4')||(LA15_0>='\u0BA8' && LA15_0<='\u0BAA')||(LA15_0>='\u0BAE' && LA15_0<='\u0BB5')||(LA15_0>='\u0BB7' && LA15_0<='\u0BB9')||(LA15_0>='\u0BBE' && LA15_0<='\u0BC2')||(LA15_0>='\u0BC6' && LA15_0<='\u0BC8')||(LA15_0>='\u0BCA' && LA15_0<='\u0BCD')||LA15_0=='\u0BD7'||(LA15_0>='\u0BE7' && LA15_0<='\u0BEF')||(LA15_0>='\u0C01' && LA15_0<='\u0C03')||(LA15_0>='\u0C05' && LA15_0<='\u0C0C')||(LA15_0>='\u0C0E' && LA15_0<='\u0C10')||(LA15_0>='\u0C12' && LA15_0<='\u0C28')||(LA15_0>='\u0C2A' && LA15_0<='\u0C33')||(LA15_0>='\u0C35' && LA15_0<='\u0C39')||(LA15_0>='\u0C3E' && LA15_0<='\u0C44')||(LA15_0>='\u0C46' && LA15_0<='\u0C48')||(LA15_0>='\u0C4A' && LA15_0<='\u0C4D')||(LA15_0>='\u0C55' && LA15_0<='\u0C56')||(LA15_0>='\u0C60' && LA15_0<='\u0C61')||(LA15_0>='\u0C66' && LA15_0<='\u0C6F')||(LA15_0>='\u0C82' && LA15_0<='\u0C83')||(LA15_0>='\u0C85' && LA15_0<='\u0C8C')||(LA15_0>='\u0C8E' && LA15_0<='\u0C90')||(LA15_0>='\u0C92' && LA15_0<='\u0CA8')||(LA15_0>='\u0CAA' && LA15_0<='\u0CB3')||(LA15_0>='\u0CB5' && LA15_0<='\u0CB9')||(LA15_0>='\u0CBE' && LA15_0<='\u0CC4')||(LA15_0>='\u0CC6' && LA15_0<='\u0CC8')||(LA15_0>='\u0CCA' && LA15_0<='\u0CCD')||(LA15_0>='\u0CD5' && LA15_0<='\u0CD6')||LA15_0=='\u0CDE'||(LA15_0>='\u0CE0' && LA15_0<='\u0CE1')||(LA15_0>='\u0CE6' && LA15_0<='\u0CEF')||(LA15_0>='\u0D02' && LA15_0<='\u0D03')||(LA15_0>='\u0D05' && LA15_0<='\u0D0C')||(LA15_0>='\u0D0E' && LA15_0<='\u0D10')||(LA15_0>='\u0D12' && LA15_0<='\u0D28')||(LA15_0>='\u0D2A' && LA15_0<='\u0D39')||(LA15_0>='\u0D3E' && LA15_0<='\u0D43')||(LA15_0>='\u0D46' && LA15_0<='\u0D48')||(LA15_0>='\u0D4A' && LA15_0<='\u0D4D')||LA15_0=='\u0D57'||(LA15_0>='\u0D60' && LA15_0<='\u0D61')||(LA15_0>='\u0D66' && LA15_0<='\u0D6F')||(LA15_0>='\u0E01' && LA15_0<='\u0E2E')||(LA15_0>='\u0E30' && LA15_0<='\u0E3A')||(LA15_0>='\u0E40' && LA15_0<='\u0E4E')||(LA15_0>='\u0E50' && LA15_0<='\u0E59')||(LA15_0>='\u0E81' && LA15_0<='\u0E82')||LA15_0=='\u0E84'||(LA15_0>='\u0E87' && LA15_0<='\u0E88')||LA15_0=='\u0E8A'||LA15_0=='\u0E8D'||(LA15_0>='\u0E94' && LA15_0<='\u0E97')||(LA15_0>='\u0E99' && LA15_0<='\u0E9F')||(LA15_0>='\u0EA1' && LA15_0<='\u0EA3')||LA15_0=='\u0EA5'||LA15_0=='\u0EA7'||(LA15_0>='\u0EAA' && LA15_0<='\u0EAB')||(LA15_0>='\u0EAD' && LA15_0<='\u0EAE')||(LA15_0>='\u0EB0' && LA15_0<='\u0EB9')||(LA15_0>='\u0EBB' && LA15_0<='\u0EBD')||(LA15_0>='\u0EC0' && LA15_0<='\u0EC4')||LA15_0=='\u0EC6'||(LA15_0>='\u0EC8' && LA15_0<='\u0ECD')||(LA15_0>='\u0ED0' && LA15_0<='\u0ED9')||(LA15_0>='\u0F18' && LA15_0<='\u0F19')||(LA15_0>='\u0F20' && LA15_0<='\u0F29')||LA15_0=='\u0F35'||LA15_0=='\u0F37'||LA15_0=='\u0F39'||(LA15_0>='\u0F3E' && LA15_0<='\u0F47')||(LA15_0>='\u0F49' && LA15_0<='\u0F69')||(LA15_0>='\u0F71' && LA15_0<='\u0F84')||(LA15_0>='\u0F86' && LA15_0<='\u0F8B')||(LA15_0>='\u0F90' && LA15_0<='\u0F95')||LA15_0=='\u0F97'||(LA15_0>='\u0F99' && LA15_0<='\u0FAD')||(LA15_0>='\u0FB1' && LA15_0<='\u0FB7')||LA15_0=='\u0FB9'||(LA15_0>='\u10A0' && LA15_0<='\u10C5')||(LA15_0>='\u10D0' && LA15_0<='\u10F6')||LA15_0=='\u1100'||(LA15_0>='\u1102' && LA15_0<='\u1103')||(LA15_0>='\u1105' && LA15_0<='\u1107')||LA15_0=='\u1109'||(LA15_0>='\u110B' && LA15_0<='\u110C')||(LA15_0>='\u110E' && LA15_0<='\u1112')||LA15_0=='\u113C'||LA15_0=='\u113E'||LA15_0=='\u1140'||LA15_0=='\u114C'||LA15_0=='\u114E'||LA15_0=='\u1150'||(LA15_0>='\u1154' && LA15_0<='\u1155')||LA15_0=='\u1159'||(LA15_0>='\u115F' && LA15_0<='\u1161')||LA15_0=='\u1163'||LA15_0=='\u1165'||LA15_0=='\u1167'||LA15_0=='\u1169'||(LA15_0>='\u116D' && LA15_0<='\u116E')||(LA15_0>='\u1172' && LA15_0<='\u1173')||LA15_0=='\u1175'||LA15_0=='\u119E'||LA15_0=='\u11A8'||LA15_0=='\u11AB'||(LA15_0>='\u11AE' && LA15_0<='\u11AF')||(LA15_0>='\u11B7' && LA15_0<='\u11B8')||LA15_0=='\u11BA'||(LA15_0>='\u11BC' && LA15_0<='\u11C2')||LA15_0=='\u11EB'||LA15_0=='\u11F0'||LA15_0=='\u11F9'||(LA15_0>='\u1E00' && LA15_0<='\u1E9B')||(LA15_0>='\u1EA0' && LA15_0<='\u1EF9')||(LA15_0>='\u1F00' && LA15_0<='\u1F15')||(LA15_0>='\u1F18' && LA15_0<='\u1F1D')||(LA15_0>='\u1F20' && LA15_0<='\u1F45')||(LA15_0>='\u1F48' && LA15_0<='\u1F4D')||(LA15_0>='\u1F50' && LA15_0<='\u1F57')||LA15_0=='\u1F59'||LA15_0=='\u1F5B'||LA15_0=='\u1F5D'||(LA15_0>='\u1F5F' && LA15_0<='\u1F7D')||(LA15_0>='\u1F80' && LA15_0<='\u1FB4')||(LA15_0>='\u1FB6' && LA15_0<='\u1FBC')||LA15_0=='\u1FBE'||(LA15_0>='\u1FC2' && LA15_0<='\u1FC4')||(LA15_0>='\u1FC6' && LA15_0<='\u1FCC')||(LA15_0>='\u1FD0' && LA15_0<='\u1FD3')||(LA15_0>='\u1FD6' && LA15_0<='\u1FDB')||(LA15_0>='\u1FE0' && LA15_0<='\u1FEC')||(LA15_0>='\u1FF2' && LA15_0<='\u1FF4')||(LA15_0>='\u1FF6' && LA15_0<='\u1FFC')||(LA15_0>='\u20D0' && LA15_0<='\u20DC')||LA15_0=='\u20E1'||LA15_0=='\u2126'||(LA15_0>='\u212A' && LA15_0<='\u212B')||LA15_0=='\u212E'||(LA15_0>='\u2180' && LA15_0<='\u2182')||LA15_0=='\u3005'||LA15_0=='\u3007'||(LA15_0>='\u3021' && LA15_0<='\u302F')||(LA15_0>='\u3031' && LA15_0<='\u3035')||(LA15_0>='\u3041' && LA15_0<='\u3094')||(LA15_0>='\u3099' && LA15_0<='\u309A')||(LA15_0>='\u309D' && LA15_0<='\u309E')||(LA15_0>='\u30A1' && LA15_0<='\u30FA')||(LA15_0>='\u30FC' && LA15_0<='\u30FE')||(LA15_0>='\u3105' && LA15_0<='\u312C')||(LA15_0>='\u4E00' && LA15_0<='\u9FA5')||(LA15_0>='\uAC00' && LA15_0<='\uD7A3')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:
            	    {
            	    if ( (input.LA(1)>='-' && input.LA(1)<='.')||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||input.LA(1)=='\u00B7'||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u0131')||(input.LA(1)>='\u0134' && input.LA(1)<='\u013E')||(input.LA(1)>='\u0141' && input.LA(1)<='\u0148')||(input.LA(1)>='\u014A' && input.LA(1)<='\u017E')||(input.LA(1)>='\u0180' && input.LA(1)<='\u01C3')||(input.LA(1)>='\u01CD' && input.LA(1)<='\u01F0')||(input.LA(1)>='\u01F4' && input.LA(1)<='\u01F5')||(input.LA(1)>='\u01FA' && input.LA(1)<='\u0217')||(input.LA(1)>='\u0250' && input.LA(1)<='\u02A8')||(input.LA(1)>='\u02BB' && input.LA(1)<='\u02C1')||(input.LA(1)>='\u02D0' && input.LA(1)<='\u02D1')||(input.LA(1)>='\u0300' && input.LA(1)<='\u0345')||(input.LA(1)>='\u0360' && input.LA(1)<='\u0361')||(input.LA(1)>='\u0386' && input.LA(1)<='\u038A')||input.LA(1)=='\u038C'||(input.LA(1)>='\u038E' && input.LA(1)<='\u03A1')||(input.LA(1)>='\u03A3' && input.LA(1)<='\u03CE')||(input.LA(1)>='\u03D0' && input.LA(1)<='\u03D6')||input.LA(1)=='\u03DA'||input.LA(1)=='\u03DC'||input.LA(1)=='\u03DE'||input.LA(1)=='\u03E0'||(input.LA(1)>='\u03E2' && input.LA(1)<='\u03F3')||(input.LA(1)>='\u0401' && input.LA(1)<='\u040C')||(input.LA(1)>='\u040E' && input.LA(1)<='\u044F')||(input.LA(1)>='\u0451' && input.LA(1)<='\u045C')||(input.LA(1)>='\u045E' && input.LA(1)<='\u0481')||(input.LA(1)>='\u0483' && input.LA(1)<='\u0486')||(input.LA(1)>='\u0490' && input.LA(1)<='\u04C4')||(input.LA(1)>='\u04C7' && input.LA(1)<='\u04C8')||(input.LA(1)>='\u04CB' && input.LA(1)<='\u04CC')||(input.LA(1)>='\u04D0' && input.LA(1)<='\u04EB')||(input.LA(1)>='\u04EE' && input.LA(1)<='\u04F5')||(input.LA(1)>='\u04F8' && input.LA(1)<='\u04F9')||(input.LA(1)>='\u0531' && input.LA(1)<='\u0556')||input.LA(1)=='\u0559'||(input.LA(1)>='\u0561' && input.LA(1)<='\u0586')||(input.LA(1)>='\u0591' && input.LA(1)<='\u05A1')||(input.LA(1)>='\u05A3' && input.LA(1)<='\u05B9')||(input.LA(1)>='\u05BB' && input.LA(1)<='\u05BD')||input.LA(1)=='\u05BF'||(input.LA(1)>='\u05C1' && input.LA(1)<='\u05C2')||input.LA(1)=='\u05C4'||(input.LA(1)>='\u05D0' && input.LA(1)<='\u05EA')||(input.LA(1)>='\u05F0' && input.LA(1)<='\u05F2')||(input.LA(1)>='\u0621' && input.LA(1)<='\u063A')||(input.LA(1)>='\u0640' && input.LA(1)<='\u0652')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||(input.LA(1)>='\u0670' && input.LA(1)<='\u06B7')||(input.LA(1)>='\u06BA' && input.LA(1)<='\u06BE')||(input.LA(1)>='\u06C0' && input.LA(1)<='\u06CE')||(input.LA(1)>='\u06D0' && input.LA(1)<='\u06D3')||(input.LA(1)>='\u06D5' && input.LA(1)<='\u06E8')||(input.LA(1)>='\u06EA' && input.LA(1)<='\u06ED')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0901' && input.LA(1)<='\u0903')||(input.LA(1)>='\u0905' && input.LA(1)<='\u0939')||(input.LA(1)>='\u093C' && input.LA(1)<='\u094D')||(input.LA(1)>='\u0951' && input.LA(1)<='\u0954')||(input.LA(1)>='\u0958' && input.LA(1)<='\u0963')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u0981' && input.LA(1)<='\u0983')||(input.LA(1)>='\u0985' && input.LA(1)<='\u098C')||(input.LA(1)>='\u098F' && input.LA(1)<='\u0990')||(input.LA(1)>='\u0993' && input.LA(1)<='\u09A8')||(input.LA(1)>='\u09AA' && input.LA(1)<='\u09B0')||input.LA(1)=='\u09B2'||(input.LA(1)>='\u09B6' && input.LA(1)<='\u09B9')||input.LA(1)=='\u09BC'||(input.LA(1)>='\u09BE' && input.LA(1)<='\u09C4')||(input.LA(1)>='\u09C7' && input.LA(1)<='\u09C8')||(input.LA(1)>='\u09CB' && input.LA(1)<='\u09CD')||input.LA(1)=='\u09D7'||(input.LA(1)>='\u09DC' && input.LA(1)<='\u09DD')||(input.LA(1)>='\u09DF' && input.LA(1)<='\u09E3')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09F1')||input.LA(1)=='\u0A02'||(input.LA(1)>='\u0A05' && input.LA(1)<='\u0A0A')||(input.LA(1)>='\u0A0F' && input.LA(1)<='\u0A10')||(input.LA(1)>='\u0A13' && input.LA(1)<='\u0A28')||(input.LA(1)>='\u0A2A' && input.LA(1)<='\u0A30')||(input.LA(1)>='\u0A32' && input.LA(1)<='\u0A33')||(input.LA(1)>='\u0A35' && input.LA(1)<='\u0A36')||(input.LA(1)>='\u0A38' && input.LA(1)<='\u0A39')||input.LA(1)=='\u0A3C'||(input.LA(1)>='\u0A3E' && input.LA(1)<='\u0A42')||(input.LA(1)>='\u0A47' && input.LA(1)<='\u0A48')||(input.LA(1)>='\u0A4B' && input.LA(1)<='\u0A4D')||(input.LA(1)>='\u0A59' && input.LA(1)<='\u0A5C')||input.LA(1)=='\u0A5E'||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A74')||(input.LA(1)>='\u0A81' && input.LA(1)<='\u0A83')||(input.LA(1)>='\u0A85' && input.LA(1)<='\u0A8B')||input.LA(1)=='\u0A8D'||(input.LA(1)>='\u0A8F' && input.LA(1)<='\u0A91')||(input.LA(1)>='\u0A93' && input.LA(1)<='\u0AA8')||(input.LA(1)>='\u0AAA' && input.LA(1)<='\u0AB0')||(input.LA(1)>='\u0AB2' && input.LA(1)<='\u0AB3')||(input.LA(1)>='\u0AB5' && input.LA(1)<='\u0AB9')||(input.LA(1)>='\u0ABC' && input.LA(1)<='\u0AC5')||(input.LA(1)>='\u0AC7' && input.LA(1)<='\u0AC9')||(input.LA(1)>='\u0ACB' && input.LA(1)<='\u0ACD')||input.LA(1)=='\u0AE0'||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B01' && input.LA(1)<='\u0B03')||(input.LA(1)>='\u0B05' && input.LA(1)<='\u0B0C')||(input.LA(1)>='\u0B0F' && input.LA(1)<='\u0B10')||(input.LA(1)>='\u0B13' && input.LA(1)<='\u0B28')||(input.LA(1)>='\u0B2A' && input.LA(1)<='\u0B30')||(input.LA(1)>='\u0B32' && input.LA(1)<='\u0B33')||(input.LA(1)>='\u0B36' && input.LA(1)<='\u0B39')||(input.LA(1)>='\u0B3C' && input.LA(1)<='\u0B43')||(input.LA(1)>='\u0B47' && input.LA(1)<='\u0B48')||(input.LA(1)>='\u0B4B' && input.LA(1)<='\u0B4D')||(input.LA(1)>='\u0B56' && input.LA(1)<='\u0B57')||(input.LA(1)>='\u0B5C' && input.LA(1)<='\u0B5D')||(input.LA(1)>='\u0B5F' && input.LA(1)<='\u0B61')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0B82' && input.LA(1)<='\u0B83')||(input.LA(1)>='\u0B85' && input.LA(1)<='\u0B8A')||(input.LA(1)>='\u0B8E' && input.LA(1)<='\u0B90')||(input.LA(1)>='\u0B92' && input.LA(1)<='\u0B95')||(input.LA(1)>='\u0B99' && input.LA(1)<='\u0B9A')||input.LA(1)=='\u0B9C'||(input.LA(1)>='\u0B9E' && input.LA(1)<='\u0B9F')||(input.LA(1)>='\u0BA3' && input.LA(1)<='\u0BA4')||(input.LA(1)>='\u0BA8' && input.LA(1)<='\u0BAA')||(input.LA(1)>='\u0BAE' && input.LA(1)<='\u0BB5')||(input.LA(1)>='\u0BB7' && input.LA(1)<='\u0BB9')||(input.LA(1)>='\u0BBE' && input.LA(1)<='\u0BC2')||(input.LA(1)>='\u0BC6' && input.LA(1)<='\u0BC8')||(input.LA(1)>='\u0BCA' && input.LA(1)<='\u0BCD')||input.LA(1)=='\u0BD7'||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C01' && input.LA(1)<='\u0C03')||(input.LA(1)>='\u0C05' && input.LA(1)<='\u0C0C')||(input.LA(1)>='\u0C0E' && input.LA(1)<='\u0C10')||(input.LA(1)>='\u0C12' && input.LA(1)<='\u0C28')||(input.LA(1)>='\u0C2A' && input.LA(1)<='\u0C33')||(input.LA(1)>='\u0C35' && input.LA(1)<='\u0C39')||(input.LA(1)>='\u0C3E' && input.LA(1)<='\u0C44')||(input.LA(1)>='\u0C46' && input.LA(1)<='\u0C48')||(input.LA(1)>='\u0C4A' && input.LA(1)<='\u0C4D')||(input.LA(1)>='\u0C55' && input.LA(1)<='\u0C56')||(input.LA(1)>='\u0C60' && input.LA(1)<='\u0C61')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0C82' && input.LA(1)<='\u0C83')||(input.LA(1)>='\u0C85' && input.LA(1)<='\u0C8C')||(input.LA(1)>='\u0C8E' && input.LA(1)<='\u0C90')||(input.LA(1)>='\u0C92' && input.LA(1)<='\u0CA8')||(input.LA(1)>='\u0CAA' && input.LA(1)<='\u0CB3')||(input.LA(1)>='\u0CB5' && input.LA(1)<='\u0CB9')||(input.LA(1)>='\u0CBE' && input.LA(1)<='\u0CC4')||(input.LA(1)>='\u0CC6' && input.LA(1)<='\u0CC8')||(input.LA(1)>='\u0CCA' && input.LA(1)<='\u0CCD')||(input.LA(1)>='\u0CD5' && input.LA(1)<='\u0CD6')||input.LA(1)=='\u0CDE'||(input.LA(1)>='\u0CE0' && input.LA(1)<='\u0CE1')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D02' && input.LA(1)<='\u0D03')||(input.LA(1)>='\u0D05' && input.LA(1)<='\u0D0C')||(input.LA(1)>='\u0D0E' && input.LA(1)<='\u0D10')||(input.LA(1)>='\u0D12' && input.LA(1)<='\u0D28')||(input.LA(1)>='\u0D2A' && input.LA(1)<='\u0D39')||(input.LA(1)>='\u0D3E' && input.LA(1)<='\u0D43')||(input.LA(1)>='\u0D46' && input.LA(1)<='\u0D48')||(input.LA(1)>='\u0D4A' && input.LA(1)<='\u0D4D')||input.LA(1)=='\u0D57'||(input.LA(1)>='\u0D60' && input.LA(1)<='\u0D61')||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||(input.LA(1)>='\u0E01' && input.LA(1)<='\u0E2E')||(input.LA(1)>='\u0E30' && input.LA(1)<='\u0E3A')||(input.LA(1)>='\u0E40' && input.LA(1)<='\u0E4E')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||(input.LA(1)>='\u0E81' && input.LA(1)<='\u0E82')||input.LA(1)=='\u0E84'||(input.LA(1)>='\u0E87' && input.LA(1)<='\u0E88')||input.LA(1)=='\u0E8A'||input.LA(1)=='\u0E8D'||(input.LA(1)>='\u0E94' && input.LA(1)<='\u0E97')||(input.LA(1)>='\u0E99' && input.LA(1)<='\u0E9F')||(input.LA(1)>='\u0EA1' && input.LA(1)<='\u0EA3')||input.LA(1)=='\u0EA5'||input.LA(1)=='\u0EA7'||(input.LA(1)>='\u0EAA' && input.LA(1)<='\u0EAB')||(input.LA(1)>='\u0EAD' && input.LA(1)<='\u0EAE')||(input.LA(1)>='\u0EB0' && input.LA(1)<='\u0EB9')||(input.LA(1)>='\u0EBB' && input.LA(1)<='\u0EBD')||(input.LA(1)>='\u0EC0' && input.LA(1)<='\u0EC4')||input.LA(1)=='\u0EC6'||(input.LA(1)>='\u0EC8' && input.LA(1)<='\u0ECD')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u0F18' && input.LA(1)<='\u0F19')||(input.LA(1)>='\u0F20' && input.LA(1)<='\u0F29')||input.LA(1)=='\u0F35'||input.LA(1)=='\u0F37'||input.LA(1)=='\u0F39'||(input.LA(1)>='\u0F3E' && input.LA(1)<='\u0F47')||(input.LA(1)>='\u0F49' && input.LA(1)<='\u0F69')||(input.LA(1)>='\u0F71' && input.LA(1)<='\u0F84')||(input.LA(1)>='\u0F86' && input.LA(1)<='\u0F8B')||(input.LA(1)>='\u0F90' && input.LA(1)<='\u0F95')||input.LA(1)=='\u0F97'||(input.LA(1)>='\u0F99' && input.LA(1)<='\u0FAD')||(input.LA(1)>='\u0FB1' && input.LA(1)<='\u0FB7')||input.LA(1)=='\u0FB9'||(input.LA(1)>='\u10A0' && input.LA(1)<='\u10C5')||(input.LA(1)>='\u10D0' && input.LA(1)<='\u10F6')||input.LA(1)=='\u1100'||(input.LA(1)>='\u1102' && input.LA(1)<='\u1103')||(input.LA(1)>='\u1105' && input.LA(1)<='\u1107')||input.LA(1)=='\u1109'||(input.LA(1)>='\u110B' && input.LA(1)<='\u110C')||(input.LA(1)>='\u110E' && input.LA(1)<='\u1112')||input.LA(1)=='\u113C'||input.LA(1)=='\u113E'||input.LA(1)=='\u1140'||input.LA(1)=='\u114C'||input.LA(1)=='\u114E'||input.LA(1)=='\u1150'||(input.LA(1)>='\u1154' && input.LA(1)<='\u1155')||input.LA(1)=='\u1159'||(input.LA(1)>='\u115F' && input.LA(1)<='\u1161')||input.LA(1)=='\u1163'||input.LA(1)=='\u1165'||input.LA(1)=='\u1167'||input.LA(1)=='\u1169'||(input.LA(1)>='\u116D' && input.LA(1)<='\u116E')||(input.LA(1)>='\u1172' && input.LA(1)<='\u1173')||input.LA(1)=='\u1175'||input.LA(1)=='\u119E'||input.LA(1)=='\u11A8'||input.LA(1)=='\u11AB'||(input.LA(1)>='\u11AE' && input.LA(1)<='\u11AF')||(input.LA(1)>='\u11B7' && input.LA(1)<='\u11B8')||input.LA(1)=='\u11BA'||(input.LA(1)>='\u11BC' && input.LA(1)<='\u11C2')||input.LA(1)=='\u11EB'||input.LA(1)=='\u11F0'||input.LA(1)=='\u11F9'||(input.LA(1)>='\u1E00' && input.LA(1)<='\u1E9B')||(input.LA(1)>='\u1EA0' && input.LA(1)<='\u1EF9')||(input.LA(1)>='\u1F00' && input.LA(1)<='\u1F15')||(input.LA(1)>='\u1F18' && input.LA(1)<='\u1F1D')||(input.LA(1)>='\u1F20' && input.LA(1)<='\u1F45')||(input.LA(1)>='\u1F48' && input.LA(1)<='\u1F4D')||(input.LA(1)>='\u1F50' && input.LA(1)<='\u1F57')||input.LA(1)=='\u1F59'||input.LA(1)=='\u1F5B'||input.LA(1)=='\u1F5D'||(input.LA(1)>='\u1F5F' && input.LA(1)<='\u1F7D')||(input.LA(1)>='\u1F80' && input.LA(1)<='\u1FB4')||(input.LA(1)>='\u1FB6' && input.LA(1)<='\u1FBC')||input.LA(1)=='\u1FBE'||(input.LA(1)>='\u1FC2' && input.LA(1)<='\u1FC4')||(input.LA(1)>='\u1FC6' && input.LA(1)<='\u1FCC')||(input.LA(1)>='\u1FD0' && input.LA(1)<='\u1FD3')||(input.LA(1)>='\u1FD6' && input.LA(1)<='\u1FDB')||(input.LA(1)>='\u1FE0' && input.LA(1)<='\u1FEC')||(input.LA(1)>='\u1FF2' && input.LA(1)<='\u1FF4')||(input.LA(1)>='\u1FF6' && input.LA(1)<='\u1FFC')||(input.LA(1)>='\u20D0' && input.LA(1)<='\u20DC')||input.LA(1)=='\u20E1'||input.LA(1)=='\u2126'||(input.LA(1)>='\u212A' && input.LA(1)<='\u212B')||input.LA(1)=='\u212E'||(input.LA(1)>='\u2180' && input.LA(1)<='\u2182')||input.LA(1)=='\u3005'||input.LA(1)=='\u3007'||(input.LA(1)>='\u3021' && input.LA(1)<='\u302F')||(input.LA(1)>='\u3031' && input.LA(1)<='\u3035')||(input.LA(1)>='\u3041' && input.LA(1)<='\u3094')||(input.LA(1)>='\u3099' && input.LA(1)<='\u309A')||(input.LA(1)>='\u309D' && input.LA(1)<='\u309E')||(input.LA(1)>='\u30A1' && input.LA(1)<='\u30FA')||(input.LA(1)>='\u30FC' && input.LA(1)<='\u30FE')||(input.LA(1)>='\u3105' && input.LA(1)<='\u312C')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FA5')||(input.LA(1)>='\uAC00' && input.LA(1)<='\uD7A3') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NCNAME"

    // $ANTLR start "NONLETTER"
    public final void mNONLETTER() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:341:3: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06F0' .. '\\u06F9' | '\\u0966' .. '\\u096F' | '\\u09E6' .. '\\u09EF' | '\\u0A66' .. '\\u0A6F' | '\\u0AE6' .. '\\u0AEF' | '\\u0B66' .. '\\u0B6F' | '\\u0BE7' .. '\\u0BEF' | '\\u0C66' .. '\\u0C6F' | '\\u0CE6' .. '\\u0CEF' | '\\u0D66' .. '\\u0D6F' | '\\u0E50' .. '\\u0E59' | '\\u0ED0' .. '\\u0ED9' | '\\u0F20' .. '\\u0F29' | '\\u0300' .. '\\u0345' | '\\u0360' .. '\\u0361' | '\\u0483' .. '\\u0486' | '\\u0591' .. '\\u05A1' | '\\u05A3' .. '\\u05B9' | '\\u05BB' .. '\\u05BD' | '\\u05BF' | '\\u05C1' .. '\\u05C2' | '\\u05C4' | '\\u064B' .. '\\u0652' | '\\u0670' | '\\u06D6' .. '\\u06DC' | '\\u06DD' .. '\\u06DF' | '\\u06E0' .. '\\u06E4' | '\\u06E7' .. '\\u06E8' | '\\u06EA' .. '\\u06ED' | '\\u0901' .. '\\u0903' | '\\u093C' | '\\u093E' .. '\\u094C' | '\\u094D' | '\\u0951' .. '\\u0954' | '\\u0962' .. '\\u0963' | '\\u0981' .. '\\u0983' | '\\u09BC' | '\\u09BE' | '\\u09BF' | '\\u09C0' .. '\\u09C4' | '\\u09C7' .. '\\u09C8' | '\\u09CB' .. '\\u09CD' | '\\u09D7' | '\\u09E2' .. '\\u09E3' | '\\u0A02' | '\\u0A3C' | '\\u0A3E' | '\\u0A3F' | '\\u0A40' .. '\\u0A42' | '\\u0A47' .. '\\u0A48' | '\\u0A4B' .. '\\u0A4D' | '\\u0A70' .. '\\u0A71' | '\\u0A81' .. '\\u0A83' | '\\u0ABC' | '\\u0ABE' .. '\\u0AC5' | '\\u0AC7' .. '\\u0AC9' | '\\u0ACB' .. '\\u0ACD' | '\\u0B01' .. '\\u0B03' | '\\u0B3C' | '\\u0B3E' .. '\\u0B43' | '\\u0B47' .. '\\u0B48' | '\\u0B4B' .. '\\u0B4D' | '\\u0B56' .. '\\u0B57' | '\\u0B82' .. '\\u0B83' | '\\u0BBE' .. '\\u0BC2' | '\\u0BC6' .. '\\u0BC8' | '\\u0BCA' .. '\\u0BCD' | '\\u0BD7' | '\\u0C01' .. '\\u0C03' | '\\u0C3E' .. '\\u0C44' | '\\u0C46' .. '\\u0C48' | '\\u0C4A' .. '\\u0C4D' | '\\u0C55' .. '\\u0C56' | '\\u0C82' .. '\\u0C83' | '\\u0CBE' .. '\\u0CC4' | '\\u0CC6' .. '\\u0CC8' | '\\u0CCA' .. '\\u0CCD' | '\\u0CD5' .. '\\u0CD6' | '\\u0D02' .. '\\u0D03' | '\\u0D3E' .. '\\u0D43' | '\\u0D46' .. '\\u0D48' | '\\u0D4A' .. '\\u0D4D' | '\\u0D57' | '\\u0E31' | '\\u0E34' .. '\\u0E3A' | '\\u0E47' .. '\\u0E4E' | '\\u0EB1' | '\\u0EB4' .. '\\u0EB9' | '\\u0EBB' .. '\\u0EBC' | '\\u0EC8' .. '\\u0ECD' | '\\u0F18' .. '\\u0F19' | '\\u0F35' | '\\u0F37' | '\\u0F39' | '\\u0F3E' | '\\u0F3F' | '\\u0F71' .. '\\u0F84' | '\\u0F86' .. '\\u0F8B' | '\\u0F90' .. '\\u0F95' | '\\u0F97' | '\\u0F99' .. '\\u0FAD' | '\\u0FB1' .. '\\u0FB7' | '\\u0FB9' | '\\u20D0' .. '\\u20DC' | '\\u20E1' | '\\u302A' .. '\\u302F' | '\\u3099' | '\\u309A' | '\\u00B7' | '\\u02D0' | '\\u02D1' | '\\u0387' | '\\u0640' | '\\u0E46' | '\\u0EC6' | '\\u3005' | '\\u3031' .. '\\u3035' | '\\u309D' .. '\\u309E' | '\\u30FC' .. '\\u30FE' )
            // java/com/google/gdata/model/select/parser/Selection.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||input.LA(1)=='\u00B7'||(input.LA(1)>='\u02D0' && input.LA(1)<='\u02D1')||(input.LA(1)>='\u0300' && input.LA(1)<='\u0345')||(input.LA(1)>='\u0360' && input.LA(1)<='\u0361')||input.LA(1)=='\u0387'||(input.LA(1)>='\u0483' && input.LA(1)<='\u0486')||(input.LA(1)>='\u0591' && input.LA(1)<='\u05A1')||(input.LA(1)>='\u05A3' && input.LA(1)<='\u05B9')||(input.LA(1)>='\u05BB' && input.LA(1)<='\u05BD')||input.LA(1)=='\u05BF'||(input.LA(1)>='\u05C1' && input.LA(1)<='\u05C2')||input.LA(1)=='\u05C4'||input.LA(1)=='\u0640'||(input.LA(1)>='\u064B' && input.LA(1)<='\u0652')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||input.LA(1)=='\u0670'||(input.LA(1)>='\u06D6' && input.LA(1)<='\u06E4')||(input.LA(1)>='\u06E7' && input.LA(1)<='\u06E8')||(input.LA(1)>='\u06EA' && input.LA(1)<='\u06ED')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0901' && input.LA(1)<='\u0903')||input.LA(1)=='\u093C'||(input.LA(1)>='\u093E' && input.LA(1)<='\u094D')||(input.LA(1)>='\u0951' && input.LA(1)<='\u0954')||(input.LA(1)>='\u0962' && input.LA(1)<='\u0963')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u0981' && input.LA(1)<='\u0983')||input.LA(1)=='\u09BC'||(input.LA(1)>='\u09BE' && input.LA(1)<='\u09C4')||(input.LA(1)>='\u09C7' && input.LA(1)<='\u09C8')||(input.LA(1)>='\u09CB' && input.LA(1)<='\u09CD')||input.LA(1)=='\u09D7'||(input.LA(1)>='\u09E2' && input.LA(1)<='\u09E3')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09EF')||input.LA(1)=='\u0A02'||input.LA(1)=='\u0A3C'||(input.LA(1)>='\u0A3E' && input.LA(1)<='\u0A42')||(input.LA(1)>='\u0A47' && input.LA(1)<='\u0A48')||(input.LA(1)>='\u0A4B' && input.LA(1)<='\u0A4D')||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A71')||(input.LA(1)>='\u0A81' && input.LA(1)<='\u0A83')||input.LA(1)=='\u0ABC'||(input.LA(1)>='\u0ABE' && input.LA(1)<='\u0AC5')||(input.LA(1)>='\u0AC7' && input.LA(1)<='\u0AC9')||(input.LA(1)>='\u0ACB' && input.LA(1)<='\u0ACD')||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B01' && input.LA(1)<='\u0B03')||input.LA(1)=='\u0B3C'||(input.LA(1)>='\u0B3E' && input.LA(1)<='\u0B43')||(input.LA(1)>='\u0B47' && input.LA(1)<='\u0B48')||(input.LA(1)>='\u0B4B' && input.LA(1)<='\u0B4D')||(input.LA(1)>='\u0B56' && input.LA(1)<='\u0B57')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0B82' && input.LA(1)<='\u0B83')||(input.LA(1)>='\u0BBE' && input.LA(1)<='\u0BC2')||(input.LA(1)>='\u0BC6' && input.LA(1)<='\u0BC8')||(input.LA(1)>='\u0BCA' && input.LA(1)<='\u0BCD')||input.LA(1)=='\u0BD7'||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C01' && input.LA(1)<='\u0C03')||(input.LA(1)>='\u0C3E' && input.LA(1)<='\u0C44')||(input.LA(1)>='\u0C46' && input.LA(1)<='\u0C48')||(input.LA(1)>='\u0C4A' && input.LA(1)<='\u0C4D')||(input.LA(1)>='\u0C55' && input.LA(1)<='\u0C56')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0C82' && input.LA(1)<='\u0C83')||(input.LA(1)>='\u0CBE' && input.LA(1)<='\u0CC4')||(input.LA(1)>='\u0CC6' && input.LA(1)<='\u0CC8')||(input.LA(1)>='\u0CCA' && input.LA(1)<='\u0CCD')||(input.LA(1)>='\u0CD5' && input.LA(1)<='\u0CD6')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D02' && input.LA(1)<='\u0D03')||(input.LA(1)>='\u0D3E' && input.LA(1)<='\u0D43')||(input.LA(1)>='\u0D46' && input.LA(1)<='\u0D48')||(input.LA(1)>='\u0D4A' && input.LA(1)<='\u0D4D')||input.LA(1)=='\u0D57'||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||input.LA(1)=='\u0E31'||(input.LA(1)>='\u0E34' && input.LA(1)<='\u0E3A')||(input.LA(1)>='\u0E46' && input.LA(1)<='\u0E4E')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||input.LA(1)=='\u0EB1'||(input.LA(1)>='\u0EB4' && input.LA(1)<='\u0EB9')||(input.LA(1)>='\u0EBB' && input.LA(1)<='\u0EBC')||input.LA(1)=='\u0EC6'||(input.LA(1)>='\u0EC8' && input.LA(1)<='\u0ECD')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u0F18' && input.LA(1)<='\u0F19')||(input.LA(1)>='\u0F20' && input.LA(1)<='\u0F29')||input.LA(1)=='\u0F35'||input.LA(1)=='\u0F37'||input.LA(1)=='\u0F39'||(input.LA(1)>='\u0F3E' && input.LA(1)<='\u0F3F')||(input.LA(1)>='\u0F71' && input.LA(1)<='\u0F84')||(input.LA(1)>='\u0F86' && input.LA(1)<='\u0F8B')||(input.LA(1)>='\u0F90' && input.LA(1)<='\u0F95')||input.LA(1)=='\u0F97'||(input.LA(1)>='\u0F99' && input.LA(1)<='\u0FAD')||(input.LA(1)>='\u0FB1' && input.LA(1)<='\u0FB7')||input.LA(1)=='\u0FB9'||(input.LA(1)>='\u20D0' && input.LA(1)<='\u20DC')||input.LA(1)=='\u20E1'||input.LA(1)=='\u3005'||(input.LA(1)>='\u302A' && input.LA(1)<='\u302F')||(input.LA(1)>='\u3031' && input.LA(1)<='\u3035')||(input.LA(1)>='\u3099' && input.LA(1)<='\u309A')||(input.LA(1)>='\u309D' && input.LA(1)<='\u309E')||(input.LA(1)>='\u30FC' && input.LA(1)<='\u30FE') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "NONLETTER"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:379:3: ( '\\u0041' .. '\\u005A' | '\\u0061' .. '\\u007A' | '\\u00C0' .. '\\u00D6' | '\\u00D8' .. '\\u00F6' | '\\u00F8' .. '\\u00FF' | '\\u0100' .. '\\u0131' | '\\u0134' .. '\\u013E' | '\\u0141' .. '\\u0148' | '\\u014A' .. '\\u017E' | '\\u0180' .. '\\u01C3' | '\\u01CD' .. '\\u01F0' | '\\u01F4' .. '\\u01F5' | '\\u01FA' .. '\\u0217' | '\\u0250' .. '\\u02A8' | '\\u02BB' .. '\\u02C1' | '\\u0386' | '\\u0388' .. '\\u038A' | '\\u038C' | '\\u038E' .. '\\u03A1' | '\\u03A3' .. '\\u03CE' | '\\u03D0' .. '\\u03D6' | '\\u03DA' | '\\u03DC' | '\\u03DE' | '\\u03E0' | '\\u03E2' .. '\\u03F3' | '\\u0401' .. '\\u040C' | '\\u040E' .. '\\u044F' | '\\u0451' .. '\\u045C' | '\\u045E' .. '\\u0481' | '\\u0490' .. '\\u04C4' | '\\u04C7' .. '\\u04C8' | '\\u04CB' .. '\\u04CC' | '\\u04D0' .. '\\u04EB' | '\\u04EE' .. '\\u04F5' | '\\u04F8' .. '\\u04F9' | '\\u0531' .. '\\u0556' | '\\u0559' | '\\u0561' .. '\\u0586' | '\\u05D0' .. '\\u05EA' | '\\u05F0' .. '\\u05F2' | '\\u0621' .. '\\u063A' | '\\u0641' .. '\\u064A' | '\\u0671' .. '\\u06B7' | '\\u06BA' .. '\\u06BE' | '\\u06C0' .. '\\u06CE' | '\\u06D0' .. '\\u06D3' | '\\u06D5' | '\\u06E5' .. '\\u06E6' | '\\u0905' .. '\\u0939' | '\\u093D' | '\\u0958' .. '\\u0961' | '\\u0985' .. '\\u098C' | '\\u098F' .. '\\u0990' | '\\u0993' .. '\\u09A8' | '\\u09AA' .. '\\u09B0' | '\\u09B2' | '\\u09B6' .. '\\u09B9' | '\\u09DC' .. '\\u09DD' | '\\u09DF' .. '\\u09E1' | '\\u09F0' .. '\\u09F1' | '\\u0A05' .. '\\u0A0A' | '\\u0A0F' .. '\\u0A10' | '\\u0A13' .. '\\u0A28' | '\\u0A2A' .. '\\u0A30' | '\\u0A32' .. '\\u0A33' | '\\u0A35' .. '\\u0A36' | '\\u0A38' .. '\\u0A39' | '\\u0A59' .. '\\u0A5C' | '\\u0A5E' | '\\u0A72' .. '\\u0A74' | '\\u0A85' .. '\\u0A8B' | '\\u0A8D' | '\\u0A8F' .. '\\u0A91' | '\\u0A93' .. '\\u0AA8' | '\\u0AAA' .. '\\u0AB0' | '\\u0AB2' .. '\\u0AB3' | '\\u0AB5' .. '\\u0AB9' | '\\u0ABD' | '\\u0AE0' | '\\u0B05' .. '\\u0B0C' | '\\u0B0F' .. '\\u0B10' | '\\u0B13' .. '\\u0B28' | '\\u0B2A' .. '\\u0B30' | '\\u0B32' .. '\\u0B33' | '\\u0B36' .. '\\u0B39' | '\\u0B3D' | '\\u0B5C' .. '\\u0B5D' | '\\u0B5F' .. '\\u0B61' | '\\u0B85' .. '\\u0B8A' | '\\u0B8E' .. '\\u0B90' | '\\u0B92' .. '\\u0B95' | '\\u0B99' .. '\\u0B9A' | '\\u0B9C' | '\\u0B9E' .. '\\u0B9F' | '\\u0BA3' .. '\\u0BA4' | '\\u0BA8' .. '\\u0BAA' | '\\u0BAE' .. '\\u0BB5' | '\\u0BB7' .. '\\u0BB9' | '\\u0C05' .. '\\u0C0C' | '\\u0C0E' .. '\\u0C10' | '\\u0C12' .. '\\u0C28' | '\\u0C2A' .. '\\u0C33' | '\\u0C35' .. '\\u0C39' | '\\u0C60' .. '\\u0C61' | '\\u0C85' .. '\\u0C8C' | '\\u0C8E' .. '\\u0C90' | '\\u0C92' .. '\\u0CA8' | '\\u0CAA' .. '\\u0CB3' | '\\u0CB5' .. '\\u0CB9' | '\\u0CDE' | '\\u0CE0' .. '\\u0CE1' | '\\u0D05' .. '\\u0D0C' | '\\u0D0E' .. '\\u0D10' | '\\u0D12' .. '\\u0D28' | '\\u0D2A' .. '\\u0D39' | '\\u0D60' .. '\\u0D61' | '\\u0E01' .. '\\u0E2E' | '\\u0E30' | '\\u0E32' .. '\\u0E33' | '\\u0E40' .. '\\u0E45' | '\\u0E81' .. '\\u0E82' | '\\u0E84' | '\\u0E87' .. '\\u0E88' | '\\u0E8A' | '\\u0E8D' | '\\u0E94' .. '\\u0E97' | '\\u0E99' .. '\\u0E9F' | '\\u0EA1' .. '\\u0EA3' | '\\u0EA5' | '\\u0EA7' | '\\u0EAA' .. '\\u0EAB' | '\\u0EAD' .. '\\u0EAE' | '\\u0EB0' | '\\u0EB2' .. '\\u0EB3' | '\\u0EBD' | '\\u0EC0' .. '\\u0EC4' | '\\u0F40' .. '\\u0F47' | '\\u0F49' .. '\\u0F69' | '\\u10A0' .. '\\u10C5' | '\\u10D0' .. '\\u10F6' | '\\u1100' | '\\u1102' .. '\\u1103' | '\\u1105' .. '\\u1107' | '\\u1109' | '\\u110B' .. '\\u110C' | '\\u110E' .. '\\u1112' | '\\u113C' | '\\u113E' | '\\u1140' | '\\u114C' | '\\u114E' | '\\u1150' | '\\u1154' .. '\\u1155' | '\\u1159' | '\\u115F' .. '\\u1161' | '\\u1163' | '\\u1165' | '\\u1167' | '\\u1169' | '\\u116D' .. '\\u116E' | '\\u1172' .. '\\u1173' | '\\u1175' | '\\u119E' | '\\u11A8' | '\\u11AB' | '\\u11AE' .. '\\u11AF' | '\\u11B7' .. '\\u11B8' | '\\u11BA' | '\\u11BC' .. '\\u11C2' | '\\u11EB' | '\\u11F0' | '\\u11F9' | '\\u1E00' .. '\\u1E9B' | '\\u1EA0' .. '\\u1EF9' | '\\u1F00' .. '\\u1F15' | '\\u1F18' .. '\\u1F1D' | '\\u1F20' .. '\\u1F45' | '\\u1F48' .. '\\u1F4D' | '\\u1F50' .. '\\u1F57' | '\\u1F59' | '\\u1F5B' | '\\u1F5D' | '\\u1F5F' .. '\\u1F7D' | '\\u1F80' .. '\\u1FB4' | '\\u1FB6' .. '\\u1FBC' | '\\u1FBE' | '\\u1FC2' .. '\\u1FC4' | '\\u1FC6' .. '\\u1FCC' | '\\u1FD0' .. '\\u1FD3' | '\\u1FD6' .. '\\u1FDB' | '\\u1FE0' .. '\\u1FEC' | '\\u1FF2' .. '\\u1FF4' | '\\u1FF6' .. '\\u1FFC' | '\\u2126' | '\\u212A' .. '\\u212B' | '\\u212E' | '\\u2180' .. '\\u2182' | '\\u3041' .. '\\u3094' | '\\u30A1' .. '\\u30FA' | '\\u3105' .. '\\u312C' | '\\uAC00' .. '\\uD7A3' | '\\u4E00' .. '\\u9FA5' | '\\u3007' | '\\u3021' .. '\\u3029' )
            // java/com/google/gdata/model/select/parser/Selection.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u0131')||(input.LA(1)>='\u0134' && input.LA(1)<='\u013E')||(input.LA(1)>='\u0141' && input.LA(1)<='\u0148')||(input.LA(1)>='\u014A' && input.LA(1)<='\u017E')||(input.LA(1)>='\u0180' && input.LA(1)<='\u01C3')||(input.LA(1)>='\u01CD' && input.LA(1)<='\u01F0')||(input.LA(1)>='\u01F4' && input.LA(1)<='\u01F5')||(input.LA(1)>='\u01FA' && input.LA(1)<='\u0217')||(input.LA(1)>='\u0250' && input.LA(1)<='\u02A8')||(input.LA(1)>='\u02BB' && input.LA(1)<='\u02C1')||input.LA(1)=='\u0386'||(input.LA(1)>='\u0388' && input.LA(1)<='\u038A')||input.LA(1)=='\u038C'||(input.LA(1)>='\u038E' && input.LA(1)<='\u03A1')||(input.LA(1)>='\u03A3' && input.LA(1)<='\u03CE')||(input.LA(1)>='\u03D0' && input.LA(1)<='\u03D6')||input.LA(1)=='\u03DA'||input.LA(1)=='\u03DC'||input.LA(1)=='\u03DE'||input.LA(1)=='\u03E0'||(input.LA(1)>='\u03E2' && input.LA(1)<='\u03F3')||(input.LA(1)>='\u0401' && input.LA(1)<='\u040C')||(input.LA(1)>='\u040E' && input.LA(1)<='\u044F')||(input.LA(1)>='\u0451' && input.LA(1)<='\u045C')||(input.LA(1)>='\u045E' && input.LA(1)<='\u0481')||(input.LA(1)>='\u0490' && input.LA(1)<='\u04C4')||(input.LA(1)>='\u04C7' && input.LA(1)<='\u04C8')||(input.LA(1)>='\u04CB' && input.LA(1)<='\u04CC')||(input.LA(1)>='\u04D0' && input.LA(1)<='\u04EB')||(input.LA(1)>='\u04EE' && input.LA(1)<='\u04F5')||(input.LA(1)>='\u04F8' && input.LA(1)<='\u04F9')||(input.LA(1)>='\u0531' && input.LA(1)<='\u0556')||input.LA(1)=='\u0559'||(input.LA(1)>='\u0561' && input.LA(1)<='\u0586')||(input.LA(1)>='\u05D0' && input.LA(1)<='\u05EA')||(input.LA(1)>='\u05F0' && input.LA(1)<='\u05F2')||(input.LA(1)>='\u0621' && input.LA(1)<='\u063A')||(input.LA(1)>='\u0641' && input.LA(1)<='\u064A')||(input.LA(1)>='\u0671' && input.LA(1)<='\u06B7')||(input.LA(1)>='\u06BA' && input.LA(1)<='\u06BE')||(input.LA(1)>='\u06C0' && input.LA(1)<='\u06CE')||(input.LA(1)>='\u06D0' && input.LA(1)<='\u06D3')||input.LA(1)=='\u06D5'||(input.LA(1)>='\u06E5' && input.LA(1)<='\u06E6')||(input.LA(1)>='\u0905' && input.LA(1)<='\u0939')||input.LA(1)=='\u093D'||(input.LA(1)>='\u0958' && input.LA(1)<='\u0961')||(input.LA(1)>='\u0985' && input.LA(1)<='\u098C')||(input.LA(1)>='\u098F' && input.LA(1)<='\u0990')||(input.LA(1)>='\u0993' && input.LA(1)<='\u09A8')||(input.LA(1)>='\u09AA' && input.LA(1)<='\u09B0')||input.LA(1)=='\u09B2'||(input.LA(1)>='\u09B6' && input.LA(1)<='\u09B9')||(input.LA(1)>='\u09DC' && input.LA(1)<='\u09DD')||(input.LA(1)>='\u09DF' && input.LA(1)<='\u09E1')||(input.LA(1)>='\u09F0' && input.LA(1)<='\u09F1')||(input.LA(1)>='\u0A05' && input.LA(1)<='\u0A0A')||(input.LA(1)>='\u0A0F' && input.LA(1)<='\u0A10')||(input.LA(1)>='\u0A13' && input.LA(1)<='\u0A28')||(input.LA(1)>='\u0A2A' && input.LA(1)<='\u0A30')||(input.LA(1)>='\u0A32' && input.LA(1)<='\u0A33')||(input.LA(1)>='\u0A35' && input.LA(1)<='\u0A36')||(input.LA(1)>='\u0A38' && input.LA(1)<='\u0A39')||(input.LA(1)>='\u0A59' && input.LA(1)<='\u0A5C')||input.LA(1)=='\u0A5E'||(input.LA(1)>='\u0A72' && input.LA(1)<='\u0A74')||(input.LA(1)>='\u0A85' && input.LA(1)<='\u0A8B')||input.LA(1)=='\u0A8D'||(input.LA(1)>='\u0A8F' && input.LA(1)<='\u0A91')||(input.LA(1)>='\u0A93' && input.LA(1)<='\u0AA8')||(input.LA(1)>='\u0AAA' && input.LA(1)<='\u0AB0')||(input.LA(1)>='\u0AB2' && input.LA(1)<='\u0AB3')||(input.LA(1)>='\u0AB5' && input.LA(1)<='\u0AB9')||input.LA(1)=='\u0ABD'||input.LA(1)=='\u0AE0'||(input.LA(1)>='\u0B05' && input.LA(1)<='\u0B0C')||(input.LA(1)>='\u0B0F' && input.LA(1)<='\u0B10')||(input.LA(1)>='\u0B13' && input.LA(1)<='\u0B28')||(input.LA(1)>='\u0B2A' && input.LA(1)<='\u0B30')||(input.LA(1)>='\u0B32' && input.LA(1)<='\u0B33')||(input.LA(1)>='\u0B36' && input.LA(1)<='\u0B39')||input.LA(1)=='\u0B3D'||(input.LA(1)>='\u0B5C' && input.LA(1)<='\u0B5D')||(input.LA(1)>='\u0B5F' && input.LA(1)<='\u0B61')||(input.LA(1)>='\u0B85' && input.LA(1)<='\u0B8A')||(input.LA(1)>='\u0B8E' && input.LA(1)<='\u0B90')||(input.LA(1)>='\u0B92' && input.LA(1)<='\u0B95')||(input.LA(1)>='\u0B99' && input.LA(1)<='\u0B9A')||input.LA(1)=='\u0B9C'||(input.LA(1)>='\u0B9E' && input.LA(1)<='\u0B9F')||(input.LA(1)>='\u0BA3' && input.LA(1)<='\u0BA4')||(input.LA(1)>='\u0BA8' && input.LA(1)<='\u0BAA')||(input.LA(1)>='\u0BAE' && input.LA(1)<='\u0BB5')||(input.LA(1)>='\u0BB7' && input.LA(1)<='\u0BB9')||(input.LA(1)>='\u0C05' && input.LA(1)<='\u0C0C')||(input.LA(1)>='\u0C0E' && input.LA(1)<='\u0C10')||(input.LA(1)>='\u0C12' && input.LA(1)<='\u0C28')||(input.LA(1)>='\u0C2A' && input.LA(1)<='\u0C33')||(input.LA(1)>='\u0C35' && input.LA(1)<='\u0C39')||(input.LA(1)>='\u0C60' && input.LA(1)<='\u0C61')||(input.LA(1)>='\u0C85' && input.LA(1)<='\u0C8C')||(input.LA(1)>='\u0C8E' && input.LA(1)<='\u0C90')||(input.LA(1)>='\u0C92' && input.LA(1)<='\u0CA8')||(input.LA(1)>='\u0CAA' && input.LA(1)<='\u0CB3')||(input.LA(1)>='\u0CB5' && input.LA(1)<='\u0CB9')||input.LA(1)=='\u0CDE'||(input.LA(1)>='\u0CE0' && input.LA(1)<='\u0CE1')||(input.LA(1)>='\u0D05' && input.LA(1)<='\u0D0C')||(input.LA(1)>='\u0D0E' && input.LA(1)<='\u0D10')||(input.LA(1)>='\u0D12' && input.LA(1)<='\u0D28')||(input.LA(1)>='\u0D2A' && input.LA(1)<='\u0D39')||(input.LA(1)>='\u0D60' && input.LA(1)<='\u0D61')||(input.LA(1)>='\u0E01' && input.LA(1)<='\u0E2E')||input.LA(1)=='\u0E30'||(input.LA(1)>='\u0E32' && input.LA(1)<='\u0E33')||(input.LA(1)>='\u0E40' && input.LA(1)<='\u0E45')||(input.LA(1)>='\u0E81' && input.LA(1)<='\u0E82')||input.LA(1)=='\u0E84'||(input.LA(1)>='\u0E87' && input.LA(1)<='\u0E88')||input.LA(1)=='\u0E8A'||input.LA(1)=='\u0E8D'||(input.LA(1)>='\u0E94' && input.LA(1)<='\u0E97')||(input.LA(1)>='\u0E99' && input.LA(1)<='\u0E9F')||(input.LA(1)>='\u0EA1' && input.LA(1)<='\u0EA3')||input.LA(1)=='\u0EA5'||input.LA(1)=='\u0EA7'||(input.LA(1)>='\u0EAA' && input.LA(1)<='\u0EAB')||(input.LA(1)>='\u0EAD' && input.LA(1)<='\u0EAE')||input.LA(1)=='\u0EB0'||(input.LA(1)>='\u0EB2' && input.LA(1)<='\u0EB3')||input.LA(1)=='\u0EBD'||(input.LA(1)>='\u0EC0' && input.LA(1)<='\u0EC4')||(input.LA(1)>='\u0F40' && input.LA(1)<='\u0F47')||(input.LA(1)>='\u0F49' && input.LA(1)<='\u0F69')||(input.LA(1)>='\u10A0' && input.LA(1)<='\u10C5')||(input.LA(1)>='\u10D0' && input.LA(1)<='\u10F6')||input.LA(1)=='\u1100'||(input.LA(1)>='\u1102' && input.LA(1)<='\u1103')||(input.LA(1)>='\u1105' && input.LA(1)<='\u1107')||input.LA(1)=='\u1109'||(input.LA(1)>='\u110B' && input.LA(1)<='\u110C')||(input.LA(1)>='\u110E' && input.LA(1)<='\u1112')||input.LA(1)=='\u113C'||input.LA(1)=='\u113E'||input.LA(1)=='\u1140'||input.LA(1)=='\u114C'||input.LA(1)=='\u114E'||input.LA(1)=='\u1150'||(input.LA(1)>='\u1154' && input.LA(1)<='\u1155')||input.LA(1)=='\u1159'||(input.LA(1)>='\u115F' && input.LA(1)<='\u1161')||input.LA(1)=='\u1163'||input.LA(1)=='\u1165'||input.LA(1)=='\u1167'||input.LA(1)=='\u1169'||(input.LA(1)>='\u116D' && input.LA(1)<='\u116E')||(input.LA(1)>='\u1172' && input.LA(1)<='\u1173')||input.LA(1)=='\u1175'||input.LA(1)=='\u119E'||input.LA(1)=='\u11A8'||input.LA(1)=='\u11AB'||(input.LA(1)>='\u11AE' && input.LA(1)<='\u11AF')||(input.LA(1)>='\u11B7' && input.LA(1)<='\u11B8')||input.LA(1)=='\u11BA'||(input.LA(1)>='\u11BC' && input.LA(1)<='\u11C2')||input.LA(1)=='\u11EB'||input.LA(1)=='\u11F0'||input.LA(1)=='\u11F9'||(input.LA(1)>='\u1E00' && input.LA(1)<='\u1E9B')||(input.LA(1)>='\u1EA0' && input.LA(1)<='\u1EF9')||(input.LA(1)>='\u1F00' && input.LA(1)<='\u1F15')||(input.LA(1)>='\u1F18' && input.LA(1)<='\u1F1D')||(input.LA(1)>='\u1F20' && input.LA(1)<='\u1F45')||(input.LA(1)>='\u1F48' && input.LA(1)<='\u1F4D')||(input.LA(1)>='\u1F50' && input.LA(1)<='\u1F57')||input.LA(1)=='\u1F59'||input.LA(1)=='\u1F5B'||input.LA(1)=='\u1F5D'||(input.LA(1)>='\u1F5F' && input.LA(1)<='\u1F7D')||(input.LA(1)>='\u1F80' && input.LA(1)<='\u1FB4')||(input.LA(1)>='\u1FB6' && input.LA(1)<='\u1FBC')||input.LA(1)=='\u1FBE'||(input.LA(1)>='\u1FC2' && input.LA(1)<='\u1FC4')||(input.LA(1)>='\u1FC6' && input.LA(1)<='\u1FCC')||(input.LA(1)>='\u1FD0' && input.LA(1)<='\u1FD3')||(input.LA(1)>='\u1FD6' && input.LA(1)<='\u1FDB')||(input.LA(1)>='\u1FE0' && input.LA(1)<='\u1FEC')||(input.LA(1)>='\u1FF2' && input.LA(1)<='\u1FF4')||(input.LA(1)>='\u1FF6' && input.LA(1)<='\u1FFC')||input.LA(1)=='\u2126'||(input.LA(1)>='\u212A' && input.LA(1)<='\u212B')||input.LA(1)=='\u212E'||(input.LA(1)>='\u2180' && input.LA(1)<='\u2182')||input.LA(1)=='\u3007'||(input.LA(1)>='\u3021' && input.LA(1)<='\u3029')||(input.LA(1)>='\u3041' && input.LA(1)<='\u3094')||(input.LA(1)>='\u30A1' && input.LA(1)<='\u30FA')||(input.LA(1)>='\u3105' && input.LA(1)<='\u312C')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FA5')||(input.LA(1)>='\uAC00' && input.LA(1)<='\uD7A3') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    public void mTokens() throws RecognitionException {
        // java/com/google/gdata/model/select/parser/Selection.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | WHITESPACE | STRINGLITERAL | NUMBER | NCNAME )
        int alt16=19;
        alt16 = dfa16.predict(input);
        switch (alt16) {
            case 1 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:52: T__20
                {
                mT__20(); 

                }
                break;
            case 9 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:58: T__21
                {
                mT__21(); 

                }
                break;
            case 10 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:64: T__22
                {
                mT__22(); 

                }
                break;
            case 11 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:70: T__23
                {
                mT__23(); 

                }
                break;
            case 12 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:76: T__24
                {
                mT__24(); 

                }
                break;
            case 13 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:82: T__25
                {
                mT__25(); 

                }
                break;
            case 14 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:88: T__26
                {
                mT__26(); 

                }
                break;
            case 15 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:94: T__27
                {
                mT__27(); 

                }
                break;
            case 16 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:100: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 17 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:111: STRINGLITERAL
                {
                mSTRINGLITERAL(); 

                }
                break;
            case 18 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:125: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 19 :
                // java/com/google/gdata/model/select/parser/Selection.g:1:132: NCNAME
                {
                mNCNAME(); 

                }
                break;

        }

    }


    protected DFA16 dfa16 = new DFA16(this);
    static final String DFA16_eotS =
        "\11\uffff\1\23\1\25\13\uffff";
    static final String DFA16_eofS =
        "\26\uffff";
    static final String DFA16_minS =
        "\1\11\10\uffff\2\75\13\uffff";
    static final String DFA16_maxS =
        "\1\ud7a3\10\uffff\2\75\13\uffff";
    static final String DFA16_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\15\1\16\1\17"+
        "\1\20\1\21\1\22\1\23\1\12\1\11\1\14\1\13";
    static final String DFA16_specialS =
        "\26\uffff}>";
    static final String[] DFA16_transitionS = {
            "\1\16\26\uffff\1\16\1\10\1\17\4\uffff\1\17\1\6\1\7\1\15\1\20"+
            "\1\3\2\20\1\13\12\20\1\1\1\uffff\1\12\1\2\1\11\1\uffff\1\14"+
            "\32\21\1\4\1\uffff\1\5\1\uffff\1\21\1\uffff\32\21\105\uffff"+
            "\27\21\1\uffff\37\21\1\uffff\72\21\2\uffff\13\21\2\uffff\10"+
            "\21\1\uffff\65\21\1\uffff\104\21\11\uffff\44\21\3\uffff\2\21"+
            "\4\uffff\36\21\70\uffff\131\21\22\uffff\7\21\u00c4\uffff\1\21"+
            "\1\uffff\3\21\1\uffff\1\21\1\uffff\24\21\1\uffff\54\21\1\uffff"+
            "\7\21\3\uffff\1\21\1\uffff\1\21\1\uffff\1\21\1\uffff\1\21\1"+
            "\uffff\22\21\15\uffff\14\21\1\uffff\102\21\1\uffff\14\21\1\uffff"+
            "\44\21\16\uffff\65\21\2\uffff\2\21\2\uffff\2\21\3\uffff\34\21"+
            "\2\uffff\10\21\2\uffff\2\21\67\uffff\46\21\2\uffff\1\21\7\uffff"+
            "\46\21\111\uffff\33\21\5\uffff\3\21\56\uffff\32\21\6\uffff\12"+
            "\21\46\uffff\107\21\2\uffff\5\21\1\uffff\17\21\1\uffff\4\21"+
            "\1\uffff\1\21\17\uffff\2\21\u021e\uffff\65\21\3\uffff\1\21\32"+
            "\uffff\12\21\43\uffff\10\21\2\uffff\2\21\2\uffff\26\21\1\uffff"+
            "\7\21\1\uffff\1\21\3\uffff\4\21\42\uffff\2\21\1\uffff\3\21\16"+
            "\uffff\2\21\23\uffff\6\21\4\uffff\2\21\2\uffff\26\21\1\uffff"+
            "\7\21\1\uffff\2\21\1\uffff\2\21\1\uffff\2\21\37\uffff\4\21\1"+
            "\uffff\1\21\23\uffff\3\21\20\uffff\7\21\1\uffff\1\21\1\uffff"+
            "\3\21\1\uffff\26\21\1\uffff\7\21\1\uffff\2\21\1\uffff\5\21\3"+
            "\uffff\1\21\42\uffff\1\21\44\uffff\10\21\2\uffff\2\21\2\uffff"+
            "\26\21\1\uffff\7\21\1\uffff\2\21\2\uffff\4\21\3\uffff\1\21\36"+
            "\uffff\2\21\1\uffff\3\21\43\uffff\6\21\3\uffff\3\21\1\uffff"+
            "\4\21\3\uffff\2\21\1\uffff\1\21\1\uffff\2\21\3\uffff\2\21\3"+
            "\uffff\3\21\3\uffff\10\21\1\uffff\3\21\113\uffff\10\21\1\uffff"+
            "\3\21\1\uffff\27\21\1\uffff\12\21\1\uffff\5\21\46\uffff\2\21"+
            "\43\uffff\10\21\1\uffff\3\21\1\uffff\27\21\1\uffff\12\21\1\uffff"+
            "\5\21\44\uffff\1\21\1\uffff\2\21\43\uffff\10\21\1\uffff\3\21"+
            "\1\uffff\27\21\1\uffff\20\21\46\uffff\2\21\u009f\uffff\56\21"+
            "\1\uffff\1\21\1\uffff\2\21\14\uffff\6\21\73\uffff\2\21\1\uffff"+
            "\1\21\2\uffff\2\21\1\uffff\1\21\2\uffff\1\21\6\uffff\4\21\1"+
            "\uffff\7\21\1\uffff\3\21\1\uffff\1\21\1\uffff\1\21\2\uffff\2"+
            "\21\1\uffff\2\21\1\uffff\1\21\1\uffff\2\21\11\uffff\1\21\2\uffff"+
            "\5\21\173\uffff\10\21\1\uffff\41\21\u0136\uffff\46\21\12\uffff"+
            "\47\21\11\uffff\1\21\1\uffff\2\21\1\uffff\3\21\1\uffff\1\21"+
            "\1\uffff\2\21\1\uffff\5\21\51\uffff\1\21\1\uffff\1\21\1\uffff"+
            "\1\21\13\uffff\1\21\1\uffff\1\21\1\uffff\1\21\3\uffff\2\21\3"+
            "\uffff\1\21\5\uffff\3\21\1\uffff\1\21\1\uffff\1\21\1\uffff\1"+
            "\21\1\uffff\1\21\3\uffff\2\21\3\uffff\2\21\1\uffff\1\21\50\uffff"+
            "\1\21\11\uffff\1\21\2\uffff\1\21\2\uffff\2\21\7\uffff\2\21\1"+
            "\uffff\1\21\1\uffff\7\21\50\uffff\1\21\4\uffff\1\21\10\uffff"+
            "\1\21\u0c06\uffff\u009c\21\4\uffff\132\21\6\uffff\26\21\2\uffff"+
            "\6\21\2\uffff\46\21\2\uffff\6\21\2\uffff\10\21\1\uffff\1\21"+
            "\1\uffff\1\21\1\uffff\1\21\1\uffff\37\21\2\uffff\65\21\1\uffff"+
            "\7\21\1\uffff\1\21\3\uffff\3\21\1\uffff\7\21\3\uffff\4\21\2"+
            "\uffff\6\21\4\uffff\15\21\5\uffff\3\21\1\uffff\7\21\u0129\uffff"+
            "\1\21\3\uffff\2\21\2\uffff\1\21\121\uffff\3\21\u0e84\uffff\1"+
            "\21\31\uffff\11\21\27\uffff\124\21\14\uffff\132\21\12\uffff"+
            "\50\21\u1cd3\uffff\u51a6\21\u0c5a\uffff\u2ba4\21",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\22",
            "\1\24",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | WHITESPACE | STRINGLITERAL | NUMBER | NCNAME );";
        }
    }
 

}