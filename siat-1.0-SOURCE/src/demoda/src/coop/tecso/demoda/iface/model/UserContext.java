//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;


public class UserContext extends Common {
	
	private static final long serialVersionUID = -7017840944558942495L;
	
	private static final long ID_AREA_COBRANZA_JUDICIAL = 11; // Para Obtener los usuarios que son Operadores Judiciales.
	
	private Long idUsuarioSwe = 0L;
	private String userName = "";
    private String longUserName = "";
	private String ipRequest = "";
	private Boolean permiteWeb = false;

    private String idsAccionesModuloUsuario = ""; // Concatenacion de "," + id de las accion permitada para el usuario + ",".
    private String codsRolUsuario = ""; // Lista separada por "," de los codigos de/los roles del usuario
    private String accionSWE = "";
    private String metodoSWE = "";

	private Long idUsuarioSiat;    
	private Long idInvestigador;
	private Long idInspector;
	private Long idSupervisor;
	private Long idProcurador;
	private Long idRNPA;
	
	private Long idAbogado;
	
	private Long idArea;
	private String desArea = "";

	private Long idCanal;
	private Long idOficina;
	private String desOficina = "";

	private Boolean isAnonimo = false; // no anonimo
	private String urlReComenzar = "";
	
	
	private String codRecurso= "";
	
	private Long idMandatario;
	
	// Constructor
    public UserContext() {
    }   
    
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getIpRequest() {
		return ipRequest;
	}
	
	public void setIpRequest(String ipRequest) {
		this.ipRequest = ipRequest;
	}

	public String getIdsAccionesModuloUsuario() {
		return this.idsAccionesModuloUsuario;
	}
	
	public void setIdsAccionesModuloUsuario(String idsAccionesModuloUsuario) {
		this.idsAccionesModuloUsuario = idsAccionesModuloUsuario;
	}

    public String getCodsRolUsuario() {
		return codsRolUsuario;
	}
    
    public void setCodsRolUsuario(String codsRolUsuario) {
		this.codsRolUsuario = codsRolUsuario;
	}

	public String getAccionSWE() {
		return accionSWE;
	}

	public void setAccionSWE(String accionActual) {
		this.accionSWE = accionActual;
	}

	public String getMetodoSWE() {
		return metodoSWE;
	}

	public void setMetodoSWE(String metodoActual) {
		this.metodoSWE = metodoActual;
	}

	public Long getIdInspector() {
		return idInspector;
	}

	public void setIdInspector(Long idInspector) {
		this.idInspector = idInspector;
	}

	public Long getIdInvestigador() {
		return idInvestigador;
	}

	public void setIdInvestigador(Long idInvestigador) {
		this.idInvestigador = idInvestigador;
	}

	public Long getIdProcurador() {
		return idProcurador;
	}

	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}

	public Long getIdRNPA() {
		return idRNPA;
	}

	public void setIdRNPA(Long idRNPA) {
		this.idRNPA = idRNPA;
	}

	public Long getIdUsuarioSiat() {
		return idUsuarioSiat;
	}

	public void setIdUsuarioSiat(Long idUsuarioSiat) {
		this.idUsuarioSiat = idUsuarioSiat;
	}

	public Long getIdArea() {
		return idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public String getDesArea() {
		return desArea;
	}

	public void setDesArea(String desArea) {
		this.desArea = desArea;
	}

	public Long getIdSupervisor() {
		return idSupervisor;
	}

	public void setIdSupervisor(Long idSupervisor) {
		this.idSupervisor = idSupervisor;
	}

	public Long getIdMandatario() {
		return idMandatario;
	}

	public void setIdMandatario(Long idMandatario) {
		this.idMandatario = idMandatario;
	}


	/**
	 * @return the longUserName
	 */
	public String getLongUserName() {
		return longUserName;
	}

	/**
	 * @param longUserName the longUserName to set
	 */
	public void setLongUserName(String longUserName) {
		this.longUserName = longUserName;
	}
	
	
	/**
	 * @return the anonimo
	 */
	public String getIsAnonimoView() {
		return isAnonimo ? "1" : "0";
	}
	
	/**
	 * @return the urlReComenzar
	 */
	public String getUrlReComenzar() {
		return urlReComenzar;
	}

	/**
	 * @param urlReComenzar the urlReComenzar to set
	 */
	public void setUrlReComenzar(String urlReComenzar) {
		this.urlReComenzar = urlReComenzar;
	}

	/**
	 * @return the isAnonimo
	 */
	public Boolean getIsAnonimo() {
		return isAnonimo;
	}

	/**
	 * @param isAnonimo the isAnonimo to set
	 */
	public void setIsAnonimo(Boolean isAnonimo) {
		this.isAnonimo = isAnonimo;
	}
	
	// Metodos para utilizar en el seteo de permiso de Liquidacion Deuda y en otros lugares mas adelante
	public Boolean getEsAnonimo(){
		return isAnonimo;
	}
	
	public Boolean getEsProcurador(){
		if (getIdProcurador() == null)
			return false;
		else
			return true;	
	}
	
	public Boolean getEsUsuarioCMD(){
		if (!getEsProcurador() && 
				!getEsAnonimo() && 
				!getEsEscribano() &&
				!getEsOperadorJudicial() &&
				!getEsUsuarioCyq() )
			return true;
		else
			return false;	
	}
	
	public Boolean getEsEscribano(){

		if (getCodsRolUsuario() != null && getCodsRolUsuario().toLowerCase().contains(",escribano,")){
			return true;
		} else {
			return false;
		}		
	}

	public Boolean getEsUsuarioCyq(){

		if (getCodsRolUsuario() != null && getCodsRolUsuario().toLowerCase().contains(",cyq,")){
			return true;
		} else {
			return false;
		}		
	}
	
	public Boolean getEsAdmin(){

		if (getCodsRolUsuario() != null && getCodsRolUsuario().toLowerCase().contains(",admin,")){
			return true;
		} else {
			return false;
		}		
	}
	
	public Boolean getEsInspector(){
		if (getIdInspector() == null)
			return false;
		else
			return true;
	}
	
	public Boolean getEsSupervisor(){
		if (getIdSupervisor() == null)
			return false;
		else
			return true;
	}
	
	/**
	 * Si el usuario posee el rol de "cjudicial", puede operar sobre deuda de distintos porcuradores.  
	 * 
	 * @author Cristian
	 * @return
	 */
	public Boolean getEsOperadorJudicial(){
		
		if (!getEsProcurador() && getIdArea() != null && getIdArea().longValue() == ID_AREA_COBRANZA_JUDICIAL){
			return true;
		} else {
			return false;
		}		
	}
	
	public Boolean getEsAbogado(){
		if (getIdAbogado() == null)
			return false;
		else
			return true;	
	}
	
	/**
	 *  Devuelve una cadena segun el tipo de usuario.
	 * 
	 * @author Cristian
	 * @return
	 */
	public String getStrTipoUsuario(){
		String strTipoUsuario = "";
		
		if (getEsProcurador())
			strTipoUsuario = "Procurador";
		else if (getEsAnonimo())
			strTipoUsuario = "Anomino";
		else if (getEsUsuarioCMD())
			strTipoUsuario = "Usuario CMD";
		else if (getEsEscribano())
			strTipoUsuario = "Escribano";
		else if (getEsAbogado())
			strTipoUsuario = "Abogado";
		else if (getEsOperadorJudicial())
			strTipoUsuario = "Operador Jud.";
		else if (getEsUsuarioCyq())
			strTipoUsuario = "Usr Cyq";
		
		return strTipoUsuario;		
	}

	public Long getIdUsuarioSwe() {
		return idUsuarioSwe;
	}

	public void setIdUsuarioSwe(Long idUsuarioSwe) {
		this.idUsuarioSwe = idUsuarioSwe;
	}

	public String getDesOficina() {
		return desOficina;
	}

	public void setDesOficina(String desOficina) {
		this.desOficina = desOficina;
	}

	public Long getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(Long idOficina) {
		this.idOficina = idOficina;
	}

	public Long getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(Long idCanal) {
		this.idCanal = idCanal;
	}

	public String getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(String codRecurso) {
		this.codRecurso = codRecurso;
	}

	public Boolean getPermiteWeb() {
		return permiteWeb;
	}

	public void setPermiteWeb(Boolean permiteWeb) {
		this.permiteWeb = permiteWeb;
	}

	public Long getIdAbogado() {
		return idAbogado;
	}
	public void setIdAbogado(Long idAbogado) {
		this.idAbogado = idAbogado;
	}
	
	
}
