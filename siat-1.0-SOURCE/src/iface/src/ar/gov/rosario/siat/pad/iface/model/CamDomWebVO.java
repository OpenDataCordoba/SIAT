//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a CamDomWeb
 * 
 * @author tecso
 */
public class CamDomWebVO extends BaseBO {

	private static final long serialVersionUID = 1L;

	private CuentaVO cuenta = new CuentaVO();
	private DomicilioVO domVie = new DomicilioVO();
	private DomicilioVO domNue = new DomicilioVO();
	// en el bean se guardo solo el cod_doc, abrev_doc y numDoc
	private DocumentoVO documento = new DocumentoVO();	
	private String nomSolicitante;
	private String apeSolicitante;
	private Integer nroTramite;
	private String mail;
	private SiNo esOrigenWeb = SiNo.SI;

	// Constructores
	public CamDomWebVO(){
		super();
	}

	// Getters y setters
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public DomicilioVO getDomVie() {
		return domVie;
	}
	public void setDomVie(DomicilioVO domVie) {
		this.domVie = domVie;
	}
	public DomicilioVO getDomNue() {
		return domNue;
	}
	public void setDomNue(DomicilioVO domNue) {
		this.domNue = domNue;
	}
	public DocumentoVO getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoVO documento) {
		this.documento = documento;
	}
	public String getNomSolicitante() {
		return nomSolicitante;
	}
	public void setNomSolicitante(String nomSolicitante) {
		this.nomSolicitante = nomSolicitante;
	}
	public String getApeSolicitante() {
		return apeSolicitante;
	}
	public void setApeSolicitante(String apeSolicitante) {
		this.apeSolicitante = apeSolicitante;
	}
	public Integer getNroTramite() {
		return nroTramite;
	}
	public void setNroTramite(Integer nroTramite) {
		this.nroTramite = nroTramite;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public SiNo getEsOrigenWeb() {
		return esOrigenWeb;
	}

	public void setEsOrigenWeb(SiNo esOrigenWeb) {
		this.esOrigenWeb = esOrigenWeb;
	}

}
