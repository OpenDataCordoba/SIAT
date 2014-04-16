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
import ar.gov.rosario.siat.gde.iface.model.EstConDeuVO;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al Estado de la Constancia de la Deuda
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_estConDeu")
public class EstConDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estConDeu";

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
	//5- Anulada (por devolución o traspaso) 
	public static final Long ID_ANULADA = 5L;
	//6- Cancelada
	public static final Long ID_CANCELADA = 6L;
 
	@Column(name = "desEstConDeu")
	private String desEstConDeu; // VARCHAR(255) NOT NULL,
	

	// Constructores
	public EstConDeu(){
		super();
	}
	
	// Getters y Setters
	public String getDesEstConDeu() {
		return desEstConDeu;
	}
	public void setDesEstConDeu(String desEstConDeu) {
		this.desEstConDeu = desEstConDeu;
	}
	

	// Metodos de Clase
	public static EstConDeu getById(Long id) {
		return (EstConDeu) GdeDAOFactory.getEstConDeuDAO().getById(id);  
	}
	
	public static EstConDeu getByIdNull(Long id) {
		return (EstConDeu) GdeDAOFactory.getEstConDeuDAO().getByIdNull(id);
	}
	
	public static List<EstConDeu> getList() {
		return (ArrayList<EstConDeu>) GdeDAOFactory.getEstConDeuDAO().getList();
	}
	
	public static List<EstConDeu> getListActivos() {			
		return (ArrayList<EstConDeu>) GdeDAOFactory.getEstConDeuDAO().getListActiva();
	}

	/**
	 * Devuelve una lista de estado de constancia, excluyendo los pasados como parametro
	 * @param estExcluidos
	 * @return
	 * @throws Exception 
	 */
	public static List<EstConDeu> getList(List<EstConDeu> listEstExcluidos) throws Exception{
		return (ArrayList<EstConDeu>) GdeDAOFactory.getEstConDeuDAO().getList(listEstExcluidos);
	}
	
	/**
	 * Devuelve una lista de estConDeu, excluyendo los pasados como parametro (estConDeuVO)
	 * @param estExcluidos
	 * @return
	 * @throws Exception 
	 */
	public static List<EstConDeu> getListFromVO(List<EstConDeuVO> listEstExcluidosVO) throws Exception{
		List<EstConDeu> listEstConDeu = new ArrayList<EstConDeu>();
		for(EstConDeuVO estConDeuVO: listEstExcluidosVO){
			listEstConDeu.add(EstConDeu.getById(estConDeuVO.getId()));
		}
		return EstConDeu.getList(listEstConDeu);
	}
	// Metodos de Instancia

	// Validaciones

	// Metodos de negocio
	
}
