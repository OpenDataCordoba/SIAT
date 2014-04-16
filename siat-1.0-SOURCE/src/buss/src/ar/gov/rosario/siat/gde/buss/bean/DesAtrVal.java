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
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a DesAtrVal Representa un atributo asociado al DesEsp y
 * se obtienen del recurso asociado a éste.
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_desAtrVal")
public class DesAtrVal extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDescuento")
	private DesEsp desEsp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idAtributo")
	private Atributo atributo;

	@Column(name = "valor")
	private String valor;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	// <#Propiedades#>

	// Constructores
	public DesAtrVal() {
		super();
		// Seteo de valores default
	}

	public DesAtrVal(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static DesAtrVal getById(Long id) {
		return (DesAtrVal) GdeDAOFactory.getDesAtrValDAO().getById(id);
	}

	public static DesAtrVal getByIdNull(Long id) {
		return (DesAtrVal) GdeDAOFactory.getDesAtrValDAO().getByIdNull(id);
	}

	public static List<DesAtrVal> getList() {
		return (ArrayList<DesAtrVal>) GdeDAOFactory.getDesAtrValDAO().getList();
	}

	public static List<DesAtrVal> getListActivos() {
		return (ArrayList<DesAtrVal>) GdeDAOFactory.getDesAtrValDAO()
				.getListActiva();
	}

	// Getters y setters

	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}

	public DesEsp getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEsp desEsp) {
		this.desEsp = desEsp;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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
		if(getAtributo()==null || getAtributo().getId()<0)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESATRVAL_ATRIBUTO_LABEL);

		if(getFechaDesde()==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESATRVAL_FECHADESDE); 

		if(getFechaHasta()==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESATRVAL_FECHAHASTA); 
		
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta()))
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.DESATRVAL_FECHADESDE, GdeError.DESATRVAL_FECHAHASTA);
		
		if(hasError())
			return false;
		
		return true;
	}

	// Metodos de negocio


	// <#MetodosBeanDetalle#>
}
