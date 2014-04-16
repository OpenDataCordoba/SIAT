Descripcion de los archivos de test

Alta Objeto Imponible
---------------------

AObjImpTest.A        Tipo Objeto imponible que no existe           	Error por no existir Tip.Obj.Imp
AObjImpTest.B        Atributos mal formateados                		Error por formatos invalidos
AObjImpTest.C        Atributo con dominio que no existe       		Error por valor fuera de dominio
AObjImpTest.D        Fechas Accion y/o Vigencia mal Formato   		Error por formatos invalidos de fechas Vig y/o Accion
AObjImpTest.E        Domicilio Mal formateado                 		Error domicilio invalido
AObjImpTest.F        Domicilio Mal formateado /piso y depto   		Error domicilio invalido
AObjImpTest.G        Alta cuenta CRT2                         		Alta cuenta 990000102 con exencion Quita sobre Tasa
AObjImpTest.H        Alta cuenta CRT3                         		Alta cuenta 990000103 con exencion Exento
AObjImpTest.I        Alta cuenta baldio con domicilio         		Alta cuenta 990000104, genera solicitud Imposible asignar Broche a Cuenta
                     sin repartidor                           
AObjImpTest.J        Alta cuenta baldio con domicilio         		Alta cuenta 990000105, asigna broche a cuenta
                     con repartidor


Modificacion Objeto Imponible
-----------------------------

MObjImpTest.A       Cambio de Baldio->Finca Cta 990000106			Cambia Atr, Genera solicitud Modif atributo, Caracter Finca/Baldio
MObjImpTest.B		Cambia atributo a CRT2. Cta 990000105			Cambia Atr, Genera solicitud Modif atributo, Informa valores de CRT y fechas vigencia
MObjImpTest.C       Cambio Retroactivo de Sup.Edif. Cta 990000102   Cambia Atr, Genera solicitud Modif atributo, Informa retroactivo y fechas vigencia
MObjImpTest.D       Cambio atributo formato invalido                Error Formato invalido atributo


Baja Objeto Imponible
---------------------

BObjImpTest.A      Baja de cuenta no existe. Cta 990000999        	Error Objeto imponible no existe
BObjImpTest.B      Baja de Obj Imp. Cta 990000103					Da de baja ObjImponible, Modifica fecha de baja.

Alta de Titular de Cuenta
-------------------------

ATita.txt			Alta Titular con Persona no Contribuyente		Crea cuentaTitular y carga la persona como Contribuyente
ATitb.txt			Alta Titular Principal y Sujeto Exento (SE)		Paso el resto de titulares a secundarios, asigna Broche de SE, y exención Exento.
ATitc.txt			Alta Titular Sujeto Exento a cuenta Exenta		Asigna Broche SE y envía Solicitud de Verificar Exención. Crea Exención a cuenta.
ATitd.txt			Alta Titular a cuenta con Exención				Envía Solicitud de Verificar Exención.

Modificación Titular
---------------------

MTita.txt			Modifica Titular Secundario a Principal			Modifica al Titular como Principal, y a los restantes los pasa a Secundarios
MTitb.txt			Persona que no es Titular de la cuenta			Error

Baja Titular
------------

BTita.txt			Baja Titular de Cuenta							Da de baja el Titular de Cuenta, cambiando la fecha hasta.
BTitb.txt			Baja Titular de que no es titular de Cuenta		Como la persona no es titular, no puede darlo de baja. Pero procesa OK, no informa error.

Alta de Relación de Cuentas (unificación/desglose)
--------------------------------------------------

ARela.txt			Alta Relación entre Ctas(990000101/990000102)	Agrega la CuentaRel

Baja de Relación de Cuentas (unificación/desglose)
--------------------------------------------------

BRela.txt			Baja Relación entre Ctas(990000101/990000102)	Baja la CuentaRel, cambiando la fecha hasta.
BRelb.txt			Baja Relación inexistente						No se puede dar de baja.Pero procesa OK, no informa error.
