//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOpeInvCon;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EstadoOpeInvConDAO extends GenericDAO {
	
	public EstadoOpeInvConDAO() {
		super(EstadoOpeInvCon.class);
	}
	


	/**
	 * Obtiene un EstadoOpeInvCon por su codigo
	 */
	public EstadoOpeInvCon getByCodigo(String codigo) throws Exception {
		EstadoOpeInvCon estadoOpeInvCon;
		String queryString = "from EstadoOpeInvCon t where t.codEstadoOpeInvCon = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		estadoOpeInvCon = (EstadoOpeInvCon) query.uniqueResult();	

		return estadoOpeInvCon; 
	}
	
	/**
	 * Obtiene una lista de los estados iniciales de opeInvCon excluyendo el pasado como parámetro
	 * @param idEstado
	 * @return
	 */
	public List<EstadoOpeInvCon> getListEstadosIniciales(Long idEstado) {
		List<EstadoOpeInvCon> listEstados;
		
		String queryString = "FROM EstadoOpeInvCon e where (e.id = "+ EstadoOpeInvCon.ID_SELECCIONADO;
		queryString += " OR e.id = "+ EstadoOpeInvCon.ID_EXCLUIR_SELEC;
		queryString += " OR e.id = "+ EstadoOpeInvCon.ID_VISITAR;
		queryString += " OR e.id = "+ EstadoOpeInvCon.ID_NO_VISITAR;
		queryString += " OR e.id = " + EstadoOpeInvCon.ID_INCLUIDO_OPERATIVO;
		queryString += ") AND e.id <> "+ idEstado;
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		listEstados = (List<EstadoOpeInvCon>)query.list();
		
		return listEstados;
	}
	


	
	public List<EstadoOpeInvCon> getList(Long[] idsEstados) {
		List<EstadoOpeInvCon> listEstados;
				
		String queryString = "FROM EstadoOpeInvCon e where e.id in("+
															StringUtil.getStringComaSeparate(idsEstados)+")" ;		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		listEstados = (List<EstadoOpeInvCon>)query.list();
		
		return listEstados;

	}
	
}
