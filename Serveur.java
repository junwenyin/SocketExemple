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

public class Serveur {
	// debut de classe
	// ----------------------------------------------------------------------

	/**
	 * Commodit&eacute; d'&eacute;criture.
	 */
	static public void a(String s) {
		System.out.println(s);
	}

	/**
	 * Singleton applicatif.
	 */
	static public Serveur theAppli;

	/**
	 * Point d'entr&eacute;e.
	 */
	static public void main(String args[]) throws Exception {
		a("---------------------");
		a(" Serveur TCP + HTTP: ");
		a("---------------------");
		a("\nB.M.G. version automne 2016 ");

		for (int w = 0; w < args.length; w++) {
			a("\t" + args[w]);
			if (args[w].startsWith("port=")) {
				port = Integer.parseInt(args[w].substring(5));
			}
		}

		a("port utilise: " + port + "!");

		theAppli = new Serveur();
		theAppli.myGo();
	}

	/**
	 * Port / Service.
	 */
	public static int port = 5000;

	/**
	 * ServerSocket.
	 */
	public static ServerSocket theConnection;

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
	public void myGo() throws Exception {
		theConnection = new ServerSocket(port);
		a("Socket de connexion en place...");
		while (true) {
			a("\n\n");
			theConversation = theConnection.accept();
			a("nouvel appel recu...");
			theIn = new BufferedReader(new InputStreamReader(theConversation.getInputStream()));
			theOut = new PrintStream(new BufferedOutputStream(theConversation.getOutputStream()));
			doConversation();
			theOut.flush();
			theOut.close();
			theIn.close();
			theConversation.close();
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
		a("...doConv()...");

		a("...premiere ligne de la question...");
		String ligne = theIn.readLine();
		int i = ligne.indexOf("/");
		int j = ligne.lastIndexOf(" ");
		pageName = new String(ligne.substring(i + 1, j));
		a("page demandee: " + pageName + "!");

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

		a("...suite de la question...");
		while (true) {
			if (ligne.equals("\r\n"))
				break;
			if (ligne.equals("\n"))
				break;
			if (ligne.equals("\r"))
				break;
			if (ligne.equals(""))
				break;
			a("serveur recoit: " + ligne);
			ligne = theIn.readLine();
		}

		a("...reponse...");

		theOut.println("HTTP/1.1 200 OK");
		if (pageName.endsWith(".java"))
			theOut.println("Content-type: text/plain");
		else
			theOut.println("Content-type: text/html");
		theOut.println();

		a("...contenu de la page demandee...");
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
