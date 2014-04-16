//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import coop.tecso.demoda.buss.bean.BaseBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.IDemodaEmun;
import coop.tecso.demoda.iface.model.PageModel;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.ReportColumnVO;
import coop.tecso.demoda.iface.model.ReportDatoVO;
import coop.tecso.demoda.iface.model.ReportPieVO;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Metodos para el uso de DAOs basados en Hibernate
 * @author tecso
 * */
public abstract class GenericAbstractDAO {
	
	private Logger log = Logger.getLogger(GenericAbstractDAO.class);
	
    protected Class bOClass;
   
    
    /* Este metodo debe implmentase de modo que retorno 
     * la session de hibernate a utilizar por el DAO.
     * Por lo general: <code>
     *   return XXXHibernateUtil.currentSession()
     * </code>
     */
    protected abstract Session currentSession();

    public GenericAbstractDAO(Class boClass) {
        this.bOClass = boClass;
    }

    public List getList() throws HibernateException {
        Session session = currentSession();
        return session.createQuery("from " + bOClass.getName()).list();
    }
    
    /**
     * Recupera un objecto de la base de datos dado su Id, 
     * si no existe arroja una HibernateException.
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    public Object getById(Long id) throws HibernateException {
    	return currentSession().load(bOClass, id);	
    	//return SweHibernateUtil.currentSession().load(bOClass, id);
    }
    
    /**
     * Recupera un objecto de la base de datos dado su Id, 
     * si no existe devuelve null.
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    public Object getByIdNull(Long id) throws HibernateException {
    	if (id == null)
    		return null;
    	else		
    		return currentSession().get(bOClass, id);
    }
    
    
    public List getList(Criteria criteria) throws HibernateException {
    	return criteria.list();
    }
    
    public List getList(Query query) throws HibernateException {
    	return query.list();
    }

    /**
     *  Obtiene el Id Serializado de un PreparedStatement.
     *  @param PreparedStatement
     *  @return Long
     */
    public long getSerial(PreparedStatement ps) throws Exception{
    	long runId = 0;
    	if(ps!=null){		
    		ResultSet rs = ps.getGeneratedKeys();
    		while (rs.next()) {
    			runId = rs.getInt(1);	 
    	    }
    		rs.close();
    	}
    	return runId;
    }
    /*
    public Long update(Object o) throws HibernateException {
        Long id = (Long) SweHibernateUtil.currentSession().save(o);
        return id;
    }
    */
    
    /**
     * GG 291106
     * Cada DAO extiende este y es un singleton
     * Al sincronizar, aseguramos que dos hilos trabajando sobre la misma instancia
     * no se pisen. (aunque es muy improbable que ocurra)
     */
    public synchronized Long update(BaseBean o) throws HibernateException {
    	try {
			String userName = DemodaUtil.currentUserContext().getUserName();
    		if (userName != null) {
    			o.setUsuarioUltMdf(userName);
    		} else { 
    			o.setUsuarioUltMdf("siat");
			}
    		o.setFechaUltMdf(new Date());
    	} catch (Exception e) {}
    	Long id = (Long) currentSession().save(o);
    	 	
    	return id;
    }
    
    public synchronized Long updateManualId(BaseBean o) throws HibernateException {
    	try {
			String userName = DemodaUtil.currentUserContext().getUserName();
    		if (userName != null) {
    			o.setUsuarioUltMdf(userName);
    		} else { 
    			o.setUsuarioUltMdf("siat");
			}
    		o.setFechaUltMdf(new Date());
    	} catch (Exception e) {}
    	
		currentSession().save(o,o.getId());

		return o.getId();
    }
    
    public void delete(Object o) throws HibernateException {
    	currentSession().delete(o);   
    }
    
    public int getCount(Criteria criteria) throws HibernateException {
    	return criteria.list().size();
    }
	
	public int getCount (Query query)throws HibernateException{
    	return query.list().size();
    }    
    
	public List getListOrdenada(String campoOrden)  throws HibernateException  {
		Session session = currentSession();
        return session.createQuery("from " + bOClass.getName() +" order by  " + campoOrden).list();
    }

	public List getListActiva()  throws HibernateException  {
		Session session = currentSession();
        return session.createQuery("from " + bOClass.getName() + " where estado = 1").list();
    }

	public List getListActivaExcluyendoListId(List<Long> listIdExcluidos)  throws HibernateException  {
		Session session = currentSession();
		String consulta = "FROM " + bOClass.getName() + 
		" WHERE estado = " + Estado.ACTIVO.getId().toString();
		if (listIdExcluidos != null && listIdExcluidos.size() > 0){
			consulta += " AND id NOT IN (" + ListUtil.getStringList(listIdExcluidos) + " )";
		}
		return session.createQuery(consulta).list();
    }


	public List executeSearch(String queryString, PageModel page) throws Exception{
		
		if (page.getReport().getImprimir()){
			
			this.executePrintSearch(queryString, page);
			return new ArrayList();
		}
		
		// obtenemos el resultado de la consulta
		Session session = currentSession();
	    Query query = session.createQuery(queryString);
	    
	    if (page.getPageNumber().longValue() == 0){
	    	page.setPageNumber(new Long(1));
	    }
		if (page.isPaged()) {
			page.setMaxRegistros(1000L); // para que funcione la paginacion del page porque no tiramos un count y si paginamos
			query.setMaxResults(page.getRecsByPage().intValue());
			query.setFirstResult(page.getFirstResult());
		}

		List listResult = query.list();

		if (page.isPaged() ) { // agregar && !page.getImprimir()
			
			// recalcula la ctd Max de registros de acuedo al size de la lista de resultados
			page.recalcularMaxRegistros(listResult.size());
		}
		
		return listResult;
	}

	public List executeCountedSearch(String queryString, PageModel page) throws Exception{
		
		if (page.getReport().getImprimir()){
			// realiza la ejecucion de la busqueda y la generacion del archivo del reporte
			this.executePrintSearch(queryString, page);
			return new ArrayList();
		}
		
		Session session = currentSession();
	    Query query;
	    Long cantidadMaxima;
		String queryCount = queryString;
		
		int posFrom = queryCount.toLowerCase().indexOf("from");
		
		if (posFrom > 1){
			queryCount = queryCount.substring(posFrom, queryString.length()-1);
		}
		
		int pos = queryCount.toLowerCase().indexOf("order by");
		if (pos >= 0) {
			queryCount = queryCount.substring(0, pos);
		}
	    query = session.createQuery("select count(*) " + queryCount);
	    cantidadMaxima = (Long) query.uniqueResult();
	    page.setMaxRegistros(cantidadMaxima);

		// obtenemos el resultado de la consulta
	    query = session.createQuery(queryString);
	    
		if (page.isPaged()) {
			query.setMaxResults(page.getRecsByPage().intValue());
			query.setFirstResult(page.getFirstResult());
		}

		return query.list();
	}
	
	/**
 		* Quita para el count los valores que se encuentren adelante del from
 		* @param queryString
 		* @param page
 		* @return
 		* @throws Exception
	*/	
	public List executeCountedQuery(String queryString, PageModel page) {
		

		
		Session session = currentSession();
	    Query query;
	    Long cantidadMaxima;
		String queryCount;
		
		int posFrom = queryString.toLowerCase().indexOf("from");

		queryCount = queryString;
		if (posFrom > 1){
			queryCount = queryString.substring(posFrom, queryString.length()-1);
		}
		int pos = queryCount.toLowerCase().indexOf("order by");
		if (pos >= 0) {
			queryCount = queryCount.substring(0, pos);
		} 
	    query = session.createQuery("select count(*) " + queryCount);
	    cantidadMaxima = (Long) query.uniqueResult();
	    page.setMaxRegistros(cantidadMaxima);

		// obtenemos el resultado de la consulta
	    query = session.createQuery(queryString);
	    
		if (page.isPaged()) {
			query.setMaxResults(page.getRecsByPage().intValue());
			query.setFirstResult(page.getFirstResult());
		}

		return query.list();
	}


	/**
	 * Obtiene la lista de resultados de la ejecucion de la consulta.
	 * 
	 * @param queryString HQL de la consulta a ejecutar
	 * @param maxResults  maxima ctd de resultados a obtener
	 * @param firstResult indice del 1er registro 
	 * @return List  lista de resultados obtenida
	 * @throws Exception
	 */
	public List executeSearch(String queryString, Integer maxResults, Integer firstResult) throws Exception{
		
		Session session = currentSession();
	    Query query = session.createQuery(queryString);
	    
	    if(maxResults != null){
	    	query.setMaxResults(maxResults);
	    }
	    if (firstResult != null){
	    	query.setFirstResult(firstResult);
	    }

		return  query.list();
	}

	/**
	 * Obtiene la lista de resultados de la ejecucion de la consulta sin paginar
	 * @param queryString
	 * @return List
	 * @throws Exception
	 */
	public List executeSearch(String queryString) throws Exception{
		return  executeSearch(queryString, null, null);
	}


	/**
	 * Ejecuta la busqueda y realiza la impresion
	 * @param queryString
	 * @param page
	 * @return obtiene la lista
	 * @throws Exception
	 */
	private void executePrintSearch(String queryString, PageModel page) throws Exception{
		
		// Obtiene el queryString adecuado para el conteo (saca el order by)
		String queryCount = "";
		int pos = queryString.toLowerCase().indexOf("order by");
		if (pos >= 0) {
			queryCount = queryString.substring(0, pos);
		} else {
			queryCount = queryString;
		}
		Session session = currentSession();
		Query query = null;
		
		// Obtener la parte previa al FROM
		int posFrom = queryCount.toLowerCase().indexOf("from");
		if (posFrom > 0){
			String previaFrom    = queryCount.substring(0, posFrom);
			String posteriorFrom = queryCount.substring(posFrom );
		
			int posSelect = previaFrom.toLowerCase().indexOf("select");
			if(posSelect >= 0){
				String ejecucion = previaFrom.substring(posSelect + 6);
				query = session.createQuery("SELECT COUNT( " + ejecucion + " ) "+ posteriorFrom);
			}
		}else{
			query = session.createQuery("SELECT COUNT(*) " + queryCount);
		}
		
		Long cantidadMaxima = (Long) query.uniqueResult();
		page.setMaxRegistros(cantidadMaxima);
		if(cantidadMaxima > page.getReport().getReportCtdMaxRes() ){
			page.addRecoverableValueError("Cantidad m&aacute;xima de registros superada. Por favor restrinja su b&uacute;squeda a&ntilde;adiendo m&aacute;s filtros.");
			return;
		}
		
		// Obtencion del query del resultado agregando el order by del report 
		String queryResultado = queryCount; 
		if (!StringUtil.isNullOrEmpty(page.getReport().getReportOrderBy())){
			queryResultado += " ORDER BY " + page.getReport().getReportOrderBy();
		}
		
		// impresion del resultado
		this.imprimirGenerico(queryResultado, page);
		return;
	}
 

	/**
	 * Retorna true si el parametro bo, posee hijos de la clase joinClass, relacionados mediante el parametro joinProperty.
	 * Este metodo requiere que exista previamente los mapeos de hibernate adecuados.
	 * <p>Ej: Para saber si una aplicacion posee usuarios asignados:
	 * <code>hasReference(apl, UsrApl.class, "aplicacion");</code>
	 * <br>donde apl es la instancia de la aplicacion que se desea saber si tiene o no usuarios.
	 * <br>donde UsrApl.class es la clase hija
	 * <br>"aplicacion" es el nombre de la propiedad que relaciona el hijo con el padre.
	 * <p>IMPLEMENTACION: este metodo simplemente terminado ejecutando el query: from UsrApl u where u.aplicacion = :apl
	*/
	public boolean hasReferenceGen(BaseBean bo, Class joinClass, String joinProperty) {
		String queryString = "from %s t where t.%s = :bo";
		Session session = currentSession();
		Query query = session.createQuery(String.format(queryString, joinClass.getName(), joinProperty));
		query.setEntity("bo", bo);
		query.setMaxResults(1);
		List list = (List) query.list();
		return list == null || list.isEmpty() ? false : true;
	}

	/**
	 * Retorna true si el parametro bo, posee hijos de la clase joinClass, relacionados mediante el parametro joinProperty y
	 * dichos Hijos posess estado == 1
	 * Este metodo requiere que exista previamente los mapeos de hibernate adecuados.
	 * <p>Ej: Para saber si una aplicacion posee usuarios asignados:
	 * <code>hasReference(apl, UsrApl.class, "aplicacion");</code>
	 * <br>donde apl es la instancia de la aplicacion que se desea saber si tiene o no usuarios.
	 * <br>donde UsrApl.class es la clase hija
	 * <br>"aplicacion" es el nombre de la propiedad que relaciona el hijo con el padre.
	 * <p>IMPLEMENTACION: este metodo simplemente terminado ejecutando el query: from UsrApl u where u.aplicacion = :apl and u.estado = 1
	*/
	public boolean hasReferenceActivoGen(BaseBean bo, Class joinClass, String joinProperty) {
		String queryString = "from %s t where t.%s = :bo and u.estado = 1";
		Session session = currentSession();
		Query query = session.createQuery(String.format(queryString, joinClass.getName(), joinProperty));
		query.setEntity("bo", bo);
    	List list = (List) query.list();
		return list == null || list.isEmpty() ? false : true;
	}

	private Object invokeGetter(String propName, Object obj) throws Exception {
		
		if(List.class.isInstance(obj)){
			return this.invokeGetter(propName, (List) obj);
		}
		
		String getterName = "get" + propName.substring(0,1).toUpperCase() + propName.substring(1);
		return obj.getClass().getMethod(getterName).invoke(obj, (Object[]) null);
	}
	
	private Object invokeGetter(String propName, List listObject) throws Exception {
		List resultados = new ArrayList();
		String getterName = "get" + propName.substring(0,1).toUpperCase() + propName.substring(1);
		for (Object object : listObject) {
			resultados.add(object.getClass().getMethod(getterName).invoke(object, (Object[]) null));
		}
		return resultados;
	}

	
	public Object invokeGetters(String propName, Object obj) throws Exception {
		if (!propName.contains(".")){
			return this.invokeGetter(propName, obj);
		}
		
		String[] propNames = propName.split("\\.");
		
		for (int i = 0; i < propNames.length; i++) {
			String pn = propNames[i];
			obj = this.invokeGetter(pn, obj);
			if(obj == null){
				break;	
			}
		}
		return obj;
	}


	/**
	 * Verifica que el objeto pasado por parametro no exista como duplicado en la base de datos.<p>
	 * El objeto se considera duplicado si:
	 *  Ya existe al menos un objeto en la base de datos que satisface las condiciones de 
	 *  igualdad para cada propiedad cargada en el UniqueMap excluyendo el objeto de la base de datos
	 *  cuyo id es igual al objeto pasado por parametro. 
	 * <p>Por ejemplo:
	 * <code>
	 *   uniqueMap.addString("codigo", "APS");
	 *   checkIsUnique(miAplicacion, uniqueMap);
	 * </code>
	 * Devuelve <code>true</code> si no existe otro registro, ademas de miAplicacion, en la base de datos
	 * que posea codigo igual a "APS". En caso contrario retorna <code>false</code>
	 * <p>La condicion de igualdad para el caso de String, puede ser Case Sensitive o No, dependiendo como
	 * se halla cargado en el UniqueMap.
	 * <p>ver clase UniqueMap()
	 * 
	*/
	public boolean checkIsUniqueGen(BaseBean obj, UniqueMap um) throws Exception {
		Class klass = obj.getClass();
		String queryString = "from " + klass.getName() + " t where ";
		
		/* Format query string */
		// Excluimos de la busqueda a el mismo, en caso de update
		if (obj.getId() != null && obj.getId() != 0) {
			queryString += " t.id <> " + obj.getId() + " and ";
		}
		// formateamos los parametros
		Iterator it = um.getFilters().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String name = ((String)pairs.getKey()).substring(1);
			String type = ((String)pairs.getKey()).substring(0,1);

			// type == S indica una busqueda NO CASE sensitive
			// type == s indica una busqueda CASE sensitive
			if ("S".equals(type)) {
			  queryString += " upper(t." + name + ") = :" + name;
			} else {
			  queryString += " t." + name + " = :" + name;
			}
			if (it.hasNext()) queryString += " and "; 
		}

		Query query = currentSession().createQuery(queryString);
		
		/* Set parametros al query */
		it = um.getFilters().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String type = ((String)pairs.getKey()).substring(0,1);
			String name = ((String)pairs.getKey()).substring(1);
			Object value = pairs.getValue();

			if (value.toString() == UniqueMap.REFLECT_COOKIE) {
				value = invokeGetter(name, obj);
			}

			if ("S".equals(type)) {
				query.setString(name, ((String) value).toUpperCase());
			} else if("s".equals(type)) {
				query.setString(name, ((String) value));
			} else if ("L".equals(type)) {
				query.setLong(name, (Long) value);
			} else if ("I".equals(type)) {
				query.setInteger(name, (Integer) value);		
			} else if ("E".equals(type)) {
				query.setEntity(name, value);
			}
		}

    	List list = (List) query.list();
		
		um.clean();
		return list == null || list.isEmpty() ? true : false;
	}

	/** Devuelve todos los BO cuyo id este en String de
	 *  id separados por coma pasados como parametro.
	 * 
	 * @param listId
	 * @return
	 * @throws HibernateException
	 */
	
	public List getListActivaByIds(String listId)  throws HibernateException  {
		Session session = currentSession();
		String consulta = "FROM " + bOClass.getName() + 
		" WHERE estado = " + Estado.ACTIVO.getId();
		if (listId != null && listId.length() > 0){
			consulta += " AND id IN (" + listId + " )";
		}
		return session.createQuery(consulta).list();
    }

	/**
	 * Devuelve todos los BO cuyo id esten en el rango entre idDesde e idHasta.
	 * Admite parametros nulos.
	 * @param idDesde
	 * @param idHasta
	 * @return List
	 * @throws HibernateException
	 */
	public List getListByRangoIds(Long idDesde, Long idHasta)  throws HibernateException  {
		Session session = currentSession();
		String consulta = "FROM " + bOClass.getName();
		boolean flagAnd = false;
		
		if(idDesde != null){
			consulta += flagAnd ? " and " : " where ";
			consulta += " id >= " + idDesde;
			flagAnd = true;
		}
		if(idHasta != null){
			consulta += flagAnd ? " and " : " where ";
			consulta += " id <= " + idHasta;
			flagAnd = true;
		}
		
		return session.createQuery(consulta).list();
    }


	/**
	 * Carga el contenedor de los datos a partir del query de la consulta y del page
	 * @param queryString consulta a ejecutar 
	 * @param  pageModel
	 * @return ContenedorVO
	 * @throws Exception
	 */
	private ContenedorVO cargarContenedor(String queryString, PageModel pageModel ) throws Exception{
		if(log.isDebugEnabled()) log.debug("cargarContenedor: QueryString = " + queryString + 
				" pageModel: " + pageModel.getClass());
	
		// obtencion de la lista de BO sin paginar
		List listBO = executeSearch(queryString);
		
		return this.cargarContenedor(listBO, pageModel.getReport());
	}

	/**
	 * Obtiene un ContenedorVO cargado con los objetos de la listaBO de acuerdo al ReportVO
	 * @param  listBO
	 * @param  report
	 * @return ContenedorVO
	 * @throws Exception
	 */
	private ContenedorVO cargarContenedor(List listBO , ReportVO report ) throws Exception{
		if(log.isDebugEnabled()) log.debug("cargarContenedor");
	
		ContenedorVO contenedorPrincipal = new ContenedorVO("");
		// copia de valores del report al contenedor
		contenedorPrincipal.setPageHeight(report.getPageHeight());
		contenedorPrincipal.setPageWidth(report.getPageWidth());
		
		for (Iterator iter = listBO.iterator(); iter.hasNext();) {
			BaseBean bean = (BaseBean) iter.next();
			// por cada procurador armar: el contenedor de procurador

			// por cada procurador-recurso:
			// armar la tabla proc-rec-comision
			// armar la tabla manzanero
			for (ReportVO subReport : report.getListReport()) {
				ContenedorVO contenedorProcurador = this.procesarReport(bean, subReport); // contiene la lista de procuradores 
				contenedorPrincipal.getListBloque().add(contenedorProcurador);
			}
		}
	
		// procesa la lista de tablas directas del pageModel
		for (ReportTableVO rtVO : report.getReportListTable()) {
			TablaVO tablaDirecta = null;
			String metodo = rtVO.getReportMetodo();
			for (Iterator iter = listBO.iterator(); iter.hasNext();) {
				BaseBean bean = (BaseBean) iter.next();
				if (StringUtil.isNullOrEmpty(metodo)){
					// proceso la fila directamente sobre el bean
					if(tablaDirecta == null) tablaDirecta = new TablaVO(rtVO.getNombre());
					// por cada procurador-recurso-comision armar la tabla PRC
					tablaDirecta.add(this.procesarFila(bean, rtVO));
					
				}else{
					// proceso la tabla sobre cada bean que se va a obtener al ejecutar el 'metodo' 
					TablaVO tVO = this.procesarReportTable(bean,rtVO);
					
					contenedorPrincipal.getListTabla().add(tVO);
				}
			}// fin iteracion bean del listResult
			
			if(tablaDirecta != null){
				
				tablaDirecta.setTitulo(rtVO.getTitulo());
				FilaVO filaCabecera = new FilaVO();
				for (ReportColumnVO rc : rtVO.getListReportColumns()) {
					filaCabecera.add(new CeldaVO(rc.getNombreColumna(), rc.getWidth()));
				}
				tablaDirecta.setFilaCabecera(filaCabecera);
				
				contenedorPrincipal.getListTabla().add(tablaDirecta);
			}
		}
		
		// procesar la tabla cabecera del contenedor principal: forman los filtros aplicados a
		// armado de la tabla filtros aplicados ubicada como cabecera
		TablaVO tablaFiltros  = new TablaVO("Filtros Aplicados");
		tablaFiltros.setTitulo(report.getReportFiltrosTitle());
		FilaVO  filaDeFiltros = new FilaVO();
		for (String claveFiltro : report.getReportFiltros().keySet()) {
			String valor = report.getReportFiltros().get(claveFiltro);
			filaDeFiltros.add(new CeldaVO(valor, claveFiltro, claveFiltro));
		}
		tablaFiltros.add(filaDeFiltros);
		
		contenedorPrincipal.setTablaFiltros(tablaFiltros);
		
		return contenedorPrincipal;
	}
	
	/**
	 * Obtiene el ContenedorVO a procesando partir del bean y el report
	 * @param  bean
	 * @param  report
	 * @return ContenedorVO
	 * @throws Exception
	 */
	private ContenedorVO procesarReport(BaseBean bean, ReportVO report) throws Exception{
		if(log.isDebugEnabled()) log.debug("procesarReport");
		if(log.isDebugEnabled()) log.debug("clase bean: " + bean.getClass());
		if(log.isDebugEnabled()) log.debug("reportBeanName del report: " + report.getReportBeanName());
		if(log.isDebugEnabled()) log.debug("metodo a ejecutar: " + report.getReportMetodo());
		
		ContenedorVO contenedorVO = new ContenedorVO(report.getReportBeanName());
		
		// dado el report de procurador invocar el metodo para cada procurador
		String metodo = report.getReportMetodo(); // tiene que ser listProRec

		if("".equals(metodo)) {
			// estamos en la lista de procuradores y el primer contenedor es sobre los procuradores
			
			//contenedorVO.setTablaCabecera(
			//		this.procesarReportDatos((BaseBean) bean, report.getReportDatos()));
			contenedorVO.setTablaCabecera(
			this.procesarReportDatos((BaseBean) bean, report.getReportListDato()));		
			
			contenedorVO.getTablaCabecera().setTitulo(report.getReportTitle());
			
			// agrega la lista de tablas: PRC y Manz al contenedor procurador-recurso si las hubiere
			contenedorVO.getListTabla().addAll(
					this.procesarReportTables((BaseBean) bean, report.getReportListTable()));

			// obtencion del contenedor de cada report contenido
			for (ReportVO subReport : report.getListReport()) {
				ContenedorVO cVO = this.procesarReport(bean, subReport);
				contenedorVO.getListBloque().add(cVO);
			}
			
			return contenedorVO;
		}	

		// ejecutar el metodo sobre el bean procurador, para obtener la lista de procurador-recurso
		List listResultado = (List) invokeGetters(metodo, bean); // procurador.getListProRec();
		// por cada procurador-recurso armar la tabla PRC y la tabla Manz 
		for (Object pr : listResultado) {
			
			//contenedorVO.setTablaCabecera( 
			//		this.procesarReportDatos((BaseBean) pr, report.getReportDatos()));
			contenedorVO.setTablaCabecera(
					this.procesarReportDatos((BaseBean) pr, report.getReportListDato()));
			
			contenedorVO.setTablaCabecera(
					this.procesarReportDatos((BaseBean) pr, report.getReportListDato()));
			
			// agrega la lista de tablas: PRC y Manz al contenedor procurador-recurso
			contenedorVO.getListTabla().addAll(
					this.procesarReportTables((BaseBean) pr, report.getReportListTable()));
			
			for (ReportVO subPM : report.getListReport()) {
				ContenedorVO cVO = this.procesarReport((BaseBean)pr, subPM);
				contenedorVO.getListBloque().add(cVO);
			}
		}
		
		return contenedorVO;
	}
	
	/**
	 * Obtiene la TablaVO con los datos obtenidos de ejecutar los metodos del reportDatos sobre el bean.
	 * Constituye la tabla Cabecera del Contenedor del Reporte.
	 * @param  bean
	 * @param  reportDatos
	 * @return TablaVO
	 * @throws Exception
	 */
	/*
	private TablaVO procesarReportDatos(BaseBean bean, Map<String, String> reportDatos) throws Exception{
		if(log.isDebugEnabled()) log.debug("procesarReportDatos");
		if(log.isDebugEnabled()) log.debug( "clase bean: " + bean.getClass());
		if(log.isDebugEnabled()) log.debug( "size del mapa reportDatos: " + reportDatos.size());
		
		TablaVO tablaCabecera = new TablaVO("tablaCabecera");
		
		FilaVO filaCabecera = new FilaVO();
		Set<String> listClaves = reportDatos.keySet();
		for (String  clave : listClaves) {
			String propName  = reportDatos.get(clave);
			Object resultado = invokeGetters(propName, bean);
			if(log.isDebugEnabled()) 
				log.debug("Clave: " + clave + ". PropNameInvocar: " + propName + ". Resultado: " + resultado);
			filaCabecera.add(new CeldaVO(this.formatearResultado(resultado),"_"+propName, clave));			
		}
		tablaCabecera.setFilaCabecera(filaCabecera);
		//tablaCabecera.setTitulo("");
		
		return tablaCabecera;
	}
	*/
	
	private TablaVO procesarReportDatos(BaseBean bean, List<ReportDatoVO> listReportDatos) throws Exception{
		if(log.isDebugEnabled()) log.debug("procesarReportDatos");
		if(log.isDebugEnabled()) log.debug( "clase bean: " + bean.getClass());
		if(log.isDebugEnabled()) log.debug( "size de la lista reportDatos: " + listReportDatos.size());
		
		TablaVO tablaCabecera = new TablaVO("tablaCabecera");
		
		FilaVO filaCabecera = new FilaVO();
		for (ReportDatoVO reportDato : listReportDatos) {
			String propName  = reportDato.getMetodoEjecutar();
			Object resultado = invokeGetters(propName, bean);
			String nombre = reportDato.getNombre();
			Class claseEnumeracion = reportDato.getClaseEnumeracion();
			if(log.isDebugEnabled())
				log.debug("Nombre: " + nombre + ". PropNameInvocar: " + propName + ". Resultado: " + resultado);
			if(claseEnumeracion != null){
				log.debug("Enumeracion: " + claseEnumeracion);
				resultado = getValorEnumeracion(claseEnumeracion,(Integer) resultado);
			}
			filaCabecera.add(new CeldaVO(this.formatearResultado(resultado),"_"+propName, nombre));
		}
		
		tablaCabecera.setFilaCabecera(filaCabecera);
		//tablaCabecera.setTitulo("");
		
		return tablaCabecera;
	}
	
	/**
	 * Obtiene la lista de TablaVO que componen el reporte
	 * @param  bean sobre el cual se genera cada una de las tablas
	 * @param  listReportTable de cada tabla a generar
	 * @return List<TablaVO>
	 * @throws Exception
	 */
	private List<TablaVO> procesarReportTables(BaseBean bean, List<ReportTableVO> listReportTable) throws Exception{
		if(log.isDebugEnabled()) log.debug("procesarReportTables");
		if(log.isDebugEnabled()) log.debug( "clase bean: " + bean.getClass());
		if(log.isDebugEnabled()) log.debug( "size del listReportTable: " + listReportTable.size());
		
		List<TablaVO> listTablaVO = new ArrayList<TablaVO>();
		
		// armar la tabla proc-rec-comision // armar la tabla manzanero
		for (ReportTableVO reportTableVO : listReportTable) {
			listTablaVO.add(this.procesarReportTable(bean, reportTableVO));
		}
		
		return listTablaVO;
	}

	/**
	 * Obtiene una TablaVO
	 * Ejecuta el reportMetodo del reportTable sobre el bean para obtener la lista de elementos de la tabla
	 * @param  bean
	 * @param  reportTable 
	 * @return TablaVO
	 * @throws Exception
	 */
	private TablaVO procesarReportTable(BaseBean bean, ReportTableVO reportTable) throws Exception{
		if(log.isDebugEnabled()) log.debug("procesarReportTable");
		if(log.isDebugEnabled()) log.debug("clase bean: " + bean.getClass());
		if(log.isDebugEnabled()) log.debug("reportMetodo a ejecutar: " + reportTable.getReportMetodo());
		
		// armar la tabla proc-rec-comision
		TablaVO tablaVO = new TablaVO(reportTable.getNombre());
		
		// ejecuta el reportMetodo del reportTable sobre el bean para obtener la lista de elementos de la tabla
		List listPRC = (List) invokeGetters(reportTable.getReportMetodo(), bean); // proRec.getListProRecCom()
		
		// por cada procurador-recurso-comision armar la tabla PRC 
		for (Object prc : listPRC) {
			log.debug("objeto prc: " + prc);
			tablaVO.add(this.procesarFila((BaseBean) prc, reportTable));
		}
		
		// armado de la fila cabecera
		FilaVO filaCabecera = new FilaVO();
		for (ReportColumnVO rc : reportTable.getListReportColumns()) {
			filaCabecera.add(new CeldaVO(rc.getNombreColumna(), rc.getWidth()));
		}

		tablaVO.setFilaCabecera(filaCabecera);
		tablaVO.setTitulo(reportTable.getTitulo());
		
		return tablaVO;
	}
	
	/**
	 * Obtiene una FilaVO ejecutando sobre el bean los metodos para cargar cada columna y pie 
	 * @param  bean
	 * @param  reportTable contiene el mapa de columnas y el mapa del pie.
	 * @return FilaVO
	 * @throws Exception
	 */
	private FilaVO procesarFila(BaseBean bean, ReportTableVO reportTable) throws Exception{
		if(log.isDebugEnabled()) log.debug("procesarFila " + bean + " reportTable: " + reportTable);
		
		FilaVO filaVO = new FilaVO(); // usamos la misma instancia

		for (ReportColumnVO rc : reportTable.getListReportColumns()) {
			CeldaVO celdaVO = this.procesarCelda(bean, rc); 
			filaVO.add(celdaVO);
		}
		
		return filaVO;
	}

	/**
	 * Ejecuta los metodos sobre el bean
	 * Si hay un solo metodo crea una sola celda,
	 * Si hay mas de un metodo, genera una celda que contiene una lista de celdas
	 * @param bean
	 * reportColumn ReportColumnVO
	 * @return CeldaVO
	 * @throws Exception
	 */
	private CeldaVO procesarCelda(BaseBean bean, ReportColumnVO reportColumn ) throws Exception{

		// obtiene, recorre y ejecuta los metodos para cada clave
		String[] propNames  = reportColumn.getMetodosEjecutar();

		ReportPieVO rp = reportColumn.getReportPie();

		if(propNames.length == 1){
			String propName = propNames[0];
			Object resultado = invokeGetters(propName, bean );

			if(log.isDebugEnabled()) 
				log.debug("Nombre Columna: " + reportColumn.getNombreColumna() + ". PropNameInvocar: " + 
						propName + ". Resultado: " + resultado);
			
			if(List.class.isInstance(resultado)){
				// si ejecuto por ejemplo listOficina.desOficina obtengo una lista de desOficina
				// resulta una celda con una lista de subceldas
				CeldaVO celdaVO = new CeldaVO();
				List listResultados = (List) resultado;
				List<CeldaVO> listSubCelda = new ArrayList<CeldaVO>();

				for (Object resu : listResultados) {
					listSubCelda.add(new CeldaVO( this.formatearResultado(resu), "_" + reportColumn.getNombreColumna()));
					this.procesarReportPie(rp, bean, resu);
				}
				
				celdaVO.setListCelda(listSubCelda);
				return celdaVO;
			}
			if(reportColumn.getClaseEnumeracion() != null){
				resultado = this.getValorEnumeracion(reportColumn.getClaseEnumeracion(),(Integer) resultado);
			}
			
			this.procesarReportPie(rp, bean, resultado);
			
			return new CeldaVO( this.formatearResultado(resultado), "_" + reportColumn.getNombreColumna());
		}

		// Creacion de una celda que contiene una lista de subceldas
		CeldaVO celdaVO = new CeldaVO();
		List<CeldaVO> listSubCelda = new ArrayList<CeldaVO>();

		for (int i = 0; i < propNames.length; i++) {
			String propName = propNames[i];
			Object resultado = invokeGetters(propName, bean );

			if(log.isDebugEnabled()) 
				log.debug("Nombre Columna: " + reportColumn.getNombreColumna() + ". PropNameInvocar: " + propName + ". Resultado: " + resultado);

			listSubCelda.add(new CeldaVO( this.formatearResultado(resultado), "_" + reportColumn.getNombreColumna()));

			this.procesarReportPie(rp, bean, resultado);

		}
		
		celdaVO.setListCelda(listSubCelda);
		
		return celdaVO;
	}
	
	private String formatearResultado(Object resultado){
		
		if(resultado == null) return "";
		
		if(Date.class.isInstance(resultado)){
			return DateUtil.formatDateForReport((Date)resultado);
		}
		
		return "" + resultado;
	}

	/**
	 * Realiza la suma o conteo del resultado y lo aplica al pie de columna
	 * @param rp
	 * @param bean
	 * @param resultado
	 * @throws Exception
	 */
	private void procesarReportPie(ReportPieVO rp, BaseBean bean, Object resultado) throws Exception{
		
		if (rp == null){return;}
		
		if (rp.getEsSuma()){
			rp.sumar((Number) resultado);
		}else if (rp.getEsCount()){
			// obtiene el id para contar los distintos 
			Long id = (Long) invokeGetters(rp.getPropiedad(), bean);
			rp.contar(id);
		}
	}
	
	/**
	 * Genera el Archivo XML.
	 * @param  contenedor de datos
	 * @param  printModel utilizado para escribir el archivo
	 * @param  fileNameXML path del directorio + nombre del archivo xml
	 * @return File
	 * @throws Exception
	 */
	private File generarXML(ContenedorVO contenedor, PrintModel printModel, String fileNameXML ) throws Exception{
		if(log.isDebugEnabled()) log.debug("genera el archivo XML");
		
		// archivo xml que contiene los datos del reporte
		OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(fileNameXML), "ISO-8859-1");
		
		// escritura del archivo xml que contiene los datos del reporte
		printModel.writeDataBegin(osWriter);
		
		printModel.writeDataObject(osWriter, contenedor,  10, "ContenedorVO"); // ver nivel 10
		printModel.writeDataEnd(osWriter);

		// cerramos el writer sobre el archivo xml generado
		osWriter.close();
		
		// apunta al Archivo xml generado
		File xmlTmpFile = new File(fileNameXML);
		
		return xmlTmpFile;
	}

	/**
	 * Realiza la impresion del Reporte 
	 * @param queryString de la consulta a ejecutar
	 * @param pageModel con los datos para generar el reporte
	 * @return PlanillaVO
	 * @throws Exception
	 */
	public PlanillaVO imprimirGenerico(String queryString, PageModel pageModel) throws Exception{
		if(log.isDebugEnabled()) log.debug("imprimirGenerico");
		
		// obtiene el contenedor de todos los datos del reporte ejecutando la consulta de acuerdo al contenido del pageModel
		ContenedorVO contenedorPrincipal = cargarContenedor(queryString , pageModel);
		//contenedorPrincipal.logearContenido();
		
		return imprimirGenerico(contenedorPrincipal, pageModel.getReport());
	}
	
	/**
	 * Realiza la impresion del Reporte en base a la lista de objetos y al report
	 * @param listBO
	 * @param report
	 * @return PlanillaVO
	 * @throws Exception
	 */
	public PlanillaVO imprimirGenerico(List listBO, ReportVO report) throws Exception{
		if(log.isDebugEnabled()) log.debug("imprimirGenerico");
		
		// obtiene el contenedor de todos los datos del reporte ejecutando la consulta de acuerdo al contenido del report
		ContenedorVO contenedorPrincipal = cargarContenedor(listBO , report);
		//contenedorPrincipal.logearContenido();
		
		return imprimirGenerico(contenedorPrincipal, report);
	}
	
	/**
	 * Realiza la impresion del Reporte en base al objeto y al report
	 * @param  bean
	 * @param  report
	 * @return PlanillaVO
	 * @throws Exception
	 */
	public PlanillaVO imprimirGenerico(BaseBean bean, ReportVO report) throws Exception{
		if(log.isDebugEnabled()) log.debug("imprimirGenerico");
		
		// cargo la lista de BaseBean a partir de la que se genera el contenedor solo con el bean
		List<BaseBean> list = new ArrayList<BaseBean>(); 
		list.add(bean);
		// reutilizacion del imprimirGenerico
		return imprimirGenerico(list, report);
	}


	/**
	 * Imprime un Contenedor de acuedo al report.
	 * @param  contenedorPrincipal
	 * @param  report
	 * @return PlanillaVO
	 * @throws Exception
	 */
	public PlanillaVO imprimirGenerico(ContenedorVO contenedorPrincipal, ReportVO report) throws Exception{
		if(log.isDebugEnabled()) log.debug("imprimirGenerico");
		
		PlanillaVO reporte = new PlanillaVO();
		
		String idUsuario = StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat());
		String fileDir = report.getReportFileDir();
		
		// Armado del XML con el uso del PrintModel
		PrintModel printModel = cargarPrintModel(report);
		printModel.setData(contenedorPrincipal);

		// archivo xml que contiene los datos del reporte
		String     fileNameXML   = "Reporte" + report.getReportBeanName() + idUsuario +".xml"; 
		File xmlFile = generarXML(contenedorPrincipal, printModel, fileDir + File.separator + fileNameXML);
		
		// Armado del PDF con el uso del PrintModel:

		// Archivo pdf a generar
		String fileNamePdf = report.getReportFileNamePdf();
		if(StringUtil.isNullOrEmpty(fileNamePdf)){
			fileNamePdf = "Reporte" + report.getReportBeanName() + idUsuario +".pdf";
		}
		File pdfFile = new File(fileDir + File.separator + fileNamePdf);
		
		// genera el archivo pdf en base al xml (y al xsl que contiene el printModel) 
		printModel.fopRender(xmlFile, pdfFile);
		
		reporte.setFileName(fileDir + File.separator + fileNamePdf);
		reporte.setDescripcion("Reporte de " + report.getReportBeanName() );
		//reporte.setCtdResultados(ctdBeans);
		
		report.setReportFileName(reporte.getFileName());
		
    	// reseteo del modo impresion a false
		report.setImprimir(false);

		return reporte;
	}

	
	
	/**
	 * Obtiene un PrintModel cargado con la cabecera y el XslString.
	 * @param pageModel
	 * @return PrintModel
	 */
	private PrintModel cargarPrintModel(ReportVO report) throws Exception{
		if(log.isDebugEnabled()) log.debug("cargaPrintModel a partir del pageModel");
		
		// Armado del XML con el uso del PrintModel
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(report.getReportFormat().intValue());
		printModel.putCabecera("FileSharePath", report.getReportFileSharePath());
		//printModel.setExcludeFileName(report.getReportExcludeFileName());
		
		// Datos del Encabezado del PDF
		printModel.putCabecera("TituloReporte", report.getReportTitle());
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", DemodaUtil.currentUserContext().getUserName());
		
		printModel.setTopeProfundidad(4);

		// obtencion del xsl a partir de un archivo y la carga en la propiedad XslString del PrintModel
		String pathCompletoArchivoXsl = report.getPathCompletoArchivoXsl();
		printModel.cargarXsl(pathCompletoArchivoXsl, PrintModel.RENDER_PDF);

		return printModel;
	}

	/**
	 * Metodo auxiliar que permite de una enumeracion ejecutar el metodo estatico getById y 
	 * luego a la instancia obtenida, ejecutar el metodo getValue()
	 * @param enumeracion
	 * @param id
	 * @return String
	 * @throws Exception
	 */
	
	public String getValorEnumeracion(String nombreEnumeracion, Integer id) throws Exception{
		
		String getterName = "getById" ;
		IDemodaEmun instanciaEnum = (IDemodaEmun) Class.forName(nombreEnumeracion).getMethod(getterName,Integer.class).invoke(null, id);
		return instanciaEnum.getValue();
	}
	public String getValorEnumeracion(Class claseEnumeracion, Integer id) throws Exception{
		
		String getterName = "getById" ;
		IDemodaEmun instanciaEnum = (IDemodaEmun) claseEnumeracion.getMethod(getterName,Integer.class).invoke(null, id);
		return instanciaEnum.getValue();
	}

	
	/**
	 *  
	 * 
	 * @param obj
	 * @param um
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public boolean checkConcurrencyGen(BaseBean obj, UniqueMap claveFuncional, String userName) throws Exception {
		Class klass = obj.getClass();
		String queryString = "from " + klass.getName() + " t where ";
		
		/* Format query string */
		// Excluimos de la busqueda a el mismo, en caso de update
		if (obj.getId() != null && obj.getId() != 0) {
			queryString += " t.id <> " + obj.getId() + " and ";
		}
		// formateamos los parametros
		Iterator it = claveFuncional.getFilters().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String name = ((String)pairs.getKey()).substring(1);
			String type = ((String)pairs.getKey()).substring(0,1);

			// type == S indica una busqueda NO CASE sensitive
			// type == s indica una busqueda CASE sensitive
			if ("S".equals(type)) {
			  queryString += " upper(t." + name + ") = :" + name;
			} else {
			  queryString += " t." + name + " = :" + name;
			}
			if (it.hasNext()) queryString += " and "; 
		}
		
		// TODO: queryString += " and t.fechaUltMdf = TO_DATE('" + DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		queryString += " and t.usuarioUltMdf <> '" + userName + "'";
		
		log.debug("checkConcurrency -> queryString: " + queryString );
		
		Query query = currentSession().createQuery(queryString);
		
		/* Set parametros al query */
		it = claveFuncional.getFilters().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String type = ((String)pairs.getKey()).substring(0,1);
			String name = ((String)pairs.getKey()).substring(1);
			Object value = pairs.getValue();

			if (value.toString() == UniqueMap.REFLECT_COOKIE) {
				value = invokeGetter(name, obj);
			}

			if ("S".equals(type)) {
				query.setString(name, ((String) value).toUpperCase());
			} else if("s".equals(type)) {
				query.setString(name, ((String) value));
			} else if ("L".equals(type)) {
				query.setLong(name, (Long) value);
			} else if ("I".equals(type)) {
				query.setInteger(name, (Integer) value);		
			} else if ("E".equals(type)) {
				query.setEntity(name, value);
			}
		}

    	List list = (List) query.list();
		
    	claveFuncional.clean();
		return list == null || list.isEmpty() ? true : false;
	}
	
	
	/**
	 * Imprime un Contenedor de acuedo al report.
	 * @param  contenedorPrincipal
	 * @param  report
	 * @return PlanillaVO
	 * @throws Exception
	 */
	public PlanillaVO imprimirGenericoSinTrim(ContenedorVO contenedorPrincipal, ReportVO report) throws Exception{
		if(log.isDebugEnabled()) log.debug("imprimirGenerico");
		
		PlanillaVO reporte = new PlanillaVO();
		
		String idUsuario = StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat());
		String fileDir = report.getReportFileDir();
		
		// Armado del XML con el uso del PrintModel
		PrintModel printModel = cargarPrintModel(report);
		printModel.setData(contenedorPrincipal);
		printModel.setNoAplicarTrim(true);
		
		// archivo xml que contiene los datos del reporte
		String     fileNameXML   = "Reporte" + report.getReportBeanName() + idUsuario +".xml"; 
		File xmlFile = generarXML(contenedorPrincipal, printModel, fileDir + File.separator + fileNameXML);
		
		// Armado del PDF con el uso del PrintModel:

		// Archivo pdf a generar
		String fileNamePdf = report.getReportFileNamePdf();
		if(StringUtil.isNullOrEmpty(fileNamePdf)){
			fileNamePdf = "Reporte" + report.getReportBeanName() + idUsuario +".pdf";
		}
		File pdfFile = new File(fileDir + File.separator + fileNamePdf);
		
		// genera el archivo pdf en base al xml (y al xsl que contiene el printModel) 
		printModel.fopRender(xmlFile, pdfFile);
		
		reporte.setFileName(fileDir + File.separator + fileNamePdf);
		reporte.setDescripcion("Reporte de " + report.getReportBeanName() );
		//reporte.setCtdResultados(ctdBeans);
		
		report.setReportFileName(reporte.getFileName());
		
    	// reseteo del modo impresion a false
		report.setImprimir(false);

		return reporte;
	}
}
