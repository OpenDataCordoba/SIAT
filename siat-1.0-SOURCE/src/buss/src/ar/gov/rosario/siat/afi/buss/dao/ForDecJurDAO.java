//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.afi.buss.bean.ForDecJur;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurSearchPage;
import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FormularioAfip;

public class ForDecJurDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ForDecJurDAO.class);
	
	public ForDecJurDAO() {
		super(ForDecJur.class);
	}
	
	public List<ForDecJur> getBySearchPage(ForDecJurSearchPage forDecJurSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ForDecJur t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ForDecJurSearchPage: " + forDecJurSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (forDecJurSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		if (!StringUtil.isNullOrEmpty(forDecJurSearchPage.getForDecJur().getCuit())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.cuit)) like '%" + 
				StringUtil.escaparUpper(forDecJurSearchPage.getForDecJur().getCuit()) + "%'";
			flagAnd = true;
		}
		
		// filtro por Nro Envio
 		if(forDecJurSearchPage.getForDecJur().getEnvioOsiris().getIdEnvioAfip() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.envioOsiris.idEnvioAfip = " +  forDecJurSearchPage.getForDecJur().getEnvioOsiris().getIdEnvioAfip();
			flagAnd = true;
		}
 		
 		//  filtro por Nro Transaccion
 		if(forDecJurSearchPage.getForDecJur().getTranAfip().getIdTransaccionAfip() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tranAfip.idTransaccionAfip = " +  forDecJurSearchPage.getForDecJur().getTranAfip().getIdTransaccionAfip();
			flagAnd = true;
		}
 		
 		// filtro por Recurso
 		if(!ModelUtil.isNullOrEmpty(forDecJurSearchPage.getForDecJur().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " +  forDecJurSearchPage.getForDecJur().getRecurso().getId();
			flagAnd = true;
		}
 		
 		// filtro por NroFormulario
 		if(forDecJurSearchPage.getForDecJur().getNroFormulario() != null && forDecJurSearchPage.getForDecJur().getNroFormulario() != FormularioAfip.TODOS.getId().intValue()){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroFormulario = " +  forDecJurSearchPage.getForDecJur().getNroFormulario();
			flagAnd = true;
		}

 		// filtro por EstForDecJur
 		if(!ModelUtil.isNullOrEmpty(forDecJurSearchPage.getForDecJur().getEstForDecJur())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estForDecJur.id = " +  forDecJurSearchPage.getForDecJur().getEstForDecJur().getId();
			flagAnd = true;
		}

 		//filtro por fecha presentacion
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getFechaPresentacionDesdeView())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaPresentacion >= TO_DATE('"+ 
					DateUtil.formatDate(forDecJurSearchPage.getFechaPresentacionDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
			flagAnd = true;
 		}
 		
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getFechaPresentacionHastaView())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaPresentacion <= TO_DATE('"+ 
					DateUtil.formatDate(forDecJurSearchPage.getFechaPresentacionHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
			flagAnd = true;
 		}
 		
 		//filtro Tipo Pago - Retencion
 		if(forDecJurSearchPage.getTipPagDecJur().getId() == 1){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.id IN(SELECT r.forDecJur.id FROM RetYPer r WHERE r.estado = "+Estado.ACTIVO.getBussId()+")";
			flagAnd = true;
 		}
 		
 		//filtro Tipo Pago - Otros Pagos
 		if(forDecJurSearchPage.getTipPagDecJur().getId() == 2){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.id IN(SELECT o.local.forDecJur.id FROM OtrosPagos o WHERE o.estado = "+Estado.ACTIVO.getBussId()+")";
			flagAnd = true;
 		}
 		
 		//-->> SUBQUERY ACTIVIDAD
 		String subQueryAct = "SELECT d.local.forDecJur.id FROM DecActLoc d ";
 		boolean incSubQueryAct = false;
 		
 		//filtro por actividad
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getCodActividadView())){
 			subQueryAct += incSubQueryAct ? " and " : " where ";
 			subQueryAct += " d.codActividad = "+forDecJurSearchPage.getCodActividad();
 			incSubQueryAct = true;
 		}

 		//filtro base imponible desde
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getBaseImponibleDesdeView())){
 			subQueryAct += incSubQueryAct ? " and " : " where ";
 			subQueryAct += " d.baseImponible >= "+forDecJurSearchPage.getBaseImponibleDesde();
 			incSubQueryAct = true;
 		}
 		
 		//filtro base imponible hasta
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getBaseImponibleHastaView())){
 			subQueryAct += incSubQueryAct ? " and " : " where ";
 			subQueryAct += " d.baseImponible <= "+forDecJurSearchPage.getBaseImponibleHasta();
 			incSubQueryAct = true;
 		}
 		
 		//filtro bases alicuota desde
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getAlicuotaDesdeView())){
 			subQueryAct += incSubQueryAct ? " and " : " where ";
 			subQueryAct += " d.aliCuota >= "+forDecJurSearchPage.getAlicuotaDesde();
 			incSubQueryAct = true;
 		}
 		
 		//filtro bases alicuota hasta
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getAlicuotaHastaView())){
 			subQueryAct += incSubQueryAct ? " and " : " where ";
 			subQueryAct += " d.aliCuota <= "+forDecJurSearchPage.getAlicuotaHasta();
 			incSubQueryAct = true;
 		}
 		
 		if (incSubQueryAct) {
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.id IN("+subQueryAct+")";
			flagAnd = true;
		}
 		//<<-- SUBQUERY ACTIVIDAD
 		
 		
 		//-->> SUBQUERY LOCAL
 		String subQueryLoc = "SELECT l.forDecJur.id FROM Local l ";
 		boolean incSubQueryLoc = false;
 		
 		//filtro adicional mesas y sillas desde
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getAdiMesasYSillasDesdeView())){
 			subQueryLoc += incSubQueryLoc ? " and " : " where ";
 			subQueryLoc += " l.mesasYSillas >= "+forDecJurSearchPage.getAdiMesasYSillasDesde();
 			incSubQueryLoc = true;
 		}
 		
 		//filtro dicional mesas y sillas hasta
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getAdiMesasYSillasHastaView())){
 			subQueryLoc += incSubQueryLoc ? " and " : " where ";
 			subQueryLoc += " l.mesasYSillas <= "+forDecJurSearchPage.getAdiMesasYSillasHasta();
 			incSubQueryLoc = true;
 		}
 		
 		//filtro adicional publicidad desde
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getAdiPublicidadDesdeView())){
 			subQueryLoc += incSubQueryLoc ? " and " : " where ";
 			subQueryLoc += " l.publicidad >= "+forDecJurSearchPage.getAdiPublicidadDesde();
 			incSubQueryLoc = true;
 		}
 		
 		//filtro adicional publicidad hasta
 		if(!StringUtil.isNullOrEmpty(forDecJurSearchPage.getAdiPublicidadHastaView())){
 			subQueryLoc += incSubQueryLoc ? " and " : " where ";
 			subQueryLoc += " l.publicidad <= "+forDecJurSearchPage.getAdiPublicidadHasta();
 			incSubQueryLoc = true;
 		}
 		
 		if (incSubQueryLoc) {
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.id IN("+subQueryLoc+")";
			flagAnd = true;
		}
 		//<<-- SUBQUERY LOCAL
 		
		// Order By
		queryString += " order by t.fechaPresentacion, t.nroFormulario ";
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return executeCountedSearch(queryString, forDecJurSearchPage);
	}
	
	/**
	 * Verifica si existe al menos un Formulario de Declaraciones Juradas para el Envio Osiris
	 * 
	 * @param envioOsiris
	 * @return
	 * @throws Exception
	 */
	public Boolean existenForDecJurForEnvio(EnvioOsiris envioOsiris) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from ForDecJur t";
		queryString += " where t.envioOsiris.id = "+envioOsiris.getId();
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    query.setMaxResults(1);
	    ForDecJur forDecJur = (ForDecJur) query.uniqueResult();
		if(forDecJur != null)
			return true;
		
		return false;
	}

}
