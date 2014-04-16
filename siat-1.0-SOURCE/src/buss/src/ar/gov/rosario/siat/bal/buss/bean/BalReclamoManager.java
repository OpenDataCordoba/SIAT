//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.Date;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.ReclamoVO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.TipoDocumento;
import ar.gov.rosario.siat.pad.buss.bean.VariosWebFacade;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaMail;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TipoBoleta;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Manejador del modulo Bal y submodulo Reclamo
 * 
 * @author tecso
 *
 */
public class BalReclamoManager {
		
	private static Logger log = Logger.getLogger(BalReclamoManager.class);
	
	private static final BalReclamoManager INSTANCE = new BalReclamoManager();
	
	/**
	 * Constructor privado
	 */
	private BalReclamoManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalReclamoManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Reclamo
	public Reclamo createReclamo(Reclamo reclamo) throws Exception {

		// Validaciones de negocio
		if (!reclamo.validateCreate()) {
			return reclamo;
		}

		BalDAOFactory.getReclamoDAO().update(reclamo);

		return reclamo;
	}
	
	public Reclamo updateReclamo(Reclamo reclamo) throws Exception {
		
		// Validaciones de negocio
		if (!reclamo.validateUpdate()) {
			return reclamo;
		}

		BalDAOFactory.getReclamoDAO().update(reclamo);

		//ahora actualizamos el campo reclamada de la deuda o cuota
		if (reclamo.getTipoBoleta().intValue() == TipoBoleta.TIPODEUDA.getId().intValue()) {
			// es deuda!
			Deuda deuda = Deuda.getById(reclamo.getIdElemento());
			deuda.setReclamada(reclamo.getEstadoReclamo().getId().intValue());
		} if (reclamo.getTipoBoleta().intValue() == TipoBoleta.TIPOCUOTA.getId().intValue()) {
			// es cuota!
			ConvenioCuota cuota = ConvenioCuota.getById(reclamo.getIdElemento());
			cuota.setReclamada(reclamo.getEstadoReclamo().getId().intValue());
		}
		
		return reclamo;
	}
	
	public Reclamo deleteReclamo(Reclamo reclamo) throws Exception {
	
		// Validaciones de negocio
		if (!reclamo.validateDelete()) {
			return reclamo;
		}
		
		BalDAOFactory.getReclamoDAO().delete(reclamo);
		
		return reclamo;
	}
	// <--- ABM Reclamo

	public Reclamo createReclamo(LiqReclamoVO reclamoVO) throws Exception {		
		Reclamo reclamo = new Reclamo();
		TipoDocumento tipoDocumento = TipoDocumento.getByIdNull(new Long(reclamoVO.getTipoDoc()));
		Banco bancoReclamo = Banco.getByIdNull(new Long(reclamoVO.getBanco()));
		
		// Validamos en tramitesWeb por algun reclamo ya realizado
		if (!PadServiceLocator.getVariosWebFacade().validateReclamoWeb(reclamoVO)){
			reclamo.addMessageValue("Reclamo ya registrado. No necesita enviarlo nuevamente.");
			//reclamo.addRecoverableValueError("Reclamo ya registrado. No necesita enviarlo nuevamente.");
			return reclamo;
		}
		
		//Datos ingresados por el contribuyente
		reclamo.setFechaPago(reclamoVO.getFechaPago());
		reclamo.setImportePagado(reclamoVO.getImportePagado());
		reclamo.setBanco(bancoReclamo);
		reclamo.setNombre(reclamoVO.getNombre());
		reclamo.setApellido(reclamoVO.getApellido());
		reclamo.setTipoDoc(tipoDocumento.getId());
		reclamo.setNroDoc(new Long(reclamoVO.getNroDoc()));
		reclamo.setCorreoElectronico(reclamoVO.getEmail());
		reclamo.setObservacion(reclamoVO.getObservacion());
		reclamo.setFechaAlta(new Date());
		
		//completamos datos de reclamo segun corresponda deuda o cuota,
		//aqui se carga informacion adicional sobre el reclamo para luego poder realizar informes
		//y busquedas.

		//Datos de Canal, Area y determinacion de EstadoReclamo segun Canal
		UserContext userContext = DemodaUtil.currentUserContext();
		reclamo.setCanal(Canal.getById(userContext.getIdCanal()));
		reclamo.setArea(Area.getById(userContext.getIdArea()));
		reclamo.setUsuarioAlta(userContext.getUserName());

		// determinamos el estado segun el canal del usuario
		EstadoReclamo estadoReclamo;
		if (Canal.ID_CANAL_CMD == reclamo.getCanal().getId()) {
			estadoReclamo = EstadoReclamo.getById(EstadoReclamo.GENERADO_CMD);
		} else {
			estadoReclamo = EstadoReclamo.getById(EstadoReclamo.GENERADO_WEB);
		}
		reclamo.setEstadoReclamo(estadoReclamo);

		//Datos segun tipo de reclamo deuda/cuota
		//ATENTO: dentro de estos if, tambien se actualiza el reclamada deuda o conveniocuota
		//con el valor de estadoReclamo.
		Cuenta cuenta = null;
		if (reclamoVO.getTipoTramite() == VariosWebFacade.COD_RECLAMO_ASENTAMIENTO_DEUDA) {
			Deuda deuda = Deuda.getById(reclamoVO.getIdDeuda());
			cuenta = deuda.getCuenta();
			reclamo.setTipoBoleta(new Long(TipoBoleta.TIPODEUDA.getId()));
			reclamo.setIdElemento(reclamoVO.getIdDeuda());
			reclamo.setPeriodo(deuda.getPeriodo());
			reclamo.setAnio(deuda.getAnio());
			reclamo.setViaDeuda(deuda.getViaDeuda());
			reclamo.setProcurador(deuda.getProcurador());
			reclamo.setSistema(deuda.getSistema());
			
			//actualizamos registro de deuda.
			deuda.setReclamada(reclamo.getEstadoReclamo().getId().intValue());
		} else if (reclamoVO.getTipoTramite() == VariosWebFacade.COD_RECLAMO_ASENTAMIENTO_CUOTA) {
			ConvenioCuota cuota = ConvenioCuota.getById(reclamoVO.getIdCuota());
			cuenta = cuota.getConvenio().getCuenta();
			reclamo.setTipoBoleta(new Long(TipoBoleta.TIPOCUOTA.getId()));
			reclamo.setIdElemento(reclamoVO.getIdCuota());
			reclamo.setNroCuota(new Long(cuota.getNumeroCuota()));
			reclamo.setNroConvenio(new Long(cuota.getConvenio().getNroConvenio()));
			reclamo.setViaDeuda(cuota.getConvenio().getViaDeuda());
			reclamo.setProcurador(cuota.getConvenio().getProcurador());
			reclamo.setSistema(cuota.getSistema());
			reclamo.setNroConvenio(new Long(cuota.getConvenio().getNroConvenio()));
			reclamo.setNroCuota(new Long(cuota.getNumeroCuota()));
			
			//actualizamos registro de ConvenioDeuda.
			cuota.setReclamada(reclamo.getEstadoReclamo().getId().intValue());
		} else {
			reclamo.addRecoverableValueError("Tipo de Reclamo desconocido, solo soporta: Deuda y Cuota.");
			return reclamo;
		}
		
		reclamo.setCuenta(cuenta);
		reclamo.setNroCuenta(new Long(cuenta.getNumeroCuenta()));
		reclamo.setRecurso(cuenta.getRecurso());
		
		// validamos y creamos reclamo en db del siat
		reclamo = createReclamo(reclamo);
		if (reclamo.hasError()) {
			return reclamo;
		}
		
		// Adaptamos un poco los datos y creamos reclamo en varios web
		reclamoVO.setTipoDoc(tipoDocumento.getAbreviatura());
		reclamoVO.setBanco(bancoReclamo.getDesBanco());
		PadServiceLocator.getVariosWebFacade().createReclamoWeb(reclamoVO);
		enviarMailRecAse(reclamoVO.getXmlForEnvioMail());

		return reclamo;
	}
	
	
	/**
	 * Envia el mail de reclamo de asentamiento
	 * @param xmlData String xml que utilizara el formulario para generar el String de salida.
	 */
	private void enviarMailRecAse(String xmlData){
		try {
			// genera el body			
			log.debug("enviarMailRecAse - enter: param: "+xmlData);
			
			PrintModel pm = Formulario.getPrintModel(Formulario.COD_FRM_MAIL_ASE, FormatoSalida.TXT);
			pm.setXmlData(xmlData);
			
			String body = new String(pm.getByteArray());
			
			String server = SiatParam.getString(SiatParam.MAIL_SERVER);
			String to = SiatParam.getString("MailTORecAse");//lotero0@rosario.gov.ar
			String from = SiatParam.getString("MailFROMRecAse");
			String subject = SiatParam.getString("SubjectMailRecAse");
			String cc = "";
			
			DemodaMail.send(server, to, from, subject, body, cc);
			log.debug("El mail de reclamo de asentamiento ha sido enviado con exito");
		} catch (Exception e) {
			log.error("Se produjo un error enviando el mail de reclamo de asentamiento", e);
			//throw new DemodaServiceException(e);
		}
	}

	public String enviarMailRespuestaRecAse(ReclamoVO reclamoVO) {
		// genera el body			
		try {

			String body = reclamoVO.getEncabezadoCorreo() + "\n\n" + reclamoVO.getRespuesta();
			String server = SiatParam.getString(SiatParam.MAIL_SERVER);
			String from = SiatParam.getString("MailFROMRespuestaRecAse");
			String to = reclamoVO.getCorreoElectronico();
			String subject = SiatParam.getString("SubjectMailRespuestaRecAse");
			String cc = "";

			DemodaMail.send(server, to, from, subject, body, cc);
			return "OK";
		} catch (Exception e) {
			log.error("Se produjo un error enviando el mail de reclamo de asentamiento", e);
			return e.toString();
		}
	}
}
