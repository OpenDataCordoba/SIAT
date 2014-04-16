//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import ar.gov.rosario.siat.def.iface.model.TipoAtributoVO;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.iface.model.ObjImpSearchPage;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ObjImpDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ObjImpDAO.class);	
	private static Logger logSt = Logger.getLogger("migrania.statistics");
	
	private static long migId = -1;
	
	public ObjImpDAO() {
		super(ObjImp.class);
	}
	
	/**
	 * GetBySearchPage convencional, filtra por tipo objeto imponible y claveFuncional
	 * 
	 * 
	 * @param objImpSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<ObjImp> getBySearchPage(ObjImpSearchPage objImpSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ObjImp t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ObjImpSearchPage: " + objImpSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (objImpSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// Recupero si existe la mascara del atributo que corresponde a clave funcional
		log.debug("getBySearchPage -> MascaraClaveFunc: " +	objImpSearchPage.getTipObjImpDefinition().getMascaraClaveFunc());
		
		// filtro por tipoObjImp
		if (!ModelUtil.isNullOrEmpty(objImpSearchPage.getObjImp().getTipObjImp())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipObjImp.id = " + objImpSearchPage.getObjImp().getTipObjImp().getId();
			flagAnd = true;
		}
		
		// filtro por clave funcional
 		if (!StringUtil.isNullOrEmpty(objImpSearchPage.getObjImp().getClaveFuncional() )) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.claveFuncional)) like '" + 
				StringUtil.escaparUpper(objImpSearchPage.getClaveFuncional()) + "%'";
			flagAnd = true;
		}
 		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ObjImp> listObjImp = (ArrayList<ObjImp>) executeSearch(queryString, objImpSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listObjImp;
	}
	
	/**
	 * 
	 * GetBySearchPage para la busqueda avanzada
	 * <p> Filtra por tipo Objeto y imponible y por cada un de los valores de 
	 * los atributos del Objeto Imponible que se hallan submitido. 
	 * 
	 * @param objImpSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<ObjImp> getBySearchPageAva(ObjImpSearchPage objImpSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String strFrom  = "SELECT t FROM ObjImp t ";
		String strWhere = "";
		String strJoin = "";
		String strWhereAva = "";
		String queryString= "";
		String alias = "";
		String strSectionWhereAva = "";

		int contAlias = 1; // Contador ulilizado para crear los nombres de los alias cada vez que se joinea con ObjImpAtrVal
		
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ObjImpSearchPage: " + objImpSearchPage.infoString()); 
		}
		
		// Armamos filtros del HQL dinamicamente 
		// Recorro la definicion del tipo objeto imposible y por cada 
		// Atributo Valor que se encuentre "valorizado" agrego una clausula al HQL
		// Teniendo en cuenta la mismas reglas que se usaron para armar la GUI de busqueda (combo, checkbox, text, text desde y hasta) y 
		// el tipo de datos (Date, Long, String, Double).
		for (TipObjImpAtrDefinition tipObjImpAtrDefinition: objImpSearchPage.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
			
			// Si posee valor cargago en la GUI
			if ( !tipObjImpAtrDefinition.getValorString().trim().equals("") && 
					!tipObjImpAtrDefinition.getValorString().trim().equals("-1") ){
				
				// Para los casos de multivalor este join se arma mas abajo, en la seccion de multivalor.
				if (!tipObjImpAtrDefinition.getEsMultivalor()){
					alias = "objAtrVal";
					alias = alias + contAlias;
					strFrom += ", ObjImpAtrVal " + alias; 
					strJoin += " AND " + alias + ".objImp.id = t.id ";
					 
					contAlias++;

					strSectionWhereAva += " AND " + alias + ".tipObjImpAtr.id = " + tipObjImpAtrDefinition.getTipObjImpAtr().getId();
				}
				
				// Posee Dominio 
				if (tipObjImpAtrDefinition.getPoseeDominio()) { 
					
					// Admite busqueda por rango (Checkbox)
					if (tipObjImpAtrDefinition.getAdmBusPorRan() ){
						// Bucle, si hay mas de un valor chequeado, utilizar el OR
						strSectionWhereAva += " AND (";
						Iterator it = tipObjImpAtrDefinition.getListValor().iterator();
						
						while (it.hasNext()) {
							Object[] valor = (Object[])it.next();
							
							strSectionWhereAva += str4TipoDato(alias , tipObjImpAtrDefinition, (String)valor[0], "=");
							
							if (it.hasNext())
								strSectionWhereAva += " OR ";
						}
						
						strSectionWhereAva += " ) ";
						
					} else {
						// NO es multivalor
						if (!tipObjImpAtrDefinition.getEsMultivalor()){
							// No Adminte busqueda por rango (Combo) y existe valor seleccionado
							if (!tipObjImpAtrDefinition.getValorString().trim().equals("-1")){
								strSectionWhereAva +=  " AND " + str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorString(), "=");
							}
						} else {
						// es MULTIVALOR	
							
							// Bucle, si hay mas de un valor chequeados
							
							Iterator it = tipObjImpAtrDefinition.getListValor().iterator();
							
							while (it.hasNext()) {
								Object[] valor = (Object[])it.next();
								
								alias = "objAtrVal";
								alias = alias + contAlias;
								strFrom += ", ObjImpAtrVal " + alias; 
								strJoin += " AND " + alias + ".objImp.id = t.id ";
								 
								contAlias++;
								
								strSectionWhereAva += " AND " + alias + ".tipObjImpAtr.id = " + tipObjImpAtrDefinition.getTipObjImpAtr().getId();
								
								strSectionWhereAva += " AND " + str4TipoDato(alias , tipObjImpAtrDefinition, (String)valor[0], "=");
								
							}
						}
					}
				}
				
				// No posee dominio
				if (!tipObjImpAtrDefinition.getPoseeDominio() ) {
					
					// Admite Busqueda por rango (Text desde y Text Hasta)
					if (tipObjImpAtrDefinition.getAdmBusPorRan()){
						// Desde
						if (!tipObjImpAtrDefinition.getValorDesdeString().trim().equals("")){
							strSectionWhereAva +=  " AND " + str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorDesdeString(), ">=");
						}
						
						// Hasta
						if (!tipObjImpAtrDefinition.getValorHastaString().trim().equals("")){
							strSectionWhereAva +=  " AND " + str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorHastaString(), "<=");							
						}
						
					// No admite Busqueda por rango (Text)
					} else {
						strSectionWhereAva +=  " AND " + str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorString(), "=");
					}					
				}
				
				strWhereAva += strSectionWhereAva;
				
				log.debug("		Atributo: " + tipObjImpAtrDefinition.getAtributo().getCodAtributo());
				log.debug( strSectionWhereAva );
				
				if (tipObjImpAtrDefinition.getEsMultivalor()){
					String multi = "";
					for (String str:tipObjImpAtrDefinition.getListMultiValor()){
						multi += " - " + str;
					}
					log.debug("EsMultivalor: " + multi);
				}
				
				strSectionWhereAva = "";
			}
			
		}
		
		// filtro por estado si el modo es seleccionar
		if (objImpSearchPage.getModoSeleccionar()) {
			strWhere = " t.estado = "+ Estado.ACTIVO.getId() + strJoin;
		}
		
		// filtro por tipoObjImp
		if (!ModelUtil.isNullOrEmpty(objImpSearchPage.getObjImp().getTipObjImp())){
			strWhere = " t.tipObjImp.id = " + objImpSearchPage.getObjImp().getTipObjImp().getId() + strJoin;
		}
		
		queryString += strFrom + " where " + strWhere + strWhereAva;
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": strFrom : " + strFrom);
	    if (log.isDebugEnabled()) log.debug(funcName + ": strWhere : " + strWhere);
	    if (log.isDebugEnabled()) log.debug(funcName + ": strJoin : " + strJoin);
	    if (log.isDebugEnabled()) log.debug(funcName + ": strWhereAva : " + strWhereAva);
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query Avanzada: " + queryString);

	    List<ObjImp> listObjImp = (ArrayList<ObjImp>) executeSearch(queryString, objImpSearchPage);
	    	
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listObjImp;

	}
	
	/**
	 * Devuelve las siguientes cadenas segun el tipo de datos del AtrValDefinicion recibido:
	 * <p>String -> alias.strValor like '%valor%'
	 * <p>Long, Double ->  (alias.strValor *1) [operador] valor o (alias.strValor) = 'valor'
	 * <p>Date -> TO_DATE('" + alias.strValor +"','%d/%m/%Y') = DateUtil.formatDate( valor, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
	 * 
	 * @param alias
	 * @param tipObjImpAtrDefinition
	 * @param operador
	 * @return
	 */
	public String str4TipoDato(String alias, AtrValDefinition tipObjImpAtrDefinition, String valor, String operador) {
		String strRet = "";
		
		// String
		if (TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(tipObjImpAtrDefinition.getAtributo().getTipoAtributo().getCodTipoAtributo())){
			strRet = " " + alias + ".strValor like '%" + valor + "%' "; 
		}
		// Catastral
		if (TipoAtributoVO.COD_TIPO_ATRIB_CATASTRAL.equals(tipObjImpAtrDefinition.getAtributo().getTipoAtributo().getCodTipoAtributo())){
			strRet = " " + alias + ".strValor " + operador + "'" + valor + "'"; 
			//strRet = " " + alias + ".strValor like '" + valor + "%' ";
		}
		// Long Double
		if (TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(tipObjImpAtrDefinition.getAtributo().getTipoAtributo().getCodTipoAtributo())
				|| TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(tipObjImpAtrDefinition.getAtributo().getTipoAtributo().getCodTipoAtributo())) {
			if ("=".trim().equals(operador) || "==".trim().equals(operador)) {
				strRet = " " + alias + ".strValor " + operador + " '" + valor + "' ";
			} else {
				strRet = " " + alias + ".strValor * 1 " + operador + " " + valor + " ";
			}
		}
		// Date
		if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(tipObjImpAtrDefinition.getAtributo().getTipoAtributo().getCodTipoAtributo())){			
			strRet = " TO_DATE(" + alias + ".strValor,'%Y%m%d') " + operador + " TO_DATE('" + valor + "','%Y%m%d')";
		}
		
		return strRet;
	}
	
	/**
	 * Obtiene el Objeto Imponible a partir del Tipo de Objeto Imponible y el Numero de Cuenta .
	 * 
	 * @return ObjImp
	 */
	public ObjImp getByTipObjImpYNroCta(Long idTipObjImp, String nroCta) throws Exception {
		ObjImp objImp;
		String queryString = "from ObjImp t where t.tipObjImp.id = :idTipObjImp and t.clave = :numeroCuenta";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("idTipObjImp", idTipObjImp);
		query.setString("numeroCuenta", nroCta);
		objImp = (ObjImp) query.uniqueResult();	

		return objImp; 
	}
		

	/**
	 * Obtiene el Objeto Imponible a partir del Tipo de Objeto Imponible y el Numero de Cuenta .
	 * 
	 * @return ObjImp
	 */
	public ObjImp getByTipObjImpYClaveFuncional(Long idTipObjImp, String claveFuncional) throws Exception {
		ObjImp objImp;
		String queryString = "from ObjImp t where t.tipObjImp.id = :idTipObjImp and t.claveFuncional = :cf";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("idTipObjImp", idTipObjImp);
		query.setString("cf", claveFuncional);
		objImp = (ObjImp) query.uniqueResult();	

		return objImp; 
	}

	 public Long createJdbc(ObjImp o) throws Exception {
		 long msPasados=0;
		 msPasados = System.currentTimeMillis();
		 long runId = 0;
		 String sql = "";
		 sql += " insert into pad_objimp"; 
		 sql += " (idtipobjimp,clave,clavefuncional,fechaalta,fechabaja,usuario,fechaultmdf,estado)";
		 sql += " values (?,?,?,?,?,?,?,?)";
		
		 Connection cn = SiatHibernateUtil.currentSession().connection();

		 PreparedStatement ps = cn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		 ps.setLong(1, o.getTipObjImp().getId());
		 ps.setString(2, o.getClave());
		 ps.setString(3, o.getClaveFuncional());
		 ps.setTimestamp(4, new Timestamp(o.getFechaAlta().getTime()));//Timestamp.valueOf(DateUtil.formatDate(o.getFechaAlta(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));
		 if(o.getFechaBaja()!=null){
			 ps.setTimestamp(5, new Timestamp(o.getFechaBaja().getTime()));//Timestamp.valueOf(DateUtil.formatDate(o.getFechaBaja(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 }else{
			 ps.setNull(5, java.sql.Types.TIMESTAMP );
		 }
		 ps.setString(6, "siat");
		 ps.setTimestamp(7, new Timestamp(Calendar.getInstance().getTimeInMillis()));//Timestamp.valueOf(DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 ps.setInt(8, 1);
		 
		 ps.executeUpdate();
	     runId = this.getSerial(ps);

	     ps.close();
		 o.setId(runId);
		 msPasados = System.currentTimeMillis() - msPasados;
		 logSt.info("Save in db: "+o.getClass().getName()+" ----------------- t = "+msPasados+" mseg");
	
		 return runId;
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
	  *  Inserta una linea con los datos del Objeto Imponible para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param objImp, output - El Objeto Imponible a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(ObjImp o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idtipobjimp|clave|clavefuncional|fechaalta|fechabaja|usuario|fechaultmdf|estado
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getTipObjImp().getId());		 
		 line.append("|");
		 line.append(o.getClave());		 
		 line.append("|");
		 line.append(o.getClaveFuncional());
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaAlta(), "yyyy-MM-dd"));
		 line.append("|");
		 if(o.getFechaBaja()!=null){
			line.append(DateUtil.formatDate(o.getFechaBaja(), "yyyy-MM-dd"));		 
		 } // Si es null no se inserta nada, viene el proximo pipe.
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
	  * Lista de Objetos Imponibles de tipo Parcela con Catastral que contenga a la pasada y ordenada por catastral.
	  * 
	  * @param catastral
	  * @return listObjImp
	  */

	 /*
	 public List<ObjImp> getListParcelaByCatastral(String catastral) {
		List<ObjImp> listObjImp = new ArrayList<ObjImp>();
		List<Object[]> listObject;
		//List<String> listString;
		String queryString = "from ObjImp t where t.tipObjImp.id = 1 and t.claveFuncional like '"+catastral+"%' order by t.claveFuncional";
		//String queryString  = "select t, atr from ObjImp t LEFT OUTER JOIN t.listObjImpAtrVal atr where t.tipObjImp.id = 1 and t.claveFuncional like '"+catastral+"%' and  atr.objImp.id = t.id and atr.tipObjImpAtr.id = 25 order by atr.strValor, t.claveFuncional";
		//String queryString  = "select t, atr from ObjImpAtrVal atr RIGHT OUTER JOIN atr.objImp t where t.tipObjImp.id = 1 and t.claveFuncional like '"+catastral+"%'and  atr.objImp.id = t.id and atr.tipObjImpAtr.id = 25 order by atr.strValor, t.claveFuncional";
		//String querySQLString  = "select t.id, t.clave, t.clavefuncional from pad_objImp t, OUTER pad_objImpAtrVal atr where t.idtipObjImp = 1 and t.claveFuncional like '"+catastral+"%' and  atr.idobjImp = t.id and atr.idtipObjImpAtr = 25 order by atr.strValor, t.claveFuncional";
		Session session = SiatHibernateUtil.currentSession();


		Query query = session.createQuery(queryString);
		//Query query = session.createSQLQuery(querySQLString);
		listObjImp = (ArrayList<ObjImp>) query.list();	
		//listObject = (ArrayList<Object[]>) query.list();
		
		//System.out.println(((ObjImp) listObject.get(0)[0]).getClave());
		
		//System.out.println(listObject.getClass().getName());
		//System.out.println(listObject.size());
		//System.out.println(listObject.isEmpty());
		//System.out.println(listObject.get(0).getClass().getName());
		//for(Object[] o: listObject)
		//	System.out.println(o[0].getClass().getName());
		//	listObjImp.add((ObjImp) o[0]);
		//listString = (ArrayList<String>) query.list();
		
		//System.out.println(""+session.createSQLQuery("").list());
		
		return listObjImp; 
	}
	*/

	 public List<ObjImp> getListParcelaByCatastral(String catastral) {
		List<ObjImp> listObjImp = new ArrayList<ObjImp>();
		String queryString = "FROM ObjImp objImp WHERE objImp.tipObjImp.id = 1 " +
			"and objImp.claveFuncional like '" + catastral + "%' order by objImp.claveFuncional";

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		listObjImp = (ArrayList<ObjImp>) query.list();	

		return listObjImp; 
	}
	 
	 /*
	 public List<ObjImp> getListParcelaByCatastrales(String catastral1, String catastral2) {
		List<ObjImp> listObjImp = new ArrayList<ObjImp>();
		String queryString = "FROM ObjImp objImp WHERE objImp.tipObjImp.id = 1 " +
			"AND (objImp.claveFuncional LIKE '" + catastral1 + "%' OR " +
			"objImp.claveFuncional LIKE '" + catastral2 + "%') " + "order by objImp.claveFuncional";

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		listObjImp = (ArrayList<ObjImp>) query.list();	

		return listObjImp; 
	}
	*/

	/**
	 * Recupera todos los parcelas cuya catastral coincida
	 * con alguna de las dos pasadas como parametro.
	 * 
	 * 
	 * @param objImpSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<ObjImp> getListParcelaByCatastrales(String catastral1, String catastral2) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del catastral1: " +  catastral1 + 
					" catastral2: " + catastral2 );
		}

		String queryString = "FROM ObjImp objImp WHERE objImp.tipObjImp.id = "+ TipObjImp.PARCELA +" AND ";
		String queryCat1 = "";
		String queryCat2 = "";

		// filtro por tipoObjImp
		if ( !StringUtil.isNullOrEmpty(catastral1) ){
			queryCat1 = "objImp.claveFuncional LIKE '" + catastral1 + "%' ";
		}
		
		if ( !StringUtil.isNullOrEmpty(catastral2) ){
			queryCat2 = "objImp.claveFuncional LIKE '" + catastral2 + "%' ";
		}

		String orderBy = "order by objImp.claveFuncional ";
		
		if ( !StringUtil.isNullOrEmpty(catastral1) && 
			!StringUtil.isNullOrEmpty(catastral2)){
			queryString = queryString + "(" + queryCat1 + " OR " +  queryCat2 + ") " + orderBy;  
		} else {
			queryString = queryString + queryCat1 + queryCat2  + orderBy;
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		List<ObjImp> listObjImp = (ArrayList<ObjImp>) query.list();	

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listObjImp;
	}

	public static void setMigId(long migId) {
		ObjImpDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

	public List<ObjImp> getListObjImpByTipObj(Long idTipObjImp) {
		
		List<ObjImp> listObjImp = new ArrayList<ObjImp>();
		String queryString = "FROM ObjImp objImp WHERE objImp.tipObjImp.id = " + idTipObjImp;

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		listObjImp = (ArrayList<ObjImp>) query.list();	

		return listObjImp; 
	}

}

