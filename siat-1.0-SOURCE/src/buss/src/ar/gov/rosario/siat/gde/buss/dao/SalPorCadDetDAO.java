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
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCad;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCadDet;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoSelAdapter;

public class SalPorCadDetDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(SalPorCadDetDAO.class);	
	
	public SalPorCadDetDAO() {
		super(SalPorCadDet.class);
	}
	
	public List<SalPorCadDet> getListBySalPorCad (SalPorCad salPorCad){
		List<SalPorCadDet> listSalPorCadDet;
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from SalPorCadDet s WHERE s.salPorCad.id = "+salPorCad.getId();
		
		Query query = session.createQuery(queryString);
		listSalPorCadDet = (List<SalPorCadDet>)query.list();
		
		return listSalPorCadDet;
	}
	
	public SalPorCadDet getSalPorCadDetbySalCadyConvenio (SalPorCad salPorCad, Convenio convenio){
		SalPorCadDet salPorCadDet;
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from SalPorCadDet s WHERE s.salPorCad.id = "+salPorCad.getId();
		
		queryString += " and s.convenio.id = "+ convenio.getId();
		
		Query query = session.createQuery(queryString);
		salPorCadDet = (SalPorCadDet)query.uniqueResult();
		
		return salPorCadDet;
	}
	public List<SalPorCadDet> getListSalPorCadDetPaged (SalPorCadMasivoSelAdapter salPorCadSelVO)throws Exception{
		
		String queryString ="FROM SalPorCadDet scd WHERE scd.salPorCad.id = "+ salPorCadSelVO.getSaldoPorCaducidad().getId();
		if(salPorCadSelVO.getEstadoProcesoConvenio() != null && salPorCadSelVO.getEstadoProcesoConvenio().intValue() != -1)
			queryString += " AND scd.procesado = "+salPorCadSelVO.getEstadoProcesoConvenio();
		queryString += " ORDER BY scd.id";
		
		List<SalPorCadDet> listSalPorCadDet = (ArrayList<SalPorCadDet>) executeCountedQuery(queryString, salPorCadSelVO);
		
		return listSalPorCadDet;
	}
	

}
