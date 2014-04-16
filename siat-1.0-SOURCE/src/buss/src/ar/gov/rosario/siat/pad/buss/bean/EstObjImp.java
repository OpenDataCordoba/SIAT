//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a EstObjImp
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_estObjImp")
public class EstObjImp extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long ID_EST_ALTA_OFICIO = 1L;
	
	
	@Column(name = "desEstObjImp")
	private String desEstObjImp;

	// <#Propiedades#>

	// Constructores
	public EstObjImp() {
		super();
		// Seteo de valores default
	}

	public EstObjImp(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static EstObjImp getById(Long id) {
		return (EstObjImp) PadDAOFactory.getEstObjImpDAO().getById(id);
	}

	public static EstObjImp getByIdNull(Long id) {
		return (EstObjImp) PadDAOFactory.getEstObjImpDAO().getByIdNull(id);
	}

	public static List<EstObjImp> getList() {
		return (List<EstObjImp>) PadDAOFactory.getEstObjImpDAO().getList();
	}

	public static List<EstObjImp> getListActivos() {
		return (List<EstObjImp>) PadDAOFactory.getEstObjImpDAO()
				.getListActiva();
	}

	// Getters y setters
	public String getDesEstObjImp() {
		return desEstObjImp;
	}

	public void setDesEstObjImp(String desEstObjImp) {
		this.desEstObjImp = desEstObjImp;
	}


	// <#MetodosBeanDetalle#>
}
