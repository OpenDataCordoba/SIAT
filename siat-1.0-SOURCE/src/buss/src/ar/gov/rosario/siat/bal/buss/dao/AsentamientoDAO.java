//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxConvenio;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AsentamientoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AsentamientoDAO.class);
	
	public AsentamientoDAO(){
		super(Asentamiento.class);
	}
	
	public List<Asentamiento> getListBySearchPage(AsentamientoSearchPage asentamientoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Asentamiento t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AsentamientoSearchPage: " + asentamientoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (asentamientoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Asentamiento excluidos
 		List<AsentamientoVO> listAsentamientoExcluidos = (ArrayList<AsentamientoVO>) asentamientoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAsentamientoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAsentamientoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Ejercicio
 		if(!ModelUtil.isNullOrEmpty(asentamientoSearchPage.getAsentamiento().getEjercicio())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.ejercicio = " +  asentamientoSearchPage.getAsentamiento().getEjercicio().getId();
			flagAnd = true;
		}

		// filtro por Servicio Banco
 		if(!ModelUtil.isNullOrEmpty(asentamientoSearchPage.getAsentamiento().getServicioBanco())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.servicioBanco = " +  asentamientoSearchPage.getAsentamiento().getServicioBanco().getId();
			flagAnd = true;
		}

 		// filtro por EstadoCorrida
 		if(!ModelUtil.isNullOrEmpty(asentamientoSearchPage.getAsentamiento().getCorrida().getEstadoCorrida())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida = " +  asentamientoSearchPage.getAsentamiento().getCorrida().getEstadoCorrida().getId();
			flagAnd = true;
		}

 		// 	 filtro por Fecha Balance Desde
		if (asentamientoSearchPage.getFechaBalanceDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBalance >= TO_DATE('" + 
					DateUtil.formatDate(asentamientoSearchPage.getFechaBalanceDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance Hasta
		if (asentamientoSearchPage.getFechaBalanceHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBalance <= TO_DATE('" + 
					DateUtil.formatDate(asentamientoSearchPage.getFechaBalanceHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// Order By
		queryString += " order by t.fechaBalance DESC, t.id DESC";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Asentamiento> listAsentamiento = (ArrayList<Asentamiento>) executeCountedSearch(queryString, asentamientoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAsentamiento;
	}

	/**
	 *  Genera el Archivo de Planilla (*.cvs) para las Transacciones del Asentamiento. 
	 * 
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesTransaccionesByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<String> listPlanillaName = new ArrayList<String>();
	
		int indiceArchivo = 0;
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"DetalleTransac_"+indiceArchivo+".csv";
		
		listPlanillaName.add(fileName);
		BufferedWriter buffer = this.createEncForReporteTransacciones(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		String queryString = "SELECT t.sistema , t.nroComprobante, t.anioComprobante , t.periodo, t.resto, t.codPago," +
		" t.caja, t.codTr, t.fechaPago, t.importe, t.recargo, t.paquete, t.marcaTr, t.reciboTr, t.fechaBalance, t.formulario " +
		"FROM bal_transaccion t "+
		"WHERE t.idAsentamiento = "+ asentamiento.getId();

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
						
			//SISTEMA, CUENTA, AÑO, PERIODO, RESTO, CP, CAJA, COD TR, FECHA PAGO, IMPORTE, RECARGO, PAQ, MARCA TR, RECIBO, FECHA BALANCE	 		
			// Sistema
			buffer.write(rs.getString(1));
			// Cuenta (NroComprobante)
			buffer.write(", " +  rs.getLong(2));
			// Año
			buffer.write(", " +  rs.getLong(3));
			// Periodo
			buffer.write(", " +  rs.getLong(4));
			// Formulario AFIP
			buffer.write(", " +  rs.getLong(16));
			// Resto
			buffer.write(", " +  rs.getLong(5));
			// Codigo de Pago
			buffer.write(", " +  rs.getLong(6));
			// Caja
			buffer.write(", " + rs.getLong(7));
			// Codigo Tr
			buffer.write(", " +  rs.getLong(8));
			// Fecha de Pago
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(9), DateUtil.ddSMMSYYYY_MASK));
			// Importe
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(10),SiatParam.DEC_IMPORTE_DB));
			// Recargo
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(11),SiatParam.DEC_IMPORTE_DB));
			// Paquete
			buffer.write(", " +  rs.getLong(12));
			// Marca Tr
			buffer.write(", " +  rs.getLong(13));
			// Recibo
			buffer.write(", " +  rs.getLong(14));
			// Fecha de Balance
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(15), DateUtil.ddSMMSYYYY_MASK));
			c++;
			if(c == 65534 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();				
				indiceArchivo++;
				fileName = idAsentamiento+"DetalleTransac_"+indiceArchivo+".csv";
				listPlanillaName.add(fileName);
				buffer = this.createEncForReporteTransacciones(new FileWriter(fileDir+"/"+fileName, false));
				c = 0; // reinicio contador 
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
			buffer.write("No existen registros de Transacciones a asentar"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		buffer.close();

		return listPlanillaName;
	}
	
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReporteTransacciones(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		// --> Creacion del Encabezado del Resultado
		// 									TRANSACCIONES
		//SISTEMA, CUENTA, AÑO, PERIODO, FORMULARIO AFIP, RESTO, CP, CAJA, COD TR, FECHA PAGO, IMPORTE, RECARGO, PAQ, MARCA TR, RECIBO, FECHA BALANCE
		buffer.write("TRANSACCIONES");
		buffer.newLine();
		buffer.write("SISTEMA");
		buffer.write(", CUENTA"); // Solo para el caso de Sistema con Tipo Deuda "Deuda Pura", anioComprobante<>"99" y periodo<>"99"
		buffer.write(", AÑO");
		buffer.write(", PERIODO");
		buffer.write(", FORMULARIO AFIP"); // Solo para el caso de Envios de origen Osiris
		buffer.write(", RESTO");
		buffer.write(", CP");
		buffer.write(", CAJA");
		buffer.write(", COD TR");
		buffer.write(", FECHA PAGO");
		buffer.write(", IMPORTE");
		buffer.write(", RECARGO");
		buffer.write(", PAQ");
		buffer.write(", MARCA TR");
		buffer.write(", RECIBO");
		buffer.write(", FECHA BALANCE");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();
		
		return buffer;
	}
	
	/**
	 *  Genera el Archivo de Planilla (*.cvs) para los Pagos Asentados durante el Asentamiento. 
	 * 
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesPagosAsentadosByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<String> listPlanillaName = new ArrayList<String>();
	
		int indiceArchivo = 0;
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"PagosAsentados_"+indiceArchivo+".csv";
		
		listPlanillaName.add(fileName);
		BufferedWriter buffer = this.createEncForReportePagosAsentados(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		String queryString = "SELECT distinct t.id, t.sistema , t.nroComprobante, t.anioComprobante , t.periodo, t.codPago," +
		" t.caja, t.fechaPago, t.importe, t.recargo, t.paquete, t.fechaBalance " +
		"FROM bal_auxPagDeu p, bal_transaccion t "+ 
		" WHERE (p.idAsentamiento = "+ asentamiento.getId()+" and t.id = p.idTransaccion) ";
		queryString += " UNION SELECT distinct t.id, t.sistema , t.nroComprobante, t.anioComprobante , t.periodo, t.codPago," +
		" t.caja, t.fechaPago, t.importe, t.recargo, t.paquete, t.fechaBalance " +
		"FROM bal_auxPagCuo c, bal_transaccion t "+ 
		" WHERE (c.idAsentamiento = "+ asentamiento.getId()+" and t.id = c.idTransaccion)";
		
		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
						
			//SISTEMA, CUENTA, AÑO, PERIODO, CP, CAJA, FECHA PAGO, IMPORTE, RECARGO, PAQ, FECHA BALANCE	 		
			// Sistema
			buffer.write(rs.getString(2));
			// Cuenta (NroComprobante)
			buffer.write(", " +  rs.getLong(3));
			// Año
			buffer.write(", " +  rs.getLong(4));
			// Periodo
			buffer.write(", " +  rs.getLong(5));
			// Codigo de Pago
			buffer.write(", " +  rs.getLong(6));
			// Caja
			buffer.write(", " + rs.getLong(7));
			// Fecha de Pago
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(8), DateUtil.ddSMMSYYYY_MASK));
			//buffer.write(", " +  rs.getString(8));
			// Importe
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(9), SiatParam.DEC_IMPORTE_DB));
			// Recargo
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(10), SiatParam.DEC_IMPORTE_DB));
			// Paquete
			buffer.write(", " +  rs.getLong(11));
			// Fecha de Balance
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(12), DateUtil.ddSMMSYYYY_MASK));
			//buffer.write(", " +  rs.getString(12));
			c++;
			if(c == 65534 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();				
				indiceArchivo++;
				fileName = idAsentamiento+"PagosAsentados_"+indiceArchivo+".csv";
				listPlanillaName.add(fileName);
				buffer = this.createEncForReportePagosAsentados(new FileWriter(fileDir+"/"+fileName, false));
				c = 0; // reinicio contador 
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
			buffer.write("No existen registros de Pagos Asentados"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		buffer.close();

		return listPlanillaName;
	}

	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReportePagosAsentados(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		// --> Creacion del Encabezado del Resultado
		// 									PAGOS
		//SISTEMA, CUENTA, AÑO, PERIODO, CP, CAJA, FECHA PAGO, IMPORTE, RECARGO, PAQ, FECHA BALANCE
		buffer.write("PAGOS");
		buffer.newLine();
		buffer.write("SISTEMA");
		buffer.write(", CUENTA"); // Solo para el caso de Sistema con Tipo Deuda "Deuda Pura", anioComprobante<>"99" y periodo<>"99"
		buffer.write(", AÑO");
		buffer.write(", PERIODO");
		buffer.write(", CP");
		buffer.write(", CAJA");
		buffer.write(", FECHA PAGO");
		buffer.write(", IMPORTE");
		buffer.write(", RECARGO");
		buffer.write(", PAQ");
		buffer.write(", FECHA BALANCE");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();
		
		return buffer;
	}

	/**
	 *  Genera el Archivo de Planilla (*.cvs) para los Indeterminados generados encontrados el Asentamiento. 
	 * 
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesIndeterminadosByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<String> listPlanillaName = new ArrayList<String>();
	
		int indiceArchivo = 0;
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"Indeterminados_"+indiceArchivo+".csv";
		
		listPlanillaName.add(fileName);
		BufferedWriter buffer = this.createEncForReporteIndeterminados(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		String queryString = "SELECT t.sistema , t.nroComprobante, t.anioComprobante , t.periodo, t.codPago," +
		" t.caja, t.fechaPago, t.importeCobrado, t.recargo, t.paquete, t.fechaBalance , t.codIndeterminado , i.desTipoIndet" +
		" FROM bal_sinIndet t, bal_tipoIndet i "+
		" WHERE t.idAsentamiento = "+ asentamiento.getId()+
		" AND i.id = t.idTipoIndet";

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
						
			//SISTEMA, CUENTA, AÑO, PERIODO, CP, CAJA, FECHA PAGO, IMPORTE, RECARGO, PAQ, FECHA BALANCE, COD INDETERMINADO, DES TIPOINDET	 		
			// Sistema
			buffer.write(rs.getString(1));
			// Cuenta (NroComprobante)
			buffer.write(", " +  rs.getLong(2));
			// Año
			buffer.write(", " +  rs.getLong(3));
			// Periodo
			buffer.write(", " +  rs.getLong(4));
			// Codigo de Pago
			buffer.write(", " +  rs.getLong(5));
			// Caja
			buffer.write(", " + rs.getLong(6));
			// Fecha de Pago
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(7), DateUtil.ddSMMSYYYY_MASK));
			// Importe
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(8), SiatParam.DEC_IMPORTE_DB));
			// Recargo
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(9), SiatParam.DEC_IMPORTE_DB));
			// Paquete
			buffer.write(", " +  rs.getLong(10));
			// Fecha de Balance
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(11), DateUtil.ddSMMSYYYY_MASK));
			// Cod.Indeterminado
			buffer.write(", " +  rs.getString(12));
			// Descripción de Tipo de Indeterminado
			buffer.write(", " +  rs.getString(13));
			
			c++;
			if(c == 65534 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();				
				indiceArchivo++;
				fileName = idAsentamiento+"Indeterminados_"+indiceArchivo+".csv";
				listPlanillaName.add(fileName);
				buffer = this.createEncForReporteIndeterminados(new FileWriter(fileDir+"/"+fileName, false));
				c = 0; // reinicio contador 
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
			buffer.write("No existen registros de Indeterminados"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		buffer.close();

		return listPlanillaName;
	}
	
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReporteIndeterminados(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		// --> Creacion del Encabezado del Resultado
		// 									INDETERMINADOS
		//SISTEMA, CUENTA, AÑO, PERIODO, CP, CAJA, FECHA PAGO, IMPORTE, RECARGO, PAQ, FECHA BALANCE, COD INDETERMINADO
		buffer.write("INDETERMINADOS");
		buffer.newLine();
		buffer.write("SISTEMA");
		buffer.write(", CUENTA"); // Solo para el caso de Sistema con Tipo Deuda "Deuda Pura", anioComprobante<>"99" y periodo<>"99"
		buffer.write(", AÑO");
		buffer.write(", PERIODO");
		buffer.write(", CP");
		buffer.write(", CAJA");
		buffer.write(", FECHA PAGO");
		buffer.write(", IMPORTE");
		buffer.write(", RECARGO");
		buffer.write(", PAQ");
		buffer.write(", FECHA BALANCE");
		buffer.write(", COD INDETERMINADO");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();
		
		return buffer;
	}
	
	/**
	 *  Genera el Archivo de Planilla (*.cvs) para los Saldos A Favor generados durante el Asentamiento. 
	 * 
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesSaldosAFavorByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<String> listPlanillaName = new ArrayList<String>();
	
		int indiceArchivo = 0;
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"SaldosAFavor_"+indiceArchivo+".csv";
		
		listPlanillaName.add(fileName);
		BufferedWriter buffer = this.createEncForReporteSaldosAFavor(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		String queryString = "SELECT t.sistema , t.nroComprobante, t.anioComprobante , t.cuota," +
		" t.fechaPago, t.importePago, t.importeDebPag, t.fechaBalance " +
		" FROM bal_sinSalAFav t"+
		" WHERE t.idAsentamiento = "+ asentamiento.getId();

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
						
			//SISTEMA, CUENTA, AÑO, CUOTA, FECHA PAGO, IMPORTE PAGO, IMPORTE DEBIO PAGAR, FECHA BALANCE	 		
			// Sistema
			buffer.write(rs.getString(1));
			// Cuenta (NroComprobante)
			buffer.write(", " +  rs.getLong(2));
			// Año
			buffer.write(", " +  rs.getLong(3));
			// Cuota
			buffer.write(", " +  rs.getLong(4));
			// Fecha de Pago
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(5), DateUtil.ddSMMSYYYY_MASK));
			// Importe Pago
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(6), SiatParam.DEC_IMPORTE_DB));
			// Importe Debio Pagar
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(7), SiatParam.DEC_IMPORTE_DB));
			// Fecha de Balance
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(8), DateUtil.ddSMMSYYYY_MASK));
			
			c++;
			if(c == 65534 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();				
				indiceArchivo++;
				fileName = idAsentamiento+"SaldosAFavor_"+indiceArchivo+".csv";
				listPlanillaName.add(fileName);
				buffer = this.createEncForReporteSaldosAFavor(new FileWriter(fileDir+"/"+fileName, false));
				c = 0; // reinicio contador 
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
			buffer.write("No existen registros de Saldos a Favor"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		buffer.close();

		return listPlanillaName;
	}
	
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReporteSaldosAFavor(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		// --> Creacion del Encabezado del Resultado
		// 									SALDOS A FAVOR
		//SISTEMA, CUENTA, AÑO, CUOTA, FECHA PAGO, IMPORTE PAGO, IMPORTE DEBIO PAGAR, FECHA BALANCE
		buffer.write("SALDOS A FAVOR");
		buffer.newLine();
		buffer.write("SISTEMA");
		buffer.write(", CUENTA"); // Solo para el caso de Sistema con Tipo Deuda "Deuda Pura", anioComprobante<>"99" y periodo<>"99"
		buffer.write(", AÑO");
		buffer.write(", CUOTA");
		buffer.write(", FECHA PAGO");
		buffer.write(", IMPORTE PAGO");
		buffer.write(", IMPORTE DEBIO PAGAR");
		buffer.write(", FECHA BALANCE");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();
		return buffer;
	}
	
	/**
	 *  Genera el Archivo de Sincronismo (*.txt) para las Partidas.
	 * 
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public String exportSinPartidaByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
	
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"partidas.txt";
		
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileDir+"/"+fileName, false)); 

		//DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// --> Resultado
		boolean resultadoVacio = true;

		// --> Busqueda de Ejercicios Actual
		String queryString = "SELECT t.idAsentamiento , t.mesPago, t.anioPago , p.codPartida, t.marca, "+
		"t.importeEjeAct, t.importeEjeVen, t.fechaBalance, p.preEjeAct, p.preEjeVen " +
		"FROM bal_sinPartida t, bal_partida p "+
		"WHERE t.idAsentamiento = "+ asentamiento.getId()+
		" AND p.id = t.idPartida"+
		" AND t.importeEjeAct > 0";

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();
		
		
		while(rs.next()){
			resultadoVacio = false;

			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(rs.getDouble(6),SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}
			
			// SERBAN, ANIO PAGO, MES PAGO, CODPARTIDA, MARCA, IMPORTEEJEACT, FECHABALANCE	 		
			// Servicio Banco
			buffer.write(asentamiento.getServicioBanco().getCodServicioBanco());
			// Anio Pago
			buffer.write("|" +  rs.getLong(3));
			// Mes Pago
			buffer.write("|" +  rs.getString(2));
			// Codigo de Partida
			buffer.write("|" + rs.getString(9) + StringUtil.completarCerosIzq(String.valueOf(rs.getLong(4)),5));
			// Marca
			buffer.write("|A");
			// Importe Ejercicio Actual
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(6),SiatParam.DEC_IMPORTE_DB));
			// Fecha de Balance
			buffer.write("|" +  DateUtil.formatDate(rs.getDate(8), DateUtil.ddMMYYYY_MASK));
			buffer.write("|");
			// crea una nueva linea
			buffer.newLine();

		} // Fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado
		
		// --> Busqueda de Ejercicios Vencidos 
		queryString = "SELECT t.idAsentamiento , t.mesPago, t.anioPago , p.codPartida, t.marca, "+
		"t.importeEjeAct, t.importeEjeVen, t.fechaBalance , p.preEjeAct, p.preEjeVen " +
		"FROM bal_sinPartida t, bal_partida p "+
		"WHERE t.idAsentamiento = "+ asentamiento.getId()+
		" AND p.id = t.idPartida"+
		" AND t.importeEjeVen > 0";

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();
				
		while(rs.next()){
			resultadoVacio = false;
		
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(rs.getDouble(7),SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			// SERBAN, ANIO PAGO, MES PAGO, CODPARTIDA, MARCA, IMPORTEEJEVEN, FECHABALANCE	 		
			// Servicio Banco
			buffer.write(asentamiento.getServicioBanco().getCodServicioBanco());
			// Anio Pago
			buffer.write("|" +  rs.getLong(3));
			// Mes Pago
			buffer.write("|" +  rs.getString(2));
			// Codigo de Partida
			buffer.write("|" + rs.getString(10)+StringUtil.completarCerosIzq(String.valueOf(rs.getLong(4)),5));
			// Marca
			buffer.write("|V");
			// Importe Ejercicio Vencido
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(7), SiatParam.DEC_IMPORTE_DB));
			// Fecha de Balance
			buffer.write("|" +  DateUtil.formatDate(rs.getDate(8), DateUtil.ddMMYYYY_MASK));
			buffer.write("|");
			// crea una nueva linea
			buffer.newLine();

		} // Fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
		//	buffer.write("No existen registros de Partida para Sincronismo"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName);
		buffer.close();

		// Crea una copia del archivo de sincronismo con el nombre esperado.
		String newFileName = "parti_"+asentamiento.getServicioBanco().getCodServicioBanco();
		DemodaUtil.copyFile(fileDir+"/"+fileName, fileDir+"/"+newFileName);
		
		return fileName;
	}

	/**
	 *  Genera el Archivo de Sincronismo (*.txt) para los Indeterminados.
	 * 
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public String exportSinIndetByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
	
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"indet.txt";
		
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileDir+"/"+fileName, false)); 

		// --> Resultado
		boolean resultadoVacio = true;

		String queryString = "SELECT t.idAsentamiento , t.sistema, t.nroComprobante , t.anioComprobante, t.periodo, "+
		"t.resto, t.codPago, t.caja, t.codTr, t.fechaPago, t.importeCobrado, t.basico, t.calculado, t.indice, t.recargo, " +
		"t.filler, t.paquete, t.marcaTr, t.reciboTr, t.fechaBalance, t.codIndeterminado, p.codPartida " +
		"FROM bal_sinIndet t, bal_partida p "+
		"WHERE t.idAsentamiento = "+ asentamiento.getId()+
		" AND p.id = t.idPartida";

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();
		
		//DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		while(rs.next()){
			resultadoVacio = false;
			
			// SISTEMA, NROCOMPROBANTE, ANIOCOMPROBANTE, PERIODO, IMPORTECOBRADO, BASICO, CALCULADO, INDICE, RECARGO,
			// CODPARTIDA,CODINDETERMINADO, FECHAPAGO, CAJA, PAQUETE, CODPAGO, FECHABALANCE, FILLER, RECIBOTR, 

			// Sistema
			buffer.write(rs.getString(2));
			// NroComprobante
			buffer.write("|" +  rs.getLong(3));
			// AnioComprobante/Periodo
			Long anio = rs.getLong(4);
			Long periodo = rs.getLong(5);
			String anioPeriodo = "";
			
			//Verifica si la transaccion posee cod ref pag y anio y periodo indica el tipo boleta
			if(anio > 0L && anio <=4L){
				anioPeriodo = StringUtil.completarCerosIzq(anio.toString(), 4);
				anioPeriodo += "00";
			}
			//Contempla los casos de recibo donde anio=99 y periodo=99
			else if(periodo == 99 && anio == 99){
				anioPeriodo = "999999"; 
			}
			//Contempla los casos donde las cuotas son mayores a 99
			else if(periodo >=100 || anio < 1000){
				anioPeriodo = StringUtil.completarCerosIzq(periodo.toString(), 6);
			}
			//Contempla deuda con anio y periodo o (cuotas menores a 100 y anio 0000)
			else{
				anioPeriodo  += StringUtil.completarCerosIzq(anio.toString(), 4);
				anioPeriodo  += StringUtil.completarCerosIzq(periodo.toString(), 2); 
			}
			buffer.write("|" +  anioPeriodo);
			// Periodo
			buffer.write("|"); 
			// Importe Cobrado
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(11), SiatParam.DEC_IMPORTE_DB));
			// Basico
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(12), SiatParam.DEC_IMPORTE_DB));
			// Calculado
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(13), SiatParam.DEC_IMPORTE_DB));
			// Indice
			buffer.write("|" +  rs.getLong(14));
			// Recargo
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(15), SiatParam.DEC_IMPORTE_DB));
			// Codigo Partida
			buffer.write("|" +  "9".concat(StringUtil.completarCerosIzq(rs.getString(22).trim(),5)));
			// Codigo Indeterminado
			buffer.write("|" +  rs.getString(21).trim());
			// Fecha de Pago 
			buffer.write("|" +  DateUtil.formatDate(rs.getDate(10), DateUtil.ddMMYYYY_MASK));
			// Caja
			buffer.write("|" +  rs.getLong(8));
			// Paquete
			buffer.write("|" +  rs.getLong(17));
			// Codigo de Pago
			buffer.write("|" +  rs.getLong(7));
			// Fecha de Balance
			buffer.write("|" +  DateUtil.formatDate(rs.getDate(20), DateUtil.ddMMYYYY_MASK));
			// Filler
			if(rs.getString(16)!=null)
				buffer.write("|" +  rs.getString(16));
			else
				buffer.write("|");
			// Recibo Tr
			buffer.write("|" +  rs.getLong(19));
			buffer.write("|");
			// crea una nueva linea
			buffer.newLine();

		} // Fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			//buffer.write("No existen registros de Indeterminado para Sincronismo"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName);
		buffer.close();
		
		// Crea una copia del archivo de sincronismo con el nombre esperado.
		String newFileName = "indet_"+asentamiento.getServicioBanco().getCodServicioBanco();
		DemodaUtil.copyFile(fileDir+"/"+fileName, fileDir+"/"+newFileName);
		
		return fileName;
	}

	
	/**
	 *  Genera el Archivo de Sincronismo (*.txt) para los Saldos A Favor.
	 * 
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public String exportSinSalAFavByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
	
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"salAFav.txt";
		
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileDir+"/"+fileName, false)); 

		// --> Resultado
		boolean resultadoVacio = true;

		String queryString = "SELECT t.idAsentamiento , t.sistema, t.nroComprobante , t.anioComprobante, t.cuota, t.filler_o, "+
		"t.importePago, t.importeDebPag, t.fechaPago, t.fechaBalance " +
		"FROM bal_sinSalAFav t "+
		"WHERE t.idAsentamiento = "+ asentamiento.getId();

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();
		
		//DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		while(rs.next()){
			resultadoVacio = false;
					
			// SISTEMA, NROCOMPROBANTE, ANIOCOMPROBANTE, CUOTA, FILLER_O, IMPORTEPAGO, IMPORTEDEBPAG, FECHAPAGO, FECHABALANCE	 		
			// Sistema
			buffer.write(rs.getString(2));
			// Nro Comprobante
			buffer.write("|" +  rs.getLong(3));
			// Anio Comprobante
			buffer.write("|" +  rs.getLong(4));
			// Cuota
			buffer.write("|" +  rs.getLong(5));
			// Filler O
			buffer.write("|" +  rs.getLong(6));
			// Importe Pago
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(7), SiatParam.DEC_IMPORTE_DB));
			// Importe que Debio Pagar
			buffer.write("|" +  NumberUtil.truncate(rs.getDouble(8), SiatParam.DEC_IMPORTE_DB));
			// Fecha de Pago
			buffer.write("|" +  DateUtil.formatDate(rs.getDate(9), DateUtil.ddMMYYYY_MASK));
			// Fecha de Balance
			buffer.write("|" +  DateUtil.formatDate(rs.getDate(10), DateUtil.ddMMYYYY_MASK));
			buffer.write("|");
			// crea una nueva linea
			buffer.newLine();

		} // Fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			//buffer.write("No existen registros de Saldo A Favor para Sincronismo"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName);
		buffer.close();

		// Crea una copia del archivo de sincronismo con el nombre esperado.
		String newFileName = "saldos_"+asentamiento.getServicioBanco().getCodServicioBanco();
		DemodaUtil.copyFile(fileDir+"/"+fileName, fileDir+"/"+newFileName);

		return fileName;
	}


	/**
	 *  Genera el Archivo de Planilla (*.cvs) para los Convenios para los cuales se enviaron solicitudes de revision
	 *  durante el Asentamiento. 
	 * 	
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesConveniosARevisionByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = SiatHibernateUtil.currentSession();
		
		List<String> listPlanillaName = new ArrayList<String>();
	
		int indiceArchivo = 0;
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"ConveniosARevision.csv";
		
		listPlanillaName.add(fileName);
		BufferedWriter buffer = this.createEncForReporteConveniosARevision(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		String queryString = "from AuxConvenio t "; 		
		queryString += " where t.asentamiento.id = " +asentamiento.getId();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		// Ejecucion de la consulta de resultado
		Query query = session.createQuery(queryString);
		List<AuxConvenio> listAuxConvenio = (ArrayList<AuxConvenio>) query.list();

		for(AuxConvenio auxConvenio: listAuxConvenio){
			resultadoVacio = false;
						
			// CUENTA, TITULAR PRINCIPAL, NRO CONVENIO, AÑO	 		
			// Cuenta
			buffer.write(auxConvenio.getConvenio().getCuenta().getNumeroCuenta());
			// Titular Principal
			buffer.write(", " +  auxConvenio.getConvenio().getCuenta().getDesTitularPrincipal());
			// Nro Convenio
			buffer.write(", " +  auxConvenio.getConvenio().getNroConvenio());
			// Año
			buffer.write(", " +  DateUtil.getAnio(auxConvenio.getConvenio().getFechaFor()));
			
			c++;
			if(c == 65534 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();				
				indiceArchivo++;
				fileName = idAsentamiento+"ConveniosARevision_"+indiceArchivo+".csv";
				listPlanillaName.add(fileName);
				buffer = this.createEncForReporteConveniosARevision(new FileWriter(fileDir+"/"+fileName, false));
				c = 0; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}

		} // Fin del recorrida del ResultSet
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Convenios A Revisión"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		buffer.close();

		return listPlanillaName;
	}
	
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReporteConveniosARevision(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		// --> Creacion del Encabezado del Resultado
		// 									CONVENIOS A REVISION
		// CUENTA, TITULAR PRINCIPAL, NRO CONVENIO, AÑO
		buffer.write("CONVENIOS A REVISION");
		buffer.newLine();
		buffer.write("CUENTA");
		buffer.write(", TITULAR PRINCIPAL");
		buffer.write(", NRO CONVENIO");
		buffer.write(", AÑO");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();
		return buffer;
	}

	
	/**
	 *  Genera el Archivo de Planilla (*.cvs) para los Convenios Cancelados durante el Asentamiento. 
	 * 	
	 * @param asentamiento
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesConveniosCanceladosByAsentamiento(Asentamiento asentamiento,String fileDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = SiatHibernateUtil.currentSession();
		
		List<String> listPlanillaName = new ArrayList<String>();
	
		int indiceArchivo = 0;
		//Genero el archivo de texto
		String idAsentamiento = StringUtil.formatLong(asentamiento.getId());
		String fileName = idAsentamiento+"ConveniosCancelados.csv";
		
		listPlanillaName.add(fileName);
		BufferedWriter buffer = this.createEncForReporteConveniosCancelados(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		String queryString = "select con from PagoDeuda t, Convenio con "; 		
		queryString += " where t.asentamiento.id = " +asentamiento.getId();
		queryString += " and t.tipoPago.id = "+TipoPago.ID_PAGO_BUENO;
		queryString += " and con.id = t.idPago";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		// Ejecucion de la consulta de resultado
		Query query = session.createQuery(queryString);
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Convenio> listConvenio = (ArrayList<Convenio>) query.list();

		for(Convenio convenio: listConvenio){
			resultadoVacio = false;
						
			// CUENTA, TITULAR PRINCIPAL, NRO CONVENIO, AÑO	 		
			// Cuenta
			buffer.write(convenio.getCuenta().getNumeroCuenta());
			// Titular Principal
			buffer.write(", " +  convenio.getCuenta().getDesTitularPrincipal());
			// Nro Convenio
			buffer.write(", " +  convenio.getNroConvenio());
			// Año
			buffer.write(", " +  DateUtil.getAnio(convenio.getFechaFor()));
			
			c++;
			if(c == 65534 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();				
				indiceArchivo++;
				fileName = idAsentamiento+"ConveniosCancelados_"+indiceArchivo+".csv";
				listPlanillaName.add(fileName);
				buffer = this.createEncForReporteConveniosCancelados(new FileWriter(fileDir+"/"+fileName, false));
				c = 0; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}

		} // Fin del recorrida del ResultSet
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Convenios Cancelados"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		buffer.close();

		return listPlanillaName;
	}
	
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReporteConveniosCancelados(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		// --> Creacion del Encabezado del Resultado
		// 									CONVENIOS CANCELADOS
		// CUENTA, TITULAR PRINCIPAL, NRO CONVENIO, AÑO
		buffer.write("CONVENIOS CANCELADOS");
		buffer.newLine();
		buffer.write("CUENTA");
		buffer.write(", TITULAR PRINCIPAL");
		buffer.write(", NRO CONVENIO");
		buffer.write(", AÑO");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();
		return buffer;
	}
	
	/**
	 *  Verifica si existe un Asentamiento para el Servicio Banco pasado con estado "En Preparacion", 
	 *  "En espera comenzar", "Procesando" o "En espera continuar". Retorna true o false.
	 *  (Excluyendo al Asentamiento que realiza la consulta)
	 * @param servicioBanco
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existAsentamientoBySerBanForCreate(ServicioBanco servicioBanco, Asentamiento ase) throws Exception{
		Asentamiento asentamiento;
		String queryString = "from Asentamiento t where t.servicioBanco.id = "+servicioBanco.getId();
		
		queryString += " and (t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_PREPARACION;  
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_ESPERA_COMENZAR;
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_PROCESANDO;
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_ESPERA_CONTINUAR+" )";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		asentamiento = (Asentamiento) query.uniqueResult();	
		
		if(asentamiento != null && (ase.getId()==null || asentamiento.getId().longValue() != ase.getId().longValue()))
			return true;
		else
			return false;		
	}
	
	/**
	 * Devuelve la fecha de del Ultimo Asentamiento procesado con exito.
	 * 
	 * @author Cristian
	 * @return
	 */
	public Date getFechaUltimoAsentamientoExitoso(){
		
		String queryString = "SELECT MAX(t.fechaBalance) FROM Asentamiento t WHERE " +
							 "t.corrida.estadoCorrida.id = " + EstadoCorrida.ID_PROCESADO_CON_EXITO;
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		Date fechaUtlAseExisto = (Date) query.uniqueResult();	
		
		return fechaUtlAseExisto;
		
	}
	
	/**
	 *  Devuelve el Asentamiento asociado al Balance y para el Servicio Banco de ids pasados como parametro.
	 *  
	 * @param idBalance
	 * @param idServicioBanco
	 * @return asentamiento
	 */
	public Asentamiento getByIdBalanceYIdSerBan(Long idBalance, Long idServicioBanco){
		
		String queryString = "FROM Asentamiento t WHERE " +
							 "t.balance.id = "+idBalance+" AND t.servicioBanco.id = "+idServicioBanco;
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		Asentamiento asentamiento = (Asentamiento) query.uniqueResult();	
		
		return asentamiento;
	}
	
	/**
	 * Devuelve los asentamientos que se encuentran en algun estado y servicio banco pasado como parametro.
	 * @param listIds
	 * @return
	 */
	public List<Asentamiento> getListBy(Long[] idsEstadoCorrida, Long[] idsServicioBanco){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString="FROM Asentamiento t WHERE ";
			   queryString+=" t.corrida.estadoCorrida.id IN ("+StringUtil.getStringComaSeparate(idsEstadoCorrida)+")";
			   queryString+=" AND t.servicioBanco.id IN ("+StringUtil.getStringComaSeparate(idsServicioBanco)+")";
				
		Query query = session.createQuery(queryString);
		return query.list();
	}
}

