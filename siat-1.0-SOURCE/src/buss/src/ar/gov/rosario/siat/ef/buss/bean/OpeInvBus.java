//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a OpeInvCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_opeInvBus")
public class OpeInvBus extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Object TIPO_AGREGAR = 1;
	public static final Object TIPO_ELIMINAR = 2;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idopeinv")
	private OpeInv opeInv;
	
	@ManyToOne
	@JoinColumn(name="idSelAlm")
	private SelAlm selAlm;

	@Column(name = "fechaBusqueda")
	private Date fechaBusqueda;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "paramSel")
	private String paramSel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCorrida")
	private Corrida corrida;
	
	@Column(name = "tipBus")
	private Integer tipBus;
		
	// <#Propiedades#>

	// Constructores
	public OpeInvBus() {
		super();
		// Seteo de valores default
	}



	// Metodos de Clase
	public static OpeInvBus getById(Long id) {
		return (OpeInvBus) EfDAOFactory.getOpeInvBusDAO().getById(id);
	}

	public static OpeInvBus getByIdNull(Long id) {
		return (OpeInvBus) EfDAOFactory.getOpeInvBusDAO().getByIdNull(id);
	}

	public static List<OpeInvBus> getList() {
		return (List<OpeInvBus>) EfDAOFactory.getOpeInvBusDAO().getList();
	}

	public static List<OpeInvBus> getListActivos() {
		return (List<OpeInvBus>) EfDAOFactory.getOpeInvBusDAO().getListActiva();
	}

	// Getters y setters
	public OpeInv getOpeInv() {
		return opeInv;
	}



	public void setOpeInv(OpeInv opeInv) {
		this.opeInv = opeInv;
	}



	public SelAlm getSelAlm() {
		return selAlm;
	}



	public void setSelAlm(SelAlm selAlm) {
		this.selAlm = selAlm;
	}



	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}



	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getParamSel() {
		return paramSel;
	}



	public void setParamSel(String paramSel) {
		this.paramSel = paramSel;
	}
	
	
	public Corrida getCorrida() {
		return corrida;
	}



	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}



	public Integer getTipBus() {
		return tipBus;
	}



	public void setTipBus(Integer tipBus) {
		this.tipBus = tipBus;
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

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el OpeInvCon. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOpeInvBusDAO().update(this);
	}

	/**
	 * Desactiva el OpeInvCon. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOpeInvBusDAO().update(this);
	}

	/**
	 * Valida la activacion del OpeInvCon
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
	 * Valida la desactivacion del OpeInvCon
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}





	// <#MetodosBeanDetalle#>
}
