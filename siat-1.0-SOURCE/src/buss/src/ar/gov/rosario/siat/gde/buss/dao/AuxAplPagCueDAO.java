//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.AuxAplPagCue;

public class AuxAplPagCueDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(AuxAplPagCueDAO.class);	
	
	public AuxAplPagCueDAO() {
		super(AuxAplPagCue.class);
	}
	
	public static List<AuxAplPagCue> getListByIdResDet(Long idResDet){
		Session session = SiatHibernateUtil.currentSession();
		List<AuxAplPagCue> listAuxAplPagCue;
		
		String queryString = "FROM AuxAplPagCue t WHERE t.resDet.id = "+ idResDet;
		Query query = session.createQuery(queryString);
		
		listAuxAplPagCue = (List<AuxAplPagCue>)query.list();
		
		return listAuxAplPagCue;
	}
	
	public static List<AuxAplPagCue> getListByIdConvenio(Long idConvenio){
		
		Session session = SiatHibernateUtil.currentSession();
		List<AuxAplPagCue> listAuxAplPagCue;
		
		String queryString = "FROM AuxAplPagCue t WHERE t.convenio.id = "+ idConvenio;
		queryString += " ORDER BY fechaPago, t.convenioCuota.numeroCuota  ASC";
		Query query = session.createQuery(queryString);
		
		listAuxAplPagCue = (List<AuxAplPagCue>) query.list();
		
		return listAuxAplPagCue;
	}

}
