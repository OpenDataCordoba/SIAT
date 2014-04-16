//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a Subrubro
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_subrubro")
public class Subrubro extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "desSubrubro", columnDefinition="VARCHAR", length=100, nullable=false) 
	private String desSubrubro;
	
	@ManyToOne( cascade= {}  )  // ver el cascade ,targetEntity=Rubro.class
    @JoinColumn(name="idRubro")
	private Rubro rubro; // idRubro

	
	public Subrubro(){
		super();
	}
	
	public static Subrubro getById(Long id) {
		return (Subrubro) DefDAOFactory.getSubrubroDAO().getById(id);
	}
	
	//	getters y setters
	public String getDesSubrubro() {
		return desSubrubro;
	}
	public void setDesSubrubro(String desSubrubro) {
		this.desSubrubro = desSubrubro;
	}
	public Rubro getRubro() {
		return rubro;
	}
	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

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
}
