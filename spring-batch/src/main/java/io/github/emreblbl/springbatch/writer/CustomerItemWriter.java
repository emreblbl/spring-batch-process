package io.github.emreblbl.springbatch.writer;

import io.github.emreblbl.springbatch.config.BatchProperties;
import io.github.emreblbl.springbatch.model.Customer;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
@Service
public class CustomerItemWriter extends FlatFileItemWriter<Customer> {
    // declared constants to better organize the code
    private static final String DEFAULT_DELIMITER = ";";
    private static final String DEFAULT_LINE_BREAK = "\n";
    private static final String ID_HEADER = "Id";
    private static final String FIRST_NAME_HEADER = "first_name";
    private static final String LAST_NAME_HEADER = "last_name";
    private static final String GENDER_HEADER = "gender";
    private static final String BIRTHDAY_HEADER = "birthday";
    private static final String ADDRESS_HEADER = "address";


    private final BatchProperties properties;

    public CustomerItemWriter(final BatchProperties properties) {
        super();
        this.properties = properties;


        // defining of line aggregator and fieldExtractor
        final DelimitedLineAggregator<Customer> lineAggregator = new DelimitedLineAggregator<Customer>();
        final BeanWrapperFieldExtractor<Customer> fieldExtractor = new BeanWrapperFieldExtractor<Customer>();

        lineAggregator.setDelimiter(DEFAULT_DELIMITER);
        lineAggregator.setFieldExtractor(fieldExtractor);

        // optional part: header formation code
        this.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(final Writer writer) throws IOException {
                // append all header descriptions
                final String header = new StringBuilder()
                        .append(ID_HEADER)
                        .append(DEFAULT_DELIMITER)
                        .append(FIRST_NAME_HEADER)
                        .append(DEFAULT_DELIMITER)
                        .append(LAST_NAME_HEADER)
                        .append(DEFAULT_DELIMITER)
                        .append(BIRTHDAY_HEADER)
                        .append(DEFAULT_DELIMITER)
                        .append(GENDER_HEADER)
                        .append(DEFAULT_DELIMITER)
                        .append(ADDRESS_HEADER)
                        .toString();

                writer.write(header);
            }
        });

        this.setResource(new FileSystemResource(properties.getAmericansFile()));
        this.setLineAggregator(lineAggregator);
    }

    @Override
    public String doWrite( final Chunk<? extends Customer> items) {

        final StringBuilder linesToWrite = new StringBuilder();

        items.forEach(customer -> {
            // creates line with Customer properties
            final String line = new StringBuilder()
                    .append(customer.getId())
                    .append(DEFAULT_DELIMITER)
                    .append(customer.getFirstName())
                    .append(DEFAULT_DELIMITER)
                    .append(customer.getLastName())
                    .append(DEFAULT_DELIMITER)
                    .append(customer.getBirthday())
                    .append(DEFAULT_DELIMITER)
                    .append(customer.getGender().getValue())
                    .append(DEFAULT_DELIMITER)
                    .append(customer.getAddress().getStreet())
                    .append(DEFAULT_DELIMITER)
                    .append(customer.getAddress().getCountry())
                    .toString();

            // appends line to StringBuilder
            linesToWrite.append(line).append(DEFAULT_LINE_BREAK);
        });

        // returns lines to write (limited to chunk size)
        return linesToWrite.toString();
    }
}
