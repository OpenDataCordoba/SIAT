//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;


import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDat;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDatDet;

public class PlaFueDatDetDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(PlaFueDatDetDAO.class);
	
	public PlaFueDatDetDAO() {
		super(PlaFueDatDet.class);
	}
	
	/**
	 * Obtiene un PlaFueDatDet por su codigo
	 */
	public PlaFueDatDet getByCodigo(String codigo) throws Exception {
		PlaFueDatDet plaFueDatDet;
		String queryString = "from PlaFueDatDet t where t.codPlaFueDatDet = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		plaFueDatDet = (PlaFueDatDet) query.uniqueResult();	

		return plaFueDatDet; 
	}


	public PlaFueDatDet getByPeriodoAnio(PlaFueDat plaFueDat, Integer periodo, Integer anio) {
		PlaFueDatDet plaFueDatDet;
		String queryString = "from PlaFueDatDet t where t.periodo = "+periodo + " and t.anio="+anio+
						" and t.plaFueDat = :plaFueDat";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("plaFueDat", plaFueDat);
		plaFueDatDet = (PlaFueDatDet) query.uniqueResult();	

		return plaFueDatDet;
	}
	
}
