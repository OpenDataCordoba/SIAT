// Reporte de Retenciones Declaradas
// Mantis 0007884: Prod. I - GRS -> AFIP - Reporte de Retenciones Declaradas
// Fecha: 29/04/2011
//===================================================================
// Criterios de búsqueda:
//----------------------
//- rangos de importes, fechas de presentacion, anio/periodo

Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")
Grs.load("<siat/const.js>")

Siat.ReporteRetencionesDeclaradas = new function () {

var Page = Grs.page();
var Sql = Grs.sql(Siat.DsName);
var Adp = Grs.adp();
var Const = Siat.Const;

this.get = function () {
	var params = Page.parameters();
	render(params);
}

this.accept = function () {
	var params = Page.parameters();
	
	//validaciones
	Adp.scheduleReport("ReporteRetencionesDeclaradas", "Iniciando Reporte", params);
	render(params);
}

var render = function (params) {

	Page.write("<p>Reporte de Retenciones Declaradas.</p>");
	
	Page.fieldset({"label":"Filtros de B&uacute;squeda"});

	//fecha presentacion desde
	Page.label({"label":"Fecha Pres. Desde:"});
	Page.input({"name" : "fechaPresDesde",
				"type": "date",
				"value": params.fechaPresDesde
				});
	//fecha presentacion hasta
	Page.label({"label":"Fecha Pres. Hasta:"});
	Page.input({"name" : "fechaPresHasta",
				"type": "date",
				"value": params.fechaPresHasta
				});
	
	//importe desde
	Page.label({"label":"Importe Desde:"});
	Page.input({"name" : "importeDesde",
				"type": "text",
				"value": params.importeDesde
				});
	//importe hasta
	Page.label({"label":"Importe Hasta:"});
	Page.input({"name" : "importeHasta",
				"type": "text",
				"value": params.importeHasta
				});
	
	//anio/periodo desde
	Page.label({"label":"A&ntilde;o/Per Desde: (aaaa/mm)"});
	Page.input({"name" : "anioPerDesde",
				"type": "text",
				"value": params.anioPerDesde
				});
	
	//anio/periodo hasta
	Page.label({"label":"A&ntilde;o/Per Hasta: (aaaa/mm)"});
	Page.input({"name" : "anioPerHasta",
				"type": "text",
				"value": params.anioPerHasta
				});
	
	Page.endfieldset();
}

}//end ReporteRetencionesDeclaradas

Grs.request(Siat.ReporteRetencionesDeclaradas);
