import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;

/*
 * TP de TLC - Etape 2
 * Test de charge sur une application web
 * Des séries de requêtes sont effectuées afin de mesurées la latence sur des GET, POST et DELETE
 */
public class TestCharge {
	
	// pour effectuer un log des opérations effectuées
	static PrintWriter writer;
	
	/*
	 * méthode principale
	 */
    public static void main(String[] args) {
        try {
        	writer = new PrintWriter("mon-log.txt", "UTF-8");
        	writer.println("Test de charge via Google AppEngine");
        	
//        	writer.println( "Test #1 : GET : https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou" );
//            sequenceGET("https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou", 10, 5);
//        	writer.println( "Test #2 : GET : https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou" );
//            sequenceGET("https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou", 100, 5);
            
//        	writer.println( "Test #3 : POST : https://vaulted-hawk-194622.appspot.com/api/advertisementV2 - content=[{ \"title\":\"Picsou\", \"price\":20}]" );
//            sequencePOST( 10, 5);
//        	writer.println( "Test #4 : POST : https://vaulted-hawk-194622.appspot.com/api/advertisementV2 - content=[{ \"title\":\"Picsou\", \"price\":20}]" );
//            sequencePOST( 100, 5);
        	
//        	writer.println( "Test #5 : GET : https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou" );
//          sequenceGET("https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou", 10, 5);
//      	writer.println( "Test #6 : GET : https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou" );
//          sequenceGET("https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou", 100, 5);
        	
        	writer.println( "Test #7 : DELETE : searchByTitle=Picsou" );
        	int i=0;
        	while( i<10 ){
	            sequencePOST( 50, 1);
	        	String requete = "https://vaulted-hawk-194622.appspot.com/api/advertisementV2?searchByTitle=Picsou";
	        	sequenceGET("https://vaulted-hawk-194622.appspot.com/api/advertisement?content="+ obtainJsonToDelete(requete),1,1);
	        	i++;
        	}
        	writer.close();
        }catch (Exception e) { System.out.println("ERREUR"); System.out.println(e.getMessage()); }
    }
    
    /*
     * effectue un GET sur une url donnée pour API V2
     * effectue un DELETE sur une url donnée pour API V1
     */
    public static void callAction( String myUrl) throws Exception {
        URL oracle = new URL(myUrl);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        in.close();
    }
    
    /*
     * Lance plusieurs exécutions de callAction - GET ou DELETE
     */
    public static void sequenceGET( String monUrl, int nbTest, int nbIteration ) throws Exception {

        int iteration = 0;
    	while(iteration<nbIteration)
    	{
	        int i=0;
	        long startCurrentTime = System.currentTimeMillis();
	        while(i<nbTest)
	        {
	        	callAction(monUrl);
	            i++;
	        }
	        long endCurrentTime = System.currentTimeMillis();
	        writer.println(i + " transactions de la séquence " + iteration);
	        writer.println("Durée totale : " + (endCurrentTime - startCurrentTime) + "ms.");
	        writer.println("Durée moyenne : " + (endCurrentTime - startCurrentTime) / i + "ms.");
	        iteration++;
    	}
    }
    
    /*
     * Lance plusieurs exécutions de la tache post()
     */
    public static void sequencePOST( int nbTest, int nbIteration ) throws Exception {

        int iteration = 0;
    	while(iteration<nbIteration)
    	{
	        int i=0;
	        long startCurrentTime = System.currentTimeMillis();
	        while(i<nbTest)
	        {
	        	post();
	            i++;
	        }
	        long endCurrentTime = System.currentTimeMillis();
	        writer.println(i + " transactions de la séquence " + iteration);
	        writer.println("Durée totale : " + (endCurrentTime - startCurrentTime) + "ms.");
	        writer.println("Durée moyenne : " + (endCurrentTime - startCurrentTime) / i + "ms.");
	        iteration++;
    	}
    }
    
    /*
     * Création d'un post directement vers l'application
     */
    public static void post() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://vaulted-hawk-194622.appspot.com/api/advertisementV2");

        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("content", "[{ \"title\":\"Picsou\", \"price\":20}]"));

        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);

            // Print out the response message
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	/*
	 * Recherche de la liste des éléments à supprimer sous forme d'un JSON
	 */
    public static String obtainJsonToDelete(String myUrl) throws Exception {
    	

    	String monJson = "";
        URL oracle = new URL(myUrl);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        String inputLine;
        CharSequence chs = "value=\"[{";
        while ((inputLine = in.readLine()) != null)
        	if (inputLine.contains(chs)){
        		// remplacer &#034; par \"
        		inputLine = inputLine.replaceAll("&#034;", "\"");
        		inputLine = inputLine.replaceAll("\" />", "");
        		monJson = inputLine.replaceAll("				value=\"", "");
        		System.out.println(monJson);
        	}
        in.close();
        return monJson;
    }
    
}
