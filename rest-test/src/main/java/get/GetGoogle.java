package get;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

public class GetGoogle {
	public static final String DEFAULT_CHARSET = "utf-8";

	public static void main(String[] args) {
		try {
			URL url = new URL("http://localhost:9999/resources/tables/DEVICE_TYPE?clause=where%20id=10");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.addRequestProperty("Accept-Language", "us,en;q=0.5");
			// connection.addRequestProperty("Accept-Content", "application/json");
			connection.setFollowRedirects(true);
			// default is "GET".
			// connection.setRequestMethod("POST");
			// connection.setDoOutput(true);
			// OutputStream os = connection.getOutputStream();
			// PrintWriter pw = new PrintWriter(os);
			// pw.println("hi, buddy!");
			// pw.close();
			// os.close();
			for (Map.Entry<String, List<String>> e : connection
					.getHeaderFields().entrySet()) {
				System.out.println(e.getKey() + " = " + e.getValue());
			}
			System.out.println("\n\n");

			String contentType = connection.getHeaderField("Content-Type");
			if (contentType != null) {
				int startOfEqual = contentType.indexOf('=');
				if (startOfEqual != -1) {
					contentType = contentType.substring(startOfEqual + 1);
					contentType = (contentType == null) ? DEFAULT_CHARSET
							: contentType;
				} else {
					contentType = DEFAULT_CHARSET;
				}
			}
			if (connection.getResponseCode() < 300 && connection.getResponseCode() >= 200) {
				Parser p = new Parser(connection);
				p.visitAllNodesWith(new NodeVisitor() {
				     public void visitTag (Tag tag)
				     {
				    	 if(tag instanceof TableTag) {
				    		 TableTag ttag = (TableTag) tag;

//				    		 System.out.println(ttag.getFirstChild().getText());
				    		 TableRow[] rows = ttag.getRows();
				    		 
				    		 for(TableRow row : rows) {
				    			 if(row.getHeaders().length != 0) {
					    			 for(int i = 0; i < row.getHeaders().length; i++) {
					    				 System.out.print(row.getHeaders()[i].getStringText() + ", ");
					    			 }
				    			 } else {
					    			 for(int i = 0; i < row.getColumnCount(); i++) {
					    				 System.out.print(row.getColumns()[i].getStringText() + ", ");
					    			 }
				    			 }
				    			 System.out.println();
				    		 }
				    	 }
				     }

				});
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
