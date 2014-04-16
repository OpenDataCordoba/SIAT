//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.gde.buss.bean.Cobranza;
import ar.gov.rosario.siat.gde.iface.model.CobranzaSearchPage;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class CobranzaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CobranzaDAO.class);	
	
	public CobranzaDAO() {
		super(Cobranza.class);
	}
	
	public List<Cobranza> getListBySearchPage(CobranzaSearchPage cobranzaSearchPage, UsuarioSiat usuario) throws Exception{
		//Session session = SiatHibernateUtil.currentSession();
		
		List<Cobranza>listCobranza;
		
		boolean flagAnd=false;
		
		

		
		String queryString = "FROM Cobranza c ";
		
		if (!ModelUtil.isNullOrEmpty(cobranzaSearchPage.getContribuyente())){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.contribuyente.id = "+cobranzaSearchPage.getContribuyente().getId();
		}
		
		if (cobranzaSearchPage.getCobranza().getOrdenControl().getNumeroOrden()!=null){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.ordenControl.numeroOrden = "+cobranzaSearchPage.getCobranza().getOrdenControl().getNumeroOrden();
		}
		
		if (cobranzaSearchPage.getCobranza().getOrdenControl().getAnioOrden()!=null){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.ordenControl.anioOrden = "+cobranzaSearchPage.getCobranza().getOrdenControl().getAnioOrden();
		}
		
		if (!ModelUtil.isNullOrEmpty(cobranzaSearchPage.getCobranza().getOrdenControl().getTipoOrden())){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.ordenControl.tipoOrden.id = "+cobranzaSearchPage.getCobranza().getOrdenControl().getTipoOrden().getId();
		}
		
		if(!ModelUtil.isNullOrEmpty(cobranzaSearchPage.getCobranza().getPerCob())){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.perCob.id = "+cobranzaSearchPage.getCobranza().getPerCob().getId();
		}
		
		if (!StringUtil.isNullOrEmpty(cobranzaSearchPage.getCaso().getIdFormateado())){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.ordenControl.idCaso = '"+cobranzaSearchPage.getCaso().getIdFormateado()+"'";
		}
		
		if(!ModelUtil.isNullOrEmpty(cobranzaSearchPage.getCobranza().getEstadoCobranza())){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.estadoCobranza.id = "+cobranzaSearchPage.getCobranza().getEstadoCobranza().getId();
		}
		
		if (cobranzaSearchPage.getProConDes()!=null){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.proFecCon >= TO_DATE('" + 
				DateUtil.formatDate(cobranzaSearchPage.getProConDes(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
		}
		
		if (cobranzaSearchPage.getProConHas()!=null){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.proFecCon <= TO_DATE('" + 
			DateUtil.formatDate(cobranzaSearchPage.getProConHas(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
		}
		
		if(!usuario.getArea().equals(Area.getByCodigo(Area.COD_SIAT))){
			queryString += (flagAnd==false)?" where ":" and ";
			flagAnd = true;
			queryString += "c.area.id = "+usuario.getArea().getId();
		}
		

		log.debug("QUERY STRING: "+queryString);
		
		listCobranza = (List<Cobranza>)executeCountedSearch(queryString, cobranzaSearchPage);
		
		return listCobranza;
	}
	
	
	public Cobranza getByOrdenControl(OrdenControl ordenControl){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT DISTINCT cd.cobranza FROM CobranzaDet cd, DetAjuDet d WHERE cd.detAjuDet.id=d.id";
		queryString += " AND d.detAju.ordenControl.id = "+ordenControl.getId();
		
		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		return (Cobranza)query.uniqueResult();
		
	}
}
