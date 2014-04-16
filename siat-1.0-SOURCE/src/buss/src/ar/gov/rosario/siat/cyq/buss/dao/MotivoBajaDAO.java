//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.MotivoBaja;

public class MotivoBajaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MotivoBajaDAO.class);
	
	public MotivoBajaDAO() {
		super(MotivoBaja.class);
	}
	
	
	public List<MotivoBaja> getListEstados() throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM MotivoBaja motivoBaja WHERE motivoBaja.tipo = 'E'";

		List<MotivoBaja> listMotivoBaja = session.createQuery(query).list();
		
		return listMotivoBaja;
	}
}
