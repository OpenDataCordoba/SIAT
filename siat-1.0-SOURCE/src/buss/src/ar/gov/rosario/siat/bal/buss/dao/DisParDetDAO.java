//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DisParDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DisParDetDAO.class);
	
	public DisParDetDAO(){
		super(DisParDet.class);
	}
	
	public List<DisParDet> getListByDisParYidTipoImporteYRecCon(DisPar disPar,Long idTipoImporte,RecCon recCon) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		Date fechaEjecucion = new Date();
		
		String queryString = "from DisParDet t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.disPar.id = " + disPar.getId();
        if(idTipoImporte != null && idTipoImporte != -1)
        		queryString += " and t.tipoImporte.id = " + idTipoImporte;
        if(recCon != null){
        	if(recCon.getId()!= null && recCon.getId().longValue() != -1)
        		queryString += " and t.recCon.id = " + recCon.getId();
        } else{
        	queryString += " and t.recCon is null ";        	
        }
        
        queryString += " and t.fechaDesde <= TO_DATE('" + 
		DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";

        queryString += " and ((t.fechaHasta is NULL) or t.fechaHasta >= TO_DATE('" + 
		DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DisParDet> listDisParDet = (ArrayList<DisParDet>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParDet; 
	}

	
	/**
	 * Devuelve el Detalle del Distribuidor de Partida indicado para los parametros pasados, con Fecha Hasta igual a Null.
	 * @param disPar
	 * @param idTipoImporte
	 * @param recCon
	 * @param partida
	 * @return
	 * @throws Exception
	 */
	public DisParDet getConFechaHastaNullByDisParYidTipoImporteYRecConYPartida(DisPar disPar,Long idTipoImporte,RecCon recCon, Partida partida) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
				
		String queryString = "from DisParDet t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.disPar.id = " + disPar.getId();
        queryString += " and t.tipoImporte.id = " + idTipoImporte;
        if(recCon != null)
        	queryString += " and t.recCon.id = " + recCon.getId();
        else
        	queryString += " and t.recCon is null";
        
        queryString += " and t.partida.id = " + partida.getId();
        
        queryString += " and t.fechaHasta is NULL";

	queryString += " order by t.tipoImporte.id";

        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    DisParDet disParDet = (DisParDet) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return disParDet; 
	}

}
