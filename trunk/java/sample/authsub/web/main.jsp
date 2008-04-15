<html>
<head>
  <title>Google Calendar AuthSub demo</title>

    <!--
    This is the page that uses the AuthSub token retrieved from Google for the
    respective user.  The web service will redirect to this page once an
    AuthSub token has been successfully retrieved.

    This page makes the following assumptions:
      - User has been assigned an login cookie by the web service
      - The web service has retrieved from Google a valid AuthSub token for the
        respective user

    This page displays a calendar widget.  The widget is populated with
    information retrieved from Google Calendar using the user's private default
    feed.

    Note: The URL is secured on the server using the token generated in the
          formProxyFeedUrl() function.
    -->


  <link rel="stylesheet" type="text/css" href="css/google.css">
  <link rel="stylesheet" type="text/css" href="css/frameless.css">
  <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="css/ieonly.css" />
  <![endif]-->

  <link href="css/datepicker.css" type="text/css" rel="stylesheet">
  <link href="css/pbox.css" type="text/css" rel="stylesheet">


  <script language="JavaScript" type="text/javascript"
    src="javascript/util.js" ></script>

  <script language="JavaScript" type="text/javascript"
    src="javascript/gdata_condensed.js" ></script>

  <script language="JavaScript" type="text/javascript">

    var dateData = {};

    var firstDayMap = {
      Sunday : 0,
      Monday : 1,
      Saturday : 6
    };


    // The following function generates the URL for the proxy.  A secure token
    // is first generated using JSP.  The token is appended as a query
    // parameter to the RetrieveFeed request
    function formProxyFeedUrl() {
      <%
      String feedUrl = (String)(request.getSession(false)).getAttribute("feedRootUrl")
          + "default/private/full";

      // Secure the URL by generating a secure token before sending the request
      // to the proxy server
      long currentTimeSeconds = ((new java.util.Date()).getTime()) / 1000;
      String cookie = sample.authsub.src.Utility.getCookieValueWithName(
        request.getCookies(), sample.authsub.src.Utility.LOGIN_COOKIE_NAME);

      String token = sample.authsub.src.SecureUrl.generateToken(cookie,
                                                                feedUrl,
                                                                "GET",
                                                                 currentTimeSeconds);
      // Pass values to javascript
      feedUrl = "\"" + feedUrl + "\"";
      token = "\"" + token + "\"";
      %>

      return "/authsub_sample/RetrieveFeedServlet?href=\"" +  <%=feedUrl%> 
        + "\"&timestamp=\"" + <%=currentTimeSeconds%> + "\"&token=\"" +
          <%=token%> +"\"";
    }

    // @param date {ICAL_Date}
    // @return {GD_Entry[]} entries for date, sorted
    function getEntriesFor(date) {
      var data = dateData[date];
      var entries = [];
      for (var e in data) entries.push(data[e]);
      entries.sort(compareEntry);
      return entries;
    }

    // a before b <=> -1
    // @param a {GD_Entry}
    // @param b {GD_Entry}
    // @return {number} satisfying "Comparable" contract for GD_Entry
    function compareEntry(a, b) {
      var cmp = IsAllDay(b) - IsAllDay(a);
      if (cmp) return cmp;
      cmp = _ICAL_GetComparable(parseTemporal(a.getStartTime())) -
            _ICAL_GetComparable(parseTemporal(b.getStartTime()));
      if (cmp) return cmp;
      var at = a.getTitle(), bt = b.getTitle();
      if (at < bt) return -1;
      return (at == bt) ? 0 : 1;
    }

    function AgendaPopup() {
      this.div_ = _forid('agendaDiv');
      document.body.appendChild(this.div_);
      this.isVisible_ = false;
    }

    // @param date {ICAL_Date}
    // @param cell {Element}
    AgendaPopup.prototype.show = function(date, cell) {
      _DatePickerSetSelection(date);
    }

    function _OpenUrl(url) {
      open(url, "_BLANK");
    }

    var HOVER_LINK_JS = ' onmouseout="this.style.textDecoration=\'none\'" ' +
        'onmouseover="this.style.textDecoration=\'underline\'" ';

    // @param date {ICAL_Date}
    // @param entry {GD_Entry}
    // @param html {string[]}
    function AddEventHtml(date, entry, html) {
      html.push("<TR>");
      var title = entry.getTitle();
      if (IsAllDay(entry)) {
        html.push('<TD colspan="2" ',
            'style="background-color:#668CD9; color:white; width:100%">');
      } else {
        html.push('<TD style="text-align: right;" class="eventChip">');
        var titlePrefix = "";
        var startDateTime = parseDateTime(entry.getStartTime());
        if (_ICAL_ToDate(startDateTime).equals(date)) {
          // event starts today! show start time
          titlePrefix = HumanTime(startDateTime) + "&nbsp;";
        } else {
        }
        html.push(titlePrefix, '</TD><TD style="width:100%">');
      }
      var onclick = "\"_OpenUrl(" + _ToJSString(entry.getEventPageUrl()) + ")\"";
      html.push('<SPAN onclick=', onclick, HOVER_LINK_JS,
          'style="cursor:pointer" class="eventChip">', title, '</SPAN>');
      html.push("</TD></TR>");
    }

    var gAgendaPopup;

    function _GetAgendaPopup() {
      if (!gAgendaPopup) gAgendaPopup = new AgendaPopup();
      return gAgendaPopup;
    }

    // @param cellId {string}
    function popupAgenda(cellId) {
      var cell = _forid(cellId);
      var dt = _DatePickerGetDateForCell(cell);
      _GetAgendaPopup().show(dt, cell);
    }

    // authentication token for reading XAPI feeds
    var token;

    // @param start {ICAL_Date}
    // @param end {ICAL_Date}
    function calendarInlineDecorator(start, end) {
      var decs = {};
      var builder = _ical_builderCopy(start);

      for (var dt = _ICAL_ToDate(builder);
           _ICAL_GetComparable(dt) <= _ICAL_GetComparable(end);
           builder.date += 1, dt = _ICAL_ToDate(builder)) {
        var str = dt.toString();
        if (str in dateData) decs[str] = 'font-weight:bold';
      }
      return decs;
    }

    // @param date {ICAL_Date}
    // @return {string} XAPI Date
    function formatDate(date) {
      var m = date.month;
      if (m < 10) m = "0" + m;
      var d = date.date;
      if (d < 10) d = "0" + d;
      return date.year + "-" + m + "-" + d;
    }

    // @param date {ICAL_Date}
    // @return mm dd[, yyyy]
    function HumanDate(date) {
      var humanDate = _DatePickerGetMonths()[date.month] + " " + date.date;
      if (_ICAL_todaysDate.year !== date.year) {
        humanDate += ", " + date.year;
      }
      return humanDate;
    }

    // @param time {ICAL_DateTime}
    // @return hh:mm{a,p}m
    function HumanTime(time) {
      var h = time.hour;
      var m = time.minute;
      if (m < 10) m = "0" + m;
      var a = (h < 12) ? "am" : "pm";
      if (h > 12) {
        h -= 12;
      } else if (h === 0) {
        h = 12;
      }
      return h + ":" + m + a;
    }

    // @param yyyy_mm_dd_date {string}
    function parseDate(yyyy_mm_dd_date) {
      var parts = yyyy_mm_dd_date.substring(0, 10).split('-');
      for (var i = 0; i < 3; ++i) {
        parts[i] = parseInt(parts[i], 10);
      }
      return _ICAL_Date_create(parts[0], parts[1], parts[2]);
    }

    function parseDateTime(str) {
      var m = str.match(/(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})/);
      for(var i = 1; i < 7; ++i) {
        m[i] = parseInt(m[i], 10);
      }
      return new _ICAL_DateTime(m[1], m[2], m[3], m[4], m[5], m[6]);
    }

    function parseTemporal(str) {
      if (str.length === 10) return parseDate(str);
      return parseDateTime(str);
    }

    // @param month {ICAL_Date}
    function loadEventsForMonth(month) {
      loadEventsForRange(_DatePickerGetFirstDate(), _DatePickerGetLastDate());
    }

    // @param start {ICAL_Date}
    // @param end {ICAL_Date}
    function loadEventsForRange(start, end) {
      loadFeed(formatDate(start), formatDate(end));
    }

    function IsAllDay(entry) {
      if ('allday' in entry) return entry.allday;
      var allday = (entry.getStartTime().length == 10);
      return (entry.allday = allday);
    }

    // @param start {string}
    // @param end {string}
    function loadFeed(start, end) {
      var feedUrl = formProxyFeedUrl();
      new GD_EventFeed(feedUrl, createLoadFeedCallback());
    }

    function createLoadFeedCallback() {
      return function(feed) {
        var entries = feed.getEntries();
        for (var i = 0; i < entries.length; ++i) {
          var entry = entries[i];
          var status = entry.getEventStatus();
          if (status == GD_EventEntry.CANCELED) continue;

          var startTime = entry.getStartTime();
          var endTime = entry.getEndTime();
          var startDate = parseDate(startTime);
          var endDate = parseDate(endTime);
          var endDateCmp;
          var builder;
          if (IsAllDay(entry)) {
            endDateCmp = _ICAL_GetComparable(endDate);
          } else {
            builder = _ical_builderCopy(endDate);
            builder.date += 1;
            endDateCmp = _ICAL_GetComparable(_ICAL_ToDate(builder));
          }
          builder = _ical_builderCopy(startDate);
          for (var dt = _ICAL_ToDate(builder);
               _ICAL_GetComparable(dt) < endDateCmp;
               builder.date += 1, dt = _ICAL_ToDate(builder)) {
            var events;
            if (dt in dateData) {
              events = dateData[dt];
            } else {
              events = {};
              dateData[dt] = events;
            }
            events[entry.getId()] = entry;
          }
        }
        _DatePickerPopulateHtml();
      }
    }

    function firstLoad() {
      if (_DatePickerIsVisible()) {
        loadEventsForMonth(_DatePickerGetDisplayedMonth());
      } else {
        var today = _ICAL_todaysDate;
        var builder = _ical_builderCopy(_ICAL_todaysDate);
        var start, end;
        builder.date -= 14;
        start = _ICAL_ToDate(builder);
        builder.date += 28;
        end = _ICAL_ToDate(builder);
        // load 2 weeks before and after the current date
        loadEventsForRange(start, end);
      }
      _nav(0);
    }

    /**
     * Navigate the current selection on the datepicker.
     * @param period {int} must be -1, 0, or 1;
     *   corresponds to previous day, today, next day
     */
    function _nav(period) {
      if (period === 0) {
        // go to today
        _DatePickerSetSelection(_ICAL_todaysDate);
      } else {
        var selectedDate = _DatePickerGetSelection();
        var builder = _ical_builderCopy(selectedDate);
        builder.date += period;
        _DatePickerSetSelection(_ICAL_ToDate(builder));
      }
    }

    function handleDatePickerSelection(event) {
      populateAgenda(event.startDate);
    }

    var gAgendaInitialized = false;

    /**
     * Initializes the agenda view.
     */
    function initAgenda() {
      if (!gAgendaInitialized) {
        _forid('prevB').src = "images/btn_prev.gif";
        _forid('nextB').src = "images/btn_next.gif";
        _forid('agenda').style.display = ''; // block
        gAgendaInitialized = true;
      }
      return true;
    }

    function populateAgenda(opt_date) {
      if (!initAgenda()) return;
      var date = opt_date || _ICAL_todaysDate;
      var entries = getEntriesFor(date);
      var html = [];
      if (entries.length) {
        for (var i = 0; i < entries.length; ++i) {
          var entry = entries[i];
          AddEventHtml(date, entry, html);
        }
      } else {
        html.push("<TR><TD><I>Nothing to do today!</I></TD></TR>");
      }
      _forid('selectedDate').innerHTML = HumanDate(date);
      _forid('agendaTable').innerHTML = html.join("");
    }

    function MyOnLoad() {
      _InitializeDatePicker('picker', calendarInlineDecorator, popupAgenda,
                            firstDayMap, handleDatePickerSelection);
      firstLoad();
    }
</script>
</head>

<body onload="MyOnLoad()">

<div class="leftcontent">
  <p class="topimage">
  <a href="http://www.google.com/">
  <img src="http://www.google.com/images/google_sm.gif" border="0"
       alt="Return to Google homepage" /></a></p>
</div>

<div class="rightcontent">
  <p class=title>Google AuthSub Demo </p>
<div class="content">
<h1>Google AuthSub Demo</h1>

<p>This page demonstrates a basic version of AuthSub in action.</p>

<p>The AuthSub token is first retrieved from Google and then used
to retrieve your Calendar feed.
</p>


<center>
    <table width="65%" border="3">
      <tr>
        <td height="100%" valign="top">

<center>
    <div  id="pickerContainer" align="center" style="display:none; width: 13em">
    <div  id="picker"></div>
    </div>
</center>

        </td>
      </tr>

      <tr>
        <td height="100%" valign="top">

<center>
<div id="agenda" style="display:none">
<table width="100%" cellspacing="0" cellpadding="0" id="navButtons"><tr>
<td><div align="center" valign="middle">
  <img  id="prevB" width="33px" height="17px" onmousedown="_nav(-1)"></div></td>
<td><div align="center" valign="middle">
  <img id="nextB" width="33px" height="17px" onmousedown="_nav(1)"></div></td>
<td><div align="center" valign="middle">
  <button  id="todayB" onmousedown="_nav(0)">Today</button></div></td>
<td id="selectedDate" style="padding-left: 1em; font-weight: bold;"></td>
</tr></table>
<table width="100%" id="agendaTable" cellspacing="1"></table>
</div>
</center>

        </td>
      </tr>
    </table>

</center>

<div id="agendaDiv"
     style="display:none; position:absolute; width: 20em; background-color: #666666;
     padding: 3px 0px 2px 0px; font-size: 83%">
 <div style="border: 1px solid gray; background-color:white; width: 100%; margin: -6px 0 0 -4px;">
 </div>
</div>


</div>
</div>

<div class="leftcontent">
  <p class="footerimage"><img src="http://www.google.com/images/art.gif"></p>
</div>
<div class="rightcontent">
  <p><img src="http://www.google.com/images/cleardot.gif" width="1" height="45"></p>
</div>

</body>
</html>
