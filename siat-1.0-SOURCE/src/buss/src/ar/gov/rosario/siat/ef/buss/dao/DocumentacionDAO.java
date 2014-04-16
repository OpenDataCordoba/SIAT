//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;


import java.util.ArrayList;

import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Documentacion;


public class DocumentacionDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(DocumentacionDAO.class);
	
	public DocumentacionDAO() {
		super(Documentacion.class);
	}

	public ArrayList<Documentacion> getListActivosOrderByTipoNroOrden() {
		String queryString=" FROM Documentacion t where t.estado=1 ORDER BY t.tipoDoc.orden";
		
		Session session = SiatHibernateUtil.currentSession();
		return (ArrayList<Documentacion>) session.createQuery(queryString).list();
	}
	

}
