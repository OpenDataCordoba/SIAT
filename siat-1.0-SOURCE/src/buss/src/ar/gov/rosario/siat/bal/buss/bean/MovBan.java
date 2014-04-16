//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Movimientos Bancarios Osiris. Se reciben, por email, archivos "movBan.txt" con información de recaudación AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_movBan")
public class MovBan extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "idOrgRecAfip")
	private Long idOrgRecAfip;

	@Column(name = "bancoAdm")
	private Long bancoAdm;
	
	@Column(name = "fechaProceso")
	private Date fechaProceso;
	
	@Column(name = "fechaAcredit")
	private Date fechaAcredit;
		
	@Column(name = "totalDebito")
	private Double totalDebito;
	
	@Column(name = "totalCredito")
	private Double totalCredito;
	
	@Column(name = "cantDetalle")
	private Long cantDetalle;
	
	@Column(name = "conciliado")
	private Integer conciliado;

	@OneToMany(mappedBy="movBan")
	@JoinColumn(name="idMovBan")
	private List<MovBanDet> listMovBanDet;
	
	// Constructores
	public MovBan(){
		super();		
	}
	
	public MovBan(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	public Long getBancoAdm() {
		return bancoAdm;
	}

	public void setBancoAdm(Long bancoAdm) {
		this.bancoAdm = bancoAdm;
	}

	public Long getCantDetalle() {
		return cantDetalle;
	}

	public void setCantDetalle(Long cantDetalle) {
		this.cantDetalle = cantDetalle;
	}

	public Integer getConciliado() {
		return conciliado;
	}

	public void setConciliado(Integer conciliado) {
		this.conciliado = conciliado;
	}

	public Date getFechaAcredit() {
		return fechaAcredit;
	}

	public void setFechaAcredit(Date fechaAcredit) {
		this.fechaAcredit = fechaAcredit;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public Long getIdOrgRecAfip() {
		return idOrgRecAfip;
	}

	public void setIdOrgRecAfip(Long idOrgRecAfip) {
		this.idOrgRecAfip = idOrgRecAfip;
	}

	public Double getTotalCredito() {
		return totalCredito;
	}

	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
	}

	public Double getTotalDebito() {
		return totalDebito;
	}

	public void setTotalDebito(Double totalDebito) {
		this.totalDebito = totalDebito;
	}

	public List<MovBanDet> getListMovBanDet() {
		return listMovBanDet;
	}

	public void setListMovBanDet(List<MovBanDet> listMovBanDet) {
		this.listMovBanDet = listMovBanDet;
	}

	// Metodos de Clase
	public static MovBan getById(Long id) {
		return (MovBan) BalDAOFactory.getMovBanDAO().getById(id);
	}
	
	public static MovBan getByIdNull(Long id) {
		return (MovBan) BalDAOFactory.getMovBanDAO().getByIdNull(id);
	}
	
	public static List<MovBan> getList() {
		return (ArrayList<MovBan>) BalDAOFactory.getMovBanDAO().getList();
	}
	
	public static List<MovBan> getListActivos() {			
		return (ArrayList<MovBan>) BalDAOFactory.getMovBanDAO().getListActiva();
	}
	
	public Boolean existeDetalleSinConciliar() throws Exception{			
		return BalDAOFactory.getMovBanDetDAO().existeDetalleSinConciliarForMovBan(this);
	}
	
	public static Boolean existeParaFechaAcredit(Date fechaAcredit) throws Exception{			
		return BalDAOFactory.getMovBanDAO().existeParaFechaAcredit(fechaAcredit);
	}
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el MovBan. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getMovBanDAO().update(this);
	}

	/**
	 * Desactiva el MovBan. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getMovBanDAO().update(this);
	}
	
	/**
	 * Valida la activacion del MovBan
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del MovBan
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//	---> ABM MovBanDet
	public MovBanDet createMovBanDet(MovBanDet movBanDet) throws Exception {

		// Validaciones de negocio
		if (!movBanDet.validateCreate()) {
			return movBanDet;
		}

		BalDAOFactory.getMovBanDetDAO().update(movBanDet);

		return movBanDet;
	}
	
	public MovBanDet updateMovBanDet(MovBanDet movBanDet) throws Exception {
		
		// Validaciones de negocio
		if (!movBanDet.validateUpdate()) {
			return movBanDet;
		}

		BalDAOFactory.getMovBanDetDAO().update(movBanDet);
		
		return movBanDet;
	}
	
	public MovBanDet deleteMovBanDet(MovBanDet movBanDet) throws Exception {
	
		// Validaciones de negocio
		if (!movBanDet.validateDelete()) {
			return movBanDet;
		}
		
		BalDAOFactory.getMovBanDetDAO().delete(movBanDet);
		
		return movBanDet;
	}
	//	<--- ABM MovBanDet
}
