//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>



package ar.gov.rosario.siat.gde.buss.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.buss.dao.SiatJDBCDAO;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaIncProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasPlanillasDeudaAdapter;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

public class SelAlmDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SelAlmDetDAO.class);	

	public SelAlmDetDAO() {
		super(SelAlmDet.class);
	}


	/**
	 * Borrar las listas de selAlmDet que correspondan al SelAlm
	 * @param selAlm
	 * @return long
	 */
	public long deleteListSelAlmDetBySelAlm (SelAlm selAlm){

		long ctdRegistrosBorrados = 0;

		String hqlMinMax = "SELECT MIN(sad.id), MAX(sad.id) FROM SelAlmDet sad where sad.selAlm = :selAlm";
		Query queryMinMax = SiatHibernateUtil.currentSession().createQuery(hqlMinMax);
		queryMinMax.setEntity("selAlm", selAlm);
		List listMinMax = queryMinMax.list();
		Object[] minMax = (Object[])listMinMax.get(0);
		Long idMin = (Long) minMax[0];
		Long idMax = (Long) minMax[1];
		long skip = 100000L;
		if(idMin == null && idMax == null){
			return 0;
		}

		long ctdRegistrosBorrar = idMax - idMin +1;
		log.debug("idMin: " + idMin + " idMax: " + idMax + " skip: " + skip + 
				"ctdRegistros a borrar: " + ctdRegistrosBorrar);

		String hqlDelete = "DELETE FROM SelAlmDet sad " +
		"WHERE sad.selAlm = :selAlm " +
		"AND sad.id >= :idMin AND sad.id <= :idCorte";

		while(idMin <= idMax){
			long idCorte = idMin + skip;
			log.debug("idMin: " + idMin + " idCorte: " + idCorte);
			Query queryDelete = SiatHibernateUtil.currentSession().createQuery(hqlDelete);
			queryDelete.setEntity("selAlm", selAlm).setLong("idMin", idMin).setLong("idCorte", idCorte);
			ctdRegistrosBorrados += queryDelete.executeUpdate();

			log.debug("ctd registros borrados: " + ctdRegistrosBorrados);
			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.currentSession().beginTransaction();
			idMin = idCorte;
		}

		return ctdRegistrosBorrados;
	}

	/**
	 * Crea todos las SelAlmDet a incluir por el proceso de envio a judiciales
	 * @param deudaIncProMasAgregarSearchPageVO
	 * @throws Exception
	 */
	/*
	public void createListSelAlmDetInc(DeudaIncProMasAgregarSearchPage deudaIncProMasAgregarSearchPageVO)throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		long skip = 0;               // inicializo el skip  
		long incrementoSkip = 10000; // incremento del skip
		boolean resultadoVacio = false;

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// armado del query string de la consulta de cada salto para armar cada query del insert de cada salto
		String idSelAlmInc  = deudaIncProMasAgregarSearchPageVO.getProcesoMasivo().getSelAlmInc().getIdView();
		String fecUltMdf    = DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
		String estadoActivo = StringUtil.formatInteger(Estado.ACTIVO.getId());
		String userName = DemodaUtil.currentUserContext().getUserName();
		if (userName == null) {
			userName = "siat";
		}

		String sqlBySearchPage = GdeDAOFactory.getDeudaAdminDAO().getSQLBySearchPage(deudaIncProMasAgregarSearchPageVO);
		// string de la creacion de la tabla temporal para contar los deudas repetidas por cuenta		
		String createTemporal = "SELECT deu.idcuenta " + sqlBySearchPage +
		" GROUP BY deu.idcuenta HAVING COUNT(deu.id) >= 2 INTO TEMP deuRepCta"; // TODO agregar id del usuario
		// divide el string del sql para agregarle el inner con la temporal y obtener el string de la consulta final		
		String[] sqlSplit = sqlBySearchPage.split("WHERE");
		// string de la consulta final		
		String queryString = sqlSplit[0] + 
			"INNER JOIN deuRepCta drc ON (deu.idcuenta == drc.idcuenta ) " // aguante el inner con la temporal 
		+ " WHERE " + sqlSplit[1];

		if(log.isDebugEnabled()) log.debug("query Temporal: " + createTemporal);
		if(log.isDebugEnabled()) log.debug("query string: " + queryString);

		// borrado de la tabla temporal si existe
		try {
			ps = con.prepareStatement("DROP TABLE deuRepCta;");
			ps.executeUpdate();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}
		// creacion de tabla temporal con las deudas repetidas
		ps = con.prepareStatement(createTemporal);
		ps.executeUpdate();

		// mientras sea resultado NO vacio de cada salto
		while(!resultadoVacio){

			// queryString de cada salto
			String queryFinal  = "SELECT SKIP " + skip + " FIRST " + incrementoSkip + 
				" " + idSelAlmInc + ", deu.id, '" + userName + "', '" + fecUltMdf + "', " + estadoActivo +
				" " + queryString;

			if(log.isDebugEnabled()) log.debug("query Select por salto: " + queryFinal);

			// ejecucion de la consulta de resultado final
			ps = con.prepareStatement(queryFinal);
			rs = ps.executeQuery();

			// iteracion del select de cada salto
			if( rs.next() ) {
		 		resultadoVacio = false;
		 		// realizar el insert
		 		String sqlInsert = "INSERT INTO gde_selalmdet (idselalm, idelemento, usuario, fechaultmdf, estado) " +
		 					queryFinal;
		 		PreparedStatement psInsert = con.prepareStatement(sqlInsert);
		 		psInsert.executeUpdate();
		 		psInsert.close();
			}else{
				resultadoVacio = true;
			}
			skip = skip + incrementoSkip; // incremento el skip
			ps.close();
		}
	}
	 */

	/**
	 * Crea la lista del Detalle de Seleccion Almacenada para la deuda incluida y excluida
	 * @param  selAlmDInc
	 * @param  listIdDeuda
	 * @param  tipoSelAlmDet 
	 * @return int ctd de selAlmDet creados
	 * @throws Exception
	 */
	public int createListSelAlmDet(SelAlmDeuda selAlmDInc, String[] listIdDeuda, TipoSelAlm tipoSelAlmDet)throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		int i = 0;
		while (i < listIdDeuda.length) {

			String idElemento = listIdDeuda[i];
			SelAlmDet selAlmDet = new SelAlmDet(selAlmDInc, Long.valueOf(idElemento), tipoSelAlmDet);
			update(selAlmDet);
			i++;
		}
		return i;
	}

	/**
	 * Obtiene el SQL de busqueda a partir de los filtros del SearchPage
	 * @param deudaIncProMasEliminarSearchPage
	 * @return String
	 * @throws Exception
	 */	
	public String getSQLBySearchPage(DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws Exception {

		// determinacion de la tabla de deuda de acuerdo a la viaDeuda del proceso masivo sobre el cual se trabaja.
		String tablaDeuda = "gde_deudaAdmin";
		if (ViaDeuda.ID_VIA_JUDICIAL == deudaIncProMasEliminarSearchPage.getProcesoMasivo().getViaDeuda().getId()){
			tablaDeuda =  "gde_deudaJudicial";
		}

		String queryString = "FROM gde_selalmdet sad " +
		"INNER JOIN "+tablaDeuda + " deu ON (sad.idelemento == deu.id) " +
		"INNER JOIN pad_cuenta cta ON (deu.idcuenta == cta.id) ";

		boolean flagAnd = false;

		/* estado activo?
		// Armamos filtros del HQL
		if (deudaIncProMasEliminarSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " d.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		 */

		// SelAlm
		queryString += flagAnd ? " and " : " where ";
		queryString += " sad.idSelAlm = " + deudaIncProMasEliminarSearchPage.getProcesoMasivo().getSelAlmInc().getId();
		flagAnd = true;

		// clasificacion deuda
		if (deudaIncProMasEliminarSearchPage.getListIdRecClaDeu() != null &&          // puede ser nula 
				deudaIncProMasEliminarSearchPage.getListIdRecClaDeu().length > 0){
			queryString += flagAnd ? " and " : " where ";
			queryString += " deu.idRecClaDeu IN (" + StringUtil.getStringComaSeparate(deudaIncProMasEliminarSearchPage.getListIdRecClaDeu()) + ") ";
			flagAnd = true;
		}

		// filtro Fecha Vencimiento Desde
		if (deudaIncProMasEliminarSearchPage.getFechaVencimientoDesde() != null ) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " deu.fechaVencimiento >= TO_DATE('" + 
			DateUtil.formatDate(deudaIncProMasEliminarSearchPage.getFechaVencimientoDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		// filtro Fecha Vencimiento Hasta
		if (deudaIncProMasEliminarSearchPage.getFechaVencimientoHasta() != null ) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " deu.fechaVencimiento <= TO_DATE('" + 
			DateUtil.formatDate(deudaIncProMasEliminarSearchPage.getFechaVencimientoHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		// numero de cuenta
		String numeroCuenta = deudaIncProMasEliminarSearchPage.getCuenta().getNumeroCuenta();
		if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " cta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
			flagAnd = true;
		}

		// TODO VER Order By
		//queryString += " ORDER BY ej.fechaEnvio DESC ";

		return queryString;
	}

	/**
	 * Genera el archivo de deuda incluida a eliminar a partir del SearchPage
	 * @param deudaIncProMasEliminarSearchPage
	 * @throws Exception
	 */
	public void exportBySearchPage(DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "SELECT cta.numerocuenta, deu.anio, deu.periodo, deu.importe, deu.saldo, deu.fechavencimiento " +
		getSQLBySearchPage(deudaIncProMasEliminarSearchPage);

		queryString += " ORDER BY cta.numerocuenta, deu.anio, deu.periodo ";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		int indiceArchivo = 0;
		//genero el archivo de texto

		// formacion del fileName con el directorio de procesamiento de la corrida del envio judicial 
		Long idProcesoMasivo = deudaIncProMasEliminarSearchPage.getProcesoMasivo().getId();
		String processDir = AdpRun.getRun(ProcesoMasivo.getById(idProcesoMasivo).getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		String fileName = processDir + File.separator + DeudaIncProMasEliminarSearchPage.FILE_NAME + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
		PlanillaVO planillaVO = new PlanillaVO(fileName);
		deudaIncProMasEliminarSearchPage.getListResult().add(planillaVO);

		// --> Creacion del Encabezado del Resultado
		BufferedWriter buffer = crearEncabezadoDeudaIncProMasEliminar(fileName);
		// <-- Fin Creacion del Encabezado del Resultado

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		boolean resultadoVacio = true;

		// --> Resultado
		long c = 0;
		while ( rs.next()) {  
			resultadoVacio = false;

			//cta.numerocuenta, deu.anio, deu.periodo,deu.importe,deu.saldo,deu.fechavencimiento " +
			// numeroCuenta
			buffer.write(rs.getString(1));
			// anio
			buffer.write(", " + rs.getLong(2));
			// periodo
			buffer.write(", " + rs.getLong(3));
			// importe
			buffer.write(", " + rs.getDouble(4));
			// saldo
			buffer.write(", " + rs.getDouble(5));
			// fecha de vencimiento
			buffer.write(", " + DateUtil.formatDate(rs.getDate(6), DateUtil.dd_MM_YYYY_MASK) );

			if(c == 30000){ // incluyendo a las filas del encabezado y considera que c arranca en cero

				buffer.close();
				planillaVO.setCtdResultados(c); 
				indiceArchivo++;
				fileName = processDir + File.separator + DeudaIncProMasEliminarSearchPage.FILE_NAME + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
				planillaVO = new PlanillaVO(fileName); 
				deudaIncProMasEliminarSearchPage.getListResult().add(planillaVO);
				buffer = crearEncabezadoDeudaIncProMasEliminar(fileName);
				c = 0; // reinicio el contador
			}else{
				// crea una nueva linea
				buffer.newLine();
			}
			//log.debug(c);
			c++;
		}

		rs.close();
		ps.close();

		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio){
			// Sin resultados
			buffer.write("No se encontraron registros de Deudas a eliminar, para los filtros seleccionados "  );
		}		 
		// <-- Fin Resultado vacio

		planillaVO.setCtdResultados(c);
		buffer.close();
	}


	/**
	 * Crea el BufferWrite con su encabezado de la deuda incluida a eliminar
	 * @param fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoDeudaIncProMasEliminar(String fileName) throws Exception{

		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// --> Creacion del Encabezado del Resultado
		//cta.numerocuenta, deu.anio, deu.periodo,deu.importe,deu.saldo,deu.fechavencimiento " +
		// cuenta
		buffer.write("Cuenta");
		// anio
		buffer.write(", Año");
		// periodo
		buffer.write(", Periodo");
		// importe
		buffer.write(", Importe");
		// importe
		buffer.write(", Saldo");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		buffer.newLine();
		// <-- Fin Creacion del Encabezado del Resultado

		return buffer;
	}



	/**
	 * Elimina la lista de SelAlmDet de acuerdo al resultado obtenido con los filtros cargados en el SearchPage
	 * @param deudaIncProMasEliminarSearchPageVO
	 * @return long
	 * @throws Exception
	 */
	public long deleteListSelAlmDet(DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO)throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String sqlBySearchPage = this.getSQLBySearchPage(deudaIncProMasEliminarSearchPageVO);

		return this.deleteListSelAlmDet(sqlBySearchPage);

	}

	/**
	 * Elimina los SelAlmDet con los filtros del fromSqlDelete
	 * Realiza un commit de la transaccion cada 10000 registros borrados
	 * @param fromSqlDelete
	 * @return long 
	 * @throws Exception
	 */
	private long deleteListSelAlmDet(String fromSqlDelete)throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		long ctdRegBorrados = 0;
		String sqlSelect  = "SELECT FIRST 10000 sad.id " + fromSqlDelete;
		String hqlDelete  = "DELETE FROM SelAlmDet sad WHERE sad.id IN (:listIdsBorrar)";

		boolean borrar = true;
		while(borrar){

			SQLQuery sqlQuery = currentSession().createSQLQuery(sqlSelect).addScalar("id",Hibernate.LONG);

			// obtener listas de 10000 ids de SelAlmDet a borrar 
			List<Long> listIdsBorrar = (ArrayList<Long>) sqlQuery.list();
			borrar = (listIdsBorrar.size() > 0);
			if (borrar){
				// ejecucion del borrado, commit y beginTransaction
				Query query = currentSession().createQuery(hqlDelete).setParameterList("listIdsBorrar", listIdsBorrar);
				query.executeUpdate();
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.currentSession().beginTransaction();
			}
		}


		log.debug("ctd Reg Borrados: " + ctdRegBorrados);
		return ctdRegBorrados;
	}


	/**
	 * Obtiene la lista de SelAlmDet de acuerdo a los filtros del SearchPage
	 * @param  deudaIncProMasEliminarSearchPage
	 * @return List<SelAlmDet>
	 * @throws Exception
	 */
	public List<SelAlmDet> getBySearchPage(DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		List<SelAlmDet> listSelAlmDet = new ArrayList<SelAlmDet>();

		String queryString = "SELECT sad.id " +
		getSQLBySearchPage(deudaIncProMasEliminarSearchPage);

		// Order By
		//queryString += " ORDER BY ej.fechaEnvio DESC ";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = SiatHibernateUtil.currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
		con.setReadOnly(true);
		con.setAutoCommit(false);
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		// --> Resultado
		List<Long> listLong = new ArrayList<Long>();


		while ( rs.next()) {
			listLong.add(rs.getLong(1));
			//SelAlmDet.getById(rs.getLong(1)); no hacer esto
		}
		// <-- Fin Resultado
		rs.close();
		ps.close();

		if (listLong.size() > 0){
			listSelAlmDet = getListSelAlmDetByListId(listLong);
		}
		return listSelAlmDet;
	}

	/**
	 * Elimina la lista de SelAlmDet.
	 * @param listIdSelAlmDet
	 * @throws Exception
	 */
	public void deleteListSelAlmDetByIds(String[] listIdSelAlmDet)throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		if (listIdSelAlmDet == null ){
			return;
		} 

		for (int i = 0; i < listIdSelAlmDet.length; i++) {
			String idElemento = listIdSelAlmDet[i];
			SelAlmDet selAlmDet = SelAlmDet.getById(Long.valueOf(idElemento));
			delete(selAlmDet);
		}
	}

	/**
	 * Obtiene la lista de SelAlmDet 
	 * @param  listLong
	 * @return List<SelAlmDet>
	 */
	private List<SelAlmDet> getListSelAlmDetByListId(List<Long> listLong){

		String queryString = "FROM SelAlmDet sad WHERE sad.id IN (:idsSelAlmDet) ";

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setParameterList("idsSelAlmDet",listLong);

		return (ArrayList<SelAlmDet>) query.list();

	}

	/**
	 * Exporta las planillas de deuda de la SelAlmDeuda
	 * @param  selAlmDeuda
	 * @param  processDir  direccion base donde generar las planillas
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportPlanillasDeudaBySelAlm(SelAlmDeuda selAlmDeuda, TipoSelAlm tipoSelAlmDet, boolean deudaSigueTitular, String processDir) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();
		Connection con = null;

		try {
			int indiceArchivo = 0;
			//genero el archivo de texto
			String idSelAlm = StringUtil.formatLong(selAlmDeuda.getId());
			String fileName = processDir + File.separator + DeudaProMasPlanillasDeudaAdapter.FILE_NAME + "_" + idSelAlm + "_" + indiceArchivo + ".csv";
			PlanillaVO planillaVO = new PlanillaVO(fileName);
			listPlanilla.add(planillaVO);

			BufferedWriter buffer = crearEncabezadoForPlanillasDeudaBySelAlm(fileName);

			//obtenemos una conexion a la db de siat.
			con = SiatJDBCDAO.getConnection();
			Statement st = null;
			con.setReadOnly(true);
			con.setAutoCommit(false);

			String sql = "drop table tmp_planilla";
			try { st = con.createStatement(); st.execute(sql); st.close();} catch (Exception  e) {try {st.close();} catch(Exception ex) {} };

			sql = "create temp table tmp_planilla (" +
			" numerocuenta varchar(20)," +
			" idrecurso int, " +
			" cuittitpri varchar(20), " +
			" nomtitpri varchar(255), " +
			" anio int, " +
			" periodo int," +
			" idreccladeu int," +
			" fechaVencimiento datetime year to day," +
			" importe decimal(16,6)," +
			" saldo decimal(16,6)," +
			" idcuenta int, " +
			" iddeuda int " + 
			" ) WITH NO LOG";
			st = con.createStatement(); st.execute(sql); st.close();

			sql = "create index ctaord_tmpplanilla on tmp_planilla(numerocuenta)";
			st = con.createStatement(); st.execute(sql); st.close();

			// con join a deuda admin
			sql =  "insert into tmp_planilla " +
			" select cta.numerocuenta, deu.idrecurso, cta.cuittitpri, cta.nomtitpri, deu.anio, deu.periodo," +
			"	 deu.idreccladeu, fechaVencimiento, importe, saldo, cta.id idcuenta, deu.id" +
			" from gde_selalmdet sel, gde_deudaadmin deu, pad_cuenta cta " +
			" where " +
			"   sel.idelemento = deu.id " +
			"   and deu.idcuenta = cta.id " +
			"   and sel.idselalm = "  + selAlmDeuda.getId() +
			"   and sel.idtiposelalmdet = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM;
			st = con.createStatement(); st.execute(sql); st.close();

			// con join a deuda judicial
			// con join a deuda admin
			sql =  "insert into tmp_planilla " +
			" select cta.numerocuenta, deu.idrecurso, cta.cuittitpri, cta.nomtitpri, deu.anio, deu.periodo, " +
			"	deu.idreccladeu, fechaVencimiento, importe, saldo, cta.id idcuenta, deu.id" +
			" from gde_selalmdet sel, gde_deudajudicial deu, pad_cuenta cta " +
			" where " +
			"   sel.idelemento = deu.id " +
			"   and deu.idcuenta = cta.id " +
			"   and sel.idselalm = "  + selAlmDeuda.getId() +
			"   and sel.idtiposelalmdet = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD;
			st = con.createStatement(); st.execute(sql); st.close();

			// recorremos la tabla temporal y generamos los archivos.
			PreparedStatement ps;
			ResultSet         rs;
			long c = 0;
			boolean resultadoVacio = true;
			String nroCuentaOld = "";
			Map<Long, Long> deudaMap = new HashMap<Long, Long>();
			
			if (deudaSigueTitular) {
				//Sigue al titular: obtenemos idCuentaTitular segun fechaEmisin de deuda
				//Buscamo datos del titular en la DB de Personas.
				//sql = "select pla.*, ct.idContribuyente from tmp_planilla pla, pad_cuentatitular ct " +
				//		" where ct.idcuenta = pla.idcuenta " +
				//		" and ct.fechaDesde <= pla.fechaVencimiento " +
				//		" and (ct.fechaHasta is null or ct.fechaHasta >= pla.fechaVencimiento ) " +
				//		" order by numerocuenta, anio, periodo, ct.esTitularPrincipal desc, ct.fechadesde, ct.id";
				
				sql = "select pla.*, tit.idContribuyente " +
				" from tmp_planilla pla " +
				" LEFT JOIN pad_cuentatitular tit " + 
				"   ON (pla.idcuenta == tit.idcuenta " +
				" and tit.fechaDesde <= pla.fechaVencimiento " +
				" and (tit.fechaHasta is null or tit.fechaHasta >= pla.fechaVencimiento) ) " +
				" order by numerocuenta, anio, periodo, tit.esTitularPrincipal desc, tit.fechadesde, tit.id";

				
				
			} else {
				// No sigue al titular usamos nomtitpri y cuittitpri
				sql = "select * from tmp_planilla order by numerocuenta, anio, periodo";				
			}
			log.debug("exportPlanillasDeudaBySelAlm():" + sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.isBeforeFirst()) resultadoVacio = false;
			while (rs.next()) {
				String nroCuenta = "";
				String codRecurso = "";
				String desTitular = "";
				String cuitTitular = "";

				// antes que nada verificamos si esta deuda
				// no la procesamos (puede venir dos o mas veces por el join que se hace 
				// con cuentatitular cuando es deudaSigueTitular)
				if (deudaSigueTitular) {
					Long idDeuda = rs.getLong("iddeuda");
					if (deudaMap.containsKey(idDeuda))
						continue;
					deudaMap.put(idDeuda, idDeuda);
				}
				
				if (nroCuentaOld != rs.getString("numerocuenta")) {
					if (deudaSigueTitular) {
						Long idContr = rs.getLong("idContribuyente");
						Persona persona = Persona.getByIdLight(idContr);
						if (persona != null) {
							desTitular = persona.getRepresent();
							cuitTitular = persona.getCuitFull();
						} else {
							desTitular = "Sin Datos: idPersona = " + idContr;
							cuitTitular = "Sin Datos";
						}
					} else {
						desTitular = StringUtil.nulltrim(rs.getString("nomtitpri"));
						cuitTitular = StringUtil.nulltrim(rs.getString("cuittitpri"));
					}
					nroCuenta = StringUtil.nulltrim(rs.getString("numerocuenta"));
					codRecurso = Recurso.getById(rs.getLong("idrecurso")).getCodRecurso();				
					nroCuentaOld = rs.getString("numerocuenta");
				}

				// numeroCuenta
				buffer.write(nroCuenta);
				// recurso
				buffer.write("," + codRecurso);
				// titularPrincipal
				buffer.write(", " + desTitular);
				// anio
				buffer.write(", " + rs.getString("anio"));
				// periodo
				buffer.write(", " + rs.getString("periodo"));
				// clasificacion de la deuda
				buffer.write(", " + RecClaDeu.getById(rs.getLong("idreccladeu")).getDesClaDeu());
				// fecha de vencimiento
				buffer.write(", " + DateUtil.formatDate(rs.getDate("fechavencimiento"), DateUtil.dd_MM_YYYY_MASK)  );
				// importe
				buffer.write(", " + rs.getString("importe"));
				// saldo
				buffer.write(", " + rs.getString("saldo"));

				if(c == 30000){ // incluyendo a las filas del encabezado y considera que c arranca en cero
					buffer.close();
					planillaVO.setCtdResultados(c); 
					indiceArchivo++;
					fileName = processDir + File.separator + DeudaProMasPlanillasDeudaAdapter.FILE_NAME + "_" + idSelAlm + "_" + indiceArchivo + ".csv";
					planillaVO = new PlanillaVO(fileName);
					listPlanilla.add(planillaVO);
					buffer = crearEncabezadoForPlanillasDeudaBySelAlm(fileName);
					c = 0; // reinicio el contador
				}else{
					// crea una nueva linea
					buffer.newLine();
				}
				c++;
			}

			sql = "drop table tmp_planilla";
			try { st = con.createStatement(); st.execute(sql); st.close();} catch (Exception  e) {try {st.close();} catch(Exception ex) {} };

			if(resultadoVacio){
				// Sin resultados
				if (tipoSelAlmDet.getEsTipoSelAlmDetDeudaAdm()){
					buffer.write("No existen registros de Deudas Administrativas en la Selección Almacenda" );
				} else if (tipoSelAlmDet.getEsTipoSelAlmDetDeudaJud()){
					buffer.write("No existen registros de Deudas Judicial en la Selección Almacenda" );
				}
			}		 

			planillaVO.setCtdResultados(c);
			buffer.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { con.close(); } catch (Exception ex) {};
		} 
		return listPlanilla;
	}

	public List<PlanillaVO> exportPlanillasConvenioCuotaBySelAlm(SelAlmDeuda selAlmDeuda, TipoSelAlm tipoSelAlmDet, String processDir) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();

		Session session = SiatHibernateUtil.currentSession();

		ProcesoMasivo procesoMasivo = ProcesoMasivo.getByIdSelAlmInc(selAlmDeuda.getId());

		int indiceArchivo = 0;
		//genero el archivo de texto
		String idSelAlm = StringUtil.formatLong(selAlmDeuda.getId());
		String fileName = processDir + File.separator + DeudaProMasPlanillasDeudaAdapter.FILE_NAME_CUO_CONV + "_" + idSelAlm + "_" + indiceArchivo + ".csv";
		PlanillaVO planillaVO = new PlanillaVO(fileName);
		listPlanilla.add(planillaVO);

		// --> Creacion del Encabezado del Resultado
		// cta.numerocuenta, titularPrincipal, numeroCuota, fechaVencimiento, capitalCuota, interes, importeCuota, actualizacion
		BufferedWriter buffer = crearEncabezadoForPlanillasConvenioCuotaBySelAlm(fileName);
		// <-- Fin Creacion del Encabezado del Resultado

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;
		Integer firstResult = 0;
		Integer maxResults = 100;

		// Iterar la lista de SelAlmDet de la SelAlm de manera paginada
		boolean contieneSelAlmDets = true;
		Long idCuentaOld = 0L;

		while (contieneSelAlmDets){
			// Obtiene la lista de SelAlmDet  
			List<SelAlmDet> listSelAlmDetPag = this.getListSelAlmDetBySelAlm(selAlmDeuda ,tipoSelAlmDet, firstResult, maxResults);

			contieneSelAlmDets = (listSelAlmDetPag.size() > 0);

			int count = 0;
			for (SelAlmDet selAlmDet : listSelAlmDetPag) {
				String desTitularPrincipal = "";
				resultadoVacio = false;
				ConvenioCuota convenioCuota = selAlmDet.obtenerConvenioCuota(); 
				if(convenioCuota == null){
					log.error("selAlmDet.obtenerConvenioCuota() no recupero un convenioCuota. idSelAlmDet = " + selAlmDet.getId());
					continue;
				}

				Cuenta cuenta = convenioCuota.getConvenio().getCuenta();

				if (idCuentaOld.longValue() != cuenta.getId().longValue()) {
					desTitularPrincipal = cuenta.getNomTitPri();
					idCuentaOld = cuenta.getId();
				}
				//cta.numerocuenta, titularPrincipal, deu.anio, deu.periodo, clasificacion de la deuda, deu.fechavencimiento, deu.importe, deu.saldo, saldo actualizado a la fecha de envio	 		
				// numeroCuenta
				buffer.write(cuenta.getNumeroCuenta());
				// titularPrincipal
				buffer.write(", " + desTitularPrincipal);
				// nroConvenio
				buffer.write(", " + convenioCuota.getConvenio().getNroConvenio());
				// numeroCuota
				buffer.write(", " + convenioCuota.getNumeroCuota());
				// fecha de vencimiento
				buffer.write(", " + DateUtil.formatDate(convenioCuota.getFechaVencimiento(), DateUtil.dd_MM_YYYY_MASK)  );
				// capitalCuota
				buffer.write(", " + convenioCuota.getCapitalCuota());
				// interes
				buffer.write(", " + convenioCuota.getInteres());
				// importe
				buffer.write(", " + convenioCuota.getImporteCuota());
				// actualizacion
				buffer.write(", " + convenioCuota.actualizacionImporteCuota(procesoMasivo.getFechaEnvio()).getRecargo());

				if(c == 30000){ // incluyendo a las filas del encabezado y considera que c arranca en cero
					buffer.close();
					planillaVO.setCtdResultados(c); 
					indiceArchivo++;
					fileName = processDir + File.separator + DeudaProMasPlanillasDeudaAdapter.FILE_NAME_CUO_CONV + "_" + idSelAlm + "_" + indiceArchivo + ".csv";
					planillaVO = new PlanillaVO(fileName);

					listPlanilla.add(planillaVO);
					buffer = crearEncabezadoForPlanillasDeudaBySelAlm(fileName);
					c = 0; // reinicio el contador
				}else{
					// crea una nueva linea
					buffer.newLine();
				}
				c++;
				//customer.updateStuff(...);
				if ( ++count % 100 == 0 ) {
					//flush a batch of updates and release memory:
					session.flush();
					session.clear();
				}
			}
			// Incremento el indice del 1er registro de paginacion
			firstResult += maxResults;
		}

		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio){
			// Sin resultados
			buffer.write("No existen registros de Convenios de Cuotas en la Selección Almacenda"  );
		}		 
		// <-- Fin Resultado vacio

		planillaVO.setCtdResultados(c);
		buffer.close();

		return listPlanilla;
	}

	/**
	 * Crea el BufferWrite con su encabezado de las deudas incluida en la seleccion almacenada
	 * @param  fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoForPlanillasDeudaBySelAlm(String fileName) throws Exception{

		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// --> Creacion del Encabezado del Resultado
		// cuenta
		buffer.write("Cuenta");
		// codrecurso
		buffer.write(", Recurso");
		// titular Principal
		buffer.write(", Titular Principal");
		// anio
		buffer.write(", Año");
		// periodo
		buffer.write(", Periodo");
		// clasificacion de la deuda
		buffer.write(", Clasific. Deuda");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		// importe
		buffer.write(", Importe");
		// saldo
		buffer.write(", Saldo");
		// saldo actualizado a la fecha de envio
		//buffer.write(", Saldo Actualizado");
		buffer.newLine();
		// <-- Fin Creacion del Encabezado del Resultado

		return buffer;
	}

	/**
	 * Crea el BufferWrite con su encabezado de los convenios de cuotas incluida en la seleccion almacenada
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoForPlanillasConvenioCuotaBySelAlm(String fileName) throws Exception{
		// cta.numerocuenta, titularPrincipal, numeroCuota, fechaVencimiento, capitalCuota, interes, importeCuota, actualizacion
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// --> Creacion del Encabezado del Resultado
		// cuenta
		buffer.write("Cuenta");
		// titular Principal
		buffer.write(", titular Principal");
		// nroConvenio
		buffer.write(", Nro. Convenio");
		// numeroCuota
		buffer.write(", Nro. Cuota");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		// capitalCuota
		buffer.write(", Capital");
		// interes
		buffer.write(", Interes");
		// importe
		buffer.write(", Importe");
		// actualizacion
		buffer.write(", Actualización");

		buffer.newLine();
		// <-- Fin Creacion del Encabezado del Resultado

		return buffer;
	}


	//	 DEUDA EXCLUIDA
	/**
	 * Crea la lista de SelAlmDet a partir del SearchPage
	 * @param procesoMasivo  	 
	 * @param deudaExcProMasAgregarSearchPageVO
	 * @return long 
	 * @throws Exception
	 */
	public long createListSelAlmDetExc(ProcesoMasivo procesoMasivo, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO)throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// armado del query string de la consulta de cada salto para armar cada query del insert de cada salto
		// String idSelAlmExc  = deudaExcProMasAgregarSearchPageVO.getProcesoMasivo().getSelAlmExc().getIdView();

		// obtencion de la fechaUltMdf comun para todos los inserts 
		Date fechaUltMdf = new Date();
		String userName     = DemodaUtil.currentUserContext().getUserName();
		if(userName == null){
			userName="siat";
		}

		Long idProcesoMasivo = procesoMasivo.getId();
		String sqlBySearchPage = "";
		TipoSelAlm tipoSelAlmDet = null;
		if (procesoMasivo.getViaDeuda().getEsViaAdmin()){
			sqlBySearchPage = GdeDAOFactory.getDeudaAdminDAO().getSQLBySearchPage(deudaExcProMasAgregarSearchPageVO);
			tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaAdm();
		}else{
			sqlBySearchPage = GdeDAOFactory.getDeudaJudicialDAO().getSQLBySearchPage(deudaExcProMasAgregarSearchPageVO);
			tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaJud();
		}

		// No contamos deudas repetidas
		// No funciona con el flush y el clear. El insert con el select sin skip excedio el log de la bdd
		SQLQuery sqlQuery = SiatHibernateUtil.currentSession().createSQLQuery("SELECT deu.id " + sqlBySearchPage );
		sqlQuery.addScalar("id", Hibernate.LONG);
		List<Long> listIdDeudas = (ArrayList<Long>) sqlQuery.list();
		//List<Long> listIdSelAlmCreadas = new ArrayList<Long>();

		long contador = 0;
		try {
			for (Long idDeuda : listIdDeudas) {
				SelAlmDet selAlmDet = new SelAlmDet(procesoMasivo.getSelAlmExc(),idDeuda ,tipoSelAlmDet, userName, fechaUltMdf);
				// utilizacion directamente del metodo save para que todas las selAlmDet tengan la misma fechaUltMdf
				// y en caso de error se puedan borrar
				Long id = (Long) currentSession().save(selAlmDet);

				//listIdSelAlmCreadas.add(selAlmDet.getId());
				if ( ++contador % 1000 == 0 ) {

					log.debug("CONTADOR : "+contador);
					//flush y clear: no funciona por la cantidad de datos
					//session.flush();
					//session.clear();
					SiatHibernateUtil.currentSession().getTransaction().commit();
					SiatHibernateUtil.closeSession();
					SiatHibernateUtil.currentSession().beginTransaction();

					// recuperamos de nuevo el ProcesoMasivo y el TipoSelAlm  por haber hecho un closeSession
					procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);
					if (procesoMasivo.getViaDeuda().getEsViaAdmin()){
						tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaAdm();
					}else{
						tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaJud();
					}
				}
			}
			return contador;
		} catch (Exception e) {
			deudaExcProMasAgregarSearchPageVO.addRecoverableValueError("error en la creacion de los SelAlmDet");
			log.debug("error en la creacion de los SelAlmDet" + e.getStackTrace());

			if (contador > 0){
				log.debug("empieza el borrado de los registros insertados y persistidos");
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction();
				// recuperamos de nuevo la SelAlm por haber hecho un closeSession
				procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);

				String strDelete = "DELETE FROM SelAlmDet sad WHERE sad.selAlm = :selAlmExc AND fechaUltMdf = :fechaUltMdf";
				Query queryDelete = SiatHibernateUtil.currentSession().createQuery(strDelete);
				queryDelete.setEntity("selAlmExc", procesoMasivo.getSelAlmExc()).setDate("fechaUltMdf", fechaUltMdf);
				int regBorrados = queryDelete.executeUpdate();
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.currentSession().beginTransaction();
				log.debug("commit del DELETE de los SelAlmDet creados");
				return contador - regBorrados;
			}
			return contador;
		}

		/*
		// queryString sin SKIP: versiones anteriores se ejecutaba con un skip
		String querySelect  = "SELECT " + idSelAlmExc + ", deu.id, '" + userName + "', '" + fecUltMdf + "', " + 
		estadoActivo + " " + sqlBySearchPage;

		String sqlInsert = "INSERT INTO gde_selalmdet (idselalm, idelemento, usuario, fechaultmdf, estado) " +
			querySelect;

		if(log.isDebugEnabled()) log.debug("query Select Insert: " + sqlInsert);

		SQLQuery sqlQuery = currentSession().createSQLQuery(sqlInsert);
		return sqlQuery.executeUpdate();
		 */

	}

	/**
	 * Obtiene el SQL a partir del SearchPage
	 * @param  deudaExcProMasEliminarSearchPage
	 * @return String
	 * @throws Exception
	 */
	public String getSQLBySearchPage(DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws Exception {

		String tablaDeuda = "gde_deudaAdmin";
		if(ViaDeuda.ID_VIA_ADMIN != deudaExcProMasEliminarSearchPage.getProcesoMasivo().getViaDeuda().getId()){
			tablaDeuda = "gde_deudaJudicial";
		}

		String queryString = "FROM gde_selalmdet sad " +
		"INNER JOIN " + tablaDeuda + " deu ON (sad.idelemento == deu.id) " +
		"INNER JOIN pad_cuenta cta ON (deu.idcuenta == cta.id) ";

		boolean flagAnd = false;

		/* estado activo?
		// Armamos filtros del HQL
		if (deudaIncProMasEliminarSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " d.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		 */

		// SelAlm
		queryString += flagAnd ? " and " : " where ";
		queryString += " sad.idSelAlm = " + deudaExcProMasEliminarSearchPage.getProcesoMasivo().getSelAlmExc().getId();
		flagAnd = true;

		// clasificacion deuda
		if (deudaExcProMasEliminarSearchPage.getListIdRecClaDeu() != null &&          // puede ser nula 
				deudaExcProMasEliminarSearchPage.getListIdRecClaDeu().length > 0){
			queryString += flagAnd ? " and " : " where ";
			queryString += " deu.idRecClaDeu IN (" + StringUtil.getStringComaSeparate(deudaExcProMasEliminarSearchPage.getListIdRecClaDeu()) + ") ";
			flagAnd = true;
		}

		// filtro Fecha Vencimiento Desde
		if (deudaExcProMasEliminarSearchPage.getFechaVencimientoDesde() != null ) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " deu.fechaVencimiento >= TO_DATE('" + 
			DateUtil.formatDate(deudaExcProMasEliminarSearchPage.getFechaVencimientoDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		// filtro Fecha Vencimiento Hasta
		if (deudaExcProMasEliminarSearchPage.getFechaVencimientoHasta() != null ) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " deu.fechaVencimiento <= TO_DATE('" + 
			DateUtil.formatDate(deudaExcProMasEliminarSearchPage.getFechaVencimientoHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		// numero de cuenta
		String numeroCuenta = deudaExcProMasEliminarSearchPage.getCuenta().getNumeroCuenta();
		if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " cta.numeroCuenta = '" + numeroCuenta + "'";
			flagAnd = true;
		}

		// TODO VER Order By
		//queryString += " ORDER BY ej.fechaEnvio DESC ";

		return queryString;
	}

	/**
	 * Exporta el archivo de deudas excluidas a eliminar de acuerdo al SearchPage
	 * @param deudaExcProMasEliminarSearchPage
	 * @throws Exception
	 */
	public void exportBySearchPage(DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "SELECT cta.numerocuenta, deu.anio, deu.periodo, deu.importe, deu.saldo, deu.fechavencimiento " +
		getSQLBySearchPage(deudaExcProMasEliminarSearchPage);

		queryString += " ORDER BY cta.numerocuenta, deu.anio, deu.periodo ";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		int indiceArchivo = 0;
		//genero el archivo de texto 
		Long idProcesoMasivo = deudaExcProMasEliminarSearchPage.getProcesoMasivo().getId();
		String processDir = AdpRun.getRun(ProcesoMasivo.getById(idProcesoMasivo).getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		String fileName = processDir + File.separator + DeudaExcProMasEliminarSearchPage.FILE_NAME + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
		PlanillaVO planillaVO = new PlanillaVO(fileName);
		deudaExcProMasEliminarSearchPage.getListResult().add(planillaVO);
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));

		// --> Creacion del Encabezado del Resultado
		//cta.numerocuenta, deu.anio, deu.periodo,deu.importe,deu.saldo,deu.fechavencimiento " +
		// cuenta
		buffer.write("Cuenta");
		// anio
		buffer.write(", Año");
		// periodo
		buffer.write(", Periodo");
		// importe
		buffer.write(", Importe");
		// importe
		buffer.write(", Saldo");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		buffer.newLine();
		// <-- Fin Creacion del Encabezado del Resultado

		buffer.newLine();

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		boolean resultadoVacio = true;

		// --> Resultado
		long c = 0;
		while ( rs.next()) {
			resultadoVacio = false;

			//cta.numerocuenta, deu.anio, deu.periodo,deu.importe,deu.saldo,deu.fechavencimiento " +
			// numeroCuenta
			buffer.write(rs.getString(1));
			// anio
			buffer.write(", " + rs.getLong(2));
			// periodo
			buffer.write(", " + rs.getLong(3));
			// importe
			buffer.write(", " + rs.getDouble(4));
			// saldo
			buffer.write(", " + rs.getDouble(5));
			// fecha de vencimiento
			buffer.write(", " + DateUtil.formatDate(rs.getDate(6), DateUtil.dd_MM_YYYY_MASK));

			if(c == 30000){ // incluyendo a las filas del encabezado y considera que c arranca en cero

				buffer.close();
				planillaVO.setCtdResultados(c); 
				indiceArchivo++;
				fileName = processDir + File.separator + DeudaExcProMasEliminarSearchPage.FILE_NAME + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv"; 
				planillaVO = new PlanillaVO(fileName);
				deudaExcProMasEliminarSearchPage.getListResult().add(planillaVO);
				buffer = new BufferedWriter(new FileWriter(fileName, false));
				c = 0; // reinicio el contador
			}else{
				// crea una nueva linea
				buffer.newLine();
			}
			//log.debug(c);
			c++;
		}

		rs.close();
		ps.close();

		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio){
			// Sin resultados
			buffer.write("No se encontraron registros de Deudas a excluir, para los filtros seleccionados "  );
		}		 
		// <-- Fin Resultado vacio

		planillaVO.setCtdResultados(c);
		buffer.close();
	}


	/**
	 * Elimina la lista de SelAlmDet de acuerdo al SearchPage
	 * @param deudaExcProMasEliminarSearchPageVO
	 * @throws Exception
	 */
	public long deleteListSelAlmDet(DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO)throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String sqlBySearchPage = this.getSQLBySearchPage(deudaExcProMasEliminarSearchPageVO);

		return this.deleteListSelAlmDet(sqlBySearchPage);

	}

	/**
	 * Obtiene la lista de SelAlmDet a partir de los filtros cargados en el SearchPage
	 * @param  deudaExcProMasEliminarSearchPage
	 * @return List<SelAlmDet>
	 * @throws Exception
	 */
	public List<SelAlmDet> getBySearchPage(DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		List<SelAlmDet> listSelAlmDet = new ArrayList<SelAlmDet>();

		String queryString = "SELECT sad.id " +
		getSQLBySearchPage(deudaExcProMasEliminarSearchPage);

		// Order By
		//queryString += " ORDER BY ej.fechaEnvio DESC ";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = SiatHibernateUtil.currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
		con.setReadOnly(true);
		con.setAutoCommit(false);
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		// --> Resultado
		List<Long> listLong = new ArrayList<Long>();


		while ( rs.next()) {
			listLong.add(rs.getLong(1));
			//SelAlmDet.getById(rs.getLong(1)); no hacer esto
		}
		// <-- Fin Resultado
		rs.close();
		ps.close();

		if (listLong.size() > 0){
			listSelAlmDet = getListSelAlmDetByListId(listLong);
		}
		return listSelAlmDet;
	}

	/**
	 * Determina si la SelAlm contiene al elemento de acuerdo al tipo de detalle de seleccion almacenada
	 * @param  selAlm
	 * @param  idTipoSelAlmDet
	 * @param  idElemento
	 * @return boolean
	 */
	public boolean contieneSelAlmDet(SelAlm selAlm, Long idElemento, Long idTipoSelAlmDet ) {

		Session session = SiatHibernateUtil.currentSession();

		String queryString = " FROM SelAlmDet sad " +
		"WHERE sad.selAlm = :selAlm " +
		"AND sad.idElemento = :idElemento " +
		"AND sad.tipoSelAlmDet.id = :idTipoSelAlmDet";

		Query query = session.createQuery(queryString)
		.setEntity("selAlm", selAlm)
		.setLong("idElemento", idElemento)
		.setLong("idTipoSelAlmDet", idTipoSelAlmDet);

		query.setMaxResults(1);

		return query.list().size() > 0; 
	}

	
	public void loadAllIdElementSelAlmDet(Map<String, String> map, Long idSelAlm) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "";
			sql += " select sel.id, id, sel.idElemento, sel.idTipoSelAlmDet from ";
			sql += "   gde_selalmdet sel ";
			sql += " where ";
			sql += "   idSelAlm = " + idSelAlm;

			con = SiatJDBCDAO.getConnection();
			con.setReadOnly(true);
			con.setAutoCommit(false);

			ps = con.prepareStatement(sql);
			System.out.println("SQL:" + sql);
			ResultSet rs = ps.executeQuery();

			map.clear();
			while (rs.next()) {
				String idElemento = rs.getString("idelemento");
				String idTipo = rs.getString("idTipoSelAlmDet");
				String idSelAlmDet = rs.getString("id");
				
				map.put(idElemento + "-" + idTipo, idSelAlmDet);
			}

			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { ps.close();} catch (Exception e) {};
			try { con.close();} catch (Exception e) {};	
		}
	}


	/**
	 * Determina si la SelAlm contiene SelAlmDet
	 * @param selAlm
	 * @return boolean
	 */
	public boolean contieneSelAlmDetBySelAlm(SelAlm selAlm){

		Session session = SiatHibernateUtil.currentSession();

		String queryString = " FROM SelAlmDet sad " +
		"WHERE sad.selAlm = :selAlm ";

		Query query = session.createQuery(queryString).setEntity("selAlm", selAlm);

		query.setMaxResults(1);

		return query.list().size() > 0; 
	}

	/**
	 * Obtiene la lista De SelAlmDet de una SelAlm de manera paginada
	 * @param  selAlm
	 * @param  tipoSelAlmDet
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<SelAlmDet>
	 */
	public List<SelAlmDet> getListSelAlmDetBySelAlm(SelAlm selAlm, TipoSelAlm tipoSelAlmDet, Integer firstResult, Integer maxResults) {

		String queryStringH = "FROM SelAlmDet sad WHERE sad.selAlm = :selAlm AND tipoSelAlmDet = :tipoSelAlmDet";			

		String queryString = "SELECT SKIP " + firstResult + " FIRST " + maxResults + " * FROM gde_selAlmDet sad " +
		"WHERE sad.idSelAlm = " + selAlm.getId() + " AND sad.idTipoSelAlmDet = " + tipoSelAlmDet.getId();			

		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		if(firstResult == null && maxResults == null){
			query = session.createQuery(queryStringH).setEntity("selAlm", selAlm);			
		}else{
			query = session.createSQLQuery(queryString).addEntity(SelAlmDet.class);			
		}

		return (ArrayList<SelAlmDet>) query.list();
	}

	/**
	 * Obtiene la TODA lista De idElemento de SelAlmDet de una SelAlm
	 * @param  selAlm
	 * @param  tipoSelAlmDet
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<SelAlmDet>
	 */
	public List<Long> getListIdElemento(Long idSelAlm, Long idTipoSelAlmDet) {
		String queryString = "SELECT idElemento FROM gde_selAlmDet sad " +
		"WHERE sad.idSelAlm = " + idSelAlm + " AND sad.idTipoSelAlmDet = " + idTipoSelAlmDet;			

		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createSQLQuery(queryString).addScalar("idElemento", Hibernate.LONG);
		return (ArrayList<Long>) query.list();
	}
	
	
	/**
	 * Obtiene la cantidad total de Detalles de la Seleccion Almacenda.
	 * @param selAlmDeuda
	 * @return Long
	 */
	public Long getTotalBySelAlmDeuda(SelAlmDeuda selAlmDeuda) {

		Session session = SiatHibernateUtil.currentSession();

		String queryString = "SELECT COUNT(*) " +
		"FROM SelAlmDet sad " +
		"WHERE sad.selAlm.id = :idSelAlm ";

		Query query = session.createQuery(queryString)
		.setLong("idSelAlm", selAlmDeuda.getId());

		Long totalBySelAlmDeuda = (Long) query.uniqueResult();	

		return totalBySelAlmDeuda; 
	}

	/**
	 * Obtiene la cantidad total de Detalles de la Seleccion Almacenada para el tipo de SelAlmDet.
	 * @param  selAlmDeuda
	 * @param  tipoSelAlmDet
	 * @return Long
	 */
	public Long getTotalBySelAlmDeudaTipoSelAlmDet(SelAlmDeuda selAlmDeuda, TipoSelAlm tipoSelAlmDet) {

		Session session = SiatHibernateUtil.currentSession();

		String queryString = "SELECT COUNT(*) " +
		"FROM SelAlmDet sad " +
		"WHERE sad.selAlm.id = :idSelAlm AND sad.tipoSelAlmDet = :tipoSelAlmDet";

		Query query = session.createQuery(queryString)
		.setLong("idSelAlm", selAlmDeuda.getId())
		.setEntity("tipoSelAlmDet", tipoSelAlmDet);

		Long totalBySelAlmDeuda = (Long) query.uniqueResult();	

		return totalBySelAlmDeuda; 
	}


	/**
	 * Exporta las planillas de cuentas desde SelAlmDet de tipo deuda y/o convenio
	 * agrupando por cuenta y/o convenio
	 * @param  selAlmDeuda
	 * @param  processDir  direccion base donde generar las planillas
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportPlanillasCuentaBySelAlm(SelAlmDeuda selAlmDeuda, TipoSelAlm tipoSelAlmDet, boolean deudaSigueTitular, String processDir) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();
		Connection con = null;
		Statement st = null;

		try {
			int indiceArchivo = 0;
			//genero el archivo de texto
			String fileNom = "planillaCuenta";
			String idSelAlm = StringUtil.formatLong(selAlmDeuda.getId());
			String fileName = processDir + File.separator +  fileNom + "_" + idSelAlm + "_" + indiceArchivo + ".csv";
			PlanillaVO planillaVO = new PlanillaVO(fileName);
			listPlanilla.add(planillaVO);

			// --> Creacion del Encabezado del Resultado
			//cta.numerocuenta, titularPrincipal, deu.anio, deu.periodo, clasificacion de la deuda, deu.fechavencimiento, deu.importe, deu.saldo, saldo actualizado a la fecha de envio
			BufferedWriter buffer = crearPlanillaCuentaBySelAlm(fileName);
			// <-- Fin Creacion del Encabezado del Resultado

			//obtenemos una conexion a la db de siat.
			con = SiatJDBCDAO.getConnection();
			con.setReadOnly(true);
			con.setAutoCommit(false);

			String sql = "drop table tmp_planilla";
			try { st = con.createStatement(); st.execute(sql); st.close();} catch (Exception  e) {try {st.close();} catch(Exception ex) {} };

			sql = "create temp table tmp_planilla (" +
			" id int, " + 
			" numerocuenta varchar(20)," +
			" nroconvenio int," +
			" idrecurso int, " +
			" cuittitpri varchar(20), " +
			" nomtitpri varchar(255), " +
			" totSaldoDeu decimal(16,6)," +
			" totImporteDeu decimal(16,6)," +
			" totImporteCuo decimal(16,6)," +
			" idcontribuyente int, " +
			" titFechaDesde  datetime year to day," +
			" titEsTitularPrincipal int, " +
			" titId int " +	
			" ) WITH NO LOG";
			st = con.createStatement(); st.execute(sql); st.close();

			sql = "create index ctaord_tmpplanilla on tmp_planilla(numerocuenta, titFechaDesde, titEsTitularPrincipal, titId)";
			st = con.createStatement(); st.execute(sql); st.close();

			// con join a deuda admin
			sql =  "insert into tmp_planilla " +
			" select distinct(cta.id), cta.numerocuenta, 0 as nroconvenio, deu.idrecurso, cta.cuittitpri, " +
			"   cta.nomtitpri, sum(deu.saldo) totSaldoDeu, sum(deu.importe) totImporteDeu, 0 totImporteCuo " +
			"   %s " + //, tit.idcontribuyente
			" from gde_selalmdet sel, gde_deudaadmin deu, pad_cuenta cta " +
			" %s " + // , pad_cuentatitular tit
			" where " +
			"   sel.idelemento = deu.id " +
			"   and deu.idcuenta = cta.id " +
			"   %s " + // join con cuentatitular y rango de fechavencimiento deuda
			"   and sel.idselalm  = " + selAlmDeuda.getId() +
			"   and sel.idtiposelalmdet = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM +
			" group by cta.id, cta.numerocuenta, cta.nomtitpri, cta.cuittitpri, deu.idrecurso " +
			" %s "; //agrupamos con idcontribuyente
			
			if (deudaSigueTitular) {
				sql = String.format(sql, 
						", tit.idcontribuyente, tit.fechadesde, tit.estitularprincipal, tit.id ", 
						", pad_cuentatitular tit ", 
						"  and cta.id = tit.idcuenta " +
						   "   and tit.fechaDesde <= deu.fechaVencimiento " +
						   "   and (tit.fechaHasta is null or tit.fechaHasta >= deu.fechaVencimiento) ", 
						", tit.idcontribuyente, tit.fechadesde, tit.estitularprincipal, tit.id  ");
			} else {
				sql = String.format(sql, ", 0 idcontribuyente, '1980-01-01' titFechadesde, 0 titEstitularPrincipal, 0 titId ", "", "", "");
			}
			log.debug("sql: " + sql);
			st = con.createStatement(); st.execute(sql); st.close();

			// con join a deuda judicial
			sql =  "insert into tmp_planilla " +
			" select distinct(cta.id), cta.numerocuenta, 0 as nroconvenio, deu.idrecurso, cta.cuittitpri, " +
			"   cta.nomtitpri, sum(deu.saldo) totSaldoDeu, sum(deu.importe) totImporteDeu, 0 totImporteCuo " +
			"   %s " + //, tit.idcontribuyente
			" from gde_selalmdet sel, gde_deudajudicial deu, pad_cuenta cta " +
			" %s " + // , pad_cuentatitular tit
			" where " +
			"   sel.idelemento = deu.id " +
			"   and deu.idcuenta = cta.id " +
			"   %s " + // join con cuentatitular y rango de fechavencimiento deuda
			"   and sel.idselalm  = " + selAlmDeuda.getId() +
			"   and sel.idtiposelalmdet = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD +
			" group by cta.id, cta.numerocuenta, cta.nomtitpri, cta.cuittitpri, deu.idrecurso " +
			" %s "; //agrupamos con idcontribuyente
			if (deudaSigueTitular) {
				sql = String.format(sql, 
						", tit.idcontribuyente, tit.fechadesde, tit.estitularprincipal, tit.id ", 
						", pad_cuentatitular tit", 
						"  and cta.id = tit.idcuenta " +
						"   and tit.fechaDesde <= deu.fechaVencimiento " +
						"   and (tit.fechaHasta is null or tit.fechaHasta >= deu.fechaVencimiento) ", 
						", tit.idcontribuyente, tit.fechadesde, tit.estitularprincipal, tit.id  ");
			} else {
				sql = String.format(sql, ", 0 idcontribuyente, '1980-01-01' titFechadesde, 0 titEstitularprincipal, 0 titId ", "", "", "");
			}
			st = con.createStatement(); st.execute(sql); st.close();
			

			// convenios 
			sql =  "insert into tmp_planilla " +
			" select distinct(conv.id), cta.numerocuenta, conv.nroconvenio, cta.idrecurso, cta.cuittitpri, " +
			" cta.nomtitpri, 0 totSaldoDeu, 0 totImporteDeu, sum(cuo.importecuota) totImporteCuo" +
			" , 0 idcontribuyente, '1980-01-01' titFechadesde, 0 titEstitularprincipal, 0 titId " + 
			" from gde_selalmdet sel,  " +
			" gde_conveniocuota cuo,  " +
			" gde_convenio conv,  " +
			" pad_cuenta cta " +
			" where  " +
			"   sel.idelemento = cuo.id " +
			"   and cuo.idconvenio = conv.id " +
			"   and conv.idcuenta = cta.id " +
			"   and sel.idselalm = " + selAlmDeuda.getId() +  
			"   and sel.idtiposelalmdet in (" + TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM + "," + TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD + ")" +  
			" group by conv.id, cta.numerocuenta, conv.nroconvenio, cta.nomtitpri, cta.cuittitpri, cta.idrecurso ";
			
			log.debug("sql: " + sql);
			st = con.createStatement(); st.execute(sql); st.close();	    

			// recorremos la tabla temporal y generamos los archivos.
			PreparedStatement ps;
			ResultSet rs;
			long c = 0;
			boolean resultadoVacio = true;
			Map<String, String> deudaMap = new HashMap<String, String>();

			sql = "select * from tmp_planilla order by numerocuenta, titFechaDesde, titEsTitularPrincipal, titId";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.isBeforeFirst()) resultadoVacio = false;
			while (rs.next()) {
				String desTitular="", cuitTitular ="";
				
				//numerocuenta, recurso, titularPrincipal, cuit, suma de saldo	 		
				buffer.write(StringUtil.nulltrim(rs.getString("numerocuenta"))); buffer.write(",");

				Long nroConv = rs.getLong("nroconvenio");
				if (nroConv > 0) {
					buffer.write(nroConv.toString()); buffer.write(",");
				} else {
					buffer.write(""); buffer.write(",");
				}

				String codRecurdo = Recurso.getById(rs.getLong("idrecurso")).getCodRecurso();
				buffer.write(codRecurdo); buffer.write(",");

				if (deudaSigueTitular) {
					Long idContr = rs.getLong("idContribuyente");
					Persona persona = Persona.getByIdLight(idContr);
					if (persona != null) {
						desTitular = persona.getRepresent();
						cuitTitular = persona.getCuitFull();
					} else {
						desTitular = "Sin Datos: idPersona = " + idContr;
						cuitTitular = "Sin Datos";
					}
				} else {
					desTitular = StringUtil.nulltrim(rs.getString("nomtitpri"));
					cuitTitular = StringUtil.nulltrim(rs.getString("cuittitpri"));
				}

				buffer.write(StringUtil.nulltrim(desTitular)); buffer.write(",");
				buffer.write(StringUtil.nulltrim(cuitTitular)); buffer.write(",");

				buffer.write(NumberUtil.round(rs.getDouble("totSaldoDeu"), 2).toString()); buffer.write(",");
				buffer.write(NumberUtil.round(rs.getDouble("totImporteDeu"), 2).toString()); buffer.write(",");
				buffer.write(NumberUtil.round(rs.getDouble("totImporteCuo"), 2).toString());

				if(c == 30000){ // incluyendo a las filas del encabezado y considera que c arranca en cero
					buffer.close();
					planillaVO.setCtdResultados(c); 
					indiceArchivo++;
					fileName = processDir + File.separator + fileNom + "_" + idSelAlm + "_" + indiceArchivo + ".csv";
					planillaVO = new PlanillaVO(fileName);
					listPlanilla.add(planillaVO);
					buffer = crearEncabezadoForPlanillasDeudaBySelAlm(fileName);
					c = 0; // reinicio el contador
				}else{
					// crea una nueva linea
					buffer.newLine();
				}
				c++;
			}
			rs.close();
			ps.close();

			sql = "drop table tmp_planilla";
			try { st = con.createStatement(); st.execute(sql); st.close();} catch (Exception  e) {try {st.close();} catch(Exception ex) {} };

			if(resultadoVacio){
				if (tipoSelAlmDet.getEsTipoSelAlmDetDeudaAdm()){
					buffer.write("No existen registros de Deudas Administrativas en la Selección Almacenda" );
				} else if (tipoSelAlmDet.getEsTipoSelAlmDetDeudaJud()){
					buffer.write("No existen registros de Deudas Judicial en la Selección Almacenda" );
				}
			}		 

			planillaVO.setCtdResultados(c);
			buffer.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { con.close(); } catch (Exception ex) {};
		} 

		return listPlanilla;
	}


	/**
	 * Crea el BufferWrite con su encabezado de las deudas cuentas en la seleccion almacenada
	 * @param  fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearPlanillaCuentaBySelAlm(String fileName) throws Exception{

		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// --> Creacion del Encabezado del Resultado
		buffer.write("Nro. Cuenta");
		buffer.write(",Nro. Convenio");
		buffer.write(",Recurso");
		buffer.write(",Cuit Tit.Principal");
		buffer.write(",Nombre Tit. Principal");
		buffer.write(",Total Saldo Deuda");
		buffer.write(",Total Importe Deuda");
		buffer.write(",Total Importe Cuota");

		buffer.newLine();
		// <-- Fin Creacion del Encabezado del Resultado

		return buffer;
	}
}
