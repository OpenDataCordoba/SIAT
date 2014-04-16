//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rec.buss.bean.EstadoObra;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.iface.model.ObraSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ObraDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ObraDAO.class);	
	
	public ObraDAO() {
		super(Obra.class);
	}
	
	public List<Obra> getBySearchPage(ObraSearchPage obraSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Obra t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ObraSearchPage: " + obraSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (obraSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// Filtro por numero obra 		
		if (obraSearchPage.getObra().getNumeroObra() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.numeroObra = " + obraSearchPage.getObra().getNumeroObra().toString() + "";
			flagAnd = true;			
		}
		
		// Filtro por descripcion		
		if (!StringUtil.isNullOrEmpty(obraSearchPage.getObra().getDesObra())) {
	        queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desObra)) like '%" + 
				StringUtil.escaparUpper(obraSearchPage.getObra().getDesObra()) + "%'";
			flagAnd = true;
		}
		
		// Filtro por estado obra
		if (! ModelUtil.isNullOrEmpty( obraSearchPage.getObra().getEstadoObra())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoObra.id = " + obraSearchPage.getObra().getEstadoObra().getId().toString();
			flagAnd = true;			
		}
		
 		// Order By
		queryString += " order by t.recurso.desRecurso, t.numeroObra ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Obra> listObra = (ArrayList<Obra>) executeCountedSearch(queryString, obraSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listObra;
	}

	public List<Obra> getListObraEstadoNoCreada(){
		Session session = SiatHibernateUtil.currentSession();
    	String sQuery = "FROM Obra o WHERE o.estadoObra.id = :idEstObra " +
    					" ORDER BY o.desObra";
    	
    	Query query = session.createQuery(sQuery).setLong("", EstadoObra.ID_CREADA);
    	return (ArrayList<Obra>) query.list();		
	}
	
	/** Recupera todas las obras filtradas
	 *  por el id de estado pasado como parametro.
	 * 
	 * @param estadoObra
	 * @return
	 */
	public List<Obra> getListByEstado(Long idEstadoObra) {
		Session session = SiatHibernateUtil.currentSession();
    	String sQuery = "FROM Obra o WHERE o.estadoObra.id = :idEstObra " +
    					" ORDER BY o.numeroObra";
    	
    	Query query = session.createQuery(sQuery).setLong("idEstObra", idEstadoObra);
    	return (ArrayList<Obra>) query.list();		
	}
	
	
	/** Recupera todas las obras asociadas
	 *  al id de Recurso y en el Estado pasados como parametros 
	 * 
	 * @param estadoObra
	 * @return
	 */
	public List<Obra> getListByRecursoYEstado(Long idRecurso, Long idEstado) {
		Session session = SiatHibernateUtil.currentSession();
    	String queryString = "from Obra o";
    	boolean flagAnd = false; 
    		
    	if (idRecurso != null) {
    		queryString += flagAnd ? " and " : " where ";
    		queryString += " o.recurso.id = " + idRecurso;
    		flagAnd = true;
    	}
    		
    	if (idEstado != null) {
    		queryString += flagAnd ? " and " : " where ";
    		queryString += " o.estadoObra.id = " + idEstado;
    		flagAnd = true;
    	}
    	
    	queryString +=" order by o.numeroObra";
    	
    	Query query = session.createQuery(queryString);
    	
    	return (ArrayList<Obra>) query.list();		
	}
	
	/**
	 * Obtiene una Obra por su Numero
	 * <i>(Nota:Se usa en la migración de PlanillaCuadra)</i>
	 * 
	 * @param numeroObra
	 * @return obra
	 */
	public Obra getByNumero(Integer numeroObra){
		Session session = SiatHibernateUtil.currentSession();

		String sQuery = "FROM Obra o WHERE o.numeroObra = "+numeroObra;
		
		Query query = session.createQuery(sQuery);
    	
		return (Obra) query.uniqueResult();		
	}
	
	/**
	 * Retorna una lista con las obras que: se encuentran anuladas, tienen planillas anuladas 
	 * o tienen cuentas anuladas.
	 * Ordenada por numero de obra.
	 * 
	 * @return List<Obra>
	 * */
	public List<Obra> getListEnAnulacion() {
		Session session = SiatHibernateUtil.currentSession();
    	String sQuery = "FROM Obra o WHERE o.id in (SELECT anuObr.obra.id FROM AnulacionObra anuObr)" +
    					" ORDER BY o.numeroObra";
    	
    	Query query = session.createQuery(sQuery);
    	return (ArrayList<Obra>) query.list();		
	}
	
	public Obra getByIdCuenta(Long idCuenta) throws Exception{
		
		String queryString = "SELECT DISTINCT o FROM Obra o, PlaCuaDet p, PlanillaCuadra pc WHERE o.id = pc.obra.id AND p.planillaCuadra.id = pc.id";
		queryString += " AND p.cuentaCdM.id = "+ idCuenta;
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		Obra obra = (Obra)query.uniqueResult();
		
		return obra;
	}
	
}
