package io.github.emreblbl.springbatch.reader;

import io.github.emreblbl.springbatch.config.BatchProperties;
import io.github.emreblbl.springbatch.model.CustomerInput;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;


@Service
public class CustomerItemReader extends FlatFileItemReader<CustomerInput> {
    private static final String DEFAULT_DELIMITER = ";";

    private static final String ID_HEADER = "Id";
    private static final String FIRST_NAME_HEADER = "first name";
    private static final String LAST_NAME_HEADER = "last name";
    private static final String GENDER_HEADER = "gender";
    private static final String BIRTHDAY_HEADER = "birthday";
    private static final String ADDRESS_HEADER = "address";
    private static final String COUNTRY_HEADER = "country";
    private static final int LINES_TO_SKIP = 1;
    private final BatchProperties properties;

    public CustomerItemReader(BatchProperties batchProperties) {
        super();

        this.properties = batchProperties;;

        final DefaultLineMapper lineMapper = new DefaultLineMapper();
        final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setNames(ID_HEADER, FIRST_NAME_HEADER, LAST_NAME_HEADER, GENDER_HEADER, BIRTHDAY_HEADER, ADDRESS_HEADER, COUNTRY_HEADER);
        tokenizer.setDelimiter(DEFAULT_DELIMITER);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FieldSetMapper() {
            @Override
            public Object mapFieldSet(FieldSet fieldSet) throws BindException {
                CustomerInput input = new CustomerInput();

                input.setId(fieldSet.readString(ID_HEADER));
                input.setFirstName(fieldSet.readString(FIRST_NAME_HEADER));
                input.setLastName(fieldSet.readString(LAST_NAME_HEADER));
                input.setGender(fieldSet.readString(GENDER_HEADER));
                input.setBirthday(fieldSet.readString(BIRTHDAY_HEADER));
                input.setAddress(fieldSet.readString(ADDRESS_HEADER));
                input.setCountry(fieldSet.readString(COUNTRY_HEADER));

                return input;
            }
        });

        this.setResource(new ClassPathResource(properties.getInputFile()));
        this.setLineMapper(lineMapper);
        this.setLinesToSkip(1);
    }
}
