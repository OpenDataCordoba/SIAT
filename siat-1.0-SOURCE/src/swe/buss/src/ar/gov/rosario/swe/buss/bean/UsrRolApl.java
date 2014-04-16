//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.iface.model.UsrRolAplVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.DemodaServiceException;


@Entity
@Table(name = "swe_usrrolapl")
@SequenceGenerator(
    name="usrrolapl_seq",
    sequenceName="swe_usrrolapl_id_seq",
    allocationSize = 0
)
public class UsrRolApl extends BaseBO {
	
	private static final long serialVersionUID = 1L;	
	
    @Id @GeneratedValue(generator="usrrolapl_seq",strategy=GenerationType.SEQUENCE)
    private Long id;
 
    public Long getId() {
        return id;
    }

	@OneToOne
    @JoinColumn(name="idUsrApl")	  
	private UsrApl usrApl;
    
	@OneToOne
    @JoinColumn(name="idRolApl")	
	private RolApl rolApl;
	
	public UsrRolApl(){
		super();
	}

	public RolApl getRolApl() {
		return rolApl;
	}
	public void setRolApl(RolApl rolApl) {
		this.rolApl = rolApl;
	}
	public UsrApl getUsrApl() {
		return usrApl;
	}
	public void setUsrApl(UsrApl usrApl) {
		this.usrApl = usrApl;
	}
	
	public static UsrRolApl getById(Long id) {
		return (UsrRolApl) SweDAOFactory.getUsrRolAplDAO().getById(id);
	}

	public void loadFromVOForCreate(UsrRolAplVO usrRolAplVO) throws DemodaServiceException{
		
		UsrApl usrApl = UsrApl.getById(usrRolAplVO.getUsrApl().getId());
		if (usrApl == null){
			throw new DemodaServiceException("No se encontro registro de Usuario de Aplicacion del Rol de Usuario de Aplicacion en la Base de Datos.");
		}
		this.setUsrApl(usrApl);
		
		RolApl rolApl = RolApl.getById(usrRolAplVO.getRolApl().getId());
		if (rolApl == null){
			throw new DemodaServiceException("No se encontro registro de Rol de Aplicacion del Rol de Usuario de Aplicacion en la Base de Datos.");
		}
		
		this.setRolApl(rolApl);
	}

	
	public boolean validateCreate() {

		//limpiamos la lista de errores
		clearError();

		/* ver que hacemos
		//Validaciones de VO
		if (StringUtil.isNullOrEmpty(getUsername())) {
			addRecoverableError(SweCommonError.USR_APL_USERNAME_REQUIRED);
		}

		if (this.fechaAlta == null) {
			addRecoverableError(SweCommonError.USR_APL_FECHAALTA_REQUIRED);
		}
		*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}


	public boolean validateDelete() {

		//limpiamos la lista de errores
		clearError();

		return true;		
	}

}
