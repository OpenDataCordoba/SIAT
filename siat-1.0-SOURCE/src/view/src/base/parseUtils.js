function li_trim(s) {
  if (typeof(s) == "string") {
    return s.replace(new RegExp("^\\s*(.*\\S+)?\\s*$"), "$1");
  } else return s;
}

function li_parse_int(s) {
  var objRegExp  = new RegExp("(^-?\\d\\d*$)");
  if ( objRegExp.test(s) ) {
    return parseInt(s,10);
  } else
    return null;
}

function li_parse_date(sdate) {
  var d,m,y,dd;
  var re = new RegExp("^([0-9]{1,2})\/([0-9]{1,2})\/([0-9]{1,4})$", "ig");
  var a=re.exec(sdate);
  if (!a) return null;
  d = parseInt(a[1],10);
  m = parseInt(a[2],10);
  y = parseInt(a[3],10);

  if (!d || !m || !y || y<1900) {
    return null;
  }
  dd = new Date(y,m-1,d);
  if ((dd.getDate() != d) || (dd.getMonth() != m-1) || (dd.getFullYear() != y))
    return null;

  return dd;
}

/* 
* String to float
* convierte un string en un float 
* la coma y el punto ambos se interpretan
* como separadores de decimal.
*/
function li_parse_float(s) {
  if (s==null) return null;
  var f = parseFloat(li_ffs(s));
  if (isNaN(f))
    f = null;
  return f;
}

/* format float string */
function li_ffs(s) { 
  var re=new RegExp(",+","ig");
  return s.replace(re, ".");
} 

/*
* Elimina los espacios al comienzo 
* de la cadena
*/
function ltrim(s) {
  var re = /\s*((\S+\s*)*)/;
  return s.replace(re, "$1");
}

/*
* Elimina los espacios al final 
* de la cadena
*/
function rtrim(s) {
  var re = /((\s*\S+)*)\s*/;
  return s.replace(re, "$1");
}

/* 
* Elimina los espacios al comienzo 
* y al final de la cadena
*/
function trim(s) {
  return ltrim(rtrim(s));
}