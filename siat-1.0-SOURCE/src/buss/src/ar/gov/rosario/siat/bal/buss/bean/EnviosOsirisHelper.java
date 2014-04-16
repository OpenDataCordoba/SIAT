//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.AlcanceEturAfip;
import coop.tecso.demoda.iface.model.CategoriaEmpresaAfip;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.EnCaracterDeAfip;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.ImpuestoAfip;
import coop.tecso.demoda.iface.model.PropietarioAfip;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoDeduccionAfip;
import coop.tecso.demoda.iface.model.TipoDocumentoAfip;
import coop.tecso.demoda.iface.model.TipoPagoAfip;
import coop.tecso.demoda.iface.model.TipoPersona;
import coop.tecso.demoda.iface.model.TratamientoActividadAfip;
import coop.tecso.demoda.iface.model.UnidadesMedidasAfip;

public class EnviosOsirisHelper {

	/**
	 *  Devuelve un string con la información de las columnas (c01n..c30n) y la del campo contenido en un string formateado.
	 * 
	 */
	public static String obtenerDataStrParaDetalleDJ(DetalleDJ detalleDJ){
		String data = "";
		
		// Agregar el Titulo del Registro
		data = EnviosOsirisHelper.agregarTituloDelRegistro(data, detalleDJ);
		
		// Columnas
		switch (detalleDJ.getRegistro()) {
        case 1: // Cabecera Encriptada
        		data += "\nCódigo Jurisdicción Cabecera: "+StringUtil.formatDouble(detalleDJ.getC01n(),"0");
        		data += "\nNúmero de Formulario: "+StringUtil.formatDouble(detalleDJ.getC02n(),"0");
        		if(FormularioAfip.getById(detalleDJ.getC02n().intValue()) != null)
        			data +=", "+FormularioAfip.getById(detalleDJ.getC02n().intValue()).getValue();
        		data += "\nImpuesto: "+StringUtil.formatDouble(detalleDJ.getC03n(),"0");
        		if(ImpuestoAfip.getById(detalleDJ.getC03n().intValue()) != null)
        			data +=", "+ImpuestoAfip.getById(detalleDJ.getC03n().intValue()).getValue();
        		data += "\nConcepto: "+StringUtil.formatDouble(detalleDJ.getC04n(),"0");
        		data += "\nCUIT: "+StringUtil.formatDouble(detalleDJ.getC05n(),"0");
        		data += "\nNúmero de Inscripción en IIBB o Convenio Multilateral:"+StringUtil.formatDouble(detalleDJ.getC06n(),"0");
        		data += "\nPeríodo: "+StringUtil.formatDouble(detalleDJ.getC07n(),"0");
        		data += "\nCuota: "+StringUtil.formatDouble(detalleDJ.getC08n(),"0");
        		data += "\nCódigo de Rectificativa: "+StringUtil.formatDouble(detalleDJ.getC09n(),"0");
        		data += "\nHora: "+DateUtil.formatDate(DateUtil.getTime(StringUtil.formatDouble(detalleDJ.getC10n(),"0"),"HHmmss"),"HH:mm:ss");
        		data += "\nVersión: "+StringUtil.formatDouble(detalleDJ.getC11n(),"0");
        		data += "\nRelease: "+StringUtil.formatDouble(detalleDJ.getC12n(),"0");
        		data += "\nVersión Interna del Aplicativo: "+StringUtil.formatDouble(detalleDJ.getC13n(),"0");
        		data += "\nNúmero Verificador: "+StringUtil.formatDouble(detalleDJ.getC14n(),"0");
        		data += "\nFecha de Vencimiento: "+DateUtil.formatDateForReport(DateUtil.getDate(StringUtil.formatDouble(detalleDJ.getC15n(),"0"),DateUtil.YYYYMMDD_MASK));
        		data += "\nC16n: "+StringUtil.formatDouble(detalleDJ.getC16n(),"0");
        		data += "\nC17n: "+StringUtil.formatDouble(detalleDJ.getC17n(),"0");
        		data += "\nC18n: "+StringUtil.formatDouble(detalleDJ.getC18n(),"0");
        		data += "\nC19n: "+StringUtil.formatDouble(detalleDJ.getC19n(),"0");
        		data += "\nC20n: "+StringUtil.formatDouble(detalleDJ.getC20n(),"0");
        		data += "\nC21n: "+StringUtil.formatDouble(detalleDJ.getC21n(),"0");
        		data += "\nC22n: "+StringUtil.formatDouble(detalleDJ.getC22n(),"0");
        		data += "\nC23n: "+StringUtil.formatDouble(detalleDJ.getC23n(),"0");
        		data += "\nC24n: "+StringUtil.formatDouble(detalleDJ.getC24n(),"0");
        		data += "\nC25n: "+StringUtil.formatDouble(detalleDJ.getC25n(),"0");
        		data += "\nC26n: "+StringUtil.formatDouble(detalleDJ.getC26n(),"0");
        		data += "\nC27n: "+StringUtil.formatDouble(detalleDJ.getC27n(),"0");
        		data += "\nC28n: "+StringUtil.formatDouble(detalleDJ.getC28n(),"0");
        		data += "\nC29n: "+StringUtil.formatDouble(detalleDJ.getC29n(),"0");
        		data += "\nC30n: "+StringUtil.formatDouble(detalleDJ.getC30n(),"0");
        		data += "\nContenido: "+detalleDJ.getContenido();
        		break;
        case 98: // Totales de Derecho y Accesorios de la DJ
			data += "\nCódigo de Impuesto: "+StringUtil.formatDouble(detalleDJ.getC01n(),"0");
    		if(ImpuestoAfip.getById(detalleDJ.getC01n().intValue()) != null)
    			data +=" - "+ImpuestoAfip.getById(detalleDJ.getC01n().intValue()).getValue();
			data += "\nConcepto: "+StringUtil.formatDouble(detalleDJ.getC02n(),"0");
			data += "\nTotal de Monto Ingresado: "+StringUtil.formatDouble(detalleDJ.getC03n(),"0");
			data += "\nC04n: "+StringUtil.formatDouble(detalleDJ.getC04n(),"0");
			data += "\nC05n: "+StringUtil.formatDouble(detalleDJ.getC05n(),"0");
			data += "\nC06n: "+StringUtil.formatDouble(detalleDJ.getC06n(),"0");
			data += "\nC07n: "+StringUtil.formatDouble(detalleDJ.getC07n(),"0");
			data += "\nC08n: "+StringUtil.formatDouble(detalleDJ.getC08n(),"0");
			data += "\nC09n: "+StringUtil.formatDouble(detalleDJ.getC09n(),"0");
			data += "\nC10n: "+StringUtil.formatDouble(detalleDJ.getC10n(),"0");
			data += "\nC11n: "+StringUtil.formatDouble(detalleDJ.getC11n(),"0");
			data += "\nC12n: "+StringUtil.formatDouble(detalleDJ.getC12n(),"0");
			data += "\nC13n: "+StringUtil.formatDouble(detalleDJ.getC13n(),"0");
			data += "\nC14n: "+StringUtil.formatDouble(detalleDJ.getC14n(),"0");
			data += "\nC15n: "+StringUtil.formatDouble(detalleDJ.getC15n(),"0");
			data += "\nC16n: "+StringUtil.formatDouble(detalleDJ.getC16n(),"0");
			data += "\nC17n: "+StringUtil.formatDouble(detalleDJ.getC17n(),"0");
			data += "\nC18n: "+StringUtil.formatDouble(detalleDJ.getC18n(),"0");
			data += "\nC19n: "+StringUtil.formatDouble(detalleDJ.getC19n(),"0");
			data += "\nC20n: "+StringUtil.formatDouble(detalleDJ.getC20n(),"0");
			data += "\nC21n: "+StringUtil.formatDouble(detalleDJ.getC21n(),"0");
			data += "\nC22n: "+StringUtil.formatDouble(detalleDJ.getC22n(),"0");
			data += "\nC23n: "+StringUtil.formatDouble(detalleDJ.getC23n(),"0");
			data += "\nC24n: "+StringUtil.formatDouble(detalleDJ.getC24n(),"0");
			data += "\nC25n: "+StringUtil.formatDouble(detalleDJ.getC25n(),"0");
			data += "\nC26n: "+StringUtil.formatDouble(detalleDJ.getC26n(),"0");
			data += "\nC27n: "+StringUtil.formatDouble(detalleDJ.getC27n(),"0");
			data += "\nC28n: "+StringUtil.formatDouble(detalleDJ.getC28n(),"0");
			data += "\nC29n: "+StringUtil.formatDouble(detalleDJ.getC29n(),"0");
			data += "\nC30n: "+StringUtil.formatDouble(detalleDJ.getC30n(),"0");
			data += "\nContenido: "+detalleDJ.getContenido();
			break;
        default: // Todos los registros restantes
    		data += "\nC01n: "+StringUtil.formatDouble(detalleDJ.getC01n(),"0");
			data += "\nC02n: "+StringUtil.formatDouble(detalleDJ.getC02n(),"0");
			data += "\nC03n: "+StringUtil.formatDouble(detalleDJ.getC03n(),"0");
			data += "\nC04n: "+StringUtil.formatDouble(detalleDJ.getC04n(),"0");
			data += "\nC05n: "+StringUtil.formatDouble(detalleDJ.getC05n(),"0");
			data += "\nC06n: "+StringUtil.formatDouble(detalleDJ.getC06n(),"0");
			data += "\nC07n: "+StringUtil.formatDouble(detalleDJ.getC07n(),"0");
			data += "\nC08n: "+StringUtil.formatDouble(detalleDJ.getC08n(),"0");
			data += "\nC09n: "+StringUtil.formatDouble(detalleDJ.getC09n(),"0");
			data += "\nC10n: "+StringUtil.formatDouble(detalleDJ.getC10n(),"0");
			data += "\nC11n: "+StringUtil.formatDouble(detalleDJ.getC11n(),"0");
			data += "\nC12n: "+StringUtil.formatDouble(detalleDJ.getC12n(),"0");
			data += "\nC13n: "+StringUtil.formatDouble(detalleDJ.getC13n(),"0");
			data += "\nC14n: "+StringUtil.formatDouble(detalleDJ.getC14n(),"0");
			data += "\nC15n: "+StringUtil.formatDouble(detalleDJ.getC15n(),"0");
			data += "\nC16n: "+StringUtil.formatDouble(detalleDJ.getC16n(),"0");
			data += "\nC17n: "+StringUtil.formatDouble(detalleDJ.getC17n(),"0");
			data += "\nC18n: "+StringUtil.formatDouble(detalleDJ.getC18n(),"0");
			data += "\nC19n: "+StringUtil.formatDouble(detalleDJ.getC19n(),"0");
			data += "\nC20n: "+StringUtil.formatDouble(detalleDJ.getC20n(),"0");
			data += "\nC21n: "+StringUtil.formatDouble(detalleDJ.getC21n(),"0");
			data += "\nC22n: "+StringUtil.formatDouble(detalleDJ.getC22n(),"0");
			data += "\nC23n: "+StringUtil.formatDouble(detalleDJ.getC23n(),"0");
			data += "\nC24n: "+StringUtil.formatDouble(detalleDJ.getC24n(),"0");
			data += "\nC25n: "+StringUtil.formatDouble(detalleDJ.getC25n(),"0");
			data += "\nC26n: "+StringUtil.formatDouble(detalleDJ.getC26n(),"0");
			data += "\nC27n: "+StringUtil.formatDouble(detalleDJ.getC27n(),"0");
			data += "\nC28n: "+StringUtil.formatDouble(detalleDJ.getC28n(),"0");
			data += "\nC29n: "+StringUtil.formatDouble(detalleDJ.getC29n(),"0");
			data += "\nC30n: "+StringUtil.formatDouble(detalleDJ.getC30n(),"0");
			data += "\nContenido: "+detalleDJ.getContenidoParaParser();
			break;
		}

		return data;
	}
	
	public static String agregarTituloDelRegistro(String data, DetalleDJ detalleDJ){
		// Titulo del Registro
		switch (detalleDJ.getRegistro()) {
        case 1: 
     		data += "Cabecera Encriptada: ";
     		data += "\n-------------------- ";
    		break;
        case 2: 
     		data += "Datos Generales de la Empresa: ";
     		data += "\n------------------------------ ";
    		break;
		case 3: // 
     		data += "Datos de Convenio : ";
     		data += "\n------------------- ";
			break;
		case 4: //  
     		data += "Socios: ";
     		data += "\n------- ";
			break;
		case 5: //  
     		data += "Firmantes: ";
     		data += "\n---------- ";
			break;
		case 6: //  
     		data += "Locales: ";
     		data += "\n-------- ";
			break;	
		case 7: //  
     		data += "Habilitaciones de los Locales: ";
     		data += "\n------------------------------ ";
			break;
		case 8: //  
     		data += "Actividades de los Locales: ";
     		data += "\n--------------------------- ";
			break;
		case 9: //  
     		data += "Exenciones de las Actividades: ";
     		data += "\n------------------------------ ";
			break;
		case 10: // 
     		data += "Otros Pagos: ";
     		data += "\n------------ ";
			break;
		case 11: // 
     		data += "Declaracion de Actividades por Local: ";
     		data += "\n------------------------------------ ";
			break;
		case 12: // 
     		data += "Totales por Local de las Actividades Declaradas: ";
     		data += "\n------------------------------------------------ ";
			break;
		case 13: //  
     		data += "Retenciones y Percepciones: ";
     		data += "\n--------------------------- ";
			break;
		case 14: //  
     		data += "Ajuste Base Imponible por cambio de Coeficiente por local: ";
     		data += "\n---------------------------------------------------------";
			break;
		case 15: //  
     		data += "Liquidacion de DJ Mensual DREI: ";
     		data += "\n------------------------------ ";
			break;
		case 16: // 
     		data += "Declaracion de Actividades ETUR por Local: ";
     		data += "\n------------------------------------------ ";
			break;
		case 17: // 
     		data += "Totales por Local de las Actividades Declaradas ETUR: ";
     		data += "\n----------------------------------------------------- ";
			break;
		case 18: //  
     		data += "Liquidacion de DJ Mensual ETUR: ";
     		data += "\n------------------------------- ";
			break;
		case 20: //  
     		data += "Datos de Domicilios: ";
     		data += "\n------------------- ";
			break;
		case 96: //  
     		data += "Datos de pago por Cuenta: ";
     		data += "\n------------------------- ";
			break;
		case 98: // 
     		data += "Totales de Derecho y Accesorios de la DJ: ";
     		data += "\n----------------------------------------- ";
			break;
        default: // Todos los registros restantes
        	data += "Desconocido: ";
 			data += "\n------------ ";
			break;
		}

		return data;
	}
	
	/**
	 * Agrega un '|' como divisor de campo segun el nro de registro.
	 * 
	 * @return
	 */
	public static String prepararContenidoParaParser(DetalleDJ detalleDJ){
		String parserCon = "";
		boolean fallo = false;
		try{
			switch (detalleDJ.getRegistro()) {
			case 1: // Cabecera Encriptada
					// Por ahora no lo parseamos porque ya viene parseado en desde Mulator 
					break;
			case 2: // Datos Generales de la Empresa
					for(int i=0; i < detalleDJ.getContenido().length(); i++){
						//   1		   2          3         4          5          6          7          8          9          10          11          12
						if(i == 1 || i == 2 || i == 12 || i == 20 || i == 28 || i == 43 || i == 93 || i == 94 || i == 95 || i == 103 || i == 104 || i == 112)
							parserCon += '|';
						parserCon += detalleDJ.getContenido().charAt(i);
					}
					break;
			case 3: // Datos de Convenio 
					for(int i=0; i < detalleDJ.getContenido().length(); i++){
						//   1		   2         3         4         5         6          7          8          9          10         11         12         13         14         15         16
						if(i == 1 || i == 6 || i == 7 || i == 8 || i == 9 || i == 10 || i == 11 || i == 12 || i == 13 || i == 14 || i == 15 || i == 16 || i == 21 || i == 22 || i == 30 || i == 38)
							parserCon += '|';
						parserCon += detalleDJ.getContenido().charAt(i);
					}
					break;
			case 4: // Socios 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		    2          3           4           5           6           7       
					if(i == 35 || i == 70 || i == 110 || i == 112 || i == 113 || i == 133 || i == 144)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 5: // Firmantes 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1   	    2          3          4          5          6             
					if(i == 35 || i == 75 || i == 77 || i == 78 || i == 98 || i == 109)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 6: // Locales 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		   2          3          4          5          6          7          8
					if(i == 9 || i == 14 || i == 22 || i == 30 || i == 80 || i == 81 || i == 82 || i == 83)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;	
			case 7: // Habilitaciones de los Locales 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1	       2          3                 
					if(i == 9 || i == 16 || i == 24)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 8: // Actividades de los Locales 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1	       2          3          4          5                 
					if(i == 9 || i == 16 || i == 24 || i == 25 || i == 26)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 9: // Exenciones de las Actividades 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1	       2          3          4          5          6             
					if(i == 9 || i == 16 || i == 21 || i == 29 || i == 37 || i == 45)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 10: // Otros Pagos
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1	       2          3          4          5          6          7       
					if(i == 9 || i == 10 || i == 18 || i == 24 || i == 44 || i == 48 || i == 63)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 11: // Declaracion de Actividades por Local (DREI)
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		   2          3          4          5          6          7          8          9          10         11         12          13          14          15          16          17          18
					if(i == 9 || i == 16 || i == 31 || i == 46 || i == 47 || i == 62 || i == 63 || i == 78 || i == 82 || i == 83 || i == 98 || i == 110 || i == 111 || i == 113 || i == 125 || i == 140 || i == 141 || i == 156)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 12: // Totales por Local de las Actividades Declaradas (DREI)
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		   2          3          4          5          6          7          8          9          10         11         12         13         14          15          16          17          18          19          20
					if(i == 9 || i == 10 || i == 11 || i == 12 || i == 27 || i == 42 || i == 43 || i == 58 || i == 62 || i == 77 || i == 81 || i == 96 || i == 97 || i == 112 || i == 127 || i == 142 || i == 157 || i == 158 || i == 173 || i == 174)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 13: // Retenciones y Percepciones 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1	       2          3          4          5          6             
					if(i == 1 || i == 12 || i == 52 || i == 60 || i == 80 || i == 95)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 14: // Ajuste Base Imponible por cambio de Coeficiente por local 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		   2          3          4          5          6          7          8           9           10          11          12          13          14
					if(i == 9 || i == 16 || i == 31 || i == 46 || i == 61 || i == 76 || i == 91 || i == 106 || i == 121 || i == 136 || i == 151 || i == 166 || i == 181 || i == 196)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 15: // Liquidacion de DJ Mensual DREI 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		    2          3          4          5          6          7          8          9          10          11          12          13    
					if(i == 15 || i == 30 || i == 31 || i == 37 || i == 39 || i == 54 || i == 69 || i == 84 || i == 92 || i == 100 || i == 106 || i == 121 || i == 136)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 16: // Declaracion de Actividades ETUR por Local
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		   2          3          4          5          6          7          8    
					if(i == 9 || i == 16 || i == 18 || i == 33 || i == 37 || i == 52 || i == 67 || i == 82)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 17: // Totales por Local de las Actividades Declaradas ETUR
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		   2          3    
					if(i == 9 || i == 24 || i == 25)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 18: // Liquidacion de DJ Mensual ETUR 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1		    2          3          4          5          6        
					if(i == 15 || i == 23 || i == 31 || i == 37 || i == 52 || i == 67)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 20: // Datos de Domicilios 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1	       2         3          4          5          6          7          8          9          10          11          12          13    
					if(i == 1 || i == 6 || i == 56 || i == 61 || i == 66 || i == 70 || i == 73 || i == 81 || i == 86 || i == 136 || i == 186 || i == 194 || i == 196)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 96: // Datos de pago por Cuenta 
				for(int i=0; i < detalleDJ.getContenido().length(); i++){
					//   1	       2          3             
					if(i == 9 || i == 13 || i == 28)
						parserCon += '|';
					parserCon += detalleDJ.getContenido().charAt(i);
				}
				break;
			case 98: // Totales de Derecho y Accesorios de la DJ
					// Por ahora no lo parseamos porque ya viene parseado en desde Mulator
					break;
			}			
		}catch (Exception e) {
			fallo = true;
			parserCon = detalleDJ.getContenido();
		}
		
		if(fallo)
			parserCon += "\n(NO SE PUDO PARSEAR EL CONTENIDO STR)";
		return parserCon;
	}
	

	/**
	 *  Devuelve un string con la información de las columnas (c01n..c30n) parseadas del campo contenido en un string preformateado.
	 * 
	 */
	public static String obtenerDataStrFromContenido(DetalleDJ detalleDJ){
		try{
			String data = "";
			Datum datum = Datum.parse(detalleDJ.getContenidoParaParser());

			// Agregar el Titulo del Registro
			data = EnviosOsirisHelper.agregarTituloDelRegistro(data, detalleDJ);
			
			// Columnas
			switch (detalleDJ.getRegistro()) {
			case 1: // Cabecera Encriptada
					// No es necesario el parseo del registro
					data = null;
					break;
			case 2: // Datos Generales de la Empresa
		     		data += "\nTipo de Organización: "+datum.getCols(0);
	        		if(TipoPersona.getById(datum.getInteger(0)) != null)
	        			data +=" - "+TipoPersona.getById(datum.getInteger(0)).getValue();
					data += "\nCategoría: "+datum.getCols(1);
	        		if(CategoriaEmpresaAfip.getById(datum.getInteger(1)) != null)
	        			data +=" - "+CategoriaEmpresaAfip.getById(datum.getInteger(1)).getValue();
					data += "\nNúmero de Inscripción en el impuesto sobre los IIBB: "+datum.getCols(2);
					data += "\nFecha de Inscripción en IIBB: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
					data += "\nFecha de Baja en IIBB: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(4),DateUtil.YYYYMMDD_MASK));
					data += "\nNúmero de Teléfono: "+datum.getCols(5);
					data += "\nCorreo Electrónico: "+datum.getCols(6);
					data += "\nOtros locales en la Prov. de Santa Fé fuera de Rosario: "+datum.getCols(7);
					data += "\nContribuyente Concursado: "+datum.getCols(8);
					data += "\nFecha Presentación del Concurso: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(9),DateUtil.YYYYMMDD_MASK));
					data += "\nContribuyente Fallido: "+datum.getCols(10);
					data += "\nFecha de Declaración de Quiebra: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(11),DateUtil.YYYYMMDD_MASK));
	        		break;
			case 3: // Datos de Convenio
		     		data += "\nRégimen General: "+SiNo.getById(datum.getInteger(0)).getValue();
					data += "\nCoeficiente de Santa Fé: "+StringUtil.formatDouble(datum.getDouble(1)/10000);
					data += "\nRégimen Especial: "+SiNo.getById(datum.getInteger(2)).getValue();
					data += "\nArtículo 6: "+SiNo.getById(datum.getInteger(3)).getValue();
					data += "\nArtículo 7: "+SiNo.getById(datum.getInteger(4)).getValue();
					data += "\nArtículo 8: "+SiNo.getById(datum.getInteger(5)).getValue();
					data += "\nArtículo 9: "+SiNo.getById(datum.getInteger(6)).getValue();
					data += "\nArtículo 10: "+SiNo.getById(datum.getInteger(7)).getValue();
					data += "\nArtículo 11: "+SiNo.getById(datum.getInteger(8)).getValue();
					data += "\nArtículo 12: "+SiNo.getById(datum.getInteger(9)).getValue();
					data += "\nArtículo 13: "+SiNo.getById(datum.getInteger(10)).getValue();
					data += "\nOtros Locales en la Prov. de Santa Fé fuera de Rosario: "+SiNo.getById(datum.getInteger(11)).getValue();
					data += "\nCoeficiente Intercomunal: "+StringUtil.formatDouble(datum.getDouble(12)/10000);
					data += "\nOtros Locales en el País fuera de la Prov. de Santa Fé: "+SiNo.getById(datum.getInteger(13)).getValue();
					data += "\nFecha de Inscripción en Convenio Multilateral: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(14),DateUtil.YYYYMMDD_MASK));
					data += "\nFecha de Baja en Convenio Multilateral: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(15),DateUtil.YYYYMMDD_MASK));
	    		break;
			case 4: // Socios
				data += "\nApellido: "+datum.getCols(0);
				data += "\nApellido Materno: "+datum.getCols(1);
				data += "\nNombre: "+datum.getCols(2);
				data += "\nEn carácter de: "+datum.getCols(3);
        		if(EnCaracterDeAfip.getById(datum.getInteger(3)) != null)
        			data +=" - "+EnCaracterDeAfip.getById(datum.getInteger(3)).getValue();
				data += "\nTipo de Documento de Identidad: "+datum.getCols(4);
        		if(TipoDocumentoAfip.getById(datum.getInteger(4)) != null)
        			data +=" - "+TipoDocumentoAfip.getById(datum.getInteger(4)).getValue();
				data += "\nNúmero de Documento de Identidad: "+datum.getCols(5);
				data += "\nCUIT/CUIL: "+datum.getCols(6);
				break;
			case 5: // Firmantes 
				data += "\nApellido: "+datum.getCols(0);
				data += "\nNombre: "+datum.getCols(1);
				data += "\nEn carácter de: "+datum.getCols(2);
        		if(EnCaracterDeAfip.getById(datum.getInteger(2)) != null)
        			data +=" - "+EnCaracterDeAfip.getById(datum.getInteger(2)).getValue();
				data += "\nTipo de Documento de Identidad: "+datum.getCols(3);
        		if(TipoDocumentoAfip.getById(datum.getInteger(3)) != null)
        			data +=" - "+TipoDocumentoAfip.getById(datum.getInteger(3)).getValue();
				data += "\nNúmero de Documento de Identidad: "+datum.getCols(4);
				data += "\nCUIT/CUIL: "+datum.getCols(5);
				break;
			case 6: // Locales 
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCantidad de Personal: "+datum.getCols(1);
				data += "\nFecha de Inicio de Actividades: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
				data += "\nFecha de Cese de Actividades: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
				data += "\nNombre de Fantasía: "+datum.getCols(4);
				data += "\nCentraliza Ingresos: "+SiNo.getById(datum.getInteger(5)).getValue();
				data += "\nContribuyente ETUR: "+SiNo.getById(datum.getInteger(6)).getValue();
				data += "\nRadio: "+datum.getCols(7);
				break;	
			case 7: // Habilitaciones de los Locales  
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCódigo de Rubro: "+datum.getCols(1);
				data += "\nFecha de Habilitación: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
				break;
			case 8: // Actividades de los Locales 
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCódigo de Actividad: "+datum.getCols(1);
				data += "\nFecha de Inicio: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
				data += "\nMarca Principal: "+datum.getCols(3);
				data += "\nTratamiento: "+datum.getCols(4);
        		if(TratamientoActividadAfip.getById(datum.getInteger(4)) != null)
        			data +=" - "+TratamientoActividadAfip.getById(datum.getInteger(4)).getValue();
				break;
			case 9: // Exenciones de las Actividades  
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCódigo de Actividad: "+datum.getCols(1);
				data += "\nNro de Resolución: "+datum.getCols(2);
				data += "\nFecha de Emisión: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
				data += "\nFecha Desde: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(4),DateUtil.YYYYMMDD_MASK));
				data += "\nFecha Hasta: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(5),DateUtil.YYYYMMDD_MASK));
				break;
			case 10: // Otros Pagos
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nTipo de Pago: "+datum.getCols(1);
				if(TipoPagoAfip.getById(datum.getInteger(1)) != null)
        			data +=" - "+TipoPagoAfip.getById(datum.getInteger(1)).getValue();
				data += "\nFecha de Pago: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
				data += "\nPeríodo del Pago: "+datum.getCols(3);
				data += "\nNúmero de Resolución: "+datum.getCols(4);
				data += "\nAño: "+datum.getCols(5);
				data += "\nImporte Pagado: "+StringUtil.formatDouble(datum.getDouble(6)/100);
				break;
			case 11: // Declaracion de Actividades por Local
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCódigo de Actividad: "+datum.getCols(1);
				data += "\nBase Imponible Exenta: "+datum.getCols(2);
				data += "\nBase Imponible: "+StringUtil.formatDouble(datum.getDouble(3)/100);
				data += "\nSigno del Ajuste por cambio de coeficiente: "+datum.getCols(4);
				data += "\nAjuste por cambio de coeficiente: "+StringUtil.formatDouble(datum.getDouble(5)/100);
				data += "\nSigno de la Base Imponible Ajustada: "+datum.getCols(6);
				data += "\nBase Imponible Ajustada: "+StringUtil.formatDouble(datum.getDouble(7)/100);
				data += "\nAlícuota:"+StringUtil.formatDouble(datum.getDouble(8)/100)+" % ";
				data += "\nSigno del Derecho Calculado: "+datum.getCols(9);
				data += "\nDerecho Calculado: "+StringUtil.formatDouble(datum.getDouble(10)/100);
				data += "\nCantidad: "+StringUtil.formatDouble(datum.getDouble(11)/100);
				data += "\nUnidades de medida: "+datum.getCols(12);
				if(UnidadesMedidasAfip.getById(datum.getInteger(12)) != null)
        			data +=" - "+UnidadesMedidasAfip.getById(datum.getInteger(12)).getValue();
				data += "\nTipo de Unidades: "+datum.getCols(13);
				data += "\nMínimo por Unidad: "+StringUtil.formatDouble(datum.getDouble(14)/100);
				data += "\nMínimo Calculado: "+StringUtil.formatDouble(datum.getDouble(15)/100);
				data += "\nSigno del Derecho Determinado: "+datum.getCols(16);
				data += "\nDerecho Determinado: "+StringUtil.formatDouble(datum.getDouble(17)/100);
				break;
			case 12: // Totales por Local de las Actividades Declaradas
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nPago Mínimo: "+SiNo.getById(datum.getInteger(1)).getValue();
				data += "\nCentraliza Ingresos: "+SiNo.getById(datum.getInteger(2)).getValue();
				data += "\nSigno del Derecho Determinado Total: "+datum.getCols(3);
				data += "\nDerecho Determinado Total: "+StringUtil.formatDouble(datum.getDouble(4)/100);
				data += "\nMínimo General: "+StringUtil.formatDouble(datum.getDouble(5)/100);
				data += "\nSigno del Derecho: "+datum.getCols(6);
				data += "\nDerecho: "+StringUtil.formatDouble(datum.getDouble(7)/100);
				data += "\nAlícuota de Publicidad: "+StringUtil.formatDouble(datum.getDouble(8)/100)+" % ";
				data += "\nPublicidad: "+StringUtil.formatDouble(datum.getDouble(9)/100);
				data += "\nAlícuota de Mesas y Sillas: "+StringUtil.formatDouble(datum.getDouble(10)/100)+" % ";
				data += "\nMesas y Sillas: "+StringUtil.formatDouble(datum.getDouble(11)/100);
				data += "\nSigno del Subtotal(1): "+datum.getCols(12);
				data += "\nSubtotal(1): "+StringUtil.formatDouble(datum.getDouble(13)/100);
				data += "\nOtros Pagos: "+StringUtil.formatDouble(datum.getDouble(14)/100);
				data += "\nComputado en el período: "+StringUtil.formatDouble(datum.getDouble(15)/100);
				data += "\nResto: "+StringUtil.formatDouble(datum.getDouble(16)/100);
				data += "\nSigno del Derecho Total: "+datum.getCols(17);
				data += "\nDerecho Total: "+StringUtil.formatDouble(datum.getDouble(18)/100);
				data += "\nPaga: "+SiNo.getById(datum.getInteger(19)).getValue();
				break;
			case 13: // Retenciones y Percepciones 
				data += "\nTipo de Deducción: "+datum.getCols(0);
        		if(TipoDeduccionAfip.getById(datum.getInteger(0)) != null)
        			data +=" - "+TipoDeduccionAfip.getById(datum.getInteger(0)).getValue();
				data += "\nCUIT Agente: "+datum.getCols(1);
				data += "\nDenominación: "+datum.getCols(2);
				data += "\nFecha: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
				data += "\nNúmero de Constancia: "+datum.getCols(4);
				data += "\nImporte: "+StringUtil.formatDouble(datum.getDouble(5)/100);
				break;
			case 14: // Ajuste Base Imponible por cambio de Coeficiente por local  
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCódigo de Actividad: "+datum.getCols(1);
				data += "\nDiferencia Base Enero: "+StringUtil.formatDouble(datum.getDouble(2)/100);
				data += "\nDiferencia Base Febrero: "+StringUtil.formatDouble(datum.getDouble(3)/100);
				data += "\nDiferencia Base Marzo: "+StringUtil.formatDouble(datum.getDouble(4)/100);
				data += "\nDiferencia Base Abril: "+StringUtil.formatDouble(datum.getDouble(5)/100);
				data += "\nDiferencia Base Mayo: "+StringUtil.formatDouble(datum.getDouble(6)/100);
				data += "\nDiferencia Base Junio: "+StringUtil.formatDouble(datum.getDouble(7)/100);
				data += "\nDiferencia Base Julio: "+StringUtil.formatDouble(datum.getDouble(8)/100);
				data += "\nDiferencia Base Agosto: "+StringUtil.formatDouble(datum.getDouble(9)/100);
				data += "\nDiferencia Base Septiembre: "+StringUtil.formatDouble(datum.getDouble(10)/100);
				data += "\nDiferencia Base Octubre: "+StringUtil.formatDouble(datum.getDouble(11)/100);
				data += "\nDiferencia Base Noviembre: "+StringUtil.formatDouble(datum.getDouble(12)/100);
				data += "\nDiferencia Base Diciembre:"+StringUtil.formatDouble(datum.getDouble(13)/100);
				break;
			case 15: // Liquidacion de DJ Mensual DREI 
				data += "\nDerecho Determinado: "+StringUtil.formatDouble(datum.getDouble(0)/100);
				data += "\nTotal de Retenciones y Percepciones: "+StringUtil.formatDouble(datum.getDouble(1)/100);
				data += "\nRetenciones y Percepciones de períodos anteriores: "+SiNo.getById(datum.getInteger(2)).getValue();
				data += "\nPeríodo Retenc. y Perc. de períodos anteriores: "+datum.getCols(3);
				data += "\nCódigo de Rectificativa de Ret. y Per. de periodos anteriores: "+datum.getCols(4);
				data += "\nMonto de Retenciones y Percepciones de periodos anteriores: "+StringUtil.formatDouble(datum.getDouble(5)/100);
				data += "\nA favor del contribuyente: "+StringUtil.formatDouble(datum.getDouble(6)/100);
				data += "\nA favor de la Dirección de la Municipalidad de Rosario: "+StringUtil.formatDouble(datum.getDouble(7)/100);
				data += "\nFecha de Vencimiento: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(8),DateUtil.YYYYMMDD_MASK));
				data += "\nFecha de pago/presentación: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(9),DateUtil.YYYYMMDD_MASK));
				data += "\nTasa de Interés: "+StringUtil.formatDouble(datum.getDouble(10)/1000);
				data += "\nRecargo por Intereses: "+StringUtil.formatDouble(datum.getDouble(11)/100);
				data += "\nDerecho Adeudado: "+StringUtil.formatDouble(datum.getDouble(12)/100);
				break;
			case 16: // Declaracion de Actividades ETUR por Local
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCódigo de Actividad: "+datum.getCols(1);
				data += "\nAlcance ETUR: "+datum.getCols(2);
	       		if(AlcanceEturAfip.getById(datum.getInteger(2)) != null)
        			data +=" - "+AlcanceEturAfip.getById(datum.getInteger(2)).getValue();
				data += "\nBase Imponible: "+StringUtil.formatDouble(datum.getDouble(3)/100);
				data += "\nAlícuota: "+StringUtil.formatDouble(datum.getDouble(4)/100)+ " % ";
				data += "\nContribución Calculada: "+StringUtil.formatDouble(datum.getDouble(5)/100);
				data += "\nMínimo Calculado: "+StringUtil.formatDouble(datum.getDouble(6)/100);
				data += "\nContribución determinada: "+StringUtil.formatDouble(datum.getDouble(7)/100);
				break;
			case 17: // Totales por Local de las Actividades Declaradas ETUR
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nContribución ETUR: "+StringUtil.formatDouble(datum.getDouble(1)/100);
				data += "\nPaga: "+SiNo.getById(datum.getInteger(2)).getValue();
				break;
			case 18: // Liquidacion de DJ Mensual ETUR 
				data += "\nDerecho Determinado: "+StringUtil.formatDouble(datum.getDouble(2)/100);
				data += "\nFecha de Vencimiento: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(1),DateUtil.YYYYMMDD_MASK));
				data += "\nFecha de pago/presentación: "+DateUtil.formatDateForReport(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
				data += "\nTasa de Interés: "+StringUtil.formatDouble(datum.getDouble(3)/1000);
				data += "\nRecargo por Intereses: "+StringUtil.formatDouble(datum.getDouble(4)/100);
				data += "\nDerecho Adeudado: "+StringUtil.formatDouble(datum.getDouble(5)/100);
				break;
			case 20: // Datos de Domicilios 
				data += "\nCódigo de Propietario: "+datum.getCols(0);
        		if(PropietarioAfip.getById(datum.getInteger(0)) != null)
        			data +=" - "+PropietarioAfip.getById(datum.getInteger(0)).getValue();
				data += "\nCódigo Interno: "+datum.getCols(1);
				data += "\nCalle: "+datum.getCols(2);
				data += "\nNúmero: "+datum.getCols(3);
				data += "\nAdicional: "+datum.getCols(4);
				data += "\nTorre: "+datum.getCols(5);
				data += "\nPiso: "+datum.getCols(6);
				data += "\nDepartamento/Oficina: "+datum.getCols(7);
				data += "\nSector: "+datum.getCols(8); // TODO VER DATO BARRIO!!!!
				data += "\nLocalidad: "+datum.getCols(9);
				data += "\nCódigo postal: "+datum.getCols(10);
				data += "\nProvincia: "+datum.getCols(11);
				break;
			case 96: // Datos de pago por Cuenta  
				data += "\nNúmero de Cuenta Contributiva: "+datum.getCols(0);
				data += "\nCódigo de Impuesto: "+datum.getCols(1);
        		if(ImpuestoAfip.getById(datum.getInteger(1)) != null)
        			data +=" - "+ImpuestoAfip.getById(datum.getInteger(1)).getValue();
				data += "\nTotal de Monto Ingresado: "+StringUtil.formatDouble(datum.getDouble(2)/100);
				break;
			case 98: // Totales de Derecho y Accesorios de la DJ
				// No es necesario el parseo del registro
				data = null;
				break;
			default: // Todos los registros restantes
	    		data += "\n(FORMATO DEL REGISTRO DESCONOCIDO)";
				break;
			}

			return data;
		}catch (Exception e) {
			return "ERROR AL INTENTAR PARSEAR EL CONTENIDO";
		}
	}

}
