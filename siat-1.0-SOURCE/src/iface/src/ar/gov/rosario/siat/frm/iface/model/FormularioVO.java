//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.iface.model.DesImpVO;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Value Object del Formulario
 * @author tecso
 *
 */
public class FormularioVO extends SiatBussImageModel {

	private static Logger log = Logger.getLogger(FormularioVO.class);
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "formularioVO";
	
	private String codFormulario;

	private String desFormulario;

	private List<ForCamVO> listForCam = new ArrayList<ForCamVO>();
	
	private FormatoSalida formatoSalida = FormatoSalida.OpcionSelecionar;

	private String xsl;

	private String xmlTest;
	
	private String xslTxt;
	
	private DesImpVO desImp = new DesImpVO();
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public FormularioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public FormularioVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesFormulario(desc);
	}
	
	// Getters y Setters

	public String getCodFormulario() {
		return codFormulario;
	}

	public void setCodFormulario(String codFormulario) {
		this.codFormulario = codFormulario;
	}

	public String getDesFormulario() {
		return desFormulario;
	}

	public void setDesFormulario(String desFormulario) {
		this.desFormulario = desFormulario;
	}

	public List<ForCamVO> getListForCam() {
		return listForCam;
	}

	public void setListForCam(List<ForCamVO> listForCam) {
		this.listForCam = listForCam;
	}

	public FormatoSalida getFormatoSalida() {
		return formatoSalida;
	}

	public void setFormatoSalida(FormatoSalida formatoSalida) {
		this.formatoSalida = formatoSalida;
	}

	public String getXmlTest() {
		return xmlTest;
	}

	public void setXmlTest(String xml_test) {
		this.xmlTest = xml_test;
	}

	public String getXslTxt() {
		return xslTxt;
	}

	public void setXslTxt(String xslTxt) {
		this.xslTxt = xslTxt;
	}

	public String getXsl() {
		return xsl;
	}

	public void setXsl(String xsl) {
		this.xsl = xsl;
	}

	public DesImpVO getDesImp() {
		return desImp;
	}

	public void setDesImp(DesImpVO desImp) {
		this.desImp = desImp;
	}

	public boolean validateCampos() {
		clearError();
		if (!ListUtil.isNullOrEmpty(getListForCam())) {
			for(ForCamVO forCam: listForCam){
				log.debug("validando campo:"+forCam.getCodForCam()+"    maxLargo:"+forCam.getLargoMaxView()+
						"        cadena ingresada:"+forCam.getValorDefecto()+
						"       largo:"+forCam.getValorDefecto().length());
				if(forCam.getLargoMax()!=null && 
					forCam.getLargoMax().intValue()<forCam.getValorDefecto().length())
						addRecoverableError(BaseError.FORMULARIO_LARGO_CAMPO_INVALIDO, 
								forCam.getCodForCam());
			}
		}
		
		if(hasError())
			return false;
		
		return true;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
