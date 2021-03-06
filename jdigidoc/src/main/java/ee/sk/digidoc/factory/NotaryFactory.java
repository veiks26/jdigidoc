/*
 * NotaryFactory.java
 * PROJECT: JDigiDoc
 * DESCRIPTION: Digi Doc functions for creating
 *	and reading signed documents. 
 * AUTHOR:  Veiko Sinivee, S|E|B IT Partner Estonia
 *==================================================
 * Copyright (C) AS Sertifitseerimiskeskus
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * GNU Lesser General Public Licence is available at
 * http://www.gnu.org/copyleft/lesser.html
 *==================================================
 */

package ee.sk.digidoc.factory;
import ee.sk.digidoc.DigiDocException;
import ee.sk.digidoc.Notary;
import ee.sk.digidoc.Signature;
import java.io.ByteArrayOutputStream;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.ocsp.OCSPResp;

/**
 * Interface for notary functions
 * @author  Veiko Sinivee
 * @version 1.0
 */
public interface NotaryFactory 
{
    /** 
     * initializes the implementation class 
     */
    public void init()
        throws DigiDocException;

    /**
     * Get confirmation from AS Sertifitseerimiskeskus
     * by creating an OCSP request and parsing the returned 
     * OCSP response
     * @param sig Signature object
     * @param signersCert signature owners cert
     * @param caCert CA cert for this signer
     * @param notaryCert notarys own cert
     * @returns Notary object
     */
    public Notary getConfirmation(Signature sig, 
        X509Certificate signersCert, X509Certificate caCert)
        throws DigiDocException;
    
    /**
     * Get confirmation from AS Sertifitseerimiskeskus
     * by creating an OCSP request and parsing the returned
     * OCSP response. CA and reponders certs are read 
     * using paths in the config file or maybe from
     * a keystore etc.
     * @param sig Signature object
     * @param signersCert signature owners cert
     * @returns Notary object
     */
    public Notary getConfirmation(Signature sig, X509Certificate signersCert) 
        throws DigiDocException;
    
    /**
     * Get confirmation from AS Sertifitseerimiskeskus
     * by creating an OCSP request and parsing the returned
     * OCSP response
     * @param sig Signature object. 
     * @param signersCert signature owners cert
     * @param caCert CA cert for this signer
     * @param notaryCert OCSP responders cert
     * @param ocspUrl OCSP responders url
     * @returns Notary object
     */
    public Notary getConfirmation(Signature sig, 
        X509Certificate signersCert, X509Certificate caCert, X509Certificate notaryCert, String ocspUrl) 
        throws DigiDocException;
    
    /**
     * Check the response and parse it's data
     * @param not initial Notary object that contains only the
     * raw bytes of an OCSP response
     * @returns Notary object with data parsed from OCSP response
     */
    public Notary parseAndVerifyResponse(Signature sig, Notary not)
        throws DigiDocException;
        
    /**
     * Returns the OCSP responders certificate
     * @param responderCN responder-id's CN
     * @param specificCertNr specific cert number that we search.
     * If this parameter is null then the newest cert is seleced (if many exist)
     * @returns OCSP responders certificate
     */
    public X509Certificate getNotaryCert(String responderCN, String specificCertNr);
    
    /**
     * Returns the CA certificate
     * @param CN CA certificates CN
     * @returns CA certificate
     */
    public X509Certificate getCACert(String responderCN);

    /**
     * Verifies the certificate by creating an OCSP request
     * and sending it to SK server.
     * @param cert certificate to verify
     * @throws DigiDocException if the certificate is not valid
     * @return ocsp response
     * @deprecated not thorougly tested
     */   
    public OCSPResp checkCertificate(X509Certificate cert)
    		throws DigiDocException;
    
    /**
     * Verifies the certificate by creating an OCSP request
     * and sending it to SK server.
     * @param cert certificate to verify
     * @param httpFrom HTTP_FROM optional argument
     * @throws DigiDocException if the certificate is not valid
     * @return ocsp response
     * @deprecated not thorougly tested
     */   
    public OCSPResp checkCertificate(X509Certificate cert, String httpFrom)
    		throws DigiDocException;
    
    /**
     * Verifies the certificate.
     * @param cert certificate to verify
     * @param bUseOcsp flag: use OCSP to verify cert. If false then use CRL instead
     * @throws DigiDocException if the certificate is not valid
     * @deprecated not thorougly tested
     */   
    public void checkCertificateOcspOrCrl(X509Certificate cert, boolean bUseOcsp) 
        throws DigiDocException;
        
    /**
     * Get confirmation from AS Sertifitseerimiskeskus
     * by creating an OCSP request and parsing the returned
     * OCSP response
     * @param nonce signature nonce
     * @param signersCert signature owners cert
     * @param notId new id for Notary object
     * @param httpFrom HTTP_FROM header value (optional)
     * @returns Notary object
     */
    public Notary getConfirmation(byte[] nonce, 
        X509Certificate signersCert, String notId, String httpFrom) 
        throws DigiDocException;
        
    /**
     * Verifies the certificate by creating an OCSP request
     * and sending it to ocsp server.
     * @param cert certificate to verify
     * @param caCert CA certificate
     * @param url OCSP responder url
     * @param bosNonce buffer to return generated nonce
     * @param sbRespId buffer to return responderId field
     * @param bosReq buffer to return ocsp request
     * @param httpFrom http_from atribute
     * @throws DigiDocException if the certificate is not valid
     * @deprecated not thorougly tested
     */   
    public OCSPResp sendCertOcsp(X509Certificate cert, X509Certificate caCert, String url, 
    		ByteArrayOutputStream bosNonce, StringBuffer sbRespId, 
    		ByteArrayOutputStream bosReq, String httpFrom)  
        throws DigiDocException;
    
    /**
     * Verifies OCSP response by given responder cert. Checks actual certificate status. 
     * @param resp ocsp response
     * @param cert certificate to check
     * @param ocspCert OCSP responders cert
     * @param nonce1 initial nonce value
     * @return true if verified ok
     * @throws DigiDocException
     * @deprecated not thorougly tested
     */
    public boolean checkCertOcsp(OCSPResp resp, X509Certificate cert, 
    		X509Certificate ocspCert, byte[] nonce1, X509Certificate caCert)
    throws DigiDocException;
    

}

