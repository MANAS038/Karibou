package karibou.recon;

import karibou.recon.config.EngineConfig;
import karibou.recon.repository.*;
import karibou.recon.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot application for Karibou Express Cash Reconciliation Engine.
 */
@SpringBootApplication
public class KaribouReconciliationApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaribouReconciliationApplication.class, args);
    }

    // Repository beans (in-memory for now)
    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository();
    }

    @Bean
    public DriverCollectionRepository driverCollectionRepository() {
        return new DriverCollectionRepository();
    }

    @Bean
    public AgentDepositRepository agentDepositRepository() {
        return new AgentDepositRepository();
    }

    @Bean
    public ReconciliationResultRepository reconciliationResultRepository() {
        return new ReconciliationResultRepository();
    }

    @Bean
    public DiscrepancyRepository discrepancyRepository() {
        return new DiscrepancyRepository();
    }

    @Bean
    public FraudPatternRepository fraudPatternRepository() {
        return new FraudPatternRepository();
    }

    @Bean
    public ReconciliationRunRepository reconciliationRunRepository() {
        return new ReconciliationRunRepository();
    }

    // Configuration bean
    @Bean
    public EngineConfig engineConfig() {
        return new EngineConfig();
    }

    // Service beans
    @Bean
    public IngestionService ingestionService(OrderRepository orderRepository,
                                             DriverCollectionRepository collectionRepository,
                                             AgentDepositRepository depositRepository) {
        return new IngestionService(orderRepository, collectionRepository, depositRepository);
    }

    @Bean
    public MatchingEngine matchingEngine(OrderRepository orderRepository,
                                        DriverCollectionRepository collectionRepository,
                                        AgentDepositRepository depositRepository,
                                        ReconciliationResultRepository resultRepository,
                                        DiscrepancyRepository discrepancyRepository,
                                        EngineConfig engineConfig) {
        return new MatchingEngine(orderRepository, collectionRepository, depositRepository, 
                                 resultRepository, discrepancyRepository, engineConfig);
    }

    @Bean
    public RunService runService(ReconciliationRunRepository runRepository,
                                 MatchingEngine matchingEngine,
                                 EngineConfig engineConfig) {
        return new RunService(runRepository, matchingEngine, engineConfig);
    }
}
