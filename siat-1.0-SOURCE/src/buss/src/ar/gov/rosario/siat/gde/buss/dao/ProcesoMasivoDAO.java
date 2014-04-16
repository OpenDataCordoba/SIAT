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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.buss.dao.SiatJDBCDAO;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ConstanciaDeu;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPDet;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPro;
import ar.gov.rosario.siat.gde.buss.bean.ProMasDeuExc;
import ar.gov.rosario.siat.gde.buss.bean.ProMasDeuInc;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;


public class ProcesoMasivoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProcesoMasivoDAO.class);	
	
	private final Long CATASTRAL= 31L;
	private final Long UBI_TERRENO = 42L;
	private final Long DOMICILIO_FINCA = 36L;
	private final Long TIPO_PARCELA = 41L;
		
	
	public ProcesoMasivoDAO() {
		super(ProcesoMasivo.class);
	}
	
	/**
	 * Obtiene la lista de ProcesoMasivo aplicando los filtros del SearchPage
	 * @param  ProcesoMasivoSearchPage
	 * @return List<ProcesoMasivo>
	 * @throws Exception
	 */
	public List<ProcesoMasivo> getBySearchPage(ProcesoMasivoSearchPage procesoMasivoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "FROM ProcesoMasivo ej ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ProcesoMasivoSearchPage: " + procesoMasivoSearchPage.infoString()); 
		}
		
		// filtro id del proceso masivo
		if (!ModelUtil.isNullOrEmpty(procesoMasivoSearchPage.getProcesoMasivo())){
			// se cargo el nro de id de proceso masivo desde la vista
			queryString += flagAnd ? " and " : " where ";
			queryString += " ej.id = " + procesoMasivoSearchPage.getProcesoMasivo().getId();
			flagAnd = true;
		}
		// es vuelta atras seteada a NO
		queryString += flagAnd ? " and " : " where ";
	    queryString += " ej.esVueltaAtras = "+ SiNo.NO.getId();
	    flagAnd = true;
	
		// estado
		if (procesoMasivoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " ej.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		// filtro Recurso
		if (!ModelUtil.isNullOrEmpty(procesoMasivoSearchPage.getProcesoMasivo().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " ej.recurso.id = " + procesoMasivoSearchPage.getProcesoMasivo().getRecurso().getId();
			flagAnd = true;
		}

		// filtro tipo proceso masivo requerido
		queryString += flagAnd ? " and " : " where ";
		queryString += " ej.tipProMas.id = " + procesoMasivoSearchPage.getProcesoMasivo().getTipProMas().getId();
		flagAnd = true;

		// filtro Estado de Corrida del Proceso
		EstadoCorridaVO estadoCorridaVO = procesoMasivoSearchPage.getProcesoMasivo().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO )) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " ej.corrida.estadoCorrida.id = " + estadoCorridaVO.getId();
            flagAnd = true;
		}
 		// filtro Fecha Envio Desde
 		if (procesoMasivoSearchPage.getFechaEnvioDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " ej.fechaEnvio >= TO_DATE('" + 
				DateUtil.formatDate(procesoMasivoSearchPage.getFechaEnvioDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// filtro Fecha Envio Hasta
 		if (procesoMasivoSearchPage.getFechaEnvioHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " ej.fechaEnvio <= TO_DATE('" + 
			DateUtil.formatDate(procesoMasivoSearchPage.getFechaEnvioHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// Order By
		queryString += " ORDER BY ej.corrida.estadoCorrida.id ASC, ej.fechaEnvio DESC, ej.id DESC "; 
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ProcesoMasivo> listProcesoMasivo = (ArrayList<ProcesoMasivo>) executeCountedSearch(queryString, procesoMasivoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listProcesoMasivo;
	}
	
	
	/*
	 * Lista de Envios Judicial en los que se envio deuda al procurador 
	 */
	public List<ProcesoMasivo> getListEnvioJudicial(Procurador procurador) throws Exception {
		String queryString = "select distinct(pm) " +  
		" from ProMasDeuInc di, ProcesoMasivo pm " +
		" where " +
		"   di.procesoMasivo.id = pm.id " +
		"   and pm.tipProMas.id = 1 " + //tipo envio judicial
		"   and di.procurador.id = " + procurador.getId() + 
        " order by pm.id desc";
        
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		return  (ArrayList<ProcesoMasivo>) query.list();
	}

	
	
	/**
	 * Obtiene el proceso masivo a traves del id de la seleccion almacendada incluida
	 * @param idSelAlmInc
	 * @return
	 * @throws Exception
	 */
	public ProcesoMasivo getByIdSelAlmInc (Long idSelAlmInc) throws Exception{
		ProcesoMasivo procesoMasivo;
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ProcesoMasivo p WHERE p.selAlmInc.id = "+ idSelAlmInc;
		
		Query query = session.createQuery(queryString);
		
		procesoMasivo = (ProcesoMasivo)query.uniqueResult();
		
		return procesoMasivo;
	}
	
	/**
	 * Obtiene la lista de ProcesoMasivo para un recurso y la lista de estados de corrida 
	 * @param recurso
	 * @param listEstadoCorrida lista de estados de corrida del envio Judicial
	 * @return List<ProcesoMasivo>
	 */
	public List<ProcesoMasivo> getListByRecursoListEstadoCorrida (Recurso recurso, List<EstadoCorrida> listEstadoCorrida){

		String queryString = "FROM ProcesoMasivo ej WHERE ej.recurso = :recurso ";
		
		if (listEstadoCorrida != null && listEstadoCorrida.size() > 0){
			queryString += " AND ej.corrida.estadoCorrida IN (:listEstadoCorrida)";
		}
		Session session = SiatHibernateUtil.currentSession();
		Query   query   = session.createQuery(queryString).setEntity("recurso", recurso);
		
		if (listEstadoCorrida != null && listEstadoCorrida.size() > 0){
			query.setParameterList("listEstadoCorrida", listEstadoCorrida);
		}
		
		return (ArrayList<ProcesoMasivo>) query.list();
	}

	/**
	 * Inserta un registro en gde_promasdeuinc, utilizando  jdbccon.
	 * Los datos que insertan son los almacenados en el parametro deu
	 */
	public int insertProMasDeuInc(ProMasDeuInc deu) throws Exception {
		GdeDAOFactory.getProMasDeuIncDAO().update(deu);
		return 1;
	}

	/**
	 * Inserta un registro en gde_promasdeuexc, utilizando  jdbccon.
	 * Los datos que insertan son los almacenados en el parametro deu
	 */
	public int insertProMasDeuExc(ProMasDeuExc deu) throws Exception {		
		GdeDAOFactory.getProMasDeuExcDAO().update(deu);
		return 1;
	}

	/**
	 * Obtiene la lista de ProcesoMasivo para el Reporte de Respuesta de Operativos 
	 * <p><i>Si no se pasa un Tipo de Proceso como parametro solo se buscan: Pre Envio Judicial (id=2), Reconfeccion Masiva (id=3)</i></p>
	 * @return List<ProcesoMasivo>
	 */
	public List<ProcesoMasivo> getListForReporte (Long idTipProMas){

		String queryString = "FROM ProcesoMasivo ej WHERE ";
		if(idTipProMas == null)
			queryString += " ej.tipProMas = 2 OR ej.tipProMas = 3";
		else
			queryString += " ej.tipProMas = "+idTipProMas;
		
		Session session = SiatHibernateUtil.currentSession();
		Query   query   = session.createQuery(queryString);
		
		return (ArrayList<ProcesoMasivo>) query.list();
	}
	
	
	/* Moueve la deuda de administrativa a judicial en bloques
	 * Retorna la cantidad de bloques que no se pudieron procesar.
	 * Ademas agrega el error la lista de errores del procesoMasivo
	 */
	public int moverDeudaAdmAJud(ProcesoMasivo procesoMasivo) throws Exception {
		Connection        con = null;
		PreparedStatement ps = null;
		String sql = "";
		long total, skip, first;
		int fullPass, blockPass, blockCount, blockTotal;
		List<Long> listSkip = new ArrayList<Long>();
		List<Long> listErrSkip = new ArrayList<Long>();

		try {
			con = SiatJDBCDAO.getConnection();
			con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
			con.setAutoCommit(false);
			skip = 0;
			first = 10000;
			blockPass = 0; fullPass = 0; 
			blockCount = 1; // se usa solo para uso informativo en estado de proceso.

			total = queryCount(con, "select count(*) from gde_promasdeuinc where idprocesomasivo=" + procesoMasivo.getId());
			while (skip <= total) {
				listSkip.add(new Long(skip));
				skip += first;
			}

			while (true) {
				blockTotal = listSkip.size();
				while (listSkip.size()>0) {
					try {
						AdpRun.changeRunMessage("Moviendo deuda y conceptos: Bloque " + blockCount + " de " + blockTotal + ". Bloques con error:" + listErrSkip.size(), 0);

						skip = listSkip.get(0);
						// seleccionamos en tabla temporal conjunto de deuda a transpasar.
						sql = "drop table tmp_di";
						try { executeUpdate(con, sql); } catch (Exception e) {}

						sql = " select skip %d first %d id idpr, iddeuda iddi, idprocurador from gde_promasdeuinc "; 
						sql+= " where idProcesoMasivo = " + procesoMasivo.getId();
						sql+= " order by idpr ";
						sql+= " into temp tmp_di with no log "; 
						executeUpdate(con, String.format(sql, skip, first));

						// hacemos el insert into deuda judicial, usando los id en la tmp_di
						String usuario = DemodaUtil.currentUserContext().getUserName();
						String fechaEnvioStr = DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.yyyy_MM_dd_MASK);
						String fechaAhora = DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
						sql = " insert into gde_deudajudicial "; 
						sql+= " select "; 
						sql+= " id, codrefpag, idcuenta, idreccladeu,"; 
						sql+= ViaDeuda.ID_VIA_JUDICIAL + " idviadeuda, ";
						sql+= EstadoDeuda.ID_JUDICIAL + " idestadodeuda, "; 
						sql+= " periodo, anio, fechaemision, fechavencimiento, "; 
						sql+= " importebruto, importe, saldo, actualizacion, ";
						sql+= " strconceptosprop, strestadodeuda, estaimpresa, idrepartidor, ";
						sql+= " di.idprocurador idprocurador, "; // usamos el idProcurador en la tmp_di
						sql+= " fechapago, idprocedimientocyq, actualizacioncyq, idemision, "; 
						sql+= " obsmotnopre, reclamada, idsistema, resto, idconvenio, ";
						//sql+= " '" + usuario + "' usuario, ";
						sql+= " usuario, ";
						sql+= " to_date('" + fechaAhora + "', '%Y-%m-%d %H:%M:%S') fechaultmdf, "; 
						sql+= " estado, idrecurso, atraseval, ";
						sql+= " to_date('" + fechaEnvioStr + "', '%Y-%m-%d') fechaEnvio ";
						sql+= " from gde_deudaadmin da, tmp_di di ";
						sql+= " where da.id = di.iddi ";
						executeUpdate(con, sql);

						// hacemos el insert into en los reccon de judicial, usando los id de la tmp_di
						sql = " insert into gde_deujudreccon ";
						sql+= " select darc.* from gde_deuadmreccon darc, tmp_di di "; 
						sql+= " where darc.iddeuda = di.iddi ";
						executeUpdate(con, sql);

						// borramos los reccon adm
						sql = " delete from gde_deuadmreccon";
						sql+= " where iddeuda in (select iddi from tmp_di)";
						executeUpdate(con, sql);

						// borramos la deudaadmin
						sql = " delete from gde_deudaadmin ";
						sql+= " where id in (select iddi from tmp_di)";
						executeUpdate(con, sql);

						con.commit();

						listSkip.remove(0);
						blockPass = 0;
						blockCount++;

					} catch (Exception e) {
						log.info("Ocurrio un error al procesar bloque. Vamos a intentar manejarlo.", e);
						try {con.rollback();} catch (Exception ex) {}	
						blockPass++;
						if (blockPass < 3) {
							AdpRun.currentRun().logDebug("Se detecto un problema. Vamos a esperar unos segundos y reintentar. nro. intento:" + blockPass);
							log.info("Se detecto un problema. Vamos a esperar unos segundos y reintentar. nro. intento:" + blockPass, e);
							Thread.sleep(10*1000);
						} else { //ya intentamos sufucientes veces, lo dejamos para lo ultimo.
							Long s = listSkip.remove(0);
							listErrSkip.add(s);
							blockPass=0;
							AdpRun.currentRun().logDebug("Se detecto un problema. Dejamos este bloque para lo ultimo e intenamos el proximo bloque. La excepcion fue:", e);
							log.error("Se detecto un problema. Dejamos este bloque para lo ultimo e intenamos el proximo bloque. La excepcion fue:", e);
						}
					}
				}
				fullPass++;
				if (listErrSkip.size() > 0 && fullPass < 2) { //todavia quedan bloques que reintentar.
					AdpRun.currentRun().logDebug("Quedan bloques " + listErrSkip.size() + " sin procesar. Realizamos otra pasada nro: " + fullPass);
					log.info("Quedan bloques sin procesar. Realizamos otra pasada nro: " + fullPass);
					listSkip.clear();
					listSkip.addAll(listErrSkip);
					listErrSkip.clear();
				} else {
					break;
				}
			}
		} catch (Exception e) {
			try {con.rollback();} catch (Exception ex) {}
			AdpRun.currentRun().logDebug("Se detecto un problema. Grave no se pudo realizar transpaso de deuda.", e);
			log.error("Se detecto un problema grave. No se pudo realizar correctamente transpaso de deuda.", e);

			String errMsg = "Se detecto un problema grave. No se pudo realizar correctamente transpaso de deuda." + e.toString() + ".";
			errMsg += "Usted puede re-activar el paso para intentar terminar de mover la deuda.";
			procesoMasivo.addRecoverableValueError(errMsg);

			return -1;
		} finally {
			try { con.close(); } catch (Exception ex) {}
		}
		
		if (listErrSkip.size() > 0) {
			String errMsg = "Se detecto " + listErrSkip.size() + " bloques de deuda con error que no se pudieron mover a Judiciales.";
			errMsg += "Usted puede re-activar el paso para intentar terminar de mover la deuda.";
			procesoMasivo.setErrorMessage(errMsg);
			procesoMasivo.addRecoverableValueError(errMsg);
		}
		
		return listErrSkip.size();
	}

	private long executeUpdate(Connection con, String sql) throws Exception {
		PreparedStatement ps = null;
		long ret = 0;		
		try {
			ps = con.prepareStatement(sql);
			ret = ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			try { ps.close(); } catch (Exception e) {}
		}
		return ret;
	}
	
	private long queryCount(Connection cn, String sql) throws Exception {
			long ret = 0;
			
			ResultSet rs = cn.createStatement().executeQuery(sql);
			rs.next();
			ret = rs.getLong(1);
			rs.close();
			return ret;
	}
	
	/**
	 * lee un valor del cache
	 * @param cache cache instanciado
	 * @param idTipObjImp idTipObjImp con valor a recuperar 
	 * @return valor del idTipObjImp cargado
	 * Retorna una cadena vacia si idObjImp no existe, o no existe el atributo o tiene su valor es null
	 * @throws Exception
	 */
	public String readValFromCache(Map<String, String> cache, Long idObjImp, Long idTipObjImp) throws Exception {
		/* 
		 * Los valores dentro del cache estan almacenados con formatos
		 * idTipObjImp=strValor;idTipObjImp=strValor;idTipObjImp=strValor;.....
		 */
		String values = cache.get(idObjImp.toString());

		// no encontro el idObjImp en los valores cargados en el cache
		if (values == null)
			return "";
		
		String[] split = values.split(";");
		for(String pair : split) {
			if (pair.startsWith(idTipObjImp + "=")) {
				String[] pv = pair.split("=");
				if (pv.length < 2) //par sin valor
					return ""; 
				return pv[1];
			}
		}
		
		// no encontro el idTipObjImp en los valores cargados en el cache
		return "";
	}

	/* Carga un mapa con idObjImp, y una cadena idTipObjImp=valor;.... por cada  idTipObjImps que se pasa */
	public void loadCacheCDProcuradores(Map<String, String> cache, Date fechaAnalisis, Long idRecurso) throws Exception {
		Connection cn = null;
		ResultSet rs; 
		String sql;
		String ids = "";
		String sFechaAnalisis = DateUtil.formatDate(fechaAnalisis, "yyyy-MM-dd");
		Long[] idTipObjImps = {CATASTRAL, UBI_TERRENO, DOMICILIO_FINCA, TIPO_PARCELA}; //atributos a meter en el cache
		int c = 0;
		
		try {
			for(Long id : idTipObjImps) {
				ids += "," + id;
			}
			ids = ids.substring(1);
		
			sql = " select " +
			" oi.id idobjimp, av.idtipobjimpatr, av.strvalor " +
			" from " +
			"   pad_objimp oi, pad_objimpatrval av, pad_cuenta ca " +
			" where " +
			"   ca.idrecurso = " + idRecurso +
			"   and oi.id = ca.idobjimp " + 
			"   and oi.id = av.idobjimp " +
			"   and av.idtipobjimpatr in (" + ids + ") " + 
			"   and av.fechanovedad <= to_date('" + sFechaAnalisis + "','%Y-%m-%d') " +
			" order by av.fechaDesde desc, av.fechaNovedad desc, av.id desc";

			cn = SiatJDBCDAO.getConnection();
			
			log.info("qry: " + sql);
			rs = cn.createStatement().executeQuery(sql);

			cache.clear();
			while(rs.next()) {
				String idObjImp = rs.getString("idobjimp");
				String strValue = rs.getString("strvalor");
				String idTipObjImpAtr = rs.getString("idtipobjimpatr");
				strValue = strValue == null ? "" : strValue.replace(";", ","); //no queremos que haya caracteres ';'
				
				String tmp = cache.get(idObjImp);
				if (tmp != null) {
					if (tmp.indexOf(idTipObjImpAtr + "=") >= 0) {
						//log.debug("descartando valor: " + String.format("%s=%s;", idTipObjImpAtr, strValue) + " en " +  idObjImp + "=" + tmp);
						continue;
					}
				} else {
					if (tmp == null) tmp = "";
				}
				
				tmp = tmp + String.format("%s=%s;", idTipObjImpAtr, strValue); 
				cache.put(idObjImp, tmp);
				if (++c % 10000 == 0) log.info("Cargando cache: " + c);
			}
			
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { cn.close(); } catch (Exception ex) {}
		}
	}
	
	
	/* 
	 * Cache cuentas tiene que ser un mapa de cuentas de un unico recurso con formato
	 * idCuenta = nroCuenta|catastral|nomTitPri|desDomEnv|ubicacion
	 */
	public String generarArchivoCDProcurador(String dirPath, Long idProcesoMasivo, Long idProcurador, Map<String, String> cache) throws Exception {
		Map<String, Double> mapConcDeuda = new HashMap<String, Double>(); 
		Connection cn = null;
		String sql;
		long idCuenta, idCuentaOld;
		int pc; //periodos count
		String[] conceptos;
		BufferedWriter sw = null;
		String filename;
			
		log.debug("generarArchivoCDProcurador Used Memory: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) /1024/1024) + "M");
		
		try {
			//ATENCION: si cambia el nombre de archivo revisar el metodo: ProcesoMasivoAdmProcesoAdapter.getTieneArchivosCdProcu()
			filename = dirPath + File.separator + "cdproc_" + idProcesoMasivo + "_" + idProcurador + ".txt";
			
			sw = new BufferedWriter(new FileWriter(filename, false));
	
			sql = " select di.id iddi, dj.*, ca.numerocuenta, ca.desdomenv, ca.nomtitpri, ca.idobjimp " +
			" from " +
			"	gde_promasdeuinc di, gde_deudajudicial dj, pad_cuenta ca" + 
			" where di.iddeuda = dj.id " +
			"   and dj.idcuenta = ca.id " + 
			"   and di.idProcesoMasivo = " + idProcesoMasivo +
			"   and di.idProcurador = " + idProcurador + 
			"   and dj.idProcurador = " + idProcurador + 
			" order by idcuenta, anio, periodo, idreccladeu";

			cn = SiatJDBCDAO.getConnection();
			ResultSet rs = cn.createStatement().executeQuery(sql);
		
			idCuentaOld = 0;
			idCuenta = 0;
			EnvioProcCD cdline = null;
			pc = 0;
			while (rs.next()) {
				int i;
				idCuenta = rs.getLong("idcuenta");

				if (idCuenta != idCuentaOld) {
					idCuentaOld = idCuenta;
					if (cdline != null) {
						sw.write(cdline.toString());
						sw.write("\n");
					}

					pc = 0; // contador de periodos					
					cdline = new EnvioProcCD();
					cdline.procurador = rs.getLong("idprocurador");
					cdline.nroCuenta = rs.getString("numerocuenta");
					cdline.anio = rs.getLong("anio");
					cdline.nomTitPri = rs.getString("nomtitpri");
					cdline.desDomEnv = rs.getString("desdomenv");
					cdline.constancia = 0L;

					//valores que salen del cache de objimp
					//usamos el cache, porque usar el definition seria terriblemente costoso
					//y necesitamos estamos viendo si los podemos generar online.
					//Asi y todo el cache esta levantando a una fecha de analisis pero no realiza
					//la logica de cortar pisados. Pero el uso que se hace de estos datos no es critico.
					//por eso opte por usar el cache.
					Long idObjImp = rs.getLong("idObjImp");
					
					if (idObjImp != null){
						cdline.catastral = readValFromCache(cache, idObjImp, CATASTRAL);
						String tipoParcela = readValFromCache(cache, idObjImp, TIPO_PARCELA);
						if (tipoParcela.equals("1")) {  //finca
							cdline.ubicacion = readValFromCache(cache, idObjImp, DOMICILIO_FINCA);						
						} else {
							cdline.ubicacion = readValFromCache(cache, idObjImp, UBI_TERRENO);												
						}
					}
					
				}
				
				cdline.periodos[pc].periodo = rs.getLong("periodo");
				cdline.periodos[pc].fechaVencimiento = rs.getDate("fechaVencimiento");
				cdline.periodos[pc].indice = 0D; 
				cdline.periodos[pc].recargo = 0D;
				cdline.periodos[pc].total = rs.getDouble("saldo");
				
				//conceptos
				conceptos = parseConceptos(rs.getString("strConceptosProp"));
				i = 0;
				mapConcDeuda.clear();
				while(i < conceptos.length) {
					mapConcDeuda.put(conceptos[i++], Double.parseDouble(conceptos[i++]));
				}		
				cdline.periodos[pc].conc1 = mapConcDeuda.get("3"); //contribucion tgi
				cdline.periodos[pc].conc2 = mapConcDeuda.get("1"); //tasa
				cdline.periodos[pc].conc3 = mapConcDeuda.get("3"); //contribucion tgi
				pc++;

				//si no cambio la cuenta y llegue a 12, hay escribir lo mismo 
				if (pc == 12) {
					idCuentaOld = -1; //forzamos a escribir una linea.
				}	
			}
			
			if (cdline != null) {
				sw.write(cdline.toString());
				sw.write("\n");
			}
			
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { cn.close(); } catch (Exception ex) {}
			try { sw.close(); } catch (Exception ex) {}
		}
		
		return filename;
	}

	// completa con ceros a la izquierda
	private String ceroPad(String value, int pad) {
		if (value == null) value = "";
		String s = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		return s.substring(0, pad - value.length()) + value;
	}

	// completa con espacios a la derecha
	private String spacePad(String value, int pad) {
		if (value == null) value = "";
		String s = "                                                                                                  ";
		return value + s.substring(0, pad - value.length());
	}

	public String[] parseConceptos(String strConceptosProp) {
		Pattern p = Pattern.compile("<(\\S+?)>(.*?)</(\\1)>");
		Matcher m = p.matcher(strConceptosProp);
		Vector<String> deudas = new Vector<String>();
		
		int pos = 0;
		while (m.find(pos)) {
			pos = m.end();    	
			deudas.add(m.group(1));
			deudas.add(m.group(2));
		}
		
		String[] t = new String[deudas.size()];
		return deudas.toArray(t);
	}
	
	
	public void generarPlanillasConstancias(ProcesoMasivo procesoMasivo) throws Exception {				
		log.info("Info: Generando planillas y constancias para TGI");
		
		DemodaTimer dt = new DemodaTimer();
		
		Connection cn = null;
		String sql;
		Long idProcesoMasivo = procesoMasivo.getId();
		boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());
		boolean generaConstaciaPostEnvio = Integer.valueOf(2).equals(procesoMasivo.getGeneraConstancia()); 

		Long idProcurador = null, idProcuradorAnt = null;
		Long idCuenta = null;
		Long idCuentaAnt = null;
		Long idDeuda = null;
		Long idCuentaTitular = null;
		Map<Long, List<Long>> mapIdCuentaTitular = new HashMap<Long, List<Long>>(); //lista de id de deuda por titular
		
		Procurador procurador = null;
		PlaEnvDeuPro plaEnvDeuPro = null;
		Cuenta cuenta = null;
		ConstanciaDeu constanciaDeu = null;
		
		PlaEnvDeuPDet plaEnvDeuPDet = null;
		
		// IdProcurador - idPlaEnvDeuPro (Para el procesoMasivo)  
		HashMap<Long, Long> planillas = new HashMap<Long, Long>();
		
		// IdCuenta;IdPlanilla - IdConstancia  (Para el procesoMasivo)
		HashMap<String, Long> constancias = new HashMap<String, Long>();
		
		String ubicacion = "";
		ResultSet rsUbi = null;
		
		Long idDomicilioFinca = TipObjImpAtr.ID_DOMICILIOFINCA;
		Long idUbiTerreno = TipObjImpAtr.ID_UBICACIONTERRENO;
		Long idTipoParcela  = TipObjImpAtr.ID_PARCELA_TIPO_PARCELA;		
		String sqlAtr = "";
		String tipoParcela = ""; 
		String ubicacionBaldio = "";
		String domicilioFinca = "";
		String strIds = idTipoParcela + ", " + idDomicilioFinca + ", " + idUbiTerreno;

		try {
			cn = SiatJDBCDAO.getConnection();
						
			log.debug(" Consultando ProMasDeuInc ... ");
			// Consultamos cantidad de registros  Pro Mas Deu Inc
			sql = "select count(id) from gde_promasdeuinc di where di.idprocesomasivo = " + idProcesoMasivo;
			ResultSet rs = cn.createStatement().executeQuery(sql);
			int recCount = 0;			
			if (rs.next()){
				recCount = rs.getInt(1);
				log.debug(" ProMasDeuIn encontrados: " + recCount);
			}
			rs.close();
			
			// Consultamos de registros  Pro Mas Deu Inc para generar constancias
			if (deudaSigueTitular) {
				sql = " select di.idprocurador, d.idcuenta, di.iddeuda " +
			     " , tit.id idCuentaTitular " + 
				 " from gde_promasdeuinc di, " +
				 (generaConstaciaPostEnvio ? " gde_deudajudicial d " : " gde_deudaadmin d ") +
				 ", pad_cuentatitular tit " + 
				 " where " +
				 " di.iddeuda = d.id " +
				 " and d.idcuenta = tit.idcuenta " +
				 " and tit.fechaDesde <= d.fechaVencimiento and (tit.fechaHasta is null or tit.fechaHasta >= d.fechaVencimiento) " + 
				 " and di.idprocesomasivo = " + idProcesoMasivo +
				 " order by " +
				 " di.idprocurador, d.idcuenta " +
				 " , tit.esTitularPrincipal desc, tit.fechaDesde, tit.id ";
			} else {
				sql = " select di.idprocurador, d.idcuenta, di.iddeuda " + 
				 " from gde_promasdeuinc di, " +
				 (generaConstaciaPostEnvio ? " gde_deudajudicial d " : " gde_deudaadmin d ") + 
				 " where " +
				 " di.iddeuda = d.id " +
				 " and di.idprocesomasivo = " + idProcesoMasivo +
				 " order by " +
				 " d.idcuenta, di.idprocurador";
			}
						
			rs = cn.createStatement().executeQuery(sql);
			
			int c = 1, d = 0; 
			int cPlanillas = planillas.size();
			int cConstancias = constancias.size();
			idProcuradorAnt = null; idProcurador = null;
			Map<Long, Long> idDeudaMap = new HashMap<Long, Long>();
			boolean eof = false;
			// Corte de control por procurador, cuenta, contribuyente
			while (!eof) {
				d++;
				if (rs.next()) {
					idProcuradorAnt = idProcurador;
					idProcurador = rs.getLong("idprocurador");
					idCuenta = rs.getLong("idcuenta");
					idDeuda = rs.getLong("iddeuda");
					idCuentaTitular = (deudaSigueTitular ? rs.getLong("idCuentaTitular") : idCuenta);
					
					// cuando deuda sigue al titular pueden aparecer registros de deuda duplicados
					// por la condicion del where (la misma deuda para uno u otro titular)
					// en ese caso el algoritmo lo que tiene que hacer es quedarse con la primer dueda que
					// encuentra segun la clausula ORDER hecha para contemplar estos casos. 
					if (deudaSigueTitular) {
						if (idDeudaMap.containsKey(idDeuda))
							continue;		
						idDeudaMap.put(idDeuda, idDeuda);
					}
				} else {
					idProcuradorAnt = idProcurador;
					eof = true;
				}
				
				log.debug(" inx: " + d  + " proc: " + idProcurador + " cue: " + idCuenta + " deu: " + idDeuda + " eof: " + eof);
				
				if (idProcuradorAnt == null){
					idProcuradorAnt = idProcurador;
				}

				if (idCuentaAnt == null){
					idCuentaAnt = idCuenta;
				}
				
				// Corte de control por cuenta
				if (idCuenta.longValue() != idCuentaAnt.longValue() 
						|| idProcurador.longValue() != idProcuradorAnt.longValue()
						|| eof) {
					
					// Preguntamos por existencia de planilla en caso de re proceso
					if (!planillas.containsKey(idProcuradorAnt)){
						
						DemodaTimer dtplanilla = new DemodaTimer();
						log.debug(" Creando planilla ...."); 
						// Creamos la planilla
						procurador = Procurador.getById(idProcuradorAnt);
						plaEnvDeuPro = procesoMasivo.createPlaEnvDeuPro(procurador);
						SiatHibernateUtil.currentSession().flush();
						planillas.put(idProcuradorAnt, plaEnvDeuPro.getId());
						cPlanillas++;
						log.debug( dtplanilla.stop("Planilla creada en:"));
					} else {
						log.debug(" Planilla Existente...."); 
						plaEnvDeuPro = PlaEnvDeuPro.getById(planillas.get(idProcuradorAnt));
					}
					
					// Preguntamos por existencia de constancia en caso de reproceso
					String constanciaKey = idCuenta + ";" + plaEnvDeuPro.getId(); 
					if (!constancias.containsKey(constanciaKey)){

						DemodaTimer dtconstancia = new DemodaTimer();
						log.debug(" Creando constancia y detalle ....");
						cuenta = Cuenta.getById(idCuentaAnt);	
						
						// Obtenemos la ubicacion si posee objeto imponible
						if (cuenta.getObjImp() != null){
							if (cuenta.getObjImp().getTipObjImp().getId().equals(TipObjImp.COMERCIO) ){
								ubicacion = cuenta.getDesDomEnv();
							} else {
								DemodaTimer dtAtr = new DemodaTimer();
								
								sqlAtr = " select atr.idtipobjimpatr, atr.strvalor from pad_objimpatrval atr, pad_cuenta cue " +
									" where atr.idobjimp  = cue.idobjimp and " +
									" atr.idtipobjimpatr in (" + strIds +") and " +
									" cue.id = " + cuenta.getId();
								
								rsUbi = cn.createStatement().executeQuery(sqlAtr);
								while (rsUbi.next()) {
									if (rsUbi.getString(1).equals(idTipoParcela.toString())){
										tipoParcela = rsUbi.getString(2);
									}
									if (rsUbi.getString(1).equals(idUbiTerreno.toString())){
										ubicacionBaldio = rsUbi.getString(2);
									}
									if (rsUbi.getString(1).equals(idDomicilioFinca.toString())){
										domicilioFinca = rsUbi.getString(2);
									}
								}
								rsUbi.close();
								
								if(tipoParcela.trim().equals("1")){
									ubicacion = domicilioFinca;
								}else if(tipoParcela.trim().equals("2")){
									ubicacion = ubicacionBaldio;
								}
								
								log.debug(dtAtr.stop(" Atributos Consultados en:"));
							}
						}
						
						for(Long idTit: mapIdCuentaTitular.keySet()) { 
							Long idTitConstancia = null;
							List<Long> idsDeuda = mapIdCuentaTitular.get(idTit);
							
							// Se crea la constancia y conDeuTit
							// Deuda sigue al contribuyente ? 
							idTitConstancia = deudaSigueTitular ? idTit : null;
							constanciaDeu = plaEnvDeuPro.createConstanciaDeu4Envio(cuenta, ubicacion, procesoMasivo.getObservacion(), idTitConstancia);
							
							// Creamos los conDeuDet y los detalles de planilla
							for (Long idDeu: idsDeuda) {
								Deuda deuda = null;

								// Nota: 2 indicaria GeneraConstacia, Post Envio (por eso busca deuda en judicial)
		
								if (generaConstaciaPostEnvio) {
									deuda = Deuda.getByIdFirstJud(idDeu);
								} else {
									deuda = Deuda.getById(idDeu);
								}
																
								constanciaDeu.createConDeuDet(deuda);

								// Creamos detalle de planilla
								plaEnvDeuPDet = new PlaEnvDeuPDet();							
								plaEnvDeuPDet.setPlaEnvDeuPro(plaEnvDeuPro);
								plaEnvDeuPDet.setIdDeuda(idDeu);
								plaEnvDeuPDet.setIdEstPlaEnvDeuPD(2L); // OK							
								plaEnvDeuPro.createPlaEnvDeuPDet(plaEnvDeuPDet);
							}
							log.debug(dtconstancia.stop("Constancia creada en:"));
						}
					}
										
					// Commit cada 50 constancias
					if (c % 50 == 0){
						SiatHibernateUtil.currentSession().getTransaction().commit();
						SiatHibernateUtil.closeSession(); // cierro
						SiatHibernateUtil.currentSession().beginTransaction();
					}
					
					// Reset
					idCuentaAnt = idCuenta;
					mapIdCuentaTitular.clear();
					
					c++;
					cConstancias++;
				}
				
				// Acumulamos los ids de Deuda para cada contribuyente
				// Nota: si los intervalos desde-hasta de cuentatitular son correctos
				//       los registros de deuda no se tienen que duplicar.
				{
					List<Long> idsDeuda = mapIdCuentaTitular.get(idCuentaTitular);
					if (idsDeuda == null) { 
						idsDeuda = new ArrayList<Long>();
						mapIdCuentaTitular.put(idCuentaTitular, idsDeuda);
					}
					idsDeuda.add(idDeuda);
				}
				
				AdpRun.changeRunMessage(String.format("Procesando %s de %s registros. %s planillas y %s constancias generadas.", d, recCount, cPlanillas, cConstancias), 60); 
			}
			
			log.debug(" El Ultimo Commit lo realiza el proceso masivo ###########");
			
			log.debug( dt.stop(" generarPlanillasConstancias()"));
			
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { cn.close(); } catch (Exception ex) {}
		}
	}
	
	@Deprecated
	public void generarPlanillasConstanciasOld(ProcesoMasivo procesoMasivo) throws Exception {
		log.info("Info: Generando planillas y constancias Tradicional.");
		DemodaTimer dt = new DemodaTimer();

		Connection cn = null;
		String sql;
		Long idProcesoMasivo = procesoMasivo.getId();
		boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());

		Long idProcurador = null;
		Long idProcuradorAnt = null;
		Long idCuenta = null;
		Long idCuentaAnt = null;
		Long idDeuda = null;
		Long idPlaEnvDeuPro = null;
		Long idConstancia = null;
		Long idCuentaTitular = null;
		Map<Long, List<Long>> mapIdCuentaTitular = new HashMap<Long, List<Long>>(); //lista de id de deuda por titular

		Procurador procurador = null;
		PlaEnvDeuPro plaEnvDeuPro = null;
		Cuenta cuenta = null;
		ConstanciaDeu constanciaDeu = null;
		DeudaAdmin deudaAdmin = null;

		PlaEnvDeuPDet plaEnvDeuPDet = null;

		// IdProcurador - idPlaEnvDeuPro (Para el procesoMasivo)  
		HashMap<Long, Long> planillas = new HashMap<Long, Long>();
		// IdCuenta - IdConstancia  (Para el procesoMasivo)
		HashMap<Long, Long> constancias = new HashMap<Long, Long>();

		String ubicacion = "";
		ResultSet rsUbi = null;


		Long idDomicilioFinca = TipObjImpAtr.ID_DOMICILIOFINCA;
		Long idUbiTerreno = TipObjImpAtr.ID_UBICACIONTERRENO;
		Long idTipoParcela  = TipObjImpAtr.ID_PARCELA_TIPO_PARCELA;
		String sqlAtr = "";
		String tipoParcela = "";
		String ubicacionBaldio = "";
		String domicilioFinca = "";
		String strIds = idTipoParcela + ", " + idDomicilioFinca + ", " + idUbiTerreno;

		//boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());

		try {

			cn = SiatJDBCDAO.getConnection();

			// 1) Recuperamos las planillas ya cargadas.
			log.debug(" Cargando Planillas ...");
			sql = "select idprocurador, id from gde_plaenvdeupro where idprocesomasivo = " + idProcesoMasivo;
			ResultSet rs = cn.createStatement().executeQuery(sql);
			while (rs.next()) {
				idProcurador = rs.getLong("idprocurador");
				idPlaEnvDeuPro = rs.getLong("id");

				planillas.put(idProcurador, idPlaEnvDeuPro);
			}
			rs.close();

			// 2) Recuperamos las constancias ya cargadas.
			log.debug(" Cargando Constancias ...");
			sql = "select idcuenta, id from gde_constanciadeu where idprocesomasivo = " + idProcesoMasivo;
			rs = cn.createStatement().executeQuery(sql);
			while (rs.next()) {
				idCuenta = rs.getLong("idcuenta");
				idConstancia = rs.getLong("id");

				constancias.put(idCuenta, idConstancia);
			}
			rs.close();

			log.debug(" Consultando ProMasDeuInc ... ");
			// Consultamos cantidad de registros  Pro Mas Deu Inc
			sql = "select count(id) from gde_promasdeuinc di where di.idprocesomasivo = " + idProcesoMasivo;
			rs = cn.createStatement().executeQuery(sql);
			int recCount = 0;
			if (rs.next()){
				recCount = rs.getInt(1);
				log.debug(" ProMasDeuIn encontrados: " + recCount);
			}
			rs.close();

			// Consultamos de registros  Pro Mas Deu Inc para generar constancias
			sql = " select di.idprocurador, d.idcuenta, di.iddeuda " +
			" , tit.id idCuentaTitular " +
			" from gde_promasdeuinc di, gde_deudaadmin d " +
			", pad_cuentatitular tit" +
			" where " +
			" di.iddeuda = d.id " +
			" and d.idcuenta = tit.idcuenta " +
			(deudaSigueTitular
					? " and tit.fechaDesde <= d.fechaVencimiento and (tit.fechaHasta is null or tit.fechaHasta >= d.fechaVencimiento) "
							: " and tit.esTitularPrincipal = 1 ") +
							" and di.idprocesomasivo = " + idProcesoMasivo +
							" order by " +
							" di.idprocurador, d.idcuenta " +
							(deudaSigueTitular ? " , tit.esTitularPrincipal desc, tit.fechaDesde, tit.id " : "");

			rs = cn.createStatement().executeQuery(sql);

			int c = 1, d = 1;
			Map<Long, Long> idDeudaMap = new HashMap<Long, Long>();
			// Corte de control por procurador, cuenta, contribuyente
			while (rs.next()) {

				idProcurador = rs.getLong("idprocurador");
				idCuenta = rs.getLong("idcuenta");
				idDeuda = rs.getLong("iddeuda");
				idCuentaTitular = rs.getLong("idCuentaTitular");

				if (deudaSigueTitular) {
					if (idDeudaMap.containsKey(idDeuda))
						continue;

					idDeudaMap.put(idDeuda, idDeuda);
				}

				//log.debug(" inx: " + d  + " proc: " + idProcurador + " cue: " + idCuenta + " deu: " + idDeuda + " isLast: " + (d == recCount));

				if (idCuentaAnt == null){
					idCuentaAnt = idCuenta;
				}

				if (idProcuradorAnt == null){
					idProcuradorAnt = idProcurador;
				}

				// Acumulamos los ids de Deuda para cada contribuyente
				// Nota: si los intervalos desde-hasta de cuentatitular son correctos
				//       los registros de deuda no se tienen que duplicar.
				{
					List<Long> idsDeuda = mapIdCuentaTitular.get(idCuentaTitular);
					if (idsDeuda == null) {
						idsDeuda = new ArrayList<Long>();
						mapIdCuentaTitular.put(idCuentaTitular, idsDeuda);
					}
					idsDeuda.add(idDeuda);
				}

				// Corte de control por cuenta
				if (idCuenta.longValue() != idCuentaAnt.longValue() || d == recCount){

					// Preguntamos por existencia de planilla
					if (!planillas.containsKey(idProcurador)){

						DemodaTimer dtplanilla = new DemodaTimer();
						log.debug(" Creando planilla ....");
						// Creamos la planilla
						procurador = Procurador.getById(idProcurador);
						plaEnvDeuPro = procesoMasivo.createPlaEnvDeuPro(procurador);

						log.debug( dtplanilla.stop("Planilla creada en:") );

					} else {
						log.debug(" Planilla Existente....");
						plaEnvDeuPro = PlaEnvDeuPro.getById(planillas.get(idProcurador));
					}

					// Preguntamos por existencia de constancia
					if (!constancias.containsKey(idCuenta)){

						DemodaTimer dtconstancia = new DemodaTimer();
						log.debug(" Creando constancia y detalle ....");
						cuenta = Cuenta.getById(idCuentaAnt);

						// Obtenemos la ubicacion si posee objeto imponible
						if (cuenta.getObjImp() != null){
							if (cuenta.getObjImp().getTipObjImp().getId().equals(TipObjImp.COMERCIO) ){
								ubicacion = cuenta.getDesDomEnv();

							} else {

								DemodaTimer dtAtr = new DemodaTimer();

								sqlAtr = "select atr.idtipobjimpatr, atr.strvalor from pad_objimpatrval atr, pad_cuenta cue " +
								"where atr.idobjimp  = cue.idobjimp and " +
								"atr.idtipobjimpatr in (" + strIds +") and " +
								"cue.id = " + cuenta.getId();

								rsUbi = cn.createStatement().executeQuery(sqlAtr);
								while (rsUbi.next()) {
									if (rsUbi.getString(1).equals(idTipoParcela.toString())){
										tipoParcela = rsUbi.getString(2);
									}
									if (rsUbi.getString(1).equals(idUbiTerreno.toString())){
										ubicacionBaldio = rsUbi.getString(2);
									}
									if (rsUbi.getString(1).equals(idDomicilioFinca.toString())){
										domicilioFinca = rsUbi.getString(2);
									}
								}
								rsUbi.close();

								if(tipoParcela.trim().equals("1")){
									ubicacion = domicilioFinca;
								}else if(tipoParcela.trim().equals("2")){
									ubicacion = ubicacionBaldio;
								}

								log.debug(dtAtr.stop(" Atributos Consultados en:"));
							}
						}

						for(Long idTit: mapIdCuentaTitular.keySet()) {
							Long idTitConstancia = null;
							List<Long> idsDeuda = mapIdCuentaTitular.get(idTit);

							// Se crea la constancia y conDeuTit
							// Deuda sigue al contribuyente ? 
							idTitConstancia = deudaSigueTitular ? idTit : null;
							constanciaDeu = plaEnvDeuPro.createConstanciaDeu4Envio(cuenta, ubicacion, procesoMasivo.getObservacion(), idTitConstancia);

							for (Long idDeu: idsDeuda) {
								// Creamos los conDeuDet              
								deudaAdmin = DeudaAdmin.getById(idDeu);
								constanciaDeu.createConDeuDet(deudaAdmin);

								// Creamos detalle de planilla
								plaEnvDeuPDet = new PlaEnvDeuPDet();
								plaEnvDeuPDet.setPlaEnvDeuPro(plaEnvDeuPro);
								plaEnvDeuPDet.setIdDeuda(idDeu);
								plaEnvDeuPDet.setIdEstPlaEnvDeuPD(2L); // OK              
								plaEnvDeuPro.createPlaEnvDeuPDet(plaEnvDeuPDet);
							}

							log.debug(dtconstancia.stop("Constancia creada en:"));
						}
					}

					// Commit cada 50 constancias
					if (c % 50 == 0){
						log.debug(" Commit ###########");
						SiatHibernateUtil.currentSession().getTransaction().commit();
						SiatHibernateUtil.closeSession(); // cierro
						SiatHibernateUtil.currentSession().beginTransaction();
					}

					// Reset
					idCuentaAnt = idCuenta;
					mapIdCuentaTitular.clear();

					c++;
				}

				d++;
			}

			log.debug(" El Ultimo Commit lo realiza el proceso masivo ###########");

			log.debug( dt.stop(" generarPlanillasConstancias()"));

			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { cn.close(); } catch (Exception ex) {}
		}
	}
	
	public PrintModel getTotalesEnvioReport(ProcesoMasivo procesoMasivo) throws Exception {
		Connection cn = null;
		try {
			String sql;
			cn = SiatJDBCDAO.getConnection();
			
			sql = " select " + 
			"   di.idprocesomasivo idProcesoMasivo,  " + 
			"   count(distinct dj.idcuenta) cuentas,  " +
			"   sum(di.saldohistorico) saldoHistorico,  " +
			"   sum(di.saldoactualizado) saldoActualizado  " +
			" from  " +
			"   gde_promasdeuinc di, gde_deudajudicial dj  " +
			" where  " +
			"   di.iddeuda = dj.id and di.idprocesomasivo in (" + procesoMasivo.getId() + ")" +
			" group by  " +
			"   di.idprocesomasivo  ";
			
			// Armado del PDF.
			PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_TOTAL_ENVJUD); 
			
			// Datos del Encabezado Generico
			printModel.putCabecera("TituloReporte", "Totales de Envio Judicial");
			printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
			printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
			printModel.putCabecera("Usuario", procesoMasivo.getCorrida().getUsuarioUltMdf());
			
			// Datos del Encabezado del PDF
			printModel.setTituloReporte("Totales de Envio Judicial");
			printModel.putCabecera("IdProcesoMasivo", procesoMasivo.getId().toString());
			printModel.putCabecera("FechaEnvio", DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.dd_MM_YYYY_MASK));
			printModel.putCabecera("Recurso", procesoMasivo.getRecurso().getCodRecurso() + "-" + procesoMasivo.getRecurso().getDesRecurso());
			printModel.putCabecera("Caso", (String) DemodaUtil.nvl(procesoMasivo.getIdCaso(), ""));
			printModel.putCabecera("Observacion", (String) DemodaUtil.nvl(procesoMasivo.getObservacion(), ""));
					
			// Se arman una tabla para el Reporte Total del Envio
			TablaVO tabla = new TablaVO("TotalesEnvJud");
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Cuentas","cuentas"));
			filaCabecera.add(new CeldaVO("Saldo Historico","saldoHistorico"));
			filaCabecera.add(new CeldaVO("Saldo Actualizado","saldoActualizado"));
			tabla.setFilaCabecera(filaCabecera);

			ResultSet rs = cn.createStatement().executeQuery(sql);
			while (rs.next()) {
				Long cuentas = rs.getLong("cuentas");
				Double totalSaldoAct = rs.getDouble("saldoActualizado");
				Double totalSaldoHistorico = rs.getDouble("saldoHistorico");

				FilaVO fila = new FilaVO();
				fila.add(new CeldaVO(cuentas.toString(), "cuentas"));
				fila.add(new CeldaVO(StringUtil.formatDouble(totalSaldoHistorico, "##########.##") , "saldoHistorico"));
				fila.add(new CeldaVO(StringUtil.formatDouble(totalSaldoAct, "##########.##") , "saldoActualizado"));
				tabla.add(fila);
			}
		
			// Cargamos los datos en el Print Model
			printModel.setData(tabla);
			printModel.setTopeProfundidad(3);
			
			rs.close();			
			return printModel;
		} catch (Exception e) {
			throw e;
		} finally {
			try { cn.close(); } catch (Exception ex) {}
		}
	}

	public PrintModel getTotalesEnvioProcurReport(ProcesoMasivo procesoMasivo) throws Exception {
		Connection cn = null;
		try {
			String sql;
			cn = SiatJDBCDAO.getConnection();
			
			sql = "select " +
			"  di.idprocesomasivo idProcesoMasivo, " +
			"  di.idprocurador idProcurador,  " +
			"  count(distinct dj.idcuenta) cuentas, " + 
			"  sum(di.saldohistorico) saldoHistorico, " + 
			"  sum(di.saldoactualizado) saldoActualizado " +
			"from  " +
			"  gde_promasdeuinc di, gde_deudajudicial dj " +
			"where " +
			"  di.iddeuda = dj.id and di.idprocesomasivo in (" + procesoMasivo.getId() + ") " +
			"group by " +
			"  di.idprocesomasivo, di.idprocurador " +
			"order by idprocesomasivo, idprocurador ";

			// Armado del PDF.
			PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_TOTAL_ENVJUDPROCUR); 
			
			// Datos del Encabezado Generico
			printModel.putCabecera("TituloReporte", "Totales de Envio Judicial");
			printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
			printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
			printModel.putCabecera("Usuario", procesoMasivo.getCorrida().getUsuarioUltMdf());
			
			// Datos del Encabezado del PDF
			printModel.setTituloReporte("Totales de Envio Judicial");
			printModel.putCabecera("IdProcesoMasivo", procesoMasivo.getId().toString());
			printModel.putCabecera("FechaEnvio", DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.dd_MM_YYYY_MASK));
			printModel.putCabecera("Recurso", procesoMasivo.getRecurso().getCodRecurso() + "-" + procesoMasivo.getRecurso().getDesRecurso());
			printModel.putCabecera("Caso", (String) DemodaUtil.nvl(procesoMasivo.getIdCaso(), ""));
			printModel.putCabecera("Observacion", (String) DemodaUtil.nvl(procesoMasivo.getObservacion(), ""));
					
			// Se arman una tabla para el Reporte Total del Envio
			TablaVO tabla = new TablaVO("TotalesEnvJudProcur");
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Procurador","procurador"));
			filaCabecera.add(new CeldaVO("Cuentas","cuentas"));
			filaCabecera.add(new CeldaVO("Saldo Historico","saldoHistorico"));
			filaCabecera.add(new CeldaVO("Saldo Actualizado","saldoActualizado"));
			tabla.setFilaCabecera(filaCabecera);

			ResultSet rs = cn.createStatement().executeQuery(sql);
			double totalSaldoAct = 0, totalSaldoHis = 0;
			while (rs.next()) {
				Long cuentas = rs.getLong("cuentas");
				Double procSaldoHis = NumberUtil.truncate(rs.getDouble("saldoHistorico"), 2);
				Double procSaldoAct = NumberUtil.truncate(rs.getDouble("saldoActualizado"), 2);
				Procurador procurador = Procurador.getById(rs.getLong("idProcurador"));
				String desProcurador = procurador.getId() + "-" + procurador.getDescripcion(); 
								
				FilaVO fila = new FilaVO();
				fila.add(new CeldaVO(desProcurador, "procurador"));
				fila.add(new CeldaVO(cuentas.toString(), "cuentas"));
				fila.add(new CeldaVO(StringUtil.formatDouble(procSaldoHis, "##########.##") , "saldoHistorico"));
				fila.add(new CeldaVO(StringUtil.formatDouble(procSaldoAct, "##########.##") , "saldoActualizado"));
				
				totalSaldoHis +=procSaldoHis;
				totalSaldoAct +=procSaldoAct;
				tabla.add(fila);
			}

			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO("","vacio")); //procurador
			filaPie.add(new CeldaVO("","vacio")); //cuentas
			filaPie.add(new CeldaVO(StringUtil.formatDouble(totalSaldoHis, "##########.##"), "saldoHistorico"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(totalSaldoAct, "##########.##"), "saldoActualizado"));
			tabla.addFilaPie(filaPie);
		
			// Cargamos los datos en el Print Model
			printModel.setData(tabla);
			printModel.setTopeProfundidad(3);
			
			rs.close();
			return printModel;
		} catch (Exception e) {
			throw e;
		} finally {
			try { cn.close(); } catch (Exception ex) {}
		}
	}
}
