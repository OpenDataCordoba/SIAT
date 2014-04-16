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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.buss.dao.SiatJDBCDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.ProMasDeuInc;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoReportesDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.SelAlmVO;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.SiNo;

public class ProMasDeuIncDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProMasDeuIncDAO.class);	

	public ProMasDeuIncDAO() {
		super(ProMasDeuInc.class);
	}

	/**
	 * Exporta las Planillas de Deuda Incluida del Envio Judicial
	 * @param  idProcesoMasivo
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportReportesDeudaIncluidaByProcesoMasivo(Long idProcesoMasivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;
		
		// es necesario volver a recuperar el proceso masivo porque por procesamientos previos afectan al de sesion
		ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);
		String desRecurso = procesoMasivo.getRecurso().getCodRecurso();
		boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();

		int indiceArchivo = 0;
		//genero el archivo de texto
		
		// formacion del fileName con el directorio de procesamiento de la corrida del envio judicial 
		String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		String fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_DEUDA + "Inc_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";

		PlanillaVO planillaVO = new PlanillaVO(fileName);
		planillaVO.setTitulo(SelAlmVO.DEUDA_INCLUIDA); // identificara el nombre del FileCorrida que se creara 
		listPlanilla.add(planillaVO);

		// --> Creacion del Buffer con su Encabezado del Resultado
		BufferedWriter buffer = crearEncabezadoProMasDeuInc(fileName);

		// --> Resultado
		boolean resultadoVacio = false;
		//int indiceSkip      = 0;        // inicializo el skip  
		//int incrementoSkip  = 100;      // incremento del skip
		long c = 0;                     // contador de registros
		Map<Long, Long> idDeudaMap = new HashMap<Long, Long>();

		// Determinacion de la tabla de deuda y del tipoSelAlmDet de acuerdo a la via de la deuda
		String tablaDeuda = "gde_deudaAdmin";
		Long idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM;
		if (procesoMasivo.getViaDeuda().getEsViaJudicial()){
			tablaDeuda = "gde_deudaJudicial";
			idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD;
		}
		
		String queryString = "SELECT pro.id idProcurador, pro.descripcion, cta.numerocuenta, ejdi.desTitulPrincipal, da.anio, " +
		"da.periodo, rcd.descladeu, da.fechavencimiento, da.importe, ejdi.saldohistorico, ejdi.saldoactualizado, da.id iddeuda " +
		(deudaSigueTitular ? ", tit.idcontribuyente " : "") +
		"FROM gde_proMasDeuInc ejdi " +
		"INNER JOIN "+ tablaDeuda + " da ON (ejdi.iddeuda == da.id) " +
		"LEFT JOIN gde_procurador pro ON (ejdi.idprocurador == pro.id) " +
		"INNER JOIN pad_cuenta cta ON (da.idcuenta == cta.id) " +
		"INNER JOIN def_recClaDeu rcd ON (da.idreccladeu == rcd.id) " +
		(deudaSigueTitular ? "INNER JOIN pad_cuentatitular tit ON (cta.id == tit.idcuenta) " : "") + 
		"WHERE ejdi.idProcesoMasivo = "+ idProcesoMasivo + 
		" AND ejdi.idTipoSelAlmDet = " + idTipoSelAlmDet +   
		(deudaSigueTitular ? " and tit.fechaDesde <= da.fechaVencimiento and (tit.fechaHasta is null or tit.fechaHasta >= da.fechaVencimiento) " : "") + 
		" ORDER BY cta.numerocuenta, da.anio, da.periodo " + 
		(deudaSigueTitular ? " , tit.esTitularPrincipal desc, tit.fechaDesde, tit.id "  : "");
		
		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			String desTitular = "";
			resultadoVacio = false;
		
			Long idDeuda = rs.getLong("idDeuda");
			if (deudaSigueTitular) {
				if (idDeudaMap.containsKey(idDeuda))
					continue;
				
				idDeudaMap.put(idDeuda, idDeuda);
			}

			// pro.id, procurador, recurso, cuenta, titular principal, anio, periodo, clasificacion de la deuda, fechaVencimiento, importe emitido, saldo historico, saldo actualizado	 		
			// procurador
			//buffer.write(proMasDeuInc.getProcurador().getDescripcion());
			String v = rs.getString("idProcurador");
			buffer.write(v==null?"":v);
			buffer.write(",");

			v = rs.getString("descripcion");
			buffer.write(v==null?"":v);
			// recurso
			buffer.write(", " + desRecurso);  // por ahora no agregado al select
			// numeroCuenta
			//DeudaAdmin deudaAdmin = proMasDeuInc.getDeudaAdmin();
			buffer.write(", " +  rs.getString("numerocuenta"));

			// titularPrincipal
			if (deudaSigueTitular) {
				Long idContr = rs.getLong("idContribuyente");
				Persona persona = Persona.getByIdLight(idContr);
				if (persona != null) {
					desTitular = persona.getRepresent();
				} else {
					desTitular = "Sin Datos: idPersona = " + idContr;
				}
			} else {
				desTitular = rs.getString("desTitulPrincipal");			
			}
			buffer.write(", " +  desTitular);
						
			// anio
			buffer.write(", " +  rs.getLong("anio"));
			// periodo
			buffer.write(", " +  rs.getInt("periodo"));
			// clasificacion de la deuda
			buffer.write(", " +  rs.getString("descladeu"));
			// fecha de vencimiento
			buffer.write(", " + DateUtil.formatDate(rs.getDate("fechavencimiento"), DateUtil.dd_MM_YYYY_MASK));
			// importe
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("importe"),SiatParam.DEC_IMPORTE_DB));
			// saldo
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("saldoHistorico"),SiatParam.DEC_IMPORTE_DB));
			// saldo actualizado
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("saldoActualizado"),SiatParam.DEC_IMPORTE_DB));
			c++;
			if(c == 30000 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();
				planillaVO.setCtdResultados(c); 
				indiceArchivo++;
				fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_DEUDA + "Inc_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
				planillaVO = new PlanillaVO(fileName);
				planillaVO.setTitulo(SelAlmVO.DEUDA_INCLUIDA); // identificara el nombre del FileCorrida que se creara
				listPlanilla.add(planillaVO);
				buffer = crearEncabezadoProMasDeuInc(fileName);
				c = 0; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}

		} // fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Deudas a Incluir"  );
		}		 
		// <-- Fin Resultado vacio
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		planillaVO.setCtdResultados(c);
		buffer.close();

		return listPlanilla;
	}
	
	public List<PlanillaVO> exportReportesConvenioCuotaIncluidoByProcesoMasivo(Long idProcesoMasivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;
		
		// es necesario volver a recuperar el proceso masivo porque por procesamientos previos afectan al de sesion
		ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);
		String desRecurso = procesoMasivo.getRecurso().getCodRecurso();

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();

		int indiceArchivo = 0;
		//genero el archivo de texto
		
		// formacion del fileName con el directorio de procesamiento de la corrida del envio judicial 
		String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		String fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_CONV_CUOTA + "Inc_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";

		PlanillaVO planillaVO = new PlanillaVO(fileName);
		planillaVO.setTitulo(SelAlmVO.CONV_CUOTA_INCLUIDA); // identificara el nombre del FileCorrida que se creara
		listPlanilla.add(planillaVO);

		// --> Creacion del Buffer con su Encabezado del Resultado
		BufferedWriter buffer = crearEncabezadoProMasConvCuoInc(fileName);

		// --> Resultado
		boolean resultadoVacio = false;
		//int indiceSkip      = 0;        // inicializo el skip  
		//int incrementoSkip  = 100;      // incremento del skip
		long c = 0;                     // contador de registros

		// Determinacion del tipoSelAlmDet de acuerdo a la via de la deuda
		Long idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM;
		if (procesoMasivo.getViaDeuda().getEsViaJudicial()){
			idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD;
		}
		
		String queryString = "SELECT pro.id idProcurador, pro.descripcion, cta.numerocuenta, ejdi.desTitulPrincipal, conv.nroConvenio, cc.numerocuota, " +
			"cc.fechavencimiento, cc.capitalcuota, cc.interes, cc.importecuota, cc.actualizacion " +
			"FROM gde_proMasDeuInc ejdi " +
			"INNER JOIN gde_conveniocuota cc ON (ejdi.iddeuda = cc.id) " + 
			"INNER JOIN gde_convenio conv ON (cc.idconvenio = conv.id) " +
			" LEFT JOIN gde_procurador pro ON (conv.idprocurador = pro.id) " +
			" INNER JOIN pad_cuenta cta ON (conv.idcuenta = cta.id) " +
			" WHERE ejdi.idProcesoMasivo = "+ idProcesoMasivo + 
			" AND ejdi.idTipoSelAlmDet = "  + idTipoSelAlmDet +
			" ORDER BY cta.numerocuenta, conv.nroConvenio, cc.numerocuota";
		
		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
			
			// pro.id, pro.descripcion, cta.numerocuenta, ejdi.desTitulPrincipal, cc.numerocuota,
			// cc.fechavencimiento, cc.capitalcuota, cc.interes, cc.importecuota, cc.actualizacion 	 		
			// procurador
			String v = rs.getString("idProcurador");
			buffer.write(v==null?"":v);
			buffer.write(",");

			String desc = rs.getString("descripcion");
			buffer.write(desc==null?"":desc );
			// recurso
			buffer.write(", " + desRecurso);  // por ahora no agregado al select
			// numeroCuenta
			buffer.write(", " +  rs.getString("numeroCuenta"));
			// titularPrincipal
			buffer.write(", " +  rs.getString("desTitulPrincipal"));
			// numeroConvenio
			buffer.write(", " +  rs.getLong("nroConvenio"));
			// numeroCuota
			buffer.write(", " +  rs.getLong("numeroCuota"));
			// fecha de vencimiento
			buffer.write(", " + DateUtil.formatDate(rs.getDate("fechaVencimiento"), DateUtil.dd_MM_YYYY_MASK));
			// cc.capitalcuota, 
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("capitalCuota"),SiatParam.DEC_IMPORTE_DB));
			// cc.interes, 
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("interes"),SiatParam.DEC_IMPORTE_DB));
			// cc.importecuota, 
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("importeCuota"),SiatParam.DEC_IMPORTE_DB));
			// cc.actualizacion
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("actualizacion"),SiatParam.DEC_IMPORTE_DB));
			
			c++;
			if(c == 30000 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();
				planillaVO.setCtdResultados(c); 
				indiceArchivo++;
				fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_CONV_CUOTA + "Inc_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
				planillaVO = new PlanillaVO(fileName);
				listPlanilla.add(planillaVO);
				buffer = crearEncabezadoProMasConvCuoInc(fileName);
				c = 0; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}

		} // fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Cuotas de Convenio a Incluir"  );
		}		 
		// <-- Fin Resultado vacio
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		planillaVO.setCtdResultados(c);
		buffer.close();

		return listPlanilla;
	}

	
	/**
	 * Creacion del BufferedWriter con su encabezado.
	 * @param  fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoProMasDeuInc(String fileName) throws Exception{
 
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// procurador
		buffer.write("Nro.Procur");
		// procurador
		buffer.write(",Procurador");
		// recurso
		buffer.write(", Recurso");
		// cuenta
		buffer.write(", Cuenta");
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
		// importe emitido
		buffer.write(", Importe");
		// saldo historico
		buffer.write(", Saldo Hist.");
		// saldo actualizado a la fecha de envio
		buffer.write(", Saldo Actualizado");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();

		return buffer;
	}

	private BufferedWriter crearEncabezadoProMasConvCuoInc(String fileName) throws Exception{
		 
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// procurador
		buffer.write("Nro.Procur");
		// procurador
		buffer.write(", Procurador");
		// recurso
		buffer.write(", Recurso");
		// cuenta
		buffer.write(", Cuenta");
		// titular Principal
		buffer.write(", Titular Principal");
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
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();

		return buffer;
	}

	/**
	 * Determina si contiene deudas incluidas el envio judicial
	 * @param  procesoMasivo
	 * @return boolean
	 */
	public boolean contieneDeudasIncluidas(ProcesoMasivo procesoMasivo){
		
		String queryString = "FROM ProMasDeuInc ejdi WHERE ejdi.procesoMasivo = :procesoMasivo ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query   query   = session.createQuery(queryString).setEntity("procesoMasivo", procesoMasivo);
		query.setMaxResults(1);
		
		return (query.list().size() > 0);
	}
	
	/**
	 * Obtiene la lista de ProMasDeuInc del ProcesoMasivo de manera paginada
	 * @param procesoMasivo
	 * @param firstResult
	 * @param maxResults
	 * @return List<ProMasDeuInc>
	 */
	public List<ProMasDeuInc> getListByProcesoMasivo(ProcesoMasivo procesoMasivo, Integer firstResult, Integer maxResults, Long idtiposelalmdet) {
		String queryString = "SELECT SKIP %s FIRST %s pmdi.* FROM gde_proMasDeuInc pmdi " +
				" WHERE pmdi.idProcesoMasivo = %s ";
		
		if (idtiposelalmdet != null) queryString += " and pmdi.idtiposelalmdet = %s ";
		
		queryString += " ORDER By id";

		queryString = String.format(queryString, firstResult, maxResults, procesoMasivo.getId(), idtiposelalmdet);
		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(queryString).addEntity(ProMasDeuInc.class);
		return (ArrayList<ProMasDeuInc>) query.list();
	}
	
	/**
	 * Obtiene la lista de ProMasDeuInc procesada o no segun el parametro procesada, del ProcesoMasivo, 
	 * de manera paginada. A partir del idProMasDeuInc sin incluir.
	 * 
	 * @param procesoMasivo
	 * @param firstResult
	 * @param maxResults
	 * @param procesada
	 * @param idProMasDeuInc
	 * @return List<ProMasDeuInc>
	 */
	public List<ProMasDeuInc> getListByProcesoMasivoProcesada(ProcesoMasivo procesoMasivo, Integer firstResult, Integer maxResults, Integer procesada, Long idProMasDeuInc ) {
		
		String queryString = "SELECT SKIP %s FIRST %s pmdi.* " +
				" FROM gde_proMasDeuInc pmdi WHERE pmdi.idProcesoMasivo = %s ";

		queryString = String.format(queryString, firstResult, maxResults, procesoMasivo.getId());

		if(SiNo.SI.getId().equals(procesada) ){
			queryString += " AND pmdi.procesada = " + SiNo.SI.getId() ;
		}else{
			queryString += " AND (pmdi.procesada IS NULL OR pmdi.procesada = " + SiNo.NO.getId() + ")" ;
		}
		
		if(idProMasDeuInc != null || idProMasDeuInc.longValue() > 0){
			queryString += " AND pmdi.id > " + idProMasDeuInc ;
		}
		
		queryString += " ORDER BY id";
		Session session = SiatHibernateUtil.currentSession();
		
		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(queryString).addEntity(ProMasDeuInc.class);
		return (ArrayList<ProMasDeuInc>) query.list();
	}


	/**
	 * Borra los ProMasDeuInc del Envio Judicial
	 * @param  procesoMasivo
	 * @return int  
	 */
	public int deleteListByProcesoMasivo(ProcesoMasivo procesoMasivo){
	
		String queryString = "DELETE FROM ProMasDeuInc ejdi WHERE ejdi.procesoMasivo = :procesoMasivo ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setEntity("procesoMasivo", procesoMasivo);
	    
		return query.executeUpdate();
	}


	/**
	 * Obtiene Lista de id de cuentas y id de deuda incluidos en ProMasDeuInc del ProcesoMasivo ordenados por idCuenta 
	 * y el criterio correspondiente a cada categoria de recurso
	 * <p>Funcionamiento: la lista que retorna es el resultado de un query a una tabla temporal ordenado por las columnas
	 * idCuenta y orden.
	 * Luego es responsabilidad de cada Categoria de Recurso poblar dicha tabla adecuandamente.
	 * Por ejemplo el criterio de reparto de TGI es ordenado por borche y catastral. Entonces en la columna orden, se llena con las catastrales.
	 * El criterio para CDM es mas complejo, requiere ordenar por obra, repartidor, nroCuadra y catastral, entonces la columna orden, 
	 * es cargada con dichos valores en formato de cadena completado con cero.
	 * <p>Luego informix realiza todo el trabajo 
	 * @param procesoMasivo
	 * @return List
	 */
	public List getListCuentaDeudaByProcesoMasivo(ProcesoMasivo procesoMasivo) {
		Session session = currentSession();
		String sql;
		
		String tablaDeuda = "gde_deudaAdmin";
		Long idTipoSelAlmDetDeuda = TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM;
		Long idTipoSelAlmDetConvCuo = TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM;
		if (procesoMasivo.getViaDeuda().getEsViaJudicial()){
			tablaDeuda = "gde_deudaJudicial";
			idTipoSelAlmDetDeuda = TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD;
			idTipoSelAlmDetConvCuo = TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD;
		}

		sql = "drop table tmp_ProMas";
		try { session.createSQLQuery(sql).executeUpdate(); } catch (Exception  e) {};
		
		sql = "create temp table tmp_proMas (" +
		    " idProcesoMasivo int," +
		    " idCuenta int, " +
		    " idElem int, " +
		    " idTipoElem int, " +
		    " orden varchar(255) " +
		    " ) WITH NO LOG";
		session.createSQLQuery(sql).executeUpdate();
		
		sql = "create index ctaord_tmpProMas on tmp_proMas(idcuenta, orden)";
		session.createSQLQuery(sql).executeUpdate();
				
		//si es recurso tgi
		Long idCategoria = procesoMasivo.getRecurso().getCategoria().getId();
		if (idCategoria.equals(Categoria.ID_TGI)) {			
			// las deudas seleccionadas con join a deuda para lograr obtener el idCuenta
			// Funcionalmente se requiere: los archivos dentro de zona, ordenados por catastral
			//                             los archivos fuera de zona, ordenados por borche y domicilio de envio.
			// El algoritmo de generacion de los archivos requiere las deudas y/o cuotas:
			//                            ordenados por idcuenta para poder hacer un corte de control para generar cada notificacion por cta.
			// Por tanto:
			// la columna orden tiene el siguiente formato:
			//    Para los fuera de zona (idbroche != null) : [1][cuenta.idBroche][cuenta.desdomenv]
			//    Para los de zona       (idbroche == null) : [2][catastral]
			// Osea que tenemos que tirar cuatro querys -> para fuera de zona: deuda y cuotas y para dentro de zona: deuda y cuotas.
			// Estos qry llenan la tabla temporal, luego se ejecuta un query ordenado por orden y idcuenta.
			
			///// DEUDAS FUERA DE ZONA /////
			//los que tiene idbroche not null, (FUERA DE ZONA) 
			sql = "INSERT INTO tmp_promas " +
			" SELECT pmdi.idProcesoMasivo idProcesoMasivo, deuda.idCuenta idCuenta, " +
			"        deuda.id idElem, pmdi.idTipoSelAlmDet idTipoElem, '1' || LPAD(cuenta.idbroche, 16, '0') || cuenta.desdomenv orden " +
			" FROM gde_ProMasDeuInc pmdi, "+ tablaDeuda + " deuda, " +
			" pad_cuenta cuenta, pad_objimp objimp " +
			" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() + 
			" and pmdi.idDeuda = deuda.id " +
			" and deuda.idCuenta = cuenta.id and cuenta.idObjImp = objimp.id " +
			" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetDeuda +
			" and cuenta.idBroche is not null";
			log.debug("sql:" + sql);
			session.createSQLQuery(sql).executeUpdate();

			///// CUOTAS FUERA DE ZONA /////
			// las cuotas seleccionadas con join a convenios para lograr obtener el idCuenta de la cuota
			// los que tiene idbroche not null (FUERA DE ZONA)
			sql = "INSERT INTO tmp_promas " +
			" SELECT pmdi.idProcesoMasivo idProcesoMasivo, conv.idCuenta idCuenta, convCuot.id idElem, " +
			"        pmdi.idTipoSelAlmDet idTipoElem, '1' || LPAD(cuenta.idbroche, 16, '0') || cuenta.desdomenv orden " +
			" FROM gde_ProMasDeuInc pmdi, gde_conveniocuota convCuot, gde_convenio conv, " +
			" pad_cuenta cuenta, pad_objimp objimp " +
			" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() +
			" and pmdi.idDeuda = convCuot.id " + 
			" and conv.idCuenta = cuenta.id and cuenta.idObjImp = objimp.id " +
			" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetConvCuo +		
			" and convCuot.idconvenio = conv.id " +
			" and cuenta.idBroche is not null";
			log.debug("sql:" + sql);
			session.createSQLQuery(sql).executeUpdate();

			///// DEUDAS DE ZONA ////
			//los que tiene idbroche null (DE ZONA)
			sql = "INSERT INTO tmp_promas " +
			" SELECT pmdi.idProcesoMasivo idProcesoMasivo, deuda.idCuenta idCuenta, " +
			"        deuda.id idElem, pmdi.idTipoSelAlmDet idTipoElem, '2' || objimp.clavefuncional orden " +
			" FROM gde_ProMasDeuInc pmdi, "+ tablaDeuda + " deuda, " +
			" pad_cuenta cuenta, pad_objimp objimp " +
			" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() + 
			" and pmdi.idDeuda = deuda.id " +
			" and deuda.idCuenta = cuenta.id and cuenta.idObjImp = objimp.id " +
			" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetDeuda +
			" and cuenta.idBroche is null";
			log.debug("sql:" + sql);
			session.createSQLQuery(sql).executeUpdate();

			///// CUOTAS DE ZONA ////
			//los que tiene idbroche null, (DE ZONA) 
			sql = "INSERT INTO tmp_promas " +
			" SELECT pmdi.idProcesoMasivo idProcesoMasivo, conv.idCuenta idCuenta, convCuot.id idElem, " +
			"        pmdi.idTipoSelAlmDet idTipoElem, '2' || objimp.clavefuncional orden " +
			" FROM gde_ProMasDeuInc pmdi, gde_conveniocuota convCuot, gde_convenio conv, " +
			" pad_cuenta cuenta, pad_objimp objimp " +
			" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() +
			" and pmdi.idDeuda = convCuot.id " + 
			" and conv.idCuenta = cuenta.id and cuenta.idObjImp = objimp.id " +
			" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetConvCuo +		
			" and convCuot.idconvenio = conv.id " +
			" and cuenta.idBroche is null";
			log.debug("sql:" + sql);
			session.createSQLQuery(sql).executeUpdate();

		} else if (idCategoria.equals(Categoria.ID_CDM)) {
			// selecciona las deudas de CDM
			sql = "INSERT INTO tmp_promas " +
			"SELECT pmdi.idProcesoMasivo idProcesoMasivo, deuda.idCuenta idCuenta, " + 
			"   deuda.id idElem, " +
			"   pmdi.idTipoSelAlmDet idTipoElem, " +
			"   LPAD(placua.idobra, 16, '0') " +
			"   || LPAD(placua.idrepartidor, 16, '0') " + 
			"   || LPAD(placua.numerocuadra, 16, '0') " +
			"   || objimp.clavefuncional orden " +
			" FROM " +
			"   gde_ProMasDeuInc pmdi, " + 
			"   gde_deudaadmin deuda, " +
			"   pad_cuenta cuenta, " +
			"   pad_objimp objimp," +
			"   cdm_placuadet placuadet, " +
			"   cdm_planillacuadra placua " +
			" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() + 
			"   and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetDeuda +
			"   and pmdi.idDeuda = deuda.id " +
			"   and deuda.idCuenta = cuenta.id " +
			"   and cuenta.idObjImp = objimp.id " +
			"   and placuadet.idcuentacdm = cuenta.id " +
			"   and placuadet.idplanillacuadra = placua.id ";
			session.createSQLQuery(sql).executeUpdate();
			
			// selecciona las cuotas de CDM
			sql = "INSERT INTO tmp_promas " +
			" SELECT " + 
			"    pmdi.idProcesoMasivo idProcesoMasivo, " +
			"    conv.idCuenta idCuenta, " +
			"    convCuot.id idElem, " +
			"    pmdi.idTipoSelAlmDet idTipoElem, " +
			"    LPAD(placua.idobra, 16, '0') " +
			"    || LPAD(placua.idrepartidor, 16, '0') " +
			"    || LPAD(placua.numerocuadra, 16, '0') " + 
			"    || objimp.clavefuncional orden " + 
			" FROM gde_ProMasDeuInc pmdi, " +
			"      gde_conveniocuota convCuot, " + 
			"      gde_convenio conv, " +
			"      pad_cuenta cuenta, " +
			"      pad_objimp objimp," +
			"      cdm_placuadet placuadet," +
			"      cdm_planillacuadra placua" +
			" WHERE " +
			"    pmdi.idProcesoMasivo = " + procesoMasivo.getId() +
			"    and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetConvCuo +
			"    and pmdi.idDeuda = convCuot.id " +
			"    and conv.idCuenta = cuenta.id and cuenta.idObjImp = objimp.id" + 
			"    and convCuot.idconvenio = conv.id" +
			"    and placuadet.idcuentacdm = cuenta.id" +
			"    and placuadet.idplanillacuadra = placua.id";
			session.createSQLQuery(sql).executeUpdate();
			
//// Para DReI/ETuR ordenado por catastral 
		} else if (idCategoria.equals(Categoria.ID_DREI)||idCategoria.equals(Categoria.ID_ETUR)) {
			///// DEUDAS ////
			sql = "INSERT INTO tmp_promas " +
			" SELECT pmdi.idProcesoMasivo idProcesoMasivo, deuda.idCuenta idCuenta, " +
			"        deuda.id idElem, pmdi.idTipoSelAlmDet idTipoElem, '2' || atroi.strvalor orden " +
			" FROM gde_ProMasDeuInc pmdi, "+ tablaDeuda + " deuda, " +
			" pad_cuenta cuenta, pad_objimp objimp , pad_objimpatrval atroi" +
			" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() + 
			" and pmdi.idDeuda = deuda.id " +
			" and deuda.idCuenta = cuenta.id and cuenta.idObjImp = objimp.id " +
			" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetDeuda +
		        " and objimp.id = atroi.idobjimp and atroi.idtipobjimpatr = 56 ";  ///no tengo en cuenta las vigencias de los valores de atributos porque este atributo no maneja vigencias (las novedades pisan el valor anterior)
			
			log.debug("sql:" + sql);
			session.createSQLQuery(sql).executeUpdate();

			///// CUOTAS ////
			sql = "INSERT INTO tmp_promas " +
			" SELECT pmdi.idProcesoMasivo idProcesoMasivo, conv.idCuenta idCuenta, convCuot.id idElem, " +
			"        pmdi.idTipoSelAlmDet idTipoElem, '2' || atroi.strvalor orden " +
			" FROM gde_ProMasDeuInc pmdi, gde_conveniocuota convCuot, gde_convenio conv, " +
			" pad_cuenta cuenta, pad_objimp objimp, pad_objimpatrval atroi " +
			" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() +
			" and pmdi.idDeuda = convCuot.id " + 
			" and conv.idCuenta = cuenta.id and cuenta.idObjImp = objimp.id " +
			" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetConvCuo +		
			" and convCuot.idconvenio = conv.id " +
			" and objimp.id = atroi.idobjimp and atroi.idtipobjimpatr = 56";  ///no tengo en cuenta las vigencias de los valores de atributos porque este atributo no maneja vigencias (las novedades pisan el valor anterior)

			log.debug("sql:" + sql);
			session.createSQLQuery(sql).executeUpdate();
			// fin toque por DReI/ETuR
		}else{
			// Para el resto
					///// DEUDAS ////
					sql = "INSERT INTO tmp_promas " +
					" SELECT pmdi.idProcesoMasivo idProcesoMasivo, deuda.idCuenta idCuenta, " +
					"        deuda.id idElem, pmdi.idTipoSelAlmDet idTipoElem, LPAD(cuenta.numeroCuenta, 10, '0') orden " +
					" FROM gde_ProMasDeuInc pmdi, "+ tablaDeuda + " deuda, " +
					" pad_cuenta cuenta " +
					" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() + 
					" and pmdi.idDeuda = deuda.id " +
					" and deuda.idCuenta = cuenta.id " +
					" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetDeuda;
					
					log.debug("sql:" + sql);
					session.createSQLQuery(sql).executeUpdate();

					///// CUOTAS ////
					sql = "INSERT INTO tmp_promas " +
					" SELECT pmdi.idProcesoMasivo idProcesoMasivo, conv.idCuenta idCuenta, convCuot.id idElem, " +
					"        pmdi.idTipoSelAlmDet idTipoElem, LPAD(cuenta.numeroCuenta, 10, '0') orden " +
					" FROM gde_ProMasDeuInc pmdi, gde_conveniocuota convCuot, gde_convenio conv, " +
					" pad_cuenta cuenta " +
					" WHERE pmdi.idProcesoMasivo = " + procesoMasivo.getId() +
					" and pmdi.idDeuda = convCuot.id " + 
					" and conv.idCuenta = cuenta.id " +
					" and pmdi.idTipoSelAlmDet = " + idTipoSelAlmDetConvCuo +		
					" and convCuot.idconvenio = conv.id ";

					log.debug("sql:" + sql);
					session.createSQLQuery(sql).executeUpdate();
		}

		
		// Obtenemos el resultado de la consulta de deudas y cuotas de tgi o cdm o cualquier recurso
		// Si si, nos traemos todos los registros en un array list.
		// En un array de 3 Long, (en jvm1.5) 4millones de estos registros ocupan 128mb mas o menos.
		sql = " select idCuenta, idElem, idTipoElem from tmp_promas " +
				" where idProcesoMasivo = " + procesoMasivo.getId() + 
				" order by orden, idCuenta"; 		
		Query query = session.createSQLQuery(sql).addScalar("idCuenta", Hibernate.LONG)
												.addScalar("idElem", Hibernate.LONG)
												.addScalar("idTipoElem", Hibernate.LONG);
		List<Object[]> list = query.list();

		// liberamos la tabla temporal
		sql = "drop table tmp_ProMas";
		try { session.createSQLQuery(sql).executeUpdate(); } catch (Exception  e) {};
		
		return list;
	}
	
	/** 
	 * Retorna la cantidad de registros en gde_promasdeuinc para un determinado proceso masivo 
	 */
	public Long getCantidadRegistrosProMasDeuInc(ProcesoMasivo procesoMasivo) throws Exception {
		Connection con = currentSession().connection();

		PreparedStatement ps = con.prepareStatement("select count(*) c from gde_promasdeuinc where idProcesoMasivo = " + procesoMasivo.getId());
		ResultSet rs = ps.executeQuery();

		long c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}

		return c;
	}


	/** 
	 * Retorna la cantidad de Deudas incluidas en el proceso masivo pasado que fueron procesadas. 
	 */
	public Long getCantidadDeuda(ProcesoMasivo procesoMasivo) throws Exception {
		Connection con = currentSession().connection();

		PreparedStatement ps = con.prepareStatement("select count(idDeuda) c from gde_promasdeuinc where idProcesoMasivo = " + procesoMasivo.getId()+" and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM);
		ResultSet rs = ps.executeQuery();

		long c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}

		return c;
	}

	/**
	 * Obtiene la lista de idDeuda incluida en el Proceso Masivo.
	 * 
	 * @param procesoMasivo
	 * @return
	 * @throws Exception
	 */
	public List<Long> getListDeudaByProcesoMasivo(ProcesoMasivo procesoMasivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "t.idDeuda from Promasdeuinc t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.procesado = "+ SiNo.SI.getId();
 		
		queryString += " and t.procesoMasivo.id = " +procesoMasivo.getId();
		queryString += " and t.tipoSelAlmDet.id = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM;
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Long> listIdDeuda = (ArrayList<Long>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listIdDeuda; 
	}
	
	/** 
	 * Retorna la cantidad de Deudas incluidas en el proceso masivo que fueron pagadas con fecha posterior al proceso. 
	 */
	public Long getCantidadDeudasPagas(ProcesoMasivo procesoMasivo) throws Exception {
		Connection con = currentSession().connection();
		long c = 0;
		long cantidad = 0;
		String fechaProceso = DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.yyyy_MM_dd_MASK); // TODO Ver bien que fecha va!!!!!
		String sqlDeudaAdmin = "select count(*)  c from gde_deudaAdmin where fechapago>'"+fechaProceso+"' and id in (select iddeuda from gde_promasdeuinc where idprocesomasivo = "+procesoMasivo.getId()+"  and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM+")";
		String sqlDeudaCancelada = "select count(*)  c from gde_deudaCancelada where fechapago>'"+fechaProceso+"' and id in (select iddeuda from gde_promasdeuinc where idprocesomasivo = "+procesoMasivo.getId()+"  and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM+")";
		String sqlDeudaJudicial = "select count(*)  c from gde_deudaJudicial where fechapago>'"+fechaProceso+"' and id in (select iddeuda from gde_promasdeuinc where idprocesomasivo = "+procesoMasivo.getId()+"  and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM+")";
		// Cuenta en la Tabla DeudaAdmin
		PreparedStatement ps = con.prepareStatement(sqlDeudaAdmin);
		ResultSet rs = ps.executeQuery();
		c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}
		cantidad += c;
		// Cuenta en la Tabla DeudaCancelada
		ps = con.prepareStatement(sqlDeudaCancelada);
		rs = ps.executeQuery();
		c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}
		cantidad += c;
		// Cuenta en la Tabla DeudaJudicial
		ps = con.prepareStatement(sqlDeudaJudicial);
		rs = ps.executeQuery();
		c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}
		cantidad += c;
		
		return cantidad;
	}
	
	/** 
	 * Retorna la cantidad de Convenios Formalizados para Deudas incluidas en el proceso masivo con fecha de formalizacion posterior al proceso. 
	 */
	public Long getCantidadConveniosFormalizados(ProcesoMasivo procesoMasivo) throws Exception {
		Connection con = currentSession().connection();
		long c = 0;
		String fechaProceso = DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK); // TODO Ver bien que fecha va!!!!!
		String sqlQuery = "select count(distinct cd.idconvenio) c from gde_conveniodeuda cd, gde_convenio co " +
				" where co.id=cd.idconvenio and co.fechafor>'"+fechaProceso+"' and cd.iddeuda in (select iddeuda from gde_promasdeuinc where idprocesomasivo = "+procesoMasivo.getId()+"  and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM+")";

		PreparedStatement ps = con.prepareStatement(sqlQuery);
		ResultSet rs = ps.executeQuery();
		c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}
		
		return c;
	}
	
	/** 
	 * Retorna la cantidad de Recibos Generados para Deudas incluidas en el proceso masivo con fecha de generacion posterior al proceso. 
	 */
	public Long getCantidadRecibosGenerados(ProcesoMasivo procesoMasivo) throws Exception {
		Connection con = currentSession().connection();
		long c = 0;
		String fechaProceso = DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.yyyy_MM_dd_MASK); // TODO Ver bien que fecha va!!!!!
		String sqlQuery = "select count(distinct rd.idrecibo) c from gde_recibodeuda rd, gde_recibo r"+
					" where r.id=rd.idrecibo and r.fechageneracion>'"+fechaProceso+"' and rd.iddeuda in (select iddeuda from gde_promasdeuinc where idprocesomasivo = "+procesoMasivo.getId()+"  and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM+")";

		PreparedStatement ps = con.prepareStatement(sqlQuery);
		ResultSet rs = ps.executeQuery();
		c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}
		
		return c;
	}
	
	/** 
	 * Retorna la cantidad de Deudas en Convenios de las incluidas en el proceso masivo con fecha de formalizacion posterior al proceso. 
	 */
	public Long getCantidadDeudaEnConvenio(ProcesoMasivo procesoMasivo) throws Exception {
		Connection con = currentSession().connection();
		long c = 0;
		String fechaProceso = DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK); // TODO Ver bien que fecha va!!!!!
		String sqlQuery = "select count(distinct cd.iddeuda) c from gde_conveniodeuda cd, gde_convenio co " +
				" where co.id=cd.idconvenio and co.fechafor>'"+fechaProceso+"' and cd.iddeuda in (select iddeuda from gde_promasdeuinc where idprocesomasivo = "+procesoMasivo.getId()+"  and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM+")";

		PreparedStatement ps = con.prepareStatement(sqlQuery);
		ResultSet rs = ps.executeQuery();
		c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}
		
		return c;
	}
	
	/** 
	 * Retorna la cantidad de Deudas en Recibos de las incluidas en el proceso masivo con fecha de generacion posterior al proceso. 
	 */
	public Long getCantidadDeudaEnRecibo(ProcesoMasivo procesoMasivo) throws Exception {
		Connection con = currentSession().connection();
		long c = 0;
		String fechaProceso = DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.yyyy_MM_dd_MASK); // TODO Ver bien que fecha va!!!!!
		String sqlQuery = "select count(distinct rd.iddeuda) c from gde_recibodeuda rd, gde_recibo r"+
					" where r.id=rd.idrecibo and r.fechageneracion>'"+fechaProceso+"' and rd.iddeuda in (select iddeuda from gde_promasdeuinc where idprocesomasivo = "+procesoMasivo.getId()+"  and procesada = "+SiNo.SI.getId()+" and idtiposelalmdet = "+TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM+")";

		PreparedStatement ps = con.prepareStatement(sqlQuery);
		ResultSet rs = ps.executeQuery();
		c = 0;
		while(rs.next()) {
			c = rs.getLong("c");
		}
		
		return c;
	}

	public Collection<? extends PlanillaVO> exportReportesCuentaIncluida(Long idProcesoMasivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();
		Connection con = null;
		Statement st = null;

		try {
			int indiceArchivo = 0;
			//genero el archivo de texto
			String fileNom = "cuentasIncluida";
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);
			String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
			String fileName = processDir + File.separator +  fileNom + "_" + procesoMasivo.getId() + "_" + indiceArchivo + ".csv";
			boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());

			PlanillaVO planillaVO = new PlanillaVO(fileName);
			planillaVO.setTitulo(SelAlmVO.CUENTA_INCLUIDA);
			listPlanilla.add(planillaVO);

			// --> Creacion del Encabezado del Resultado
			//cta.numerocuenta, titularPrincipal, deu.anio, deu.periodo, clasificacion de la deuda, deu.fechavencimiento, deu.importe, deu.saldo, saldo actualizado a la fecha de envio
			BufferedWriter buffer = crearCuentaIncluida(fileName);
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
			" idcontribuyente int " + 
			" ) WITH NO LOG";
			st = con.createStatement(); st.execute(sql); st.close();

			sql = "create index ctaord_tmpplanilla on tmp_planilla(numerocuenta)";
			st = con.createStatement(); st.execute(sql); st.close();

			String tablaDeuda = "gde_deudaadmin";
			if (procesoMasivo.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL) {
				tablaDeuda = "gde_deudajudicial";
			}
						
			// con join a deuda admin
			sql =  "insert into tmp_planilla " +
			" select distinct(cta.id), cta.numerocuenta, 0 as nroconvenio, deu.idrecurso, cta.cuittitpri, " +
			"   cta.nomtitpri, sum(deu.saldo) totSaldoDeu, sum(deu.importe) totImporteDeu, 0 totImporteCuo" +
			"   %s " + //, tit.idcontribuyente
			" from gde_promasdeuinc inc, " + tablaDeuda + " deu, pad_cuenta cta " +
			" %s " + // , pad_cuentatitular tit
			" where " +
			"   inc.iddeuda = deu.id " +
			"   and deu.idcuenta = cta.id " +
			"   %s " + // join con cuentatitular y rango de fechavencimiento deuda
			"   and inc.idProcesoMasivo  = " + procesoMasivo.getId() +
			" group by cta.id, cta.numerocuenta, cta.nomtitpri, cta.cuittitpri, deu.idrecurso " +
			" %s "; //agrupamos con idcontribuyente
			if (deudaSigueTitular) {
				sql = String.format(sql, 
						", tit.idcontribuyente", 
						", pad_cuentatitular tit", 
						"  and cta.id = tit.idcuenta " +
						   "   and tit.fechaDesde <= deu.fechaVencimiento " +
						   "   and (tit.fechaHasta is null or tit.fechaHasta >= deu.fechaVencimiento) ",
						", tit.idcontribuyente ");				
			} else {
				sql = String.format(sql, ", 0 as idContribuyente", "", "", "");
			}
			st = con.createStatement(); st.execute(sql); st.close();

			// convenios 
			sql =  "insert into tmp_planilla " +
			" select distinct(conv.id), cta.numerocuenta, conv.nroconvenio, cta.idrecurso, cta.cuittitpri, " +
			" cta.nomtitpri, 0 totSaldoDeu, 0 totImporteDeu, sum(cuo.importecuota) totImporteCuo " + 
			" , 0 idContribuyente " +
			" from gde_promasdeuinc inc,  " +
			" gde_conveniocuota cuo,  " +
			" gde_convenio conv,  " +
			" pad_cuenta cta " +
			" where  " +
			"   inc.iddeuda = cuo.id " +
			"   and cuo.idconvenio = conv.id " +
			"   and conv.idcuenta = cta.id " +
			"   and inc.idprocesomasivo = " + procesoMasivo.getId() +  
			" group by conv.id, cta.numerocuenta, conv.nroconvenio, cta.nomtitpri, cta.cuittitpri, cta.idrecurso ";
			st = con.createStatement(); st.execute(sql); st.close();	    

			// recorremos la tabla temporal y generamos los archivos.
			PreparedStatement ps;
			ResultSet         rs;
			long c = 0;
			boolean resultadoVacio = true;		

			sql = "select * from tmp_planilla order by numerocuenta";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.isBeforeFirst()) resultadoVacio = false;
			while (rs.next()) {
				String desTitular = "";
				String cuitTitular = "";
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
				
				buffer.write(desTitular); buffer.write(",");
				buffer.write(cuitTitular); buffer.write(",");

				buffer.write(NumberUtil.round(rs.getDouble("totSaldoDeu"), 2).toString()); buffer.write(",");
				buffer.write(NumberUtil.round(rs.getDouble("totImporteDeu"), 2).toString()); buffer.write(",");
				buffer.write(NumberUtil.round(rs.getDouble("totImporteCuo"), 2).toString());

				if(c == 30000){ // incluyendo a las filas del encabezado y considera que c arranca en cero
					buffer.close();
					planillaVO.setCtdResultados(c); 
					indiceArchivo++;
					fileName = processDir + File.separator +  fileNom + "_" + procesoMasivo.getId() + "_" + indiceArchivo + ".csv";
					planillaVO = new PlanillaVO(fileName);
					planillaVO.setTitulo(SelAlmVO.CUENTA_INCLUIDA);
					listPlanilla.add(planillaVO);
					buffer = crearCuentaIncluida(fileName);
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
				buffer.write("No existen registros de Cuentas Incluidas" );
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
	private BufferedWriter crearCuentaIncluida(String fileName) throws Exception{

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

	/**
	 * Retorna la lista de procuradores que participan en todo el conjunto de deuda incluida del proceso masivo
	 * @param procesoMasivo
	 * @param procurador
	 * @return
	 */
	public List<Procurador> getListProcuradorEnvio(ProcesoMasivo procesoMasivo) {
		String qry = "select distinct pro.* from gde_promasdeuinc pmdi, gde_procurador pro " + 
			" where pmdi.idProcesoMasivo = %s " +
			" and pmdi.idProcurador = pro.id ";

		qry = String.format(qry, procesoMasivo.getId());
		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(qry).addEntity(Procurador.class);
		return (ArrayList<Procurador>) query.list();
	}

	/**
	 * Retorna la lista de deudajudicial para un procurador que participo de este envio.
	 * AKA. la deuda judicial enviada a al procurador en este envio.
	 * @param procesoMasivo
	 * @param procurador
	 * @return
	 */
	public List<DeudaJudicial> getListDeudaJudicial(ProcesoMasivo procesoMasivo, Procurador procurador, long skip, long first) {
		//String queryString = "SELECT SKIP %s FIRST %s pmdi.* FROM gde_proMasDeuInc pmdi WHERE pmdi.idProcesoMasivo = %s";
		String qry = "SELECT SKIP %d FIRST %d dj.* FROM gde_proMasDeuInc pmdi, gde_deudajudicial dj " +
				" WHERE pmdi.idProcesoMasivo = %s " +
				" and pmdi.idProcurador = %s " +
				" and dj.idProcurador = %s " +
				" and pmdi.idDeuda = dj.id " +
				" order by dj.idcuenta, dj.anio, dj.periodo, dj.id";
		
		qry = String.format(qry, skip, first, procesoMasivo.getId(), procurador.getId(), procurador.getId());
		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(qry).addEntity(DeudaJudicial.class);
		return (ArrayList<DeudaJudicial>) query.list();
	}

}
