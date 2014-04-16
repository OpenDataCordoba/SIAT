//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.novobjimp;

import java.util.Date;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaRel;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * LLeva acabo las funcionalidades de Novedad de Relaciones de Cuenta.
 * En el CUS de Proceso de Novedades de Objeto Imponibles
 * @author Tecso Coop. Ltda.
 *
 */
public class NovRelacionCuenta {
	
	Logger log = Logger.getLogger(NovRelacionCuenta.class);
	
	public static final int COLSCODTIPOBJIMP = 1; //"Codigo Tipo Objeto Imponible" 
	public static final int COLSCODAREAORIG = 2; //"Codigo Area Origen"
	public static final int COLSFECHAACCION = 3; //"Fecha Accio"
	public static final int COLSCLAVE = 5;//"Clave"
	public static final int COLSCLAVERELACIONADA = 6;//"Clave Relacionada" 
	public static final int COLSFECHAVIG = 7; //"Fecha Vigencia"
	public static final int COLSTIPORELACION = 8; //"Tipo Relacion"
	
	
	public boolean altaRelacion(AdpRun run, Datum datum) throws Exception {
		try {
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Alta de Relacion de Cuentas, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
			
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Alta de Relacion de Cuentas: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Alta de Relacion de Cuentas: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Alta de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos el ObjImp Relacionado de la db.
			ObjImp objImpRel = null;
			objImpRel = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVERELACIONADA));
			if(objImpRel == null){
				String msg = "Proceso de Novedad, Alta de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVERELACIONADA)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos la Cuenta Principal asociada al ObjImp obtenido con Clave igual a la Clave recibida.
			Cuenta cuenta = objImp.getCuentaPrincipal();
			if(cuenta == null){
				String msg = "Proceso de Novedad, Alta de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no tiene una Cuenta Principal asociada.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Obtenemos la Cuenta Principal Relacionada asociada al ObjImp obtenido con con Clave igual a la Clave Relacionada recibida.
			Cuenta cuentaRelacionada = objImpRel.getCuentaPrincipal();
			if(cuentaRelacionada == null){
				String msg = "Proceso de Novedad, Alta de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVERELACIONADA)
						+" no tiene una Cuenta Principal asociada.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}

			// Creamos la Relacion entre las Cuentas
			CuentaRel cuentaRel = new CuentaRel();
        	cuentaRel.setCuentaOrigen(cuenta);
        	cuentaRel.setCuentaDestino(cuentaRelacionada);
           	cuentaRel.setFechaDesde(datum.getDate(COLSFECHAVIG));
        	cuentaRel.setEsVisible(SiNo.SI.getId());
			
        	cuentaRel = cuenta.createCuentaRel(cuentaRel);
			
        	if (cuentaRel.hasError()) {
	          	String msg = "Proceso de Novedad, Alta de Relacion de Cuenta, No se pudo dar de Alta. (ver log)";
	           	String msgErr = msg + cuentaRel.infoString() + "\n";
	           	msgErr += cuentaRel.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);		
	           	return false;
			}
			
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Alta de Relacion de Cuenta: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			throw new Exception(e);
		}	
		return true;
	}

	public boolean bajaRelacion(AdpRun run, Datum datum) throws Exception {
		try {
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Baja de Relacion de Cuentas, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
			
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Baja de Relacion de Cuentas: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Baja de Relacion de Cuentas: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos que el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Baja de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos que el ObjImp Relacionado de la db.
			ObjImp objImpRel = null;
			objImpRel = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVERELACIONADA));
			if(objImpRel == null){
				String msg = "Proceso de Novedad, Baja de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVERELACIONADA)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos la Cuenta Principal asociada al ObjImp obtenido con Clave igual a la Clave recibida.
			Cuenta cuenta = objImp.getCuentaPrincipal();
			if(cuenta == null){
				String msg = "Proceso de Novedad, Baja de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no tiene una Cuenta Principal asociada.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Obtenemos la Cuenta Principal Relacionada asociada al ObjImp obtenido con con Clave igual a la Clave Relacionada recibida.
			Cuenta cuentaRelacionada = objImpRel.getCuentaPrincipal();
			if(cuentaRelacionada == null){
				String msg = "Proceso de Novedad, Baja de Relacion de Cuentas: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVERELACIONADA)
						+" no tiene una Cuenta Principal asociada.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
				
			// Damos de baja a la Relacion de Cuentas.
			cuenta.desactivarCuentaRel(cuentaRelacionada ,datum.getDate(COLSFECHAVIG));
			
			if (cuenta.hasError()) {
	           	String msg = "Proceso de Novedad, Baja de Relacion de Cuenta, No se pudo dar de Baja. (ver log)";
	           	String msgErr = msg + cuenta.infoString() + "\n";
	           	msgErr += cuenta.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);
	           	return false;
			} else {
				String msg = "Proceso de Novedad, Baja de Relacion de Cuenta, Se realizo baja con Exito";
	           	run.changeStateFinOk(msg);
			}
			
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Baja de Relacion de Cuenta: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			throw new Exception(e);
		}
		return true;
	}

	public boolean modifRelacion(AdpRun run, Datum datum) throws Exception {
		String msg = "Error! No se acepta modificación 'M' para la Relación entre Cuentas";
		run.changeStateError(msg);
		return false;
	}
}
