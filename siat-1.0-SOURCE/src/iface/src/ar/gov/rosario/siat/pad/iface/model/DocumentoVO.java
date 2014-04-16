//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class DocumentoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private TipoDocumentoVO tipoDocumento = new TipoDocumentoVO();

    private Long numero;
    private String  numeroView  = "";
    private String tipoyNumeroView ="";

	// Constructores    
    public DocumentoVO(){}

	// Getters y Setters    
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
		this.numeroView = StringUtil.completarCerosIzq(StringUtil.formatLong(numero), 8);
	}
	public TipoDocumentoVO getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(TipoDocumentoVO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	// View Getters y Setters	
	public String getNumeroView() {
		return numeroView;
	}
	public void setNumeroView(String numeroView) {
		this.numeroView = numeroView;
	}
	
	
    
	public void setTipoyNumeroView(String tipoyNumeroView) {
		this.tipoyNumeroView = tipoyNumeroView;
	}

	/**
	 * Obtiene el tipo y nro de documento adecuado a la vista
	 * @return String
	 */
	public String getTipoyNumeroView() {
		return this.tipoyNumeroView;
	}
}
