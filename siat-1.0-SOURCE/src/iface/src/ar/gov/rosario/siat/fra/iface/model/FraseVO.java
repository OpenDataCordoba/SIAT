//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Frase
 * @author tecso
 *
 */
public class FraseVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "fraseVO";
	
	
	private String desFrase;
	private String codFrase;
	private String modulo;
	private String pagina;
	private String etiqueta;
    private String valorPublico;
	private String valorPrivado;

	// Buss Flags
	private Boolean publicarBussEnabled = false;
	
	// View Constants
	
	
	// Constructores
	public FraseVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public FraseVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesFrase(desc);
	}
	
	// Getters y Setters
	public String getCodFrase() {
		return codFrase;
	}

	public void setCodFrase(String codFrase) {
		this.codFrase = codFrase;
	}
	
	public String getDesFrase() {
		return desFrase;
	}

	public void setDesFrase(String desFrase) {
		this.desFrase = desFrase;
	}
	
		
	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
    public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getValorPublico() {
		return valorPublico;
	}

	public void setValorPublico(String valorPublico) {
		this.valorPublico = valorPublico;
	}

	public String getValorPrivado() {
		return valorPrivado;
	}

	public void setValorPrivado(String valorPrivado) {
		this.valorPrivado = valorPrivado;
	}
	

	// Buss flags getters y setters
	public String getPublicarEnabled() {
		//por ahora no hacen falta definir permisos
		return getPublicarBussEnabled() ? "enabled" : "disabled";
	}
	
	public Boolean getPublicarBussEnabled() {
		return publicarBussEnabled;
	}
	public void setPublicarBussEnabled(Boolean publicarBussEnabled) {
		this.publicarBussEnabled = publicarBussEnabled;
	}
	
	
	// View flags getters
	
	
	// View getters
}
