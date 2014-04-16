//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.SinSalAFav;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class SinSalAFavDAO extends GenericDAO {

	public SinSalAFavDAO(){
		super(SinSalAFav.class);
	}
	
	/**
	 * Elimina los registros de SinSalAFav que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from SinSalAFav t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}

}
