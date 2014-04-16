Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")
Grs.load("<siat/const.js>")

Siat.ReporteGrsTest = new function () {

var Page = Grs.page();
var Sql = Grs.sql(Siat.DsName);
var Adp = Grs.adp();
var Const = Siat.Const;

var SqlRecurso = "select id, desRecurso label from def_recurso order by desRecurso"; 
var ListDia = [{"id":1, "label":"Lunes"}, {"id":2, "label":"Martes"}, {"id":2, "label":"MIERCOLES"}];
var ListColor = [{"id":"ROJO", "label":"Rojo"}, {"id":"AZUL", "label":"Azul"}, {"id":"VERDE", "label":"Verde"}];


this.get = function () {
	Page.write("Hola get()");
	var params = Page.parameters();
	render(params);
}

this.accept = function () {
	Page.write("Hola accept()");
	var params = Page.parameters();
	Page.write(show_props(params));
	Adp.scheduleReport("ReporteConvenioCaduco", "Iniciando Reporte", params);
}

this.fede = function () {
	Page.write("Hola fede()");
}


var show_props = function (obj) {  
   var result = "";
   for (var i in obj) {  
      result += "" + i + " = " + obj[i] + "\n";  
   }  
   return result;  
}

var render = function (params) {

	Page.fieldset({"label":"Parametros Reporte Convenios Caducos"});

	Page.label({"label":"Recurso:"});
	Page.input({"name" : "idRecurso",
				"type": "select",
				"checked" : params.idRecurso,
				"colspan" : "3",
				"options": Sql.list(SqlRecurso)
				});

	Page.label({"label":"Día"});
	Page.input({"name":"idDia",
				"type": "select",
				"checked":params.idDia,
				"onChange":"alert('Ud. cambio de Dia');",
				"options": ListDia
				});

	Page.label({"label":"Colores"});
	Page.input({"name":"color",
				"type": "select",
				"checked":params.color,
				"onChange":"alert('Ud. cambio de Color');",
				"options": ListColor
				});



	Page.label({"label":"(*) Fecha ingreso:"});
	Page.input({"name": "fecIngreso",
				"type": "date",
				"option" : params.fecIngreso
				});

	Page.endfieldset();
}

}//end ReporteAbc

Grs.request(Siat.ReporteGrsTest);
