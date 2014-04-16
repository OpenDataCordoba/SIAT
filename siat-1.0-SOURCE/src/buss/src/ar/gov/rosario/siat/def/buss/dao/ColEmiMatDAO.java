//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ColEmiMat;

public class ColEmiMatDAO extends GenericDAO {

	public ColEmiMatDAO() {
		super(ColEmiMat.class);
	}
	
	/**
	 * Obtiene una columna de una matriz de emision 
	 * por su codigo
	 */
	public ColEmiMat getByCodigo(String codColumna) throws Exception {
		ColEmiMat colEmiMat;
		String queryString = "from ColEmiMat t where t.codColumna = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codColumna);
		colEmiMat = (ColEmiMat) query.uniqueResult();	

		return colEmiMat; 
	}
	
}
