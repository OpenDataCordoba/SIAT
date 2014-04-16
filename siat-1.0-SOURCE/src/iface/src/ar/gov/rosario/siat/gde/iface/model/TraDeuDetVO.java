//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.ModelUtil;

/**
 * Value Object de la deuda traspasada de un procurador a otro
 * @author tecso
 *
 */
public class TraDeuDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "traDeuDetVO";
	
	private TraspasoDeudaVO traspasoDeuda = new TraspasoDeudaVO(); 
	private DeudaVO deuda = new DeudaVO();
	private Long idDeuda; 
	private ConstanciaDeuVO constanciaDeuOri = new ConstanciaDeuVO();
	
	private ConDeuDetVO conDeuDet = new ConDeuDetVO();
	
	// Buss Flags
	private Boolean verConstanciaBussEnabled = true;
	
	
	// View Constants
	
	
	// Constructores
	public TraDeuDetVO() {
		super();
	}

	public TraDeuDetVO(TraspasoDeudaVO traspasoDeudaVO, DeudaJudicialVO deudaJudicialVO) {
		this();
		this.traspasoDeuda = traspasoDeudaVO;
		this.deuda   = deudaJudicialVO;
		this.idDeuda = deudaJudicialVO.getId();
	}

	public TraDeuDetVO(TraspasoDeudaVO traspasoDeudaVO, DeudaJudicialVO deudaJudicialVO, ConstanciaDeuVO constanciaDeuOriVO) {
		this(traspasoDeudaVO, deudaJudicialVO );
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
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public TraspasoDeudaVO getTraspasoDeuda() {
		return traspasoDeuda;
	}
	public void setTraspasoDeuda(TraspasoDeudaVO traspasoDeuda) {
		this.traspasoDeuda = traspasoDeuda;
	}
	public ConDeuDetVO getConDeuDet() {
		return conDeuDet;
	}
	public void setConDeuDet(ConDeuDetVO conDeuDet) {
		this.conDeuDet = conDeuDet;
	}
	
	
	// View getters
	


	public String getConstanciaDeuOriView(){
		if (!ModelUtil.isNullOrEmpty(this.getConstanciaDeuOri())){
			return this.getConstanciaDeuOri().getNumeroBarraAnioConstanciaView();
		}else{
			return "";
		}
	}

	/**
	 * Obtiene el Id compuestro con idDeuda + idEstadoDeuda adecuado para la visualizacion.
	 * Separados por el caracter -
	 * @return String
	 */
	public String getIdForDetalleDeudaView(){
		String idForDetalleDeudaView = this.getIdDeuda() + "-" + 
		this.getDeuda().getEstadoDeuda().getId();
		
		return idForDetalleDeudaView;
	}
	
	// Buss flags getters y setters
	public Boolean getVerConstanciaBussEnabled() {
		return verConstanciaBussEnabled;
	}
	public void setVerConstanciaBussEnabled(Boolean verConstanciaBussEnabled) {
		this.verConstanciaBussEnabled = verConstanciaBussEnabled;
	}
	
	
	// View flags getters
	
	
	
}
