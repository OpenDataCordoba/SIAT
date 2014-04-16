//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;


import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.LiqConvenioCuentaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.LiqReconfeccionBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJur;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJurRec;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuotaSaldoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.LiqRecibos;
import ar.gov.rosario.siat.gde.iface.model.LiqReconfeccionAdapter;
import ar.gov.rosario.siat.gde.iface.service.IGdeReconfeccionService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeReconfeccionServiceHbmImpl implements IGdeReconfeccionService {

	private Log log = LogFactory.getLog(GdeReconfeccionServiceHbmImpl.class);

	public LiqReconfeccionAdapter getLiqReconfeccionAdapter(UserContext userContext, Long idCuenta, String[] listaIdDeudaEstado, 
			boolean esReconfEspecial, LiqCuentaVO liqCuentaFiltro) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Obtiene el adapter
			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			cuenta.setLiqCuentaFilter(liqCuentaFiltro);
			
			LiqReconfeccionBeanHelper reconfHelper = new LiqReconfeccionBeanHelper(cuenta);	
			LiqReconfeccionAdapter liqReconfeccionAdapter = reconfHelper.getReconfeccion(listaIdDeudaEstado);			
			List<LiqDeudaVO> listLiqDeudaVO = liqReconfeccionAdapter.getListDeuda();

			liqReconfeccionAdapter.setCuentaFilter(liqCuentaFiltro);
			
			boolean perteneceDeduaMismoProcurador = true;

			if (listaIdDeudaEstado != null && listaIdDeudaEstado.length > 0){
				Procurador procPrimerReg = Deuda.getById(listLiqDeudaVO.get(0).getIdDeuda()).getProcurador();
				Long idProcuradorPrimerReg = procPrimerReg!=null?procPrimerReg.getId():null;

				for (LiqDeudaVO liqDeudaVO:listLiqDeudaVO){
					// Valida que las deudas seleccionadas pertenezcan al mismo procurador
					Deuda deuda = Deuda.getById(liqDeudaVO.getIdDeuda());
					Long idProcurador=deuda.getProcurador() != null?deuda.getProcurador().getId():null; 
					if (idProcurador != null && !idProcurador.equals(idProcuradorPrimerReg)){
						perteneceDeduaMismoProcurador = false;
					}
					
					
					// Restriccion sobre la reimpresion/reconfeccion de deuda DRei/ETur a partir de la fecha determinada por los parametros correspondiente.
					// Si el Recurso se encuentra en la lista de los restringidos sólo se permite cuando el saldo es mayor a cero y menor al importe. 
					Date fechaRestriccion = null; 
					String listaIdRecurso = null;
					try{
						 /*
						* Mantis #0005901: Se necesita que los parámetros AnioLimitacionRG, MesLimitacionRG y ListaIdRecurso
						* actúen solamente cuando el usuario es "internet" (perfil "anonimo")
						*/
						if (userContext.getEsAnonimo()) {
						   listaIdRecurso = SiatParam.getString(SiatParam.LISTA_ID_RECURSO);
						   fechaRestriccion = DateUtil.getFirstDatOfMonth(SiatParam.getInteger(SiatParam.MES_LIMITACION_RG)+1, SiatParam.getInteger(SiatParam.ANIO_LIMITACION_RG));
						}
						}catch (Exception e) {}
					String idRecursoStr = "|" + deuda.getRecurso().getId().toString() + "|";
					if (!StringUtil.isNullOrEmpty(listaIdRecurso) && fechaRestriccion != null 
							&& DateUtil.isDateAfterOrEqual(deuda.getFechaVencimiento(), fechaRestriccion)
							&& listaIdRecurso.indexOf(idRecursoStr) >= 0
							&& (deuda.getImporte().doubleValue() == 0 || deuda.getSaldo().doubleValue() == deuda.getImporte().doubleValue())){ 
						liqReconfeccionAdapter.addRecoverableError(GdeError.LIQRECONF_DEUDAS_RESTRINGIDA);
						return liqReconfeccionAdapter;
					}
					
				}

				if (!perteneceDeduaMismoProcurador){
					liqReconfeccionAdapter.addRecoverableError(GdeError.LIQRECONF_DEUDAS_NO_PERMITIDAS);
					return liqReconfeccionAdapter;
				}
			}
			
			//liqReconfeccionAdapter = reconfHelper.getReconfeccion(listaIdDeudaEstado);
			log.debug("esReconfEspecial:"+esReconfEspecial);
			liqReconfeccionAdapter.setEsReconfeccionEspecial(esReconfEspecial);

			return liqReconfeccionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LiqReconfeccionAdapter reconfeccionar(UserContext userContext, LiqReconfeccionAdapter liqReconfeccionAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			Long idCanal=userContext.getIdCanal();
			Canal canal = Canal.getByIdNull(idCanal);
			String usuario = userContext.getUserName();
			
			// Valida que se haya ingresado una fecha, solo para reconf especial)
			if(liqReconfeccionAdapter.isEsReconfeccionEspecial()) {
				boolean valid = true;
				log.debug("fechaVencimiento:" + liqReconfeccionAdapter.getFechaVencimientoEsp());
				if (liqReconfeccionAdapter.getFechaVencimientoEsp() == null) {
					liqReconfeccionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.LIQRECONF_FECHAVENCIMIENTO_ESP);
					valid = false;
				}
				if (liqReconfeccionAdapter.getFechaActualizacionEsp() == null) {
					liqReconfeccionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.LIQRECONF_FECHAACTUALIZACION_ESP);
					valid = false;
				}
				
				if (valid && DateUtil.isDateBefore(liqReconfeccionAdapter.getFechaVencimientoEsp(), new Date())) {
					liqReconfeccionAdapter.addRecoverableError(GdeError.LIQRECONF_FECHAVENCIMIENTOMENOR);
					valid = false;
				}
					
				//validamos que fechavto sea un dia habil
				if (valid && !Feriado.esDiaHabil(liqReconfeccionAdapter.getFechaVencimientoEsp())) {
					liqReconfeccionAdapter.addRecoverableError(GdeError.LIQRECONF_NO_DIAHABIL);
					valid = false;						
				}

				if (!valid) return liqReconfeccionAdapter;
			}
			
			if(liqReconfeccionAdapter.isRecEnBlanco()) {
				if (ModelUtil.isNullOrEmpty(liqReconfeccionAdapter.getDecJur().getTipDecJurRec())){
					liqReconfeccionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.LIQRECONF_TIPO_DECLARACION);
				}else {
					TipDecJurRec tipDecJurRec = TipDecJurRec.getById(liqReconfeccionAdapter.getDecJur().getTipDecJurRec().getId());
					if(tipDecJurRec.getTipDecJur().getId().longValue()==TipDecJur.ID_ORIGINAL){
						for(LiqDeudaVO liqDeuda: liqReconfeccionAdapter.getListDeuda()){
							Deuda deuda = Deuda.getById(liqDeuda.getIdDeuda());
							if((deuda.getImporte().doubleValue() > 0D && deuda.getSaldo().doubleValue() == 0D) || deuda.getConvenio()!=null){
								liqReconfeccionAdapter.addRecoverableError(GdeError.LIQRECONF_TIPO_DECLARACION_INCORRECTO);
								break;
							}
						}
					}
				}
				
				
				if (liqReconfeccionAdapter.hasErrorRecoverable())
					return liqReconfeccionAdapter;
			}

			// llama al healper con el canal, procurador 		
			liqReconfeccionAdapter = LiqReconfeccionBeanHelper.reconfeccionar(liqReconfeccionAdapter, canal, usuario);
            if (liqReconfeccionAdapter.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return liqReconfeccionAdapter;
            } 
            
            // Si existe cuenta y caso, grabamos el uso expediente 
            if (liqReconfeccionAdapter.getCuenta() != null){
	            AccionExp accionExp = AccionExp.getById(AccionExp.ID_RECONFECCION_ESPECIAL);
	            Cuenta cuenta = Cuenta.getById(liqReconfeccionAdapter.getCuenta().getIdCuenta());
	            liqReconfeccionAdapter.getCasoContainer().clearErrorMessages();
	            CasServiceLocator.getCasCasoService().registrarUsoExpediente(
	            		liqReconfeccionAdapter.getCasoContainer(), 
	            		new AccionExp(), accionExp, cuenta, 
	            		liqReconfeccionAdapter.reconfEspInfoString(true) );
	            // Si no pasa la validacion, vuelve a la vista. 
	            if (liqReconfeccionAdapter.getCasoContainer().hasError()){
	            	SiatHibernateUtil.currentSession().getTransaction().rollback();
	            	liqReconfeccionAdapter.getCasoContainer().passErrorMessages(liqReconfeccionAdapter);
	            	return liqReconfeccionAdapter;
	            }
            }
            
            SiatHibernateUtil.currentSession().getTransaction().commit();
            if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
            return liqReconfeccionAdapter;
		} catch (Exception e) {
           	SiatHibernateUtil.currentSession().getTransaction().rollback();
        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LiqReconfeccionAdapter getCuotaSaldo (UserContext userContext, LiqConvenioCuotaSaldoAdapter liqConvenioCuotaSaldoAdapter)throws Exception{
		
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession();

		Convenio convenio = Convenio.getById(liqConvenioCuotaSaldoAdapter.getConvenio().getIdConvenio());
		Long idConvenio = convenio.getId();
		LiqReconfeccionAdapter liqReconfeccionAdapter = new LiqReconfeccionAdapter();
		Integer cuotaDesde = liqConvenioCuotaSaldoAdapter.getCuotaDesde();
		if (cuotaDesde == null){
			liqReconfeccionAdapter.addRecoverableError(GdeError.CONVENIO_CUOTASALDODESDENULA);
		}else{
			if (cuotaDesde > convenio.getCantidadCuotasPlan() || cuotaDesde <=0){
				liqReconfeccionAdapter.addRecoverableError(GdeError.CONVENIO_CUOTASALDODESDEFUERA_RANGO);
			}
		}

		if (liqReconfeccionAdapter.hasErrorRecoverable())return liqReconfeccionAdapter;

		liqConvenioCuotaSaldoAdapter.setTieneCuotasVencidas(LiqConvenioCuentaBeanHelper.getTieneCuotasVencidas(idConvenio, cuotaDesde));

		if (liqConvenioCuotaSaldoAdapter.getTieneCuotasVencidas()){
			liqReconfeccionAdapter.addRecoverableError(GdeError.CUOTASALDO_CUOTASVENCIDAS);
		}

		if (cuotaDesde < convenio.getPlan().getCanMinCuoParCuoSal()){
			liqReconfeccionAdapter.addRecoverableError(GdeError.CUOTASALDO_DESDE_MENOR_MIN_PLAN);
		}

		if (liqReconfeccionAdapter.hasErrorRecoverable())return liqReconfeccionAdapter;

		LiqReconfeccionBeanHelper reconfBeanHelper = new LiqReconfeccionBeanHelper(convenio);
		Long[] idCuotas=new Long[1];
		//cargo algo a la lista solo para reutilizar el metodo para obtener liqReconfeccionAdapter
		idCuotas[0]=convenio.getListConvenioCuota().get(0).getId();

		liqReconfeccionAdapter = reconfBeanHelper.getReconfeccionCuotas(idCuotas);
		liqReconfeccionAdapter.setEsCuotaSaldo(true);
		liqReconfeccionAdapter.setCuotaDesCuoSal(liqConvenioCuotaSaldoAdapter.getCuotaDesde());


		return liqReconfeccionAdapter;

	}

	public LiqReconfeccionAdapter getReconfeccionCuota (UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO)throws Exception{
		Convenio convenio=Convenio.getById(liqFormConvenioAdapterVO.getConvenio().getIdConvenio());
		
		LiqReconfeccionBeanHelper reconfBeanHelper = new LiqReconfeccionBeanHelper(convenio);
		
		Long[] idCuotas=new Long[liqFormConvenioAdapterVO.getListIdCuotaSelected().length];
		int i=0;
		for (String id: liqFormConvenioAdapterVO.getListIdCuotaSelected()){
			idCuotas[i]= Long.valueOf(id).longValue();
			i++;
		}
		return reconfBeanHelper.getReconfeccionCuotas(idCuotas);
	}

	public PrintModel getImprimirRecibos(UserContext userContext, List<LiqReciboVO> listReciboVO) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			//Si es usuario interno, setea el nombre del usuario en la cabecera
			String usuario = "";
			Long idCanal=userContext.getIdCanal();
			if(idCanal.equals(Canal.ID_CANAL_CMD))
				usuario = userContext.getUserName();
			
			// Elegimos el xsl a utilizar
			String codXsl = Recibo.COD_FRM_RECONF_DEUDA_TGI;
			
			if(ServicioBanco.COD_OTROS_TRIBUTOS.equals(listReciboVO.get(0).getServicioBanco().getCodServicioBanco())){
				codXsl = Recibo.COD_FRM_RECIBO_DEUDA;
			}
			
			
			if(listReciboVO.get(0).getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)){
				codXsl= Recibo.COD_FRM_RECONF_DEUDA_DERPUB;
			}
			
			// Se crea el PrintModel que retorna
			PrintModel print = Formulario.getPrintModelForPDF(codXsl);
			print.putCabecera("usuario", usuario);
			print.setData(new LiqRecibos(listReciboVO));
			print.setTopeProfundidad(8);

			return print;		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel getImprimirRecibosCuotas(UserContext userContext, List<LiqReciboVO> listReciboVO) throws Exception {
		// Se obtiene el formulario de recibo de cuota
		PrintModel print = Formulario.getPrintModelForPDF(Recibo.COD_FRM_RECIBO_CUOTA);
		print.setData(new LiqRecibos(listReciboVO));
		print.setTopeProfundidad(3);

		return print;		
	}
	
	public LiqReconfeccionAdapter getLiqReconfeccionAdapterForVolantePagoIntRS(UserContext userContext, Long idCuenta, String[] listaIdDeudaEstado, LiqCuentaVO liqCuentaFiltro) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Obtiene el adapter
			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			cuenta.setLiqCuentaFilter(liqCuentaFiltro);
			
			LiqReconfeccionBeanHelper reconfHelper = new LiqReconfeccionBeanHelper(cuenta);	
			LiqReconfeccionAdapter liqReconfeccionAdapter = reconfHelper.getVolantePagoIntRS(listaIdDeudaEstado);			
			//List<LiqDeudaVO> listLiqDeudaVO = liqReconfeccionAdapter.getListDeuda();
			liqReconfeccionAdapter.setCuentaFilter(liqCuentaFiltro);
			
			liqReconfeccionAdapter.setEsReconfeccionEspecial(false);

			return liqReconfeccionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public LiqReconfeccionAdapter generarVolantePagoIntRS(UserContext userContext, LiqReconfeccionAdapter liqReconfeccionAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
					
			Long idCanal=userContext.getIdCanal();
			Canal canal = Canal.getByIdNull(idCanal);
			String usuario = userContext.getUserName();
			
			// Valida que se haya ingresado una fecha pago	
			log.debug("fechaPago:" + liqReconfeccionAdapter.getFechaPago());
			if (liqReconfeccionAdapter.getFechaPago() == null) {
				liqReconfeccionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.LIQVOLANTEPAGOINTRS_FECHAPAGO);
				return liqReconfeccionAdapter;
			}
			/* TODO Ver si dejamos esta validacion
			//validamos que fechavto sea un dia habil
			if (!Feriado.esDiaHabil(liqReconfeccionAdapter.getFechaPago())) {
				liqReconfeccionAdapter.addRecoverableError(GdeError.LIQVOLANTEPAGOINTRS_NO_DIAHABIL);
				return liqReconfeccionAdapter;						
			}*/
    
			// llama al healper	
			liqReconfeccionAdapter = LiqReconfeccionBeanHelper.generarVolantePagoIntRS(liqReconfeccionAdapter, canal, usuario);
            if (liqReconfeccionAdapter.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return liqReconfeccionAdapter;
            } 
			
            SiatHibernateUtil.currentSession().getTransaction().commit();
            if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
            return liqReconfeccionAdapter;
		} catch (Exception e) {
           	SiatHibernateUtil.currentSession().getTransaction().rollback();
        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

}
