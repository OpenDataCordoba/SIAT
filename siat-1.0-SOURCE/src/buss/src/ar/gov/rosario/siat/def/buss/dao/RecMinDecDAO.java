//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecMinDec;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class RecMinDecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecMinDecDAO.class);	
	
	public RecMinDecDAO() {
		super(RecMinDec.class);
	}

	/**
	 * Devuelve el RecMinDec vigente para el valor y fecha pasados como parametro
	 * @param valor
	 * @param fecha
	 * @return null si no encuentra nada
	 */
	public RecMinDec getVigente(Recurso recurso, Double valor, Date fecha){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from RecMinDec t where t.valRefDes <= :valor AND t.valRefHas>= :valor AND " +
				"t.fechaDesde<= :fecha AND (t.fechaHasta is null OR t.fechaHasta>= :fecha)";
		queryString += " AND t.recurso.id = "+recurso.getId();
		
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		query.setDouble("valor", valor);
		query.setDate("fecha", fecha);
		
		RecMinDec recMinDec = (RecMinDec) query.uniqueResult();	

		return recMinDec;
	}
	
	/**
	 * Devuelve el RecMinDec vigente con valor mínimo
	 * @param fecha
	 * @param 
	 * @return null si no encuentra nada
	 */
	public RecMinDec getMinimoVigenteForMulta(Recurso recurso, Date fecha){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from RecMinDec t where t.fechaDesde<= :fecha AND (t.fechaHasta is null OR t.fechaHasta>= :fecha)";
		queryString += " AND t.recurso.id = "+recurso.getId();
		queryString += " ORDER BY t.minimo ASC";
		
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		query.setDate("fecha", fecha);
		query.setMaxResults(1);
		RecMinDec recMinDec = (RecMinDec) query.uniqueResult();	

		return recMinDec;
	}
	

	/**
	 * controla que no se superpongan periodos, respectos a las fechas pasadas como parametros, con las ya ingresadas
	 * */
	public List<RecMinDec> getByRecursoFechaDesFechaHas(Long idRecurso, Date fechaDesde, Date fechaHasta){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
				
		String queryString = "from RecMinDec t where t.recurso.id = "+idRecurso;
		
						// me pasa un rango grande
		queryString += " AND ( " +
								"( (t.fechaDesde is null OR t.fechaDesde>= :fechaDesde) AND (t.fechaHasta is null OR t.fechaHasta<= :fechaHasta) )";
						// me pasa un rango mas chico
		queryString += " OR ( (t.fechaDesde is null OR t.fechaDesde<= :fechaDesde) AND (t.fechaHasta is null OR t.fechaHasta>= :fechaHasta) )";
						// si pasa un rango con un valor intermedio entre una fecha desde y hasta ya ingresado
		queryString += " OR ( (t.fechaDesde is null OR t.fechaDesde<= :fechaDesde) AND (t.fechaHasta is null OR t.fechaHasta>= :fechaDesde) )";
		queryString += " OR ( (t.fechaDesde is null OR t.fechaDesde<= :fechaHasta) AND (t.fechaHasta is null OR t.fechaHasta>= :fechaHasta) )" +
						")";//cierra el AND
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
	
		query.setDate("fechaDesde", fechaDesde);
		query.setDate("fechaHasta", fechaHasta);
		
		//RecMinDec recMinDec = (RecMinDec) query.uniqueResult();	

	    List<RecMinDec> listRecMinDec = (ArrayList<RecMinDec>) query.list();

		return listRecMinDec;
	}

}
