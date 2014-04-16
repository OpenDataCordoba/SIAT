//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.ComAjuDetVO;
import ar.gov.rosario.siat.ef.iface.model.ComAjuVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a ComAju - Compensacion de los ajustes
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_comAju")
public class ComAju extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDetAju")
	private DetAju detAju;
	
	@Column(name="fechaSolicitud")
	private Date fechaSolicitud;

	@Column(name = "fechaAplicacion")
	private Date fechaAplicacion;

	@Column(name = "saldoFavorOriginal")
	private Double saldoFavorOriginal;

	@Column(name = "totalCompensado")
	private Double totalCompensado;

	@OneToMany( mappedBy="comAju")
	@JoinColumn(name="idComAju")
	private List<ComAjuDet> listComAjuDet = new ArrayList<ComAjuDet>();
	
	// <#Propiedades#>

	// Constructores
	public ComAju() {
		super();
		// Seteo de valores default
	}

	public ComAju(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static ComAju getById(Long id) {
		return (ComAju) EfDAOFactory.getComAjuDAO().getById(id);
	}

	public static ComAju getByIdNull(Long id) {
		return (ComAju) EfDAOFactory.getComAjuDAO().getByIdNull(id);
	}

	public static List<ComAju> getList() {
		return (ArrayList<ComAju>) EfDAOFactory.getComAjuDAO().getList();
	}

	public static List<ComAju> getListActivos() {
		return (ArrayList<ComAju>) EfDAOFactory.getComAjuDAO().getListActiva();
	}

	// Getters y setters
	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public DetAju getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAju detAju) {
		this.detAju = detAju;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public Double getSaldoFavorOriginal() {
		return saldoFavorOriginal;
	}

	public void setSaldoFavorOriginal(Double saldoFavorOriginal) {
		this.saldoFavorOriginal = saldoFavorOriginal;
	}

	public Double getTotalCompensado() {
		return totalCompensado;
	}

	public void setTotalCompensado(Double totalCompensado) {
		this.totalCompensado = totalCompensado;
	}

	public List<ComAjuDet> getListComAjuDet() {
		return listComAjuDet;
	}

	public void setListComAjuDet(List<ComAjuDet> listComAjuDet) {
		this.listComAjuDet = listComAjuDet;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
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
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (getDetAju() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DETAJU_LABEL);
		}

		if (fechaAplicacion == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	EfError.COMAJU_FECHA_APLICACION_LABEL);
		}
		
		if (fechaSolicitud == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	EfError.COMAJU_FECHA_SOLICITUD_LABEL);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el ComAju. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getComAjuDAO().update(this);
	}

	/**
	 * Desactiva el ComAju. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getComAjuDAO().update(this);
	}

	/**
	 * Valida la activacion del ComAju
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del ComAju
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// ---> ABM ComAjuDet
	public ComAjuDet createComAjuDet(ComAjuDet comAjuDet) throws Exception {
		// Validaciones de negocio
		if (!comAjuDet.validateCreate()) {
			return comAjuDet;
		}
		EfDAOFactory.getComAjuDetDAO().update(comAjuDet);
		return comAjuDet;
	}

	public ComAjuDet updateComAjuDet(ComAjuDet comAjuDet) throws Exception {

		// Validaciones de negocio
		if (!comAjuDet.validateUpdate()) {
			return comAjuDet;
		}
		EfDAOFactory.getComAjuDetDAO().update(comAjuDet);

		return comAjuDet;
	}

	public ComAjuDet deleteComAjuDet(ComAjuDet comAjuDet) throws Exception {

		// Validaciones de negocio
		if (!comAjuDet.validateDelete()) {
			return comAjuDet;
		}

		EfDAOFactory.getComAjuDetDAO().delete(comAjuDet);

		return comAjuDet;
	}
	// <#MetodosBeanDetalle#>

	public ComAjuVO toVO4View() throws Exception {
		ComAjuVO comAjuVO = new ComAjuVO();
		comAjuVO.setId(getId());
		comAjuVO.setFechaAplicacion(fechaAplicacion);
		comAjuVO.setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));
		comAjuVO.setSaldoFavorOriginal(saldoFavorOriginal);
		comAjuVO.setTotalCompensado(totalCompensado);
		comAjuVO.setFechaSolicitud(fechaSolicitud);
		
		comAjuVO.setDetAju(detAju.toVO4View());
		
		// lista de comAjuDet
		List<ComAjuDetVO> listComAjuDetVO = new ArrayList<ComAjuDetVO>();
		for(ComAjuDet comAjuDet: listComAjuDet){
			listComAjuDetVO.add(comAjuDet.toVO4View());
		}
		comAjuVO.setListComAjuDet(listComAjuDetVO);
		
		return comAjuVO;
	}
}
