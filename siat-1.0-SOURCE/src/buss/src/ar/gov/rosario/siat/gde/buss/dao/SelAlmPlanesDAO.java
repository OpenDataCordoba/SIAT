//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCad;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmPlanes;

public class SelAlmPlanesDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(SelAlmDeudaDAO.class);	
	
	public SelAlmPlanesDAO() {
		super(SelAlmPlanes.class);
	}
	
	public static List<SelAlmDet>getListByIdSelAlmPlan(SelAlmPlanes selAlmPlan){
		Session session=SiatHibernateUtil.currentSession();
		List<SelAlmDet>listSelAlmDet;
		
		String queryString = "from SelAlmDet s WHERE s.selAlm.id = " + selAlmPlan.getId();
		
		Query query = session.createQuery(queryString);
		
		listSelAlmDet = (List<SelAlmDet>) query.list();
		
		return listSelAlmDet;
	}
	
	public List<SelAlmDet>getListBySalPorCad(SalPorCad salPorCad){
		Session session=SiatHibernateUtil.currentSession();
		List<SelAlmDet>listSelAlmDet;
		SelAlmPlanes selAlmPlan = salPorCad.getSelAlmPlanes();
		
		String queryString = "from SelAlmDet s WHERE s.selAlm.id = " + selAlmPlan.getId();
		
		queryString += " AND s.idElemento NOT IN (Select sc.convenio.id FROM SalPorCadDet sc WHERE sc.salPorCad.id = "+salPorCad.getId();
		
		queryString += " AND sc.procesado = 1 )";
		
		queryString +=" )";
		
		Query query = session.createQuery(queryString);
		
		listSelAlmDet = (List<SelAlmDet>) query.list();
		
		return listSelAlmDet;
	}
	
	public static SelAlmDet getSelAlmDetByIdElemento(SelAlmPlanes selAlmPlanes, Long id){
		Session session = SiatHibernateUtil.currentSession();
		SelAlmDet selAlmDet;
		
		String queryString = "from SelAlmDet s WHERE s.selAlm.id = " + selAlmPlanes.getId();
		queryString += " AND s.idElemento = "+ id;
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		
		selAlmDet = (SelAlmDet) query.uniqueResult();
		
		return selAlmDet;
	}

}
