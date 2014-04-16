//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.def.buss.bean.Seccion;

public class SeccionDAO extends GenericDAO {

	public SeccionDAO() {
		super(Seccion.class);
	}

	public List getListActivosOrder()  throws HibernateException  {
		Session session = currentSession();
        return session.createQuery("from Seccion where estado = 1 order by id").list();
    }
}
