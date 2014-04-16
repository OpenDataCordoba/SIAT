//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.novobjimp;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.exe.buss.bean.ContribExe;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.EstadoCueExe;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.HisEstCueExe;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.EstCue;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.TipoTitular;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.Estado;

/**
 * LLeva acabo las funcionalidades de Novedad de Titulares.
 * En el CUS de Proceso de Novedades de Objeto Imponibles
 * @author Tecso Coop. Ltda.
 *
 */

public class NovTitular {

	Logger log = Logger.getLogger(NovTitular.class);
	
	public static final int COLSCODTIPOBJIMP = 1; //"Codigo Tipo Objeto Imponible" 
	public static final int COLSCODAREAORIG = 2; //"Codigo Area Origen"
	public static final int COLSFECHAACCION = 3; //"Fecha Accio"
	public static final int COLSCLAVE = 5;//"Clave"
	public static final int COLSNROCUENTA = 6;//"Nro Cuenta"  
	public static final int COLSFECHAVIG = 7; //"Fecha Vigencia"
	public static final int COLSPERSONA = 8; //"Id Persona"
	public static final int COLSTIPOTITULAR = 9; //"Tipo Titular"
	public static final int COLSESPRINCIPAL = 10; //"Es Titular Principal"
	
	//public static final int EXTRADIAS = 2; //"Cantidad de Dias que se suman para marcar cuentas"  
	
	public boolean altaTitular(AdpRun run, Datum datum) throws Exception {

		try {

			String asuSolicitud = "Alta de Titular para Cuenta del Objeto Imponible con Catastral: ";
			String desSolicitud = "Titular dado de Alta: ";
	
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Alta de Titular de Cuenta, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
			
			// Obtenemos el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Alta de Titular de Cuenta: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			asuSolicitud += objImp.getClaveFuncional();
			
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Alta de Titular de Cuenta: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Alta de Titular de Cuenta: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos la Cuenta Principal Activa asociada al ObjImp.
			Cuenta cuenta = objImp.getCuentaPrincipalActiva();
			if(cuenta == null){
				String msg = "Proceso de Novedad, Alta de Titular de Cuenta: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no tiene una Cuenta Principal activa asociada.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
        	if(cuenta.getRecurso().getModiTitCtaPorIface()==0){
				String msg = "Proceso de Novedad, Alta de Titular de Cuenta: El ObjImp con TipObjImp="
					+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
					+" esta asociado al Recurso: "+cuenta.getRecurso().getDesRecurso()
					+" y NO modifica Titulares de Cuenta por Novedad de Objeto Imponible.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;        		
        	}
			
			// Obtenemos el Tipo de Titular.
			Long idTipoTitular = datum.getLong(COLSTIPOTITULAR);
			TipoTitular tipoTitular = TipoTitular.getByIdNull(idTipoTitular);
			if(tipoTitular == null){
				String msg = "Proceso de Novedad, Alta de Titular de Cuenta: El Tipo de Titular con id="
						+idTipoTitular+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Asigna El Titular a la Cuenta
			CuentaTitular cuentaTitular = new CuentaTitular();
        	cuentaTitular.setCuenta(cuenta);
        	Contribuyente contribuyente = new Contribuyente();
        	Persona persona = new Persona();
        	persona.setId(datum.getLong(COLSPERSONA));
        	contribuyente.setPersona(persona);
        	cuentaTitular.setContribuyente(contribuyente);
        	cuentaTitular.setTipoTitular(tipoTitular);
        	int esPrincipal = 0;
        	if(datum.getCols(COLSESPRINCIPAL).equals("S")){
        		esPrincipal = 1;
        	}
        	cuentaTitular.setEsTitularPrincipal(esPrincipal); 
        	cuentaTitular.setFechaDesde(datum.getDate(COLSFECHAVIG));
        	cuentaTitular.setFechaNovedad(datum.getDate(COLSFECHAACCION));
        	cuentaTitular.setEsAltaManual(0);
        	cuentaTitular = cuenta.createCuentaTitular(cuentaTitular);
           	// Marcamos cuando corresponde las cuentas modificando cambioTitularIF
        	// (sobre la cuenta principal, siempre que modiTitCtaPorIface sea true)
        	if(cuenta.getRecurso().getModiTitCtaPorIface()==1){
        			contribuyente.setPersona(Persona.getById(contribuyente.getPersona().getId()));
        			desSolicitud += contribuyente.getPersona().getRepresent() ;
        			desSolicitud += "Fecha Novedad: "+datum.getCols(COLSFECHAACCION);
        			desSolicitud += " Fecha Desde: "+datum.getCols(COLSFECHAVIG);
        			
        			cuenta.solicitudPorCambioTitular(asuSolicitud, desSolicitud);
        	}
        	
        	// Verifico si el contribuyente es sujeto exento
        	ContribExe contribExe = null;
        	contribExe = ContribExe.getVigenteByIdContribuyente(contribuyente.getPersona().getId(), datum.getDate(COLSFECHAVIG));
        	// Si lo es, busco si tiene una exencion y/o broche
        	if(contribExe != null){
        		// ¿Tiene Exencion?
        		if(contribExe.getExencion()!=null){
        			// Buscar si existe una exencion vigente de este tipo para la cuenta 'Ha Lugar' .
        			List<CueExe> listCueExe = CueExe.getListVigentesByIdCuentaIdExencionIdEstado(cuenta.getId(),contribExe.getExencion().getId(), new Date(), EstadoCueExe.ID_HA_LUGAR);
        			// Si existe agregar historico con mismo estado y observacion aclaratoria. 
        			if(!ListUtil.isNullOrEmpty(listCueExe)){
        				// Se crea un Historico de CueExe
        				CueExe cueExe = listCueExe.get(0);
        				HisEstCueExe hisEstCueExe = new HisEstCueExe();
        				hisEstCueExe.setCueExe(cueExe); // Se toma la primera. (En gral no debe encontrar mas de una)
        				hisEstCueExe.setEstadoCueExe(cueExe.getEstadoCueExe());
        				hisEstCueExe.setObservaciones("Novedades de Catastro, Alta de Titular: Contribuyente es Sujeto Exento");
        				hisEstCueExe.setLogCambios("No se modificó el Estado.");
        				hisEstCueExe.setFecha(fechaDesde);
        				ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);
        			// Si no existe se crea.
        			}else{
        				// Se crea una CueExe
        				CueExe cueExe = new CueExe();
        				cueExe.setCuenta(cuenta);
        				cueExe.setExencion(contribExe.getExencion());
        				cueExe.setFechaDesde(datum.getDate(COLSFECHAVIG));
        				cueExe.setTipoSujeto(TipoSujeto.getById(TipoSujeto.ID_NOVEDADES));
        				cueExe.setEstadoCueExe(EstadoCueExe.getById(EstadoCueExe.ID_HA_LUGAR));
        				cueExe.setObservaciones("Novedades de Catastro, Alta de Titular: Contribuyente es Sujeto Exento");
        				cueExe.setFechaHasta(contribExe.getFechaHasta());
        				ExeDAOFactory.getCueExeDAO().update(cueExe);
        				// Se crea el Historico de CueExe
        				HisEstCueExe hisEstCueExe = new HisEstCueExe();
        				hisEstCueExe.setCueExe(cueExe); 
        				hisEstCueExe.setEstadoCueExe(cueExe.getEstadoCueExe());
        				hisEstCueExe.setObservaciones("Novedades de Catastro, Alta de Titular: Contribuyente es Sujeto Exento");
        				hisEstCueExe.setLogCambios("Alta de Exencion.");
        				hisEstCueExe.setFecha(fechaDesde);
        				ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);
        			}
        			
        		}
        		// ¿Tiene Broche?
        		if(contribExe.getBroche()!=null){
        			// Asigno el Broche a la cuenta principal.
        			cuenta.asignarBroche(contribExe.getBroche(), datum.getDate(COLSFECHAVIG), null);
        		}
        	}
        	// Si la cuenta esta en estado CREADO, y se pudo agregar el titular correctamente, se la activa.
        	if(!cuentaTitular.hasError() && cuenta.getEstado().intValue()==Estado.CREADO.getId()){
        		cuenta.setEstado(Estado.ACTIVO.getId());
        		cuenta.setEstCue(EstCue.getById(EstCue.ID_ACTIVO));
        		PadDAOFactory.getCuentaDAO().update(cuenta);
        	}    	
        	
        	if (cuentaTitular.hasError()) {
	           	String msg = "Proceso de Novedad, Alta de Titular de Cuenta, No se pudo dar de Alta. (ver log)";
	           	String msgErr = msg + cuentaTitular.infoString() + "\n";
	           	msgErr += cuentaTitular.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
        	
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Alta de Titular de Cuenta: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			throw new Exception(e);
		}	
		return true;
	}

	// TODO Abrir metodo segun TipObjImp. Ahora esta funcional para 'Parcela', se buscan exenciones para Tgi.
	public boolean bajaTitular(AdpRun run, Datum datum) throws Exception {

		try {

			String asuSolicitud = "Baja de Titular para Cuenta del Objeto Imponible con Catastral: ";
			String desSolicitud = "Titular dado de Baja: ";
		
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Baja de Titular de Cuenta, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}

			// Obtenemos el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Baja de Titular de Cuenta: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			asuSolicitud += objImp.getClaveFuncional();
			
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Baja de Titular de Cuenta: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Baja de Titular de Cuenta: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos la Cuenta Principal Activa asociada al ObjImp.
			Cuenta cuenta = objImp.getCuentaPrincipalActiva();
			if(cuenta == null){
				String msg = "Proceso de Novedad, Baja de Titular de Cuenta: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no tiene una Cuenta Principal activa asociada.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos el Titular.
			Long idTitular = datum.getLong(COLSPERSONA);
			Contribuyente contribuyente = Contribuyente.getByIdNull(idTitular);
			if(contribuyente == null){
				String msg = "Proceso de Novedad, Baja de Titular de Cuenta: El Titular con id="
						+idTitular+" no es un contribuyente en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
				
			// Baja de la Relacion Titular Cuenta
			cuenta.desactivarCuentaTitular(contribuyente, datum.getDate(COLSFECHAVIG));
			
			// Verificamos si se pudo desactivar la Relacion Cuenta Titular antes de seguir.
			if (cuenta.hasError()) {
	          	String msg = "Proceso de Novedad, Baja de Titular de Cuenta, No se pudo dar de Baja. (ver log)";
	           	String msgErr = msg + cuenta.infoString() + "\n";
	           	msgErr += cuenta.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);		
	        	return false;
			} 
			
        	// Marcamos cuando corresponde las cuentas modificando cambioTitularIF
        	// (sobre la cuenta principal, siempre que modiTitCtaPorIface sea true)
        	if(cuenta.getRecurso().getModiTitCtaPorIface()==1){
        			contribuyente.setPersona(Persona.getById(contribuyente.getPersona().getId()));
        			desSolicitud += contribuyente.getPersona().getRepresent() ;
        			desSolicitud += " Fecha Novedad: "+datum.getCols(COLSFECHAACCION);
        			desSolicitud += " Fecha Hasta: "+datum.getCols(COLSFECHAVIG);
        			
        			cuenta.solicitudPorCambioTitular(asuSolicitud, desSolicitud);
        	}
        	
        	// Verifico si el contribuyente es sujeto exento
        	ContribExe contribExe = null;
        	contribExe = ContribExe.getVigenteByIdContribuyente(contribuyente.getId(), datum.getDate(COLSFECHAVIG));
        	// Si lo es, busco si tiene una exencion y/o broche
        	if(contribExe != null){
        		desSolicitud += " (Titular es Sujeto Exento)";
        		// ¿Tiene Exencion?
        		if(contribExe.getExencion()!=null){
        			// Buscar si existe una exencion vigente de este tipo para la cuenta 'Ha Lugar' .
        			List<CueExe> listCueExe = CueExe.getListVigentesByIdCuentaIdExencionIdEstado(cuenta.getId(),contribExe.getExencion().getId(), new Date(), EstadoCueExe.ID_HA_LUGAR);
        			// Si existe  se revoca.  
        			if(!ListUtil.isNullOrEmpty(listCueExe)){
        				// Se cambia el estado de CueExe
        				CueExe cueExe = listCueExe.get(0); // Se toma la primera. (En gral no debe encontrar mas de una)
        				cueExe.setEstadoCueExe(EstadoCueExe.getById(EstadoCueExe.ID_REVOCADA));
        				ExeDAOFactory.getCueExeDAO().update(cueExe);
        				
        				// Se crea un Historico de CueExe
        				HisEstCueExe hisEstCueExe = new HisEstCueExe();
        				hisEstCueExe.setCueExe(cueExe); 
        				hisEstCueExe.setEstadoCueExe(cueExe.getEstadoCueExe());
        				hisEstCueExe.setObservaciones("Novedades de Catastro, Baja de Titular: Contribuyente es Sujeto Exento");
        				hisEstCueExe.setLogCambios("Se modificó el Estado. Fecha de Cambio: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));
        				hisEstCueExe.setFecha(fechaDesde);
        				ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);
        			}
        			// (Antes se mandaba solicitud, se dejo el codigo comentado)
        			// Creamos una solicitud de verificar exencion para la cuenta principal.
        			/*CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
        					,asuSolicitud,desSolicitud,cuenta);*/
        		}
        		// ¿Tiene Broche?
        		if(contribExe.getBroche()!=null){
        			// Creamos una solicituda de asignacion de broche a cuenta para la cuenta principal.        			
        			CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_ASIGNACION_BROCHE_CUENTA, 
        							asuSolicitud, desSolicitud, cuenta);        			
        		}
        	}
        	
        	// Buscamos si tiene alguna Exencion de Caso Social o Jubilado.
        	// Si tiene la revocamos.
        	List<CueExe> listCueExeCS = CueExe.getListVigentesByIdCuentaIdExencionIdEstado(cuenta.getId(), Exencion.ID_EXENCION_CASO_SOCIAL, new Date(), EstadoCueExe.ID_HA_LUGAR);
        	if(!ListUtil.isNullOrEmpty(listCueExeCS)){
        		// Se cambia el estado de CueExe
				CueExe cueExe = listCueExeCS.get(0); // Se toma la primera. (En gral no debe encontrar mas de una)
				cueExe.setEstadoCueExe(EstadoCueExe.getById(EstadoCueExe.ID_REVOCADA));
				ExeDAOFactory.getCueExeDAO().update(cueExe);
				
				// Se crea un Historico de CueExe
				HisEstCueExe hisEstCueExe = new HisEstCueExe();
				hisEstCueExe.setCueExe(cueExe); 
				hisEstCueExe.setEstadoCueExe(cueExe.getEstadoCueExe());
				hisEstCueExe.setObservaciones("Novedades de Catastro, Baja de Titular.");
				hisEstCueExe.setLogCambios("Se modificó el Estado. Fecha de Cambio: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));
				hisEstCueExe.setFecha(fechaDesde);
				ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);
				// TODO Ver si además mandamos solucitud de revision de caso
        	}
        	List<CueExe> listCueExeJUB = CueExe.getListVigentesByIdCuentaIdExencionIdEstado(cuenta.getId(), Exencion.ID_EXENCION_EXENTO_5_MINIMOS, new Date(), EstadoCueExe.ID_HA_LUGAR);
        	if(!ListUtil.isNullOrEmpty(listCueExeJUB)){
        		// Se cambia el estado de CueExe
				CueExe cueExe = listCueExeJUB.get(0); // Se toma la primera. (En gral no debe encontrar mas de una)
				cueExe.setEstadoCueExe(EstadoCueExe.getById(EstadoCueExe.ID_REVOCADA));
				ExeDAOFactory.getCueExeDAO().update(cueExe);
				
				// Se crea un Historico de CueExe
				HisEstCueExe hisEstCueExe = new HisEstCueExe();
				hisEstCueExe.setCueExe(cueExe); 
				hisEstCueExe.setEstadoCueExe(cueExe.getEstadoCueExe());
				hisEstCueExe.setObservaciones("Novedades de Catastro, Baja de Titular.");
				hisEstCueExe.setLogCambios("Se modificó el Estado. Fecha de Cambio: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));
				hisEstCueExe.setFecha(fechaDesde);
				ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);
				// TODO Ver si además mandamos solucitud de revision de caso
        	}
                	
         	if (cuenta.hasError()) {
	          	String msg = "Proceso de Novedad, Baja de Titular de Cuenta, No se pudo dar de Baja. (ver log)";
	           	String msgErr = msg + cuenta.infoString() + "\n";
	           	msgErr += cuenta.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);		
	           	return false;
			}
         	
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Baja de Titular de Cuenta: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			throw new Exception(e);
		}
		return true;

	}

	public boolean modifTitular(AdpRun run, Datum datum) throws Exception {

		try {

			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
			
			// Obtenemos el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos la Cuenta Principal Activa asociada al ObjImp.
			Cuenta cuenta = objImp.getCuentaPrincipalActiva();
			if(cuenta == null){
				String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no tiene una Cuenta Principal activa asociada.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos el Titular.
			Long idTitular = datum.getLong(COLSPERSONA);
			Contribuyente contribuyente = Contribuyente.getByIdNull(idTitular);
			if(contribuyente == null){
				String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta: El Titular con id="
						+idTitular+" no es un contribuyente en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			// Obtenemos el Tipo de Titular.
			Long idTipoTitular = datum.getLong(COLSTIPOTITULAR);
			TipoTitular tipoTitular = TipoTitular.getByIdNull(idTipoTitular);
			if(tipoTitular == null){
				String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta: El Tipo de Titular con id="
						+idTipoTitular+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}

			// Modificamos la Relacion Titular Cuenta
			CuentaTitular cuentaTitular = cuenta.getCuentaTitularByContribuyente(contribuyente);
		 	cuentaTitular.setTipoTitular(tipoTitular);
		 	int esPrincipal = 0;
        	if(datum.getCols(COLSESPRINCIPAL).equals("S")){
        		esPrincipal = 1;
        	}
        	cuentaTitular.setEsTitularPrincipal(esPrincipal); 
        	cuenta.updateCuentaTitular(cuentaTitular);
			
        	/*
        	// Marcamos cuando corresponde las cuentas modificando cambioTitularIF
        	// (sobre la cuenta principal, siempre que modiTitCtaPorIface sea true)
        	if(cuenta.getRecurso().getModiTitCtaPorIface()==1){
        		Date fechaVigencia = datum.getDate(COLSFECHAVIG); 
        		Date fechaAccion = datum.getDate(COLSFECHAACCION);
        		Date fechaVigMasExtraDias = DateUtil.addDaysToDate(fechaVigencia,EXTRADIAS);
        		Date fechaSistema = new Date();
        		//	Verificamos que fechaVigencia < fechaAccion || fechaVigencia+INTERVALODIAS < fechaSistema
        		if(DateUtil.isDateBefore(fechaVigencia, fechaAccion) || DateUtil.isDateBefore(fechaVigMasExtraDias, fechaSistema)){      			
        			//cuenta.marcarCambioTitularIF();
        		}
        	}*/

           	if (cuenta.hasError()) {
	           	String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta, No se pudo modificar. (ver log)";
	           	String msgErr = msg + cuenta.infoString() + "\n";
	           	msgErr += cuenta.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Modificacion de Titular de Cuenta: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			throw new Exception(e);
		}
		return true;
	}

}
