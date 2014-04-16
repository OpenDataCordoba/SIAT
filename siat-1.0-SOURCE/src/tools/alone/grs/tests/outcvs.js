var Test  = {};

Test.tests = function() {
	Grs.printf("%s\n", "Begin Grs CsvOut test");
	Test.test1();
}

//test conection datasources
Test.test1 = function () {
	var out;
	
	out = Grs.outCsv("testcsv1.csv");
	out.fmt();


	Test.mktable1(out);
	out.close();
		
	out = Grs.outHtml("testcsv1.html");
	Test.mktable1(out);
	out.close();
}


Test.mktable1 = function(out) { 
	var row = {"c1":"Text", "c2":1, "c3":1.2, "c4":new Date()};

	out.fieldset("Parametros");
	out.field("Propiedad 1:", "XX1");
	out.field("Propiedad 2:", "XX2");
	out.field("Propiedad 3:", "XX2");
	out.field("Propiedad 4:", "XX4");
	out.endfieldset();

	out.table("Resultado");
	out.thead(["Columna 1", "Columna 2", "Columna 3", "Columna 4"]);
	out.order(["c1","c2","c3","c4"]);
	
	out.row(row);
	out.row(row);
	out.row(row);
	out.row(row);
	out.row(row);
	out.row(row);

	out.endtable();
}

Test.tests();
