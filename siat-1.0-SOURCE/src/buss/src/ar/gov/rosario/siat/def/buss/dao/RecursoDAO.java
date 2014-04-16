//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Tipo;
import ar.gov.rosario.siat.def.iface.model.RecursoSearchPage;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class RecursoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecursoDAO.class);	
	
	public RecursoDAO() {
		super(Recurso.class);
	}
	
	public List<Recurso> getListBySearchPage(RecursoSearchPage recursoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Recurso t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del RecursoSearchPage: " + recursoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (recursoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Recurso excluidos
 		List<RecursoVO> listRecursoExcluidos = (ArrayList<RecursoVO>) recursoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listRecursoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listRecursoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codRecurso
 		if (!StringUtil.isNullOrEmpty(recursoSearchPage.getRecurso().getCodRecurso())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codRecurso)) like '%" + 
				StringUtil.escaparUpper(recursoSearchPage.getRecurso().getCodRecurso()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por desRecurso
 		if (!StringUtil.isNullOrEmpty(recursoSearchPage.getRecurso().getDesRecurso())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desRecurso)) like '%" + 
				StringUtil.escaparUpper(recursoSearchPage.getRecurso().getDesRecurso()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por Categoria
 		if(!ModelUtil.isNullOrEmpty(recursoSearchPage.getRecurso().getCategoria())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.categoria = " +  recursoSearchPage.getRecurso().getCategoria().getId();
			flagAnd = true;
		}
 		
 		// filtro por Tipo de Categoria
 		// Se quita el tipo ya que se accede desde diferentes opciones de menu segun el tipo
 		/*if(!ModelUtil.isNullOrEmpty(recursoSearchPage.getRecurso().getCategoria().getTipo())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.categoria.tipo = " +  recursoSearchPage.getRecurso().getCategoria().getTipo().getId();
			flagAnd = true;
		}*/
 		
 		
 		
 		Long idTipo;
 		if(recursoSearchPage.isEsNoTrib())
 			idTipo=Tipo.ID_TIPO_NOTRIB;
 		else
 			idTipo=Tipo.ID_TIPO_TRIBUTARIA;
 		
 		queryString += flagAnd ? " and ":"where";
 		queryString += " t.categoria.tipo = " + idTipo;
 		// Order By
		queryString += " order by t.codRecurso ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Recurso> listRecurso = (ArrayList<Recurso>) executeCountedSearch(queryString, recursoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecurso;
	}

	/**
	 * Obtiene lista de Recursos Activos para la Categoria especificada
	 * @author tecso
	 * @param Long idCategoria	
	 * @return List<Recurso> 
	 */
	public List<Recurso> getListActivosByIdCategoria(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Recurso t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idCategoria: " + id); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.categoria.id = " + id;

 		// Order By
		queryString += " order by t.desRecurso ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Recurso> listRecurso = (ArrayList<Recurso>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecurso;
	}
	
	/**
	 * Obtiene la lista de todos los recursos asociados a la categoria
	 * Ordenada por descripcion del recurso
	 * (Obtiene solo los Recursos Activos)
	 * @param  id de la categoria
	 * @return List<Recurso>
	 */
	public List<Recurso> getListByIdCategoria(Long id) {			
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM Recurso t ";
		queryString += " WHERE t.categoria.id = " + id;
		queryString += " AND t.estado = "+ Estado.ACTIVO.getId();
		queryString += " ORDER BY t.desRecurso ";
		
	    Query query = session.createQuery(queryString);
	    return (ArrayList<Recurso>) query.list();
	}


	/**
	 * Obtiene lista de Recursos Principales Activos para el Tipo de Objeto Imponible especificado
	 * @author Tecso
	 * @param Long idTipObjImp	
	 * @return List<Recurso> 
	 */
	public List<Recurso> getListRecPriActivosByIdTipObjImp(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Recurso t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idCategoria: " + id); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.tipObjImp.id = " + id;
        
        queryString += " and t.esPrincipal = 1";

 		// Order By
		queryString += " order by t.desRecurso ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Recurso> listRecurso = (ArrayList<Recurso>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecurso;
	}
	
	
	public List<Recurso> getListVigentes(Date date) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM Recurso r " +
							" WHERE r.fechaAlta <= :fecha AND " +
							" ( r.fechaBaja IS NULL OR r.fechaBaja > :fecha ) " + 
							" ORDER BY r.categoria";
    	
		Query query = session.createQuery(sQuery)
    					.setDate("fecha", date);
    	
    	return  (ArrayList<Recurso>) query.list();
    }
 
	@SuppressWarnings("unchecked")
	public List<Recurso> getListTributarios() throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM Recurso r " +
							" WHERE r.categoria.tipo.id = 1" + 
							" ORDER BY r.categoria, r.desRecurso";
    	
		Query query = session.createQuery(sQuery);
    	
    	return  (ArrayList<Recurso>) query.list();
    }
	
	public List<Recurso> getListTributariosVigentes(Date date) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM Recurso r " +
							" WHERE r.fechaAlta <= :fecha AND " +
							" r.categoria.tipo.id=1 AND " +
							" ( r.fechaBaja IS NULL OR r.fechaBaja > :fecha ) " + 
							" ORDER BY r.categoria, r.desRecurso";
    	
		Query query = session.createQuery(sQuery)
    					.setDate("fecha", date);
    	
    	return  (ArrayList<Recurso>) query.list();
    }
	
	/**
	 * Obtiene un Recurso por su codigo
	 */
	public Recurso getByCodigo(String codigo) {
		Recurso recurso;
		String queryString = "from Recurso t where t.codRecurso = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		recurso = (Recurso) query.uniqueResult();	

		return recurso; 
	}
	
	/**
	 * Obtiene la lista de recursos activos con envio de deuda a judicial
	 * @return List<Recurso>
	 */
	public List<Recurso> getListActivosEnvioJudicial() {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM Recurso r ";
	    
		// Armamos filtros del HQL
		queryString += " WHERE r.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " AND r.enviaJudicial = " + SiNo.SI.getId();

 		// Order By
		queryString += " ORDER BY r.desRecurso ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Recurso> listRecurso = (ArrayList<Recurso>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecurso;
	}

	/**
	 * Devuelve los RecAtrCue para un recurso y que sean vigentes a una fecha dada.
	 * 
	 * @param recurso
	 * @param fecha
	 * @return
	 */
	public List<RecAtrCue> getListRecAtrCueVigentes(Recurso recurso, Date fecha){
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM RecAtrCue r " +
							" WHERE r.recurso = :recurso AND " +
							" r.fechaDesde <= :fecha AND " +
							" ( r.fechaHasta IS NULL OR r.fechaHasta > :fecha )";
    	
		Query query = session.createQuery(sQuery)
							.setEntity("recurso", recurso)
							.setDate("fecha", fecha);
    	
    	return  (ArrayList<RecAtrCue>) query.list();
	}
	
	public List<Recurso> getListAutoliquidablesVigentes(Date date) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM Recurso r " +
							" WHERE r.esAutoliquidable=1 AND r.fechaAlta <= :fecha AND " +
							" ( r.fechaBaja IS NULL OR r.fechaBaja > :fecha ) " + 
							" ORDER BY r.categoria";
    	
		Query query = session.createQuery(sQuery)
    					.setDate("fecha", date);
    	
    	return  (ArrayList<Recurso>) query.list();
    }

	/**
	 * Obtiene la lista de recursos que permiten emisiones
	 * masivas o puntuales.
	 *
	 * @return listRecurso
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public List<Recurso> getListEmitibles() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = ""; 
	 	strQuery += "select recurso from Recurso recurso ";
	 	strQuery +=	"where (recurso.perEmiDeuMas = "+ SiNo.SI.getBussId() + " ";
	 	strQuery +=		"or recurso.perEmiDeuPuntual = "+ SiNo.SI.getBussId() + ") ";
	 	strQuery +=	  "and recurso.categoria.tipo.id = 1 ";
	 	strQuery +=	  "and recurso.estado = "+ Estado.ACTIVO.getId() + " ";
	 	strQuery +=	"order by recurso.categoria, recurso.desRecurso ";
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Recurso>) query.list(); 
	 }

	/**
	 * Obtiene la lista de recursos que permiten emisiones
	 * masivas. 
	 *
	 * @return listRecurso
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public List<Recurso> getListWidthEmisionMas() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = ""; 
	 	strQuery += "select recurso from Recurso recurso ";
	 	strQuery +=	"where recurso.perEmiDeuMas = "+ SiNo.SI.getBussId() + " ";
	 	strQuery +=	  "and recurso.estado = "+ Estado.ACTIVO.getId() + " ";
	 	strQuery +=	"order by recurso.desRecurso ";
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Recurso>) query.list(); 
	 }
	
	/**
	 * Obtiene la lista de recursos que permiten emisiones
	 * puntuales. 
	 *
	 * @return listRecurso
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public List<Recurso> getListWidthEmisionPuntual() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = ""; 
	 	strQuery += "select recurso from Recurso recurso ";
	 	strQuery +=	"where recurso.perEmiDeuPuntual = "+ SiNo.SI.getBussId() + " ";
	 	strQuery +=	  "and recurso.estado = "+ Estado.ACTIVO.getId() + " ";
	 	strQuery +=	"order by recurso.desRecurso ";
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Recurso>) query.list(); 
	 }

	
	/**
	 * Obtiene la lista de recursos activos que permiten
	 * emisiones extraordinarias
	 * 
	 * @return listRecurso
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public List<Recurso> getListWithEmisionExt() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = ""; 
	 	strQuery += "select recurso from Recurso recurso ";
	 	strQuery +=	"where recurso.perEmiDeuExt = " + SiNo.SI.getBussId() + " ";
	 	strQuery +=   "and recurso.estado = "+ Estado.ACTIVO.getId() + " ";
	 	strQuery +=	"order by recurso.desRecurso ";
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Recurso>) query.list(); 
	 }

	
	/**
	 * 
	 * Devuelve los recursos que permiten alta manual y son no tributarios
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<Recurso> getListPermitenAltaManualCuentaVigentes(Date date) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM Recurso r " +
							" WHERE r.altaCtaManual=1 AND " +
							" r.categoria.tipo.id=1 AND " +
							" r.fechaAlta <= :fecha AND " +
							" ( r.fechaBaja IS NULL OR r.fechaBaja > :fecha ) " + 
							" ORDER BY r.categoria";
    	
		Query query = session.createQuery(sQuery)
    					.setDate("fecha", date);
    	
    	return  (ArrayList<Recurso>) query.list();
    }
	
	/**
	 *  Obtiene la lista de Recursos No Tributarios vigentes a la fecha pasada como parametro.
	 *  
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<Recurso> getListNoTributariosVigentes(Date date) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM Recurso r " +
							" WHERE r.categoria.tipo.id="+Tipo.ID_TIPO_NOTRIB.toString()+" AND " +
							" r.fechaAlta <= :fecha AND " +
							" ( r.fechaBaja IS NULL OR r.fechaBaja > :fecha ) " + 
							" ORDER BY r.categoria.desCategoria, r.desRecurso";
    	
		Query query = session.createQuery(sQuery).setDate("fecha", date);
    	
    	return  (ArrayList<Recurso>) query.list();
    }
	
	/**
	 * Obtiene la lista de recursos que permiten emisiones puntuales.
	 * 
	 * @return listRecurso
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public List<Recurso> getListWithEmisionPuntual() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = ""; 
	 	strQuery += "select recurso from Recurso recurso";
	 	strQuery +=	" where recurso.perEmiDeuPuntual = " + SiNo.SI.getBussId();
	 	strQuery +=	" order by recurso.categoria.desCategoria, recurso.desRecurso ";
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Recurso>) query.list(); 
	 }

	/**
	 * Obtiene la lista de recursos vigentes que permiten emisiones puntuales.
	 * 
	 * @param fecha
	 * @return listRecurso
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public List<Recurso> getListWithEmisionPuntualVigentes(Date fecha) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = ""; 
	 	strQuery += "select recurso from Recurso recurso ";
	 	strQuery +=	"where recurso.perEmiDeuPuntual = " + SiNo.SI.getBussId();
	 	strQuery +=   " and (recurso.fechaBaja is null or recurso.fechaBaja > :fecha)";
	 	strQuery +=	" order by recurso.categoria.desCategoria, recurso.desRecurso ";
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setDate("fecha", fecha);		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Recurso>) query.list(); 
	 }
	
	/**
	 * Obtiene la lista de recursos que permiten impresiones 
	 * masivas de deuda
	 * 
	 * @return listRecurso
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public List<Recurso> getListWithImpresionMasiva() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = ""; 
	 	strQuery += "select recurso from Recurso recurso";
	 	strQuery +=	" where recurso.perImpMasDeu = " + SiNo.SI.getBussId();
	 	strQuery +=	" order by recurso.categoria.desCategoria, recurso.desRecurso ";
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Recurso>) query.list(); 
	 }
	
	/**
	 * Actualiza la fecha de ultimo envio procesado (FecUltEnvPro) 
	 * para los recursos DReI y ETuR. 
	 * 
	 * @param date
	 */
	public Integer updateFecUltEnvProForDReIorETuR(Date date){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	 	String strQuery = "update Recurso r set r.fecUltEnvPro = :fecha"; 
	 		   strQuery +=	" where r.codRecurso = :codDReI ";
	 		   strQuery +=	" or r.codRecurso = :codETuR " ;
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(strQuery)
							 .setDate("fecha", date)
							 .setString("codDReI", Recurso.COD_RECURSO_DReI)
							 .setString("codETuR", Recurso.COD_RECURSO_ETuR);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return query.executeUpdate();
		
	}
}
