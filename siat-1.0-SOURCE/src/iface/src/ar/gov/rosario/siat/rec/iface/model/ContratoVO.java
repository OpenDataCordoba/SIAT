//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Contrato
 * @author tecso
 *
 */
public class ContratoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "contratoVO";
	
	private RecursoVO recurso = new RecursoVO();
	private TipoContratoVO tipoContrato = new TipoContratoVO();
	private String numero;
	private Double importe;
	private String descripcion;
	
	private String importeView;
	
	// Buss Flags
	
	// View Constants
	
	// Constructores
	public ContratoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ContratoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}
	
	// Getters y Setters
	
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public TipoContratoVO getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(TipoContratoVO tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe); 
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	
	public Long getIdRecurso() {
		Long idRecurso = this.getRecurso().getId();
		if (idRecurso == null) {
			idRecurso = new Long(-1);
		}
		return idRecurso;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
