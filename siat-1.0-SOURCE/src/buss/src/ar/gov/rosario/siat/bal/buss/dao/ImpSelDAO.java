//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.ImpSel;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ImpSelDAO extends GenericDAO {

	
	
	public ImpSelDAO() {
		super(ImpSel.class);
	}
	

	/**
	 * Obtiene un SelladoImporte por su codigo
	 */
	public ImpSel getByCodigo(String codigo) throws Exception {
		ImpSel impSel;
		String queryString = "from ImpSel t where t.codImpSel = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		impSel = (ImpSel) query.uniqueResult();	

		return impSel; 
	}
	
	/**
	 * FALTA PROBARLO
	 * Trae el importe de un sellado para una determinada fecha.
	 * En una misma fecha no pueda haber más de uno. 
	 * @param fecha
	 * @return
	 */
	public ImpSel getByFecha(Date fecha){
		ImpSel impSel;
		String queryString = "from ImpSel t where t.fechaDesde <= TO_DATE('" + 
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
			" AND (fechaHasta IS NOT NULL OR fechaHasta>"+DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		impSel = (ImpSel) query.uniqueResult();	

		return impSel;
	}
	
	/**
	 * Devuelve TRUE si existe un impSel entre esas fechas para un determinado sellado, ya sea que haya empezado antes y/o que termine después y/o que contenga el rango ingresado.
	 * Es para evitar el solapamiento de los importes de un sellado
	 * @param idSellado 
	 * @param idSelladoImporteActual Si no es nulo, se lo agrega a la consulta para que no devuelva TRUE si existe este id entre las fechas 
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return
	 */
	public Boolean existeImpSel(Long idSellado, Long idImpSelActual, Date fechaDesde, Date fechaHasta){
		// Trae las que empiezen y/o terminen entre esas fechas ó las que contengan al rango ingresado
		String queryString = "from ImpSel t where t.sellado.id="+idSellado;
		if(idImpSelActual!=null && idImpSelActual>0)
				queryString += " AND t.id<>"+idImpSelActual;
		if(fechaDesde!=null && fechaHasta!=null){
									// registros que empiecen o terminen entre esas fechas
			queryString += " AND ( ( t.fechaDesde BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR ( t.fechaHasta BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR"+
									// registros que contengan el rango de fechas ingresado (empiezan antes y terminan después o no terminan)
									"( t.fechaDesde <=TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										" AND ( t.fechaHasta IS NULL OR t.fechaHasta >= TO_DATE('"+
												DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+										
											  ")"+
									")"+
								")";	
		}else{
			if(fechaDesde!=null){
				queryString +=" AND ("+
										// que empiece después
									  	"t.fechaDesde >= TO_DATE('"+
											DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										// que termine después
										" OR t.fechaHasta >= TO_DATE('"+
											DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+	
										//que empiece antes y no termine
										" OR (t.fechaDesde <= TO_DATE('"+
												DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND "+
												"t.fechaHasta IS NULL"+
											 ")"+
									")";
			}
			if(fechaHasta!=null){
				//TODO implementar esta parte
			}
		}
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		List<ImpSel> listResult = (ArrayList<ImpSel>)query.list();	

		return (listResult!=null && !listResult.isEmpty()?true:false);
	}	
	
	public ImpSel getBySellado(long idSellado, Date fecha){
		String queryString = "from ImpSel t where t.sellado.id="+idSellado;
		queryString +=" AND t.fechaDesde<=TO_DATE('"+
											DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
					 " AND (t.fechaHasta IS NULL OR t.fechaHasta>TO_DATE('"+
											DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))"+
					 " AND t.estado="+Estado.ACTIVO.getId();
		Session session = SiatHibernateUtil.currentSession();
		return (ImpSel)session.createQuery(queryString).uniqueResult();
	}
	
	/**
	 * Devuelve el ImpSel (Importe de Sellado) con fecha hasta null.
	 * 
	 * @param sellado
	 * @param impSel a Excluir
	 * @return
	 * @throws Exception
	 */
	public ImpSel getConFechaHastaNullBySellado(Sellado sellado) throws Exception{
		Session session = SiatHibernateUtil.currentSession();
				
		String queryString = "from ImpSel t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.sellado.id = " + sellado.getId();
        
        queryString += " and t.fechaHasta is null"; 
                
	    Query query = session.createQuery(queryString);
	    ImpSel impSel = (ImpSel) query.uniqueResult();
		
		return impSel; 
	}

}
