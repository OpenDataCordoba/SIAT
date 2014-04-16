//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

public class ProcessMessage<T> {

	public static final Long POISON_MSG = -1L;   
	
	private Long id;

	private Long messageType;
	
	private T data;
	
	// Getters y Setters
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getMessageType() {
		return this.messageType;
	}


	public void setMessageType(Long messageType) {
		this.messageType = messageType;
	}
	
	public T getData() {
		return this.data;
	}
	
	public void setData(T data) {
		 this.data = data;
	}

	public boolean isPoisonMessage() {
		return POISON_MSG.equals(this.messageType); 
	}
	
}