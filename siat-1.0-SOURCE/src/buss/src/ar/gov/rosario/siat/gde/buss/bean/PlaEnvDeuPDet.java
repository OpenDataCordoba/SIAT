//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import coop.tecso.demoda.buss.bean.BaseBO;


@Entity
@Table(name = "gde_plaEnvDeuPDet")
public class PlaEnvDeuPDet extends BaseBO {

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idPlaEnvDeuPro")
	private PlaEnvDeuPro plaEnvDeuPro;
	
	@Column(name = "idDeuda")
	private Long idDeuda;
		
	@Column(name = "idEstPlaEnvDeuPD")
	private Long idEstPlaEnvDeuPD; 

	@Transient
	Logger log = Logger.getLogger(PlaEnvDeuPDet.class);
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaEnvDeuPDet";

	// Constructores
	public PlaEnvDeuPDet() {
		super();
	}

	// Metodos de Clase
	public static PlaEnvDeuPDet getById(Long id) {
		//return (PlaEnvDeuPDet) GdeDAOFactory.getPlaEnvDeuPDetDAO().getById(id);
		return null;
	}
	 
	public static PlaEnvDeuPDet getByIdNull(Long id) {
		//return (PlaEnvDeuPDet) GdeDAOFactory.getPlaEnvDeuPDetDAO().getByIdNull(id);
		return null;
	}
	
	public static List<PlaEnvDeuPDet> getList() {
		//return (ArrayList<PlaEnvDeuPDet>) GdeDAOFactory.getPlaEnvDeuPDetDAO().getList();
		return null;
	}
	
	public static List<PlaEnvDeuPDet> getListActivos() {			
		//return (ArrayList<PlaEnvDeuPDet>) GdeDAOFactory.getPlaEnvDeuPDetDAO().getListActiva();
		return null;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setPlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) {
		this.plaEnvDeuPro = plaEnvDeuPro;
	}

	public PlaEnvDeuPro getPlaEnvDeuPro() {
		return plaEnvDeuPro;
	}

	public Long getIdEstPlaEnvDeuPD() {
		return idEstPlaEnvDeuPD;
	}
	public void setIdEstPlaEnvDeuPD(Long idEstPlaEnvDeuPD) {
		this.idEstPlaEnvDeuPD = idEstPlaEnvDeuPD;
	}
	
}
