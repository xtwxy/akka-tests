package resttest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class HttpRequest {

	public static final String DEFAULT_CHARSET = "utf-8";
	public static String send(
                String requestUrl, 
                String method, 
                String contentType,
                String acceptContentType,
		Map<String, String> headers, 
                String body
            ) throws Exception {
		requestUrl = requestUrl.replaceAll(" ", "%20");
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();
	
		connection.addRequestProperty("Accept-Content", acceptContentType);
		connection.addRequestProperty("Content-Type", contentType);
	
		for(Map.Entry<String, String> h : headers.entrySet()) {
			connection.addRequestProperty(h.getKey(), h.getValue());
		}
		
		connection.setFollowRedirects(true);
		// default is "GET".
		connection.setRequestMethod(method);
		if("POST".equals(method) || "PUT".equals(method)) {
			 connection.setDoOutput(true);
			 OutputStream os = connection.getOutputStream();
			 PrintWriter pw = new PrintWriter(os);
			 pw.print(body);
			 pw.close();
			 os.close();
		}
		StringBuffer headerSection = new StringBuffer();
		for (Map.Entry<String, List<String>> e : connection
				.getHeaderFields().entrySet()) {
			System.out.println(e.getKey() + " = " + e.getValue());
			if(e.getKey() != null) {
				headerSection.append(e.getKey() + " = " + e.getValue()).append("\n");
			} else {
				headerSection.append(e.getValue()).append("\n");
			}
		}
		System.out.println("\n\n");
		headerSection.append("\n\n");
	
		String responseContentType = connection.getHeaderField("Content-Type");
		if (responseContentType != null) {
			int startOfEqual = responseContentType.indexOf('=');
			if (startOfEqual != -1) {
				responseContentType = responseContentType.substring(startOfEqual + 1);
				responseContentType = (responseContentType == null) ? DEFAULT_CHARSET
						: responseContentType;
			} else {
				responseContentType = DEFAULT_CHARSET;
			}
		}
		if (connection.getResponseCode() < 300
				&& connection.getResponseCode() >= 200) {
			
			return headerSection.toString() + asString((InputStream)connection.getContent(), responseContentType);
		} else {
			return headerSection.toString();
		}
	}
	/**
	   * This convenience method allows to read a
	   * {@link org.apache.commons.fileupload.FileItemStream}'s
	   * content into a string, using the given character encoding.
	   * @param pStream The input stream to read.
	   * @param pEncoding The character encoding, typically "UTF-8".
	   * @see #asString(InputStream)
	   * @return The streams contents, as a string.
	   * @throws IOException An I/O error occurred.
	   */
	  public static String asString(InputStream pStream, String pEncoding)
	          throws IOException {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      copy(pStream, baos, true);
	      return baos.toString(pEncoding);
	  }

	    /**
	     * Default buffer size for use in
	     * {@link #copy(InputStream, OutputStream, boolean)}.
	     */
	    private static final int DEFAULT_BUFFER_SIZE = 8192;

	    /**
	     * Copies the contents of the given {@link InputStream}
	     * to the given {@link OutputStream}. Shortcut for
	     * <pre>
	     *   copy(pInputStream, pOutputStream, new byte[8192]);
	     * </pre>
	     * @param pInputStream The input stream, which is being read.
	     * It is guaranteed, that {@link InputStream#close()} is called
	     * on the stream.
	     * @param pOutputStream The output stream, to which data should
	     * be written. May be null, in which case the input streams
	     * contents are simply discarded.
	     * @param pClose True guarantees, that {@link OutputStream#close()}
	     * is called on the stream. False indicates, that only
	     * {@link OutputStream#flush()} should be called finally.
	     *
	     * @return Number of bytes, which have been copied.
	     * @throws IOException An I/O error occurred.
	     */
	    public static long copy(InputStream pInputStream,
	            OutputStream pOutputStream, boolean pClose)
	            throws IOException {
	        return copy(pInputStream, pOutputStream, pClose,
	                new byte[DEFAULT_BUFFER_SIZE]);
	    }
	    /**
	     * Copies the contents of the given {@link InputStream}
	     * to the given {@link OutputStream}.
	     * @param pIn The input stream, which is being read.
	     *   It is guaranteed, that {@link InputStream#close()} is called
	     *   on the stream.
	     * @param pOut The output stream, to which data should
	     *   be written. May be null, in which case the input streams
	     *   contents are simply discarded.
	     * @param pClose True guarantees, that {@link OutputStream#close()}
	     *   is called on the stream. False indicates, that only
	     *   {@link OutputStream#flush()} should be called finally.
	     * @param pBuffer Temporary buffer, which is to be used for
	     *   copying data.
	     * @return Number of bytes, which have been copied.
	     * @throws IOException An I/O error occurred.
	     */
	    public static long copy(InputStream pIn,
	            OutputStream pOut, boolean pClose,
	            byte[] pBuffer)
	    throws IOException {
	        OutputStream out = pOut;
	        InputStream in = pIn;
	        try {
	            long total = 0;
	            for (;;) {
	                int res = in.read(pBuffer);
	                if (res == -1) {
	                    break;
	                }
	                if (res > 0) {
	                    total += res;
	                    if (out != null) {
	                        out.write(pBuffer, 0, res);
	                    }
	                }
	            }
	            if (out != null) {
	                if (pClose) {
	                    out.close();
	                } else {
	                    out.flush();
	                }
	                out = null;
	            }
	            in.close();
	            in = null;
	            return total;
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (Throwable t) {
	                    /* Ignore me */
	                }
	            }
	            if (pClose  &&  out != null) {
	                try {
	                    out.close();
	                } catch (Throwable t) {
	                    /* Ignore me */
	                }
	            }
	        }
	    }

}
