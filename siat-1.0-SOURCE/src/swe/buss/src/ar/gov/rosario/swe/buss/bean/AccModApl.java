//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import java.util.List;

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
import ar.gov.rosario.swe.iface.model.AccModAplVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * ModApl
 * @author tecso
 *
 */

@Entity
@Table(name = "swe_accmodapl")
@SequenceGenerator(
    name="accmodapl_seq",
    sequenceName="swe_accmodapl_id_seq",
    allocationSize = 0
)
public class AccModApl extends BaseBO {	
 	private static final long serialVersionUID = 1L;
 	
    @Id @GeneratedValue(generator="accmodapl_seq",strategy=GenerationType.SEQUENCE)
	private Long id;
 
    public Long getId() {
        return id;
    }

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAplicacion") 
	private Aplicacion aplicacion;	
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idModApl") 
	private ModApl modApl;
	
	@Column(name = "nombreAccion")
	private String  nombreAccion;
	
	@Column(name = "nombreMetodo")
	private String  nombreMetodo;
	
	@Column(name = "descripcion")
	private String  descripcion; 
	
	public AccModApl() {
		super();
	}
	
    public static AccModApl getById(Long id) {
		return (AccModApl) SweDAOFactory.getAccModAplDAO().getById(id);
	}	
	
	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public ModApl getModApl() {
		return modApl;
	}

	public void setModApl(ModApl modApl) {
		this.modApl = modApl;
	}
	
	public String getNombreAccion ()  {
		return nombreAccion;
	}

	public void setNombreAccion(String nombreAccion) {
		this.nombreAccion = nombreAccion;
	}

	public String getNombreMetodo() {
		return nombreMetodo;
	}

	public void setNombreMetodo(String nombreMetodo) {
		this.nombreMetodo = nombreMetodo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve las DISTNTAS acciones definidas para el modulo de la aplicacion pasado como parametro.<br>
	 * (Hace un distinct en la tabla de accmodapl)
	 * @param modApl
	 * @return
	 * @author arobledo
	 */
	public static List<String> getAccionesForMod(Aplicacion aplicacion, ModApl modApl){
		return SweDAOFactory.getAccModAplDAO().getAccionesForMod(aplicacion, modApl);
	}
	
	public void loadFromVO(AccModAplVO accModAplVO) {
		setModApl(ModApl.getById(accModAplVO.getModApl().getId()));
		// seteo la aplicacion del modulo
		setAplicacion(getModApl().getAplicacion());
		setNombreAccion(accModAplVO.getNombreAccion());
		setNombreMetodo(accModAplVO.getNombreMetodo());		
		setDescripcion(accModAplVO.getDescripcion());
	}

	public void loadFromVOUpdate(AccModAplVO accModAplVO) {
		setNombreAccion(accModAplVO.getNombreAccion());
		setNombreMetodo(accModAplVO.getNombreMetodo());
		setDescripcion(accModAplVO.getDescripcion());
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

		if (SweHibernateUtil.hasReference(this, RolAccModApl.class, "accModApl")) {
			addRecoverableError(SweCommonError.ACCMODAPL_ROLACCMODAPL_HASREF);
		}
		if (SweHibernateUtil.hasReference(this, ItemMenu.class, "accModApl")) {
			addRecoverableError(SweCommonError.ACCMODAPL_ITEMMENU_HASREF);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private void validate () {

		if (getAplicacion() == null) {
			addRecoverableError(SweCommonError.ACCMODAPL_APLICACION_REQUIRED);
		}
		
		if (getModApl() == null) {
			addRecoverableError(SweCommonError.ACCMODAPL_MODAPL_REQUIRED);
		}
		
		if (StringUtil.isNullOrEmpty(getNombreAccion())) {
			addRecoverableError(SweCommonError.ACCMODAPL_NOMBREACCION_REQUIRED);
		}

		if (StringUtil.isNullOrEmpty(getNombreMetodo())) {
			addRecoverableError(SweCommonError.ACCMODAPL_NOMBREMETODO_REQUIRED);
		}

		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(SweCommonError.ACCMODAPL_DESCRIPCION_REQUIRED);
		}		
		
	}
	
}
