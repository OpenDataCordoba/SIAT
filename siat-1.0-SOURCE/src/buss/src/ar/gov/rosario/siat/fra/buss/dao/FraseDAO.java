//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.fra.buss.bean.Frase;
import ar.gov.rosario.siat.fra.iface.model.FraseSearchPage;
import ar.gov.rosario.siat.fra.iface.model.FraseVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class FraseDAO extends GenericDAO {

	private Log log = LogFactory.getLog(FraseDAO.class);
	
	public FraseDAO() {
		super(Frase.class);
	}
	
	public List<Frase> getBySearchPage(FraseSearchPage fraseSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Frase t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del FraseSearchPage: " + fraseSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (fraseSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro frase excluidos
 		List<FraseVO> listFraseExcluidos = (List<FraseVO>) fraseSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listFraseExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listFraseExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 	   
 		// filtro por modulo
 		if (fraseSearchPage.getModulo().getId()!=-1) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.modulo = '" + 
				fraseSearchPage.getModulo().getCod() + "'";
			flagAnd = true;
		}
          
		// filtro por pagina
 		if (!StringUtil.isNullOrEmpty(fraseSearchPage.getFrase().getPagina())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.pagina)) like '%" + 
				StringUtil.escaparUpper(fraseSearchPage.getFrase().getPagina()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por etiqueta
 		if (!StringUtil.isNullOrEmpty(fraseSearchPage.getFrase().getEtiqueta())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.etiqueta)) like '%" + 
				StringUtil.escaparUpper(fraseSearchPage.getFrase().getEtiqueta()) + "%'";
			flagAnd = true;
		}
 		
        // filtro por valor Publicado
 		if (!StringUtil.isNullOrEmpty(fraseSearchPage.getFrase().getValorPublico())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.valorpublico)) like '%" + 
				StringUtil.escaparUpper(fraseSearchPage.getFrase().getValorPublico()) + "%'";
			flagAnd = true;
		}
          
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(fraseSearchPage.getFrase().getDesFrase())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desFrase)) like '%" + 
				StringUtil.escaparUpper(fraseSearchPage.getFrase().getDesFrase()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		//queryString += " order by t.codFrase ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Frase> listFrase = (ArrayList<Frase>) executeCountedSearch(queryString, fraseSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listFrase;
	}

	/**
	 * Obtiene un Frase por su codigo
	 */
	public Frase getByCodigo(String codigo) throws Exception {
		Frase frase;
		String queryString = "from Frase t where t.codFrase = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		frase = (Frase) query.uniqueResult();	

		return frase; 
	}

	public Frase getByKey(String modulo, String pagina, String etiqueta) {
		Frase frase;
		String queryString = "from Frase t where t.modulo = :modulo AND t.pagina = :pagina AND t.etiqueta = :etiqueta";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("modulo", modulo).
				setString("pagina", pagina).setString("etiqueta", etiqueta);
		frase = (Frase) query.uniqueResult();	

		return frase; 

	}
}
