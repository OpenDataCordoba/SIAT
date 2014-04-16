//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.ProPasDeb;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebSearchPage;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ProPasDebDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProPasDebDAO.class);
	
	public ProPasDebDAO() {
		super(ProPasDeb.class);
	}
	
	public List<ProPasDeb> getBySearchPage(ProPasDebSearchPage proPasDebSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ProPasDeb t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ProPasDebSearchPage: " + proPasDebSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (proPasDebSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro por recurso
 		RecursoVO recurso = proPasDebSearchPage.getProPasDeb().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

 		// filtro por estado de la corrida
 		EstadoCorridaVO estadoCorridaVO = proPasDebSearchPage.getProPasDeb().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId(); 
			flagAnd = true;
		}

  		// Order By
		queryString += " order by t.fechaUltMdf desc";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ProPasDeb> listProPasDeb = (ArrayList<ProPasDeb>) executeCountedSearch(queryString, proPasDebSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listProPasDeb;
	}

	@SuppressWarnings("unchecked") 
	public List<Object[]> getListDeudaAdminBy(Recurso recurso, Integer anio, Integer periodo,
			Atributo atributo,String atrValor) {
		 
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");

		boolean atrFilter = atributo != null && !StringUtil.isNullOrEmpty(atrValor); 
		
		String strQuery = ""; 
	 	strQuery += "select deuda.importe, deuda.fechaVencimiento, cuenta.numeroCuenta, cuenta.id ";
	 	strQuery += "from gde_deudaAdmin deuda, pad_cuenta cuenta ";
	 	if (atrFilter) 
	 		strQuery += ", pad_objImp objImp, pad_objImpAtrVal objImpAtrVal ";
	 	
	 	strQuery +=	"where deuda.idCuenta = cuenta.id ";
	 	strQuery +=	  " and deuda.idRecurso = " + recurso.getId();
	 	strQuery +=	  " and deuda.anio = " + anio;
	 	strQuery +=	  " and deuda.periodo = " + periodo;
	 	
	 	//	Verificamos en la lista de ids de Clasificacion Deuda a incluir en el Proceso de PAS/Debito
		String listaIdClaDeuForPasDebito = null;
		try{ listaIdClaDeuForPasDebito = SiatParam.getString(SiatParam.LISTA_ID_CLADEU_FOR_PAS_DEBITO); }catch (Exception e) {}
		if (!StringUtil.isNullOrEmpty(listaIdClaDeuForPasDebito)){
			String idClaDeuStrForQuery = StringUtil.replace(listaIdClaDeuForPasDebito, "|", ",");
			if(idClaDeuStrForQuery.startsWith(","))
				idClaDeuStrForQuery = idClaDeuStrForQuery.substring(1);
			if(idClaDeuStrForQuery.endsWith(","))
				idClaDeuStrForQuery = idClaDeuStrForQuery.substring(0, idClaDeuStrForQuery.length()-1);

		 	strQuery +=	  " and deuda.idRecClaDeu in ("+idClaDeuStrForQuery+")";
		}
		
	 	if (atrFilter) {
		 	strQuery +=	  " and objImp.id = cuenta.idObjImp";
		 	strQuery +=	  " and objImp.id = objImpAtrVal.idObjImp";
		 	strQuery +=	  " and objImpAtrVal.idTipObjImpAtr = " + TipObjImpAtr.getByIdAtributo(atributo.getId()).getId();
		 	strQuery +=	  " and objImpAtrVal.strValor = '" + atrValor + "'";
	 	}
	 	
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);

	 	Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(strQuery)
                                .addScalar("importe", Hibernate.DOUBLE)
                                .addScalar("fechaVencimiento", Hibernate.DATE)
                                .addScalar("numeroCuenta", Hibernate.STRING)
                                .addScalar("id", Hibernate.LONG);
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");

		return (ArrayList<Object[]>) query.list(); 
	 }

}
