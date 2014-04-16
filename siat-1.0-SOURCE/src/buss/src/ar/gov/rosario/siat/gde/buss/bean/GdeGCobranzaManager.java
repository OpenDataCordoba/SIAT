//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.ef.buss.bean.DetAju;
import ar.gov.rosario.siat.ef.buss.bean.DetAjuDet;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.buss.bean.TipoOrden;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CobranzaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.ConAtrVal;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Manejador del m&oacute;dulo Gde y submodulo GDeudaAuto
 * 
 * @author tecso
 *
 */
public class GdeGCobranzaManager {
		
	private static Logger log = Logger.getLogger(GdeGDeudaAutoManager.class);
	
	private static final GdeGCobranzaManager INSTANCE = new GdeGCobranzaManager();
	
	/**
	 * Constructor privado
	 */
	private GdeGCobranzaManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static GdeGCobranzaManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Cobranza
	public Long createCobranzaByOrdenControl(OrdenControl ordenControl) throws Exception {

		Cobranza cobranza = new Cobranza();
		Session session =SiatHibernateUtil.currentSession();
		Contribuyente contribuyente = ordenControl.getContribuyente();
		cobranza.setContribuyente(contribuyente);
		ConAtrVal conAtrVal = contribuyente.getConAtrValVigenteByIdAtributo(Atributo.getByCodigo(Atributo.COD_CER).getId());
		Area area;
		if (conAtrVal != null && conAtrVal.getValor().equals("1") ){
			area = Area.getByCodigo(Area.COD_CER);
		}else{
			area = Area.getByCodigo(Area.COD_COBRANZA_ADMINISTRATIVA);
		}
		cobranza.setArea(area);
		cobranza.setFechaInicio(new Date());
		cobranza.setEstadoCobranza(EstadoCobranza.getById(EstadoCobranza.ID_INICIO));
		cobranza.setOrdenControl(ordenControl);
		
		//se debe cargar el de la resolucion de emision por pantalla
		//cobranza.setIdCaso(ordenControl.getIdCaso());

		GdeDAOFactory.getCobranzaDAO().update(cobranza);
		
		session.flush();
		cobranza.setListCobranzaDet(new ArrayList<CobranzaDet>());
		
		
		
		Double importeACobrar=0D;
		
		for (DetAju detAju :ordenControl.getListDetAju()){
			for (DetAjuDet detAjuDet: detAju.getListDetAjuDet()){ 
				
				Double aj = (detAjuDet.getAjuste()!=null)?detAjuDet.getAjuste():0D;
				Double comp = (detAjuDet.getCompensacion()!=null)?detAjuDet.getCompensacion():0D;
				
				// Se trunca a dos decimales el valor del ajuste para que se guarde correctamente en la db y no se genere la deuda para ajustes menores a 0.01 (Mantis 5017)
				Double totAjuste = NumberUtil.truncate(aj - comp, SiatParam.DEC_IMPORTE_DB);
				
				Double pago = (detAjuDet.getPago()!=null)?detAjuDet.getPago():0D;
				Double retencion = (detAjuDet.getRetencion()!=null)?detAjuDet.getRetencion():0D;
				
				//no se verifica mas el saldo, cobranza quiere ver todos los periodos
				//if (totAjuste > 0){
					CobranzaDet cobranzaDet = new CobranzaDet();
					cobranzaDet.setCobranza(cobranza);
					cobranzaDet.setPeriodo(detAjuDet.getPeriodoOrden().getPeriodo());
					cobranzaDet.setAnio(detAjuDet.getPeriodoOrden().getAnio());
					cobranzaDet.setAjuste(totAjuste);
					cobranzaDet.setImporteInicial(pago+retencion);
					cobranzaDet.setCuenta(detAjuDet.getPeriodoOrden().getOrdConCue().getCuenta());
					cobranzaDet.setEstadoAjuste(EstadoAjuste.getById(EstadoAjuste.ID_ESTADO_NOEMITIDO));
					cobranzaDet.setDetAjuDet(detAjuDet);
					cobranza.getListCobranzaDet().add(cobranzaDet);
					GdeDAOFactory.getCobranzaDetDAO().update(cobranzaDet);
					session.flush();
					
					//Verifico los pagos ya efectuados para saber el total a cobrar
					Cuenta cuenta = cobranzaDet.getCuenta();
					List<DeudaAdmin>listDeudaAdmin = cuenta.getListDeudaAdminByAnioAndPeriodo(cobranzaDet.getAnio(), cobranzaDet.getPeriodo());
					
					Double pagado=0D;
					for (DeudaAdmin deudaAdmin: listDeudaAdmin){
						List<PagoDeuda>listPagoDeuda = PagoDeuda.getByDeuda(deudaAdmin.getId());
						for (PagoDeuda pagoDeuda:listPagoDeuda){
							pagado += pagoDeuda.getImporte();
						}
					}
					if (totAjuste > 0)
						importeACobrar+= (cobranzaDet.getAjuste()+cobranzaDet.getImporteInicial()-pagado);
				//}
			}
			
		}
		cobranza.setImporteACobrar(importeACobrar);
		return GdeDAOFactory.getCobranzaDAO().update(cobranza);
	}
	
	
	public void updateCobranza(Cobranza cobranza) throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		
		//se debe cargar el de la resolucion de emision por pantalla
		//cobranza.setIdCaso(ordenControl.getIdCaso());

		GdeDAOFactory.getCobranzaDAO().update(cobranza);
		
		session.flush();
		cobranza.setListCobranzaDet(new ArrayList<CobranzaDet>());
		
		
		
		Double importeACobrar=0D;
		
		for (DetAju detAju :cobranza.getOrdenControl().getListDetAju()){
			for (DetAjuDet detAjuDet: detAju.getListDetAjuDet()){ 
				
				Double aj = (detAjuDet.getAjuste()!=null)?detAjuDet.getAjuste():0D;
				Double comp = (detAjuDet.getCompensacion()!=null)?detAjuDet.getCompensacion():0D;
				
				Double totAjuste = aj - comp;
				
				Double pago = (detAjuDet.getPago()!=null)?detAjuDet.getPago():0D;
				Double retencion = (detAjuDet.getRetencion()!=null)?detAjuDet.getRetencion():0D;
				
				//no se verifica mas el saldo, cobranza quiere ver todos los periodos
				//if (totAjuste > 0){
					CobranzaDet cobranzaDet=null;
					
					cobranzaDet = CobranzaDet.getByDetAjuDetAndCobranza(detAjuDet,cobranza);
					
					boolean cambiaValor=false;
					if (cobranzaDet == null){
						cobranzaDet= new CobranzaDet();
						cobranzaDet.setCobranza(cobranza);
						cobranzaDet.setPeriodo(detAjuDet.getPeriodoOrden().getPeriodo());
						cobranzaDet.setAnio(detAjuDet.getPeriodoOrden().getAnio());
						cobranzaDet.setCuenta(detAjuDet.getPeriodoOrden().getOrdConCue().getCuenta());
						cobranzaDet.setEstadoAjuste(EstadoAjuste.getById(EstadoAjuste.ID_ESTADO_NOEMITIDO));
						cobranzaDet.setDetAjuDet(detAjuDet);
					}else{
						if (cobranzaDet.getAjuste()!=null && cobranzaDet.getAjuste()!=totAjuste && cobranzaDet.getIdDeuda()!=null)
							cambiaValor=true;
					}
					
					
					cobranzaDet.setAjuste(totAjuste);
					cobranzaDet.setImporteInicial(pago+retencion);
					
					if (cambiaValor)
						cobranzaDet.setEstadoAjuste(EstadoAjuste.getById(EstadoAjuste.ID_ESTADO_MODIF_FISC));
						
					GdeDAOFactory.getCobranzaDetDAO().update(cobranzaDet);
					session.flush();
					
					//Verifico los pagos ya efectuados para saber el total a cobrar
					Cuenta cuenta = cobranzaDet.getCuenta();
					List<DeudaAdmin>listDeudaAdmin = cuenta.getListDeudaAdminByAnioAndPeriodo(cobranzaDet.getAnio(), cobranzaDet.getPeriodo());
					
					Double pagado=0D;
					for (DeudaAdmin deudaAdmin: listDeudaAdmin){
						List<PagoDeuda>listPagoDeuda = PagoDeuda.getByDeuda(deudaAdmin.getId());
						for (PagoDeuda pagoDeuda:listPagoDeuda){
							pagado += pagoDeuda.getImporte();
						}
					}
					if (totAjuste > 0)
						importeACobrar+= (cobranzaDet.getAjuste()+cobranzaDet.getImporteInicial()-pagado);
				//}
			}
			
		}
		cobranza.setImporteACobrar(importeACobrar);
		GdeDAOFactory.getCobranzaDAO().update(cobranza);

	}
	
	public void updateCobranzaByOrdenControl(Cobranza cobranza, OrdenControl ordenControl) throws Exception {

		Session session =SiatHibernateUtil.currentSession();
		Contribuyente contribuyente = ordenControl.getContribuyente();
		cobranza.setContribuyente(contribuyente);
		ConAtrVal conAtrVal = contribuyente.getConAtrValVigenteByIdAtributo(Atributo.getByCodigo(Atributo.COD_CER).getId());
		Area area;
		if (conAtrVal != null && conAtrVal.getValor().equals("1") ){
			area = Area.getByCodigo(Area.COD_CER);
		}else{
			area = Area.getByCodigo(Area.COD_COBRANZA_ADMINISTRATIVA);
		}
		cobranza.setArea(area);
		cobranza.setFechaInicio(new Date());
		cobranza.setEstadoCobranza(EstadoCobranza.getById(EstadoCobranza.ID_INICIO));
		cobranza.setOrdenControl(ordenControl);
		
		//se debe cargar el de la resolucion de emision por pantalla
		//cobranza.setIdCaso(ordenControl.getIdCaso());

		GdeDAOFactory.getCobranzaDAO().update(cobranza);
		
		session.flush();
		cobranza.setListCobranzaDet(new ArrayList<CobranzaDet>());
		
		
		
		Double importeACobrar=0D;
		
		for (DetAju detAju :ordenControl.getListDetAju()){
			for (DetAjuDet detAjuDet: detAju.getListDetAjuDet()){ 
				
				Double aj = (detAjuDet.getAjuste()!=null)?detAjuDet.getAjuste():0D;
				Double comp = (detAjuDet.getCompensacion()!=null)?detAjuDet.getCompensacion():0D;
				
				Double totAjuste = aj - comp;
				
				Double pago = (detAjuDet.getPago()!=null)?detAjuDet.getPago():0D;
				Double retencion = (detAjuDet.getRetencion()!=null)?detAjuDet.getRetencion():0D;
				
				//no se verifica mas el saldo, cobranza quiere ver todos los periodos
				//if (totAjuste > 0){
					CobranzaDet cobranzaDet = new CobranzaDet();
					cobranzaDet.setCobranza(cobranza);
					cobranzaDet.setPeriodo(detAjuDet.getPeriodoOrden().getPeriodo());
					cobranzaDet.setAnio(detAjuDet.getPeriodoOrden().getAnio());
					cobranzaDet.setAjuste(totAjuste);
					cobranzaDet.setImporteInicial(pago+retencion);
					cobranzaDet.setCuenta(detAjuDet.getPeriodoOrden().getOrdConCue().getCuenta());
					cobranzaDet.setEstadoAjuste(EstadoAjuste.getById(EstadoAjuste.ID_ESTADO_NOEMITIDO));
					cobranzaDet.setDetAjuDet(detAjuDet);
					cobranza.getListCobranzaDet().add(cobranzaDet);
					GdeDAOFactory.getCobranzaDetDAO().update(cobranzaDet);
					session.flush();
					
					//Verifico los pagos ya efectuados para saber el total a cobrar
					Cuenta cuenta = cobranzaDet.getCuenta();
					List<DeudaAdmin>listDeudaAdmin = cuenta.getListDeudaAdminByAnioAndPeriodo(cobranzaDet.getAnio(), cobranzaDet.getPeriodo());
					
					Double pagado=0D;
					for (DeudaAdmin deudaAdmin: listDeudaAdmin){
						List<PagoDeuda>listPagoDeuda = PagoDeuda.getByDeuda(deudaAdmin.getId());
						for (PagoDeuda pagoDeuda:listPagoDeuda){
							pagado += pagoDeuda.getImporte();
						}
					}
					if (totAjuste > 0)
						importeACobrar+= (cobranzaDet.getAjuste()+cobranzaDet.getImporteInicial()-pagado);
				//}
			}
			
		}
		cobranza.setImporteACobrar(importeACobrar);
		GdeDAOFactory.getCobranzaDAO().update(cobranza);

	}
	
	
	public CobranzaVO aplicarAjustes(CobranzaVO cobranzaVO) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Cobranza cobranza = Cobranza.getById(cobranzaVO.getId());
		
		boolean esVerificacion=false;
		
		if(cobranza.getOrdenControl().getTipoOrden().getId().longValue()==TipoOrden.ID_VERIFICACION.longValue()){
			esVerificacion=true;
		}
		
		if (cobranzaVO.getFechaResolucion()==null && !esVerificacion){
			cobranzaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.COBRANZA_FECHARESOLUCION_LABEL);
		}
		
		if(StringUtil.isNullOrEmpty(cobranzaVO.getCaso().getIdFormateado()) && !esVerificacion){
			cobranzaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.CASO_LABEL);
		}
		
		if(cobranzaVO.hasError()){
			return cobranzaVO;
		}
		
		CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(cobranzaVO.getCaso().getIdFormateado());
		
		cobranza.setFechaResolucion(cobranzaVO.getFechaResolucion());
		cobranza.setIdCaso(caso.getIdFormateado());
		
		GdeDAOFactory.getCobranzaDAO().update(cobranza);
		
		List<Deuda>listDeudaAnular = null;
		
		for (CobranzaDet cobranzaDet:cobranza.getListCobranzaDet()){
			
			//Creo los ajustes no emitidos
			if (cobranzaDet.getAjuste()>0D && cobranzaDet.getEstadoAjuste().getId().longValue()==EstadoAjuste.ID_ESTADO_NOEMITIDO.longValue()){
				
				DeudaAdmin deudaAdmin = new DeudaAdmin();
				
				if(!esVerificacion)
					deudaAdmin.setStrEstadoDeuda("Exp. "+ caso.getNumero());
				
				deudaAdmin.setRecurso(cobranzaDet.getCuenta().getRecurso());
		        deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		        deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		        deudaAdmin.setFechaEmision(new Date());
		        deudaAdmin.setEstaImpresa(SiNo.NO.getId());
		        deudaAdmin.setSistema(Sistema.getSistemaEmision(cobranzaDet.getCuenta().getRecurso()));           
		        deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
		        Long a= new Long(cobranzaDet.getAnio());
		        deudaAdmin.setAnio(a);
		        
		        Long periodo= new Long(cobranzaDet.getPeriodo());
		        deudaAdmin.setPeriodo(periodo);
		        deudaAdmin.setCuenta(cobranzaDet.getCuenta());
		        
		        //Busco el periodo original para obtener la fecha de vencimiento
		        Deuda deudaOrig = Deuda.getPerOriByCuentaPeriodoAnio(cobranzaDet.getCuenta(), deudaAdmin.getPeriodo(), deudaAdmin.getAnio().intValue());
		        
		        Date fechaVencimiento;
				if (deudaOrig != null){
					fechaVencimiento=deudaOrig.getFechaVencimiento();
				}else{
					Contribuyente contribuyente = cobranza.getContribuyente();
					if (contribuyente.getNroIsib()!=null)
						if (contribuyente.getNroIsib().indexOf("900")==0)
							fechaVencimiento = DateUtil.getDate("15/"+cobranzaDet.getPeriodo()+"/"+cobranzaDet.getAnio(), DateUtil.ddSMMSYYYY_MASK);
						else
							fechaVencimiento = DateUtil.getDate("10/"+cobranzaDet.getPeriodo()+"/"+cobranzaDet.getAnio(), DateUtil.ddSMMSYYYY_MASK);
					else
						fechaVencimiento = DateUtil.getDate("10/"+cobranzaDet.getPeriodo()+"/"+cobranzaDet.getAnio(), DateUtil.ddSMMSYYYY_MASK);
					fechaVencimiento=Feriado.nextDiaHabil(fechaVencimiento);
				}
		        
		        deudaAdmin.setFechaVencimiento(fechaVencimiento);
		        
		        RecClaDeu recClaDeu = null;
		        
		        if (cobranzaDet.getCuenta().getRecurso().equals(Recurso.getDReI())){
		        	if (!esVerificacion)
		        		recClaDeu=RecClaDeu.getById(RecClaDeu.ID_AJUSTE_FISCAL_DREI);
		        	else
		        		recClaDeu=RecClaDeu.getById(RecClaDeu.ID_AJUSTE_VERIFICADO_DREI);
		        }
		        	
		        if (cobranzaDet.getCuenta().getRecurso().equals(Recurso.getETur())){
		        	if(!esVerificacion)
		        		recClaDeu=RecClaDeu.getById(RecClaDeu.ID_AJUSTE_FISCAL_ETUR);
		        	else
		        		recClaDeu=RecClaDeu.getById(RecClaDeu.ID_AJUSTE_VERIFICADO_ETUR);
		        }
		        
		        deudaAdmin.setRecClaDeu(recClaDeu);
		        deudaAdmin.setReclamada(SiNo.NO.getId());
		        deudaAdmin.setResto(2L);  // Se graba con resto distinto de cero para evitar problemas de Asentamiento de la Deuda Original migrada. (Fix Mantis 5077)
		        deudaAdmin.setEstado(Estado.ACTIVO.getId());
		        deudaAdmin.setImporte(cobranzaDet.getAjuste());
		        deudaAdmin.setImporteBruto(cobranzaDet.getAjuste());
		        deudaAdmin.setSaldo(cobranzaDet.getAjuste());
		        
		        List<DeuAdmRecCon> listRecCon = new ArrayList<DeuAdmRecCon>();
		        for (RecCon recCon : cobranzaDet.getCuenta().getRecurso().getListRecCon()){
		        	DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
		        	deuAdmRecCon.setImporte(cobranzaDet.getAjuste()*recCon.getPorcentaje());
		        	deuAdmRecCon.setRecCon(recCon);
		        	deuAdmRecCon.setDeuda(deudaAdmin);
		        	deuAdmRecCon.setSaldo(deudaAdmin.getImporte()*recCon.getPorcentaje());
		        	deuAdmRecCon.setImporteBruto(deudaAdmin.getImporte()*recCon.getPorcentaje());
		        	listRecCon.add(deuAdmRecCon);
		        }
		        
		        deudaAdmin = GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdmin,listRecCon);
		        
				cobranzaDet.setIdDeuda(deudaAdmin.getId());
				DetAjuDet detAjuDet = cobranzaDet.getDetAjuDet();
				
				detAjuDet.setIdDeuda(deudaAdmin.getId());
				
				cobranzaDet.setEstadoAjuste(EstadoAjuste.getById(EstadoAjuste.ID_ESTADO_EMITIDO));
				
				EfDAOFactory.getDetAjuDetDAO().update(detAjuDet);
				
				deudaAdmin.passErrorMessages(cobranzaVO);
			}
			
			//Modifico los valores ya emitidos para una modificacion
			if(cobranzaDet.getEstadoAjuste().getId().longValue()==EstadoAjuste.ID_ESTADO_MODIF_FISC){
				Deuda deuda = Deuda.getById(cobranzaDet.getIdDeuda());
				if (cobranzaDet.getAjuste()<= 0D && deuda!=null){
					
					if(deuda != null && deuda.getEstadoDeuda().equals(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA))){
						if(listDeudaAnular==null){
							listDeudaAnular=new ArrayList<Deuda>();
						}
						listDeudaAnular.add(deuda);
					}
				}else{
					DeudaAdmin deudaAdmin = DeudaAdmin.getById(deuda.getId());
					deudaAdmin.setImporteBruto(cobranzaDet.getAjuste());
					deudaAdmin.setImporte(cobranzaDet.getAjuste());
					deudaAdmin.setSaldo(cobranzaDet.getAjuste());
					deudaAdmin.setStrEstadoDeuda("Exp. "+ caso.getNumero());
					
					List<DeuAdmRecCon>listDeuAdmRecCon = GdeDAOFactory.getDeuAdmRecConDAO().getListByDeudaAdmin(deudaAdmin);
					for (DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
						deuAdmRecCon.setImporte(cobranzaDet.getAjuste()*deuAdmRecCon.getRecCon().getPorcentaje());
						deuAdmRecCon.setImporteBruto(cobranzaDet.getAjuste()*deuAdmRecCon.getRecCon().getPorcentaje());
						deuAdmRecCon.setSaldo(cobranzaDet.getAjuste()*deuAdmRecCon.getRecCon().getPorcentaje());
						GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
					}
					
					GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);
				}
				cobranzaDet.setEstadoAjuste(EstadoAjuste.getById(EstadoAjuste.ID_ESTADO_EMITIDO));
			}
			
			GdeDAOFactory.getCobranzaDetDAO().update(cobranzaDet);
		}
		
		if(listDeudaAnular!=null){
			
				for (Deuda deu:listDeudaAnular){
					Anulacion anulacionDeuda = new Anulacion();
					anulacionDeuda.setIdDeuda(deu.getId());
					anulacionDeuda.setFechaAnulacion(new Date());
					anulacionDeuda.setMotAnuDeu(MotAnuDeu.getById(MotAnuDeu.ID_ANULACION));
					anulacionDeuda.setRecurso(deu.getRecurso());
					anulacionDeuda.setViaDeuda(deu.getViaDeuda());
					anulacionDeuda.setObservacion("Anulada por modificacion en el calculo de ajuste de fiscalizacion");
					
				 	GdeDAOFactory.getDeudaAnuladaDAO().update(anulacionDeuda);
				 	
					GdeGDeudaManager.getInstance().anularDeuda(anulacionDeuda, deu, null);
				}

		}
		
		GesCob gesCob = new GesCob();
		if (!cobranzaVO.hasError()){
			gesCob.setCobranza(cobranza);
			gesCob.setFecha(new Date());
			gesCob.setEstadoCobranza(cobranza.getEstadoCobranza());
			gesCob.setObservacion("Ajustes emitidos según resoluci\u00F3n: "+cobranzaVO.getCaso().getNumero()+ ", de Fecha "+DateUtil.formatDate(cobranzaVO.getFechaResolucion(), DateUtil.ddSMMSYYYY_MASK));
			GdeDAOFactory.getGesCobDAO().update(gesCob);
		}
		gesCob.passErrorMessages(cobranzaVO);
		
		return cobranzaVO;
	}
	
}