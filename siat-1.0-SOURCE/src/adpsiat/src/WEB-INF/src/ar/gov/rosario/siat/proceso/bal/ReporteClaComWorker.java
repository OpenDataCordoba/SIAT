//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.bal;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Clasificador;
import ar.gov.rosario.siat.bal.buss.bean.InformeClaCom;
import ar.gov.rosario.siat.bal.buss.bean.Nodo;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PrintModel;

/**
 * Genera el Reporte de Clasificador Comparativo en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteClaComWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteClasificadorWorker.class);
		
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Reporte de Clasificador Comparativo: Generar PDF
			this.generarReporte(adpRun);
		}else{
			// No existen otros pasos para el Proceso.
		}
	}

	public void reset(AdpRun adpRun) throws Exception {
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	/**
	 *  Generar Reporte de Clasificador Comparativo (PDF).
	 * 
	 * @param adpRun
	 * @throws Exception
	 */
	public void generarReporte(AdpRun adpRun) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			String ID_CLASIFICADOR_PARAM = "idClasificador";
			String PRI_FECHA_DESDE_PARAM = "priFechaDesde";
			String PRI_FECHA_HASTA_PARAM = "priFechaHasta";
			String SEG_FECHA_DESDE_PARAM = "segFechaDesde";
			String SEG_FECHA_HASTA_PARAM = "segFechaHasta";
			String NIVEL_PARAM = "nivel";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
	
			AdpParameter paramIdClasificador = adpRun.getAdpParameter(ID_CLASIFICADOR_PARAM);
			AdpParameter paramPriFechaHasta = adpRun.getAdpParameter(PRI_FECHA_HASTA_PARAM);
			AdpParameter paramPriFechaDesde = adpRun.getAdpParameter(PRI_FECHA_DESDE_PARAM);
			AdpParameter paramSegFechaHasta = adpRun.getAdpParameter(SEG_FECHA_HASTA_PARAM);
			AdpParameter paramSegFechaDesde = adpRun.getAdpParameter(SEG_FECHA_DESDE_PARAM);
			AdpParameter paramNivel = adpRun.getAdpParameter(NIVEL_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);
			
			Date priFechaDesde= DateUtil.getDate(paramPriFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date priFechaHasta= DateUtil.getDate(paramPriFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date segFechaDesde= DateUtil.getDate(paramSegFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date segFechaHasta= DateUtil.getDate(paramSegFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Long idClasificador = NumberUtil.getLong(paramIdClasificador.getValue());
			Integer nivel = NumberUtil.getInt(paramNivel.getValue());
								
			String userName = paramUserName.getValue();
			String userId = paramUserId.getValue();
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
								
			// Levanto la Corrida
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
			
			// Generamos el reporte pare el clasificador Rubro (clasificador principal)
			Clasificador clasificador = Clasificador.getById(Clasificador.ID_CLA_RUBRO);

			// Aqui obtiene lista de BOs
			List<Nodo> listNodo = Nodo.getListActivosByClasificador(clasificador);  

			List<Nodo> listNodoArbol = Nodo.obtenerListaArbolConTotalesComparativo(listNodo, priFechaDesde, priFechaHasta, segFechaDesde, segFechaHasta, null);
			
			List<NodoVO> listNodoVOOrdenados = (ArrayList<NodoVO>) ListUtilBean.toVO(listNodoArbol, 1,false);
			
			Clasificador claCom = Clasificador.getById(idClasificador);
			
			List<Nodo> listNodoRubro = listNodo;
			
			if(claCom.getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue()){
				// Aqui obtiene lista de BOs
				listNodo = Nodo.getListActivosByClasificador(claCom);  
				
				listNodoArbol = Nodo.obtenerListaArbolConTotalesComparativo(listNodo,  priFechaDesde, priFechaHasta, segFechaDesde, segFechaHasta, listNodoRubro);
				
				listNodoVOOrdenados = (ArrayList<NodoVO>) ListUtilBean.toVO(listNodoArbol, 1,false);
			}

			
			List<NodoVO> listNodoVOFiltrada = new ArrayList<NodoVO>();
	
			for(NodoVO nodo: listNodoVOOrdenados){
				if(nodo.getNivel().intValue() <= nivel.intValue())
					listNodoVOFiltrada.add(nodo);
			}

			Double totalGeneral = 0D;
			Double subTotal = 0D;
			Double aRestar = 0D;	
			Double totalGeneralCom = 0D;
			Double subTotalCom = 0D;
			Double aRestarCom = 0D;	
			for(NodoVO nodoVO: listNodoVOFiltrada){
				if(nodoVO.getEsNodoRaiz()){
					totalGeneral += nodoVO.getTotal();
					totalGeneralCom += nodoVO.getTotalCom();
					if(claCom.getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()){
						// Calculamos Subtotal Rubros 11 al 42 (pedido por el area de Balance)
						Long codNodoRaiz = null;
						try{ codNodoRaiz = Long.valueOf(nodoVO.getCodigo());}catch(Exception e){}
						if(codNodoRaiz != null && codNodoRaiz >=11 && codNodoRaiz <= 42){
							subTotal += nodoVO.getTotal();
							subTotalCom += nodoVO.getTotalCom();
						}							
					}
				}
				// Para el Clasificador de Procedencia: Calculamos total de 04.02.90 y 04.02.91 para restarlos al totalRecaudado y obtener el subTotal requerido (pedido por el area de Balance)
				if(claCom.getId().longValue() == Clasificador.ID_CLA_PROC.longValue() 
						&& ("04.02.90".equals(nodoVO.getClave()) || "04.02.91".equals(nodoVO.getClave()))){
					aRestar += nodoVO.getTotal();		
					aRestarCom += nodoVO.getTotalCom();		
				}
			}
			// Para el Clasificador de Procedencia: Calculamos el subTotal (Sin Ctas. 04.02.90 y 04.02.91) (pedido por el area de Balance)
			if(claCom.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
				subTotal = totalGeneral - aRestar;		
				subTotalCom = totalGeneralCom - aRestarCom;		
			}	
			
			InformeClaCom informe = new InformeClaCom();
	
			informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			informe.setDesClasificador(claCom.getDescripcion().toUpperCase());
	
			informe.setPriFechaDesde(DateUtil.formatDate(priFechaDesde, DateUtil.dd_MM_YYYY_MASK));
			informe.setPriFechaHasta(DateUtil.formatDate(priFechaHasta, DateUtil.dd_MM_YYYY_MASK));
			informe.setSegFechaDesde(DateUtil.formatDate(segFechaDesde, DateUtil.dd_MM_YYYY_MASK));
			informe.setSegFechaHasta(DateUtil.formatDate(segFechaHasta, DateUtil.dd_MM_YYYY_MASK));
			informe.setNivelFiltro(nivel.toString());
			informe.setTotalGeneralPri(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGeneral, 2)));
			informe.setTotalGeneralSeg(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGeneralCom, 2)));
			Double variacionTotal = 0D;
			if(totalGeneral != 0){
				variacionTotal = ((totalGeneralCom/totalGeneral)-1)*100;
			}
			informe.setVariacionTotal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(variacionTotal, 2)));
			if(claCom.getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue() || claCom.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
				informe.setSubTotalPri(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotal, 2)));
				informe.setSubTotalSeg(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotalCom, 2)));
				informe.setMostrarSubTotal("true");		
				Double variacionSubTotal = 0D;
				if(subTotal != 0){
					variacionSubTotal = ((subTotalCom/subTotal)-1)*100;
				}
				informe.setVariacionSubTotal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(variacionSubTotal, 2)));
			}
			if(claCom.getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue()){					
				informe.setClasificadorRubro("false");
			}
			informe.setListNodo(listNodoVOFiltrada);
			
			// Generamos el printModel
			PrintModel print =null;
			
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CLACOM);
			
			print.setNoAplicarTrim(true);
			print.putCabecera("TituloReporte", "Reporte de Clasificador Comparativo");
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			
			print.putCabecera("Usuario", userName);
			print.setData(informe);
			print.setTopeProfundidad(2);
			
			String fileDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator; 
			
			// Archivo pdf a generar
			String fileNamePdf = adpRun.getId()+"ClasificadorComparativo"+ userId +".pdf"; 
			File pdfFile = new File(fileDir+fileNamePdf);
			
			OutputStream out = new java.io.FileOutputStream(pdfFile);
			
			out.write(print.getByteArray());
			
			String nombre = "Reporte de Clasificador Comparativo";
			String descripcion = "Consulta de Totales para Clasificador Comparando para dos rangos de fechas.";
			
			// Guardamos el archivo generado en como archivo de salida del proceso.
			corrida.addOutputFile(nombre, descripcion, fileDir+fileNamePdf);		
			
			 if (corrida.hasError()) {
		        	tx.rollback();
		        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
		        	String error = "Error al generar PDF para el formulario";
		        	adpRun.changeState(AdpRunState.FIN_ERROR, error, false);
		        	adpRun.logError(error);
		     } else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					adpRun.changeState(AdpRunState.FIN_OK, "Reporte Generado Ok", true);
					String adpMessage = "Resultado de la peticion del usuario "+userName
					+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
					adpRun.changeDesCorrida(adpMessage);
			}
		    log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
