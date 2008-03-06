/* Copyright (c) 2006 Google Inc.
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

/**
* @fileoverview
* This file contains some common helper functions
*/

// Based on <http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/
// core.html#ID-1950641247>
var DOM_ELEMENT_NODE = 1;
var DOM_ATTRIBUTE_NODE = 2;
var DOM_TEXT_NODE = 3;
var DOM_CDATA_SECTION_NODE = 4;
var DOM_ENTITY_REFERENCE_NODE = 5;
var DOM_ENTITY_NODE = 6;
var DOM_PROCESSING_INSTRUCTION_NODE = 7;
var DOM_COMMENT_NODE = 8;
var DOM_DOCUMENT_NODE = 9;
var DOM_DOCUMENT_TYPE_NODE = 10;
var DOM_DOCUMENT_FRAGMENT_NODE = 11;
var DOM_NOTATION_NODE = 12;


 //
 // regular expression for xsd date-time
 //
var UTIL_PARSE_XMLDATE = /(\d{4})-(\d{2})-(\d{2})/;
var UTIL_PARSE_XMLDATETIME = 
 /(\d{4})-?(\d{2})-?(\d{2})T(\d{2}):(\d{2}):(\d{2})(?:\.(\d+))(Z)?(([+-])(\d{2}):(\d{2}))?/;



function UTIL_inherits(subClass, superClass) {
  subClass.prototype = new superClass();
  subClass.prototype.Base = superClass.prototype;
  subClass.prototype.constructor = superClass.constructor;
}

/**
* helper to detect if a variable is persistable
* @param toTest the value to test
* @return true if peristable
*/
function UTIL_isPersistable(toTest) {
  if (typeof(toTest) == 'string') {
    if (toTest !== null && toTest.length > 0  ) {
      return true;
    }
  }
  return false; 
}

/**
* helper to convert a date to RFC3339 string presentation
* @param date {Date} the date to convert
* @return {string} date formatted according to RFC3339
*/
function UTIL_dateToRFC3339(dateToConvert) {
  var strReturn = null; 
  strReturn = dateToConvert.getUTCFullYear() + "-" + 
    UTIL_pad((dateToConvert.getUTCMonth()+1).toString(), 2, "0")  + "-" + 
    UTIL_pad((dateToConvert.getUTCDate()).toString(), 2, "0"); 
  if (dateToConvert.getTime() > 0) {
    strReturn += "T" + 
          UTIL_pad(dateToConvert.getUTCHours().toString(), 2, "0") + ":" + 
          UTIL_pad(dateToConvert.getUTCMinutes().toString(), 2, "0") + ":" + 
          UTIL_pad(dateToConvert.getUTCSeconds().toString(), 2, "0"); 
        // convert to hours
    var timediff = dateToConvert.getTimezoneOffset();
    timediff /= 60;  
    strReturn += timediff > 0 ? "+" : "-"; 
    strReturn += UTIL_pad(Math.abs(timediff).toFixed(0), 2, "0") + ":00"; 
  }
  return strReturn;
}

/**
* helper to convert a string in RFC3339  presentation to a date
* @param dateString  the string date value to convert
* @return the date
*/
 function UTIL_parseXmlDateTime(dateString) {
   var d = null;
   var dateParts = dateString.match(UTIL_PARSE_XMLDATETIME);
   if (dateParts == null) {
     //
     // if dateTime didn't parse, try date
     //
     return UTIL_parseXmlDate(dateString);
   } else {
     d = new Date();
     var year = parseInt(dateParts[1],10);
     var month = parseInt(dateParts[2],10);
     var day = parseInt(dateParts[3],10);
     var hour = parseInt(dateParts[4],10);
     var min = parseInt(dateParts[5],10);
     var sec = parseInt(dateParts[6],10);
 
     if ((dateParts[8] || dateParts[9]) && !(hour==0 && min==0 && sec==0)) {
       // utc mode
       d.setUTCFullYear(year);
       d.setUTCMonth(month-1);
       d.setUTCDate(day);
       d.setUTCHours(hour);
       d.setUTCMinutes(min);
       d.setUTCSeconds(sec);
       d.setUTCMilliseconds(0);

       if (dateParts[8] === 'Z') {
         // so the time is in UTC
         LOG_TRACE(dateParts[8], "UTIL_parseXmlDateTime", dateString); 
       } else {
         // should be an offset now
         var timeOffset = 0; 
         if (dateParts[10]) {
           timeOffset = Number(dateParts[11]) * 60; 
           timeOffset += Number(dateParts[12]);
           timeOffset *= ((dateParts[10] === '-') ? 1 : -1);
         }
         timeOffset *= 60 * 1000; 
         d = new Date(Number(d) + timeOffset);
       }
       
       //
       // BUGBUG - apply +/- bias from part 8
       //
     } else {
       d.setFullYear(year);
       d.setMonth(month-1);
       d.setDate(day);
       d.setHours(hour);
       d.setMinutes(min);
       d.setSeconds(sec);
       d.setMilliseconds(0);
     }
   }
   LOG_TRACE(dateString + "----" + d, "UTIL_parseXmlDateTime", dateString);
   return d;
 }
 
 
 /**
* helper to convert a string in RFC3339  presentation to a date
* @param dateString  the string date value to convert, no timezone
* @return the date
*/
 function UTIL_parseXmlDate(dateString) {
   var d;
   var dateParts = dateString.match(UTIL_PARSE_XMLDATE);
   if (dateParts != null) {
     d = new Date();
     d.setHours(0);
     d.setMinutes(0);
     d.setSeconds(0);
     d.setMilliseconds(0);
 
     var year = parseInt(dateParts[1],10);
     var month = parseInt(dateParts[2],10);
     var day = parseInt(dateParts[3],10);
 
     d.setFullYear(year);
     d.setMonth(month-1);
     d.setDate(day);
   }
   return d;
 }

/**
* helper to pad a string with a given char
* @param str the string to pad
* @param length the length to pad to
* @param chr the char to use for padding
* @return the padded string
*/
function UTIL_pad(str, length, chr) {
  while (length > str.length) {
    str = chr + str;
  }
  return str;
}

/**
* helper to encode a string that is passed in to URL encoding
* this is used for requests to a webserver, so do not use 
* encodeUriComponent here
* @param sStr the stirng to encode
* @return the encoded string
*/
function UTIL_urlEncodeString(sStr) {
  return escape(sStr).
      replace(/\+/g, '%2B').
        replace(/\"/g,'%22').
          replace(/\'/g, '%27').
            replace(/\//g,'%2F');
}

/**
* helper to attach an event to a target object
* @param target the object to attach the event to
* @param type the event type
* @param callback the event callback
*/
function UTIL_domEventHandler(target, type, callback) {
  if (target.addEventListener) {
    target.addEventListener(type, callback, false);
  } else {
    target.attachEvent("on" + type, callback);
  }
}

/**
* helper to create a DIV element, currently used for debugging
* @param opt_text the text in the div
* @param opt_className the css class to use
*/
function UTIL_domCreateDiv(opt_text, opt_className) {
  var el = document.createElement("div");
  if (opt_text) {
    UTIL_domAppendChild(el, document.createTextNode(opt_text));
  }
  if (opt_className) {
    el.className = opt_className;
  }
  return el;
}

/**
* helper to append a child in the dom
* @param parent the node to append to
* @param child the node to append
*/
function UTIL_domAppendChild(parent, child) {
  try {
    parent.appendChild(child);
  } catch (e) {
    LOG_ERROR("UTIL_domAppendChild: " + e);
  }
  return child;
}

/**
* Applies the given function to each element of the array, preserving
* this, and passing the index.
*/
function UTIL_mapExec(array, func) {
  for (var i = 0; i < array.length; ++i) {
    func.call(this, array[i], i);
  }
}

/**
* Returns an array that contains the return value of the given
* function applied to every element of the input array.
*/
function UTIL_mapExpr(array, func) {
  var ret = [];
  for (var i = 0; i < array.length; ++i) {
    ret.push(func(array[i]));
  }
  return ret;
}

/**
* Reverses the given array in place.
*/
function UTIL_reverseInplace(array) {
  for (var i = 0; i < array.length / 2; ++i) {
    var h = array[i];
    var ii = array.length - i - 1;
    array[i] = array[ii];
    array[ii] = h;
  }
}

/**
* Shallow-copies an array.
*/
function UTIL_copyArray(dst, src) { 
  for (var i = 0; i < src.length; ++i) {
    dst.push(src[i]);
  }
}

/**
* Returns the text value of a node; for nodes without children this
* is the nodeValue, for nodes with children this is the concatenation
* of the value of all children.
*/
function UTIL_xmlValue(node) {
  if (!node) {
    return '';
  }

  var ret = '';
  if (node.nodeType == DOM_TEXT_NODE ||
      node.nodeType == DOM_CDATA_SECTION_NODE ||
      node.nodeType == DOM_ATTRIBUTE_NODE) {
    ret += node.nodeValue;

  } else if (node.nodeType == DOM_ELEMENT_NODE ||
             node.nodeType == DOM_DOCUMENT_NODE ||
             node.nodeType == DOM_DOCUMENT_FRAGMENT_NODE) {
    for (var i = 0; i < node.childNodes.length; ++i) {
      ret += arguments.callee(node.childNodes[i]);
    }
  }
  return ret;
}

/**
* Returns the representation of a node as XML text.
*/
function UTIL_xmlText(node, opt_cdata) {
  var buf = [];
  xmlTextR(node, buf, opt_cdata);
  return buf.join('');
}

/**
* worker function for UTIL_xmlText()
*/
function xmlTextR(node, buf, cdata) {
  if (node.nodeType == DOM_TEXT_NODE) {
    buf.push(xmlEscapeText(node.nodeValue));

  } else if (node.nodeType == DOM_CDATA_SECTION_NODE) {
    if (cdata) {
      buf.push(node.nodeValue);
    } else {
      buf.push('<![CDATA[' + node.nodeValue + ']]>');
    }

  } else if (node.nodeType == DOM_COMMENT_NODE) {
    buf.push('<!--' + node.nodeValue + '-->');

  } else if (node.nodeType == DOM_ELEMENT_NODE) {
    buf.push('<' + xmlFullNodeName(node));
    for (var i = 0; i < node.attributes.length; ++i) {
      var a = node.attributes[i];
      if (a && a.nodeName && a.nodeValue) {
        buf.push(' ' + xmlFullNodeName(a) + '="' + 
                 xmlEscapeAttr(a.nodeValue) + '"');
      }
    }

    if (node.childNodes.length === 0) {
      buf.push('/>');
    } else {
      buf.push('>');
      for (i = 0; i < node.childNodes.length; ++i) {
        arguments.callee(node.childNodes[i], buf, cdata);
      }
      buf.push('</' + xmlFullNodeName(node) + '>');
    }

  } else if (node.nodeType == DOM_DOCUMENT_NODE ||
             node.nodeType == DOM_DOCUMENT_FRAGMENT_NODE) {
    for (i = 0; i < node.childNodes.length; ++i) {
      arguments.callee(node.childNodes[i], buf, cdata);
    }
  }
}


/**
* helper for xmlTextR
*/
function xmlFullNodeName(n) {
  if (n.prefix && n.nodeName.indexOf(n.prefix + ':') !== 0) {
    return n.prefix + ':' + n.nodeName;
  } else {
    return n.nodeName;
  }
}

/**
* Escape XML special markup chracters: tag delimiter < > and entity
* reference start delimiter &. The escaped string can be used in XML
* text portions (i.e. between tags).
*/
function xmlEscapeText(s) {
  return ('' + s).replace(/&/g, '&amp;').replace(/</, '&lt;').
    replace(/>/, '&gt;');
}

/**
* Escape XML special markup characters: tag delimiter < > entity
* reference start delimiter & and quotes ". The escaped string can be
* used in double quoted XML attribute value portions (i.e. in
* attributes within start tags).
*/
function xmlEscapeAttr(s) {
  return xmlEscapeText(s).replace(/\"/g, '&quot;');
}

/**
* Escape markup in XML text, but don't touch entity references. The
* escaped string can be used as XML text (i.e. between tags).
*/
function xmlEscapeTags(s) {
  return s.replace(/</, '&lt;').replace(/>/g, '&gt;');
}




// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end

/* Copyright (c) 2006 Google Inc.
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



/**
* @fileoverview
* BrowserDetector (object)
*
* A class for detecting version 5 browsers by the Javascript objects 
* they support and not their user agent strings (which can be 
* spoofed).
*
* Warning: Though slow to develop, browsers may begin to add 
* DOM support in later versions which might require changes to this 
* file.
*
* Typical usage:
* Detect = new BrowserDetector();
* if (Detect.IE()) //IE-only code...
*/

/**
* @constructor
*/
function BrowserDetector() {
  //IE 4+
  this.IE = function() {
    try {
      return this.Run(document.all && window.ActiveXObject) !==false;
    } catch(e) {
      /* IE 5.01 doesn't support the 'contains' object and 
         fails the first test */
      if (document.all) {
        return true;
      }
      return false;
    }
  };
  
  //IE 5.5+
  this.IE_5_5_newer = function(){
    try { 
      return this.Run(this.IE() && Array.prototype.pop)!==false;
    } catch(e) {return false;}
  };
  
  //IE 5, Macintosh
  this.IE_5_Mac = function(){
    try {
      return(true === undefined);
    } catch(e) {
      return(
            document.all && 
            document.getElementById && 
            !document.mimeType && 
            !window.opera)!==false;
    }
  };

  //Opera 7+
  this.OPERA = function(){
    try {
      return this.Run(document.all && document.contains)!==false;
    } catch(e) {return false;}
  };

  //Gecko, actually Mozilla 1.2+
  this.MOZILLA = function() {
    try {
      return this.Run(
                     document.implementation && 
                     document.implementation.createDocument && 
                     document.evaluate && 
                     !document.contains )!==false;
    } catch(e) {return false;}
  };

  //Safari
  this.SAFARI = function(){
    try {
      return this.Run(
                     document.implementation && 
                     document.implementation.createDocument && 
                     document.contains)!==false;
    } catch(e) {return false;}
  };

  //Any browser which supports the W3C DOM
  this.DOM = function() {
    return(document.getElementById);
  };

  this.Run = function(test) {
    if (test===undefined) {
      return false;
    } else {
      return test;
    }
  };

  this.XPathSupport = function() {
    return this.IE_5_5_newer() || document.implementation.hasFeature('XPath','3.0'); 
  };
}

var DOM = (document.getElementById);
var Detect;
if (DOM) {
  Detect = new BrowserDetector();
}


// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end
/* Copyright (c) 2006 Google Inc.
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


//------------------------------------------------------------------------------

function DumpError(msg) {}
function DumpException(msg) {}

//------------------------------------------------------------------------------
// Logging
//------------------------------------------------------------------------------

function LOG_LEVEL() {}

LOG_LEVEL.NONE  = 0;
LOG_LEVEL.ERROR = 1;
LOG_LEVEL.DEBUG = 2;
LOG_LEVEL.TRACE = 3;
LOG_LEVEL.ALL   = 4;

function DEBUG_ENABLE_CROSSSITE() {} 

function DEBUG_CROSSSITE() { return false;}

//------------------------------------------------------------------------------
// Inline log functions
//------------------------------------------------------------------------------



function LOG_CLASS(classDef, name) {}

function LOG(msg, opt_level) {}

function LOG_DEBUG(msg, opt_show) {}

function LOG_ERROR(msg) {}

function LOG_EVENT(msg) {}

function LOG_TRACE(obj, fn, args) {}

function LOG_SEPARATOR(opt_text) {}

function LOG_TIMER_START(name) {}

function LOG_TIMER_STOP(name) {}

function LOG_XML_PRINT(doc, opt_level) {}
function LOG_DUMP_FEED(feed) {}

function VARJ_INC(name) {}

//------------------------------------------------------------------------------
// Inline debug functions
//------------------------------------------------------------------------------

function DBG_ALERT(msg, opt_prefix) {}

function DBG_ASSERT(cond, opt_msg) {}

function DBG_ABSTRACT(obj, fn, args) {}

function DBG_NOT_IMPLEMENTED(str) {}

// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end

/* Copyright (c) 2006 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/


/** @filecomment
* transport.js provides the basic server related operations:
* get a feed, create an item, update & delete an item
* we abstract this out, so that a unit test framework based on FILE transport
* can be developed later
*/



/** returns the appropriate transportobject based on 
* the passed in type
*/
function GD_getTransport(transportType) {
  switch (transportType) {
    case GD_Transport.TRANSPORT_FILE:
      // File Transport object
      DBG_ALERT("File transport NYI");
      return null;
    case GD_Transport.TRANSPORT_HTTP:
      return new GD_HttpTransport();
    case GD_Transport.TRANSPORT_GOOGLECALENDAR:
      return new GD_GaiaTransport("cl"); 
    default:
      return new GD_GaiaTransport("cl"); 

  }
}

/** 
* base transport, for the common things in life
*/ 
function GD_Transport() { 
  this.userName_ = null;
  this.pwd_ = null;
} 

GD_Transport.TRANSPORT_FILE = 1;
GD_Transport.TRANSPORT_HTTP = 2;
GD_Transport.TRANSPORT_GOOGLECALENDAR = 3; 


GD_Transport.AUTH_HANDLER = "https://www.google.com/accounts/ClientLogin"; 
GD_Transport.AUTH_HEADER  = "Authorization"; 
GD_Transport.AUTH_HEADERVALUE = "GoogleLogin auth="; 
GD_Transport.AUTH_TOKEN = "Auth";
GD_Transport.AUTH_SERVICE = "service=";
GD_Transport.AUTH_SOURCE = "source=desktop_Feed_reader";
GD_Transport.AUTH_USER  = "Email=";
GD_Transport.AUTH_PWD = "Passwd="; 
GD_Transport.METHOD_OVERRIDE = "X-HTTP-Method-Override"; 
GD_Transport.NOREDIRECT_HEADER = "X-If-No-Redirect";
GD_Transport.SETCOOKIE_HEADER = "Set-Cookie"; 
GD_Transport.COOKIE_HEADER = "Cookie"; 


/**
* set method GD_Transport.userName_
*/
GD_Transport.prototype.setUserName = function(stringValue) { 
  this.userName_ = stringValue; 
}; 

/** 
* get method GD_Transport.userName_
*/
GD_Transport.prototype.getUserName = function() { 
  return this.userName_; 
};

/** 
* set method GD_Transport.pwd_
*/ 
GD_Transport.prototype.setPassword = function(stringValue) { 
  this.pwd_ = stringValue; 
};

/** 
* get method GD_Transport.pwd_
*/ 
GD_Transport.prototype.getPassword = function() { 
  return this.pwd_; 
};

/** 
 * prototype for the GetDocument method
 * @param docUri {string} the URI of the XML document to load
 * @param opt_callback {function} takes one argument,
 *     the loaded XMLDocument for the documentUri
 */
GD_Transport.prototype.GetDomDocument = function(docUri, opt_callback) {};

/** 
* prototype for the InsertEntry method
*/ 
GD_Transport.prototype.InsertEntry = function(feed, entry) {
};

/** 
* prototype for the UpdateEntry method
*/ 
GD_Transport.prototype.UpdateEntry = function(entry) {
};

/** 
* prototype for the DeleteEntry method
*/ 
GD_Transport.prototype.DeleteEntry = function(entry) {
};

/** 
* file transport section, prototype only
*/ 
function GD_FileTransport() {
  GD_Transport.call(this);
}

UTIL_inherits(GD_FileTransport, GD_Transport);

GD_FileTransport.prototype.toString = function(opt_verbose) {
  return "GD_FileTransport()";
};



/**
 * http transport
 */
function GD_HttpTransport() {
  GD_Transport.call(this);
  this.shardingCookie_ = null;
}

UTIL_inherits(GD_HttpTransport, GD_Transport);

GD_HttpTransport.prototype.toString = function(opt_verbose) {
  return "GD_HttpTransport()";
};

/**
* set method GD_HttpTransport.shardingCookie_
*/
GD_HttpTransport.prototype.setCookie = function(str) { 
  this.shardingCookie_ = str;
};

/**
* get method GD_HttpTransport.shardingCookie_
*/
GD_HttpTransport.prototype.getCookie = function() { 
  return this.shardingCookie_; 
};

/**
 * gets the DomDocument for the HTTP transport
 * @param docUri {string} the URI of the XML document to load
 * @param opt_callback {function} takes one argument,
 *     the loaded XMLDocument for the documentUri
 */
GD_HttpTransport.prototype.GetDomDocument = function(docUri, opt_callback) {
  LOG_TRACE(docUri, "GD_HttpTransport => GetDomDocument", null);
  var callback = (opt_callback)
    ? function(request) { opt_callback.call(null, request.responseXML); }
    : undefined;
  var params = new XmlHttpParameters("GET", docUri, 200, null, callback);
  this.CallXmlHttp(params);
};

/**
* DeleteEntry for the HTTP transport,
* gets the target URI from the passed in entry
*/
GD_HttpTransport.prototype.DeleteEntry = function(gdEntry, opt_callBack) {
  var deleteUri = gdEntry.getEditUri();
  LOG_TRACE(deleteUri, "GD_HttpTransport => DeleteEntry", null);
  var params = new XmlHttpParameters("DELETE", deleteUri, 204,
      null, opt_callBack);
  this.CallXmlHttp(params);
};

/**
* UpdateEntry for the HTTP transport,
* gets the target URI from the passed in entry
*/
GD_HttpTransport.prototype.UpdateEntry = function(gdEntry, opt_callBack) {
  var updateUri = gdEntry.getEditUri();
  LOG_TRACE(updateUri, "GD_HttpTransport => UpdateEntry", null);
  var params = new XmlHttpParameters("PUT", updateUri, -1,
      gdEntry.save(), opt_callBack);
  this.CallXmlHttp(params);
};

/**
 * InsertEntry for the HTTP transport,
 * sets the target URI from the passed in feed
 */
GD_HttpTransport.prototype.InsertEntry = function(ofFeed, gdEntry, 
                                                  opt_callBack) {
  var insertUri = ofFeed.getPostUri();
  if (insertUri != null) {
    LOG_TRACE(insertUri, "GD_HttpTransport => InsertEntry", null);
    var params = new XmlHttpParameters("POST", insertUri, -1,
        gdEntry.save(), opt_callBack);
    this.CallXmlHttp(params);
  }
  else {
    throw "this is a read only feed";  
  }
};

/**
 * SetHeaders for the HTTP transport,
 * just sets the accept content type here
 */
GD_HttpTransport.prototype.SetHeaders = function(xmlHttpRequestObject) {
  LOG_TRACE("Entering...", "HTTP.SetHeaders", null);
  xmlHttpRequestObject.setRequestHeader("Accept", "text/xml"); 
  xmlHttpRequestObject.setRequestHeader("content-type", "application/atom+xml"); 
  xmlHttpRequestObject.setRequestHeader("Cache-Control", "no-cache"); 
};

/**
 * @param params {XmlHttpParameters} 
 */
GD_HttpTransport.prototype.PrepareAuthentication = function(params) {
  this.DoRequest(params);
};

/**
 * Bundle a collection of parameters that are used when making an
 * XMLHttpRequest. The state of a transaction should be stored in 
 * the XmlHttpParameters rather than the GD_HttpTransport, as the
 * GD_HttpTransport is likely a singleton, so requests it facilitates
 * should not share state.
 *
 * @param verb {string} must be "GET" or "DELETE" or "PUT" or "POST"
 * @param uri {string}
 * @param allowedStatus {int}
 * @param payload {string?}
 * @param opt_callback {function} will receive one argument,
 *     the loaded XMLHttpRequest, and its return value will be ignored
 * @param opt_retrying {boolean} indicates whether this is a retry attempt;
 *     defaults to false
 */
function XmlHttpParameters(verb, uri, allowedStatus, payload,
                           opt_callback, opt_retrying) {
  this.verb = verb;
  this.uri = uri;
  this.allowedStatus = allowedStatus;
  this.payload = payload;
  this.callback = opt_callback;
  this.retrying = !!opt_retrying;
}

/**
 * method do create the actual request. This one just calls PrepareAuth
 *   which will continue calling DoRequest after auth is done
 * @param params {XmlHttpParameters}
 */
GD_HttpTransport.prototype.CallXmlHttp = function(params) {
  try {
    LOG_TRACE("Entering...", "CallXmlHttp", null);
    this.PrepareAuthentication(params);
  } catch (e) {
    DBG_ALERT(e.toString());
    DBG_ALERT('The exception happened in CallXmlHttp for  ' + params.verb + 
            ' at URI: ' + params.uri);    
    throw e; 
  }
  LOG_TRACE("Exiting...", "CallXmlHttp", null);  
};

/**
 * @param params {XmlHttpParameters} 
 */
GD_HttpTransport.prototype.DoRequest = function(params) {
  try {
    LOG_TRACE("Entering...", "CallXmlHttp", null);
    
    var xmlRequest = XMLX_getHttpRequest();
    var fAsync = false; 
    if (params.callback) {
      xmlRequest.onreadystatechange =
        function() { GD_HandleAsyncData(xmlRequest, params); };
      fAsync = true; 
    }

    if (DEBUG_CROSSSITE()) {
      netscape.security.PrivilegeManager.
          enablePrivilege("UniversalBrowserRead");
      netscape.security.PrivilegeManager.
          enablePrivilege("UniversalBrowserWrite");
    }
       
    LOG_TRACE("Using default XlmRequest open", "CallXmlHttp", null); 
    xmlRequest.open(params.verb, params.uri, fAsync); 

    // thwart the cache
    if (params.verb == "GET") {
      xmlRequest.setRequestHeader("If-Modified-Since",
                                  "Sat, 1 Jan 2000 00:00:00 GMT");
    }

    this.SetHeaders(xmlRequest);

    if (xmlRequest.overrideMimeType) {
      //only need to do this for mozilla based browsers. IE would throw
      xmlRequest.overrideMimeType('text/xml');
    }

    LOG_TRACE("Sending..." + xmlRequest, "CallXmlHttp", null);
    xmlRequest.send(params.payload);
    LOG_TRACE("Returned, readyState:" + xmlRequest.readyState, 
              "CallXmlHttp", null);
    if (fAsync === false) {              
      this.HandleOnXmlRequestFinished(xmlRequest, params);
    }
  } catch (e) {
    DBG_ALERT(e.toString());
    DBG_ALERT('The exception happened in CallXmlHttp for  ' + params.verb_ + 
            ' at URI: '  + params.uri_);
    throw e; 
  }
  LOG_TRACE("Exiting...", "CallXmlHttp", null);  
};

/**
* async callback method
*/
function GD_HandleAsyncData(request, params) {
  if (request.readyState != 4) {
    return; 
  }
  LOG_TRACE("Callback done...", "GD_HandleAsyncData", null);
  GoogleDataFactory.getTransport().
      HandleOnXmlRequestFinished(request, params);
}


/** 
 * handling of errors/results of the xmlhttprequest
 */ 
GD_HttpTransport.prototype.HandleOnXmlRequestFinished =
    function(xmlRequest, params) {
  switch (xmlRequest.readyState) {
    case 1:
    case 2:
    case 3:
      DBG_ALERT('Bad Ready State: ' + xmlRequest.status);
      return false;
    case 4:
      if (xmlRequest.status > 299 || 
          (params.allowedStatus !== -1 && 
          xmlRequest.status != params.allowedStatus)) {
        if (params.retrying !== true) {
          /*
           * this might be a redirect/sharding issue, redo the whole thing
           * in javascript you can not handle the 302 return yourself, 
           * so what happens is we get our auth token, we try to go to the 
           * server, we get a redirect back now the redirect does not redo 
           * the "postdata" nor the custom auth header, so it fails with 
           * 401. Hence, redo it. But only once. 
           */
          if (xmlRequest.status === 401) {
            params = true;
            this.CallXmlHttp(params);
          } else if (xmlRequest.status === 412) {
            // the server want's to redirect us to a shard !! 
            // get the cookie and save it, then retry
            var cookie = xmlRequest.getResponseHeader(
                                  GD_Transport.SETCOOKIE_HEADER);
            DBG_ASSERT(cookie != null, 
           "we got no cookie back from the redirection server"); 
            
            if (cookie != null) {                                    
              this.setCookie(cookie); 
            }
            // now retry
            params.retrying = true;
            this.CallXmlHttp(params);
          }
        }
        DBG_ALERT('Request resulted in a bad status code for the operation: ' 
                      + xmlRequest.status + " " + params.allowedStatus);
      }
      break;
  }
  if (params.callback) {
    LOG_TRACE("calling callback to feed code", "HandleOnXmlRequestFinished",
              null);
    params.callback.call(null, xmlRequest); 
  }
}

/**
 * Gaia authentication transport. 
 */
function GD_GaiaTransport(serviceName) {
  GD_HttpTransport.call(this);
  this.serviceName_ = serviceName; 
  this.gaiaToken_ = null; 
}

UTIL_inherits(GD_GaiaTransport, GD_HttpTransport);

GD_GaiaTransport.prototype.toString = function(opt_verbose) {
  return "GD_GaiaTransport() for " + this.serviceName_;
};

/**
* set method GD_GaiaTransport.gaiaToken_
*/
GD_GaiaTransport.prototype.setToken = function(string) { 
  this.gaiaToken_ = string; 
};

/**
* get method GD_GaiaTransport.gaiaToken_
*/
GD_GaiaTransport.prototype.getToken = function() { 
  if (/\r|\n/.test(this.gaiaToken_) === true) {
    alert("potential attack pattern");
  }
  return this.gaiaToken_; 
};

/**
 * function to get an authtoken from the gaia servers
 * @param params {XmlHttpParameters} 
 */
GD_GaiaTransport.prototype.QueryAuthToken = function(params) {
  // Create a new request to the authentication URL.    
  LOG_TRACE("Entering...", "QueryAuthToken()", null);
  var token = null; 
  try {    
    var xmlRequest = XMLX_getHttpRequest();
    var response = null;

    // construct the payload
    
    var postData = GD_Transport.AUTH_USER + 
                   encodeURIComponent(this.getUserName()) + "&" + 
                   GD_Transport.AUTH_PWD + 
                   encodeURIComponent(this.getPassword()) + "&" +
                   GD_Transport.AUTH_SOURCE + "&" + 
                   GD_Transport.AUTH_SERVICE + 
                   encodeURIComponent(this.serviceName_); 

    LOG_TRACE("postData = " + postData, "QueryAuthToken()", null); 
    var responseData = null; 
    
    var request = xmlRequest;
    xmlRequest.onreadystatechange = function() {
      GD_GaiaHandleAsyncData(request, params);
    }
    
    xmlRequest.open("POST", GD_Transport.AUTH_HANDLER, true); 
    xmlRequest.setRequestHeader("content-type", 
                                "application/x-www-form-urlencoded"); 
    LOG_TRACE("sending..." + xmlRequest, "QueryAuthToken()", null);
    xmlRequest.send(postData);
  } catch (e) {
    DBG_ALERT(e.toString());
    DBG_ALERT('The exception happened in QueryAuthData');    
  }
};

/**
 * callback for the QueryAuthToken
 * @param params {XmlHttpParameters} 
 */
function GD_GaiaHandleAsyncData(request, params) {
  LOG_TRACE("Entering callback..", "GD_GaiaHandleAsyncData", null);  
  if (request.readyState != 4) {
    return; 
  }
  GoogleDataFactory.getTransport().HandleOnQueryAuthRequestFinished(
      request, params); 
  LOG_TRACE("QueryAuthCallback done...", "GD_GaiaHandleAsyncData", null);  
}

/** 
 * handling of errors/results of the xmlhttprequest
 * @param request {XMLHttpRequest}
 * @param params {XmlHttpParameters} 
 */ 
GD_GaiaTransport.prototype.HandleOnQueryAuthRequestFinished = function(
    request, params) {
  var response = null;
  var token = null;

  LOG_TRACE("Entering...", "HandleOnQueryAuthRequestFinished", null);
  if (request.status > 299) {
    DBG_ALERT('Bad server response during auth operation: ' + 
             this.xmlRequest.status);
    DBG_ALERT('body: ' + params.postData);
  } else {
    response = request.responseText;
  }

  DBG_ASSERT(response !== null, 
    "we got no data back from the authentication server"); 
    
  if (response !== null) {
    // now parse the result
    var result = response.match(/^Auth=(.*)/m);
    DBG_ASSERT(result !== null);
    DBG_ASSERT(result[0] !== null);
    DBG_ASSERT(result[1] !== null); 
    if (result !== null) {
      token = result[1];       
    }
  }

  LOG_TRACE(token, "HandleOnQueryAuthRequestFinished()", null);
  this.setToken(token);
  // stack needs to unwind, as you can not call from XmlHttp.onreadystate
  // into another xmlHttp object
  window.setTimeout(function() { GD_GaiaContinue(params) }, 0); 
  LOG_TRACE("exiting...", "HandleOnQueryAuthRequestFinished", null);
}

/**
 * callback for the QueryAuthToken
 */
function GD_GaiaContinue(params) {
  LOG_TRACE("GD_GaiaContinue...", "GD_GaiaContinue", null);
  GoogleDataFactory.getTransport().DoRequest(params);
}

/**
* check if we need to get a token
* @returns true if we need to get a token
*/
GD_GaiaTransport.prototype.NeedToken = function() {
  if (UTIL_isPersistable(this.getUserName()) && this.getToken() == null) {
    return true; 
  }
  return false;
};


/** 
 * if a token is needed, will get one and set it
 */
GD_GaiaTransport.prototype.PrepareAuthentication = function(params) {
  if (this.NeedToken()) {
    this.QueryAuthToken(params);
  } else {
    this.DoRequest(params);
  }
};

/**
* sets the auth header for Gaia, if we have a token
* @param xmlHttpRequestObject object to set the header on
*/
GD_GaiaTransport.prototype.SetHeaders = function(xmlHttpRequestObject) {
  LOG_TRACE("Entering...", "Gaia.SetHeaders", null);
  xmlHttpRequestObject.setRequestHeader(GD_Transport.NOREDIRECT_HEADER, "1"); 
  if (this.getCookie() !== null) {
    // set a previously stored shard cookie
    LOG_TRACE("setting a stored cookie..." + this.getCookie(),
      "SetHeaders", null); 
    xmlHttpRequestObject.setRequestHeader(GD_Transport.COOKIE_HEADER,
                                          this.getCookie(), null);
  }
  if (this.getToken() !== null) {
    xmlHttpRequestObject.setRequestHeader(GD_Transport.AUTH_HEADER, 
                    GD_Transport.AUTH_HEADERVALUE + this.getToken());
  }
  this.Base.SetHeaders(xmlHttpRequestObject);
};





/**
* GD_Factory() is used to access the underlying transport layer
* one instance of the factory exists as a global
*/
function GD_Factory() {
  this.transPort_ = GD_getTransport(GD_Transport.TRANSPORT_GOOGLECALENDAR); 
};


// set method GD_Factory.transPort_ 
GD_Factory.prototype.setTransport = function(value) { 
  this.transPort_ = value; 
};

// get method GD_Factory.transPort_
GD_Factory.prototype.getTransport = function() { 
  return this.transPort_; 
};




/**
* declaration of our global GoogleDataFactory, so that the rest 
* of the code can share this...
*/
var GoogleDataFactory = new GD_Factory(); 

// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end
/* Copyright (c) 2006 Google Inc.
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

/**
* @fileoverview
* this file relies on detect.js to do the browser detection
* it abstracts a minimal set of xpath methods so that
* you can do cross browser xpath
* the code seems to work fine in IE6+, Mozilla, Opera8.5+ and Safari2.0+
*/

var XMLX_ieProgId_ = undefined;

/** initialize XMLX_ieProgId_ */
(function () {
  // Nobody (on the web) is really sure which of the progid's listed is totally
  // necessary. It is known, for instance, that certain installations of IE
  // will not work with only Microsoft.XMLHTTP, as well as with MSXML2.XMLHTTP.
  // Safest course seems to be to do this -- include all known progids for
  // XmlHttp.
  if (typeof XMLHttpRequest == 'undefined' &&
      typeof ActiveXObject != 'undefined') {
    var activeXIdents = [
      "MSXML2.XMLHTTP.5.0", "MSXML2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0",
      "MSXML2.XMLHTTP", "MICROSOFT.XMLHTTP.1.0", "MICROSOFT.XMLHTTP.1",
      "MICROSOFT.XMLHTTP" ];
    for (var i = 0; i < activeXIdents.length; ++i) {
      var candidate = activeXIdents[i];
      try {
        new ActiveXObject(candidate);
        XMLX_ieProgId_ = candidate;
        return;
      } catch (e) {
        // do nothing; try next choice
      }
    }
    // couldn't find any matches
    throw ("Could not create ActiveXObject. ActiveX might be disabled, or " +
           "msxml might not be installed");
  }
})();


/** 
 * helper function to return the correct XMLHttpRequest() object
 */
function XMLX_getHttpRequest() {
  if (XMLX_ieProgId_ !== undefined) {
    return new ActiveXObject(XMLX_ieProgId_);
  } else {
    return new XMLHttpRequest();
  }
};


/** 
* helper to return a brand new XMLDomDocument
*/
function XMLX_createDomDocument(optXml) {
  var doc = null;

  if (optXml) {
    if (typeof(optXml) != 'string') {
      optXml = XMLX_serializeNode(optXml); 
    }
  }

  if (document.implementation.createDocument) {
    if (optXml) {
      var domParser = new DOMParser();
      doc = domParser.parseFromString(optXml, "text/xml");
    } else {
      doc = document.implementation.createDocument("", "", null);
    }
  } else {
    doc = new ActiveXObject("MSXML.DOMDocument");
    if (optXml) {
      doc.loadXML(optXml);
    }
  }

  DBG_ASSERT(doc !== null, "Could not create a DOM document"); 

  return doc; 

};


/** 
* helper to serialize an xmlnode into text
*/
function XMLX_serializeNode(xmlNode) {
  var text = null; 

  try {
    // this is for Gecko based browsers
    LOG_TRACE("Trying serializer", "XMLX_serializeNode", null);
    var xmlSerializer = new XMLSerializer();
    // safari 2.02 seems to work fine with this.
    text = xmlSerializer.serializeToString(xmlNode);
  } 
  catch ( e ) {
    try {
      // Internet Explorer.
      LOG_TRACE("Trying IE way to serialize", "XMLX_serializeNode", null);
      text = xmlNode.xml;
    }
    catch (e) {
      // everyone else
      LOG_TRACE("Trying UTIL_xmlText(node)", "XMLX_serializeNode", null);
      text = UTIL_xmlText(xmlNode);
    }
  }
  return text; 
};


/** 
* encapsulates the xpath methods we are exposing
*/
function XMLX_getNodes(xmlNode, xpath) {
  DBG_ASSERT(xmlNode, "XML_getNodes: " + xpath);
  LOG_DEBUG("XML_getNodes: " + xpath);
  try {
    var document; 

    if (xmlNode.ownerDocument !== null) {
      document = xmlNode.ownerDocument; 
    } else {
      document = xmlNode;
    }

    if (Detect.XPathSupport()) {
      if (Detect.IE_5_5_newer()) {
        LOG_DEBUG("XML_getNodes:  Inside IE5.5 path");
        document.setProperty("SelectionLanguage", "XPath");
        document.setProperty("SelectionNamespaces", XML.NAMESPACES);
        return xmlNode.selectNodes(xpath);
      } else {
        LOG_DEBUG("XML_getNodes:  Inside MOZILLA path");
        var nsResolver =  function(prefix){
          var s = XML.NAMESPACE_MAP[prefix];
          if (s) {
            return s;
          } else {
            throw "Unknown prefix: '" + prefix+"'";
          }
        };
        
        
        var tempResult = document.evaluate(xpath, xmlNode, nsResolver, 
            XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null); 
        
        // now make it an array
        var nodeList = new Array(tempResult.snapshotLength);
        for (var i = 0; i < nodeList.length; ++i) {
          nodeList[i] = tempResult.snapshotItem(i);
        }
        
        return nodeList;
      }
    } else {
      return XMLX_parse(xmlNode, xpath); 
    }
  } catch (e) {
     // LOG_XML_PRINT(xmlNode); 
     // this is everyone else
    LOG_DEBUG("XML_getNodes:  catch path - " + e.toString());
    return XMLX_parse(xmlNode, xpath); 
  }
};

/** 
* helper method to call the javascript parser
*/
function XMLX_parse(xmlNode, xpath) {
  LOG_DEBUG("XML_parse:  no xpath support, hence i am here"); 
  var expr = xpathParse(xpath);
  var ctx = new ExprContext(xmlNode);
  var ret = expr.evaluate(ctx);
  return ret.nodeSetValue();
}

/**
* @param xmlNode the node to search from
* @xpath the xpath to search for
* @return the first xmlnode found
*/
function XMLX_getNode(xmlNode, xpath) {
  var result = XMLX_getNodes(xmlNode, xpath);
  if (result) {
    return result[0]; 
  }
  return null; 
}



// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end


/* Copyright (c) 2006 Google Inc.
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
// All Rights Reserved
//
// An XPath parser and evaluator written in JavaScript. The
// implementation is complete except for functions handling
// namespaces.
//
// Reference: [XPATH] XPath Specification
// <http://www.w3.org/TR/1999/REC-xpath-19991116>.
//
//
// The API of the parser has several parts:
//
// 1. The parser function xpathParse() that takes a string and returns
// an expession object.
//
// 2. The expression object that has an evaluate() method to evaluate the
// XPath expression it represents. (It is actually a hierarchy of
// objects that resembles the parse tree, but an application will call
// evaluate() only on the top node of this hierarchy.)
//
// 3. The context object that is passed as an argument to the evaluate()
// method, which represents the DOM context in which the expression is
// evaluated.
//
// 4. The value object that is returned from evaluate() and represents
// values of the different types that are defined by XPath (number,
// string, boolean, and node-set), and allows to convert between them.
//
// These parts are near the top of the file, the functions and data
// that are used internally follow after them.
//

function assert(condition) { DBG_ASSERT(condition); }

// The entry point for the parser.
//
// @param expr a string that contains an XPath expression.
// @return an expression object that can be evaluated with an
// expression context.

// right now, only used for safari

function xpathParse(expr) {
  LOG_DEBUG('XPath parse ' + expr);
  xpathParseInit();

  var cached = xpathCacheLookup(expr);
  if (cached) {
    LOG_DEBUG('XPath ....cached ');

    return cached;
  }

  // Optimize for a few common cases: simple attribute node tests
  // (@id), simple element node tests (page), variable references
  // ($address), numbers (4), multi-step path expressions where each
  // step is a plain element node test
  // (page/overlay/locations/location).
  
  if (expr.match(/^(\$|@)?\w+$/i)) {
    var ret = makeSimpleExpr(expr);
    xpathParseCache[expr] = ret;
    LOG_DEBUG('XPath ... simple');

    return ret;
  }

  if (expr.match(/^\w+(\/\w+)*$/i)) {
    ret = makeSimpleExpr2(expr);
    xpathParseCache[expr] = ret;
    LOG_DEBUG('XPath ... simple 2 ');

    return ret;
  }

  var cachekey = expr; // expr is modified during parse

  var stack = [];
  var ahead = null;
  var previous = null;
  var done = false;

  var parse_count = 0;
  var lexer_count = 0;
  var reduce_count = 0;
  
  while (!done) {
    parse_count+=1;
    expr = expr.replace(/^\s*/, '');
    previous = ahead;
    ahead = null;

    var rule = null;
    var match = '';
    for (var i = 0; i < xpathTokenRules.length; ++i) {
      var result = xpathTokenRules[i].re.exec(expr);
      lexer_count+=1;
      if (result && result.length > 0 && result[0].length > match.length) {
        rule = xpathTokenRules[i];
        match = result[0];
        break;
      }
    }

    // Special case: allow operator keywords to be element and
    // variable names.

    // NOTE: The parser resolves conflicts by looking ahead,
    // and this is the only case where we look back to
    // disambiguate. So this is indeed something different, and
    // looking back is usually done in the lexer (via states in the
    // general case, called "start conditions" in flex(1)). Also,the
    // conflict resolution in the parser is not as robust as it could
    // be, so I'd like to keep as much off the parser as possible (all
    // these precedence values should be computed from the grammar
    // rules and possibly associativity declarations, as in bison(1),
    // and not explicitly set.

    if (rule &&
        (rule == TOK_DIV || 
         rule == TOK_MOD ||
         rule == TOK_AND || 
         rule == TOK_OR) &&
        (!previous || 
         previous.tag == TOK_AT || 
         previous.tag == TOK_DSLASH || 
         previous.tag == TOK_SLASH ||
         previous.tag == TOK_AXIS || 
         previous.tag == TOK_DOLLAR)) {
      rule = TOK_QNAME;
    }

    if (rule) {
      expr = expr.substr(match.length);
      LOG_DEBUG('XPath token:  parse ' + match + ' -- ' + rule.label);

      ahead = {
        tag: rule,
        match: match,
        prec: rule.prec ?  rule.prec : 0, // || 0 is removed by the compiler
        expr: makeTokenExpr(match) };

    } else {
      LOG_DEBUG('XPath DONE');
      done = true;
    }

    while (xpathReduce(stack, ahead)) {
      reduce_count+=1;
      LOG_DEBUG('XPATH stack: ' + stackToString(stack));
    }
  }

  LOG_DEBUG('XPATH stack:' + stackToString(stack));


  if (stack.length != 1) {
    throw 'XPath parse error ' + cachekey + ':\n' + stackToString(stack);
  }

  result = stack[0].expr;
  xpathParseCache[cachekey] = result;
  LOG_DEBUG('XPath parse: ' + parse_count + ' / ' + 
        lexer_count + ' / ' + reduce_count);

  return result;
}

var xpathParseCache = {};

function xpathCacheLookup(expr) {
  return xpathParseCache[expr];
}

function xpathReduce(stack, ahead) {
  var cand = null;

  if (stack.length > 0) {
    var top = stack[stack.length-1];
    var ruleset = xpathRules[top.tag.key];

    if (ruleset) {
      for (var i = 0; i < ruleset.length; ++i) {
        var rule = ruleset[i];
        var match = xpathMatchStack(stack, rule[1]);
        if (match.length) {
          cand = {
            tag: rule[0],
            rule: rule,
            match: match };
          cand.prec = xpathGrammarPrecedence(cand);
          break;
        }
      }
    }
  }

  var ret;
  if (cand && (!ahead || cand.prec > ahead.prec || 
               (ahead.tag.left && cand.prec >= ahead.prec))) {
    for (i = 0; i < cand.match.matchlength; ++i) {
      stack.pop();
    }
    LOG_DEBUG('reduce ' + cand.tag.label + ' ' + cand.prec +
          ' ahead ' + (ahead ? ahead.tag.label + ' ' + ahead.prec + 
                       (ahead.tag.left ? ' left' : '') : ' none '));


    var matchexpr = UTIL_mapExpr(cand.match, function(m) { return m.expr; });
    cand.expr = cand.rule[3].apply(null, matchexpr);

    stack.push(cand);
    ret = true;

  } else {
    if (ahead) {
      LOG_DEBUG('shift ' + ahead.tag.label + ' ' + ahead.prec + 
            (ahead.tag.left ? ' left' : '') +
            ' over ' + (cand ? cand.tag.label + ' ' + 
                        cand.prec : ' none'));
      stack.push(ahead);
    }
    ret = false;
  }
  return ret;
}

function xpathMatchStack(stack, pattern) {

  // NOTE: The stack matches for variable cardinality are
  // greedy but don't do backtracking. This would be an issue only
  // with rules of the form A* A, i.e. with an element with variable
  // cardinality followed by the same element. Since that doesn't
  // occur in the grammar at hand, all matches on the stack are
  // unambiguous.

  var S = stack.length;
  var P = pattern.length;
  var p, s;
  var match = [];
  match.matchlength = 0;
  var ds = 0;
  for (p = P - 1, s = S - 1; p >= 0 && s >= 0; --p, s -= ds) {
    ds = 0;
    var qmatch = [];
    if (pattern[p] == Q_MM) {
      p -= 1;
      match.push(qmatch);
      while (s - ds >= 0 && stack[s - ds].tag == pattern[p]) {
        qmatch.push(stack[s - ds]);
        ds += 1;
        match.matchlength += 1;
      }

    } else if (pattern[p] == Q_01) {
      p -= 1;
      match.push(qmatch);
      while (s - ds >= 0 && ds < 2 && stack[s - ds].tag == pattern[p]) {
        qmatch.push(stack[s - ds]);
        ds += 1;
        match.matchlength += 1;
      }

    } else if (pattern[p] == Q_1M) {
      p -= 1;
      match.push(qmatch);
      if (stack[s].tag == pattern[p]) {
        while (s - ds >= 0 && stack[s - ds].tag == pattern[p]) {
          qmatch.push(stack[s - ds]);
          ds += 1;
          match.matchlength += 1;
        }
      } else {
        return [];
      }

    } else if (stack[s].tag == pattern[p]) {
      match.push(stack[s]);
      ds += 1;
      match.matchlength += 1;

    } else {
      return [];
    }

    UTIL_reverseInplace(qmatch);
    qmatch.expr = UTIL_mapExpr(qmatch, function(m) { return m.expr; });
  }

  UTIL_reverseInplace(match);

  if (p == -1) {
    return match;

  } else {
    return [];
  }
}

function xpathTokenPrecedence(tag) {
  return tag.prec || 2;
}

function xpathGrammarPrecedence(frame) {
  var ret = 0;

  if (frame.rule) { /* normal reduce */
    if (frame.rule.length >= 3 && frame.rule[2] >= 0) {
      ret = frame.rule[2];

    } else {
      for (var i = 0; i < frame.rule[1].length; ++i) {
        var p = xpathTokenPrecedence(frame.rule[1][i]);
        ret = Math.max(ret, p);
      }
    }
  } else if (frame.tag) { /* TOKEN match */
    ret = xpathTokenPrecedence(frame.tag);

  } else if (frame.length) { /* Q_ match */
    for (var j = 0; j < frame.length; ++j) {
      var p = xpathGrammarPrecedence(frame[j]);
      ret = Math.max(ret, p);
    }
  }

  return ret;
}

function stackToString(stack) {
  var ret = '';
  for (var i = 0; i < stack.length; ++i) {
    if (ret) {
      ret += '\n';
    }
    ret += stack[i].tag.label;
  }
  return ret;
}


// XPath expression evaluation context. An XPath context consists of a
// DOM node, a list of DOM nodes that contains this node, a number
// that represents the position of the single node in the list, and a
// current set of variable bindings. (See XPath spec.)
//
// The interface of the expression context:
//
//   Constructor -- gets the node, its position, the node set it
//   belongs to, and a parent context as arguments. The parent context
//   is used to implement scoping rules for variables: if a variable
//   is not found in the current context, it is looked for in the
//   parent context, recursively. Except for node, all arguments have
//   default values: default position is 0, default node set is the
//   set that contains only the node, and the default parent is null.
//
//     Notice that position starts at 0 at the outside interface;
//     inside XPath expressions this shows up as position()=1.
//
//   clone() -- creates a new context with the current context as
//   parent. If passed as argument to clone(), the new context has a
//   different node, position, or node set. What is not passed is
//   inherited from the cloned context.
//
//   setVariable(name, expr) -- binds given XPath expression to the
//   name.
//
//   getVariable(name) -- what the name says.
//
//   setNode(node, position) -- sets the context to the new node and
//   its corresponding position. Needed to implement scoping rules for
//   variables in XPath. (A variable is visible to all subsequent
//   siblings, not only to its children.)

function ExprContext(node, position, nodelist, parent) {
  this.node = node;
  this.position = position || 0;
  this.nodelist = nodelist || [ node ];
  this.variables = {};
  this.parent = parent || null;
  if (parent) {
    this.root = parent.root;
  } else if (this.node.nodeType == DOM_DOCUMENT_NODE) {
    // NOTE: DOM Spec stipulates that the ownerDocument of a
    // document is null. Our root, however is the document that we are
    // processing, so the initial context is created from its document
    // node, which case we must handle here explcitly.
    this.root = node;
  } else {
    this.root = node.ownerDocument;
  }
}

ExprContext.prototype.clone = function(node, position, nodelist) {
  return new ExprContext(
      node || this.node,
      typeof position != 'undefined' ? position : this.position,
      nodelist || this.nodelist, this);
};

ExprContext.prototype.setVariable = function(name, value) {
  this.variables[name] = value;
};

ExprContext.prototype.getVariable = function(name) {
  if (typeof this.variables[name] != 'undefined') {
    return this.variables[name];

  } else if (this.parent) {
    return this.parent.getVariable(name);

  } else {
    return null;
  }
}

ExprContext.prototype.setNode = function(node, position) {
  this.node = node;
  this.position = position;
}


// XPath expression values. They are what XPath expressions evaluate
// to. Strangely, the different value types are not specified in the
// XPath syntax, but only in the semantics, so they don't show up as
// nonterminals in the grammar. Yet, some expressions are required to
// evaluate to particular types, and not every type can be coerced
// into every other type. Although the types of XPath values are
// similar to the types present in JavaScript, the type coercion rules
// are a bit peculiar, so we explicitly model XPath types instead of
// mapping them onto JavaScript types. (See XPath spec.)
//
// The four types are:
//
//   StringValue
//
//   NumberValue
//
//   BooleanValue
//
//   NodeSetValue
//
// The common interface of the value classes consists of methods that
// implement the XPath type coercion rules:
//
//   stringValue() -- returns the value as a JavaScript String,
//
//   numberValue() -- returns the value as a JavaScript Number,
//
//   booleanValue() -- returns the value as a JavaScript Boolean,
//
//   nodeSetValue() -- returns the value as a JavaScript Array of DOM
//   Node objects.
//

function StringValue(value) {
  this.value = value;
  this.type = 'string';
}

StringValue.prototype.stringValue = function() {
  return this.value;
}

StringValue.prototype.booleanValue = function() {
  return this.value.length > 0;
}

StringValue.prototype.numberValue = function() {
  return this.value - 0;
}

StringValue.prototype.nodeSetValue = function() {
  throw this;
}

function BooleanValue(value) {
  this.value = value;
  this.type = 'boolean';
}

BooleanValue.prototype.stringValue = function() {
  return '' + this.value;
}

BooleanValue.prototype.booleanValue = function() {
  return this.value;
}

BooleanValue.prototype.numberValue = function() {
  return this.value ? 1 : 0;
}

BooleanValue.prototype.nodeSetValue = function() {
  throw this;
}

function NumberValue(value) {
  this.value = value;
  this.type = 'number';
}

NumberValue.prototype.stringValue = function() {
  return '' + this.value;
}

NumberValue.prototype.booleanValue = function() {
  return !!this.value;
}

NumberValue.prototype.numberValue = function() {
  return this.value - 0;
}

NumberValue.prototype.nodeSetValue = function() {
  throw this;
}

function NodeSetValue(value) {
  this.value = value;
  this.type = 'node-set';
}

NodeSetValue.prototype.stringValue = function() {
  if (this.value.length == 0) {
    return '';
  } else {
    return UTIL_xmlValue(this.value[0]);
  }
}

NodeSetValue.prototype.booleanValue = function() {
  return this.value.length > 0;
}

NodeSetValue.prototype.numberValue = function() {
  return this.stringValue() - 0;
}

NodeSetValue.prototype.nodeSetValue = function() {
  return this.value;
};

// XPath expressions. They are used as nodes in the parse tree and
// possess an evaluate() method to compute an XPath value given an XPath
// context. Expressions are returned from the parser. Teh set of
// expression classes closely mirrors the set of non terminal symbols
// in the grammar. Every non trivial nonterminal symbol has a
// corresponding expression class.
//
// The common expression interface consists of the following methods:
//
// evaluate(context) -- evaluates the expression, returns a value.
//
// toString() -- returns the XPath text representation of the
// expression (defined in xsltdebug.js).
//
// parseTree(indent) -- returns a parse tree representation of the
// expression (defined in xsltdebug.js).

function TokenExpr(m) {
  this.value = m;
}

TokenExpr.prototype.evaluate = function() {
  return new StringValue(this.value);
};

function LocationExpr() {
  this.absolute = false;
  this.steps = [];
}

LocationExpr.prototype.appendStep = function(s) {
  this.steps.push(s);
}

LocationExpr.prototype.prependStep = function(s) {
  var steps0 = this.steps;
  this.steps = [ s ];
  for (var i = 0; i < steps0.length; ++i) {
    this.steps.push(steps0[i]);
  }
};

LocationExpr.prototype.evaluate = function(ctx) {
  var start;
  if (this.absolute) {
    start = ctx.root;

  } else {
    start = ctx.node;
  }

  var nodes = [];
  xPathStep(nodes, this.steps, 0, start, ctx);
  return new NodeSetValue(nodes);
};

function xPathStep(nodes, steps, step, input, ctx) {
  var s = steps[step];
  var ctx2 = ctx.clone(input);
  var nodelist = s.evaluate(ctx2).nodeSetValue();


  for (var i = 0; i < nodelist.length; ++i) {
    if (step == steps.length - 1) {
      nodes.push(nodelist[i]);
    } else {
      xPathStep(nodes, steps, step + 1, nodelist[i], ctx);
    }
  }
}

function StepExpr(axis, nodetest, predicate) {
  this.axis = axis;
  this.nodetest = nodetest;
  this.predicate = predicate || [];
}

StepExpr.prototype.appendPredicate = function(p) {
  this.predicate.push(p);
}

StepExpr.prototype.evaluate = function(ctx) {
  var input = ctx.node;
  var nodelist = [];

  // NOTE: When this was a switch() statement, it didn't work
  // in Safari/2.0. Not sure why though; it resulted in the JavaScript
  // console output "undefined" (without any line number or so).

  if (this.axis ==  xpathAxis.ANCESTOR_OR_SELF) {
    nodelist.push(input);
    for (var n = input.parentNode; n; n = input.parentNode) {
      nodelist.push(n);
    }

  } else if (this.axis == xpathAxis.ANCESTOR) {
    for (var n = input.parentNode; n; n = input.parentNode) {
      nodelist.push(n);
    }

  } else if (this.axis == xpathAxis.ATTRIBUTE) {
    UTIL_copyArray(nodelist, input.attributes);

  } else if (this.axis == xpathAxis.CHILD) {
    UTIL_copyArray(nodelist, input.childNodes);
  } else if (this.axis == xpathAxis.DESCENDANT_OR_SELF) {
    nodelist.push(input);
    xpathCollectDescendants(nodelist, input);

  } else if (this.axis == xpathAxis.DESCENDANT) {
    xpathCollectDescendants(nodelist, input);

  } else if (this.axis == xpathAxis.FOLLOWING) {
    for (var n = input.parentNode; n; n = n.parentNode) {
      for (var nn = n.nextSibling; nn; nn = nn.nextSibling) {
        nodelist.push(nn);
        xpathCollectDescendants(nodelist, nn);
      }
    }

  } else if (this.axis == xpathAxis.FOLLOWING_SIBLING) {
    for (var n = input.nextSibling; n; n = input.nextSibling) {
      nodelist.push(n);
    }

  } else if (this.axis == xpathAxis.NAMESPACE) {
    DBG_ALERT('not implemented: axis namespace');

  } else if (this.axis == xpathAxis.PARENT) {
    if (input.parentNode) {
      nodelist.push(input.parentNode);
    }

  } else if (this.axis == xpathAxis.PRECEDING) {
    for (var n = input.parentNode; n; n = n.parentNode) {
      for (var nn = n.previousSibling; nn; nn = nn.previousSibling) {
        nodelist.push(nn);
        xpathCollectDescendantsReverse(nodelist, nn);
      }
    }

  } else if (this.axis == xpathAxis.PRECEDING_SIBLING) {
    for (var n = input.previousSibling; n; n = input.previousSibling) {
      nodelist.push(n);
    }

  } else if (this.axis == xpathAxis.SELF) {
    nodelist.push(input);

  } else {
    throw 'ERROR -- NO SUCH AXIS: ' + this.axis;
  }

  // process node test
  var nodelist0 = nodelist;
  nodelist = [];
  for (var i = 0; i < nodelist0.length; ++i) {
    var n = nodelist0[i];
    if (this.nodetest.evaluate(ctx.clone(n, i, nodelist0)).booleanValue()) {
      nodelist.push(n);
    }
  }

  // process predicates
  for (var i = 0; i < this.predicate.length; ++i) {
    var nodelist0 = nodelist;
    nodelist = [];
    for (var ii = 0; ii < nodelist0.length; ++ii) {
      var n = nodelist0[ii];
      if (this.predicate[i].evaluate(ctx.clone(n, ii, nodelist0)).booleanValue()) {
        nodelist.push(n);
      }
    }
  }

  return new NodeSetValue(nodelist);
};

function NodeTestAny() {
  this.value = new BooleanValue(true);
}

NodeTestAny.prototype.evaluate = function(ctx) {
  return this.value;
};

function NodeTestElement() {}

NodeTestElement.prototype.evaluate = function(ctx) {
  return new BooleanValue(ctx.node.nodeType == DOM_ELEMENT_NODE);
}

function NodeTestText() {}

NodeTestText.prototype.evaluate = function(ctx) {
  return new BooleanValue(ctx.node.nodeType == DOM_TEXT_NODE);
}

function NodeTestComment() {}

NodeTestComment.prototype.evaluate = function(ctx) {
  return new BooleanValue(ctx.node.nodeType == DOM_COMMENT_NODE);
}

function NodeTestPI(target) {
  this.target = target;
}

NodeTestPI.prototype.evaluate = function(ctx) {
  return new
  BooleanValue(ctx.node.nodeType == DOM_PROCESSING_INSTRUCTION_NODE &&
               (!this.target || ctx.node.nodeName == this.target));
}

function NodeTestNC(nsprefix) {
  this.regex = new RegExp("^" + nsprefix + ":");
  this.nsprefix = nsprefix;
}

NodeTestNC.prototype.evaluate = function(ctx) {
  var n = ctx.node;
  return new BooleanValue(this.regex.match(n.nodeName));
}

function NodeTestName(name) {
  this.name = name;
}

NodeTestName.prototype.evaluate = function(ctx) {
  var n = ctx.node;
  return new BooleanValue(n.nodeName == this.name);
}

function PredicateExpr(expr) {
  this.expr = expr;
}

PredicateExpr.prototype.evaluate = function(ctx) {
  var v = this.expr.evaluate(ctx);
  if (v.type == 'number') {
    // NOTE: Internally, position is represented starting with
    // 0, however in XPath position starts with 1. See functions
    // position() and last().
    return new BooleanValue(ctx.position == v.numberValue() - 1);
  } else {
    return new BooleanValue(v.booleanValue());
  }
};

function FunctionCallExpr(name) {
  this.name = name;
  this.args = [];
}

FunctionCallExpr.prototype.appendArg = function(arg) {
  this.args.push(arg);
};

FunctionCallExpr.prototype.evaluate = function(ctx) {
  var fn = '' + this.name.value;
  var f = this.xpathfunctions[fn];
  if (f) {
    return f.call(this, ctx);
  } else {
    LOG_DEBUG('XPath NO SUCH FUNCTION ' + fn);
    return new BooleanValue(false);
  }
};

FunctionCallExpr.prototype.xpathfunctions = {
  'last': function(ctx) {
    assert(this.args.length == 0);
    // NOTE: XPath position starts at 1.
    return new NumberValue(ctx.nodelist.length);
  },

  'position': function(ctx) {
    assert(this.args.length == 0);
    // NOTE: XPath position starts at 1.
    return new NumberValue(ctx.position + 1);
  },

  'count': function(ctx) {
    assert(this.args.length == 1);
    var v = this.args[0].evaluate(ctx);
    return new NumberValue(v.nodeSetValue().length);
  },

  'id': function(ctx) {
    assert(this.args.length == 1);
    var e = this.args.evaluate(ctx);
    var ret = [];
    var ids;
    if (e.type == 'node-set') {
      ids = [];
      for (var i = 0; i < e.length; ++i) {
        var v = UTIL_xmlValue(e[i]).split(/\s+/);
        for (var ii = 0; ii < v.length; ++ii) {
          ids.push(v[ii]);
        }
      }
    } else {
      ids = e.split(/\s+/);
    }
    var d = ctx.node.ownerDocument;
    for (var i = 0; i < ids.length; ++i) {
      var n = d.getElementById(ids[i]);
      if (n) {
        ret.push(n);
      }
    }
    return new NodeSetValue(ret);
  },

  'local-name': function(ctx) {
    DBG_ALERT('not implmented yet: XPath function local-name()');
  },

  'namespace-uri': function(ctx) {
    DBG_ALERT('not implmented yet: XPath function namespace-uri()');
  },

  'name': function(ctx) {
    assert(this.args.length == 1 || this.args.length == 0);
    var n;
    if (this.args.length == 0) {
      n = [ ctx.node ];
    } else {
      n = this.args[0].evaluate(ctx).nodeSetValue();
    }

    if (n.length == 0) {
      return new StringValue('');
    } else {
      return new StringValue(n[0].nodeName);
    }
  },

  'string':  function(ctx) {
    assert(this.args.length == 1 || this.args.length == 0);
    if (this.args.length == 0) {
      return new StringValue(new NodeSetValue([ ctx.node ]).stringValue());
    } else {
      return new StringValue(this.args[0].evaluate(ctx).stringValue());
    }
  },

  'concat': function(ctx) {
    var ret = '';
    for (var i = 0; i < this.args.length; ++i) {
      ret += this.args[i].evaluate(ctx).stringValue();
    }
    return new StringValue(ret);
  },

  'starts-with': function(ctx) {
    assert(this.args.length == 2);
    var s0 = this.args[0].evaluate(ctx).stringValue();
    var s1 = this.args[1].evaluate(ctx).stringValue();
    return new BooleanValue(s0.indexOf(s1) == 0);
  },

  'contains': function(ctx) {
    assert(this.args.length == 2);
    var s0 = this.args[0].evaluate(ctx).stringValue();
    var s1 = this.args[1].evaluate(ctx).stringValue();
    return new BooleanValue(s0.indexOf(s1) != -1);
  },

  'substring-before': function(ctx) {
    assert(this.args.length == 2);
    var s0 = this.args[0].evaluate(ctx).stringValue();
    var s1 = this.args[1].evaluate(ctx).stringValue();
    var i = s0.indexOf(s1);
    var ret;
    if (i == -1) {
      ret = '';
    } else {
      ret = s0.substr(0,i);
    }
    return new StringValue(ret);
  },

  'substring-after': function(ctx) {
    assert(this.args.length == 2);
    var s0 = this.args[0].evaluate(ctx).stringValue();
    var s1 = this.args[1].evaluate(ctx).stringValue();
    var i = s0.indexOf(s1);
    var ret;
    if (i == -1) {
      ret = '';
    } else {
      ret = s0.substr(i + s1.length);
    }
    return new StringValue(ret);
  },

  'substring': function(ctx) {
    // NOTE: XPath defines the position of the first character in a
    // string to be 1, in JavaScript this is 0 ([XPATH] Section 4.2).
    assert(this.args.length == 2 || this.args.length == 3);
    var s0 = this.args[0].evaluate(ctx).stringValue();
    var s1 = this.args[1].evaluate(ctx).numberValue();
    var ret;
    if (this.args.length == 2) {
      var i1 = Math.max(0, Math.round(s1) - 1);
      ret = s0.substr(i1);

    } else {
      var s2 = this.args[2].evaluate(ctx).numberValue();
      var i0 = Math.round(s1) - 1;
      var i1 = Math.max(0, i0);
      var i2 = Math.round(s2) - Math.max(0, -i0);
      ret = s0.substr(i1, i2);
    }
    return new StringValue(ret);
  },

  'string-length': function(ctx) {
    var s;
    if (this.args.length > 0) {
      s = this.args[0].evaluate(ctx).stringValue();
    } else {
      s = new NodeSetValue([ ctx.node ]).stringValue();
    }
    return new NumberValue(s.length);
  },

  'normalize-space': function(ctx) {
    var s;
    if (this.args.length > 0) {
      s = this.args[0].evaluate(ctx).stringValue();
    } else {
      s = new NodeSetValue([ ctx.node ]).stringValue();
    }
    s = s.replace(/^\s*/,'').replace(/\s*$/,'').replace(/\s+/g, ' ');
    return new StringValue(s);
  },

  'translate': function(ctx) {
    assert(this.args.length == 3);
    var s0 = this.args[0].evaluate(ctx).stringValue();
    var s1 = this.args[1].evaluate(ctx).stringValue();
    var s2 = this.args[2].evaluate(ctx).stringValue();

    for (var i = 0; i < s1.length; ++i) {
      s0 = s0.replace(new RegExp(s1.charAt(i), 'g'), s2.charAt(i));
    }
    return new StringValue(s0);
  },

  'boolean': function(ctx) {
    assert(this.args.length == 1);
    return new BooleanValue(this.args[0].evaluate(ctx).booleanValue());
  },

  'not': function(ctx) {
    assert(this.args.length == 1);
    var ret = !this.args[0].evaluate(ctx).booleanValue();
    return new BooleanValue(ret);
  },

  'true': function(ctx) {
    assert(this.args.length == 0);
    return new BooleanValue(true);
  },

  'false': function(ctx) {
    assert(this.args.length == 0);
    return new BooleanValue(false);
  },

  'lang': function(ctx) {
    assert(this.args.length == 1);
    var lang = this.args[0].evaluate(ctx).stringValue();
    var xmllang;
    var n = ctx.node;
    while (n && n != n.parentNode /* just in case ... */) {
      xmllang = n.getAttribute('xml:lang');
      if (xmllang) {
        break;
      }
      n = n.parentNode;
    }
    if (!xmllang) {
      return new BooleanValue(false);
    } else {
      var re = new RegExp('^' + lang + '$', 'i');
      return new BooleanValue(xmllang.match(re) ||
                              xmllang.replace(/_.*$/,'').match(re));
    }
  },

  'number': function(ctx) {
    assert(this.args.length == 1 || this.args.length == 0);

    if (this.args.length == 1) {
      return new NumberValue(this.args[0].evaluate(ctx).numberValue());
    } else {
      return new NumberValue(new NodeSetValue([ ctx.node ]).numberValue());
    }
  },

  'sum': function(ctx) {
    assert(this.args.length == 1);
    var n = this.args[0].evaluate(ctx).nodeSetValue();
    var sum = 0;
    for (var i = 0; i < n.length; ++i) {
      sum += UTIL_xmlValue(n[i]) - 0;
    }
    return new NumberValue(sum);
  },

  'floor': function(ctx) {
    assert(this.args.length == 1);
    var num = this.args[0].evaluate(ctx).numberValue();
    return new NumberValue(Math.floor(num));
  },

  'ceiling': function(ctx) {
    assert(this.args.length == 1);
    var num = this.args[0].evaluate(ctx).numberValue();
    return new NumberValue(Math.ceil(num));
  },

  'round': function(ctx) {
    assert(this.args.length == 1);
    var num = this.args[0].evaluate(ctx).numberValue();
    return new NumberValue(Math.round(num));
  },

  // TODO: The following functions are custom. There is a
  // standard that defines how to add functions, which should be
  // applied here.

  'ext-join': function(ctx) {
    assert(this.args.length == 2);
    var nodes = this.args[0].evaluate(ctx).nodeSetValue();
    var delim = this.args[1].evaluate(ctx).stringValue();
    var ret = '';
    for (var i = 0; i < nodes.length; ++i) {
      if (ret) {
        ret += delim;
      }
      ret += UTIL_xmlValue(nodes[i]);
    }
    return new StringValue(ret);
  },

  // ext-if() evaluates and returns its second argument, if the
  // boolean value of its first argument is true, otherwise it
  // evaluates and returns its third argument.

  'ext-if': function(ctx) {
    assert(this.args.length == 3);
    if (this.args[0].evaluate(ctx).booleanValue()) {
      return this.args[1].evaluate(ctx);
    } else {
      return this.args[2].evaluate(ctx);
    }
  },

  // ext-cardinal() evaluates its single argument as a number, and
  // returns the current node that many times. It can be used in the
  // select attribute to iterate over an integer range.
  
  'ext-cardinal': function(ctx) {
    assert(this.args.length >= 1);
    var c = this.args[0].evaluate(ctx).numberValue();
    var ret = [];
    for (var i = 0; i < c; ++i) {
      ret.push(ctx.node);
    }
    return new NodeSetValue(ret);
  }
};

function UnionExpr(expr1, expr2) {
  this.expr1 = expr1;
  this.expr2 = expr2;
}

UnionExpr.prototype.evaluate = function(ctx) {
  var nodes1 = this.expr1.evaluate(ctx).nodeSetValue();
  var nodes2 = this.expr2.evaluate(ctx).nodeSetValue();
  var I1 = nodes1.length;
  for (var i2 = 0; i2 < nodes2.length; ++i2) {
    for (var i1 = 0; i1 < I1; ++i1) {
      if (nodes1[i1] == nodes2[i2]) {
        // break inner loop and continue outer loop, labels confuse
        // the js compiler, so we don't use them here.
        i1 = I1;
      }
    }
    nodes1.push(nodes2[i2]);
  }
  return new NodeSetValue(nodes2);
};

function PathExpr(filter, rel) {
  this.filter = filter;
  this.rel = rel;
}

PathExpr.prototype.evaluate = function(ctx) {
  var nodes = this.filter.evaluate(ctx).nodeSetValue();
  var nodes1 = [];
  for (var i = 0; i < nodes.length; ++i) {
    var nodes0 = this.rel.evaluate(ctx.clone(nodes[i], i, nodes)).nodeSetValue();
    for (var ii = 0; ii < nodes0.length; ++ii) {
      nodes1.push(nodes0[ii]);
    }
  }
  return new NodeSetValue(nodes1);
};

function FilterExpr(expr, predicate) {
  this.expr = expr;
  this.predicate = predicate;
}

FilterExpr.prototype.evaluate = function(ctx) {
  var nodes = this.expr.evaluate(ctx).nodeSetValue();
  for (var i = 0; i < this.predicate.length; ++i) {
    var nodes0 = nodes;
    nodes = [];
    for (var j = 0; j < nodes0.length; ++j) {
      var n = nodes0[j];
      if (this.predicate[i].evaluate(ctx.clone(n, j, nodes0)).booleanValue()) {
        nodes.push(n);
      }
    }
  }

  return new NodeSetValue(nodes);
}

function UnaryMinusExpr(expr) {
  this.expr = expr;
}

UnaryMinusExpr.prototype.evaluate = function(ctx) {
  return new NumberValue(-this.expr.evaluate(ctx).numberValue());
};

function BinaryExpr(expr1, op, expr2) {
  this.expr1 = expr1;
  this.expr2 = expr2;
  this.op = op;
}

BinaryExpr.prototype.evaluate = function(ctx) {
  var ret;
  switch (this.op.value) {
    case 'or':
      ret = new BooleanValue(this.expr1.evaluate(ctx).booleanValue() ||
                             this.expr2.evaluate(ctx).booleanValue());
      break;

    case 'and':
      ret = new BooleanValue(this.expr1.evaluate(ctx).booleanValue() &&
                             this.expr2.evaluate(ctx).booleanValue());
      break;

    case '+':
      ret = new NumberValue(this.expr1.evaluate(ctx).numberValue() +
                            this.expr2.evaluate(ctx).numberValue());
      break;

    case '-':
      ret = new NumberValue(this.expr1.evaluate(ctx).numberValue() -
                            this.expr2.evaluate(ctx).numberValue());
      break;

    case '*':
      ret = new NumberValue(this.expr1.evaluate(ctx).numberValue() *
                            this.expr2.evaluate(ctx).numberValue());
      break;

    case 'mod':
      ret = new NumberValue(this.expr1.evaluate(ctx).numberValue() %
                            this.expr2.evaluate(ctx).numberValue());
      break;

    case 'div':
      ret = new NumberValue(this.expr1.evaluate(ctx).numberValue() /
                            this.expr2.evaluate(ctx).numberValue());
      break;

    case '=':
      ret = this.compare(ctx, function(x1, x2) { return x1 == x2; });
      break;

    case '!=':
      ret = this.compare(ctx, function(x1, x2) { return x1 != x2; });
      break;

    case '<':
      ret = this.compare(ctx, function(x1, x2) { return x1 < x2; });
      break;

    case '<=':
      ret = this.compare(ctx, function(x1, x2) { return x1 <= x2; });
      break;

    case '>':
      ret = this.compare(ctx, function(x1, x2) { return x1 > x2; });
      break;

    case '>=':
      ret = this.compare(ctx, function(x1, x2) { return x1 >= x2; });
      break;

    default:
      DBG_ALERT('BinaryExpr.evaluate: ' + this.op.value);
  }
  return ret;
};

BinaryExpr.prototype.compare = function(ctx, cmp) {
  var v1 = this.expr1.evaluate(ctx);
  var v2 = this.expr2.evaluate(ctx);

  var ret;
  if (v1.type == 'node-set' && v2.type == 'node-set') {
    var n1 = v1.nodeSetValue();
    var n2 = v2.nodeSetValue();
    ret = false;
    for (var i1 = 0; i1 < n1.length; ++i1) {
      for (var i2 = 0; i2 < n2.length; ++i2) {
        if (cmp(UTIL_xmlValue(n1[i1]), UTIL_xmlValue(n2[i2]))) {
          ret = true;
          // Break outer loop. Labels confuse the jscompiler and we
          // don't use them.
          i2 = n2.length;
          i1 = n1.length;
        }
      }
    }

  } else if (v1.type == 'node-set' || v2.type == 'node-set') {

    if (v1.type == 'number') {
      var s = v1.numberValue();
      var n = v2.nodeSetValue();

      ret = false;
      for (var i = 0;  i < n.length; ++i) {
        var nn = UTIL_xmlValue(n[i]) - 0;
        if (cmp(s, nn)) {
          ret = true;
          break;
        }
      }

    } else if (v2.type == 'number') {
      var n = v1.nodeSetValue();
      var s = v2.numberValue();

      ret = false;
      for (var i = 0;  i < n.length; ++i) {
        var nn = UTIL_xmlValue(n[i]) - 0;
        if (cmp(nn, s)) {
          ret = true;
          break;
        }
      }

    } else if (v1.type == 'string') {
      var s = v1.stringValue();
      var n = v2.nodeSetValue();

      ret = false;
      for (var i = 0;  i < n.length; ++i) {
        var nn = UTIL_xmlValue(n[i]);
        if (cmp(s, nn)) {
          ret = true;
          break;
        }
      }

    } else if (v2.type == 'string') {
      var n = v1.nodeSetValue();
      var s = v2.stringValue();

      ret = false;
      for (var i = 0;  i < n.length; ++i) {
        var nn = UTIL_xmlValue(n[i]);
        if (cmp(nn, s)) {
          ret = true;
          break;
        }
      }

    } else {
      ret = cmp(v1.booleanValue(), v2.booleanValue());
    }

  } else if (v1.type == 'boolean' || v2.type == 'boolean') {
    ret = cmp(v1.booleanValue(), v2.booleanValue());

  } else if (v1.type == 'number' || v2.type == 'number') {
    ret = cmp(v1.numberValue(), v2.numberValue());

  } else {
    ret = cmp(v1.stringValue(), v2.stringValue());
  }

  return new BooleanValue(ret);
}

function LiteralExpr(value) {
  this.value = value;
}

LiteralExpr.prototype.evaluate = function(ctx) {
  return new StringValue(this.value);
};

function NumberExpr(value) {
  this.value = value;
}

NumberExpr.prototype.evaluate = function(ctx) {
  return new NumberValue(this.value);
};

function VariableExpr(name) {
  this.name = name;
}

VariableExpr.prototype.evaluate = function(ctx) {
  return ctx.getVariable(this.name);
}

// Factory functions for semantic values (i.e. Expressions) of the
// productions in the grammar. When a production is matched to reduce
// the current parse state stack, the function is called with the
// semantic values of the matched elements as arguments, and returns
// another semantic value. The semantic value is a node of the parse
// tree, an expression object with an evaluate() method that evaluates the
// expression in an actual context. These factory functions are used
// in the specification of the grammar rules, below.

function makeTokenExpr(m) {
  return new TokenExpr(m);
}

function passExpr(e) {
  return e;
}

function makeLocationExpr1(slash, rel) {
  rel.absolute = true;
  return rel;
}

function makeLocationExpr2(dslash, rel) {
  rel.absolute = true;
  rel.prependStep(makeAbbrevStep(dslash.value));
  return rel;
}

function makeLocationExpr3(slash) {
  var ret = new LocationExpr();
  ret.appendStep(makeAbbrevStep('.'));
  ret.absolute = true;
  return ret;
}

function makeLocationExpr4(dslash) {
  var ret = new LocationExpr();
  ret.absolute = true;
  ret.appendStep(makeAbbrevStep(dslash.value));
  return ret;
}

function makeLocationExpr5(step) {
  var ret = new LocationExpr();
  ret.appendStep(step);
  return ret;
}

function makeLocationExpr6(rel, slash, step) {
  rel.appendStep(step);
  return rel;
}

function makeLocationExpr7(rel, dslash, step) {
  rel.appendStep(makeAbbrevStep(dslash.value));
  return rel;
}

function makeStepExpr1(dot) {
  return makeAbbrevStep(dot.value);
}

function makeStepExpr2(ddot) {
  return makeAbbrevStep(ddot.value);
}

function makeStepExpr3(axisname, axis, nodetest) {
  return new StepExpr(axisname.value, nodetest);
}

function makeStepExpr4(at, nodetest) {
  return new StepExpr('attribute', nodetest);
}

function makeStepExpr5(nodetest) {
  return new StepExpr('child', nodetest);
}

function makeStepExpr6(step, predicate) {
  step.appendPredicate(predicate);
  return step;
}

function makeAbbrevStep(abbrev) {
  switch (abbrev) {
  case '//':
    return new StepExpr('descendant-or-self', new NodeTestAny);

  case '.':
    return new StepExpr('self', new NodeTestAny);

  case '..':
    return new StepExpr('parent', new NodeTestAny);
  }
}

function makeNodeTestExpr1(asterisk) {
  return new NodeTestElement;
}

function makeNodeTestExpr2(ncname, colon, asterisk) {
  return new NodeTestNC(ncname.value);
}

function makeNodeTestExpr3(qname) {
  return new NodeTestName(qname.value);
}

function makeNodeTestExpr4(typeo, parenc) {
  var type = typeo.value.replace(/\s*\($/, '');
  switch(type) {
  case 'node':
    return new NodeTestAny;

  case 'text':
    return new NodeTestText;

  case 'comment':
    return new NodeTestComment;

  case 'processing-instruction':
    return new NodeTestPI;
  }
}

function makeNodeTestExpr5(typeo, target, parenc) {
  var type = typeo.replace(/\s*\($/, '');
  if (type != 'processing-instruction') {
    throw type;
  }
  return new NodeTestPI(target.value);
}

function makePredicateExpr(pareno, expr, parenc) {
  return new PredicateExpr(expr);
}

function makePrimaryExpr(pareno, expr, parenc) {
  return expr;
}

function makeFunctionCallExpr1(name, pareno, parenc) {
  return new FunctionCallExpr(name);
}

function makeFunctionCallExpr2(name, pareno, arg1, args, parenc) {
  var ret = new FunctionCallExpr(name);
  ret.appendArg(arg1);
  for (var i = 0; i < args.length; ++i) {
    ret.appendArg(args[i]);
  }
  return ret;
}

function makeArgumentExpr(comma, expr) {
  return expr;
}

function makeUnionExpr(expr1, pipe, expr2) {
  return new UnionExpr(expr1, expr2);
}

function makePathExpr1(filter, slash, rel) {
  return new PathExpr(filter, rel);
}

function makePathExpr2(filter, dslash, rel) {
  rel.prependStep(makeAbbrevStep(dslash.value));
  return new PathExpr(filter, rel);
}

function makeFilterExpr(expr, predicates) {
  if (predicates.length > 0) {
    return new FilterExpr(expr, predicates);
  } else {
    return expr;
  }
}

function makeUnaryMinusExpr(minus, expr) {
  return new UnaryMinusExpr(expr);
}

function makeBinaryExpr(expr1, op, expr2) {
  return new BinaryExpr(expr1, op, expr2);
}

function makeLiteralExpr(token) {
  // remove quotes from the parsed value:
  var value = token.value.substring(1, token.value.length - 1);
  return new LiteralExpr(value);
}

function makeNumberExpr(token) {
  return new NumberExpr(token.value);
}

function makeVariableReference(dollar, name) {
  return new VariableExpr(name.value);
}

// Used before parsing for optimization of common simple cases. See
// the begin of xpathParse() for which they are.
function makeSimpleExpr(expr) {
  if (expr.charAt(0) == '$') {
    return new VariableExpr(expr.substr(1));
  } else if (expr.charAt(0) == '@') {
    var a = new NodeTestName(expr.substr(1));
    var b = new StepExpr('attribute', a);
    var c = new LocationExpr();
    c.appendStep(b);
    return c;
  } else if (expr.match(/^[0-9]+$/)) {
    return new NumberExpr(expr);
  } else {
    var a = new NodeTestName(expr);
    var b = new StepExpr('child', a);
    var c = new LocationExpr();
    c.appendStep(b);
    return c;
  }
}

function makeSimpleExpr2(expr) {
  var steps = expr.split('/');
  var c = new LocationExpr();
  for (var i in steps) {
    var a = new NodeTestName(steps[i]);
    var b = new StepExpr('child', a);
    c.appendStep(b);
  }
  return c;
}

// The axes of XPath expressions.

var xpathAxis = {
  ANCESTOR_OR_SELF: 'ancestor-or-self',
  ANCESTOR: 'ancestor',
  ATTRIBUTE: 'attribute',
  CHILD: 'child',
  DESCENDANT_OR_SELF: 'descendant-or-self',
  DESCENDANT: 'descendant',
  FOLLOWING_SIBLING: 'following-sibling',
  FOLLOWING: 'following',
  NAMESPACE: 'namespace',
  PARENT: 'parent',
  PRECEDING_SIBLING: 'preceding-sibling',
  PRECEDING: 'preceding',
  SELF: 'self'
};

var xpathAxesRe = [
    xpathAxis.ANCESTOR_OR_SELF,
    xpathAxis.ANCESTOR,
    xpathAxis.ATTRIBUTE,
    xpathAxis.CHILD,
    xpathAxis.DESCENDANT_OR_SELF,
    xpathAxis.DESCENDANT,
    xpathAxis.FOLLOWING_SIBLING,
    xpathAxis.FOLLOWING,
    xpathAxis.NAMESPACE,
    xpathAxis.PARENT,
    xpathAxis.PRECEDING_SIBLING,
    xpathAxis.PRECEDING,
    xpathAxis.SELF
].join('|');


// The tokens of the language. The label property is just used for
// generating debug output. The prec property is the precedence used
// for shift/reduce resolution. Default precedence is 0 as a lookahead
// token and 2 on the stack. TODO: this is certainly not
// necessary and too complicated. Simplify this!

// NOTE: tabular formatting is the big exception, but here it should
// be OK.

var TOK_PIPE =   { label: "|",   prec:   17, re: new RegExp("^\\|") };
var TOK_DSLASH = { label: "//",  prec:   19, re: new RegExp("^//")  };
var TOK_SLASH =  { label: "/",   prec:   30, re: new RegExp("^/")   };
var TOK_AXIS =   { label: "::",  prec:   20, re: new RegExp("^::")  };
var TOK_COLON =  { label: ":",   prec: 1000, re: new RegExp("^:")  };
var TOK_AXISNAME = { label: "[axis]", re: new RegExp('^(' + xpathAxesRe + ')') };
var TOK_PARENO = { label: "(",   prec:   34, re: new RegExp("^\\(") };
var TOK_PARENC = { label: ")",               re: new RegExp("^\\)") };
var TOK_DDOT =   { label: "..",  prec:   34, re: new RegExp("^\\.\\.") };
var TOK_DOT =    { label: ".",   prec:   34, re: new RegExp("^\\.") };
var TOK_AT =     { label: "@",   prec:   34, re: new RegExp("^@")   };

var TOK_COMMA =  { label: ",",               re: new RegExp("^,") };

var TOK_OR =     { label: "or",  prec:   10, re: new RegExp("^or\\b") };
var TOK_AND =    { label: "and", prec:   11, re: new RegExp("^and\\b") };
var TOK_EQ =     { label: "=",   prec:   12, re: new RegExp("^=")   };
var TOK_NEQ =    { label: "!=",  prec:   12, re: new RegExp("^!=")  };
var TOK_GE =     { label: ">=",  prec:   13, re: new RegExp("^>=")  };
var TOK_GT =     { label: ">",   prec:   13, re: new RegExp("^>")   };
var TOK_LE =     { label: "<=",  prec:   13, re: new RegExp("^<=")  };
var TOK_LT =     { label: "<",   prec:   13, re: new RegExp("^<")   };
var TOK_PLUS =   { label: "+",   prec:   14, re: new RegExp("^\\+"), left: true };
var TOK_MINUS =  { label: "-",   prec:   14, re: new RegExp("^\\-"), left: true };
var TOK_DIV =    { label: "div", prec:   15, re: new RegExp("^div\\b"), left: true };
var TOK_MOD =    { label: "mod", prec:   15, re: new RegExp("^mod\\b"), left: true };

var TOK_BRACKO = { label: "[",   prec:   32, re: new RegExp("^\\[") };
var TOK_BRACKC = { label: "]",               re: new RegExp("^\\]") };
var TOK_DOLLAR = { label: "$",               re: new RegExp("^\\$") };

var TOK_NCNAME = { label: "[ncname]", re: new RegExp('^[a-z][-\\w]*','i') };

var TOK_ASTERISK = { label: "*", prec: 15, re: new RegExp("^\\*"), left: true };
var TOK_LITERALQ = { label: "[litq]", prec: 20, re: new RegExp("^'[^\\']*'") };
var TOK_LITERALQQ = {
  label: "[litqq]",
  prec: 20,
  re: new RegExp('^"[^\\"]*"')
};

var TOK_NUMBER  = {
  label: "[number]",
  prec: 35,
  re: new RegExp('^\\d+(\\.\\d*)?') };

var TOK_QNAME = {
  label: "[qname]",
  re: new RegExp('^([a-z][-\\w]*:)?[a-z][-\\w]*','i')
};

var TOK_NODEO = {
  label: "[nodetest-start]",
  re: new RegExp('^(processing-instruction|comment|text|node)\\(')
};

// The table of the tokens of our grammar, used by the lexer: first
// column the tag, second column a regexp to recognize it in the
// input, third column the precedence of the token, fourth column a
// factory function for the semantic value of the token.
//
// NOTE: order of this list is important, because the first match
// counts. Cf. DDOT and DOT, and AXIS and COLON.

var xpathTokenRules = [
    TOK_DSLASH,
    TOK_SLASH,
    TOK_DDOT,
    TOK_DOT,
    TOK_AXIS,
    TOK_COLON,
    TOK_AXISNAME,
    TOK_NODEO,
    TOK_PARENO,
    TOK_PARENC,
    TOK_BRACKO,
    TOK_BRACKC,
    TOK_AT,
    TOK_COMMA,
    TOK_OR,
    TOK_AND,
    TOK_NEQ,
    TOK_EQ,
    TOK_GE,
    TOK_GT,
    TOK_LE,
    TOK_LT,
    TOK_PLUS,
    TOK_MINUS,
    TOK_ASTERISK,
    TOK_PIPE,
    TOK_MOD,
    TOK_DIV,
    TOK_LITERALQ,
    TOK_LITERALQQ,
    TOK_NUMBER,
    TOK_QNAME,
    TOK_NCNAME,
    TOK_DOLLAR
];

// All the nonterminals of the grammar. The nonterminal objects are
// identified by object identity; the labels are used in the debug
// output only.
var XPathLocationPath = { label: "LocationPath" };
var XPathRelativeLocationPath = { label: "RelativeLocationPath" };
var XPathAbsoluteLocationPath = { label: "AbsoluteLocationPath" };
var XPathStep = { label: "Step" };
var XPathNodeTest = { label: "NodeTest" };
var XPathPredicate = { label: "Predicate" };
var XPathLiteral = { label: "Literal" };
var XPathExpr = { label: "Expr" };
var XPathPrimaryExpr = { label: "PrimaryExpr" };
var XPathVariableReference = { label: "Variablereference" };
var XPathNumber = { label: "Number" };
var XPathFunctionCall = { label: "FunctionCall" };
var XPathArgumentRemainder = { label: "ArgumentRemainder" };
var XPathPathExpr = { label: "PathExpr" };
var XPathUnionExpr = { label: "UnionExpr" };
var XPathFilterExpr = { label: "FilterExpr" };
var XPathDigits = { label: "Digits" };

var xpathNonTerminals = [
    XPathLocationPath,
    XPathRelativeLocationPath,
    XPathAbsoluteLocationPath,
    XPathStep,
    XPathNodeTest,
    XPathPredicate,
    XPathLiteral,
    XPathExpr,
    XPathPrimaryExpr,
    XPathVariableReference,
    XPathNumber,
    XPathFunctionCall,
    XPathArgumentRemainder,
    XPathPathExpr,
    XPathUnionExpr,
    XPathFilterExpr,
    XPathDigits
];

// Quantifiers that are used in the productions of the grammar.
var Q_01 = { label: "?" };
var Q_MM = { label: "*" };
var Q_1M = { label: "+" };

// Tag for left associativity (right assoc is implied by undefined).
var ASSOC_LEFT = true;

// The productions of the grammar. Columns of the table:
//
// - target nonterminal,
// - pattern,
// - precedence,
// - semantic value factory
//
// The semantic value factory is a function that receives parse tree
// nodes from the stack frames of the matched symbols as arguments and
// returns an a node of the parse tree. The node is stored in the top
// stack frame along with the target object of the rule. The node in
// the parse tree is an expression object that has an evaluate() method
// and thus evaluates XPath expressions.
//
// The precedence is used to decide between reducing and shifting by
// comparing the precendence of the rule that is candidate for
// reducing with the precedence of the look ahead token. Precedence of
// -1 means that the precedence of the tokens in the pattern is used
// instead. TODO: It shouldn't be necessary to explicitly assign
// precedences to rules.

var xpathGrammarRules =
  [
   [ XPathLocationPath, [ XPathRelativeLocationPath ], 18,
     passExpr ],
   [ XPathLocationPath, [ XPathAbsoluteLocationPath ], 18,
     passExpr ],

   [ XPathAbsoluteLocationPath, [ TOK_SLASH, XPathRelativeLocationPath ], 18, 
     makeLocationExpr1 ],
   [ XPathAbsoluteLocationPath, [ TOK_DSLASH, XPathRelativeLocationPath ], 18,
     makeLocationExpr2 ],

   [ XPathAbsoluteLocationPath, [ TOK_SLASH ], 0,
     makeLocationExpr3 ],
   [ XPathAbsoluteLocationPath, [ TOK_DSLASH ], 0,
     makeLocationExpr4 ],

   [ XPathRelativeLocationPath, [ XPathStep ], 31,
     makeLocationExpr5 ],
   [ XPathRelativeLocationPath,
     [ XPathRelativeLocationPath, TOK_SLASH, XPathStep ], 31,
     makeLocationExpr6 ],
   [ XPathRelativeLocationPath,
     [ XPathRelativeLocationPath, TOK_DSLASH, XPathStep ], 31,
     makeLocationExpr7 ],

   [ XPathStep, [ TOK_DOT ], 33,
     makeStepExpr1 ],
   [ XPathStep, [ TOK_DDOT ], 33,
     makeStepExpr2 ],
   [ XPathStep,
     [ TOK_AXISNAME, TOK_AXIS, XPathNodeTest ], 33,
     makeStepExpr3 ],
   [ XPathStep, [ TOK_AT, XPathNodeTest ], 33,
     makeStepExpr4 ],
   [ XPathStep, [ XPathNodeTest ], 33,
     makeStepExpr5 ],
   [ XPathStep, [ XPathStep, XPathPredicate ], 33,
     makeStepExpr6 ],

   [ XPathNodeTest, [ TOK_ASTERISK ], 33,
     makeNodeTestExpr1 ],
   [ XPathNodeTest, [ TOK_NCNAME, TOK_COLON, TOK_ASTERISK ], 33,
     makeNodeTestExpr2 ],
   [ XPathNodeTest, [ TOK_QNAME ], 33,
     makeNodeTestExpr3 ],
   [ XPathNodeTest, [ TOK_NODEO, TOK_PARENC ], 33,
     makeNodeTestExpr4 ],
   [ XPathNodeTest, [ TOK_NODEO, XPathLiteral, TOK_PARENC ], 33,
     makeNodeTestExpr5 ],

   [ XPathPredicate, [ TOK_BRACKO, XPathExpr, TOK_BRACKC ], 33,
     makePredicateExpr ],

   [ XPathPrimaryExpr, [ XPathVariableReference ], 33,
     passExpr ],
   [ XPathPrimaryExpr, [ TOK_PARENO, XPathExpr, TOK_PARENC ], 33,
     makePrimaryExpr ],
   [ XPathPrimaryExpr, [ XPathLiteral ], 30,
     passExpr ],
   [ XPathPrimaryExpr, [ XPathNumber ], 30,
     passExpr ],
   [ XPathPrimaryExpr, [ XPathFunctionCall ], 30,
     passExpr ],

   [ XPathFunctionCall, [ TOK_QNAME, TOK_PARENO, TOK_PARENC ], -1,
     makeFunctionCallExpr1 ],
   [ XPathFunctionCall,
     [ TOK_QNAME, TOK_PARENO, XPathExpr, XPathArgumentRemainder, Q_MM,
       TOK_PARENC ], -1,
     makeFunctionCallExpr2 ],
   [ XPathArgumentRemainder, [ TOK_COMMA, XPathExpr ], -1,
     makeArgumentExpr ],

   [ XPathUnionExpr, [ XPathPathExpr ], 20,
     passExpr ],
   [ XPathUnionExpr, [ XPathUnionExpr, TOK_PIPE, XPathPathExpr ], 20,
     makeUnionExpr ],

   [ XPathPathExpr, [ XPathLocationPath ], 20, 
     passExpr ], 
   [ XPathPathExpr, [ XPathFilterExpr ], 19, 
     passExpr ], 
   [ XPathPathExpr, 
     [ XPathFilterExpr, TOK_SLASH, XPathRelativeLocationPath ], 20,
     makePathExpr1 ],
   [ XPathPathExpr,
     [ XPathFilterExpr, TOK_DSLASH, XPathRelativeLocationPath ], 20,
     makePathExpr2 ],

   [ XPathFilterExpr, [ XPathPrimaryExpr, XPathPredicate, Q_MM ], 20,
     makeFilterExpr ], 

   [ XPathExpr, [ XPathPrimaryExpr ], 16,
     passExpr ],
   [ XPathExpr, [ XPathUnionExpr ], 16,
     passExpr ],

   [ XPathExpr, [ TOK_MINUS, XPathExpr ], -1,
     makeUnaryMinusExpr ],

   [ XPathExpr, [ XPathExpr, TOK_OR, XPathExpr ], -1,
     makeBinaryExpr ],
   [ XPathExpr, [ XPathExpr, TOK_AND, XPathExpr ], -1,
     makeBinaryExpr ],

   [ XPathExpr, [ XPathExpr, TOK_EQ, XPathExpr ], -1,
     makeBinaryExpr ],
   [ XPathExpr, [ XPathExpr, TOK_NEQ, XPathExpr ], -1,
     makeBinaryExpr ],

   [ XPathExpr, [ XPathExpr, TOK_LT, XPathExpr ], -1,
     makeBinaryExpr ],
   [ XPathExpr, [ XPathExpr, TOK_LE, XPathExpr ], -1,
     makeBinaryExpr ],
   [ XPathExpr, [ XPathExpr, TOK_GT, XPathExpr ], -1,
     makeBinaryExpr ],
   [ XPathExpr, [ XPathExpr, TOK_GE, XPathExpr ], -1,
     makeBinaryExpr ],

   [ XPathExpr, [ XPathExpr, TOK_PLUS, XPathExpr ], -1,
     makeBinaryExpr, ASSOC_LEFT ],
   [ XPathExpr, [ XPathExpr, TOK_MINUS, XPathExpr ], -1,
     makeBinaryExpr, ASSOC_LEFT ],

   [ XPathExpr, [ XPathExpr, TOK_ASTERISK, XPathExpr ], -1,
     makeBinaryExpr, ASSOC_LEFT ],
   [ XPathExpr, [ XPathExpr, TOK_DIV, XPathExpr ], -1,
     makeBinaryExpr, ASSOC_LEFT ],
   [ XPathExpr, [ XPathExpr, TOK_MOD, XPathExpr ], -1,
     makeBinaryExpr, ASSOC_LEFT ],

   [ XPathLiteral, [ TOK_LITERALQ ], -1,
     makeLiteralExpr ],
   [ XPathLiteral, [ TOK_LITERALQQ ], -1,
     makeLiteralExpr ],

   [ XPathNumber, [ TOK_NUMBER ], -1,
     makeNumberExpr ],

   [ XPathVariableReference, [ TOK_DOLLAR, TOK_QNAME ], 200,
     makeVariableReference ]
   ];

// That function computes some optimizations of the above data
// structures and will be called right here. It merely takes the
// counter variables out of the global scope.

var xpathRules = [];

function xpathParseInit() {
  if (xpathRules.length) {
    return;
  }

  // Some simple optimizations for the xpath expression parser: sort
  // grammar rules descending by length, so that the longest match is
  // first found.

  xpathGrammarRules.sort(function(a,b) {
    var la = a[1].length;
    var lb = b[1].length;
    if (la < lb) {
      return 1;
    } else if (la > lb) {
      return -1;
    } else {
      return 0;
    }
  });

  var k = 1;
  for (var i = 0; i < xpathNonTerminals.length; ++i) {
    xpathNonTerminals[i].key = k++;
  }

  for (i = 0; i < xpathTokenRules.length; ++i) {
    xpathTokenRules[i].key = k++;
  }

  LOG_DEBUG('XPath parse INIT: ' + k + ' rules');

  // Another slight optimization: sort the rules into bins according
  // to the last element (observing quantifiers), so we can restrict
  // the match against the stack to the subest of rules that match the
  // top of the stack.
  //
  // TODO: What we actually want is to compute states as in
  // bison, so that we don't have to do any explicit and iterated
  // match against the stack.

  function push_(array, position, element) {
    if (!array[position]) {
      array[position] = [];
    }
    array[position].push(element);
  }

  for (i = 0; i < xpathGrammarRules.length; ++i) {
    var rule = xpathGrammarRules[i];
    var pattern = rule[1];

    for (var j = pattern.length - 1; j >= 0; --j) {
      if (pattern[j] == Q_1M) {
        push_(xpathRules, pattern[j-1].key, rule);
        break;

      } else if (pattern[j] == Q_MM || pattern[j] == Q_01) {
        push_(xpathRules, pattern[j-1].key, rule);
        --j;

      } else {
        push_(xpathRules, pattern[j].key, rule);
        break;
      }
    }
  }

  LOG_DEBUG('XPath parse INIT: ' + xpathRules.length + ' rule bins');

  var sum = 0;
  UTIL_mapExec(xpathRules, function(i) {
    if (i) {
      sum += i.length;
    }
  });

  LOG_DEBUG('XPath parse INIT: ' + (sum / xpathRules.length) + ' average bin size');
}

// Local utility functions that are used by the lexer or parser.

function xpathCollectDescendants(nodelist, node) {
  for (var n = node.firstChild; n; n = n.nextSibling) {
    nodelist.push(n);
    arguments.callee(nodelist, n);
  }
}

function xpathCollectDescendantsReverse(nodelist, node) {
  for (var n = node.lastChild; n; n = n.previousSibling) {
    nodelist.push(n);
    arguments.callee(nodelist, n);
  }
}


// The entry point for the library: match an expression against a DOM
// node. Returns an XPath value.
function xpathDomEval(expr, node) {
  var expr1 = xpathParse(expr);
  var ret = expr1.evaluate(new ExprContext(node));
  return ret;
}

// Utility function to sort a list of nodes. Used by xsltSort() and
// nxslSelect().
function xpathSort(input, sort) {
  if (sort.length == 0) {
    return;
  }

  var sortlist = [];

  for (var i = 0; i < input.nodelist.length; ++i) {
    var node = input.nodelist[i];
    var sortitem = { node: node, key: [] };
    var context = input.clone(node, 0, [ node ]);
    
    for (var j = 0; j < sort.length; ++j) {
      var s = sort[j];
      var value = s.expr.evaluate(context);

      var evalue;
      if (s.type == 'text') {
        evalue = value.stringValue();
      } else if (s.type == 'number') {
        evalue = value.numberValue();
      }
      sortitem.key.push({ value: evalue, order: s.order });
    }

    // Make the sort stable by adding a lowest priority sort by
    // id. This is very convenient and furthermore required by the
    // spec ([XSLT] - Section 10 Sorting).
    sortitem.key.push({ value: i, order: 'ascending' });

    sortlist.push(sortitem);
  }

  sortlist.sort(xpathSortByKey);

  var nodes = [];
  for (var i = 0; i < sortlist.length; ++i) {
    nodes.push(sortlist[i].node);
  }
  input.nodelist = nodes;
  input.setNode(nodes[0], 0);
}


// Sorts by all order criteria defined. According to the JavaScript
// spec ([ECMA] Section 11.8.5), the compare operators compare strings
// as strings and numbers as numbers.
//
// NOTE: In browsers which do not follow the spec, this breaks only in
// the case that numbers should be sorted as strings, which is very
// uncommon.

function xpathSortByKey(v1, v2) {
  // NOTE: Sort key vectors of different length never occur in
  // xsltSort.

  for (var i = 0; i < v1.key.length; ++i) {
    var o = v1.key[i].order == 'descending' ? -1 : 1;
    if (v1.key[i].value > v2.key[i].value) {
      return +1 * o;
    } else if (v1.key[i].value < v2.key[i].value) {
      return -1 * o;
    }
  }

  return 0;
}

// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end

/* Copyright (c) 2006 Google Inc.
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

/**
* @fileoverview
* XML Support for Mozilla/IE equalization
* also some base XML support classes for convienience, like the binding 
* system
* XML Binding
* - binders are utility classes that are used to read and write values from
*   and to an XML document; they are configured using xpath expressions.
* - there are scalar binders for text nodes and attributes, and a list
*   binder for repeated node elements; more elaborate binders can be hand built.
* - binders are stateless so they are reusable across XML documents.
* - controls are constructed with one or more binders.
* - controls paint into and extract (save) values from DOM elements.
* - type handlers coordinate the binding of controls to XML documents.
*/

var XML = {}; 

XML.FEED_HEADER = '<?xml version="1.0"?>';

XML.DEFAULT_NAMESPACE = "atom";

XML.NAMESPACE_MAP = {
  "atom"          : 'http://www.w3.org/2005/Atom',
  "fs"            : 'http://www.google.com/schema/forseti/2005-04-01',
  "gd"            : 'http://schemas.google.com/g/2005',
  "gCal"          : 'http://schemas.google.com/gCal/2005',
  "xsl"           : 'http://www.w3.org/1999/XSL/Transform',
  "dc"            : 'http://purl.org/dc/elements/1.1/',
  "content"       : 'http://purl.org/rss/1.0/modules/content/',
  "xhtml"         : 'http://www.w3.org/1999/xhtml'};

XML.KIND_SCHEME = "http://schemas.google.com/g/2005#kind";

XML.NAMESPACES = "";

new function() {
  var i = 0;
  for (var abbr in XML.NAMESPACE_MAP) {
    if (abbr) {
      ++i;
      if (i > 0) {
        XML.NAMESPACES += " ";
      }
      XML.NAMESPACES += "xmlns:" + abbr + "='" + XML.NAMESPACE_MAP[abbr] + "'";
    }
  }
}; 

/** 
* @param abbr {String} a string indicating the prefix for the namespace
* @return the full namespace
*/
function XML_getNameSpace(abbr) {
  var ns = XML.NAMESPACE_MAP[abbr];
  DBG_ASSERT(ns, "XML.GET_NAMESPACE: " + abbr);
  return ns;
}


/**
* helper to check a loaded dom document
* @param doc the dom document object
* @return the xml error parser value
*/ 
function XML_ValidateDocument(doc) {
  var err = null; 
  if (!doc) {
    DBG_ALERT("no valid XML document passed to XML_ValidateDocument");
  } else {
    if (doc.parseError !== 0) {
      err = "Could not obtain document; possibly a connection failure or a permission problem." + doc.parseError;
    } else {
      LOG_XML_PRINT(doc);
    }
  }
  return err;
}

/** 
* @param doc {Object} a document object
* @return a string representing the persisted document, including feed header
*/
function XML_serializeDocument(doc) {
  return XML.FEED_HEADER + XMLX_serializeNode(doc);
}

/** 
* @param opt_xml xml to create a document from
* @return an xml document object
*/
function XML_createDocument(opt_xml) {
  LOG_DEBUG("XML_createDocument: " + opt_xml);
  var doc = XMLX_createDomDocument(opt_xml);
  return doc;
}


/** 
* @param xmlDoc xmlDoc to create a node in
* @param parent the parent node
* @param elementName the name of the new element node
* @opt_namespace the optional namespace of the element
* @opt_text the optional text for the element
* @opt_head indicates if the new node should be appended or inserted before
* @return the new node
*/
function XML_createNode(xmlDoc, parent, elementName, opt_namespace, opt_text, opt_head) {
  DBG_ASSERT(xmlDoc, "XML_createNode: " + [xmlDoc, parent, elementName, opt_namespace, opt_text]);
  if (!opt_namespace) {
    opt_namespace = XML.DEFAULT_NAMESPACE;
  }
  var ns = XML_getNameSpace(opt_namespace);
  var node = xmlDoc.CreateNode(1, opt_namespace + ":" + elementName, ns);
  if (opt_text) {
    node.text = opt_text;
  }
  if (parent) {
    if (opt_head) {
      if (parent.childNodes.length) {
        parent.insertBefore(node, parent.childNodes[0]);
      } else {
        parent.appendChild(node);
      }
    } else {
      parent.appendChild(node);
    }
  }
  return node;
}

/**
* @param tag the tag to create
* @opt_namespace the optional namespace the tag is in
* @opt_attributes the attributes of the tag
* @constructor
*/
function XML_Tag(tag, opt_namespace, opt_attributes) {
  this.tag_ = tag;
  if (Detect.XPathSupport() || opt_namespace) {
    // only do the namespace jumbo when we have support
    this.namespace_ = opt_namespace ? opt_namespace : XML.DEFAULT_NAMESPACE;
    this.qname_ = this.namespace_ + ":" + this.tag_;
  } else {
    this.namespace_ = ""; 
    this.qname_ =  this.tag_;
  }
  this.attributes_ = opt_attributes;
  // TODO: iterate attributes for filtering (how to do multiple attribute)
  if (opt_attributes) {
    this.qname_ += "[";
    var count = 0;
    for (var attr in opt_attributes) {
      if (count) {
        this.qname_ += " and ";
      }
      this.qname_ += "@" + attr + "='" + opt_attributes[attr] + "'";
      ++count;
    }
    this.qname_ += "]";
  }
}

XML_Tag.prototype.toString = function() {
  return "XML_Tag('" + this.qname_ + "')";
};

/**
* @xmlRoot the parent node
* @opt_head if the new node should be created before or after
* @return the new node
*/
XML_Tag.prototype.CreateNode = function(xmlRoot, opt_head) {
  var xmlNode = XML_createNode(xmlRoot.ownerDocument, xmlRoot, this.tag_, this.namespace_, null, opt_head);
  for (var attr in this.attributes_) {
    if (this.attributes_[attr]) {
      xmlNode.setAttribute(attr, this.attributes_[attr]);
    }
  }
  return xmlNode;
};

/**
* Example: XML_NodeBinder(new XML_Tag("id")),
* @param tag the tag to bind to
* @constructor
*/
function XML_NodeBinder(tags) {
  if (tags) {
    // make array (since tags can by array or single tag)
    this.tags_ = tags.length ? tags : [tags];
    var qname = "";
    for (var i = 0; i < this.tags_.length; i+=1) {
      if (i > 0) {
        qname += "/";
      }
      qname += this.tags_[i].qname_;
    }
    this.qname_ = qname;
    // TODO: compute parent qname
  } else {
    this.tags_ = null;
    this.qname_ = null;
  }
  this.parentQname_ = null;
}

XML_NodeBinder.prototype.toString = function() {
  return "XML_NodeBinder('" + this.qname_ + "')";
};

/**
* returns the node that is bound
* @param xmlRoot the node to test
* @return the bound node
*/ 
XML_NodeBinder.prototype.GetNode = function(xmlRoot) {
  return this.qname_ ? XMLX_getNode(xmlRoot, this.qname_) : xmlRoot;
};

/**
* either get's the bound node, or creates one in the parent
* @param xmlRoot the node to test
* @return the bound or newly created node
*/ 
XML_NodeBinder.prototype.GetOrCreateNode = function(xmlRoot) {
  var xmlNode = this.GetNode(xmlRoot);
  if (!xmlNode) {
    xmlNode = this.CreateNode(xmlRoot);
  }
  DBG_ASSERT(xmlNode, this + ".GetOrCreateNode: " + xmlRoot);
  return xmlNode;
};


/**
* creates a new node in the parent
* @param xmlRoot the node to test
* @return the newly created node
*/ 
XML_NodeBinder.prototype.CreateNode = function(xmlRoot, opt_head) {
  DBG_ASSERT(this.tags_, "XML_NodeBinder.CreateNode: No tags");

  if (this.tags_.length == 1) {
    return this.tags_[0].CreateNode(xmlRoot, opt_head);
  } else {
    var parentNode = this.GetParentNode(xmlRoot, true);
    return this.tags_[this.tags_.length-1].CreateNode(parentNode, opt_head);
  }
};


/**
* deletes a node in the parent
* @param xmlRoot the node to test
* @return true if there was a node to delete
*/ 
XML_NodeBinder.prototype.DeleteNode = function(xmlRoot) {
  var xmlNode = this.GetNode(xmlRoot);
  if (xmlNode) {
    var parentNode = this.GetParentNode(xmlRoot);
    parentNode.removeChild(xmlNode);
    return true;
  }
  return false;
};

/**
* returns the parentNode. If that one does not exist yet, create one
* @param xmlRoot the node to test
* @param opt_create create if true and parent does not exist yet
* @return the parent node
*/ 
XML_NodeBinder.prototype.GetParentNode = function(xmlRoot, opt_create) {
  DBG_ASSERT(this.tags_, "XML_NodeBinder.GetParentNode: No tags");
  // TODO: pre-compute xpath of parent node
  var len = this.tags_.length;
  
  if (len == 1) {
    return xmlRoot;
  }
  var xmlNode = null;
  if (this.parentQname_) {
    xmlNode = xmlRoot.selectSingleNode(this.parentQname_);
  }
  if (!xmlNode) {
    // create parents
    for (var i = 0; i < len-1; i++) {
      xmlNode = xmlRoot.selectSingleNode(this.tags_[i].qname_);
      if (!xmlNode) {
        if (!opt_create) {
          return null;
        }
        xmlNode = this.tags_[i].CreateNode(xmlRoot);
      }
      xmlRoot = xmlNode;
    }
  }
  return xmlNode;
};

/**
* Example: XML_NodeListBinder(new XML_Tag("id")),
* @param tag the tag to bind to
* @constructor
*/
function XML_NodeListBinder(tags) {
  DBG_ASSERT(tags, "XML_NodeListBinder");
  XML_NodeBinder.call(this, tags);
}

UTIL_inherits(XML_NodeListBinder, XML_NodeBinder);
XML_NodeListBinder.prototype.toString = function() {
  return "XML_NodeListBinder('" + this.qname_ + "')";
};

/**
* @param xmlRoot the node to query
* @return a nodelist 
*/
XML_NodeListBinder.prototype.GetNodes = function(xmlRoot) {
  return XMLX_getNodes(xmlRoot, this.qname_); 
};

/**
* @param xmlRoot the parent node
* @param xmlNode the node to delete
* @return true if successfully deleted
*/
XML_NodeListBinder.prototype.DeleteNode = function(xmlRoot, xmlNode) {
  var parentNode = this.GetParentNode(xmlRoot);
  if (parentNode) {
    parentNode.removeChild(xmlNode);
    return true;
  }
  return false;
};


/**
* removes all child nodes
* @param xmlRoot the parent node
* @return true if successfully deleted
*/
XML_NodeListBinder.prototype.DeleteNodes = function(xmlRoot) {
  var parentNode = this.GetParentNode(xmlRoot);
  if (parentNode) {
    var nodes = this.GetNodes(xmlRoot);
    for (var i = 0; i < nodes.length; i+=1) {
      parentNode.removeChild(nodes[i]);
    }
    return true;
  }
  return false;
};

/**
* Example: XML_TextBinder(new XML_Tag("id")),
* @param tag the tag to bind to
* @opt_default the optional default value
* @constructor
*/
function XML_TextBinder(tags, opt_default) {
  DBG_ASSERT(tags, "XML_TextBinder");
  XML_NodeBinder.call(this, tags);
  this.defaultValue_ = opt_default;
}
UTIL_inherits(XML_TextBinder, XML_NodeBinder);
XML_TextBinder.prototype.toString = function() {
  return "XML_TextBinder(" + this.qname_ + (this.defaultValue_ ? ", " + this.defaultValue_ : "") + ")";
};

/**
* get for the value
* @param xmlRoot the node to get the value from
* @return the value for the binder
*/ 
XML_TextBinder.prototype.getValue = function(xmlRoot) {
  DBG_ASSERT(xmlRoot, "XML_TextBinder.getValue");
  var node = this.GetNode(xmlRoot);
  return(node ? UTIL_xmlValue(node) : this.defaultValue_);
};

/**
* set for the value
* @param xmlRoot the node to set the value at
* @value the value to set
*/ 
XML_TextBinder.prototype.setValue = function(xmlRoot, value) {
  DBG_ASSERT(xmlRoot, "XML_TextBinder.setValue: " + value);
  var node = this.GetOrCreateNode(xmlRoot);
  if (node.text != value) {
    node.text = value;
    return true;
  }
  return false;
};

/**
* Example: new XML_AttributeBinder(new XML_Tag("when"), "startTime"),
* @param tag the tag to bind to
* @param attrName the attribute name to bind
* @opt_default the optional default value
* @constructor
*/
function XML_AttributeBinder(tags, attrName, opt_default) {
  DBG_ASSERT(attrName, "XML_AttributeBinder");
  XML_NodeBinder.call(this, tags);
  this.attrName_ = attrName;
  this.defaultValue_ = opt_default;
}
UTIL_inherits(XML_AttributeBinder, XML_NodeBinder);
XML_AttributeBinder.prototype.toString = function() {
  return "XML_AttributeBinder('" + this.qname_ + "', '" + this.attrName_ + "'"+ (this.defaultValue_ ? ", " + this.defaultValue_ : "") + ")";
};

/**
* get for the value
* @param xmlRoot the node to get the value from
* @return the value for the binder
*/ 
XML_AttributeBinder.prototype.getValue = function(xmlRoot) {
  DBG_ASSERT(xmlRoot, "XML_AttributeBinder.getValue");
  var node = this.GetNode(xmlRoot);
  return(node ? node.getAttribute(this.attrName_) : this.defaultValue_);
};

/**
* set for the value
* @param xmlRoot the node to set the value at
* @value the value to set
*/ 
XML_AttributeBinder.prototype.setValue = function(xmlRoot, value) {
  DBG_ASSERT(xmlRoot, "XML_AttributeBinder.setValue: " + value);
  var node = this.GetOrCreateNode(xmlRoot);
  if (node.getAttribute(this.attrName_) != value) {
    node.setAttribute(this.attrName_, value);
    return true;
  }
  return false;
};


// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end

/* Copyright (c) 2006 Google Inc.
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

/**
* @fileoverview
* This file contains GD_Query class, a helper class that can construct
* Google data API query URIs 
*/

/**
* atom category helper object
*/
function AtomCategory(term, opt_scheme, opt_label) {
  DBG_ASSERT(term, "AtomCategory: " + arguments);
  this.term = term;
  this.scheme = opt_scheme;
  this.label = opt_label;
  if (this.scheme) {
    this.uri = "{" + this.scheme + "}" + term;
  } else {
    this.uri = term; 
  }
  
}
AtomCategory.prototype.toString = function() {
  return this.uri; 
} ; 

/**
* GD_Query helper object
* @constructor
* @param opt_BaseUri the optional base Uri to use
*/
function GD_Query(opt_BaseUri) {
  this.categories_ = new Array();
  this.searchString_ = null;
  this.authorString_ = null;
  this.startdate_ = null;
  this.endDate_ = null;
  this.startIndex_ = 0; 
  this.numToRetrieve_ = 0; 
  this.altFormatString_ = null; 
  this.baseUriString_ = opt_BaseUri; 
  this.updated_ = false;
}

GD_Query.FORMAT_ATOM = "atom";
GD_Query.FORMAT_RSS = "rss"; 

/**
* toString overload. Returns, based on opt_verbose
* either a GD_Query("categories") 
* or       GD_Query("<numCategeories>")
*/
GD_Query.prototype.toString = function(opt_verbose) {
  if (opt_verbose) {
    var str = "GD_Query(";
    for (var i = 0; i < this.categories.length; i+=1) {
      if (i > 0) {
        str += ", ";
      }
      str += this.categories[i];
    }
    str += ")";
    return str;
  } else {
    return "GD_Query(" + this.categories_.length + ")";
  }
};

/**
* add a category to the query and keep the category array sorted.
*/
GD_Query.prototype.addCategory = function(category) {
  var i;
  for (i = this.categories_.length - 1; (i >= 0 && 
      category.uri < this.categories_[i].uri); i-=1) {
    this.categories_[i+1] = this.categories[i];
  }
  this.categories_[i+1] = category;
  this.setUpdated();
};

/**
* clears the categories array of the GD_Query object
*/
GD_Query.prototype.clearCategories = function() {
  if (this.categories_.length > 0) {
    this.categories_.clear();
    this.setUpdated();
  }
};

/**
* set's a category of the GD_Query object
*/
GD_Query.prototype.getCategory = function(index) {
  return this.categories_[index];
};

/**
* get's the category count of the GD_Query object
*/
GD_Query.prototype.getCategoryCount = function() {
  return this.categories_.length;
};

/**
*  set's the search string of the GD_Query object
*/
GD_Query.prototype.setSearchString = function(string) {
  this.searchString_ = string;
  this.setUpdated();
};

/**
* get's the search string of the GD_Query object
*/
GD_Query.prototype.getSearchString = function() {
  return this.searchString_;
};

/**
* set's the author string of the GD_Query object
*/
GD_Query.prototype.setAuthorString = function(string) {
  this.authorString_ = string;
  this.setUpdated();
};

/**
* get's the search string of the GD_Query object
*/
GD_Query.prototype.getAuthorString = function() {
  return this.authorString_;
};

/**
* set's the startdate of the GD_Query object
*/
GD_Query.prototype.setStartDate = function(string) {
  this.startDate_ = string;
  this.setUpdated();
};

/**
* get's the startDate of the GD_Query object
*/
GD_Query.prototype.getStartDate = function() {
  return this.startDate_;
};

/**
* set's the enddate of the GD_Query object
*/
GD_Query.prototype.setEndDate = function(string) {
  this.endDate_ = string;
  this.setUpdated();
};

/**
* get's the endDate of the GD_Query object
*/
GD_Query.prototype.getEndDate = function() {
  return this.endDate_;
};

/**
* set method GD_Query.startIndex_
*/
GD_Query.prototype.setStartIndex = function(string) { 
  this.startIndex_ = string; 
  this.setUpdated();
};

/**
* get method GD_Query.startIndex_
*/
GD_Query.prototype.getStartIndex = function() { 
  return this.startIndex_; 
};

/**
* set method GD_Query.numberToRetrieve_
*/
GD_Query.prototype.setNumberToRetrieve = function(string) { 
  this.numberToRetrieve_ = string; 
  this.setUpdated();
};

/**
* get method GD_Query.numberToRetrieve_
*/
GD_Query.prototype.getNumberToRetrieve = function() { 
  return this.numberToRetrieve_; 
};

/**
* set method GD_Query.altFormatString_
*/
GD_Query.prototype.setFormatString = function(string) { 
  this.altFormatString_ = string; 
  this.setUpdated();
};

/**
* get method GD_Query.altFormatString_
*/
GD_Query.prototype.getFormatString = function() { 
  return this.altFormatString_; 
};

/**
* set method GD_Query.baseUriString_
*/
GD_Query.prototype.setBaseUriString = function(string) { 
  this.baseUriString_ = string; 
  this.setUpdated();
};

/**
* get method GD_Query.baseUriString_
*/
GD_Query.prototype.getBaseUriString = function() { 
  return this.baseUriString_; 
};

/**
* get's the isUpdated property of the GD_Query object
*/
GD_Query.prototype.isUpdated = function() {
  return this.updated_;
};

/**
* set's the isUpdated property of the GD_Query object
*/
GD_Query.prototype.setUpdated = function() {
  this.updated_ = true;
};

/**
* clone's the object
*/
GD_Query.prototype.clone = function() {
  var retQuery = new GD_Query();
  for (var i = 0; i < this.categories_.length; i+=1) {
    retQuery.addCategory(this.categories_[i]);
  }
  retQuery.setSearchString(this.searchString_);
  retQuery.setUpToDate();

  return retQuery;
};

/**
* calculates the target URI string based on the properties set
*/
GD_Query.prototype.CalculateUri = function() {
  var retQueryUri = this.baseUriString_; 
  var paraConnection = "?" ; 
  var firstTime = true; 

  if (UTIL_isPersistable(retQueryUri) && 
    retQueryUri.charAt(retQueryUri.length-1)=="/") {
    // remove the trailing slash
    retQueryUri = retQueryUri.substr(0, retQueryUri.length -1); 
  }


  for (var count in this.categories_) {
    var category = this.categories_[count]; 
    if (UTIL_isPersistable(category.toString())) {
      if (firstTime === true) {
        retQueryUri += "/-";
        firstTime = false; 
      }
      retQueryUri += "/" + UTIL_urlEncodeString(category.toString());
    }
  }


  if (UTIL_isPersistable(this.altFormatString) && 
    this.altFormatString != GD_Query.FORMAT_ATOM) {
    retQueryUri += paraConnection + "alt=" + 
                  encodeURIComponent(this.altFormatString); 
    paraConnection = "&"; 
  }

  if (UTIL_isPersistable(this.searchString_)) {
    retQueryUri += paraConnection + "q=" + 
                  encodeURIComponent(this.searchString_);
    paraConnection = "&"; 
  }


  if (UTIL_isPersistable(this.authorString_)) {
    retQueryUri += paraConnection + "author=" + 
              encodeURIComponent(this.authorString_);
    paraConnection = "&"; 
  }



  if (this.startDate_ instanceof Date) {
    retQueryUri += paraConnection + "updated-min=" + 
                   UTIL_dateToRFC3339(this.startDate_);
    paraConnection = "&"; 
  }

  if (this.endDate_ instanceof Date) {
    retQueryUri += paraConnection + "updated-max=" + 
                    UTIL_dateToRFC3339(this.endDate_); 
    paraConnection = "&"; 
  }

  if (this.startIndex_ !== 0) {
    retQueryUri += paraConnection + "start-index=" + 
                  encodeURIComponent(this.startIndex_);
    paraConnection = "&"; 
  }

  // Currently, if the max-results parameter is not set, it defaults to 25
  // rather than returning all of the results. For now, if the client does
  // not specify the max-results in the JavaScript library, we set it to an
  // arbitrarily large number, in this case, 5000.
  var numToRetrieve = (this.numToRetrieve_) ? this.numToRetrieve_ : 5000;
  retQueryUri += paraConnection + "max-results=" + 
                 encodeURIComponent(numToRetrieve); 
  paraConnection = "&"; 

  return retQueryUri; 
};


// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end

/* Copyright (c) 2006 Google Inc.
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

/**
* @fileoverview
* This file contains the GD_Entry class, a representation and
* access mechanims to the atom/feed/entry xml part of the feed 
* properties are exposed as needed, so feel free to add
* to them following the examples below as you need them
*
*/

/**
* A GD_Entry object provides methods for persisting the feed/entry
* object, as well as some rudimentary access methods to properties
*/ 
function GD_Entry(xmlNode, ofFeed) {
  this.xmlNode_ = xmlNode;
  this.ofFeed_ = ofFeed; 
  this.isDirty_ = false; 
  this.isDeleted_ = false;
  this.isNew_ = false;
}

/**
* GoogleData entry commonly used properties, add at your leisure
*/
GD_Entry.BINDERS = {
  "id"                : new XML_TextBinder(new XML_Tag("id")),
  "title"             : new XML_TextBinder(new XML_Tag("title")),
  "updated"           : new XML_TextBinder(new XML_Tag("updated")),
  "published"         : new XML_TextBinder(new XML_Tag("published")),
  "author.name"       : new XML_TextBinder([new XML_Tag("author"), 
                        new XML_Tag("name")]),
  "content"           : new XML_TextBinder(new XML_Tag("content")),
  "summary"           : new XML_TextBinder(new XML_Tag("summary")), 
    // link supports
  "edit"              : new XML_AttributeBinder(new XML_Tag("link", 
                        "atom",{"rel":"edit"}), "href"), 
  "self"              : new XML_AttributeBinder(new XML_Tag("link", 
                        "atom",{"rel":"self"}), "href"), 
  "alternate"         : new XML_AttributeBinder(new XML_Tag("link", 
                        "atom", {"rel":"alternate"}), "href"), 
    // categories
  "kind"              : new XML_AttributeBinder(new XML_Tag("category", 
                        "atom",{"scheme":XML.KIND_SCHEME}), "term")  
}; 




/** 
* property get/set for the dirty flag
*/
GD_Entry.prototype.setDirty = function(optFlag){
  this.isDirty_ = (optFlag === false) ? false: true; 
};
GD_Entry.prototype.getDirty = function() {
  return this.isDirty_; 
};


/** 
* property get/set for the deleted property
*/
GD_Entry.prototype.setDeleted = function(optFlag) { 
  this.isDeleted_ = (optFlag === false) ? false: true;   
  this.setDirty();
};
GD_Entry.prototype.getDeleted = function() { 
  return this.isDeleted_; 
};


/** 
* property get/set for the isNew_ property
*/
GD_Entry.prototype.setNew = function(optFlag) { 
  this.isNew_ = (optFlag === false) ? false: true; 
  this.setDirty();
};
GD_Entry.prototype.getNew = function() { 
  return this.isNew_; 
};

GD_Entry.prototype.toString = function() {
  return "GD_Entry(" + GD_Entry.BINDERS.title.getValue(this.xmlNode_) +  ")";
};


/** 
* to make the entry more usable, provide direct access to some properties
*/


/** 
* property get/set to Atom:entry:Id
*/
GD_Entry.prototype.getId = function() { 
  return GD_Entry.BINDERS.id.getValue(this.xmlNode_); 
};
GD_Entry.prototype.setId = function(newValue) {
  GD_Entry.BINDERS.id.setValue(this.xmlNode_, newValue);
  this.setDirty();
};


/** 
* property get/set to Atom:entry:title
*/
GD_Entry.prototype.getTitle = function() { 
  return GD_Entry.BINDERS.title.getValue(this.xmlNode_); 
};
GD_Entry.prototype.setTitle = function(newValue) {
  GD_Entry.BINDERS.title.setValue(this.xmlNode_, newValue);
  this.setDirty();
};

/** 
* property get/set to Atom:entry:updated
*/
GD_Entry.prototype.getUpdated = function() { 
  return GD_Entry.BINDERS.updated.getValue(this.xmlNode_); 
};
GD_Entry.prototype.setUpdated = function(newValue) {
  GD_Entry.BINDERS.updated.setValue(this.xmlNode_, newValue);
  this.setDirty();
};

/** 
* property get/set to Atom:entry:published
*/
GD_Entry.prototype.getPublished = function() { 
  return GD_Entry.BINDERS.published.getValue(this.xmlNode_); 
};
GD_Entry.prototype.setPublished = function(newValue) {
  GD_Entry.BINDERS.published.setValue(this.xmlNode_, newValue);
  this.setDirty();
};


/** 
* property get/set to Atom:entry:author:name
*/
GD_Entry.prototype.getAuthorName = function() { 
  return GD_Entry.BINDERS["author.name"].getValue(this.xmlNode_); 
};
GD_Entry.prototype.setAuthorName = function(newValue) {
  GD_Entry.BINDERS["author.name"].setValue(this.xmlNode_, newValue);
  this.setDirty();
};

/** 
* property get/set to Atom:entry:content
*/
GD_Entry.prototype.getContent = function() { 
  return GD_Entry.BINDERS.content.getValue(this.xmlNode_); 
};
GD_Entry.prototype.setContent = function(newValue) {
  GD_Entry.BINDERS.content.setValue(this.xmlNode_, newValue);
  this.setDirty();
};

/** 
* property get/set to Atom:entry:summary
*/
GD_Entry.prototype.getSummary = function() { 
  return GD_Entry.BINDERS.summary.getValue(this.xmlNode_); 
};
GD_Entry.prototype.setSummary = function(newValue) {
  GD_Entry.BINDERS.summary.setValue(this.xmlNode_, newValue);
  this.setDirty();
};

/** 
* property get/set to Atom:entry:category==kind
*/
GD_Entry.prototype.getKind = function() { 
  return GD_Entry.BINDERS.kind.getValue(this.xmlNode_); 
};
GD_Entry.prototype.setKind = function(newValue) {
  GD_Entry.BINDERS.kind.setValue(this.xmlNode_, newValue);
  this.setDirty();
};

/** 
* property get to Atom:entry:rel==edit
*/
GD_Entry.prototype.getEditUri = function() {
  return GD_Entry.BINDERS.edit.getValue(this.xmlNode_); 
};

/** 
* property get to Atom:entry:rel==self
*/
GD_Entry.prototype.getSelfUri = function() {
  return GD_Entry.BINDERS.self.getValue(this.xmlNode_); 
};
/** 
* property get to Atom:entry:rel==alternate
*/
GD_Entry.prototype.getAlternateUri = function() {
  return GD_Entry.BINDERS.alternate.getValue(this.xmlNode_); 
};


/**
* save the entry, return a XML document
* serialized into a string
* @returns the xml representing the entry as a string
*/
GD_Entry.prototype.save = function() {
  var document = XML_createDocument(this.xmlNode_); 
  return XML_serializeDocument(document); 
};


/**
* update the entry back to the store
* @params optForceIt if this is true, the entry will be saved regardless 
*                    of state
*/
GD_Entry.prototype.flush = function(optForceIt) {
  // only update if we are dirty or forced
  if (this.getDirty() || optForceIt === true) {
    if (this.getDeleted()) {
      LOG_DEBUG("deleting entry", true);
      GoogleDataFactory.getTransport().DeleteEntry(this);
    } else if (this.getNew()) {
      LOG_DEBUG("inserting new  entry", true);
      GoogleDataFactory.getTransport().InsertEntry(this.ofFeed_, this);
    } else {
      LOG_DEBUG("updating entry", true);
      GoogleDataFactory.getTransport().UpdateEntry(this);
    }
  }
};



/**
* this supports our loading functionallity
*/
// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end
/* Copyright (c) 2006 Google Inc.
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

/**
 * @fileoverview
 * GoogleData Feed object
 * access mechanims to the atom/feed/ xml part of the feed 
 */

/**
 * Create a new GoogleData Feed object.
 *
 * Because a GD_Feed is loaded asynchronously, the following usage will likely
 * result in undesired behavior:
 *
 * var feed = new GD_Feed(someUrl);
 * var entries = feed.getEntries();
 * // start using entries...
 *
 * When getEntries() is invoked in the above example, the new GD_Feed has not
 * loaded yet, so entries will be an empty array. Instead, getEntries() should
 * be invoked from a callback:
 *
 * var callback = function(feed) {
 *   var entries = feed.getEntries();
 *   // start using entries...
 * }
 * var feed = new GD_Feed(someUrl);
 *
 * In this example, getEntries() is not called until after the feed is loaded,
 * so it will result in the expected behavior. 
 *
 * @param uriStringOrQuery either a URI string or a queryObject
 * @param opt_callback {function} callback function that is called once
 *     the new GD_Feed is loaded -- it will receive the GD_Feed as
 *     its only argument, and its return value will be ignored.
 * @constructor
 */
function GD_Feed(uriStringOrQuery, opt_callback) {
  // declare the variables
  this.uriString_ = null; 
  this.xmlDom_ = null; 
  this.entries_ = null; 
  this.links_ = null;
  this.isLoaded_ = false;
  if (typeof(uriStringOrQuery) == "string") {
    this.uriString_ = uriStringOrQuery; 
  } else if (uriStringOrQuery != null) {
    this.uriString_ = uriStringOrQuery.CalculateUri(); 
  }
  if (this.uriString_) {
    var thisFeed = this;
    var callback = function(xmlDom) {
      thisFeed.xmlDom_ = xmlDom;
      XML_ValidateDocument(thisFeed.xmlDom_);
      thisFeed.isLoaded_ = true;
      LOG_XML_PRINT(thisFeed.xmlDom_);
      if (opt_callback) {
        opt_callback.call(null, thisFeed);
      }
    };
    LOG_TRACE(this.uriString_, "loading from", null); 
    // now try to get the underlying xml document
    GoogleDataFactory.getTransport().
        GetDomDocument(this.uriString_, callback);
    LOG_TRACE("finished reload Feed", "GD_Feed", uriStringOrQuery);
  }
};

/** @return true if the feed has been loaded */
GD_Feed.prototype.isLoaded = function() {
  return this.isLoaded_;
}

/**
 * GoogleData entry commonly used properties
 */
GD_Feed.BINDERS = {
  entries : new XML_NodeListBinder(new XML_Tag("entry")),
  links   : new XML_NodeListBinder(new XML_Tag("link")), 
  postUri : new XML_AttributeBinder(new XML_Tag("link", "atom",
              {rel : "http://schemas.google.com/g/2005#post"}), "href"), 
  nextUri : new XML_AttributeBinder(new XML_Tag("link", "atom",
              {rel : "next"}), "href"), 
  prevUri : new XML_AttributeBinder(new XML_Tag("link", "atom",
              {rel : "prev"}), "href")
};

/**
 * Get the entries that are defined in this feed.
 * @return GD_Entry[]
 */ 
GD_Feed.prototype.getEntries = function() {
  if (this.entries_ === null && this.xmlDom_) {
    // first time. create them.
    this.entries_ = [];
    if (this.xmlDom_.documentElement) {
      var entryNodes = GD_Feed.BINDERS.entries.GetNodes(
          this.xmlDom_.documentElement); 
      for (var i = 0; i < entryNodes.length; ++i) {
        var entryNode = entryNodes[i];
        var entry = this.createEntry(entryNode); 
        this.entries_.push(entry);
      }
    }
  }
  // return a copy of the array since the client can mutate it
  return [].concat(this.entries_);
};

/**
 * this is designed for subclassing, just encapsulates what 
 * kind of entry to create
 * @protected
 */ 
GD_Feed.prototype.createEntry = function(xmlNode) {
  return new GD_Entry(xmlNode, this);
}

/**
 * get the creation URI back
 */
GD_Feed.prototype.getUri = function() {
  return this.uriString_; 
};

/**
 * get the link collection
 */
GD_Feed.prototype.getLinks = function() {
  if (this.links_ === null) {
    // first time. create them.
    this.links_ = GD_Feed.BINDERS.links.getNodes(
        this.xmlDom_.documentElement); 
  }
  return this.links_; 
};


/** @return a string representation of the feed */
GD_Feed.prototype.toString = function() {
  return this.uriString_; 
};

/**
 * get the POST Uri for new Inserts
 */
GD_Feed.prototype.getPostUri = function() {
  return GD_Feed.BINDERS.postUri.getValue(this.xmlDom_.documentElement); 
};

/**
 * get the URI for the next chunk
 */
GD_Feed.prototype.getNextChunkUri = function() {
  return GD_Feed.BINDERS.nextUri.getValue(this.xmlDom_.documentElement); 
};

/**
 * get the URI for the previous chunk
 */
GD_Feed.prototype.getPrevChunkUri = function() {
  return GD_Feed.BINDERS.prevUri.getValue(this.xmlDom_.documentElement); 
};

/**
 * Get a new GD_Feed for the next chunk
 *
 * @param opt_callback {function} callback function that is called once
 *     the new GD_Feed is loaded -- it will receive the GD_Feed as
 *     its only argument, and its return value will be ignored.
 */
GD_Feed.prototype.nextChunk = function(opt_callback) {
  return new GD_Feed(this.getNextChunkUri(), opt_callback);
};

/**
 * Get a new GD_Feed for the previous chunk
 *
 * @param opt_callback {function} callback function that is called once
 *     the new GD_Feed is loaded -- it will receive the GD_Feed as
 *     its only argument, and its return value will be ignored.
 */
GD_Feed.prototype.prevChunk = function(opt_callback) {
  return new GD_Feed(this.getPrevChunkUri(), opt_callback);
};

/**
 * save the feed back to the server
 */
GD_Feed.prototype.flush = function() {
  // as we can not save a feed in the first place,
  // all we are doing here is iterating over the entries
  // and call flush on them
  // ensure entries are there
  var entries = this.getEntries();   
  for (var i = 0; i < entries.length; ++i) {
    entries[i].flush();
  }
};



// remove this if you merge 
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end
/* Copyright (c) 2006 Google Inc.
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

/**
 * @fileoverview
 * this file contains calendar specific subclasses of
 * the entry and feed classes
 */

/**
 * GD_EventFeed subclass.
 */

/** @see GD_Feed */
function GD_EventFeed(uriStringOrQuery, callBack) {
  LOG_TRACE("Created EventFeed", "GD_EventFeed()", null);
  GD_Feed.call(this, uriStringOrQuery, callBack);
}

UTIL_inherits(GD_EventFeed, GD_Feed);

GD_EventFeed.BINDERS = {
  when : new XML_NodeListBinder(new XML_Tag("when", "gd")),
  originalEvent : new XML_NodeListBinder(new XML_Tag("originalEvent", "gd")),
  recurrence : new XML_NodeListBinder(new XML_Tag("recurrence", "gd"))
};

/**
 * This is designed for subclassing, just encapsulates what
 * kind of entry to create. Here we want event entries in the GD_EventFeed.
 * @protected
 */
GD_EventFeed.prototype.createEntry = function(xmlNode) {
  return new GD_EventEntry(xmlNode, this);
}

/**
 * Get the entries that are defined in this feed.
 * @return GD_EventEntry[]
 */
GD_EventFeed.prototype.getEntries = function() {
  if (this.entries_ === null && this.xmlDom_) {

    // declare some functions private to this function

    /**
     * Find the <gd:originalEvent> node in an <entry>, if any
     *
     * @param entryNode {Element}
     * @return the <gd:originalEvent>, if present; otherwise, return null
     */
    function hasOriginalEvent_(entryNode) {
      var oe = GD_EventFeed.BINDERS.originalEvent.GetNodes(entryNode);
      return (oe && oe.length) ? oe[0] : null;
    }

    /**
     * We want to sort an array of entryNodes so that nodes that are
     * specialized recurrences appear first. This way, when they are
     * processed, they can be added to a "processed" list, so that when
     * an entryNode that represents a recurrence is processed, it can
     * check each instance of the recurrence against the "processed" list
     * so that it does add a duplicate entry
     */
    function compareEntryNode_(a, b) {
      return !!hasOriginalEvent_(b) - !!hasOriginalEvent_(a);
    }

    /**
     * Entry is part of a recurrence, so some of its methods need to be
     * overridden.
     */
    function adaptEntry_(entry, id, whenNode) {
      var start = whenNode.getAttribute('startTime');
      var end = whenNode.getAttribute('endTime');
      entry.getStartTime = function() { return start; }
      entry.getEndTime = function() { return end; }
      entry.getId = function() { return id + start + end; }
    }

    // first time. create them.
    this.entries_ = [];
    var entryNodesCollection =
        GD_Feed.BINDERS.entries.GetNodes(this.xmlDom_.documentElement);
    // keys in processedNodes are identifiers for specialized recurrences
    // that have been processed individually instead of as part of
    // a recurrence
    var processedNodes = {};
    var key, entry;
    // any specialized events must be processed first so that
    // processedNodes is populated before recurrences are evaluated
    var entryNodes = [];
    for (var i = 0; i < entryNodesCollection.length; ++i) {
      entryNodes.push(entryNodesCollection[i]);
    }
    entryNodes.sort(compareEntryNode_);
    for (var i = 0; i < entryNodes.length; ++i) {
      var entryNode = entryNodes[i];
      // see if this entry is a recurrence
      if (GD_EventFeed.BINDERS.recurrence.GetNodes(entryNode).length) {
        var whens = GD_EventFeed.BINDERS.when.GetNodes(entryNode);
        var id = undefined;
        for (var j = 0; j < whens.length; ++j) {
          entry = this.createEntry(entryNode);
          if (!id) id = entry.getId();
          var when = whens[j];
          key = id + when.getAttribute('startTime');
          // a specialization of this recurrence may have already been
          // processed, in which case, continue
          if (key in processedNodes) continue;
          adaptEntry_(entry, id, when);
          this.entries_.push(entry);
        }
      } else {
        // check if has gd:originalEvent, indictating specialization
        entry = this.createEntry(entryNode);
        var originalEvent = hasOriginalEvent_(entryNode);
        if (originalEvent) {
          // this node is also listed in a recurrence
          var oeId = originalEvent.getAttribute('href');
          var oeWhen = GD_EventFeed.BINDERS.when.GetNodes(originalEvent)[0];
          key = oeId + entry.getStartTime();
          processedNodes[key] = undefined;
        }
        this.entries_.push(entry);
      }
    }
  }
  return [].concat(this.entries_);
};

/**
 * Get a new GD_EventFeed for the next chunk
 *
 * @param opt_callback {function} callback function that is called once
 *     the new GD_EventFeed is loaded -- it will receive the GD_EventFeed as
 *     its only argument, and its return value will be ignored.
 */
GD_EventFeed.prototype.nextChunk = function(opt_callback) {
  return new GD_EventFeed(this.getNextChunkUri(), opt_callback);
};

/**
 * Get a new GD_EventFeed for the previous chunk
 *
 * @param opt_callback {function} callback function that is called once
 *     the new GD_EventFeed is loaded -- it will receive the GD_EventFeed as
 *     its only argument, and its return value will be ignored.
 */
GD_EventFeed.prototype.prevChunk = function(opt_callback) {
  return new GD_EventFeed(this.getPrevChunkUri(), opt_callback);
};

/**
* GD_EventEntry subclass.
*/
function GD_EventEntry(xmlNode, ofFeed) {
  LOG_TRACE("Created EventEntry", "GD_EventEntry()", null);
  GD_Entry.call(this, xmlNode, ofFeed);
}

UTIL_inherits(GD_EventEntry, GD_Entry);

/** Event status enum */
GD_EventEntry.EventStatus = {
  CANCELED : 0,
  CONFIRMED : 1,
  TENTATIVE : 2,
  UNKNOWN : 3
};

/**
 * Binders specific for an event entry
 */
GD_EventEntry.BINDERS = {
  // gd:when specific
  "start"             : new XML_AttributeBinder(
                            new XML_Tag("when", "gd"), "startTime"),
  "end"               : new XML_AttributeBinder(
                            new XML_Tag("when", "gd"), "endTime"),
  // gd:eventStatus
  "eventStatus"       : new XML_AttributeBinder(
                            new XML_Tag("eventStatus", "gd"), "value")
};

// gd: specific section

/**
 * property get/set to gd:when.startTime
 */
GD_EventEntry.prototype.getStartTime = function() {
  return GD_EventEntry.BINDERS.start.getValue(this.xmlNode_);
};

GD_EventEntry.prototype.setStartTime = function(newValue) {
  GD_EventEntry.BINDERS.start.setValue(this.xmlNode_, newValue);
  this.setDirty();
};

/**
 * property get/set to gd:when.endTime
 */
GD_EventEntry.prototype.getEndTime = function() {
  return GD_EventEntry.BINDERS.end.getValue(this.xmlNode_);
};
GD_EventEntry.prototype.setEndTime = function(newValue) {
  GD_EventEntry.BINDERS.end.setValue(this.xmlNode_, newValue);
  this.setDirty();
};


/**
 * parses the event status string, and returns an EventStatus code
 */
function GD_ParseEventStatus_(value) {
  var m = "";
  if (value) {
    m = value.match(/http:\/\/schemas.google.com\/g\/2005#event\.(.*)/);
  }
  switch (m[1]) {
    case 'canceled' : return GD_EventEntry.EventStatus.CANCELED;
    case 'confirmed' : return GD_EventEntry.EventStatus.CONFIRMED;
    case 'tentative' : return GD_EventEntry.EventStatus.TENTATIVE;
    default: return GD_EventEntry.EventStatus.UNKNOWN;
  }
}

/**
 * @return {GD_EventEntry.EventStatus}
 */
GD_EventEntry.prototype.getEventStatus = function() {
  var value = GD_EventEntry.BINDERS.eventStatus.getValue(this.xmlNode_);
  return GD_ParseEventStatus_(value);
}

/**
 * @return {string} URL for the event's event page
 */
GD_EventEntry.prototype.getEventPageUrl = function() {
  var url = this.getAlternateUri();
  if (url) return url;
  // this is probably safari -- works fine on browsers with
  // proper xpath support
  var children = this.xmlNode_.childNodes;
  for (var i = 0; i < children.length; ++i) {
    var child = children[i];
    if (child.nodeName == 'link' && child.getAttribute('rel') == 'alternate') {
      return child.getAttribute('href');
    }
  }
  return null;
}


// remove this if you merge
if (window.GD_Loader) {
  // continue loading
  window.GD_Loader();
}
// end


