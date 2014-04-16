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
import ar.gov.rosario.siat.gde.buss.bean.AuxLiqComProDeu;
import ar.gov.rosario.siat.gde.buss.bean.LiqComPro;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.ListUtil;
public class AuxLiqComProDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AuxLiqComProDeuDAO.class);	
	
	public AuxLiqComProDeuDAO() {
		super(AuxLiqComProDeu.class);
	}
	
	/**
	 * Obtiene un AuxLiqComProDeu por su codigo
	 */
	public AuxLiqComProDeu getByCodigo(String codigo) throws Exception {
		AuxLiqComProDeu auxLiqComProDeu;
		String queryString = "from AuxLiqComProDeu t where t.codAuxLiqComProDeu = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		auxLiqComProDeu = (AuxLiqComProDeu) query.uniqueResult();	

		return auxLiqComProDeu; 
	}


	public List<AuxLiqComProDeu> getByIdLiqComPro(Long idLiqComPro) {
		String queryString = "from AuxLiqComProDeu t where t.liqComPro.id ="+idLiqComPro;
		queryString += " ORDER BY t.id";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		return (ArrayList<AuxLiqComProDeu>)query.list();

	}
	
	public void delete(List<LiqComPro> listLiqComPro) throws Exception{
		String listIdLiqCOmPro = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listLiqComPro, 0, false));
		String queryString = "DELETE FROM AuxLiqComProDeu where liqComPro.id in("+listIdLiqCOmPro+")";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		query.executeUpdate();
		
	}
}
