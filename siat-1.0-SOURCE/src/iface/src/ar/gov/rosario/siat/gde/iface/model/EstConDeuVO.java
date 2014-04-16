//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Estado de la Constancia de deuda
 * @author tecso
 *
 */
public class EstConDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estConDeuVO";
	//7- Creada
	public static final Long ID_CREADA = 7L;	
	//1- Emitida (por envio judicial o traspaso) 
	public static final Long ID_EMITIDA = 1L;
	//2- Habilitada 
	public static final Long ID_HABILITADA = 2L;
	//3- Modificada: . datos formales                        . quantum (deuda devuelta a CA)                        . leyendas 
	public static final Long ID_MODIFICADA = 3L;
	//4- Recompuesta 
	public static final Long ID_RECOMPUESTA = 4L;
	//5- Anulada (por devoluci\u00F3n o traspaso) 
	public static final Long ID_ANULADA = 5L;
	//6- Cancelada
	public static final Long ID_CANCELADA = 6L;
	
	private String desEstConDeu;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstConDeuVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstConDeuVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstConDeu(desc);
	}

	// Getters y Setters
		public String getDesEstConDeu() {
		return desEstConDeu;
	}
	public void setDesEstConDeu(String desEstConDeu) {
		this.desEstConDeu = desEstConDeu;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
