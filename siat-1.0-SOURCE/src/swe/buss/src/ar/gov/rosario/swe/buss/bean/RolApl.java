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
import ar.gov.rosario.swe.iface.model.RolAplVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.StringUtil;

@Entity
@Table(name = "swe_rolapl")
@SequenceGenerator(
    name="rolapl_seq",
    sequenceName="swe_rolapl_id_seq",
    allocationSize = 0
)
public class RolApl extends BaseBO {
	
	private static final long serialVersionUID = 1L;
		
	@Id @GeneratedValue(generator="rolapl_seq",strategy=GenerationType.SEQUENCE)
	    private Long id;
	 
	    public Long getId() {
	        return id;
	}
	
	public static final Long ROLAPL_ADMIN_SWE = 1L;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;	
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAplicacion")        
	private Aplicacion   aplicacion;
	//private Integer permiteWeb; //indica si permite Web -- Se paso al UsrApl
	
	public RolApl(){
		super();
	}
	

	public static RolApl getById(Long id) {
		return (RolApl) SweDAOFactory.getRolAplDAO().getById(id);
	}	
	
	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
/*	public Integer getPermiteWeb() {
		return permiteWeb;
	}
	public void setPermiteWeb(Integer permiteWeb) {
		this.permiteWeb = permiteWeb;
	}	
	*/

	public void loadFromVO(RolAplVO rolAplVO) throws DemodaServiceException{
		setCodigo(rolAplVO.getCodigo());
		setDescripcion(rolAplVO.getDescripcion());

		Aplicacion aplicacion = Aplicacion.getById(rolAplVO.getAplicacion().getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }
        setAplicacion(aplicacion);
        //setPermiteWeb(rolAplVO.getPermiteWeb().getBussId());
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

		if (SweHibernateUtil.hasReference(this, UsrRolApl.class, "rolApl")) {
			addRecoverableError(SweCommonError.ROLAPL_USRROLAPL_HASREF);
		}
		if (SweHibernateUtil.hasReference(this, RolAccModApl.class, "rolApl")) {
			addRecoverableError(SweCommonError.ROLAPL_ROLACCMODAPL_HASREF); 
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private void validate () {
		
		if (StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(SweCommonError.ROLAPL_CODIGO_REQUIRED);
		}
		
		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(SweCommonError.ROLAPL_DESCRIPCION_REQUIRED);
		}

		if (getAplicacion() == null) {
			addRecoverableError(SweCommonError.APLICACION_DESCRIPCION_REQUIRED);
		}
		
	}
}
