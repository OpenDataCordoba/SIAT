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

import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReconfeccionAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipDecJurRecVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;


public class LiqReconfeccionBeanHelper {
	
	private static Log log = LogFactory.getLog(LiqReconfeccionBeanHelper.class);
	
	private Cuenta cuenta;
	
	private Convenio convenio;
	
	private Procedimiento procedimiento;

	public static final Long CANT_DIAS_HABILES_SIGTES = 5L;
	
	public LiqReconfeccionBeanHelper(Cuenta cuenta) {
		super();
		this.cuenta = cuenta;
	}
	public LiqReconfeccionBeanHelper (Convenio convenio){
		super();
		this.convenio=convenio;
		this.cuenta = convenio.getCuenta();
		this.procedimiento = convenio.getProcedimiento();
	}
	
	public LiqReconfeccionAdapter getReconfeccionCuotas(Long[] idCuotas)throws Exception{
		
		LiqReconfeccionAdapter liqReconfeccionAdapter = new LiqReconfeccionAdapter();
		
		// Para Convenios comunes
		if (cuenta != null){
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 		
			LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta();
			liqReconfeccionAdapter.setCuenta(liqCuentaVO);
			
		// Para Convenios de Cyq	
		} else {
			liqReconfeccionAdapter.setProcedimiento(procedimiento.toVOWithPersona());
		}
		
		if (cuenta != null){
			if(convenio.getEstadoConvenio().getId().longValue()!= EstadoConvenio.ID_VIGENTE || 
				convenio.estaCaduco(convenio.getRecurso().getFecUltPag())){
				liqReconfeccionAdapter.addRecoverableError(GdeError.CONVENIO_RECONFECCION_ESTADO);
				return liqReconfeccionAdapter;
			}	
		} else {
			if(convenio.getEstadoConvenio().getId().longValue()!= EstadoConvenio.ID_VIGENTE || 
					convenio.estaCaduco(new Date())){
				liqReconfeccionAdapter.addRecoverableError(GdeError.CONVENIO_RECONFECCION_ESTADO);
				return liqReconfeccionAdapter;
			}	
		}
		
		List<LiqCuotaVO> listCuotaVO = new ArrayList<LiqCuotaVO>();
		List<ConvenioCuota> listConvenioCuota = new ArrayList<ConvenioCuota>();
		liqReconfeccionAdapter.setTieneDeudaVencida(false);
		liqReconfeccionAdapter.setEsReimpresionCuotas(true);
		
		if (idCuotas.length>0){
			listConvenioCuota = (ArrayList<ConvenioCuota>) ListUtilBean.getListBeanByListId(convenio.getListConvenioCuota(), idCuotas);
			for (ConvenioCuota convenioCuota:listConvenioCuota){
				LiqCuotaVO cuotaVO = new LiqCuotaVO();
				if (convenioCuota.getNumeroCuota()==1 && convenioCuota.getSellado()!= null){
					cuotaVO.setImporteSellado(convenioCuota.getImporteSellado());
				}
				cuotaVO.setIdCuota(convenioCuota.getId());
				cuotaVO.setCapital(convenioCuota.getCapitalCuota());
				cuotaVO.setInteres(convenioCuota.getInteres());
				cuotaVO.setImporte(convenioCuota.getImporteCuotaSinSellado());
				cuotaVO.setNroCuota(convenioCuota.getNumeroCuota().toString());
				cuotaVO.setRecargo(0D);
				if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), new Date())){
					if (this.convenio.getPlan().getEsManual()!=1 && convenioCuota.getNumeroCuota().intValue()<this.convenio.getPlan().getCuoDesParaRec()){
						liqReconfeccionAdapter.addRecoverableError(GdeError.CONVENIO_CUOTADESDE_RECONFECCION);
					}
					cuotaVO.setRecargo(convenioCuota.actualizacionImporteCuota().getRecargo());
					liqReconfeccionAdapter.setTieneDeudaVencida(true);
				}
				cuotaVO.setTotal(cuotaVO.getImporte()+cuotaVO.getRecargo());
				cuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK));
				listCuotaVO.add(cuotaVO);
			}
			
			liqReconfeccionAdapter.setListCuotas(listCuotaVO);
			List<Date> listFechas=Feriado.getProximosDiasForReconfeccion(CANT_DIAS_HABILES_SIGTES);
			for (Date fecha : listFechas){
				if (!liqReconfeccionAdapter.isVerMensajeCaducidad() && convenio.estaCaduco(fecha)){
					liqReconfeccionAdapter.setVerMensajeCaducidad(true);
					liqReconfeccionAdapter.setMensajeCaducidad("De no registrarse pagos posteriores a la fecha de \u00FAltimo asentamiento, el convenio podría registrar caducidad a partir de la fecha "
								+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
				}
				liqReconfeccionAdapter.getListFechasReconf().add(fecha);
			} 
			//se vuelve atras esta validacion el 27/10/08
			//liqReconfeccionAdapter.setListFechasReconf(Feriado.getProximosDiasForReconfeccion(CANT_DIAS_HABILES_SIGTES));
			LiqConvenioVO liqConvenioVO = new LiqConvenioVO();
			liqConvenioVO.setIdConvenio(this.convenio.getId());
			liqConvenioVO.setNroConvenio(this.convenio.getNroConvenio().toString());
			liqConvenioVO.setDesPlan(this.convenio.getPlan().getDesPlan());
			liqConvenioVO.setCanCuotasPlan(this.convenio.getCantidadCuotasPlan().toString());
			liqConvenioVO.setCantidadCuotasPlan(this.convenio.getCantidadCuotasPlan());
			liqReconfeccionAdapter.setConvenio(liqConvenioVO);
		}else{
			liqReconfeccionAdapter.addRecoverableError("Debe seleccionar al menos una cuota a reimprimir.");
		}
			return liqReconfeccionAdapter;
		
	}
	
	/**
	 * 
	 * Este metodo obtiene un LiqReconfeccionAdapter preparado para reconfeccionar
	 * Verifica entre la lista de deuda si hay deuda vencida o no y setea la bandera que corresponda
	 * para determinar si hay que pedir la fecha de vencimiento o no.
	 * 
	 * idDeudaEstadoDeuda lista de string con la forma "idcuenta-idEstado"
	 * @throws Exception 
	 */
	public LiqReconfeccionAdapter getReconfeccion(String[] idDeudaEstadoDeuda) throws Exception{
		LiqReconfeccionAdapter liqReconfeccionAdapter = new LiqReconfeccionAdapter();
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
		LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.OBJIMP | LiqDeudaBeanHelper.ATR_CUE_4_RECIBO | LiqDeudaBeanHelper.TITULARES);
		liqReconfeccionAdapter.setCuenta(liqCuentaVO);
		
		liqReconfeccionAdapter.setTieneDeudaVencida(false); // inicializa en false
		
		if(idDeudaEstadoDeuda!=null && idDeudaEstadoDeuda.length>0){
			for(String idDeudaEstado: idDeudaEstadoDeuda){
				String[] split = idDeudaEstado.split("-");
				long idDeuda = Long.parseLong(split[0]);
				long idEstadoDeuda = Long.parseLong(split[1]);
				Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
				
				
				if (deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.SI.getId().intValue()
						&& (deuda.getSaldo()==0 || deuda.getConvenio()!=null) && !liqReconfeccionAdapter.isRecEnBlanco()){
					liqReconfeccionAdapter.setRecEnBlanco(true);
				}
				// verifica si la deuda esta vencida y en caso afirmativo, setea la bandera correspondinete en el adater
				if (deuda.estaVencida())
					liqReconfeccionAdapter.setTieneDeudaVencida(true);
				
				
				// toVO
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO();
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdEstadoDeuda(idEstadoDeuda);
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				liqDeudaVO.setSaldo(deuda.getSaldo());
				
				//Modificacion para soportar la reimpresión en blanco de periodos en convenio de autoliquidables 
				if (deuda.getConvenio()!=null){
					liqDeudaVO.setPoseeConvenio(true);
					liqDeudaVO.setSaldo(0D);
					liqDeudaVO.setActualizacion(0D);
					liqDeudaVO.setTotal(0D);
				}else{
					DeudaAct deudaAct = deuda.actualizacionSaldo();
					liqDeudaVO.setActualizacion(deudaAct.getRecargo());
					liqDeudaVO.setTotal(deudaAct.getImporteAct());
				}
				
				if (deuda.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue() && 
						liqDeudaVO.getTotal().doubleValue()==0D && deuda.estaVencida()){
					liqDeudaVO.setActualizacion(ActualizaDeuda.actualizar(new Date(), deuda.getFechaVencimiento(), 
							1D, false, true).getRecargo()+1);
				}
				
				liqReconfeccionAdapter.getListDeuda().add(liqDeudaVO);
				//liqReconfeccionAdapter.setListFechasReconf(Feriado.getProximosDiasHabiles(CANT_DIAS_HABILES_SIGTES));

			}
			
			if (liqReconfeccionAdapter.isRecEnBlanco()){
				List<TipDecJurRec> listTipDecJurRec = TipDecJurRec.getListVigenteByRecurso(new Date(),Cuenta.getById(liqCuentaVO.getIdCuenta()).getRecurso());
				liqReconfeccionAdapter.setListTipDecJurRec(ListUtilBean.toVO(listTipDecJurRec,1, new TipDecJurRecVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			liqReconfeccionAdapter.setListFechasReconf(Feriado.getProximosDiasForReconfeccion(CANT_DIAS_HABILES_SIGTES));
			// Seteo de Permisos
		/*	liqReconfeccionAdapter.setVerDeudaContribEnabled(liqDeudaBeanHelper.getPermiso(GdeSecurityConstants.MTD_VER_DEUDA_CONTRIB, null));
			liqReconfeccionAdapter.setVerConvenioEnabled(liqDeudaBeanHelper.getPermiso(GdeSecurityConstants.MTD_VER_CONVENIO, null));
			liqReconfeccionAdapter.setVerCuentaEnabled(liqDeudaBeanHelper.getPermiso(GdeSecurityConstants.MTD_VER_CUENTA, null));
			liqReconfeccionAdapter.setVerCuentaDesgUnifEnabled(liqDeudaBeanHelper.getPermiso(GdeSecurityConstants.MTD_VER_CUENTA_DESG_UNIF, null));
			liqReconfeccionAdapter.setVerCuentaRelEnabled(liqDeudaBeanHelper.getPermiso(GdeSecurityConstants.MTD_VER_CUENTA_REL, null));
			
			liqReconfeccionAdapter.setListCuentaRel(liqDeudaBeanHelper.getCuentasRelacionadas());
			liqReconfeccionAdapter.setExenciones(liqDeudaBeanHelper.getExenciones());*/
		}else{
			liqReconfeccionAdapter.addRecoverableError(GdeError.LIQRECONF_NO_HAY_DEUDAS_SELECCIONADAS);
		}
		return liqReconfeccionAdapter;
	}

	public static LiqReconfeccionAdapter reconfeccionar(LiqReconfeccionAdapter liqReconfeccionAdapter, Canal canal, String usuario) throws Exception {
		//Verifico si la reconfeccion es de Cuotas o de Deuda
		if (!liqReconfeccionAdapter.getEsReimpresionCuotas()){
		
			List<LiqDeudaVO> listLiqDeudaVO = liqReconfeccionAdapter.getListDeuda();
			// Se obtiene la cuenta
			Cuenta cuenta = Cuenta.getById(liqReconfeccionAdapter.getCuenta().getIdCuenta());
			
			// Pasamos el filtro que viene de la liquidacion de deuda.
			cuenta.setLiqCuentaFilter(liqReconfeccionAdapter.getCuentaFilter());
			
			for (CuentaTitular ct: cuenta.getListCuentaTitularVigentes(new Date())){
				if (ct.getEsTitularPrincipal().intValue()== SiNo.SI.getId().intValue()){
					Contribuyente cont = ct.getContribuyente();
					cont.loadPersonaFromMCR();
					Persona persona = cont.getPersona();
					liqReconfeccionAdapter.getCuenta().setCuitTitularPrincipalContr(persona.getCuitContr());
				}
			}
			
			// Esta variable depende del servicio banco de la deuda.
			Integer cantMaxDeudaPorRecibo = null;
			
			// se pasan listLiqDeudaVO a listDeuda
			List<Deuda> listDeuda = new ArrayList<Deuda>();
			for(LiqDeudaVO liqDeudaVO: listLiqDeudaVO) {
				Deuda deuda = Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda());

				// Seteamos el valor de la cantidad de deuda por recibo segun el servicio banco del primer registro de deuda.
				if (cantMaxDeudaPorRecibo == null){
					
					if (deuda.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI))  {						
						cantMaxDeudaPorRecibo = Cuenta.MAX_CANT_DEUDA_X_RECIBO_RECONFECCION_DREI;
					} else if (ServicioBanco.COD_OTROS_TRIBUTOS.equals(deuda.getSistema().getServicioBanco().getCodServicioBanco())){
						cantMaxDeudaPorRecibo = Cuenta.MAX_CANT_DEUDA_X_RECIBO_RECONFECCION_GRE; 
					} else {
						// Tgi, Drei, Etur, Cdm, etc.
						cantMaxDeudaPorRecibo = Cuenta.MAX_CANT_DEUDA_X_RECIBO_RECONFECCION;
					}
				}
				
				//Si es una reconfeccion especial ignorar la deuda no vencida.
				if (liqReconfeccionAdapter.isEsReconfeccionEspecial() && !deuda.estaVencida()) {
					continue;
				}
				
				listDeuda.add(deuda);
			}
			
			// Se realiza la reconfeccion
			Procurador procurador = null;
			UserContext userContext = DemodaUtil.currentUserContext();
			if(userContext.getEsProcurador()){
				procurador = Procurador.getById(userContext.getIdProcurador());
				log.debug("Busco en procurador:"+procurador.getId()+"   "+procurador.getDescripcion());
			}
			
			Long idBroche = cuenta.getBroche()!=null?cuenta.getBroche().getId():null;
			
			/* Definimos que fecha de vencimiento y actualizacion usamos
			 * para las reconfeccion especial se usan dos fechas distintas ingresadas ambas por pantalla
			 * para las reconfeccion cotidiana, se usa la fechaReconf ofrecida en un combo, y ambas actualizacion y vencimiento tiene esa fecha.*/
			Date fechaVencimiento = null;
			Date fechaActualizacion = null;
			boolean contemplaSellado = true;
			if (liqReconfeccionAdapter.isEsReconfeccionEspecial()) {
				// especial
				fechaVencimiento = liqReconfeccionAdapter.getFechaVencimientoEsp();
				fechaActualizacion = liqReconfeccionAdapter.getFechaActualizacionEsp();
				contemplaSellado = false;
			} else {
				// comun
				Date fechaReconfSelecetd = DateUtil.getDate(liqReconfeccionAdapter.getFechaReconfSelected());
				fechaVencimiento = fechaReconfSelecetd;
				fechaActualizacion = fechaReconfSelecetd;
				contemplaSellado = true;
			}
			
			boolean esRectificativa=false;
			if (liqReconfeccionAdapter.isRecEnBlanco()){
				TipDecJurRec tipDecJurRec = TipDecJurRec.getById(liqReconfeccionAdapter.getDecJur().getTipDecJurRec().getId());
				if (tipDecJurRec.getTipDecJur().getId().longValue()==TipDecJur.ID_RECTIFICATIVA)
					esRectificativa=true;
			}
			
			List<Recibo> listRecibo = cuenta.reconfeccionar(listDeuda, fechaVencimiento, fechaActualizacion, 
													canal, procurador, null, contemplaSellado, idBroche, cantMaxDeudaPorRecibo, esRectificativa);

			//si es una reconfeccione especial, seteamos el campo observacion.
			if (liqReconfeccionAdapter.isEsReconfeccionEspecial()) {
				for(Recibo recibo: listRecibo) {
					recibo.setObservacion(liqReconfeccionAdapter.reconfEspInfoString(false));
				}
			}
			// Se pasa la lista de recibos a reciboVO
			List<LiqReciboVO> listLiqReciboVO = new ArrayList<LiqReciboVO>();
			for(Recibo recibo: listRecibo){
				/*
				if (recibo.getRecurso().getEsAutoliquidable().intValue()== SiNo.SI.getId().intValue() && (recibo.getTotCapitalOriginal()==0 
						|| recibo.getTotImporteRecibo().doubleValue()==0D)){
					ReciboDeuda	reciboDeuda = recibo.getListReciboDeuda().get(0);
					DeudaAct dAct = ActualizaDeuda.actualizar(recibo.getFechaVencimiento(), reciboDeuda.getDeuda().getFechaVencimiento(), 1D, false,true);
					reciboDeuda.setActualizacion(dAct.getImporteAct());
					log.debug("------------------Actualización: "+reciboDeuda.getActualizacion());
						
				}
				*/
				LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqReconfeccionAdapter.getCuenta());
				
				// Ademas pasamos los datos de la reconfeccionespecial que no estan en LiqReciboVO
				
				liqReciboVO.setFechaActualizacionEspView(liqReconfeccionAdapter.getFechaActualizacionEspView());
				liqReciboVO.setEsReconfeccionEspecial(liqReconfeccionAdapter.isEsReconfeccionEspecial());
				if (liqReconfeccionAdapter.getCasoContainer().getCaso() != null) {
					liqReciboVO.setCasoView(liqReconfeccionAdapter.getCasoContainer().getCaso().getCasoView());
				}
				
				listLiqReciboVO.add(liqReciboVO);
			}
			
			liqReconfeccionAdapter.setListRecibos(listLiqReciboVO);		
		} else {
			List<LiqCuotaVO> listCuotaVO = liqReconfeccionAdapter.getListCuotas();
			Convenio convenio = Convenio.getById(liqReconfeccionAdapter.getConvenio().getIdConvenio());
			List<Long> listIdCuota = new ArrayList<Long>();
			for (LiqCuotaVO cuotaVO : listCuotaVO){
				listIdCuota.add(cuotaVO.getIdCuota().longValue());
			}
			List<ReciboConvenio> listReciboConvenio;
			Procurador procurador = null;
			UserContext userContext = DemodaUtil.currentUserContext();
			if(userContext.getEsProcurador()){
				procurador = Procurador.getById(userContext.getIdProcurador());
				log.debug("Busco en procurador:"+procurador.getId()+"   "+procurador.getDescripcion());
			}
			String cuotaDesde="";
			if (!liqReconfeccionAdapter.getEsCuotaSaldo()){
				listReciboConvenio = convenio.reconfeccionar(listIdCuota, DateUtil.getDate(liqReconfeccionAdapter.getFechaReconfSelected()), canal, procurador);
			}else{
				List<ConvenioCuota>listaCuotas = GdeDAOFactory.getConvenioCuotaDAO().getListByNroCuotaMayorIgualA(convenio, liqReconfeccionAdapter.getCuotaDesCuoSal());
				for (ConvenioCuota convenioCuota : listaCuotas){
					if (convenioCuota.getEsIndeterminada()){
						liqReconfeccionAdapter.addRecoverableError(GdeError.CONVENIO_CUOTASALDOCONINDETERMINADA);
						return liqReconfeccionAdapter;
					}
				}
				listReciboConvenio = convenio.getCuotaSaldo(listaCuotas, canal, usuario);
				cuotaDesde = listReciboConvenio.get(0).getListRecConCuo().get(0).getConvenioCuota().getNumeroCuota().toString();
			}
			
			log.debug("FECHA VENCIMIENTO "+listReciboConvenio.get(0).getFechaVencimiento());
			// Se pasa la lista de recibosConvenio a reciboVO
			List<LiqReciboVO> listLiqReciboVO = new ArrayList<LiqReciboVO>();
			for (ReciboConvenio reciboConvenio:listReciboConvenio){
				
				LiqReciboVO reciboVO;

				if (liqReconfeccionAdapter.getCuenta() != null){
					reciboVO = ReciboBeanHelper.getReciboVO(reciboConvenio, liqReconfeccionAdapter.getConvenio(), liqReconfeccionAdapter.getCuenta());					
				} else {
					reciboVO = ReciboBeanHelper.getReciboVO(reciboConvenio, liqReconfeccionAdapter.getConvenio(), liqReconfeccionAdapter.getProcedimiento());
				}
				
				reciboVO.setEsCuotaSaldo(liqReconfeccionAdapter.getEsCuotaSaldo());
				reciboVO.setCuotaDesde(cuotaDesde);
				listLiqReciboVO.add(reciboVO);
			}
			liqReconfeccionAdapter.setListRecibos(listLiqReciboVO);
		}
		
		return liqReconfeccionAdapter;
	}
	
	/* *
	 * Esta funcion pasa de Recibo a RecivoVO que es lo que finalmente se imprime
	 * 
	 * @param recibo
	 * @param cuenta
	 * @return
	 * @throws Exception
	 * /
	public static LiqReciboVO getReciboVO(Recibo recibo, LiqCuentaVO cuenta) throws Exception{
		LiqReciboVO reciboVO = new LiqReciboVO();
		reciboVO.setId(recibo.getId());
		
		// Seteamos la descripcion de la categoria del recuso
		reciboVO.getRecurso().setDesRecurso(recibo.getRecurso().getCategoria().getDesCategoria());
		
		// TODO: Obtener el codigo de recurso del "codigo" de la categoria del mismo.
		if (recibo.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP) || 
				recibo.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG)){
			reciboVO.getRecurso().setCodRecurso("CdM");
		} else {
			reciboVO.getRecurso().setCodRecurso(recibo.getRecurso().getCodRecurso());
		}
		
		// Seteo el procurador
		reciboVO.getProcurador().setDescripcion(recibo.getProcurador()!=null?recibo.getProcurador().getDescripcion():"");
		
		reciboVO.setAnioRecibo(recibo.getAnioRecibo());
		reciboVO.setCodRefPag(String.valueOf(recibo.getCodRefPag()));
		reciboVO.setCuenta(cuenta);
		reciboVO.setDesActualizacion(recibo.getDesActualizacion());
		reciboVO.setDesCapitalOrigina(recibo.getDesCapitalOriginal());
		reciboVO.setSellado((recibo.getSellado()!=null?recibo.getSellado().getImporteSellado():0));
		reciboVO.setPropietario(recibo.getCuenta().getDesTitularPrincipal());

		log.debug("## getReciboVO() - transfiere getNroCodigoBarra()"); 
		reciboVO.setNroCodigoBarra(recibo.getNroCodigoBarra());
		reciboVO.setNroCodigoBarraDelim(recibo.getNroCodigoBarraDelim());
		
		log.debug("## reciboVO.getNroCodigoBarra()="+reciboVO.getNroCodigoBarra());
		log.debug("## reciboVO.getCodigoBarraDelim()="+reciboVO.getNroCodigoBarraDelim());
		
		// se setea el descuento aplicado, puede ser que sea nulo si todavï¿½a no venciï¿½ la deuda
		if(recibo.getDesEsp()!=null){
			reciboVO.setDesDescuento(recibo.getDesEsp().getLeyendaDesEsp());
			reciboVO.setIdDescuento(recibo.getDesEsp().getId());
		}else if(recibo.getDesGen()!=null){
			reciboVO.setDesDescuento(recibo.getDesGen().getLeyendaDesGen());
			reciboVO.setIdDescuento(recibo.getDesGen().getId());
		}
		reciboVO.setFechaGeneracion(recibo.getFechaGeneracion());
		reciboVO.setFechaVto(recibo.getFechaVencimiento());
		reciboVO.setNumeroRecibo(recibo.getNroRecibo());
		reciboVO.setTotActualizacion(recibo.getTotActualizacion());
		
		// Seteo de la lista de cabecerea de conceptos(Tasa, sobretasa, C.A.P., adicionales, etc)
		for(RecCon recCon: recibo.getCuenta().getRecurso().getListRecConPorOrdenVisualizacion()){
			RecConVO recConVO = new RecConVO();
			recConVO.setId(recCon.getId());
			recConVO.setCodRecCon(recCon.getCodRecCon());			
			recConVO.setAbrRecCon(recCon.getAbrRecCon());
			recConVO.setDesRecCon(recCon.getDesRecCon().toUpperCase());
			recConVO.setOrdenVisualizacion(recCon.getOrdenVisualizacion());
			reciboVO.getListRecCon().add(recConVO);
		}		
		
		//Seteo de la lista de reciboDeuda
		for(ReciboDeuda reciboDeuda: recibo.getListReciboDeuda()){
			LiqReciboDeudaVO reciboDeudaVO = new LiqReciboDeudaVO();
			reciboDeudaVO.setCapitalOriginal(reciboDeuda.getCapitalOriginal());
			reciboDeudaVO.setTotActualizacion(reciboDeuda.getTotActualizacion());
			reciboDeudaVO.setTotCapital(reciboDeuda.getTotCapital());
			reciboDeudaVO.setPeriodoDeuda(reciboDeuda.getPeriodoDeuda());
			reciboDeudaVO.setTotalReciboDeuda(reciboDeuda.getTotalReciboDeuda());
			reciboDeudaVO.setConceptosDeuda(reciboDeuda.getConceptos());			
			reciboVO.setTotal(reciboVO.getTotal()+reciboDeudaVO.getTotalReciboDeuda());
			
			// seteo de los valores de los conceptos en el orden correcto.
			 LiqConceptoDeudaVO[] conceptos = new LiqConceptoDeudaVO[reciboVO.getListRecCon().size()];
			 List<DeuAdmRecCon> listDeuAdmRecCon = reciboDeuda.getListDeuAdmRecCon();
			 for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
				 int nroOrden = getOrdenVisualizacion(reciboVO.getListRecCon(), deuAdmRecCon.getRecCon().getId());
				 LiqConceptoDeudaVO concepto = new LiqConceptoDeudaVO();
				 concepto.setDesConcepto(deuAdmRecCon.getRecCon().getDesRecCon());
				 concepto.setImporte(deuAdmRecCon.getSaldo());
				 conceptos[nroOrden] = concepto;				 
			 }	
			 log.debug("LiqReconfeccionBeanHelper.getReciboVO() conceptos del reciboDeuda");
			 for(int i=0;i<conceptos.length;i++){
				 if(conceptos[i]==null)
					 conceptos[i] = new LiqConceptoDeudaVO();
				 reciboDeudaVO.getListConceptosDeuda().add(conceptos[i]);
				 log.debug(conceptos[i].getDesConcepto()+":"+conceptos[i].getImporte());				 
			 }			 
			 reciboVO.getListReciboDeuda().add(reciboDeudaVO);			 
		}
		
		// Se sacan las columnas que tengan todos los valores en 0
		reciboVO.eliminarColumnasConceptoEnCero();	
		 
		//Seteo de la leyenda del descuento
		if(recibo.getDesEsp()!=null){
			reciboVO.setDesDescuento(recibo.getDesEsp().getLeyendaDesEsp());
		}else if(recibo.getDesGen()!=null){
			reciboVO.setDesDescuento(recibo.getDesGen().getLeyendaDesGen());
		}
		
		reciboVO.setTotalPagar(recibo.getTotImporteRecibo());
		reciboVO.setTotCapitalOriginal(recibo.getTotCapitalOriginal());

		log.debug("toVO especifico para Recibos de Contribucion de Mejoras");
		if (recibo.getDatosReciboCdM() != null)
			reciboVO.setDatosReReciboCdMVO((DatosReciboCdMVO) recibo.getDatosReciboCdM().toVO());  
		
		return reciboVO;
	}
	
	/**
	 * Esta funcion pasa de ReciboConvenio a RecivoVO que es lo que finalmente se imprime
	 * 
	 * @param recibo
	 * @param cuenta
	 * @return
	 * @throws Exception
	 * /
	public static LiqReciboVO getReciboVO(ReciboConvenio reciboConvenio, LiqConvenioVO convenio, LiqCuentaVO cuenta) throws Exception{
		LiqReciboVO reciboVO = new LiqReciboVO();
		reciboVO.setId(reciboConvenio.getId());
		reciboVO.setAnioRecibo(reciboConvenio.getAnioRecibo().intValue());
		reciboVO.setCodRefPag(String.valueOf(reciboConvenio.getCodRefPag()));
		reciboVO.setCuenta(cuenta);
		reciboVO.setConvenio(convenio);

		reciboVO.setSellado((reciboConvenio.getSellado()!=null?reciboConvenio.getSellado().getImporteSellado():0));
		reciboVO.setPropietario(reciboConvenio.getConvenio().getCuenta().getDesTitularPrincipal());

		log.debug("## getReciboVO() - transfiere getNroCodigoBarra()"); 
		reciboVO.setNroCodigoBarra(reciboConvenio.getNroCodigoBarra());
		reciboVO.setNroCodigoBarraDelim(reciboConvenio.getNroCodigoBarraDelim());
		
		log.debug("## reciboVO.getNroCodigoBarra()="+reciboVO.getNroCodigoBarra());
		log.debug("## reciboVO.getCodigoBarraDelim()="+reciboVO.getNroCodigoBarraDelim());
		
		// se setea el descuento aplicado, puede ser que sea nulo si todavï¿½a no venciï¿½ la deuda
		if(reciboConvenio.getDesEsp()!=null){
			reciboVO.setDesDescuento(reciboConvenio.getDesEsp().getLeyendaDesEsp());
			reciboVO.setIdDescuento(reciboConvenio.getDesEsp().getId());
		}else if(reciboConvenio.getDesGen()!=null){
			reciboVO.setDesDescuento(reciboConvenio.getDesGen().getLeyendaDesGen());
			reciboVO.setIdDescuento(reciboConvenio.getDesGen().getId());
		}
		reciboVO.setFechaGeneracion(reciboConvenio.getFechaGeneracion());
		reciboVO.setFechaVto(reciboConvenio.getFechaVencimiento());
		reciboVO.setNumeroRecibo(reciboConvenio.getNroRecibo());

		
		// Seteo de la lista de cabecerea de conceptos(Tasa, sobretasa, C.A.P., adicionales, etc)
		for(RecCon recCon: reciboConvenio.getConvenio().getCuenta().getRecurso().getListRecConPorOrdenVisualizacion()){
			RecConVO recConVO = new RecConVO();
			recConVO.setId(recCon.getId());
			recConVO.setCodRecCon(recCon.getCodRecCon());			
			recConVO.setAbrRecCon(recCon.getAbrRecCon());
			recConVO.setDesRecCon(recCon.getDesRecCon().toUpperCase());
			recConVO.setOrdenVisualizacion(recCon.getOrdenVisualizacion());
			reciboVO.getListRecCon().add(recConVO);
		}		
		
		//Seteo de la lista de reciboDeuda
		Double totCapital=0D;
		Double totAct =0D;
		Double totInteres=0D;
		Double desCapital =0D;
		Double desAct =0D;
		Double desInt = 0D;
		
		for(RecConCuo reciboConCuo: reciboConvenio.getListRecConCuo()){
			//Seteo de la lista de LiCuotaVO
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			ConvenioCuota convenioCuota=reciboConCuo.getConvenioCuota();
			liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			liqCuotaVO.setCapital(reciboConCuo.getTotCapitalOriginal());
			liqCuotaVO.setInteres(reciboConCuo.getTotIntFin());
			
			liqCuotaVO.setImporteCuota(liqCuotaVO.getCapital()+liqCuotaVO.getInteres());
			liqCuotaVO.setRecargo(reciboConCuo.getTotActualizacion());
			liqCuotaVO.setTotal(liqCuotaVO.getImporteCuota()+reciboConCuo.getTotActualizacion());
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			
			reciboVO.getListCuota().add(liqCuotaVO);
			
			
			reciboVO.setTotalPagar(convenioCuota.getImporteCuota());
			reciboVO.setTotCapitalOriginal(convenioCuota.getImporteCuota());
			totCapital += liqCuotaVO.getCapital();
			totAct += liqCuotaVO.getRecargo();
			totInteres += liqCuotaVO.getInteres();
			desCapital += reciboConCuo.getDesCapitalOriginal();
			desAct += reciboConCuo.getDesActualizacion();
			desInt += reciboConCuo.getDesIntFin();

		}
		reciboVO.setDesActualizacion(desAct);
		reciboVO.setDesInteres(desInt);
		reciboVO.setDesCapitalOrigina(desCapital);
		reciboVO.setTotActualizacion(totAct);
		reciboVO.setTotCapitalOriginal(totCapital);
		reciboVO.setTotInteres(totInteres);
		reciboVO.setTotal(reciboConvenio.getTotImporteRecibo());
		
		// Se sacan las columnas que tengan todos los valores en 0
		reciboVO.eliminarColumnasConceptoEnCero();	
		 
		//Seteo de la leyenda del descuento
		if(reciboConvenio.getDesEsp()!=null){
			reciboVO.setDesDescuento(reciboConvenio.getDesEsp().getLeyendaDesEsp());
		}else if(reciboConvenio.getDesGen()!=null){
			reciboVO.setDesDescuento(reciboConvenio.getDesGen().getLeyendaDesGen());
		}
		
		reciboVO.setTotalPagar(reciboConvenio.getTotImporteRecibo());

		return reciboVO;
	}
	
	/**
	 * Devuelve el orden de visualizacion del recCon que llega como parametro en la lista
	 * <br>Se corresponde con el lugar que ocupa el recCon en listRecCon.
	 * <br>Es para que los reciboDeuda tenga cargados los conceptos en el mismo orden que el recibo, para que aparezcan en las columnas correspondientes
	 * @param recCon
	 * @return
	 * /
	private static int getOrdenVisualizacion(List<RecConVO> listRecCon, Long  idRecCon) {
		int nroOrden = 0;
		if(listRecCon!=null){
			for(RecConVO recCon: listRecCon){
				if(recCon.getId()==idRecCon)
					return nroOrden;
				nroOrden++;
			}
		}		
		return 0;
	}*/
	
	/**
	 * 
	 * Este metodo obtiene un LiqReconfeccionAdapter preparado para la generacion del volante de pago de intereses de RS
	 * 
	 * idDeudaEstadoDeuda lista de string con la forma "idcuenta-idEstado"
	 * @throws Exception 
	 */
	public LiqReconfeccionAdapter getVolantePagoIntRS(String[] idDeudaEstadoDeuda) throws Exception{
		LiqReconfeccionAdapter liqReconfeccionAdapter = new LiqReconfeccionAdapter();
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
		LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.OBJIMP | LiqDeudaBeanHelper.ATR_CUE_4_RECIBO | LiqDeudaBeanHelper.TITULARES);
		liqReconfeccionAdapter.setCuenta(liqCuentaVO);
		
		liqReconfeccionAdapter.setTieneDeudaVencida(false);
		
		if(idDeudaEstadoDeuda!=null && idDeudaEstadoDeuda.length>0){
			if(idDeudaEstadoDeuda.length != 1){
				liqReconfeccionAdapter.addRecoverableError(GdeError.LIQVOLANTEPAGOINTRS_MAS_DE_UNA_DEUDAS_SELECCIONADA);
				return liqReconfeccionAdapter;
			}
			
			for(String idDeudaEstado: idDeudaEstadoDeuda){
				String[] split = idDeudaEstado.split("-");
				long idDeuda = Long.parseLong(split[0]);
				long idEstadoDeuda = Long.parseLong(split[1]);
				Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
				
				if(deuda.getViaDeuda() != null && deuda.getViaDeuda().getId().longValue() != ViaDeuda.ID_VIA_ADMIN){
					liqReconfeccionAdapter.addRecoverableError(GdeError.LIQVOLANTEPAGOINTRS_DEUDAS_NO_PERMITIDAS);
					return liqReconfeccionAdapter;						
				}
				
				if (!"RS".equals(deuda.getRecClaDeu().getAbrClaDeu())){
					liqReconfeccionAdapter.addRecoverableError(GdeError.LIQVOLANTEPAGOINTRS_DEUDA_NO_RS_SELECCIONADA);
					return liqReconfeccionAdapter;
				}
				
				if (deuda.getImporte().doubleValue() != deuda.getSaldo().doubleValue()){
					liqReconfeccionAdapter.addRecoverableError(GdeError.LIQVOLANTEPAGOINTRS_DEUDA_RS_NO_PERMITIDA);
					return liqReconfeccionAdapter;
				}
				
				// toVO
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO();
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdEstadoDeuda(idEstadoDeuda);
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				liqDeudaVO.setSaldo(deuda.getSaldo());
			
				DeudaAct deudaAct = deuda.actualizacionSaldo();
				liqDeudaVO.setActualizacion(deudaAct.getRecargo());
				liqDeudaVO.setTotal(deudaAct.getImporteAct());
				
				liqReconfeccionAdapter.getListDeuda().add(liqDeudaVO);

			}
				
		}else{
			liqReconfeccionAdapter.addRecoverableError(GdeError.LIQVOLANTEPAGOINTRS_NO_HAY_DEUDAS_SELECCIONADAS);
		}
		return liqReconfeccionAdapter;
	}
	
	/** 
	 *  Generar Volante de Pago de Intereses de Regimen Simplificado para la deuda seleccionada a la fecha de pago indicada.
	 * 
	 * @param liqReconfeccionAdapter
	 * @param canal
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public static LiqReconfeccionAdapter generarVolantePagoIntRS(LiqReconfeccionAdapter liqReconfeccionAdapter, Canal canal, String usuario) throws Exception {
	
		List<LiqDeudaVO> listLiqDeudaVO = liqReconfeccionAdapter.getListDeuda();
		// Se obtiene la cuenta
		Cuenta cuenta = Cuenta.getById(liqReconfeccionAdapter.getCuenta().getIdCuenta());
		
		// Pasamos el filtro que viene de la liquidacion de deuda.
		cuenta.setLiqCuentaFilter(liqReconfeccionAdapter.getCuentaFilter());
		
		for (CuentaTitular ct: cuenta.getListCuentaTitularVigentes(new Date())){
			if (ct.getEsTitularPrincipal().intValue()== SiNo.SI.getId().intValue()){
				Contribuyente cont = ct.getContribuyente();
				cont.loadPersonaFromMCR();
				Persona persona = cont.getPersona();
				liqReconfeccionAdapter.getCuenta().setCuitTitularPrincipalContr(persona.getCuitContr());
			}
		}
		
		// Se pasa la liqDeudaVO a listDeuda (se maneja como lista porque se sigue la misma logica que en la reconfeccion, pero solo se permite seleccionar un registro)
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		for(LiqDeudaVO liqDeudaVO: listLiqDeudaVO) {
			Deuda deuda = Deuda.getById(liqDeudaVO.getIdDeuda(), liqDeudaVO.getIdEstadoDeuda());
			
			listDeuda.add(deuda);
		}
				
		// Definimos que fecha de vencimiento y actualizacion usamos
		Date fechaVencimiento = liqReconfeccionAdapter.getFechaPago();
		Date fechaActualizacion = liqReconfeccionAdapter.getFechaPago(); 
		boolean contemplaSellado = false; // TODO Por ahora no.
							
		// Se realiza la generacion del volante de pago:
		List<Recibo> listRecibo = cuenta.generarVolantePagoIntRS(listDeuda, fechaVencimiento, fechaActualizacion, canal, contemplaSellado);
	
		// Se pasa la lista de recibos a reciboVO
		List<LiqReciboVO> listLiqReciboVO = new ArrayList<LiqReciboVO>();
		for(Recibo recibo: listRecibo){
			LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqReconfeccionAdapter.getCuenta());
			
			// Ademas pasamos los datos que no estan en LiqReciboVO
			liqReciboVO.setEsVolPagIntRS(true);
			liqReciboVO.setEsDeudaRS(true);
			liqReciboVO.setCumur(cuenta.getValorCumurVigente(new Date()).getValor());
			
			listLiqReciboVO.add(liqReciboVO);
		}
		
		liqReconfeccionAdapter.setListRecibos(listLiqReciboVO);		
	
		
		return liqReconfeccionAdapter;
	}
}
