//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Atributos del contribuyente
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_tipoDomicilio")
public class TipoDomicilio extends BaseBO {

	// Propiedades
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_DOMICILIO_ENVIO = 1L;

	@Column(name = "desTipoDomicilio")
	private String desTipoDomicilio;
	
	// Constructores
	public TipoDomicilio(){
		super();
	}
	
	// Getters y setters
	public String getDesTipoDomicilio() {
		return desTipoDomicilio;
	}
	public void setDesTipoDomicilio(String desTipoDomicilio) {
		this.desTipoDomicilio = desTipoDomicilio;
	}
	
	// Metodos de clase
	public static TipoDomicilio getById(Long id) {
		return (TipoDomicilio) PadDAOFactory.getTipoDomicilioDAO().getById(id);
	}
	
	public static TipoDomicilio getByIdNull(Long id) {
		return (TipoDomicilio) PadDAOFactory.getTipoDomicilioDAO().getByIdNull(id);
	}
	
	public static TipoDomicilio getTipoDomicilioEnvio() {
		return (TipoDomicilio) PadDAOFactory.getTipoDomicilioDAO().getById(1L);
	}

	// Metodos de Instancia
	// Validaciones
	
	/**
	 * Valida la creacion
	 * @author Ivan
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		/*Ejemplo:
		//Validaciones de VO
		if (StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(DefError.ATRIBUTO_${PROPIEDAD}_REQUIRED);
		}
		*/

		if (hasError()) {
			return false;
		}

		return true;
	}
	/**
	 * Valida la actualizacion
	 * @author Ivan
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		/*Ejemplo:
		//Validaciones de VO
		if (StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(DefError.ATRIBUTO_${PROPIEDAD}_REQUIRED);
		}
		*/

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}
	/**
	 * Valida la eliminacion
	 * @author Ivan
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		/*Ejemplo:
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado1}.class, "${bean}")) {
			addRecoverableError(DefError.ATRIBUTO_${BEANRELACIONADO1}_HASREF);
		}
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado2}.class, "${bean}")) {
			addRecoverableError(DefError.ATRIBUTO_${BEANRELACIONADO2}_HASREF);
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio
	
}
