//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPro;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProVO;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PlaEnvDeuProDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlaEnvDeuProDAO.class);	
	
	public static String NAME_SEQUENCE = "gde_planenvpro_sq";

	private static long migId;
	
	public PlaEnvDeuProDAO() {
		super(PlaEnvDeuPro.class);
	}

	public List<PlaEnvDeuPro> getBySearchPage(PlaEnvDeuProSearchPage searchPage, boolean filtrarHabilitadas) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		log.debug(funcName+": enter");
		
	//	String queryString = "select distinct(t) from PlaEnvDeuPro t, ConstanciaDeu c WHERE c.plaEnvDeuPro.id = t.id ";
		String queryString = "select distinct(t.plaEnvDeuPro) from ConstanciaDeu t ";
	    boolean flagAnd = false;
	
		// Armamos filtros del HQL
		if (searchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.plaEnvDeuPro.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}			
		
		// filtro sellado excluidos
 		List<PlaEnvDeuProVO> listExcluidos = (ArrayList<PlaEnvDeuProVO>) searchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listExcluidos);
			queryString += " t.plaEnvDeuPro.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por recurso
 		if (searchPage.getPlaEnvDeuPro().getProcesoMasivo().getRecurso().getId()!=null && searchPage.getPlaEnvDeuPro().getProcesoMasivo().getRecurso().getId()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.recurso.id=" + searchPage.getPlaEnvDeuPro().getProcesoMasivo().getRecurso().getId();
			flagAnd = true;
		}

		// filtro por Procurador
 		if (searchPage.getPlaEnvDeuPro().getProcurador().getId()!=null && searchPage.getPlaEnvDeuPro().getProcurador().getId()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.procurador.id =" + searchPage.getPlaEnvDeuPro().getProcurador().getId();
			flagAnd = true;
		}
 		
		// filtro por NroPlanilla
 		if (searchPage.getPlaEnvDeuPro().getNroPlanilla()!=null && searchPage.getPlaEnvDeuPro().getNroPlanilla()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.nroPlanilla =" + searchPage.getPlaEnvDeuPro().getNroPlanilla();
			flagAnd = true;
		}
 		
		// filtro anioPlanilla
 		if (searchPage.getPlaEnvDeuPro().getAnioPlanilla()!=null && searchPage.getPlaEnvDeuPro().getAnioPlanilla()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.anioPlanilla =" + searchPage.getPlaEnvDeuPro().getAnioPlanilla();
			flagAnd = true;
		}
 		
		// filtro estadoPlanilla
 		if (searchPage.getPlaEnvDeuPro().getEstPlaEnvDeuPr().getId()!=null && searchPage.getPlaEnvDeuPro().getEstPlaEnvDeuPr().getId()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.estPlaEnvDeuPr.id =" + searchPage.getPlaEnvDeuPro().getEstPlaEnvDeuPr().getId();
			flagAnd = true;
		} 		

		// filtro fechaEnvioDesde
 		if (searchPage.getFechaEnvioDesde()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.fechaEnvio>=TO_DATE('" +DateUtil.formatDate(searchPage.getFechaEnvioDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
			flagAnd = true;
		}
 		
		// filtro fechaEnvioHasta
 		if (searchPage.getFechaEnvioHasta()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.fechaEnvio<=TO_DATE('" +DateUtil.formatDate(searchPage.getFechaEnvioHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
			flagAnd = true;
		} 		
 		
 		// filtro por habilitadas
 		if(filtrarHabilitadas){
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.fechaHabilitacion IS NOT NULL " ;
			flagAnd = true; 			
 		}
 		
 		// Order By
		queryString += " order by t.plaEnvDeuPro.anioPlanilla, t.plaEnvDeuPro.nroPlanilla DESC";
		
		log.debug("query: "+queryString);
	    List<PlaEnvDeuPro> listResult = (ArrayList<PlaEnvDeuPro>) executeCountedSearch(queryString, searchPage);
	    Session sesion = SiatHibernateUtil.currentSession();
	    Query query = sesion.createQuery(queryString);
	    searchPage.setMaxRegistros(new Long(getCount(query)));
		
		return listResult;
	}
	
	/**
	 * Obtiene el PlaEnvDeuPro para un Procurador y un ProcesoMasivo
	 * @param  procurador
	 * @param  procesoMasivo
	 * @return PlaEnvDeuPro
	 */
	public PlaEnvDeuPro getPlaEnvDeuProByProcProMas(Procurador procurador, ProcesoMasivo procesoMasivo){
		String queryString = "FROM  PlaEnvDeuPro pedp WHERE pedp.procurador = :procurador AND pedp.procesoMasivo = :procesoMasivo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("procurador", procurador)
			.setEntity("procesoMasivo", procesoMasivo);
		return (PlaEnvDeuPro) query.uniqueResult(); 
	}
	
	
	/**
	 * Obtiene el siguiente valor de la secuencia que corresponde al siguiente nro de planilla.
	 * @return Long
	 */
	public Long getNextVal(){
		return super.getNextVal(PlaEnvDeuProDAO.NAME_SEQUENCE);
	}
	
	
	public List<PlaEnvDeuPro> getListByProcesoMasivo(ProcesoMasivo procesoMasivo){
		String queryString = "FROM  PlaEnvDeuPro pedp WHERE pedp.procesoMasivo = :procesoMasivo order by pedp.procurador.id";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("procesoMasivo", procesoMasivo);
		return (ArrayList<PlaEnvDeuPro>) query.list(); 

	}
	
	/**
	 * Elimina los Historicos de cada planilla de envio de deuda a procuradores del proceso masivo
	 * @param procesoMasivo
	 * @return int
	 */
	public int deleteByProcesoMasivo(ProcesoMasivo procesoMasivo) {
		
		String queryString = "DELETE FROM PlaEnvDeuPro pedp WHERE pedp.procesoMasivo = :procesoMasivo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("procesoMasivo", procesoMasivo);
		return query.executeUpdate();	
	}

	
	
	//////////////
	public static long getMigId() {
		return migId;
	}
	public static void setMigId(long migId) {
		PlaEnvDeuProDAO.migId = migId;
	}

	/**
	 *  Devuelve el proximo valor de id a asignar. 
	 *  Para se inicializa obteniendo el ultimo id asignado el archivo de migracion con datos pasados como parametro
	 *  y luego en cada llamada incrementa el valor.
	 * 
	 * @return long - el proximo id a asignar.
	 * @throws Exception
	 */
	public long getNextId(String path, String nameFile) throws Exception{
		// Si migId==-1 buscar MaxId en el archivo de load. Si migId!=-1, migId++ y retornar migId;
		if(getMigId()==-1){
			setMigId(this.getLastId(path, nameFile)+1);
		}else{
			setMigId(getMigId() + 1);
		}

		return getMigId();
	}
	
	public Long createForLoad(PlaEnvDeuPro o, LogFile output) throws Exception {
		long id = getNextId(output.getPath(), output.getNameFile());
/*
		Column name          Type                                    Nulls
		id                   serial                                  no
		anioplanilla         integer                                 no
		nroplanilla          integer                                 no
		idprocurador         integer                                 no
		fechaenvio           datetime year to day                    yes
		fecharecepcion       datetime year to day                    yes
		idprocesomasivo      integer                                 yes
		idestplaenvdeupr     integer                                 no
		usuario              char(1)                                 no
		fechaultmdf          datetime year to second                 no
		estado               smallint                                no
		totalregistros       integer                                 yes
		importetotal         decimal(16,6)                           yes
		cantidadcuentas      integer                                 yes
		fechahabilitacion    datetime year to second                 yes
		observaciones        varchar(100,0)                          yes		
		idCaso               varchar(255,0)                          yes
*/
		
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getNroPlanilla());
		line.append("|");
		line.append(o.getAnioPlanilla());
		line.append("|");
		line.append(o.getProcurador().getId());
		line.append("|");
		if (o.getFechaEnvio() != null) {
			line.append(DateUtil.formatDate(o.getFechaEnvio(), "yyyy-MM-dd"));
		}
		line.append("|");
		//fecharecepcion = null
		line.append("|");
		//idprocesomasivo = null
		line.append("|"); 		
		line.append(o.getEstPlaEnvDeuPr().getId());
		line.append("|"); 				
		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");
		line.append("2010-01-01 00:00:00");
		line.append("|");
		line.append("1");
		line.append("|");
		//totalregistros = null
		line.append("|");		
		//importetotal  = null
		line.append("|");
		//cantidadcuentas  = null
		line.append("|");
		//fechahabilitacion  = null
		line.append("|");
		//observaciones  = null
		line.append("|");
		if (o.getIdCaso() != null) {
			line.append(o.getIdCaso());
		}
		line.append("|");
		
		output.addline(line.toString());
		// Seteamos el id generado en el bean.
		o.setId(id);
	
		return id;
	}

	
	

}
