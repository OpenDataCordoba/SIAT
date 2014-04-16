//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Accion;
import ar.gov.rosario.siat.bal.buss.bean.BalDefinicionManager;
import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqPlanVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.TipoPerForVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.seg.buss.bean.Oficina;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class LiqFormConvenioBeanHelper {
	
	private static Log log = LogFactory.getLog(LiqFormConvenioBeanHelper.class);
	
	private Cuenta cuenta;

	public static final Long CANT_DIAS_HABILES_SIGTES = 5L;
	
	public LiqFormConvenioBeanHelper(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	/**
	 * idDeudaEstadoDeuda lista de string con la forma "idcuenta-idEstado"
	 * @throws Exception 
	 */
	public LiqFormConvenioAdapter getLiqFormConvenioInit(LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception{
		
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
		LiqCuentaVO LiqCuentaVO = liqDeudaBeanHelper.getCuenta();
		liqFormConvenioAdapter.setCuenta(LiqCuentaVO);
				
		// Reset de valores
		liqFormConvenioAdapter.clearErrorMessages();
		
		liqFormConvenioAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());
				
		// Validarmos que exista deuda seleccionada.
		if (liqFormConvenioAdapter.getListIdDeudaSelected() == null ||
				liqFormConvenioAdapter.getListIdDeudaSelected().length == 0 ){
			
			liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_SELECT_DEUDA_REQUERIDO);
			
			return liqFormConvenioAdapter;
		}
		
		ViaDeuda viaDeuda = null;
		List<Deuda> listDeudaSeleccionada = new ArrayList<Deuda>();
		List<Long> listIdDeudas=new ArrayList<Long>();
		Recurso recurso = null;
		// Recuperamos la Deuda seleccinada
		for(String idDeudaEstado: liqFormConvenioAdapter.getListIdDeudaSelected()){
			String[] split = idDeudaEstado.split("-");
			long idDeuda = Long.parseLong(split[0]);
			listIdDeudas.add(idDeuda);
			long idEstadoDeuda = Long.parseLong(split[1]);
			Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
			if(recurso==null)
				recurso=deuda.getRecurso();
			
			if (recurso.getEsAutoliquidable().intValue()== SiNo.SI.getId().intValue()){
				if (deuda.getSaldo()==0D){
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_SELECT_DEUDA_CERO);
					return liqFormConvenioAdapter;
				}
				if(deuda.getConvenio()!=null){
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_SELECT_DEUDA_ENCONVENIO);
					return liqFormConvenioAdapter;
				}
			}
			
			listDeudaSeleccionada.add(deuda);
			
			//	Validamos que toda la deuda seleccionada corresponda a la misma  via.
			// Seteo la via de la primer deuda
			if (viaDeuda == null){
				viaDeuda = deuda.getViaDeuda();
			
			} 
			
			/*else if (idViaDeuda.longValue() != deuda.getViaDeuda().getId()){
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_SELECT_DEUDA_DISTINTA_VIA);
				return liqFormConvenioAdapter;}
			capazmente aca se pueda validar	*/
		}
		
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
			liqFormConvenioAdapter.addRecoverableValueError(msg);
		}
		
		UserContext userContext = DemodaUtil.currentUserContext();
		
		// Validaciones de Vias y permisos.
		// Validamos que solo un procurado pueda formalizar deuda en via judicial 			
		if (userContext.getEsProcurador() && ViaDeuda.ID_VIA_JUDICIAL != viaDeuda.getId().longValue()){
			liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VIA_DEUDA_NO_PERMITIDA_USR);
			return liqFormConvenioAdapter;
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
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_DEUDA_NO_PERMITIDA_USR);
				return liqFormConvenioAdapter;
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
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_DEUDA_DISTINTO_PROCURADOR);
				return liqFormConvenioAdapter;
			}
		}
					
		if (userContext.getEsUsuarioCMD() && ViaDeuda.ID_VIA_ADMIN != viaDeuda.getId().longValue()){
			liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VIA_DEUDA_NO_PERMITIDA_USR);
			return liqFormConvenioAdapter;
		}
		// Fin Validaciones
		
		liqFormConvenioAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());
		
		// Paso a LiqDeudaVO
		for (Deuda deuda:listDeudaSeleccionada){
			// Obtentemos la deuda actualizada a la fecha de hoy
			LiqDeudaVO liqDeudaVO = getLiqDeudaVOActualizado(deuda, new Date(), null);
						
			liqDeudaVO.setVerDetalleDeudaEnabled(true);
			
			liqFormConvenioAdapter.getListDeuda().add(liqDeudaVO);
		}
		
		liqFormConvenioAdapter.setListFechasForm(Feriado.getProximosDiasForReconfeccion(CANT_DIAS_HABILES_SIGTES));
		
		// Se le pide al LiqConvenioAdapter que calcule el total historico de la deuda seleccionada.
		liqFormConvenioAdapter.calcularTotalHistorio();
		
		// Seteo de Permisos
		
		return liqFormConvenioAdapter;
	}
	
	
	/**
	 * Obtiene la lista de planes disponibles no manuales para la deuda seleccionada y habilitados o no segun corresponda.
	 * 
	 * @author Cristian
	 * @param liqFormConvenioAdapter
	 * @return LiqFormConvenioAdapter
	 * @throws Exception
	 */
	public static LiqFormConvenioAdapter getPlanes(LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception{
		
		List<LiqDeudaVO> listLiqDeudaVO = liqFormConvenioAdapter.getListDeuda();
		// Se obtiene la cuenta
		Cuenta cuenta = Cuenta.getById(liqFormConvenioAdapter.getCuenta().getIdCuenta());
		
		Long idRecurso = cuenta.getRecurso().getId();
		Long idViaDeuda = null;
		Date fechaProbableForm = DateUtil.getDate(liqFormConvenioAdapter.getFechaFormSelected());
		
		// se pasan listLiqDeudaVO a listDeuda
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		for(LiqDeudaVO liqDeudaVO: listLiqDeudaVO){
			listDeuda.add(Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda()));
		}
		
		// Reset de la lista de deuda
		liqFormConvenioAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());
		
		for(Deuda deuda:listDeuda){
			// toVO a patacon
			
			// Tomo como via deuda, la via del primer registro seleccionado.
			if (idViaDeuda == null){
				idViaDeuda = deuda.getViaDeuda().getId();
			}
			
			// Obtentemos la deuda actualizada a la fecha de formalizacion			
			LiqDeudaVO liqDeudaVO = getLiqDeudaVOActualizado(deuda, fechaProbableForm, null);
			
			liqFormConvenioAdapter.getListDeuda().add(liqDeudaVO);
		}

		// Se le pide al LiqConvenioAdapter que calcule el total actualizado a la fecha de probable formalizacion de la deuda seleccionada.
		liqFormConvenioAdapter.calcularTotalActualizado();
		
		// Obtener la minima y la maxima fecha vencimiento
		// Obtener los planes que esten entre ese rango de fechas para el mismo Recurso y Via de la cuenta seleccionada
		// y que se encuentren activos.
		
		List<Plan> listPlan = Plan.getListVigentesyActivos(idRecurso, idViaDeuda, fechaProbableForm);
		
		// Reset a la lista de planes
		liqFormConvenioAdapter.setListPlan(new ArrayList<LiqPlanVO>());
		
		for (Plan plan:listPlan){
			LiqPlanVO liqPlanVO = new LiqPlanVO();
			
			liqPlanVO.setIdPlan(plan.getId());
			liqPlanVO.setDesPlan(plan.getDesPlan());
			liqPlanVO.setLeyendaPlan(plan.getLeyendaPlan());
			liqPlanVO.setLinkNormativa(plan.getLinkNormativa());
			
			// Realizo el chekeo para saber si el plan se muestra hablitado o no
			liqPlanVO.setEsSeleccionable(chkEsSeleccionable(plan, cuenta, listDeuda, fechaProbableForm, liqPlanVO));
			
			liqFormConvenioAdapter.getListPlan().add(liqPlanVO);
		}
		
		return liqFormConvenioAdapter;
	}
	
	/**
	 * Obtiene la lista de planes "Manuales" que se encuentren disponibles para la deuda seleccionada. 
	 * 
	 * @author tecso
	 * @param liqFormConvenioAdapter
	 * @return
	 * @throws Exception
	 */
	public static LiqFormConvenioAdapter getPlanesEsp(LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception{
		
		// Requeridos
		if (liqFormConvenioAdapter.getFechaFormalizacion()== null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_FECHAFORMALIZACION);
		}
		if (liqFormConvenioAdapter.getDescCapital() == null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_DESCCAPITAL);
		}
		if (liqFormConvenioAdapter.getDescActualizacion() == null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_DESCACTUALIZACION);
		}
		if (liqFormConvenioAdapter.getInteres() == null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_INTERES);
		}
		if (liqFormConvenioAdapter.getVenPrimeraCuota() == null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_VENPRIMERACUOTA);
		}
		if (liqFormConvenioAdapter.getCantMaxCuo() == null && liqFormConvenioAdapter.getImpMinCuo() == null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_UNVALORREQUERIDO);
		}
		if (liqFormConvenioAdapter.getCantMaxCuo() != null && liqFormConvenioAdapter.getCantMaxCuo() <0){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_VALORMENORQUECERO, GdeError.PLAN_CANTMAXCUO);
		}
		if (liqFormConvenioAdapter.getImpMinCuo() != null && liqFormConvenioAdapter.getImpMinCuo() <0){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_VALORMENORQUECERO, GdeError.PLAN_IMPMINCUOESP);
		}
		
		if (liqFormConvenioAdapter.getConvenio().getCaso().getIdFormateado() == null ||
				!liqFormConvenioAdapter.getConvenio().getCaso().getEsValido()){
			liqFormConvenioAdapter.addRecoverableError(GdeError.CONVENIOESP_CASOVALIDO_REQUERIDO); 
		}
		
		if (liqFormConvenioAdapter.hasError()){
			return liqFormConvenioAdapter;
		}

		// Formatos y Rangos
		// Descuento de Capital entre 0 y 1
		if (liqFormConvenioAdapter.getDescCapital().doubleValue() < 0 || 
				liqFormConvenioAdapter.getDescCapital().doubleValue() > 1){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, GdeError.PLAN_DESCCAPITAL);
		}
		
		// Descuento de Actualizacion entre 0 y 1
		if (liqFormConvenioAdapter.getDescActualizacion().doubleValue() < 0 || 
				liqFormConvenioAdapter.getDescActualizacion().doubleValue() > 1){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, GdeError.PLAN_DESCACTUALIZACION);
		}
		
		// Porcentaje de Interes entre 0 y 1
		if (liqFormConvenioAdapter.getInteres().doubleValue() < 0 || 
				liqFormConvenioAdapter.getInteres().doubleValue() > 1){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, GdeError.PLAN_INTERES);
		}
		
		// Vencimiento mayor a hoy. 
		if (DateUtil.isDateBefore(liqFormConvenioAdapter.getVenPrimeraCuota(), new Date())){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLAN_VENPRIMERACUOTA, BaseError.MSG_FECHA_ACTUAL);
		}
		
		if (liqFormConvenioAdapter.hasError()){
			return liqFormConvenioAdapter;
		}
		
		// Logica de negocio para recuperar los planes.
		List<LiqDeudaVO> listLiqDeudaVO = liqFormConvenioAdapter.getListDeuda();
		// Se obtiene la cuenta
		Cuenta cuenta = Cuenta.getById(liqFormConvenioAdapter.getCuenta().getIdCuenta());
		
		Long idRecurso = cuenta.getRecurso().getId();
		Long idViaDeuda = null;
		Date fechaProbableForm = liqFormConvenioAdapter.getFechaFormalizacion();
		
		// se pasan listLiqDeudaVO a listDeuda
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		for(LiqDeudaVO liqDeudaVO: listLiqDeudaVO){
			listDeuda.add(Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda()));
		}
		
		// Reset de la lista de deuda
		liqFormConvenioAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());
		
		for(Deuda deuda:listDeuda){
			// toVO a patacon
			
			// Tomo como via deuda, la via del primer registro seleccionado.
			if (idViaDeuda == null){
				idViaDeuda = deuda.getViaDeuda().getId();
			}
			
			// Obtentemos la deuda actualizada a la fecha de formalizacion
			LiqDeudaVO liqDeudaVO = getLiqDeudaVOActualizado(deuda, fechaProbableForm, null);
			
			liqFormConvenioAdapter.getListDeuda().add(liqDeudaVO);
		}

		// Se le pide al LiqConvenioAdapter que calcule el total actualizado a la fecha de probable formalizacion de la deuda seleccionada.
		liqFormConvenioAdapter.calcularTotalActualizado();
		
		// Obtener la minima y la maxima fecha vencimiento
		// Obtener los planes que esten entre ese rango de fechas para el mismo Recurso y Via de la cuenta seleccionada
		
		List<Plan> listPlan = Plan.getListVigentesyActivosManuales(idRecurso, idViaDeuda, fechaProbableForm);
		
		// Reset a la lista de planes
		liqFormConvenioAdapter.setListPlan(new ArrayList<LiqPlanVO>());
		
		for (Plan plan:listPlan){
			LiqPlanVO liqPlanVO = new LiqPlanVO();
			
			liqPlanVO.setIdPlan(plan.getId());
			liqPlanVO.setDesPlan(plan.getDesPlan());
			liqPlanVO.setLeyendaPlan(plan.getLeyendaPlan());
			liqPlanVO.setLinkNormativa(plan.getLinkNormativa());
					
			liqFormConvenioAdapter.getListPlan().add(liqPlanVO);
		}
		
		return liqFormConvenioAdapter;
	}
	
	/**
	 * Verifica a partir de una deuda, si la cantidad de deuda impaga seleccionada es igual
	 * al total impago de la cuenta para la via de la deuda
	 * @param deuda
	 * @param cantDeudaSel
	 * @return
	 * @throws Exception
	 */
	public static Boolean getEsTotalImpParaDescuento (Deuda deuda, Integer cantDeudaSel) throws Exception{
		Boolean totalImpago = false;
		
		//Valido si aplica Total Impago para Administrativa
		if (deuda.getViaDeuda().equals(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN))){
			List<DeudaAdmin>listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdminSinConvenio(deuda.getCuenta());
			Integer cantDeudaImpaga = listDeudaAdmin.size();
			for (DeudaAdmin deudaAdmin : listDeudaAdmin){
				if (deudaAdmin.getEsIndeterminada()){
					cantDeudaImpaga= cantDeudaImpaga -1;
				}
			}
			if (cantDeudaSel==cantDeudaImpaga){
				totalImpago=true;
			}
		}
		
		//Valido si aplica Total Impago para Judicial
		if (deuda.getViaDeuda().equals(ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL))){
			List<DeudaJudicial>listDeudaJudicial = GdeDAOFactory.getDeudaJudicialDAO().getListDeudaJudSinConvenio(deuda.getCuenta(), deuda.getProcurador());
			Integer cantDeudaImpaga = listDeudaJudicial.size();
			for (DeudaJudicial deudaJudicial : listDeudaJudicial){
				if (deudaJudicial.getEsIndeterminada()){
					cantDeudaImpaga= cantDeudaImpaga -1;
				}
			}
			if (cantDeudaImpaga==cantDeudaSel){
				totalImpago=true;
			}
		}
		return totalImpago;
	}
	
	/**
	 * Obtiene la lista de alernativas de cuotas para el plan y el conjunto de deuda seleccionados.
	 * 
	 * @author Cristian
	 * @param liqFormConvenioAdapter
	 * @return
	 * @throws Exception
	 */
	public static LiqFormConvenioAdapter getAlternativaCuotas(LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception{
		
		Long idPlan  = liqFormConvenioAdapter.getPlanSelected().getIdPlan();
		
		Plan plan = Plan.getById(idPlan);
		Date fechaProbableForm; 
		
		// Se setean los datos  segun sea un plan especial o manual
		if (liqFormConvenioAdapter.getEsEspecial() ){
			
			DatosPlanEspecial dpe = new DatosPlanEspecial();
			
			dpe.setCantMaxCuo(liqFormConvenioAdapter.getCantMaxCuo());
			dpe.setDescActualizacion(liqFormConvenioAdapter.getDescActualizacion());
			dpe.setDescCapital(liqFormConvenioAdapter.getDescCapital());
			dpe.setFechaFormalizacion(liqFormConvenioAdapter.getFechaFormalizacion());
			dpe.setImpMinCuo(liqFormConvenioAdapter.getImpMinCuo());
			dpe.setImporteAnticipo(liqFormConvenioAdapter.getImporteAnticipo());
			dpe.setInteres(liqFormConvenioAdapter.getInteres());
			dpe.setVenPrimeraCuota(liqFormConvenioAdapter.getVenPrimeraCuota());
			
			plan.setDatosPlanEspecial(dpe);
			
			fechaProbableForm = liqFormConvenioAdapter.getFechaFormalizacion();
			
		} else {
			fechaProbableForm = DateUtil.getDate(liqFormConvenioAdapter.getFechaFormSelected());		
			
		}
		
		// se pasan listLiqDeudaVO a listDeuda obteniendo cada una por id
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		Double totalDeudaActualizada=0D;
		
		for(LiqDeudaVO liqDeudaVO: (List<LiqDeudaVO>)liqFormConvenioAdapter.getListDeuda()){
			Deuda deuda = Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda());
			listDeuda.add(deuda);
			totalDeudaActualizada += deuda.actualizacionSaldo(fechaProbableForm).getImporteAct();
		}
		
		// Se resetea la lista de LiqDeudaVO para recalcularla.
		liqFormConvenioAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());
		
		// Se recorre la lista de deuda obtenida y se calcula la actualizacion para cada una.
		// para ser mostrada nuevamente sin descuentos.
		
		for(Deuda deuda:listDeuda){
			LiqDeudaVO liqDeudaVO = getLiqDeudaVOActualizado(deuda, fechaProbableForm, null);
			liqFormConvenioAdapter.getListDeuda().add(liqDeudaVO);
		}
		
		liqFormConvenioAdapter.calcularTotalActualizado();
		
		
		// Se instancia un container
		LiqDeudaConvenioContainer liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaProbableForm); 		
		
		LiqPlanVO liqPlanVO = new LiqPlanVO();
		
		// Se solicita la totalizacion de la deuda original		
		Double totalDeudaOriginal = liqDeudaConvenioContainer.calcularTotalCapitalOriginal();
		
		
		
		totalDeudaOriginal = NumberUtil.round(totalDeudaOriginal, SiatParam.DEC_IMPORTE_CALC);
		 
		log.debug("### getAlternativaCuotas -> totalDeudaOriginal: " + totalDeudaOriginal);
		
		Double monto = 0D;
		Double interes = 0D;
		Double anticipo = 0D;
		Double valorCuotaRestante = 0D;
		Double totalPlan = 0D;
		Boolean totalImpago = false;
		
		//Valido si aplica Total Impago 
		totalImpago = LiqFormConvenioBeanHelper.getEsTotalImpParaDescuento(listDeuda.get(0), listDeuda.size());
		
		for (int i=1; i <= plan.obtenerCanMaxCuo().intValue(); i++ ){
			
			// Se obtiene el descuento para el numero de cuota actual
			PlanDescuento planDesc = null;
			
			if (totalImpago){
				planDesc = plan.getPlanDescuentoTotImpago(i, fechaProbableForm);
			}
			
			if (planDesc == null){
				planDesc =plan.getPlanDescuento(i, fechaProbableForm);
			}
			liqDeudaConvenioContainer.setPlanDescuento(planDesc);
			
			log.debug("### Desc. cuota: " + i +
					 " # desc. Cap: " + liqDeudaConvenioContainer.getDescuentoCapital() +
					 " desc. Act: " + liqDeudaConvenioContainer.getDescuentoActualizacion() +					 
					 " desc. Int: " + liqDeudaConvenioContainer.getDescuentoInteres());
			
			log.debug("#####  calcularActualizacion p/cuota: " + i + " #####");
			liqDeudaConvenioContainer.calcularActualizacion();
			
			
			
			monto =  liqDeudaConvenioContainer.calcularTotalEnPlan();
			monto = NumberUtil.round(monto, SiatParam.DEC_IMPORTE_CALC);
			
			anticipo = plan.getAnticipo(i, monto);
			anticipo = NumberUtil.round(anticipo, SiatParam.DEC_IMPORTE_CALC);
			
			interes = plan.getInteresFinanciero(i, fechaProbableForm);
			interes = NumberUtil.round(interes, SiatParam.DEC_PORCENTAJE_CALC);
			
			Double importeSellado = 0D;
			Sellado sellado = BalDefinicionManager.aplicarSellado(listDeuda.get(0).getRecurso().getId() , Accion.ID_ACCION_FORMALIZAR_CONVENIO, new Date(), 0, 0D);
			if (sellado !=null){
				importeSellado= sellado.getImporteSellado();
			}

			// Si el interes no es nulo, se calcula en monto del modo normal
			if(interes != null){
				
				log.debug("### Input calcularConvenioMetal i: " + i +
						" monto: "+ monto + 
						" interes: " + interes + 
						" desc. Int: " + liqDeudaConvenioContainer.getDescuentoInteres() +
						" anticipo: " + anticipo +
						" round Anticipo: " + NumberUtil.round(anticipo, 2) );
				
				interes = new Double(interes - (interes * liqDeudaConvenioContainer.getDescuentoInteres()));
				interes = NumberUtil.round(interes, SiatParam.DEC_PORCENTAJE_CALC);
				
				log.debug("### Input calcularConvenioMetal : interes c/desc. :" + interes);
				
			
				if (i > 1){
					valorCuotaRestante = liqDeudaConvenioContainer.calcularConvenioMetal(new Double(monto - anticipo), i-1, interes);
					valorCuotaRestante = NumberUtil.round(valorCuotaRestante, SiatParam.DEC_IMPORTE_CALC);
				}
				
				//sumo el sellado si corresponde a la primera cuota
				anticipo += importeSellado;
				
				totalPlan = new Double(anticipo + (valorCuotaRestante * (i-1))); 
				totalPlan = NumberUtil.round(totalPlan, SiatParam.DEC_IMPORTE_CALC);
				
				log.debug("### Cuota: " + i +
							" Anticipo: " + anticipo.doubleValue() + 
							" Restantes: " + valorCuotaRestante.doubleValue() +
							" total: " + totalPlan.doubleValue());
				
				
				// Si el importe total de la deuda es mayor al minimo para esa cantidad de cuotas
				Double importeMinDeuda = plan.getImpMinDeu(i, fechaProbableForm);
				importeMinDeuda = NumberUtil.round(importeMinDeuda, SiatParam.DEC_IMPORTE_CALC);
				
				log.debug("###  getAlternativaCuotas -> i: " + i + 
						" totalDeudaOriginal: " + totalDeudaOriginal + 
						" importeMinDeuda: " + importeMinDeuda);
				//Se cambia la validacion sobre importe minimo contra el total actualizado en vez del original
				if (totalDeudaActualizada.doubleValue() >= importeMinDeuda.doubleValue()) {
					
					Double importeMinCuota = plan.getImpMinCuo(i, fechaProbableForm);
					
					log.debug("### getAlternativaCuotas valorCuotaRestante: " + valorCuotaRestante + 
							" importeMinCuota: " + importeMinCuota);
					
					Double montoMinimoAValidar = anticipo.doubleValue();;
					// Se agrega validacion si es manual compara con valor cuota restante, sino con anticipo (bug 547)
					if (plan.getEsManual().intValue()==SiNo.SI.getId().intValue()){
						if (i>1) montoMinimoAValidar = valorCuotaRestante.doubleValue();
					}
					//	Si el valor de la cuota no es inferior a la minimo del plan, agrego la cuota a mostrar
					// Modificado para que la validacion sea del importe minimo contra el anticipo (Bug 288)
					if (montoMinimoAValidar.doubleValue() >= importeMinCuota.doubleValue()){
				  	    LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
				  		
				  	    liqCuotaVO.setNroCuota("" + i);
				  		
				  		liqCuotaVO.setEsSeleccionable(true);				  		
				  		liqCuotaVO.setAnticipo(anticipo);
				  		liqCuotaVO.setValorCuotasRestantes(valorCuotaRestante);
				  		liqCuotaVO.setTotal(totalPlan);
				  		
				  		liqPlanVO.getListAltCuotas().add(liqCuotaVO);
						  	    
					} else {
						break;
					}
			
				} else {
					break;
				}
					
			//Si el inters es nulo, armar mensaje para mostrar en la cuota correspondiente.
			} else {					
				LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
		  		
				liqCuotaVO.setNroCuota("" + i);
		  	    
		  		liqCuotaVO.setEsSeleccionable(false);
		  		liqCuotaVO.setMsgErrorCuota(GdeError.MSG_CUOTA_INTERES_NULO);
		  					  		
		  		liqPlanVO.getListAltCuotas().add(liqCuotaVO);
			}
		}
		
		// Paso de datos de Bean a VO
		liqPlanVO.setIdPlan(plan.getId());
		liqPlanVO.setDesPlan(plan.getDesPlan());
		liqPlanVO.setDesViaDeuda(plan.getViaDeuda().getDesViaDeuda());
		liqPlanVO.setLeyendaPlan(plan.getLeyendaPlan());
		liqPlanVO.setLinkNormativa(plan.getLinkNormativa());
				
		liqFormConvenioAdapter.setPlanSelected(liqPlanVO);
		
		
		return liqFormConvenioAdapter;
	}

	/**
	 * 
	 * Obtiene la simulacion de cuotas para un numero de cuotas recibido.
	 * 
	 * @author Cristian
	 * @param liqFormConvenioAdapter
	 * @return
	 * @throws Exception
	 */
	public static LiqFormConvenioAdapter getSimulacionCuotas(LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception{
		
		Long idPlan  = liqFormConvenioAdapter.getPlanSelected().getIdPlan();
		
		// Obtenemos el plan.
		Plan plan = Plan.getById(idPlan);
		
		Date fechaProbableForm; 
		
		if (liqFormConvenioAdapter.getEsEspecial() ){
			
			DatosPlanEspecial dpe = new DatosPlanEspecial();
			
			dpe.setCantMaxCuo(liqFormConvenioAdapter.getCantMaxCuo());
			dpe.setDescActualizacion(liqFormConvenioAdapter.getDescActualizacion());
			dpe.setDescCapital(liqFormConvenioAdapter.getDescCapital());
			dpe.setFechaFormalizacion(liqFormConvenioAdapter.getFechaFormalizacion());
			dpe.setImpMinCuo(liqFormConvenioAdapter.getImpMinCuo());
			dpe.setImporteAnticipo(liqFormConvenioAdapter.getImporteAnticipo());
			dpe.setInteres(liqFormConvenioAdapter.getInteres());
			dpe.setVenPrimeraCuota(liqFormConvenioAdapter.getVenPrimeraCuota());
			
			plan.setDatosPlanEspecial(dpe);
			
			fechaProbableForm = liqFormConvenioAdapter.getFechaFormalizacion();
			
		} else {
			fechaProbableForm = DateUtil.getDate(liqFormConvenioAdapter.getFechaFormSelected());		
			
		}
	
		// se pasan listLiqDeudaVO a listDeuda obteniendo cada una por id
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		for(LiqDeudaVO liqDeudaVO: (List<LiqDeudaVO>)liqFormConvenioAdapter.getListDeuda()){
			listDeuda.add(Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda()));
		}
		
		// Se instancia un container
		LiqDeudaConvenioContainer liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaProbableForm); 		
		
		String nroCuota = liqFormConvenioAdapter.getNroCuotaSelected();
		Integer cantidadCuotas = new Integer(nroCuota);
		
		// Reset de lista de cuotas para la simulacion
		liqFormConvenioAdapter.getPlanSelected().setListCuotasForm(new ArrayList<LiqCuotaVO>());
		
		// Se le solicita al Container que calcule la simulacion de la cuotas
		int retSimulacion = liqDeudaConvenioContainer.calcularSimulacionCuotas(plan, fechaProbableForm, cantidadCuotas);	

		// Si el resultado de la simulacion es erroneo, cargo el error correspondiente y retorno el adapter.
		if (retSimulacion > 1){
			
			if (LiqDeudaConvenioContainer.INTERES_NULO == retSimulacion ){
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_NULO);
			
			} else if (LiqDeudaConvenioContainer.INTERES_MAL_FORMATO == retSimulacion ){				
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_MAL_FORMATO);
				
			} else if (LiqDeudaConvenioContainer.VENCIMIENTO_NULO == retSimulacion ){
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VENCIMIENTO_NULO);
			}
			
			return liqFormConvenioAdapter;
		}

		// Si la simulacion fue EXITOSA se re actualiza la deuda seleccionada para mostrarla con los descuentos aplicados.
		// Se resetea la lista de LiqDeudaVO para recalcularla.
		liqFormConvenioAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());
		
		// Se recorre la lista de deuda obtenida y se calcula la actualizacion para cada una.
		PlanDescuento planDescuento = null;
		Boolean totalImpago = false;
		
		//Valido si aplica Total Impago 
		totalImpago = LiqFormConvenioBeanHelper.getEsTotalImpParaDescuento(listDeuda.get(0), listDeuda.size());
		
		if (totalImpago){
			planDescuento = plan.getPlanDescuentoTotImpago(cantidadCuotas, fechaProbableForm);
		}
		
		if (planDescuento == null){
			planDescuento =plan.getPlanDescuento(cantidadCuotas, fechaProbableForm);
		}

		
		for(Deuda deuda:listDeuda){
			LiqDeudaVO liqDeudaVO = getLiqDeudaVOActualizado(deuda, fechaProbableForm, planDescuento);
			liqFormConvenioAdapter.getListDeuda().add(liqDeudaVO);
		}
		
		liqFormConvenioAdapter.calcularTotalActualizado();
		
		List<ConvenioCuota> listConvenioCuota = liqDeudaConvenioContainer.getListConvenioCuota();

		// Traspado de Convenio Cuota -> LiqCuotaVO
		for (ConvenioCuota convenioCuota: listConvenioCuota){
			// Agregado de cuota a la lista de la simulacion.
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			
			// Seteo de valores a mostrar
			if (convenioCuota.getSellado()!=null){
				liqCuotaVO.setNroCuota(""+convenioCuota.getNumeroCuota()+"(*)");
				liqFormConvenioAdapter.setTieneSellado(true);
				liqFormConvenioAdapter.setImporteSelladoView(StringUtil.redondearDecimales(convenioCuota.getImporteSellado(), 0, SiatParam.DEC_IMPORTE_VIEW));
			}else{
				liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			}
			liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
			liqCuotaVO.setInteres(convenioCuota.getInteres());
			liqCuotaVO.setTotal(convenioCuota.getImporteCuota());
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			
			liqFormConvenioAdapter.getPlanSelected().getListCuotasForm().add(liqCuotaVO);			
		}
		
		liqFormConvenioAdapter.getPlanSelected().setDesCapitalOriginal(liqDeudaConvenioContainer.getDescuentoCapital());
		liqFormConvenioAdapter.getPlanSelected().setDesActualizacion(liqDeudaConvenioContainer.getDescuentoActualizacion());
		
		liqFormConvenioAdapter.getPlanSelected().setInteres(liqDeudaConvenioContainer.getInteres());
		liqFormConvenioAdapter.getPlanSelected().setDesInteres(liqDeudaConvenioContainer.getDescuentoInteres());
		liqFormConvenioAdapter.getPlanSelected().setInteresAplicado(liqDeudaConvenioContainer.getInteresAplicado());
		
		liqFormConvenioAdapter.getPlanSelected().setTotalCapital(liqDeudaConvenioContainer.getTotalCapital());
		liqFormConvenioAdapter.getPlanSelected().setTotalInteres(liqDeudaConvenioContainer.getTotalInteres());
		liqFormConvenioAdapter.getPlanSelected().setTotalImporte(liqDeudaConvenioContainer.getTotalImporte());
		
		return liqFormConvenioAdapter;
		
	}
	
	/**
	 * - Realiza la formalizacion del Convenio 
	 * - devuelve el adapter para poder hacer la impresion
	 * 
	 * @author Cristian
	 * @param liqFormConvenioAdapter
	 * @return
	 * @throws Exception
	 */
	public synchronized static LiqFormConvenioAdapter formalizarPlan(LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 

		try {
			
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			UserContext userContext = DemodaUtil.currentUserContext();
			
			Integer cantidadCuotas = new Integer(liqFormConvenioAdapter.getNroCuotaSelected());
			
			// Obtencion del Plan 		
			Plan plan = Plan.getById(liqFormConvenioAdapter.getPlanSelected().getIdPlan());
			
			Date fechaFormalizacion; 
			PlanDescuento planDescuento = null;
			PlanIntFin planIntFin = null; 
			
			if (liqFormConvenioAdapter.getEsEspecial() ){
				
				DatosPlanEspecial dpe = new DatosPlanEspecial();
				
				dpe.setCantMaxCuo(liqFormConvenioAdapter.getCantMaxCuo());
				dpe.setDescActualizacion(liqFormConvenioAdapter.getDescActualizacion());
				dpe.setDescCapital(liqFormConvenioAdapter.getDescCapital());
				dpe.setFechaFormalizacion(liqFormConvenioAdapter.getFechaFormalizacion());
				dpe.setImpMinCuo(liqFormConvenioAdapter.getImpMinCuo());
				dpe.setImporteAnticipo(liqFormConvenioAdapter.getImporteAnticipo());
				dpe.setInteres(liqFormConvenioAdapter.getInteres());
				dpe.setVenPrimeraCuota(liqFormConvenioAdapter.getVenPrimeraCuota());
				
				plan.setDatosPlanEspecial(dpe);
				
				fechaFormalizacion = liqFormConvenioAdapter.getFechaFormalizacion();
				
			} else {
				fechaFormalizacion = DateUtil.getDate(liqFormConvenioAdapter.getFechaFormSelected());		
				// Se recorre la lista de deuda obtenida y se calcula la actualizacion para cada una.
				Boolean totalImpago = false;
				Deuda primerDeuda = Deuda.getById(((LiqDeudaVO)liqFormConvenioAdapter.getListDeuda().get(0)).getIdDeuda());
				
				//Valido si aplica Total Impago 
				totalImpago = LiqFormConvenioBeanHelper.getEsTotalImpParaDescuento(primerDeuda, liqFormConvenioAdapter.getListDeuda().size());
				
				if (totalImpago){
					planDescuento = plan.getPlanDescuentoTotImpago(cantidadCuotas, fechaFormalizacion);
				}
				
				if (planDescuento == null){
					planDescuento =plan.getPlanDescuento(cantidadCuotas, fechaFormalizacion);
				}
				
				// Obtencion del Plan Int Fin utilizado
				planIntFin = plan.getPlanIntFin(cantidadCuotas, fechaFormalizacion);
			}
						
			
			// Obtencion de la cuenta
			Cuenta cuenta = Cuenta.getById(liqFormConvenioAdapter.getCuenta().getIdCuenta());
			// Obtencion del Plan Descuento utilizado

			// Obtencion del Estado Convenio
			EstadoConvenio estadoConvenio = EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE); 
			// Obtencion del tipo de persona que formaliza
			TipoPerFor tipoPerFor = TipoPerFor.getByIdNull(liqFormConvenioAdapter.getConvenio().getTipoPerFor().getId());
			// Obtencion del tipo de documentacion aportada
			TipoDocApo  tipoDocApo = TipoDocApo.getByIdNull(liqFormConvenioAdapter.getConvenio().getTipoDocApo().getId());  
			
			// Obtencion del canal
			Canal canal = Canal.getById(userContext.getIdCanal());
			
			// Obtenemos el area
			Area area = Area.getByIdNull(userContext.getIdArea());
			
			// Obtenemos la oficina
			Oficina oficina = Oficina.getByIdNull(userContext.getIdOficina());
			
			//Obtengo el Recurso de la cuenta
			Recurso recurso = cuenta.getRecurso();
			
			/* ****************************************************************
			 * 				Recalculo de las cuotas
			 * ****************************************************************
			 */
			
			// Se pasan listLiqDeudaVO a listDeuda
			List<Deuda> listDeuda = new ArrayList<Deuda>();
			
			
/* *************************************************************************************************** 
 * 				 -Metodo utilizado para simular que existe un registro de deuda de distinta via a la seleccionada.
 * 				 -Este caso no deberia ocurrir nunca en produccion.
 * 
				 LiqDeudaVO liqDeudaTestDistinaVia = new LiqDeudaVO();
				 
				 liqDeudaTestDistinaVia.setIdDeuda(19621022L);
				 liqDeudaTestDistinaVia.setIdEstadoDeuda(5L);
				 
				 liqFormConvenioAdapter.getListDeuda().add(liqDeudaTestDistinaVia);
				 
 * 
 * ***************************************************************************************************
 */

			
			Long idViaDeuda = null;
			for(LiqDeudaVO liqDeudaVO: (List<LiqDeudaVO>)liqFormConvenioAdapter.getListDeuda()){
				
				Deuda deuda = Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda());
				
				//	Validamos que toda la deuda seleccionada corresponda a la misma  via.
				// Seteo la via de la primer deuda
				if (idViaDeuda == null){
					idViaDeuda = deuda.getViaDeuda().getId();
				
				} else if (idViaDeuda.longValue() != deuda.getViaDeuda().getId()){
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_SELECT_DEUDA_DISTINTA_VIA);
					return liqFormConvenioAdapter;			
				}
				
				listDeuda.add(deuda);
			}
			
			ViaDeuda viaDeuda = listDeuda.get(0).getViaDeuda();

			// Validamos que solo un procurado pueda formalizar deuda en via judicial 			
			if (userContext.getEsProcurador() && ViaDeuda.ID_VIA_JUDICIAL != viaDeuda.getId().longValue()){
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VIA_DEUDA_NO_PERMITIDA_USR);
				return liqFormConvenioAdapter;
			}
			
			// Si es procurador y deuda en via judicial, toda la deuda debe pertenecer al mismo.
			if (userContext.getEsProcurador() && ViaDeuda.ID_VIA_JUDICIAL == viaDeuda.getId().longValue()){
				boolean perteneceDeduaProcurador = true;
				
				for (Deuda deuda:listDeuda){
					if (deuda.getProcurador() == null || deuda.getProcurador().getId() == null ||
							deuda.getProcurador().getId().longValue() != userContext.getIdProcurador().longValue()){
						perteneceDeduaProcurador = false;
					}					
				}
				
				if (!perteneceDeduaProcurador){
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_DEUDA_NO_PERMITIDA_USR);
					return liqFormConvenioAdapter;
				}
			}
			
			// Si el usuario es operador, toda la deuda debe pertenecer al mismo procurador
			if (userContext.getEsOperadorJudicial() && ViaDeuda.ID_VIA_JUDICIAL == viaDeuda.getId().longValue()){
				boolean perteneceDeduaMismoProcurador = true;
				
				Long idProcuradorPrimerReg = listDeuda.get(0).getProcurador().getId();
				
				for (Deuda deuda:listDeuda){
					if (deuda.getProcurador() == null || deuda.getProcurador().getId() == null ||
							deuda.getProcurador().getId().longValue() != idProcuradorPrimerReg.longValue()){
						perteneceDeduaMismoProcurador = false;
					}
				}
				
				if (!perteneceDeduaMismoProcurador){
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_DEUDA_NO_PERMITIDA_USR);
					return liqFormConvenioAdapter;
				}
			}
						
			if (userContext.getEsUsuarioCMD() && ViaDeuda.ID_VIA_ADMIN != viaDeuda.getId().longValue()){
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VIA_DEUDA_NO_PERMITIDA_USR);
				return liqFormConvenioAdapter;
			}
			
			// Obtencion del Procurador (una vez que se pasaron las validaciones anteriores)
			Procurador procurador;
			
			if (userContext.getEsOperadorJudicial()){
				Long idProcuradorPrimerReg = listDeuda.get(0).getProcurador().getId();
				procurador = Procurador.getByIdNull(idProcuradorPrimerReg);
				
			} else {
				procurador = Procurador.getByIdNull(userContext.getIdProcurador());
			}
			
			// Instancio un Deuda Convenio Container
			LiqDeudaConvenioContainer liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaFormalizacion);
			
			// Llamo a la simulacion de cuotas.
			int retSimulacion = liqDeudaConvenioContainer.calcularSimulacionCuotas(plan, fechaFormalizacion, cantidadCuotas);
			
			//	Si el resultado de la simulacion es erroneo, cargo el error correspondiente y retorno el adapter.
			if (retSimulacion > 1){
				
				if (LiqDeudaConvenioContainer.INTERES_NULO == retSimulacion ){
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_NULO);
				
				} else if (LiqDeudaConvenioContainer.INTERES_MAL_FORMATO == retSimulacion ){				
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_MAL_FORMATO);
					
				} else if (LiqDeudaConvenioContainer.VENCIMIENTO_NULO == retSimulacion ){
					liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VENCIMIENTO_NULO);
				}
				
				return liqFormConvenioAdapter;
			}
						
			List<ConvenioDeuda> listConvenioDeuda = liqDeudaConvenioContainer.getListConvenioDeuda();
			
			List<ConvenioCuota> listConvenioCuota = liqDeudaConvenioContainer.getListConvenioCuota();
			
			Convenio convenio = new Convenio(); 
			// Seteo el Plan
			convenio.setPlan(plan);
			// Numero de Convenio
			convenio.setNroConvenio(plan.obtenerNroConvenio());
			// Cuenta		
			convenio.setCuenta(cuenta);
			//Recurso
			convenio.setRecurso(recurso);
			// Via Deuda
			convenio.setViaDeuda(viaDeuda);
			// Canal
			convenio.setCanal(canal);
			// PlanDescuento
			convenio.setPlanDescuento(planDescuento);
			// PlanIntFin
			convenio.setPlanIntFin(planIntFin);			
			// Estado Convenio = Vigente
			convenio.setEstadoConvenio(estadoConvenio);
			// Procurador		
			convenio.setProcurador(procurador);
			// Fecha de Alta
			convenio.setFechaAlta(new Date());
			
			Sistema sistemESB = plan.getSistema().getSistemaEsServicioBanco();
			if (sistemESB == null){
				// Retornamos error por encontrar mas de un sistema, esServicioBanco.
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_SERVICIOBANCO_NO_UNICO);
				return liqFormConvenioAdapter;
			}
			
			convenio.setSistema(sistemESB);
			// Usuario For
			convenio.setUsuarioFor(userContext.getUserName());
			// Fecha Formalizacion
			if (liqFormConvenioAdapter.getEsEspecial() ){
				convenio.setFechaFor(new Date());
			}else{
				convenio.setFechaFor(fechaFormalizacion);
			}
			// Tipo Persona que Formaliza
			convenio.setTipoPerFor(tipoPerFor);
			// Persona que Formaliza
			if(liqFormConvenioAdapter.getConvenio().getPersona().getEsPersonaJuridica()){
				// Retornamos error por encontrar que la persona que formaliza el convenio es de tipo Juridica.
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_PERSONA_JURIDICA);
				return liqFormConvenioAdapter;				
			}
			convenio.setIdPerFor(liqFormConvenioAdapter.getConvenio().getPersona().getId());
			// Tipo de Documentacion Aportada
			convenio.setTipoDocApo(tipoDocApo);
			// Observacion Formalizacion
			convenio.setObservacionFor(liqFormConvenioAdapter.getConvenio().getObservacionFor());
			
			// Total Capital Original
			convenio.setTotCapitalOriginal(liqDeudaConvenioContainer.calcularTotalCapitalOriginal());
			// Descuento al Capital Original
			convenio.setDesCapitalOriginal(liqDeudaConvenioContainer.getTotalDescuentoCapOri());
			// Total Actualizacion
			convenio.setTotActualizacion(liqDeudaConvenioContainer.calcularTotalActualizacion());
			// Descuento de la Actulizacion
			convenio.setDesActualizacion(liqDeudaConvenioContainer.getTotalDescuentoActualiz());
			// Total Interes
			convenio.setTotInteres(liqDeudaConvenioContainer.getTotalInteres());
			// Descuento Interes
			convenio.setDesInteres(liqDeudaConvenioContainer.getTotalDescuentoInteres());
			// Importe Convenio
			convenio.setTotImporteConvenio(liqDeudaConvenioContainer.getTotalImporte());
			
			// Cantidad Cuotas Plan
			convenio.setCantidadCuotasPlan(cantidadCuotas);
			// Ultima Cuota Imputada = 0
			convenio.setUltCuoImp(0);
			// IP de la maquina donde se formaliza el convenio.
			convenio.setIp(userContext.getIpRequest());
			
			// Ofinica
			convenio.setOficina(oficina);
			// Area
			convenio.setArea(area);
			
			//Aplica pagos a cuenta
			convenio.setAplicaPagCue(SiNo.SI.getId());
			
			// Si el plan es especial, se registra el uso de expediente
			if (liqFormConvenioAdapter.getEsEspecial()){
			
				// 1) Registro uso de expediente 
	        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_FORMALIZACION_PLAN); 
	        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(
	        			liqFormConvenioAdapter.getConvenio(), 
	        			convenio, 
	        			accionExp, 
	        			convenio.getCuenta(), 
	        			convenio.infoString() );
	        	// Si no pasa la validacion, vuelve a la vista. 
	        	if (liqFormConvenioAdapter.getConvenio().hasError()){
	        		tx.rollback();
	        		liqFormConvenioAdapter.getConvenio().passErrorMessages(liqFormConvenioAdapter);
	        		return liqFormConvenioAdapter;
	        	}
	        	// 2) Esta linea debe ir siempre despues de 1).
	        	convenio.setIdCaso(liqFormConvenioAdapter.getConvenio().getIdCaso());
			}
			
			plan.formalizarConvenio(convenio,
									listDeuda,
									listConvenioDeuda, 
									listConvenioCuota);
			
						
		    if (convenio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}            	
            	convenio.passErrorMessages(liqFormConvenioAdapter);
			} else {
				tx.commit();
				
				// Seteo el id del convenio creado para volver a obtenerlo para la pantalla de impresion.
				liqFormConvenioAdapter.getConvenio().setIdConvenio(convenio.getId());
				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return liqFormConvenioAdapter;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Chekea si se muestra habilitado o no el plan. 
	 * 
	 * @author Cristian
	 * @param plan
	 * @param cuenta
	 * @param listDeuda
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	private static boolean chkEsSeleccionable(Plan plan, Cuenta cuenta, List<Deuda> listDeuda, Date fechaForm, LiqPlanVO liqPlanVO) throws Exception{
		
		String funcName = "chkEsSeleccionable";
		Date minFechaVto = getMinFechaVto(listDeuda);
		Date maxFechaVto = getMaxFechaVto(listDeuda);
		
		log.debug(funcName + " plan: " + plan.getDesPlan()); 
				
		// ---> 4.2.2.1 Chekeo de exenciones del plan contra las de la cuenta **************************************.
		List<Exencion> listExencionesPlan = plan.getListExencionesVigentes(fechaForm);
				
		log.debug(funcName + " listExencionesPlan.size: " + listExencionesPlan.size());
		
		// Si el plan posee exenciones, y la cuenta no, salgo por falso.
		if (listExencionesPlan.size() > 0){
			if (!cuenta.tieneAlgunaExencion(listExencionesPlan, fechaForm)){
				for (Deuda deuda:listDeuda){
					if (!cuenta.tieneAlgunaExencion(listExencionesPlan, deuda.getFechaVencimiento())){
						log.debug(funcName + " Deshabilitado: Por no poseer ninguna de las exenciones del Plan ");
						liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_EXENCION);
						return false;
					}
				}
			}
		}
		
		// ---> 4.2.2.2 Validar el tipo de deuda a incluir en el plan "Vencida", "No Vencida" o "Ambas"
		// Vencida
		if (TipoDeudaPlan.ID_TIPO_DEUDA_VENCIDA.equals(plan.getTipoDeudaPlan().getId())){			
			log.debug(funcName + ": check deuda Vencida minFechaVto: " + DateUtil.formatDate(minFechaVto, DateUtil.ddSMMSYYYY_MASK) +
								" maxFechaVto: " + DateUtil.formatDate(maxFechaVto, DateUtil.ddSMMSYYYY_MASK) + 
								" fechaProbableForm: " + DateUtil.formatDate(fechaForm, DateUtil.ddSMMSYYYY_MASK));
			
			if (DateUtil.isDateAfterOrEqual(minFechaVto, fechaForm) ||
					DateUtil.isDateAfterOrEqual(maxFechaVto, fechaForm)){
				log.debug(funcName + " Deshabilitado: Por exitir deuda No Vencida a la fecha " + 
						DateUtil.formatDate(fechaForm, DateUtil.ddSMMSYYYY_MASK) );
				liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_TPO_DEU_VENCIDA);
				return false;
			}
		}
		// No Vencida
		if (TipoDeudaPlan.ID_TIPO_DEUDA_NO_VENCIDA.equals(plan.getTipoDeudaPlan().getId())){			
			log.debug(funcName + " check deuda NO Vencida minFechaVto: " + DateUtil.formatDate(minFechaVto, 
						DateUtil.ddSMMSYYYY_MASK) +
					" maxFechaVto: " + DateUtil.formatDate(maxFechaVto, DateUtil.ddSMMSYYYY_MASK) + 
					" fechaProbableForm: " + DateUtil.formatDate(fechaForm, DateUtil.ddSMMSYYYY_MASK));
			if (DateUtil.isDateBeforeOrEqual(minFechaVto, fechaForm) ||
					DateUtil.isDateBeforeOrEqual(maxFechaVto, fechaForm)){
				log.debug(funcName + " Deshabilitado: Por exitir deuda Vencida a la fecha " + 
						DateUtil.formatDate(fechaForm, DateUtil.ddSMMSYYYY_MASK) );
				liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_TPO_DEU_NO_VENCIDA);
				return false;
			}
		}
		
		// ---> 4.2.2.3 Validar que el rango de fechas de vencimientos ingresado se encuentre dentro de los configurados en el plan.
		if (DateUtil.isDateAfter(plan.getFecVenDeuDes(), minFechaVto) ||
				DateUtil.isDateBefore(plan.getFecVenDeuHas(), maxFechaVto) ){
			
			log.debug(funcName + " Deshabilitado: Encontrarse Fuera del rango plan.getFecVenDeuDes(): " + 
					DateUtil.formatDate(plan.getFecVenDeuDes(), DateUtil.ddSMMSYYYY_MASK) +
					"  plan.getFecVenDeuHas(): " + DateUtil.formatDate(plan.getFecVenDeuHas(), DateUtil.ddSMMSYYYY_MASK));
			liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_FECHAS_VTO_FUERA_RANGO);
			return false;
		}
		
		
		// ---> 4.2.2.4 Validar que la Clasificacion de los registros de deuda seleccionados se corresponda con los
		// que tiene el plan en su configuracion.
		List<RecClaDeu> listClaDeuPlan = plan.getListClaDeuVigentes(fechaForm);
		
		// Cambio introduciodo el 29-05-2008 
		// (Si el plan no posee Clasificaciones de Deuda Vigentes, todas las seleccionadas son validas)
		if (listClaDeuPlan != null && listClaDeuPlan.size() > 0 ){
		
			List<RecClaDeu> listClaDeuDeudaSel = new ArrayList<RecClaDeu>(); 
			
			// Formo un listado con un distinct de la clasificacion de deuda de los registro seleccionados. 
			for (Deuda deuda:listDeuda){
				RecClaDeu recClaDeu = deuda.getRecClaDeu();
				if (!ListUtilBean.contains(listClaDeuDeudaSel, recClaDeu)){
					listClaDeuDeudaSel.add(recClaDeu);
				}			
			}
			
			// Chequeo si alguna de las clasificaciones de deuda no es encontrada en la configuracion del plan 
			for (RecClaDeu recClaDeuChk:listClaDeuDeudaSel){			
				if (!ListUtilBean.contains(listClaDeuPlan, recClaDeuChk)){
					log.debug(funcName + " Deshabilitado: De encuentra una clasificacion deuda que no corresponde al plan: " + 
							recClaDeuChk.getDesClaDeu());
					
					liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_CLA_DEU);
					return false;
				}
			}
		}
		
		// ---> 4.2.2.5 Validar que suma de saldos historios sea mayor o igual que Importe Minimo del plan
		Double totSaldoHist = 0D;
		
		for (Deuda deuda:listDeuda){
			//se modifica a partir del 07/11/08 para que verifique contra importe actualizado normal
			//totSaldoHist += deuda.getSaldo(); 
			totSaldoHist += deuda.actualizacionSaldo(fechaForm).getImporteAct();
		}
		
		Double minimoImporteDeuda = plan.getMinimoImpMinDeu(fechaForm);
		
		log.debug(funcName + " Suma Saldo hist: " + totSaldoHist + " Importe Min. Plan: " + minimoImporteDeuda);
		
		if (totSaldoHist.doubleValue() < minimoImporteDeuda.doubleValue()){
			log.debug(funcName + " Deshabilitado: Suma Saldo hist: " + totSaldoHist + " Importe Min. Plan: " + minimoImporteDeuda);
			liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_IMP_MINIMO);
			return false;
		}
		
		// ---> 4.2.2.6 Validar que la cantidad de periodos seleccionados sea mayor o igual a Cantidad Minima Periodos Plan
		if (listDeuda.size() < plan.getCanMinPer().intValue()) {
			log.debug(funcName + " Deshabilitado: Cantidad periodos seleccionados menor que configuracion del plan");
			liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_CANT_MIN_PERIODO);
			return false;
		}
				
		/* ---> 4.2.2.7	Si el plan "Aplica Total Impago", verificar: 
		 * Que no quede deuda del Tipo, Clasificacion y Rango de Fechas de Vencimiento sin Seleccionar
		 */
		
		// ---> 4.2.2.8 Si el usuario posee permisos especiales, puenteamos esta validacion.
		// TODO: implementar la validacion  4.2.2.5
		//UserContext userContext = DemodaUtil.currentUserContext();
		//if(!userContext.getEsEspecial()){
		
		// Si el plan Aplica Total Impago 
		// Cuando se llega a esta instancia ya se valido que la via de toda la deuda seleccionada sea la misma.
		if (plan.getAplicaTotalImpago().intValue() == 1) { 
		
			// Antes se valido que toda la deuda seleccionada corresonde al mismo estado.
			EstadoDeuda estadoDeuda = listDeuda.get(0).getEstadoDeuda();
			Long idViaDeudaSelected = listDeuda.get(0).getViaDeuda().getId();
			
			// Obtener la lista de ids de la deuda seleccionada
			Long[] listIdsDeudaSel = ListUtilBean.getArrLongIdFromListBaseBO(listDeuda);
			
			// Recupero la lista total de Clasificacion de deuda del recurso de la cuenta.
			List<RecClaDeu> listTotalRecClaDeu = RecClaDeu.getListByIdRecurso(cuenta.getRecurso().getId()); 
			
			// A la lista anterior le quito los elementos de la de la configuracion del plan.
			List<RecClaDeu> listRecClaDeuAExcluir = (List<RecClaDeu>) ListUtilBean.getListExclude(listTotalRecClaDeu, listClaDeuPlan);
			
			// Formo una lista con los ids de la clasificaciones de deuda que NO pertenecen al Pan
			Long[] listIdsRecClaDeuAExcluir = ListUtilBean.getArrLongIdFromListBaseBO(listRecClaDeuAExcluir);
			
			Date fechaVtoDesde = plan.getFecVenDeuDes();
			Date fechaVtoHasta = plan.getFecVenDeuHas();
			
			// Dependiendo del Tipo de Deuda del Plan "Vencida", "No Vencida" o "Ambas", tuneo el rango de fecha a consultar
			if ( TipoDeudaPlan.ID_TIPO_DEUDA_VENCIDA.equals(plan.getTipoDeudaPlan().getId())){
				if (DateUtil.isDateAfter(fechaVtoHasta, fechaForm))
					fechaVtoHasta = fechaForm;
			}
			if ( TipoDeudaPlan.ID_TIPO_DEUDA_NO_VENCIDA.equals(plan.getTipoDeudaPlan().getId())){
				if (DateUtil.isDateBefore(fechaVtoDesde, fechaForm))
					fechaVtoDesde = fechaForm;
			}
			
			List<Deuda> listDeudaNoSelect = Deuda.existeDeudaNotIn(cuenta, listIdsDeudaSel, estadoDeuda.getId(), listIdsRecClaDeuAExcluir, 
					fechaVtoDesde, fechaVtoHasta, idViaDeudaSelected);
			
			/*	Si existe deuda devuelta, chequear que NO sea:
					 *  	- EsExcentaPago
					 *  	- EsConvenio
					 *		- EsIndeterminada
					 *		- EsReclamada
			*/
				
			// Esta bandera se setea en el bucle a "true", solo si existe deuda 
			// que cumpla las condiciones para ser seleccionada y 
			// no halla sido seleccionada por el usuario
			boolean hayDeudaNoSelect = false; 
			
			for(Deuda da:listDeudaNoSelect) {
				if (!da.getEsExcentaPago() &&
						!da.getEsConvenio() &&
						!da.getEsIndeterminada() && 
						!da.getEsReclamada()  &&  
						!da.getEstaEnAsentamiento() ){
					
					hayDeudaNoSelect = true;
				}
			}
			
			if (hayDeudaNoSelect){				
				log.debug(funcName + " Deshabilitado: Plan aplica total impago y Existe deuda sin seleccionar" );
				log.debug(funcName + " estadoDeuda: " + estadoDeuda.getDesEstadoDeuda());
				log.debug(funcName + " TipoDeudaPlan: " +plan.getTipoDeudaPlan().getDesTipoDeudaPlan());
				log.debug(funcName + " fechaForm: " + DateUtil.formatDate(fechaForm, DateUtil.ddSMMSYYYY_MASK));
				log.debug(funcName + " plan.getFecVenDeuDes: " + DateUtil.formatDate(plan.getFecVenDeuDes(), DateUtil.ddSMMSYYYY_MASK));
				log.debug(funcName + " plan.getFecVenDeuHas: " + DateUtil.formatDate(plan.getFecVenDeuHas(), DateUtil.ddSMMSYYYY_MASK));
				log.debug(funcName + " fechaVtoDesde consultar: " + DateUtil.formatDate(fechaVtoDesde, DateUtil.ddSMMSYYYY_MASK));
				log.debug(funcName + " fechaVtoHasta consultar: " + DateUtil.formatDate(fechaVtoHasta, DateUtil.ddSMMSYYYY_MASK));
				
				liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_APLICA_TOT_IMPAGO);
				return false;
			}
		}	
		// <--- Fin 4.2.2.7
		
		
		// 4.2.2.9 Validar Atributos Valorizados
		if (plan.getListPlanAtrVal()!=null && plan.getListPlanAtrVal().size()>0){
			for (PlanAtrVal planAtrVal: plan.getListPlanAtrVal()){
				GenericAtrDefinition genAtrDef=cuenta.getCuentaDefinitionValueFullContr(new Date()).getGenericAtrDefinition(planAtrVal.getAtributo().getId());
				if(genAtrDef == null || genAtrDef.getValorString()==null || !genAtrDef.getValorString().equals(planAtrVal.getValor()) ){
					liqPlanVO.setMsgDeshabilitado(GdeError.MSG_PLAN_DESHAB_X_ATR_VAL);
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Devuelve la menor fecha vencimiento de la lista de deuda recibida
	 * Supone la lista ordenada por fechaVencimiento.
	 * 
	 * @author Cristian
	 * @param listDeuda
	 * @return date
	 */
	private static Date getMinFechaVto(List<Deuda> listDeuda){
		Date minFechaVto = listDeuda.get(0).getFechaVencimiento();
				
		return minFechaVto;
	}
	
	/**
	 * Devuelve la Mayor fecha vencimiento de la lista de deuda recibida
	 * Supone la lista ordenada por fechaVencimiento.
	 * 
	 * @author Cristian
	 * @param listDeuda
	 * @return date
	 */
	private static Date getMaxFechaVto(List<Deuda> listDeuda){
		Date maxFechaVto = listDeuda.get(listDeuda.size() -1).getFechaVencimiento();
				
		return maxFechaVto;
	}

	/**
	 * Obtiene un convenio ya formalizado para ser mostrado y permitir la impresion de formulario de formalizacion
	 * y el talonario de recibos de cuotas.
	 * 
	 * @author Cristian
	 * @param liqFormConvenioAdapter
	 * @return
	 * @throws Exception
	 */
	public static LiqFormConvenioAdapter getConvenioFormalizado(LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception{
		
		Convenio convenio = Convenio.getById(liqFormConvenioAdapter.getConvenio().getIdConvenio());
		
		// Se reseta el liqFormConvenioAdapter para evitar errones en la vista
		LiqFormConvenioAdapter liqFormConvenioAdapterVO = new LiqFormConvenioAdapter(); 
		
		// Pasamos el filtro
		liqFormConvenioAdapterVO.setCuentaFilter(liqFormConvenioAdapter.getCuentaFilter());
		
		// De ConvenoDeuda a listDeuda
		String [] idDeudaEstadoDeudaSelected = new String[convenio.getListConvenioDeuda().size()];
		
		liqFormConvenioAdapterVO.setListDeuda(new ArrayList<LiqDeudaVO>());
		
		int i=0;	
		for (ConvenioDeuda convenioDeuda: convenio.getListConvenioDeuda()){
			
			idDeudaEstadoDeudaSelected[i] = convenioDeuda.getDeuda().getId() + "-" + convenioDeuda.getDeuda().getEstadoDeuda().getId();
			i++;
			
			LiqDeudaVO liqDeudaVO = new LiqDeudaVO();
			liqDeudaVO.setIdDeuda(convenioDeuda.getDeuda().getId());
			liqDeudaVO.setIdEstadoDeuda(convenioDeuda.getDeuda().getEstadoDeuda().getId());
			liqDeudaVO.setPeriodoDeuda(convenioDeuda.getDeuda().getStrPeriodo());			
			liqDeudaVO.setFechaVencimiento(convenioDeuda.getDeuda().getFechaVencimiento());
			liqDeudaVO.setSaldo(convenioDeuda.getCapitalEnPlan());			
			liqDeudaVO.setActualizacion(convenioDeuda.getActEnPlan());
			liqDeudaVO.setTotal(convenioDeuda.getTotalEnPlan());
			
			liqFormConvenioAdapterVO.getListDeuda().add(liqDeudaVO);
			
		}

		liqFormConvenioAdapterVO.setListIdDeudaSelected(idDeudaEstadoDeudaSelected);
		
		// Se le pide al LiqConvenioAdapter que calcule el total Actualizado de la deuda seleccionada.
		liqFormConvenioAdapterVO.calcularTotalActualizado();
		
		List<ConvenioCuota> listConvenioCuota = convenio.getListConvenioCuota();

		// Cuenta	**********************	
		convenio.getCuenta().setLiqCuentaFilter(liqFormConvenioAdapterVO.getCuentaFilter());
		
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(convenio.getCuenta()); 
		LiqCuentaVO LiqCuentaVO = liqDeudaBeanHelper.getCuenta();
		liqFormConvenioAdapterVO.setCuenta(LiqCuentaVO);
		
		// Convenio ******************
		
		liqFormConvenioAdapterVO.setNroCuotaSelected("" +convenio.getCantidadCuotasPlan());		
		liqFormConvenioAdapterVO.setFechaFormSelected( DateUtil.formatDate(convenio.getFechaFor(), DateUtil.ddSMMSYYYY_MASK));
		
		liqFormConvenioAdapterVO.getConvenio().setIdConvenio(convenio.getId());
		
		liqFormConvenioAdapterVO.getConvenio().setNroConvenio("" + convenio.getNroConvenio());
		liqFormConvenioAdapterVO.getConvenio().setFechaFor(DateUtil.formatDate(convenio.getFechaFor(), DateUtil.ddSMMSYYYY_MASK));		
		
		liqFormConvenioAdapterVO.getConvenio().setCantidadCuotasPlan(convenio.getCantidadCuotasPlan());
		
		liqFormConvenioAdapterVO.getConvenio().setTotImporteConvenio(convenio.getTotImporteConvenio());
		
		// Diferenciacion con los convenios migrados que no poseen datos de la persona que lo formalizo.
		if (convenio.getIdPerFor() != null){
			Persona persona = Persona.getById(convenio.getIdPerFor());
			liqFormConvenioAdapterVO.getConvenio().setPersona((PersonaVO) persona.toVO(3));
		} else {
			PersonaVO personaNoExiste = new PersonaVO();
			personaNoExiste.setNombres(convenio.getObservacionFor());
			liqFormConvenioAdapterVO.getConvenio().setPersona(personaNoExiste);
		}
		
		liqFormConvenioAdapterVO.getConvenio().setTipoPerFor((TipoPerForVO) convenio.getTipoPerFor().toVO());
		liqFormConvenioAdapterVO.getConvenio().setObservacionFor(convenio.getObservacionFor());
		
		if (convenio.getPlan().getEsManual().intValue()==1){
			liqFormConvenioAdapterVO.setEsEspecial(true);
		}

		// Si la via del convenio es judicial, seteamos el procurador		
		if (convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
			liqFormConvenioAdapterVO.getConvenio().setProcurador((ProcuradorVO) convenio.getProcurador().toVO(0, false));
			liqFormConvenioAdapterVO.getConvenio().setPoseeProcurador(true);
		}
		
		// Plan **********************
		LiqPlanVO liqPlanVO = new LiqPlanVO();
		
		liqPlanVO.setIdPlan(convenio.getPlan().getId());
		liqPlanVO.setDesPlan(convenio.getPlan().getDesPlan());
		liqPlanVO.setDesViaDeuda(convenio.getPlan().getViaDeuda().getDesViaDeuda());
		liqPlanVO.setLeyendaPlan(convenio.getPlan().getLeyendaPlan());
						
		liqFormConvenioAdapterVO.setPlanSelected(liqPlanVO);
		
		
		// Convenio Cuotas **********************
		// Traspado de Convenio Cuota -> LiqCuotaVO
		for (ConvenioCuota convenioCuota: listConvenioCuota){
			// Agregado de cuota a la lista de la simulacion.
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			
			// Seteo de valores a mostrar
			if (convenioCuota.getNumeroCuota()==1 && convenioCuota.getSellado()!=null){
				liqCuotaVO.setNroCuota(""+convenioCuota.getNumeroCuota()+"(*)");
				liqFormConvenioAdapter.setTieneSellado(true);
				liqFormConvenioAdapter.setImporteSelladoView(StringUtil.redondearDecimales(convenioCuota.getImporteSellado(), 0, SiatParam.DEC_IMPORTE_VIEW));
			}else{
				liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			}
			liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
			liqCuotaVO.setInteres(convenioCuota.getInteres());
			liqCuotaVO.setTotal(convenioCuota.getImporteCuota());
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			
			liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().add(liqCuotaVO);			
			
		}
		
		Double descCapitalOri = convenio.getDesCapitalOriginal() / convenio.getTotCapitalOriginal();
		descCapitalOri = NumberUtil.round(descCapitalOri, SiatParam.DEC_PORCENTAJE_CALC);
		
		liqFormConvenioAdapterVO.getPlanSelected().setDesCapitalOriginal(descCapitalOri);
		Double descActualizacion;
		if (convenio.getTotActualizacion()!=0){
			descActualizacion = convenio.getDesActualizacion() / convenio.getTotActualizacion();
		}else{
			descActualizacion=1D;
		}
		descActualizacion = NumberUtil.round(descActualizacion, SiatParam.DEC_PORCENTAJE_CALC);
		
		liqFormConvenioAdapterVO.getPlanSelected().setDesActualizacion(descActualizacion);
		
		// Si no es manual, recupero el Plan Int Fin Utilizado.
		if (convenio.getPlan().getEsManual().intValue() == 0 ){
			PlanIntFin planIntFin = convenio.getPlanIntFin();
			
			if (planIntFin != null){
				Double descInteres;
				if (convenio.getTotInteres()!=0){
					descInteres = convenio.getDesActualizacion() / convenio.getTotActualizacion();
				}else{
					descInteres = 1D;
				}
				Double interesAplicado = new Double(planIntFin.getInteres() - (planIntFin.getInteres()*descInteres));
				interesAplicado = NumberUtil.round(interesAplicado, SiatParam.DEC_PORCENTAJE_CALC);
				
				liqFormConvenioAdapterVO.getPlanSelected().setInteres(planIntFin.getInteres());
				liqFormConvenioAdapterVO.getPlanSelected().setDesInteres(descInteres);
				liqFormConvenioAdapterVO.getPlanSelected().setInteresAplicado(interesAplicado ); 
			}
		}
		
		liqFormConvenioAdapterVO.getPlanSelected().setTotalCapital(convenio.calcularTotCapDesc());
		liqFormConvenioAdapterVO.getPlanSelected().setTotalInteres(convenio.getTotInteres());
		liqFormConvenioAdapterVO.getPlanSelected().setTotalImporte(convenio.getTotImporteConvenio());
		
		// Seteo como anticipo el valor de la cuota 1
		liqFormConvenioAdapterVO.getConvenio().setAnticipo(listConvenioCuota.get(0).getImporteCuota());
		
		return liqFormConvenioAdapterVO;
	
	}
	
	
	public static Boolean getEsReconfeccion (Convenio convenio, Long[] listIdsCuotasSelected) throws Exception{
		List<ConvenioCuota> listConvenioCuota;
		listConvenioCuota = (List<ConvenioCuota>) ListUtilBean.getListBeanByListId(convenio.getListConvenioCuota(), listIdsCuotasSelected);
		Boolean esReconfeccion=false;
		for (ConvenioCuota convenioCuota : listConvenioCuota){
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), new Date())){
				esReconfeccion = true;
				break;
			}
		}
		return esReconfeccion;
	}
	
	/**
	 * Devuelve una lista de LiqRecibosVO listos para imprimir
	 * 
	 * Chequea si vienen ids de cuota seleccionados, o si debe imprimir todas las cuotas del convenio.
	 * Si son Cuotas Vencidas, las reconfecciona.
	 * Si son Cuotas NO Vencidas, las imprime directamente.   
	 * 
	 * 
	 * @author Cristian
	 * @param convenio
	 * @return
	 * @throws Exception
	 */
	public static List<LiqReciboVO> getListLiqReciboVO(Convenio convenio, Long[] listIdsCuotasSelected) throws Exception {
		
		List<LiqReciboVO> listReciboVO = new ArrayList<LiqReciboVO>();
		
		Cuenta cuenta = null;
		Procedimiento procedimiento = null;
		
		LiqCuentaVO liqCuentaVO = null;
		ProcedimientoVO procedimientoVO = null;
				
		// Obtenemos el LiqCuentaVO
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(convenio);
				
		LiqConvenioVO liqConvenioVO = liqDeudaBeanHelper.getConvenio(); 

		if (convenio.getCuenta() != null){
			cuenta = convenio.getCuenta();
			liqCuentaVO = liqDeudaBeanHelper.getCuenta();
		} else {
			procedimiento = convenio.getProcedimiento();
			procedimientoVO = procedimiento.toVOWithPersona();
		}		
		
		List<ConvenioCuota> listConvenioCuota = null;
		
		if (listIdsCuotasSelected != null){
			listConvenioCuota = (List<ConvenioCuota>) ListUtilBean.getListBeanByListId(convenio.getListConvenioCuota(), listIdsCuotasSelected);
		} else {
			listConvenioCuota = convenio.getListConvenioCuota();
		}
		
		// Creo y agrego a la lista un LiqReciboVO por cada cuota.
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			
			LiqReciboVO reciboVO = new LiqReciboVO();
			//reciboVO.setId(recibo.getId());
			//reciboVO.setAnioRecibo(recibo.getAnioRecibo());
			
			reciboVO.setCodRefPag(String.valueOf(convenioCuota.getCodRefPag()));
			reciboVO.setConvenio(liqConvenioVO);

			// Para convenios comunes
			if (cuenta != null){
				
				reciboVO.setCuenta(liqCuentaVO);
				
				reciboVO.setPropietario(cuenta.getNombreTitularPrincipal());
				
				reciboVO.getRecurso().setCodRecurso(convenio.getRecurso().getCodRecurso());
				
				if (convenio.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP) || 
						convenio.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG)){
					reciboVO.getRecurso().setCodRecurso("CdM");
				}
			
			// Para convenios de Cyq	
			} else {
				
				reciboVO.setProcedimiento(procedimientoVO);
				
			}
			
			//reciboVO.setDesActualizacion(recibo.getDesActualizacion());
			//reciboVO.setDesCapitalOrigina(recibo.getDesCapitalOriginal());
			//reciboVO.setSellado((recibo.getSellado()!=null?recibo.getSellado().getImporteSellado():0));
			
	
			log.debug("## getReciboVO() - transfiere getNroCodigoBarra()"); 
			reciboVO.setNroCodigoBarra(convenioCuota.getNroCodigoBarra());
			reciboVO.setNroCodigoBarraDelim(convenioCuota.getNroCodigoBarraDelim());
			
			log.debug("## reciboVO.getNroCodigoBarra()="+reciboVO.getNroCodigoBarra());
			log.debug("## reciboVO.getCodigoBarraDelim()="+reciboVO.getNroCodigoBarraDelim());
			
			//reciboVO.setFechaGeneracion(recibo.getFechaGeneracion());
			
			reciboVO.setFechaVto(convenioCuota.getFechaVencimiento());
			reciboVO.setNumeroRecibo(new Long("000000000"));
			
			// No hay actualizacion porque es impresion directa y no reconfeccion.
			reciboVO.setTotActualizacion(0D);
			Double importeSellado = 0D;
			if (convenioCuota.getNumeroCuota()==1 && convenioCuota.getSellado()!=null){
				importeSellado = convenioCuota.getImporteSellado();
				reciboVO.setImporteSellado(importeSellado);
				reciboVO.setSellado(importeSellado);
			}
						
			//Seteo de la lista de LiCuotaVO
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			
			liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
			liqCuotaVO.setInteres(convenioCuota.getInteres());
			
			liqCuotaVO.setImporteCuota(convenioCuota.getImporteCuota()-importeSellado);
			liqCuotaVO.setRecargo(0d);
			liqCuotaVO.setTotal(convenioCuota.getImporteCuota()-importeSellado);
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			
			reciboVO.getListCuota().add(liqCuotaVO);
			
			
			reciboVO.setTotalPagar(convenioCuota.getImporteCuota());
			reciboVO.setTotCapitalOriginal(convenioCuota.getImporteCuota());
			
			listReciboVO.add(reciboVO);

			// La cantidad de recibos a imprimir esta definida en el plan.
			// que por ahora no se valida
			// if (i == convenio.getPlan().getCanCuoAImpEnForm().intValue())
			//	break;
					
		}
		
		return listReciboVO;
		
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
		liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
		
		Double capitalConDesc = new Double(deuda.getSaldo() - deuda.getSaldo() * descuentoCapital);
		capitalConDesc = NumberUtil.round(capitalConDesc, SiatParam.DEC_IMPORTE_CALC);
		
		
		boolean exentaAct = deuda.getCuenta().exentaActualizacion(fecha, deuda.getFechaVencimiento());
		log.debug(" ### getLiqDeudaVOActualizado: " + exentaAct);
				
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
}
