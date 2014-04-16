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

import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.AtrEmisionDefinition;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.iface.model.DatosReciboCdMVO;
import ar.gov.rosario.siat.gde.iface.model.LiqAtrEmisionVO;
import ar.gov.rosario.siat.gde.iface.model.LiqConceptoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.ReciboAdapter;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;


public class ReciboBeanHelper {
	
	private static Log log = LogFactory.getLog(ReciboBeanHelper.class);
	
	public static ReciboAdapter getReciboCuota(Convenio convenio, ReciboConvenio reciboConvenio)throws Exception{
		
		ReciboAdapter reciboAdapter = new ReciboAdapter();
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(convenio); 
		LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta();
		LiqConvenioVO liqConvenioVO = liqDeudaBeanHelper.getConvenio();
		
		reciboAdapter.setCuenta(liqCuentaVO);
		reciboAdapter.setConvenio(liqConvenioVO);
		
		//		 Se pasa la lista de recibos a reciboVO
		List<LiqReciboVO> listLiqReciboVO = new ArrayList<LiqReciboVO>();
		
		listLiqReciboVO.add(getReciboVO(reciboConvenio, liqConvenioVO, liqCuentaVO));
		
		reciboAdapter.setListRecibos(listLiqReciboVO);	
		
		reciboAdapter.setEsCuotaSaldo(reciboAdapter.getListRecibos().get(0).getEsCuotaSaldo());
		
		return reciboAdapter;
		
	}
	

	public static ReciboAdapter getReciboDeuda(Cuenta cuenta, Recibo recibo) throws Exception{
		
		ReciboAdapter reciboAdapter = new ReciboAdapter();
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
		LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta();
		reciboAdapter.setCuenta(liqCuentaVO);
		
		// Se pasa la lista de recibos a reciboVO
		List<LiqReciboVO> listLiqReciboVO = new ArrayList<LiqReciboVO>();
		
		listLiqReciboVO.add(getReciboVO(recibo, liqCuentaVO));
		
		reciboAdapter.setListRecibos(listLiqReciboVO);	
		
		reciboAdapter.setEsCuotaSaldo(reciboAdapter.getListRecibos().get(0).getEsCuotaSaldo());
		
		return reciboAdapter;
	}

	/**
	 * Esta funcion pasa de Recibo a RecivoVO
	 * 
	 * Deberia servir tanto para la impresion, como para la visualizacion.
	 * 
	 * @param recibo
	 * @param cuenta
	 * @return
	 * @throws Exception
	 */
	public static LiqReciboVO getReciboVO(Recibo recibo, LiqCuentaVO cuenta) throws Exception{
		LiqReciboVO reciboVO = new LiqReciboVO();
		reciboVO.setId(recibo.getId());
		Boolean cdm=false;
		
		// Seteamos el flag esta impreso
		if (recibo.getEstaImpreso() != null)
			reciboVO.setEstaImpreso(recibo.getEstaImpreso().equals(SiNo.SI.getBussId()));
		
		// Pasamos la bandera de Autoliquidable.
		reciboVO.getRecurso().setEsAutoliquidable(SiNo.getById(recibo.getRecurso().getEsAutoliquidable()));	
		
		// Seteamos el servicioBanco
		reciboVO.setServicioBanco((ServicioBancoVO)recibo.getServicioBanco().toVO(0));
		
		Recurso recurso = recibo.getRecurso();
		if (recurso.equals(Recurso.getDReI()) || recurso.equals(Recurso.getETur())){
			reciboVO.setEsComercio(true);
			if(recurso.equals(Recurso.getDReI()))
				reciboVO.getRecurso().setDeclaraAdicionales(true);
		}
		
		if (recibo.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP) || 
				recibo.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG)){
			
			// Seteamos la descripcion de la categoria del recuso
			reciboVO.getRecurso().setDesRecurso(recibo.getRecurso().getCategoria().getDesCategoria());

			reciboVO.getRecurso().setCodRecurso("CdM");
			reciboVO.setTipoDeudaStr("CUOTA");
			cdm=true;
		} else {
			
			// Seteamos la descripcion de la categoria del recuso
			reciboVO.getRecurso().setDesRecurso(recibo.getRecurso().getDesRecurso());

			reciboVO.getRecurso().setCodRecurso(recibo.getRecurso().getCodRecurso());
			reciboVO.setTipoDeudaStr("PERIODO");
		}
		
		// Setea la fecha de asentamiento
		reciboVO.getRecurso().setFecUltPag(recibo.getRecurso().getFecUltPag());
		
		// Seteo el procurador
		reciboVO.getProcurador().setDescripcion(recibo.getProcurador()!=null?recibo.getProcurador().getDescripcion():"");
		
		reciboVO.setAnioRecibo(recibo.getAnioRecibo());
		reciboVO.setCodRefPag(String.valueOf(recibo.getCodRefPag()));
		reciboVO.setNroSistema(recibo.getIdSistema());
		reciboVO.setCuenta(cuenta);
		reciboVO.setDesActualizacion(recibo.getDesActualizacion());
		reciboVO.setDesCapitalOrigina(recibo.getDesCapitalOriginal());
		reciboVO.setSellado((recibo.getSellado()!=null?recibo.getSellado().getImporteSellado():0));
		reciboVO.setImporteSellado(recibo.getImporteSellado());
		reciboVO.setObservacion(recibo.getObservacion());
		
		// Seteamos el valor de escuota saldo
		reciboVO.setEsCuotaSaldo(recibo.getEsCuotaSaldo()==null?false:recibo.getEsCuotaSaldo().intValue()==SiNo.SI.getId().intValue());
		
		//reciboVO.setPropietario(recibo.getCuenta().getDesTitularPrincipal());
		reciboVO.setPropietario(recibo.getCuenta().getDesTitularPrincipalContr());
		reciboVO.setIdBroche(recibo.getIdBroche()!=null?recibo.getIdBroche():0);
		
		log.debug("## getReciboVO() - transfiere getNroCodigoBarra()"); 
		reciboVO.setNroCodigoBarra(recibo.getNroCodigoBarra());
		reciboVO.setNroCodigoBarraDelim(recibo.getNroCodigoBarraDelim());
		log.debug("## reciboVO.getNroCodigoBarra()="+reciboVO.getNroCodigoBarra());
		log.debug("## reciboVO.getCodigoBarraDelim()="+reciboVO.getNroCodigoBarraDelim());

		log.debug("## getReciboVO() - generando Codigo de Barras para Txt"); 
		reciboVO.setCodBarForTxt(recibo.getCodBarForTxt());
		reciboVO.setCodBarForTxtDelim(recibo.getCodBarForTxtDelim());
		log.debug("## reciboVO.getCodBarForTxt()="+reciboVO.getCodBarForTxt());
		log.debug("## reciboVO.getCodBarForTxtDelim()="+reciboVO.getCodBarForTxtDelim());		

		reciboVO.setPagaVencido(recibo.getPagaVencido());
		
		// se setea el descuento aplicado, puede ser que sea nulo si todav�a no venci� la deuda
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
		if (recibo.getEsCuotaSaldo()==null || recibo.getEsCuotaSaldo() == SiNo.NO.getId()){
			reciboVO.setTotActualizacion(recibo.getTotActualizacion());
		}else{
			reciboVO.setTotActualizacion(0D);
		}
		
		//Validacion para que muestre como reimpresion una rectificativa no vencida de drei
		if(recibo.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue()&&
				DateUtil.isDateAfterOrEqual(recibo.getListReciboDeuda().get(0).getDeuda().getFechaVencimiento(),
						new Date()))
				reciboVO.setEsRecNoVencida(true);
		
		reciboVO.setFechaPago(recibo.getFechaPago());
		Boolean estaPago = false;
		if (recibo.getFechaPago() != null){
			reciboVO.setDesEstado("Pagado - Fecha de Pago: " + DateUtil.formatDate(recibo.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
			estaPago =  true;
		} else {
			reciboVO.setDesEstado("Impago");
		}
		reciboVO.setEstaPago(estaPago);
		
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
		
		// Seteo de la lista de reciboDeuda
		log.debug("Va a iterar ListReciboDeuda -----");
		for(ReciboDeuda reciboDeuda: recibo.getListReciboDeuda()){
			log.debug("Periodo:"+reciboDeuda.getDeuda().getStrPeriodo());
			log.debug("capital:"+reciboDeuda.getCapitalOriginal());
			log.debug("desCapital"+reciboDeuda.getDesCapitalOriginal());
			log.debug("totCapital:"+reciboDeuda.getTotCapital());
			log.debug("act:"+reciboDeuda.getActualizacion());
			log.debug("desAct:"+reciboDeuda.getDesActualizacion());
			log.debug("totAct:"+reciboDeuda.getTotActualizacion());
			log.debug("totReciboDeuda:"+reciboDeuda.getTotalReciboDeuda());
			
			LiqReciboDeudaVO reciboDeudaVO = new LiqReciboDeudaVO();
			
			//Se pasa el importe de la deuda para poder discriminar los recibos con Importe CERO
			if(reciboDeuda.getDeuda()!=null){
				reciboDeudaVO.setDeuda(new LiqDeudaVO());
				reciboDeudaVO.getDeuda().setImporte(reciboDeuda.getDeuda().getImporte());
			}
			
			if (cdm){
				reciboDeuda.setPeriodoDeuda(reciboDeuda.getDeuda().getStrPeriodoCdmSinCuota());
			}else{
				reciboDeuda.setPeriodoDeuda(reciboDeuda.getDeuda().getStrPeriodo());
			}
			//reciboDeuda.setTotalReciboDeuda(reciboDeuda.getTotCapital()+reciboDeuda.getTotActualizacion());
			reciboDeudaVO.setCapitalOriginal(reciboDeuda.getCapitalOriginal());
			reciboDeudaVO.setDesCapitalOriginal(reciboDeuda.getDesCapitalOriginal());
			reciboDeudaVO.setTotCapital(reciboDeuda.getTotCapital());
			
			reciboDeudaVO.setActualizacion(reciboDeuda.getActualizacion());			
			reciboDeudaVO.setDesActualizacion(reciboDeuda.getDesActualizacion());
			reciboDeudaVO.setTotActualizacion(reciboDeuda.getTotActualizacion());
			
			reciboDeudaVO.setTotalReciboDeuda(reciboDeuda.getTotalReciboDeuda());
			
			reciboDeudaVO.setPeriodoDeuda(reciboDeuda.getPeriodoDeuda());
			reciboDeudaVO.setConceptosDeuda(reciboDeuda.getConceptos());
			
			if (reciboDeuda.getRectificativa()!=null && reciboDeuda.getRectificativa()==SiNo.SI.getId() && reciboDeuda.getRectificativa().intValue()==SiNo.SI.getId().intValue()&&reciboDeuda.getCapitalOriginal()==0D){
				RecClaDeu recClaDeu=null;
				if(reciboDeuda.getDeuda().getRecurso().equals(Recurso.getDReI()))
						recClaDeu=RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI);
				if(reciboDeuda.getDeuda().getRecurso().equals(Recurso.getETur()))
					recClaDeu=RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_ETUR);
				if(reciboDeuda.getDeuda().getRecurso().equals(Recurso.getByCodigo(Recurso.COD_RECURSO_DPUB)))
					recClaDeu=RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_DERPUB);
				String strPeriodoDeuda = recClaDeu.getAbrClaDeu() + " "+ reciboDeuda.getDeuda().getPeriodo() + "/"+reciboDeuda.getDeuda().getAnio();
				reciboDeudaVO.setPeriodoDeuda(strPeriodoDeuda);
			}
				
			
			reciboDeudaVO.getDeuda().setFechaVto(DateUtil.formatDate(reciboDeuda.getDeuda().getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK));
			reciboVO.setTotal(reciboVO.getTotal()+reciboDeudaVO.getTotalReciboDeuda());
			if (estaPago && reciboDeuda.getDeuda().getEsIndeterminada()){
				reciboDeudaVO.setTipoPago("INDETERMINADO"); 
			}else if (estaPago){
				reciboDeudaVO.setTipoPago("PAGO BUENO");
			}
			
			// seteo de los valores de los conceptos en el orden correcto.
			 LiqConceptoDeudaVO[] conceptos = new LiqConceptoDeudaVO[reciboVO.getListRecCon().size()];
			 List<DeuAdmRecCon> listDeuAdmRecCon = reciboDeuda.getListDeuAdmRecCon();
			 for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
				 int nroOrden = getOrdenVisualizacion(reciboVO.getListRecCon(), deuAdmRecCon.getRecCon().getId());
				 LiqConceptoDeudaVO concepto = new LiqConceptoDeudaVO();
				 concepto.setDesConcepto(deuAdmRecCon.getRecCon().getDesRecCon());
				 concepto.setImporte(deuAdmRecCon.getSaldo());
				 concepto.setAbrConcepto(deuAdmRecCon.getRecCon().getAbrRecCon());
				 conceptos[nroOrden] = concepto;
				 
			 }	
			 log.debug("LiqReciboBeanHelper.getReciboVO() conceptos del reciboDeuda");
			 for(int i=0;i<conceptos.length;i++){
				 if(conceptos[i]==null)
					 conceptos[i] = new LiqConceptoDeudaVO();
				 reciboDeudaVO.getListConceptosDeuda().add(conceptos[i]);
				 log.debug(conceptos[i].getDesConcepto()+":"+conceptos[i].getImporte());				 
			 }			 

			 log.debug("LiqReciboBeanHelper.getReciboVO() ----- Obteniendo los atributos de emision de la deuda");
			 AtrEmisionDefinition atrEmisionDefinition = reciboDeuda.getAtrEmisionDefinition();
			 if (atrEmisionDefinition != null) {
				reciboDeudaVO.setLiqAtrEmisionVO(new LiqAtrEmisionVO(atrEmisionDefinition));
			 }
			 
			 reciboVO.getListReciboDeuda().add(reciboDeudaVO);						 
		}
		log.debug("Fin iteracion ListReciboDeuda -----");
		
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
		
		if (recibo.getDatosReciboCdM() != null){
			log.debug("toVO especifico para Recibos de Contribucion de Mejoras");
			reciboVO.setDatosReciboCdMVO((DatosReciboCdMVO) recibo.getDatosReciboCdM().toVO(1,false));
		}else{
			log.debug("NO hizo el toVO especifico para Recibos de Contribucion de Mejoras");
		}

		// Setea la Localidad
		reciboVO.setLocalidad(recibo.getCuenta().getStrLocalidad());
		
		/*if(recibo.getProcesoMasivo().getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)){
			log.debug("Va a setear datos especificos para la reconfeccion");
		}*/
		
		// Seteamos la descripcion en caso que sea cuota saldo
		if (recibo.getEsCuotaSaldo() != null && recibo.getEsCuotaSaldo().intValue() == 1){
			
			if (recibo.getListReciboDeuda().size() == 1){
				reciboVO.setDesCuotaSaldo("Cuota " + recibo.getListReciboDeuda().get(0).getPeriodoDeuda());
				
			} else {
				reciboVO.setDesCuotaSaldo("Desde cuota " + recibo.getListReciboDeuda().get(0).getPeriodoDeuda() + 
						" hasta cuota " + recibo.getListReciboDeuda().get(recibo.getListReciboDeuda().size() -1 ).getPeriodoDeuda());
			}
		}
		
		
		//Seteo el Servicio a Banco
		
		ServicioBancoVO servicioBancoVO = (ServicioBancoVO) recibo.getServicioBanco().toVO(1,false);
		servicioBancoVO.setListSerBanDesGen(null);
		servicioBancoVO.setListSerBanRec(null);
		reciboVO.setServicioBanco(servicioBancoVO);
		
		return reciboVO;
	}
		
	/**
	 * Esta funcion pasa de ReciboConvenio a RecivoVO
	 * 
	 * @param recibo
	 * @param cuenta
	 * @return
	 * @throws Exception
	 */
	public static LiqReciboVO getReciboVO(ReciboConvenio reciboConvenio, LiqConvenioVO convenio, LiqCuentaVO cuenta) throws Exception{
		LiqReciboVO reciboVO = new LiqReciboVO();
		reciboVO.setId(reciboConvenio.getId());
		reciboVO.setAnioRecibo(reciboConvenio.getAnioRecibo().intValue());
		reciboVO.setCodRefPag(String.valueOf(reciboConvenio.getCodRefPag()));
		reciboVO.setCuenta(cuenta);
		reciboVO.setConvenio(convenio);
		
		//reciboVO.setEsCuotaSaldo(reciboConvenio.getEsCuotaSaldo().intValue()==SiNo.SI.getId().intValue());
		reciboVO.getRecurso().setCodRecurso(reciboConvenio.getConvenio().getRecurso().getCodRecurso());
		
		if (reciboConvenio.getConvenio().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP) || 
				reciboConvenio.getConvenio().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG)){
			reciboVO.getRecurso().setCodRecurso("CdM");
		}
		
		// Seteamos el valor de escuota saldo
		reciboVO.setEsCuotaSaldo(reciboConvenio.getEsCuotaSaldo()==null?false:reciboConvenio.getEsCuotaSaldo().intValue()==SiNo.SI.getId().intValue());
		

		reciboVO.setSellado((reciboConvenio.getSellado()!=null?reciboConvenio.getSellado().getImporteSellado():0));
		reciboVO.setImporteSellado(reciboConvenio.getImporteSellado());
		
		reciboVO.setPropietario(reciboConvenio.getConvenio().getCuenta().getNombreTitularPrincipal());

		log.debug("## getReciboVO() - transfiere getNroCodigoBarra()"); 
		reciboVO.setNroCodigoBarra(reciboConvenio.getNroCodigoBarra());
		reciboVO.setNroCodigoBarraDelim(reciboConvenio.getNroCodigoBarraDelim());
		
		log.debug("## reciboVO.getNroCodigoBarra()="+reciboVO.getNroCodigoBarra());
		log.debug("## reciboVO.getCodigoBarraDelim()="+reciboVO.getNroCodigoBarraDelim());
		
		reciboVO.setPagaVencido(reciboConvenio.getPagaVencido());		

		// se setea el descuento aplicado, puede ser que sea nulo si todav�a no venci� la deuda
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
		Boolean estaPago=false;
		reciboVO.setFechaPago(reciboConvenio.getFechaPago());
		if (reciboConvenio.getFechaPago() != null){
			reciboVO.setDesEstado("Pagado - Fecha de Pago: " + DateUtil.formatDate(reciboConvenio.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
			estaPago=true;
		} else {
			reciboVO.setDesEstado("Impago");
		}
		
		reciboVO.setEstaPago(estaPago);
		
		/*/ Seteo de la lista de cabecerea de conceptos(Tasa, sobretasa, C.A.P., adicionales, etc)
		for(RecCon recCon: reciboConvenio.getConvenio().getCuenta().getRecurso().getListRecConPorOrdenVisualizacion()){
			RecConVO recConVO = new RecConVO();
			recConVO.setId(recCon.getId());
			recConVO.setCodRecCon(recCon.getCodRecCon());			
			recConVO.setAbrRecCon(recCon.getAbrRecCon());
			recConVO.setDesRecCon(recCon.getDesRecCon().toUpperCase());
			recConVO.setOrdenVisualizacion(recCon.getOrdenVisualizacion());
			reciboVO.getListRecCon().add(recConVO);
		}*/
		
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
			//si esta incluida la cuota 1 con sellado seteo al recibo el sellado si es reimpresion, sino lo dejo en el importe cuota
			if (convenioCuota.getNumeroCuota()==1 && convenioCuota.getSellado()!=null){
				liqCuotaVO.setImporteSellado(convenioCuota.getImporteSellado());
				if (DateUtil.isDateAfterOrEqual(convenioCuota.getFechaVencimiento(), reciboConvenio.getFechaVencimiento())){
					reciboVO.setSellado(convenioCuota.getImporteSellado());
				}else{
					liqCuotaVO.setImporteCuota(convenioCuota.getImporteCuota());
				}
			}
			liqCuotaVO.setTotal(liqCuotaVO.getImporteCuota()+reciboConCuo.getTotActualizacion());
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			if (estaPago && convenioCuota.getEsIndeterminada()){
				liqCuotaVO.setTipoPago("INDETERMINADO");
			}else if (estaPago){
				liqCuotaVO.setTipoPago(convenioCuota.getEstadoConCuo().getDesEstadoConCuo());
			}
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
	 * Esta funcion pasa de ReciboConvenio a RecivoVO
	 * 
	 * @param recibo
	 * @param cuenta
	 * @return
	 * @throws Exception
	 */
	public static LiqReciboVO getReciboVO(ReciboConvenio reciboConvenio, LiqConvenioVO convenio, ProcedimientoVO procedimiento) throws Exception{
		LiqReciboVO reciboVO = new LiqReciboVO();
		reciboVO.setId(reciboConvenio.getId());
		reciboVO.setAnioRecibo(reciboConvenio.getAnioRecibo().intValue());
		reciboVO.setCodRefPag(String.valueOf(reciboConvenio.getCodRefPag()));		
		reciboVO.setProcedimiento(procedimiento);
		reciboVO.setConvenio(convenio);
		
		// Seteamos el valor de escuota saldo
		reciboVO.setEsCuotaSaldo(reciboConvenio.getEsCuotaSaldo()==null?false:reciboConvenio.getEsCuotaSaldo().intValue()==SiNo.SI.getId().intValue());
		

		reciboVO.setSellado((reciboConvenio.getSellado()!=null?reciboConvenio.getSellado().getImporteSellado():0));
		reciboVO.setImporteSellado(reciboConvenio.getImporteSellado());
		
		log.debug("## getReciboVO() - transfiere getNroCodigoBarra()"); 
		reciboVO.setNroCodigoBarra(reciboConvenio.getNroCodigoBarra());
		reciboVO.setNroCodigoBarraDelim(reciboConvenio.getNroCodigoBarraDelim());
		
		log.debug("## reciboVO.getNroCodigoBarra()="+reciboVO.getNroCodigoBarra());
		log.debug("## reciboVO.getCodigoBarraDelim()="+reciboVO.getNroCodigoBarraDelim());
		
		reciboVO.setPagaVencido(reciboConvenio.getPagaVencido());		

		// se setea el descuento aplicado, puede ser que sea nulo si todavia no vencio la deuda
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
		Boolean estaPago=false;
		reciboVO.setFechaPago(reciboConvenio.getFechaPago());
		if (reciboConvenio.getFechaPago() != null){
			reciboVO.setDesEstado("Pagado - Fecha de Pago: " + DateUtil.formatDate(reciboConvenio.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
			estaPago=true;
		} else {
			reciboVO.setDesEstado("Impago");
		}
		
		reciboVO.setEstaPago(estaPago);
		
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
			//si esta incluida la cuota 1 con sellado seteo al recibo el sellado si es reimpresion, sino lo dejo en el importe cuota
			if (convenioCuota.getNumeroCuota()==1 && convenioCuota.getSellado()!=null){
				liqCuotaVO.setImporteSellado(convenioCuota.getImporteSellado());
				if (DateUtil.isDateAfterOrEqual(convenioCuota.getFechaVencimiento(), reciboConvenio.getFechaVencimiento())){
					reciboVO.setSellado(convenioCuota.getImporteSellado());
				}else{
					liqCuotaVO.setImporteCuota(convenioCuota.getImporteCuota());
				}
			}
			liqCuotaVO.setTotal(liqCuotaVO.getImporteCuota()+reciboConCuo.getTotActualizacion());
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			if (estaPago && convenioCuota.getEsIndeterminada()){
				liqCuotaVO.setTipoPago("INDETERMINADO");
			}else if (estaPago){
				liqCuotaVO.setTipoPago(convenioCuota.getEstadoConCuo().getDesEstadoConCuo());
			}
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
	 */
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
	}

}
