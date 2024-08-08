package com.udemy;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
	
	/*
	 * Job: Um job no Spring Batch é uma tarefa que realiza uma determinada função. Pode ser composto de um ou mais steps.
	 * JobRepository: Responsável por salvar os metadados do Spring Batch, como o status do job, histórico de execuções, etc.
	 * Step:  Uma etapa do job, que define uma unidade de trabalho
	 */
	@Bean
	public Job imprimeOlaJob(JobRepository jobRepository, Step step) {
		return new JobBuilder("imprimeOla", jobRepository)
				.start(step)
				.incrementer(new RunIdIncrementer())
				.build();
	}
	
	/*
	 * PlatformTransactionManager: Gerenciador de transações que garante que as operações dentro do step sejam tratadas como transações.
	 * 
	 * Tasklet: Uma Tasklet no Spring Batch é uma interface que define uma unidade de trabalho a ser executada como parte de um step. 
	 * É uma abordagem simples para implementar uma lógica de processamento única dentro de um step, 
	 * em oposição ao processamento baseado em chunks, que é mais adequado para processar grandes volumes de dados em pedaços menores.
	 * 
	 * StepContribution: Representa a contribuição do step atual para a execução geral do job. Permite atualizar o status do step e outras 
	 * informações relacionadas à execução.
	 * 
	 * ChunkContext: Fornece o contexto de execução do chunk, incluindo informações como parâmetros do job, contexto do step, etc.
	 * 
	 * RepeatStatus: Indica se o tasklet terminou sua execução ou se deve ser repetido. 
	 * Pode retornar RepeatStatus.FINISHED para indicar que o tasklet completou sua tarefa ou 
	 * RepeatStatus.CONTINUABLE se precisar continuar a execução.
	 */ 
	@Bean
	public Step imprimeOlaStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("imprimeOlaStep", jobRepository)
				.tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
					String nome = (String) chunkContext.getStepContext().getJobParameters().get("nome");
					Long runId = (Long) chunkContext.getStepContext().getJobParameters().get("run.id");
	                
	                System.out.println("Run ID: " + runId);  // Log para verificar o run.id
					System.out.println("Olá " + nome);
					return RepeatStatus.FINISHED;
				}, transactionManager).build();
				
		
	}
	
}
