//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.DetAjuDet;
import ar.gov.rosario.siat.gde.buss.bean.Cobranza;
import ar.gov.rosario.siat.gde.buss.bean.CobranzaDet;

public class CobranzaDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CobranzaDetDAO.class);	
	
	public CobranzaDetDAO() {
		super(CobranzaDet.class);
	}
	
	
	
	public CobranzaDet getByDetAjuDetAndCobranza(DetAjuDet detAjuDet,Cobranza cobranza){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from CobranzaDet c where c.detAjuDet.id = " + detAjuDet.getId();
		
		queryString += " and c.cobranza.id = "+cobranza.getId();
		
		Query query = session.createQuery(queryString);
		
		
		return (CobranzaDet) query.uniqueResult();
		
	}
}
