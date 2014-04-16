//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.rec;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;


/**
 * Procesa los mensajes de ADP respecto del directorio 
 * Input de novedades de Osiris.
 * @author Tecso Coop. Ltda.
 */
public class NovOsirisWorker implements AdpWorker {

	public void reset(AdpRun adpRun) throws Exception {
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		NovOsiris novOsiris = new NovOsiris();
		novOsiris.getEnvios(adpRun);
	}

	/**
	 * Verifica si existen novedades de osiris por procesar en las tablas grabadas por mulator
	 * 		. Consulta primero la ultima fechaRegistroMulat de la tabla bal_tranAfip (en principio no verificamos el estado, porque suponemos que la fecha de registro depende de 
	 * 		cuando mulator graba la novedad a la tabla)
	 * 		. Con esta fecha buscamos si existen novedades en la tabla 'transaccion' de la base de mulator.
	 *				. si existe al menos un registro, devolver true;
	 *				. si no, false.
	 */
	public boolean validate(AdpRun adpRun) throws Exception {
		//si el paremetro tiene algun valor, evitar la validacion de fechas.
		if (!SiatParam.getString(SiatParam.OBTENERENVIO_ENVIOID, "").equals("")) {
			return true;
		}
		
		Date fechaUltRegistroMulat = BalDAOFactory.getEnvioOsirisDAO().getUltimaFechaRegistroMulat();
		if(fechaUltRegistroMulat == null){
			// Si no encuentra registros en la tabla de envios, verifico de un mes hacia atras.
			fechaUltRegistroMulat= DateUtil.getDate(2009, 11, 1);//DateUtil.addMonthsToDate(new Date(), -1);			
		}
		List<EnvioOsiris> listEnvioOsiris = BalDAOFactory.getMulatorJDBCDAO().getListNuevosEnviosByFechaMayorIgual(fechaUltRegistroMulat);
		if(ListUtil.isNullOrEmpty(listEnvioOsiris)) {
			adpRun.setMensajeEstado("No hay envio que obtener desde Mulator a la fecha: " + DateUtil.formatDate(fechaUltRegistroMulat, DateUtil.ddSMMSYYYY_MASK));
			return false;
		} else {
			return true;
		}
	}

}
