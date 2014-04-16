//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.RecAli;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.RecMinDec;
import ar.gov.rosario.siat.def.buss.bean.RecTipAli;
import ar.gov.rosario.siat.def.buss.bean.RecTipUni;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipRecConADec;
import ar.gov.rosario.siat.def.buss.bean.ValUnRecConADe;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ValUnRecConADeVO;
import ar.gov.rosario.siat.ef.buss.bean.DetAjuDet;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDetVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.AgeRet;
import ar.gov.rosario.siat.gde.buss.bean.Anulacion;
import ar.gov.rosario.siat.gde.buss.bean.CierreComercio;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.buss.bean.DecJurDet;
import ar.gov.rosario.siat.gde.buss.bean.DecJurPag;
import ar.gov.rosario.siat.gde.buss.bean.DescuentoMulta;
import ar.gov.rosario.siat.gde.buss.bean.Desglose;
import ar.gov.rosario.siat.gde.buss.bean.DesgloseDet;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaAutoManager;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import ar.gov.rosario.siat.gde.buss.bean.MotivoCierre;
import ar.gov.rosario.siat.gde.buss.bean.Multa;
import ar.gov.rosario.siat.gde.buss.bean.MultaDet;
import ar.gov.rosario.siat.gde.buss.bean.MultaHistorico;
import ar.gov.rosario.siat.gde.buss.bean.OriDecJur;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.PlanDescuento;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJurRec;
import ar.gov.rosario.siat.gde.buss.bean.TipPagDecJur;
import ar.gov.rosario.siat.gde.buss.bean.TipoMulta;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioAdapter;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioVO;
import ar.gov.rosario.siat.gde.iface.model.DecJurDetVO;
import ar.gov.rosario.siat.gde.iface.model.DecJurPagVO;
import ar.gov.rosario.siat.gde.iface.model.DecJurVO;
import ar.gov.rosario.siat.gde.iface.model.DescuentoMultaVO;
import ar.gov.rosario.siat.gde.iface.model.DesgloseAjusteAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDetalleDecJurVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqRecMinVO;
import ar.gov.rosario.siat.gde.iface.model.MotivoCierreVO;
import ar.gov.rosario.siat.gde.iface.model.MultaAdapter;
import ar.gov.rosario.siat.gde.iface.model.MultaDetVO;
import ar.gov.rosario.siat.gde.iface.model.MultaHistoricoVO;
import ar.gov.rosario.siat.gde.iface.model.MultaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.MultaVO;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaVO;
import ar.gov.rosario.siat.gde.iface.service.IGdeGDeudaAutoService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.EstCue;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.PadObjetoImponibleManager;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeGDeudaAutoServiceHbmImpl implements IGdeGDeudaAutoService {

	private Logger log = Logger.getLogger(GdeGDeudaAutoServiceHbmImpl.class);
	
	public static final String CREADO = "Creado";
	public static final String MODIFICADO = "Modificado";
	public static final Long CANT_DIAS_HABILES_SIGTES = 5L;
	
	/**
	 * Este metodo init recibe un LiqFormConvenioAdapter que siempre llega instanciado por cuestiones de navegacion
	 * de estos CUS en particular.  
	 * 
	 */
	public DesgloseAjusteAdapter getDesgloseAjusteInit(UserContext userContext, DesgloseAjusteAdapter desgloseAjusteAdapterVO) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Cuenta cuenta = Cuenta.getById(desgloseAjusteAdapterVO.getCuenta().getIdCuenta());


		return this.getDesgloseAjusteInit(desgloseAjusteAdapterVO, cuenta);

	}
	/**
	 * idDeudaEstadoDeuda lista de string con la forma "idcuenta-idEstado"
	 * @throws Exception 
	 */
	public DesgloseAjusteAdapter getDesgloseAjusteInit(DesgloseAjusteAdapter desgloseAjusteAdapter,Cuenta cuenta) throws Exception{

		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
		LiqCuentaVO LiqCuentaVO = liqDeudaBeanHelper.getCuenta();
		desgloseAjusteAdapter.setCuenta(LiqCuentaVO);

		// Reset de valores
		desgloseAjusteAdapter.clearErrorMessages();

		desgloseAjusteAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());

		// Validarmos que exista deuda seleccionada.
		if (desgloseAjusteAdapter.getListIdDeudaSelected() == null ||
				desgloseAjusteAdapter.getListIdDeudaSelected().length == 0 ){

			desgloseAjusteAdapter.addRecoverableError(GdeError.MSG_SELECT_DEUDA_REQUERIDO);

			return desgloseAjusteAdapter;
		}

		ViaDeuda viaDeuda = null;
		List<Deuda> listDeudaSeleccionada = new ArrayList<Deuda>();
		List<Long> listIdDeudas=new ArrayList<Long>();
		// Recuperamos la Deuda seleccinada
		for(String idDeudaEstado: desgloseAjusteAdapter.getListIdDeudaSelected()){
			String[] split = idDeudaEstado.split("-");
			long idDeuda = Long.parseLong(split[0]);
			listIdDeudas.add(idDeuda);
			long idEstadoDeuda = Long.parseLong(split[1]);
			Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);

			if ((deuda.getRecClaDeu().getId().longValue()!= RecClaDeu.ID_AJUSTE_FISCAL_DREI)
					&& (deuda.getRecClaDeu().getId().longValue()!=RecClaDeu.ID_AJUSTE_FISCAL_ETUR)){
				desgloseAjusteAdapter.addRecoverableError(GdeError.MSG_DESGLOSE_AJUSTE_CLASIFICACION_DEUDA);
				return desgloseAjusteAdapter;
			}

			listDeudaSeleccionada.add(deuda);

			//	Validamos que toda la deuda seleccionada corresponda a la misma  via.
			// Seteo la via de la primer deuda
			if (viaDeuda == null){
				viaDeuda = deuda.getViaDeuda();

			} 

		}

		List<Long>listIdEnAsentam=Deuda.getListIdDeudaAuxPagDeu(listIdDeudas);
		if(!ListUtil.isNullOrEmpty(listIdEnAsentam)){
			String msg="";
			msg = (listIdEnAsentam.size()>1)?"Los per\u00EDodos ":"El per\u00EDodo ";
			int count=0;
			for (Long id:listIdEnAsentam){
				msg+=(count==0)?"":", ";
				msg+=Deuda.getById(id).getStrPeriodo();
				count++;
			}
			msg += (count>1)?" se encuentran en proceso de asentamiento de pagos":" se encuentra en proceso de asentamiento de pago";
			desgloseAjusteAdapter.addRecoverableValueError(msg);
		}

		UserContext userContext = DemodaUtil.currentUserContext();

		// Validaciones de Vias y permisos.
		// Validamos que solo un procurado pueda formalizar deuda en via judicial 			
		if (userContext.getEsProcurador() && ViaDeuda.ID_VIA_JUDICIAL != viaDeuda.getId().longValue()){
			desgloseAjusteAdapter.addRecoverableError(GdeError.MSG_VIA_DEUDA_NO_PERMITIDA_USR);
			return desgloseAjusteAdapter;
		}

		// Si es procurador y deuda en via judicial, toda la deuda debe pertenecer al mismo.
		if (userContext.getEsProcurador() && ViaDeuda.ID_VIA_JUDICIAL == viaDeuda.getId().longValue()){
			boolean perteneceDeduaProcurador = true;

			for (Deuda deuda:listDeudaSeleccionada){
				if (deuda.getProcurador() == null || deuda.getProcurador().getId() == null ||
						deuda.getProcurador().getId().longValue() != userContext.getIdProcurador().longValue()){
					perteneceDeduaProcurador = false;
				}					
			}

			if (!perteneceDeduaProcurador){
				desgloseAjusteAdapter.addRecoverableError(GdeError.MSG_DEUDA_NO_PERMITIDA_USR);
				return desgloseAjusteAdapter;
			}
		}

		// Si el usuario es operador judicial, toda la deuda debe pertenecer al mismo procurador
		if (userContext.getEsOperadorJudicial() && ViaDeuda.ID_VIA_JUDICIAL == viaDeuda.getId().longValue()){
			boolean perteneceDeduaMismoProcurador = true;

			Long idProcuradorPrimerReg = listDeudaSeleccionada.get(0).getProcurador().getId();

			for (Deuda deuda:listDeudaSeleccionada){
				if (deuda.getProcurador() == null || deuda.getProcurador().getId() == null ||
						deuda.getProcurador().getId().longValue() != idProcuradorPrimerReg.longValue()){
					perteneceDeduaMismoProcurador = false;
				}
			}

			if (!perteneceDeduaMismoProcurador){
				desgloseAjusteAdapter.addRecoverableError(GdeError.MSG_DEUDA_DISTINTO_PROCURADOR);
				return desgloseAjusteAdapter;
			}
		}

		if (userContext.getEsUsuarioCMD() && ViaDeuda.ID_VIA_ADMIN != viaDeuda.getId().longValue()){
			desgloseAjusteAdapter.addRecoverableError(GdeError.MSG_VIA_DEUDA_NO_PERMITIDA_USR);
			return desgloseAjusteAdapter;
		}
		// Fin Validaciones

		// Paso a LiqDeudaVO
		for (Deuda deuda:listDeudaSeleccionada){
			// Obtentemos la deuda actualizada a la fecha de hoy
			LiqDeudaVO liqDeudaVO = getLiqDeudaVOActualizado(deuda, new Date(), null);

			liqDeudaVO.setVerDetalleDeudaEnabled(true);

			desgloseAjusteAdapter.getListDeuda().add(liqDeudaVO);
		}

	// Se le pide al LiqConvenioAdapter que calcule el total historico de la deuda seleccionada.
		desgloseAjusteAdapter.calcularTotalHistorio();

		// Seteo de Permisos

		return desgloseAjusteAdapter;
	}

	/**
	 * Recibe una deuda, una fecha y un planDescuento (que puede venir nulo)
	 * 
	 * Devuelve un LiqDeudaVO listo para mostrar, con la actualizacion calculada y si existen, descuentos aplicados. 
	 * 
	 * @author Cristian
	 * @param deuda
	 * @param fecha
	 * @param planDescuento
	 * @return
	 * @throws Exception
	 */
	private static LiqDeudaVO getLiqDeudaVOActualizado(Deuda deuda, Date fecha, PlanDescuento planDescuento) throws Exception{

		String funcName = DemodaUtil.currentMethodName();

		Double descuentoCapital = 0d;
		Double descuentoActualizacion = 0d;

		if (planDescuento != null){
			if (planDescuento.getPorDesCap() != null)
				descuentoCapital = planDescuento.getPorDesCap();
			else
				descuentoCapital = 0d;

			if (planDescuento.getPorDesAct() != null)
				descuentoActualizacion = planDescuento.getPorDesAct();
			else
				descuentoActualizacion = 0d;
		}

		LiqDeudaVO liqDeudaVO = new LiqDeudaVO();
		liqDeudaVO.setIdDeuda(deuda.getId());
		liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
		liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
		liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
		liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
		liqDeudaVO.setImporte(deuda.getImporte());


		Double capitalConDesc = new Double(deuda.getSaldo() - deuda.getSaldo() * descuentoCapital);
		capitalConDesc = NumberUtil.round(capitalConDesc, SiatParam.DEC_IMPORTE_CALC);


		boolean exentaAct = deuda.getCuenta().exentaActualizacion(fecha, deuda.getFechaVencimiento());


		// Llamamos a la actualizacion de fede
		DeudaAct deudaAct = ActualizaDeuda.actualizar(fecha, 
				deuda.getFechaVencimiento(),
				capitalConDesc, 
				exentaAct,
				deuda.esActualizable());

		Double actualizaConDesc = new Double(deudaAct.getRecargo() - deudaAct.getRecargo() * descuentoActualizacion);
		actualizaConDesc = NumberUtil.round(actualizaConDesc, SiatParam.DEC_IMPORTE_CALC);

		liqDeudaVO.setSaldo(capitalConDesc);
		liqDeudaVO.setActualizacion(actualizaConDesc);

		Double total = new Double(capitalConDesc + actualizaConDesc);
		total = NumberUtil.round(total, SiatParam.DEC_IMPORTE_CALC);

		liqDeudaVO.setTotal(total);

		return liqDeudaVO;
	}

	public MultaVO activarMulta(UserContext userContext, MultaVO multaVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Multa multa = Multa.getById(multaVO.getId());

            multa.activar();

            if (multa.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				multaVO =  (MultaVO) multa.toVO(0,false);
			}
            multa.passErrorMessages(multaVO);
            
            log.debug(funcName + ": exit");
            return multaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public MultaVO createMulta(UserContext userContext, MultaVO multaVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			multaVO.clearError();
			
			if(multaVO.getTipoMulta().getEsImporteManual().getId()==1){
				if (multaVO.getImporte() == null){
					multaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DEUDA_IMPORTE);
				}else{
					Double importe = multaVO.getImporte();
					if (importe.intValue() < 0)
						multaVO.addRecoverableError(
								BaseError.MSG_CAMPO_REQUERIDO,
								GdeError.DEUDA_IMPORTE);
				}
				if (multaVO.getFechaVencimiento() == null){
					multaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DEUDA_FECHAVENCIMIENTO);
				}
			}
			
			if(multaVO.getTipoMulta().getEsImporteManual().getId()==SiNo.NO.getId()){
				if (multaVO.getFechaResolucion()==null){
					multaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.MULTA_FECHARESOLUCION_LABEL);
				}
				if (StringUtil.isNullOrEmpty(multaVO.getCaso().getIdFormateado())){
					multaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.MULTA_RESOLUCION_LABEL);
				}
			}
			
			TipoMulta tipoMulta = TipoMulta.getById(multaVO.getTipoMulta().getId());
			if (tipoMulta.getCanMinDes()!=null && multaVO.getCanMin()<tipoMulta.getCanMinDes()){
				multaVO.addRecoverableError(GdeError.MULTA_CANMIN_MENOR_CANMINDES);
			}
			if (tipoMulta.getCanMinHas()!=null &&  multaVO.getCanMin()>tipoMulta.getCanMinHas()){
				multaVO.addRecoverableError(GdeError.MULTA_CANMIN_MAYOR_CANMINHAS);
			}

			if(multaVO.hasError()){
				return multaVO;
			}
		
			// Copiado de propiadades de VO al BO
            Multa multa = new Multa();
			
            Cuenta cuenta = Cuenta.getById(multaVO.getCuenta().getId());
			multa.setCuenta(cuenta);
			if(multaVO.getOrdenControl().getId()!=null){
				OrdenControl ordenControl = OrdenControl.getById(multaVO.getOrdenControl().getId());
				multa.setOrdenControl(ordenControl);
			}
			multa.setFechaEmision(new Date());
			multa.setImporte(multaVO.getImporte());
			
			multa.setTipoMulta(tipoMulta);
			if(multaVO.getDescuentoMulta().getId()!=null){
				DescuentoMulta descuentoMulta = DescuentoMulta.getByIdNull(multaVO.getDescuentoMulta().getId());
				multa.setDescuentoMulta(descuentoMulta);
			}
			
			if (multaVO.getFechaResolucion()!=null)
				multa.setFechaResolucion(multaVO.getFechaResolucion());
			
			if (!StringUtil.isNullOrEmpty(multaVO.getCaso().getIdFormateado()))
				multa.setIdCaso(multaVO.getCaso().getIdFormateado());
			
			if(!multa.getImporte().equals(0)){
				DeudaAdmin deudaAdmin = new DeudaAdmin();
			    
				deudaAdmin.setRecurso(tipoMulta.getRecurso());
		        deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		        deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		        deudaAdmin.setFechaEmision(new Date());
		        deudaAdmin.setEstaImpresa(SiNo.NO.getId());
		        deudaAdmin.setSistema(Sistema.getSistemaEmision(multa.getTipoMulta().getRecurso()));           
		        deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
		        Long a= new Long(DateUtil.getAnio(deudaAdmin.getFechaEmision()));
		        
		        if (multaVO.getFechaResolucion()!=null)
		        	a= new Long(DateUtil.getAnio(multaVO.getFechaResolucion()));
		        
		        if(cuenta.getRecurso().getAtributoAse()!=null)
		        	deudaAdmin.setAtrAseVal(cuenta.getAtrAseValObjImp());
		        	
		        deudaAdmin.setAnio(a);
		        
		        Long periodo= new Long(DateUtil.getMes(deudaAdmin.getFechaEmision()));
		        
		        if (multaVO.getFechaResolucion()!=null)
		        	periodo= new Long(DateUtil.getMes(multaVO.getFechaResolucion()));
		        
		        deudaAdmin.setPeriodo(periodo);
		        deudaAdmin.setCuenta(cuenta);
		        
		        if (!StringUtil.isNullOrEmpty(multaVO.getCaso().getNumero()))
		        	deudaAdmin.setStrEstadoDeuda("Exp. "+multaVO.getCaso().getNumero());
		        Date diaInicio=new Date();
		        if (multaVO.getFechaNotificacion()!=null)
		        	diaInicio=multaVO.getFechaNotificacion();
		        Date dia = Feriado.getSumarDiasHabil(diaInicio,15L);
		        if (multaVO.getTipoMulta().getEsImporteManual().getEsNO())
		        	deudaAdmin.setFechaVencimiento(dia);
		        else
		        	deudaAdmin.setFechaVencimiento(multaVO.getFechaVencimiento());
		        deudaAdmin.setRecClaDeu(multa.getTipoMulta().getRecClaDeu());
		        deudaAdmin.setReclamada(SiNo.NO.getId());
		        deudaAdmin.setResto(3L);  // Se graba con resto distinto de cero para evitar problemas de Asentamiento de la Deuda Original migrada. (Fix Mantis 5077)
		        deudaAdmin.setEstado(Estado.ACTIVO.getId());
		        deudaAdmin.setImporte(multaVO.getImporte());
		        deudaAdmin.setImporteBruto(multaVO.getImporte());
		        deudaAdmin.setSaldo(multaVO.getImporte());
		        
		        List<DeuAdmRecCon> listRecCon = new ArrayList<DeuAdmRecCon>();
		        for (RecCon recCon : tipoMulta.getRecurso().getListRecCon()){
		        	DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
		        	deuAdmRecCon.setImporte(deudaAdmin.getImporte()*recCon.getPorcentaje());
		        	deuAdmRecCon.setRecCon(recCon);
		        	deuAdmRecCon.setDeuda(deudaAdmin);
		        	deuAdmRecCon.setSaldo(deudaAdmin.getImporte()*recCon.getPorcentaje());
		        	deuAdmRecCon.setImporteBruto(deudaAdmin.getImporte()*recCon.getPorcentaje());
		        	listRecCon.add(deuAdmRecCon);
		        }
		        
		        deudaAdmin = GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdmin,listRecCon);
			    
				multa.setIdDeuda(deudaAdmin.getId());
				
				/*multa.setImporte(multaVO.getImporte());*/
				
				
				deudaAdmin.passErrorMessages(multaVO);
			
			}
			multa.setFechaVencimiento(multaVO.getFechaVencimiento());
			multa.setEstado(multaVO.getEstado().getId());
			
		
			if(multaVO.hasError()){
		  		tx.rollback();
		  		return multaVO;	  		
		  	}
		
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            multa = GdeGDeudaAutoManager.getInstance().createMulta(multa);
            
			if(multaVO.getTipoMulta().getEsImporteManual().getEsNO()){

				for (MultaDetVO multaDetVO : multaVO.getListMultaDet()){
					//crear un multadet por cada elemento de la lista y setearle detajudet
					MultaDet multaDet = new MultaDet();
						
					multaDet.setMulta(multa);
					DetAjuDet detAjuDet = DetAjuDet.getById(multaDetVO.getDetAjuDet().getId());
					
					multaDet.setDetAjuDet(detAjuDet);
					multaDet.setPeriodo(detAjuDet.getPeriodoOrden().getPeriodo());
					multaDet.setAnio(detAjuDet.getPeriodoOrden().getAnio());
					multaDet.setImporteBase(multaDetVO.getImporteBase());
					multaDet.setImporteAct(multaDetVO.getImporteAct());
					multaDet.setPorOri(multaDetVO.getPorOri());
					multaDet.setPorDes(multaVO.getDescuentoMulta().getPorcentaje());
					
					multaDet.setPorApl(multaDet.getPorOri() * (1-multaDet.getPorDes()));
					multaDet.setImporteAplicado(multaDet.getImporteBase() * multaDet.getPorApl());	
					
					// multa tipo revision
					multaDet.setPagoContadoOBueno(multaDetVO.getPagoContadoOBueno());
					multaDet.setResto(multaDetVO.getResto());
					multaDet.setPagoActualizado(multaDetVO.getPagoActualizado());
					multaDet.setRestoActualizado(multaDetVO.getRestoActualizado());
					multaDet.setAplicado(multaDetVO.getAplicado());
					
					multaDet = GdeGDeudaAutoManager.getInstance().createMultaDet(multaDet);
					
					multa.getListMultaDet().add(multaDet);

				}
				Date diaInicio=new Date();
		        if (multaVO.getFechaNotificacion()!=null)
		        	diaInicio=multaVO.getFechaNotificacion();
		        Date dia = Feriado.getSumarDiasHabil(diaInicio,15L);
				multa.setFechaVencimiento(dia);
				
				multa.setFechaActualizacion(multaVO.getFechaActualizacion());
				multa.setFechaNotificacion(multaVO.getFechaNotificacion());
				
				// multa tipo revision
				multa.setTotalAplicado(multaVO.getTotalAplicado());
				multa.setImporteMultaAnterior(multaVO.getImporteMultaAnterior());
			}
			GdeDAOFactory.getMultaDAO().update(multa);
			
			session.flush();
			if (!ListUtil.isNullOrEmpty(multaVO.getListMultaHistorico())){
				multa.setListMultaHistorico(new ArrayList<MultaHistorico>());
				for (MultaHistoricoVO multaHistVO: multaVO.getListMultaHistorico()){
					MultaHistorico multaHistorico = new MultaHistorico();
					multaHistorico.setMulta(multa);
					multaHistorico.setFecha(new Date());
					multaHistorico.setPorcentaje(NumberUtil.truncate(multaHistVO.getPorcentaje(),SiatParam.DEC_PORCENTAJE_DB));
					multaHistorico.setPeriodoDesde(multaHistVO.getPeriodoDesde());
					multaHistorico.setPeriodoHasta(multaHistVO.getPeriodoHasta());
					multaHistorico.setAnioDesde(multaHistVO.getAnioDesde());
					multaHistorico.setAnioHasta(multaHistVO.getAnioHasta());
					multaHistorico.setObservacion(multaHistVO.getObservacion());
					multaHistorico.setImporteTotal(multaHistVO.getImporteTotal());
					if (multaHistVO.getCaso()!=null){
						multaHistorico.setIdCaso(multaHistVO.getCaso().getIdFormateado());
					}
					
					GdeDAOFactory.getMultaHistoricoDAO().update(multaHistorico);
					session.flush();
					
					multa.getListMultaHistorico().add(multaHistorico);
				}
			}
			
			GdeDAOFactory.getMultaDAO().update(multa);
			
			if(multaVO.hasError()){
		  		tx.rollback();
		  		return multaVO;	  		
		  	}
			
            if (multa.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				multaVO =  (MultaVO) multa.toVO(2,false);
			}
			multa.passErrorMessages(multaVO);
			multaVO = (MultaVO) multa.toVO(1);
            log.debug(funcName + ": exit");
            return multaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			e.printStackTrace();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public MultaVO deleteMulta(UserContext userContext, MultaVO multaVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			multaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Multa multa = Multa.getById(multaVO.getId());
			
			if(!multa.getEstado().equals(Estado.CREADO.getId())){
				multaVO.addRecoverableError(GdeError.MSG_IMPOSIBLE_ELIMINAR);
			}else{
				// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
				for(MultaDet multaDet: multa.getListMultaDet()){
					
					multaDet = MultaDet.getById(multaDet.getId());
					multaDet = GdeGDeudaAutoManager.getInstance().deleteMultaDet(multaDet);
					multaDet.passErrorMessages(multa);
					if (multa.hasError()) {
		            	tx.rollback();
		            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
					}
				}
				multa = GdeGDeudaAutoManager.getInstance().deleteMulta(multa);
			}
			
			if (multaVO.hasError()) {
            	return multaVO;
			}
			if (multa.hasError()) {
            	tx.rollback();
            	return multaVO;
			}
			if (multa.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				multaVO =  (MultaVO) multa.toVO(0,false);
			}
			multa.passErrorMessages(multaVO);
            
            log.debug(funcName + ": exit");
            return multaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public MultaVO desactivarMulta(UserContext userContext, MultaVO multaVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Multa multa = Multa.getById(multaVO.getId());
                           
            multa.desactivar();

            if (multa.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				multaVO =  (MultaVO) multa.toVO(0,false);
			}
            multa.passErrorMessages(multaVO);
            
            log.debug(funcName + ": exit");
            return multaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public MultaAdapter getMultaAdapterForCreate(UserContext userContext, MultaAdapter multaAdapter)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Cuenta cuenta = Cuenta.getById(multaAdapter.getIdCuenta());
			
			MultaVO multaVO =  new MultaVO();
			multaVO.setCuenta(cuenta.toVOForView());
			
			if (multaAdapter.isEnCierreComercio()){
				TipoMultaVO tipoMulta;
				TipoMulta tipMul=TipoMulta.getById(TipoMulta.ID_CIERRE_COMERCIO);
				tipoMulta = (TipoMultaVO)(tipMul).toVO(1);	
				multaVO.setTipoMulta(tipoMulta);
				multaAdapter.setDetalleValido(true);
				for(MultaVO multa : multaAdapter.getListMulta()){
					if(!multa.getDeuda().getEstadoDeuda().getId().equals(EstadoDeuda.ID_ANULADA)){
						multaAdapter.addRecoverableError(GdeError.IMPOSIBLE_AGREGAR_MULTA);
					}
				}
				if (tipMul.getCanMinDes()!=null ){
					RecMinDec recMinDec = RecMinDec.getMinimoVigenteForMulta(cuenta.getRecurso(), new Date());
					if (recMinDec != null){
						if (tipMul.getCanMinHas()!=null && tipMul.getCanMinDes().doubleValue()==tipMul.getCanMinHas().doubleValue()){
							multaVO.setImporte(recMinDec.getMinimo()*tipMul.getCanMinDes());
							multaVO.setModificaImporte(false);
							multaVO.setCanMin(tipMul.getCanMinDes());
						}
						if (tipMul.getCanMinHas()!=null && tipMul.getCanMinHas()> tipMul.getCanMinDes()){
							multaVO.setTieneRangosDeMinimos(true);
							multaVO.setMinimo(recMinDec.getMinimo());
						}
					}
				}
			}else{
				multaAdapter.getMulta().setEstado(Estado.ACTIVO);
			}
			multaAdapter.setMulta(multaVO);
			
			//Seteo la lista de tipo de multa
			multaAdapter.setListTipoMulta( (ArrayList<TipoMultaVO>)
					ListUtilBean.toVO(TipoMulta.getListActivosByIdRecurso(cuenta.getRecurso().getId()),
					new TipoMultaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			multaAdapter.setListDescuentoMulta( (ArrayList<DescuentoMultaVO>)
					ListUtilBean.toVO(DescuentoMulta.getListByIdRecurso(cuenta.getRecurso().getId()),
					new DescuentoMultaVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			
						
			log.debug(funcName + ": exit");
			return multaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public MultaAdapter getMultaAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Multa multa = Multa.getById(commonKey.getId());
			
	        MultaAdapter multaAdapter = new MultaAdapter();
	        multaAdapter.setMulta((MultaVO) multa.toVO(2));
	    
			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return multaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MultaAdapter getMultaAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Multa multa = Multa.getById(commonKey.getId());

	        MultaAdapter multaAdapter = new MultaAdapter();
	        multaAdapter.setMulta((MultaVO) multa.toVO(1));
	        
	        if(multaAdapter.getMulta().getTipoMulta().getId().equals(TipoMulta.ID_MULTA_REVISION)){
	        	multaAdapter.setEsTipoRevision(1);
	        }
	        multaAdapter.getMulta().setCuenta(multa.getCuenta().toVOForView());
	        
	        for (MultaHistoricoVO multaHistorico: multaAdapter.getMulta().getListMultaHistorico()){
	        	CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(multaHistorico.getIdCaso());
				multaHistorico.setCaso(caso);
	        }
	        
			log.debug(funcName + ": exit");
			return multaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public MultaAdapter getMultaAdapterParamTipoMulta(UserContext userContext,
			MultaAdapter multaAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			multaAdapter.clearError();
			
			MultaVO multaVO=multaAdapter.getMulta();
			multaVO.setTieneRangosDeMinimos(false);
			
			//si no es nulo lo seleccionado
			if(!ModelUtil.isNullOrEmpty(multaVO.getTipoMulta())){
				TipoMulta tipoMulta = TipoMulta.getById(multaAdapter.getMulta().getTipoMulta().getId());
				if (tipoMulta.getCanMinDes()!=null ){
					RecMinDec recMinDec = RecMinDec.getMinimoVigenteForMulta(tipoMulta.getRecurso(), new Date());
					if (recMinDec != null){
						if (tipoMulta.getCanMinDes().doubleValue()==tipoMulta.getCanMinHas().doubleValue()){
							multaVO.setCanMin(tipoMulta.getCanMinDes());
							multaVO.setImporte(recMinDec.getMinimo()*tipoMulta.getCanMinDes());
							multaVO.setModificaImporte(false);
							multaAdapter.setDetalleValido(true);
						}
						if (tipoMulta.getCanMinHas()!=null && tipoMulta.getCanMinHas()> tipoMulta.getCanMinDes()){
							multaVO.setTieneRangosDeMinimos(true);
							multaVO.setMinimo(recMinDec.getMinimo());
						}
					}
				}
				multaVO.setTipoMulta((TipoMultaVO)tipoMulta.toVO());
				multaAdapter.setMulta(multaVO);
			}
			
			
			log.debug(funcName + ": exit");
			return multaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public MultaAdapter getMultaAdapterParamDescuentoMulta(UserContext userContext,
			MultaAdapter multaAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			multaAdapter.clearError();
			
			// Logica del param

			//si no es nulo lo seleccionado
			if(!ModelUtil.isNullOrEmpty(multaAdapter.getMulta().getDescuentoMulta())){
				DescuentoMulta descuentoMulta = DescuentoMulta.getById(multaAdapter.getMulta().getDescuentoMulta().getId());
				multaAdapter.getMulta().setDescuentoMulta((DescuentoMultaVO)descuentoMulta.toVO());
			}else{
				// si selecciona "ninguno"
				DescuentoMultaVO dtoMulta = new DescuentoMultaVO();
				dtoMulta.setPorcentaje(0D);
				dtoMulta.setFechaDesde(new Date());
				dtoMulta.setDescripcion("Ninguno");
				multaAdapter.getMulta().setDescuentoMulta(dtoMulta);
			}
			
			if(multaAdapter.getMulta().getTipoMulta().getEsImporteManual().getEsNO()){
				multaAdapter = createDetalleMulta(userContext, multaAdapter);
			}
			if(multaAdapter.hasError()){
		  		return multaAdapter;	  		
		  	}
		
			log.debug(funcName + ": exit");
			return multaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			e.printStackTrace();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public MultaAdapter getMultaAdapterParamDetalleMulta(UserContext userContext,
			MultaAdapter multaAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			multaAdapter.clearError();
			
			// Logica del param
			
			TipoMulta tipoMulta = TipoMulta.getByIdNull(multaAdapter.getMulta().getTipoMulta().getId());
			
			if (tipoMulta==null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.MULTA_TIPOMULTA);
			}

			OrdenControl ordenControl=null;
			
			boolean caso=false;
			if(multaAdapter.getMulta().getCaso().isSubmited()){
				caso=true;
			}
			
			Integer numero = multaAdapter.getMulta().getOrdenControl().getNumeroOrden();
			
			Integer anio = multaAdapter.getMulta().getOrdenControl().getAnioOrden();
			
			boolean asociadaOrden=false;
			if (tipoMulta.getAsociadaAOrden()!=null && tipoMulta.getAsociadaAOrden().intValue()==SiNo.SI.getId().intValue())
				asociadaOrden=true;
			
			if (asociadaOrden && numero == null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.MULTA_NUMEROORDEN_LABEL);
			}
			
			if (asociadaOrden && anio == null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.MULTA_ANIOORDEN_LABEL);
			}
			
			/*if (!caso && !asociadaOrden)
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,CasError.CASO_LABEL);
			*/
			if(asociadaOrden && (numero != null) && (anio!=null)){
				ordenControl = OrdenControl.getByNumeroAnioCaso(multaAdapter.getMulta().getOrdenControl().getNumeroOrden(), multaAdapter.getMulta().getOrdenControl().getAnioOrden(), multaAdapter.getMulta().getOrdenControl().getCaso().getIdFormateado());
			}
			
			if(asociadaOrden && ordenControl==null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTA_ORDEN);
			}
			
			if(tipoMulta.getEsImporteManual()==0 && multaAdapter.getMulta().getFechaActualizacion()==null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTA_FECHAACTUALIZACION);
			}
			
			if(multaAdapter.hasError()){
				return multaAdapter;
			}
			
			if (asociadaOrden){
				OrdenControlVO	ordenControlVO= (OrdenControlVO)ordenControl.toVO();
				multaAdapter.getMulta().setOrdenControl(ordenControlVO);
			}
			
			
			//si no selecciona ningun descuento
			DescuentoMultaVO dtoMulta = new DescuentoMultaVO();
			dtoMulta.setPorcentaje(0D);
			dtoMulta.setFechaDesde(new Date());
			dtoMulta.setDescripcion("Ninguno");
			multaAdapter.getMulta().setDescuentoMulta(dtoMulta);
			
			if(multaAdapter.getMulta().getTipoMulta().getEsImporteManual().getEsNO()){
				multaAdapter = createDetalleMulta(userContext,multaAdapter);
			}
			if(multaAdapter.hasError()){
				return multaAdapter;
			}
			multaAdapter.setDetalleValido(true);
		
			log.debug(funcName + ": exit");
			return multaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			e.printStackTrace();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
 
	public PrintModel imprimirMulta(UserContext userContext, MultaAdapter multaAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_MULTA_GDE);
			print.putCabecera("usuario", userContext.getUserName());
			print.setData(multaAdapterVO);
			print.setTopeProfundidad(5);

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}

	public MultaVO updateMulta(UserContext userContext, MultaVO multaVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			multaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Multa multa = Multa.getById(multaVO.getId());
			
			if(!multaVO.validateVersion(multa.getFechaUltMdf())) return multaVO;
			
			if(multaVO.getFechaNotificacion()==null){
				multaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTA_FECHANOTIFICACION);
			}else{
				if (multaVO.getFechaNotificacion() != null && 
						DateUtil.isDateBefore(multaVO.getFechaNotificacion(), multaVO.getFechaEmision())){
					multaVO.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
						GdeError.MULTA_FECHANOTIFICACION, GdeError.MULTA_FECHAEMISION);
				}
			}
			if (multaVO.hasError()) {
            	tx.rollback();
            	return multaVO;
			}
	        multa.setFechaNotificacion(multaVO.getFechaNotificacion());
	        Date dia = Feriado.getSumarDiasHabil(multa.getFechaNotificacion(),15L);
	        Deuda deuda = Deuda.getById(multa.getIdDeuda());
	        
	        if(multaVO.getFechaVencimiento()!=null)
	        	dia=multaVO.getFechaVencimiento();
	        
	        
			deuda.setFechaVencimiento(dia); 
		
			multa.setFechaVencimiento(dia);
			
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            multa = GdeGDeudaAutoManager.getInstance().updateMulta(multa);
            
            if (multa.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				multaVO =  (MultaVO) multa.toVO(0,false);
			}
			multa.passErrorMessages(multaVO);
            
            log.debug(funcName + ": exit");
            return multaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public MultaSearchPage getMultaSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			MultaSearchPage multaSearchPage = new MultaSearchPage();
			
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			//listRecurso = Recurso.getListActivos();			
			listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			
			multaSearchPage.setListRecurso(listRecursoVO);
			multaSearchPage.getRecurso().setId(-1L);
			
			multaSearchPage.setFechaEmisionDesde(null);
			multaSearchPage.setFechaEmisionHasta(null);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return multaSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MultaSearchPage getMultaSearchPageResult(UserContext userContext, MultaSearchPage multaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			multaSearchPage.clearError();

			Long idRecurso=multaSearchPage.getRecurso().getId();
			
			//Aqui realizar validaciones
			//Verifico que se haya seleccionado un recurso
			if (idRecurso==null || idRecurso < 1L){
				multaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTASEARCHPAGE_CUENTA_RECURSO_LABEL);
			}
			//Verifico que se haya ingresado un numero de cuenta
			if (StringUtil.isNullOrEmpty(multaSearchPage.getMulta().getCuenta().getNumeroCuenta())){
				multaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTASEARCHPAGE_CUENTA_LABEL);
			}

			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( multaSearchPage.getFechaEmisionDesde() != null && multaSearchPage.getFechaEmisionHasta() != null &&
					DateUtil.isDateAfter(multaSearchPage.getFechaEmisionDesde(), multaSearchPage.getFechaEmisionHasta())){
				multaSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.MULTA_SEARCHPAGE_FECHAEMISIONDESDE, GdeError.MULTA_SEARCHPAGE_FECHAEMISIONHASTA);
			}
			
			Cuenta cuenta=null;
			
			if (!multaSearchPage.hasErrorRecoverable()){
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, multaSearchPage.getMulta().getCuenta().getNumeroCuenta());
				if (cuenta ==null){
					multaSearchPage.addRecoverableError(GdeError.MULTASEARCHPAGE_CUENTA_NOEXISTE);
				}
			}
			
			//Si hubo errores salgo
			if (multaSearchPage.hasErrorRecoverable()){
				return multaSearchPage;
			}		
			multaSearchPage.getMulta().setCuenta(cuenta.toVOForView());
	   		
			// Aqui obtiene lista de BOs
			List<Multa> listMulta = GdeDAOFactory.getMultaDAO().getBySearchPage(multaSearchPage);  

			//Aqui pasamos BO a VO
					
	   		multaSearchPage.setListResult(ListUtilBean.toVO(listMulta, 2));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return multaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DesgloseAjusteAdapter getDesglose(DesgloseAjusteAdapter desgloseAjusteAdapter) throws Exception{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {

			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		   
			List<LiqDeudaVO> listDeudaVO = desgloseAjusteAdapter.getListDeuda();
		
			
			Date fechaLimite = desgloseAjusteAdapter.getFechaLimite();
           	
			//Creacion deglose
			Desglose desglose = new Desglose();
			
			desglose.setFechalimite(fechaLimite);
			desglose.setObservacion(desgloseAjusteAdapter.getObservacion());
			desglose.setCuenta(Cuenta.getById(desgloseAjusteAdapter.getCuenta().getIdCuenta()));
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_ORDEN_CONTROL_FISCAL); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(desgloseAjusteAdapter.getDesglose(), desglose, 
        			accionExp,null, desglose.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (desgloseAjusteAdapter.hasError()){
        		tx.rollback();
        		return desgloseAjusteAdapter;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	desglose.setIdCaso(desgloseAjusteAdapter.getDesglose().getIdCaso());
        	
		
			List<DesgloseDet> listDesgloseDet= new ArrayList<DesgloseDet>();
			
			
			for(LiqDeudaVO deudaVO:listDeudaVO){
				

				Deuda deuda = Deuda.getById(deudaVO.getIdDeuda());
			
				DeudaAct deudaAct = deuda.actualizacionSaldo(fechaLimite);
				Double recargo =deudaAct.getRecargo();

				// Setea los datos en la deudaAdmin para "Ajuste Fiscal Accesorio" 
				DeudaAdmin deudaAdminAcc = new DeudaAdmin();
				deudaAdminAcc.setRecurso(deuda.getRecurso());
				deudaAdminAcc.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
				deudaAdminAcc.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
				deudaAdminAcc.setFechaEmision(deuda.getFechaEmision());
				deudaAdminAcc.setEstaImpresa(SiNo.NO.getId());
				deudaAdminAcc.setSistema(deuda.getSistema());           
				deudaAdminAcc.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
				deudaAdminAcc.setAnio(deuda.getAnio());
				deudaAdminAcc.setPeriodo(deuda.getPeriodo());
				deudaAdminAcc.setCuenta(deuda.getCuenta());
				if(DateUtil.isDateBefore(fechaLimite, deuda.getFechaVencimiento())){
					desgloseAjusteAdapter.addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.DESGLOSE_AJUSTE_FECHA_LIMITE, 
							GdeError.DESGLOSE_AJUSTE_FECHA_VENCIMIENTO);
					return desgloseAjusteAdapter;
				}
				deudaAdminAcc.setFechaVencimiento(fechaLimite);//modicación 
				if(deudaAdminAcc.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
					deudaAdminAcc.setRecClaDeu(RecClaDeu.getByIdNull(RecClaDeu.ID_AJUSTE_FISCAL_DREI_ACCESORIO));
				}else{
					deudaAdminAcc.setRecClaDeu(RecClaDeu.getByIdNull(RecClaDeu.ID_AJUSTE_FISCAL_ETUR_ACCESORIO));
				}
				deudaAdminAcc.setEmision(deuda.getEmision());
				deudaAdminAcc.setReclamada(SiNo.NO.getId());
				deudaAdminAcc.setResto(4L);  // Se graba con resto distinto de cero para evitar problemas de Asentamiento de la Deuda Original migrada. (Fix Mantis 5077)
				deudaAdminAcc.setEstado(Estado.ACTIVO.getId());
				deudaAdminAcc.setAtrAseVal(deuda.getAtrAseVal());
				deudaAdminAcc.setImporte(recargo);//modicación
				deudaAdminAcc.setImporteBruto(recargo);//modicación
				deudaAdminAcc.setSaldo(recargo);//modicación
		
				
				// Calcula el importe y setea los conceptos            
				List<DeuAdmRecCon> listDeuAdmRecConAcc= new ArrayList<DeuAdmRecCon>();

				List<RecCon> listRecCon = deuda.getRecurso().getListRecCon();
				for(RecCon c: listRecCon){
					DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
					deuAdmRecCon.setDeuda(deudaAdminAcc);
					deuAdmRecCon.setRecCon(c);

					Double importe = deudaAdminAcc.getImporte();

					deuAdmRecCon.setImporte(importe*c.getPorcentaje());//modicación
					deuAdmRecCon.setImporteBruto(importe*c.getPorcentaje());//modicación
					deuAdmRecCon.setSaldo(importe*c.getPorcentaje());//modicación
					listDeuAdmRecConAcc.add(deuAdmRecCon);

				}                        
                
				
				// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
                deudaAdminAcc = GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdminAcc, listDeuAdmRecConAcc);           
				
				deudaAdminAcc.passErrorMessages(desgloseAjusteAdapter);
				
				if (desgloseAjusteAdapter.hasError()){
					break;
				}
				// Setea los datos en la deudaAdmin para "Ajuste Fiscal Historico"
				DeudaAdmin deudaAdmin = new DeudaAdmin();
				deudaAdmin.setRecurso(deuda.getRecurso());
				deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
				deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
				deudaAdmin.setFechaEmision(deuda.getFechaEmision());
				deudaAdmin.setEstaImpresa(SiNo.NO.getId());
				deudaAdmin.setSistema(deuda.getSistema());           
				deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
				deudaAdmin.setAnio(deuda.getAnio());
				deudaAdmin.setPeriodo(deuda.getPeriodo());
				deudaAdmin.setCuenta(deuda.getCuenta());
				deudaAdmin.setFechaVencimiento(deuda.getFechaVencimiento());
				if(deudaAdmin.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
					deudaAdmin.setRecClaDeu(RecClaDeu.getByIdNull(RecClaDeu.ID_AJUSTE_FISCAL_DREI_HISTORICO));
				}else{
					deudaAdmin.setRecClaDeu(RecClaDeu.getByIdNull(RecClaDeu.ID_AJUSTE_FISCAL_ETUR_HISTORICO));
				}
				deudaAdmin.setEmision(deuda.getEmision());
				deudaAdmin.setReclamada(SiNo.NO.getId());
				deudaAdmin.setResto(0L);
				deudaAdmin.setEstado(Estado.ACTIVO.getId());
				deudaAdmin.setAtrAseVal(deuda.getAtrAseVal());
				deudaAdmin.setImporte(deuda.getSaldo());
				deudaAdmin.setImporteBruto(deuda.getSaldo());
				deudaAdmin.setSaldo(deuda.getSaldo());

				// Calcula el importe y setea los conceptos            
				List<DeuAdmRecCon> listDeuAdmRecCon= new ArrayList<DeuAdmRecCon>();
			
			
				
				for(RecCon c: listRecCon){
					DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
					
					deuAdmRecCon.setDeuda(deudaAdmin);
					deuAdmRecCon.setRecCon(c);

					Double importe = deudaAdmin.getImporte() * c.getPorcentaje();
				
					deuAdmRecCon.setImporte(importe);
					deuAdmRecCon.setImporteBruto(importe);
					deuAdmRecCon.setSaldo(importe);
					listDeuAdmRecCon.add(deuAdmRecCon);
				}              
				
				// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
				deudaAdmin = GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdmin, listDeuAdmRecCon);           
			
				deudaAdmin.passErrorMessages(desgloseAjusteAdapter);
				

				if (desgloseAjusteAdapter.hasError()){
					break;
				}
				// Setea los datos en la "deuda Anulada"
				Anulacion anulacionDueda = new Anulacion();
				anulacionDueda.setIdDeuda(deuda.getId());
				anulacionDueda.setFechaAnulacion(new Date());
				anulacionDueda.setMotAnuDeu(MotAnuDeu.getById(MotAnuDeu.ID_DEGLOSE_AJUSTE));
				anulacionDueda.setRecurso(deuda.getRecurso());
				anulacionDueda.setViaDeuda(deuda.getViaDeuda());
				
			 	GdeDAOFactory.getDeudaAnuladaDAO().update(anulacionDueda);
				
				anulacionDueda = GdeGDeudaManager.getInstance().anularDeuda(anulacionDueda,deuda,null);
				
				anulacionDueda.passErrorMessages(desgloseAjusteAdapter);
				
				if (desgloseAjusteAdapter.hasError()){
					break;
				}
				session.flush();
				
				//Creacion de detalle de desglose
					DesgloseDet desgloseDet = new DesgloseDet();
					desgloseDet.setDesglose(desglose);
					desgloseDet.setIddeudaOrig(deuda.getId());
					desgloseDet.setIdAjuFisAcc(deudaAdminAcc.getId());
					desgloseDet.setIdAjuFisHis(deudaAdmin.getId());
					desgloseDet.setAnulacion(anulacionDueda);
					
				
					listDesgloseDet.add(desgloseDet);

			}
			 
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            desglose = GdeGDeudaAutoManager.getInstance().createDesglose(desglose, listDesgloseDet);
			desglose.passErrorMessages(desgloseAjusteAdapter);
			
			if (desgloseAjusteAdapter.hasError()) {
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				tx.rollback();
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
			return desgloseAjusteAdapter;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	private MultaAdapter createDetalleMulta(UserContext userContext,MultaAdapter multaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			
		List<DetAjuDet> listDetAjuDet = DetAjuDet.getByCuentaAjusteOrden(multaAdapter.getMulta().getCuenta().getId(),multaAdapter.getMulta().getOrdenControl().getId());
		List<DetAjuDetVO> listDetAjuDetVO = ListUtilBean.toVO(listDetAjuDet,1);
		
		if(listDetAjuDetVO.size()==0){
			multaAdapter.addRecoverableError(GdeError.MSG_MULTA_DETAJUDET_NOEXISTE);
			return multaAdapter;
		}else{
			//Verifico que se haya seleccionado un descuento
			multaAdapter.setTotalMulta(0D);
			
			if (multaAdapter.getMulta().getListMultaDet().size()==0){
				//Double importeMultaAnterior = 0.0;
				
				if(multaAdapter.getMulta().getTipoMulta().getId().equals(TipoMulta.ID_MULTA_REVISION)){					
					// multa por revision					
					multaAdapter.setEsTipoRevision(1);
					// controlo si la orden de control tiene multa asociada
					List<Multa> listMulta = Multa.getListByOrdenControlTipoMulta(multaAdapter.getMulta().getOrdenControl().getId(),multaAdapter.getMulta().getTipoMulta().getId());
					
					if(listMulta.size()>0){
						// recupero los importe
						for(Multa multa: listMulta){
							multaAdapter.getMulta().setImporteMultaAnterior(multaAdapter.getMulta().getImporteMultaAnterior()+ multa.getImporte());
						}
					}
				}
				for (DetAjuDet detAjuDet : listDetAjuDet){
					//crear un multadet por cada elemento de la lista y setearle detajudet
					DetAjuDetVO detAjuDetVO = (DetAjuDetVO)detAjuDet.toVO(1);
					
					Double porMulta = detAjuDet.getPorMulta();
					//No se pude definir una multa para la cuenta
					//if(detAjuDet.getPorMulta()==null || detAjuDet.getPorMulta()==0){
					if(porMulta==null){
						porMulta=0D;;
					}
						MultaDetVO multaDet = new MultaDetVO();
						
						multaDet.setDetAjuDet(detAjuDetVO);
						multaDet.setMulta(multaAdapter.getMulta());
						multaDet.setPeriodo(detAjuDet.getPeriodoOrden().getPeriodo());
						multaDet.setAnio(detAjuDet.getPeriodoOrden().getAnio());
						multaDet.setImporteBase(detAjuDet.getAjuste() + detAjuDet.getPagPosFecIni());
						if(multaAdapter.getMulta().getTipoMulta().getId().equals(TipoMulta.ID_MULTA_REVISION)){
							// multa por revision
							
							if(detAjuDet.getDeuda()!=null){
								List<PagoDeuda> listPagoDeuda = PagoDeuda.getByDeuda(detAjuDet.getDeuda().getId());								
							
								for(PagoDeuda pagoDeuda: listPagoDeuda){
									if(pagoDeuda.getTipoPago().getId().equals(TipoPago.ID_RECIBO_DE_RECONFECCION)){
										multaDet.setPagoContadoOBueno(pagoDeuda.getImporte()+multaDet.getPagoContadoOBueno());
										multaDet.setPagoActualizado(pagoDeuda.getActualizacion());
									}
									if(pagoDeuda.getTipoPago().getId().equals(TipoPago.ID_PAGO_BUENO)){
										multaDet.setPagoContadoOBueno(pagoDeuda.getImporte()+multaDet.getPagoContadoOBueno());
										Convenio convenio = Convenio.getById(pagoDeuda.getIdPago());
										DeudaAct deudaAct = ActualizaDeuda.actualizar(convenio.getFechaFor(), Deuda.getById(pagoDeuda.getIdDeuda()).getFechaVencimiento(),pagoDeuda.getImporte(),false,true);
										multaDet.setPagoActualizado(multaDet.getPagoActualizado() + deudaAct.getRecargo());
									}
									multaDet.setResto(multaDet.getImporteBase() - multaDet.getPagoContadoOBueno());
									DeudaAct deudaAct = ActualizaDeuda.actualizar(new Date(), Deuda.getById(pagoDeuda.getIdDeuda()).getFechaVencimiento(),multaDet.getResto(),false,true);
									multaDet.setRestoActualizado(multaDet.getRestoActualizado() + deudaAct.getRecargo());
									multaDet.setAplicado(multaDet.getPagoActualizado() + multaDet.getRestoActualizado());
								}
							}
							multaAdapter.getMulta().setTotalAplicado(multaAdapter.getMulta().getTotalAplicado() + multaDet.getAplicado());

						}else{
							//multaDet.setImporteBase(detAjuDet.getAjuste());
							Cuenta cuenta = detAjuDet.getDetAju().getOrdConCue().getCuenta();
							Deuda deudaOrig = Deuda.getPerOriByCuentaPeriodoAnio(cuenta, detAjuDet.getPeriodoOrden().getPeriodo().longValue(), detAjuDet.getPeriodoOrden().getAnio());
							Date fechaVencimiento;
							if (deudaOrig != null){
								fechaVencimiento=deudaOrig.getFechaVencimiento();
							}else{
								Contribuyente contribuyente = cuenta.getListCuentaTitularVigentes(new Date()).get(0).getContribuyente();
								if (contribuyente.getNroIsib()!=null)
									if (contribuyente.getNroIsib().indexOf("900")==0)
										fechaVencimiento = DateUtil.getDate("15/"+detAjuDet.getPeriodoOrden().getPeriodo()+"/"+detAjuDet.getPeriodoOrden().getAnio(), DateUtil.ddSMMSYYYY_MASK);
									else
										fechaVencimiento = DateUtil.getDate("10/"+detAjuDet.getPeriodoOrden().getPeriodo()+"/"+detAjuDet.getPeriodoOrden().getAnio(), DateUtil.ddSMMSYYYY_MASK);
								else
									fechaVencimiento = DateUtil.getDate("10/"+detAjuDet.getPeriodoOrden().getPeriodo()+"/"+detAjuDet.getPeriodoOrden().getAnio(), DateUtil.ddSMMSYYYY_MASK);
								fechaVencimiento=Feriado.nextDiaHabil(fechaVencimiento);
							}
							multaDet.setImporteAct(ActualizaDeuda.actualizar(fechaVencimiento, detAjuDet.getAjuste(), false, true).getImporteAct());
							multaDet.setPorOri(porMulta);
							multaDet.setPorDes(multaAdapter.getMulta().getDescuentoMulta().getPorcentaje());											
							multaDet.setPorApl((multaDet.getPorDes()!=null && multaDet.getPorDes()!=0D && porMulta>0)?multaDet.getPorDes():multaDet.getPorOri());
							multaDet.setImporteAplicado(multaDet.getImporteAct() * multaDet.getPorApl());	
							
							multaAdapter.setTotalMulta(multaAdapter.getTotalMulta() + multaDet.getImporteAplicado());
						}	
							multaAdapter.getMulta().getListMultaDet().add(multaDet);
							// seteo total de la multa
							
						
					
				}
			}else{
				for (MultaDetVO multaDetVO:multaAdapter.getMulta().getListMultaDet()){
					multaDetVO.setPorDes(multaAdapter.getMulta().getDescuentoMulta().getPorcentaje());											
					multaDetVO.setPorApl((multaDetVO.getPorDes()!=null && multaDetVO.getPorDes()!=0D && multaDetVO.getPorOri()>0)?multaDetVO.getPorDes():multaDetVO.getPorOri());
					multaDetVO.setImporteAplicado(multaDetVO.getImporteAct() * multaDetVO.getPorApl());	

					// seteo total de la multa
					multaAdapter.setTotalMulta(multaAdapter.getTotalMulta() + multaDetVO.getImporteAplicado());
				}
			}
			if(multaAdapter.getMulta().getTipoMulta().getId().equals(TipoMulta.ID_MULTA_REVISION)){
				multaAdapter.getMulta().setImporte(multaAdapter.getMulta().getTotalAplicado() - multaAdapter.getMulta().getImporteMultaAnterior());
			}else{
				multaAdapter.getMulta().setImporte(multaAdapter.getTotalMulta());
			}
		}
		
		return multaAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			e.printStackTrace();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CierreComercioAdapter getCierreComercioAdapterInit(UserContext userContext,CierreComercioAdapter cierreComercioAdapter) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			cierreComercioAdapter.clearMessage();
			
		
			Long idCuenta = new Long(cierreComercioAdapter.getIdCuenta());
			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			
			cierreComercioAdapter.setListMotivoCierre( (ArrayList<MotivoCierreVO>)
					ListUtilBean.toVO(MotivoCierre.getList(),
					new MotivoCierreVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			cierreComercioAdapter.setListAtributoObjImp(liqDeudaBeanHelper.getListAtrObjImp(true));
			cierreComercioAdapter.setListGestionDeudaAdmin(liqDeudaBeanHelper.getDeudaAdminForCierreComercio());
			

			if((cuenta.getFechaBaja()!=null)||(null!=cuenta.getEstCue()&&cuenta.getEstCue().getId().equals(EstCue.ID_BAJA_EN_TRAMITE)))
				cierreComercioAdapter.setPermiteIniCierreCom(false);
			else cierreComercioAdapter.setPermiteIniCierreCom(true);
			
			if(cierreComercioAdapter.getListGestionDeudaAdmin().size()>0){
				if(cierreComercioAdapter.isPermiteIniCierreCom())
					cierreComercioAdapter.addMessage(GdeError.MSG_NO_CONTINUAR_TRAMITE);
			}
			
			CierreComercio cierreComercio = CierreComercio.getByObjImp(cuenta.getObjImp());
			cierreComercioAdapter.getListMulta().clear();
			if(cierreComercio!=null){
				CierreComercioVO cierreComercioVO = (CierreComercioVO)cierreComercio.toVO(1);
				cierreComercioAdapter.setCierreComercio(cierreComercioVO);
				
				cierreComercioAdapter.getMotivoCierre().setDesMotivo((cierreComercio.getMotivoCierre()!=null)?cierreComercio.getMotivoCierre().getDesMotivo():"");
				
				// trae las multas solo en estado ACTIVO
				List<Multa> listMulta = Multa.getListByCuentaTipoMulta(idCuenta, TipoMulta.ID_CIERRE_COMERCIO);
				for(Multa multa:listMulta){
					MultaVO multaVO = (MultaVO)multa.toVO(1);
					DeudaVO deuda = (DeudaVO)Deuda.getById(multa.getIdDeuda()).toVO(1);
					multaVO.setDeuda(deuda);
					cierreComercioAdapter.getListMulta().add(multaVO);
					cierreComercioAdapter.getCierreComercio().getMulta().setId(multa.getId());
				}
			
				cierreComercioAdapter.getCierreComercio().getCaso().setIdCaso(cierreComercio.getIdCaso());
				cierreComercioAdapter.getCierreComercio().getCasoNoEmiMul().setIdCaso(cierreComercio.getIdCasoNoEmiMul());
				if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaCeseActividad(), DateUtil.addDaysToDate(new Date(),-90))){
					if(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento()!=null){
						if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento(),cierreComercioAdapter.getCierreComercio().getFechaCeseActividad())){
							cierreComercioAdapter.setAplicaMulta(true);
						}else{
							cierreComercioAdapter.setAplicaMulta(false);
						}
					}else{
						cierreComercioAdapter.setAplicaMulta(true);
					}
				}else{
					cierreComercioAdapter.setAplicaMulta(false);
				}
			}else{
				cierreComercioAdapter.setCierreComercio(new CierreComercioVO());
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cierreComercioAdapter;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CierreComercioAdapter createCierreComercio(UserContext userContext, CierreComercioAdapter cierreComercioAdapter)
	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cierreComercioAdapter.clearErrorMessages();
			
			if (cierreComercioAdapter.getCierreComercio().getFechaCeseActividad() == null){
				cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_FECHACESEACTIVIDAD);
			}
			MotivoCierre motivoCierre = MotivoCierre.getByIdNull(cierreComercioAdapter.getCierreComercio().getMotivoCierre().getId());
			
			if (motivoCierre==null){
				cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_MOTIVOCIERRE);
			}else{
				if(motivoCierre.getId()==MotivoCierre.ID_FALLECIMIENTO_TITULAR){
					if (cierreComercioAdapter.getCierreComercio().getFechaFallecimiento() == null){
						cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_FECHAFALLECIMIENTOTITULAR);
					}
				}
			}
			
			
			if (cierreComercioAdapter.hasError()) {
            	return cierreComercioAdapter;
			}
			
			if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaCeseActividad(), DateUtil.addDaysToDate(new Date(),-90))){
				if(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento()!=null){
					if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento(),cierreComercioAdapter.getCierreComercio().getFechaCeseActividad())){
						cierreComercioAdapter.setAplicaMulta(true);
					}else{
						cierreComercioAdapter.setAplicaMulta(false);
					}
				}else{
					cierreComercioAdapter.setAplicaMulta(true);
				}
			}else{
				cierreComercioAdapter.setAplicaMulta(false);
			}
			
			CierreComercio cierreComercio = new CierreComercio();
			cierreComercio.setFechaCeseActividad(cierreComercioAdapter.getCierreComercio().getFechaCeseActividad());
			cierreComercio.setFechaFallecimiento(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento());
			
			cierreComercio.setMotivoCierre(motivoCierre);
			cierreComercio.setFechaTramite(new Date());
			Cuenta cuenta = Cuenta.getById(cierreComercioAdapter.getIdCuenta());
			cierreComercio.setObjImp(cuenta.getObjImp());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CIERRE_COMERCIO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(cierreComercioAdapter.getCierreComercio(), cierreComercio, 
        			accionExp, cuenta, cierreComercio.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (cierreComercioAdapter.getCierreComercio().hasError()){
        		tx.rollback();        		
        		cierreComercioAdapter.getCierreComercio().passErrorMessages(cierreComercioAdapter);
        		return cierreComercioAdapter;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	cierreComercio.setIdCaso(cierreComercioAdapter.getCierreComercio().getIdCaso());
			
        	// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            cierreComercio = GdeGDeudaAutoManager.getInstance().createCierreComercio(cierreComercio);
            
			cierreComercioAdapter.getCierreComercio().setId(cierreComercio.getId());
			//cierreComercioAdapter.setAgregaCierre(false);
			
			if (cierreComercioAdapter.hasError()) {
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				tx.rollback();
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
			
		    log.debug(funcName + ": exit");
		    return cierreComercioAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			e.printStackTrace();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public CierreComercioAdapter updateCierreComercio(UserContext userContext, CierreComercioAdapter cierreComercioAdapter)
	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cierreComercioAdapter.clearErrorMessages();
			
			cierreComercioAdapter.setListMotivoCierre( (ArrayList<MotivoCierreVO>)
					ListUtilBean.toVO(MotivoCierre.getList(),
					new MotivoCierreVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			if (cierreComercioAdapter.getCierreComercio().getFechaCeseActividad() == null){
				cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_FECHACESEACTIVIDAD);
			}
			MotivoCierre motivoCierre = MotivoCierre.getByIdNull(cierreComercioAdapter.getCierreComercio().getMotivoCierre().getId());
			
			if (motivoCierre==null){
				cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_MOTIVOCIERRE);
			}else{
				if(motivoCierre.getId()==MotivoCierre.ID_FALLECIMIENTO_TITULAR){
					if (cierreComercioAdapter.getCierreComercio().getFechaFallecimiento() == null){
						cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_FECHAFALLECIMIENTOTITULAR);
					}
				}
			}
			
			if (cierreComercioAdapter.hasError()) {
            	return cierreComercioAdapter;
			}
			
			if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaCeseActividad(), DateUtil.addDaysToDate(new Date(),-90))){
				if(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento()!=null){
					if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento(),cierreComercioAdapter.getCierreComercio().getFechaCeseActividad())){
						cierreComercioAdapter.setAplicaMulta(true);
					}else{
						cierreComercioAdapter.setAplicaMulta(false);
					}
				}else{
					cierreComercioAdapter.setAplicaMulta(true);
				}
			}else{
				cierreComercioAdapter.setAplicaMulta(false);
			}

			if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaCeseActividad(), DateUtil.addDaysToDate(new Date(),-90))){
				if(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento()!=null){
					if(DateUtil.isDateBefore(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento(),cierreComercioAdapter.getCierreComercio().getFechaCeseActividad())){
						cierreComercioAdapter.setAplicaMulta(true);
					}else{
						cierreComercioAdapter.setAplicaMulta(false);
					}
				}else{
					cierreComercioAdapter.setAplicaMulta(true);
				}
			}else{
				cierreComercioAdapter.setAplicaMulta(false);
			}
			cierreComercioAdapter.getListMulta().clear();
			// trae las multas solo en estado ACTIVO
			List<Multa> listMulta = Multa.getListByCuentaTipoMulta(cierreComercioAdapter.getIdCuenta(), TipoMulta.ID_CIERRE_COMERCIO);
			for(Multa multa:listMulta){
				MultaVO multaVO = (MultaVO)multa.toVO(1);
				DeudaVO deuda = (DeudaVO)Deuda.getById(multa.getIdDeuda()).toVO(1);
				multaVO.setDeuda(deuda);
				cierreComercioAdapter.getListMulta().add(multaVO);
			}
			CierreComercio cierreComercio = CierreComercio.getById(cierreComercioAdapter.getCierreComercio().getId());
			cierreComercio.setFechaCeseActividad(cierreComercioAdapter.getCierreComercio().getFechaCeseActividad());
			cierreComercio.setFechaFallecimiento(cierreComercioAdapter.getCierreComercio().getFechaFallecimiento());
			cierreComercio.setMotivoCierre(motivoCierre);
			cierreComercio.setFechaTramite(new Date());
			Cuenta cuenta = Cuenta.getById(cierreComercioAdapter.getIdCuenta());
			cierreComercio.setObjImp(cuenta.getObjImp());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CIERRE_COMERCIO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(cierreComercioAdapter.getCierreComercio(), cierreComercio, 
        			accionExp, cuenta, cierreComercio.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (cierreComercioAdapter.getCierreComercio().hasError()){
        		tx.rollback();        		
        		cierreComercioAdapter.getCierreComercio().passErrorMessages(cierreComercioAdapter);
        		return cierreComercioAdapter;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	cierreComercio.setIdCaso(cierreComercioAdapter.getCierreComercio().getIdCaso());
			
			
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            cierreComercio = GdeGDeudaAutoManager.getInstance().updateCierreComercio(cierreComercio);

           
            if (cierreComercioAdapter.hasError()) {
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				tx.rollback();
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
		    log.debug(funcName + ": exit");
		    return cierreComercioAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			e.printStackTrace();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public CierreComercioAdapter getCierreComercioAdapterParamMotivoCierre(UserContext userContext,
			CierreComercioAdapter cierreComercioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			cierreComercioAdapter.clearError();
			
			//si no es nulo lo seleccionado
			if(!ModelUtil.isNullOrEmpty(cierreComercioAdapter.getCierreComercio().getMotivoCierre())){
				MotivoCierre motivoCierre = MotivoCierre.getById(cierreComercioAdapter.getCierreComercio().getMotivoCierre().getId());
				cierreComercioAdapter.getCierreComercio().setMotivoCierre((MotivoCierreVO)motivoCierre.toVO());
			}
			
			log.debug(funcName + ": exit");
			return cierreComercioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CierreComercioAdapter inicioCierreComercio(UserContext userContext, CierreComercioAdapter cierreComercioAdapter)
	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cierreComercioAdapter.clearErrorMessages();
			
			if (cierreComercioAdapter.getCierreComercio().getFechaCeseActividad() == null){
				cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_FECHACESEACTIVIDAD);
			}
			MotivoCierre motivoCierre = MotivoCierre.getByIdNull(cierreComercioAdapter.getCierreComercio().getMotivoCierre().getId());
			
			if (motivoCierre==null){
				cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_MOTIVOCIERRE);
			}else{
				if(motivoCierre.getId()==MotivoCierre.ID_FALLECIMIENTO_TITULAR){
					if (cierreComercioAdapter.getCierreComercio().getFechaFallecimiento() == null){
						cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.CIERRE_COMERCIO_FECHAFALLECIMIENTOTITULAR);
					}
				}
			}
			
			if(cierreComercioAdapter.getCierreComercio().getCaso().getIdFormateado()==null){
				cierreComercioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CIERRE_COMERCIO_CASO);
			}
			
			if(cierreComercioAdapter.hasError()){
				return cierreComercioAdapter;
			}
			
			CierreComercio cierreComercio = CierreComercio.getById(cierreComercioAdapter.getCierreComercio().getId());
			cierreComercioAdapter.getListMulta().clear();
			for(MultaVO multaVO: cierreComercioAdapter.getListMulta()){
				Multa multa = Multa.getById(multaVO.getId());
				Deuda deuda = Deuda.getById(multa.getIdDeuda());
				
				if(deuda.getEstadoDeuda().getId().longValue()!=EstadoDeuda.ID_ANULADA){
					cierreComercioAdapter.getCierreComercio().setMulta(multaVO);
					cierreComercio.setMulta(multa);
				}
			}
			if(cierreComercioAdapter.isAplicaMulta() && cierreComercioAdapter.getCierreComercio().getMulta().getId()==null){
				if(cierreComercioAdapter.getCierreComercio().getCasoNoEmiMul().getIdCaso()==null){
					cierreComercioAdapter.addRecoverableError(GdeError.CIERRE_COMERCIO_CASO_NOEMIMUL);
				}
			}
			
			
			if(cierreComercioAdapter.hasError()){
				return cierreComercioAdapter;
			}
			
			Cuenta cuenta = Cuenta.getById(cierreComercioAdapter.getIdCuenta());
			
			// 1) Registro uso de expediente de no emision multa
        	AccionExp accionExpIni = AccionExp.getById(AccionExp.ID_CIERRE_COMERCIO);
        	boolean casonulo=cierreComercioAdapter.getCierreComercio().getCaso()==null;
        	log.debug("caso nulo ="+String.valueOf(casonulo));
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(cierreComercioAdapter.getCierreComercio(), cierreComercio, 
        			accionExpIni, cuenta, cierreComercio.infoString() );
			
			// 2) Registro uso de expediente de no emision multa
        	if (cierreComercioAdapter.getCierreComercio().getCasoNoEmiMul().getIdCaso()!=null){
	        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CIERRE_COMERCIO_NOEMIMUL); 
	        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(cierreComercioAdapter.getCierreComercio().getCasoNoEmiMul(), cierreComercio, 
	        			accionExp, cuenta, cierreComercio.infoString() );
        	}
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (cierreComercioAdapter.getCierreComercio().hasError()){
        		tx.rollback();
        		cierreComercioAdapter.getCierreComercio().passErrorMessages(cierreComercioAdapter);
        		return cierreComercioAdapter;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	cierreComercio.setIdCasoNoEmiMul(cierreComercioAdapter.getCierreComercio().getCasoNoEmiMul().getIdCaso());
			
        	cierreComercio = GdeGDeudaAutoManager.getInstance().updateCierreComercio(cierreComercio);

        	List<Cuenta> listCuenta = cuenta.getListCuentaRelacionadas();
			listCuenta.add(cuenta);
			
			//Si los recursos de las cuentas relacionadas dan baja por baja de cta principal se dan de baja 
			for(Cuenta cta: listCuenta){
				if (cuenta.getObjImp().getCuentaPrincipal().equals(cuenta)||(cuenta.getRecurso().getBajaCtaPorPri()!=null && cuenta.getRecurso().getBajaCtaPorPri().intValue()==SiNo.SI.getId().intValue())){
					cta.setPermiteEmision(0);//No permite emision
					cta.setEstCue(EstCue.getById(EstCue.ID_BAJA_EN_TRAMITE));
					//- issue 8391: Modificación en Inicio de Cierre de Comercio
					cta.setFechaBaja(new Date());
					PadDAOFactory.getCuentaDAO().update(cta);
					if (cta.hasError())
						cta.passErrorMessages(cierreComercioAdapter);
				}
			}
			
			
			if (cierreComercioAdapter.hasError()) {
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				tx.rollback();
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
			
		    log.debug(funcName + ": exit");
		    return cierreComercioAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			e.printStackTrace();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public MultaAdapter createMultaHistorico(UserContext userContext,MultaAdapter multaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
		multaAdapter.clearError();
		List<MultaDetVO> listMultaDetVO = multaAdapter.getMulta().getListMultaDet();
		
		MultaHistoricoVO multaHistVO = multaAdapter.getMultaHistorico();
		
		//Validaciones
		if (multaHistVO.getImporteTotal()==null){
			if (multaHistVO.getPorcentaje() == null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTAHISTORICO_PORCENTAJE_LABEL);
			}
			if (multaHistVO.getPeriodoDesde() == null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTAHISTORICO_PERIODODESDE_LABEL);
			}
			if (multaHistVO.getPeriodoHasta() == null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTAHISTORICO_PERIODOHASTA_LABEL);
			}
			if (multaHistVO.getAnioDesde() == null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTAHISTORICO_ANIODESDE_LABEL);
			}
			if (multaHistVO.getAnioHasta() == null){
				multaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTAHISTORICO_ANIOHASTA_LABEL);
			}
		}
		if (multaAdapter.hasErrorRecoverable())
			return multaAdapter;
		
		if(listMultaDetVO.size()==0){
			multaAdapter.addRecoverableError(GdeError.MSG_MULTA_DETAJUDET_NOEXISTE);
			return multaAdapter;
		}else {
			//Verifico que se haya seleccionado un descuento
			
			multaAdapter.setTotalMulta(0D);
			multaAdapter.getMulta().getListMultaHistorico().add(multaAdapter.getMultaHistorico());
			
			if (multaHistVO.getImporteTotal()==null){
				Date fechaDesde = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(multaAdapter.getMultaHistorico().getPeriodoDesde().toString(),2)+"/"+multaAdapter.getMultaHistorico().getAnioDesde(), DateUtil.ddSMMSYYYY_MASK);
				Date fechaHasta = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(multaAdapter.getMultaHistorico().getPeriodoHasta().toString(),2)+"/"+multaAdapter.getMultaHistorico().getAnioHasta(), DateUtil.ddSMMSYYYY_MASK);
				
				for (MultaDetVO multaDet : listMultaDetVO){
					//crear un multadet por cada elemento de la lista y setearle detajudet
					Date perAnio = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(multaDet.getPeriodo().toString(),2)+"/"+multaDet.getAnio(), DateUtil.ddSMMSYYYY_MASK);
					log.debug("fechaDesde: "+fechaDesde);
					log.debug("fechaHasta: "+fechaHasta);
					log.debug("perAnio: "+perAnio);
					if (DateUtil.isDateAfterOrEqual(perAnio, fechaDesde)&& DateUtil.isDateBeforeOrEqual(perAnio, fechaHasta)){
					
						multaDet.setPorOri(multaAdapter.getMultaHistorico().getPorcentaje());											
						multaDet.setPorApl(multaDet.getPorOri() * (1-multaDet.getPorDes()));
						multaDet.setImporteAplicado(multaDet.getImporteAct() * multaDet.getPorApl());	
						
						// seteo total de la multa
						
					}
					multaAdapter.setTotalMulta(multaAdapter.getTotalMulta() + multaDet.getImporteAplicado());
				}
			}else{
				multaAdapter.setTotalMulta(multaHistVO.getImporteTotal());
			}
		}
		multaAdapter.getMulta().setImporte(multaAdapter.getTotalMulta());
		multaAdapter.getMulta().setListMultaDet(listMultaDetVO);
		
		multaAdapter.setMultaHistorico(new MultaHistoricoVO());
  
		
		return multaAdapter;
		
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// ---> ABM CierreComercio
	public CierreComercioAdapter getCierreComercioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CierreComercio cierreComercio = CierreComercio.getById(commonKey.getId());

	        CierreComercioAdapter cierreComercioAdapter = new CierreComercioAdapter();

	        cierreComercioAdapter.setCierreComercio((CierreComercioVO) cierreComercio.toVO(2));
	        
	        Cuenta cuenta = cierreComercio.getObjImp().getCuentaPrincipal();
	        
	        cierreComercioAdapter.getCierreComercio().setCuentaVO((CuentaVO) cuenta.toVOWithRecurso());
	        
	        cierreComercioAdapter.getCierreComercio().getCuentaVO().setId(cuenta.getId());

	        cierreComercioAdapter.getCierreComercio().setFechaCierreDef(cierreComercio.getFechaCierreDef());
	        
	        log.debug(funcName + ": exit");
			return cierreComercioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public CierreComercioAdapter getCierreComercioAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			
			SiatHibernateUtil.currentSession(); 

			
			CierreComercio cierreComercio = CierreComercio.getById(commonKeyPlan.getId());
			
	        CierreComercioAdapter cierreComercioAdapter = new CierreComercioAdapter();
	        cierreComercioAdapter.setCierreComercio((CierreComercioVO) cierreComercio.toVO(2));

		    cierreComercioAdapter.setCierreComercio((CierreComercioVO) cierreComercio.toVO(2));
		    
		    cierreComercioAdapter.setCierreDefinitivo(true);
	        
	        Cuenta cuenta = cierreComercio.getObjImp().getCuentaByRecurso(Recurso.getDReI());
	        
	        if (cuenta!=null) cierreComercioAdapter.getCierreComercio().setCuentaVO((CuentaVO) cuenta.toVOWithRecurso());

	        cierreComercioAdapter.getCierreComercio().setFechaCierreDef(cierreComercio.getFechaCierreDef());
	        
			log.debug(funcName + ": exit");
			return cierreComercioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CierreComercioVO updateCierreComercio(UserContext userContext, CierreComercioVO cierreComercioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cierreComercioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            CierreComercio cierreComercio = CierreComercio.getById(cierreComercioVO.getId());
            
            cierreComercio.setFechaCierreDef(cierreComercioVO.getFechaCierreDef());
            cierreComercio.setFechaCeseActividad(cierreComercioVO.getFechaCeseActividad());
            
            if(cierreComercioVO.getCaso().getAccion().equals("eliminar"))
            	cierreComercio.setIdCaso(null);
            else
            	cierreComercio.setIdCaso(cierreComercioVO.getCaso().getIdFormateado());
            
            log.debug("idCaso: "+cierreComercio.getIdCaso());
            if(cierreComercio.getFechaCierreDef()!=null){
            
	            ObjImp objImp = cierreComercio.getObjImp();
	            
	            Cuenta ctaPriAct = objImp.getCuentaPrincipal();
	            
	            ctaPriAct.setFechaBaja(cierreComercioVO.getFechaCierreDef());
	            ctaPriAct.setEstCue(EstCue.getById(EstCue.ID_BAJA_CIERRE_DEFINITIVO));
	             
	            PadDAOFactory.getCuentaDAO().update(ctaPriAct);
	            
	            if(ctaPriAct.getRecurso().getBajaCtaPorIface()!=null && ctaPriAct.getRecurso().getBajaCtaPorIface().intValue() == SiNo.SI.getId().intValue()){
	            	for(Cuenta c: objImp.getListCuentaSecundariaActiva()){
	            		c.setFechaBaja(cierreComercioVO.getFechaCierreDef());
	            		c.setEstCue(EstCue.getById(EstCue.ID_BAJA_CIERRE_DEFINITIVO));
	            		PadDAOFactory.getCuentaDAO().update(c);
	            	}
	            }
            }
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            cierreComercio = PadObjetoImponibleManager.getInstance().updateCierreComercio(cierreComercio);
            
            if (cierreComercio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cierreComercioVO =  (CierreComercioVO) cierreComercio.toVO(1);
			}
            cierreComercio.passErrorMessages(cierreComercioVO);
            
            log.debug(funcName + ": exit");
            return cierreComercioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM CierreComercio
	
	// ---> Declaracion Jurada Masiva
	public LiqDecJurAdapter getLiqDecJurAdapterInit(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Cuenta cuenta = Cuenta.getByIdNull(liqDecJurAdapterVO.getCuenta().getIdCuenta());
			
			if (cuenta == null){
				liqDecJurAdapterVO.addNonRecoverableError("No se pudo recuperar la cuenta");
				return liqDecJurAdapterVO;
			}
			
			// Seteamos banderola es ETur
			if (cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
				liqDecJurAdapterVO.setEsEtur(true);
			}
			
			LiqDeudaBeanHelper ldbh = new LiqDeudaBeanHelper(cuenta);
			
			liqDecJurAdapterVO.setCuenta(ldbh.getCuenta());
			
			
			// Validarmos que exista deuda seleccionada.
			if (liqDecJurAdapterVO.getListIdDeudaSelected() == null ||
					liqDecJurAdapterVO.getListIdDeudaSelected().length == 0 ){
				
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar los periodos a incluir en la Declaracion Jurada");
				
				return liqDecJurAdapterVO;
			} else {
				
				LiqRecMinVO periodo;
				boolean existePeriodo = false;
				for(String idDeudaEstado: liqDecJurAdapterVO.getListIdDeudaSelected()){
					String[] split = idDeudaEstado.split("-");
					long idDeuda = Long.parseLong(split[0]);
					long idEstadoDeuda = Long.parseLong(split[1]);
					Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
					
					// Bug 807: El recurso Etur es autoliquidable a partir del periodo 01/2006 
					if (liqDecJurAdapterVO.getEsEtur() && deuda.getAnio().intValue() < 2006){
						liqDecJurAdapterVO.addRecoverableValueError("Para ETuR debe seleccionar periodos posteriores al 01 del 2006");
						return liqDecJurAdapterVO;
					}
					
					// Restrinccion para DRei/ETur a partir de la fecha determinada por los parametros correspondiente. 
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
					if (!StringUtil.isNullOrEmpty(listaIdRecurso) && fechaRestriccion != null && DateUtil.isDateAfterOrEqual(deuda.getFechaVencimiento(), fechaRestriccion)
							&& listaIdRecurso.indexOf(idRecursoStr) >= 0){
						liqDecJurAdapterVO.addRecoverableError(GdeError.DECJURADAPTER_DEUDA_RESTRINCCION_AFIP);
						return liqDecJurAdapterVO;
					}
					
					// Si el periodo no exite, lo agregamos
					for (LiqRecMinVO liqRecMinVO:liqDecJurAdapterVO.getListPeriodos()){
						if (deuda.getPeriodo().intValue() == liqRecMinVO.getPeriodo().intValue() &&
								  deuda.getAnio().intValue() == liqRecMinVO.getAnio().intValue()){
							existePeriodo = true;
							break;
						}
					}
					
					if (!existePeriodo){
							// El id del periodo es el numero de orden en el que se encuentra.
							periodo = new LiqRecMinVO(deuda.getPeriodo().intValue(), 
													  deuda.getAnio().intValue(),
									                  new Long(liqDecJurAdapterVO.getListPeriodos().size()));
							
							liqDecJurAdapterVO.getListPeriodos().add(periodo);
					}
					
					existePeriodo = false;
				}	
				
				liqDecJurAdapterVO.cargarPeriodos();
			}
			
			Recurso drei = Recurso.getByCodigo(Recurso.COD_RECURSO_DReI);
			
			List<RecConADec> listRecConADec= DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(drei.getId(), 
					new Date(),
					TipRecConADec.ID_CONCEPTOS);
			
			liqDecJurAdapterVO.getListActividad().add(new RecConADecVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			liqDecJurAdapterVO.getListActividad().addAll(ListUtilBean.toVO(listRecConADec));
			
			log.debug(funcName + ": exit");
			return liqDecJurAdapterVO;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public LiqDecJurAdapter getLiqDecJurAdapterDetalle(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			liqDecJurAdapterVO.clearError();
			
			// Validamos que existan Actividades 
			if (liqDecJurAdapterVO.getListActividadDec().size() == 0){
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar al menos una actividad");
			}
			
			// Validamos que existan Cantidad de personal para todos los rangos.
			if (liqDecJurAdapterVO.getEsDReI() && liqDecJurAdapterVO.getListPeriodosHasta().size() > 0){
				liqDecJurAdapterVO.addRecoverableValueError("Debe indicar cantidad de personal para todos los periodos seleccionados");
			}
			
			if (liqDecJurAdapterVO.hasError()){
				return liqDecJurAdapterVO;
			}
			
			// Generamos grilla
			liqDecJurAdapterVO.resetForDetalle();
			
			LiqDetalleDecJurVO detalle;
			for (LiqRecMinVO periodo:liqDecJurAdapterVO.getListPeriodos()){
				
				for(RecConADecVO actividad:liqDecJurAdapterVO.getListActividadDec() ){
					
					detalle = new LiqDetalleDecJurVO(); 
					
					detalle.setId(new Long(liqDecJurAdapterVO.getListDetalle().size()));
					
					detalle.setActividad(actividad);
					detalle.setPeriodo(periodo.getPeriodo());
					detalle.setAnio(periodo.getAnio());
					
					liqDecJurAdapterVO.getListDetalle().add(detalle);
				}
			}
			
			Cuenta cuenta = Cuenta.getByIdNull(liqDecJurAdapterVO.getCuenta().getIdCuenta());
			
			// Valores para DReI ################################################
			if (liqDecJurAdapterVO.getEsDReI()){
				//  Unidad
				List<RecTipUni> listRecTipUni = DefDAOFactory.getRecTipUniDAO().getListTipUniByRecurso(cuenta.getRecurso());
				
				liqDecJurAdapterVO.getListUnidad().add(new RecTipUniVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				liqDecJurAdapterVO.getListUnidad().addAll(ListUtilBean.toVO(listRecTipUni));
				 
				// Tipos Unidad
				List<RecConADec> listTiposUnidad = DefDAOFactory.getRecConADecDAO().getListTipoUnidadVigenteByRecurso4Map(cuenta.getRecurso(), new Date());
				
				liqDecJurAdapterVO.getListTipoUnidad().add(new RecConADecVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				
				// Valor para opcion seleccionar
				List<RecConADecVO> listTipoUnidad =  new ArrayList<RecConADecVO>();
				listTipoUnidad.add(new RecConADecVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				liqDecJurAdapterVO.getMapTipoUnidad().put(-1L, listTipoUnidad); 
				
				for (RecConADec recConADec:listTiposUnidad){
					
					RecConADecVO  recConADecVO = (RecConADecVO) recConADec.toVO4Map(); 
					
					// MapTipoUnidad: clave -> id recTipUni    (Unidad)
					//				  valor -> list recConADec (Tipo Unidad)		
					//Si existe se lo agregamos a la lista
					if(liqDecJurAdapterVO.getMapTipoUnidad().containsKey(recConADecVO.getRecTipUni().getId())){
						liqDecJurAdapterVO.getMapTipoUnidad().get(recConADecVO.getRecTipUni().getId()).add(recConADecVO);
						
					} else {
					// Si no existe lo creamos y agregamos la opcion seleccionar
						listTipoUnidad =  new ArrayList<RecConADecVO>();
						listTipoUnidad.add(recConADecVO);
						liqDecJurAdapterVO.getMapTipoUnidad().put(recConADecVO.getRecTipUni().getId(), listTipoUnidad); 
					}
				}
				
				// Recuperamos la lista de alicuotas
				List<RecAli> listAliCuotas = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(
						cuenta.getRecurso().getId(), new Date(),RecTipAli.COD_OGI);
				
				liqDecJurAdapterVO.setListAlicuota(ListUtilBean.toVO(listAliCuotas, new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				liqDecJurAdapterVO.setAlicuota(new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			// Valores para ETur  ################################################	
			} else {
				
				Recurso drei = Recurso.getByCodigo(Recurso.COD_RECURSO_DReI);
				
				Cuenta cuentaDrei = Cuenta.getByIdRecursoYNumeroCuenta(drei.getId(), cuenta.getNumeroCuenta());
				
				// Levantamos el monto imponible de detJurDet para el mesmo periodo y actividad.
				for(LiqDetalleDecJurVO liqDetalleDecJurVO:liqDecJurAdapterVO.getListDetalle()){
					
					DecJurDet decJurDet = DecJurDet.getByCuentaPeriodoAnioyActividad(
							cuentaDrei,
							liqDetalleDecJurVO.getPeriodo(), 
							liqDetalleDecJurVO.getAnio(),
							liqDetalleDecJurVO.getActividad().getId());
					
					if (decJurDet != null && decJurDet.getBase() != null){
						liqDetalleDecJurVO.setMontoImponible(decJurDet.getBase());
					}
					
				}
				
				List<ValUnRecConADe> listValUnRecConADe = ValUnRecConADe.getListActivos();
				
				RecAliVO recAliVO;
				List<RecAliVO> listAliCuotas = new ArrayList<RecAliVO>();
				recAliVO = new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR);
				recAliVO.setAlicuota(0D);
				recAliVO.setAlicuotaView(StringUtil.SELECT_OPCION_SELECCIONAR);
				listAliCuotas.add(recAliVO);

				ValUnRecConADeVO minimo;
				List<ValUnRecConADeVO> listMinimos = new ArrayList<ValUnRecConADeVO>();
				minimo = new ValUnRecConADeVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR);
				listMinimos.add(minimo);
				
				// Recuperamos la lista de alicuotas
				for(ValUnRecConADe valUnRecConADe:listValUnRecConADe){
					
					if(valUnRecConADe.getRecConADec().getTipRecConADec().getId().longValue() == 
						TipRecConADec.ID_TIPO_ACTIVIDAD_ESPECIFICA_ETUR.longValue()){
						
						if (valUnRecConADe.getRecAli() != null){
							
							recAliVO = (RecAliVO) valUnRecConADe.getRecAli().toVO(0, false);

							if (!this.contains(listAliCuotas, recAliVO)){
								listAliCuotas.add(recAliVO);
							}
							
							minimo = (ValUnRecConADeVO) valUnRecConADe.toVO(0, false);
							listMinimos.add(minimo);
						}
					}				
				}
				
				liqDecJurAdapterVO.setListAlicuota(listAliCuotas);
				liqDecJurAdapterVO.setAlicuota(new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
				liqDecJurAdapterVO.setListMinimo(listMinimos);
				liqDecJurAdapterVO.setMinimo(new ValUnRecConADeVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			}
			
			log.debug(funcName + ": exit");
			return liqDecJurAdapterVO;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Devuelve true o false se una alicuota ya existe dentro de una lista.
	 * 
	 * @param listAliCuotas
	 * @param alicuota
	 * @return
	 */
	private boolean contains(List<RecAliVO> listAliCuotas, RecAliVO alicuota){
		for(RecAliVO recAli:listAliCuotas){
			if(recAli.getAlicuota().equals(alicuota.getAlicuota())){
				return true;
			}
		}
		return false;	
	}
	
	
	public LiqDecJurAdapter getLiqDecJurAdapterGeneral(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			
			// Validamos .... 
			for(LiqRecMinVO liq:liqDecJurAdapterVO.getListRecMin()){
				log.debug(" Per: " + liq.getId() + " d:" +  liq.getPeriodoDesdeView() + " h:" + liq.getPeriodoHastaView() + " val:" + liq.getValor());
			}
			
			// Generamos grilla general
			liqDecJurAdapterVO.resetForGeneral();
			
			RecMinDec recMinDec;
			
			Cuenta cuenta = Cuenta.getByIdNull(liqDecJurAdapterVO.getCuenta().getIdCuenta());
			
			LiqDetalleDecJurVO general;
			Integer cantPersonal, periodo, anio;
			Double maxTotalDetalle, subTotal1, subTotal2, totalConcepto;
			Date fecVig;
			
			List<Long> listIdRecClaDeu = new ArrayList<Long>();
			
			listIdRecClaDeu.add(RecClaDeu.ID_DDJJ_ORIGINAL);
			listIdRecClaDeu.add(RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI);
			listIdRecClaDeu.add(Recurso.getETur().getRecClaDeuOriginal(new Date()).getId());
			listIdRecClaDeu.add(RecClaDeu.ID_DDJJ_RECTIFICATIVA_ETUR);
			
			TipoPago tipoPagoRetencion = TipoPago.getById(TipoPago.ID_RETENCION_DECLARADA); 
			
			List<Long> listIdDeudas=new ArrayList<Long>();
			// Para cada periodo creamos un objeto Detalle (para la seccion general)
			for (LiqRecMinVO liqPeriodo:liqDecJurAdapterVO.getListPeriodos()){
				
				general = new LiqDetalleDecJurVO(); 
				
				general.setId(new Long(liqDecJurAdapterVO.getListGeneral().size()));
				
				periodo = liqPeriodo.getPeriodo();
				anio = liqPeriodo.getAnio();
							
				cantPersonal = liqDecJurAdapterVO.getCantPersonal(periodo, anio);
				
				log.debug(" per: " + periodo +"/"+anio  + " cantPersonal: " + cantPersonal);
				
				// Fecha de vigencia es el primer dia del periodo.
				fecVig = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(periodo.toString(),2)+"/"+ anio,DateUtil.ddSMMSYYYY_MASK);
				
				log.debug(" fechaVig: " + DateUtil.formatDate(fecVig, DateUtil.ddSMMSYYYY_MASK));
				
				// Buscamos el minimo vigente.
				recMinDec = RecMinDec.getVigente(cuenta.getRecurso(), new Double(cantPersonal), fecVig);
				
				if (recMinDec != null){
					general.setMinCantPer(recMinDec.getMinimo());
				} else { 
					general.setMinCantPer(0D);
				}
				
				general.setCantPer(cantPersonal);
				general.setPeriodo(periodo);
				general.setAnio(anio);
				
				maxTotalDetalle = 0D;
				
				// Obtenemos el mayor de los totales del detalle para el periodo
				for (LiqDetalleDecJurVO detalle:liqDecJurAdapterVO.getListDetalle()){
					
					log.debug(" compara: " + periodo + "/" + anio + "   ----> " + detalle.getPeriodo() + "/" + detalle.getAnio() );
					
					totalConcepto = 0D;

					// Dentro del mismo periodo 
					if (detalle.getPeriodo().intValue() == periodo.intValue() && 
							detalle.getAnio().intValue() == anio.intValue()){
						
						if (detalle.getSubTotal1() != null )
							subTotal1 = detalle.getSubTotal1();
						else 
							subTotal1 = 0D;
						
						if(detalle.getSubTotal2() != null)
							subTotal2 = detalle.getSubTotal2();
						else
							subTotal2 = 0D;	
							
						log.debug(" 	subTotal1: "  + subTotal1 + " subTotal2: " + subTotal2); 
								
						if (subTotal1.doubleValue() > subTotal2.doubleValue()){
							maxTotalDetalle += subTotal1;
							totalConcepto = subTotal1;
						} else { 
							maxTotalDetalle += subTotal2;
							totalConcepto = subTotal2;
						}
						
						// Creamos los DetJurDetVO, para utilizar en la simulacion.
						
						DecJurDetVO decJurDetVO = new DecJurDetVO();
						
						decJurDetVO.setBase(detalle.getMontoImponible());
						decJurDetVO.setMultiplo(detalle.getAlicuota());
						decJurDetVO.setSubtotal1(detalle.getSubTotal1());

						decJurDetVO.setCanUni(detalle.getCantUni()==null?0:new Double(detalle.getCantUni()));
						decJurDetVO.setRecConADec(detalle.getActividad());
						decJurDetVO.setRecTipUni(detalle.getUnidad());
						decJurDetVO.setUnidad(detalle.getUnidad().getNomenclatura());
						decJurDetVO.setTipoUnidad(detalle.getTipoUnidad());
						decJurDetVO.setDesTipoUnidad(detalle.getTipoUnidad().getDesConcepto());
						decJurDetVO.setValUnidad(detalle.getValorUni());
						decJurDetVO.setSubtotal2(detalle.getSubTotal2());
						decJurDetVO.setTotalConcepto(totalConcepto);
						
						general.getListDetJurDet().add(decJurDetVO);
					}
				}
				
				log.debug("maxTotalDetalle: " + maxTotalDetalle);
				log.debug("MinCantPer: " + general.getMinCantPer());
				
				if (maxTotalDetalle.doubleValue() > general.getMinCantPer().doubleValue())
					general.setDeterminado(maxTotalDetalle);
				else
					general.setDeterminado(general.getMinCantPer());
				
				
				// Inicializacion de valores
				general.setAdicPub(0D);
				general.setAdicOtro(0D);
				general.setRetencion(0D);
				general.setPago(0D);
				general.setDeclarado(0D);
				general.setEnConvenio(0D);
				
				// Buscamos las deuda para el periodo y clasificacion Original o Rectificativa
				List<Deuda>  listDeuda = DeudaAdmin.getListByCuentaPeriodoAnioIdsRecClaDeu(cuenta, periodo, anio, listIdRecClaDeu);
				
				List<PagoDeuda> listPago = new ArrayList<PagoDeuda>();
				// Buscamos pagos para el periodo.
				for (Deuda deuda:listDeuda){
					log.debug(" #########################  ids Deuda " + deuda.getId());
					listPago.addAll(PagoDeuda.getByDeuda(deuda.getId()));
					
					// Acumulamos en Declarado
					if (deuda.getIdConvenio() == null){
						general.setDeclarado(general.getDeclarado() + deuda.getImporte());
					} else {
					// Acumulamos en enConvenio	
						general.setEnConvenio(general.getEnConvenio() + deuda.getImporte());
					} 
					// Lista de IdDeuda para Chequer si se esta procesando en un Asentamiento
					listIdDeudas.add(deuda.getId());					
				}
				
				// Separamos los en Pagos y Retenciones 
				for (PagoDeuda pagoDeuda: listPago){
					
					// Retencion
					if (pagoDeuda.getTipoPago().getId().intValue() == tipoPagoRetencion.getId().intValue()){
						general.setRetencion(general.getRetencion() + pagoDeuda.getImporte());
					} else {
					// Otros Pagos	
						general.setPago(general.getPago() + pagoDeuda.getImporte());
					}
					
				}
								
				general.calcularTotal();
				
				liqDecJurAdapterVO.getListGeneral().add(general);
				
			}
			
			// Verifica que la deuda no se encuentre en un proceso de Asentamiento
			List<Long> listIdEnAsentam=Deuda.getListIdDeudaAuxPagDeu(listIdDeudas);
			if(!ListUtil.isNullOrEmpty(listIdEnAsentam)){
				String msg="";
				msg = (listIdEnAsentam.size()>1)?"Los per\u00EDodos ":"El per\u00EDodo ";
				int count=0;
				for (Long id:listIdEnAsentam){
					msg+=(count==0)?"":", ";
					msg+=Deuda.getById(id).getStrPeriodo();
					count++;
				}
				msg += (count>1)?" se encuentran en proceso de asentamiento de pagos":" se encuentra en proceso de asentamiento de pago";
				liqDecJurAdapterVO.addRecoverableValueError(msg);
			}
			if(liqDecJurAdapterVO.hasError()){
				log.debug(funcName + ": exit");
				return liqDecJurAdapterVO;
			}
			
			// Lista para la seccion retenciones
			List<AgeRet> listAgeRet = AgeRet.getListActivosByRecurso(cuenta.getRecurso());
			liqDecJurAdapterVO.setListAgeRet(ListUtilBean.toVO(listAgeRet,1));
			
			
			// Listas de Alicuotas
			List<RecAli> listAliPub = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(
					cuenta.getRecurso().getId(), new Date(),RecTipAli.COD_PUBLICIDAD);
			
			List<RecAli> listAliMyS = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(
					cuenta.getRecurso().getId(), new Date(),RecTipAli.COD_MESAS_Y_SILLAS);
			
			liqDecJurAdapterVO.setListAliPub(ListUtilBean.toVO(listAliPub, new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			liqDecJurAdapterVO.setListAliMyS(ListUtilBean.toVO(listAliMyS, new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			liqDecJurAdapterVO.setAliPub(new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			liqDecJurAdapterVO.setAliMyS(new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return liqDecJurAdapterVO;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public LiqDecJurAdapter getLiqDecJurAdapterSimular(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			
			// Validamos .... 
			liqDecJurAdapterVO.clearError();
			// Validamos fecha Formalizacion
			if (liqDecJurAdapterVO.getFechaFormalizacion() == null ){
				liqDecJurAdapterVO.addRecoverableValueError("La fecha de Formalizacion del convenio es requerida");				
				return liqDecJurAdapterVO;
			} 
			
			List<DecJur> listDecJur = new ArrayList<DecJur>();
			List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
			
			Long idCuenta = liqDecJurAdapterVO.getCuenta().getIdCuenta();
			
			// Para cada periodo, simularCrearDecJur
			for (LiqDetalleDecJurVO general:liqDecJurAdapterVO.getListGeneral()){
				
				DecJurVO decJurVO = new DecJurVO(); 
				
				decJurVO.setValRefMin(general.getCantPer().doubleValue());
				decJurVO.setPeriodo(general.getPeriodo());
				decJurVO.setAnio(general.getAnio());
				decJurVO.getCuenta().setId(idCuenta);
				
				decJurVO.setAliPub(general.getPorcAdicPub());
				decJurVO.setAliMesYSil(general.getPorcAdicOtro());
				
				decJurVO.setTotalPublicidad(general.getAdicPub());
				decJurVO.setTotMesYSil(general.getAdicOtro());
				
				log.debug(funcName + " ***********************************************************");
				log.debug(funcName + " decJur: " + general.getPeriodo() + "/" + general.getAnio());

				// Pasamos las retenciones
				for(DecJurPagVO decJurPagVO:general.getListDecJurPag()){
					decJurPagVO.getTipPagDecJur().setId(TipPagDecJur.ID_RETENCION);
					log.debug(funcName + " 	retencion: " + decJurPagVO.getCertificado() + " - " + decJurPagVO.getImporte()); 
					decJurVO.getListDecJurPag().add(decJurPagVO);
				}
				
				// Si posee otros pagos, los pasamos tambien.
				if (general.getMayorOtrosPagos() != null && general.getMayorOtrosPagos().doubleValue() > 0){
					DecJurPagVO decJurPagVO = new DecJurPagVO();
					decJurPagVO.getTipPagDecJur().setId(TipPagDecJur.ID_DJMISMOPERIODO);
					decJurPagVO.setImporte(general.getMayorOtrosPagos());
					decJurVO.getListDecJurPag().add(decJurPagVO);
				}
				
				// Pasamos los valores para crear los Detalles (detJurDet)
				decJurVO.getListDecJurDet().addAll(general.getListDetJurDet());
				
				// LLamamos a la simulacion
				decJurVO = this.createDecJur(decJurVO, listDecJur, false); 
				
				if (decJurVO.hasError()){
					decJurVO.passErrorMessages(liqDecJurAdapterVO);
				}
			}
			
			// Si todos son validos
			// Para cada decJur creado (simulado), corremos simularProcesarDDJJ 
			if (!liqDecJurAdapterVO.hasError()){
				
				for (DecJur decJur:listDecJur){
					
					// procesar ....
					log.debug(funcName + " ##########################################################");
					log.debug(" procesarDDJJ " + decJur.getPeriodo() + "/" + decJur.getAnio());
					decJur = decJur.procesarDDJJ(listDeuda, liqDecJurAdapterVO.getFechaFormalizacion(), false);
					
					if (decJur.hasError()){
						decJur.passErrorMessages(liqDecJurAdapterVO);
					}
				}
				
				for (LiqDeudaVO liqDeudaVO:listDeuda){
					// Si hay al meno un regitro de deuda CREADO o MODIFICADO.
					if(!StringUtil.isNullOrEmpty(liqDeudaVO.getDesEstado())){
						liqDecJurAdapterVO.setGenerarDeudaEnabled(true);
					}
				}

				if (!liqDecJurAdapterVO.isGenerarDeudaEnabled()){
					liqDecJurAdapterVO.addMessageValue("La declaracion no genera ni modifica registros de deuda");
				}
				
				// Setamos la lista para mostrarla
				liqDecJurAdapterVO.setListDeudaSimulada(listDeuda); 
				liqDecJurAdapterVO.calcularTotalDeuda();
			}
			
			log.debug(funcName + ": exit");
			return liqDecJurAdapterVO;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	
	public LiqDecJurAdapter getLiqDecJurAdapterSimularAFecha(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			
			// Validamos .... 
			liqDecJurAdapterVO.clearError();
			// Validamos fecha Formalizacion
			if (liqDecJurAdapterVO.getFechaFormalizacion() == null ){
				liqDecJurAdapterVO.addRecoverableValueError("La fecha de Formalizacion del convenio es requerida");				
				return liqDecJurAdapterVO;
			} 
			
			for (LiqDeudaVO liqDeuda:liqDecJurAdapterVO.getListDeudaSimulada()){
				
				DeudaAct deudaAct = ActualizaDeuda.actualizar(liqDecJurAdapterVO.getFechaFormalizacion(), 
													liqDeuda.getFechaVencimiento(), 
													liqDeuda.getSaldo(), false, true);
				
				liqDeuda.setActualizacion(deudaAct.getRecargo());
	            liqDeuda.setTotal(deudaAct.getImporteAct());
				
			}
			
			liqDecJurAdapterVO.calcularTotalDeuda();
			
			log.debug(funcName + ": exit");
			return liqDecJurAdapterVO;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public LiqDecJurAdapter getLiqDecJurAdapterGenerar(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Validamos .... 
			liqDecJurAdapterVO.clearError();
			
			// Validamos fecha Formalizacion
			if (liqDecJurAdapterVO.getFechaFormalizacion() == null ){
				liqDecJurAdapterVO.addRecoverableValueError("La fecha de Formalizacion del convenio es requerida");				
			} 
			
			if (liqDecJurAdapterVO.hasError()){
				return liqDecJurAdapterVO;
			}
			
			List<DecJur> listDecJur = new ArrayList<DecJur>();
			List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
			
			Long idCuenta = liqDecJurAdapterVO.getCuenta().getIdCuenta();
			
			// Para cada periodo, simularCrearDecJur
			for (LiqDetalleDecJurVO general:liqDecJurAdapterVO.getListGeneral()){
				
				DecJurVO decJurVO = new DecJurVO(); 
				
				decJurVO.setValRefMin(general.getCantPer().doubleValue());
				decJurVO.setPeriodo(general.getPeriodo());
				decJurVO.setAnio(general.getAnio());
				decJurVO.getCuenta().setId(idCuenta);
				
				decJurVO.setAliPub(general.getPorcAdicPub());
				decJurVO.setAliMesYSil(general.getPorcAdicOtro());
				
				decJurVO.setTotalPublicidad(general.getAdicPub());
				decJurVO.setTotMesYSil(general.getAdicOtro());
				
				// Pasamos las retenciones
				for(DecJurPagVO decJurPagVO:general.getListDecJurPag()){
					decJurPagVO.getTipPagDecJur().setId(TipPagDecJur.ID_RETENCION);
					decJurVO.getListDecJurPag().add(decJurPagVO);
				}
				
				// Si posee otros pagos, los pasamos tambien.
				if (general.getMayorOtrosPagos() != null && general.getMayorOtrosPagos().doubleValue() > 0){
					DecJurPagVO decJurPagVO = new DecJurPagVO();
					decJurPagVO.getTipPagDecJur().setId(TipPagDecJur.ID_DJMISMOPERIODO);
					decJurPagVO.setImporte(general.getMayorOtrosPagos());
					decJurVO.getListDecJurPag().add(decJurPagVO);
				}
				
				// Pasamos los valores para crear los Detalles (detJurDet)
				decJurVO.getListDecJurDet().addAll(general.getListDetJurDet());
								
				// LLamamos a la simulacion
				decJurVO = this.createDecJur(decJurVO, listDecJur, true); 
				
				if (decJurVO.hasError()){
					decJurVO.passErrorMessages(liqDecJurAdapterVO);
				}
			}
			
			// Si todos son validos
			// Para cada decJur creado (simulado), corremos simularProcesarDDJJ 
			if (!liqDecJurAdapterVO.hasError()){
				
				for (DecJur decJur:listDecJur){
					
					// procesar ....
					log.debug(" procesarDDJJ " + decJur.getPeriodo() + "/" + decJur.getAnio());
					decJur = decJur.procesarDDJJ(listDeuda, liqDecJurAdapterVO.getFechaFormalizacion(), true);
					
					if (decJur.hasError()){
						decJur.passErrorMessages(liqDecJurAdapterVO);
					}
				}
				
				// Armamos la lista de ids de deuda para la formalizacion de convenio.
				if (!liqDecJurAdapterVO.hasError()){
					
					List<String> listIds = new ArrayList<String>();
					for(LiqDeudaVO liqDeudaVO: listDeuda){
						
						Deuda deuda = Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda());
						// Para el convenio, solo agregamos deuda que no este en convenio
						// y que tenga importe mayor a cero
						if (deuda.getIdConvenio() == null && deuda.getSaldo().doubleValue() > 0){
							listIds.add(liqDeudaVO.getIdDeuda().toString() + "-" + liqDeudaVO.getIdEstadoDeuda().toString());
						}
					}
					
					String[] listIsDeudaGenerada = new String[listIds.size()];
					
					for (int i=0; i < listIds.size(); i++){
						listIsDeudaGenerada[i] = listIds.get(i);
					}
					
					liqDecJurAdapterVO.setListIdDeudaGenerada(listIsDeudaGenerada); 
					liqDecJurAdapterVO.calcularTotalDeuda();
				}
			}
			
			
			if (liqDecJurAdapterVO.hasError()) {
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				tx.rollback();
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
			
			log.debug(funcName + ": exit");
			return liqDecJurAdapterVO;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			e.printStackTrace();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	private DecJurVO createDecJur(DecJurVO decJurVO, List<DecJur> listDecJur, boolean persistir) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			// Cantidad personal
			if (decJurVO.getValRefMin()==null){
				decJurVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DECJURADAPTER_CANPER_LABEL);
			}
			
			// Se valida existecia de periodo/anio
			if (decJurVO.getPeriodo() == null && decJurVO.getAnio() == null){
				decJurVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURADAPTER_PERIODO_LABEL);
			}
			
			// Fin validacion requeridos y rangos
			if (decJurVO.hasErrorRecoverable()){
				return decJurVO;
			}
			
			Integer periodo = decJurVO.getPeriodo();
			Integer anio = decJurVO.getAnio();
			
			Cuenta cuenta = Cuenta.getById(decJurVO.getCuenta().getId());
			
			Deuda deuda=null;
			TipDecJurRec tipDecJurRec = null;
			
			// Validamos que exista la deuda para el periodo ingresado
			if (!decJurVO.hasErrorRecoverable()){
				deuda = DeudaAdmin.getByCuentaPeriodoAnioParaDJ(cuenta, periodo.longValue(), anio);
				if (deuda == null){
					decJurVO.addRecoverableError(GdeError.DECJURADAPTER_DEUDA_NOENCONTRADA);
				} else {
					
					// Discernimos el tipo de Declaracion a crear
					List<DecJur> listDecJurAnt = DecJur.getListByDeuda(deuda);

					// Si no existen declaraciones anteriores
					if (listDecJurAnt.size() > 0){
						tipDecJurRec = TipDecJurRec.getById(TipDecJurRec.ID_DJ_RECTIFICATIVA);
					
					} else {
					// Existen anteriores
						
						// Si tiene otros Pagos es Rectificativa
						
						if (decJurVO.poseePagosMismoPeriodo()){
							tipDecJurRec = TipDecJurRec.getById(TipDecJurRec.ID_DJ_RECTIFICATIVA);
							
						} else {
						// Sino es original	
							tipDecJurRec = TipDecJurRec.getById(TipDecJurRec.ID_DJ_ORIGINAL);
							
						}
					}	
				}
			}
			
			log.debug(funcName + " hasErr: " + decJurVO.hasError());
			
			if (decJurVO.hasError()){
				return decJurVO;
			}
			
			// Creamos la declaracion jurada y sus detalles 
			DecJur decJur = new DecJur();
			decJur.setCuenta(cuenta);
			decJur.setFechaPresentacion(new Date());
			decJur.setFechaNovedad(new Date());
			decJur.setRecurso(cuenta.getRecurso());
			decJur.setTipDecJurRec(tipDecJurRec);
			decJur.setOriDecJur(OriDecJur.getById(OriDecJur.ID_CMD));
			decJur.setPeriodo(periodo);
			decJur.setAnio(anio);
			decJur.setIdDeuda(deuda.getId());
			decJur.setValRefMin(decJurVO.getValRefMin());
			
			decJur.setAliPub(decJurVO.getAliPub());
			decJur.setAliMesYSil(decJurVO.getAliMesYSil());
			
			decJur.setTotalPublicidad(decJurVO.getTotalPublicidad());
			decJur.setTotMesYSil(decJurVO.getTotMesYSil());
			
			decJur.setTotalDeclarado(0D);
			decJur.setSubtotal(0D);
			decJur.setOtrosPagos(0D);
			
			Date fecVig = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			// Obtenemos el Valor de referencia minimo
			if (decJur.getValRefMin()!=null){
				RecMinDec recMinDec = RecMinDec.getVigente(cuenta.getRecurso(), decJur.getValRefMin(), fecVig);
				if (recMinDec == null)
					decJur.setMinRec(0D);
				else
					decJur.setMinRec(recMinDec.getMinimo());
			}
			
			decJur.setEstado(Estado.CREADO.getId());
			
			// Seteamos la lista de Detalles
			decJur.setListDecJurDet(new ArrayList<DecJurDet>());
			
			for(DecJurDetVO decJurDetVO:decJurVO.getListDecJurDet()){
				DecJurDet decJurDet = new DecJurDet();
				
				decJurDet.setBase(decJurDetVO.getBase()==null?0D:decJurDetVO.getBase());
				decJurDet.setMultiplo(decJurDetVO.getMultiplo()==null?0D:decJurDetVO.getMultiplo());
				decJurDet.setSubtotal1(decJurDetVO.getSubtotal1()==null?0D:decJurDetVO.getSubtotal1());
	
				decJurDet.setCanUni(decJurDetVO.getCanUni());
				
				RecConADec recConADec =  RecConADec.getByIdNull(decJurDetVO.getRecConADec().getId());
				decJurDet.setRecConADec(recConADec);
				decJurDet.setDetalle(recConADec.getDesConcepto());
				
				RecTipUni recTipUni = RecTipUni.getByIdNull(decJurDetVO.getRecTipUni().getId());
				decJurDet.setRecTipUni(recTipUni);
				decJurDet.setUnidad(recTipUni==null?null:recTipUni.getNomenclatura());
				
				RecConADec tipoUnidad =  RecConADec.getByIdNull(decJurDetVO.getTipoUnidad().getId());
				decJurDet.setTipoUnidad(tipoUnidad);
				decJurDet.setDesTipoUnidad(tipoUnidad==null?null:tipoUnidad.getDesConcepto());
				decJurDet.setValUnidad(decJurDetVO.getValUnidad()==null?0D:decJurDetVO.getValUnidad());
				decJurDet.setSubtotal2(decJurDetVO.getSubtotal2()==null?0D:decJurDetVO.getSubtotal2());
				decJurDet.setTotalConcepto(decJurDetVO.getTotalConcepto()==null?0D:decJurDetVO.getTotalConcepto());
				
				log.debug(" decJurDet: subTot1: " + decJurDet.getSubtotal1() + 
						" subTot2: " + decJurDet.getSubtotal2());
				
				decJur.getListDecJurDet().add(decJurDet);
			}
			
			// Seteamos la lista de Otros Pagos.
			decJur.setListDecJurPag(new ArrayList<DecJurPag>());
			for (DecJurPagVO decJurPagVO:decJurVO.getListDecJurPag()){
				TipPagDecJur tipPagDecJur = TipPagDecJur.getById(decJurPagVO.getTipPagDecJur().getId()); 
				DecJurPag decJurPag = new DecJurPag(); 
				
				decJurPag.setDecJur(decJur);
				decJurPag.setTipPagDecJur(tipPagDecJur);
				decJurPag.setImporte(decJurPagVO.getImporte());
				
				AgeRet ageRet = AgeRet.getByIdNull(decJurPagVO.getAgeRet().getId());
				decJurPag.setAgeRet(ageRet);
				if (ageRet != null) decJurPag.setCuitAgente(ageRet.getCuit());
				decJurPag.setCertificado(decJurPagVO.getCertificado());
				
				log.debug(" decJurPag: tipo: " + tipPagDecJur.getId() + 
						" importe: " + decJurPag.getImporte());
				
				decJur.getListDecJurPag().add(decJurPag);
			}
			
			// LLamamoos a recalcular los valores sin realizar update
			decJur.recalcularValores(false);
			
			// Segun bandera simulacion
			if (persistir) {
				// LLamo al update del DAO
				GdeDAOFactory.getDecJurDAO().update(decJur);
				
				for(DecJurDet decJurDet:decJur.getListDecJurDet()){
					decJurDet.setDecJur(decJur);
					GdeDAOFactory.getDecJurDetDAO().update(decJurDet);
				}
					
				for(DecJurPag decJurPag:decJur.getListDecJurPag()){
					decJurPag.setDecJur(decJur);
					GdeDAOFactory.getDecJurPagDAO().update(decJurPag);
				}
			}
			
			// Agregamos a la lista
			listDecJur.add(decJur);
					
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurVO;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}
	
	
	private DecJur procesarDDJJ (DecJur decJur, List<LiqDeudaVO> listDeuda, Date fechaAct, boolean persistir)throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + ": enter");
		
		try{
			
			// obtenemos el registro de deuda asociada.
			DeudaAdmin deuda = DeudaAdmin.getById(decJur.getIdDeuda());
			
			if (deuda == null){
				decJur.addRecoverableError(GdeError.DECJURADAPTER_DEUDA_NOENCONTRADA);
			}
			
			if (decJur.hasError()){
				return decJur;
			}

			// Si el declarado - otros pagos es mayor a cero (Queda saldo)
			//if (decJur.getTotalDeclarado()-totalOtrosPagos > 0) {
							
				//log.debug(funcName + " Queda Saldo");
				// Vuelvo a calcular los campos totales
				// y actualizamos la declaracion jurada.
				log.debug(funcName + " recalcularValores");
				decJur.recalcularValores(false);
				
				// Obtenemos la Clasificacion Ceuda Rectificativa para el Recurso de la Deuda.
				RecClaDeu clasifRectif = deuda.getRecClaDeuRectificativa();
				
				// Actualizada importe o fechaPago de deuda o 
				// crea nuevo regitro de deuda segun otros pagos de la declaracion.
				decJur = this.procesarDeudaSegunOtrosPago(decJur, deuda, clasifRectif, listDeuda, fechaAct, persistir);
				
			/*} /*else {
				log.debug(funcName + " NO Queda Saldo");
				LiqDeudaVO liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
				listDeuda.add(liqDeudaVO);
			}*/
			
			//Seteo procesada la declaración jurada
			decJur.setEstado(Estado.ACTIVO.getId());
						
			return decJur;
			
		}catch (Exception exception){
			log.error("ServiceError en: ", exception);
			exception.printStackTrace();
			throw new DemodaServiceException(exception); 
		}/*finally{
			SiatHibernateUtil.closeSession();
		}*/
	}
	
	
	
	/**
	 * Procesa deuda, actualiza o crea nueva segun otros pagos
	 * 
	 * @param decJur
	 * @param deuda
	 * @param recClaDeu
	 * @return
	 * @throws DemodaServiceException
	 */
	private DecJur procesarDeudaSegunOtrosPago(DecJur decJur, DeudaAdmin deuda, RecClaDeu recClaDeu, List<LiqDeudaVO> listDeuda, 
			Date fechaAct, boolean persistir)
		throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			LiqDeudaVO liqDeudaVO = null;
			DeudaAdmin nuevaDeuda = null;
			
			// Si tine otros pagos. 
			if(!ListUtil.isNullOrEmpty(decJur.getListDecJurPag()) || deuda.getFechaPago()!=null){
				log.debug(funcName + ": Si tiene otros pagos");
				
				Double totalOtrosPagos=0D;
				
				if (decJur.getOtrosPagos()!=null)
					totalOtrosPagos= decJur.getOtrosPagos();
				
				log.debug(funcName + " decJur: " + decJur.getPeriodo() + "/" + decJur.getAnio() + 
						" dec: " + decJur.getTotalDeclarado() + " o. pag:" + totalOtrosPagos);
				
				TipPagDecJur tipPagMismoPeriodo = TipPagDecJur.getById(TipPagDecJur.ID_DJMISMOPERIODO);
				TipPagDecJur tipPagRetencion = TipPagDecJur.getById(TipPagDecJur.ID_RETENCION);
				
				// Obtenemos una lista de TipPagDecJur
				List<TipPagDecJur> listTipPagDecJur = new ArrayList<TipPagDecJur>();	
				for (DecJurPag djp:decJur.getListDecJurPag()){
					listTipPagDecJur.add(djp.getTipPagDecJur());
				}
				
				// Si tiene pago tipo 3 (Del mismo periodo)
				if (ListUtil.isInList(listTipPagDecJur, tipPagMismoPeriodo) || deuda.getFechaPago()!=null){
					log.debug(funcName + ": Si tiene pago tipo 3");
					
					// Solo si queda saldo, creamos la rectificativa
					//Modificacion 20-10-09 --> puede haber deuda con fecha de pago e importe 0 por compensaciones, etc
					if (decJur.getTotalDeclarado()-totalOtrosPagos > 0 || deuda.getFechaPago()!=null) {
						log.debug(funcName + ": Queda Saldo");
						
						// Agregamos la deuda original antes de crear la nueva
						liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
						listDeuda.add(liqDeudaVO);
						
						// Crea Deuda Rectificativa por la diff
						// Segun bandera simulacion
						nuevaDeuda = this.createDeuda4DecJur(deuda, recClaDeu, decJur, persistir);
						
						// Relacionamos la Declaracion Jurada con la nueva deuda creada
						decJur.setIdDeuda(nuevaDeuda.getId());
						GdeDAOFactory.getDecJurDAO().update(decJur);
						
						liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(nuevaDeuda, fechaAct);
						liqDeudaVO.setDesEstado(CREADO);
						listDeuda.add(liqDeudaVO);
					} else {
						// La deuda queda tal cual estaba
						log.debug(funcName + ": NO Queda Saldo");
						liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
						listDeuda.add(liqDeudaVO);						
					}
				
				// Si tiene pagos de tipo retencion 	
				} else if (ListUtil.isInList(listTipPagDecJur, tipPagRetencion)){		
					
					// Si el declarado - otros pagos es mayor a cero (Queda saldo)
					if (decJur.getTotalDeclarado()-totalOtrosPagos <= 0) {
						log.debug(funcName + ": NO Queda Saldo");
						// Seteamos declarado, y fachaPago.
						deuda.setFechaPago(deuda.getFechaVencimiento());
					} else {
						log.debug(funcName + ": Queda Saldo");
					}
					
					deuda = this.updateDeuda4DecJur(deuda, decJur, persistir);
					liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
					liqDeudaVO.setDesEstado(MODIFICADO);
					listDeuda.add(liqDeudaVO);						
					
					// Si NO tiene pago tipo 3	
				} else {
					log.debug(funcName + ": NO tiene pago tipo 3");
					// Si la deuda esta Paga o en Convenio, creamos una nueva
					if (deuda.getFechaPago() != null || deuda.getIdConvenio() != null){
						
						// Agregamos a deuda original antes de crear la nueva
						liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
						listDeuda.add(liqDeudaVO);
						
						// Creamos deuda
						// Segun bandera simulacion
						nuevaDeuda = this.createDeuda4DecJur(deuda, recClaDeu, decJur, persistir);
						
						// Relacionamos la Declaracion Jurada con la nueva deuda creada
						decJur.setIdDeuda(nuevaDeuda.getId());
						GdeDAOFactory.getDecJurDAO().update(decJur);
						
						liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(nuevaDeuda, fechaAct);
						liqDeudaVO.setDesEstado(CREADO);
						listDeuda.add(liqDeudaVO);						
						
					}
					
					// Modificamos deuda
					// Segun bandera simulacion
					deuda = this.updateDeuda4DecJur(deuda, decJur, persistir);
					
					liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
					liqDeudaVO.setDesEstado(MODIFICADO);
					listDeuda.add(liqDeudaVO);
					
				}
				
				// Si no tiene otros Pagos	
			} else {
				log.debug(funcName + ": No tiene otros pagos");
				
				// Si la deuda esta Paga o en Convenio, creamos una nueva
				if (deuda.getFechaPago() != null || deuda.getIdConvenio() != null){
					
					// Agregamos a deuda original antes de crear la nueva
					liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
					listDeuda.add(liqDeudaVO);

					// Segun bandera simulacion
					nuevaDeuda = this.createDeuda4DecJur(deuda, recClaDeu, decJur, persistir);
					
					// Relacionamos la Declaracion Jurada con la nueva deuda creada
					decJur.setIdDeuda(nuevaDeuda.getId());
					GdeDAOFactory.getDecJurDAO().update(decJur);
					
					liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(nuevaDeuda, fechaAct);
					liqDeudaVO.setDesEstado(CREADO);
					listDeuda.add(liqDeudaVO);
					
				}
				
				// Segun bandera simulacion
				deuda = this.updateDeuda4DecJur(deuda, decJur, persistir);
				
				liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
				liqDeudaVO.setDesEstado(MODIFICADO);
				listDeuda.add(liqDeudaVO);
				
			}
			
			deuda.passErrorMessages(decJur);
		
			return decJur;
			
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}
	
	
	
	/**
	 * 
	 * 
	 * 
	 * @param deuda
	 * @param decJur
	 * @return
	 * @throws DemodaServiceException
	 */
	private DeudaAdmin updateDeuda4DecJur(DeudaAdmin deuda, DecJur decJur, boolean persistir) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			// Guardo valor de deuda antes de determinar
			decJur.setImporteAntDeu(deuda.getImporte());
			
			//Seteo el importe de la deuda
			Double importeDeuda = decJur.getTotalDeclarado();
			deuda.setImporte(importeDeuda);
			
			//Obtengo la lista de deuRecCon
			List<DeuAdmRecCon> listDeuRecCon = deuda.getListDeuRecCon();
			
			//Seteo el importe de los conceptos
			for (DeuAdmRecCon deuAdmRecCon : listDeuRecCon){
				Double porcentajeRecCon = deuAdmRecCon.getRecCon().getPorcentaje();
				Double importeDeuRecCon=NumberUtil.round(importeDeuda* porcentajeRecCon,SiatParam.DEC_IMPORTE_DB);
				deuAdmRecCon.setImporte(importeDeuRecCon);
				deuAdmRecCon.setImporteBruto(importeDeuRecCon);
				
				// Segun bandera simulacion				
				if(persistir)
					GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
			}
			
			//Armo el string de conceptos de la deuda
			deuda.setStrConceptosByListRecCon(listDeuRecCon);
			
			//Si la declaracion es rectificativa verifico si hubo pagos registrados de declaraciones y los borro
			if (decJur.getTipDecJurRec().getId().longValue()==TipDecJurRec.ID_DJ_RECTIFICATIVA){
				
				//Lista de DecJur asociadas al periodo original
				List<DecJur>listDecJur = DecJur.getListByDeudaDecJurExcluir(deuda, decJur);	
				
				//id's de TipoPago a eliminar separados por coma
				String idsTipoPago = TipoPago.ID_RETENCION_DECLARADA+",";
					   idsTipoPago+= TipoPago.ID_PERCEPCION_DECLARADA;
					   
				// Segun bandera simulacion
				if (persistir)
					GdeDAOFactory.getPagoDeudaDAO().deletePagosDDJJ(listDecJur,idsTipoPago);
			}
			
			//Si hubo otros pagos los cargo en PagoDeuda
			Double restoAImporte=0D;
			if (decJur.getOtrosPagos()!=null && decJur.getOtrosPagos()!=0D){
				log.debug("LISTA PAGOS: "+decJur.getListDecJurPag().size());
				for (DecJurPag decJurPag : decJur.getListDecJurPag()){
					Long idTipPagDecJur = decJurPag.getTipPagDecJur().getId().longValue();
					log.debug("id tipo pago: "+idTipPagDecJur);
					if (idTipPagDecJur == TipPagDecJur.ID_RETENCION || 
							idTipPagDecJur==TipPagDecJur.ID_PERCEPCION){
						PagoDeuda pagoDeuda = new PagoDeuda();
						pagoDeuda.setFechaPago(decJurPag.getFechaPago());
						pagoDeuda.setIdDeuda(decJur.getIdDeuda());
						pagoDeuda.setIdPago(decJur.getId());
						pagoDeuda.setImporte(decJurPag.getImporte());
						pagoDeuda.setFechaPago(new Date());
						pagoDeuda.setActualizacion(0D);
						pagoDeuda.setEsPagoTotal(0);
						log.debug("creando pago deuda");
						if (idTipPagDecJur == TipPagDecJur.ID_RETENCION){
							pagoDeuda.setTipoPago(TipoPago.getById(TipoPago.ID_RETENCION_DECLARADA));
						}else if (idTipPagDecJur == TipPagDecJur.ID_PERCEPCION){
							pagoDeuda.setTipoPago(TipoPago.getById(TipoPago.ID_PERCEPCION_DECLARADA));
						}
						// Segun bandera simulacion						
						if (persistir)
							GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);
					}
					restoAImporte+=decJurPag.getImporte();
				}
			}
			
			log.debug("IMPORTE DEUDA: "+deuda.getImporte() + " ,PAGOS: "+restoAImporte);
			//Seteo el saldo de la deuda
			Double saldo = 0D;
			
			if (deuda.getImporte()-restoAImporte > 0)
				saldo = deuda.getImporte()-restoAImporte;
			
			// Guardo valor de saldo de deuda antes de determinar
			decJur.setSaldoAntDeu(deuda.getSaldo());
			
			deuda.setSaldo(saldo);

			// Segun bandera simulacion						
			if (persistir){
				GdeDAOFactory.getDeudaDAO().update(deuda);
				GdeDAOFactory.getDecJurDAO().update(decJur);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return deuda;
			
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
		
	}

	
	/**
	 * Crea registro de deuda con clasificacion recibida para los datos de periodo de la declaracion recibida.
	 * 
	 * @param deuda
	 * @param clasifRectif
	 * @param decJur
	 * @return
	 * @throws DemodaServiceException
	 */
	private DeudaAdmin createDeuda4DecJur(DeudaAdmin deuda, RecClaDeu clasifRectif, DecJur decJur, boolean persistir) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
	        // Setea los datos en la deudaAdmin que se va a crear, con la lista de conceptos 
	        DeudaAdmin deudaAdmin = new DeudaAdmin();
	        deudaAdmin.setRecurso(deuda.getRecurso());
	        deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
	        deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
	        deudaAdmin.setFechaEmision(new Date());
	        deudaAdmin.setEstaImpresa(SiNo.NO.getId());
	        deudaAdmin.setSistema(deuda.getSistema());
	        
	        // Segun bandera simulacion						
			if (persistir)       
				deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
			
	        deudaAdmin.setAnio(deuda.getAnio());
	        deudaAdmin.setPeriodo(deuda.getPeriodo());
	        deudaAdmin.setCuenta(deuda.getCuenta());
	        deudaAdmin.setFechaVencimiento(deuda.getFechaVencimiento());
	        deudaAdmin.setRecClaDeu(clasifRectif);
	        deudaAdmin.setReclamada(SiNo.NO.getId());
	        deudaAdmin.setResto(1L); // Se graba con resto distinto de cero para evitar problemas de Asentamiento de la Deuda Original migrada. (Fix Mantis 5077)
	        deudaAdmin.setEstado(Estado.ACTIVO.getId());
	        deudaAdmin.setAtrAseVal(deuda.getAtrAseVal());
	         
	        /*  importe = totalDeclarado - sumatoria de list de decJurPag.importe == 3
	          saldo = importe - sumatoria de list de decJurPag donde tipoPagDecJur != 3 */
	        
	        log.debug(funcName + " declarado: " + decJur.getTotalDeclarado());
	        log.debug(funcName + " sum tipo=3: " + decJur.getSumatoriaOtrosPagos(true));
	        log.debug(funcName + " sum tipo!=3: " + decJur.getSumatoriaOtrosPagos(false));
	        
	        Double importeDeuda = decJur.getTotalDeclarado() - decJur.getSumatoriaOtrosPagos(true);
	        
	        if(importeDeuda.doubleValue() < 0)
	        	importeDeuda = 0D;
	        	
	        deudaAdmin.setImporte(importeDeuda);
	        deudaAdmin.setImporteBruto(deudaAdmin.getImporte());
	        
	        Double saldoDeuda = deudaAdmin.getImporte() - decJur.getSumatoriaOtrosPagos(false);
	        
	        if(saldoDeuda.doubleValue() < 0 )
	        	saldoDeuda = 0D;	
	        
	        deudaAdmin.setSaldo(saldoDeuda);
	        
	        log.debug(funcName + " importeDedua: " + deudaAdmin.getImporte());
	        log.debug(funcName + " saldo: " + deudaAdmin.getSaldo());
	        
	        // Calcula el importe y setea los conceptos            
	        List<DeuAdmRecCon> listDeuAdmRecCon= new ArrayList<DeuAdmRecCon>();
	        //deudaAdmin.setListDeuRecCon(listDeuAdmRecCon);
	        
	       for(RecCon recCon: deuda.getRecurso().getListRecCon()){

		        DeuAdmRecCon deuAdmRecConNuevo = new DeuAdmRecCon();
	        	
	        	deuAdmRecConNuevo.setDeuda(deudaAdmin);
	        	deuAdmRecConNuevo.setRecCon(recCon); 
	        	
	        	Double porcentajeRecCon = deuAdmRecConNuevo.getRecCon().getPorcentaje();
				Double importeDeuRecCon=NumberUtil.round(importeDeuda * porcentajeRecCon, SiatParam.DEC_IMPORTE_DB);
				deuAdmRecConNuevo.setImporte(importeDeuRecCon);
				deuAdmRecConNuevo.setImporteBruto(importeDeuRecCon);
				deuAdmRecConNuevo.setSaldo(0D);
				
	            listDeuAdmRecCon.add(deuAdmRecConNuevo);
	       }
	     
	        // Segun bandera simulacion
			if (persistir)        
				deudaAdmin = GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdmin, listDeuAdmRecCon);
        
	        if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	        
	        return deudaAdmin;
	        
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
		
	}
	
	
	public PrintModel imprimirSimulacion(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		PrintModel print = null;
		try {			
			
			LiqDecJurAdapter liqDecJurAdapter = new LiqDecJurAdapter();
			
			liqDecJurAdapter.setListDeudaSimulada(liqDecJurAdapterVO.getListDeudaSimulada());
			liqDecJurAdapter.setCuenta(liqDecJurAdapterVO.getCuenta());
			liqDecJurAdapter.setTotalSaldo(liqDecJurAdapterVO.getTotalSaldo());
			liqDecJurAdapter.setTotalImporte(liqDecJurAdapterVO.getTotalImporte());
			liqDecJurAdapter.setTotalActualizacion(liqDecJurAdapterVO.getTotalActualizacion());
			liqDecJurAdapter.setTotal(liqDecJurAdapterVO.getTotal());
			
			
			// Obtiene el formulario
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_DECJURMAS_SIMULA);
					
			// Le setea los datos		
			print.putCabecera("usuario", userContext.getUserName());
			print.putCabecera("FechaActualCompleta", DateUtil.getDateEnLetras(new Date()));
			print.setData(liqDecJurAdapter);
			print.setTopeProfundidad(3);
			print.setDeleteXMLFile(false);
	
			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}
	// <--- Declaracion Jurada Masiva
}
