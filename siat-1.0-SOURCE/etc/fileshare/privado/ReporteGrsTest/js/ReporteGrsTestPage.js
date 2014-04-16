Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")
Grs.load("<siat/const.js>")

Siat.ReporteGrsTest = new function () {

var Page = Grs.page();
var Sql = Grs.sql(Siat.DsName);
var Adp = Grs.adp();
var Const = Siat.Const;

var SqlRecurso = "select id, desRecurso label from def_recurso order by desRecurso"; 

var ListDia = [{"id":1, "label":"Lunes"}, {"id":2, "label":"Martes"}, {"id":2, "label":"Miercoles"}];
var ListColor = [{"id":"ROJO", "label":"Rojo"}, {"id":"AZUL", "label":"Azul"}, {"id":"VERDE", "label":"Verde"}];


this.get = function () {
	var params = Page.parameters();

	if (params.idRecurso == null)
		params.idRecurso = Const.RecursoIdTgi;

	render(params);
}

this.accept = function () {
	var params = Page.parameters();
	
	//validaciones
	if (params.fechaAnalisis == "") {
		Grs.printf("params.fechaAnalisis: '%s'\n", params.fechaAnalisis);
		Page.message("Fecha Analisis es requerido.");
	}
	
	// si no hay advertencias scheduleamos el reporte
	if (Page.messages().length == 0) {
		Adp.scheduleReport("ReporteGrsTest", "Iniciando Reporte", params);
	}
	
	render(params);
}

var render = function (params) {

	Page.title("Reporte Demo para Grs");
	Page.p("Reporte ejemplo para la construcción de otros reportes.");

	Page.fieldset({"label":"Parámetros"});

	Page.label({"label":"Recurso:"});
	Page.input({"name" : "idRecurso",
				"type": "select",
				"selected" : params.idRecurso,
				"colspan" : "3",
				"options": Sql.list(SqlRecurso)
				});

	Page.label({"label":"Fecha Vigentes:"});
	Page.input({"name" : "fechaAnalisis",
				"type": "date",
				"value": params.fechaAnalisis,
				"colspan" : "3",
				});

    // otros de ejemplo
	Page.label({"label":"Día"});
	Page.input({"name":"idDia",
				"type": "select",
				"value": params.idDia,
				"onChange":"alert('Ud. cambio de Dia');",
				"options": ListDia
				});

	Page.label({"label":"Colores"});
	Page.input({"name":"color",
				"type": "select",
				"value": params.color,
				"onChange":"alert('Ud. cambio de Color');",
				"options": ListColor
				});

	Page.endfieldset();
}

}//end ReporteAbc

Grs.request(Siat.ReporteGrsTest);
