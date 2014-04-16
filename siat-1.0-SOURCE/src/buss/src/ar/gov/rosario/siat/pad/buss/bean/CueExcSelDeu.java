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
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuVO;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Representa la seleccion de exclusion de deuda de cuenta 
 * @author tecso
 *
 */
@Entity
@Table(name = "pad_cueExcSelDeu")
public class CueExcSelDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCueExcSel") 
    private CueExcSel cueExcSel;
	
	@Column(name = "idDeuda")
    private Long idDeuda;
	
	@Column(name = "idCaso")
    private String idCaso;
	
	@Column(name = "observacion")
	private String observacion; // VARCHAR(255) 
		
	
	// Constructores
	
	public CueExcSelDeu() {
		super();
	}

	//Getters y Setters
	public CueExcSel getCueExcSel() {
		return cueExcSel;
	}
	public void setCueExcSel(CueExcSel cueExcSel) {
		this.cueExcSel = cueExcSel;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	// Metodos de clase	
	public static CueExcSelDeu getById(Long id) {
		return (CueExcSelDeu) PadDAOFactory.getCueExcSelDeuDAO().getById(id);
	}
	
	public static CueExcSelDeu getByIdNull(Long id) {
		return (CueExcSelDeu) PadDAOFactory.getCueExcSelDeuDAO().getByIdNull(id);
	}
	
	public static List<CueExcSelDeu> getList() {
		return (ArrayList<CueExcSelDeu>) PadDAOFactory.getCueExcSelDeuDAO().getList();
	}
	
	public static List<CueExcSelDeu> getListActivos() {			
		return (ArrayList<CueExcSelDeu>) PadDAOFactory.getCueExcSelDeuDAO().getListActiva();
	}

	public Deuda getDeudaAdmin() throws Exception {
		return DeudaAdmin.getById(this.getIdDeuda());
	}
	
	/**
	  * Busca una Deuda de una Cuenta Excluida, 
	  * si no encuentra la conbinacion devuelve null.  
	  * 
	  * @param idCueExcSel
	  * @param idDeuda
	  * @return 
	  * @throws Exception
	 */ 
	public static CueExcSelDeu getCueExcSelDeuByCueExcSelYDeuda(Long idCueExcSel, Long idDeuda) throws Exception{
		return (CueExcSelDeu) PadDAOFactory.getCueExcSelDeuDAO().getCueExcSelDeuByCueExcSelYDeuda(idCueExcSel,idDeuda);
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
				
		//Validacion de si la deuda fue exluida de la cuenta previamente.
		if (CueExcSelDeu.getCueExcSelDeuByCueExcSelYDeuda(getCueExcSel().getId(), getIdDeuda()) != null)
				addRecoverableError(PadError.CUEEXCSELDEU_DEUDA_EXCLUIDA_DE_CUENTA);

		
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la activacion del CueExcSelDeu
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
	 * Valida la desactivacion del CueExcDeu
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
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
	
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	// Metodos de negocio
	/**
	 * Activa el CueExcSelDeu. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getCueExcSelDeuDAO().update(this);
	}

	/**
	 * Desactiva el CueExcSelDeu. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getCueExcSelDeuDAO().update(this);
	}

	/**
	 * Especializacion del metodo toVO() para CueExcSelDeu.
	 * 
	 * @return CueExcSelDeuVO
	 * @throws Exception
	 */
	public CueExcSelDeuVO toVOforView() throws Exception {
		CueExcSelDeuVO cueExcSelDeuVO = new CueExcSelDeuVO();
		
		cueExcSelDeuVO = (CueExcSelDeuVO)this.toVO(2,false);
		cueExcSelDeuVO.setCueExcSel((CueExcSelVO) this.getCueExcSel().toVO(3, false));
		
		cueExcSelDeuVO.setDeuda((DeudaVO) getDeudaAdmin().toVO());
		
		return cueExcSelDeuVO;
	}

	@Override
	public String infoString() {
		String ret="Seleccion de exclusion de deuda de cuenta ";
		
		
		if(cueExcSel!=null){
			if(cueExcSel.getCuenta()!=null){
				ret+=" - Cuenta: "+cueExcSel.getCuenta().getNumeroCuenta();
			}
		}

		try {
			Deuda deudaAdmin = getDeudaAdmin();
			if(deudaAdmin!=null){
				ret+=" - Deuda Admin: "+deudaAdmin.getPeriodo()+"/"+deudaAdmin.getAnio() + "   fecha Vto.:"+deudaAdmin.getFechaVencimiento();
			}
		} catch (Exception e) {}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		
		return ret;
	}
}
