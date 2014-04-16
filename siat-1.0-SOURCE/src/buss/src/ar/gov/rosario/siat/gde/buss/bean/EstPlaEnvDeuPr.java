//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al Estado de la Planilla de Deuda Enviada al Procurador
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_estPlaEnvDeuPr")
public class EstPlaEnvDeuPr extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estPlaEnvDeuPr";

	 //1. Emitida 
	public static final Long ID_EMITIDA     = 1L;
	//2. Anulada (por traspaso completo o por devolucion completa) 
	public static final Long ID_ANULADA     = 2L;
	//3. Modifica (algunas constancias fueron traspasadas o fueron modificadas (quantum)) 
	public static final Long ID_MODIFICADA  = 3L;
	//4. Recompuesta 
	public static final Long ID_RECOMPUESTA = 4L;
	//5. Habilitada
	public static final Long ID_HABILITADA  = 5L;
	//6. Cancelada
	public static final Long ID_CANCELADA  = 6L;
	

	@Column(name = "desEstPlaEnvDeuPro")
	private String desEstPlaEnvDeuPro; // VARCHAR(100) NOT NULL,
	

	// Constructores
	public EstPlaEnvDeuPr(){
		super();
	}
	
	// Getters y Setters
	public String getDesEstPlaEnvDeuPro() {
		return desEstPlaEnvDeuPro;
	}
	public void setDesEstPlaEnvDeuPro(String desEstPlaEnvDeuPro) {
		this.desEstPlaEnvDeuPro = desEstPlaEnvDeuPro;
	}

	// Metodos de Clase
	public static EstPlaEnvDeuPr getById(Long id) {
		return (EstPlaEnvDeuPr) GdeDAOFactory.getEstPlaEnvDeuPrDAO().getById(id);  
	}
	 
	public static EstPlaEnvDeuPr getByIdNull(Long id) {
		return (EstPlaEnvDeuPr) GdeDAOFactory.getEstPlaEnvDeuPrDAO().getByIdNull(id);
	}
	
	public static List<EstPlaEnvDeuPr> getList() {
		return (ArrayList<EstPlaEnvDeuPr>) GdeDAOFactory.getEstPlaEnvDeuPrDAO().getList();
	}
	
	public static List<EstPlaEnvDeuPr> getListActivos() {			
		return (ArrayList<EstPlaEnvDeuPr>) GdeDAOFactory.getEstPlaEnvDeuPrDAO().getListActiva();
	}


	// Metodos de Instancia

	// Validaciones

	// Metodos de negocio
	
}
