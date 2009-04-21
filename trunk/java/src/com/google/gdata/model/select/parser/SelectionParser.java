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
// $ANTLR 3.1.1 java/com/google/gdata/model/select/parser/Selection.g 2009-04-20 11:14:26

package com.google.gdata.model.select.parser;

import com.google.gdata.model.QName;
import com.google.gdata.model.select.ElementCondition;
import com.google.gdata.model.select.ElementSelector;
import com.google.gdata.model.select.ExistenceCondition;
import com.google.gdata.model.select.NotCondition;
import com.google.gdata.model.select.ValueMatcher.Operation;

import java.util.ArrayList;
import java.util.List;

import static com.google.gdata.model.select.parser.ParserUtil.and;
import static com.google.gdata.model.select.parser.ParserUtil.buildDateComparisonExpression;
import static com.google.gdata.model.select.parser.ParserUtil.buildDateTimeComparisonExpression;
import static com.google.gdata.model.select.parser.ParserUtil.buildElementSelector;
import static com.google.gdata.model.select.parser.ParserUtil.buildNumberComparisonExpression;
import static com.google.gdata.model.select.parser.ParserUtil.buildQName;
import static com.google.gdata.model.select.parser.ParserUtil.buildStringComparisonExpression;
import static com.google.gdata.model.select.parser.ParserUtil.or;
import static com.google.gdata.model.select.parser.ParserUtil.unquote;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Tokenizes and parses the element selection expressions.
 *
 * This grammar defines a simple language with xpath-like 
 * expressions.
 * 
 * WARNING: when upgrading this file, please run it
 * through a recent version of antlr, downloaded from
 * the antlr website. The version is third_party/java/antlr
 * is old and will happily generate invalid grammar files
 * without complaining.
 * and remove this comment (BUG 1295025)
 */
public class SelectionParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NCNAME", "STRINGLITERAL", "NUMBER", "WHITESPACE", "SIGN", "DIGIT", "EXP", "LETTER", "NONLETTER", "':'", "'='", "','", "'['", "']'", "'('", "')'", "'!='", "'>'", "'>='", "'<'", "'<='", "'/'", "'@'"
    };
    public static final int SIGN=8;
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

    // delegates
    // delegators


        public SelectionParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public SelectionParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return SelectionParser.tokenNames; }
    public String getGrammarFileName() { return "java/com/google/gdata/model/select/parser/Selection.g"; }


    /** Maps namespace aliases to namespace. */
    private final NamespaceContext namespaces = new NamespaceContext();

    /**
     * Returns the namespace context used by the parser.
     */
    NamespaceContext getNamespaceContext() {
      return namespaces;
    }

    public SelectionParser() {
      super(null);
    }

    @Override
    public void reset() {
      super.reset();
      // This is called from the constructor.
      if (namespaces != null) {
          namespaces.reset();
      }
    }

    /** Fails at the first error and throws {@link InternalParseException}. */
    @Override
    public void reportError(RecognitionException e) {
      throw new InternalParseException(getErrorMessage(e, getTokenNames()), e);
    }



    // $ANTLR start "selectionExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:92:1: selectionExpr returns [List<ElementSelector> value] : ( namespaceDeclarations )? s= elementSelectors EOF ;
    public final List<ElementSelector> selectionExpr() throws RecognitionException {
        List<ElementSelector> value = null;

        List<ElementSelector> s = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:93:3: ( ( namespaceDeclarations )? s= elementSelectors EOF )
            // java/com/google/gdata/model/select/parser/Selection.g:93:5: ( namespaceDeclarations )? s= elementSelectors EOF
            {
            // java/com/google/gdata/model/select/parser/Selection.g:93:5: ( namespaceDeclarations )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==NCNAME) ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1==13) ) {
                    int LA1_2 = input.LA(3);

                    if ( ((input.LT(1).getText().equals("xmlns"))) ) {
                        alt1=1;
                    }
                }
                else if ( (LA1_1==14) ) {
                    alt1=1;
                }
            }
            switch (alt1) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:93:5: namespaceDeclarations
                    {
                    pushFollow(FOLLOW_namespaceDeclarations_in_selectionExpr66);
                    namespaceDeclarations();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_elementSelectors_in_selectionExpr71);
            s=elementSelectors();

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_selectionExpr73); 

                value = s;
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "selectionExpr"


    // $ANTLR start "elementConditionExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:98:1: elementConditionExpr returns [ElementCondition value] : e= orExpr EOF ;
    public final ElementCondition elementConditionExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition e = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:99:3: (e= orExpr EOF )
            // java/com/google/gdata/model/select/parser/Selection.g:99:5: e= orExpr EOF
            {
            pushFollow(FOLLOW_orExpr_in_elementConditionExpr94);
            e=orExpr();

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_elementConditionExpr96); 

                value = e;            
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "elementConditionExpr"


    // $ANTLR start "namespaceDeclarations"
    // java/com/google/gdata/model/select/parser/Selection.g:105:1: namespaceDeclarations : namespaceDeclaration ( namespaceDeclarations )? ;
    public final void namespaceDeclarations() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:106:3: ( namespaceDeclaration ( namespaceDeclarations )? )
            // java/com/google/gdata/model/select/parser/Selection.g:106:5: namespaceDeclaration ( namespaceDeclarations )?
            {
            pushFollow(FOLLOW_namespaceDeclaration_in_namespaceDeclarations112);
            namespaceDeclaration();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:106:26: ( namespaceDeclarations )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==NCNAME) ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1==13) ) {
                    int LA2_2 = input.LA(3);

                    if ( ((input.LT(1).getText().equals("xmlns"))) ) {
                        alt2=1;
                    }
                }
                else if ( (LA2_1==14) ) {
                    alt2=1;
                }
            }
            switch (alt2) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:106:27: namespaceDeclarations
                    {
                    pushFollow(FOLLOW_namespaceDeclarations_in_namespaceDeclarations115);
                    namespaceDeclarations();

                    state._fsp--;


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "namespaceDeclarations"


    // $ANTLR start "namespaceDeclaration"
    // java/com/google/gdata/model/select/parser/Selection.g:109:1: namespaceDeclaration : xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL ;
    public final void namespaceDeclaration() throws RecognitionException {
        Token alias=null;
        Token uri=null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:110:3: ( xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL )
            // java/com/google/gdata/model/select/parser/Selection.g:110:5: xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL
            {
            pushFollow(FOLLOW_xmlnsToken_in_namespaceDeclaration130);
            xmlnsToken();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:110:16: ( ':' alias= NCNAME )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==13) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:110:18: ':' alias= NCNAME
                    {
                    match(input,13,FOLLOW_13_in_namespaceDeclaration134); 
                    alias=(Token)match(input,NCNAME,FOLLOW_NCNAME_in_namespaceDeclaration138); 

                    }
                    break;

            }

            match(input,14,FOLLOW_14_in_namespaceDeclaration142); 
            uri=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_namespaceDeclaration146); 

                namespaces.add(alias == null ? "" : (alias!=null?alias.getText():null), unquote((uri!=null?uri.getText():null)));
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "namespaceDeclaration"


    // $ANTLR start "elementSelectors"
    // java/com/google/gdata/model/select/parser/Selection.g:115:1: elementSelectors returns [List<ElementSelector> value] : (first= elementSelector ) ( ',' more= elementSelector )* ;
    public final List<ElementSelector> elementSelectors() throws RecognitionException {
        List<ElementSelector> value = null;

        ElementSelector first = null;

        ElementSelector more = null;



            List<ElementSelector> selectors = new ArrayList<ElementSelector>();
            value = selectors;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:120:3: ( (first= elementSelector ) ( ',' more= elementSelector )* )
            // java/com/google/gdata/model/select/parser/Selection.g:120:5: (first= elementSelector ) ( ',' more= elementSelector )*
            {
            // java/com/google/gdata/model/select/parser/Selection.g:120:5: (first= elementSelector )
            // java/com/google/gdata/model/select/parser/Selection.g:120:6: first= elementSelector
            {
            pushFollow(FOLLOW_elementSelector_in_elementSelectors177);
            first=elementSelector();

            state._fsp--;

             selectors.add(first); 

            }

            // java/com/google/gdata/model/select/parser/Selection.g:121:5: ( ',' more= elementSelector )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==15) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:121:6: ',' more= elementSelector
            	    {
            	    match(input,15,FOLLOW_15_in_elementSelectors188); 
            	    pushFollow(FOLLOW_elementSelector_in_elementSelectors192);
            	    more=elementSelector();

            	    state._fsp--;

            	     selectors.add(more); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "elementSelectors"


    // $ANTLR start "elementSelector"
    // java/com/google/gdata/model/select/parser/Selection.g:124:1: elementSelector returns [ElementSelector value] : p= elementPathExpr ( '[' c= orExpr ']' )? ( '(' s= elementSelectors ')' )? ;
    public final ElementSelector elementSelector() throws RecognitionException {
        ElementSelector value = null;

        PathExpression p = null;

        ElementCondition c = null;

        List<ElementSelector> s = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:125:3: (p= elementPathExpr ( '[' c= orExpr ']' )? ( '(' s= elementSelectors ')' )? )
            // java/com/google/gdata/model/select/parser/Selection.g:125:5: p= elementPathExpr ( '[' c= orExpr ']' )? ( '(' s= elementSelectors ')' )?
            {
            pushFollow(FOLLOW_elementPathExpr_in_elementSelector217);
            p=elementPathExpr();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:126:5: ( '[' c= orExpr ']' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==16) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:126:6: '[' c= orExpr ']'
                    {
                    match(input,16,FOLLOW_16_in_elementSelector225); 
                    pushFollow(FOLLOW_orExpr_in_elementSelector229);
                    c=orExpr();

                    state._fsp--;

                    match(input,17,FOLLOW_17_in_elementSelector231); 

                    }
                    break;

            }

            // java/com/google/gdata/model/select/parser/Selection.g:127:5: ( '(' s= elementSelectors ')' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==18) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:127:6: '(' s= elementSelectors ')'
                    {
                    match(input,18,FOLLOW_18_in_elementSelector241); 
                    pushFollow(FOLLOW_elementSelectors_in_elementSelector245);
                    s=elementSelectors();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_elementSelector247); 

                    }
                    break;

            }


                value = buildElementSelector(p, c, s);
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "elementSelector"


    // $ANTLR start "orExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:132:1: orExpr returns [ElementCondition value] : left= andExpr ( orToken right= andExpr )* ;
    public final ElementCondition orExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition left = null;

        ElementCondition right = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:133:3: (left= andExpr ( orToken right= andExpr )* )
            // java/com/google/gdata/model/select/parser/Selection.g:133:5: left= andExpr ( orToken right= andExpr )*
            {
            pushFollow(FOLLOW_andExpr_in_orExpr274);
            left=andExpr();

            state._fsp--;

             value = left; 
            // java/com/google/gdata/model/select/parser/Selection.g:134:5: ( orToken right= andExpr )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==NCNAME) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:134:6: orToken right= andExpr
            	    {
            	    pushFollow(FOLLOW_orToken_in_orExpr284);
            	    orToken();

            	    state._fsp--;

            	    pushFollow(FOLLOW_andExpr_in_orExpr288);
            	    right=andExpr();

            	    state._fsp--;

            	     value = or(value, right); 

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "orExpr"


    // $ANTLR start "andExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:137:1: andExpr returns [ElementCondition value] : left= finalExpr ( andToken right= finalExpr )* ;
    public final ElementCondition andExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition left = null;

        ElementCondition right = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:138:3: (left= finalExpr ( andToken right= finalExpr )* )
            // java/com/google/gdata/model/select/parser/Selection.g:138:5: left= finalExpr ( andToken right= finalExpr )*
            {
            pushFollow(FOLLOW_finalExpr_in_andExpr311);
            left=finalExpr();

            state._fsp--;

             value = left; 
            // java/com/google/gdata/model/select/parser/Selection.g:139:5: ( andToken right= finalExpr )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==NCNAME) ) {
                    switch ( input.LA(2) ) {
                    case NCNAME:
                        {
                        int LA8_5 = input.LA(3);

                        if ( ((input.LT(1).getText().equals("and"))) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case 18:
                        {
                        int LA8_6 = input.LA(3);

                        if ( ((input.LT(1).getText().equals("and"))) ) {
                            alt8=1;
                        }


                        }
                        break;
                    case 26:
                        {
                        int LA8_7 = input.LA(3);

                        if ( ((input.LT(1).getText().equals("and"))) ) {
                            alt8=1;
                        }


                        }
                        break;

                    }

                }


                switch (alt8) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:139:6: andToken right= finalExpr
            	    {
            	    pushFollow(FOLLOW_andToken_in_andExpr320);
            	    andToken();

            	    state._fsp--;

            	    pushFollow(FOLLOW_finalExpr_in_andExpr324);
            	    right=finalExpr();

            	    state._fsp--;

            	     value = and(value, right); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "andExpr"


    // $ANTLR start "finalExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:142:1: finalExpr returns [ElementCondition value] : ( ( notToken '(' notl= orExpr ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr ')' ) | (c= comparisonOrExistenceExpr ) );
    public final ElementCondition finalExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition notl = null;

        ElementCondition l = null;

        ElementCondition c = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:143:3: ( ( notToken '(' notl= orExpr ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr ')' ) | (c= comparisonOrExistenceExpr ) )
            int alt9=5;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:143:5: ( notToken '(' notl= orExpr ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:143:5: ( notToken '(' notl= orExpr ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:143:6: notToken '(' notl= orExpr ')'
                    {
                    pushFollow(FOLLOW_notToken_in_finalExpr346);
                    notToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr348); 
                    pushFollow(FOLLOW_orExpr_in_finalExpr352);
                    notl=orExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_finalExpr354); 
                     value = new NotCondition(notl); 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:144:5: ( trueToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:144:5: ( trueToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:144:6: trueToken '(' ')'
                    {
                    pushFollow(FOLLOW_trueToken_in_finalExpr364);
                    trueToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr366); 
                    match(input,19,FOLLOW_19_in_finalExpr368); 
                     value = ElementCondition.ALL; 

                    }


                    }
                    break;
                case 3 :
                    // java/com/google/gdata/model/select/parser/Selection.g:145:5: ( falseToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:145:5: ( falseToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:145:6: falseToken '(' ')'
                    {
                    pushFollow(FOLLOW_falseToken_in_finalExpr378);
                    falseToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr380); 
                    match(input,19,FOLLOW_19_in_finalExpr382); 
                     value = ElementCondition.NONE; 

                    }


                    }
                    break;
                case 4 :
                    // java/com/google/gdata/model/select/parser/Selection.g:146:5: ( '(' l= orExpr ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:146:5: ( '(' l= orExpr ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:146:6: '(' l= orExpr ')'
                    {
                    match(input,18,FOLLOW_18_in_finalExpr392); 
                    pushFollow(FOLLOW_orExpr_in_finalExpr396);
                    l=orExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_finalExpr398); 
                     value = l; 

                    }


                    }
                    break;
                case 5 :
                    // java/com/google/gdata/model/select/parser/Selection.g:147:5: (c= comparisonOrExistenceExpr )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:147:5: (c= comparisonOrExistenceExpr )
                    // java/com/google/gdata/model/select/parser/Selection.g:147:6: c= comparisonOrExistenceExpr
                    {
                    pushFollow(FOLLOW_comparisonOrExistenceExpr_in_finalExpr410);
                    c=comparisonOrExistenceExpr();

                    state._fsp--;

                     value = c; 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "finalExpr"


    // $ANTLR start "comparisonOrExistenceExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:150:1: comparisonOrExistenceExpr returns [ElementCondition value] : (d= dateOrDateTimeComparison | o= otherComparison );
    public final ElementCondition comparisonOrExistenceExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition d = null;

        ElementCondition o = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:151:5: (d= dateOrDateTimeComparison | o= otherComparison )
            int alt10=2;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:151:7: d= dateOrDateTimeComparison
                    {
                    pushFollow(FOLLOW_dateOrDateTimeComparison_in_comparisonOrExistenceExpr434);
                    d=dateOrDateTimeComparison();

                    state._fsp--;

                     value = d; 

                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:152:7: o= otherComparison
                    {
                    pushFollow(FOLLOW_otherComparison_in_comparisonOrExistenceExpr446);
                    o=otherComparison();

                    state._fsp--;

                    value = o; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "comparisonOrExistenceExpr"


    // $ANTLR start "dateOrDateTimeComparison"
    // java/com/google/gdata/model/select/parser/Selection.g:155:1: dateOrDateTimeComparison returns [ElementCondition value] : xsToken ':' ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) ) ;
    public final ElementCondition dateOrDateTimeComparison() throws RecognitionException {
        ElementCondition value = null;

        Token str=null;
        PathExpression p = null;

        Operation c = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:156:5: ( xsToken ':' ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) ) )
            // java/com/google/gdata/model/select/parser/Selection.g:156:7: xsToken ':' ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) )
            {
            pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison470);
            xsToken();

            state._fsp--;

            match(input,13,FOLLOW_13_in_dateOrDateTimeComparison472); 
            // java/com/google/gdata/model/select/parser/Selection.g:157:9: ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==NCNAME) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==18) ) {
                    int LA11_2 = input.LA(3);

                    if ( ((input.LT(1).getText().equals("date"))) ) {
                        alt11=1;
                    }
                    else if ( ((input.LT(1).getText().equals("dateTime"))) ) {
                        alt11=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 11, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:157:10: ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:157:10: ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:157:11: dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')'
                    {
                    pushFollow(FOLLOW_dateToken_in_dateOrDateTimeComparison485);
                    dateToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison487); 
                    pushFollow(FOLLOW_predicateExpr_in_dateOrDateTimeComparison491);
                    p=predicateExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison493); 
                    pushFollow(FOLLOW_comparator_in_dateOrDateTimeComparison497);
                    c=comparator();

                    state._fsp--;

                    pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison512);
                    xsToken();

                    state._fsp--;

                    match(input,13,FOLLOW_13_in_dateOrDateTimeComparison514); 
                    pushFollow(FOLLOW_dateToken_in_dateOrDateTimeComparison516);
                    dateToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison518); 
                    str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison522); 
                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison524); 

                    }


                                value = buildDateComparisonExpression(p, c, str);
                              

                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:161:11: ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:161:11: ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:161:12: dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')'
                    {
                    pushFollow(FOLLOW_dateTimeToken_in_dateOrDateTimeComparison540);
                    dateTimeToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison542); 
                    pushFollow(FOLLOW_predicateExpr_in_dateOrDateTimeComparison546);
                    p=predicateExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison548); 
                    pushFollow(FOLLOW_comparator_in_dateOrDateTimeComparison552);
                    c=comparator();

                    state._fsp--;

                    pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison567);
                    xsToken();

                    state._fsp--;

                    match(input,13,FOLLOW_13_in_dateOrDateTimeComparison569); 
                    pushFollow(FOLLOW_dateTimeToken_in_dateOrDateTimeComparison571);
                    dateTimeToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison573); 
                    str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison577); 
                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison579); 

                    }


                                value = 
                                    buildDateTimeComparisonExpression(p, c, str);
                              

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "dateOrDateTimeComparison"


    // $ANTLR start "otherComparison"
    // java/com/google/gdata/model/select/parser/Selection.g:168:1: otherComparison returns [ElementCondition value] : p= predicateExpr (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )? ;
    public final ElementCondition otherComparison() throws RecognitionException {
        ElementCondition value = null;

        Token str=null;
        Token num=null;
        PathExpression p = null;

        Operation c = null;



            boolean nan = false;
            boolean inf = false;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:173:3: (p= predicateExpr (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )? )
            // java/com/google/gdata/model/select/parser/Selection.g:173:5: p= predicateExpr (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?
            {
            pushFollow(FOLLOW_predicateExpr_in_otherComparison615);
            p=predicateExpr();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:173:21: (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?
            int alt13=2;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:173:22: c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )
                    {
                    pushFollow(FOLLOW_comparator_in_otherComparison620);
                    c=comparator();

                    state._fsp--;

                    // java/com/google/gdata/model/select/parser/Selection.g:173:35: (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )
                    int alt12=4;
                    alt12 = dfa12.predict(input);
                    switch (alt12) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:173:36: str= STRINGLITERAL
                            {
                            str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_otherComparison625); 

                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:174:9: num= NUMBER
                            {
                            num=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_otherComparison638); 

                            }
                            break;
                        case 3 :
                            // java/com/google/gdata/model/select/parser/Selection.g:175:9: ( naNToken )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:175:9: ( naNToken )
                            // java/com/google/gdata/model/select/parser/Selection.g:175:10: naNToken
                            {
                            pushFollow(FOLLOW_naNToken_in_otherComparison650);
                            naNToken();

                            state._fsp--;

                             nan = true; 

                            }


                            }
                            break;
                        case 4 :
                            // java/com/google/gdata/model/select/parser/Selection.g:176:9: ( infToken )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:176:9: ( infToken )
                            // java/com/google/gdata/model/select/parser/Selection.g:176:10: infToken
                            {
                            pushFollow(FOLLOW_infToken_in_otherComparison665);
                            infToken();

                            state._fsp--;

                             inf = true; 

                            }


                            }
                            break;

                    }


                    }
                    break;

            }


                if (c == null) {
                  value = new ExistenceCondition(
                      p.getAttribute(), p.getPathSteps());
                } else if (str != null) {
                  value = buildStringComparisonExpression(p, c, str);
                } else if (nan) {
                  value = buildNumberComparisonExpression(p, c, Double.NaN);
                } else if (inf) {
                  value = buildNumberComparisonExpression(p, c, 
                      Double.POSITIVE_INFINITY);
                } else {
                  value = buildNumberComparisonExpression(p, c, num);
                }
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "otherComparison"


    // $ANTLR start "comparator"
    // java/com/google/gdata/model/select/parser/Selection.g:193:1: comparator returns [Operation value] : ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) );
    public final Operation comparator() throws RecognitionException {
        Operation value = null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:194:3: ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) )
            int alt20=6;
            alt20 = dfa20.predict(input);
            switch (alt20) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:194:5: ( ( eqToken | '=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:194:5: ( ( eqToken | '=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:194:6: ( eqToken | '=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:194:6: ( eqToken | '=' )
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==NCNAME) ) {
                        alt14=1;
                    }
                    else if ( (LA14_0==14) ) {
                        alt14=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 0, input);

                        throw nvae;
                    }
                    switch (alt14) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:194:7: eqToken
                            {
                            pushFollow(FOLLOW_eqToken_in_comparator692);
                            eqToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:194:17: '='
                            {
                            match(input,14,FOLLOW_14_in_comparator696); 

                            }
                            break;

                    }

                     value = Operation.EQ; 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:195:5: ( ( neToken | '!=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:195:5: ( ( neToken | '!=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:195:6: ( neToken | '!=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:195:6: ( neToken | '!=' )
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==NCNAME) ) {
                        alt15=1;
                    }
                    else if ( (LA15_0==20) ) {
                        alt15=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 0, input);

                        throw nvae;
                    }
                    switch (alt15) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:195:7: neToken
                            {
                            pushFollow(FOLLOW_neToken_in_comparator708);
                            neToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:195:17: '!='
                            {
                            match(input,20,FOLLOW_20_in_comparator712); 

                            }
                            break;

                    }

                     value = Operation.NEQ; 

                    }


                    }
                    break;
                case 3 :
                    // java/com/google/gdata/model/select/parser/Selection.g:196:5: ( ( gtToken | '>' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:196:5: ( ( gtToken | '>' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:196:6: ( gtToken | '>' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:196:6: ( gtToken | '>' )
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==NCNAME) ) {
                        alt16=1;
                    }
                    else if ( (LA16_0==21) ) {
                        alt16=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 0, input);

                        throw nvae;
                    }
                    switch (alt16) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:196:7: gtToken
                            {
                            pushFollow(FOLLOW_gtToken_in_comparator724);
                            gtToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:196:17: '>'
                            {
                            match(input,21,FOLLOW_21_in_comparator728); 

                            }
                            break;

                    }

                     value = Operation.GT; 

                    }


                    }
                    break;
                case 4 :
                    // java/com/google/gdata/model/select/parser/Selection.g:197:5: ( ( gteToken | '>=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:197:5: ( ( gteToken | '>=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:197:6: ( gteToken | '>=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:197:6: ( gteToken | '>=' )
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==NCNAME) ) {
                        alt17=1;
                    }
                    else if ( (LA17_0==22) ) {
                        alt17=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 0, input);

                        throw nvae;
                    }
                    switch (alt17) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:197:7: gteToken
                            {
                            pushFollow(FOLLOW_gteToken_in_comparator740);
                            gteToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:197:18: '>='
                            {
                            match(input,22,FOLLOW_22_in_comparator744); 

                            }
                            break;

                    }

                     value = Operation.GTE; 

                    }


                    }
                    break;
                case 5 :
                    // java/com/google/gdata/model/select/parser/Selection.g:198:5: ( ( ltToken | '<' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:198:5: ( ( ltToken | '<' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:198:6: ( ltToken | '<' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:198:6: ( ltToken | '<' )
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==NCNAME) ) {
                        alt18=1;
                    }
                    else if ( (LA18_0==23) ) {
                        alt18=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 0, input);

                        throw nvae;
                    }
                    switch (alt18) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:198:7: ltToken
                            {
                            pushFollow(FOLLOW_ltToken_in_comparator756);
                            ltToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:198:17: '<'
                            {
                            match(input,23,FOLLOW_23_in_comparator760); 

                            }
                            break;

                    }

                     value = Operation.LT; 

                    }


                    }
                    break;
                case 6 :
                    // java/com/google/gdata/model/select/parser/Selection.g:199:5: ( ( lteToken | '<=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:199:5: ( ( lteToken | '<=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:199:6: ( lteToken | '<=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:199:6: ( lteToken | '<=' )
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==NCNAME) ) {
                        alt19=1;
                    }
                    else if ( (LA19_0==24) ) {
                        alt19=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 0, input);

                        throw nvae;
                    }
                    switch (alt19) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:199:7: lteToken
                            {
                            pushFollow(FOLLOW_lteToken_in_comparator772);
                            lteToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:199:18: '<='
                            {
                            match(input,24,FOLLOW_24_in_comparator776); 

                            }
                            break;

                    }

                     value = Operation.LTE; 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "comparator"


    // $ANTLR start "predicateExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:202:1: predicateExpr returns [PathExpression value] : ( (path= attributeOrElementPath ) | ( textToken '(' ')' ) );
    public final PathExpression predicateExpr() throws RecognitionException {
        PathExpression value = null;

        PathExpression path = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:203:3: ( (path= attributeOrElementPath ) | ( textToken '(' ')' ) )
            int alt21=2;
            alt21 = dfa21.predict(input);
            switch (alt21) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:203:5: (path= attributeOrElementPath )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:203:5: (path= attributeOrElementPath )
                    // java/com/google/gdata/model/select/parser/Selection.g:203:6: path= attributeOrElementPath
                    {
                    pushFollow(FOLLOW_attributeOrElementPath_in_predicateExpr800);
                    path=attributeOrElementPath();

                    state._fsp--;

                     value = path; 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:204:5: ( textToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:204:5: ( textToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:204:6: textToken '(' ')'
                    {
                    pushFollow(FOLLOW_textToken_in_predicateExpr810);
                    textToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_predicateExpr812); 
                    match(input,19,FOLLOW_19_in_predicateExpr814); 
                     value = new PathExpression(); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "predicateExpr"


    // $ANTLR start "elementPathExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:207:1: elementPathExpr returns [PathExpression value] : first= nameTest[false] ( '/' more= nameTest[false] )* ;
    public final PathExpression elementPathExpr() throws RecognitionException {
        PathExpression value = null;

        QName first = null;

        QName more = null;



            PathExpression expression = new PathExpression();
            value = expression;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:212:3: (first= nameTest[false] ( '/' more= nameTest[false] )* )
            // java/com/google/gdata/model/select/parser/Selection.g:212:5: first= nameTest[false] ( '/' more= nameTest[false] )*
            {
            pushFollow(FOLLOW_nameTest_in_elementPathExpr843);
            first=nameTest(false);

            state._fsp--;

             expression.add(first); 
            // java/com/google/gdata/model/select/parser/Selection.g:213:5: ( '/' more= nameTest[false] )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==25) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:213:6: '/' more= nameTest[false]
            	    {
            	    match(input,25,FOLLOW_25_in_elementPathExpr854); 
            	    pushFollow(FOLLOW_nameTest_in_elementPathExpr858);
            	    more=nameTest(false);

            	    state._fsp--;

            	     expression.add(more); 

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "elementPathExpr"


    // $ANTLR start "attributeOrElementPath"
    // java/com/google/gdata/model/select/parser/Selection.g:216:1: attributeOrElementPath returns [PathExpression value] : attributeOrElement[expression] ( '/' attributeOrElement[expression] )* ;
    public final PathExpression attributeOrElementPath() throws RecognitionException {
        PathExpression value = null;


            PathExpression expression = new PathExpression();
            value = expression;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:221:3: ( attributeOrElement[expression] ( '/' attributeOrElement[expression] )* )
            // java/com/google/gdata/model/select/parser/Selection.g:221:5: attributeOrElement[expression] ( '/' attributeOrElement[expression] )*
            {
            pushFollow(FOLLOW_attributeOrElement_in_attributeOrElementPath889);
            attributeOrElement(expression);

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:221:36: ( '/' attributeOrElement[expression] )*
            loop23:
            do {
                int alt23=2;
                alt23 = dfa23.predict(input);
                switch (alt23) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:221:37: '/' attributeOrElement[expression]
            	    {
            	    match(input,25,FOLLOW_25_in_attributeOrElementPath893); 
            	    pushFollow(FOLLOW_attributeOrElement_in_attributeOrElementPath895);
            	    attributeOrElement(expression);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "attributeOrElementPath"


    // $ANTLR start "attributeOrElement"
    // java/com/google/gdata/model/select/parser/Selection.g:224:1: attributeOrElement[PathExpression value] : (at= '@' )? qname= nameTest[at != null] ;
    public final void attributeOrElement(PathExpression value) throws RecognitionException {
        Token at=null;
        QName qname = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:225:3: ( (at= '@' )? qname= nameTest[at != null] )
            // java/com/google/gdata/model/select/parser/Selection.g:225:5: (at= '@' )? qname= nameTest[at != null]
            {
            // java/com/google/gdata/model/select/parser/Selection.g:225:5: (at= '@' )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==26) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:225:6: at= '@'
                    {
                    at=(Token)match(input,26,FOLLOW_26_in_attributeOrElement916); 

                    }
                    break;

            }

            pushFollow(FOLLOW_nameTest_in_attributeOrElement922);
            qname=nameTest(at != null);

            state._fsp--;


                if (value.hasAttribute()) {
                  throw new InternalParseException(input.LT(1), 
                      "An attribute has already been specified");
                }
                boolean isAttribute = at != null;
                if (isAttribute) {
                  value.setAttribute(qname);
                } else {
                  value.add(qname);
                }
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "attributeOrElement"


    // $ANTLR start "nameTest"
    // java/com/google/gdata/model/select/parser/Selection.g:239:1: nameTest[boolean isAttribute] returns [QName value] : left= NCNAME ( ':' right= NCNAME )? ;
    public final QName nameTest(boolean isAttribute) throws RecognitionException {
        QName value = null;

        Token left=null;
        Token right=null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:240:3: (left= NCNAME ( ':' right= NCNAME )? )
            // java/com/google/gdata/model/select/parser/Selection.g:240:5: left= NCNAME ( ':' right= NCNAME )?
            {
            left=(Token)match(input,NCNAME,FOLLOW_NCNAME_in_nameTest946); 
            // java/com/google/gdata/model/select/parser/Selection.g:240:17: ( ':' right= NCNAME )?
            int alt25=2;
            alt25 = dfa25.predict(input);
            switch (alt25) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:240:18: ':' right= NCNAME
                    {
                    match(input,13,FOLLOW_13_in_nameTest949); 
                    right=(Token)match(input,NCNAME,FOLLOW_NCNAME_in_nameTest953); 

                    }
                    break;

            }


                value = buildQName(namespaces, left, right, isAttribute);
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "nameTest"


    // $ANTLR start "xmlnsToken"
    // java/com/google/gdata/model/select/parser/Selection.g:246:1: xmlnsToken : {...}? NCNAME ;
    public final void xmlnsToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:246:12: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:246:14: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("xmlns"))) ) {
                throw new FailedPredicateException(input, "xmlnsToken", "input.LT(1).getText().equals(\"xmlns\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_xmlnsToken971); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "xmlnsToken"


    // $ANTLR start "orToken"
    // java/com/google/gdata/model/select/parser/Selection.g:247:1: orToken : {...}? NCNAME ;
    public final void orToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:247:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:247:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("or"))) ) {
                throw new FailedPredicateException(input, "orToken", "input.LT(1).getText().equals(\"or\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_orToken981); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "orToken"


    // $ANTLR start "andToken"
    // java/com/google/gdata/model/select/parser/Selection.g:248:1: andToken : {...}? NCNAME ;
    public final void andToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:248:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:248:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("and"))) ) {
                throw new FailedPredicateException(input, "andToken", "input.LT(1).getText().equals(\"and\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_andToken991); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "andToken"


    // $ANTLR start "notToken"
    // java/com/google/gdata/model/select/parser/Selection.g:249:1: notToken : {...}? NCNAME ;
    public final void notToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:249:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:249:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("not"))) ) {
                throw new FailedPredicateException(input, "notToken", "input.LT(1).getText().equals(\"not\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_notToken1001); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "notToken"


    // $ANTLR start "trueToken"
    // java/com/google/gdata/model/select/parser/Selection.g:250:1: trueToken : {...}? NCNAME ;
    public final void trueToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:250:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:250:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("true"))) ) {
                throw new FailedPredicateException(input, "trueToken", "input.LT(1).getText().equals(\"true\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_trueToken1011); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "trueToken"


    // $ANTLR start "falseToken"
    // java/com/google/gdata/model/select/parser/Selection.g:251:1: falseToken : {...}? NCNAME ;
    public final void falseToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:251:12: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:251:14: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("false"))) ) {
                throw new FailedPredicateException(input, "falseToken", "input.LT(1).getText().equals(\"false\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_falseToken1021); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "falseToken"


    // $ANTLR start "eqToken"
    // java/com/google/gdata/model/select/parser/Selection.g:252:1: eqToken : {...}? NCNAME ;
    public final void eqToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:252:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:252:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("eq"))) ) {
                throw new FailedPredicateException(input, "eqToken", "input.LT(1).getText().equals(\"eq\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_eqToken1031); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "eqToken"


    // $ANTLR start "neToken"
    // java/com/google/gdata/model/select/parser/Selection.g:253:1: neToken : {...}? NCNAME ;
    public final void neToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:253:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:253:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("ne"))) ) {
                throw new FailedPredicateException(input, "neToken", "input.LT(1).getText().equals(\"ne\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_neToken1041); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "neToken"


    // $ANTLR start "textToken"
    // java/com/google/gdata/model/select/parser/Selection.g:254:1: textToken : {...}? NCNAME ;
    public final void textToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:254:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:254:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("text"))) ) {
                throw new FailedPredicateException(input, "textToken", "input.LT(1).getText().equals(\"text\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_textToken1051); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "textToken"


    // $ANTLR start "ltToken"
    // java/com/google/gdata/model/select/parser/Selection.g:255:1: ltToken : {...}? NCNAME ;
    public final void ltToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:255:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:255:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("lt"))) ) {
                throw new FailedPredicateException(input, "ltToken", "input.LT(1).getText().equals(\"lt\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_ltToken1061); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "ltToken"


    // $ANTLR start "lteToken"
    // java/com/google/gdata/model/select/parser/Selection.g:256:1: lteToken : {...}? NCNAME ;
    public final void lteToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:256:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:256:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("lte"))) ) {
                throw new FailedPredicateException(input, "lteToken", "input.LT(1).getText().equals(\"lte\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_lteToken1071); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "lteToken"


    // $ANTLR start "gtToken"
    // java/com/google/gdata/model/select/parser/Selection.g:257:1: gtToken : {...}? NCNAME ;
    public final void gtToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:257:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:257:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("gt"))) ) {
                throw new FailedPredicateException(input, "gtToken", "input.LT(1).getText().equals(\"gt\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_gtToken1081); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "gtToken"


    // $ANTLR start "gteToken"
    // java/com/google/gdata/model/select/parser/Selection.g:258:1: gteToken : {...}? NCNAME ;
    public final void gteToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:258:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:258:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("gte"))) ) {
                throw new FailedPredicateException(input, "gteToken", "input.LT(1).getText().equals(\"gte\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_gteToken1091); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "gteToken"


    // $ANTLR start "naNToken"
    // java/com/google/gdata/model/select/parser/Selection.g:259:1: naNToken : {...}? NCNAME ;
    public final void naNToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:259:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:259:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("NaN"))) ) {
                throw new FailedPredicateException(input, "naNToken", "input.LT(1).getText().equals(\"NaN\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_naNToken1101); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "naNToken"


    // $ANTLR start "infToken"
    // java/com/google/gdata/model/select/parser/Selection.g:260:1: infToken : {...}? NCNAME ;
    public final void infToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:260:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:260:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("INF"))) ) {
                throw new FailedPredicateException(input, "infToken", "input.LT(1).getText().equals(\"INF\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_infToken1111); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "infToken"


    // $ANTLR start "xsToken"
    // java/com/google/gdata/model/select/parser/Selection.g:261:1: xsToken : {...}? NCNAME ;
    public final void xsToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:261:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:261:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("xs"))) ) {
                throw new FailedPredicateException(input, "xsToken", "input.LT(1).getText().equals(\"xs\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_xsToken1121); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "xsToken"


    // $ANTLR start "dateToken"
    // java/com/google/gdata/model/select/parser/Selection.g:262:1: dateToken : {...}? NCNAME ;
    public final void dateToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:262:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:262:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("date"))) ) {
                throw new FailedPredicateException(input, "dateToken", "input.LT(1).getText().equals(\"date\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_dateToken1131); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "dateToken"


    // $ANTLR start "dateTimeToken"
    // java/com/google/gdata/model/select/parser/Selection.g:263:1: dateTimeToken : {...}? NCNAME ;
    public final void dateTimeToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:263:15: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:263:17: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("dateTime"))) ) {
                throw new FailedPredicateException(input, "dateTimeToken", "input.LT(1).getText().equals(\"dateTime\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_dateTimeToken1141); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "dateTimeToken"

    // Delegated rules


    protected DFA9 dfa9 = new DFA9(this);
    protected DFA10 dfa10 = new DFA10(this);
    protected DFA13 dfa13 = new DFA13(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA20 dfa20 = new DFA20(this);
    protected DFA21 dfa21 = new DFA21(this);
    protected DFA23 dfa23 = new DFA23(this);
    protected DFA25 dfa25 = new DFA25(this);
    static final String DFA9_eotS =
        "\24\uffff";
    static final String DFA9_eofS =
        "\1\uffff\1\3\22\uffff";
    static final String DFA9_minS =
        "\2\4\2\uffff\1\0\17\uffff";
    static final String DFA9_maxS =
        "\1\32\1\31\2\uffff\1\0\17\uffff";
    static final String DFA9_acceptS =
        "\2\uffff\1\4\1\5\15\uffff\1\1\1\2\1\3";
    static final String DFA9_specialS =
        "\4\uffff\1\0\17\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\1\15\uffff\1\2\7\uffff\1\3",
            "\1\3\10\uffff\2\3\2\uffff\1\3\1\4\7\3",
            "",
            "",
            "\1\uffff",
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
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "142:1: finalExpr returns [ElementCondition value] : ( ( notToken '(' notl= orExpr ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr ')' ) | (c= comparisonOrExistenceExpr ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_4 = input.LA(1);

                         
                        int index9_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("not"))) ) {s = 17;}

                        else if ( ((input.LT(1).getText().equals("true"))) ) {s = 18;}

                        else if ( ((input.LT(1).getText().equals("false"))) ) {s = 19;}

                        else if ( ((input.LT(1).getText().equals("text"))) ) {s = 3;}

                         
                        input.seek(index9_4);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 9, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA10_eotS =
        "\21\uffff";
    static final String DFA10_eofS =
        "\1\uffff\1\2\17\uffff";
    static final String DFA10_minS =
        "\2\4\1\uffff\1\0\15\uffff";
    static final String DFA10_maxS =
        "\1\32\1\31\1\uffff\1\0\15\uffff";
    static final String DFA10_acceptS =
        "\2\uffff\1\2\15\uffff\1\1";
    static final String DFA10_specialS =
        "\3\uffff\1\0\15\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1\25\uffff\1\2",
            "\1\2\10\uffff\1\3\1\2\2\uffff\11\2",
            "",
            "\1\uffff",
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
            "",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "150:1: comparisonOrExistenceExpr returns [ElementCondition value] : (d= dateOrDateTimeComparison | o= otherComparison );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA10_3 = input.LA(1);

                         
                        int index10_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("xs"))) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index10_3);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 10, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA13_eotS =
        "\20\uffff";
    static final String DFA13_eofS =
        "\1\10\17\uffff";
    static final String DFA13_minS =
        "\2\4\13\uffff\1\0\2\uffff";
    static final String DFA13_maxS =
        "\1\30\1\32\13\uffff\1\0\2\uffff";
    static final String DFA13_acceptS =
        "\2\uffff\1\1\5\uffff\1\2\7\uffff";
    static final String DFA13_specialS =
        "\15\uffff\1\0\2\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\1\11\uffff\1\2\2\uffff\1\10\1\uffff\1\10\5\2",
            "\1\15\2\2\13\uffff\1\10\7\uffff\1\10",
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
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "173:21: (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_13 = input.LA(1);

                         
                        int index13_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (((input.LT(1).getText().equals("gte"))||(input.LT(1).getText().equals("lte"))||(input.LT(1).getText().equals("gt"))||(input.LT(1).getText().equals("lt"))||(input.LT(1).getText().equals("eq"))||(input.LT(1).getText().equals("ne")))) ) {s = 2;}

                        else if ( (true) ) {s = 8;}

                         
                        input.seek(index13_13);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA12_eotS =
        "\12\uffff";
    static final String DFA12_eofS =
        "\12\uffff";
    static final String DFA12_minS =
        "\1\4\2\uffff\1\0\6\uffff";
    static final String DFA12_maxS =
        "\1\6\2\uffff\1\0\6\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\1\2\5\uffff\1\3\1\4";
    static final String DFA12_specialS =
        "\3\uffff\1\0\6\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\3\1\1\1\2",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "173:35: (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_3 = input.LA(1);

                         
                        int index12_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("NaN"))) ) {s = 8;}

                        else if ( ((input.LT(1).getText().equals("INF"))) ) {s = 9;}

                         
                        input.seek(index12_3);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA20_eotS =
        "\13\uffff";
    static final String DFA20_eofS =
        "\13\uffff";
    static final String DFA20_minS =
        "\1\4\1\0\11\uffff";
    static final String DFA20_maxS =
        "\1\30\1\0\11\uffff";
    static final String DFA20_acceptS =
        "\2\uffff\1\1\1\2\1\3\1\4\1\5\1\6\3\uffff";
    static final String DFA20_specialS =
        "\1\uffff\1\0\11\uffff}>";
    static final String[] DFA20_transitionS = {
            "\1\1\11\uffff\1\2\5\uffff\1\3\1\4\1\5\1\6\1\7",
            "\1\uffff",
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

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "193:1: comparator returns [Operation value] : ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_1 = input.LA(1);

                         
                        int index20_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("eq"))) ) {s = 2;}

                        else if ( ((input.LT(1).getText().equals("ne"))) ) {s = 3;}

                        else if ( ((input.LT(1).getText().equals("gt"))) ) {s = 4;}

                        else if ( ((input.LT(1).getText().equals("gte"))) ) {s = 5;}

                        else if ( ((input.LT(1).getText().equals("lt"))) ) {s = 6;}

                        else if ( ((input.LT(1).getText().equals("lte"))) ) {s = 7;}

                         
                        input.seek(index20_1);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA21_eotS =
        "\20\uffff";
    static final String DFA21_eofS =
        "\2\uffff\1\1\15\uffff";
    static final String DFA21_minS =
        "\1\4\1\uffff\1\4\15\uffff";
    static final String DFA21_maxS =
        "\1\32\1\uffff\1\31\15\uffff";
    static final String DFA21_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\14\uffff";
    static final String DFA21_specialS =
        "\20\uffff}>";
    static final String[] DFA21_transitionS = {
            "\1\2\25\uffff\1\1",
            "",
            "\1\1\10\uffff\2\1\2\uffff\1\1\1\3\7\1",
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
            "",
            "",
            ""
    };

    static final short[] DFA21_eot = DFA.unpackEncodedString(DFA21_eotS);
    static final short[] DFA21_eof = DFA.unpackEncodedString(DFA21_eofS);
    static final char[] DFA21_min = DFA.unpackEncodedStringToUnsignedChars(DFA21_minS);
    static final char[] DFA21_max = DFA.unpackEncodedStringToUnsignedChars(DFA21_maxS);
    static final short[] DFA21_accept = DFA.unpackEncodedString(DFA21_acceptS);
    static final short[] DFA21_special = DFA.unpackEncodedString(DFA21_specialS);
    static final short[][] DFA21_transition;

    static {
        int numStates = DFA21_transitionS.length;
        DFA21_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA21_transition[i] = DFA.unpackEncodedString(DFA21_transitionS[i]);
        }
    }

    class DFA21 extends DFA {

        public DFA21(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 21;
            this.eot = DFA21_eot;
            this.eof = DFA21_eof;
            this.min = DFA21_min;
            this.max = DFA21_max;
            this.accept = DFA21_accept;
            this.special = DFA21_special;
            this.transition = DFA21_transition;
        }
        public String getDescription() {
            return "202:1: predicateExpr returns [PathExpression value] : ( (path= attributeOrElementPath ) | ( textToken '(' ')' ) );";
        }
    }
    static final String DFA23_eotS =
        "\14\uffff";
    static final String DFA23_eofS =
        "\1\1\13\uffff";
    static final String DFA23_minS =
        "\1\4\13\uffff";
    static final String DFA23_maxS =
        "\1\31\13\uffff";
    static final String DFA23_acceptS =
        "\1\uffff\1\2\11\uffff\1\1";
    static final String DFA23_specialS =
        "\14\uffff}>";
    static final String[] DFA23_transitionS = {
            "\1\1\11\uffff\1\1\2\uffff\1\1\1\uffff\6\1\1\13",
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

    static final short[] DFA23_eot = DFA.unpackEncodedString(DFA23_eotS);
    static final short[] DFA23_eof = DFA.unpackEncodedString(DFA23_eofS);
    static final char[] DFA23_min = DFA.unpackEncodedStringToUnsignedChars(DFA23_minS);
    static final char[] DFA23_max = DFA.unpackEncodedStringToUnsignedChars(DFA23_maxS);
    static final short[] DFA23_accept = DFA.unpackEncodedString(DFA23_acceptS);
    static final short[] DFA23_special = DFA.unpackEncodedString(DFA23_specialS);
    static final short[][] DFA23_transition;

    static {
        int numStates = DFA23_transitionS.length;
        DFA23_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA23_transition[i] = DFA.unpackEncodedString(DFA23_transitionS[i]);
        }
    }

    class DFA23 extends DFA {

        public DFA23(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 23;
            this.eot = DFA23_eot;
            this.eof = DFA23_eof;
            this.min = DFA23_min;
            this.max = DFA23_max;
            this.accept = DFA23_accept;
            this.special = DFA23_special;
            this.transition = DFA23_transition;
        }
        public String getDescription() {
            return "()* loopback of 221:36: ( '/' attributeOrElement[expression] )*";
        }
    }
    static final String DFA25_eotS =
        "\20\uffff";
    static final String DFA25_eofS =
        "\1\2\17\uffff";
    static final String DFA25_minS =
        "\1\4\17\uffff";
    static final String DFA25_maxS =
        "\1\31\17\uffff";
    static final String DFA25_acceptS =
        "\1\uffff\1\1\1\2\15\uffff";
    static final String DFA25_specialS =
        "\20\uffff}>";
    static final String[] DFA25_transitionS = {
            "\1\2\10\uffff\1\1\14\2",
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
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA25_eot = DFA.unpackEncodedString(DFA25_eotS);
    static final short[] DFA25_eof = DFA.unpackEncodedString(DFA25_eofS);
    static final char[] DFA25_min = DFA.unpackEncodedStringToUnsignedChars(DFA25_minS);
    static final char[] DFA25_max = DFA.unpackEncodedStringToUnsignedChars(DFA25_maxS);
    static final short[] DFA25_accept = DFA.unpackEncodedString(DFA25_acceptS);
    static final short[] DFA25_special = DFA.unpackEncodedString(DFA25_specialS);
    static final short[][] DFA25_transition;

    static {
        int numStates = DFA25_transitionS.length;
        DFA25_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA25_transition[i] = DFA.unpackEncodedString(DFA25_transitionS[i]);
        }
    }

    class DFA25 extends DFA {

        public DFA25(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 25;
            this.eot = DFA25_eot;
            this.eof = DFA25_eof;
            this.min = DFA25_min;
            this.max = DFA25_max;
            this.accept = DFA25_accept;
            this.special = DFA25_special;
            this.transition = DFA25_transition;
        }
        public String getDescription() {
            return "240:17: ( ':' right= NCNAME )?";
        }
    }
 

    public static final BitSet FOLLOW_namespaceDeclarations_in_selectionExpr66 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_elementSelectors_in_selectionExpr71 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_selectionExpr73 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpr_in_elementConditionExpr94 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_elementConditionExpr96 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_namespaceDeclaration_in_namespaceDeclarations112 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_namespaceDeclarations_in_namespaceDeclarations115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_xmlnsToken_in_namespaceDeclaration130 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_13_in_namespaceDeclaration134 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NCNAME_in_namespaceDeclaration138 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_namespaceDeclaration142 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_namespaceDeclaration146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementSelector_in_elementSelectors177 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_15_in_elementSelectors188 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_elementSelector_in_elementSelectors192 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_elementPathExpr_in_elementSelector217 = new BitSet(new long[]{0x0000000000050002L});
    public static final BitSet FOLLOW_16_in_elementSelector225 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_orExpr_in_elementSelector229 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_elementSelector231 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_18_in_elementSelector241 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_elementSelectors_in_elementSelector245 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_elementSelector247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpr_in_orExpr274 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_orToken_in_orExpr284 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_andExpr_in_orExpr288 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_finalExpr_in_andExpr311 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_andToken_in_andExpr320 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_finalExpr_in_andExpr324 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_notToken_in_finalExpr346 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr348 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_orExpr_in_finalExpr352 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_trueToken_in_finalExpr364 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr366 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_falseToken_in_finalExpr378 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr380 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_finalExpr392 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_orExpr_in_finalExpr396 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparisonOrExistenceExpr_in_finalExpr410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateOrDateTimeComparison_in_comparisonOrExistenceExpr434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherComparison_in_comparisonOrExistenceExpr446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison470 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison472 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateToken_in_dateOrDateTimeComparison485 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison487 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_predicateExpr_in_dateOrDateTimeComparison491 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison493 = new BitSet(new long[]{0x0000000001F04010L});
    public static final BitSet FOLLOW_comparator_in_dateOrDateTimeComparison497 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison512 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison514 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateToken_in_dateOrDateTimeComparison516 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison518 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison522 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateTimeToken_in_dateOrDateTimeComparison540 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison542 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_predicateExpr_in_dateOrDateTimeComparison546 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison548 = new BitSet(new long[]{0x0000000001F04010L});
    public static final BitSet FOLLOW_comparator_in_dateOrDateTimeComparison552 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison567 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison569 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateTimeToken_in_dateOrDateTimeComparison571 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison573 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison577 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicateExpr_in_otherComparison615 = new BitSet(new long[]{0x0000000001F04012L});
    public static final BitSet FOLLOW_comparator_in_otherComparison620 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_otherComparison625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_otherComparison638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_naNToken_in_otherComparison650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_infToken_in_otherComparison665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_eqToken_in_comparator692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_comparator696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_neToken_in_comparator708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_comparator712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_gtToken_in_comparator724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_comparator728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_gteToken_in_comparator740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_comparator744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ltToken_in_comparator756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_comparator760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lteToken_in_comparator772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_comparator776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeOrElementPath_in_predicateExpr800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_textToken_in_predicateExpr810 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_predicateExpr812 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_predicateExpr814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nameTest_in_elementPathExpr843 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_elementPathExpr854 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_nameTest_in_elementPathExpr858 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_attributeOrElement_in_attributeOrElementPath889 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_attributeOrElementPath893 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_attributeOrElement_in_attributeOrElementPath895 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_26_in_attributeOrElement916 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_nameTest_in_attributeOrElement922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_nameTest946 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_nameTest949 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NCNAME_in_nameTest953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_xmlnsToken971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_orToken981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_andToken991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_notToken1001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_trueToken1011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_falseToken1021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_eqToken1031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_neToken1041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_textToken1051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_ltToken1061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_lteToken1071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_gtToken1081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_gteToken1091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_naNToken1101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_infToken1111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_xsToken1121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_dateToken1131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_dateTimeToken1141 = new BitSet(new long[]{0x0000000000000002L});

}