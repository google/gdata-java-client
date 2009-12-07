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
// $ANTLR 3.2 Sep 23, 2009 12:02:23 java/com/google/gdata/model/select/parser/Selection.g 2009-11-18 01:32:27

package com.google.gdata.model.select.parser;

import com.google.gdata.model.ElementKey;
import com.google.gdata.model.Path;
import com.google.gdata.model.PathException;
import com.google.gdata.model.QName;
import com.google.gdata.model.select.ElementCondition;
import com.google.gdata.model.select.ElementSelector;
import com.google.gdata.model.select.ExistenceCondition;
import com.google.gdata.model.select.NotCondition;
import com.google.gdata.model.select.Selector;
import com.google.gdata.model.select.ValueMatcher.Operation;

import java.util.ArrayList;
import java.util.List;

import static com.google.gdata.model.select.parser.ParserUtil.and;
import static com.google.gdata.model.select.parser.ParserUtil.buildDateComparisonExpression;
import static com.google.gdata.model.select.parser.ParserUtil.buildDateTimeComparisonExpression;
import static com.google.gdata.model.select.parser.ParserUtil.buildNumberComparisonExpression;
import static com.google.gdata.model.select.parser.ParserUtil.buildQName;
import static com.google.gdata.model.select.parser.ParserUtil.buildSelector;
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NCNAME", "STRINGLITERAL", "NUMBER", "WHITESPACE", "SIGN", "DIGIT", "EXP", "LETTER", "NONLETTER", "':'", "'='", "','", "'['", "']'", "'('", "')'", "'!='", "'>'", "'>='", "'<'", "'<='", "'/'", "'@'", "'*'"
    };
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
    public static final int STRINGLITERAL=5;
    public static final int T__13=13;
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



    /**
     * The ParseMode enum defines the supported selection parsing models.
     */
    enum ParseMode {
      /**
       * The XML parse mode is based upon a simple XPath subset.
       */
      XML,
        
      /**
       * The JSON parse mode relaxes the selection parsing model based upon the
       * JSON syntax.   No namespace prefixes will be used for path elements and
       * path steps can match either attribute or elements (no '@' prefix is
       * required for attribute).
       */
      JSON
    }

    private ParseMode parseMode = ParseMode.XML;

    /**
     * Sets the parsing model for the selection parser.  The default parsing model
     * if not explicitly set is {@link ParseMode.XML}.
     */
    public void setParseMode(ParseMode mode) {
      this.parseMode = mode;
    }

    /** Maps namespace aliases to namespace. */
    private final NamespaceContext namespaces = new NamespaceContext();

    /**
     * Returns the namespace context used by the parser.
     */
    NamespaceContext getNamespaceContext() {
      return namespaces;
    }

    /** 
     * Path to root element type of expression.  Defaults to an empty relative path
     * to enable simple syntax testing without path validation.
     */
    private Path rootPath = Path.ROOT;

    /**
     * Sets the root element type of the expression.  Selections and condition paths
     * will be validated relative to this root (if absolute).
     */
    public void setRootPath(Path rootPath) {
      this.rootPath = rootPath;
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
    // java/com/google/gdata/model/select/parser/Selection.g:131:1: selectionExpr returns [ElementSelector value] : ( namespaceDeclarations )? s= selectors[rootPath] EOF ;
    public final ElementSelector selectionExpr() throws RecognitionException {
        ElementSelector value = null;

        List<Selector> s = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:132:3: ( ( namespaceDeclarations )? s= selectors[rootPath] EOF )
            // java/com/google/gdata/model/select/parser/Selection.g:132:5: ( namespaceDeclarations )? s= selectors[rootPath] EOF
            {
            // java/com/google/gdata/model/select/parser/Selection.g:132:5: ( namespaceDeclarations )?
            int alt1=2;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:132:5: namespaceDeclarations
                    {
                    pushFollow(FOLLOW_namespaceDeclarations_in_selectionExpr66);
                    namespaceDeclarations();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_selectors_in_selectionExpr71);
            s=selectors(rootPath);

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_selectionExpr74); 

                if (rootPath == null || rootPath.getSelectedElement() == null) {
                  value = ElementSelector.from(ElementKey.of(QName.ANY), null, s);
                } else {
                  value = ElementSelector.from(rootPath.getSelectedElement().getKey(), null, s);
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
    // $ANTLR end "selectionExpr"


    // $ANTLR start "elementConditionExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:141:1: elementConditionExpr returns [ElementCondition value] : e= orExpr[rootPath] EOF ;
    public final ElementCondition elementConditionExpr() throws RecognitionException {
        ElementCondition value = null;

        ElementCondition e = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:142:3: (e= orExpr[rootPath] EOF )
            // java/com/google/gdata/model/select/parser/Selection.g:142:5: e= orExpr[rootPath] EOF
            {
            pushFollow(FOLLOW_orExpr_in_elementConditionExpr95);
            e=orExpr(rootPath);

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_elementConditionExpr98); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:148:1: namespaceDeclarations : namespaceDeclaration ( namespaceDeclarations )? ;
    public final void namespaceDeclarations() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:149:3: ( namespaceDeclaration ( namespaceDeclarations )? )
            // java/com/google/gdata/model/select/parser/Selection.g:149:5: namespaceDeclaration ( namespaceDeclarations )?
            {
            pushFollow(FOLLOW_namespaceDeclaration_in_namespaceDeclarations114);
            namespaceDeclaration();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:149:26: ( namespaceDeclarations )?
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:149:27: namespaceDeclarations
                    {
                    pushFollow(FOLLOW_namespaceDeclarations_in_namespaceDeclarations117);
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
    // java/com/google/gdata/model/select/parser/Selection.g:152:1: namespaceDeclaration : xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL ;
    public final void namespaceDeclaration() throws RecognitionException {
        Token alias=null;
        Token uri=null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:153:3: ( xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL )
            // java/com/google/gdata/model/select/parser/Selection.g:153:5: xmlnsToken ( ':' alias= NCNAME )? '=' uri= STRINGLITERAL
            {
            pushFollow(FOLLOW_xmlnsToken_in_namespaceDeclaration132);
            xmlnsToken();

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:153:16: ( ':' alias= NCNAME )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==13) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:153:18: ':' alias= NCNAME
                    {
                    match(input,13,FOLLOW_13_in_namespaceDeclaration136); 
                    alias=(Token)match(input,NCNAME,FOLLOW_NCNAME_in_namespaceDeclaration140); 

                    }
                    break;

            }

            match(input,14,FOLLOW_14_in_namespaceDeclaration144); 
            uri=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_namespaceDeclaration148); 

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


    // $ANTLR start "selectors"
    // java/com/google/gdata/model/select/parser/Selection.g:158:1: selectors[Path path] returns [List<Selector> value] : (first= selector[path] ( ',' more= selector[path] )* )? ;
    public final List<Selector> selectors(Path path) throws RecognitionException {
        List<Selector> value = null;

        Selector first = null;

        Selector more = null;



            List<Selector> selectors = new ArrayList<Selector>();
            value = selectors;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:163:3: ( (first= selector[path] ( ',' more= selector[path] )* )? )
            // java/com/google/gdata/model/select/parser/Selection.g:163:5: (first= selector[path] ( ',' more= selector[path] )* )?
            {
            // java/com/google/gdata/model/select/parser/Selection.g:163:5: (first= selector[path] ( ',' more= selector[path] )* )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==NCNAME||(LA5_0>=26 && LA5_0<=27)) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:163:6: first= selector[path] ( ',' more= selector[path] )*
                    {
                    pushFollow(FOLLOW_selector_in_selectors181);
                    first=selector(path);

                    state._fsp--;

                     selectors.add(first); 
                    // java/com/google/gdata/model/select/parser/Selection.g:164:7: ( ',' more= selector[path] )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==15) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // java/com/google/gdata/model/select/parser/Selection.g:164:8: ',' more= selector[path]
                    	    {
                    	    match(input,15,FOLLOW_15_in_selectors194); 
                    	    pushFollow(FOLLOW_selector_in_selectors198);
                    	    more=selector(path);

                    	    state._fsp--;

                    	     selectors.add(more); 

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


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
    // $ANTLR end "selectors"


    // $ANTLR start "selector"
    // java/com/google/gdata/model/select/parser/Selection.g:167:1: selector[Path current] returns [Selector value] : p= selectionPathExpr[current] ( '[' c= orExpr[$p.value] ']' )? ( '(' s= selectors[$p.value] ')' )? ;
    public final Selector selector(Path current) throws RecognitionException {
        Selector value = null;

        Path p = null;

        ElementCondition c = null;

        List<Selector> s = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:168:3: (p= selectionPathExpr[current] ( '[' c= orExpr[$p.value] ']' )? ( '(' s= selectors[$p.value] ')' )? )
            // java/com/google/gdata/model/select/parser/Selection.g:168:5: p= selectionPathExpr[current] ( '[' c= orExpr[$p.value] ']' )? ( '(' s= selectors[$p.value] ')' )?
            {
            pushFollow(FOLLOW_selectionPathExpr_in_selector228);
            p=selectionPathExpr(current);

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:169:5: ( '[' c= orExpr[$p.value] ']' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==16) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:169:6: '[' c= orExpr[$p.value] ']'
                    {
                    match(input,16,FOLLOW_16_in_selector236); 
                    pushFollow(FOLLOW_orExpr_in_selector240);
                    c=orExpr(p);

                    state._fsp--;

                    match(input,17,FOLLOW_17_in_selector243); 

                    }
                    break;

            }

            // java/com/google/gdata/model/select/parser/Selection.g:170:5: ( '(' s= selectors[$p.value] ')' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==18) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:170:6: '(' s= selectors[$p.value] ')'
                    {
                    match(input,18,FOLLOW_18_in_selector253); 
                    pushFollow(FOLLOW_selectors_in_selector257);
                    s=selectors(p);

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_selector260); 

                    }
                    break;

            }


                value = buildSelector(p, c, s, input.LT(1));
              

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
    // $ANTLR end "selector"


    // $ANTLR start "orExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:175:1: orExpr[Path current] returns [ElementCondition value] : left= andExpr[current] ( orToken right= andExpr[current] )* ;
    public final ElementCondition orExpr(Path current) throws RecognitionException {
        ElementCondition value = null;

        ElementCondition left = null;

        ElementCondition right = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:176:3: (left= andExpr[current] ( orToken right= andExpr[current] )* )
            // java/com/google/gdata/model/select/parser/Selection.g:176:5: left= andExpr[current] ( orToken right= andExpr[current] )*
            {
            pushFollow(FOLLOW_andExpr_in_orExpr289);
            left=andExpr(current);

            state._fsp--;

             value = left; 
            // java/com/google/gdata/model/select/parser/Selection.g:177:5: ( orToken right= andExpr[current] )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==NCNAME) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:177:6: orToken right= andExpr[current]
            	    {
            	    pushFollow(FOLLOW_orToken_in_orExpr300);
            	    orToken();

            	    state._fsp--;

            	    pushFollow(FOLLOW_andExpr_in_orExpr304);
            	    right=andExpr(current);

            	    state._fsp--;

            	     value = or(value, right); 

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
    // $ANTLR end "orExpr"


    // $ANTLR start "andExpr"
    // java/com/google/gdata/model/select/parser/Selection.g:180:1: andExpr[Path current] returns [ElementCondition value] : left= finalExpr[current] ( andToken right= finalExpr[current] )* ;
    public final ElementCondition andExpr(Path current) throws RecognitionException {
        ElementCondition value = null;

        ElementCondition left = null;

        ElementCondition right = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:181:3: (left= finalExpr[current] ( andToken right= finalExpr[current] )* )
            // java/com/google/gdata/model/select/parser/Selection.g:181:5: left= finalExpr[current] ( andToken right= finalExpr[current] )*
            {
            pushFollow(FOLLOW_finalExpr_in_andExpr330);
            left=finalExpr(current);

            state._fsp--;

             value = left; 
            // java/com/google/gdata/model/select/parser/Selection.g:182:5: ( andToken right= finalExpr[current] )*
            loop9:
            do {
                int alt9=2;
                alt9 = dfa9.predict(input);
                switch (alt9) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:182:6: andToken right= finalExpr[current]
            	    {
            	    pushFollow(FOLLOW_andToken_in_andExpr340);
            	    andToken();

            	    state._fsp--;

            	    pushFollow(FOLLOW_finalExpr_in_andExpr344);
            	    right=finalExpr(current);

            	    state._fsp--;

            	     value = and(value, right); 

            	    }
            	    break;

            	default :
            	    break loop9;
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
    // java/com/google/gdata/model/select/parser/Selection.g:185:1: finalExpr[Path current] returns [ElementCondition value] : ( ( notToken '(' notl= orExpr[current] ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr[current] ')' ) | (c= comparisonOrExistenceExpr[current] ) );
    public final ElementCondition finalExpr(Path current) throws RecognitionException {
        ElementCondition value = null;

        ElementCondition notl = null;

        ElementCondition l = null;

        ElementCondition c = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:186:3: ( ( notToken '(' notl= orExpr[current] ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr[current] ')' ) | (c= comparisonOrExistenceExpr[current] ) )
            int alt10=5;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:186:5: ( notToken '(' notl= orExpr[current] ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:186:5: ( notToken '(' notl= orExpr[current] ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:186:6: notToken '(' notl= orExpr[current] ')'
                    {
                    pushFollow(FOLLOW_notToken_in_finalExpr369);
                    notToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr371); 
                    pushFollow(FOLLOW_orExpr_in_finalExpr375);
                    notl=orExpr(current);

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_finalExpr378); 
                     value = new NotCondition(notl); 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:187:5: ( trueToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:187:5: ( trueToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:187:6: trueToken '(' ')'
                    {
                    pushFollow(FOLLOW_trueToken_in_finalExpr388);
                    trueToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr390); 
                    match(input,19,FOLLOW_19_in_finalExpr392); 
                     value = ElementCondition.ALL; 

                    }


                    }
                    break;
                case 3 :
                    // java/com/google/gdata/model/select/parser/Selection.g:188:5: ( falseToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:188:5: ( falseToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:188:6: falseToken '(' ')'
                    {
                    pushFollow(FOLLOW_falseToken_in_finalExpr402);
                    falseToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_finalExpr404); 
                    match(input,19,FOLLOW_19_in_finalExpr406); 
                     value = ElementCondition.NONE; 

                    }


                    }
                    break;
                case 4 :
                    // java/com/google/gdata/model/select/parser/Selection.g:189:5: ( '(' l= orExpr[current] ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:189:5: ( '(' l= orExpr[current] ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:189:6: '(' l= orExpr[current] ')'
                    {
                    match(input,18,FOLLOW_18_in_finalExpr416); 
                    pushFollow(FOLLOW_orExpr_in_finalExpr420);
                    l=orExpr(current);

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_finalExpr423); 
                     value = l; 

                    }


                    }
                    break;
                case 5 :
                    // java/com/google/gdata/model/select/parser/Selection.g:190:5: (c= comparisonOrExistenceExpr[current] )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:190:5: (c= comparisonOrExistenceExpr[current] )
                    // java/com/google/gdata/model/select/parser/Selection.g:190:6: c= comparisonOrExistenceExpr[current]
                    {
                    pushFollow(FOLLOW_comparisonOrExistenceExpr_in_finalExpr435);
                    c=comparisonOrExistenceExpr(current);

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
    // java/com/google/gdata/model/select/parser/Selection.g:193:1: comparisonOrExistenceExpr[Path current] returns [ElementCondition value] : (d= dateOrDateTimeComparison[current] | o= otherComparison[current] );
    public final ElementCondition comparisonOrExistenceExpr(Path current) throws RecognitionException {
        ElementCondition value = null;

        ElementCondition d = null;

        ElementCondition o = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:194:5: (d= dateOrDateTimeComparison[current] | o= otherComparison[current] )
            int alt11=2;
            alt11 = dfa11.predict(input);
            switch (alt11) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:194:7: d= dateOrDateTimeComparison[current]
                    {
                    pushFollow(FOLLOW_dateOrDateTimeComparison_in_comparisonOrExistenceExpr462);
                    d=dateOrDateTimeComparison(current);

                    state._fsp--;

                     value = d; 

                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:195:7: o= otherComparison[current]
                    {
                    pushFollow(FOLLOW_otherComparison_in_comparisonOrExistenceExpr475);
                    o=otherComparison(current);

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
    // java/com/google/gdata/model/select/parser/Selection.g:198:1: dateOrDateTimeComparison[Path current] returns [ElementCondition value] : xsToken ':' ( ( dateToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) ) ;
    public final ElementCondition dateOrDateTimeComparison(Path current) throws RecognitionException {
        ElementCondition value = null;

        Token str=null;
        Path p = null;

        Operation c = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:199:5: ( xsToken ':' ( ( dateToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) ) )
            // java/com/google/gdata/model/select/parser/Selection.g:199:7: xsToken ':' ( ( dateToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) )
            {
            pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison502);
            xsToken();

            state._fsp--;

            match(input,13,FOLLOW_13_in_dateOrDateTimeComparison504); 
            // java/com/google/gdata/model/select/parser/Selection.g:200:9: ( ( dateToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' ) | ( dateTimeToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==NCNAME) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==18) ) {
                    int LA12_2 = input.LA(3);

                    if ( ((input.LT(1).getText().equals("date"))) ) {
                        alt12=1;
                    }
                    else if ( ((input.LT(1).getText().equals("dateTime"))) ) {
                        alt12=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 12, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:200:10: ( dateToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:200:10: ( dateToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:200:11: dateToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateToken '(' str= STRINGLITERAL ')'
                    {
                    pushFollow(FOLLOW_dateToken_in_dateOrDateTimeComparison517);
                    dateToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison519); 
                    pushFollow(FOLLOW_predicateExpr_in_dateOrDateTimeComparison523);
                    p=predicateExpr(current);

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison526); 
                    pushFollow(FOLLOW_comparator_in_dateOrDateTimeComparison530);
                    c=comparator();

                    state._fsp--;

                    pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison545);
                    xsToken();

                    state._fsp--;

                    match(input,13,FOLLOW_13_in_dateOrDateTimeComparison547); 
                    pushFollow(FOLLOW_dateToken_in_dateOrDateTimeComparison549);
                    dateToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison551); 
                    str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison555); 
                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison557); 

                    }


                                value = buildDateComparisonExpression(p, c, str);
                              

                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:204:11: ( dateTimeToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:204:11: ( dateTimeToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:204:12: dateTimeToken '(' p= predicateExpr[current] ')' c= comparator xsToken ':' dateTimeToken '(' str= STRINGLITERAL ')'
                    {
                    pushFollow(FOLLOW_dateTimeToken_in_dateOrDateTimeComparison573);
                    dateTimeToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison575); 
                    pushFollow(FOLLOW_predicateExpr_in_dateOrDateTimeComparison579);
                    p=predicateExpr(current);

                    state._fsp--;

                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison582); 
                    pushFollow(FOLLOW_comparator_in_dateOrDateTimeComparison586);
                    c=comparator();

                    state._fsp--;

                    pushFollow(FOLLOW_xsToken_in_dateOrDateTimeComparison601);
                    xsToken();

                    state._fsp--;

                    match(input,13,FOLLOW_13_in_dateOrDateTimeComparison603); 
                    pushFollow(FOLLOW_dateTimeToken_in_dateOrDateTimeComparison605);
                    dateTimeToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_dateOrDateTimeComparison607); 
                    str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison611); 
                    match(input,19,FOLLOW_19_in_dateOrDateTimeComparison613); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:211:1: otherComparison[Path current] returns [ElementCondition value] : p= predicateExpr[current] (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )? ;
    public final ElementCondition otherComparison(Path current) throws RecognitionException {
        ElementCondition value = null;

        Token str=null;
        Token num=null;
        Path p = null;

        Operation c = null;



            boolean nan = false;
            boolean inf = false;
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:216:3: (p= predicateExpr[current] (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )? )
            // java/com/google/gdata/model/select/parser/Selection.g:216:5: p= predicateExpr[current] (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?
            {
            pushFollow(FOLLOW_predicateExpr_in_otherComparison651);
            p=predicateExpr(current);

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:216:30: (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?
            int alt14=2;
            alt14 = dfa14.predict(input);
            switch (alt14) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:216:31: c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )
                    {
                    pushFollow(FOLLOW_comparator_in_otherComparison657);
                    c=comparator();

                    state._fsp--;

                    // java/com/google/gdata/model/select/parser/Selection.g:216:44: (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )
                    int alt13=4;
                    alt13 = dfa13.predict(input);
                    switch (alt13) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:216:45: str= STRINGLITERAL
                            {
                            str=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_otherComparison662); 

                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:217:9: num= NUMBER
                            {
                            num=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_otherComparison675); 

                            }
                            break;
                        case 3 :
                            // java/com/google/gdata/model/select/parser/Selection.g:218:9: ( naNToken )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:218:9: ( naNToken )
                            // java/com/google/gdata/model/select/parser/Selection.g:218:10: naNToken
                            {
                            pushFollow(FOLLOW_naNToken_in_otherComparison687);
                            naNToken();

                            state._fsp--;

                             nan = true; 

                            }


                            }
                            break;
                        case 4 :
                            // java/com/google/gdata/model/select/parser/Selection.g:219:9: ( infToken )
                            {
                            // java/com/google/gdata/model/select/parser/Selection.g:219:9: ( infToken )
                            // java/com/google/gdata/model/select/parser/Selection.g:219:10: infToken
                            {
                            pushFollow(FOLLOW_infToken_in_otherComparison702);
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
    // java/com/google/gdata/model/select/parser/Selection.g:235:1: comparator returns [Operation value] : ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) );
    public final Operation comparator() throws RecognitionException {
        Operation value = null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:236:3: ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) )
            int alt21=6;
            alt21 = dfa21.predict(input);
            switch (alt21) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:236:5: ( ( eqToken | '=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:236:5: ( ( eqToken | '=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:236:6: ( eqToken | '=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:236:6: ( eqToken | '=' )
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==NCNAME) ) {
                        alt15=1;
                    }
                    else if ( (LA15_0==14) ) {
                        alt15=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 0, input);

                        throw nvae;
                    }
                    switch (alt15) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:236:7: eqToken
                            {
                            pushFollow(FOLLOW_eqToken_in_comparator729);
                            eqToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:236:17: '='
                            {
                            match(input,14,FOLLOW_14_in_comparator733); 

                            }
                            break;

                    }

                     value = Operation.EQ; 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:237:5: ( ( neToken | '!=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:237:5: ( ( neToken | '!=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:237:6: ( neToken | '!=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:237:6: ( neToken | '!=' )
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==NCNAME) ) {
                        alt16=1;
                    }
                    else if ( (LA16_0==20) ) {
                        alt16=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 0, input);

                        throw nvae;
                    }
                    switch (alt16) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:237:7: neToken
                            {
                            pushFollow(FOLLOW_neToken_in_comparator745);
                            neToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:237:17: '!='
                            {
                            match(input,20,FOLLOW_20_in_comparator749); 

                            }
                            break;

                    }

                     value = Operation.NEQ; 

                    }


                    }
                    break;
                case 3 :
                    // java/com/google/gdata/model/select/parser/Selection.g:238:5: ( ( gtToken | '>' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:238:5: ( ( gtToken | '>' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:238:6: ( gtToken | '>' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:238:6: ( gtToken | '>' )
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==NCNAME) ) {
                        alt17=1;
                    }
                    else if ( (LA17_0==21) ) {
                        alt17=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 0, input);

                        throw nvae;
                    }
                    switch (alt17) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:238:7: gtToken
                            {
                            pushFollow(FOLLOW_gtToken_in_comparator761);
                            gtToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:238:17: '>'
                            {
                            match(input,21,FOLLOW_21_in_comparator765); 

                            }
                            break;

                    }

                     value = Operation.GT; 

                    }


                    }
                    break;
                case 4 :
                    // java/com/google/gdata/model/select/parser/Selection.g:239:5: ( ( gteToken | '>=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:239:5: ( ( gteToken | '>=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:239:6: ( gteToken | '>=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:239:6: ( gteToken | '>=' )
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==NCNAME) ) {
                        alt18=1;
                    }
                    else if ( (LA18_0==22) ) {
                        alt18=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 0, input);

                        throw nvae;
                    }
                    switch (alt18) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:239:7: gteToken
                            {
                            pushFollow(FOLLOW_gteToken_in_comparator777);
                            gteToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:239:18: '>='
                            {
                            match(input,22,FOLLOW_22_in_comparator781); 

                            }
                            break;

                    }

                     value = Operation.GTE; 

                    }


                    }
                    break;
                case 5 :
                    // java/com/google/gdata/model/select/parser/Selection.g:240:5: ( ( ltToken | '<' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:240:5: ( ( ltToken | '<' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:240:6: ( ltToken | '<' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:240:6: ( ltToken | '<' )
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==NCNAME) ) {
                        alt19=1;
                    }
                    else if ( (LA19_0==23) ) {
                        alt19=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 0, input);

                        throw nvae;
                    }
                    switch (alt19) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:240:7: ltToken
                            {
                            pushFollow(FOLLOW_ltToken_in_comparator793);
                            ltToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:240:17: '<'
                            {
                            match(input,23,FOLLOW_23_in_comparator797); 

                            }
                            break;

                    }

                     value = Operation.LT; 

                    }


                    }
                    break;
                case 6 :
                    // java/com/google/gdata/model/select/parser/Selection.g:241:5: ( ( lteToken | '<=' ) )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:241:5: ( ( lteToken | '<=' ) )
                    // java/com/google/gdata/model/select/parser/Selection.g:241:6: ( lteToken | '<=' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:241:6: ( lteToken | '<=' )
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==NCNAME) ) {
                        alt20=1;
                    }
                    else if ( (LA20_0==24) ) {
                        alt20=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 20, 0, input);

                        throw nvae;
                    }
                    switch (alt20) {
                        case 1 :
                            // java/com/google/gdata/model/select/parser/Selection.g:241:7: lteToken
                            {
                            pushFollow(FOLLOW_lteToken_in_comparator809);
                            lteToken();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // java/com/google/gdata/model/select/parser/Selection.g:241:18: '<='
                            {
                            match(input,24,FOLLOW_24_in_comparator813); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:244:1: predicateExpr[Path current] returns [Path value] : ( (path= selectionPathExpr[current] ) | ( textToken '(' ')' ) );
    public final Path predicateExpr(Path current) throws RecognitionException {
        Path value = null;

        Path path = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:245:3: ( (path= selectionPathExpr[current] ) | ( textToken '(' ')' ) )
            int alt22=2;
            alt22 = dfa22.predict(input);
            switch (alt22) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:245:5: (path= selectionPathExpr[current] )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:245:5: (path= selectionPathExpr[current] )
                    // java/com/google/gdata/model/select/parser/Selection.g:245:6: path= selectionPathExpr[current]
                    {
                    pushFollow(FOLLOW_selectionPathExpr_in_predicateExpr839);
                    path=selectionPathExpr(current);

                    state._fsp--;

                     value = path; 

                    }


                    }
                    break;
                case 2 :
                    // java/com/google/gdata/model/select/parser/Selection.g:246:5: ( textToken '(' ')' )
                    {
                    // java/com/google/gdata/model/select/parser/Selection.g:246:5: ( textToken '(' ')' )
                    // java/com/google/gdata/model/select/parser/Selection.g:246:6: textToken '(' ')'
                    {
                    pushFollow(FOLLOW_textToken_in_predicateExpr850);
                    textToken();

                    state._fsp--;

                    match(input,18,FOLLOW_18_in_predicateExpr852); 
                    match(input,19,FOLLOW_19_in_predicateExpr854); 
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
    // java/com/google/gdata/model/select/parser/Selection.g:249:1: selectionPathExpr[Path current] returns [Path value] : pathStep[builder] ( '/' pathStep[builder] )* ;
    public final Path selectionPathExpr(Path current) throws RecognitionException {
        Path value = null;


            Path.Builder builder = current.buildFrom();
          
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:253:3: ( pathStep[builder] ( '/' pathStep[builder] )* )
            // java/com/google/gdata/model/select/parser/Selection.g:253:5: pathStep[builder] ( '/' pathStep[builder] )*
            {
            pushFollow(FOLLOW_pathStep_in_selectionPathExpr885);
            pathStep(builder);

            state._fsp--;

            // java/com/google/gdata/model/select/parser/Selection.g:253:23: ( '/' pathStep[builder] )*
            loop23:
            do {
                int alt23=2;
                alt23 = dfa23.predict(input);
                switch (alt23) {
            	case 1 :
            	    // java/com/google/gdata/model/select/parser/Selection.g:253:24: '/' pathStep[builder]
            	    {
            	    match(input,25,FOLLOW_25_in_selectionPathExpr889); 
            	    pushFollow(FOLLOW_pathStep_in_selectionPathExpr891);
            	    pathStep(builder);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop23;
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
    // java/com/google/gdata/model/select/parser/Selection.g:258:1: pathStep[Path.Builder value] : (at= '@' )? qname= nameTest[at != null] ;
    public final void pathStep(Path.Builder value) throws RecognitionException {
        Token at=null;
        QName qname = null;


        try {
            // java/com/google/gdata/model/select/parser/Selection.g:259:3: ( (at= '@' )? qname= nameTest[at != null] )
            // java/com/google/gdata/model/select/parser/Selection.g:259:5: (at= '@' )? qname= nameTest[at != null]
            {
            // java/com/google/gdata/model/select/parser/Selection.g:259:5: (at= '@' )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==26) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:259:6: at= '@'
                    {
                    at=(Token)match(input,26,FOLLOW_26_in_pathStep914); 

                    }
                    break;

            }

            pushFollow(FOLLOW_nameTest_in_pathStep920);
            qname=nameTest(at != null);

            state._fsp--;


                boolean isAttribute = at != null;
                boolean added = false;
                try {
                  if (isAttribute) {
                    added = value.addIfAttribute(qname);
                  } else {
                    added = value.addIfElement(qname);
                    if (!added && parseMode == ParseMode.JSON) {
                      // The '@' prefix is not required for JSON attribute selections, since
                      // there's no distinction between properties derived from elements or
                      // attributes in JSON.
                      added = value.addIfAttribute(qname);
                    }
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
    // java/com/google/gdata/model/select/parser/Selection.g:284:1: nameTest[boolean isAttribute] returns [QName value] : left= ( '*' | NCNAME ) ( ':' right= ( '*' | NCNAME ) )? ;
    public final QName nameTest(boolean isAttribute) throws RecognitionException {
        QName value = null;

        Token left=null;
        Token right=null;

        try {
            // java/com/google/gdata/model/select/parser/Selection.g:285:3: (left= ( '*' | NCNAME ) ( ':' right= ( '*' | NCNAME ) )? )
            // java/com/google/gdata/model/select/parser/Selection.g:285:5: left= ( '*' | NCNAME ) ( ':' right= ( '*' | NCNAME ) )?
            {
            left=(Token)input.LT(1);
            if ( input.LA(1)==NCNAME||input.LA(1)==27 ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            // java/com/google/gdata/model/select/parser/Selection.g:285:25: ( ':' right= ( '*' | NCNAME ) )?
            int alt25=2;
            alt25 = dfa25.predict(input);
            switch (alt25) {
                case 1 :
                    // java/com/google/gdata/model/select/parser/Selection.g:285:26: ':' right= ( '*' | NCNAME )
                    {
                    match(input,13,FOLLOW_13_in_nameTest953); 
                    right=(Token)input.LT(1);
                    if ( input.LA(1)==NCNAME||input.LA(1)==27 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }


                value = buildQName(namespaces, parseMode, left, right, isAttribute);
              

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
    // java/com/google/gdata/model/select/parser/Selection.g:291:1: xmlnsToken : {...}? NCNAME ;
    public final void xmlnsToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:291:12: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:291:14: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("xmlns"))) ) {
                throw new FailedPredicateException(input, "xmlnsToken", "input.LT(1).getText().equals(\"xmlns\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_xmlnsToken981); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:292:1: orToken : {...}? NCNAME ;
    public final void orToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:292:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:292:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("or"))) ) {
                throw new FailedPredicateException(input, "orToken", "input.LT(1).getText().equals(\"or\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_orToken991); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:293:1: andToken : {...}? NCNAME ;
    public final void andToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:293:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:293:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("and"))) ) {
                throw new FailedPredicateException(input, "andToken", "input.LT(1).getText().equals(\"and\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_andToken1001); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:294:1: notToken : {...}? NCNAME ;
    public final void notToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:294:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:294:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("not"))) ) {
                throw new FailedPredicateException(input, "notToken", "input.LT(1).getText().equals(\"not\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_notToken1011); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:295:1: trueToken : {...}? NCNAME ;
    public final void trueToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:295:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:295:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("true"))) ) {
                throw new FailedPredicateException(input, "trueToken", "input.LT(1).getText().equals(\"true\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_trueToken1021); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:296:1: falseToken : {...}? NCNAME ;
    public final void falseToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:296:12: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:296:14: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("false"))) ) {
                throw new FailedPredicateException(input, "falseToken", "input.LT(1).getText().equals(\"false\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_falseToken1031); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:297:1: eqToken : {...}? NCNAME ;
    public final void eqToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:297:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:297:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("eq"))) ) {
                throw new FailedPredicateException(input, "eqToken", "input.LT(1).getText().equals(\"eq\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_eqToken1041); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:298:1: neToken : {...}? NCNAME ;
    public final void neToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:298:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:298:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("ne"))) ) {
                throw new FailedPredicateException(input, "neToken", "input.LT(1).getText().equals(\"ne\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_neToken1051); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:299:1: textToken : {...}? NCNAME ;
    public final void textToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:299:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:299:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("text"))) ) {
                throw new FailedPredicateException(input, "textToken", "input.LT(1).getText().equals(\"text\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_textToken1061); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:300:1: ltToken : {...}? NCNAME ;
    public final void ltToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:300:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:300:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("lt"))) ) {
                throw new FailedPredicateException(input, "ltToken", "input.LT(1).getText().equals(\"lt\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_ltToken1071); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:301:1: lteToken : {...}? NCNAME ;
    public final void lteToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:301:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:301:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("lte"))) ) {
                throw new FailedPredicateException(input, "lteToken", "input.LT(1).getText().equals(\"lte\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_lteToken1081); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:302:1: gtToken : {...}? NCNAME ;
    public final void gtToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:302:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:302:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("gt"))) ) {
                throw new FailedPredicateException(input, "gtToken", "input.LT(1).getText().equals(\"gt\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_gtToken1091); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:303:1: gteToken : {...}? NCNAME ;
    public final void gteToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:303:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:303:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("gte"))) ) {
                throw new FailedPredicateException(input, "gteToken", "input.LT(1).getText().equals(\"gte\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_gteToken1101); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:304:1: naNToken : {...}? NCNAME ;
    public final void naNToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:304:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:304:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("NaN"))) ) {
                throw new FailedPredicateException(input, "naNToken", "input.LT(1).getText().equals(\"NaN\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_naNToken1111); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:305:1: infToken : {...}? NCNAME ;
    public final void infToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:305:10: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:305:12: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("INF"))) ) {
                throw new FailedPredicateException(input, "infToken", "input.LT(1).getText().equals(\"INF\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_infToken1121); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:306:1: xsToken : {...}? NCNAME ;
    public final void xsToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:306:9: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:306:11: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("xs"))) ) {
                throw new FailedPredicateException(input, "xsToken", "input.LT(1).getText().equals(\"xs\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_xsToken1131); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:307:1: dateToken : {...}? NCNAME ;
    public final void dateToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:307:11: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:307:13: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("date"))) ) {
                throw new FailedPredicateException(input, "dateToken", "input.LT(1).getText().equals(\"date\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_dateToken1141); 

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
    // java/com/google/gdata/model/select/parser/Selection.g:308:1: dateTimeToken : {...}? NCNAME ;
    public final void dateTimeToken() throws RecognitionException {
        try {
            // java/com/google/gdata/model/select/parser/Selection.g:308:15: ({...}? NCNAME )
            // java/com/google/gdata/model/select/parser/Selection.g:308:17: {...}? NCNAME
            {
            if ( !((input.LT(1).getText().equals("dateTime"))) ) {
                throw new FailedPredicateException(input, "dateTimeToken", "input.LT(1).getText().equals(\"dateTime\")");
            }
            match(input,NCNAME,FOLLOW_NCNAME_in_dateTimeToken1151); 

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
    protected DFA11 dfa11 = new DFA11(this);
    protected DFA14 dfa14 = new DFA14(this);
    protected DFA13 dfa13 = new DFA13(this);
    protected DFA21 dfa21 = new DFA21(this);
    protected DFA22 dfa22 = new DFA22(this);
    protected DFA23 dfa23 = new DFA23(this);
    protected DFA25 dfa25 = new DFA25(this);
    static final String DFA1_eotS =
        "\14\uffff";
    static final String DFA1_eofS =
        "\2\2\12\uffff";
    static final String DFA1_minS =
        "\1\4\1\15\3\uffff\1\0\6\uffff";
    static final String DFA1_maxS =
        "\1\33\1\31\3\uffff\1\0\6\uffff";
    static final String DFA1_acceptS =
        "\2\uffff\1\2\3\uffff\1\1\5\uffff";
    static final String DFA1_specialS =
        "\5\uffff\1\0\6\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\1\25\uffff\2\2",
            "\1\5\1\6\2\2\1\uffff\1\2\6\uffff\1\2",
            "",
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
            return "132:5: ( namespaceDeclarations )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA1_5 = input.LA(1);

                         
                        int index1_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("xmlns"))) ) {s = 6;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index1_5);
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
        "\14\uffff";
    static final String DFA2_eofS =
        "\2\2\12\uffff";
    static final String DFA2_minS =
        "\1\4\1\15\3\uffff\1\0\6\uffff";
    static final String DFA2_maxS =
        "\1\33\1\31\3\uffff\1\0\6\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\2\10\uffff\1\1";
    static final String DFA2_specialS =
        "\5\uffff\1\0\6\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\1\25\uffff\2\2",
            "\1\5\1\13\2\2\1\uffff\1\2\6\uffff\1\2",
            "",
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
            return "149:26: ( namespaceDeclarations )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA2_5 = input.LA(1);

                         
                        int index2_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("xmlns"))) ) {s = 11;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index2_5);
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
        "\12\uffff";
    static final String DFA9_eofS =
        "\1\2\11\uffff";
    static final String DFA9_minS =
        "\2\4\3\uffff\4\0\1\uffff";
    static final String DFA9_maxS =
        "\1\23\1\33\3\uffff\4\0\1\uffff";
    static final String DFA9_acceptS =
        "\2\uffff\1\2\6\uffff\1\1";
    static final String DFA9_specialS =
        "\5\uffff\1\0\1\1\1\2\1\3\1\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\1\14\uffff\1\2\1\uffff\1\2",
            "\1\5\15\uffff\1\6\7\uffff\1\7\1\10",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
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
            return "()* loopback of 182:5: ( andToken right= finalExpr[current] )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_5 = input.LA(1);

                         
                        int index9_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("and"))) ) {s = 9;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index9_5);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA9_6 = input.LA(1);

                         
                        int index9_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("and"))) ) {s = 9;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index9_6);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA9_7 = input.LA(1);

                         
                        int index9_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("and"))) ) {s = 9;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index9_7);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA9_8 = input.LA(1);

                         
                        int index9_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("and"))) ) {s = 9;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index9_8);
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
        "\25\uffff";
    static final String DFA10_eofS =
        "\1\uffff\1\3\23\uffff";
    static final String DFA10_minS =
        "\2\4\3\uffff\1\0\17\uffff";
    static final String DFA10_maxS =
        "\1\33\1\31\3\uffff\1\0\17\uffff";
    static final String DFA10_acceptS =
        "\2\uffff\1\4\1\5\16\uffff\1\1\1\2\1\3";
    static final String DFA10_specialS =
        "\5\uffff\1\0\17\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1\15\uffff\1\2\7\uffff\2\3",
            "\1\3\10\uffff\2\3\2\uffff\1\3\1\5\7\3",
            "",
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
            return "185:1: finalExpr[Path current] returns [ElementCondition value] : ( ( notToken '(' notl= orExpr[current] ')' ) | ( trueToken '(' ')' ) | ( falseToken '(' ')' ) | ( '(' l= orExpr[current] ')' ) | (c= comparisonOrExistenceExpr[current] ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA10_5 = input.LA(1);

                         
                        int index10_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("not"))) ) {s = 18;}

                        else if ( ((input.LT(1).getText().equals("true"))) ) {s = 19;}

                        else if ( ((input.LT(1).getText().equals("false"))) ) {s = 20;}

                        else if ( ((input.LT(1).getText().equals("text"))) ) {s = 3;}

                         
                        input.seek(index10_5);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 10, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA11_eotS =
        "\22\uffff";
    static final String DFA11_eofS =
        "\1\uffff\1\2\20\uffff";
    static final String DFA11_minS =
        "\2\4\2\uffff\1\0\15\uffff";
    static final String DFA11_maxS =
        "\1\33\1\31\2\uffff\1\0\15\uffff";
    static final String DFA11_acceptS =
        "\2\uffff\1\2\16\uffff\1\1";
    static final String DFA11_specialS =
        "\4\uffff\1\0\15\uffff}>";
    static final String[] DFA11_transitionS = {
            "\1\1\25\uffff\2\2",
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
            "",
            ""
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "193:1: comparisonOrExistenceExpr[Path current] returns [ElementCondition value] : (d= dateOrDateTimeComparison[current] | o= otherComparison[current] );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA11_4 = input.LA(1);

                         
                        int index11_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("xs"))) ) {s = 17;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index11_4);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 11, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA14_eotS =
        "\21\uffff";
    static final String DFA14_eofS =
        "\1\10\20\uffff";
    static final String DFA14_minS =
        "\2\4\13\uffff\1\0\3\uffff";
    static final String DFA14_maxS =
        "\1\30\1\33\13\uffff\1\0\3\uffff";
    static final String DFA14_acceptS =
        "\2\uffff\1\1\5\uffff\1\2\10\uffff";
    static final String DFA14_specialS =
        "\15\uffff\1\0\3\uffff}>";
    static final String[] DFA14_transitionS = {
            "\1\1\11\uffff\1\2\2\uffff\1\10\1\uffff\1\10\5\2",
            "\1\15\2\2\13\uffff\1\10\7\uffff\2\10",
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
            "",
            ""
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "216:30: (c= comparator (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) ) )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA14_13 = input.LA(1);

                         
                        int index14_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (((input.LT(1).getText().equals("gte"))||(input.LT(1).getText().equals("lte"))||(input.LT(1).getText().equals("gt"))||(input.LT(1).getText().equals("lt"))||(input.LT(1).getText().equals("eq"))||(input.LT(1).getText().equals("ne")))) ) {s = 2;}

                        else if ( (true) ) {s = 8;}

                         
                        input.seek(index14_13);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 14, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA13_eotS =
        "\12\uffff";
    static final String DFA13_eofS =
        "\12\uffff";
    static final String DFA13_minS =
        "\1\4\2\uffff\1\0\6\uffff";
    static final String DFA13_maxS =
        "\1\6\2\uffff\1\0\6\uffff";
    static final String DFA13_acceptS =
        "\1\uffff\1\1\1\2\5\uffff\1\3\1\4";
    static final String DFA13_specialS =
        "\3\uffff\1\0\6\uffff}>";
    static final String[] DFA13_transitionS = {
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
            return "216:44: (str= STRINGLITERAL | num= NUMBER | ( naNToken ) | ( infToken ) )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_3 = input.LA(1);

                         
                        int index13_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("NaN"))) ) {s = 8;}

                        else if ( ((input.LT(1).getText().equals("INF"))) ) {s = 9;}

                         
                        input.seek(index13_3);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA21_eotS =
        "\13\uffff";
    static final String DFA21_eofS =
        "\13\uffff";
    static final String DFA21_minS =
        "\1\4\1\0\11\uffff";
    static final String DFA21_maxS =
        "\1\30\1\0\11\uffff";
    static final String DFA21_acceptS =
        "\2\uffff\1\1\1\2\1\3\1\4\1\5\1\6\3\uffff";
    static final String DFA21_specialS =
        "\1\uffff\1\0\11\uffff}>";
    static final String[] DFA21_transitionS = {
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
            return "235:1: comparator returns [Operation value] : ( ( ( eqToken | '=' ) ) | ( ( neToken | '!=' ) ) | ( ( gtToken | '>' ) ) | ( ( gteToken | '>=' ) ) | ( ( ltToken | '<' ) ) | ( ( lteToken | '<=' ) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA21_1 = input.LA(1);

                         
                        int index21_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LT(1).getText().equals("eq"))) ) {s = 2;}

                        else if ( ((input.LT(1).getText().equals("ne"))) ) {s = 3;}

                        else if ( ((input.LT(1).getText().equals("gt"))) ) {s = 4;}

                        else if ( ((input.LT(1).getText().equals("gte"))) ) {s = 5;}

                        else if ( ((input.LT(1).getText().equals("lt"))) ) {s = 6;}

                        else if ( ((input.LT(1).getText().equals("lte"))) ) {s = 7;}

                         
                        input.seek(index21_1);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 21, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA22_eotS =
        "\21\uffff";
    static final String DFA22_eofS =
        "\2\uffff\1\1\16\uffff";
    static final String DFA22_minS =
        "\1\4\1\uffff\1\4\16\uffff";
    static final String DFA22_maxS =
        "\1\33\1\uffff\1\31\16\uffff";
    static final String DFA22_acceptS =
        "\1\uffff\1\1\2\uffff\1\2\14\uffff";
    static final String DFA22_specialS =
        "\21\uffff}>";
    static final String[] DFA22_transitionS = {
            "\1\2\25\uffff\2\1",
            "",
            "\1\1\10\uffff\2\1\2\uffff\1\1\1\4\7\1",
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
            return "244:1: predicateExpr[Path current] returns [Path value] : ( (path= selectionPathExpr[current] ) | ( textToken '(' ')' ) );";
        }
    }
    static final String DFA23_eotS =
        "\17\uffff";
    static final String DFA23_eofS =
        "\1\1\16\uffff";
    static final String DFA23_minS =
        "\1\4\16\uffff";
    static final String DFA23_maxS =
        "\1\31\16\uffff";
    static final String DFA23_acceptS =
        "\1\uffff\1\2\14\uffff\1\1";
    static final String DFA23_specialS =
        "\17\uffff}>";
    static final String[] DFA23_transitionS = {
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
            return "()* loopback of 253:23: ( '/' pathStep[builder] )*";
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
            return "285:25: ( ':' right= ( '*' | NCNAME ) )?";
        }
    }
 

    public static final BitSet FOLLOW_namespaceDeclarations_in_selectionExpr66 = new BitSet(new long[]{0x000000000C000010L});
    public static final BitSet FOLLOW_selectors_in_selectionExpr71 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_selectionExpr74 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orExpr_in_elementConditionExpr95 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_elementConditionExpr98 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_namespaceDeclaration_in_namespaceDeclarations114 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_namespaceDeclarations_in_namespaceDeclarations117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_xmlnsToken_in_namespaceDeclaration132 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_13_in_namespaceDeclaration136 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NCNAME_in_namespaceDeclaration140 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_namespaceDeclaration144 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_namespaceDeclaration148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selector_in_selectors181 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_15_in_selectors194 = new BitSet(new long[]{0x000000000C000010L});
    public static final BitSet FOLLOW_selector_in_selectors198 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_selectionPathExpr_in_selector228 = new BitSet(new long[]{0x0000000000050002L});
    public static final BitSet FOLLOW_16_in_selector236 = new BitSet(new long[]{0x000000000C040010L});
    public static final BitSet FOLLOW_orExpr_in_selector240 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_selector243 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_18_in_selector253 = new BitSet(new long[]{0x000000000C080010L});
    public static final BitSet FOLLOW_selectors_in_selector257 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_selector260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpr_in_orExpr289 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_orToken_in_orExpr300 = new BitSet(new long[]{0x000000000C040010L});
    public static final BitSet FOLLOW_andExpr_in_orExpr304 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_finalExpr_in_andExpr330 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_andToken_in_andExpr340 = new BitSet(new long[]{0x000000000C040010L});
    public static final BitSet FOLLOW_finalExpr_in_andExpr344 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_notToken_in_finalExpr369 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr371 = new BitSet(new long[]{0x000000000C040010L});
    public static final BitSet FOLLOW_orExpr_in_finalExpr375 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_trueToken_in_finalExpr388 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr390 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_falseToken_in_finalExpr402 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_finalExpr404 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr406 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_finalExpr416 = new BitSet(new long[]{0x000000000C040010L});
    public static final BitSet FOLLOW_orExpr_in_finalExpr420 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_finalExpr423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparisonOrExistenceExpr_in_finalExpr435 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateOrDateTimeComparison_in_comparisonOrExistenceExpr462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_otherComparison_in_comparisonOrExistenceExpr475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison502 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison504 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateToken_in_dateOrDateTimeComparison517 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison519 = new BitSet(new long[]{0x000000000C040010L});
    public static final BitSet FOLLOW_predicateExpr_in_dateOrDateTimeComparison523 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison526 = new BitSet(new long[]{0x0000000001F04010L});
    public static final BitSet FOLLOW_comparator_in_dateOrDateTimeComparison530 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison545 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison547 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateToken_in_dateOrDateTimeComparison549 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison551 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison555 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison557 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateTimeToken_in_dateOrDateTimeComparison573 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison575 = new BitSet(new long[]{0x000000000C040010L});
    public static final BitSet FOLLOW_predicateExpr_in_dateOrDateTimeComparison579 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison582 = new BitSet(new long[]{0x0000000001F04010L});
    public static final BitSet FOLLOW_comparator_in_dateOrDateTimeComparison586 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_xsToken_in_dateOrDateTimeComparison601 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_dateOrDateTimeComparison603 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dateTimeToken_in_dateOrDateTimeComparison605 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_dateOrDateTimeComparison607 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_dateOrDateTimeComparison611 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_dateOrDateTimeComparison613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicateExpr_in_otherComparison651 = new BitSet(new long[]{0x0000000001F04012L});
    public static final BitSet FOLLOW_comparator_in_otherComparison657 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_otherComparison662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_otherComparison675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_naNToken_in_otherComparison687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_infToken_in_otherComparison702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_eqToken_in_comparator729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_comparator733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_neToken_in_comparator745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_comparator749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_gtToken_in_comparator761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_comparator765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_gteToken_in_comparator777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_comparator781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ltToken_in_comparator793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_comparator797 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lteToken_in_comparator809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_comparator813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectionPathExpr_in_predicateExpr839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_textToken_in_predicateExpr850 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_predicateExpr852 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_predicateExpr854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pathStep_in_selectionPathExpr885 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_selectionPathExpr889 = new BitSet(new long[]{0x000000000C000010L});
    public static final BitSet FOLLOW_pathStep_in_selectionPathExpr891 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_26_in_pathStep914 = new BitSet(new long[]{0x000000000C000010L});
    public static final BitSet FOLLOW_nameTest_in_pathStep920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_nameTest944 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_nameTest953 = new BitSet(new long[]{0x0000000008000010L});
    public static final BitSet FOLLOW_set_in_nameTest957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_xmlnsToken981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_orToken991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_andToken1001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_notToken1011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_trueToken1021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_falseToken1031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_eqToken1041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_neToken1051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_textToken1061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_ltToken1071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_lteToken1081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_gtToken1091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_gteToken1101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_naNToken1111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_infToken1121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_xsToken1131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_dateToken1141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NCNAME_in_dateTimeToken1151 = new BitSet(new long[]{0x0000000000000002L});

}