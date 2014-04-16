//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.iface.model.EmisionExtSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionMasSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class EmisionDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EmisionDAO.class);	
	
	public EmisionDAO() {
		super(Emision.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Emision> getBySearchPage(EmisionMasSearchPage emisionMasSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Emision t ";
	    boolean flagAnd = false;
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EmisionSearchPage: " + emisionMasSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (emisionMasSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro por Recurso
 		RecursoVO recurso = emisionMasSearchPage.getEmision().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

 		// filtro por Tipo de Emision
 		TipoEmisionVO tipoEmision = emisionMasSearchPage.getEmision().getTipoEmision();
 		if (!ModelUtil.isNullOrEmpty(tipoEmision)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoEmision.id = " + tipoEmision.getId();
			flagAnd = true;
		}

  		// filtro por Estado de la corrida
 		EstadoCorridaVO estadoCorridaVO = emisionMasSearchPage.getEmision().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId(); 
			flagAnd = true;
		}
 		
 		// filtro por Fecha Desde
 		Date fechaDesde = emisionMasSearchPage.getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// filtro por Fecha Hasta
 		Date fechaHasta = emisionMasSearchPage.getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
 		
		queryString += " order by t.fechaUltMdf desc, t.fechaEmision desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Emision> listEmision = (ArrayList<Emision>) executeCountedSearch(queryString, emisionMasSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEmision;
	}
	
	@SuppressWarnings("unchecked")
	public List<Emision> getBySearchPage(EmisionExtSearchPage emisionExtSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Emision t ";
	    boolean flagAnd = false;
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EmisionSearchPage: " + emisionExtSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (emisionExtSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro por Recurso
 		RecursoVO recurso = emisionExtSearchPage.getEmision().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

 		// filtro por Tipo de Emision
 		TipoEmisionVO tipoEmision = emisionExtSearchPage.getEmision().getTipoEmision();
 		if (!ModelUtil.isNullOrEmpty(tipoEmision)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoEmision.id = " + tipoEmision.getId(); 
			flagAnd = true;
		}

  		// filtro por Fecha Desde
 		Date fechaDesde = emisionExtSearchPage.getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// filtro por Fecha Hasta
 		Date fechaHasta = emisionExtSearchPage.getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
 		
		queryString += " order by t.fechaUltMdf desc, t.fechaEmision desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Emision> listEmision = (ArrayList<Emision>) executeCountedSearch(queryString, emisionExtSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEmision;
	}

	@SuppressWarnings("unchecked")
	public List<Emision> getBySearchPage(EmisionPuntualSearchPage emisionPuntualSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Emision t ";
	    boolean flagAnd = false;
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EmisionSearchPage: " + emisionPuntualSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (emisionPuntualSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro por Recurso
 		RecursoVO recurso = emisionPuntualSearchPage.getEmision().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

 		// filtro por Tipo de Emision
 		TipoEmisionVO tipoEmision = emisionPuntualSearchPage.getEmision().getTipoEmision();
 		if (!ModelUtil.isNullOrEmpty(tipoEmision)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoEmision.id = " + tipoEmision.getId(); 
			flagAnd = true;
		}

 		// filtro por Cuenta
 		String numeroCuenta = emisionPuntualSearchPage.getEmision().getCuenta().getNumeroCuenta();
 		if (!StringUtil.isNullOrEmpty(numeroCuenta)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'"; 
			flagAnd = true;
		}
 		
  		// filtro por Fecha Desde
 		Date fechaDesde = emisionPuntualSearchPage.getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// filtro por Fecha Hasta
 		Date fechaHasta = emisionPuntualSearchPage.getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
 		
		queryString += " order by t.fechaUltMdf desc, t.fechaEmision desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Emision> listEmision = (ArrayList<Emision>) executeCountedSearch(queryString, emisionPuntualSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEmision;
	}

	public List<String> exportReportesCdMPasoUnoByEmision(Emision emision,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		// Lista de las Planillas generadas 
		List<String> listPlanillaName = new ArrayList<String>();
	
		int numeroArchivo = 1;
		//Genero el archivo de texto
		String idEmision = StringUtil.formatLong(emision.getId());
		String fileName = idEmision+"DetalleDeuda_"+numeroArchivo+".csv";
		
		listPlanillaName.add(fileName);
		
		BufferedWriter buffer = this.createEncForReportesCdMPasoUno(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		// contador de registrosc
		long regCounter = 0;               

		String queryString = "SELECT c.numeroCuenta , aux.periodo, aux.anio, aux.fechaVencimiento, aux.importe, aux.conc1, aux.conc2 " +
			"FROM pad_cuenta c, emi_auxDeuda aux "+
			"WHERE c.id = aux.idCuenta AND aux.idEmision = "+ emision.getId();

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
						
			//NUMERO CUENTA DE TGI, PERIODO, A�O, FECHA VENCIMIENTO, IMPORTE, CAPITAL, INTERES	 		

			//Numero de Cuenta TGI
			buffer.write(rs.getString(1));
			//Periodo 
			buffer.write(", " +  rs.getLong(2));
			// A�o
			buffer.write(", " +  rs.getLong(3));
			// Fecha de Vencimiento
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(4), DateUtil.ddSMMSYYYY_MASK));
			// Importe
			buffer.write(", " +  rs.getDouble(5));
			// Capital
			buffer.write(", " +  rs.getDouble(6));
			// Interes
			buffer.write(", " +  rs.getDouble(7));

			regCounter++;
			if(regCounter == 65534 ){ // incluyendo a las filas del encabezado y considera que regCounter arranca en cero
			
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
				buffer.close();				
				numeroArchivo++;
				fileName = idEmision+"DetalleDeuda_"+numeroArchivo+".csv";
				listPlanillaName.add(fileName);
				buffer = this.createEncForReportesCdMPasoUno(new FileWriter(fileDir+"/"+fileName, false));
				regCounter = 0; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}

		} // Fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Deudas"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
		buffer.close();

		return listPlanillaName;
	}
	
	@SuppressWarnings("unchecked")
	public List<Emision> getBySearchPage(EmisionExternaSearchPage emisionExternaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Emision t ";
	    boolean flagAnd = false;
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EmisionSearchPage: " + emisionExternaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (emisionExternaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro por Recurso
 		RecursoVO recurso = emisionExternaSearchPage.getEmision().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

 		// filtro por Tipo de Emision
 		TipoEmisionVO tipoEmision = emisionExternaSearchPage.getEmision().getTipoEmision();
 		if (!ModelUtil.isNullOrEmpty(tipoEmision)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoEmision.id = " + tipoEmision.getId(); 
			flagAnd = true;
		}

  		// filtro por Estado de la corrida
 		EstadoCorridaVO estadoCorridaVO = emisionExternaSearchPage.getEmision().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId(); 
			flagAnd = true;
		}
 		
 		// filtro por Fecha Desde
 		Date fechaDesde = emisionExternaSearchPage.getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// filtro por Fecha Hasta
 		Date fechaHasta = emisionExternaSearchPage.getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
 		
 		// En la observacion se encuentra el nombre del archivo de emision externa.
 		if (!StringUtil.isNullOrEmpty(emisionExternaSearchPage.getEmision().getObservacion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.observacion = '" + emisionExternaSearchPage.getEmision().getObservacion() + "'";
			
			flagAnd = true;
		}
 		
		queryString += " order by t.fechaUltMdf desc, t.fechaEmision desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Emision> listEmision = (ArrayList<Emision>) executeCountedSearch(queryString, emisionExternaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEmision;
	}
	
	public List<Long> getListCuentaActivasBy(Recurso recurso, Atributo atributo, String atrVal, 
			Date fechaAnalisis) throws Exception {
	
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");
	
		
		if (atributo != null && !StringUtil.isNullOrEmpty(atrVal)) {
			// Determinamos si aplicamos los filtros 
			// por atributo del Objeto Imponible
		
			TipObjImp tipObjImp = recurso.getTipObjImp();
			if (recurso.getTipObjImp() != null) {
				
				log.debug("Filtro por atributo de Objeto Imponible");
				
				if (tipObjImp.esAtributoObjImp(atributo)) {
					TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getByIdAtributo(atributo.getId());
					return PadDAOFactory.getCuentaDAO()
						.getListCuentaActivasBy(recurso, tipObjImpAtr, atrVal, fechaAnalisis);
				}
			}

			// Determinamos si aplicamos los filtros 
			// por atributo de Cuenta
			RecAtrCue recAtrCue = RecAtrCue.getAbiertoByIdRecAtrCue(recurso.getId(), atributo.getId());
			if (recurso.esAtributoCuenta(atributo)) {
				
				log.debug("Filtro por atributo de Cuenta");
				
				return PadDAOFactory.getCuentaDAO()
					.getListCuentaActivasBy(recurso, recAtrCue, atrVal, fechaAnalisis);
			}
		}
	
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");
			
		return PadDAOFactory.getCuentaDAO().getListCuentaActivasBy(recurso, fechaAnalisis); 
	 }
		
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReportesCdMPasoUno(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		
		// --> Creacion del Encabezado del Resultado
		// 						DEUDA GENERADA
		//	NUMERO DE CUENTA TGI, PERIODO, ANIO, FECHA VENCIMIENTO, IMPORTE, CAPITAL, INTERES
		
		buffer.write("NUMERO DE CUENTA TGI"); 
		buffer.write(", PERIODO");
		buffer.write(", A�O");
		buffer.write(", FECHA VENCIMIENTO");
		buffer.write(", IMPORTE");
		buffer.write(", CAPITAL");
		buffer.write(", INTERES");

		// <-- Fin Creacion del Encabezado del Resultado
		
		buffer.newLine();
		
		return buffer;
	}
		
	// A deprecar
	public List<Emision> getBySearchPage(EmisionSearchPage emisionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Emision t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EmisionSearchPage: " + emisionSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (emisionSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro emision excluidos
 		List<EmisionVO> listEmisionExcluidos = (ArrayList<EmisionVO>) emisionSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listEmisionExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listEmisionExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 		
 		// filtro por recurso
 		RecursoVO recurso = emisionSearchPage.getEmision().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

 		// filtro por estado de la corrida
 		EstadoCorridaVO estadoCorridaVO = emisionSearchPage.getEmision().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId(); 
			flagAnd = true;
		}
 		
 		// filtro por fecha Desde
 		Date fechaDesde = emisionSearchPage.getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// filtro por fecha Hasta
 		Date fechaHasta = emisionSearchPage.getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
 		
 		// filtro por Tipo de Emision
 		TipoEmisionVO tipoEmision = emisionSearchPage.getEmision().getTipoEmision();
 		if (!ModelUtil.isNullOrEmpty(tipoEmision)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoEmision.id = " + tipoEmision.getId(); 
			flagAnd = true;
		}

		queryString += " order by t.fechaUltMdf desc, t.fechaEmision desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Emision> listEmision = (ArrayList<Emision>) executeCountedSearch(queryString, emisionSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEmision;
	}

	
	/**
	 *  Verifica si existe alguna corrida de Emision para el recurso indicado en ejecucion o en espera de continuar. 
	 *  
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existeEmisionEnEjecucion(Recurso recurso) throws Exception{
		Emision emision;
		String queryString = "from Emision t where ";
		queryString += " t.recurso.id = "+recurso.getId();
		queryString += " and (t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_PROCESANDO;
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_ESPERA_CONTINUAR+" )";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		emision = (Emision) query.uniqueResult();	
		
		if(emision != null)
			return true;
		else
			return false;		
	}

}
