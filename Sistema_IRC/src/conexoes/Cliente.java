/*
 * SISTEMA IRC
 * GRUPO: LUCAS RIBEIRO, GABRIEL BRITO, PAULO HENRIQUE
 * 3º ADS - B
 * PROFESSOR: GERSON DA PENHA
 * MATÉRIA: PROGRAMAÇÃO ORIENTADA A OBJETO 2019
 */



package conexoes;

import java.io.*;
import java.net.*;
import java.util.Random;

import javax.swing.JOptionPane;

public class Cliente extends Thread {
// Flag que indica quando se deve terminar a execuï¿½ï¿½o.
	private static boolean done = false;
	static int chaveCrip = 10234;
	public static void main(String args[]) {
		try {
			// Para se conectar a algum servidor, basta se criar um
			// objeto da classe Socket. O primeiro parï¿½metro ï¿½ o IP ou
			// o endereï¿½o da mï¿½quina a qual se quer conectar e o
			// segundo parï¿½metro ï¿½ a porta da aplicaï¿½ï¿½o. Neste caso,
			// utiliza-se o IP da mï¿½quina local (127.0.0.1) e a porta
			// da aplicaï¿½ï¿½o ServidorDeChat. Nada impede a mudanï¿½a
			// desses valores, tentando estabelecer uma conexï¿½o com
			// outras portas em outras mï¿½quinas.
			Socket conexao = new Socket("127.0.0.1",2227);
			//Socket conexao = new Socket("172.16.2.113",2227);

			// uma vez estabelecida a comunicaï¿½ï¿½o, deve-se obter os
			// objetos que permitem controlar o fluxo de comunicaï¿½ï¿½o
			PrintStream saida = new PrintStream((conexao).getOutputStream());
			// enviar antes de tudo o nome do usuï¿½rio
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Entre com o seu nome: ");
			String meuNome = teclado.readLine();
			if (meuNome.equals("."))
				meuNome = Anonimo();
			saida.println(meuNome);

			// Uma vez que tudo estï¿½ pronto, antes de iniciar o loop
			// principal, executar a thread de recepï¿½ï¿½o de mensagens.
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

				// antes de enviar, verifica se a conexï¿½o nï¿½o foi fechada
				if (done) {
					break;
				}
				// envia para o servidor
				saida.println(linha);
			}
		} catch (IOException e) {
			// Caso ocorra alguma excessï¿½o de E/S, mostre qual foi.
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

	// parte que controla a recepï¿½ï¿½o de mensagens deste cliente
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
	      
	// execuï¿½ï¿½o da thread
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

                                
                             
                                    
				// verifica se ï¿½ uma linha vï¿½lida. Pode ser que a conexï¿½o
				// foi interrompida. Neste caso, a linha ï¿½ null. Se isso
				// ocorrer, termina-se a execuï¿½ï¿½o saindo com break
				if (linha == null) {
					System.out.println("Conexï¿½o encerrada!");
					break;
				}
				// caso a linha nï¿½o seja nula, deve-se imprimi-la				
			}
		} catch (IOException e) {
			// caso ocorra alguma exceï¿½ï¿½o de E/S, mostre qual foi.
			System.out.println("IOException: " + e);
		}
		// sinaliza para o main que a conexï¿½o encerrou.
		done = true;
	}
}