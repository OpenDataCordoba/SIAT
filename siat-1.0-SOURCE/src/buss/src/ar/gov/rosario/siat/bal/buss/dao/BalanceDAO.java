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
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.DetallePago;
import ar.gov.rosario.siat.bal.buss.bean.EstTranAfip;
import ar.gov.rosario.siat.bal.iface.model.BalanceSearchPage;
import ar.gov.rosario.siat.bal.iface.model.BalanceVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.Estado;

@SuppressWarnings("unchecked")
public class BalanceDAO extends GenericDAO {

	private Log log = LogFactory.getLog(BalanceDAO.class);
	
	public BalanceDAO(){
		super(Balance.class);
	}
	
	public List<Balance> getListBySearchPage(BalanceSearchPage balanceSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Balance t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del BalanceSearchPage: " + balanceSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (balanceSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Balance excluidos
 		List<BalanceVO> listBalanceExcluidos = (ArrayList<BalanceVO>) balanceSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listBalanceExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listBalanceExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Ejercicio
 		if(!ModelUtil.isNullOrEmpty(balanceSearchPage.getBalance().getEjercicio())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.ejercicio = " +  balanceSearchPage.getBalance().getEjercicio().getId();
			flagAnd = true;
		}

 		// filtro por EstadoCorrida
 		if(!ModelUtil.isNullOrEmpty(balanceSearchPage.getBalance().getCorrida().getEstadoCorrida())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida = " +  balanceSearchPage.getBalance().getCorrida().getEstadoCorrida().getId();
			flagAnd = true;
		}

 		// 	 filtro por Fecha Balance Desde
		if (balanceSearchPage.getFechaBalanceDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBalance >= TO_DATE('" + 
					DateUtil.formatDate(balanceSearchPage.getFechaBalanceDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance Hasta
		if (balanceSearchPage.getFechaBalanceHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBalance <= TO_DATE('" + 
					DateUtil.formatDate(balanceSearchPage.getFechaBalanceHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// Order By
		queryString += " order by t.fechaBalance DESC, t.id DESC";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Balance> listBalance = (ArrayList<Balance>) executeCountedSearch(queryString, balanceSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listBalance;
	}


	/**
	 *  Genera el Archivo de Planilla (*.cvs) para los Indeterminados generados en los Asentamientos. 
	 * 
	 * @param balance
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesIndeterminadosByBalance(Balance balance,String fileDir) throws Exception{
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
		String idBalance = balance.getId().toString();
		String fileName = idBalance+"-"+AdpRun.currentRun().getPasoActual()+"Indeterminados_"+indiceArchivo+".csv";
		
		listPlanillaName.add(fileName);
		BufferedWriter buffer = this.createEncForReporteIndeterminados(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		String queryString = "SELECT t.sistema , t.nroComprobante, t.clave, t.codPago," +
		" t.caja, t.fechaPago, t.importeCobrado, t.recargo, t.paquete, t.fechaBalance , t.codIndet " +
		" FROM bal_indetBal t WHERE t.idBalance = "+ balance.getId();

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// Ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
						
			//SISTEMA, CUENTA, CLAVE, CP, CAJA, FECHA PAGO, IMPORTE, RECARGO, PAQ, FECHA BALANCE, COD INDET,
			// Sistema
			buffer.write(rs.getString(1));
			// Cuenta (NroComprobante)
			buffer.write(", " +  rs.getLong(2));
			// Clave
			buffer.write(", " +  rs.getString(3));
			// Codigo de Pago
			buffer.write(", " +  rs.getLong(4));
			// Caja
			buffer.write(", " + rs.getLong(5));
			// Fecha de Pago
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(6), DateUtil.ddSMMSYYYY_MASK));
			// Importe
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(7), SiatParam.DEC_IMPORTE_DB));
			// Recargo
			buffer.write(", " +  NumberUtil.truncate(rs.getDouble(8), SiatParam.DEC_IMPORTE_DB));
			// Paquete
			buffer.write(", " +  rs.getLong(9));
			// Fecha de Balance
			buffer.write(", " +  DateUtil.formatDate(rs.getDate(10), DateUtil.ddSMMSYYYY_MASK));
			// Cod.Indeterminado
			buffer.write(", " +  rs.getString(11));
			
			c++;
			if(c == 65534 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();				
				indiceArchivo++;
				fileName = idBalance+"-"+AdpRun.currentRun().getPasoActual()+"Indeterminados_"+indiceArchivo+".csv";
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
		//SISTEMA, CUENTA, CLAVE, CP, CAJA, FECHA PAGO, IMPORTE, RECARGO, PAQ, FECHA BALANCE, COD INDET
		buffer.write("INDETERMINADOS");
		buffer.newLine();
		buffer.write("SISTEMA");
		buffer.write(", CUENTA"); // Solo para el caso de Sistema con Tipo Deuda "Deuda Pura", anioComprobante<>"99" y periodo<>"99"
		buffer.write(", CLAVE");
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
	 *  Verifica si existe un Balance con estado "En Preparacion", 
	 *  "En espera comenzar", "Procesando" o "En espera continuar". Retorna true o false.
	 *  (Excluyendo al Balance que realiza la consulta)
	 *  Se agrega filtro por Ejercicio. Solo valida que no exista un Balance en ejecucion para el mismo ejercicio.
	 *  
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existBalanceSinTerminar(Balance bal) throws Exception{
		Balance balance;
		String queryString = "from Balance t where ";
		queryString += " (t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_PREPARACION;  
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_ESPERA_COMENZAR;
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_PROCESANDO;
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_ESPERA_CONTINUAR+" )";
		queryString += " and t.ejercicio.id = "+bal.getEjercicio().getId();
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		balance = (Balance) query.uniqueResult();	
		
		if(balance != null && (bal.getId()==null || balance.getId().longValue() != bal.getId().longValue()))
			return true;
		else
			return false;		
	}

	/**
	 * Retorna un map de DetallePago con clave='anio;periodo' con todos los DetallesPagos 
	 * que no esten ANULADOS o con ERROR, que correpondan a 'idCuenta' y filtrando 
	 * por los 'impuestos'  
	 * @param numeroCuenta detalles de pago de este nro cuenta
	 * @param impuestos lista separada por ',' 
	 * @return
	 */
	public HashMap<String, DetallePago> getMapDetallePagoValido(String nroCuenta, String impuestos) {
		String queryString = "select d from TranAfip t, DetallePago d where ";
		queryString += " t.id = d.tranAfip.id ";
		queryString += " and t.estTranAfip.id != " + EstTranAfip.ID_ANULADA;
		queryString += " and t.estTranAfip.id != " + EstTranAfip.ID_CON_ERROR;
		queryString += " and d.numeroCuenta = :nroCuenta";
		queryString += " and d.impuesto in (" + impuestos + ")"; 
				
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setString("nroCuenta", nroCuenta);
		
		HashMap<String, DetallePago> ret = new HashMap<String, DetallePago>();
		
		List<DetallePago> list = query.list();
		for(DetallePago d : list) {
			String key = d.getAnio() + ";" + d.getPeriodo();
			ret.put(key, d);
		}
		
		return ret;
	}
}
