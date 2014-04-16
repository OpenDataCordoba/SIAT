//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.ParCueBan;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ParCueBanDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ParCueBanDAO.class);
	
	public ParCueBanDAO() {
		super(ParCueBan.class);
	}
	
	
	/**
	 * Obtiene un ParCueBan por su codigo
	 */
	public ParCueBan getByCodigo(String codigo) throws Exception {
		ParCueBan parCueBan;
		String queryString = "from ParCueBan t where t.codParCueBan = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		parCueBan = (ParCueBan) query.uniqueResult();	

		return parCueBan; 
	}
	
	/**
	 * Obtiene la Cuenta Bancaria vigente a la fecha pasada para el id de partida especificado.
	 * 
	 * @param idPartida
	 * @param fecha
	 * @return cuentaBanco
	 */
	public CuentaBanco getVigenteByIdPartidaYFecha(Long idPartida, Date fecha) throws Exception {
		ParCueBan parCueBan;
		String queryString = "from ParCueBan t where t.partida.id = "+idPartida;

		queryString += " and t.fechaDesde <= TO_DATE('" + 
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
		queryString += " and (t.fechaHasta is NULL or t.fechaHasta >= TO_DATE('" + 
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		parCueBan = (ParCueBan) query.uniqueResult();	
		if(parCueBan != null)
			return parCueBan.getCuentaBanco();
		else 
			return null;
	}
	
}
