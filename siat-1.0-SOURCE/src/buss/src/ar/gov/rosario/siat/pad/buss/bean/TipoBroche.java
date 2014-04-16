//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipo de Broche
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_tipoBroche")
public class TipoBroche extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final long ID_BROCHES_DE_ADMINISTRACION=1;
	public static final long ID_CORREO_FUERA_DE_LA_CIUDAD=2;
	public static final long ID_REPARTIDORES_FUERA_DE_ZONA=3;
	public static final long ID_CORREO_DENTRO_DE_LA_CIUDAD=4;
	public static final long ID_DPVYU=5;
	public static final long ID_REPARTIDORES_DE_ZONA=6;
	public static final long ID_NO_SE_ENVIA=7;
	public static final long ID_SPV=8;
	
	@Column(name = "desTipoBroche")
	private String desTipoBroche;
	
	
	// Constructores
	
	public TipoBroche(){
		super();
	}

	// Getters y Setters
	
	public String getDesTipoBroche() {
		return desTipoBroche;
	}
	public void setDesTipoBroche(String desTipoBroche) {
		this.desTipoBroche = desTipoBroche;
	}

	// Metodos de clase	
	public static TipoBroche getById(Long id) {
		return (TipoBroche) PadDAOFactory.getTipoBrocheDAO().getById(id);
	}
	
	public static TipoBroche getByIdNull(Long id) {
		return (TipoBroche) PadDAOFactory.getTipoBrocheDAO().getByIdNull(id);
	}
	
	public static List<TipoBroche> getList() {
		return (ArrayList<TipoBroche>) PadDAOFactory.getTipoBrocheDAO().getList();
	}
	
	public static List<TipoBroche> getListActivos() {			
		return (ArrayList<TipoBroche>) PadDAOFactory.getTipoBrocheDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
}
