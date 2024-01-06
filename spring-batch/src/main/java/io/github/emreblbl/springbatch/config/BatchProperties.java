package io.github.emreblbl.springbatch.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix ="batch" )
@Getter
@Setter
public class BatchProperties {

    private String inputFile;
    private String americansFile;
}
