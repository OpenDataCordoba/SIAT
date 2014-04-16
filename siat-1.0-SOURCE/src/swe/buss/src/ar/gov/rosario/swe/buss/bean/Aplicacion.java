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
import ar.gov.rosario.swe.iface.model.AplicacionVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Aplicaciones segurisadas por SWE
 * @author tecso
 *
 */

@Entity
@Table(name = "swe_aplicacion")
@SequenceGenerator(
		name="aplicacion_seq", 
		sequenceName="swe_aplicacion_id_seq",  
		allocationSize = 0
		)
public class Aplicacion extends BaseBO {
		
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(generator="aplicacion_seq",strategy=GenerationType.SEQUENCE)
	private Long id;
 
  
	public Long getId() {
        return id;
    }
    
    @Column(name = "codigo")
	private String codigo;
	
    @Column(name = "descripcion")
	private String descripcion;
    
    @Column(name = "segTimeOut")
	private Long segTimeOut;
    
    @Column(name = "maxNivelMenu")
	private Integer maxNivelMenu;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_tipoAuth")
	private TipoAuth tipoAuth = new TipoAuth();
    	
	public Aplicacion(){
		super();
	}
	
    public static Aplicacion getById(Long id) {
		return (Aplicacion) SweDAOFactory.getAplicacionDAO().getById(id);
	}

    public static List<Aplicacion> getAllActivas() throws Exception{
    	return SweDAOFactory.getAplicacionDAO().getAllActivas();
    }
    
    public List<ModApl> getListModAplActivos() {
		return SweDAOFactory.getModAplDAO().findByAplicacion(this);
	}	

	/** Obtiene la lista de acciones de la aplicacion.
	 * <p>Esta lista corresponde a la union las acciones de cada modulo. 
	*/
    public List<AccModApl> getListAccApl() {
		return SweDAOFactory.getAccModAplDAO().findByAplicacion(this);
	}	
    
	public void loadFromVO(AplicacionVO aplicacionVO) {
		TipoAuth tipAuth = null;
		setCodigo(aplicacionVO.getCodigo());
		setDescripcion(aplicacionVO.getDescripcion());
		setSegTimeOut(aplicacionVO.getSegTimeOut());
		setMaxNivelMenu(aplicacionVO.getMaxNivelMenu());		
		if (aplicacionVO.getTipoAuth().getId() != -1) 
			tipAuth = TipoAuth.getById(aplicacionVO.getTipoAuth().getId());				
		setTipoAuth(tipAuth);
				
	}

	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de VO 
		this.validateComunDeVO();
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}

	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de VO 
		this.validateComunDeVO();
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}
	
	private void validateComunDeVO() {

		// Validaciones comunes de VO
		if (StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(SweCommonError.APLICACION_CODIGO_REQUIRED);
		}

		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(SweCommonError.APLICACION_DESCRIPCION_REQUIRED);
		}

		if (getSegTimeOut() == null || getSegTimeOut().longValue() == 0) {
			addRecoverableError(SweCommonError.APLICACION_SEGTIMEOUT_REQUIRED);
		}
		if (getMaxNivelMenu() == null ) {
			addRecoverableError(SweCommonError.APLICACION_MAXNIVELMENU_REQUIRED);
		}else{
			if (getMaxNivelMenu().intValue() < 0){
				addRecoverableError(SweCommonError.APLICACION_MAXNIVELMENU_MENOR_CERO);
			}
		}
		
		if (getTipoAuth() == null) {
			addRecoverableError(SweCommonError.APLICACION_TIPOAUTH_REQUIRED);
		}
				
		System.out.print(getTipoAuth());
	}

	

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();

		if (SweHibernateUtil.hasReference(this, UsrApl.class, "aplicacion")) {
			addRecoverableError(SweCommonError.APLICACION_USRAPL_HASREF);
		}
		if (SweHibernateUtil.hasReference(this, RolApl.class, "aplicacion")) {
			addRecoverableError(SweCommonError.APLICACION_ROLAPL_HASREF);
		}
		if (SweHibernateUtil.hasReference(this, ModApl.class, "aplicacion")) {
			addRecoverableError(SweCommonError.APLICACION_MODAPL_HASREF);
		}
		if (SweHibernateUtil.hasReference(this, ItemMenu.class, "aplicacion")) {
			addRecoverableError(SweCommonError.APLICACION_ITEMMENU_HASREF);
		}
		if (SweHibernateUtil.hasReference(this, UsrAplAdmSwe.class, "aplicacion")) {
			addRecoverableError(SweCommonError.APLICACION_PERMISOSAPP_HASREF);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	//getters y setters

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
	public Long getSegTimeOut() {
		return segTimeOut;
	}
	public void setSegTimeOut(Long segTimeOut) {
		this.segTimeOut = segTimeOut;
	}
	public Integer getMaxNivelMenu() {
		return maxNivelMenu;
	}
	public void setMaxNivelMenu(Integer maxNivelMenu) {
		this.maxNivelMenu = maxNivelMenu;
	}
    
    public void setTipoAuth(TipoAuth tipoAuth) {
		this.tipoAuth = tipoAuth;
	}

	public TipoAuth getTipoAuth() {
		return tipoAuth;
	}


	/**
	 * Obtiene la lista de Items de Menu roots (de nivel cero) de la Aplicacion
	 * @return List<ItemMenu>
	 * @throws Exception
	 */
	public List<ItemMenu> getListItemMenuRoot() throws Exception{
		return SweDAOFactory.getItemMenuDAO().getListItemMenuPadresByAplicacion(this);
	}
}
