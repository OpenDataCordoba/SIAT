//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del AnuObrDet
 * @author tecso
 *
 */
public class AnuObrDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "anuObrDetVO";
	
	private AnulacionObraVO anulacionObra = new AnulacionObraVO();
	private Long idDeuda;
	
	// Constructores
	public AnuObrDetVO() {
		super();
	}

	// Getters y Setters
	public AnulacionObraVO getAnulacionObra() {
		return anulacionObra;
	}

	public void setAnulacionObra(AnulacionObraVO anulacionObra) {
		this.anulacionObra = anulacionObra;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	
}
