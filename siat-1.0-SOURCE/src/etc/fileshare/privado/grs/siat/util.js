/*
Modulo de funcionales utiles de siat
 */

Siat.Util = new function () {
	// makeSeparatedForString
	// Devuelve una cadena con lo que sea separados por comas entre desde y
	// hasta
	//
	this.makeSeparatedForString = function (desde, hasta){
		t = [];
		for(var i = desde; i<=hasta; i++){
			t.push(i);
		}
		return t.join(",");
	}

	// quote, pone entre comillas simples al parametro 'v'.
	// Si v es un array, retorna una nuevo array con cada elemento entre comillas. 
	this.quote = function (v){
		if (v instanceof Array){
			t = [];
			for(var i=0;i < v.length; i++){
				t.push("'" + v[i] + "'");
			}
		} else {
			t = ("'" + v + "'");
		}
		return t;
	}
}
