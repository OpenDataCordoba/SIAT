//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a RecAtrCueV
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_recAtrCueV")
public class RecAtrCueV extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idRecAtrCue")
	private RecAtrCue recAtrCue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCuenta")
	private Cuenta cuenta;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	@Column(name = "valor")
	private String valor;   

    @Column(name="idCaso") 
	private String idCaso;

	// <#Propiedades#>

	// Constructores
	public RecAtrCueV() {
		super();
		// Seteo de valores default
	}
	
	public RecAtrCueV(RecAtrCue recAtrCue, Cuenta cuenta){
		super();
		this.recAtrCue=recAtrCue;
		this.cuenta=cuenta;
	}

	public RecAtrCueV(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static RecAtrCueV getById(Long id) {
		return (RecAtrCueV) PadDAOFactory.getRecAtrCueVDAO().getById(id);
	}

	public static RecAtrCueV getByIdNull(Long id) {
		return (RecAtrCueV) PadDAOFactory.getRecAtrCueVDAO().getByIdNull(id);
	}

	public static List<RecAtrCueV> getList() {
		return (ArrayList<RecAtrCueV>) PadDAOFactory.getRecAtrCueVDAO().getList();
	}

	public static List<RecAtrCueV> getListActivos() {
		return (ArrayList<RecAtrCueV>) PadDAOFactory.getRecAtrCueVDAO()
				.getListActiva();
	}
	
	public static RecAtrCueV getVigenteByIdRecAtrCue(Long idRecCueAtr, Long idCuenta, Date fechaVigencia) {
		return (RecAtrCueV) PadDAOFactory.getRecAtrCueVDAO().getVigenteByIdRecAtrCue(idRecCueAtr,idCuenta,fechaVigencia);
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
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

	public RecAtrCue getRecAtrCue() {
		return recAtrCue;
	}

	public void setRecAtrCue(RecAtrCue recAtrCue) {
		this.recAtrCue = recAtrCue;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	// Getters y setters



	// Metodos de negocio


	// <#MetodosBeanDetalle#>
}
