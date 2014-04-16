//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Map;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqNotificacionPrinter {

	private static Logger log = Logger.getLogger(LiqNotificacionPrinter.class);

	public static String format(LiqNotificacionVO liqNotif,
			Map<String, String> parameters) {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		StringBuilder fileContent = new StringBuilder();
		
		String titulo = parameters.get("titulo");
		fileContent.append("IDECAJA01N" + fillWithBlanks(titulo, 60) + '\n');

		String titulo2 = parameters.get("titulo2");
		fileContent.append("IDECAJA02N" + fillWithBlanks(titulo2, 60) + '\n');

		String cuentaView = StringUtil.completarCerosIzq(liqNotif.getCuenta().getNroCuentaView(), 10);
		String num = cuentaView.substring(1,8);
		String dv =  cuentaView.substring(8,10);
		String numCuenta = num + '-' + dv;
		fileContent.append("IDECAJA03N" +  numCuenta + '\n');

		String cuitTitPri = liqNotif.getCuenta().getCuitTitularPrincipalContr();
		if (!StringUtil.isNullOrEmpty(cuitTitPri))
			fileContent.append("IDECAJA04N" + fillWithBlanks(cuitTitPri, 13) + '\n');

		LiqAtrValorVO atrUbic = liqNotif.getCuenta().getAtrObjImpByCod("Ubicacion");
		String ubicacion = (atrUbic != null) ? atrUbic.getValue() : "";
		fileContent.append("IDECAJA05N" + fillWithBlanks(ubicacion, 48) + '\n');

                
		if ("TGI".equalsIgnoreCase(liqNotif.getCuenta().getCodRecurso())) {
		        String catastral = liqNotif.getCuenta().getDesClaveFuncional();
			String broche = liqNotif.getCuenta().getDesBroche();
			fileContent.append("IDECAJA06N" + "CATASTRAL: " + fillWithBlanks(catastral + " BROCHE: " + broche, 50) + '\n');
		} else {
		     if ("CDM".equalsIgnoreCase(liqNotif.getCuenta().getCodRecurso())) { 
		        String catastral = liqNotif.getCuenta().getDesClaveFuncional();
			fileContent.append("IDECAJA06N" + "CATASTRAL: " + fillWithBlanks(catastral, 50) + '\n');			
		     } else {
		        LiqAtrValorVO atrCatas = liqNotif.getCuenta().getAtrObjImpByCod("Catastral");
		        String catastral = (atrCatas != null) ? atrCatas.getValue() : "";
			fileContent.append("IDECAJA06N" + "CATASTRAL: " + fillWithBlanks(catastral, 50) + '\n');
	             }
		} 
		
		String nomTitPri = liqNotif.getCuenta().getNombreTitularPrincipal();
		fileContent.append("IDECAJA07N" + fillWithBlanks(nomTitPri, 26) + '\n');

		String desDom = liqNotif.getCuenta().getDesDomEnv();
		fileContent.append("IDECAJA08N" + fillWithBlanks(desDom, 26) + '\n');
		
		String fechaAsentamiento = parameters.get("fechaAsentamiento");
		fileContent.append("IDECAJA09N" + fillWithBlanks(fechaAsentamiento, 10) + '\n');

		String desRecurso = liqNotif.getCuenta().getDesRecurso();
		fileContent.append("IDECAJA10N" + fillWithBlanks(desRecurso, 25) + '\n');

		// Periodos de deuda
		String lineaDeuda1 = liqNotif.getLineaDeuda1();
		String lineaDeuda2 = liqNotif.getLineaDeuda2();
		String lineaDeuda3 = liqNotif.getLineaDeuda3();
		String lineaDeuda4 = liqNotif.getLineaDeuda4();
		
		if (!StringUtil.isNullOrEmpty(lineaDeuda1))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaDeuda1, 65) + '\n');

		if (!StringUtil.isNullOrEmpty(lineaDeuda2))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaDeuda2, 65) + '\n');

		if (!StringUtil.isNullOrEmpty(lineaDeuda3))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaDeuda3, 65) + '\n');

		if (!StringUtil.isNullOrEmpty(lineaDeuda4))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaDeuda4, 65) + '\n');
		
		// Cuotas de Convenios de Pago
		String lineaConvenioCuota1 = liqNotif.getLineaConvenioCuota1();
		String lineaConvenioCuota2 = liqNotif.getLineaConvenioCuota2();
		String lineaConvenioCuota3 = liqNotif.getLineaConvenioCuota3();
		String lineaConvenioCuota4 = liqNotif.getLineaConvenioCuota4();
		
		if (!StringUtil.isNullOrEmpty(lineaConvenioCuota1))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaConvenioCuota1, 65) + '\n');

		if (!StringUtil.isNullOrEmpty(lineaConvenioCuota2))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaConvenioCuota2, 65) + '\n');

		if (!StringUtil.isNullOrEmpty(lineaConvenioCuota3))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaConvenioCuota3, 65) + '\n');

		if (!StringUtil.isNullOrEmpty(lineaConvenioCuota4))
			fileContent.append("IDECAJA11N" + fillWithBlanks(lineaConvenioCuota4, 65) + '\n');

		// Renglones informativos
		for (int i = 1; i <= 12 ; i++) {
			String renglon_inf = parameters.get("renglon_inf" + ((i < 10) ? "0":"") + i);
			if (!StringUtil.isNullOrEmpty(renglon_inf))
				fileContent.append("IDECAJA12N" + fillWithBlanks(renglon_inf, 80) + '\n');
		}

		String resAreaEmisiora = parameters.get("RespAreaEmisora");
		fileContent.append("IDECAJA13N" + fillWithBlanks(resAreaEmisiora, 30) + '\n');

		String areaEmisiora = parameters.get("AreaEmisora");
		fileContent.append("IDECAJA14N" + fillWithBlanks(areaEmisiora, 40) + '\n');

		
		log.debug(fileContent.toString());
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return fileContent.toString();
	}

	private static String fillWithBlanks(String s, int size) {
		return StringUtil.fillWithBlanksToRight(s, size);
	}
}


