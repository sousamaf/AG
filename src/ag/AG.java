package ag;
import java.util.Random;

public class AG
{
	int QTD_GENE = 8; // Quantidade de genes do indivíduo
	int TAM_POP = 10; // Tamanho da população.
	int pop_aux_posicao = 0; // Representa o preenchimento da população auxiliar.
	
	// Vetor da população
	int[][] iPOPULACAO = new int[TAM_POP][QTD_GENE];
	// Vetor da população auxiliar.
	int[][] iPOP_AUX = new int[TAM_POP][QTD_GENE];
	
	float[] PESOS = new float[QTD_GENE];
	
	// Vetor de avaliacao
	float[][] avaliacao = new float[TAM_POP][2]; 
	
	public void buscar()
	{
		int T = 50;
		int geracao, individuo;
		
		this.pop_inicial();
		for(geracao = 0; geracao < T; geracao++)
		{
			System.out.println("\nGeração: " + geracao);
			this.avaliacao();
			if(this.criterioDeParada(2.0f))
			{
				System.out.println("\nCritério de parada fitness máximo.");
				break;
			}
			
			for(individuo = 0; individuo < TAM_POP/2; individuo++)
			{
				//System.out.print("\nPares de Indivíduos: " + individuo);
				//this.cruzamento_simples();
				this.cruzamento_uniforme();
			}
			System.out.print("\nMutação.");
			this.mutacao();
			
			System.out.print("\nSubstituição.");
			System.out.println("");
			this.substituicao();
			pop_aux_posicao = 0;
		}
	}
	
	public boolean criterioDeParada(float carga)
	{
		int i = 0;
		for(i = 0; i < TAM_POP; i++)
		{
			if(avaliacao[i][0] == carga)
			{
				return true;
			}
		}
		return false;
	}
	
	public void avaliacao()
	{
		int i, j;
		float carga = 2.0f;
		float fitness = 0.0f;
		int theOne = 0;
		float theBest = 0.0f;
		float somaFitness = 0.0f;
		
		for(i = 0; i < TAM_POP; i++)
		{
			for(j = 0; j < QTD_GENE; j++)
			{
				fitness += (iPOPULACAO[i][j] * PESOS[j]);
			}
			if(fitness > carga)
				fitness = 0.0f;
			
			avaliacao[i][0] = fitness;
			
			
			if(theBest < fitness)
			{
				theBest = fitness;
				theOne = i;
			}
			
			fitness = 0.0f;
		}
		// Calcula o fitness global/total
		for(i = 0; i < TAM_POP; i++)
		{
			somaFitness += avaliacao[i][0];
		}
		// calcula a porcentagem para a roleta.
		for(i = 0; i < TAM_POP; i++)
		{
			avaliacao[i][1] = (avaliacao[i][0] * 100) / somaFitness;
		}
		
		System.out.print("\nMelhor indivíduo: ");
		for(i = 0; i < QTD_GENE; i++)
		{
			System.out.print(" " + iPOPULACAO[theOne][i]);
		}
		System.out.print(" Fitness: " + theBest);
		System.out.print("\nPesos: ");
		for(i = 0; i < QTD_GENE; i++)
		{
			System.out.print(" " + PESOS[i]);
		}
	}
	
	public int[] roleta()
	{
		int[] pais = new int[2];
		int num_sorteado, i;
		float soma_porcentagem = 0.0f;
		Random r = new Random();
		num_sorteado = r.nextInt(101);
		int contPai2 = 0;
		//System.out.println("Roleta: Pai1");
		for(i = 0; i < TAM_POP; i++)
		{
			soma_porcentagem += avaliacao[i][1];
			if(num_sorteado < soma_porcentagem)
			{
				pais[0] = i;
				break;
			}
		}
		soma_porcentagem = 0.0f;
		
		//System.out.println("Roleta: Pai2");
		do{
			//System.out.print(".");
			num_sorteado = r.nextInt(101);
			for(i = 0; i < TAM_POP; i++)
			{
				soma_porcentagem += avaliacao[i][1];
				if(num_sorteado < soma_porcentagem)
				{
					pais[1] = i;
					break;
				}
			}
			contPai2++;
			//System.out.println(contPai2);
		}while(pais[0] == pais[1] && contPai2 < 10);
		
		if(pais[0] == pais[1])
		{
			System.out.println("Roleta: selecao simples.");
			pais[1] = this.selecao()[1];
		}
		
		//System.out.println("");		
		return pais;
	}
	
	
	public void pop_inicial()
	{
		int i, j;
		Random r = new Random();
		for(i = 0; i < TAM_POP; i++)
		{
			for(j = 0; j < QTD_GENE; j++)
			{
				iPOPULACAO[i][j] = r.nextInt(2);
			}
		}
	}
	
	public int[] selecao()
	{
		int[] pais = new int[2];
		
		pais = this.roleta();
		return pais;
	}
	
	public int[] selecao_simples()
	{
		int iPai1, iPai2;
		Random r = new Random();
		int[] pais = new int[2];
		iPai1 = r.nextInt(TAM_POP);
		iPai2 = r.nextInt(TAM_POP);
		
		while(iPai1 == iPai2)
		{
			iPai2 = r.nextInt(TAM_POP);
		}
		pais[0] = iPai1;
		pais[1] = iPai2;
		return pais;
	}
	public void cruzamento_simples()
	{
		// @TODO Tratar número de genes ímpares.
		int i, meio;
		int[] pais = new int[2];
		pais = this.selecao();
		
		meio = QTD_GENE / 2;
		for(i = 0; i < QTD_GENE; i++)
		{
			if(i <= meio)
			{
				iPOP_AUX[pop_aux_posicao][i] = iPOPULACAO[pais[0]][i];
				iPOP_AUX[pop_aux_posicao+1][i] = iPOPULACAO[pais[1]][i];
			} else
			{
				iPOP_AUX[pop_aux_posicao][i] = iPOPULACAO[pais[1]][i];
				iPOP_AUX[pop_aux_posicao+1][i] = iPOPULACAO[pais[0]][i];
			}
		}
		pop_aux_posicao += 2;
	}
	
	public void cruzamento_uniforme()
	{
		int i;
		int[] pais = new int[2];
		Random r = new Random();
		pais = this.selecao();

		for(i = 0; i < QTD_GENE; i++)
		{
			if(r.nextInt(2) == 0)
			{
				iPOP_AUX[pop_aux_posicao][i] = iPOPULACAO[pais[0]][i];
				iPOP_AUX[pop_aux_posicao+1][i] = iPOPULACAO[pais[1]][i];
			} else
			{
				iPOP_AUX[pop_aux_posicao][i] = iPOPULACAO[pais[1]][i];
				iPOP_AUX[pop_aux_posicao+1][i] = iPOPULACAO[pais[0]][i];
			}
		}
		pop_aux_posicao += 2;

	}
	
	public void mutacao()
	{
		Random r = new Random();
		int gene = 0;
		int[] pais = new int[2];
		pais = this.selecao();

		gene = r.nextInt(QTD_GENE);
		
		if(iPOP_AUX[pais[0]][gene] == 0)
			iPOP_AUX[pais[0]][gene] = 1;
		else
			iPOP_AUX[pais[0]][gene] = 0;
	}
	
	public void substituicao()
	{
		int i, j;
		for(i = 0; i < TAM_POP; i++)
		{
			for(j = 0; j < QTD_GENE; j++)
			{
				iPOPULACAO[i][j] = iPOP_AUX[i][j];
			}
		}
	}
	
	public AG()
	{
		PESOS[0] = (float) 0.50;
		PESOS[1] = (float) 0.75;
		PESOS[2] = (float) 0.20;
		PESOS[3] = (float) 0.90;
		PESOS[4] = (float) 1.0;
		PESOS[5] = (float) 1.2;
		PESOS[6] = (float) 0.1;
		PESOS[7] = (float) 1.0;
	}
}
