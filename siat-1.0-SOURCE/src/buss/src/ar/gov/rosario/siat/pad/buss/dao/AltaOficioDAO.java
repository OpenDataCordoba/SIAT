//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.pad.buss.bean.AltaOficio;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.EstObjImp;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class AltaOficioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AltaOficioDAO.class);
	
	public AltaOficioDAO() {
		super(AltaOficio.class);
	}
	
	public List<Cuenta> getBySearchPage(AltaOficioSearchPage altaOficioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		 String queryString = "SELECT cuenta FROM Cuenta cuenta, AltaOficio af WHERE " +
	 		"cuenta.objImp.id=af.objImp.id AND cuenta.recurso.id="+Recurso.getDReI().getId();

		 queryString +=" AND cuenta.estObjImp.id="+EstObjImp.ID_EST_ALTA_OFICIO;
		 
		 if(!StringUtil.isNullOrEmpty(altaOficioSearchPage.getCuenta().getNumeroCuenta())){
			 queryString += " AND cuenta.numeroCuenta='"+
			 	StringUtil.formatNumeroCuenta(altaOficioSearchPage.getCuenta().getNumeroCuenta())+"'";
		 }
		
		 if(!StringUtil.isNullOrEmpty(altaOficioSearchPage.getNroComercio())){
			 queryString  +=" AND cuenta.objImp.claveFuncional=" + altaOficioSearchPage.getNroComercio();
		 }
		
		 if(altaOficioSearchPage.getIdEstadoSel()!=null && altaOficioSearchPage.getIdEstadoSel().intValue()>0){
		 	queryString +=" AND cuenta.estado="+altaOficioSearchPage.getIdEstadoSel();
		 }
		 
		 if(altaOficioSearchPage.getFechaDesde()!=null){
			 queryString +=" AND af.fecha>=TO_DATE('" +DateUtil.formatDate(altaOficioSearchPage.getFechaDesde(),
					DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
		 }
		 
		 if(altaOficioSearchPage.getFechaHasta()!=null){
			 queryString +=" AND af.fecha<=TO_DATE('" +DateUtil.formatDate(altaOficioSearchPage.getFechaHasta(),
					DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
		 }
		 
 		// Order By
		queryString += " order by af.fecha DESC";		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Cuenta> listAltaOficio = (ArrayList<Cuenta>) executeCountedSearch(queryString, altaOficioSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAltaOficio;
	}

	/**
	 * Obtiene un AltaOficio por su codigo
	 */
	public AltaOficio getByCodigo(String codigo) throws Exception {
		AltaOficio altaOficio;
		String queryString = "from AltaOficio t where t.codAltaOficio = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		altaOficio = (AltaOficio) query.uniqueResult();	

		return altaOficio; 
	}

	public AltaOficio getByObjImp(Long idObjImp) {
		AltaOficio altaOficio;
		String queryString = "from AltaOficio t where t.objImp.id = :id";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("id", idObjImp);
		altaOficio = (AltaOficio) query.uniqueResult();	

		return altaOficio; 

	}
	
}
