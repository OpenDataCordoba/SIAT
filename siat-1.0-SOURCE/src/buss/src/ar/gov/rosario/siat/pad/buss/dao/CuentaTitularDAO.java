//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Cuit;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class CuentaTitularDAO extends GenericDAO {

	private Logger log = Logger.getLogger(CuentaTitularDAO.class);	
	
	private static long migId = -1;
	
	public CuentaTitularDAO() {
		super(CuentaTitular.class);
	}

	/**
	 * Obtiene la lista de cuentas del Contribuyente ordenadas por descripcion del recurso y por fecha desde
	 * @param contribuyente
	 * @return List<CuentaTitular>
	 */
	public List<CuentaTitular> getListOrderByRecursoFecDesde(Contribuyente contribuyente){
		
		Session session = SiatHibernateUtil.currentSession();
	    String consulta = "FROM CuentaTitular ct WHERE ct.contribuyente = :contribuyente " +
			"ORDER BY ct.cuenta.recurso.desRecurso, ct.fechaDesde ";  
	    
	    Query query = session.createQuery(consulta).setEntity("contribuyente", contribuyente );
	    
	    return (ArrayList<CuentaTitular>) query.list();
	}
	
	
	/**
	 * Determina si el Contribuyente registra ser titular de cuenta
	 * @param contribuyente
	 * @return Boolean
	 */
	public Boolean registraTitularDeCuentaByContribuyente(Contribuyente contribuyente){
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "SELECT COUNT(ct) FROM CuentaTitular ct " +
			" WHERE ct.contribuyente = :contribuyente ";
    	Query query = session.createQuery(sQuery).setEntity("contribuyente", contribuyente );
    	
    	Long cantidad = (Long) query.uniqueResult(); 				
    	return cantidad > 0;

	}
	
	/**
	 * Obtiene la CuentaTitular para la Cuenta y el Contribuyente indicados 
	 * @param cuenta, contribuyente
	 * @return cuentaTitular
	 */
	public CuentaTitular getByCuentaYContribuyente(Cuenta cuenta, Contribuyente contribuyente){
    	Session session = SiatHibernateUtil.currentSession();
    	
    	String queryString = "FROM CuentaTitular c " +
			" WHERE c.cuenta.id = " + cuenta.getId()+
			" AND c.contribuyente.id = " + contribuyente.getId() +
			" AND (c.fechaHasta is null OR c.fechaHasta  > TO_DATE('" +
				DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
    	
    	Query query = session.createQuery(queryString);
		CuentaTitular cuentaTitular = (CuentaTitular) query.uniqueResult();
		
    	return cuentaTitular;

	}
	
	
	/**
	 * Obtiene las CuentaTitular para la Cuenta y fecha de vigencia desde indicados 
	 * Ordenado de tal manera que aparece primiero el titular principal.
	 * No cambiar este orden.
	 * 
	 * @param cuenta, contribuyente
	 * @return cuentaTitular
	 */
	public List<CuentaTitular> getByCuentaYFechaVigencia(Cuenta cuenta, Date fecha){
    	Session session = SiatHibernateUtil.currentSession();
    	
    	String queryString = "FROM CuentaTitular c " +
			" WHERE c.cuenta.id = " + cuenta.getId();
			
    	queryString += " AND (c.fechaHasta is null OR c.fechaHasta  > TO_DATE('" +
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) " +
			" ORDER BY esTitularPrincipal DESC";
    	
    	Query query = session.createQuery(queryString);
		
		return (ArrayList<CuentaTitular>) query.list();
	}
	
	/**
	 * Obtiene la cuenta titular principal para la cuenta dada.
	 * Si la cuenta no tiene tiene titulares principales vigentes, 
	 * retorna la cuentatitular con mayor id
	 * Si la cuenta no tiene titulares devuelve null
	 * @param cuenta
	 * @return CuentaTitular
	 */
	public CuentaTitular getPrincipalByCuenta(Cuenta cuenta) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
    	String queryString = "FROM CuentaTitular ct " +
			" WHERE ct.cuenta.id = " + cuenta.getId() +
			" and (ct.fechaHasta is null or ct.fechaHasta > :fechaVig)" + 
			" AND ct.esTitularPrincipal = " + SiNo.SI.getId();
	    	//" ORDER BY id desc";
    	
    	Query query = session.createQuery(queryString);
    	query.setParameter("fechaVig", new Date());
    	
    	CuentaTitular titular = (CuentaTitular) query.uniqueResult();
    	return titular;
    	//List<CuentaTitular> listTitular = query.list();

    	/*if (listTitular.isEmpty()) {
    		//cuenta.addRecoverableValueError("La Cuenta " + cuenta.getNumeroCuenta() + " no posee titulares.");
			return null;
    	}
    	
    	//buscamos el primero que esta marcado como principal.
    	for(CuentaTitular ct : listTitular) {
    		if (SiNo.SI.getId().equals(ct.getEsTitularPrincipal())) {
    			return ct;
    		}
    	}
    	//si estoy aca es que no econtre un titular principal.
    	return listTitular.get(0);
    	*/
	}

	
	/**
	 *  Devuelve el proximo valor de id a asignar. 
	 *  Para se inicializa obteniendo el ultimo id asignado el archivo de migracion con datos pasados como parametro
	 *  y luego en cada llamada incrementa el valor.
	 * 
	 * @return long - el proximo id a asignar.
	 * @throws Exception
	 */
	public long getNextId(String path, String nameFile) throws Exception{
		// Si migId==-1 buscar MaxId en el archivo de load. Si migId!=-1, migId++ y retornar migId;
		if(getMigId()==-1){
			setMigId(this.getLastId(path, nameFile)+1);
		}else{
			setMigId(getMigId() + 1);
		}
		 
		return getMigId();
	}
	 
	/**
	 *  Inserta una linea con los datos de la CuentaTitular para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param cuentaTitular, output - La CuentaTitular a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(CuentaTitular o, LogFile output) throws Exception {

		// Obtenemos el valor del id autogenerado a insertar.
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|idcuenta|idcontribuyente|idtipotitular|estitularprincipal|fechadesde|fechanovedad|esaltamanual|usuario|fechaultmdf|estado|
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getCuenta().getId());		 
		line.append("|");
		line.append(o.getContribuyente().getId());		 
		line.append("|");
		line.append(o.getTipoTitular().getId());		 
		line.append("|");
		line.append(o.getEsTitularPrincipal());		 
		line.append("|");
		line.append(DateUtil.formatDate(o.getFechaDesde(), "yyyy-MM-dd"));
		line.append("|");
		if(o.getFechaNovedad()!=null){
			line.append(DateUtil.formatDate(o.getFechaNovedad(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.	 
		line.append("|");
		line.append(o.getEsAltaManual());		 
		line.append("|");
		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");
		 
		line.append("2010-01-01 00:00:00");
		 
		line.append("|");
		line.append("1");
		line.append("|");

		if(o.getFechaHasta()!=null){
			line.append(DateUtil.formatDate(o.getFechaHasta(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.	 
		line.append("|");
		 
		output.addline(line.toString());
		 
		// Seteamos el id generado en el bean.
		o.setId(id);
	
		return id;
	}
	 
	/**
	 * Obtiene las cuentas titular vigentes para una cuenta en una fecha
	 * Poniendo el titular principal al principio de la lista.
	 * (No cambiar este orden.)
	 * @param  cuenta
	 * @param  fecha
	 * @return List<CuentaTitular>
	 */
	public List<CuentaTitular> getListCuentaTitularVigentes(Cuenta cuenta, Date fecha) {
		Session session = SiatHibernateUtil.currentSession();
	    	
		String queryString = "FROM CuentaTitular ct " +
			" WHERE ct.cuenta = :cuenta " +
			" AND ct.fechaDesde <= :fecha " +
			" AND (ct.fechaHasta IS NULL OR ct.fechaHasta > :fecha) " +
			" order by esTitularPrincipal desc";
	    	
		Query query = session.createQuery(queryString)
			.setEntity("cuenta", cuenta)
			.setDate("fecha", fecha);

		return (ArrayList<CuentaTitular>) query.list();
	}

	/**
	 * Obtiene las cuentas titular vigentes para una cuenta en una fecha,
	 * considerando un intervalo de tiempo cerrado.
	 * 
	 * Pone el titular principal al principio de la lista.
	 * (No cambiar este orden.)
	 * @param  cuenta
	 * @param  fecha
	 * @return List<CuentaTitular>
	 */
	public List<CuentaTitular> getListCuentaTitularVigentesCerrado(Cuenta cuenta, Date fecha) {
		Session session = SiatHibernateUtil.currentSession();
	    	
		String queryString = "FROM CuentaTitular ct " +
			" WHERE ct.cuenta = :cuenta " +
			" AND ct.fechaDesde <= :fecha " +
			" AND (ct.fechaHasta IS NULL OR ct.fechaHasta >= :fecha) " +
			" order by esTitularPrincipal desc";
	    	
		Query query = session.createQuery(queryString)
			.setEntity("cuenta", cuenta)
			.setDate("fecha", fecha);

		return (ArrayList<CuentaTitular>) query.list();
	}
	
	
	/**
	 * Retorna el nombre y apellido o descripcion juridica del titular principal de la cuenta.
	 * <p>Si chekOtros es true, verficia si existes algun otro titular vigente al dia de hoy y agrega la cadena "y Otros" en caso que exista alguno mas del principal.
	 * <p>Si la cuenta no tiene ningun titular principal retorna, retorna el de mayor id de CuentaTitular (osea el ultimo insertado.)
	 * <p>Si la cuenta no tiene ningun titular de cuenta, retorna "";
	 */
	public String getNombreTitularPrincipal(Cuenta cuenta, boolean checkOtros) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection con;
		PreparedStatement ps;
		ResultSet rs;
		long count = 0;
		String ret = "";
		String apellido = "";
		String nombre = "";

		// ahora nos conseguimos la connection JDBC de hibernate...
		SiatHibernateUtil.currentSession().flush();
		con = currentSession().connection();
		String generalDbName = SiatParam.getGeneralDbName();

		String sql = "select cuentatit.id, cuentatit.esTitularPrincipal, pid.id_persona, per.apellido, per.nombre_per from ";
		sql += " pad_cuentatitular cuentatit ";
		sql += "   INNER JOIN " + generalDbName + ":persona_id pid on (pid.id_persona = cuentatit.idContribuyente) ";
		sql += "   INNER JOIN " + generalDbName + ":personas per on (pid.cuit00 = per.cuit00 and pid.cuit01 = per.cuit01 and pid.cuit02 = per.cuit02 and pid.cuit03 = per.cuit03) ";
		sql += " where ";
		sql += "   cuentatit.idCuenta = ?"  ;
		sql += "   and (cuentatit.fechaHasta is null or cuentatit.fechaHasta > ?) ";
		sql += " order by cuentatit.esTitularPrincipal desc, cuentatit.id desc";

		// ejecucion de la consulta de resultado
		if (log.isDebugEnabled()) log.debug("queryString: " + sql + " idcuenta=" + cuenta.getId() + " fechaDesde= ahora!");
		ps = con.prepareStatement(sql);
		ps.setLong(1, cuenta.getId());
		ps.setTimestamp(2, new Timestamp(new Date().getTime()));
		rs = ps.executeQuery();

		//probamos si es persona fisica
		while(rs.next()) {
			if (count == 0) {
				apellido = rs.getString("apellido");
				nombre = rs.getString("nombre_per");
			}
			count++;
			if (!checkOtros)
				break;
		}
		rs.close();
		ps.close();

		//probamos si es persona juridica
		if (count == 0)  {
			sql = "select cuentatit.id, cuentatit.esTitularPrincipal, pid.id_persona, soc.razon_soc from ";
			sql += " pad_cuentatitular cuentatit ";
			sql += "   INNER JOIN " + generalDbName + ":persona_id pid on (pid.id_persona = cuentatit.idContribuyente) ";
			sql += "   INNER JOIN " + generalDbName + ":sociedades soc on (pid.cuit00 = soc.cuit00_soc and pid.cuit01 = soc.cuit01_soc and pid.cuit02 = soc.cuit02_soc and pid.cuit03 = soc.cuit03_soc) ";
			sql += " where ";
			sql += "   cuentatit.idCuenta = ?"  ;
			sql += "   and (cuentatit.fechaHasta is null or cuentatit.fechaHasta > ?) ";
			sql += " order by cuentatit.esTitularPrincipal desc, cuentatit.id desc";

			if (log.isDebugEnabled()) log.debug("queryString: " + sql + " idcuenta=" + cuenta.getId() + " fechaDesde= ahora!");
			ps = con.prepareStatement(sql);
			ps.setLong(1, cuenta.getId());
			ps.setTimestamp(2, new Timestamp(new Date().getTime()));
			rs = ps.executeQuery();
			while(rs.next()) {
				if (count == 0) {
					apellido = rs.getString("razon_soc");
					nombre = "";
				}
				count++;
				if (!checkOtros)
					break;
			}
			rs.close();
			ps.close();
		}

		if (count>0) {
			if (nombre == null) nombre = "";
			if (apellido == null) apellido = "";
			ret = apellido.trim() + " " + nombre.trim();
			ret = ret.toUpperCase();
			if (count > 1) ret += " Y OTROS"; 
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return ret;
	}

	//TODO: para usar en la migracion. Hay que hacer que filtre por idContribuyente, no por idCuenta 
	public Cuit getCuit(Cuenta cuenta) throws Exception {
		
		Connection con;
		PreparedStatement ps;
		ResultSet rs;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		String generalDbName = SiatParam.getGeneralDbName();
		String sql = "select pid.cuit00, pid.cuit01, pid.cuit02, pid.cuit03 from ";
		sql += " pad_cuentatitular cuentatit ";
		sql += "   INNER JOIN " + generalDbName + ":persona_id pid on (pid.id_persona = cuentatit.idContribuyente) ";
		sql += " where ";
		sql += "   cuentatit.idCuenta = ?"  ;		
		if (log.isDebugEnabled()) log.debug("queryString: " + sql + " idcuenta=" + cuenta.getId() + " fechaDesde= ahora!");
		ps = con.prepareStatement(sql);
		ps.setLong(1, cuenta.getId());
		
		rs = ps.executeQuery();
		while(rs.next()) {
			log.debug(rs.getObject(1));
			log.debug(rs.getObject(2));
			log.debug(rs.getObject(3));
			log.debug(rs.getObject(4));
		}
		rs.close();
		ps.close();
		return null;
	}

	/**
	 * Devuelve una lista de Ids de las Cuentas que tienen un unico Titular y no es principal.
	 * (usado en la migracion para completar los titulares principales)
	 * 
	 * @return
	 */
	public List<Long> getListIdCuentaWithOneTitular(){
		List<Object[]> listIdCuenta;
		Session session = SiatHibernateUtil.currentSession();
    	
		String querySQLString = "select idcuenta, max(estitularprincipal) from pad_cuentatitular" +
					" group by idcuenta having count(idcuenta) = 1 and max(estitularprincipal) = 0" +
					" order by idcuenta";
		//select idcuenta, max(estitularprincipal)  
		//from pad_cuentatitular
		//group by idcuenta
		//having count(idcuenta) = 1 and max(estitularprincipal) = 0 
		//order by idcuenta
		
		Query query = session.createSQLQuery(querySQLString);
		listIdCuenta = (ArrayList<Object[]>) query.list();
		List<Long> listIdCuentaLong = new ArrayList<Long>();
		for(Object[] idCta:listIdCuenta){
			if(idCta != null)
				listIdCuentaLong.add(new Long((Integer) idCta[0]));
		}
		
		return listIdCuentaLong;
	}
	
	/**
	 * Obtiene un mapa con idCuenta y idPersona.
 	 * 
	 * @return mapa
	 */
	public Map<String, String> getMapaTitularPrincipalYCuenta() throws Exception {
    	Session session = SiatHibernateUtil.currentSession();

    	Map<String, String> mapCtaPer = new HashMap<String, String>();

    	String querySQLString = "select idCuenta, idContribuyente FROM pad_CuentaTitular ct " +
			" WHERE (ct.fechaHasta is null or ct.fechaHasta > :fechaVig)" + 
			" AND ct.esTitularPrincipal = " + SiNo.SI.getId();
    	
    	Query query = session.createSQLQuery(querySQLString);
    	query.setParameter("fechaVig", new Date());
    	
    	//CuentaTitular titular = (CuentaTitular) query.uniqueResult();
    	List<Object[]> listIdCtaPer = (ArrayList<Object[]>) query.list();
    	
    	if(listIdCtaPer != null){
    		for(Object[] idCtaPer: listIdCtaPer){
    			mapCtaPer.put(((Integer) idCtaPer[0]).toString(), ((Integer) idCtaPer[1]).toString());
    		}
    	}
    	
    	return mapCtaPer;
	}

	
	/**
 	 * Obtiene una lista de cuentasTitulares para el contribuyente pasado como parametro y excluye de la lista 
 	 * que retorna, la cuenta pasada como parametro
	 * @param contribuyente
	 * @param cuentaExcluir  - Esta cuenta no se incluira en la lista que retorna
	 * @return
	 */
	public List<CuentaTitular> getList(Contribuyente contribuyente, Cuenta cuentaExcluir) {
		Session session = SiatHibernateUtil.currentSession();
    	
		String queryString = "FROM CuentaTitular ct " +
			" WHERE ct.cuenta != :cuenta " +
			" AND ct.contribuyente = :contribuyente";
//			" AND ct.fechaDesde <= :fecha " +
//			" AND (ct.fechaHasta IS NULL OR ct.fechaHasta > :fecha) " +
	//		" order by esTitularPrincipal desc";
	    	
		Query query = session.createQuery(queryString)
			.setEntity("cuenta", cuentaExcluir)
			.setEntity("contribuyente", contribuyente);

		return (ArrayList<CuentaTitular>) query.list();

	}
	
	
	public List<CuentaTitular> getListByContribuyente(Contribuyente contribuyente) {
		Session session = SiatHibernateUtil.currentSession();
    	
		String queryString = "FROM CuentaTitular ct " +
			" WHERE ct.contribuyente = :contribuyente";
//			" AND ct.fechaDesde <= :fecha " +
//			" AND (ct.fechaHasta IS NULL OR ct.fechaHasta > :fecha) " +
	//		" order by esTitularPrincipal desc";
	    	
		Query query = session.createQuery(queryString)
			.setEntity("contribuyente", contribuyente);

		return (ArrayList<CuentaTitular>) query.list();

	}

	public static void setMigId(long migId) {
		CuentaTitularDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

	
	/**
	 *  Verifica si el contribuyente es titular vigente para la Cuenta en la fecha desde pasada.
	 *  
	 * @param cuenta
	 * @param contribuyente
	 * @param fechaDesde
	 * @return Boolean
	 */
	public Boolean existeComoTitularVigente(Cuenta cuenta, Contribuyente contribuyente, Date fechaDesde){
    	Session session = SiatHibernateUtil.currentSession();
    	
		String queryString = "FROM CuentaTitular ct WHERE ct.contribuyente = :contribuyente and ct.cuenta = :cuenta ";
		queryString += " and ct.fechaDesde <= :fechaDesde ";
		queryString += " and (ct.fechaHasta IS NULL OR ct.fechaHasta >= :fechaDesde)";

    	Query query = session.createQuery(queryString).setEntity("contribuyente", contribuyente )
    												  .setEntity("cuenta", cuenta)
    												  .setDate("fechaDesde", fechaDesde);
    	query.setMaxResults(1);
    	
    	CuentaTitular cuentaTitular = (CuentaTitular) query.uniqueResult(); 				
    	
    	if(cuentaTitular != null)
    		return true;
    	
    	return false;
	}
}
