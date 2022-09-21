package com.toby.rest;

import com.toby.dto.GlPayDTO;
import com.toby.root.PayDTO;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;

//import sun.misc.IOUtils;

/**
 *
 * @author Lenovo
 */
@Path("/payment")
public class PayRest {

    @Transactional
    @Path("/request")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String request(PayDTO payDTO) throws IOException {
        URL url = new URL("https://oppwa.com/v1/checkouts");

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + payDTO.token);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        String data = ""
                + "entityId=" + payDTO.entityId
                + "&amount=" + payDTO.amount
                + "&currency=" + payDTO.currency
                + "&paymentType=" + payDTO.paymentType ;

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();
        int responseCode = conn.getResponseCode();
        InputStream is;

        if (responseCode >= 400) {
            is = conn.getErrorStream();
        } else {
            is = conn.getInputStream();
        }

        return IOUtils.toString(is);
    }
    
    @Transactional
    @Path("/requestStatue")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String showRequest(PayDTO payDTO) throws IOException {
	URL url = new URL("https://oppwa.com/v1/checkouts/" + payDTO.paymentId + "/payment?entityId=" + payDTO.entityId);

	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + payDTO.token);


	int responseCode = conn.getResponseCode();
	InputStream is;

	if (responseCode >= 400) is = conn.getErrorStream();
	else is = conn.getInputStream();

	return IOUtils.toString(is);
}
    
 
    

}
