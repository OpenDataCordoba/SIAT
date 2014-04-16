//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a MotAnuDeu
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_motAnuDeu")
public class MotAnuDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;	
	
	public static final long ID_DESANULACION = 1L;
	public static final long ID_ANULACION = 2L;
	public static final long ID_FALLECIMIENTO_TITULAR = 3L;
	public static final long ID_CONDONACION = 4L;
	public static final long ID_PRESCRIPCION = 5L;
	public static final long ID_CAMBIOPLAN_CDM = 6L;
	public static final long ID_DEGLOSE_AJUSTE = 7L;

	@Column(name = "desMotAnuDeu")
	private String desMotAnuDeu;
	
	// Constructores
	public MotAnuDeu(){
		super();
	}
	
	public MotAnuDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static MotAnuDeu getById(Long id) {
		return (MotAnuDeu) GdeDAOFactory.getMotAnuDeuDAO().getById(id);
	}
	
	public static MotAnuDeu getByIdNull(Long id) {
		return (MotAnuDeu) GdeDAOFactory.getMotAnuDeuDAO().getByIdNull(id);
	}
	
	public static List<MotAnuDeu> getList() {
		return (List<MotAnuDeu>) GdeDAOFactory.getMotAnuDeuDAO().getList();
	}
	
	public static List<MotAnuDeu> getListActivos() {			
		return (List<MotAnuDeu>) GdeDAOFactory.getMotAnuDeuDAO().getListActiva();
	}
	
	public static List<MotAnuDeu> getListMotAnuDeuAnulan() {			
		return (List<MotAnuDeu>) GdeDAOFactory.getMotAnuDeuDAO().getListMotAnuDeuAnulan();
	}
	
	// Getters y setters
	public String getDesMotAnuDeu() {
		return desMotAnuDeu;
	}

	public void setDesMotAnuDeu(String desMotAnuDeu) {
		this.desMotAnuDeu = desMotAnuDeu;
	}
	
}
