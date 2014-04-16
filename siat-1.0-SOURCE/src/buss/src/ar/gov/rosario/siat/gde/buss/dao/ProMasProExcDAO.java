//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ProMasProExc;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;

public class ProMasProExcDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(ProMasProExcDAO.class);	
	
	public ProMasProExcDAO() {
		super(ProMasProExc.class);
	}
	
	/**
	 * Borra la lista de ProMasProExc del ProcesoMasivo
	 * @param procesoMasivo
	 */
	public void deleteListProMasProExcByProMas(ProcesoMasivo procesoMasivo){
		Session session = SiatHibernateUtil.currentSession();
		
    	String sQuery = "DELETE FROM ProMasProExc ejpe WHERE ejpe.procesoMasivo = :procesoMasivo ";
    					
    	Query query = session.createQuery(sQuery).setEntity("procesoMasivo", procesoMasivo);
    	query.executeUpdate();		
	}

}
