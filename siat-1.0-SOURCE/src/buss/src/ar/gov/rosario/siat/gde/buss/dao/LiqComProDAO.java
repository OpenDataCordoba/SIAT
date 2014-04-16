//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.LiqCom;
import ar.gov.rosario.siat.gde.buss.bean.LiqComPro;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.ListUtil;

public class LiqComProDAO extends GenericDAO {

	private Log log = LogFactory.getLog(LiqComProDAO.class);	
	
	public LiqComProDAO() {
		super(LiqComPro.class);
	}
	
	/**
	 * Obtiene un LiqComPro por su codigo
	 */
	public LiqComPro getByCodigo(String codigo) throws Exception {
		LiqComPro liqComPro;
		String queryString = "from LiqComPro t where t.codLiqComPro = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		liqComPro = (LiqComPro) query.uniqueResult();	

		return liqComPro; 
	}


	public LiqComPro get(LiqCom liqCom, Procurador procurador) {
		LiqComPro liqComPro;
		String queryString = "from LiqComPro t ";
		boolean flagAnd = false;
		
		//Filtro por liqCom
		if(liqCom!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.liqCom.id="+liqCom.getId();
			flagAnd=true;
		}
		
		//Filtro por procurador
		if(procurador!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.procurador.id="+procurador.getId();
			flagAnd=true;
		}				
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		liqComPro = (LiqComPro) query.uniqueResult();	

		return liqComPro; 

	}


	public List<LiqComPro> getByIdLiqCom(Long idLiqCom) {
		String queryString = "from LiqComPro t where t.liqCom.id = "+idLiqCom;
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		return (ArrayList<LiqComPro>) query.list();	
	}


	public List<LiqComPro> getListByLiqCom(Long idLiqCom) {
		String queryString = "from LiqComPro t ";
		boolean flagAnd = false;
		
		//Filtro por liqCom
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " t.liqCom.id="+idLiqCom;
		flagAnd=true;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		return query.list(); 
	}
	
	public void delete(List<LiqComPro> listLiqComPro) throws Exception{
		String listIdLiqCOmPro = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listLiqComPro, 0, false));
		String queryString = "DELETE FROM LiqComPro where id in("+listIdLiqCOmPro+")";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		query.executeUpdate();
		
	}
}
