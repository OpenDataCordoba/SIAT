/*
 +-------------------------------------------------------------------+
 |                J S - C A L C U L A T O R   (v1.2)                 |
 |                                                                   |
 | Copyright Gerd Tentler               www.gerd-tentler.de/tools    |
 | Created: Jun. 17, 2003               Last modified: Mar. 17, 2007 |
 +-------------------------------------------------------------------+
 | This program may be used and hosted free of charge by anyone for  |
 | personal purpose as long as this copyright notice remains intact. |
 |                                                                   |
 | Obtain permission before selling the code for this program or     |
 | hosting this software on a commercial website or redistributing   |
 | this software over the Internet or in any other medium. In all    |
 | cases copyright must remain intact.                               |
 +-------------------------------------------------------------------+

======================================================================================================

 This script was tested with the following systems and browsers:

 - Windows XP: IE 6, NN 7, Opera 7 + 9, Firefox 2
 - Mac OS X:   IE 5, Safari 1

 If you use another browser or system, this script may not work for you - sorry.

======================================================================================================
*/
//--------------------------------------------------------------------------------------------------------
// Configuration
//--------------------------------------------------------------------------------------------------------
var calcBGColor = "#ABCDEF";             // calculator background color
var calcBorder = "2px outset white";     // calculator border (CSS spec: size style color, e.g. "2px outset white")
var calcFontSize = 15;                   // calculator font size (pixels)
var calcMode = "dec";                    // calculator default mode: "dec" (decimal) or "hex" (hexadecimal)
var calcFadeSpeed = 15;                  // calculator fade speed (0 - 30; 0 = no fading)*

var title = "JS-Calculator";             // title bar text
var titleBGColor = "#405BA2";            // title bar background color
var titleColor = "#FFFFFF";              // title bar font color

var displayBGColor = "#E0F0FF";          // display background color
var displayBorder = "2px inset white";   // display border (CSS spec: size style color, e.g. "2px inset white")
var displayColor = "#0000A0";            // display font color

var buttonBGColor = "#D0E0F0";           // button background color
var buttonColor = "#000000";             // button font color

// * Fading was successfully tested only on Windows XP with IE 6, NN 7 and Firefox 1 + 2. It seems that
//   other browsers and systems do not support this feature.

//--------------------------------------------------------------------------------------------------------
// Get browser
//--------------------------------------------------------------------------------------------------------
var DOM = document.getElementById;
var IE4 = document.all;

//--------------------------------------------------------------------------------------------------------
// Calculator buttons
//--------------------------------------------------------------------------------------------------------
var buttons = new Array();
buttons[0] = new Array('AC', 'CI', '&larr;', '',         'Hex');
buttons[1] = new Array('D',  'E',   'F',     '',         'Dec');
buttons[2] = new Array('A',  'B',   'C',     '+/-',      '');
buttons[3] = new Array('7',  '8',   '9',     '&divide;', 'x&sup2;');
buttons[4] = new Array('4',  '5',   '6',     '&times;',  '&radic;');
buttons[5] = new Array('1',  '2',   '3',     '&minus;',  '1/x');
buttons[6] = new Array('0',  '.',   '=',     '+',        '%');

//--------------------------------------------------------------------------------------------------------
// Calculator functions
//--------------------------------------------------------------------------------------------------------
var buffer = formula = lastOp = lastInput = '';
if(!calcMode) calcMode = 'dec';

function makeButtons() {
  var i, j, link;
  var html = '<form name="f1">';

  for(i = 0; i < buttons.length; i++) {
    html += '<tr align="center">';

    for(j = 0; j < buttons[i].length; j++) {
      switch(buttons[i][j]) {
        case 'AC':       link = 'clearAll()'; break;
        case 'CI':       link = 'clearInput()'; break;
        case '&larr;':   link = 'delChar()'; break;
        case 'Hex':      link = 'dec2hex()'; break;
        case 'Dec':      link = 'hex2dec()'; break;
        case '+':        link = "operator('+')"; break;
        case '&minus;':  link = "operator('-')"; break;
        case '&divide;': link = "operator('/')"; break;
        case '&times;':  link = "operator('*')"; break;
        case '%':        link = "operator('%')"; break;
        case '&radic;':  link = "operator('sqrt')"; break;
        case 'x&sup2;':  link = "operator('pow')"; break;
        case '1/x':      link = "operator('1/x')"; break;
        case '+/-':      link = "operator('+/-')"; break;
        case '=':        link = "operator('=')"; break;
        default:         link = "character('" + buttons[i][j] + "')";
      }
      html += '<td>';

      if(buttons[i][j]) {
        html += '<input type="button" id="btn' + i + j + '" value="' + buttons[i][j] +
                '" style="' + cssButton + '" onMouseUp="' + link + '" onFocus="this.blur()">';
      }
      html += '</td>';
    }
    html += '</tr>';
  }
  html += '</form>';

  return html;
}

function disableButtons() {
  var mode = (calcMode == 'hex') ? false : true;
  document.f1.btn10.disabled = mode;
  document.f1.btn11.disabled = mode;
  document.f1.btn12.disabled = mode;
  document.f1.btn20.disabled = mode;
  document.f1.btn21.disabled = mode;
  document.f1.btn22.disabled = mode;
  document.f1.btn61.disabled = !mode;
  document.f1.btn34.disabled = !mode;
  document.f1.btn44.disabled = !mode;
  document.f1.btn54.disabled = !mode;
  document.f1.btn64.disabled = !mode;
}

function print(text) {
  var obj = null;

  if(DOM) obj = document.getElementById('calcDisplay');
  else if(IE4) obj = document.all.calcDisplay;

  if(obj) {
    var op = '';
    if(calcMode == 'hex') text = text.replace('0x', '');

    switch(lastOp) {
      case '+': op = lastOp; break;
      case '-': op = '&minus;'; break;
      case '*': op = '&times;'; break;
      case '/': op = '&divide;'; break;
    }
    obj.innerHTML = '<div style="' + cssDisplayMode + '">' + calcMode.toUpperCase() + '</div>' +
                    '<div style="' + cssDisplayOp + '">' + op + '</div>' + text;
  }
}

function character(c) {
  if(formula == 'calculated') formula = buffer = lastInput = '';
  if(!buffer && calcMode == 'hex') buffer = '0x';

  var charCnt = buffer.length;
  if(calcMode == 'hex') charCnt -= 2;

  if((calcMode == 'hex' && charCnt < 16) || (calcMode == 'dec' && charCnt < 20)) {
    if(c >= '1' && c <= '9') {
      buffer += c;
      print(buffer);
    }
    else if(c == '0') {
      if(buffer && buffer != '0x') {
        buffer += c;
        print(buffer);
      }
    }
    else if(calcMode == 'hex') {
      c = c.toUpperCase();
      if(c >= 'A' && c <= 'F') {
        buffer += c;
        print(buffer);
      }
    }
    else if(c == '.') {
      if(!buffer) buffer = '0.';
      else if(buffer.indexOf('.') == -1) buffer += '.';
    }
    lastInput = buffer;
  }
}

function operator(op) {
  if(formula == 'calculated') formula = '';
  if(calcMode == 'hex' && buffer.length > 20) buffer = buffer.substr(0, 20);
  if(buffer.charAt(buffer.length - 1) == '.') buffer += '0';

  switch(op) {
    case 'sqrt':  if(buffer) {
                    formula = 'Math.sqrt(' + buffer + ')';
                    lastOp = '';
                    calculate();
                  }
                  else clearAll();
                  break;

    case 'pow':   if(buffer) {
                    formula = 'Math.pow(' + buffer + ', 2)';
                    lastOp = '';
                    calculate();
                  }
                  else clearAll();
                  break;

    case '1/x':   if(buffer) {
                    formula = '1/' + buffer;
                    lastOp = '';
                    calculate();
                  }
                  else clearAll();
                  break;

    case '%':     if(formula && buffer) {
                    if(lastOp != '*') {
                      var perc = eval(formula + '*' + buffer + '/100');
                      formula += lastOp + perc;
                    }
                    else formula += lastOp + buffer + '/100';
                    lastOp = '';
                    calculate();
                  }
                  else clearAll();
                  break;

    case '+/-':   if(buffer && buffer != '0' && buffer != '0x0') {
                    if(buffer.charAt(0) == '-') buffer = buffer.substr(1);
                    else buffer = '-' + buffer;
                    print(buffer);
                  }
                  break;

    case '+':
    case '-':
    case '*':
    case '/':     lastOp = op;
                  if(formula) {
                    if(buffer) {
                      formula += op + buffer;
                      calculate();
                      formula = buffer;
                      buffer = '';
                    }
                  }
                  else {
                    if(!buffer) buffer = (calcMode == 'hex') ? '0x0' : '0';
                    formula = buffer;
                    buffer = '';
                  }
                  break;

    case '=':     if(buffer) {
                    if(formula) formula += lastOp + buffer;
                    else if(lastOp && lastInput) {
                      formula = buffer + lastOp + lastInput;
                    }
                    if(formula) calculate();
                  }
                  else clearAll();
                  break;
  }
}

function calculate() {
  if(formula) {
    var error = false;
    var result = '';

    formula = formula.replace(/--([0-9A-Fx]+)/, '-(-$1)');

    try {
      result = eval(formula);

      if(!isFinite(result)) error = true;
      else if(calcMode == 'hex') {
        result = Math.floor(result);
        result = result.toString(16).toUpperCase();
      }
      else {
        var s = result.toString();
        if(s.indexOf('.') != -1 && s.indexOf('e') == -1) {
          result = Math.round(result * 10000000000000) / 10000000000000;
        }
        result = result.toString();
      }
    }
    catch(e) {
      error = true;
    }

    if(error) {
      buffer = '';
      print('ERROR');
    }
    else {
      if(calcMode == 'hex') {
        if(result.charAt(0) == '-') result = '-0x' + result.substr(1);
        else result = '0x' + result;
      }
      print(result);
      buffer = result;
    }
    window.status = formula + '=' + result;
    formula = 'calculated';
  }
}

function dec2hex() {
  if(calcMode != 'hex') {
    calcMode = 'hex';
    disableButtons();

    if(buffer) {
      formula = 'calculated';
      lastInput = lastOp = '';
      window.status = '';
      buffer = parseInt(buffer);

      if(isNaN(buffer)) {
        buffer = '';
        print('ERROR');
      }
      else {
        buffer = buffer.toString(16).toUpperCase();
        buffer = (buffer.charAt(0) == '-') ? '-0x' + buffer.substr(1) : '0x' + buffer;
        print(buffer);
      }
    }
    else clearAll();
  }
  else clearAll();
}

function hex2dec() {
  if(calcMode != 'dec') {
    calcMode = 'dec';
    disableButtons();

    if(buffer) {
      formula = 'calculated';
      lastInput = lastOp = '';
      window.status = '';
      buffer = parseInt(buffer, 16);

      if(isNaN(buffer)) {
        buffer = '';
        print('ERROR');
      }
      else {
        buffer = buffer.toString();
        print(buffer);
      }
    }
    else clearAll();
  }
  else clearAll();
}

function clearInput() {
  buffer = lastInput = '';
  print('0');
  window.status = '';
}

function clearAll() {
  formula = lastOp = '';
  clearInput();
}

function delChar() {
  if(formula != 'calculated') {
    if(buffer) {
      buffer = buffer.substr(0, buffer.length - 1);

      if(!buffer || buffer.search(/^\-?(0x?)?$/) != -1) clearInput();
      else print(buffer);
    }
  }
}

//--------------------------------------------------------------------------------------------------------
// Visual effects functions
//--------------------------------------------------------------------------------------------------------
var timer = opacity = 0;

function setOpacity(op) {
  if(obj) {
    obj.style.filter = 'alpha(opacity=' + op + ')';
    obj.style.mozOpacity = '.1';
    if(obj.filters) obj.filters.alpha.opacity = op;
    if(!IE4 && obj.style.setProperty) obj.style.setProperty('-moz-opacity', op / 100, '');
  }
}

function fadeIn() {
  if(sobj) {
    sobj.visibility = 'visible';
    if(calcFadeSpeed && opacity < 100) {
      opacity += calcFadeSpeed;
      if(opacity > 100) opacity = 100;
      setOpacity(opacity);
      if(timer) clearTimeout(timer);
      timer = setTimeout('fadeIn()', 1);
    }
    else {
      opacity = 100;
      setOpacity(100);
    }
  }
}

function fadeOut() {
  if(sobj) {
    if(calcFadeSpeed && opacity > 0) {
      opacity -= calcFadeSpeed;
      if(opacity < 0) opacity = 0;
      setOpacity(opacity);
      if(timer) clearTimeout(timer);
      timer = setTimeout('fadeOut()', 1);
    }
    else {
      opacity = 0;
      setOpacity(0);
      sobj.visibility = 'hidden';
      clearAll();      
    }
  }
}

var objDestino;

function viewCalc(idObjDestino) {
  if(sobj && sobj.visibility != 'visible') {
	 
	objDestino = document.getElementById(idObjDestino);
	 
    document.onkeydown = getKeyCode;
    sobj.left = mouseX + 'px';
    sobj.top = mouseY + 'px';
    fadeIn();
  }
}

function hideCalc() {
  document.onkeydown = null;
  fadeOut();
  clearAll();
}

//--------------------------------------------------------------------------------------------------------
// General functions
//--------------------------------------------------------------------------------------------------------
function getScrollLeft() {
  var scrLeft = 0;
  if(document.documentElement && document.documentElement.scrollLeft)
    scrLeft = document.documentElement.scrollLeft;
  else if(document.body && document.body.scrollLeft)
    scrLeft = document.body.scrollLeft;
  else if(window.pageXOffset) scrLeft = window.pageXOffset;
  return scrLeft;
}

function getScrollTop() {
  var scrTop = 0;
  if(document.documentElement && document.documentElement.scrollTop)
    scrTop = document.documentElement.scrollTop;
  else if(document.body && document.body.scrollTop)
    scrTop = document.body.scrollTop;
  else if(window.pageYOffset) scrTop = window.pageYOffset;
  return scrTop;
}

//--------------------------------------------------------------------------------------------------------
// Event handlers
//--------------------------------------------------------------------------------------------------------
var mouseX = mouseY = 0;
var dragging = false;

function getKeyCode(e) {
  var k;

  if(e && e.which) k = e.which;
  else if(event && event.keyCode) k = event.keyCode;

  if(k == 13) {
	  operator('=');	  
	  objDestino.value=buffer;
	  fadeOut();
  }else if(k) {
    if(k == 27) clearAll();
    else if(k == 46) clearInput();
    else if(k == 37) delChar();
    else if(k >= 96 && k <= 105) character(k - 96);
    else if(k >= 65 && k <= 70) character(String.fromCharCode(k));
    else if(k == 110 || k == 188 || k == 190) character('.');
    else if(k == 106) operator('*');
    else if(k == 107) operator('+');
    else if(k == 109) operator('-');
    else if(k == 111) operator('/');
    else if(k == 88) hideCalc();   
  }
}

function getMouse(e) {
  var mX = mouseX;
  var mY = mouseY;

  if(e && e.pageX != null) {
    mouseX = e.pageX;
    mouseY = e.pageY;
  }
  else if(event && event.clientX != null) {
    mouseX = event.clientX + getScrollLeft();
    mouseY = event.clientY + getScrollTop();
  }
  if(mouseX < 0) mouseX = 0;
  if(mouseY < 0) mouseY = 0;

  if(dragging && sobj) {
    var x = parseInt(sobj.left + 0);
    var y = parseInt(sobj.top + 0);
    sobj.left = x + (mouseX - mX) + 'px';
    sobj.top = y + (mouseY - mY) + 'px';
  }
}

function startDrag(e) {
  if(!DOM && !IE4) return;
  var firedobj = (e && e.target) ? e.target : event.srcElement;
  var topelement = DOM ? 'HTML' : 'BODY';

  if(DOM && firedobj.nodeType == 3) firedobj = firedobj.parentNode;

  if(firedobj.className == 'titlebar') {
    firedobj.unselectable = true;

    while(firedobj.tagName != topelement && firedobj.className != 'calculator')
      firedobj = DOM ? firedobj.parentNode : firedobj.parentElement;

    if(firedobj.className == 'calculator') {
      sobj = firedobj.style;
      dragging = true;
    }
  }
}

document.onmousemove = getMouse;
document.onmousedown = startDrag;
document.onmouseup = function() { dragging = false; }

//--------------------------------------------------------------------------------------------------------
// Build calculator
//--------------------------------------------------------------------------------------------------------
var calcWidth = calcFontSize * 17;
var obj, sobj;

var cssCalculator = (calcBGColor ? 'background-color: ' + calcBGColor + '; ' : '') +
                    (calcBorder ? 'border: ' + calcBorder + '; ' : '');

var cssTitleBar = 'cursor: default; ' +
                  'font-family: Arial, Helvetica; ' +
                  'font-size: ' + (calcFontSize + 1) + 'px; ' +
                  'font-weight: bold; ' +
                  'padding: 4px; ' +
                  (titleColor ? 'color: ' + titleColor + '; ' : '') +
                  (titleBGColor ? 'background-color: ' + titleBGColor + '; ' : '');

var cssDisplay = 'width: ' + calcWidth + 'px; ' +
                 'height: ' + (calcFontSize + 4) + 'px; ' +
                 'font-family: Arial, Helvetica; ' +
                 'font-size: ' + (calcFontSize + 1) + 'px; ' +
                 'font-weight: bold; ' +
                 'text-align: right; ' +
                 'margin: 2px; ' +
                 'padding: 4px; ' +
                 'overflow: hidden; ' +
                 'white-space: nowrap; ' +
                 (displayColor ? 'color: ' + displayColor + '; ' : '') +
                 (displayBGColor ? 'background-color: ' + displayBGColor + '; ' : '') +
                 (displayBorder ? 'border: ' + displayBorder + '; ' : '');

var cssDisplayMode = 'width: ' + Math.round(calcFontSize * 1.5) + 'px; ' +
                     'font-family: Arial, Helvetica; ' +
                     'font-weight: normal; ' +
                     'font-size: ' + Math.round(calcFontSize / 1.5) + 'px; ' +
                     'white-space: nowrap; ' +
                     'float: right; ' +
                     (displayColor ? ' color: ' + displayColor + '; ' : '');

var cssDisplayOp = 'width: ' + Math.round(calcFontSize / 1.5) + 'px; ' +
                   'font-family: Arial, Helvetica; ' +
                   'font-weight: normal; ' +
                   'font-size: ' + Math.round(calcFontSize / 1.5) + 'px; ' +
                   'text-align: left; ' +
                   'float: left; ' +
                   (displayColor ? ' color: ' + displayColor + '; ' : '');

var cssButton = 'font-family: Arial, Helvetica; ' +
                'font-weight: bold; ' +
                'width: ' + (calcFontSize * 3) + 'px; ' +
                'font-size: ' + (calcFontSize - 1) + 'px; ' +
                (buttonColor ? 'color: ' + buttonColor + '; ' : '') +
                (buttonBGColor ? 'background-color: ' + buttonBGColor + '; ' : '');

var cssButtonClose = 'font-family: Arial, Helvetica; ' +
                     'font-weight: bold; ' +
                     'width: ' + Math.round(calcFontSize * 1.5) + 'px; ' +
                     'font-size: ' + (calcFontSize - 1) + 'px; ' +
                     (buttonColor ? 'color: ' + buttonColor + '; ' : '') +
                     (buttonBGColor ? 'background-color: ' + buttonBGColor + '; ' : '');

document.write('<div id="calculator" class="calculator" style="position:absolute; z-index:69; visibility:hidden">' +
               '<table border="0" cellspacing="0" cellpadding="2" style="' + cssCalculator + '">' +
               '<tr><td colspan="' + (buttons[0].length - 1) + '" class="titlebar" style="' + cssTitleBar + '">' + title +
               '</td><td align="right" class="titlebar" style="' + cssTitleBar + '">' +
               '<input type="button" value="&times;" style="' + cssButtonClose +
               '" onClick="hideCalc()" onFocus="this.blur()"></td></tr>' +
               '<tr><td colspan="' + buttons[0].length + '" align="center">' +
               '<div id="calcDisplay" style="' + cssDisplay + '"></div>' +
               '</td></tr>' + makeButtons() +  '</table></div>');

if(DOM) obj = document.getElementById('calculator');
else if(IE4) obj = document.all.calculator;
if(obj) sobj = obj.style;

disableButtons();
clearAll();

//--------------------------------------------------------------------------------------------------------
