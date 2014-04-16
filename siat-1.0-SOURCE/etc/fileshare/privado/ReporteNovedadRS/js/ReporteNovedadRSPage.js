// Reporte de Novedades de RS
// Mantis 0007883: Prod. I - GRS -> AFIP - Reporte de Novedades de RS
// Fecha: 27/04/2011
//===================================================================
// Criterios de búsqueda:
//----------------------
//	- categoríaRS: [1, 2, 3, 4]
//	- codactividad
//	- rango de ingresos brutos anuales, superficie afectada, cant personal
//	- rango de fechas de adhesion y de fechas de baja

Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")
Grs.load("<siat/const.js>")

Siat.ReporteNovedadRS = new function () {

var Page = Grs.page();
var Sql = Grs.sql(Siat.DsName);
var Adp = Grs.adp();
var Const = Siat.Const;

// categorias de Regimen Simplificado
var ListCategoriaRS = [{"id":"", "label":"Todas"},{"id":1, "label":"1"}, {"id":2, "label":"2"}, {"id":3, "label":"3"},{"id":4, "label":"4"}];

// actividades que declara DReI (idrecurso = 15)
var SqlActividad = "SELECT id, codconcepto||' - '||SUBSTRING(desconcepto FROM 1 FOR 60) label " +
				   " FROM def_recconadec WHERE idrecurso = 15 order by 2"; 

this.get = function () {
	var params = Page.parameters();
	render(params);
}

this.accept = function () {
	var params = Page.parameters();
	
	//validaciones
	Adp.scheduleReport("ReporteNovedadRS", "Iniciando Reporte", params);
	render(params);
}

var render = function (params) {

	Page.write("<p>Reporte de Novedades de Regimen Simplificado.</p>");
	
	Page.fieldset({"label":"Filtros de B&uacute;squeda"});

	Page.label({"label":"Actividad a Declarar:"});
	Page.input({"name" : "idActividad",
				"type": "select",
				"selected" : params.idActividad,
				"colspan" : "3",
				"options": Sql.list(SqlActividad)
				});

	Page.label({"label":"Fecha Adhesi&oacute;n Desde:"});
	Page.input({"name" : "fechaAdhesionDesde",
				"type": "date",
				"value": params.fechaAdhesionDesde
				});
	
	Page.label({"label":"Fecha Adhesi&oacute;n Hasta:"});
	Page.input({"name" : "fechaAdhesionHasta",
				"type": "date",
				"value": params.fechaAdhesionHasta
				});
	
	Page.label({"label":"Fecha Baja Desde:"});
	Page.input({"name" : "fechaBajaDesde",
				"type": "date",
				"value": params.fechaBajaDesde
				});
	
	Page.label({"label":"Fecha Baja Hasta:"});
	Page.input({"name" : "fechaBajaHasta",
				"type": "date",
				"value": params.fechaBajaHasta
				});

	Page.label({"label":"IIBB Anuales Desde:"});
	Page.input({"name" : "ingBruAnuDesde",
				"type": "text",
				"value": params.ingBruAnuDesde
				});
	
	Page.label({"label":"IIBB Anuales Hasta:"});
	Page.input({"name" : "ingBruAnuHasta",
				"type": "text",
				"value": params.ingBruAnuHasta
				});
	
	Page.label({"label":"Sup. afectada Desde:"});
	Page.input({"name" : "supAfeDesde",
				"type": "text",
				"value": params.supAfeDesde
				});
	
	Page.label({"label":"Sup. afectada Hasta:"});
	Page.input({"name" : "supAfeHasta",
				"type": "text",
				"value": params.supAfeHasta
				});
	
	Page.label({"label":"Cant. Personal Desde:"});
	Page.input({"name" : "canPerDesde",
				"type": "text",
				"value": params.canPerDesde
				});
	
	Page.label({"label":"Cant. Personal Hasta:"});
	Page.input({"name" : "canPerHasta",
				"type": "text",
				"value": params.canPerHasta
				});
	
	Page.label({"label":"Categor&iacute;a"});
	Page.input({"name":"categoria",
				"type": "select",
				"value": params.categoria,
				"options": ListCategoriaRS
				});

	Page.endfieldset();
}

}//end ReporteNovedadRS

Grs.request(Siat.ReporteNovedadRS);
