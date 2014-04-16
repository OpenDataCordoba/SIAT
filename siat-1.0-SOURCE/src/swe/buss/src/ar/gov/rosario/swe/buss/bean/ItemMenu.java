//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.buss.dao.SweHibernateUtil;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


@Entity
@Table(name = "swe_itemMenu")
@SequenceGenerator(
    name="itemMenu_seq",
    sequenceName="swe_itemMenu_id_seq",
    allocationSize = 0
)

public class ItemMenu extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator="itemMenu_seq",strategy=GenerationType.SEQUENCE)
    private Long id;
 
    public Long getId() {
        return id;
   }
	
	
	private static Logger log = Logger.getLogger(ItemMenu.class);

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAplicacion")
	private Aplicacion   aplicacion;
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idItemMenuPadre")
	private ItemMenu     itemMenuPadre; // ojo con la recursividad
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAccModApl")
	private AccModApl    accModApl; 
	@Column(name = "titulo")
	private String       titulo;
	@Column(name = "descripcion")
	private String       descripcion;
	@Column(name = "nroOrden")
	private Integer 	 nroOrden;
	@Column(name = "url")
	private String       url;
	    
    @OneToMany()
    @JoinColumn(name="idItemMenuPadre")  
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    @OrderBy("nroOrden")
	private List<ItemMenu> listItemMenuHijos = new ArrayList<ItemMenu>(); // necesario su inicializacion
 	
	// del view
    @Transient
	private boolean seleccionadoView = false;
	
	public ItemMenu(){
	}
	
	public static ItemMenu getById(Long id) {
		return (ItemMenu) SweDAOFactory.getItemMenuDAO().getById(id);
	}
	
	public AccModApl getAccModApl() {
		return accModApl;
	}

	public void setAccModApl(AccModApl accModApl) {
		this.accModApl = accModApl;
	}

	public boolean isSeleccionadoView() {
		return seleccionadoView;
	}
	public void setSeleccionadoView(boolean seleccionadoView) {
		this.seleccionadoView = seleccionadoView;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Integer getNroOrden() {
		return nroOrden;
	}

	public void setNroOrden(Integer nroOrden) {
		this.nroOrden = nroOrden;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ItemMenu getItemMenuPadre() {
		return itemMenuPadre;
	}
	public void setItemMenuPadre(ItemMenu itemMenuPadre) {
		this.itemMenuPadre = itemMenuPadre;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public List<ItemMenu> getListItemMenuHijos() {
		return listItemMenuHijos;
	}

	public void setListItemMenuHijos(List<ItemMenu> listItemMenuHijos) {
		this.listItemMenuHijos = listItemMenuHijos;
	}
	
	/**
	 * Carga la lista de items de menu hijo del item de menu en la propiedad.
	 * @throws DemodaServiceException
	 */
	public void cargarListItemMenuHijos()throws DemodaServiceException{
		log.debug(this.getId());
		this.listItemMenuHijos = this.obtenerListItemMenuHijos();
		
		if (this.tieneHijos()) {
			log.debug("tiene hijos");
			Iterator iteratorItemMenu = listItemMenuHijos.iterator();
			while ( iteratorItemMenu.hasNext() ) {
				ItemMenu itemMenu = (ItemMenu) iteratorItemMenu.next();
				itemMenu.cargarListItemMenuHijos();
			}
		} else {
			this.listItemMenuHijos = new ArrayList<ItemMenu>();
		}
	}

	/**
	 * Obtiene la lista de items de menu hijos del item de menu 
	 * @return List<ItemMenu>
	 * @throws DemodaServiceException
	 */
	public List<ItemMenu> obtenerListItemMenuHijos() throws DemodaServiceException{
		String funcName = "obtenerListItemMenuHijos()";
		log.debug("iniciando..." + funcName);
		
		try {
			
			List<ItemMenu> listIMH = (ArrayList<ItemMenu>) SweDAOFactory.getItemMenuDAO().getListItemMenuHijos(this);
			
			log.debug("Finalizando " + funcName);
			return listIMH;
			
		} catch (Exception e) {
			log.error(funcName + " : " + e.getMessage());
			throw new DemodaServiceException(e);
		}
	}
	/**
	 * Obtiene la lista de Items de Menu hijos activos del Item de Menu
	 * @return List<ItemMenu>
	 * @throws DemodaServiceException
	 */
/*
	public List<ItemMenu> getListItemMenuHijosActivos() throws DemodaServiceException {
		String funcName = "getListItemMenuHijosActivos()";
		log.debug("iniciando..." + funcName);
		
		try {
			
			log.debug("Finalizando " + funcName);
			return SweDAOFactory.getItemMenuDAO().getListItemMenuHijosActivos(this);
			
		} catch (Exception e) {
			log.error(funcName + " : " + e.getMessage());
			throw new DemodaServiceException(e);
		}
	}
*/	
	
	public List<ItemMenu> getListItemMenuHijosByListIdAccionModulo(List<Long> listIdAccionModulo) 
		throws DemodaServiceException {
		
		String funcName = "getListItemMenuHijosByListIdAccionModulo(List<Long> listIdAccionModulo)";
		
		try {
			List<ItemMenu> listItemMenuHijosToReturn = new ArrayList<ItemMenu>(); 
			List<ItemMenu> listItemMenuHijosActivos = this.getListItemMenuHijos();
			
			for (ItemMenu itemMenu : listItemMenuHijosActivos) {
				
				// si tiene permiso el usuario lo agrego a la lista de menues hijo
				if ( itemMenu.isEnabled(listIdAccionModulo) ) {
					
					listItemMenuHijosToReturn.add(itemMenu);
				}
				
			}
			
			return listItemMenuHijosToReturn;
			
		} catch (Exception e) {
			log.error(funcName + " : " + e.getMessage());
			throw new DemodaServiceException(e);
		}
		
	}
	
	public boolean isEnabled ( List<Long> listIdAccionModulo ) 
		throws DemodaServiceException {
		
		String funcName = "isEnabled ( List<Long> listIdAccionModulo )";
		
		boolean isEnabled = false;
		
		try {
		
			if (!this.tieneHijos()) {
				
				if (this.isAccionModuloInList(listIdAccionModulo)) {
					isEnabled = true;
				}
				
			} else {
				
				List<ItemMenu> listItemMenuHijosActivos = this.listItemMenuHijos;
	
				Iterator iteratorItemMenu = listItemMenuHijosActivos.iterator();
				
				while ( iteratorItemMenu.hasNext() && !isEnabled ) {
					
					ItemMenu itemMenu = (ItemMenu) iteratorItemMenu.next();
					
					isEnabled = itemMenu.isEnabled(listIdAccionModulo);
				}
			}
			
			return isEnabled;
			
		} catch (Exception e) {
			log.error(funcName + " : " + e.getMessage());
			throw new DemodaServiceException(e);
		}

	}
	
	private boolean isAccionModuloInList (List<Long> listIdAccionModulo) {
		
		boolean isAccionModuloInList = false;
		
		if (listIdAccionModulo != null && this.accModApl != null) {
			
			for (Long idAccionModulo : listIdAccionModulo) {
				
				if (idAccionModulo.equals( this.accModApl.getId() )) {
					isAccionModuloInList = true;
				}
				
			}
			
		}
		
		return isAccionModuloInList;
		
	}
	
	private boolean isHoja () {
		boolean isHoja = false;
		
		if (accModApl != null ) {
			isHoja = true;
		}
		return isHoja;
	}
	
	public boolean tieneHijos () {
		boolean tieneHijos = false;
		
		if (this.listItemMenuHijos != null && this.listItemMenuHijos.size() > 0 ) {
			tieneHijos = true;
		}
		return tieneHijos;
	}
	
	public ItemMenu buscarItemById(Long idItemMenuHijoBusc){
		
		log.debug("idItemMenuHijoBusc: " + idItemMenuHijoBusc);
		ItemMenu itemMenuEncontrado = null;
		if(this.getId().longValue() == idItemMenuHijoBusc){
			itemMenuEncontrado = this;
		}
		
		Iterator iteratorListItemMenuHijo = this.listItemMenuHijos.iterator();
		
		while (iteratorListItemMenuHijo.hasNext() && itemMenuEncontrado == null ) {
			
			ItemMenu itemMenuHijo = (ItemMenu) iteratorListItemMenuHijo.next();
			itemMenuEncontrado = itemMenuHijo.buscarItemById(idItemMenuHijoBusc);
		}
		
		return itemMenuEncontrado;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}
	
	
	public void loadFromVOForCreate(ItemMenuVO itemMenuVO) throws DemodaServiceException{
		
		setDescripcion(StringUtil.escapar(itemMenuVO.getDescripcion()));
		setTitulo(StringUtil.escapar(itemMenuVO.getTitulo()));
		setUrl(StringUtil.escapar(itemMenuVO.getUrl()));
		setNroOrden(itemMenuVO.getNroOrden());
		
		// aplicacion
		Aplicacion aplicacion = Aplicacion.getById(itemMenuVO.getAplicacion().getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }
        setAplicacion(aplicacion);
        
        // item menu padre
        if (!ModelUtil.isNullOrEmpty(itemMenuVO.getItemMenuPadre())){
        	ItemMenu itemMenuPadre = ItemMenu.getById(itemMenuVO.getItemMenuPadre().getId());
            if (itemMenuPadre == null) {
            	throw new DemodaServiceException("No se encontro registro item Menu Padre en la Base de Datos.");
            }
            setItemMenuPadre(itemMenuPadre);
        }else{
        	setItemMenuPadre(null); // ojo
        }
	}

	public void loadFromVOForUpdate(ItemMenuVO itemMenuVO) throws DemodaServiceException{
		
		setDescripcion(StringUtil.escapar(itemMenuVO.getDescripcion()));
		setTitulo(StringUtil.escapar(itemMenuVO.getTitulo()));
		setUrl(StringUtil.escapar(itemMenuVO.getUrl()));
		setNroOrden(itemMenuVO.getNroOrden());
		
        // accion modulo
        if (!ModelUtil.isNullOrEmpty(itemMenuVO.getAccModApl())){
        	AccModApl accModApl = AccModApl.getById(itemMenuVO.getAccModApl().getId());
            if (accModApl == null) {
            	throw new DemodaServiceException("No se encontro registro Acc Mod Apl en la Base de Datos.");
            }
            setAccModApl(accModApl);
        }else{
        	setAccModApl(null); // ojo
        }
	}



	/**
	 * titulo requerido.
	 * unico dentro del padre: por restriccion en la bdd
	 * @return boolean
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();

		// validaciones de VO
		this.validate();


		// validaciones de negocio
		/*
		Integer maxNivelMenu = this.getAplicacion().getMaxNivelMenu(); 
		if (maxNivelMenu.intValue() != 0 ){			
			Integer nivel = this.calcularNivel();
			if (nivel.intValue() > maxNivelMenu.intValue()){
				addRecoverableError(SweCommonError.ITEM_MENU_NIVEL_MAYOR_MAX_NIVEL_APLIC);
			}
		}
		*/
		
		if (hasError()) {
			return false;
		}
				
		return true;
	}

	/**
	 * Requeridos: nombreUsuario y fechaAlta
	 * @return
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		this.validate();
		
		if (hasError()) {
			return false;
		}
		
		// validaciones de negocio
		
		return true;
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();

		if (SweHibernateUtil.hasReference(this, ItemMenu.class, "itemMenuPadre")) {
			addRecoverableError(SweCommonError.ITEM_MENU_ITEM_MENU_PADRE_HASREF);
		}
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private void validate () {
		//Validaciones de VO
		if (StringUtil.isNullOrEmpty(getTitulo())) {
			addRecoverableError(SweCommonError.ITEM_MENU_TITULO_REQUIRED);
		}
	}
	
	/**
	 * Calcula el nivel del Item de Menu.
	 * Si no tiene padre, (es de nivel root) retorna 1
	 * @return Integer nivel
	 */
	public Integer calcularNivel() {
		int nivel = 1;
		ItemMenu imp = this.getItemMenuPadre();
		while (imp != null) {
			imp = imp.getItemMenuPadre();
			nivel++;
		}
		return nivel + 1;
	}
}
