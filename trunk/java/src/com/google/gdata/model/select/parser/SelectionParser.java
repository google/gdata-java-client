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

import com.google.gdata.model.Path;
import com.google.gdata.model.PathException;
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
    // java/com/google/gdata/model/select/parser/Selection.g:86:1: selectionExpr returns [List<ElementSelector> value] : ( namespaceDeclarations )? s= elementSelectors EOF ;
    public final List<ElementSelector> selectionExpr() throws RecognitionException {
        List<ElementSelector> value = null;

        List<ElementSelector> s = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:87:3: ( ( namespaceDeclarations )? s= elementSelectors EOF )
            // java/com/google/gdata/model/select/parser/Selection.g:87:5: ( namespaceDeclarations )? s= elementSelectors EOF
            {
            // java/com/google/gdata/model/select/parser/Selection.g:87:5: ( namespaceDeclarations )?
            int alt1=2;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:87:5: namespaceDeclarations
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
    // java/com/google/gdata/model/select/parser/Selection.g:92:1: elementConditionExpr returns [ElementCondition value] : e= orExpr EOF ;
    public final ElementCondition elementConditionExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition e = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:93:3: (e= orExpr EOF )
            // java/com/google/gdata/model/select/parser/Selection.g:93:5: e= orExpr EOF
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
    // java/com/google/gdata/model/select/parser/Selection.g:99:1: namespaceDeclarations : namespaceDeclaration ( namespaceDeclarations )? ;
    public final void namespaceDeclarations() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:100:3: ( namespaceDeclaration ( namespaceDeclarations )? )
            // java/com/google/gdata/model/select/parser/Selection.g:100:5: namespaceDeclaration ( namespaceDeclarations )?
            {
            pushFollow(FOLLOW_namespaceDeclaration_in_namespaceDeclarations112);
            namespaceDeclaration();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:100:26: ( namespaceDeclarations )?
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:100:27: namespaceDeclarations
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
    // java/com/google/gdata/model/select/parser/Selection.g:103:1: namespaceDeclaration : xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL ;
    public final void namespaceDeclaration() throws RecognitionException {
        Token alias=null;
        Token uri=null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:104:3: ( xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL )
            // java/com/google/gdata/model/select/parser/Selection.g:104:5: xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL
            {
            pushFollow(FOLLOW_xmlnsToken_in_namespaceDeclaration130);
            xmlnsToken();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:104:16: ( ':' alias= NCNAME )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==13) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:104:18: ':' alias= NCNAME
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
    // java/com/google/gdata/model/select/parser/Selection.g:109:1: elementSelectors returns [List<ElementSelector> value] : (first= elementSelector ) ( ',' more= elementSelector )* ;
    public final List<ElementSelector> elementSelectors() throws RecognitionException {
        List<ElementSelector> value = null;

        ElementSelector first = null;

        ElementSelector more = null;



            List<ElementSelector> selectors = new ArrayList<ElementSelector>();
            value = selectors;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:114:3: ( (first= elementSelector ) ( ',' more= elementSelector )* )
            // java/com/google/gdata/model/select/parser/Selection.g:114:5: (first= elementSelector ) ( ',' more= elementSelector )*
            {
            // java/com/google/gdata/model/select/parser/Selection.g:114:5: (first= elementSelector )
            // java/com/google/gdata/model/select/parser/Selection.g:114:6: first= elementSelector
            {
            pushFollow(FOLLOW_elementSelector_in_elementSelectors177);
            first=elementSelector();

            state._fsp--;

             selectors.add(first); 

            }

            // java/com/google/gdata/model/select/parser/Selection.g:115:5: ( ',' more= elementSelector )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==15) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:115:6: ',' more= elementSelector
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
    // java/com/google/gdata/model/select/parser/Selection.g:118:1: elementSelector returns [ElementSelector value] : p= selectionPathExpr ( '[' c= orExpr ']' )? ( '(' s= elementSelectors ')' )? ;
    public final ElementSelector elementSelector() throws RecognitionException {
        ElementSelector value = null;

        Path p = null;

        ElementCondition c = null;

        List<ElementSelector> s = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:119:3: (p= selectionPathExpr ( '[' c= orExpr ']' )? ( '(' s= elementSelectors ')' )? )
            // java/com/google/gdata/model/select/parser/Selection.g:119:5: p= selectionPathExpr ( '[' c= orExpr ']' )? ( '(' s= elementSelectors ')' )?
            {
            pushFollow(FOLLOW_selectionPathExpr_in_elementSelector217);
            p=selectionPathExpr();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:120:5: ( '[' c= orExpr ']' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==16) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:120:6: '[' c= orExpr ']'
                    {
                    match(input,16,FOLLOW_16_in_elementSelector224); 
                    pushFollow(FOLLOW_orExpr_in_elementSelector228);
                    c=orExpr();

                    state._fsp--;

                    match(input,17,FOLLOW_17_in_elementSelector230); 

                    }
                    break;

            }

            // java/com/google/gdata/model/select/parser/Selection.g:121:5: ( '(' s= elementSelectors ')' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==18) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:121:6: '(' s= elementSelectors ')'
                    {
                    match(input,18,FOLLOW_18_in_elementSelector240); 
                    pushFollow(FOLLOW_elementSelectors_in_elementSelector244);
                    s=elementSelectors();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_elementSelector246); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:126:1: orExpr returns [ElementCondition value] : left= andExpr ( orToken right= andExpr )* ;
    public final ElementCondition orExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition left = null;

        ElementCondition right = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:127:3: (left= andExpr ( orToken right= andExpr )* )
            // java/com/google/gdata/model/select/parser/Selection.g:127:5: left= andExpr ( orToken right= andExpr )*
            {
            pushFollow(FOLLOW_andExpr_in_orExpr273);
            left=andExpr();

            state._fsp--;

             value = left; 
            // java/com/google/gdata/model/select/parser/Selection.g:128:5: ( orToken right= andExpr )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==NCNAME) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:128:6: orToken right= andExpr
            	    {
            	    pushFollow(FOLLOW_orToken_in_orExpr283);
            	    orToken();

            	    state._fsp--;

            	    pushFollow(FOLLOW_andExpr_in_orExpr287);
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
    // java/com/google/gdata/model/select/parser/Selection.g:131:1: andExpr returns [ElementCondition value] : left= finalExpr ( andToken right= finalExpr )* ;
    public final ElementCondition andExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition left = null;

        ElementCondition right = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:132:3: (left= finalExpr ( andToken right= finalExpr )* )
            // java/com/google/gdata/model/select/parser/Selection.g:132:5: left= finalExpr ( andToken right= finalExpr )*
            {
            pushFollow(FOLLOW_finalExpr_in_andExpr310);
            left=finalExpr();

            state._fsp--;

             value = left; 
            // java/com/google/gdata/model/select/parser/Selection.g:133:5: ( andToken right= finalExpr )*
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
            	    // java/com/google/gdata/model/select/parser/Selection.g:133:6: andToken right= finalExpr
            	    {
            	    pushFollow(FOLLOW_andToken_in_andExpr319);
            	    andToken();

            	    state._fsp--;

            	    pushFollow(FOLLOW_finalExpr_in_andExpr323);
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
    // java/com/google/gdata/model/select/parser/Selection.g:136:1: finalExpr returns [ElementCondition value] : ( ( notToken '(' notl= orExpr ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr ')' ) | (c= comparisonOrExistenceExpr ) );
    public final ElementCondition finalExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition notl = null;

        ElementCondition l = null;

        ElementCondition c = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:137:3: ( ( notToken '(' notl= orExpr ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr ')' ) | (c= comparisonOrExistenceExpr ) )
            int alt9=5;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:137:5: ( notToken '(' notl= orExpr ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:137:5: ( notToken '(' notl= orExpr ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:137:6: notToken '(' notl= orExpr ')'
                    {
                    pushFollow(FOLLOW_notToken_in_finalExpr345);
                    notToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr347); 
                    pushFollow(FOLLOW_orExpr_in_finalExpr351);
                    notl=orExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_finalExpr353); 
                     value = new NotCondition(notl); 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:138:5: ( trueToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:138:5: ( trueToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:138:6: trueToken '(' ')'
                    {
                    pushFollow(FOLLOW_trueToken_in_finalExpr363);
                    trueToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr365); 
                    match(input,19,FOLLOW_19_in_finalExpr367); 
                     value = ElementCondition.ALL; 

                    }


                    }
                    break;
                case 3 :
                    // java/com/google/gdata/model/select/parser/Selection.g:139:5: ( falseToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:139:5: ( falseToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:139:6: falseToken '(' ')'
                    {
                    pushFollow(FOLLOW_falseToken_in_finalExpr377);
                    falseToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr379); 
                    match(input,19,FOLLOW_19_in_finalExpr381); 
                     value = ElementCondition.NONE; 

                    }


                    }
                    break;
                case 4 :
                    // java/com/google/gdata/model/select/parser/Selection.g:140:5: ( '(' l= orExpr ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:140:5: ( '(' l= orExpr ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:140:6: '(' l= orExpr ')'
                    {
                    match(input,18,FOLLOW_18_in_finalExpr391); 
                    pushFollow(FOLLOW_orExpr_in_finalExpr395);
                    l=orExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_finalExpr397); 
                     value = l; 

                    }


                    }
                    break;
                case 5 :
                    // java/com/google/gdata/model/select/parser/Selection.g:141:5: (c= comparisonOrExistenceExpr )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:141:5: (c= comparisonOrExistenceExpr )
                    // java/com/google/gdata/model/select/parser/Selection.g:141:6: c= comparisonOrExistenceExpr
                    {
                    pushFollow(FOLLOW_comparisonOrExistenceExpr_in_finalExpr409);
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
    // java/com/google/gdata/model/select/parser/Selection.g:144:1: comparisonOrExistenceExpr returns [ElementCondition value] : (d= dateOrDateTimeComparison | o= otherComparison );
    public final ElementCondition comparisonOrExistenceExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition d = null;

        ElementCondition o = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:145:5: (d= dateOrDateTimeComparison | o= otherComparison )
            int alt10=2;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:145:7: d= dateOrDateTimeComparison
                    {
                    pushFollow(FOLLOW_dateOrDateTimeComparison_in_comparisonOrExistenceExpr433);
                    d=dateOrDateTimeComparison();

                    state._fsp--;

                     value = d; 

                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:146:7: o= otherComparison
                    {
                    pushFollow(FOLLOW_otherComparison_in_comparisonOrExistenceExpr445);
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
    // java/com/google/gdata/model/select/parser/Selection.g:149:1: dateOrDateTimeComparison returns [ElementCondition value] : xsToken ':' ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) ) ;
    public final ElementCondition dateOrDateTimeComparison() throws RecognitionException {
        ElementCondition value = null;

        Token str=null;
        Path p = null;

        Operation c = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:150:5: ( xsToken ':' ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) ) )
            // java/com/google/gdata/model/select/parser/Selection.g:150:7: xsToken ':' ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) )
            {
            pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison469);
            xsToken();

            state._fsp--;

            match(input,13,FOLLOW_13_in_dateOrDateTimeComparison471); 
            // java/com/google/gdata/model/select/parser/Selection.g:151:9: ( ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) )
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
                    // java/com/google/gdata/model/select/parser/Selection.g:151:10: ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:151:10: ( dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:151:11: dateToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')'
                    {
                    pushFollow(FOLLOW_dateToken_in_dateOrDateTimeComparison484);
                    dateToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison486); 
                    pushFollow(FOLLOW_predicateExpr_in_dateOrDateTimeComparison490);
                    p=predicateExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison492); 
                    pushFollow(FOLLOW_comparator_in_dateOrDateTimeComparison496);
                    c=comparator();

                    state._fsp--;

                    pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison511);
                    xsToken();

                    state._fsp--;

                    match(input,13,FOLLOW_13_in_dateOrDateTimeComparison513); 
                    pushFollow(FOLLOW_dateToken_in_dateOrDateTimeComparison515);
                    dateToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison517); 
                    str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison521); 
                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison523); 

                    }


                                value = buildDateComparisonExpression(p, c, str);
                              

                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:155:11: ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:155:11: ( dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:155:12: dateTimeToken '(' p= predicateExpr ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')'
                    {
                    pushFollow(FOLLOW_dateTimeToken_in_dateOrDateTimeComparison539);
                    dateTimeToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison541); 
                    pushFollow(FOLLOW_predicateExpr_in_dateOrDateTimeComparison545);
                    p=predicateExpr();

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison547); 
                    pushFollow(FOLLOW_comparator_in_dateOrDateTimeComparison551);
                    c=comparator();

                    state._fsp--;

                    pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison566);
                    xsToken();

                    state._fsp--;

                    match(input,13,FOLLOW_13_in_dateOrDateTimeComparison568); 
                    pushFollow(FOLLOW_dateTimeToken_in_dateOrDateTimeComparison570);
                    dateTimeToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison572); 
                    str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison576); 
                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison578); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:162:1: otherComparison returns [ElementCondition value] : p= predicateExpr (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )? ;
    public final ElementCondition otherComparison() throws RecognitionException {
        ElementCondition value = null;

        Token str=null;
        Token num=null;
        Path p = null;

        Operation c = null;



            boolean nan = false;
            boolean inf = false;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:167:3: (p= predicateExpr (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )? )
            // java/com/google/gdata/model/select/parser/Selection.g:167:5: p= predicateExpr (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?
            {
            pushFollow(FOLLOW_predicateExpr_in_otherComparison614);
            p=predicateExpr();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:167:21: (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?
            int alt13=2;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:167:22: c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )
                    {
                    pushFollow(FOLLOW_comparator_in_otherComparison619);
                    c=comparator();

                    state._fsp--;

                    // java/com/google/gdata/model/select/parser/Selection.g:167:35: (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )
                    int alt12=4;
                    alt12 = dfa12.predict(input);
                    switch (alt12) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:167:36: str= STRINGLITERAL
                            {
                            str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_otherComparison624); 

                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:168:9: num= NUMBER
                            {
                            num=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_otherComparison637); 

                            }
                            break;
                        case 3 :
                            // java/com/google/gdata/model/select/parser/Selection.g:169:9: ( naNToken )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:169:9: ( naNToken )
                            // java/com/google/gdata/model/select/parser/Selection.g:169:10: naNToken
                            {
                            pushFollow(FOLLOW_naNToken_in_otherComparison649);
                            naNToken();

                            state._fsp--;

                             nan = true; 

                            }


                            }
                            break;
                        case 4 :
                            // java/com/google/gdata/model/select/parser/Selection.g:170:9: ( infToken )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:170:9: ( infToken )
                            // java/com/google/gdata/model/select/parser/Selection.g:170:10: infToken
                            {
                            pushFollow(FOLLOW_infToken_in_otherComparison664);
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
                  value = new ExistenceCondition(p);
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
    // java/com/google/gdata/model/select/parser/Selection.g:186:1: comparator returns [Operation value] : ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) );
    public final Operation comparator() throws RecognitionException {
        Operation value = null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:187:3: ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) )
            int alt20=6;
            alt20 = dfa20.predict(input);
            switch (alt20) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:187:5: ( ( eqToken | '=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:187:5: ( ( eqToken | '=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:187:6: ( eqToken | '=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:187:6: ( eqToken | '=' )
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
                            // java/com/google/gdata/model/select/parser/Selection.g:187:7: eqToken
                            {
                            pushFollow(FOLLOW_eqToken_in_comparator691);
                            eqToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:187:17: '='
                            {
                            match(input,14,FOLLOW_14_in_comparator695); 

                            }
                            break;

                    }

                     value = Operation.EQ; 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:188:5: ( ( neToken | '!=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:188:5: ( ( neToken | '!=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:188:6: ( neToken | '!=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:188:6: ( neToken | '!=' )
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
                            // java/com/google/gdata/model/select/parser/Selection.g:188:7: neToken
                            {
                            pushFollow(FOLLOW_neToken_in_comparator707);
                            neToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:188:17: '!='
                            {
                            match(input,20,FOLLOW_20_in_comparator711); 

                            }
                            break;

                    }

                     value = Operation.NEQ; 

                    }


                    }
                    break;
                case 3 :
                    // java/com/google/gdata/model/select/parser/Selection.g:189:5: ( ( gtToken | '>' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:189:5: ( ( gtToken | '>' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:189:6: ( gtToken | '>' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:189:6: ( gtToken | '>' )
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
                            // java/com/google/gdata/model/select/parser/Selection.g:189:7: gtToken
                            {
                            pushFollow(FOLLOW_gtToken_in_comparator723);
                            gtToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:189:17: '>'
                            {
                            match(input,21,FOLLOW_21_in_comparator727); 

                            }
                            break;

                    }

                     value = Operation.GT; 

                    }


                    }
                    break;
                case 4 :
                    // java/com/google/gdata/model/select/parser/Selection.g:190:5: ( ( gteToken | '>=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:190:5: ( ( gteToken | '>=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:190:6: ( gteToken | '>=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:190:6: ( gteToken | '>=' )
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
                            // java/com/google/gdata/model/select/parser/Selection.g:190:7: gteToken
                            {
                            pushFollow(FOLLOW_gteToken_in_comparator739);
                            gteToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:190:18: '>='
                            {
                            match(input,22,FOLLOW_22_in_comparator743); 

                            }
                            break;

                    }

                     value = Operation.GTE; 

                    }


                    }
                    break;
                case 5 :
                    // java/com/google/gdata/model/select/parser/Selection.g:191:5: ( ( ltToken | '<' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:191:5: ( ( ltToken | '<' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:191:6: ( ltToken | '<' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:191:6: ( ltToken | '<' )
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
                            // java/com/google/gdata/model/select/parser/Selection.g:191:7: ltToken
                            {
                            pushFollow(FOLLOW_ltToken_in_comparator755);
                            ltToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:191:17: '<'
                            {
                            match(input,23,FOLLOW_23_in_comparator759); 

                            }
                            break;

                    }

                     value = Operation.LT; 

                    }


                    }
                    break;
                case 6 :
                    // java/com/google/gdata/model/select/parser/Selection.g:192:5: ( ( lteToken | '<=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:192:5: ( ( lteToken | '<=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:192:6: ( lteToken | '<=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:192:6: ( lteToken | '<=' )
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
                            // java/com/google/gdata/model/select/parser/Selection.g:192:7: lteToken
                            {
                            pushFollow(FOLLOW_lteToken_in_comparator771);
                            lteToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:192:18: '<='
                            {
                            match(input,24,FOLLOW_24_in_comparator775); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:195:1: predicateExpr returns [Path value] : ( (path= selectionPathExpr ) | ( textToken '(' ')' ) );
    public final Path predicateExpr() throws RecognitionException {
        Path value = null;

        Path path = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:196:3: ( (path= selectionPathExpr ) | ( textToken '(' ')' ) )
            int alt21=2;
            alt21 = dfa21.predict(input);
            switch (alt21) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:196:5: (path= selectionPathExpr )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:196:5: (path= selectionPathExpr )
                    // java/com/google/gdata/model/select/parser/Selection.g:196:6: path= selectionPathExpr
                    {
                    pushFollow(FOLLOW_selectionPathExpr_in_predicateExpr799);
                    path=selectionPathExpr();

                    state._fsp--;

                     value = path; 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:197:5: ( textToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:197:5: ( textToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:197:6: textToken '(' ')'
                    {
                    pushFollow(FOLLOW_textToken_in_predicateExpr809);
                    textToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_predicateExpr811); 
                    match(input,19,FOLLOW_19_in_predicateExpr813); 
                     value = Path.ROOT; 

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


    // $ANTLR start "selectionPathExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:200:1: selectionPathExpr returns [Path value] : pathStep[builder] ( '/' pathStep[builder] )* ;
    public final Path selectionPathExpr() throws RecognitionException {
        Path value = null;


            Path.Builder builder = Path.builder();
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:204:3: ( pathStep[builder] ( '/' pathStep[builder] )* )
            // java/com/google/gdata/model/select/parser/Selection.g:204:5: pathStep[builder] ( '/' pathStep[builder] )*
            {
            pushFollow(FOLLOW_pathStep_in_selectionPathExpr842);
            pathStep(builder);

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:204:23: ( '/' pathStep[builder] )*
            loop22:
            do {
                int alt22=2;
                alt22 = dfa22.predict(input);
                switch (alt22) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:204:24: '/' pathStep[builder]
            	    {
            	    match(input,25,FOLLOW_25_in_selectionPathExpr846); 
            	    pushFollow(FOLLOW_pathStep_in_selectionPathExpr848);
            	    pathStep(builder);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


                value = builder.build();
              

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
    // $ANTLR end "selectionPathExpr"


    // $ANTLR start "pathStep"
    // java/com/google/gdata/model/select/parser/Selection.g:209:1: pathStep[Path.Builder value] : (at= '@' )? qname= nameTest[at != null] ;
    public final void pathStep(Path.Builder value) throws RecognitionException {
        Token at=null;
        QName qname = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:210:3: ( (at= '@' )? qname= nameTest[at != null] )
            // java/com/google/gdata/model/select/parser/Selection.g:210:5: (at= '@' )? qname= nameTest[at != null]
            {
            // java/com/google/gdata/model/select/parser/Selection.g:210:5: (at= '@' )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==26) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:210:6: at= '@'
                    {
                    at=(Token)match(input,26,FOLLOW_26_in_pathStep871); 

                    }
                    break;

            }

            pushFollow(FOLLOW_nameTest_in_pathStep877);
            qname=nameTest(at != null);

            state._fsp--;


                boolean isAttribute = at != null;
                boolean added = false;
                try {
                  if (isAttribute) {
                    added = value.addIfAttribute(qname);
                  } else {
                    added = value.addIfElement(qname) ||
                        value.addIfAttribute(qname);
                  }
                  if (!added) {
                    throw new InternalParseException(input.LT(1), 
                        "Invalid path step:" + qname);
                  }
                } catch (PathException pe) {
                  throw new InternalParseException(input.LT(1), pe.getMessage());
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
    // $ANTLR end "pathStep"


    // $ANTLR start "nameTest"
    // java/com/google/gdata/model/select/parser/Selection.g:231:1: nameTest[boolean isAttribute] returns [QName value] : left= NCNAME ( ':' right= NCNAME )? ;
    public final QName nameTest(boolean isAttribute) throws RecognitionException {
        QName value = null;

        Token left=null;
        Token right=null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:232:3: (left= NCNAME ( ':' right= NCNAME )? )
            // java/com/google/gdata/model/select/parser/Selection.g:232:5: left= NCNAME ( ':' right= NCNAME )?
            {
            left=(Token)match(input,NCNAME,FOLLOW_NCNAME_in_nameTest901); 
            // java/com/google/gdata/model/select/parser/Selection.g:232:17: ( ':' right= NCNAME )?
            int alt24=2;
            alt24 = dfa24.predict(input);
            switch (alt24) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:232:18: ':' right= NCNAME
                    {
                    match(input,13,FOLLOW_13_in_nameTest904); 
                    right=(Token)match(input,NCNAME,FOLLOW_NCNAME_in_nameTest908); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:238:1: xmlnsToken : {...}? NCNAME ;
    public final void xmlnsToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:238:12: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:238:14: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("xmlns"))) ) {
                throw new FailedPredicateException(input, "xmlnsToken", "input.LT(1).getText().equals(\"xmlns\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_xmlnsToken926); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:239:1: orToken : {...}? NCNAME ;
    public final void orToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:239:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:239:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("or"))) ) {
                throw new FailedPredicateException(input, "orToken", "input.LT(1).getText().equals(\"or\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_orToken936); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:240:1: andToken : {...}? NCNAME ;
    public final void andToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:240:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:240:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("and"))) ) {
                throw new FailedPredicateException(input, "andToken", "input.LT(1).getText().equals(\"and\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_andToken946); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:241:1: notToken : {...}? NCNAME ;
    public final void notToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:241:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:241:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("not"))) ) {
                throw new FailedPredicateException(input, "notToken", "input.LT(1).getText().equals(\"not\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_notToken956); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:242:1: trueToken : {...}? NCNAME ;
    public final void trueToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:242:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:242:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("true"))) ) {
                throw new FailedPredicateException(input, "trueToken", "input.LT(1).getText().equals(\"true\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_trueToken966); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:243:1: falseToken : {...}? NCNAME ;
    public final void falseToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:243:12: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:243:14: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("false"))) ) {
                throw new FailedPredicateException(input, "falseToken", "input.LT(1).getText().equals(\"false\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_falseToken976); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:244:1: eqToken : {...}? NCNAME ;
    public final void eqToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:244:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:244:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("eq"))) ) {
                throw new FailedPredicateException(input, "eqToken", "input.LT(1).getText().equals(\"eq\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_eqToken986); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:245:1: neToken : {...}? NCNAME ;
    public final void neToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:245:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:245:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("ne"))) ) {
                throw new FailedPredicateException(input, "neToken", "input.LT(1).getText().equals(\"ne\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_neToken996); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:246:1: textToken : {...}? NCNAME ;
    public final void textToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:246:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:246:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("text"))) ) {
                throw new FailedPredicateException(input, "textToken", "input.LT(1).getText().equals(\"text\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_textToken1006); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:247:1: ltToken : {...}? NCNAME ;
    public final void ltToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:247:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:247:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("lt"))) ) {
                throw new FailedPredicateException(input, "ltToken", "input.LT(1).getText().equals(\"lt\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_ltToken1016); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:248:1: lteToken : {...}? NCNAME ;
    public final void lteToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:248:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:248:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("lte"))) ) {
                throw new FailedPredicateException(input, "lteToken", "input.LT(1).getText().equals(\"lte\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_lteToken1026); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:249:1: gtToken : {...}? NCNAME ;
    public final void gtToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:249:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:249:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("gt"))) ) {
                throw new FailedPredicateException(input, "gtToken", "input.LT(1).getText().equals(\"gt\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_gtToken1036); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:250:1: gteToken : {...}? NCNAME ;
    public final void gteToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:250:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:250:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("gte"))) ) {
                throw new FailedPredicateException(input, "gteToken", "input.LT(1).getText().equals(\"gte\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_gteToken1046); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:251:1: naNToken : {...}? NCNAME ;
    public final void naNToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:251:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:251:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("NaN"))) ) {
                throw new FailedPredicateException(input, "naNToken", "input.LT(1).getText().equals(\"NaN\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_naNToken1056); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:252:1: infToken : {...}? NCNAME ;
    public final void infToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:252:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:252:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("INF"))) ) {
                throw new FailedPredicateException(input, "infToken", "input.LT(1).getText().equals(\"INF\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_infToken1066); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:253:1: xsToken : {...}? NCNAME ;
    public final void xsToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:253:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:253:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("xs"))) ) {
                throw new FailedPredicateException(input, "xsToken", "input.LT(1).getText().equals(\"xs\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_xsToken1076); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:254:1: dateToken : {...}? NCNAME ;
    public final void dateToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:254:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:254:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("date"))) ) {
                throw new FailedPredicateException(input, "dateToken", "input.LT(1).getText().equals(\"date\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_dateToken1086); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:255:1: dateTimeToken : {...}? NCNAME ;
    public final void dateTimeToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:255:15: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:255:17: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("dateTime"))) ) {
                throw new FailedPredicateException(input, "dateTimeToken", "input.LT(1).getText().equals(\"dateTime\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_dateTimeToken1096); 

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


    protected DFA1 dfa1 = new DFA1(this);
    protected DFA2 dfa2 = new DFA2(this);
    protected DFA9 dfa9 = new DFA9(this);
    protected DFA10 dfa10 = new DFA10(this);
    protected DFA13 dfa13 = new DFA13(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA20 dfa20 = new DFA20(this);
    protected DFA21 dfa21 = new DFA21(this);
    protected DFA22 dfa22 = new DFA22(this);
    protected DFA24 dfa24 = new DFA24(this);
    static final String DFA1_eotS =
        "\12\uffff";
    static final String DFA1_eofS =
        "\1\uffff\1\2\10\uffff";
    static final String DFA1_minS =
        "\1\4\1\15\1\uffff\1\0\6\uffff";
    static final String DFA1_maxS =
        "\1\32\1\31\1\uffff\1\0\6\uffff";
    static final String DFA1_acceptS =
        "\2\uffff\1\2\6\uffff\1\1";
    static final String DFA1_specialS =
        "\3\uffff\1\0\6\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\1\25\uffff\1\2",
            "\1\3\1\11\2\2\1\uffff\1\2\6\uffff\1\2",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "87:5: ( namespaceDeclarations )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA1_3 = input.LA(1);

                         
                        int index1_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("xmlns"))) ) {s = 9;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index1_3);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 1, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA2_eotS =
        "\12\uffff";
    static final String DFA2_eofS =
        "\1\uffff\1\2\10\uffff";
    static final String DFA2_minS =
        "\1\4\1\15\1\uffff\1\0\6\uffff";
    static final String DFA2_maxS =
        "\1\32\1\31\1\uffff\1\0\6\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\2\6\uffff\1\1";
    static final String DFA2_specialS =
        "\3\uffff\1\0\6\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\1\25\uffff\1\2",
            "\1\3\1\11\2\2\1\uffff\1\2\6\uffff\1\2",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "100:26: ( namespaceDeclarations )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA2_3 = input.LA(1);

                         
                        int index2_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("xmlns"))) ) {s = 9;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index2_3);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 2, _s, input);
            error(nvae);
            throw nvae;
        }
    }
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
            return "136:1: finalExpr returns [ElementCondition value] : ( ( notToken '(' notl= orExpr ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr ')' ) | (c= comparisonOrExistenceExpr ) );";
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
        "\2\4\2\uffff\1\0\14\uffff";
    static final String DFA10_maxS =
        "\1\32\1\31\2\uffff\1\0\14\uffff";
    static final String DFA10_acceptS =
        "\2\uffff\1\2\15\uffff\1\1";
    static final String DFA10_specialS =
        "\4\uffff\1\0\14\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1\25\uffff\1\2",
            "\1\2\10\uffff\1\4\1\2\2\uffff\11\2",
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
            return "144:1: comparisonOrExistenceExpr returns [ElementCondition value] : (d= dateOrDateTimeComparison | o= otherComparison );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA10_4 = input.LA(1);

                         
                        int index10_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("xs"))) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index10_4);
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
            return "167:21: (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?";
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
            return "167:35: (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )";
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
            return "186:1: comparator returns [Operation value] : ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) );";
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
            return "195:1: predicateExpr returns [Path value] : ( (path= selectionPathExpr ) | ( textToken '(' ')' ) );";
        }
    }
    static final String DFA22_eotS =
        "\17\uffff";
    static final String DFA22_eofS =
        "\1\1\16\uffff";
    static final String DFA22_minS =
        "\1\4\16\uffff";
    static final String DFA22_maxS =
        "\1\31\16\uffff";
    static final String DFA22_acceptS =
        "\1\uffff\1\2\14\uffff\1\1";
    static final String DFA22_specialS =
        "\17\uffff}>";
    static final String[] DFA22_transitionS = {
            "\1\1\11\uffff\13\1\1\16",
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

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "()* loopback of 204:23: ( '/' pathStep[builder] )*";
        }
    }
    static final String DFA24_eotS =
        "\20\uffff";
    static final String DFA24_eofS =
        "\1\2\17\uffff";
    static final String DFA24_minS =
        "\1\4\17\uffff";
    static final String DFA24_maxS =
        "\1\31\17\uffff";
    static final String DFA24_acceptS =
        "\1\uffff\1\1\1\2\15\uffff";
    static final String DFA24_specialS =
        "\20\uffff}>";
    static final String[] DFA24_transitionS = {
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

    static final short[] DFA24_eot = DFA.unpackEncodedString(DFA24_eotS);
    static final short[] DFA24_eof = DFA.unpackEncodedString(DFA24_eofS);
    static final char[] DFA24_min = DFA.unpackEncodedStringToUnsignedChars(DFA24_minS);
    static final char[] DFA24_max = DFA.unpackEncodedStringToUnsignedChars(DFA24_maxS);
    static final short[] DFA24_accept = DFA.unpackEncodedString(DFA24_acceptS);
    static final short[] DFA24_special = DFA.unpackEncodedString(DFA24_specialS);
    static final short[][] DFA24_transition;

    static {
        int numStates = DFA24_transitionS.length;
        DFA24_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA24_transition[i] = DFA.unpackEncodedString(DFA24_transitionS[i]);
        }
    }

    class DFA24 extends DFA {

        public DFA24(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 24;
            this.eot = DFA24_eot;
            this.eof = DFA24_eof;
            this.min = DFA24_min;
            this.max = DFA24_max;
            this.accept = DFA24_accept;
            this.special = DFA24_special;
            this.transition = DFA24_transition;
        }
        public String getDescription() {
            return "232:17: ( ':' right= NCNAME )?";
        }
    }
 

    public static final BitSet FOLLOW_namespaceDeclarations_in_selectionExpr66 = new BitSet(new long[]{0x0000000004000010L});
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
    public static final BitSet FOLLOW_15_in_elementSelectors188 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_elementSelector_in_elementSelectors192 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_selectionPathExpr_in_elementSelector217 = new BitSet(new long[]{0x0000000000050002L});
    public static final BitSet FOLLOW_16_in_elementSelector224 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_orExpr_in_elementSelector228 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_elementSelector230 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_18_in_elementSelector240 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_elementSelectors_in_elementSelector244 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_elementSelector246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpr_in_orExpr273 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_orToken_in_orExpr283 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_andExpr_in_orExpr287 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_finalExpr_in_andExpr310 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_andToken_in_andExpr319 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_finalExpr_in_andExpr323 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_notToken_in_finalExpr345 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr347 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_orExpr_in_finalExpr351 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_trueToken_in_finalExpr363 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr365 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_falseToken_in_finalExpr377 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr379 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_finalExpr391 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_orExpr_in_finalExpr395 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparisonOrExistenceExpr_in_finalExpr409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateOrDateTimeComparison_in_comparisonOrExistenceExpr433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherComparison_in_comparisonOrExistenceExpr445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison469 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison471 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateToken_in_dateOrDateTimeComparison484 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison486 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_predicateExpr_in_dateOrDateTimeComparison490 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison492 = new BitSet(new long[]{0x0000000001F04010L});
    public static final BitSet FOLLOW_comparator_in_dateOrDateTimeComparison496 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison511 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison513 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateToken_in_dateOrDateTimeComparison515 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison517 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison521 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateTimeToken_in_dateOrDateTimeComparison539 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison541 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_predicateExpr_in_dateOrDateTimeComparison545 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison547 = new BitSet(new long[]{0x0000000001F04010L});
    public static final BitSet FOLLOW_comparator_in_dateOrDateTimeComparison551 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison566 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison568 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateTimeToken_in_dateOrDateTimeComparison570 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison572 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison576 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicateExpr_in_otherComparison614 = new BitSet(new long[]{0x0000000001F04012L});
    public static final BitSet FOLLOW_comparator_in_otherComparison619 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_otherComparison624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_otherComparison637 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_naNToken_in_otherComparison649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_infToken_in_otherComparison664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_eqToken_in_comparator691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_comparator695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_neToken_in_comparator707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_comparator711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_gtToken_in_comparator723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_comparator727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_gteToken_in_comparator739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_comparator743 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ltToken_in_comparator755 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_comparator759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lteToken_in_comparator771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_comparator775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectionPathExpr_in_predicateExpr799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_textToken_in_predicateExpr809 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_predicateExpr811 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_predicateExpr813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pathStep_in_selectionPathExpr842 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_selectionPathExpr846 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_pathStep_in_selectionPathExpr848 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_26_in_pathStep871 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_nameTest_in_pathStep877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_nameTest901 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_nameTest904 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NCNAME_in_nameTest908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_xmlnsToken926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_orToken936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_andToken946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_notToken956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_trueToken966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_falseToken976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_eqToken986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_neToken996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_textToken1006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_ltToken1016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_lteToken1026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_gtToken1036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_gteToken1046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_naNToken1056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_infToken1066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_xsToken1076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_dateToken1086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_dateTimeToken1096 = new BitSet(new long[]{0x0000000000000002L});

}