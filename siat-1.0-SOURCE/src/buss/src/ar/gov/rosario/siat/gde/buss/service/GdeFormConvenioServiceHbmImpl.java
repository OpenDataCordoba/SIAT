//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;


import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.LiqFormConvenioBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.TipoDocApo;
import ar.gov.rosario.siat.gde.buss.bean.TipoPerFor;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.LiqRecibos;
import ar.gov.rosario.siat.gde.iface.model.TipoDocApoVO;
import ar.gov.rosario.siat.gde.iface.model.TipoPerForVO;
import ar.gov.rosario.siat.gde.iface.service.IGdeFormConvenioService;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import ar.gov.rosario.siat.seg.buss.bean.Oficina;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeFormConvenioServiceHbmImpl implements IGdeFormConvenioService {
	
	private Logger log = Logger.getLogger(GdeFormConvenioServiceHbmImpl.class);
	
	
	/**
	 * Este metodo init recibe un LiqFormConvenioAdapter que siempre llega instanciado por cuestiones de navegacion
	 * de estos CUS en particular.  
	 * 
	 */
	public LiqFormConvenioAdapter getLiqFormConvenioInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Cuenta cuenta = Cuenta.getById(liqFormConvenioAdapterVO.getCuenta().getIdCuenta());
		
		cuenta.setLiqCuentaFilter(liqFormConvenioAdapterVO.getCuentaFilter());
		
		LiqFormConvenioBeanHelper liqFormConvHelper = new LiqFormConvenioBeanHelper(cuenta);
		
		return liqFormConvHelper.getLiqFormConvenioInit(liqFormConvenioAdapterVO);
	}

	public LiqFormConvenioAdapter getPlanes(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		return LiqFormConvenioBeanHelper.getPlanes(liqFormConvenioAdapter);
	}
	
	public LiqFormConvenioAdapter getAlternativaCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		return LiqFormConvenioBeanHelper.getAlternativaCuotas(liqFormConvenioAdapter);
	}
	
	public LiqFormConvenioAdapter getSimulacionCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		return LiqFormConvenioBeanHelper.getSimulacionCuotas(liqFormConvenioAdapter);
	}

	public LiqFormConvenioAdapter getFormalizarPlanInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			List<TipoPerFor> listTipoPerFor = TipoPerFor.getListActivos();
			List<TipoDocApo> listTipoDocApo = TipoDocApo.getListActivos();
			
			// Inicializamos esta bandera en falso para que no muestre el boton modificar datos
			liqFormConvenioAdapter.getConvenio().setPoseeDatosPersona(false);
			
			Oficina oficina = Oficina.getByIdNull(userContext.getIdOficina());
			Area area = Area.getByIdNull(userContext.getIdArea());
			
			String lugarFor = ""; 

			if (oficina != null)
				lugarFor += oficina.getDesOficina();
			else
				lugarFor += area.getDesArea();
			
			liqFormConvenioAdapter.getConvenio().setLugarFor(lugarFor);
			
			liqFormConvenioAdapter.setListTipoPerFor(ListUtilBean.toVO(listTipoPerFor, 
					new TipoPerForVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			liqFormConvenioAdapter.setListTipoDocApo(ListUtilBean.toVO(listTipoDocApo, 
					new TipoDocApoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Lista de Sexo para la busqueda reducida de personas
			liqFormConvenioAdapter.setListSexo(Sexo.getList(Sexo.OpcionSeleccionar));
			
			// Se calcula la simulacion de las cuotas para en numero de cuotas seleccionadas.
			liqFormConvenioAdapter = LiqFormConvenioBeanHelper.getSimulacionCuotas(liqFormConvenioAdapter);
			
			
			// Diferenciamos si se trata de un plan especial
			if (liqFormConvenioAdapter.getEsEspecial()){
				String obs = "";
				
				if (!StringUtil.isNullOrEmpty(liqFormConvenioAdapter.getCantMaxCuoView()))
					obs += "PARAMETROS DEL CONVENIO ESPECIAL Cantidad M\u00E1xima de Cuotas: "+ liqFormConvenioAdapter.getCantMaxCuoView() ;
				
				if (!StringUtil.isNullOrEmpty(liqFormConvenioAdapter.getImpMinCuoView()))
					obs += "; Importe M\u00EDnimo de Cuota: " + liqFormConvenioAdapter.getImpMinCuoView();
				
				obs += "; Fecha de Actualizaci\u00F3n de deuda: "+ liqFormConvenioAdapter.getFechaFormalizacionView();
				obs += "; Descuento de Capital: " + liqFormConvenioAdapter.getDescCapitalView(); 
				obs += "; Descuento de Actualizaci\u00F3n" + liqFormConvenioAdapter.getDescActualizacionView();
				obs += "; Inter\u00E9s Financiero: "+ liqFormConvenioAdapter.getInteresView(); 
				obs += "; Vencimiento 1er Cuota: " + liqFormConvenioAdapter.getVenPrimeraCuotaView(); 
				obs += "; Importe Manual Anticipo: " + liqFormConvenioAdapter.getImporteAnticipoView();
				
				liqFormConvenioAdapter.getConvenio().setObservacionFor(obs);
			}
			
			log.debug(funcName + ": exit");
			return liqFormConvenioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LiqFormConvenioAdapter paramPersona(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO, Long selectedId) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// Con el idPersona obtenger cargar la persona del facade
			Persona persona = Persona.getById(selectedId);
			liqFormConvenioAdapterVO.getConvenio().setPersona((PersonaVO) persona.toVO(3));
			liqFormConvenioAdapterVO.getConvenio().setPoseeDatosPersona(true);
			
			log.debug(funcName + ": exit");
			return liqFormConvenioAdapterVO;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public LiqFormConvenioAdapter formalizarPlan(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		log.debug( funcName + " liqFormConvenioAdapter: " + liqFormConvenioAdapter.infoString());
		
		return LiqFormConvenioBeanHelper.formalizarPlan(liqFormConvenioAdapter);
	}
	
	public PrintModel getPrintForm(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			// liqFormConvenioAdapterVO viene populado por el metodo  getConvenioFormalizado();
			
			Plan plan = Plan.getById(liqFormConvenioAdapterVO.getPlanSelected().getIdPlan());
			liqFormConvenioAdapterVO.getConvenio().setLeyendaForm(plan.getLeyendaForm());
			liqFormConvenioAdapterVO.getConvenio().setOrdenanza(plan.getOrdenanza());
			
			// Se crea el PrintModel que retorna
			PrintModel print = new PrintModel();
			print.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
			print.putCabecera("Usuario", userContext.getUserName());
			print.setExcludeFileName("/publico/general/reportes/default.exclude");
			print.setXslPdfString(plan.getFormulario().getXsl());
			print.setXslTxtString(plan.getFormulario().getXslTxt());
			print.setData(liqFormConvenioAdapterVO);
			print.setTopeProfundidad(3);
			print.putCabecera("FechaActualEnLetras" , DateUtil.getDateEnLetras(new Date()));
			
			return print;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	
	public PrintModel getPrintFormAuto(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
						
			Plan plan = Plan.getById(liqFormConvenioAdapterVO.getPlanSelected().getIdPlan());
			liqFormConvenioAdapterVO.getConvenio().setLeyendaForm(plan.getLeyendaForm());
			liqFormConvenioAdapterVO.getConvenio().setOrdenanza(plan.getOrdenanza());
			
			// Se crea el PrintModel que retorna
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FORM_CONVENIO_AUTO);
			print.setData(liqFormConvenioAdapterVO);
			print.setTopeProfundidad(3);
			print.putCabecera("Usuario", userContext.getUserName());
			print.putCabecera("FechaActualEnLetras" , DateUtil.getDateEnLetras(new Date()));
			
			return print;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PrintModel getPrintAltCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		liqFormConvenioAdapterVO.clearError();
		try {
			
			// Se crea el PrintModel que retorna
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_PLAN_ALT_CUOTAS);
			print.setData(liqFormConvenioAdapterVO);
			print.setTopeProfundidad(3);
			print.putCabecera("Usuario", userContext.getUserName());
			print.putCabecera("FechaActualEnLetras" , DateUtil.getDateEnLetras(new Date()));
			
			
			return print;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PrintModel getPrintRecibos(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			Convenio convenio = Convenio.getById(liqFormConvenioAdapterVO.getConvenio().getIdConvenio()); 
			
			// Pasamos el filtro de la cuenta.
			convenio.getCuenta().setLiqCuentaFilter(liqFormConvenioAdapterVO.getCuentaFilter());
			
			Long[] listIdCuotasSelected = ListUtil.getArrLongIdFromArrStringId(liqFormConvenioAdapterVO.getListIdCuotaSelected());
			
			List<LiqReciboVO> listReciboVO = LiqFormConvenioBeanHelper.getListLiqReciboVO(convenio, listIdCuotasSelected); 
			
			// Se obtiene el formulario de recibo de cuota
			PrintModel print = Formulario.getPrintModelForPDF(Recibo.COD_FRM_RECIBO_CUOTA);
			print.setData(new LiqRecibos(listReciboVO));
			print.setTopeProfundidad(3);
			
			return print;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public LiqFormConvenioAdapter getConvenioFormalizado(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
		try{
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession();
		
			Convenio convenio = Convenio.getById(liqFormConvenioAdapterVO.getConvenio().getIdConvenio());

			if(convenio.getProcedimiento() == null){
				return LiqFormConvenioBeanHelper.getConvenioFormalizado(liqFormConvenioAdapterVO);
				
			} else {
				return CyqServiceLocator.getConcursoyQuiebraService().getConvenioFormalizado(userContext, liqFormConvenioAdapterVO);
			}
		
		}catch (Exception e){
			log.error("Service error: ", e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public LiqFormConvenioAdapter getPlanesEsp(UserContext userContext,
			LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
	
		return LiqFormConvenioBeanHelper.getPlanesEsp(liqFormConvenioAdapterVO);
	}

	
	public void crearTransaccionDummy(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			Convenio convenio = Convenio.getById(liqConvenioCuentaAdapterVO.getConvenio().getIdConvenio()); 
				
			Long[] listId = new Long[liqConvenioCuentaAdapterVO.getListIdCuotaSelected().length];
			
			for (int i=0; i < liqConvenioCuentaAdapterVO.getListIdCuotaSelected().length; i++){
				listId[i] = new Long(liqConvenioCuentaAdapterVO.getListIdCuotaSelected()[i]);
			}
			
			List<ConvenioCuota> listConvenioCuotaSelectd = (List<ConvenioCuota>) ListUtilBean.getListBeanByListId(
					convenio.getListConvenioCuota(), listId);
			
			LogFile output = new LogFile("/tmp/", "transaccionConvenio-" + convenio.getId() + ".txt", false);
			
			// (0)sistema -> 2 
			// (1)codrefpag -> 10
			// (2)fecha_pago = ddmmaaaa
			// (3)importe = con punto y 4 decimales
			// (4)recargo = 000000.0000
			// (5)fecha_balance = ddmmaaaa
			
			DecimalFormat decimalFormat = new DecimalFormat("0.000");
			
			for (ConvenioCuota convenioCuota:listConvenioCuotaSelectd){
				String[] arrValues = new String[6];
				arrValues[0] = "" + convenioCuota.getSistema().getSistemaEsServicioBanco().getNroSistema();				
				arrValues[1] = "" + convenioCuota.getCodRefPag();
				arrValues[2] = DateUtil.formatDate(new Date() , DateUtil.ddMMYYYY_MASK);
				arrValues[3] = "" + (decimalFormat.format(convenioCuota.getImporteCuota())).replace(",", ".") ;
				arrValues[4] = "00000.0000";
				arrValues[5] = "12122008";
				
				escribiArchivo(arrValues, output);
			}
			
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	
	public void escribiArchivo(String[] arrValues, LogFile output) throws Exception {

		// Estrucura de la linea:
		// (0)sistema -> 2 
		// (1)codrefpag -> 10
		// 000 tipoBoleta 00 - tipoBoleta=3 
		// resto = 0000
		// cp = 4
		// caja= idBanco
		// cod_tr = 00
		// (2)fecha_pago = ddmmaaaa
		// (3)importe = con punto y 4 decimales
		// (4)recargo = 000000.0000
		// cd = 0
		// paquete = 4154
		// marca_tr = 0
		// recibo_tr = 0000
		// (5)fecha_balance = ddmmaaaa
		
		StringBuffer line = new StringBuffer();
		line.append(arrValues[0]);		 
		line.append("|");
		line.append(StringUtil.completarCerosIzq(arrValues[1], 10));		 
		line.append("|");
		line.append("000300");
		line.append("|");
		line.append("0000");
		line.append("|");
		line.append("4");
		line.append("|");
		line.append("65"); //caja
		line.append("|");
		line.append("00");
		line.append("|");
		line.append(arrValues[2]); // fechaPago
		line.append("|");		
		line.append(arrValues[3]); // importe
		line.append("|");
		line.append(arrValues[4]); // recargo
		line.append("|");
		line.append("0");
		line.append("|");
		line.append("4154");
		line.append("|");
		line.append("0");
		line.append("|");
		line.append("0000");
		line.append("|");
		line.append(arrValues[5]); // fechaBalance
		line.append("|");
		
		output.addline(line.toString());

	}
	
}
