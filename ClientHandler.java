//----------------------------------------------------------------------
//----------------------------------------------------------------------
//
//		-------------------------------------
//		 Tentative de serveur de pages html.
//		-------------------------------------
//
//----------------------------------------------------------------------
//----------------------------------------------------------------------

//	Java --- langage
//
import java.io.*;

//	Java --- transmissions
//	
import java.net.*;

//----------------------------------------------------------------------
//----------------------------------------------------------------------

public class ClientHandler implements Runnable {
	// debut de classe
	// ----------------------------------------------------------------------

	/**
	 * Commodit&eacute; d'&eacute;criture.
	 */
	static public void log(String s) {
		System.out.println("Thread name:" + Thread.currentThread().getName()+"---"+s);
	}

	public ClientHandler(Socket theCon) {
		theConversation = theCon;
	}

	/**
	 * Fichiers de la conversation.
	 */
	public Socket theConversation;
	public BufferedReader theIn;
	public PrintStream theOut;
	public String pageName;
	public BufferedReader page;

	/**
	 * Routine principale.
	 */
	public void run() {
		try {
			theIn = new BufferedReader(new InputStreamReader(theConversation.getInputStream()));
			theOut = new PrintStream(new BufferedOutputStream(theConversation.getOutputStream()));
			doConversation();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				theOut.flush();
				theOut.close();
				theIn.close();
				theConversation.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Le serveur fait la conversation avec un client. (sans erreur)
	 */
	public void doConversation() {
		try {
			doConv();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Le serveur fait la conversation avec un client. (avec erreurs
	 * eventuelles)
	 */
	public void doConv() throws Exception {
		log("...doConv()...");

		log("...premiere ligne de la question...");
		String ligne = theIn.readLine();
		int i = ligne.indexOf("/");
		int j = ligne.lastIndexOf(" ");
		pageName = new String(ligne.substring(i + 1, j));
		log("page demandee: " + pageName + "!");

		// indesirable + systematique:
		if (pageName.endsWith(".ico"))
			return;
		// indesirables:
		if (pageName.endsWith(".gif"))
			return;
		if (pageName.endsWith(".jpg"))
			return;
		if (pageName.endsWith(".jpeg"))
			return;
		if (pageName.endsWith(".mpg"))
			return;
		if (pageName.endsWith(".mpeg"))
			return;

		page = new BufferedReader(new InputStreamReader(new FileInputStream(pageName)));

		log("...suite de la question...");
		while (true) {
			if (ligne.equals("\r\n"))
				break;
			if (ligne.equals("\n"))
				break;
			if (ligne.equals("\r"))
				break;
			if (ligne.equals(""))
				break;
			log("serveur recoit: " + ligne);
			ligne = theIn.readLine();
		}

		log("...reponse...");

		theOut.println("HTTP/1.1 200 OK");
		if (pageName.endsWith(".java"))
			theOut.println("Content-type: text/plain");
		else
			theOut.println("Content-type: text/html");
		theOut.println();

		log("...contenu de la page demandee...");
		String ligpage = page.readLine(); // TODO: add read
		while (ligpage != null) {
			theOut.println(ligpage);
			ligpage = page.readLine(); // TODO: add write
		}

		page.close();
		theOut.flush();
	}

	// ----------------------------------------------------------------------
	// fin de classe
}

// ----------------------------------------------------------------------
// ----------------------------------------------------------------------
// ----------------------------------------------------------------------
// ----------------------------------------------------------------------
