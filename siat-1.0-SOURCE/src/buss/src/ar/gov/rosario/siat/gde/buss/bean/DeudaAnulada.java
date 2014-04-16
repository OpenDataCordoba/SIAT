//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;

@Entity
@Table(name = "gde_deudaAnulada")
public class DeudaAnulada extends Deuda {

	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idDeuda")
	private List<DeuAnuRecCon> listDeuRecCon;
	
	@Column(name = "fechaAnulacion")
	private Date fechaAnulacion;
	
	@Column(name = "observacion")
	private String observacion;
	
	@Column(name = "idCaso")
	private String idCaso;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCorrida")
	private Corrida corrida;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idMotAnuDeu") 
	private MotAnuDeu motAnuDeu;
	
	
	// Contructores 
	public DeudaAnulada() {
		super();
	}

	// Getters y Setters
	public List<DeuAnuRecCon> getListDeuRecCon() {
		return listDeuRecCon;
	}
	public void setListDeuRecCon(List<DeuAnuRecCon> listDeuRecCon) {
		this.listDeuRecCon = listDeuRecCon;
	}
	
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public MotAnuDeu getMotAnuDeu() {
		return motAnuDeu;
	}
	public void setMotAnuDeu(MotAnuDeu motAnuDeu) {
		this.motAnuDeu = motAnuDeu;
	}
	
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	
	
	// Metodos de clase


	public static DeudaAnulada getById(Long id) {
		return (DeudaAnulada) GdeDAOFactory.getDeudaAnuladaDAO().getById(id);
	}
	
	public static DeudaAnulada getByIdNull(Long id) {
		return (DeudaAnulada) GdeDAOFactory.getDeudaAnuladaDAO().getByIdNull(id);
	}
	
	public static DeudaAnulada getByCodRefPag(Long codRefPag){
		return (DeudaAnulada) GdeDAOFactory.getDeudaAnuladaDAO().getByCodRefPag(codRefPag);
	}
	
	public static Deuda getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaAnulada) GdeDAOFactory.getDeudaAnuladaDAO().getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
	}
		
	public static Deuda getByCtaPerAnioSisResForRS(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaAnulada) GdeDAOFactory.getDeudaAnuladaDAO().getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema, resto);
	}

	public static Deuda getOriginalByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaAnulada) GdeDAOFactory.getDeudaAnuladaDAO().getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
	}
	
	public static List<Deuda> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		listDeuda.addAll(GdeDAOFactory.getDeudaAnuladaDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto));
		//return (ArrayList<Deuda>) GdeDAOFactory.getDeudaAnuladaDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
		return listDeuda;
	}
	
	public static List<DeudaAnulada> getList() {
		return (ArrayList<DeudaAnulada>) GdeDAOFactory.getDeudaAnuladaDAO().getList();
	}
	
	public static List<DeudaAnulada> getListActivos() {			
		return (ArrayList<DeudaAnulada>) GdeDAOFactory.getDeudaAnuladaDAO().getListActiva();
	}
	
	public static Deuda getByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		return (DeudaAnulada) GdeDAOFactory.getDeudaAnuladaDAO().getByCuentaPeriodoAnio(cuenta, periodo, anio);
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
	
	// Administracion de DeuAnuRecCon
	public DeuAnuRecCon createDeuAnuRecCon(DeuAnuRecCon deuAnuRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuAnuRecCon.validateCreate()) {
			return deuAnuRecCon;
		}

		GdeDAOFactory.getDeuAnuRecConDAO().update(deuAnuRecCon);
		
		return deuAnuRecCon;
	}	

	public DeuAnuRecCon updateDeuAnuRecCon(DeuAnuRecCon deuAnuRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuAnuRecCon.validateUpdate()) {
			return deuAnuRecCon;
		}

		GdeDAOFactory.getDeuAnuRecConDAO().update(deuAnuRecCon);
		
		return deuAnuRecCon;
	}	

	public DeuAnuRecCon deleteDeuAnuRecCon(DeuAnuRecCon deuAnuRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuAnuRecCon.validateDelete()) {
			return deuAnuRecCon;
		}
				
		GdeDAOFactory.getDeuAnuRecConDAO().delete(deuAnuRecCon);
		
		return deuAnuRecCon;
	}


}
