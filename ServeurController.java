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

public class ServeurController {
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
	public static int port = 6000;

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
			a("nouvel THread start...");
			Runnable newThread = new ClientHandler(theConversation);
			Thread t = new Thread(newThread);
			t.start();
		}
	}

	// ----------------------------------------------------------------------
	// fin de classe
}

// ----------------------------------------------------------------------
// ----------------------------------------------------------------------
// ----------------------------------------------------------------------
// ----------------------------------------------------------------------
