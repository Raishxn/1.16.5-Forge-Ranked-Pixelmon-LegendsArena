package com.raishxn.legendsarena.config;

import com.raishxn.legendsarena.legendsarena;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private final Path configDir;

    // Instâncias do Yaml para cada POJO
    private Yaml yamlCore, yamlElos, yamlTiers, yamlMessages, yamlSeasons;
    private Yaml yamlCommands, yamlPermissions, yamlRewards, yamlStats;
    private Yaml yamlAntiSmurf, yamlBattleRules; // NOVO

    // POJOs de config carregados
    private CoreConfig coreConfig;
    private ElosConfig elosConfig;
    private TiersConfig tiersConfig;
    private MessagesConfig messagesConfig;
    private SeasonsConfig seasonsConfig;
    private CommandsConfig commandsConfig;
    private PermissionsConfig permissionsConfig;
    private RewardsConfig rewardsConfig;
    private StatsConfig statsConfig;
    private AntiSmurfConfig antiSmurfConfig; // NOVO
    private BattleRulesConfig battleRulesConfig; // NOVO

    public ConfigManager(Path configDir) {
        this.configDir = configDir;
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                legendsarena.LOGGER.error("Falha ao criar diretório de configuração!", e);
            }
        }

        LoaderOptions loaderOptions = new LoaderOptions();

        // Inicializa um Yaml para cada classe de config
        this.yamlCore = new Yaml(new Constructor(CoreConfig.class, loaderOptions));
        this.yamlElos = new Yaml(new Constructor(ElosConfig.class, loaderOptions));
        this.yamlTiers = new Yaml(new Constructor(TiersConfig.class, loaderOptions));
        this.yamlMessages = new Yaml(new Constructor(MessagesConfig.class, loaderOptions));
        this.yamlSeasons = new Yaml(new Constructor(SeasonsConfig.class, loaderOptions));
        this.yamlCommands = new Yaml(new Constructor(CommandsConfig.class, loaderOptions));
        this.yamlPermissions = new Yaml(new Constructor(PermissionsConfig.class, loaderOptions));
        this.yamlRewards = new Yaml(new Constructor(RewardsConfig.class, loaderOptions));
        this.yamlStats = new Yaml(new Constructor(StatsConfig.class, loaderOptions));
        this.yamlAntiSmurf = new Yaml(new Constructor(AntiSmurfConfig.class, loaderOptions)); // NOVO
        this.yamlBattleRules = new Yaml(new Constructor(BattleRulesConfig.class, loaderOptions)); // NOVO
    }

    public void loadConfigs() {
        legendsarena.LOGGER.info("Carregando todos arquivos de configuração...");

        // Usa o método genérico para carregar cada arquivo
        this.coreConfig = loadConfig("config.yml", CoreConfig.class, this.yamlCore);
        this.elosConfig = loadConfig("elos.yml", ElosConfig.class, this.yamlElos);
        this.tiersConfig = loadConfig("tiers.yml", TiersConfig.class, this.yamlTiers);
        this.messagesConfig = loadConfig("messages.yml", MessagesConfig.class, this.yamlMessages);
        this.seasonsConfig = loadConfig("seasons.yml", SeasonsConfig.class, this.yamlSeasons);
        this.commandsConfig = loadConfig("commands.yml", CommandsConfig.class, this.yamlCommands);
        this.permissionsConfig = loadConfig("permissions.yml", PermissionsConfig.class, this.yamlPermissions);
        this.rewardsConfig = loadConfig("rewards.yml", RewardsConfig.class, this.yamlRewards);
        this.statsConfig = loadConfig("stats.yml", StatsConfig.class, this.yamlStats);

        // --- NOVOS ARQUIVOS ---
        this.antiSmurfConfig = loadConfig("anti_smurf.yml", AntiSmurfConfig.class, this.yamlAntiSmurf);
        this.battleRulesConfig = loadConfig("battle_rules.yml", BattleRulesConfig.class, this.yamlBattleRules);

        // Carregar bans (que são uma pasta)
        loadBans();

        // Carregar schemas (que são uma pasta) // NOVO
        loadSchemas();

        legendsarena.LOGGER.info("Configurações carregadas.");
    }

    // Método genérico para carregar um arquivo de config
    private <T> T loadConfig(String fileName, Class<T> clazz, Yaml yaml) {
        File configFile = configDir.resolve(fileName).toFile();

        if (!configFile.exists()) {
            legendsarena.LOGGER.warn("Arquivo " + fileName + " não encontrado. Criando novo a partir dos recursos...");
            copyDefaultFile(fileName, configFile);
        }

        // Se o arquivo ainda não existir (falha ao copiar), retorne nulo
        if (!configFile.exists()) {
            legendsarena.LOGGER.error("Não foi possível criar o arquivo " + fileName + ". Carregamento abortado para este arquivo.");
            return null;
        }

        try (InputStream in = new FileInputStream(configFile)) {
            // Se o arquivo estiver vazio, o loadAs pode retornar null, o que é ok
            return yaml.loadAs(in, clazz);
        } catch (Exception e) {
            legendsarena.LOGGER.error("Falha ao carregar " + fileName, e);
            return null; // Retorna nulo se falhar
        }
    }

    private void loadBans() {
        // Lógica especial para carregar a pasta 'bans/'
        Path bansDir = configDir.resolve("bans");
        Path perTierDir = bansDir.resolve("per_tier");

        if (!Files.exists(bansDir)) {
            try { Files.createDirectories(bansDir); } catch (IOException e) { /* ... */ }
        }
        if (!Files.exists(perTierDir)) {
            try { Files.createDirectories(perTierDir); } catch (IOException e) { /* ... */ }
        }

        // Copia os arquivos de bans padrão
        copyDefaultFile("bans/global_bans.yml", bansDir.resolve("global_bans.yml").toFile());
        // Adicione os bans per_tier que você quer gerar por padrão
        copyDefaultFile("bans/per_tier/ou_bans.yml", perTierDir.resolve("ou_bans.yml").toFile());
        copyDefaultFile("bans/per_tier/uu_bans.yml", perTierDir.resolve("uu_bans.yml").toFile());
        copyDefaultFile("bans/per_tier/nationaldex_bans.yml", perTierDir.resolve("nationaldex_bans.yml").toFile());
    }

    // --- NOVO MÉTODO ---
    private void loadSchemas() {
        Path schemasDir = configDir.resolve("schemas");

        if (!Files.exists(schemasDir)) {
            try { Files.createDirectories(schemasDir); } catch (IOException e) {
                legendsarena.LOGGER.error("Falha ao criar diretório 'schemas'!", e);
            }
        }

        // Copia o arquivo SQL padrão
        copyDefaultFile("schemas/tables.sql", schemasDir.resolve("tables.sql").toFile());
    }

    // Método para copiar arquivos padrão
    private void copyDefaultFile(String resourcePath, File destination) {
        if(destination.exists()) return;

        // Garante que os diretórios pais existam antes de tentar copiar
        File parentDir = destination.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (InputStream in = legendsarena.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                legendsarena.LOGGER.warn("Recurso não encontrado: " + resourcePath + ". Criando arquivo vazio.");
                // Cria um arquivo vazio se o recurso não for encontrado
                destination.createNewFile();
                return;
            }
            Files.copy(in, destination.toPath());
        } catch (IOException e) {
            legendsarena.LOGGER.error("Falha ao copiar config padrão: " + resourcePath, e);
        } catch (Exception e) {
            legendsarena.LOGGER.error("Erro inesperado ao copiar " + resourcePath, e);
        }
    }

    // --- Getters para todas as configs ---
    public CoreConfig getCoreConfig() { return this.coreConfig; }
    public ElosConfig getElosConfig() { return this.elosConfig; }
    public TiersConfig getTiersConfig() { return this.tiersConfig; }
    public MessagesConfig getMessagesConfig() { return this.messagesConfig; }
    public SeasonsConfig getSeasonsConfig() { return this.seasonsConfig; }
    public CommandsConfig getCommandsConfig() { return this.commandsConfig; }
    public PermissionsConfig getPermissionsConfig() { return this.permissionsConfig; }
    public RewardsConfig getRewardsConfig() { return this.rewardsConfig; }
    public StatsConfig getStatsConfig() { return this.statsConfig; }
    public AntiSmurfConfig getAntiSmurfConfig() { return this.antiSmurfConfig; }
    public BattleRulesConfig getBattleRulesConfig() { return this.battleRulesConfig; }
}