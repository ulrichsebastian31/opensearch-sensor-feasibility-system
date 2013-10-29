package net.eads.astrium.hmas.operations;

import net.eads.astrium.hmas.exceptions.DreamEoSpsException;


/**
 * 
 * @author re-sulrich
 * 
 * This class is a generic operation that generalises the EOSPS operations
 * and provides basic functions such as getting request, writing response
 * and two functions called validRequest and executeRequest that will be implemented by the specifications
 * 
 */
public abstract class DreamEOSPSOperation<DreamConfFolder, RequestClass, ResponseClass, ErrorClass extends DreamEoSpsException> {

        private DreamConfFolder serviceConfiguration;

	private RequestClass request;
	private ResponseClass response;
	/**
	 * 
	 * @param request
	 */
	public DreamEOSPSOperation(DreamConfFolder serviceConfiguration, RequestClass request) {

                this.serviceConfiguration = serviceConfiguration;
		this.setRequest(request);

	}

	/**
	 * 
	 * @param request
	 */
	public void setRequest(RequestClass request) {

		this.request = request;

	}

	public RequestClass getRequest() {
		return request;
	}

	/**
	 * 
	 * @param response
	 */
	public void setResponse(ResponseClass response) {
		this.response = response;
	}

	public ResponseClass getResponse() {
		return response;
	}
        
        /**
         * 
	 * @param serviceConfiguration
         */
        public DreamConfFolder getServiceConfiguration() {
            return serviceConfiguration;
        }

        public void setServiceConfiguration(DreamConfFolder serviceConfiguration) {
            this.serviceConfiguration = serviceConfiguration;
        }

	public abstract void validRequest() throws DreamEoSpsException;

	public abstract void executeRequest() throws DreamEoSpsException;

}