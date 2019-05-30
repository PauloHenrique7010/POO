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
import java.util.Random;

import javax.swing.JOptionPane;

public class Cliente extends Thread {
// Flag que indica quando se deve terminar a execu��o.
	private static boolean done = false;
	static int chaveCrip = 10234;
	public static void main(String args[]) {
		try {
			// Para se conectar a algum servidor, basta se criar um
			// objeto da classe Socket. O primeiro par�metro � o IP ou
			// o endere�o da m�quina a qual se quer conectar e o
			// segundo par�metro � a porta da aplica��o. Neste caso,
			// utiliza-se o IP da m�quina local (127.0.0.1) e a porta
			// da aplica��o ServidorDeChat. Nada impede a mudan�a
			// desses valores, tentando estabelecer uma conex�o com
			// outras portas em outras m�quinas.
			Socket conexao = new Socket("127.0.0.1",2227);
			//Socket conexao = new Socket("172.16.2.113",2227);

			// uma vez estabelecida a comunica��o, deve-se obter os
			// objetos que permitem controlar o fluxo de comunica��o
			PrintStream saida = new PrintStream((conexao).getOutputStream());
			// enviar antes de tudo o nome do usu�rio
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Entre com o seu nome: ");
			String meuNome = teclado.readLine();
			if (meuNome.equals("."))
				meuNome = Anonimo();
			saida.println(meuNome);

			// Uma vez que tudo est� pronto, antes de iniciar o loop
			// principal, executar a thread de recep��o de mensagens.
			Thread t = new Cliente(conexao);
			t.start();

			// loop principal: obtendo uma linha digitada no teclado e
			// enviando-a para o servidor.
			String linha;

			while (true) {
				// ler a linha digitada no teclado
				System.out.print("> ");
				linha = teclado.readLine();
				//linha = encriptar(chaveCrip, linha);

				// antes de enviar, verifica se a conex�o n�o foi fechada
				if (done) {
					break;
				}
				// envia para o servidor
				saida.println(linha);
			}
		} catch (IOException e) {
			// Caso ocorra alguma excess�o de E/S, mostre qual foi.
			System.out.println("IOException: " + e);
		}
	}
	
	public static String Anonimo() {
		 String anon="anon";
        Random generator = new Random(); 
        int i = generator.nextInt(999) + 1;
        String is=String.valueOf(i);
        anon=anon.concat(is);
        return anon;         		
	}

	// parte que controla a recep��o de mensagens deste cliente
	private Socket conexao;
	// construtor que recebe o socket deste cliente

	public Cliente(Socket s) {
		conexao = s;
	}
	
	public static String encriptar(int chave, String texto){
        // Variavel que ira guardar o texto crifrado
        StringBuilder textoCifrado = new StringBuilder();
        // Variavel com tamanho do texto a ser encriptado
        int tamanhoTexto = texto.length();

        // Criptografa cada caracter por vez 
        for(int c=0; c < tamanhoTexto; c++){
           // Transforma o caracter em codigo ASCII e faz a criptografia
           int letraCifradaASCII = ((int) texto.charAt(c)) + chave;

           // Verifica se o codigo ASCII esta no limite dos caracteres imprimiveis
           while(letraCifradaASCII > 126)
              letraCifradaASCII -= 94;

           // Transforma codigo ASCII criptografado em caracter ao novo texto
           textoCifrado.append( (char)letraCifradaASCII );
        }

        // Por fim retorna a mensagem criptografada por completo
        return textoCifrado.toString();
        }

	 public String decriptar(int chave, String textoCifrado){
	      // Variavel que ira guardar o texto decifrado
	      StringBuilder texto = new StringBuilder();
	      // Variavel com tamanho do texto a ser decriptado
	      int tamanhoTexto = textoCifrado.length();
	      
	      // Deografa cada caracter por vez
	      for(int c=0; c < tamanhoTexto; c++){
	         // Transforma o caracter em codigo ASCII e faz a deografia
	         int letraDecifradaASCII = ((int) textoCifrado.charAt(c)) - chave;
	         
	         // Verifica se o codigo ASCII esta no limite dos caracteres imprimiveis
	         while(letraDecifradaASCII < 32)
	            letraDecifradaASCII += 94;

	         // Transforma codigo ASCII deografado em caracter ao novo texto
	         texto.append( (char)letraDecifradaASCII );
	      }
	      
	      // Por fim retorna a mensagem deografada por completo
	      return texto.toString();
	   }
	      
	// execu��o da thread
	public void run() {

		try {

			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			String linha, linhaCrip;
                        String linhaOut;
			while (true) {
				// pega o que o servidor enviou
				linha = entrada.readLine();    
				linhaCrip = encriptar(chaveCrip,linha);
                                linhaOut = decriptar(chaveCrip, linhaCrip);
                                System.out.println();
                                System.out.println(linhaOut);
                                System.out.print("...> ");

                                
                             
                                    
				// verifica se � uma linha v�lida. Pode ser que a conex�o
				// foi interrompida. Neste caso, a linha � null. Se isso
				// ocorrer, termina-se a execu��o saindo com break
				if (linha == null) {
					System.out.println("Conex�o encerrada!");
					break;
				}
				// caso a linha n�o seja nula, deve-se imprimi-la				
			}
		} catch (IOException e) {
			// caso ocorra alguma exce��o de E/S, mostre qual foi.
			System.out.println("IOException: " + e);
		}
		// sinaliza para o main que a conex�o encerrou.
		done = true;
	}
}