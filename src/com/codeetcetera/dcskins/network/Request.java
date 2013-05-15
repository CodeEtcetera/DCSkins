/**
 * File: Request.java
 * 
 */
package com.codeetcetera.dcskins.network;

/**
 * @author CodeEtcetera
 * 
 */
public class Request {
	private final String user;
	private byte dataType = -1;
	private byte responseType = -1;
	private byte[] response;
	
	public Request(final String user, final byte dataType) {
		this.user = user;
		this.dataType = dataType;
	}
	
	public boolean isRequestSet() {
		return(user != null);
	}
	
	public boolean isResponseSet() {
		return(isRequestSet() && response != null && responseType >= 0);
	}
	
	/**
	 * @return the responseType
	 */
	public byte getResponseType() {
		return responseType;
	}
	
	/**
	 * @param responseType
	 *            the responseType to set
	 */
	public void setResponseType(final byte responseType) {
		this.responseType = responseType;
	}
	
	/**
	 * @return the response
	 */
	public byte[] getResponse() {
		return response;
	}
	
	/**
	 * @param response
	 *            the response to set
	 */
	public void setResponse(final byte[] response) {
		this.response = response;
	}
	
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * @return the dataType
	 */
	public byte getDataType() {
		return dataType;
	}
}
