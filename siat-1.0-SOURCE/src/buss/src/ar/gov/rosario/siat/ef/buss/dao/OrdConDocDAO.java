//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Acta;
import ar.gov.rosario.siat.ef.buss.bean.OrdConDoc;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.buss.bean.TipoActa;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class OrdConDocDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OrdConDocDAO.class);
	
	public OrdConDocDAO() {
		super(OrdConDoc.class);
	}


	/**
	 * Obtiene una lista de OrdConDoc filtrando por el campo idActaProc con el valor pasado como parametro
	 * @param id
	 * @return
	 */
	public List<OrdConDoc> getListByActaProc(Acta actaProcedimiento) {
		String queryString = "FROM OrdConDoc t where t.actaProc= :actaProcedimiento";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("actaProcedimiento", actaProcedimiento);
		
		return query.list();
	}
	
	/**
	 * Obtiene los OrdconDoc de todas las actas de la orden de control pasada como parametro,
	 * que sean del tipo INICIO o REQ y que el idActaProc sea null o igual al acta pasada como parametro
	 * @param ordenControl
	 * @return
	 */
	public List<OrdConDoc> getList4ActaProc(OrdenControl ordenControl, Acta actaProc){
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter - OrdenControl:"+(ordenControl!=null?ordenControl.getId():null));

		String queryString = "FROM OrdConDoc t where (t.actaProc=null or t.actaProc= :actaProc) "+
				" and t.acta.tipoActa IN("+TipoActa.ID_TIPO_INICIO_PROCEDIMIENTO+", "+TipoActa.ID_TIPO_REQ_INF+") " +
				" and t.ordenControl = :ordenControl";		
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("ordenControl", ordenControl)
																			.setEntity("actaProc", actaProc);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return query.list();
		
	}

}
