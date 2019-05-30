'''Implementar os algoritmos de escalonamento:
1.	FCFS
2.	SJF
3.	SRTF
4.	Round Robin
5.	Multinível
a.	Primeiro nível RR
b.	Segundo nível FCFS
Para todos os algoritmos, deverão constar um menu para as seguintes entradas.
•	Quantidade de processos para simular
•	Tamanho do Burst do processo (manual ou arquivo)
•	Tempo de chegada, quando necessário
•	Prioridade, quando necessário
•	Quantum de tempo, quando necessário
Resultados
•	Lista dos processos com os seus burst, prioridades e tempo de chegada
•	Exibir o Waiting Time e Turnaround Time de cada processo no algoritmo
•	Exibir o Waiting Time médio
•	Exibir o Turnaround Time médio
Padrão Arquivo de Entrada de Dados
@nomeprocesso;burst;tempochegada;prioridade;quantum&
Arquivo para o FCFS
@p1;24;0;0;0&
@p2;3;0;0;0&
Arquivo para o STRF
@p1;8;2;0;0&
@p2;4;4;0;0&
Arquivo para o RR e multinível
@p1;80;0;0;10&
@p1;60;0;0;10&'''

    
import random
import operator

class Processo(object):
  def __init__(self, nome,burst,tcheg, taround,tresposta,prioridade,quantum):
      self.nome = nome
      self.burst = burst
      self.tcheg = tcheg
      self.taround = taround
      self.tresposta = tresposta
      self.prioridade = prioridade
      self.quantum = quantum
  
  def setNome(nome):
    self.nome = nome

  def setBurst(burst):
    self.burst = burst
  
  def setTcheg(tcheg):
    self.tcheg = tcheg

  def setTcheg(taround):
    self.taround = taround
  def setTresposta(tresposta):
    self.tresposta = tresposta
  def setPrioridad(prioridade):
    self.prioridade = prioridade
  def setQuantum(quantum):
    self.quantum = quantum

def FCFS(): 
  c = input("Burst manual ou arquivo(M/A)?\n ")  

  if c == "A" or c == "a":
    #burst e tempo de chegada automático
    '''arquivo = "fcfs.txt"
    arquivo = open(arquivo)
    arquivo = arquivo.read()
    arquivo = arquivo.split()'''
    arquivo = ['@p1;15;0&','@p2;3;0&','@p3;4;0&','@p4;8;2&']
    print(arquivo)
    lista = []

    for x in range(len(arquivo)):
      prepara = arquivo[x]
      prepara = prepara.split('@')
      separa = prepara[1]
      separa = separa.split('&')
      gol = separa[0]
      gol = gol.split(';')
      processo = gol[0]
      processo = int(processo[1:])
      burst = int(gol[1])
      tchegada = int(gol[2])

      if x == 0:
        tresposta = tchegada
        taround = burst
        tresposta = tchegada
      else:
        tresposta = taround - tchegada
        taround += burst-tchegada
        
      proc = Processo(x,burst,tchegada,taround,tresposta)
      lista.append(proc)
      
      
      
  else:
    lista = []
    n = int(input("Qual a quantidade de processos?\n"))
    #burst e tempo de chegada manual
    for i in range(n):
        b = int(input("P" + str(i+1) +" Burst:"))
        t = int(input("P" + str(i+1) +" Tempo de Chegada:")) 
        if i == 0:
          tresposta = t
          taround = b
          tresposta = t
        else:
          tresposta = taround - t
          taround += b-t
          

        proc = Processo(i,b,t,taround,tresposta)
        lista.append(proc)
  
def SJF(): #@nomeprocesso;burst;tempochegada;prioridade;quantum& 
  c = input("Burst manual ou arquivo(M/A)?\n ")  

  if c == "A" or c == "a":
    #burst e tempo de chegada automático
    '''arquivo = "fcfs.txt"
    arquivo = open(arquivo)
    arquivo = arquivo.read()
    arquivo = arquivo.split()'''
    arquivo = ['@p1;15;0&','@p2;3;0&','@p3;4;0&','@p4;8;2&']
    print(arquivo)
    lista = []

    for x in range(len(arquivo)):
      prepara = arquivo[x]
      prepara = prepara.split('@')
      separa = prepara[1]
      separa = separa.split('&')
      gol = separa[0]
      gol = gol.split(';')
      processo = gol[0]
      processo = int(processo[1:])
      burst = int(gol[1])
      tchegada = int(gol[2])

      if x == 0:
        tresposta = tchegada
        taround = burst
        tresposta = tchegada
      else:
        tresposta = taround - tchegada
        taround += burst-tchegada
        
      proc = Processo(x,burst,tchegada,taround,tresposta)
      lista.append(proc)

    lista.sort(key = operator.attrgetter("burst"), reverse = False)
    lista.sort(key = operator.attrgetter("tcheg"), reverse = False)
      
      
      
  else:
    lista = []
    n = int(input("Qual a quantidade de processos?\n"))
    #burst e tempo de chegada manual
    for i in range(n):
        b = int(input("P" + str(i+1) +" Burst:"))
        t = int(input("P" + str(i+1) +" Tempo de Chegada:")) 
        if i == 0:
          tresposta = t
          taround = b
          tresposta = t
        else:
          tresposta = taround - t
          taround += b-t
          

        proc = Processo(i,b,t,taround,tresposta)
        lista.append(proc)  
  
    lista.sort(key = operator.attrgetter("burst"), reverse = False)
    lista.sort(key = operator.attrgetter("tcheg"), reverse = False)    


  print ("Todos os processos...")
  print ("Processo","Tempo de Burst","Tempo de chegada","TurnAround","Tempo de resposta","Tempo de Espera")
  contador = 0
  for i in lista:
    if contador == 0:
      print("\t",i.nome,"\t\t\t",i.burst,"\t\t",i.tcheg,"\t\t\t",i.taround,"\t\t\t",i.tresposta,"\t\t\t\t",i.tresposta)
    else:
      print("\t",i.nome,"\t\t\t",i.burst,"\t\t\t",i.tcheg,"\t\t\t",i.taround,"\t\t\t",i.tresposta,"\t\t\t",i.tresposta)
    contador +=1


def preparaProcessos(arquivo):
    #burst e tempo de chegada automático
    
    print(arquivo)
    lista = []

    for x in range(len(arquivo)):
      prepara = arquivo[x]
      prepara = prepara.split('@')
      separa = prepara[1]
      separa = separa.split('&')
      gol = separa[0]
      gol = gol.split(';')
      processo = gol[0]
      processo = int(processo[1:])
      burst = int(gol[1])
      tchegada = int(gol[2])
      quantum = int(gol[3])
      prioridade = int(gol[4])

      if x == 0:
        tresposta = tchegada
        taround = burst
        tresposta = tchegada
      else:
        tresposta = taround - tchegada
        taround += burst-tchegada
        
      proc = Processo(x,burst,tchegada,taround,tresposta,quantum,prioridade)
      lista.append(proc)
    return lista
    

def RR():
    c = input("Burst manual ou arquivo(M/A)?\n ")
    if c == "A" or c == "a":
        arquivo = ['@p1;19;0;7;0&','@p3;23;0;7;0&','@p3;31;0;7;0&','@p4;11;2;7;0&']
        lista = []
        lista = preparaProcessos(arquivo)
        contador = 1
        podePara = 0
        vetorRR = []
        while True:
            podePara = 0
            
            for x in lista:
                while x.burst >= 0:
                    if contador <= 7:
         #               print(f'Antes -> Processo {x.nome} burst {x.burst}')
                        x.burst -= 1
                        contador += 1
                        
        #                print(f'Depois -> Processo {x.nome} burst {x.burst}')
                    else :
                        vetorRR.append((str(x.nome)+';'+str(x.burst)))
                        contador = 1
                        break
            
            for x in lista:
                if x.burst > 0:
                    podePara = 0
                else:
                    podePara += 1
            if podePara == 4:
                break
        print('OI \n')
    print(vetorRR)
        
        


    

def SRTF():
  c = input("Burst manual ou arquivo(M/A)?\n ")
  tempoPassado = 0
  atual = 0
  lista = []
  
 
  def vetorTempo(processoQueEstaAgora,tempopassado):
    for x in range(len(lista)): #pega o processo que esta
        if lista[x].nome == processoQueEstaAgora:
            atual = lista[x].nome
            break
    print(f'P{lista[atual].nome};{lista[atual].burst};{lista[atual].tcheg}')

    
    while lista[atual].burst != 0:
        for x in range(len(lista)):
            if lista[x].nome != lista[atual].nome: #nao pegar o mesmo processo
                pass
            break
        break
                    
    
  

  
  if c == "A" or c == "a":
    '''arquivo = "fcfs.txt"
    arquivo = open(arquivo)
    arquivo = arquivo.read()
    arquivo = arquivo.split()'''
    arquivo = ['@p0;8;0&','@p1;2;2&','@p2;3;5&','@p3;2;7&']
    lista = preparaProcessos(arquivo) 
    
    lista.sort(key = operator.attrgetter("tcheg"), reverse = False)
    for x in lista:
        vetorTempo(x.nome,0)
        break
    
    
      
      
      
  else:
    lista = []
    n = int(input("Qual a quantidade de processos?\n"))
    #burst e tempo de chegada manual
    for i in range(n):
        b = int(input("P" + str(i+1) +" Burst:"))
        t = int(input("P" + str(i+1) +" Tempo de Chegada:")) 
        if i == 0:
          tresposta = t
          taround = b
          tresposta = t
        else:
          tresposta = taround - t
          taround += b-t
          

        proc = Processo(i,b,t,taround,tresposta)
        lista.append(proc)  
  
    lista.sort(key = operator.attrgetter("burst"), reverse = False)
    lista.sort(key = operator.attrgetter("tcheg"), reverse = False)    


  print ("Todos os processos...")
  print ("Processo","Tempo de Burst","Tempo de chegada","TurnAround","Tempo de resposta","Tempo de Espera")
  contador = 0
  for i in lista:
    if contador == 0:
      print("\t",i.nome,"\t\t\t",i.burst,"\t\t",i.tcheg,"\t\t\t",i.taround,"\t\t\t",i.tresposta,"\t\t\t\t",i.tresposta)
    else:
      print("\t",i.nome,"\t\t\t",i.burst,"\t\t\t",i.tcheg,"\t\t\t",i.taround,"\t\t\t",i.tresposta,"\t\t\t",i.tresposta)
    contador +=1
    
while True:
  print('--------------------------------------------------------------------------------------\n')
  op = input("Para sair digite 'sair':\n")
  if op.lower() == 'sair':
    break
  else:
    esc = int(input('Digite o tipo de escalonamento a ser executado: \n 1 -> FCFS\n 2 -> SJF \n 3 -> SRTF\n 4 -> Round Robin\n MULTI NÍVEL \n 5 -> RR \n 6 -> Segundo nível FCFS\n'))
    if esc == 1: #FCFS
      FCFS()
      print('\t\tFIM')
      print('---------------------------------------------------------')
    elif esc == 2: #SJF
      SJF()
      print('\t\tFIM')
      print('--------------------------------------')

     
    elif esc == 3:
      SRTF()
      print('\t\tFIM')
      print('--------------------------------------')
    elif esc == 4:
      pass
    elif esc == 5:
      RR()
      
    elif esc == 6:
      print('segundo fcfs')
