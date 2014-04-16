//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.ModelUtil;

/**
 * Value Object de la devolucion de deuda
 * @author tecso
 *
 */
public class DevDeuDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "devDeuDetVO";
	
	private DevolucionDeudaVO devolucionDeuda = new DevolucionDeudaVO(); 
	private DeudaVO deuda = new DeudaVO();
	private Long idDeuda; 
	private ConstanciaDeuVO constanciaDeuOri = new ConstanciaDeuVO(); 
	
	private ConDeuDetVO conDeuDet = new ConDeuDetVO();
	
	// Buss Flags
	private Boolean verConstanciaBussEnabled = true;
	
	// View Constants
	
	
	// Constructores
	public DevDeuDetVO() {
		super();
	}
	
	public DevDeuDetVO(DevolucionDeudaVO devolucionDeudaVO, DeudaVO deudaVO) {
		super();
		this.devolucionDeuda = devolucionDeudaVO;
		this.deuda   = deudaVO;
		this.idDeuda = deudaVO.getId();
	}

	public DevDeuDetVO(DevolucionDeudaVO devolucionDeudaVO, DeudaVO deudaVO, ConstanciaDeuVO constanciaDeuOriVO) {
		this(devolucionDeudaVO,deudaVO);
		this.constanciaDeuOri = constanciaDeuOriVO;
	}
	
	// Getters y Setters
	public ConstanciaDeuVO getConstanciaDeuOri() {
		return constanciaDeuOri;
	}
	public void setConstanciaDeuOri(ConstanciaDeuVO constanciaDeuOri) {
		this.constanciaDeuOri = constanciaDeuOri;
	}
	public DeudaVO getDeuda() {
		return deuda;
	}
	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}
	public DevolucionDeudaVO getDevolucionDeuda() {
		return devolucionDeuda;
	}
	public void setDevolucionDeuda(DevolucionDeudaVO devolucionDeuda) {
		this.devolucionDeuda = devolucionDeuda;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public ConDeuDetVO getConDeuDet() {
		return conDeuDet;
	}
	public void setConDeuDet(ConDeuDetVO conDeuDet) {
		this.conDeuDet = conDeuDet;
	}
	
	
	// View getters


	/**
	 * Obtiene el Id compuestro con idDeuda + idEstadoDeuda adecuado para la visualizacion.
	 * Separados por el caracter -
	 * @return String
	 */
	public String getIdForDetalleDeudaView(){
		String idForDetalleDeudaView = this.getIdDeuda() + "-" + this.getDeuda().getEstadoDeuda().getId();
		
		return idForDetalleDeudaView;
	}
	
	// Buss flags getters y setters

	public String getConstanciaDeuOriView(){
		if (!ModelUtil.isNullOrEmpty(this.getConstanciaDeuOri())){
			return this.getConstanciaDeuOri().getNumeroBarraAnioConstanciaView();
		}else{
			return "";
		}
	}

	public Boolean getVerConstanciaBussEnabled() {
		return verConstanciaBussEnabled;
	}
	public void setVerConstanciaBussEnabled(Boolean verConstanciaBussEnabled) {
		this.verConstanciaBussEnabled = verConstanciaBussEnabled;
	}

	
	
	// View flags getters
	
}
