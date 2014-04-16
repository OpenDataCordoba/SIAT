//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.iface.model.SistemaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class SistemaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SistemaDAO.class);	
	
	public SistemaDAO() {
		super(Sistema.class);
	}
	
	public List<Sistema> getListBySearchPage(SistemaSearchPage sistemaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Sistema t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AtributoSearchPage: " + sistemaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		// Si estoy en modo seleccionar solo muestro los activos
		if (sistemaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro atributos excluidos
 		List<SistemaVO> listSistemasExcluidos = (ArrayList<SistemaVO>) sistemaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listSistemasExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listSistemasExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
 		/** Se quito la relacion sitema-recurso
		// filtro por idRecurso
 		Long idRecurso = sistemaSearchPage.getSistema().getRecurso().getId();
 		if (idRecurso!=null && idRecurso.longValue()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id =" + idRecurso;
			flagAnd = true;
		}
		**/
		
		// filtro por nroSistema
 		Long nroSistema = sistemaSearchPage.getSistema().getNroSistema();
 		if (nroSistema!=null && nroSistema.longValue()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroSistema = " + nroSistema;				
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.nroSistema ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Sistema> listSistema = (ArrayList<Sistema>) executeCountedSearch(queryString, sistemaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSistema;
	}
	
	/**
	 * Obtiene lista de Sistemas para el Servicio Banco especificado
	 * @author Tecso
	 * @param ServicioBanco servicioBanco	
	 * @return List<Sistema> 
	 */
	public List<Sistema> getListByServicioBanco(ServicioBanco servicioBanco) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Sistema t ";
	    
		// Armamos filtros del HQL
        queryString += " where t.servicioBanco.id = " + servicioBanco.getId();
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Sistema> listSistema = (ArrayList<Sistema>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSistema;
	}
	/**
	 * Obtiene un Sistema Servicio Banco para un determinado Sistema con determinado Servicio Banco
	 */
	public Sistema getSistemaServicioBanco (Sistema sistema){
		String queryString = "from Sistema t where t.esServicioBanco =:uno AND" +
							 " t.servicioBanco =:servBanc";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
				.setInteger("uno", 1)
				.setEntity("servBanc", sistema.getServicioBanco());
		
		List<Sistema> listSistema = query.list();
		
		Sistema ret = null;
		
		if (listSistema.size() == 1){
			ret = listSistema.get(0);
		}
		
		return ret;
	}
	
	
	/**
	 * Devuelve la cantidad de Sistemas marcados como esServicioBanco 
	 * para el ServivioBanco del sistema recibido.
	 *  
	 * 
	 * @author Cristian
	 * @param sistema
	 * @return
	 */
	public boolean chkUniqueSistemaServicioBanco(Sistema sistema){
		String funcName = DemodaUtil.currentMethodName();
		
		String queryString = "SELECT COUNT(t.id) FROM Sistema t " +
							 "WHERE t.esServicioBanco =:uno AND" +
		 					 " t.servicioBanco =:servBanc";
		
		if (sistema.getId() != null){
			queryString += " AND t.id != :id"; 
		}

		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
					.setInteger("uno", 1)
					.setEntity("servBanc", sistema.getServicioBanco());
		
		if (sistema.getId() != null){
			query.setLong("id", sistema.getId());
		}
		
		Long cant = (Long) query.uniqueResult();
		
		log.debug( funcName + " qry: " + query);
		log.debug( funcName + " : " + sistema.infoString());
		
		// Para Agregar o Modificar un sistema. 
		if (sistema.getEsServicioBanco().intValue() == 1 && cant.longValue() == 1 ){
			return false;	
		} else if (sistema.getEsServicioBanco().intValue() == 0 && cant.longValue() == 1) {
			return true;
		// Para update o Create	
		} else if (cant.longValue() == 0){
			return true;
		
		// La cantidad resulto menor que cero o mayor que uno. 
		} else {
			return false;
		}

	} 
	
	
	/**
	 * Obtiene un Sistema por su numero
	 */
	public Sistema getByNroSistema(Long nroSistema) throws Exception {
		Sistema sistema;
		String queryString = "from Sistema t where t.nroSistema = :numero";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", nroSistema);
		sistema = (Sistema) query.uniqueResult();	

		return sistema; 
	}


	/**
	 * Obtiene el sistema que es servicioBanco (esServicioBanco=1)
	 * de todos los sistemas a los que pertenece el recurso pasado como parametro.
	 * @param recurso
	 * @return
	 */
	public Sistema getSistemaEmision(Recurso recurso) {
		try {
			String queryString = "";
			queryString += "select sistema from Sistema sistema, SerBanRec serBanRec  ";
			queryString += "where sistema.servicioBanco.id = serBanRec.servicioBanco.id ";
			queryString += 	 "and sistema.esServicioBanco = :si ";
			queryString += 	 "and serBanRec.recurso.id = :idRecurso ";
			queryString += 	 "and serBanRec.fechaDesde <= :fecha ";
			queryString += 	 "and (serBanRec.fechaHasta is null or serBanRec.fechaHasta >= :fecha) ";
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString)
								 .setInteger("si", SiNo.SI.getBussId())
								 .setLong("idRecurso", recurso.getId())
								 .setDate("fecha"	 , new Date());
	
			Sistema sistema = (Sistema) query.uniqueResult();
			return sistema;
		
		} catch (NonUniqueResultException e) {
			return null;
		}
	}
}
