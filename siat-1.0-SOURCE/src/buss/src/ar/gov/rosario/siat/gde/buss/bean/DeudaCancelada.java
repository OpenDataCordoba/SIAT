//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;

/**
 * Deuda Cancelada
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_deudaCancelada")
public class DeudaCancelada extends Deuda {

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idDeuda")
	private List<DeuCanRecCon> listDeuRecCon;

	// Contructores 
	public DeudaCancelada() {
		super();
	}

	// Getters y Setters

	public List<DeuCanRecCon> getListDeuRecCon() {
		return listDeuRecCon;
	}

	public void setListDeuRecCon(List<DeuCanRecCon> listDeuRecCon) {
		this.listDeuRecCon = listDeuRecCon;
	}
	
	// Metodos de clase

	public static DeudaCancelada getById(Long id) {
		return (DeudaCancelada) GdeDAOFactory.getDeudaCanceladaDAO().getById(id);
	}
	
	public static DeudaCancelada getByIdNull(Long id) {
		return (DeudaCancelada) GdeDAOFactory.getDeudaCanceladaDAO().getByIdNull(id);
	}
	
	public static DeudaCancelada getByCodRefPag(Long codRefPag){
		return (DeudaCancelada) GdeDAOFactory.getDeudaCanceladaDAO().getByCodRefPag(codRefPag);
	}

	public static List<Deuda> getByCodRefPagBySearchPage(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		return (List<Deuda>) GdeDAOFactory.getDeudaAdminDAO().getByCodRefPagBySearchPage(liqCodRefPagSearchPage);
	}
	
	public static Deuda getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaCancelada) GdeDAOFactory.getDeudaCanceladaDAO().getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
	}
		
	public static Deuda getByCtaPerAnioSisResForRS(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaCancelada) GdeDAOFactory.getDeudaCanceladaDAO().getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema, resto);
	}

	public static Deuda getOriginalByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaCancelada) GdeDAOFactory.getDeudaCanceladaDAO().getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
	}
	
	public static List<Deuda> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		listDeuda.addAll(GdeDAOFactory.getDeudaCanceladaDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto));
		//return (ArrayList<Deuda>) GdeDAOFactory.getDeudaCanceladaDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
		return listDeuda;
	}
	
	public static List<DeudaCancelada> getList() {
		return (ArrayList<DeudaCancelada>) GdeDAOFactory.getDeudaCanceladaDAO().getList();
	}
	
	public static List<DeudaCancelada> getListActivos() {			
		return (ArrayList<DeudaCancelada>) GdeDAOFactory.getDeudaCanceladaDAO().getListActiva();
	}
	
	public static Deuda getByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		return (DeudaCancelada) GdeDAOFactory.getDeudaCanceladaDAO().getByCuentaPeriodoAnio(cuenta, periodo, anio);
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;		
	}

	/**
	 * Valida la eliminacion
	 * @author 
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio
	
	// Administracion de DeuCanRecCon
	public DeuCanRecCon createDeuCanRecCon(DeuCanRecCon deuCanRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuCanRecCon.validateCreate()) {
			return deuCanRecCon;
		}

		GdeDAOFactory.getDeuCanRecConDAO().update(deuCanRecCon);
		
		return deuCanRecCon;
	}	

	public DeuCanRecCon updateDeuCanRecCon(DeuCanRecCon deuCanRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuCanRecCon.validateUpdate()) {
			return deuCanRecCon;
		}

		GdeDAOFactory.getDeuCanRecConDAO().update(deuCanRecCon);
		
		return deuCanRecCon;
	}	

	public DeuCanRecCon deleteDeuCanRecCon(DeuCanRecCon deuCanRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuCanRecCon.validateDelete()) {
			return deuCanRecCon;
		}
				
		GdeDAOFactory.getDeuCanRecConDAO().delete(deuCanRecCon);
		
		return deuCanRecCon;
	}

	/*
	@Override
	public List<DeuRecCon> getListRecCon() {
		// TODO Auto-generated method stub
		return null;
	}
	*/	


}
