//OUT
//test out simple table generation
function test0() {
	var out = grs.outHtml();	
	var table = out.table();

	table.table("Reporte de Cuentas");
	  table.thead(); 
	    table.tr();
	      table.th("Numero Cuenta");
	      table.th("Titular");
	     table.endtr();  
           table.endthead();
	   table.tbody();
	     table.tr();
	       table.td("123");
	       table.td("Leonardo Fagnano");
             table.endtr();  
	   table.endtbody();
	 table.endtable();
	grs.printf("%s\n", out.out());
	return "OK";
}

//test type conversion with rhino
function test1() {

	var fmt = {"props": {"numerocuenta": "r%s", 
			     "nomtitpri": "c%s", 
			     "desdomenv": "c%s", 
			     "totdeuda": "r%.5f"},
		"types": {"Decimal": "r%s", 
						  "String": "r%s", 
						  "Integer": "r%.5f"}
		  	  };
	var out = grs.out();	
	var table = out.table();

	table.fmt(fmt);
	table.table("Reporte de Cuentas");
  	   table.thead(["l:Nro. CTA", "r:Titular","Total"]); 
	   table.tbody();
	     table.tr();
	       table.td("123");
	       table.td("Leonardo Fagnano");
	       table.td(53,"l%.2f");
         table.endtr();  
	   table.endtbody();
	 table.endtable();
	grs.printf("%s\n", out.out());
	return "OK";
}


//test type conversion with rhino
function test2() {

	var fmt = {"props": {numerocuenta: "r%.8d", 
					     nomtitpri: "c%s", 
					     desdomenv: "c%s", 
					     "totdeuda": "r%.2f"},
				"types": {Decimal: "r%.2f", 
						  String: "r%s", 
						  Integer: "r%d"}
		  	  };
	var order = ["numerocuenta", "nomtitpri", "desdomenv", "totdeuda"];
	
	var row = {totdeuda:89, nomtitpri: "Alberto", numerocuenta:999, desdomenv:'Sarmiento 123'};
	
	var out = grs.out();	
	var table = out.table();

	table.fmt(fmt);
	table.order(order);
	table.table("Reporte de Cuentas");
  	   table.thead(["l:Nro. CTA", "r:Titular","c:Domicilio","Total"]); 
	   table.tbody();
	   		table.brow(row);
	   table.endtbody();
	 table.endtable();
	grs.printf("%s\n", out.out());
	return "OK";
}


//test type conversion with rhino
function test3() {

	var fmt = {props: {numerocuenta: 'r%2f', 
					   nomtitpri: 'c%s', 
					   desdomenv: 'c%s', 
					   totdeuda: 'r%8f'},
				types: {Decimal: 'r%.2f', 
						String : 'l%s', 
						Integer: 'r%d'}
		  	  };
	var order = ["numerocuenta", "nomtitpri", "desdomenv", "totdeuda"];
	
	var row = {totdeuda:89, nomtitpri: "Alberto", numerocuenta:999, desdomenv:'Sarmiento 123'};
	
	var out = grs.out();	
	var fieldset = out.fieldset();


	fieldset.fieldset("Filtros de Busqueda");
	fieldset.field("Filtro1:","leonardo");
	fieldset.field("Filtro2:","fagnano");
	fieldset.field("Filtro4:","tecso");
	fieldset.field("2:Filtro5:","descripción muy larga que necesita ocupar mas espacio");
	fieldset.field(":Filtro6:","DReI");
	fieldset.endfieldset();
	
	var table = out.table();
	
	table.fmt(fmt);
	table.order(order);
	table.table("Reporte de Cuentas");
  	   table.thead(["l:Nro. CTA", "r:Titular","c:Domicilio","Total"]); 
	   table.tbody();
	   		table.row(row);
	   table.endtbody();
	 table.endtable();
	grs.printf("%s\n", out.out());
	return "OK";
}

function mktest(name, fnt) {
	var result = fnt();
	grs.printf("mktest:%s:%s\n", name, result);
}

function tests() {
	grs.printf("%s\n", "Begin Grs Sql test");
	mktest("test0", test0);
	mktest("test1", test1);
	mktest("test3", test3);
}

//doit
tests();
