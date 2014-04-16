//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.gde.buss.bean.Anulacion;
import ar.gov.rosario.siat.gde.buss.bean.AuxDeudaAnuladaReport;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;

public class AnulacionDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AnulacionDAO.class);	
	
	private static long migId = -1;
	
	public AnulacionDAO() {
		super(Anulacion.class);
	}
	
/*	public List<Anulacion> getBySearchPage(AnulacionSearchPage anulacionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Anulacion t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AnulacionSearchPage: " + anulacionSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (anulacionSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro anulacion excluidos
 		List<AnulacionVO> listAnulacionExcluidos = (List<AnulacionVO>) anulacionSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAnulacionExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAnulacionExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(anulacionSearchPage.getAnulacion().getCodAnulacion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codAnulacion)) like '%" + 
				StringUtil.escaparUpper(anulacionSearchPage.getAnulacion().getCodAnulacion()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(anulacionSearchPage.getAnulacion().getDesAnulacion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desAnulacion)) like '%" + 
				StringUtil.escaparUpper(anulacionSearchPage.getAnulacion().getDesAnulacion()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codAnulacion ";
		* /
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Anulacion> listAnulacion = (ArrayList<Anulacion>) executeCountedSearch(queryString, anulacionSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAnulacion;
	}*/

	
	 /**
	  *  Devuelve el proximo valor de id a asignar. 
	  *  Se inicializa obteniendo el ultimo id asignado en el archivo de migracion con datos pasados como parametro
	  *  y luego en cada llamada incrementa el valor.
	  * 
	  * @return long - el proximo id a asignar.
	  * @throws Exception
	  */
	 public long getNextId(String path, String nameFile) throws Exception{
		 // Si migId==-1 buscar MaxId en el archivo de load. Si migId!=-1, migId++ y retornar migId;
		 if(migId==-1){
				 migId = this.getLastId(path, nameFile)+1;
		 }else{
			 migId++;
		 }
		 
		 return migId;
	 }


	 /**
	  *  Inserta una linea con los datos de la Anulacion para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param anulacion, output - Registro de Anulacion de Deuda a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(Anulacion o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|fechaanulacion|idmotanudeu|iddeuda|usuario|fechaultmdf|estado
		 // Nueva Estructura a partir de 22-10-2008:
		 // id|fechaanulacion|idmotanudeu|iddeuda|idRecurso|idViaDedua|usuario|fechaultmdf|estado
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaAnulacion(), "yyyy-MM-dd"));
		 line.append("|");
		 line.append(o.getMotAnuDeu().getId());		 
		 line.append("|");
		 line.append(o.getIdDeuda());		 
		 line.append("|");
		 line.append(o.getRecurso().getId());
		 line.append("|");
		 line.append(o.getViaDeuda().getId());
		 line.append("|");		 
		 line.append(DemodaUtil.currentUserContext().getUserName());
		 line.append("|");
		 line.append("2010-01-01 00:00:00");
		 line.append("|");
		 line.append("1");
		 line.append("|");
	      
		 output.addline(line.toString());
		 
		 // Seteamos el id generado en el bean.
		 o.setId(id);
	
		 return id;
	 }

	 /**
	  * Devuelve anulacion en forma paginada 
	  * 
	  * @param skip
	  * @param first
	  * @return
	  * @throws Exception
	  */
	 public List<Object> getListAnulacionForFix(Long skip, Long first) throws Exception {
		 
		 	String queryString = "SELECT SKIP " + skip + " FIRST " + first + " anu.id FROM gde_anulacion anu";
			
		 	log.debug("queryString: " + queryString);
		 	Session session = SiatHibernateUtil.currentSession();

			Query query = session.createSQLQuery(queryString);
			
			return (ArrayList<Object>) query.list(); 
	 }
	 
	
	public AuxDeudaAnuladaReport generarPdfForReport(AuxDeudaAnuladaReport auxDeudaAnuladaReport) throws Exception{
		log.debug("generarPDF4Report - enter");
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileName = idCorrida+"_ReporteDeudaAnulada_"+ auxDeudaAnuladaReport.getUserId();

		PlanillaVO planilla = new PlanillaVO();
		ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			

		// Genera la tabla de filtros de la busqueda
		FilaVO filaDeCabecera = new FilaVO();		
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getRecurso().getId()!=null?auxDeudaAnuladaReport.getRecurso().getDesRecurso():"Todos" , "Recurso", "Recurso"));
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getViaDeuda().getId()!=null?auxDeudaAnuladaReport.getViaDeuda().getDesViaDeuda():"Todos", "Via Deuda", "Via Deuda"));
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getFechaDesdeView(), "fechaDesde", "Fecha Desde"));
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getFechaHastaView(), "fechaHasta", "Fecha Hasta"));
		TablaVO tablaFiltros = new TablaVO("FiltrosAplicados");
		tablaFiltros.add(filaDeCabecera);
		contenedorVO.setTablaCabecera(tablaFiltros);
		
		long totalAnulaciones = countAnulaciones(auxDeudaAnuladaReport.getRecurso(), auxDeudaAnuladaReport.getViaDeuda(), 
												         auxDeudaAnuladaReport.getFechaDesde(), auxDeudaAnuladaReport.getFechaHasta());
		
		TablaVO tablaPie = new TablaVO("TotalAnulaciones");
		tablaPie.setFilaTitulo(new FilaVO("Total General"));
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total de Anulaciones: "));
		filaPie.add(new CeldaVO("" + totalAnulaciones));
		
		tablaPie.setFilaCabecera(filaPie);
		contenedorVO.add(tablaPie);
		
		log.debug("Total de Anulaciones: " + totalAnulaciones);
		
		log.debug("Totales por Recurso: ");
		tablaPie = new TablaVO("TotalPorRecurso");
		tablaPie.setFilaTitulo(new FilaVO("Totales por Recurso"));
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Recurso"));
		filaPie.add(new CeldaVO("Total"));
		tablaPie.setFilaCabecera(filaPie);
		
		
		List<Object[]> listPorRecurso =  countPorRecurso(auxDeudaAnuladaReport.getRecurso(), auxDeudaAnuladaReport.getViaDeuda(), 
		         auxDeudaAnuladaReport.getFechaDesde(), auxDeudaAnuladaReport.getFechaHasta());
		
	 	for (Object[] arr:listPorRecurso){
	 		Long idRecurso = (new Long((Integer)arr[0]));
	 		String totalPorRecurso = "" + arr[1].toString();
	 			
	 		Recurso recurso = Recurso.getById(idRecurso);
	 		
	 		filaPie = new FilaVO();
			filaPie.add(new CeldaVO(recurso.getDesRecurso() + ": "));
			filaPie.add(new CeldaVO(totalPorRecurso));
			tablaPie.add(filaPie);
	 	}
		
		contenedorVO.add(tablaPie);
		
		
		log.debug("Totales por Vía: ");
		tablaPie = new TablaVO("TotalPorVia");
		tablaPie.setFilaTitulo(new FilaVO("Totales por Vía"));
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Vía"));
		filaPie.add(new CeldaVO("Total"));
		tablaPie.setFilaCabecera(filaPie);
		
		List<Object[]> listPorVia =  countPorVia(auxDeudaAnuladaReport.getRecurso(), auxDeudaAnuladaReport.getViaDeuda(), 
		         auxDeudaAnuladaReport.getFechaDesde(), auxDeudaAnuladaReport.getFechaHasta());
			
	 	for (Object[] arr:listPorVia){

	 		Long idViaDeuda = (new Long((Integer)arr[0]));
	 		String totalPorVia = "" + arr[1].toString();
	 			
	 		ViaDeuda viaDeuda = ViaDeuda.getById(idViaDeuda);
	 		
	 		filaPie = new FilaVO();
			filaPie.add(new CeldaVO(viaDeuda.getDesViaDeuda() + ": "));
			filaPie.add(new CeldaVO(totalPorVia));
			tablaPie.add(filaPie);
	 	}
		contenedorVO.add(tablaPie);
		
		
		log.debug("Totales por Motivo: ");
		tablaPie = new TablaVO("TotalPorMotivo");
		tablaPie.setFilaTitulo(new FilaVO("Totales por Motivo"));
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Motivo"));
		filaPie.add(new CeldaVO("Total"));
		tablaPie.setFilaCabecera(filaPie);
		
		List<Object[]> listPorMotivo =  countPorMotAnuDeu(auxDeudaAnuladaReport.getRecurso(), auxDeudaAnuladaReport.getViaDeuda(), 
		         auxDeudaAnuladaReport.getFechaDesde(), auxDeudaAnuladaReport.getFechaHasta());
		
	 	for (Object[] arr:listPorMotivo){
	 		
	 		Long idMotAnuDeu = (new Long((Integer)arr[0]));
	 		String totalPorMotivo = "" + arr[1].toString();
	 			
	 		MotAnuDeu motAnuDeu = MotAnuDeu.getById(idMotAnuDeu);
	 		
	 		filaPie = new FilaVO();
			filaPie.add(new CeldaVO(motAnuDeu.getDesMotAnuDeu() + ": "));
			filaPie.add(new CeldaVO(totalPorMotivo));
			tablaPie.add(filaPie);
	 	}

		contenedorVO.add(tablaPie);
		
		// Generacion del PrintModel		
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(FormatoSalida.PDF.getId());
		printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
		printModel.cargarXsl("/mnt/publico/general/reportes/db/REPORTE_GENERICO.xsl", PrintModel.RENDER_PDF);
		printModel.setTopeProfundidad(5);
		printModel.setData(contenedorVO);
		
		printModel.putCabecera("TituloReporte", "Reporte de Anulacion Deuda");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxDeudaAnuladaReport.getUserName());
							
		// Genera el PDF
		String fileNamePdf = fileName + ".pdf";			
		byte[] byteStream = printModel.getByteArray();
		FileOutputStream gesJudReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
		gesJudReportFile.write(byteStream);
		gesJudReportFile.close();
		
		// Setea en el adapter, los datos del archivo generado
		planilla.setFileName(fileDir+"/"+fileNamePdf);
		planilla.setDescripcion("Reporte de Anulacion Deuda");
		auxDeudaAnuladaReport.setReporteGenerado(planilla);
		
		log.debug("generarPDF4Report - exit");
		return auxDeudaAnuladaReport;
	}

	public Long countAnulaciones(Recurso recurso, ViaDeuda viaDeuda, Date fechaDesde, Date fechaHasta){
		
		boolean flagAnd = false; 
	    String  queryString = "SELECT COUNT(*) FROM Anulacion anulacion ";
	    	
	    if(recurso != null && recurso.getId() != null){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " anulacion.recurso.id = "+ recurso.getId();
	    	flagAnd = true;
	    }
	    
		if (viaDeuda != null && viaDeuda.getId() != null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " anulacion.viaDeuda.id = "+ viaDeuda.getId();
 			flagAnd = true; 
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " anulacion.fechaAnulacion >= :fechaDesde AND  anulacion.fechaAnulacion <= :fechaHasta";
	    
	    Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
					  .setDate("fechaDesde", fechaDesde)
					  .setDate("fechaHasta", fechaHasta);
		
		Long contAnulaciones = (Long) query.uniqueResult();
		
		return contAnulaciones;
		
	}
	
	
	public List<Object[]> countPorRecurso(Recurso recurso, ViaDeuda viaDeuda, Date fechaDesde, Date fechaHasta){
		
		boolean flagAnd = false; 
	    String  queryString = "SELECT DISTINCT anulacion.idRecurso, COUNT(anulacion.idRecurso) as cant FROM gde_anulacion anulacion ";
	    	
	    if(recurso != null && recurso.getId() != null){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " anulacion.idRecurso = "+ recurso.getId();
	    	flagAnd = true;
	    }
	    
		if (viaDeuda != null && viaDeuda.getId() != null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " anulacion.idViaDeuda = "+ viaDeuda.getId();
 			flagAnd = true; 
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " anulacion.fechaAnulacion >= :fechaDesde AND  anulacion.fechaAnulacion <= :fechaHasta";
		queryString += " GROUP BY anulacion.idRecurso";
		
		log.debug("countPorRecurso: " + queryString);
		
	    Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString)//.addEntity("anulacion", Anulacion.class)
					  .setDate("fechaDesde", fechaDesde)
					  .setDate("fechaHasta", fechaHasta);
		
		List<Object[]> listCountPorRecurso = query.list();
		
		return listCountPorRecurso;
	}
	
	
	public List<Object[]> countPorVia(Recurso recurso, ViaDeuda viaDeuda, Date fechaDesde, Date fechaHasta){
		
		boolean flagAnd = false; 
	    String  queryString = "SELECT DISTINCT anulacion.idViaDeuda, COUNT(anulacion.idViaDeuda) as cant FROM gde_anulacion anulacion ";
	    	
	    if(recurso != null && recurso.getId() != null){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " anulacion.idRecurso = "+ recurso.getId();
	    	flagAnd = true;
	    }
	    
		if (viaDeuda != null && viaDeuda.getId() != null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " anulacion.idViaDeuda = "+ viaDeuda.getId();
 			flagAnd = true; 
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " anulacion.fechaAnulacion >= :fechaDesde AND  anulacion.fechaAnulacion <= :fechaHasta";
		queryString += " GROUP BY anulacion.idViaDeuda";
		
		log.debug("countPorVia: " + queryString);
		
	    Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString)//.addEntity("anulacion", Anulacion.class)
					  .setDate("fechaDesde", fechaDesde)
					  .setDate("fechaHasta", fechaHasta);
		
		List<Object[]> listCountPorVia = query.list();
		
		return listCountPorVia;
	}
	
	public List<Object[]> countPorMotAnuDeu(Recurso recurso, ViaDeuda viaDeuda, Date fechaDesde, Date fechaHasta){
		
		boolean flagAnd = false; 
	    String  queryString = "SELECT DISTINCT anulacion.idMotAnuDeu, COUNT(anulacion.idMotAnuDeu) as cant FROM gde_anulacion anulacion ";
	    	
	    if(recurso != null && recurso.getId() != null){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " anulacion.idrecurso = "+ recurso.getId();
	    	flagAnd = true;
	    }
	    
		if (viaDeuda != null && viaDeuda.getId() != null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " anulacion.idViaDeuda = "+ viaDeuda.getId();
 			flagAnd = true; 
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " anulacion.fechaAnulacion >= :fechaDesde AND  anulacion.fechaAnulacion <= :fechaHasta";
		queryString += " GROUP BY anulacion.idMotAnuDeu";
		
	    Session session = SiatHibernateUtil.currentSession();
	    
	    log.debug("countPorMotivo: " + queryString);
	    
		Query query = session.createSQLQuery(queryString)//.addEntity("anulacion", Anulacion.class)
					  .setDate("fechaDesde", fechaDesde)
					  .setDate("fechaHasta", fechaHasta);
		
		List<Object[]> listCountPorMotAnuDeu = query.list();
		
		return listCountPorMotAnuDeu;
	}
	
	@Deprecated 
	public AuxDeudaAnuladaReport generarPdfForReportAnterior(AuxDeudaAnuladaReport auxDeudaAnuladaReport) throws Exception{
		log.debug("generarPDF4Report - enter");
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileName = idCorrida+"_ReporteDeudaAnulada_"+ auxDeudaAnuladaReport.getUserId();

		PlanillaVO planilla = new PlanillaVO();
		ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			

		// Genera la tabla de filtros de la busqueda
		FilaVO filaDeCabecera = new FilaVO();		
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getRecurso().getId()!=null?auxDeudaAnuladaReport.getRecurso().getDesRecurso():"Todos" , "Recurso", "Recurso"));
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getViaDeuda().getId()!=null?auxDeudaAnuladaReport.getViaDeuda().getDesViaDeuda():"Todos", "Via Deuda", "Via Deuda"));
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getFechaDesdeView(), "fechaDesde", "Fecha Desde"));
		filaDeCabecera.add(new CeldaVO(auxDeudaAnuladaReport.getFechaHastaView(), "fechaHasta", "Fecha Hasta"));
		TablaVO tablaFiltros = new TablaVO("FiltrosAplicados");
		tablaFiltros.add(filaDeCabecera);
		contenedorVO.setTablaCabecera(tablaFiltros);
		
		List<Anulacion> listAnulacion = getListAnulacion(auxDeudaAnuladaReport.getRecurso(), auxDeudaAnuladaReport.getViaDeuda(), 
												         auxDeudaAnuladaReport.getFechaDesde(), auxDeudaAnuladaReport.getFechaHasta());
		
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Fecha"));
		filaCabecera.add(new CeldaVO("Recurso"));
		filaCabecera.add(new CeldaVO("Cuenta"));
		filaCabecera.add(new CeldaVO("Vía"));
		filaCabecera.add(new CeldaVO("Período"));
		filaCabecera.add(new CeldaVO("Motivo"));
		filaCabecera.add(new CeldaVO("Observación"));
		filaCabecera.add(new CeldaVO("Usuario"));
		
		
		TablaVO tablacontenido = new TablaVO("Contenido");
		tablacontenido.setFilaCabecera(filaCabecera);
		HashMap<Long, String> mapCuentas = new HashMap<Long, String>(); 
		
		long totalAnulaciones = 0L;
		HashMap<String, Long> mapTotalesRecurso = new HashMap<String, Long>(); 
		HashMap<String, Long> mapTotalesVia = new HashMap<String, Long>();
		HashMap<String, Long> mapTotalesMotivo = new HashMap<String, Long>();
		
		for (Anulacion anulacion:listAnulacion){
			
			FilaVO filaContenido = new FilaVO();
			// Fecha
			filaContenido.add(new CeldaVO(DateUtil.formatDate(anulacion.getFechaAnulacion(), DateUtil.ddSMMSYYYY_MASK)));
			
			String desRecurso = anulacion.getRecurso().getDesRecurso();
			// Recurso
			filaContenido.add(new CeldaVO(desRecurso));
			
			Deuda deuda = Deuda.getById(anulacion.getIdDeuda());
			String nroCuenta = "No encontrada";
			String periodoDeuda = "No encontrado";
			
			if (deuda != null){
				if (!mapCuentas.containsKey(deuda.getIdCuenta())){
					mapCuentas.put(deuda.getIdCuenta(), deuda.getCuenta().getNumeroCuenta());
				}
				nroCuenta = mapCuentas.get(deuda.getIdCuenta());
				periodoDeuda = deuda.getStrPeriodo();
			}
			// Cuenta
			filaContenido.add(new CeldaVO(nroCuenta));
			// Via
			String viaDeuda = anulacion.getViaDeuda().getDesViaDeuda();
			filaContenido.add(new CeldaVO(viaDeuda));
			// Periodo
			filaContenido.add(new CeldaVO(periodoDeuda));
			// Motivo Anulacion
			String desMotivo = anulacion.getMotAnuDeu().getDesMotAnuDeu();
			filaContenido.add(new CeldaVO(desMotivo));
			// Observacion
			filaContenido.add(new CeldaVO(anulacion.getObservacion()));
			// Usuario
			filaContenido.add(new CeldaVO(anulacion.getUsuario()));
			
			// Total de anulaciones
			totalAnulaciones ++;
			
			// Totales por recurso
			if (!mapTotalesRecurso.containsKey(desRecurso)){
				mapTotalesRecurso.put(desRecurso, 1L);
			} else {				
				mapTotalesRecurso.put(desRecurso, (Long) mapTotalesRecurso.get(desRecurso) + 1);				
			}
			
			// Totales por via			
			if (!mapTotalesVia.containsKey(viaDeuda)){
				mapTotalesVia.put(viaDeuda, 1L);
			} else {				
				mapTotalesVia.put(viaDeuda, (Long) mapTotalesVia.get(viaDeuda) + 1);				
			}
			
			// Totales por Motivo
			if (!mapTotalesMotivo.containsKey(desMotivo)){
				mapTotalesMotivo.put(desMotivo, 1L);
			} else {				
				mapTotalesMotivo.put(desMotivo, (Long) mapTotalesMotivo.get(desMotivo) + 1);				
			}
			
			tablacontenido.add(filaContenido); 
		}
		
		contenedorVO.add(tablacontenido);
		
		TablaVO tablaPie = new TablaVO("TotalAnulaciones");
		tablaPie.setFilaTitulo(new FilaVO("Total General"));
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total de Anulaciones: "));
		filaPie.add(new CeldaVO("" + totalAnulaciones));
		
		tablaPie.setFilaCabecera(filaPie);
		contenedorVO.add(tablaPie);
		
		
		log.debug("Total de Anulaciones: " + totalAnulaciones);
		
		log.debug("Totales por Recurso: ");
		tablaPie = new TablaVO("TotalPorRecurso");
		tablaPie.setFilaTitulo(new FilaVO("Totales por Recurso"));
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Recurso"));
		filaPie.add(new CeldaVO("Total"));
		tablaPie.setFilaCabecera(filaPie);
		
		 	Set<String> totRecKey = mapTotalesRecurso.keySet();
		 	for (String tk:totRecKey){
		 		log.debug("		" + tk + ": " + mapTotalesRecurso.get(tk));
		 		
		 		filaPie = new FilaVO();
				filaPie.add(new CeldaVO(tk + ": "));
				filaPie.add(new CeldaVO("" + mapTotalesRecurso.get(tk)));
				tablaPie.add(filaPie);
		 	}
		
		contenedorVO.add(tablaPie);
		
		
		log.debug("Totales por Vía: ");
		tablaPie = new TablaVO("TotalPorVia");
		tablaPie.setFilaTitulo(new FilaVO("Totales por Vía"));
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Vía"));
		filaPie.add(new CeldaVO("Total"));
		tablaPie.setFilaCabecera(filaPie);
		
			Set<String> totViaKey = mapTotalesVia.keySet();
		 	for (String vk:totViaKey){
		 		log.debug("		" + vk + ": " + mapTotalesVia.get(vk));
		 		
		 		filaPie = new FilaVO();
				filaPie.add(new CeldaVO(vk + ": "));
				filaPie.add(new CeldaVO("" + mapTotalesVia.get(vk)));
				tablaPie.add(filaPie);
		 	}
		contenedorVO.add(tablaPie);
		
		
		log.debug("Totales por Motivo: ");
		tablaPie = new TablaVO("TotalPorMotivo");
		tablaPie.setFilaTitulo(new FilaVO("Totales por Motivo"));
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Motivo"));
		filaPie.add(new CeldaVO("Total"));
		tablaPie.setFilaCabecera(filaPie);
		
			Set<String> totMotKey = mapTotalesMotivo.keySet();
		 	for (String mk:totMotKey){
		 		log.debug("		" + mk + ": " + mapTotalesMotivo.get(mk));
		 		
		 		filaPie = new FilaVO();
				filaPie.add(new CeldaVO(mk + ": "));
				filaPie.add(new CeldaVO("" + mapTotalesMotivo.get(mk)));
				tablaPie.add(filaPie);
		 	}

		contenedorVO.add(tablaPie);
		
		// Generacion del PrintModel		
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(FormatoSalida.PDF.getId());
		printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
		printModel.cargarXsl("/mnt/publico/general/reportes/db/REPORTE_GENERICO.xsl", PrintModel.RENDER_PDF);
		printModel.setTopeProfundidad(5);
		printModel.setData(contenedorVO);
		
		printModel.putCabecera("TituloReporte", "Reporte de Anulacion Deuda");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxDeudaAnuladaReport.getUserName());
							
		// Genera el PDF
		String fileNamePdf = fileName + ".pdf";			
		byte[] byteStream = printModel.getByteArray();
		FileOutputStream gesJudReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
		gesJudReportFile.write(byteStream);
		gesJudReportFile.close();
		
		// Setea en el adapter, los datos del archivo generado
		planilla.setFileName(fileDir+"/"+fileNamePdf);
		planilla.setDescripcion("Reporte de Anulacion Deuda");
		auxDeudaAnuladaReport.setReporteGenerado(planilla);
		
		log.debug("generarPDF4Report - exit");
		return auxDeudaAnuladaReport;
	}
	
	@Deprecated
	public List<Anulacion> getListAnulacion(Recurso recurso, ViaDeuda viaDeuda, Date fechaDesde, Date fechaHasta){
	
		boolean flagAnd = false; 
	    String  queryString = "FROM Anulacion anulacion ";
	    	
	    if(recurso != null && recurso.getId() != null){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " anulacion.recurso.id = "+ recurso.getId();
	    	flagAnd = true;
	    }
	    
		if (viaDeuda != null && viaDeuda.getId() != null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " anulacion.viaDeuda.id = "+ viaDeuda.getId();
 			flagAnd = true; 
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " anulacion.fechaAnulacion >= :fechaDesde AND  anulacion.fechaAnulacion <= :fechaHasta";
	    
	    Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
					  .setDate("fechaDesde", fechaDesde)
					  .setDate("fechaHasta", fechaHasta);
		
		List<Anulacion> listAnulacion = (ArrayList<Anulacion>) query.list();
		
		return listAnulacion;
		
	}
	
	/**
	 * Obtiene el registro de anulacion para la deuda de id pasado como parametro
	 * 
	 * @param idDeuda
	 * @return
	 * @throws Exception
	 */
	public Anulacion getByIdDeuda(Long idDeuda) throws Exception{
		Anulacion anulacion = null;
		String queryString = "from Anulacion t where t.idDeuda = "+idDeuda;
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		
		anulacion = (Anulacion) query.uniqueResult();	
		
		return anulacion;
	}
	
}
