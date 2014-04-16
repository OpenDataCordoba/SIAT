//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Datum;

/**
 * @author Tecso Coop. Ltda.
 *  
 */
@Entity
@Table(name = "pad_domicilio")
public class Domicilio extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Transient
	private Logger log = Logger.getLogger(Domicilio.class);
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idTipDom") 
	private TipoDomicilio tipoDomicilio;
	
	@Column(name = "numero")
	private Long   	  numero;
	
	@Column(name = "bis")
    private Integer   bis;
	
	@Column(name = "letracalle")
    private String 	  letraCalle;
	
	@Column(name = "piso")
    private String   piso;
	
	@Column(name = "depto")
    private String   depto;
	
	@Column(name = "monoblock")
    private String    monoblock;
    
	@Column(name = "numeroHasta")
	private Long   	  numeroHasta;
	
	@Column(name = "letranumero")
	private String letraNumero;
	
	@Column(name = "letranumeroHasta")
	private String letraNumeroHasta;
	
	@Column(name = "letracalleHasta")
    private String 	  letraCalleHasta;	

	@Column(name = "refGeografica")
    private String    refGeografica;
    
	@Column(name = "esvalidado")
    private Integer 	  esValidado;
	
	@Column(name = "observacion")
	private String observacion;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name="codCalle", column = @Column(name="codCalle") ),
		@AttributeOverride(name="nombreCalle", column = @Column(name="nomCalle") ),
        @AttributeOverride(name="estado", column = @Column(name="estado", insertable=false, updatable=false) ),
        @AttributeOverride(name="fechaUltMdf", column = @Column(name="fechaUltMdf", insertable=false, updatable=false) ),
        @AttributeOverride(name="usuarioUltMdf", column = @Column(name="usuario", insertable=false, updatable=false) ) 
	} )
	private Calle calle;
    
    // COORDENADAS
	@Transient
	private Double coordenadaX;
	@Transient
	private Double coordenadaY;
	
	@Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="codPostal", column = @Column(name="codPostal") ),
            @AttributeOverride(name="codSubPostal", column = @Column(name="codSubpostal") ),
            @AttributeOverride(name="estado", column = @Column(name="estado", insertable=false, updatable=false) ),
            @AttributeOverride(name="fechaUltMdf", column = @Column(name="fechaUltMdf", insertable=false, updatable=false) ),
            @AttributeOverride(name="usuarioUltMdf", column = @Column(name="usuario", insertable=false, updatable=false) ) 
            
    } )
    private Localidad localidad;
    
	@Column(name = "codPostalFueraRosario")
    private String codPostalFueraRosario;
    
    /*idtipdom    
    cod_calle ???
    nomcalle ???
    *numero
    *bis
    *letracalle
    *piso
    *depto
    *monoblock
    
    localidad ????
    codpostal ????
    codsubpostal ????
    
    *referenciageografica
    
    numerohasta ???
    esvalidado ??? */

    
   
	/**
     * Constructor
     */
    public Domicilio() {
    }

	// Metodos de clase
	public static Domicilio getById(Long id) {
		return (Domicilio) PadDAOFactory.getDomicilioDAO().getById(id);
	}
	
	public static Domicilio getByIdWhitLocalidad(Long id) throws Exception{
		Domicilio domicilio = (Domicilio) PadDAOFactory.getDomicilioDAO().getById(id);
		Localidad localidad = Localidad.getByCodPostSubPost(domicilio.getLocalidad().getCodPostal(), 
			domicilio.getLocalidad().getCodSubPostal());
		domicilio.setLocalidad(localidad);

		return domicilio;
	}
	
	public static Domicilio getByIdNull(Long id) {
		return (Domicilio) PadDAOFactory.getDomicilioDAO().getByIdNull(id);
	}
    
    // Getters y Setter
    public TipoDomicilio getTipoDomicilio() {
		return tipoDomicilio;
	}
	public void setTipoDomicilio(TipoDomicilio tipoDomicilio) {
		this.tipoDomicilio = tipoDomicilio;
	}

	public Integer getEsValidado() {
		return esValidado;
	}

	public void setEsValidado(Integer esValidado) {
		this.esValidado = esValidado;
	}

	public String getLetraCalleHasta() {
		return letraCalleHasta;
	}

	public void setLetraCalleHasta(String letraCalleHasta) {
		this.letraCalleHasta = letraCalleHasta;
	}

	public String getLetraNumero() {
		return letraNumero;
	}

	public void setLetraNumero(String letraNumero) {
		this.letraNumero = letraNumero;
	}
	
	public String getLetraNumeroHasta() {
		return letraNumeroHasta;
	}

	public void setLetraNumeroHasta(String letraNumeroHasta) {
		this.letraNumeroHasta = letraNumeroHasta;
	}

	public Long getNumeroHasta() {
		return numeroHasta;
	}

	public void setNumeroHasta(Long numeroHasta) {
		this.numeroHasta = numeroHasta;
	}

	public Calle getCalle() {
		return calle;
	}

	public void setCalle(Calle calle) {
		this.calle = calle;
	}

	public Double getCoordenadaX() {
		return coordenadaX;
	}

	public void setCoordenadaX(Double coordenadaX) {
		this.coordenadaX = coordenadaX;
	}

	public Double getCoordenadaY() {
		return coordenadaY;
	}

	public void setCoordenadaY(Double coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

	public String getDepto() {
		return depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}

	public String getLetraCalle() {
		return letraCalle;
	}

	public void setLetraCalle(String letraCalle) {
		this.letraCalle = letraCalle;
	}

	
	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public String getMonoblock() {
		return monoblock;
	}

	public void setMonoblock(String monoblock) {
		this.monoblock = monoblock;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getRefGeografica() {
		return refGeografica;
	}

	public void setRefGeografica(String refGeografica) {
		this.refGeografica = refGeografica;
	}
		
	public Integer getBis() {
		return bis;
	}
	public void setBis(Integer bis) {
		this.bis = bis;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getCodPostalFueraRosario() {
		return codPostalFueraRosario;
	}

	public void setCodPostalFueraRosario(String codPostalFueraRosario) {
		this.codPostalFueraRosario = codPostalFueraRosario;
	}

	public boolean equals(Domicilio domicilio){
		boolean igual = false;
		
		if (domicilio != null){
			igual = this.getRepresent().equalsIgnoreCase(domicilio.getRepresent());
			log.debug("domicilio.getRepresent = " + domicilio.getRepresent());
		}
		
		log.debug("igual: " + igual);
		return igual;
	}
	
	public String getRepresent(){
		String represent = "";
		//cod calle
		if (this.getCalle() != null && this.getCalle().getId() != null){
			represent += this.getCalle().getId();
		}
		
		//depto
		if (this.getDepto() != null && this.getDepto().trim() != ""){
			represent += this.getDepto();
		}
		
		//letraCalle
		if (this.getLetraCalle() != null && this.getLetraCalle().trim() != ""){
			represent += this.getLetraCalle().trim().toUpperCase();
		}
		
		//letraNumero
		if (this.getLetraNumero() != null && this.getLetraNumero().trim() != ""){
			represent += this.getLetraNumero().trim().toUpperCase();
		}
		
		//localidad
//		if (this.getLocalidad() != null){
//		igual &= domicilio.getLocalidad() != null && domicilio.getLocalidad().trim().equals(this.getLocalidad().trim());
//		}else{
//		igual &= (domicilio.getLocalidad() == null); 
//		}
		
		//monoblock
		if (this.getMonoblock() != null && this.getMonoblock().trim() != ""){
			represent += this.getMonoblock().trim().toUpperCase(); 
		}
		
		//nombreCalle
		if (this.getNombreCalle() != null && this.getNombreCalle().trim() != "" ){
			represent += this.getNombreCalle().trim().toUpperCase();
		}
		
		//numero
		if (this.getNumero() != null){
			represent += this.getNumero();
		}
		
		//piso
		if (!StringUtil.isNullOrEmpty(this.getPiso())){
			represent += this.getPiso();
		}
		
		//refGeografica
		if (this.getRefGeografica() != null ){
			represent += this.getRefGeografica().trim().toUpperCase();
		}
		return represent;
		
	}
	
	public String getViewDomicilio(){
		String domicilio = "";
        
        if (this.getCodPostalFueraRosario() != null) {
            domicilio += this.getCodPostalFueraRosario().trim() + "  ";
        }		
		
        if (this.getNombreCalle() != null) {
            domicilio += this.getNombreCalle().trim();
        }
		
		if (this.getLetraCalle() != null && this.getLetraCalle().trim() != ""){
			domicilio += " " + this.getLetraCalle();
		}

		if (this.getNumero() != null){
			domicilio += " " + this.getNumero().toString();
			if (this.getLetraNumero() != null){
				domicilio += " " + this.getLetraNumero();
			}
		}

		if (this.getBis() != null && this.getBis().intValue() != 0) {
			domicilio += " Bis";
		}

		if (this.getMonoblock() != null && !this.getMonoblock().trim().equals("")){
			domicilio += " MB: " + this.getMonoblock();
		}

		if (this.getPiso() != null && !this.getPiso().trim().equals("")) {
			domicilio += " P:" + this.getPiso().toString();
		}
		
		if (this.getDepto() != null && !this.getDepto().trim().equals("")){
			domicilio += " D:" + this.getDepto().toString();
		}

		return domicilio;
	}
	
	public String getNombreCalle(){

		if (this.getCalle() != null )
			return this.getCalle().getNombreCalle();
		else
			return "";
		
	}
	
	public String getViewConLocalidad(){
		if (this.getLocalidad() != null){
			return this.getViewDomicilio() + " " + this.getLocalidad().getDescripcionPostal();
		}else{
			return this.getViewDomicilio();
		}
	}

	
	/**
	 * Realiza la carga de datos a traves del facade.
	 * Carga la localidad y la calle. Setea el campo EsValidado si puede validar la calle y la localidad.
	 * @throws Exception
	 */
    public boolean loadDataFromMCR() throws Exception{
    	Localidad localidadEncontrada = null;
    	boolean loadOK = true;

    	if (this.getLocalidad() == null){
    		log.error("Domicilio sin localidad");
    		return false;
    	}
    	// busca la localidad por codigo postal y codigo sub postal
    	localidadEncontrada = Localidad.getByCodPostSubPost(this.getLocalidad().getCodPostal(),this.getLocalidad().getCodSubPostal());
    	if (localidadEncontrada != null){
    		// solamente si la encuentra la carga
    		this.setLocalidad(localidadEncontrada);
    	}else{
    		// si no la encuentra no la carga. Solo logea el error
    		log.error("Localidad no encontrada con codPostal: " + this.getLocalidad().getCodPostal() + " codSubPostal: " + this.getLocalidad().getCodSubPostal() );
    		loadOK = false;
    	}

    	if(this.getCalle() != null){
    		if (this.getLocalidad().isRosario()){
    			Calle calleEncontrada = Calle.getByIdNull(this.getCalle().getCodCalle());
    			if(calleEncontrada != null){
    				// solamente si la encuentra la carga
    				this.setCalle(calleEncontrada);
    			}else{
    	    		// si no la encuentra no la carga. Solo logea el error    				
    				loadOK = false;
    				log.error("Calle no encontrada con id: " + this.getCalle().getCodCalle());
    			}
    		}else{
    			loadOK = false;
    		}
    	}else{
    		loadOK = false;
    	}
    	
    	return loadOK;
    }

    /**<p>
     * Devuelve un objecto Domicilio con el resultado de parsear el String pasado como parametro. El parametro debe
     * seguir el siguiente formato:  
     * </p><p><i>
     * "codCalle$nomCalle$numero$letra$monoblock$piso$depto$codPostal$codSubPostal$descripcionGeografica"
     * </i>
     * </p>
     * <p>
     * En el domicilio devuelto se setea como Tipo de Domicilio, 'Domicilio de Envio'. (Adecuado para el Atributo Domicilio de Envio) 
     * </p>
     * @param domicilioToParse - El string con el domicilio que se parseara.
     * @return Un objeto de tipo Domicilio con el resultado de parsear el string pasado.
     * @throws Exception
     */
    public static Domicilio valueOf(String domicilioToParse) throws Exception{
    	Domicilio domicilio = new Domicilio();
    	Calle calle = new Calle();
    	Localidad localidad = new Localidad();
    	TipoDomicilio tipoDomicilio;
    	Datum domicilioParser;
    	Long idTipoDomicilio = 1L;
    	
    	domicilioParser = Datum.parseAtChar(domicilioToParse, '$');
    	
    	// TODO Ver si seteamos Domicilio de Envio como Tipo, o ninguno.
    	tipoDomicilio = TipoDomicilio.getById(idTipoDomicilio);
    	if(tipoDomicilio == null){
    		throw new Exception("No existe el Tipo de Domicilio: 'Domicilio de Envio', en la DB.");
    	}
    	
    	Long codCalle = null;
    	Long numero = null;
    	String piso = null;
    	String depto = null;
    	if(!StringUtil.isNullOrEmpty(domicilioParser.getCols(0)))
    		codCalle = domicilioParser.getLong(0);
    	if(!StringUtil.isNullOrEmpty(domicilioParser.getCols(2)))
    		numero = domicilioParser.getLong(2);
    	if(!StringUtil.isNullOrEmpty(domicilioParser.getCols(5)))
    		piso = domicilioParser.getCols(5).trim();
    	if(!StringUtil.isNullOrEmpty(domicilioParser.getCols(6)))
    		depto = domicilioParser.getCols(6).trim();    	
    	domicilio.setTipoDomicilio(tipoDomicilio);
    	if(codCalle != null)
    		calle.setCodCalle(domicilioParser.getLong(0));    		
    	calle.setNombreCalle(domicilioParser.getCols(1));
    	domicilio.setCalle(calle);
    	if(numero != null){
    		domicilio.setNumero(Long.signum(domicilioParser.getLong(2))*domicilioParser.getLong(2));
    		if(domicilioParser.getLong(2)<0){
    			domicilio.setBis(1);
    		}else{
    			domicilio.setBis(0);
    		}    		
    	}
    	domicilio.setLetraCalle(domicilioParser.getCols(3));
    	domicilio.setMonoblock(domicilioParser.getCols(4));
    	if(piso != null)
    		domicilio.setPiso(domicilioParser.getCols(5).trim());    		
    	if(depto != null)
    		domicilio.setDepto(domicilioParser.getCols(6).trim());

    	String codPostal = domicilioParser.getString(7);
    	if (!StringUtil.isNullOrEmpty(codPostal)) {
    		localidad.setCodPostal(Long.valueOf(codPostal));
    	}
    	
    	String codSubPostal = domicilioParser.getString(8);
    	if (!StringUtil.isNullOrEmpty(codSubPostal)) {
    		localidad.setCodSubPostal(Long.valueOf(codSubPostal));
    	}
    	
    	domicilio.setLocalidad(localidad);
    	domicilio.setRefGeografica(domicilioParser.getString(9));
    	if(domicilio.loadDataFromMCR()){
    		domicilio.setEsValidado(1);
    	} else{
    		domicilio.setEsValidado(0);
    	}
      	
    	return domicilio;
    }
    
    /**
     * Devuelve un String que contiene los datos del domicilio en el siguiente formato:
     * <p><i>
     * "codCalle$nomCalle$numero$letra$monoblock$piso$depto$codPostal$codSubPostal$descripcionGeografica"
     * </i></p>
     * 
     * @return Devuelte un String formateado con datos del domicilio
     * @throws Exception
     */
    public String toStringAtr() throws Exception{
    	StringBuffer domicilioString = new StringBuffer();
    	
    	if(getCalle()==null || getLocalidad() ==null){
    		throw new Exception("El domicilio no tiene Localidad o Calle cargada.");
    	}
    	
    	domicilioString.append(getCalle().getCodCalle());
    	domicilioString.append("$");
    	domicilioString.append(getCalle().getNombreCalle());
    	domicilioString.append("$");
    	domicilioString.append(getNumero());
    	domicilioString.append("$");
    	domicilioString.append(getLetraCalle());
    	domicilioString.append("$");
    	domicilioString.append(getMonoblock());
    	domicilioString.append("$");
    	domicilioString.append(getPiso());
    	domicilioString.append("$");
    	domicilioString.append(getDepto());
    	domicilioString.append("$");
    	domicilioString.append(getLocalidad().getCodPostal());
    	domicilioString.append("$");
    	domicilioString.append(getLocalidad().getCodSubPostal());
    	domicilioString.append("$");
    	domicilioString.append(getRefGeografica());
    	
    	return domicilioString.toString();
    }
    
    /**
     * <p>
     * Valida si el String del parametro respeta el formato establecido para parsear un domicilio.
     * </p><p>
     * Formato Valido:</p>
     * <p><i>
     * "codCalle$nomCalle$numero$letra$monoblock$piso$depto$codPostal$codSubPostal$descripcionGeografica"
     * </i></p> 
     * 
     * @param domicilioToParse
     * @return True si es valido, False en caso contrario.
     */
    public static boolean isValidToParse(String domicilioToParse){
    	
    	Datum domicilioParser = Datum.parseAtChar(domicilioToParse, '$');
    	
    	if(domicilioParser.getColNumMax()!=10)
    		return false;
    	if (!StringUtil.isNullOrEmpty(domicilioParser.getCols(0)) && !StringUtil.isLong(domicilioParser.getCols(0))){
    		return false;			   
		}	
    	if (!StringUtil.isNullOrEmpty(domicilioParser.getCols(2)) && !StringUtil.isLong(domicilioParser.getCols(2))){
		   return false;			   
		}	
    	/*if (!StringUtil.isNullOrEmpty(domicilioParser.getCols(5)) && !StringUtil.isInteger(domicilioParser.getCols(5))){
		   return false;			   
		}	
    	if (!StringUtil.isNullOrEmpty(domicilioParser.getCols(6)) && !StringUtil.isInteger(domicilioParser.getCols(6))){
		   return false;			   
		}*/	
    	if (!StringUtil.isNullOrEmpty(domicilioParser.getCols(7)) && !StringUtil.isLong(domicilioParser.getCols(7))){
		   return false;			   
		}	
    	if (!StringUtil.isNullOrEmpty(domicilioParser.getCols(8)) && !StringUtil.isLong(domicilioParser.getCols(8))){
		   return false;			   
		}	

    	return true;
    }
    
    /**
     * Devuelve un valor Integer valido para setear en Piso o Depto convirtiendo desde un String.
     * <i>(Por ejemplo: el valor string podria ser A, B, C, D,... o PA, PB)</i> 
     * <p>
     * Por ejemplo se convierte (a,A)->1; (b,B)->2; .... ; (k,K)->11 y (pb,PB)->1; (pa,PA)->2; (ep,EP)->2
     * </p>
     * 
     * @param valor - String a convertir
     * @return Integer
     */
    public Integer convertPisoYDeptoFromString(String valor){
    	Integer valorValido = null;
    	
    	// Si el String es un Integer devuelve el valor que se obtiene del mismo.
    	if(StringUtil.isInteger(valor)){
    		return  Integer.valueOf(valor);
    	}
    	
    	// Si el String es del tipo "0A", "0B", etc. Se quita el cero inicial y luego se convierte.
    	if(valor.length()==2){
    		if(valor.charAt(0)=='0'){
    			valor = String.valueOf(valor.charAt(1));
    		}
    	}
    	
    	// Si el String es una letra.
    	if(valor.toUpperCase().equals("A"))
    		valorValido =  1;
    	if(valor.toUpperCase().equals("B"))
    		valorValido =  2;
    	if(valor.toUpperCase().equals("C"))
    		valorValido =  3;
    	if(valor.toUpperCase().equals("D"))
    		valorValido =  4;
    	if(valor.toUpperCase().equals("E"))
    		valorValido =  5;
    	if(valor.toUpperCase().equals("F"))
    		valorValido =  6;
    	if(valor.toUpperCase().equals("G"))
    		valorValido =  7;
    	if(valor.toUpperCase().equals("H"))
    		valorValido =  8;
    	if(valor.toUpperCase().equals("I"))
    		valorValido =  9;
    	if(valor.toUpperCase().equals("J"))
    		valorValido =  10;
    	if(valor.toUpperCase().equals("K"))
    		valorValido =  11;
    	if(valor.toUpperCase().equals("L"))
    		valorValido =  12;
    	if(valor.toUpperCase().equals("M"))
    		valorValido =  13;
    	if(valor.toUpperCase().equals("N"))
    		valorValido =  14;
    	if(valor.toUpperCase().equals("Ñ"))
    		valorValido =  15;
    	if(valor.toUpperCase().equals("O"))
    		valorValido =  16;
    	if(valor.toUpperCase().equals("P"))
    		valorValido =  17;
    	if(valor.toUpperCase().equals("Q"))
    		valorValido =  18;
    	if(valor.toUpperCase().equals("R"))
    		valorValido =  19;
    	if(valor.toUpperCase().equals("S"))
    		valorValido =  20;
    	if(valor.toUpperCase().equals("T"))
    		valorValido =  21;
    	if(valor.toUpperCase().equals("U"))
    		valorValido =  22;
    	if(valor.toUpperCase().equals("V"))
    		valorValido =  23;
    	if(valor.toUpperCase().equals("W"))
    		valorValido =  24;
    	if(valor.toUpperCase().equals("X"))
    		valorValido =  25;
    	if(valor.toUpperCase().equals("Y"))
    		valorValido =  26;
    	if(valor.toUpperCase().equals("Z"))
    		valorValido =  27;
    	
    	// Si el String es un numero Romano.
    	if(valor.toUpperCase().equals("I"))
    		valorValido =  1;
    	if(valor.toUpperCase().equals("II"))
    		valorValido =  2;
    	if(valor.toUpperCase().equals("III"))
    		valorValido =  3;
    	if(valor.toUpperCase().equals("IV"))
    		valorValido =  4;
    	if(valor.toUpperCase().equals("V"))
    		valorValido =  5;
    	if(valor.toUpperCase().equals("VI"))
    		valorValido =  6;
    	if(valor.toUpperCase().equals("VII"))
    		valorValido =  7;
    	if(valor.toUpperCase().equals("VIII"))
    		valorValido =  8;
    	if(valor.toUpperCase().equals("IX"))
    		valorValido =  9;
    	if(valor.toUpperCase().equals("X"))
    		valorValido =  10;
    	if(valor.toUpperCase().equals("XII"))
    		valorValido =  11;
    	if(valor.toUpperCase().equals("XII"))
    		valorValido =  12;
    	if(valor.toUpperCase().equals("XIII"))
    		valorValido =  13;
    	if(valor.toUpperCase().equals("XIV"))
    		valorValido =  14;
    	if(valor.toUpperCase().equals("XV"))
    		valorValido =  15;
    	if(valor.toUpperCase().equals("XVI"))
    		valorValido =  16;
    	if(valor.toUpperCase().equals("XVII"))
    		valorValido =  17;
    	if(valor.toUpperCase().equals("XVIII"))
    		valorValido =  18;
    	if(valor.toUpperCase().equals("XIX"))
    		valorValido =  19;
    	if(valor.toUpperCase().equals("XX"))
    		valorValido =  20;
    	if(valor.toUpperCase().equals("XXI"))
    		valorValido =  21;
    	
    	// Si el String es del tipo PB, PA, EP.
    	if(valor.toUpperCase().equals("PB")) // Planta Baja
    		valorValido =  1;
    	if(valor.toUpperCase().equals("PA")) // Planta Alta
    		valorValido =  2;
    	if(valor.toUpperCase().equals("EP")) // Entre Piso
    		valorValido =  2;
    	/*
    	// Si el String es del tipo ALT, BAJ.
    	if(valor.toUpperCase().equals("BAJ")) // Planta Baja
    		valorValido =  1;
    	if(valor.toUpperCase().equals("ALT")) // Planta Alta
    		valorValido =  2;
    	*/
    	return valorValido;
    }
    
	// Validaciones 
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		if (!validate()){
			return false;
		}
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		if (!validate())
			return false;
	
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	public boolean validate() throws Exception {
		
		//Validaciones requeridos

		// Localidad: distinta de nulo con codigo postal distinto de nulo, codigo subpostal distinto de nulo
		if (this.getLocalidad() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.LOCALIDAD_LABEL);
		}else{
			if(this.getLocalidad().getCodPostal() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.LOCALIDAD_LABEL);
			}
		}
		
		// Calle: nula o (id distinta de nula y con nombre de calle distinta de nula)
		if (this.getCalle() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CALLE_LABEL);
		}
		
		// Numero
		if (this.getNumero() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.DOMICILIO_NUMERO_REF);
		}
		
		// Bis
		if (this.getBis() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.DOMICILIO_BIS);
		}
		
		if (hasError()) {
			return false;
		}
		return true;
	}


	/**
	 * Validacion especifica cuando el domicilio trabaja con MCR
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validateForMCR() throws Exception {
		
		//Validaciones requeridos

		// Localidad: distinta de nulo con codigo postal distinto de nulo, codigo subpostal distinto de nulo
		if (this.getLocalidad() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.LOCALIDAD_LABEL);
		}else{
			if(this.getLocalidad().getCodPostal() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.LOCALIDAD_LABEL);
			}
		}
		
		// Calle: nula o (id distinta de nula y con nombre de calle distinta de nula)
		if (this.getCalle() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CALLE_LABEL);
		}else{
			if(this.getCalle().getCodCalle() == null && StringUtil.isNullOrEmpty(this.getCalle().getNombreCalle())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CALLE_LABEL);
			}
		}
		// Numero
		if (this.getNumero() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.DOMICILIO_NUMERO_REF);
		}
		
		// Bis
		if (this.getBis() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.DOMICILIO_BIS);
		}
		
		// Letra calle: se decidio no hacer validaciones sobre la misma

		if (hasError()) {
			return false;
		}
		return true;
	}
	
}
