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

import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.Clasificador;
import ar.gov.rosario.siat.bal.buss.bean.Ejercicio;
import ar.gov.rosario.siat.bal.buss.bean.InformeClasificador;
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
 * Genera el Reporte de Clasificador en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteClasificadorWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteClasificadorWorker.class);
		
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Reporte de Clasificador: Generar PDF
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
	 *  Generar Reporte de Clasificador (PDF).
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
			
			String ID_EJERCICIO_PARAM = "idEjercicio";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String ID_BALANCE_PARAM = "idBalance";
			String REPORTE_EXTRA_PARAM = "reporteExtra";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
	
			AdpParameter paramIdEjercicio = adpRun.getAdpParameter(ID_EJERCICIO_PARAM);
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramIdBalance = adpRun.getAdpParameter(ID_BALANCE_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);
			AdpParameter paramReporteExtra = adpRun.getAdpParameter(REPORTE_EXTRA_PARAM);
			
			Long idEjercicio = NumberUtil.getLong(paramIdEjercicio.getValue());
			Date fechaDesde= DateUtil.getDate(paramFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta= DateUtil.getDate(paramFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Long idBalance = null;
			Balance balance = null;
			if(paramIdBalance != null){
				idBalance = Long.valueOf(paramIdBalance.getValue());
				balance = Balance.getById(idBalance);
			}
			String userName = paramUserName.getValue();
			String userId = paramUserId.getValue();
			boolean reporteExtra = "true".equals(paramReporteExtra.getValue());
			Long idClasificador = null;
			Long idNodo = null;
			Integer nivel = null;
			if(reporteExtra){
				String ID_CLASIFICADOR_PARAM = "idClasificador";
				String NIVEL_PARAM = "nivel";
				String ID_NODO_PARAM = "idNodo";
				AdpParameter paramIdClasificador = adpRun.getAdpParameter(ID_CLASIFICADOR_PARAM);
				AdpParameter paramNivel = adpRun.getAdpParameter(NIVEL_PARAM);
				AdpParameter paramIdNodo = adpRun.getAdpParameter(ID_NODO_PARAM);
				
				idClasificador = NumberUtil.getLong(paramIdClasificador.getValue());					
				nivel = NumberUtil.getInt(paramNivel.getValue());
				idNodo = NumberUtil.getLong(paramIdNodo.getValue());
			}
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			Ejercicio ejercicio = Ejercicio.getByIdNull(idEjercicio);
			String desEjercicio = "No seleccionado";
			if(ejercicio != null){
				desEjercicio = ejercicio.getDesEjercicio();
				fechaDesde = ejercicio.getFecIniEje();
				fechaHasta = ejercicio.getFecFinEje();
			}
					
			// Levanto la Corrida
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
			// Lista de Nodos VO para reporte extra si es necesario.
			List<NodoVO> listNodoVOReporteExtra = null;
			
			// Generamos el reporte pare el clasificador Rubro (clasificador principal)
			Clasificador clasificador = Clasificador.getById(Clasificador.ID_CLA_RUBRO);

			// Aqui obtiene lista de BOs
			List<Nodo> listNodo = Nodo.getListActivosByClasificador(clasificador);  

			List<Nodo> listNodoArbol = Nodo.obtenerListaArbolConTotales(listNodo, fechaDesde, fechaHasta, null, balance);
			
			List<NodoVO> listNodoVOOrdenados = (ArrayList<NodoVO>) ListUtilBean.toVO(listNodoArbol, 1,false);

			Double totalGeneral = 0D;
			Double subTotal = 0D;
			for(NodoVO nodoVO: listNodoVOOrdenados){
				nodoVO.setTotal(NumberUtil.truncate(nodoVO.getTotal(), 2)); 
				nodoVO.setTotalEjeAct(NumberUtil.truncate(nodoVO.getTotalEjeAct(), 2));
				nodoVO.setTotalEjeVen(NumberUtil.truncate(nodoVO.getTotalEjeVen(), 2));
				if(nodoVO.getEsNodoRaiz()){
					totalGeneral += nodoVO.getTotal();
					// Calculamos Subtotal Rubros 11 al 42 (pedido por el area de Balance)
					Long codNodoRaiz = null;
					try{ codNodoRaiz = Long.valueOf(nodoVO.getCodigo());}catch(Exception e){}
					if(codNodoRaiz != null && codNodoRaiz >=11 && codNodoRaiz <= 42){
						subTotal += nodoVO.getTotal();
					}
				}
			}
			
			if(reporteExtra && idClasificador.longValue() == Clasificador.ID_CLA_RUBRO.longValue()){
				listNodoVOReporteExtra = listNodoVOOrdenados;
			}
			InformeClasificador informe = new InformeClasificador();
			
			informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			informe.setDesClasificador(clasificador.getDescripcion().toUpperCase());
			informe.setDesEjercicio(desEjercicio);
			informe.setFechaDesde(DateUtil.formatDate(fechaDesde, DateUtil.dd_MM_YYYY_MASK));
			informe.setFechaHasta(DateUtil.formatDate(fechaHasta, DateUtil.dd_MM_YYYY_MASK));
			informe.setTotalGeneral(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGeneral, 2)));
			informe.setSubTotal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotal, 2)));
			informe.setMostrarSubTotal("true");
			if(idBalance != null){
				informe.setNroBalance(idBalance.toString());				
			}
			
			informe.setListNodo(listNodoVOOrdenados);
		
			// Generamos el printModel
			PrintModel print =null;
			
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CLASIFICADOR);
			
			print.setNoAplicarTrim(true);
			print.putCabecera("TituloReporte", "Clasificador de "+clasificador.getDescripcion());
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			
			print.putCabecera("Usuario", userName);
			print.setData(informe);
			print.setTopeProfundidad(2);
			
			
			String fileDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator; 
						
			// Archivo pdf a generar
			String fileNamePdf = adpRun.getId()+"Clasificador"+clasificador.getDescripcion()+ userId +".pdf"; 
			File pdfFile = new File(fileDir+fileNamePdf);
			
			OutputStream out = new java.io.FileOutputStream(pdfFile);
			
			out.write(print.getByteArray());
			
			String nombre = "Reporte de Clasificador de "+clasificador.getDescripcion();
			String descripcion = "Consulta de Totales para Clasificador.";
			
			// Guardamos el archivo generado en como archivo de salida del proceso.
			corrida.addOutputFile(nombre, descripcion, fileDir+fileNamePdf);
			
			// Generamos el reporte pare los clasificadores restantes (usando el principal)
			List<Clasificador> listClasificadorRestante = Clasificador.getListActivosExcluyendoId(clasificador.getId());
			List<Nodo> listNodoRubro = listNodo;
			for(Clasificador cla: listClasificadorRestante){
				// Aqui obtiene lista de BOs
				listNodo = Nodo.getListActivosByClasificador(cla);  

				listNodoArbol = Nodo.obtenerListaArbolConTotales(listNodo, fechaDesde, fechaHasta, listNodoRubro, balance);
				
				listNodoVOOrdenados = (ArrayList<NodoVO>) ListUtilBean.toVO(listNodoArbol, 1,false);

				totalGeneral = 0D;
				Double aRestar = 0D;
				for(NodoVO nodoVO: listNodoVOOrdenados){
					nodoVO.setTotal(NumberUtil.truncate(nodoVO.getTotal(), 2));
					nodoVO.setTotalEjeAct(NumberUtil.truncate(nodoVO.getTotalEjeAct(), 2));
					nodoVO.setTotalEjeVen(NumberUtil.truncate(nodoVO.getTotalEjeVen(), 2));
					if(nodoVO.getEsNodoRaiz()){
						totalGeneral += nodoVO.getTotal();					
					}
					// Para el Clasificador de Procedencia: Calculamos total de 04.02.90 y 04.02.91 para restarlos al totalRecaudado y obtener el subTotal requerido (pedido por el area de Balance)
					if(cla.getId().longValue() == Clasificador.ID_CLA_PROC.longValue() 
							&& ("04.02.90".equals(nodoVO.getClave()) || "04.02.91".equals(nodoVO.getClave()))){
							aRestar += nodoVO.getTotal();							
					}
				}
				// Para el Clasificador de Procedencia: Calculamos el subTotal (Sin Ctas. 04.02.90 y 04.02.91) (pedido por el area de Balance)
				if(cla.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
					subTotal = totalGeneral - aRestar;					
				}				
				
				if(reporteExtra && idClasificador.longValue() == cla.getId().longValue()){
					listNodoVOReporteExtra = listNodoVOOrdenados;
				}
				
				informe = new InformeClasificador();
				
				informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
				informe.setDesClasificador(cla.getDescripcion().toUpperCase());
				informe.setDesEjercicio(desEjercicio);
				informe.setFechaDesde(DateUtil.formatDate(fechaDesde, DateUtil.dd_MM_YYYY_MASK));
				informe.setFechaHasta(DateUtil.formatDate(fechaHasta, DateUtil.dd_MM_YYYY_MASK));
				informe.setClasificadorRubro("false");
				informe.setTotalGeneral(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGeneral, 2)));
				if(cla.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
					informe.setSubTotal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotal, 2)));
					informe.setMostrarSubTotal("true");					
				}
				
				informe.setListNodo(listNodoVOOrdenados);
				if(idBalance != null){
					informe.setNroBalance(idBalance.toString());				
				}
				
				// Generamos el printModel
				print =null;
				
				print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CLASIFICADOR);
				
				print.setNoAplicarTrim(true);
				print.putCabecera("TituloReporte", "Clasificador de "+cla.getDescripcion());
				print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
				print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
				
				print.putCabecera("Usuario", userName);
				print.setData(informe);
				print.setTopeProfundidad(2);
							
				// Archivo pdf a generar
				fileNamePdf = adpRun.getId()+"Clasificador"+cla.getDescripcion()+ userId +".pdf"; 
				pdfFile = new File(fileDir+fileNamePdf);
				
				out = new java.io.FileOutputStream(pdfFile);
				
				out.write(print.getByteArray());
				
				nombre = "Reporte de Clasificador de "+cla.getDescripcion();
				descripcion = "Consulta de Totales para Clasificador.";
				
				// Guardamos el archivo generado en como archivo de salida del proceso.
				corrida.addOutputFile(nombre, descripcion, fileDir+fileNamePdf);				
			}
			
			// Generamos el reporte extra para los filtros seteados.
			if(reporteExtra){
				Clasificador claRepExtra = Clasificador.getById(idClasificador);
				Nodo nodoRepExtra = Nodo.getByIdNull(idNodo);
				
				List<NodoVO> listNodoVOFiltrada = new ArrayList<NodoVO>();
				if(nodoRepExtra != null){
					String claveNodoRepExtra = nodoRepExtra.armarClave();
					for(NodoVO nodo: listNodoVOReporteExtra){
						if(nodo.getClave().startsWith(claveNodoRepExtra))
							listNodoVOFiltrada.add(nodo);
					}
				}else{
					for(NodoVO nodo: listNodoVOReporteExtra){
						if(nodo.getNivel().intValue() <= nivel.intValue())
							listNodoVOFiltrada.add(nodo);
					}
				}
				totalGeneral = 0D;
				subTotal = 0D;
				Double aRestar = 0D;		
				for(NodoVO nodoVO: listNodoVOFiltrada){
					if(nodoVO.getEsNodoRaiz()){
						totalGeneral += nodoVO.getTotal();
						if(claRepExtra.getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()){
							// Calculamos Subtotal Rubros 11 al 42 (pedido por el area de Balance)
							Long codNodoRaiz = null;
							try{ codNodoRaiz = Long.valueOf(nodoVO.getCodigo());}catch(Exception e){}
							if(codNodoRaiz != null && codNodoRaiz >=11 && codNodoRaiz <= 42){
								subTotal += nodoVO.getTotal();
							}							
						}
					}
					// Para el Clasificador de Procedencia: Calculamos total de 04.02.90 y 04.02.91 para restarlos al totalRecaudado y obtener el subTotal requerido (pedido por el area de Balance)
					if(claRepExtra.getId().longValue() == Clasificador.ID_CLA_PROC.longValue() 
							&& ("04.02.90".equals(nodoVO.getClave()) || "04.02.91".equals(nodoVO.getClave()))){
							aRestar += nodoVO.getTotal();							
					}
				}
				// Para el Clasificador de Procedencia: Calculamos el subTotal (Sin Ctas. 04.02.90 y 04.02.91) (pedido por el area de Balance)
				if(claRepExtra.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
					subTotal = totalGeneral - aRestar;					
				}
				
				informe = new InformeClasificador();
				
				informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
				informe.setDesClasificador(claRepExtra.getDescripcion().toUpperCase());
				informe.setDesEjercicio(desEjercicio);
				informe.setFechaDesde(DateUtil.formatDate(fechaDesde, DateUtil.dd_MM_YYYY_MASK));
				informe.setFechaHasta(DateUtil.formatDate(fechaHasta, DateUtil.dd_MM_YYYY_MASK));
				informe.setReporteExtra("true");
				informe.setNivelFiltro(nivel.toString());
				if(nodoRepExtra != null)
					informe.setDesNodoFiltro(nodoRepExtra.getClave()+" - "+nodoRepExtra.getDescripcion());
				else
					informe.setDesNodoFiltro("Todos");
				informe.setTotalGeneral(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGeneral, 2)));
				if(claRepExtra.getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue() || claRepExtra.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
					informe.setSubTotal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotal, 2)));
					informe.setMostrarSubTotal("true");					
				}
				if(claRepExtra.getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue()){					
					informe.setClasificadorRubro("false");
				}
				informe.setListNodo(listNodoVOFiltrada);
			
				if(idBalance != null){
					informe.setNroBalance(idBalance.toString());				
				}
				
				// Generamos el printModel
				print =null;
				
				print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CLASIFICADOR);
				
				print.setNoAplicarTrim(true);
				print.putCabecera("TituloReporte", "Reporte de Clasificador Filtrado");
				print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
				print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
				
				print.putCabecera("Usuario", userName);
				print.setData(informe);
				print.setTopeProfundidad(2);
							
				// Archivo pdf a generar
				fileNamePdf = adpRun.getId()+"ClasificadorFiltrado"+ userId +".pdf"; 
				pdfFile = new File(fileDir+fileNamePdf);
				
				out = new java.io.FileOutputStream(pdfFile);
				
				out.write(print.getByteArray());
				
				nombre = "Reporte de Clasificador Adicional Filtrado";
				descripcion = "Consulta de Totales para Clasificador filtrado.";
				
				// Guardamos el archivo generado en como archivo de salida del proceso.
				corrida.addOutputFile(nombre, descripcion, fileDir+fileNamePdf);		

			}
			
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
