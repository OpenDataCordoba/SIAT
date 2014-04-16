//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a Rubro
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_rubro")
public class Rubro extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "desRubro", columnDefinition="VARCHAR", length=100, nullable=false) 
	private String desRubro;
	
	@ManyToOne( cascade= {}  )  // ver el cascade ,targetEntity=Categoria.class
    @JoinColumn(name="idCategoria")
	private Categoria categoria;
	
	@OneToMany(cascade={})
	@JoinColumn(name="idRubro")
	private List<Subrubro> listSubrubro;
	
	public Rubro(){
		super();
	}
	
	public static Rubro getById(Long id) {
		return (Rubro) DefDAOFactory.getRubroDAO().getById(id);
	}
	
	//	getters y setters
	public String getDesRubro() {
		return desRubro;
	}
	public void setDesRubro(String desRubro) {
		this.desRubro = desRubro;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public List<Subrubro> getListSubrubro() {
		return listSubrubro;
	}
	public void setListSubrubro(List<Subrubro> listSubrubro) {
		this.listSubrubro = listSubrubro;
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
	
	
	//////////// Metodos de SubRubro
	
	public Subrubro createSubrubro(Subrubro subrubro) throws Exception {
		
		// Validaciones de VO y negocio
		if (!subrubro.validateCreate()) {
			return subrubro;
		}

		DefDAOFactory.getSubrubroDAO().update(subrubro);
		
		return subrubro;
	}	

	public Subrubro updateSubrubro(Subrubro subrubro) throws Exception {
		
		// Validaciones de VO y negocio
		if (!subrubro.validateUpdate()) {
			return subrubro;
		}

		DefDAOFactory.getSubrubroDAO().update(subrubro);
		
		return subrubro;
	}	

	public Subrubro deleteSubrubro(Subrubro subrubro) throws Exception {
		
		// Validaciones de VO y negocio
		if (!subrubro.validateDelete()) {
			return subrubro;
		}

		DefDAOFactory.getSubrubroDAO().delete(subrubro);
		
		return subrubro;
	}	

}
