var Page = Grs.page();

var Page.prepare = new function() {
	
	Page.inputStartPage("Formulario de Prueba");

	Page.inputStartFieldSet(
			{"id":"fs1",
				"style":"{color:black;}",
				"name":"fs1",
				"legend":"Parametros"
			});

	Page.inputLabel(
			{
				"id":"Label1",
				"style":"{color:red;}",
				"name":"Label1",
				"for":"inputSelect1",
				"option":"Recursos:"
			});

	Page.input(
			{
				"id":"inputSelect1",
				"style":"{color:black;}",
				"name":"inputSelect1",
				"disabled":"false",
				"checked":"Recurso1",
				"multiple":"false",
				"colspan":"3",
				"onChange":"alert('Ud. cambio de Recurso');",
				"options":[
				           {"value":"recurso1",
				        	   "label":"Recurso 1"},
				        	   {"value":"recurso2",
				        		   "label":"Recurso 2"},
				        		   {"value":"recurso3",
				        			   "label":"Recurso 3"}
				        		   ]
			});

	Page.inputLabel(
			{
				"id":"Label2",
				"style":"{color:red;}",
				"name":"Label2",
				"for":"inputSelect2",
				"option":"Planes:"
			});

	Page.input(
			{
				"id":"inputSelect2",
				"style":"{color:black;}",
				"name":"inputSelect2",
				"disabled":"false",
				"checked":"plan1",
				"multiple":"false",
				"type": "select",
				"onChange":"alert('Ud. cambio de Plan');",
				"options":[
				           {"value":"plan1",
				        	   "label":"Plan 1"},
				        	   {"value":"plan2",
				        		   "label":"Plan 2"},
				        		   {"value":"plan3",
				        			   "label":"Plan 3"}
				        		   ]
			});

	Page.inputLabel(
			{
				"id":"Label3",
				"style":"{color:red;}",
				"name":"Label3",
				"for":"inputSelect3",
				"option":"Procuardores:"
			});

	Page.inputSelect(
			{
				"id":"inputSelect3",
				"style":"{color:black;}",
				"name":"inputSelect3",
				"disabled":"false",
				"checked":"procurador1",
				"multiple":"false",
				"onChange":"alert('Ud. cambio de Procurador');",
				"options":	[
				          	 {"value":"procurador1",
				          		 "label":"Procurador 1"},
				          		 {"value":"procurador2",
				          			 "label":"Procurador 2"},
				          			 {"value":"procurador3",
				          				 "label":"Procurador 3"}
				          			 ]
			});

	Page.inputLabel(
			{
				"id":"inputFecha",
				"name":"Label4",
				"for":"inputFecha",
				"option":"(*) Fecha ingreso:"
			});

	Page.inputTextFecha(
			{
				"id":"fecha",
				"name":"fecha"
			});

	Page.inputEndFieldSet(
			{
				"id":"fs1"
			});

	Page.inputStartFieldSet(
			{
				"id":"fs2",
				"style":"{color:black;}",
				"name":"fs2",
				"legend":"Prueba Secci&oacute;n Botonera"
			});

	Page.inputStartButtonSection(page);

	Page.inputButton(
			{
				"id":"inputBtn3",
				"style":"{color:blue;}",
				"name":"inputBtn3",
				"disabled":"false",
				"type":"submit",
				"option":"Submit"
			});

	Page.inputEndButtonSection(page);

	Page.inputEndFieldSet(
			{
				"id":"fs2"
			});


	Page.inputStartFieldSet(
			{
				"id":"fs2",
				"style":"{color:black;}",
				"name":"fs2",
				"legend":"Resultados"
			});

	Page.inputEndFieldSet(
			{
				"id":"fs2"
			});
}

Page.prepare();