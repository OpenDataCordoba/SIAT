//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ConstanciaDeu;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPro;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ConstanciaDeuDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(ConstanciaDeuDAO.class);	
	
	public static String NAME_SEQUENCE = "gde_constancia_sq";
	
	private static long migId = -1;
	
	public ConstanciaDeuDAO() {
		super(ConstanciaDeu.class);
	}

	public List<ConstanciaDeu> getBySearchPage(ConstanciaDeuSearchPage searchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		String queryString = "from ConstanciaDeu t ";
	    boolean flagAnd = false;
	
		// Armamos filtros del HQL
		if (searchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}			
		
		// filtro sellado excluidos
 		List<PlaEnvDeuProVO> listExcluidos = (ArrayList<PlaEnvDeuProVO>) searchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por recurso
 		if (!ModelUtil.isNullOrEmpty(searchPage.getConstanciaDeu().getCuenta().getRecurso())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.recurso.id=" + searchPage.getConstanciaDeu().getCuenta().getRecurso().getId();
			flagAnd = true;
		}

		// filtro por Procurador
 		if (!ModelUtil.isNullOrEmpty(searchPage.getConstanciaDeu().getProcurador())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.procurador.id =" + searchPage.getConstanciaDeu().getProcurador().getId();
			flagAnd = true;
		}

		// filtro por NroConstancia
 		if (searchPage.getConstanciaDeu().getNumero()!=null && searchPage.getConstanciaDeu().getNumero()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.numero =" + searchPage.getConstanciaDeu().getNumero();
			flagAnd = true;
		}
 		
		// filtro anioConstancia
 		if (searchPage.getConstanciaDeu().getAnio()!=null && searchPage.getConstanciaDeu().getAnio()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.anio =" + searchPage.getConstanciaDeu().getAnio();
			flagAnd = true;
		}
 		
		// filtro por NroPlanilla
 		if (searchPage.getConstanciaDeu().getPlaEnvDeuPro().getNroPlanilla()!=null && searchPage.getConstanciaDeu().getPlaEnvDeuPro().getNroPlanilla()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.nroPlanilla =" + searchPage.getConstanciaDeu().getPlaEnvDeuPro().getNroPlanilla();
			flagAnd = true;
		}
 		
		// filtro anioPlanilla
 		if (searchPage.getConstanciaDeu().getPlaEnvDeuPro().getAnioPlanilla()!=null && searchPage.getConstanciaDeu().getPlaEnvDeuPro().getAnioPlanilla()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.plaEnvDeuPro.anioPlanilla =" + searchPage.getConstanciaDeu().getPlaEnvDeuPro().getAnioPlanilla();
			flagAnd = true;
		}
 		
 		// filtro por NumeroCuenta
		if(!StringUtil.isNullOrEmpty(searchPage.getConstanciaDeu().getCuenta().getNumeroCuenta())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.numeroCuenta = '" + 
				StringUtil.formatNumeroCuenta(searchPage.getConstanciaDeu().getCuenta().getNumeroCuenta()) + "'";
			flagAnd = true;
		}
 		
		// filtro estadoConstancia
 		if (!ModelUtil.isNullOrEmpty(searchPage.getConstanciaDeu().getEstConDeu())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.estConDeu.id =" + searchPage.getConstanciaDeu().getEstConDeu().getId();
			flagAnd = true;
		} 		

		// filtro fechaEnvioDesde
 		if (searchPage.getFechaEnvioDesde()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.procesoMasivo.fechaEnvio>=TO_DATE('" +DateUtil.formatDate(searchPage.getFechaEnvioDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
			flagAnd = true;
		}
 		
		// filtro fechaEnvioHasta
 		if (searchPage.getFechaEnvioHasta()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.procesoMasivo.fechaEnvio<=TO_DATE('" +DateUtil.formatDate(searchPage.getFechaEnvioHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
			flagAnd = true;
		} 		
 		
 		//filtro por estados excluidos
 		if(searchPage.getListEstConDeuVOExluir()!=null && !searchPage.getListEstConDeuVOExluir().isEmpty()){
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(searchPage.getListEstConDeuVOExluir());
			queryString += " t.estConDeu.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
 		}
 		
 		//buscar solo por habilitadas 
 		if(searchPage.getBuscarSoloHabilitadas()){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.fechaHabilitacion is not null";
 		}
 		
 		//buscar solo creadas manualmente 
 		if(searchPage.getBuscarCreadasManualmente()){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.plaEnvDeuPro.id is null";
 		}
 		
 		// Order By
		queryString += " order by t.anio DESC, t.numero DESC";
		
	    List<ConstanciaDeu> listResult = (ArrayList<ConstanciaDeu>) executeCountedSearch(queryString, searchPage);
		
		return listResult;
	}
	
	/**
	 * Obtiene la Constancia de Deuda para una Planilla de Envio de Deuda a Procurador y una cuenta
	 * @param plaEnvDeuPro
	 * @param cuenta
	 * @return ConstanciaDeu
	 */
	public ConstanciaDeu getConstanciaDeuByPlaEnvDeuProCuenta(PlaEnvDeuPro plaEnvDeuPro, Cuenta cuenta) {
		
		String queryString = "FROM  ConstanciaDeu cd " +
				"WHERE cd.plaEnvDeuPro = :plaEnvDeuPro " +
				"AND cd.cuenta = :cuenta"; // se puede agregar el envio judicial
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plaEnvDeuPro", plaEnvDeuPro)
			.setEntity("cuenta", cuenta);
		return (ConstanciaDeu) query.uniqueResult(); 
	}

	/**
	 * Obtiene el siguiente valor de la secuencia que corresponde al siguiente nro de Constancia.
	 * @return Long
	 */
	public Long getNextVal(){
		return super.getNextVal(ConstanciaDeuDAO.NAME_SEQUENCE);
	}

	/**
	 * Elimina las constancias de deuda del proceso masivo
	 * @param procesoMasivo
	 * @return int
	 */
	public int deleteByProcesoMasivo(ProcesoMasivo procesoMasivo) {
		
		String queryString = "DELETE FROM ConstanciaDeu cd where cd.procesoMasivo = :procesoMasivo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("procesoMasivo", procesoMasivo);
		return query.executeUpdate();	
	}

	public List<ConstanciaDeu> getListByProcesoMasivo(ProcesoMasivo procesoMasivo){
		String queryString = "FROM ConstanciaDeu cd WHERE cd.procesoMasivo = :procesoMasivo order by cd.procurador.id";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("procesoMasivo", procesoMasivo);
		return (ArrayList<ConstanciaDeu>) query.list();
	}

	public static long getMigId() {
		return migId;
	}
	public static void setMigId(long migId) {
		ConstanciaDeuDAO.migId = migId;
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
	
	public Long createForLoad(ConstanciaDeu o, LogFile output) throws Exception {
		
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|numero|anio|idprocurador|idcuenta|idestcondeu|iddomicilio|idprocesomasivo|idplaenvdeupro|observacion|
		// fechahabilitacion|usrcreador|desdomenv|destitulares|desdomubi|usuario|fechaultmdf|estado|
		
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getNumero());
		line.append("|");
		line.append(o.getAnio());
		line.append("|");
		if (o.getProcurador() != null)
			line.append(o.getProcurador().getId());
		line.append("|");
		line.append(o.getCuenta().getId());
		line.append("|");
		line.append(o.getEstConDeu().getId());
		line.append("|");
		line.append("|"); 		
		line.append("|");
		line.append("|");
		if (o.getObservacion() != null)
			line.append(o.getObservacion());
		line.append("|");
		line.append("|");
		line.append(o.getUsrCreador());
		line.append("|");		
		line.append("|");
		if (o.getDesTitulares() != null)
				line.append(o.getDesTitulares());
		line.append("|");
		line.append("|");
		
		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");
		line.append("2010-01-01 00:00:00");
		line.append("|");
		line.append("1");
		line.append("|");
	
		output.addline(line.toString());
	
		// Seteamos el id generado en el bean.
		o.setId(id);
	
		return id;
	}
}
