//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.buss.dao.SweHibernateUtil;
import ar.gov.rosario.swe.iface.model.ModAplVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * ModApl
 * @author tecso
 *
 */

@Entity
@Table(name = "swe_modapl")
@SequenceGenerator(
    name="modapl_seq",
    sequenceName="swe_modapl_id_seq",
    allocationSize = 0
)

public class ModApl extends BaseBO {
		
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator="modapl_seq",strategy=GenerationType.SEQUENCE)
	    private Long id;
	 
	    public Long getId() {
	        return id;
	   }
	
	@Column(name = "nombreModulo")
	private String       nombreModulo;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAplicacion")
	private Aplicacion   aplicacion;
	
	public ModApl() {
		super();
	}
	
    public static ModApl getById(Long id) {
		return (ModApl) SweDAOFactory.getModAplDAO().getById(id);
	}	

	public String getNombreModulo() {
		return nombreModulo;
	}
	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}
	
	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}
	
	public void loadFromVO(ModAplVO modAplVO) {
		setNombreModulo(modAplVO.getNombreModulo());
		setAplicacion(Aplicacion.getById(modAplVO.getAplicacion().getId()));
	}

	public void loadFromVOUpdate(ModAplVO modAplVO) {
		setNombreModulo(modAplVO.getNombreModulo());
	}	

	public boolean validateCreate() {

		//limpiamos la lista de errores
		clearError();

		validate();

		if (hasError()) {
			return false;
		}

		return true;
	}

	public boolean validateUpdate() {

		//limpiamos la lista de errores
		clearError();

		validate();
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();

		if (SweHibernateUtil.hasReference(this, AccModApl.class, "modApl")) {
			addRecoverableError(SweCommonError.MODAPL_ACCMODAPL_HASREF);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private void validate () {
		
		if (StringUtil.isNullOrEmpty(getNombreModulo())) {
			addRecoverableError(SweCommonError.MODAPL_NOMBREMODULO_REQUIRED);
		}

		if (getAplicacion() == null) {
			addRecoverableError(SweCommonError.APLICACION_DESCRIPCION_REQUIRED);
		}
		
	}
	
	
}
