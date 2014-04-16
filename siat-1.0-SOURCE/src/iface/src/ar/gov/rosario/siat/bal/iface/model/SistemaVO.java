//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.TipoDeudaVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Sistema
 * @author tecso
 *
 */
public class SistemaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "sistemaVO";
	
	private Long 	nroSistema;
	private String 	desSistema="";
	private SiNo esServicioBanco = SiNo.OpcionSelecionar;
	private TipoDeudaVO tipoDeuda = new TipoDeudaVO();
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	private RecursoVO recurso = new RecursoVO();
	
	private String nroSistemaView="";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public SistemaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public SistemaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesSistema(desc);
	}

	// Getters y Setters
	public String getDesSistema() {
		return desSistema;
	}

	public void setDesSistema(String desSistema) {
		this.desSistema = desSistema;
	}

	
	public SiNo getEsServicioBanco() {
		return esServicioBanco;
	}

	public void setEsServicioBanco(SiNo esServicioBanco) {
		this.esServicioBanco = esServicioBanco;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	public TipoDeudaVO getTipoDeuda() {
		return tipoDeuda;
	}

	public void setTipoDeuda(TipoDeudaVO tipoDeuda) {
		this.tipoDeuda = tipoDeuda;
	}

	public Long getNroSistema() {
		return nroSistema;
	}

	public void setNroSistema(Long nroSistema) {
		this.nroSistema = nroSistema;
		this.nroSistemaView = (nroSistema!=null?String.valueOf(nroSistema):"");
	}

	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getNroSistemaView() {
		return nroSistemaView;
	}

	public void setNroSistemaView(String nroSistemaView) {
		this.nroSistemaView = nroSistemaView;
	}

	
}
