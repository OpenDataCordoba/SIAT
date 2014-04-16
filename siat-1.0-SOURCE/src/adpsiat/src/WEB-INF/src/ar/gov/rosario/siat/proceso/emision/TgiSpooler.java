//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.pad.buss.bean.AsignaRepartidor;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Formato del archivo para ImpriPost:
 * 
 * CAJA01N(150): NOMBRE(30) | CUIT(2-8-2) | COD. GESTION(10) | DOMICILIO ENVIO(30) | LOCALIDAD(32) 
 * CAJA02N(150): CUENTA(8-2) | CATASTRAL(2/3/3/3/3) | UBICACION(50) | CARACTER(6) | SUP. TERRENO(6) | 
 * 				 RADIO REF. TRIB.(1) | NRO. BROCHE(4) | VAL. TERR.(9) | SUP. EDIF.(6) | VAL. EDIF.(9) | 
 * 				 PORC. PH(2,2)
 * CAJA03N(150): PERIODO(2)"/"AÑO(4) | IMPORTE(10,2) | DESC. MEJ(20) | IMPORTE MEJ.(10,2) | FEC. VTO.(2-2-4) | 
 * 				 IMPORTE TOTAL(10,2)
 * CAJA04N(150):
 * CAJA05N(150):
 * CAJA06N(150):
 * CAJA07N(150):
 * CAJA12N(150):
 * CAJA08N(150):
 * CAJA09N(150):
 * CAJA10N(150):
 * CAJA13N(150):
 * CAJA14N(150):
 * CAJA15N(150):
 * CAJA16N(150):
 * CAJA17N(150):      
 * 
 * */

public class TgiSpooler {

	private static Logger log = Logger.getLogger(TgiSpooler.class);
	
	private Recurso recurso;
	
	private AsignaRepartidor asignadorTgi;
	
	private TipObjImpDefinition tipObjImpDef;
	
	private FileWriter fileWriter;

	public TgiSpooler(String outputDir, String name) {
		try {
			this.recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_TGI);
			this.asignadorTgi = new AsignaRepartidor(recurso.getId(), new Date());
			this.tipObjImpDef = TipObjImp.getByCodigo("PARCELA").getDefinitionForManual();
			this.fileWriter   = new FileWriter(outputDir + File.separator + name);
			createEncabezado();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("No se pudo crear el spooler");
		}
	}
	
	public synchronized void createRegistro(DeudaAdmin deuda) throws Exception {
		
		Long anio = 0L;
		Long periodo = 0L;
		Cuenta cuenta = deuda.getCuenta();
		ObjImp objImp = cuenta.getObjImp();
		objImp.loadDefinition(this.tipObjImpDef, new Date());

		// numero cuenta
		String numCuenta = cuenta.getNumeroCuenta();
		// nombre
		String nombre = cuenta.getNombreTitularPrincipal();
		// cuit
		String cuit = cuenta.getCuitTitPri();
		// codigo de gestion
		String codGestion = cuenta.getCodGesCue();
		// domicilio de envio
		String domicilio = cuenta.getDesDomEnv() != null ? cuenta.getDesDomEnv() : "";
		// localidad
		String localidad = "";

		// catastral
		String catastral  = (String) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("Catastral");
		// tipo parcela
		Long tipoParcela  = (Long) 	 tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("TipoParcela");
		// radio ref trib
		Long radRefTrib   = (Long) 	 tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("RadRefTrib");
		// ubicacion finca
		String ubicFinca  = (String) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("DomicilioFinca");
		// ubicacion baldio
		String ubicBaldio = (String) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("UbiTerreno");
		// superficie del terreno
		Double supTerreno = (Double) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("SupTerreno");
		// valuacion terreno reforma
		Double valTerrRef = (Double) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("ValTerrenoRef");
		// superficie edificada  
		Double supEdi 	 = (Double) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("SupEdiTerreno");
		// valuacion edificada reforma
		Double valEdifRef = (Double) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("ValEdificadaRef");
		// porcentaje ph
		Double porcPh 	  = (Double) tipObjImpDef.getValorObjectTipObjImpAtrDefinitionByCodigo("PorcPHSubParcela");

		// broche
		Broche broche = cuenta.getBroche();
		// id de broche
		Long idBroche = (broche == null) ? 0L : broche.getId();
		// numero de broche
		Long nroBroche = 0L;
		if (broche == null){
			// NOTA: Utilizamos el idBroche, para ser coeherentes con el sistema de broches actual.
			//       Tal vez en posteriores implementaciones convenga utilizar el repartidor: buscarNroRepartidorPorCatastral()
			nroBroche = asignadorTgi.buscarIdBrochePorCatastral(catastral);
			if (nroBroche == null){
				// si no encontro repartidor, logeo como error.
				//AdpRun.logRun("Falla asignacion Broche para CtaTgi:Catastral " + cuenta.getNumeroCuenta() + ":" + catastral);
				nroBroche = 0L;
			}
		} else {
			// si el broche no es nulo, asigno el repartidor asociado al broche
			// NOTA: En realidad utilizamos el idBroche, para ser coeherentes con el sistema de broches actual.
			//       Tal vez en posteriores implementaciones convenga utilizar el repartidor activo de este broche.
			nroBroche = idBroche;							
		}
		// codigo postal
		String codPostal = cuenta.getDomicilioEnvio().getLocalidad().getCodPostal().toString();
		// descripcion postal
		String desPostal = cuenta.getDomicilioEnvio().getLocalidad().getDescripcionPostal();
		if (nroBroche.equals(900L)) {
			localidad = codPostal + " - " + desPostal;
		} else if (nroBroche.equals(999L)) {
			localidad = "              ";
		} else {
			localidad = "2000 - ROSARIO";
		}

		String caracter  = "";
		String ubicacion = "";
		if (tipoParcela.equals(1L)) {
			caracter  = "FINCA";
			ubicacion = ubicFinca;
		} else {
			caracter  = "BALDIO";
			ubicacion = ubicBaldio;
		}
		
		String numCodBar = Recibo.createNumCodBar(cuenta, deuda.getCodRefPag(), 
				null, deuda.getSistema().getId(), anio, periodo, deuda.getImporte(), 
				deuda.getFechaVencimiento(), false);
		
		String codBar = StringUtil.genCodBarForTxt(numCodBar);

		StringBuilder fileEntry = new StringBuilder();

		fileEntry.append("CAJA01N");
		fileEntry.append(formatString(nombre, 30));
		fileEntry.append(formatString(cuit, 14));
		fileEntry.append(formatString(codGestion, 10));
		fileEntry.append(formatString(domicilio, 30));
		fileEntry.append(formatString(localidad, 32));
		fileEntry.append("\n");

		fileEntry.append("CAJA02N");
		fileEntry.append(formatString(numCuenta, 11));
		fileEntry.append(formatString(catastral, 18));
		fileEntry.append(formatString(ubicacion, 50));
		fileEntry.append(formatString(caracter, 6));
		fileEntry.append(formatString(supTerreno.toString(), 6));
		fileEntry.append(formatString(radRefTrib.toString(), 1));
		fileEntry.append(formatString(nroBroche.toString(), 4));
		fileEntry.append(formatString(valTerrRef.toString(), 9));
		fileEntry.append(formatString(supEdi.toString(), 6));
		fileEntry.append(formatString(valEdifRef.toString(), 9));
		fileEntry.append(formatString(porcPh.toString(), 5));
		fileEntry.append("\n");

		fileEntry.append("CAJA03N");
		fileEntry.append(formatString(periodo + "/" + anio, 7));
		fileEntry.append(formatString(deuda.getImporte().toString(), 13));
		fileEntry.append(formatString("", 20));
		fileEntry.append(formatString("", 13));
		fileEntry.append(formatString(deuda.getFechaVencimiento().toString(), 10));
		fileEntry.append(formatString(deuda.getImporteBruto().toString(), 13));
		
		fileEntry.append("CAJA04N");
		fileEntry.append(formatString(numCodBar,50));

		fileEntry.append("CAJA05N");
		fileEntry.append(formatString(codBar, 122));
		
		fileEntry.append("CAJA07N");
		fileEntry.append(formatString(numCodBar,50));

		fileEntry.append("CAJA08N");
		fileEntry.append(formatString(codBar, 122));

		
		// Guardamos la entrada en el archivo
		this.fileWriter.write(fileEntry.toString());
	}

	
	private void createEncabezado()  throws IOException {
		this.fileWriter.write("%!\n");
		this.fileWriter.write("(tgi4.jdt)STARTLM\n");
	}
	
	public static String formatString(String s, int size) {
		return StringUtil.fillWithBlanksToRight(s, size);
	}

}


