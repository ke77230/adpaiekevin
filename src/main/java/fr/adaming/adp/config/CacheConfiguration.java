package fr.adaming.adp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, fr.adaming.adp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, fr.adaming.adp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, fr.adaming.adp.domain.User.class.getName());
            createCache(cm, fr.adaming.adp.domain.Authority.class.getName());
            createCache(cm, fr.adaming.adp.domain.User.class.getName() + ".authorities");
            createCache(cm, fr.adaming.adp.domain.Location.class.getName());
            createCache(cm, fr.adaming.adp.domain.Location.class.getName() + ".employees");
            createCache(cm, fr.adaming.adp.domain.Location.class.getName() + ".employeurs");
            createCache(cm, fr.adaming.adp.domain.Employee.class.getName());
            createCache(cm, fr.adaming.adp.domain.Employee.class.getName() + ".jobs");
            createCache(cm, fr.adaming.adp.domain.Employee.class.getName() + ".locations");
            createCache(cm, fr.adaming.adp.domain.Employeur.class.getName());
            createCache(cm, fr.adaming.adp.domain.Employeur.class.getName() + ".conventionCollectives");
            createCache(cm, fr.adaming.adp.domain.Employeur.class.getName() + ".locations");
            createCache(cm, fr.adaming.adp.domain.Job.class.getName());
            createCache(cm, fr.adaming.adp.domain.Job.class.getName() + ".employees");
            createCache(cm, fr.adaming.adp.domain.TauxDImposition.class.getName());
            createCache(cm, fr.adaming.adp.domain.Mention.class.getName());
            createCache(cm, fr.adaming.adp.domain.Mention.class.getName() + ".ficheDePaies");
            createCache(cm, fr.adaming.adp.domain.Conge.class.getName());
            createCache(cm, fr.adaming.adp.domain.Bonus.class.getName());
            createCache(cm, fr.adaming.adp.domain.Contrat.class.getName());
            createCache(cm, fr.adaming.adp.domain.FicheDePaie.class.getName());
            createCache(cm, fr.adaming.adp.domain.FicheDePaie.class.getName() + ".cotisations");
            createCache(cm, fr.adaming.adp.domain.FicheDePaie.class.getName() + ".mentions");
            createCache(cm, fr.adaming.adp.domain.ConventionCollective.class.getName());
            createCache(cm, fr.adaming.adp.domain.ConventionCollective.class.getName() + ".employeurs");
            createCache(cm, fr.adaming.adp.domain.Cotisation.class.getName());
            createCache(cm, fr.adaming.adp.domain.Cotisation.class.getName() + ".ficheDePaies");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
