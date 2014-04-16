//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuVO;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Representa la seleccion de exclusion de cuenta
 * @author tecso
 *
 */
@Entity
@Table(name = "pad_cueExcSel")
public class CueExcSel extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuenta") 
    private Cuenta cuenta;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idArea")
    private Area area;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idCueExcSel")
	@OrderBy(clause="idCueExcSel")
	private List<CueExcSelDeu> listCueExcSelDeu;
	
	@Column(name = "idCuenta", insertable=false, updatable=false)
	private Long idCuenta;

	// Constructores
	
	public CueExcSel() {
		super();
	}

	//Getters y Setters
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public List<CueExcSelDeu> getListCueExcSelDeu() {
		return listCueExcSelDeu;
	}
	public void setListCueExcSelDeu(List<CueExcSelDeu> listCueExcSel) {
		this.listCueExcSelDeu = listCueExcSel;
	}

	// Metodos de clase	
	public static CueExcSel getById(Long id) {
		return (CueExcSel) PadDAOFactory.getCueExcSelDAO().getById(id);
	}
	
	public static CueExcSel getByIdNull(Long id) {
		return (CueExcSel) PadDAOFactory.getCueExcSelDAO().getByIdNull(id);
	}
	
	public static List<CueExcSel> getList() {
		return (ArrayList<CueExcSel>) PadDAOFactory.getCueExcSelDAO().getList();
	}
	
	public static List<CueExcSel> getListActivos() {			
		return (ArrayList<CueExcSel>) PadDAOFactory.getCueExcSelDAO().getListActiva();
	}
	
	/**
	  * Busca una Cuenta Excluida por Numero Cuenta, por Recurso y codigo de Area, 
	  * si no encuentra la conbinacion devuelve null.  
	  * 
	  * @param numeroCuenta
	  * @param idRecurso
	  * @param codArea
	  * @return 
	  * @throws Exception
	 */ 
	public static CueExcSel getCueExcSelByCuentaYRecursoYArea(String numeroCuenta,Long idRecurso, String codArea) throws Exception{
		return (CueExcSel) PadDAOFactory.getCueExcSelDAO().getCueExcSelByCuentaYRecursoYArea(numeroCuenta,idRecurso,codArea);
	}
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio

		//Validacion de si la cuenta fue exluida previamente.
		if (CueExcSel.getCueExcSelByCuentaYRecursoYArea(getCuenta().getNumeroCuenta(), 
				getCuenta().getRecurso().getId(), getArea().getCodArea()) != null)
				addRecoverableError(PadError.CUEEXCSEL_CUENTA_EXCLUIDA);
		
		if (hasError()) {
			return false;
		}
		
		return !hasError();
	}
	

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de Requeridos
		if (getCuenta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					PadError.CUEEXCSEL_CUENTA_LABEL);
		}

		if (getArea() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					PadError.CUEEXCSEL_AREA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	/**
	 * Obtiene la lista de CueExcSelDeu activas de la cueExcSel
	 * @return List<CueExcSelDeu>
	 */
	public List<CueExcSelDeu> getListCueExcSelDeuActivas(){
		
		return PadDAOFactory.getCueExcSelDeuDAO().getListCueExcSelDeuActivas(this);
	}

	// Metodos de negocio
	/**
	 * Activa el CueExcSel. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getCueExcSelDAO().update(this);
	}

	/**
	 * Desactiva el CueExcSel. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getCueExcSelDAO().update(this);
	}

	/**
	 * Valida la activacion del CueExcSel
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del CueExcSel
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}
	
	//	 Administracion de CueExeSelDeu
	public CueExcSelDeu createCueExcSelDeu(CueExcSelDeu cueExcSelDeu) throws Exception {		
		// Validaciones de negocio
		if (!cueExcSelDeu.validateCreate()) {
			return cueExcSelDeu;
		}

		PadDAOFactory.getCueExcSelDeuDAO().update(cueExcSelDeu);	
		
		return cueExcSelDeu;
	}
	
	//	Fin Administracion de CueExeSelDeu
	
	/**
	 * Especializacion del metodo toVO() para CueExcSel.
	 * 
	 * @return CueExcSelVO
	 * @throws Exception
	 */
	public CueExcSelVO toVOforView() throws Exception {
		CueExcSelVO cueExcSelVO = new CueExcSelVO();
		
		cueExcSelVO = (CueExcSelVO)this.toVO(3,false);
		
		cueExcSelVO.setListCueExcSelDeu(new ArrayList<CueExcSelDeuVO>());
		
		if (this.getListCueExcSelDeu() != null) {
			for (CueExcSelDeu item: this.getListCueExcSelDeu())  
				cueExcSelVO.getListCueExcSelDeu().add((CueExcSelDeuVO) item.toVOforView());
		}
		
		return cueExcSelVO;
	}

	public Long getIdCuenta() {
		return idCuenta;
	}
	
}
