Sql
---

Sql permite ejecutar Sql querys a bases de datos.
Posee varias funciones que todas ejecutan el Query,
pero se diferencian en la forma que retornan los
resultados del Query.

También posee funciones para simplificar las 
operaciones de insert, update y delete.

Resumen
---

	var Sql = Grs.sql("java:comp/env/ds/siat")

	Sql.row(sql, param ...)
	Sql.list(sql, param ...)
	Sql.cursor(sql, param ...)
	Sql.exec(sql, param ...)
	Sql.value(sql, param ...)

	Sql.get(table, id)
	Sql.save(table, obj)	
	Sql.delete(table, id)

    Sql.close()

Uso
---
Las funciones 'value()', 'row()', 'cursor()', 'list()', 'exec()'
aceptan en el primer parámetro una cadena con el Query a ejecutar.
La cadena de Query puede contener caracters especiales que son
interpretados como 'Parámetros de Sql'. Los restante parametros 
de las funciones son valores que puede tomar dichos 'Parametros 
del Sql'.

Ademas todos los metodos soportan enviar en el primer parametro
un subcadena a modo de bandera que afecta el funcionamiento de 
la llamada.	


Parametros de Sql
---
Por Ejemplo:
    Sql.exec("select * from persona where id = #i", 14)

Donde '#i' es un Parámetro Sql tomará un valor entero al momento 
de ejecutar el query. En este caso tomará el valor 14. 
  
Se aceptan los siguientes parametros:

  - #i entero (integer, shorint, etc)
  - #f real (float, double, Numeric, Number, et)
  - #s string (char, varchar, etc)
  - #d date (Date, timestamp, etc)

Los parametros de arriba son convertidos "Sql Binding Parameters' 
soportados por varios motores de Sql. Y deben ser usados siempre 
en clausulas where o donde lo soporten los motores

Parametro macro
---
Ademas se permite un parametro extra, sustituye textualmente el valor
pasado.

  - #m macro (Para sustituir literalmente en el Sql)

A diferencia de los demas parametros #m, puede usarse en cualquier parte.
La diferencia radica en el momento de la sustitución del valor. Para el 
caso de #m, los valores son sustituidos previa ejecución del Sql.
Con los demas parametros se utilizan las funcionalidades del Motor de Sql.
Y puede derivar en un incremento de la performance, asi como conversion y
chequeo automatico de tipos, y evitarse problemas de seguridad.


Banderas
---
Todos los metodos que ejecutan querys aceptan al principio de la cadena
una seccion de banderas que empizan y terminan con corchetes.

Las banderas se utlizan para modificar el comportamiento de la ejecucion
del query. Las banderas implementadas son:

  - [id] Especifica que luego de ejecutar el comando se retorne el id
    generado.

Ejemplo de usos de parametros
---

  Sql.exec("select * from #m where id = #i", "gde_deudaadm", 345)
  Sql.exec("select * from #s where id = #i", "gde_deudaadm", 345) //ERROR

  Sql.exec("select * from persona where nombre like '%#m%", "fernan")
  Sql.exec("select * from persona where fechaNac < #m", new Date()); //ERROR
  Sql.exec("select * from persona where fechaNac < #d", new Date()); //OK
  Sql.exec("select * from persona where fechaNac < '#m'", '2011-03-02'); //OK (dependiendo del motor)


Ejemplo por tablas
---
  //recuperar persona id = 12
  persona = Sql.get("persona", 12)
  
  //modificar nombre por Ariel
  persona.nombre = "Ariel"
  Sql.save("persona", persona)
  
  //crear una nueva persona similar a 12
  persona.id = 0
  Sql.save("persona", persona)
  
  //crear una nueva persona
  //recuperar nuevo id
  persona = {}
  persona.nombre = "Carlos"
  persona.apellido = "Chombini"
  var id = Sql.save("[id] persona", persona)
  
  //borrar persona
  sql.delete("persona", 13)
  
  
Ejemplos para recuperar valores
---

  //recuperar persona por id
  persona = Sql.row("select * from persona where dni=#i", 24586843);
  
  //recuperar lista de personas en memoria
  familiares = Sql.list("select * from persona where idparent = #i", 13)
  
  //recuperar un valor
  cant = Sql.value("select count(*) n from persona where idparent = #i", 13)
  
  //recorrer toda las personas de la tabla
  cr = Sql.cursor("select * from persona")
  while (persona = cr.read) {
    mailto(persona)
  }
  cr.close()
  

  
Ejemplos para ejecutar sql
---
  //actualizar varios registros y obtener la cantidad modificados
  n = Sql.exec("update persona set estado = #i where idparent = #i", 3, 13)
  
  //bulk insert y leer regitros afectados.
  n = Sql.exec("insert into nombres values (select id, nombre from persona where idparent = #i)", 13)
  
  //eliminar varios registros y leer regitros afectados.
  n = Sql.exec("delete from nombre where id > #i", 256)

  