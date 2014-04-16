//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a DecJurPag
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipPagDecJur")
public class TipPagDecJur extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_RETENCION = 1L;
	public static final Long ID_PERCEPCION=2L;
	public static final Long ID_DJMISMOPERIODO=3L;
	
	//--> Tipos de Pagos Viejos..deberian borrarse ???
	public static final Long ID_CAMBIO_COEFICIENTE=4L;
	public static final Long ID_POR_RESOLUCION=5L;
	public static final Long ID_RESTO_PER_ANT=6L;
	//<--
	
	public static final Long ID_PAGO_OSIRIS=7L;
	public static final Long ID_RETENCION_OSIRIS=8L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name="desTipPag")
	private String desTipPag;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")
	private Date fechaHasta;
	
	
	// Constructores
	public TipPagDecJur(){
		super();
	}
	
	// Metodos de Clase
	public static TipPagDecJur getById(Long id) {
		return (TipPagDecJur) GdeDAOFactory.getTipPagDecJurDAO().getById(id);
	}
	
	public static TipPagDecJur getByIdNull(Long id) {
		return (TipPagDecJur) GdeDAOFactory.getTipPagDecJurDAO().getByIdNull(id);
	}
	
	public static List<TipPagDecJur> getList() {
		return (List<TipPagDecJur>) GdeDAOFactory.getTipPagDecJurDAO().getList();
	}
	
	public static List<TipPagDecJur> getListActivos() {			
		return (List<TipPagDecJur>) GdeDAOFactory.getTipPagDecJurDAO().getListActiva();
	}
	
	public static List<TipPagDecJur>getListVigenteByRecurso(Date fecha, Recurso recurso){
		return GdeDAOFactory.getTipPagDecJurDAO().getListVigenteByRecurso(fecha, recurso);
	}
	
	// Getters y setters
	public String getDesTipPag() {
		return desTipPag;
	}

	public void setDesTipPag(String desTipPag) {
		this.desTipPag = desTipPag;
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
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	
		
		return true;
	}
	
}
