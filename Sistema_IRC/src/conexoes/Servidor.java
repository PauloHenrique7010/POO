/*
 * SISTEMA IRC
 * GRUPO: LUCAS RIBEIRO, GABRIEL BRITO, PAULO HENRIQUE
 * 3� ADS - B
 * PROFESSOR: GERSON DA PENHA
 * MAT�RIA: PROGRAMA��O ORIENTADA A OBJETO 2019
 */


package conexoes;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.jndi.cosnaming.IiopUrl.Address;

public class Servidor extends Thread {
	public static void main(String args[]) {
		// instancia o vetor de clientes conectados
		clientes = new Vector();
		nomeClientes = new Vector();
		try {
			// criando um socket que fica escutando a porta 2222.
			ServerSocket s = new ServerSocket(2227);
			// Loop principal.
			while (true) {
				// aguarda algum cliente se conectar. A execu��o do
				// servidor fica bloqueada na chamada do m�todo accept da
				// classe ServerSocket. Quando algum cliente se conectar
				// ao servidor, o m�todo desbloqueia e retorna com um
				// objeto da classe Socket, que � porta da comunica��o.
				System.out.print("Esperando alguem se conectar...\n");
				Socket conexao = s.accept();
				InetAddress address = conexao.getInetAddress();
				int portinha = conexao.getPort();
				

				System.out.println(" Conectou! no ip "+address.toString());
				// cria uma nova thread para tratar essa conex�o
				Thread t = new Servidor(conexao);
				t.start();
				// voltando ao loop, esperando mais algu�m se conectar.
			}
		} catch (IOException e) {
			// caso ocorra alguma excess�o de E/S, mostre qual foi.
			System.out.println("IOException: " + e);
		}

	}

	// Parte que controla as conex�es por meio de threads.
	// Note que a instancia��o est� no main.
	private static Vector clientes;
	private static Vector nomeClientes;
	// socket deste cliente
	private Socket conexao;
// nome deste cliente
	private String meuNome;

	// construtor que recebe o socket deste cliente
	public Servidor(Socket s) {
		conexao = s;
	}
		
	public static String formaMsgSemArroba(String linha) {
		int cont = 0;
		String msgPronta = "";
		String valores = linha;
		String outr = null;
		char k = '@';
		String retorno =null;
        String[] arrayValores = valores.split(" ");
        for (String s: arrayValores) {
        	outr = s; //@lrsonne 
            if (outr.charAt(0) == k) {
            	retorno = (outr.substring(1, outr.length()));
            	break;
            }
            else {
            	retorno = "";
            }
            cont+=1;
            
        	
        }
        return retorno;
	}
	public static String SeparaUsuario(String linha) {
		//@lrsonne oi
		String valores = linha;
		String outr = null;
		String retorno = null;
		char k = '@';
        String[] arrayValores = valores.split(" ");
        for (String s: arrayValores) {
            outr = s; //@lrsonne 
            if (outr.charAt(0) == k) {
            	retorno = (outr.substring(1, outr.length()));
            	break;
            }
            else {
            	retorno = "";
            }
        }
        return retorno;
        
        
		
	}

	// execu��o da thread
	public void run() {
		try {
			// objetos que permitem controlar fluxo de comunica��o
			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			PrintStream saida = new PrintStream(conexao.getOutputStream());
			// primeiramente, espera-se pelo nome do cliente
			meuNome = entrada.readLine();
			// agora, verifica se string recebida � valida, pois
			// sem a conex�o foi interrompida, a string � null.
			// Se isso ocorrer, deve-se terminar a execu��o.
			if (meuNome == null) {
				return;
			}
			// Uma vez que se tem um cliente conectado e conhecido,
			// coloca-se fluxo de sa�da para esse cliente no vetor de
			// clientes conectados.
			clientes.add(saida);
			nomeClientes.add(meuNome);
			// clientes � objeto compartilhado por v�rias threads!
			// De acordo com o manual da API, os m�todos s�o
			// sincronizados. Portanto, n�o h� problemas de acessos
			// simult�neos.

			// Loop principal: esperando por alguma string do cliente.
			// Quando recebe, envia a todos os conectados at� que o
			// cliente envie linha em branco.
			// Verificar se linha � null (conex�o interrompida)
			// Se n�o for nula, pode-se compar�-la com m�todos string
			String linha = entrada.readLine();
			
			while (linha != null && !(linha.trim().equals(""))) {
				// reenvia a linha para todos os clientes conectados
				if (SeparaUsuario(linha) != ""){
					sendPrivate(saida, " PV: ", linha);
					// espera por uma nova linha.
					linha = entrada.readLine();						
				}
				else {
					sendToAll(saida, " disse: ", linha);
					// espera por uma nova linha.
					linha = entrada.readLine();
				}
			}
			// Uma vez que o cliente enviou linha em branco, retira-se
			// fluxo de sa�da do vetor de clientes e fecha-se conex�o.
			sendToAll(saida, " saiu ", "do chat!");
			clientes.remove(saida);
			nomeClientes.remove(meuNome);
			conexao.close();
		} catch (IOException e) {
			// Caso ocorra alguma excess�o de E/S, mostre qual foi.
			System.out.println("IOException: " + e);
		}
	}
	
	public void sendPrivate(PrintStream saida, String acao, String linha) throws IOException {
		Enumeration e = clientes.elements();
		Enumeration n = nomeClientes.elements();
		String privado = "";
		String pess = "";
		int contador = 0;
		
		while (e.hasMoreElements()) {
			// obt�m o fluxo de sa�da de um dos clientes
			PrintStream chat = (PrintStream) e.nextElement();
			privado = SeparaUsuario(linha);
			String msgPrivada = "";
			
			pess = (String) nomeClientes.get(contador);	
			contador +=1;			
			
			if (pess.trim().equals((String) privado.trim())) {
				//msgPrivada = formaMsgSemArroba(linha);
				msgPrivada = linha;
				chat.println(meuNome + acao + msgPrivada);
				break;
			}
		}
	}	

	// enviar uma mensagem para todos, menos para o pr�prio
	public void sendToAll(PrintStream saida, String acao, String linha) throws IOException {
		Enumeration e = clientes.elements();
		while (e.hasMoreElements()) {
			// obt�m o fluxo de sa�da de um dos clientes
			PrintStream chat = (PrintStream) e.nextElement();
			
			// envia para todos, menos para o pr�prio usu�rio
			if (chat != saida) {
				chat.println(meuNome + acao + linha);
			}
		}
	}
}
