//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.EstadoProced;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EstadoProcedDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EstadoProcedDAO.class);	
	
	public EstadoProcedDAO() {
		super(EstadoProced.class);
	}
	

	public List<EstadoProced> getListTransicionesForEstado(EstadoProced estadoProced) throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String transiciones = estadoProced.getTransiciones();
		
		if(StringUtil.isNullOrEmpty(transiciones))
			return new ArrayList<EstadoProced>();
		
		String query = "";
				
		if (transiciones.equals("*")){
			query = "WHERE (estadoProced.tipo = 'E' OR estadoProced.tipo = 'A') " +
					"AND estadoProced.id > 1 AND estadoProced.id <> " + estadoProced.getId();
		} else {
			query = "WHERE (estadoProced.tipo = 'E' OR estadoProced.tipo = 'A') " +
					"AND estadoProced.id IN (" + transiciones + ")";
		}
		
		query= "FROM EstadoProced estadoProced " + query;
		
		List<EstadoProced> listEstadoProced = session.createQuery(query).list();
		
		return listEstadoProced;
	}
	
	
	
	public List<EstadoProced> getListEstados() throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM EstadoProced estadoProced WHERE estadoProced.tipo = 'E'";

		List<EstadoProced> listEstadoProced = session.createQuery(query).list();
		
		return listEstadoProced;
	}
}
